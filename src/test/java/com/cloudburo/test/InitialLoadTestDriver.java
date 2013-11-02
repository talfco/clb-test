package com.cloudburo.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.cloudburo.test.base.BaseTestDriver;
import com.cloudburo.test.dataobj.CustomerDBObject;


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
		  
		  persistentStoreManager.loadTestDataSet(testfile,new CustomerDBObject());
	  }
	  
	  @AfterSuite
	  public void afterSuite() {
	  }
	 
	
}
