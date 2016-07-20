package com.traits.scheduler;

import com.traits.model.Configure;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by YeFeng on 2016/7/19.
 */
@DisallowConcurrentExecution
public class WorkTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");

    private String dbtype, host, db;
    private int port;

    public WorkTrigger() {
        Configure conf = Configure.getSingleton();



    }

    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
