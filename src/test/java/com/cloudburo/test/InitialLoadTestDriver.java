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

package com.cloudburo.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.cloudburo.test.base.BaseTestDriver;
import com.cloudburo.test.dataobj.CustomerDBObject;

/**
 * This is a simple testng Test Driver which doesn't do any test but which will load a test dataset
 */
public class InitialLoadTestDriver extends BaseTestDriver {

	 private static final Logger logger = Logger.getLogger(InitialLoadTestDriver.class.getCanonicalName());
	
	 @Parameters({"testfile","appurl","storeendpoint","storeport","storeinstance","storeuser","storepassword","storemanager"})
	  @BeforeSuite
	  public void beforeSuite(String testfile,String appurl, String storeendpoint,String storeport,String storeinstance,
			  String storeuser,String storepassword, String storemanager) throws Exception {
		  logger.log(Level.INFO, "Using {0}","testset '"+testfile+"' connecting to "+storeendpoint+":"+storeport+
				  "/"+storeinstance+"@"+storeuser);
		  
		  
		  initalizePersistentStoreManager(storeendpoint, storeport, storeinstance, storeuser, 
				  storepassword, storemanager);
		  
		  // This will load a test data set for the Customer object
		  persistentStoreManager.loadTestDataSet(testfile,new CustomerDBObject());
	  }
	  
	  @AfterSuite
	  public void afterSuite() {
	  }
	 
	
}
