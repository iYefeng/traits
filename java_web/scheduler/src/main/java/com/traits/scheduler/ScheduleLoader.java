package com.traits.scheduler;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by YeFeng on 2016/5/17.
 */
public class ScheduleLoader implements ServletContextListener {

    TaskScheduler scheduler = new TaskScheduler();
    Logger logger = Logger.getLogger("scheduler");

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.debug(this.getClass().getClassLoader().getResource("/").getPath());
        PropertyConfigurator.configure(
                this.getClass().getClassLoader().getResource("/").getPath() + "log4j.properties"
        );
        scheduler.run();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        scheduler.stop();
    }
}
