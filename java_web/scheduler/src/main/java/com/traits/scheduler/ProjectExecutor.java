package com.traits.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.traits.jython.JythonEvaluable;
import com.traits.model.BaseProject;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by YeFeng on 2016/5/18.
 */
public class ProjectExecutor implements Job {

    Logger logger = Logger.getLogger("scheduler");
    private BaseProject project;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> ProjectExecutor execute");

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        this.project = (BaseProject) jobDataMap.get("currentProject");

        logger.debug("running project Id: " + project.getId());
        String lunchTime = df.format(new Date());


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

        args.put("lunchTime", lunchTime);
        String jsonStr = JSON.toJSONString(args);
        logger.debug(jsonStr);


        logger.info("<< ProjectExecutor execute");
    }
}
