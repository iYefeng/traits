package com.traits.db;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * Created by YeFeng on 2016/7/21.
 */
public class RedisHandler {
    private Jedis handler;

    Logger logger = Logger.getLogger("scheduler");

    public RedisHandler(String host, Integer port, String db) throws Exception {
        if (port != null) {
            handler = new Jedis(host, port);
        } else {
            handler = new Jedis(host);
        }
        if (db != null && Integer.valueOf(db) > 0) {
            handler.select(Integer.valueOf(db));
        }
    }

    public Jedis getHandler() {
        return handler;
    }

    public void setHandler(Jedis handler) {
        this.handler = handler;
    }
}
