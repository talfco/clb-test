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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bson.BSONObject;
import com.mongodb.BasicDBObject;

import com.cloudburo.test.base.BaseDBObject;
import com.cloudburo.test.load.CSVTestDataLoader;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestAPIBasedDBStorageManager implements PersistentStoreManager {
	
	private static String urlRequest = "";
	private static final Logger logger = Logger.getLogger(RestAPIBasedDBStorageManager.class.getCanonicalName());

	public void connectDB(HashMap<String, String> connectionProps)
			throws Exception {
		urlRequest = connectionProps.get(PersistentStoreManager.STORE_ENDPOINT)+":"+connectionProps.get(PersistentStoreManager.STORE_PORT)+"/"+
				connectionProps.get(PersistentStoreManager.STORE_INSTANCENAME)+"/";
	}

	public BSONObject getCollectionObject(String collection, String id)
			throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		HttpGet httpGet = new HttpGet(urlRequest+collection+"/"+id);
        String content = "";
		String line = "";
		CloseableHttpResponse resp;
		BufferedReader rd;
		JsonParser parser = new JsonParser();
		resp = httpclient.execute(httpGet);
		rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		content="";
        while ((line = rd.readLine()) != null)  content+=line;
        logger.log(Level.INFO, "Got content {0}",content);
        JsonObject obj = parser.parse(content).getAsJsonObject();
        Set<Entry<String, JsonElement>> entrySet = obj.entrySet();
        Iterator<Entry<String, JsonElement>> it = entrySet.iterator();
        BasicDBObject boj =  new BasicDBObject();
        while (it.hasNext()) {		
        	Entry<String,JsonElement> map = it.next();
        	boj.append(map.getKey(), map.getValue());
        }
        return boj;
	}

	public long countCollectionObjects(String collection) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		int count = 0;
		// Let's retrieve first all entries and then delete them
		HttpGet httpGet = new HttpGet(urlRequest+collection);
	    boolean doFetch = true;
        String content = "";
		String line = "";
		CloseableHttpResponse resp;
		BufferedReader rd;
		JsonParser parser;
	    while (doFetch)	{
	    	doFetch = false;
	    	resp = httpclient.execute(httpGet);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			content="";
	        while ((line = rd.readLine()) != null)  content+=line;
	        logger.log(Level.INFO, "Got content {0}",content);
	        parser = new JsonParser();
		    Iterator<JsonElement> jselemIt = parser.parse(content).getAsJsonArray().iterator();

			while (jselemIt.hasNext()) {
				JsonObject obj = jselemIt.next().getAsJsonObject();
				if (obj.has("_id")) {
					count++;				
				} else {
					// Ok this must be the meta record then
					// We differentiate two types of 
					if (obj.has("_maxRec")) {
						// We can return the result immediately
						return obj.get("_maxRec").getAsInt();
					} else if (obj.has("_cursor")) {
						// We have to iterate with the cursor
						if (obj.get("_cursor").getAsString().equals("")) {
							doFetch = false;
						} else {
						   doFetch = true;
						   logger.log(Level.INFO, "Paging: Going to fetch records");
						}
					} else {
						doFetch = false;
						logger.log(Level.WARNING, "Unrecoginize JSON Element in return {0}",obj.toString());
						return -1;
					}
				}
			}	
	    }
		return count;
	}

	public void loadTestDataSet(String dataset, BaseDBObject dbo)
			throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		
		// Let's retrieve first all entries and then delete them
		HttpGet httpGet = new HttpGet(urlRequest+dbo.getDBCollectionName());
	    boolean doFetch = true;
        String content = "";
		String line = "";
		CloseableHttpResponse resp;
		BufferedReader rd;
		JsonParser parser;
	    while (doFetch)	{
	    	doFetch = false;
	    	resp = httpclient.execute(httpGet);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			content="";
	        while ((line = rd.readLine()) != null)  content+=line;
	        logger.log(Level.INFO, "Got content {0}",content);
	        parser = new JsonParser();
		    Iterator<JsonElement> jselemIt = parser.parse(content).getAsJsonArray().iterator();

			while (jselemIt.hasNext()) {
				JsonObject obj = jselemIt.next().getAsJsonObject();
				if (obj.has("_id")) {
					String id = obj.get("_id").getAsString();
					logger.log(Level.INFO, "Delete JSON Object {0}",id);
					HttpDelete httpDelete = new HttpDelete(urlRequest+dbo.getDBCollectionName()+"/"+id);
					resp = httpclient.execute(httpDelete);
					resp.close();
				
				} else {
					// Ok this must be the meta record then
					// We differentiate two types of 
					if (obj.has("_maxRec")) {
						int offset = obj.get("_offset").getAsInt()+obj.get("_limit").getAsInt();
						if (offset > obj.get("_maxRec").getAsInt())  {
							doFetch = false;
						} else {
							doFetch = true;
							logger.log(Level.INFO, "Paging: Going to fetch records with offset {0}",offset);
							//httpGet = new HttpGet(urlRequest+dbo.getDBCollectionName()+"?offset="+offset+"?maxRec="+obj.get("_maxRec").getAsString());
						}
					} else if (obj.has("_cursor")) {
						if (obj.get("_cursor").getAsString().equals("")) {
							doFetch = false;
						} else {
						   doFetch = true;
						   logger.log(Level.INFO, "Paging: Going to fetch records");
						}
					} else {
						doFetch = false;
						logger.log(Level.WARNING, "Unrecoginize JSON Element in return {0}",obj.toString());
					}
				}
			}	
	    }
	    
	    // Load the Test Data Set
		CSVTestDataLoader csvloader = new com.cloudburo.test.load.CSVTestDataLoader();
		Iterator<BSONObject> iter = csvloader.loadTestDataSet(dataset,dbo).iterator();
		
		// Now insert all elements
		while (iter.hasNext()) {
			String jsonStr = ((BaseDBObject)iter.next()).toString();
			HttpPost httpPost = new HttpPost(urlRequest+dbo.getDBCollectionName());
			httpPost.setEntity(new StringEntity(jsonStr,ContentType.create("application/json")));
			logger.log(Level.INFO, "Creating JSON Object {0}",jsonStr);
			resp = httpclient.execute(httpPost);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            content = "";
			line = "";
            while ((line = rd.readLine()) != null) content+=line;
            parser = new JsonParser();
    		JsonObject object = parser.parse(content).getAsJsonObject();
            logger.log(Level.INFO, "Created object {0}",object.get("_id"));
            resp.close();
		}
	}

}
