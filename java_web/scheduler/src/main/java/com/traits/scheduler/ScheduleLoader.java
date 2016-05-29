package com.traits.scheduler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by YeFeng on 2016/5/17.
 */
public class ScheduleLoader implements ServletContextListener {

    Logger logger = Logger.getLogger("scheduler");
    private TaskScheduler taskScheduler;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.debug(this.getClass().getClassLoader().getResource("/").getPath());
        PropertyConfigurator.configure(
                this.getClass().getClassLoader().getResource("/").getPath() + "log4j.properties"
        );
        try{
            taskScheduler = new TaskScheduler();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return;
        }
        taskScheduler.run();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (taskScheduler != null) {
            taskScheduler.stop();
        }
    }
}
