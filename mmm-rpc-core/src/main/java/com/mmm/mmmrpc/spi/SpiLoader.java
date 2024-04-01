package com.mmm.mmmrpc.spi;

/*
 * SPI加载器
 */

import cn.hutool.core.io.resource.ResourceUtil;
import com.mmm.mmmrpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.ServiceLoader.load;

@Slf4j
public class SpiLoader {

    /*
     * 存储已加载的类
     */
    private  static Map<String,Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    /*
        对象实例缓存
     */
    private  static Map<String,Object> instanceCache =  new ConcurrentHashMap<>();

    /*
     * 系统SPI目录
     */
    private  static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /*
       用户SPI目录
     */
    private  static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /*
        扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /*
     * 动态加载的类列表
     */
    public  static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /*
     * 加载所有类型
     */
    public static void loadALL() {
        log.info("加载所有 SPI");
        for (Class<?>  aClass : LOAD_CLASS_LIST) {
            load(aClass);
        }
    }

    /*
        获取某个接口的实例
     */
    public static <T> T getInstance(Class<T> tClass, String key) {
        String tclassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tclassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s类型", tclassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader的 %s 不存在 key=%s的类型", tclassName,key));
        }

        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();

        if (!instanceCache.containsKey(implClassName)) {
           try{
               instanceCache.put(implClassName, implClass.newInstance());
           } catch (InstantiationException | IllegalAccessException e) {
               String errorMsg = String.format("SpiLoader的 %s 实例化失败", implClassName);
               throw new RuntimeException(errorMsg,e);
           }
        }
        return (T) instanceCache.get(implClassName);
    }

    /*
     * 加载某个类型
     */
    public static Map<String, Class<?>>  load(Class<?> loadClass) {
        log.info("加载类型为{} 的 SPI", loadClass.getName());
        // 扫描路径，加载用户自定义优先系统
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        // 扫描路径，加载用户自定义优先系统
        for (String scanDir : SCAN_DIRS) {
            List<URL> reources = ResourceUtil.getResources(scanDir + loadClass.getName());
            // 遍历资源
            for (URL resource : reources) {
                try{
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // 过滤注释行
                        if (line.startsWith("#") || line.trim().length() == 0) {
                            continue;
                        }
                        String[] keyValue = line.split("=");
                        if (keyValue.length < 1) {
                            throw new RuntimeException(String.format("%s 配置文件格式错误", resource));
                        }
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();
                        keyClassMap.put(key, Class.forName(value));
                    }
                }catch (Exception e) {
                    log.error("spi 资源加载失败", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }


}
