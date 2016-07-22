package com.traits.model;

import com.traits.util.Crontab;
import org.apache.log4j.Logger;

import java.io.Serializable;
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
 `group` varchar(64) DEFAULT NULL,
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
public class BaseProject implements Serializable {

    static final Logger logger = Logger.getLogger("scheduler");

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
        UNKNOWN(-1), SHELL(0), PYTHON(1), SHELLSCRIPT(2);

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
                case 2:
                    return SHELLSCRIPT;
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
    private String group;
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
    private String script;
    private Integer num_workers;

    private Status _sysStatus;

    public static ArrayList<BaseProject> load(HashMap<String, ArrayList<Object>> map, int count) {
        ArrayList<BaseProject> projects = new ArrayList<BaseProject>();

        for (int i = 0; i < count; ++i) {
            BaseProject tmp = new BaseProject();
            for (String key : map.keySet()) {
                Object obj = map.get(key).get(i);
                tmp.setKeyValue(key, obj);
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
        this.group = other.group;
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
        this.script = other.script;
        this.num_workers = other.num_workers;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("project info {\n");
        sb.append("id: " + id + ",\n");
        sb.append("name: " + name + ",\n");
        sb.append("status:" + status + ",\n");
        sb.append("crontab:" + crontab + ",\n");
        sb.append("dependence: " + dependence + ",\n");
        sb.append("group: " + group + ",\n");
        sb.append("type: " + type + ",\n");
        sb.append("user: " + user + ",\n");
        sb.append("pkg: " + pkg + ",\n");
        sb.append("updatetime: " + String.valueOf(updatetime) + ",\n");
        sb.append("delay: " + String.valueOf(delay) + ",\n");
        sb.append("emails: " + emails + ",\n");
        sb.append("cellphones: " + cellphones + "\n");
        sb.append("args_script: " + args_script + "\n");
        sb.append("retry: " + retry + "\n");
        String tmpscript = script.length() > 100 ? script.substring(0, 100) + "..." : script;
        sb.append("script: " + tmpscript + "\n");
        sb.append("num_workers: " + num_workers + "\n");
        sb.append("}\n");

        return sb.toString();
    }

    public void setKeyValue(String key, Object obj) {
        if (key.equals("id")) {
            this.setId((String) obj);
        } else if (key.equals("name")) {
            this.setName((String) obj);
        } else if (key.equals("status")) {
            this.setStatus(obj);
        } else if (key.equals("crontab")) {
            this.setCrontab((String) obj);
            if (this.getCrontab() != null) {
                try {
                    this.setCron(new Crontab(this.getCrontab()));
                } catch (ParseException e) {
                    this.setCron(null);
                }
            }
        } else if (key.equals("dependence")) {
            this.setDependence((String) obj);
        } else if (key.equals("group")) {
            this.setGroup((String) obj);
        } else if (key.equals("type")) {
            this.setType(obj);
        } else if (key.equals("user")) {
            this.setUser((String) obj);
        } else if (key.equals("pkg")) {
            this.setPkg((String) obj);
        } else if (key.equals("updatetime")) {
            this.setUpdatetime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("delay")) {
            this.setDelay(obj == null ? 0 : (Long) obj);
        } else if (key.equals("emails")) {
            this.setEmails((String) obj);
        } else if (key.equals("cellphones")) {
            this.setCellphones((String) obj);
        } else if (key.equals("args_script")) {
            this.setArgs_script((String) obj);
        } else if (key.equals("retry")) {
            this.setRetry(obj == null ? 0 : (Integer) obj);
        } else if (key.equals("script")) {
            this.setScript((String) obj);
        } else if (key.equals("num_workers")) {
            this.setNum_workers(obj == null ? 1 : (Integer) obj);
            if (this.getNum_workers() == 0) {
                this.setNum_workers(1);
            }
        }
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

    public void setStatus(Object value) {
        this.status = Status.valueOf(value == null ? -1 : (Integer) value);
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(Object value) {
        this.type = Type.valueOf(value == null ? -1 : (Integer) value);
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

    public Double getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Double updatetime) {
        this.updatetime = updatetime;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
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

    public String getArgs_script() {
        return args_script;
    }

    public void setArgs_script(String args_script) {
        this.args_script = args_script;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public Crontab getCron() {
        return cron;
    }

    public void setCron(Crontab cron) {
        this.cron = cron;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Status get_sysStatus() {
        return _sysStatus;
    }

    public void set_sysStatus(Status _sysStatus) {
        this._sysStatus = _sysStatus;
    }

    public Integer getNum_workers() {
        return num_workers;
    }

    public void setNum_workers(Integer num_workers) {
        this.num_workers = num_workers;
    }
}
