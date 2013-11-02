package com.cloudburo.test.db;

import org.bson.BSONObject;

public interface PersistentStoreManager {
	public void connectDB() throws Exception;
	public BSONObject getCollectionObject(String collection, String id) throws Exception;
	public long countCollectionObjects(String collection) throws Exception;
	
}
