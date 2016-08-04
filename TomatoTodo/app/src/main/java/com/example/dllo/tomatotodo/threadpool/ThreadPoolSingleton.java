package com.example.dllo.tomatotodo.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zly on 16/8/4.
 */
public class ThreadPoolSingleton {
    public ThreadPoolExecutor threadPoolExecutor;
    private static ThreadPoolSingleton threadPoolSingleton;

    private ThreadPoolSingleton(){
        int CPUCores = Runtime.getRuntime().availableProcessors();
        threadPoolExecutor = new ThreadPoolExecutor(CPUCores + 1, CPUCores * 2 + 1, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
    }

    public static ThreadPoolSingleton getInstance(){
        if (threadPoolSingleton == null){
            synchronized (ThreadPoolSingleton.class){
                if (threadPoolSingleton == null){
                    threadPoolSingleton = new ThreadPoolSingleton();
                }
            }
        }
        return threadPoolSingleton;
    }
}
