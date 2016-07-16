package com.traits.model;

import org.apache.log4j.Logger;

/**
 * Created by YeFeng on 2016/5/18.
 */
public class BaseTask {

    Logger logger = Logger.getLogger("scheduler");

    public enum Status {
        UNKNOWN(-1), INIT(0), ACTIVE(1), RUNNING(2), SUCCESS(3), FAIL(4), STOP(5), DELETE(6);

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
    private String project_id;
    private Status status;
    private double updatetime;
    private double starttime;
    private double endtime;
    private String args;
    private String log;
    private double dependence_finish_rate;
    private double triggertime;




    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("task info {\n");
        sb.append("id: " + id + ",\n");
        sb.append("name: " + name + ",\n");
        sb.append("projectId: " + project_id + ",\n");
        sb.append("status:" + status + ",\n");
        sb.append("updatetime: " + updatetime + ",\n");
        sb.append("starttime: " + starttime + ",\n");
        sb.append("endtime: " + endtime + ",\n");
        sb.append("args: " + args + ",\n");
        String tmplog = log.length() > 100 ? log.substring(0, 100) + "..." : log;
        sb.append("log:" + tmplog + ",\n");
        sb.append("dependence_finish_rate: " + dependence_finish_rate + ",\n");
        sb.append("triggertime: " + triggertime + ",\n");
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

    public double getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(double updatetime) {
        this.updatetime = updatetime;
    }

    public double getStarttime() {
        return starttime;
    }

    public void setStarttime(double starttime) {
        this.starttime = starttime;
    }

    public double getEndtime() {
        return endtime;
    }

    public void setEndtime(double endtime) {
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

    public double getDependence_finish_rate() {
        return dependence_finish_rate;
    }

    public void setDependence_finish_rate(double dependence_finish_rate) {
        this.dependence_finish_rate = dependence_finish_rate;
    }

    public double getTriggertime() {
        return triggertime;
    }

    public void setTriggertime(double triggertime) {
        this.triggertime = triggertime;
    }
}
