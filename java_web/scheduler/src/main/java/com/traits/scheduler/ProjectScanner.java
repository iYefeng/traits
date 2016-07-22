package com.traits.scheduler;

import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.model.Configure;
import com.traits.storage.BaseStorage;
import com.traits.storage.MongoDBStorage;
import com.traits.storage.MySQLStorage;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by YeFeng on 2016/5/18.
 */
public class ProjectScanner {

    private final static ProjectScanner singleton = new ProjectScanner();
    private String dbtype, host, database, table, user, passwd;
    private int port;
    Logger logger = Logger.getLogger("scheduler");

    private Map<String, BaseProject> _projectMap = new HashMap<String, BaseProject>();


    private ProjectScanner() {
        Configure conf = Configure.getSingleton();
        dbtype = conf.dbtype;
        host = conf.host;
        port = conf.port;
        database = conf.database;
        user = conf.user;
        passwd = conf.passwd;
    }

    public static ProjectScanner getInstance() {
        return singleton;
    }

    public boolean loadProjects() {
        logger.info(">> loading projects");
        BaseStorage _storage = null;

        try {
            if (dbtype.equals("mysql")) {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            } else if (dbtype.equals("mongodb")) {
                _storage = new MongoDBStorage(host, port, database, user, passwd);
            } else {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            }

            for (Map.Entry<String, BaseProject> p: _projectMap.entrySet()) {  //// set project to delete
                p.getValue().set_sysStatus(BaseProject.Status.DELETE);
            }

            ArrayList<BaseProject> projects_tmp = _storage.getProjects();
            for (BaseProject tmp : projects_tmp) {
                String projectId = tmp.getId();
                if (_projectMap.containsKey(projectId)) {                    ///// for old project
                    BaseProject sysProject = _projectMap.get(projectId);
                    if (sysProject.getUpdatetime() < tmp.getUpdatetime()) {  // modified project
                        sysProject.copy(tmp);
                        sysProject.set_sysStatus(BaseProject.Status.MODIFIED);
                    } else {                                                 // unmodified project
                        sysProject.set_sysStatus(BaseProject.Status.DONE);
                    }
                } else {                                                     ///// for new project
                    BaseProject newproject = new BaseProject();
                    newproject.copy(tmp);
                    newproject.set_sysStatus(BaseProject.Status.ADDED);
                    _projectMap.put(newproject.getId(), newproject);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return false;
        }

        _storage.release();
        logger.info("<< loading projects");
        return true;
    }

    public Map<String, BaseProject> get_projectMap() {
        return _projectMap;
    }

}
