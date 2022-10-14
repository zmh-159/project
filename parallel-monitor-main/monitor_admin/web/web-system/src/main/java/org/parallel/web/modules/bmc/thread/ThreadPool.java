package org.parallel.web.modules.bmc.thread;


import java.util.concurrent.*;
public class ThreadPool {
    public static ExecutorService httpPool(){
        return new ThreadPoolExecutor(10, 100 ,5, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}

