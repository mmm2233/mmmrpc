package com.mmm.myclassloader;

import java.io.*;

public class OpsClassLoader extends ClassLoader {
    private String rootDirPath;
    public OpsClassLoader(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    private byte[] getClassDePass(String className) throws IOException {
        String classpath = rootDirPath + className;

        FileInputStream fis = new FileInputStream(classpath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bufferSize = 1024;
        int n = 0;
        byte[] buffer = new byte[bufferSize];
        while ((n = fis.read(buffer)) != -1) {
            baos.write(buffer, 0, n);
        }
        byte[] data = baos.toByteArray();
        return data;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classDePass = getClassDePass(name);
            if (classDePass == null) {
                throw new ClassNotFoundException(name);
            }
            return defineClass(name, classDePass, 0, classDePass.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
