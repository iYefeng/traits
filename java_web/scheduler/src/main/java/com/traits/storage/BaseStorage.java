package com.traits.storage;

import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by YeFeng on 2016/7/16.
 */
public class BaseStorage {

    public BaseStorage() {}

    public void release() {}

    public BaseStorage(String host, int port, String database, String user, String passwd) {}

    public ArrayList<BaseProject> getProjects() throws Exception {
        return new ArrayList<BaseProject>();
    }

    public ArrayList<BaseProject> getProjects(String[] fields) throws SQLException {
        return new ArrayList<BaseProject>();
    }

    public BaseProject getProjectById(String pid) throws SQLException {
        return new BaseProject();
    }

    public ArrayList<BaseTask> getInitTasks() throws Exception {
        return new ArrayList<BaseTask>();
    }

    public ArrayList<BaseTask> getCheckingTasks() throws Exception {
        return new ArrayList<BaseTask>();
    }

    public ArrayList<BaseTask> getActiveTasks() throws Exception {
        return new ArrayList<BaseTask>();
    }

    public HashSet<String> getSuccessOrPassedTasks() throws Exception {
        return new HashSet<String>();
    }

    public boolean saveOneTask(BaseTask task) throws Exception {
        return false;
    }


}
