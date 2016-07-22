package com.traits.storage;

import org.apache.commons.lang3.StringUtils;
import com.traits.db.MySQLHandler;
import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.scheduler.SysScheduler;
import com.traits.util.Crontab;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public void release() {
        handler.release();
    }

    public ArrayList<BaseProject> getProjects() throws SQLException {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `projectdb`");
        return BaseProject.load(result, handler.getCurrentCount());
    }

    public ArrayList<BaseProject> getProjects(String[] fields) throws SQLException {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT %s from `projectdb`",
                new String[]{StringUtils.join(fields, ", ")});
        return BaseProject.load(result, handler.getCurrentCount());
    }

    public BaseProject getProjectById(String pid) throws SQLException {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `projectdb` WHERE `id`='%s'",
                new String[]{pid});
        ArrayList<BaseProject> projects = BaseProject.load(result, handler.getCurrentCount());
        return projects.size() == 0 ? null : projects.get(0);
    }

    public boolean saveOneTask(BaseTask task) throws SQLException {
        boolean flag =  task.saveTask(handler);
        return flag;
    }

    public ArrayList<BaseTask> getInitTasks() throws Exception {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `taskdb` where `status`=0");
        return BaseTask.load(result, handler.getCurrentCount());
    }

    public ArrayList<BaseTask> getCheckingTasks() throws Exception {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `taskdb` where `status`=7");
        return BaseTask.load(result, handler.getCurrentCount());
    }

    public ArrayList<BaseTask> getActiveTasks() throws Exception {
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from `taskdb` where `status`=1");
        return BaseTask.load(result, handler.getCurrentCount());
    }

    public HashSet<String> getSuccessOrPassedTasks() throws Exception {

        Date current = new Date();
        Date beforeDate = new Date(current.getTime() - 60L * 60L * 24L * 31L * 1000L);

        HashMap<String, ArrayList<Object>> result = handler.query(
                "SELECT `id` from `taskdb` where (`status`=3 OR `status`=8) AND `lunchtime` > '%s'",
                new String[]{df.format(beforeDate)}
        );

        HashSet<String> taskSet = new HashSet<String>();
        for (int i = 0; i < handler.getCurrentCount(); ++i) {
            taskSet.add((String) result.get("id").get(i));
        }

        return taskSet;
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
