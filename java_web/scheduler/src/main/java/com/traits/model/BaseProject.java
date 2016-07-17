package com.traits.model;

import com.traits.util.Crontab;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YeFeng on 2016/7/16.
 *
 *
 Table:
 CREATE TABLE `projectdb` (
 `id` varchar(100) NOT NULL DEFAULT '' COMMENT 'project id',
 `name` varchar(512) DEFAULT NULL,
 `status` tinyint(4) DEFAULT NULL COMMENT '-1-unknown; 0-stop; 1-running; 2-delete; 3-debug; 4-checking',
 `crontab` varchar(128) DEFAULT NULL,
 `dependence` varchar(1024) DEFAULT NULL,
 `type` tinyint(4) DEFAULT NULL COMMENT '-1:unknown; 0-shell; 1-python; ',
 `user` varchar(128) DEFAULT NULL,
 `pkg` varchar(1024) DEFAULT NULL,
 `updatetime` timestamp NULL DEFAULT NULL,
 `delay` int(11) DEFAULT NULL,
 `emails` varchar(1024) DEFAULT NULL,
 `cellphones` varchar(1024) DEFAULT NULL,
 `args_script` varchar(1024) DEFAULT NULL,
 `retry` tinyint(4) DEFAULT NULL,
 PRIMARY KEY (`id`),
 KEY `idx_status` (`status`),
 KEY `idx_user` (`user`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

 *
 */
public class BaseProject {

    Logger logger = Logger.getLogger("scheduler");

    public enum Status {
        UNKNOWN(-1), STOP(0), RUNNING(1), DELETE(2), DEBUG(3),
        CHECKING(4), MODIFIED(5), ADDED(6), DONE(7);

        private int value = 0;

        private Status(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static Status valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return STOP;
                case 1:
                    return RUNNING;
                case 2:
                    return DELETE;
                case 3:
                    return DEBUG;
                case 4:
                    return CHECKING;
                case 5:
                    return MODIFIED;
                case 6:
                    return ADDED;
                case 7:
                    return DONE;
                default:
                    return UNKNOWN;
            }
        }

        public int value() {
            return this.value;
        }
    }

    public enum Type {
        UNKNOWN(-1), SHELL(0), PYTHON(1);

        private int value;

        private Type(int value) {
            this.value = value;
        }

        public static Type valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return SHELL;
                case 1:
                    return PYTHON;
                default:
                    return UNKNOWN;
            }
        }

        public int value() {
            return this.value;
        }
    }

    private String id;
    private String name;
    private Status status;
    private String crontab;
    private String dependence;
    private Type type;
    private String user;
    private String pkg;
    private Double updatetime;
    private Long delay;
    private String emails;
    private String cellphones;
    private String args_script;
    private Integer retry;
    private Crontab cron;


    private Status _sysStatus;

    public static ArrayList<BaseProject> load(HashMap<String, ArrayList<Object>> map, int count) {
        ArrayList<BaseProject> projects = new ArrayList<BaseProject>();

        for (int i = 0; i < count; ++i) {
            BaseProject tmp = new BaseProject();
            tmp.setId((String) map.get("id").get(i));
            tmp.setName((String) map.get("name").get(i));
            tmp.setStatus(Status.valueOf(map.get("status").get(i) == null ? -1 : (Integer) map.get("status").get(i)));
            tmp.setCrontab((String) map.get("crontab").get(i));
            tmp.setDependence((String) map.get("dependence").get(i));
            tmp.setType(Type.valueOf(map.get("type").get(i) == null ? -1 : (Integer) map.get("type").get(i)));
            tmp.setUser((String) map.get("user").get(i));
            tmp.setPkg((String) map.get("pkg").get(i));
            tmp.setUpdatetime(map.get("updatetime").get(i) == null ? 0 : (Double) map.get("updatetime").get(i));
            tmp.setDelay(map.get("delay").get(i) == null ? 0 : (Long) map.get("delay").get(i));
            tmp.setEmails((String) map.get("emails").get(i));
            tmp.setCellphones((String) map.get("cellphones").get(i));
            tmp.setArgs_script((String) map.get("args_script").get(i));
            tmp.setRetry(map.get("retry").get(i) == null ? 0 : (Integer) map.get("retry").get(i));
            if (tmp.getCrontab() != null) {
                try {
                    tmp.setCron(new Crontab(tmp.getCrontab()));
                } catch (ParseException e) {
                    tmp.setCron(null);
                }
            }
            projects.add(tmp);
        }

        return projects;
    }

    public void copy(BaseProject other) {
        if (this.id == null) {
            this.id = other.id;
        }
        this.name = other.name;
        this.status = other.status;
        this.crontab = other.crontab;
        this.dependence = other.dependence;
        this.type = other.type;
        this.user = other.user;
        this.pkg = other.pkg;
        this.updatetime = other.updatetime;
        this.delay = other.delay;
        this.emails = other.emails;
        this.cellphones = other.cellphones;
        this.args_script = other.args_script;
        this.cron = other.cron;
        this.retry = other.retry;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("project info {\n");
        sb.append("id: " + id + ",\n");
        sb.append("name: " + name + ",\n");
        sb.append("status:" + status + ",\n");
        sb.append("crontab:" + crontab + ",\n");
        sb.append("dependence: " + dependence + ",\n");
        sb.append("type: " + type + ",\n");
        sb.append("user: " + user + ",\n");
        sb.append("pkg: " + pkg + ",\n");
        sb.append("updatetime: " + String.valueOf(updatetime) + ",\n");
        sb.append("delay: " + String.valueOf(delay) + ",\n");
        sb.append("emails: " + emails + ",\n");
        sb.append("cellphones: " + cellphones + "\n");
        sb.append("args_script: " + args_script + "\n");
        sb.append("retry: " + retry + "\n");
        sb.append("}\n");

        return sb.toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCrontab() {
        return crontab;
    }

    public void setCrontab(String crontab) {
        this.crontab = crontab;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public double getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(double updatetime) {
        this.updatetime = updatetime;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getCellphones() {
        return cellphones;
    }

    public void setCellphones(String cellphones) {
        this.cellphones = cellphones;
    }

    public Status get_sysStatus() {
        return _sysStatus;
    }

    public void set_sysStatus(Status _sysStatus) {
        this._sysStatus = _sysStatus;
    }

    public String getArgs_script() {
        return args_script;
    }

    public void setArgs_script(String args_script) {
        this.args_script = args_script;
    }

    public Crontab getCron() {
        return cron;
    }

    public void setCron(Crontab cron) {
        this.cron = cron;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
