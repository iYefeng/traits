package com.traits.model;

import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by YeFeng on 2016/7/17.
 */
public class TaskCache {
    private final static TaskCache singleton = new TaskCache();

    Logger logger = Logger.getLogger("scheduler");

    TreeMap<Double, BaseTask> _taskMap = new TreeMap<Double, BaseTask>(
        new Comparator<Double>() {
            public int compare(Double o1, Double o2){
                if (o1 == null || o2 == null)
                    return 0;
                return o1.compareTo(o2);
            }
        }
    );

    public TreeMap<Double, BaseTask> get_taskMap() {
        return _taskMap;
    }

    public void set_taskMap(TreeMap<Double, BaseTask> _taskMap) {
        this._taskMap = _taskMap;
    }

    public static TaskCache getInstance() {
        return singleton;
    }

    public static void main(String[] args) {
        TaskCache t = TaskCache.getInstance();
        t.get_taskMap().put(12.0, new BaseTask());
        t.get_taskMap().put(1.0, new BaseTask());
        t.get_taskMap().put(22.0, new BaseTask());

        System.out.println(t.get_taskMap().firstEntry().getKey());

        t.get_taskMap().put(0.1, new BaseTask());

        t.get_taskMap().pollFirstEntry();

        t.get_taskMap().put(2.0, new BaseTask());

        for (Map.Entry<Double, BaseTask> tt : t.get_taskMap().entrySet()) {
            System.out.println(tt.getKey());
        }
    }



}
