package com.traits.scheduler;

import com.traits.db.RedisHandler;
import com.traits.model.BaseProject;
import com.traits.model.BaseTask;
import com.traits.model.Configure;
import com.traits.model.TaskCache;
import com.traits.storage.BaseStorage;
import com.traits.storage.MongoDBStorage;
import com.traits.storage.MySQLStorage;
import com.traits.util.SerializeUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by YeFeng on 2016/7/17.
 */

@DisallowConcurrentExecution
public class TaskTrigger implements Job {

    Logger logger = Logger.getLogger("scheduler");
    private String dbtype, host, database, user, passwd, redis_host, redis_db;
    private int port, redis_port;

    private static RedisHandler redis_handler = null;

    private HashMap<String, BaseProject> _projectMap;
    private HashMap<String, BaseTask> _initTaskMap;
    private HashMap<String, BaseTask> _checkingTaskMap;
    private HashMap<String, BaseTask> _activeTaskMap;
    private HashSet<String> _successOrPassedTaskSet;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    private Long triggerDelta = 1L * 60 * 10;
    private Long ignore = 7L * 60 * 60 * 24;

    private Pattern depReg = Pattern.compile("(day|hour|minute|second)\\((0|-\\d+)\\)((-|\\+)\\d+)*,\\s*(\\d+)");

    public TaskTrigger() {
        Configure conf = Configure.getSingleton();
        dbtype = conf.dbtype;
        host = conf.host;
        port = conf.port;
        database = conf.database;
        user = conf.user;
        passwd = conf.passwd;

        triggerDelta = conf.triggerDelta;
        ignore = conf.ignore;

        redis_host = conf.redis_host;
        redis_port = conf.redis_port;
        redis_db = conf.redis_db;

        if (redis_handler == null) {
            try {
                redis_handler = new RedisHandler(redis_host, redis_port, redis_db);
                logger.debug("Connected Redis");
            } catch (Exception e) {
                logger.error("Redis init error");
                e.printStackTrace();
            }
        }
    }

    public void loadProjects(BaseStorage _storage) throws Exception {
        _projectMap = new HashMap<String, BaseProject>();
        for (BaseProject p : _storage.getProjects()) {
            _projectMap.put(p.getId(), p);
        }
    }

    public void loadInitTasks(BaseStorage _storage) throws Exception {
        _initTaskMap = new HashMap<String, BaseTask>();
        for (BaseTask t : _storage.getInitTasks()) {
            _initTaskMap.put(t.getId(), t);
        }
    }

    public void loadCheckingTasks(BaseStorage _storage) throws Exception {
        _checkingTaskMap = new HashMap<String, BaseTask>();
        for (BaseTask t : _storage.getCheckingTasks()) {
            _checkingTaskMap.put(t.getId(), t);
        }
    }

    public void loadActiveTasks(BaseStorage _storage) throws Exception {
        _activeTaskMap = new HashMap<String, BaseTask>();
        for (BaseTask t : _storage.getActiveTasks()) {
            _activeTaskMap.put(t.getId(), t);
        }
    }

    public void loadSuccessOrPassedTask(BaseStorage _storage) throws Exception {
        _successOrPassedTaskSet = _storage.getSuccessOrPassedTasks();
    }

    public static Date day(double current, int delta) {
        double day = ((long) (current / (86400.0))) * (86400L);
        double baseTimeStamp = day = delta * 86400;
        return new Date((long) (baseTimeStamp * 1000));
    }

    public static Date hour(double current, int delta) {
        double hour = ((long) (current / (3600.0))) * (3600);
        double baseTimeStamp = hour + delta * 3600;
        return new Date((long) (baseTimeStamp * 1000));
    }

    public static Date minute(double current, int delta) {
        double minute = ((long) (current / (60.0))) * (60);
        double baseTimeStamp = minute + delta * 60;
        return new Date((long) (baseTimeStamp * 1000));
    }

    public static Date second(double current, int delta) {
        double second = ((long) current);
        double baseTimeStamp = second + delta;
        return new Date((long) (baseTimeStamp * 1000));
    }


