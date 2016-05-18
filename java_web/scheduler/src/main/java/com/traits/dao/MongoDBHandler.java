package com.traits.dao;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BsonField;
import com.mongodb.util.ObjectSerializer;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YeFeng on 2016/5/18.
 */
public class MongoDBHandler {

    MongoClient client = null;
    MongoDatabase db = null;

    public MongoDBHandler(String host, int port, String database, String user, String passwd)
            throws MongoException
    {
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

    public List<Document> find(String collection) {
        List<Document> result = new ArrayList<Document>();
        MongoCollection col = db.getCollection(collection);
        FindIterable<Document> cursor = col.find();
        for (Document doc: cursor) {
            result.add(doc);
        }
        return result;
    }

    public List<Document> find(String collection, Bson filter) {
        List<Document> result = new ArrayList<Document>();
        MongoCollection col = db.getCollection(collection);
        FindIterable<Document> cursor = col.find(filter);
        for (Document doc: cursor) {
            result.add(doc);
        }
        return result;
    }

    public boolean updateMany(String collection, ArrayList<Document> docs) {
        return true;
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
    }

}
