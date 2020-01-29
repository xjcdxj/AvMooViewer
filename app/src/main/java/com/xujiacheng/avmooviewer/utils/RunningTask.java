package com.xujiacheng.avmooviewer.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunningTask {
    public static ExecutorService THREAD_POOL;


    public static void addTask(Runnable task) {
        if (THREAD_POOL == null) {
            THREAD_POOL = Executors.newCachedThreadPool();
        }
        THREAD_POOL.submit(task);
    }
}
