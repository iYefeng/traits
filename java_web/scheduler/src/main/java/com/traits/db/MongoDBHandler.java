package com.traits.db;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YeFeng on 2016/5/18.
 */
public class MongoDBHandler {

    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("MongoDBHandler");
    private MongoClient client = null;
    private MongoDatabase db = null;

    public MongoDBHandler(String host, int port, String database, String user, String passwd)
            throws MongoException {
        ServerAddress addrs = new ServerAddress(host, port);
        if (user == null) {
            user = "";
        }
        if (passwd == null) {
            passwd = "";
        }
        if (!user.isEmpty() && !passwd.isEmpty()) {
            MongoCredential credential = MongoCredential.createScramSha1Credential(
                    user,
                    database,
                    passwd.toCharArray()
            );
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            credentials.add(credential);
            client = new MongoClient(addrs, credentials);
        } else {
            client = new MongoClient(addrs);
        }

        // connect to database
        db = client.getDatabase(database);
    }

    public static void main(String[] args) {
        MongoDBHandler handler = new MongoDBHandler("192.168.238.200", 27017, "resultdb", null, null);
        Bson filter = new BasicDBObject().append("_id", new ObjectId("57371f53a7277f85daa71f94"));
        List<Document> docs = handler.find("QQ_TEST1", filter);
        for (Document doc : docs) {
            for (Map.Entry<String, Object> item : doc.entrySet()) {
                System.out.println(item.getKey() + ":" + String.valueOf(item.getValue()));
            }
        }
        ArrayList<Document> delete = new ArrayList<Document>();
        delete.add(new Document().append("_id", new ObjectId("57371f53a7277f85daa71f94")));
        boolean ret = handler.deleteMany("QQ_TEST1", null, delete);
        String a = "cc";
    }

    public List<Document> find(String collection) {
        List<Document> result = new ArrayList<Document>();
        MongoCollection col = db.getCollection(collection);
        FindIterable<Document> cursor;
        try {
            cursor = col.find();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
        for (Document doc : cursor) {
            result.add(doc);
        }
        return result;
    }

    public List<Document> find(String collection, Bson filter) {
        List<Document> result = new ArrayList<Document>();
        MongoCollection col = db.getCollection(collection);
        FindIterable<Document> cursor;
        try {
            cursor = col.find(filter);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
        for (Document doc : cursor) {
            result.add(doc);
        }
        return result;
    }

    public boolean insertMany(String collection, ArrayList<Document> docs) {
        InsertOneModel tmp = null;
        com.mongodb.bulk.BulkWriteResult result;
        ArrayList<InsertOneModel> insertList = new ArrayList<InsertOneModel>();
        MongoCollection col = db.getCollection(collection);
        for (Document doc : docs) {
            if (doc.containsKey("_id")) {
                doc.remove("_id");
            }
            tmp = new InsertOneModel(doc);
            insertList.add(tmp);
        }
        try {
            result = col.bulkWrite(insertList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return result.wasAcknowledged() && result.getInsertedCount() == docs.size();

    }

    public boolean insertOrUpdateMany(String collection, String filterKey, ArrayList<Document> docs) {
        UpdateOneModel tmp = null;
        com.mongodb.bulk.BulkWriteResult result;
        ArrayList<UpdateOneModel> updateList = new ArrayList<UpdateOneModel>();
        UpdateOptions opt = new UpdateOptions().upsert(true);
        MongoCollection col = db.getCollection(collection);
        if (filterKey == null) filterKey = "_id";
        for (Document doc : docs) {
            Object filterValue = doc.get(filterKey);
            Bson filter = new BasicDBObject().append(filterKey, filterValue);
            if (doc.containsKey("_id") && !filterKey.equals("_id")) {
                doc.remove("_id");
            }
            tmp = new UpdateOneModel(filter, doc, opt);
            updateList.add(tmp);
        }
        try {
            result = col.bulkWrite(updateList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return result.wasAcknowledged() &&
                (result.getMatchedCount() + result.getUpserts().size()) == docs.size();
    }

    public boolean deleteMany(String collection, String filterKey, ArrayList<Document> docs) {
        DeleteOneModel tmp = null;
        com.mongodb.bulk.BulkWriteResult result;
        ArrayList<DeleteOneModel> deleteList = new ArrayList<DeleteOneModel>();
        MongoCollection col = db.getCollection(collection);
        if (filterKey == null) filterKey = "_id";
        for (Document doc : docs) {
            Object filterValue = doc.get(filterKey);
            Bson filter = new BasicDBObject().append(filterKey, filterValue);
            tmp = new DeleteOneModel(filter);
            deleteList.add(tmp);
        }
        try {
            result = col.bulkWrite(deleteList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return result.wasAcknowledged();
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoClient getClient() {
        return client;
    }

}
