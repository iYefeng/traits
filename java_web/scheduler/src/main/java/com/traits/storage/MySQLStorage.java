package com.traits.storage;

import com.traits.db.MySQLHandler;
import com.traits.model.BaseProject;

import java.sql.SQLException;
import java.util.ArrayList;
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

    public MySQLStorage(String host, int port, String database, String user, String passwd) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.passwd = passwd;
    }

    public ArrayList<BaseProject> getProjects() throws SQLException {
        MySQLHandler handler = new MySQLHandler(host, port, database, user, passwd);
        ArrayList<BaseProject> projects = new ArrayList<BaseProject>();
        HashMap<String, ArrayList<Object>> result = handler.query("SELECT * from projectdb");

        for (int i = 0; i < handler.getCurrentCount(); ++i) {
            BaseProject tmp = new BaseProject();
            tmp.setId((String) result.get("id").get(i));
            tmp.setName((String) result.get("name").get(i));
            tmp.setStatus(BaseProject.Status.valueOf(result.get("status").get(i) == null ? -1 : (Integer) result.get("status").get(i)));
            tmp.setCrontab((String) result.get("crontab").get(i));
            tmp.setDependence((String) result.get("dependence").get(i));
            tmp.setType(BaseProject.Type.valueOf(result.get("type").get(i) == null ? -1 : (Integer) result.get("type").get(i)));
            tmp.setUser((String) result.get("user").get(i));
            tmp.setPkg((String) result.get("pkg").get(i));
            tmp.setUpdatetime(result.get("updatetime").get(i) == null ? 0 : (Double) result.get("updatetime").get(i));
            tmp.setDelay(result.get("delay").get(i) == null ? 0 :(Long) result.get("delay").get(i));
            tmp.setEmails((String) result.get("emails").get(i));
            tmp.setCellphones((String) result.get("cellphones").get(i));
            tmp.setArgs_script((String) result.get("args_script").get(i));

            projects.add(tmp);
        }

        handler.release();
        return projects;
    }

    public static void main(String[] args) throws SQLException {
        MySQLStorage ms = new MySQLStorage("127.0.0.1", 3306, "scheduler", "dev", "123456");
        ArrayList<BaseProject> projects = ms.getProjects();

        for (BaseProject i : projects) {
            System.out.println(i.toString());
        }
    }
}
