package com.cloudburo.test.db;

import java.util.HashMap;

import org.bson.BSONObject;

import com.cloudburo.test.domain.DomainDBObject;

public interface PersistentStoreManager {
	

	public static final String STORE_ENDPOINT="ENDPOINT";
	public static final String STORE_PORT="PORT";
	public static final String STORE_INSTANCENAME="INSTANCENAME";
	public static final String STORE_USER="USER";
	public static final String STORE_PASSWORD="PASSWORD";
	
	public void connectDB(HashMap<String,String> connectionProps) throws Exception;
	public BSONObject getCollectionObject(String collection, String id) throws Exception;
	public long countCollectionObjects(String collection) throws Exception;
	public void loadTestDataSet( String dataset, DomainDBObject dbo) throws Exception;
	
}
