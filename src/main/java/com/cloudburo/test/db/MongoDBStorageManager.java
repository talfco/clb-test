/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013 Felix Kuestahler <felix@cloudburo.com> http://cloudburo.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.cloudburo.test.db;

import java.util.HashMap;
import java.util.Iterator;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.cloudburo.test.load.CSVTestDataLoader;
import com.cloudburo.test.base.BaseDBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Felix Kuestahler / Cloudburo.ch
 * The MongoDBStorageManager will store the test data set in a nonSQL MongoDB
 */
public class MongoDBStorageManager implements PersistentStoreManager {
	
	private static final Logger logger = Logger.getLogger(MongoDBStorageManager.class.getCanonicalName());
	
	private DB db;
	
	public void connectDB(HashMap<String,String> connectionProps) throws Exception {	
		Mongo mongo = new Mongo(connectionProps.get(PersistentStoreManager.STORE_ENDPOINT), 
				Integer.parseInt(connectionProps.get(PersistentStoreManager.STORE_PORT)));
		db = mongo.getDB(connectionProps.get(PersistentStoreManager.STORE_INSTANCENAME));
		boolean auth = db.authenticate(connectionProps.get(PersistentStoreManager.STORE_USER), connectionProps.get(PersistentStoreManager.STORE_PASSWORD).toCharArray());
		if (!auth) {
			throw new Exception("Authentication to Mongo DB failed");
		}
	}	

	public void loadTestDataSet( String dataset, BaseDBObject dbo) throws Exception{
		DBCollection coll = db.getCollection(dbo.getDBCollectionName());
		coll.drop();
		coll = db.getCollection(dbo.getDBCollectionName());
		CSVTestDataLoader csvloader = new com.cloudburo.test.load.CSVTestDataLoader();
	
		Iterator<BSONObject> iter = csvloader.loadTestDataSet(dataset,dbo).iterator();
		while (iter.hasNext())
			coll.insert((DBObject)iter.next() );
        
		// lets get all the documents in the collection and print them out
		DBCursor cur = coll.find();
		while (cur.hasNext()) { logger.log(Level.INFO, "Inserted {0}",cur.next());}
	}
	
	public BSONObject getCollectionObject(String collection, String id) throws Exception {
		DBCollection coll = db.getCollection(collection);
		BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBCursor cur = coll.find(query);
        if (!cur.hasNext()) return null;
        return cur.next();
	}
	
	public long countCollectionObjects(String collection) throws Exception {
		DBCollection coll = db.getCollection(collection);
		return coll.getCount();
	}
}
