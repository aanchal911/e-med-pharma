package com.emedpharma.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "emedpharma";
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
        }
        return database;
    }
    
    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}