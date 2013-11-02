package com.cloudburo.test;

import java.util.HashMap;

import com.cloudburo.test.db.PersistentStoreManager;

public class RootDriver {
	
	public PersistentStoreManager getPersistentStoreManager (String storeendpoint,String storeport,String storeinstance,
			  String storeuser,String storepassword, String storemanager) throws Exception {
		HashMap<String,String> props = new HashMap<String,String>();
		  props.put(PersistentStoreManager.STORE_ENDPOINT, storeendpoint);
		  props.put(PersistentStoreManager.STORE_PORT, storeport);
		  props.put(PersistentStoreManager.STORE_INSTANCENAME, storeinstance);
		  props.put(PersistentStoreManager.STORE_USER, storeuser);
		  props.put(PersistentStoreManager.STORE_PASSWORD, storepassword);
		  
		  // Connect Test Data
		  PersistentStoreManager dbcontroller = (PersistentStoreManager) Class.forName(storemanager).newInstance();
		  dbcontroller.connectDB(props);
		  return dbcontroller;

	}

}
