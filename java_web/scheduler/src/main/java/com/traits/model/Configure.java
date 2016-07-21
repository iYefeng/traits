package com.traits.model;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;

/**
 * Created by YeFeng on 2016/7/20.
 */
public class Configure {
    private final static Configure singleton = new Configure();

    Logger logger = Logger.getLogger("scheduler");

    public String dbtype, host, database, user, passwd;
    public int port;
    public Long triggerDelta;
    public Long ignore;
    public int POOL_SIZE, maxActiveCount;
    public String msgQueue;

    public String redis_host = "127.0.0.1";
    public String redis_db = "1";
    public Integer redis_port = 6379;

    private Pattern uriReg = Pattern.compile("(\\w+)://(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)/(\\d+)");


    private Configure() {
        String configPath = this.getClass()
                .getClassLoader()
                .getResource("/")
                .getPath() + "conf.properties";
        Properties confProperties = new Properties();
        try {
            confProperties.load(new FileInputStream(configPath));
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }

        dbtype = confProperties.getProperty("task.storage", "mysql");
        host = confProperties.getProperty("task.host", "127.0.0.1");
        port = Integer.parseInt(confProperties.getProperty("task.port", "3306"));
        database = confProperties.getProperty("task.db", "scheduler");
        user = confProperties.getProperty("task.user", null);
        passwd = confProperties.getProperty("task.password", null);

        triggerDelta = Long.valueOf(confProperties.getProperty("conf.task.trigger.triggerDelta", "600"));
        ignore = Long.valueOf(confProperties.getProperty("conf.task.trigger.ignore", "604800"));

        POOL_SIZE = Integer.valueOf(confProperties.getProperty("conf.worker.threadpool.size", "4"));
        maxActiveCount = Integer.valueOf(confProperties.getProperty("conf.worker.maxActiveCount", "4"));

        String redisTmp = confProperties.getProperty("task.message.queue", "redis://127.0.0.1:6379/1");
        java.util.regex.Matcher mat = uriReg.matcher(redisTmp);
        if (mat.find()) {
            if (mat.group(1).equals("redis")) {
                redis_host = mat.group(2);
                redis_port = Integer.valueOf(mat.group(3));
                redis_db = mat.group(4);
            }
        }

    }

    public static Configure getSingleton() {
        return singleton;
    }

}
