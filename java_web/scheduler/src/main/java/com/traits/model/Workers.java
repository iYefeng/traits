package com.traits.model;

import com.traits.db.RedisHandler;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by YeFeng on 2016/7/20.
 */
public class Workers {

    private ThreadPoolExecutor threadPool;
    private int POOL_SIZE = 4;

    private final static Workers singleton = new Workers();

    HashMap<BaseTask, Future<Integer>> futureList = new HashMap<BaseTask, Future<Integer>>();
    HashSet<String> runningTasks = new HashSet<String>();

    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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
        Future<Integer> future = threadPool.submit(task);
        futureList.put(task, future);
        runningTasks.add(task.getId().split("#")[0]);
        return true;
    }

    public boolean removeTaskFromFutureList(BaseTask t) {
        futureList.remove(t);
        return true;
    }


    public static Workers getSingleton() {
        return singleton;
    }

    public HashMap<BaseTask, Future<Integer>> getFutureList() {
        return futureList;
    }

    public void setFutureList(HashMap<BaseTask, Future<Integer>> futureList) {
        this.futureList = futureList;
    }

    public HashSet<String> getRunningTasks() {
        return runningTasks;
    }

    public void setRunningTasks(HashSet<String> runningTasks) {
        this.runningTasks = runningTasks;
    }
}
