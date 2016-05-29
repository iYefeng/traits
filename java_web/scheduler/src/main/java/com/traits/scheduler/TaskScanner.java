package com.traits.scheduler;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by YeFeng on 2016/5/18.
 */
public class TaskScanner {

    private final static TaskScanner singleton = new TaskScanner();
    private String dbtype, host, database, table, user, passwd;
    private int port;
    Logger logger = Logger.getLogger("scheduler");

    private TaskScanner() {
        String configPath = this.getClass().getClassLoader().getResource("/").getPath()
                + "conf.properties";
        Properties confProperties = new Properties();
        try {
            confProperties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        dbtype = confProperties.getProperty("task.storage", "mongodb");
        host = confProperties.getProperty("task.host", "127.0.0.1");
        port = Integer.parseInt(confProperties.getProperty("task.port", "27017"));
        database = confProperties.getProperty("task.db", "scheduler");
        table = confProperties.getProperty("task.table", "task");
        user = confProperties.getProperty("task.user", "");
        passwd = confProperties.getProperty("task.passwd", "");





    }

    public static TaskScanner getInstance() {
        return singleton;
    }





}
