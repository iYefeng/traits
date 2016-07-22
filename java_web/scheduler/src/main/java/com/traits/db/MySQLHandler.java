package com.traits.db;

import java.sql.*;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by YeFeng on 2016/7/16.
 */
public class MySQLHandler {
    private Connection mDBConnection;
    private Statement mStatement;
    private PreparedStatement mPreparedStatement;
    private Logger logger;

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public int getCurrentCount() {
        return currentCount;
    }

    private int currentCount = 0;

    public MySQLHandler(String host, int port, String database, String user, String passwd)
            throws SQLException{
        logger = Logger.getLogger("MySQLHandler");
        String databaseUrl = "jdbc:mysql://" +
                host +
                ":" +
                (port > 0 ? String.valueOf(port) : "3306") +
                "/" +
                database;

        Properties p = new Properties();
        if (user != null && passwd != null) {
            p.put("user", user);
            p.put("password", passwd);
        }
        p.put("useUnicode", "true");
        p.put("characterEncoding", "utf-8");
        mDBConnection = DriverManager.getConnection(databaseUrl, p);

        mStatement = mDBConnection.createStatement();
    }

    public void release(){
        try {
            if(mStatement != null) {
                mStatement.close();
                mStatement = null;
            }
            if(mPreparedStatement != null) {
                mPreparedStatement.close();
                mPreparedStatement = null;
            }
            if(mDBConnection != null) {
                mDBConnection.close();
                mDBConnection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public HashMap<String, ArrayList<Object>> query(String SQL) {
        return query(SQL, null);
    }

    public HashMap<String, ArrayList<Object>> query(String SQL, String[] args){
        HashMap<String, ArrayList<Object>> result = null;
        currentCount = 0;
        String sqlStatement;

        if (args != null) {
            sqlStatement = String.format(SQL, args);
        } else {
            sqlStatement = SQL;
        }

        logger.debug(sqlStatement);

        try {
            mDBConnection.setAutoCommit(true);
            ResultSet resultSet = mStatement.executeQuery(sqlStatement);

            if(resultSet != null) {
                ArrayList<HashMap<String,Object>> dataList = new ArrayList<HashMap<String, Object>>();
                while(resultSet.next()) {
                    HashMap<String, Object> rowData = new HashMap<String, Object>();
                    ResultSetMetaData metadata = resultSet.getMetaData();
                    String column;
                    for(int i = 1; i <= metadata.getColumnCount(); ++i) {
                        column = metadata.getColumnLabel(i);
                        Object value = resultSet.getObject(i);
                        rowData.put(column, value);
                    }
                    dataList.add(rowData);
                }

                if(dataList.size() > 0) {
                    result = new HashMap<String, ArrayList<Object>>();

                    for(HashMap<String, Object> dataItem : dataList) {
                        currentCount += 1;
                        for(String key : dataItem.keySet()){
                            ArrayList<Object> list = result.get(key);
                            if(list == null) {
                                list = new ArrayList<Object>();
                                result.put(key, list);
                            }

                            Object tmp = dataItem.get(key);
                            if (tmp instanceof Timestamp) {
                                tmp = (double)((Timestamp) tmp).getTime() / 1000.0;
                            } else if (tmp instanceof java.sql.Date) {
                                tmp = (double)((java.sql.Date) tmp).getTime() / 1000.0;
                            }

                            list.add(tmp);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public boolean executeMany(String SQL, ArrayList<ArrayList<Object>> dataList) {
        return executeMany(SQL, null, dataList);
    }

    public boolean executeMany(String SQL, String[] args, ArrayList<ArrayList<Object>> dataList) {
        boolean success = false;
        String sqlStatement;

        if (args != null) {
            sqlStatement = String.format(SQL, args);
        } else {
            sqlStatement = SQL;
        }

        logger.debug(sqlStatement);


        try {
            mDBConnection.setAutoCommit(false);
            mPreparedStatement = mDBConnection.prepareStatement(sqlStatement);

            for(ArrayList<Object> values : dataList) {
                logger.debug(mPreparedStatement.toString());
                for(int i = 0; i < values.size(); i++) {
                    if (values.get(i) instanceof Date) {
                        String tsStr = df.format(values.get(i));
                        mPreparedStatement.setObject(i + 1, tsStr);
                    } else {
                        mPreparedStatement.setObject(i + 1, values.get(i));
                    }
                }
                mPreparedStatement.addBatch();
            }
            logger.debug(mPreparedStatement.toString());


            int[] batchResult = mPreparedStatement.executeBatch();
            mDBConnection.commit();

            if(batchResult.length == dataList.size()) {
                success = true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return success;

    }

    public static void main(String[] args) throws SQLException {
        MySQLHandler my = new MySQLHandler("127.0.0.1", 3306, "scheduler", "dev", "123456");
        HashMap<String, ArrayList<Object>> ret = my.query("SELECT %s, %s from %s where id='%s'",
                new String[]{
                        "id",
                        "name",
                        "projectdb",
                        "123"
                });

        if (ret != null) {
            Set<String> keys =  ret.keySet();
            for (int i = 0; i < my.currentCount; ++i) {
                for (String j : keys) {
                    String key = j;
                    String value = String.valueOf(ret.get(j).get(i));
                    System.out.println(key + ":" + value);
                }
                System.out.println();
            }
        }

        ArrayList<ArrayList<Object>> dataList = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> tmp = new ArrayList<Object>();
        tmp.add(12);
        dataList.add(tmp);
        tmp = new ArrayList<Object>();
        tmp.add("1");
        dataList.add(tmp);

        System.out.println(my.executeMany("DELETE FROM %s where %s=? ", new String[]{"projectdb", "id"}, dataList));


    }


}