    public ArrayList<String> parseDependence(BaseProject _self,
                                             HashMap<String, BaseProject> _projectMap,
                                             Date lunchDate) {
        ArrayList<String> dep = new ArrayList<String>();

        // projectId   basetime, count
        // task.test1:[(day(-3)+2345, 12),(hour(-2), 1),(minute(0), 3)];

        String depStr = _self.getDependence();
        String[] deps = StringUtils.split(depStr.replace(" ", "").replace("\r\n|\r|\n", ""), ";");
        for (String each : deps) {
            String[] tmp = StringUtils.split(each.trim(), ":");
            if (tmp.length == 2) {
                String pid = tmp[0];
                double lunchTimeStamp = lunchDate.getTime() / 1000.0;
                String tmp1;
                if (tmp[1].startsWith("[(") && tmp[1].endsWith(")]")) {
                    tmp1 = tmp[1].substring(2, tmp[1].length() - 2);
                } else if (tmp[1].startsWith("(") && tmp[1].endsWith(")")) {
                    tmp1 = tmp[1].substring(1, tmp[1].length() - 1);
                } else {
                    logger.error("dependence is parsed error");
                    continue;
                }
                String[] tmp2 = tmp1.replace("),(", "\1").split("\1");
                for (String rule : tmp2) {
                    java.util.regex.Matcher mat = depReg.matcher(rule);
                    if (mat.find()) {
                        try {
                            String fun = mat.group(1);
                            int delta = Integer.valueOf(mat.group(2) == null ? "0" : mat.group(2));
                            int offset = Integer.valueOf(mat.group(3) == null ? "0" : mat.group(3));
                            int count = Integer.valueOf(mat.group(5));
                            Method method = this.getClass().getMethod(fun, double.class, int.class);
                            Date base = (Date) method.invoke(null, lunchTimeStamp, delta);
                            if (_projectMap.containsKey(pid)) {
                                BaseProject p = _projectMap.get(pid);
                                Date tmpd2 = new Date(base.getTime() + offset * 1000);
                                if (tmpd2.getTime() > lunchDate.getTime()) {
                                    logger.warn("basetime is not allowed before lunchtime");
                                    tmpd2 = lunchDate;
                                }
                                ArrayList<Date> reqDate = p.getCron().getTimeBefore(tmpd2, count);
                                for (Date tmpdate : reqDate) {
                                    for (int j = 0; j < p.getNum_workers(); ++j) {
                                        String taskName = String.format("%s @ %s", p.getId(), df.format(tmpdate));
                                        dep.add(DigestUtils.md5Hex(taskName) + "#" + String.valueOf(j) );
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("dependence is parsed error");
                            logger.error(e.getMessage());
                            e.printStackTrace();
                            continue;
                        }
                    } else {
                        logger.error(String.format("dependence %s is parsed error", rule));
                        continue;
                    }
                }
            }
        }
        return dep;
    }

    public boolean checkTask(HashSet<String> _successOrPassedTaskSet,
                             HashMap<String, BaseProject> _projectMap,
                             BaseProject project,
                             BaseTask task,
                             Date lunchDate) {
        if (project.getDependence() == null || project.getDependence().equals("")) {
            task.setDependence_finish_rate(1.0);
            return true;
        }
        ArrayList<String> requireTasks = parseDependence(project, _projectMap, lunchDate);

        for (String s : requireTasks) {
            logger.debug("requre Task id: " + s);
        }

        int total = requireTasks.size();
        int matched = 0;
        for (String req : requireTasks) {
            if (_successOrPassedTaskSet.contains(req)) {
                matched += 1;
            }
        }

        if (total == matched) {
            task.setDependence_finish_rate(1.0);
            return true;
        } else {
            task.setDependence_finish_rate( ((double) matched) / total);
            return false;
        }
    }

    public void initLoad() {
        logger.info(">> TaskTrigger initLoad Task");
        BaseStorage _storage = null;
        TaskCache tc = TaskCache.getInstance();
        try {
            if (dbtype.equals("mysql")) {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            } else if (dbtype.equals("mongodb")) {
                _storage = new MongoDBStorage(host, port, database, user, passwd);
            } else {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            }

            loadCheckingTasks(_storage);
            loadActiveTasks(_storage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return;
        }

        TreeMap<Double, BaseTask> taskCacheMap = tc.get_taskMap();
        for (BaseTask task : _checkingTaskMap.values()) {
            taskCacheMap.put(task.getTriggertime(), task);
        }

//        for (BaseTask task : _activeTaskMap.values()) {
//            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
//            try {
//                _storage.saveOneTask(task);
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//            }
//            redis_handler.getHandler().rpush(
//                    "scheduler.task.queue".getBytes(),
//                    SerializeUtil.serialize(task));
//        }


        _storage.release();
        logger.info("<< TaskTrigger initLoad Task");
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info(">> TaskTrigger execute");
        BaseStorage _storage = null;
        TaskCache tc = TaskCache.getInstance();

//        try {
//            redis_handler = new RedisHandler(redis_host, redis_port, redis_db);
//            System.out.println(redis_handler.getHandler().get("test"));
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            e.printStackTrace();
//        }

        try {
            if (dbtype.equals("mysql")) {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            } else if (dbtype.equals("mongodb")) {
                _storage = new MongoDBStorage(host, port, database, user, passwd);
            } else {
                _storage = new MySQLStorage(host, port, database, user, passwd);
            }

            loadProjects(_storage);
            loadInitTasks(_storage);
            loadSuccessOrPassedTask(_storage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return;
        }

        for (Map.Entry<String, BaseTask> kv : _initTaskMap.entrySet()) {
            BaseTask t = kv.getValue();
            String pid = t.getProject_id();
            if (_projectMap.containsKey(pid)) {
                BaseProject p = _projectMap.get(pid);
                if ( (((long)((t.getLunchtime() + p.getDelay()) * 1000)) < (new Date()).getTime()
                        && checkTask(_successOrPassedTaskSet, _projectMap, p, t, new Date((new Double(t.getLunchtime() * 1000)).longValue())))

                        || t.getStatus() == BaseTask.Status.FORCERUNNING) {

                    // the task satifies dependence
                    t.setStatus(BaseTask.Status.ACTIVE);
                    t.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    try {
                        _storage.saveOneTask(t);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    if (redis_handler != null) {
                        redis_handler.getHandler().rpush(
                                "scheduler.task.queue".getBytes(),
                                SerializeUtil.serialize(t));
                    }
                } else {
                    // task does not satifies dependence
                    t.setStatus(BaseTask.Status.CHECKING);
                    t.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    t.setTriggertime((((new Date()).getTime() + triggerDelta * 1000) / 1000.0 + p.getDelay()));
                    try {
                        _storage.saveOneTask(t);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    tc.get_taskMap().put(t.getTriggertime(), t);
                }
            }
        }

        TreeMap<Double, BaseTask> tset = tc.get_taskMap();

        while (!tset.isEmpty()) {
            Map.Entry<Double, BaseTask> t = tset.firstEntry();
            Double triggerTime = t.getKey();
            BaseTask task = t.getValue();

            if (triggerTime < ((new Date()).getTime() / 1000.0)) {
                String pid = task.getProject_id();
                if (_projectMap.containsKey(pid)) {
                    BaseProject p = _projectMap.get(pid);
                    if (p.getStatus() == BaseProject.Status.RUNNING
                            || p.getStatus() == BaseProject.Status.DEBUG) {   // running or debug
                        if (checkTask(_successOrPassedTaskSet, _projectMap, p, task,
                                new Date((new Double(task.getLunchtime() * 1000)).longValue()))){
                            // the task satifies dependence
                            task.setStatus(BaseTask.Status.ACTIVE);
                            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                            try {
                                _storage.saveOneTask(task);
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                e.printStackTrace();
                            }
                            // tc.get_taskMap().pollFirstEntry();

                            if (redis_handler != null) {
                                redis_handler.getHandler().rpush(
                                        "scheduler.task.queue".getBytes(),
                                        SerializeUtil.serialize(task));
                            }
                            tset.pollFirstEntry();
                        } else {                                // does not satify dependence

                            task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                            task.setTriggertime(((double) (new Date()).getTime() + triggerDelta * 1000) / 1000.0);

                            tset.pollFirstEntry();

                            // TODO check task if it exists longer than 7 days
                            if ((long)((task.getLunchtime() + ignore) * 1000L) > (new Date()).getTime()) {
                                task.setStatus(BaseTask.Status.CHECKING);
                                tset.put(task.getTriggertime(), task);
                            } else {
                                task.setStatus(BaseTask.Status.IGNORED);
                            }

                            try {
                                _storage.saveOneTask(task);
                            } catch (Exception e) {
                                logger.error(e.getMessage());
                                e.printStackTrace();
                            }

                        }
                    } else {                                    // stop delete...
                        if (p.getStatus() == BaseProject.Status.DELETE) {
                            task.setStatus(BaseTask.Status.DELETE);
                        } else {
                            task.setStatus(BaseTask.Status.STOP);
                        }
                        task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                        task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                        try {
                            _storage.saveOneTask(task);
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                            e.printStackTrace();
                        }
                        tset.pollFirstEntry();
                    }
                } else {                                        // delete
                    task.setStatus(BaseTask.Status.DELETE);
                    task.setUpdatetime(((double) (new Date()).getTime()) / 1000.0);
                    task.setEndtime(((double) (new Date()).getTime()) / 1000.0);
                    try {
                        _storage.saveOneTask(task);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        e.printStackTrace();
                    }
                    tset.pollFirstEntry();
                }
            } else {                                            // time don't arrive
                break;
            }
        }

        _storage.release();
        logger.info("<< TaskTrigger execute");
    }
}
