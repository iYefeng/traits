package com.traits.storage;

import com.traits.model.BaseProject;
import com.traits.model.BaseTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeFeng on 2016/7/16.
 */
public class BaseStorage {

    public BaseStorage() {}

    public BaseStorage(String host, int port, String database, String user, String passwd) {}

    public ArrayList<BaseProject> getProjects() throws Exception {
        return new ArrayList<BaseProject>();
    }

    public ArrayList<BaseTask> getInitTasks() throws Exception {
        return new ArrayList<BaseTask>();
    }

    public ArrayList<BaseTask> getSuccessOrPassedTasks() throws Exception {
        return new ArrayList<BaseTask>();
    }

    public boolean saveOneTask(BaseTask task) throws Exception {
        return false;
    }


}
