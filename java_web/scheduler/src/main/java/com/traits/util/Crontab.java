package com.traits.util;

import com.traits.scheduler.SysScheduler;
import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YeFeng on 2016/7/17.
 */
public class Crontab extends CronExpression {

    Logger logger = Logger.getLogger("Crontab");

    private long deltaMinute = 60 * 1000;
    private long deltaHour = 60 * 60 * 1000;
    private long deltaDay = 60 * 60 * 24 * 1000;
    private long deltaWeek = deltaDay * 7;
    private long deltaMonth = deltaDay * 31;
    private long deltaYear = deltaDay * 366;
    private long[] delta = new long[]{deltaMinute, deltaHour, deltaDay, deltaWeek, deltaMonth, deltaYear};

    /**
     * Constructs a new <CODE>CronExpression</CODE> based on the specified
     * parameter.
     *
     * @param cronExpression String representation of the cron expression the
     *                       new object should represent
     * @throws ParseException if the string expression cannot be parsed into a valid
     *                                  <CODE>CronExpression</CODE>
     */
    public Crontab(String cronExpression) throws ParseException {
        super(cronExpression);
    }

    public ArrayList<Date> getTimeBefore(Date baseTime, int count) {
        ArrayList<Date> result = null;
        ArrayList<Date> beforeDates = new ArrayList<Date>();
        ArrayList<Date> tmp;

        for (int cnt = 0; cnt < delta.length; ++cnt) {
            logger.debug("cnt: " + cnt);
            tmp = new ArrayList<Date>();
            Date _before_time = new Date(baseTime.getTime() - delta[cnt]);

            Date next = getTimeAfter(_before_time);
            while (next != null && next.getTime() < (baseTime.getTime() - (cnt == 0 ? 0 : delta[cnt-1]))) {
                tmp.add(next);
                next = getTimeAfter(next);
            }

            tmp.addAll(beforeDates);
            beforeDates = tmp;
            if (beforeDates.size() >= count) {
                result = new ArrayList<Date>();
                int len = beforeDates.size();
                for (int j = 0; j < count; ++j) {
                    result.add(beforeDates.get(len - 1 -j));
                }
                break;
            }
        }
        return result;
    }

    public static void main(String[] args) throws ParseException {
        ArrayList<Date> result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss | E | D | F | w | W");

        Crontab cron = new Crontab("0 */10 16 ? * SUN");
        Date current = new Date();

        System.out.println("Current: " + df.format(current));

        result = cron.getTimeBefore(current, 20);
        for (Date d : result) {
            System.out.println(df.format(d));
        }

    }

}
