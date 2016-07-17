package com.traits.scheduler;

import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.model.TaskCache;
import com.traits.storage.BaseStorage;
import com.traits.storage.MongoDBStorage;
import com.traits.storage.MySQLStorage;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by YeFeng on 2016/7/17.
 */

@DisallowConcurrentExecution
public class TaskTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");
    private String dbtype, host, database, table, user, passwd;
    private int port;

    private HashMap<String, BaseProject> _projectMap;
    private HashMap<String, BaseTask> _initTaskMap;
    private HashMap<String, BaseTask> _successOrPassedTaskMap;

    public TaskTrigger() {
        String configPath = this.getClass().getClassLoader().getResource("/").getPath()
                + "conf.properties";
        Properties confProperties = new Properties();
        try {
            confProperties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        dbtype = confProperties.getProperty("task.storage", "mysql");
        host = confProperties.getProperty("task.host", "127.0.0.1");
        port = Integer.parseInt(confProperties.getProperty("task.port", "3306"));
        database = confProperties.getProperty("task.db", "scheduler");
        user = confProperties.getProperty("task.user", null);
        passwd = confProperties.getProperty("task.password", null);
    }

    public void loadProjects(BaseStorage _storage) throws Exception {
        _projectMap = new HashMap<String, BaseProject>();
        for (BaseProject p : _storage.getProjects()) {
            _projectMap.put(p.getId(), p);
        }
    }

    public void loadInitTasks(BaseStorage _storage) throws Exception {
        _initTaskMap = new HashMap<String, BaseTask>();
        for (BaseTask t : _storage.getInitTasks()) {
            _initTaskMap.put(t.getId(), t);
        }
    }

    public void loadSuccessOrPassedTask(BaseStorage _storage) throws Exception {
        _successOrPassedTaskMap = new HashMap<String, BaseTask>();
        for (BaseTask t: _storage.getSuccessOrPassedTasks()) {
            _successOrPassedTaskMap.put(t.getId(), t);
        }
    }

    public ArrayList<String> parseDependence(BaseProject _self,
                                             HashMap<String, BaseProject> _projectMap,
                                             Date lunchDate) {
        ArrayList<String> dep = new ArrayList<String>();

        // projectId   basetime, count
        // task.test1:([day(-3)+2345, 12),(hour(-2), 1),(minute(0), 3)];




        return dep;
    }

    public boolean checkTask(HashMap<String, BaseTask> _successOrPassedTaskMap,
                             HashMap<String, BaseProject> _projectMap,
                             BaseProject project,
                             Date lunchDate) {
        if (project.getDependence() == null || project.getDependence().equals("")) {
            return true;
        }



        return false;
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> TaskTrigger execute");
        BaseStorage _storage = null;
        TaskCache tc = TaskCache.getInstance();

        try {
            if (dbtype.equals("mysql")) {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            } else if (dbtype.equals("mongodb")) {
                _storage = new MongoDBStorage(host, port, database, user, passwd);
            } else {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            }

            loadProjects(_storage);
            loadInitTasks(_storage);
            loadSuccessOrPassedTask(_storage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return;
        }

        for (Map.Entry<String, BaseTask> kv : _initTaskMap.entrySet()) {
            BaseTask t = kv.getValue();
            String pid = t.getProject_id();
            if (_projectMap.containsKey(pid)) {
                BaseProject p = _projectMap.get(pid);
                if ( ((long)((t.getLunchtime() + p.getDelay()) * 1000)) < (new Date()).getTime()
                        && checkTask(_successOrPassedTaskMap, _projectMap, p,
                        new Date((new Double(t.getLunchtime() * 1000)).longValue()))) {
                    // the task satifies dependence
                    t.setStatus(BaseTask.Status.ACTIVE);
                    t.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    try {
                        _storage.saveOneTask(t);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    // task does not satifies dependence
                    t.setStatus(BaseTask.Status.CHECKING);
                    t.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    t.setTriggertime((((new Date()).getTime() + 60 * 10 * 1000) / 1000.0 + p.getDelay()));
                    try {
                        _storage.saveOneTask(t);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    tc.get_taskMap().put(t.getTriggertime(), t);
                }
            }
        }

        for (Map.Entry<Double, BaseTask> t : tc.get_taskMap().entrySet()) {
            Double triggerTime = t.getKey();
            BaseTask task = t.getValue();

            if (triggerTime < ((new Date()).getTime() / 1000.0)) {
                String pid = task.getProject_id();
                if (_projectMap.containsKey(pid)) {
                    BaseProject p = _projectMap.get(pid);
                    if (p.getStatus() == BaseProject.Status.RUNNING
                            || p.getStatus() == BaseProject.Status.DEBUG) {   // running or debug
                        if (checkTask(_successOrPassedTaskMap, _projectMap, p,
                                new Date((new Double(task.getLunchtime() * 1000)).longValue()))){
                            // the task satifies dependence
                            task.setStatus(BaseTask.Status.ACTIVE);
                            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                            try {
                                _storage.saveOneTask(task);
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                e.printStackTrace();
                            }
                            tc.get_taskMap().pollFirstEntry();
                        } else {                                // does not satify dependence
                            task.setStatus(BaseTask.Status.CHECKING);
                            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                            task.setTriggertime(((double) (new Date()).getTime() + 60 * 10 * 1000) / 1000.0);
                            try {
                                _storage.saveOneTask(task);
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                e.printStackTrace();
                            }
                            tc.get_taskMap().pollFirstEntry();
                            tc.get_taskMap().put(task.getTriggertime(), task);
                        }
                    } else {                                    // stop delete...
                        if (p.getStatus() == BaseProject.Status.DELETE) {
                            task.setStatus(BaseTask.Status.DELETE);
                        } else {
                            task.setStatus(BaseTask.Status.STOP);
                        }
                        task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                        task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                        try {
                            _storage.saveOneTask(task);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            e.printStackTrace();
                        }
                        tc.get_taskMap().pollFirstEntry();
                    }
                } else {                                        // delete
                    task.setStatus(BaseTask.Status.DELETE);
                    task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                    try {
                        _storage.saveOneTask(task);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    tc.get_taskMap().pollFirstEntry();
                }
            } else {                                            // time don't arrive
                break;
            }
        }


        logger.info("<< TaskTrigger execute");
    }
}
