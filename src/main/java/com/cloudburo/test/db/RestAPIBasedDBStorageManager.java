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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bson.BSONObject;

import com.cloudburo.test.base.BaseDBObject;
import com.cloudburo.test.load.CSVTestDataLoader;
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
		// TODO Auto-generated method stub
		return null;
	}

	public long countCollectionObjects(String collection) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public void loadTestDataSet(String dataset, BaseDBObject dbo)
			throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		CSVTestDataLoader csvloader = new com.cloudburo.test.load.CSVTestDataLoader();
		Iterator<BSONObject> iter = csvloader.loadTestDataSet(dataset,dbo).iterator();
		
		while (iter.hasNext()) {
			String jsonStr = ((BaseDBObject)iter.next()).toString();
			HttpPost httpPost = new HttpPost(urlRequest+dbo.getDBCollectionName());
			httpPost.setEntity(new StringEntity(jsonStr,ContentType.create("application/json")));
			logger.log(Level.INFO, "Creatin JSON Object {0}",jsonStr);
			HttpResponse resp = httpclient.execute(httpPost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            String content = "";
			String line = "";
            while ((line = rd.readLine()) != null) 
            	content+=line;
            JsonParser parser = new JsonParser();
    		JsonObject object = parser.parse(content).getAsJsonObject();
            logger.log(Level.INFO, "Created object {0}",object.get("_id"));
		}
	}

}
