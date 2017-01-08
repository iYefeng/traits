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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
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
    private RandomAccessFile monitorFile = null;
    private FileChannel monitorFileChannel = null;

    private String sourcePath;
    private String logPath;
    private String host;
    private int port;
    private String user;
    private String password;

    public void configure(Context context) {

        logger.debug(">> begin configure");
        sourcePath = context.getString(FileMonitorSourceConstants.CONFIG_FILENAME);
        logPath = context.getString(FileMonitorSourceConstants.CONFIG_LOGNAME);
        host = context.getString(FileMonitorSourceConstants.CONFIG_HOST);
        port = context.getInteger(FileMonitorSourceConstants.CONFIG_PORT);
        user = context.getString(FileMonitorSourceConstants.CONFIG_USER);
        password = context.getString(FileMonitorSourceConstants.CONFIG_PWD);

        if (sourceCounter == null) {
            sourceCounter = new SourceCounter(getName());
        }

        logger.debug("sourcePath: {}, logPath: {}\n host: {}, port: {}",
                sourcePath,
                logPath,
                host,
                port);
        logger.debug("<< end configure");
    }

    @Override
    public synchronized void start() {
        logger.info(">> FileMonitorSource start");

        channelProcessor = getChannelProcessor();
        executor = Executors.newSingleThreadScheduledExecutor();
        runner = new FileMonitorWorker();
        executor.scheduleWithFixedDelay(runner, 500, 2000, TimeUnit.MILLISECONDS);
        sourceCounter.start();

        super.start();
    }

    @Override
    public synchronized void stop() {
        logger.info(">> FileMonitorSource stop");

        if (this.monitorFileChannel != null) {
            try {
                this.monitorFileChannel.close();
            } catch (IOException e) {
                logger.error(this.sourcePath + " filechannel close Exception", e);
            }
        }
        if (this.monitorFile != null) {
            try {
                this.monitorFile.close();
            } catch (IOException e) {
                logger.error(this.sourcePath + " file close Exception", e);
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10L, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            logger.info("Interrupted while awaiting termination", ex);
        }
        executor.shutdownNow();
        sourceCounter.stop();

        super.stop();

    }

}
