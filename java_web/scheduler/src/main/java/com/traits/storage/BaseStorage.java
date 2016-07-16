package com.traits.storage;

import com.traits.model.BaseProject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YeFeng on 2016/7/16.
 */
public class BaseStorage {

    public BaseStorage() {}

    public BaseStorage(String host, int port, String database, String user, String passwd) {}

    public ArrayList<BaseProject> getProjects() throws SQLException {
        return new ArrayList<BaseProject>();
    }


}
