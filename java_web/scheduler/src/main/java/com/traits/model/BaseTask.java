package com.traits.model;

import com.traits.db.MySQLHandler;
import com.traits.scheduler.SysScheduler;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Created by YeFeng on 2016/5/18.
 *
 table
 CREATE TABLE `taskdb` (
 `id` varchar(32) NOT NULL DEFAULT '' COMMENT 'uuid',
 `name` varchar(128) DEFAULT NULL,
 `lunchtime` timestamp NULL DEFAULT NULL,
 `project_id` varchar(100) DEFAULT NULL,
 `status` tinyint(4) DEFAULT NULL COMMENT '0-init; 1-active; 2-running; 3-success; 4-fail; 5-stop; 6-delete',
 `updatetime` timestamp NULL DEFAULT NULL,
 `starttime` timestamp NULL DEFAULT NULL,
 `endtime` timestamp NULL DEFAULT NULL,
 `args` varchar(1024) DEFAULT NULL COMMENT 'json {"lunchTime": 123456789} by default',
 `log` varchar(10240) DEFAULT NULL,
 `dependence_finish_rate` decimal(10,5) DEFAULT NULL,
 `triggertime` timestamp NULL DEFAULT NULL,
 `retry_count` tinyint(4) DEFAULT NULL,
 PRIMARY KEY (`id`),
 KEY `idx_project_id` (`project_id`),
 KEY `idx_status` (`status`),
 KEY `idx_starttime` (`starttime`),
 KEY `idx_name` (`name`),
 KEY `idx_lunchtime` (`lunchtime`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 */
public class BaseTask implements Callable<Integer>, Serializable {

    static final Logger logger = Logger.getLogger("scheduler");

    public Integer call() throws Exception {
        System.out.println(String.format("\n*** Running task %s\n", name));
        for (int i = 0; i < 5; ++i) {
            Thread.sleep(1000);
            System.out.println(String.format("Sleeping Task %s", name));
        }

        System.out.println(String.format("\nRunning task %s\n", name));
        return 0;
    }


    public enum Status {
        UNKNOWN(-1), INIT(0), ACTIVE(1), RUNNING(2), SUCCESS(3),
        FAIL(4), STOP(5), DELETE(6), CHECKING(7), PASSED(8), FORCERUNNING(9),
        IGNORED(10);

        private int value = 0;

        private Status(int value) {    //    必须是private的，否则编译错误
            this.value = value;
        }

        public static Status valueOf(int value) {    //    手写的从int到enum的转换函数
            switch (value) {
                case 0:
                    return INIT;
                case 1:
                    return ACTIVE;
                case 2:
                    return RUNNING;
                case 3:
                    return SUCCESS;
                case 4:
                    return FAIL;
                case 5:
                    return STOP;
                case 6:
                    return DELETE;
                case 7:
                    return CHECKING;
                case 8:
                    return PASSED;
                case 9:
                    return FORCERUNNING;
                case 10:
                    return IGNORED;
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
    private Double lunchtime;
    private String project_id;
    private Status status;
    private Double updatetime;
    private Double starttime;
    private Double endtime;
    private String args;
    private String log;
    private Double dependence_finish_rate;
    private Double triggertime;
    private Integer retry_count;
    private String working_node;

    private BaseProject _project;

    public Date transTimestamp(Double ts) {
        Long _timestamp;
        Date _date;
        if (ts != null && ts != 0.0) {
            _timestamp = (new Double(ts * 1000)).longValue();
            _date = new Date(_timestamp);
            return _date;
        } else return null;
    }


    public boolean saveTask(MySQLHandler handler) {
        ArrayList<ArrayList<Object>> dataList = new ArrayList<ArrayList<Object>>();

        StringBuffer sb = new StringBuffer();
        sb.append("REPLACE INTO `taskdb` (");
        sb.append("`id`, ");
        sb.append("`name`, ");
        sb.append("`lunchtime`, ");
        sb.append("`project_id`, ");
        sb.append("`status`, ");
        sb.append("`updatetime`, ");
        sb.append("`starttime`, ");
        sb.append("`endtime`, ");
        sb.append("`args`, ");
        sb.append("`log`, ");
        sb.append("`dependence_finish_rate`, ");
        sb.append("`triggertime`, ");
        sb.append("`retry_count`, ");
        sb.append("`working_node`");
        sb.append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        ArrayList<Object> tmp = new ArrayList<Object>();
        tmp.add(id);
        tmp.add(name);
        tmp.add(transTimestamp(lunchtime));
        tmp.add(project_id);
        tmp.add(status.value());
        tmp.add(transTimestamp(updatetime));
        tmp.add(transTimestamp(starttime));
        tmp.add(transTimestamp(endtime));
        tmp.add(args);
        tmp.add(log);
        tmp.add(dependence_finish_rate);
        tmp.add(transTimestamp(triggertime));
        tmp.add(retry_count);
        tmp.add(working_node);

        dataList.add(tmp);

        String SQL = sb.toString();
        return  handler.executeMany(SQL, dataList);
    }

    public static ArrayList<BaseTask> load(HashMap<String, ArrayList<Object>> map, int count) {
        ArrayList<BaseTask> tasks = new ArrayList<BaseTask>();

        for (int i = 0; i < count; ++i) {
            BaseTask tmp = new BaseTask();
            for (String key : map.keySet()) {
                Object obj = map.get(key).get(i);
                tmp.setKeyValue(key, obj);
            }
            tasks.add(tmp);
        }
        return tasks;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("task info {\n");
        sb.append("id: " + id + ",\n");
        sb.append("name: " + name + ",\n");
        sb.append("lunchtime: " + lunchtime + ",\n");
        sb.append("project_id: " + project_id + ",\n");
        sb.append("status:" + status + ",\n");
        sb.append("updatetime: " + updatetime + ",\n");
        sb.append("starttime: " + starttime + ",\n");
        sb.append("endtime: " + endtime + ",\n");
        sb.append("args: " + args + ",\n");
        String tmplog = log.length() > 100 ? log.substring(0, 100) + "..." : log;
        sb.append("log:" + tmplog + ",\n");
        sb.append("dependence_finish_rate: " + dependence_finish_rate + ",\n");
        sb.append("triggertime: " + triggertime + ",\n");
        sb.append("retry_count: " + retry_count + ",\n");
        sb.append("working_node: " + working_node + ",\n");
        sb.append("}\n");

        return sb.toString();
    }

    public void setKeyValue(String key, Object obj) {
        if (key.equals("id")) {
            this.setId((String) obj);
        } else if (key.equals("name")) {
            this.setName((String) obj);
        } else if (key.equals("lunchtime")) {
            this.setLunchtime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("project_id")) {
            this.setProject_id((String) obj);
        } else if (key.equals("status")) {
            this.setStatus(obj);
        } else if (key.equals("updatetime")) {
            this.setUpdatetime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("starttime")) {
            this.setStarttime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("endtime")) {
            this.setEndtime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("args")) {
            this.setArgs((String) obj);
        } else if (key.equals("log")) {
            this.setLog((String) obj);
        } else if (key.equals("dependence_finish_rate")) {
            if (obj instanceof BigDecimal) {
                this.setDependence_finish_rate(obj == null ? 0 : ((BigDecimal) obj).doubleValue());
            } else {
                this.setDependence_finish_rate(obj == null ? 0 : (Double) obj);
            }
        } else if (key.equals("triggertime")) {
            this.setTriggertime(obj == null ? 0 : (Double) obj);
        } else if (key.equals("retry_count")) {
            this.setRetry_count(obj == null ? 0 : (Integer) obj);
        } else if (key.equals("working_node")) {
            this.setWorking_node((String) obj);
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

    public Double getLunchtime() {
        return lunchtime;
    }

    public void setLunchtime(Double lunchtime) {
        this.lunchtime = lunchtime;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
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

    public Double getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Double updatetime) {
        this.updatetime = updatetime;
    }

    public Double getStarttime() {
        return starttime;
    }

    public void setStarttime(Double starttime) {
        this.starttime = starttime;
    }

    public Double getEndtime() {
        return endtime;
    }

    public void setEndtime(Double endtime) {
        this.endtime = endtime;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Double getDependence_finish_rate() {
        return dependence_finish_rate;
    }

    public void setDependence_finish_rate(Double dependence_finish_rate) {
        this.dependence_finish_rate = dependence_finish_rate;
    }

    public Double getTriggertime() {
        return triggertime;
    }

    public void setTriggertime(Double triggertime) {
        this.triggertime = triggertime;
    }

    public Integer getRetry_count() {
        return retry_count;
    }

    public void setRetry_count(Integer retry_count) {
        this.retry_count = retry_count;
    }

    public BaseProject get_project() {
        return _project;
    }

    public void set_project(BaseProject _project) {
        this._project = _project;
    }

    public String getWorking_node() {
        return working_node;
    }

    public void setWorking_node(String working_node) {
        this.working_node = working_node;
    }
}
