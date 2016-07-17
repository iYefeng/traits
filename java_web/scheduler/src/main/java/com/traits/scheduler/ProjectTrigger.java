package com.traits.scheduler;

import com.traits.model.BaseProject;
import org.apache.log4j.Logger;
import org.quartz.*;

import java.util.Map;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


/**
 * Created by YeFeng on 2016/5/18.
 */

@DisallowConcurrentExecution
public class ProjectTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");

    final String TRIGGER_GROUP_NAME = "PROJECTTRIGER";
    final String JOB_GROUP_NAME = "PROJECTJOB";
    Scheduler sched = SysScheduler.getScheduler();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> ProjectTrigger execute");
        ProjectScanner _projectscanner = ProjectScanner.getInstance();
        boolean flag = _projectscanner.loadProjects();
        if (flag) {
            Map<String, BaseProject> projectMap = _projectscanner.get_projectMap();

            for (Map.Entry<String, BaseProject> item : projectMap.entrySet()) {
                BaseProject p = item.getValue();
                if (p.get_sysStatus() == BaseProject.Status.ADDED) {

                    try {
                        if (p.getStatus() == BaseProject.Status.RUNNING
                                || p.getStatus() == BaseProject.Status.DEBUG) {
                            logger.debug("Add a running project");
                            JobDetail jobDetail = newJob(ProjectExecutor.class)
                                    .withIdentity(p.getId(), JOB_GROUP_NAME).build();
                            jobDetail.getJobDataMap().put("currentProject", p);
                            Trigger trigger = null;
                            if (p.getCrontab() != null && p.getCrontab().equals("")) {
                                trigger = newTrigger()
                                        .withIdentity(p.getId(), TRIGGER_GROUP_NAME)
                                        .withSchedule(simpleSchedule().withRepeatCount(0))
                                        .forJob(jobDetail)
                                        .build();
                            } else if (p.getCrontab() != null) {
                                trigger = newTrigger()
                                        .withIdentity(p.getId(), TRIGGER_GROUP_NAME)
                                        .startNow()
                                        .withSchedule(cronSchedule(p.getCrontab()))
                                        .build();
                            }

                            if (trigger != null) {
                                sched.scheduleJob(jobDetail, trigger);
                            }
                        }

                        if (!sched.isShutdown()) {
                            sched.start(); //
                        }
                    } catch (SchedulerException e) {
                        // TODO Auto-generated catch block
                        logger.debug(e.getMessage());
                        e.printStackTrace();
                    } //

                    p.set_sysStatus(BaseProject.Status.DONE);
                } else if (p.get_sysStatus() == BaseProject.Status.MODIFIED) {

                    try {
                        sched.pauseTrigger(new TriggerKey(p.getId(), TRIGGER_GROUP_NAME));
                        sched.unscheduleJob(new TriggerKey(p.getId(), TRIGGER_GROUP_NAME));//
                        sched.deleteJob(new JobKey(p.getId(), JOB_GROUP_NAME)); //
                    } catch (Exception e) {
                        logger.debug(e.getMessage());
                        e.printStackTrace();
                    }

                    try {
                        // add new task
                        if (p.getStatus() == BaseProject.Status.RUNNING
                                || p.getStatus() == BaseProject.Status.DEBUG) {
                            JobDetail jobDetail = newJob(ProjectExecutor.class)
                                    .withIdentity(p.getId(), JOB_GROUP_NAME).build();
                            jobDetail.getJobDataMap().put("currentProject", p);
                            Trigger trigger = null;
                            if (p.getCrontab() != null && p.getCrontab().equals("")) {
                                trigger = newTrigger()
                                        .withIdentity(p.getId(), TRIGGER_GROUP_NAME)
                                        .withSchedule(simpleSchedule().withRepeatCount(0))
                                        .forJob(jobDetail)
                                        .build();
                            } else if (p.getCrontab() != null){
                                trigger = newTrigger()
                                        .withIdentity(p.getId(), TRIGGER_GROUP_NAME)
                                        .startNow()
                                        .withSchedule(cronSchedule(p.getCrontab()))
                                        .build();
                            }
                            if (trigger != null) {
                                sched.scheduleJob(jobDetail, trigger);
                            }
                        }

                        if (!sched.isShutdown()) {
                            sched.start(); //
                        }
                    } catch (SchedulerException e) {
                        logger.debug(e.getMessage());
                        e.printStackTrace();
                    }

                    p.set_sysStatus(BaseProject.Status.DONE);
                } else if (p.get_sysStatus() == BaseProject.Status.DELETE) {
                    try {
                        sched.pauseTrigger(new TriggerKey(p.getId(), TRIGGER_GROUP_NAME));
                        sched.unscheduleJob(new TriggerKey(p.getId(), TRIGGER_GROUP_NAME));
                        sched.deleteJob(new JobKey(p.getId(), JOB_GROUP_NAME));
                    } catch (SchedulerException e) {
                        // TODO Auto-generated catch block
                        logger.debug(e.getMessage());
                        e.printStackTrace();
                    }

                    p.setStatus(BaseProject.Status.DELETE);
                    p.set_sysStatus(BaseProject.Status.DONE);
                }
            }


        }

        logger.info("<< ProjectTrigger execute");
    }

}
