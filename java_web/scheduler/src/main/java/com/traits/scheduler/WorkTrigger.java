package com.traits.scheduler;

import com.traits.db.RedisHandler;
import com.traits.model.*;
import com.traits.storage.BaseStorage;
import com.traits.storage.MongoDBStorage;
import com.traits.storage.MySQLStorage;
import com.traits.util.SerializeUtil;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by YeFeng on 2016/7/19.
 */
@DisallowConcurrentExecution
public class WorkTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");

    private String dbtype, host, database, user, passwd, redis_host, redis_db;
    private int port, redis_port, maxActiveCount;

    private static RedisHandler redis_handler = null;

    public WorkTrigger() {
        Configure conf = Configure.getSingleton();
        dbtype = conf.dbtype;
        host = conf.host;
        port = conf.port;
        database = conf.database;
        user = conf.user;
        passwd = conf.passwd;

        redis_host = conf.redis_host;
        redis_port = conf.redis_port;
        redis_db = conf.redis_db;
        maxActiveCount = conf.maxActiveCount;

        if (redis_handler == null) {
            try {
                redis_handler = new RedisHandler(redis_host, redis_port, redis_db);
            } catch (Exception e) {
                logger.error("Redis init error");
                e.printStackTrace();
            }
        }
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info(">> WorkTrigger execute");

        BaseStorage _storage = null;
        BaseProject project = null;

        try {
            if (dbtype.equals("mysql")) {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            } else if (dbtype.equals("mongodb")) {
                _storage = new MongoDBStorage(host, port, database, user, passwd);
            } else {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return;
        }

        if (redis_handler != null) {

            Workers _workers = Workers.getSingleton();

            Iterator<Map.Entry<BaseTask, Future<Integer>>> it = _workers.getFutureList().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<BaseTask, Future<Integer>> kvTask = it.next();
                BaseTask task = kvTask.getKey();
                Future<Integer> future = kvTask.getValue();
                if (future.isDone())  {
                    task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);

                    try {
                        if (future.get() == 0) {
                            task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                            task.setStatus(BaseTask.Status.SUCCESS);
                        } else {
                            if (task.getRetry_count() >= task.get_project().getRetry()) {
                                task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                                task.setStatus(BaseTask.Status.FAIL);
                            } else {
                                task.setStatus(BaseTask.Status.ACTIVE);
                                task.setRetry_count(task.getRetry_count() + 1);
                                // push to redis
                                redis_handler.getHandler().rpush(
                                        "scheduler.task.queue".getBytes(),
                                        SerializeUtil.serialize(task));
                            }

                        }
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }

                    it.remove();
                    _workers.getRunningTasks().remove(task.getId().split("#")[0]);
                } else if (future.isCancelled()) {
                    task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                    task.setStatus(BaseTask.Status.DELETE);
                    it.remove();
                    _workers.getRunningTasks().remove(task.getId().split("#")[0]);
                } else {
                    task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                }

                try {
                    _storage.saveOneTask(kvTask.getKey());
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }


            if (_workers.getWorkingCount() >= maxActiveCount) {
                logger.info("The number of active tasks is larger than maxActiveCount");
                return;
            }


            byte[] task_s = redis_handler.getHandler().lpop("scheduler.task.queue".getBytes());
            if (task_s == null) return;
            BaseTask task = (BaseTask) SerializeUtil.unserialize(task_s);
            if (task == null) return;

            if (_workers.getRunningTasks().contains(task.getId().split("#")[0])) {
                redis_handler.getHandler().rpush("scheduler.task.queue".getBytes(),
                        SerializeUtil.serialize(task));
                return;
            }

            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);

            try {
                project = _storage.getProjectById(task.getProject_id());
            } catch (SQLException e) {
                logger.error("get project error");
                e.printStackTrace();
            }

            if (project == null) {
                logger.info("project is not found");
                task.setStatus(BaseTask.Status.DELETE);
                try {
                    _storage.saveOneTask(task);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
                return;
            }

            if (project.getStatus() == BaseProject.Status.RUNNING
                    || project.getStatus() == BaseProject.Status.DEBUG) {
                // run task
                task.setStatus(BaseTask.Status.RUNNING);
                try {
                    _storage.saveOneTask(task);
                } catch (Exception e) {
                    logger.error("save task error");
                    e.printStackTrace();
                }
                task.set_project(project);
                _workers.submitTask(task);
            } else if (project.getStatus() == BaseProject.Status.DELETE) {
                // pass
                task.setStatus(BaseTask.Status.DELETE);
                try {
                    _storage.saveOneTask(task);
                } catch (Exception e) {
                    logger.error("save task error");
                    e.printStackTrace();
                }
            } else {
                task.setStatus(BaseTask.Status.STOP);
                try {
                    _storage.saveOneTask(task);
                } catch (Exception e) {
                    logger.error("save task error");
                    e.printStackTrace();
                }
            }
        }
        _storage.release();
        logger.info("<< WorkTrigger execute");
    }
}
