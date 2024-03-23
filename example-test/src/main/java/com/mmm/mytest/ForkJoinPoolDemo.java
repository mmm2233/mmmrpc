package com.mmm.mytest;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.RecursiveTask;

class ForkJoinPoolDemo {
    public static void main(String[] args) {
        ForkJoinPoolDemo forkJoinPoolDemo = new ForkJoinPoolDemo();
        //ForkJoinWorkerThread forkJoinWorkerThread = new ForkJoinWorkerThread(new ForkJoinPool());
        testFor();
        testForkJoin();
    }

    private static void testFor() {
        Instant start = Instant.now();
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <=1000*10000 ; i++) {
            list.add(i);
        }
        Instant end = Instant.now();
        System.out.println("for循环耗时：" + Duration.between(start, end).toMillis() + "ms");
    }

    //测试ForkJoin框架
    private static void testForkJoin() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Instant start = Instant.now();
        List<Integer> list = forkJoinPool.invoke(new IdByFindUpdate(1, 1000*1000));
        Instant end = Instant.now();
        System.out.println("ForkJoinPool耗时：" + Duration.between(start, end).toMillis() + "ms");
    }
}


class IdByFindUpdate extends RecursiveTask<List> {

    private Integer startId;

    private Integer endId;

    private static final Integer THRESHOLD = 10;
    public IdByFindUpdate(Integer startId, Integer endId) {
        this.startId = startId;
        this.endId = endId;
    }

    @Override
    protected List<Integer> compute() {
        int taskSize = endId - startId;
        List<Integer> list = new ArrayList<>();
        // 任务足够小，直接处理
        if (taskSize <= THRESHOLD) {
            for (int i = startId; i <= endId; i++) {
                // 模拟从数据库中查询数据
                list.add(i);
            }
            return list;
        }

        // 任务足够大，拆分任务
        IdByFindUpdate leftTask = new IdByFindUpdate(startId, (startId + endId) / 2);
        leftTask.fork();
        IdByFindUpdate rightTask = new IdByFindUpdate((startId + endId) / 2 + 1, endId);
        rightTask.fork();

        //任务合并
        list.addAll(leftTask.join());
        list.addAll(rightTask.join());
        return list;
    }
}