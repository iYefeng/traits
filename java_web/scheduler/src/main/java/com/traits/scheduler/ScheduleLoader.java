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
    private SysScheduler _sysScheduler;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.debug(this.getClass().getClassLoader().getResource("/").getPath());
        PropertyConfigurator.configure(
                this.getClass().getClassLoader().getResource("/").getPath() + "log4j.properties"
        );
        try{
            _sysScheduler = new SysScheduler();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return;
        }

        _sysScheduler.run();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (_sysScheduler != null) {
            _sysScheduler.stop();
        }
    }
}
