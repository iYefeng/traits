package com.traits.scheduler;


import org.apache.log4j.Logger;
import org.quartz.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by YeFeng on 2016/5/17.
 */
public class SysScheduler {


    Logger logger = Logger.getLogger("scheduler");
    private Properties confProperties;
    private SchedulerFactory schedFact;
    private static Scheduler sched;

    public SysScheduler() throws SchedulerException {
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
        logger.info("SysScheduler running");
        try {
            String schedulerType = confProperties.getProperty("scheduler.type", "projectTrigger, taskTrigger, taskWorker");

            if (schedulerType.contains("projectTrigger")) {
                logger.info("run as projectTrigger");
                // define the job and tie it to our HelloJob class
                JobDetail job = newJob(ProjectTrigger.class)
                        .withIdentity("SystemJob", "projectTrigger")
                        .build();
                // Trigger the job to run now, and then every 40 seconds
                Trigger trigger = newTrigger()
                        .withIdentity("SysytemTrigger", "projectTrigger")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(10)
                                .repeatForever())
                        .build();
                // Tell quartz to schedule the job using our trigger
                sched.scheduleJob(job, trigger);
            }

            if (schedulerType.contains("taskTrigger")) {
                logger.info("run as taskTrigger");

                TaskTrigger tt = new TaskTrigger();
                tt.initLoad();

                // define the job and tie it to our HelloJob class
                JobDetail job = newJob(TaskTrigger.class)
                        .withIdentity("SystemJob", "taskTrigger")
                        .build();
                // Trigger the job to run now, and then every 40 seconds
                Trigger trigger = newTrigger()
                        .withIdentity("SysytemTrigger", "taskTrigger")
                        .startNow()
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(20)
                                .repeatForever())
                        .build();
                // Tell quartz to schedule the job using our trigger
                sched.scheduleJob(job, trigger);
            }

            if (schedulerType.contains("taskWorker")) {
                logger.info("run as taskWorker");
                Random r = new Random((new Date()).getTime());

                // define the job and tie it to our HelloJob class
                JobDetail job = newJob(WorkTrigger.class)
                        .withIdentity("SystemJob", "WorkTrigger")
                        .build();
                // Trigger the job to run now, and then every 40 seconds
                Trigger trigger = newTrigger()
                        .withIdentity("SysytemTrigger", "WorkTrigger")
                        .startAt(new Date((new Date()).getTime() + r.nextInt(1000)))
                        .withSchedule(simpleSchedule()
                                .withIntervalInSeconds(1)
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

    public static Scheduler getScheduler() {
        return sched;
    }
}
