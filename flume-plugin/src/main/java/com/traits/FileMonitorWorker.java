package com.traits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by YeFeng on 2017/1/7.
 */
public class FileMonitorWorker implements Runnable {

    private static final Logger logger = LoggerFactory
            .getLogger(FileMonitorWorker.class);

    public void run() {

        logger.debug(">> FileMonitorWorker running");

    }
}
