package com.traits.scheduler;


import org.apache.log4j.Logger;
import org.quartz.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by YeFeng on 2016/5/17.
 */
public class TaskScheduler {


    Logger logger = Logger.getLogger("scheduler");

    public static void run() {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
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
            if (!sched.isShutdown())
                sched.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public static void stop() {
        try {
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            sched.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
