package com.cloudburo.test.db;

import java.util.Iterator;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import com.cloudburo.test.load.CSVTestDataLoader;
import com.cloudburo.test.dataobj.DomainDBObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MogoDBStorageManager implements PersistentStoreManager {

	DB db;
	
	public void connectDB() throws Exception {
		Mongo mongo = new Mongo("ds029817.mongolab.com", 29817);
		db = mongo.getDB("cloudburo");
		boolean auth = db.authenticate("user1", "carmen".toCharArray());
		if (!auth) {
			throw new Exception("Authentication to Mongo DB failed");
		}
	}	

	public void loadTestDataSet( String dataset, DomainDBObject dbo) throws Exception{
		DBCollection coll = db.getCollection(dbo.getDBCollectionName());
		coll.drop();
		coll = db.getCollection(dbo.getDBCollectionName());
		CSVTestDataLoader csvloader = new com.cloudburo.test.load.CSVTestDataLoader();
	
		Iterator<BSONObject> iter = csvloader.loadTestDataSet(dataset,dbo).iterator();
		while (iter.hasNext())
			coll.insert((DBObject)iter.next() );
        
		// lets get all the documents in the collection and print them out
		DBCursor cur = coll.find();
		while (cur.hasNext()) { System.out.println(cur.next());}
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
