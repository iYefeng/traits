package com.traits.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.traits.db.MySQLHandler;
import com.traits.jython.JythonEvaluable;
import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.model.Configure;
import com.traits.storage.BaseStorage;
import com.traits.storage.MongoDBStorage;
import com.traits.storage.MySQLStorage;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;


/**
 * Created by YeFeng on 2016/5/18.
 */
public class ProjectExecutor implements Job {

    Logger logger = Logger.getLogger("scheduler");
    private BaseProject project;
    private String dbtype, host, database, table, user, passwd;
    private int port;


    public ProjectExecutor() {
        Configure conf = Configure.getSingleton();
        dbtype = conf.dbtype;
        host = conf.host;
        port = conf.port;
        database = conf.database;
        user = conf.user;
        passwd = conf.passwd;
    }

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> ProjectExecutor execute");
        BaseStorage _storage = null;

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        this.project = (BaseProject) jobDataMap.get("currentProject");

        logger.debug("running project Id: " + project.getId());
        BaseTask task = new BaseTask();

        Date starttime = new Date();
        Date lunchTime;
        if (project.getCron() != null) {
            ArrayList<Date> t = project.getCron().getTimeBefore(new Date(context.getFireTime().getTime() + 1), 1);
            if (t != null) {
                lunchTime = t.get(0);
            } else {
                lunchTime = context.getFireTime();
            }

        } else {
            lunchTime = context.getFireTime();
        }

        String taskName = String.format("%s @ %s", project.getId(), df.format(lunchTime));



        HashMap<String, Object> args = null;

        String script = project.getArgs_script();
        logger.debug(">> jpython:" + script);
        if (script != null && !script.equals("")) {
            logger.debug(script);
            JythonEvaluable jpython = new JythonEvaluable(script);
            Object ret = jpython.evaluate();
            if (ret instanceof HashMap) {
                args = ((HashMap) ret);
            }
        }

        if (args == null) {
            args = new HashMap<String, Object>();
        }

        args.put("lunchTime", lunchTime.getTime());
        args.put("starttime", starttime.getTime());
        String argsJsonStr = JSON.toJSONString(args);
        logger.debug(argsJsonStr);

        for (int j = 0; j < project.getNum_workers(); ++j) {
            String taskId = DigestUtils.md5Hex(taskName) + "#" + String.valueOf(j);
            task.setId(taskId);
            task.setProject_id(project.getId());
            task.setName(taskName);
            task.setLunchtime(((double) lunchTime.getTime()) / 1000.0);
            task.setStarttime(((double) starttime.getTime()) / 1000.0);
            task.setStatus(BaseTask.Status.INIT);
            task.setArgs(argsJsonStr);
            task.setUpdatetime(((double) starttime.getTime()) / 1000.0);


            try {
                if (dbtype.equals("mysql")) {
                    _storage = new MySQLStorage(host, port, database, user, passwd);
                } else if (dbtype.equals("mongodb")) {
                    _storage = new MongoDBStorage(host, port, database, user, passwd);
                } else {
                    _storage = new MySQLStorage(host, port, database, user, passwd);
                }

                _storage.saveOneTask(task);
            } catch (Exception e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }

        _storage.release();
        logger.info("<< ProjectExecutor execute");
    }
}
