package com.traits.storage;

import com.traits.db.MySQLHandler;
import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.scheduler.SysScheduler;
import com.traits.util.Crontab;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by YeFeng on 2016/7/16.
 */
public class MySQLStorage extends BaseStorage {

    private String host;
    private int port;
    private String database;
    private String user;
    private String passwd;
    private MySQLHandler handler;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MySQLStorage(String host, int port, String database, String user, String passwd) throws SQLException {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.passwd = passwd;
        this.handler = new MySQLHandler(host, port, database, user, passwd);
    }

    protected void finalize()
    {
        this.handler.release();
    }

    public ArrayList<BaseProject> getProjects() throws SQLException {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `projectdb`");

        return BaseProject.load(result, handler.getCurrentCount());
    }

    public boolean saveOneTask(BaseTask task) throws SQLException {
        boolean flag =  task.saveTask(handler);
        return flag;
    }

    public ArrayList<BaseTask> getInitTasks() throws Exception {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `taskdb` where `status`=0");

        return BaseTask.load(result, handler.getCurrentCount());
    }

    public ArrayList<BaseTask> getSuccessOrPassedTasks() throws Exception {

        Date current = new Date();
        Date beforeDate = new Date(current.getTime() - 60L * 60L * 24L * 31L * 1000L);

        HashMap<String, ArrayList<Object>> result = handler.query(
                "SELECT * from `taskdb` where (`status`=3 OR `status`=8) AND `lunchtime` > '%s'",
                new String[]{df.format(beforeDate)}
        );

        return BaseTask.load(result, handler.getCurrentCount());
    }

    public static void main(String[] args) throws SQLException {
        MySQLStorage ms = new MySQLStorage("127.0.0.1", 3306, "scheduler", "dev", "123456");
        ArrayList<BaseProject> projects = ms.getProjects();

        for (BaseProject i : projects) {
            System.out.println(i.toString());
        }

        Date current = new Date();
        long tmp = current.getTime() - 86400L*1000*30;
        System.out.println(current.getTime());
        System.out.println(tmp);
        Date beforeDate = new Date(tmp);

        System.out.println(df.format(current));
        System.out.println(df.format(beforeDate));
    }
}
