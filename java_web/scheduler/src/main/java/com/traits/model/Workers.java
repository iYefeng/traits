package com.traits.model;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by YeFeng on 2016/7/20.
 */
public class Workers {

    private ThreadPoolExecutor threadPool;
    private int POOL_SIZE = 4;

    private final static Workers singleton = new Workers();

    Logger logger = Logger.getLogger("scheduler");

    private Workers() {
        Configure conf = Configure.getSingleton();
        POOL_SIZE = conf.POOL_SIZE;
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
    }

    public int getWorkingCount() {
        return threadPool.getActiveCount();
    }

    public boolean submitTask(BaseTask task) {
        return false;
    }

    public static Workers getSingleton() {
        return singleton;
    }


}
