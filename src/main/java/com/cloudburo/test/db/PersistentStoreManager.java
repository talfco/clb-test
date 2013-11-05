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

import org.bson.BSONObject;

import com.cloudburo.test.base.BaseDBObject;

/**
 * The base interface which must be implmeneted by a PersistenStoreManager
 */
public interface PersistentStoreManager {
	

	public static final String STORE_ENDPOINT="ENDPOINT";
	public static final String STORE_PORT="PORT";
	public static final String STORE_INSTANCENAME="INSTANCENAME";
	public static final String STORE_USER="USER";
	public static final String STORE_PASSWORD="PASSWORD";
	
	public void connectDB(HashMap<String,String> connectionProps) throws Exception;
	public BSONObject getCollectionObject(String collection, String id) throws Exception;
	public long countCollectionObjects(String collection) throws Exception;
	public void loadTestDataSet( String dataset, BaseDBObject dbo) throws Exception;
	
}
