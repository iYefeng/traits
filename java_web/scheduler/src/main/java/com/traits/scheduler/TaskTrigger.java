package com.traits.scheduler;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * Created by YeFeng on 2016/5/18.
 */
public class TaskTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> TaskTrigger execute");
        TaskScanner taskscanner = TaskScanner.getInstance();


        JobDataMap data = context.getMergedJobDataMap();
        System.out.println("someProp = " + data.getString("someProp"));
        logger.info("<< TaskTrigger execute");
    }

}
