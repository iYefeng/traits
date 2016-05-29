package com.traits.scheduler;


import org.apache.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by YeFeng on 2016/5/17.
 */
public class TaskScheduler {


    Logger logger = Logger.getLogger("scheduler");
    private Properties confProperties;
    private SchedulerFactory schedFact;
    private Scheduler sched;

    public TaskScheduler() throws SchedulerException {
        String configPath = this.getClass().getClassLoader().getResource("/").getPath()
                + "conf.properties";
        confProperties = new Properties();
        try {
            confProperties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        schedFact = new org.quartz.impl.StdSchedulerFactory();
        sched = schedFact.getScheduler();
        //Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
    }

    public void run() {
        try {
            String schedulerType = confProperties.getProperty("scheduler.type", "master");

            if (schedulerType.equals("master")) {
                // define the job and tie it to our HelloJob class
                JobDetail job = newJob(TaskTrigger.class)
                        .withIdentity("SystemJob", "group1")
                        .usingJobData("someProp", "someValue")
                        .build();
                // Trigger the job to run now, and then every 40 seconds
                Trigger trigger = newTrigger()
                        .withIdentity("SysytemTrigger", "group1")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(10)
                                .repeatForever())
                        .build();
                // Tell quartz to schedule the job using our trigger
                sched.scheduleJob(job, trigger);
            }

            if (!sched.isShutdown())
                sched.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
