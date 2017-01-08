package com.traits;

import com.google.common.base.Preconditions;
import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.instrumentation.SourceCounter;
import org.apache.flume.lifecycle.LifecycleState;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by YeFeng on 2017/1/7.
 */
public class FileMonitorSource extends AbstractSource
        implements Configurable, EventDrivenSource {

    private static final Logger logger = LoggerFactory
            .getLogger(FileMonitorSource.class);

    private ChannelProcessor channelProcessor;
    private SourceCounter sourceCounter;
    private ScheduledExecutorService executor;
    private FileMonitorWorker runner;

    private String sourcePath;
    private String logPath;
    private String hostName;
    private int port;

    public void configure(Context context) {
        sourcePath = context.getString(FileMonitorSourceConstants.CONFIG_FILENAME);
        logPath = context.getString(FileMonitorSourceConstants.CONFIG_LOGNAME);


        if (sourceCounter == null) {
            sourceCounter = new SourceCounter(getName());
        }

        logger.debug("sourcePath: {}, logPath: {}", sourcePath, logPath);
    }

    @Override
    public synchronized void start() {
        channelProcessor = getChannelProcessor();
        executor = Executors.newSingleThreadScheduledExecutor();
        runner = new FileMonitorWorker();
        executor.scheduleWithFixedDelay(runner, 500, 2000, TimeUnit.MILLISECONDS);
        sourceCounter.start();

        super.start();
        logger.info("FileMonitorSource start");
    }

    @Override
    public synchronized void stop() {


        super.stop();
        logger.info("FileMonitorSource stop");
    }

}
