package com.cloudburo.test.base;

import java.util.HashMap;

import com.cloudburo.test.db.PersistentStoreManager;

public class BaseTestDriver {
	
	protected static PersistentStoreManager persistentStoreManager;
	
	public void initalizePersistentStoreManager (String storeendpoint,String storeport,String storeinstance,
			  String storeuser,String storepassword, String storemanager) throws Exception {
		HashMap<String,String> props = new HashMap<String,String>();
		  props.put(PersistentStoreManager.STORE_ENDPOINT, storeendpoint);
		  props.put(PersistentStoreManager.STORE_PORT, storeport);
		  props.put(PersistentStoreManager.STORE_INSTANCENAME, storeinstance);
		  props.put(PersistentStoreManager.STORE_USER, storeuser);
		  props.put(PersistentStoreManager.STORE_PASSWORD, storepassword);
		  
		  // Connect Test Data
		  persistentStoreManager = (PersistentStoreManager) Class.forName(storemanager).newInstance();
		  persistentStoreManager.connectDB(props);
	}

}
