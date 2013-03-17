package com.cloudburo.test;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.bson.BSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import com.cloudburo.test.dataobj.CustomerDBObject;
import com.cloudburo.test.dataobj.DomainDBObject;
import com.cloudburo.test.dataobj.ProductTypeDBObject;
import com.cloudburo.test.dataobj.ServiceTemplateDBObject;
import com.cloudburo.test.db.MongoDBController;
import com.cloudburo.test.load.CSVTestDataLoader;
import com.cloudburo.test.page.StartPage;
import com.mongodb.DBObject;

public class TestDriver {
	
  private static WebDriver driver;	
  private static MongoDBController dbcontroller;
  //private static String HOST = "http://stark-coast-2148.herokuapp.com"; // http://localhost:5000
  private static String HOST = "http://localhost:5000"; // http://localhost:5000
	
  @Parameters({"testfile"})
  @BeforeSuite
  public void beforeSuite(String testfile) throws Exception {
	  System.out.println("Using testset "+testfile);
	  dbcontroller = new MongoDBController();
	  dbcontroller.connectDB();
	  dbcontroller.loadTestDataSet(testfile,new CustomerDBObject());
	  dbcontroller.loadTestDataSet(testfile,new ServiceTemplateDBObject());
	  driver = new FirefoxDriver();
	  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }
  
  @AfterSuite
  public void afterSuite() {
	  driver.close();
	  driver.quit();
  }

  @Parameters({"listvalue1", "listvalue2", "collection","url"})
  @Test(description="Testing the basic list entry and content detail")
  public void testBaseContentDisplay(String listvalue1, String listvalue2, String collection,String url) throws Exception {
	  
	  driver.get(HOST+"/"+url);
	  StartPage page = new StartPage(driver);
	  Thread.sleep(1000);
	  WebElement elem = page.getActiveLevel2MenuEntry();
	  // Test that the menu for the selection is selected
	  Assert.assertEquals(elem.getAttribute("id"), collection.toLowerCase()+"-dp");
	 
	  checkPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,0);
  }
  
  @Parameters({"listvalue1", "listvalue2","collection","dbobject"})
  @Test(description="Testing the insert of a new entry")
  public void testInsertDeleteRecord(String listvalue1, String listvalue2,String collection, String dbobject) throws Exception {
	  CSVTestDataLoader loader = new CSVTestDataLoader();
	  List<DBObject> list = loader.loadTestDataSet("insert1recordb",(DomainDBObject)Class.forName(dbobject).newInstance());
	  StartPage page = new StartPage(driver);
	  
	  
	  DBObject elem = list.get(0); 
	  Iterator<String> it = elem.keySet().iterator();
	  List<WebElement> primaryList = page.getPrimaryListEntries();
	  int primListSize = primaryList.size();
	 
	  // Get the Drop Down Entry to open it
	  WebElement welem = page.getActiveLevel2MenuDropDown(); 
	  welem.click();
	  // Get the new record menu entry in the drop down menu
	  welem = page.getActiveLevel2MenuNewRecord();
	  welem.click();
	  // So the primary content form should contain a form
	  welem = page.getPrimaryContentForm();
	  
	  // Let's fill in our new record
	  while (it.hasNext()) {
		  String name = it.next();
		  String value = (String)elem.get(name);
		  WebElement inputfield = welem.findElement(By.id(name));
		  Assert.assertNotNull(inputfield);
		  if (inputfield.getTagName().equals("select")) {
			  Select selectBox = new Select(inputfield);
		      selectBox.selectByValue(value); 
		  } else {
		    if (name.endsWith("date")) { 
			    value = localizeDate((String)elem.get(name));
		    } 		  
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(Keys.BACK_SPACE);
		    inputfield.sendKeys(value);  
		    inputfield.sendKeys(Keys.TAB);  
		  }
	  }
	  // Now Press the submit button
	  WebElement btn = welem.findElement(By.className("btn-primary"));
	  Assert.assertNotNull(btn);
	  btn.click();
	  // Let's wait until the view mode is reached
	  page.getPrimaryContentH1();
	  // Now we should have one record more than beforehand
	  primaryList = page.getPrimaryListEntries();
	  Assert.assertEquals(primaryList.size(),primListSize+1 );
	  // The selected entry must be the new one added
	  WebElement selectedEntry = page.getSelectedListEntry();
	  String uid = selectedEntry.getAttribute("id");
	  DBObject dobj = dbcontroller.getCollectionObject(collection, uid);
	  // Iterate once through the test set
	  it = elem.keySet().iterator();
	  while (it.hasNext()) {
		  String name = it.next();
		  String value = (String)elem.get(name);
		  Assert.assertNotNull(dobj.get(name), "Retrieving DB Attribute '"+name+"'");
		  Assert.assertEquals(value.trim(), dobj.get(name).toString().trim());
	  }
	  checkPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,-1);
	  
	  // Delete the entry again
	  // This is the last entry
	  selectedEntry = page.getSelectedListEntry();
	  uid = selectedEntry.getAttribute("id");
	  // Get the Drop Down Entry to open it
	  welem = page.getActiveLevel2MenuDropDown(); 
	  welem.click();
	  // Get the new record menu entry in the drop down menu
	  welem = page.getActiveLevel2MenuDeleteRecord();
	  welem.click();
	  welem = page.getDeleteModalOKButton();
	  welem.click();
	  // Click away the msgarea
	  Thread.sleep(1000);
	  welem = page.getMessageAreaCloseIcon();
	  welem.click();
	  // Now the selected entry shouldnt be anmore in the db
	  dobj = dbcontroller.getCollectionObject(collection, uid);
	  Assert.assertNull(dobj);
	  checkPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,-1);

  }
  
  @Parameters({"listvalue1", "listvalue2","collection"})
  @Test(description="Testing the cancelling of a insert of a new entry")
  public void testInsertCancelRecord(String listvalue1, String listvalue2,String collection) throws Exception {
	  Thread.sleep(1000);
	  StartPage page = new StartPage(driver);
	  List<WebElement> primaryList = page.getPrimaryListEntries();
	  int primListSize = primaryList.size();
	  
	  // Get the Drop Down Entry to open it
	  WebElement welem = page.getActiveLevel2MenuDropDown(); 
	  welem.click();
	  // Get the new record menu entry in the drop down menu
	  welem = page.getActiveLevel2MenuNewRecord();
	  welem.click();
	  // So the primary content form should contain a form
	  welem = page.getPrimaryContentForm();
	  
	  // Let's fill in our new record
	  // Now Press the submit button
	  WebElement btn = welem.findElement(By.className("btn-cancel"));
	  Assert.assertNotNull(btn);
	  btn.click();
	  // Now we should have same record as beforehand
	  primaryList = page.getPrimaryListEntries();
	  Assert.assertEquals(primaryList.size(),primListSize );
	  // The selected entry should be the top one, so index = 0
	  checkPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,0);
  }
  
  
  @Parameters({"collection", "dbobject"})
  @Test(description="Modifying an entry with in place editor")
  public void testModifyRecordInPlaceEditor(String collection, String dbobject) throws Exception {
	  doInPlace(collection, "save", dbobject);
  }
  
  @Parameters({"collection", "dbobject"})
  @Test(description="Modifying and cancel an entry with in place editor")
  public void testModifyCancelRecordInPlaceEditor(String collection, String dbobject) throws Exception {
	  doInPlace(collection, "cancel", dbobject); 
  }
  
  private String localizeDate(String dt) throws Exception {
	  SimpleDateFormat df =  new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	  df.parse(dt);
	  Calendar calendar = df.getCalendar();
	  String day = "";
	  if ( calendar.get(Calendar.DAY_OF_MONTH)<10) day +="0";
	  day +=calendar.get(Calendar.DAY_OF_MONTH);
	  String month = "";
	  if ( calendar.get(Calendar.MONTH)+1<10) month +="0";
	  month += calendar.get(Calendar.MONTH)+1;
	  String value =  day + "."+ month + "."+calendar.get(Calendar.YEAR) ;
	  System.out.println("Localized Date to "+value);
	  return value;
  }
  
  private void doInPlace(String collection,String mode, String dbObject) throws Exception {
	  CSVTestDataLoader loader = new CSVTestDataLoader();
	  
	  List<DBObject> list = null;
	  if (mode.equals("save"))
		  list = loader.loadTestDataSet("insert1record",(DomainDBObject)Class.forName(dbObject).newInstance());
	  else
		  list = loader.loadTestDataSet("insert1recorda",(DomainDBObject)Class.forName(dbObject).newInstance());
	  
	  DBObject elem = list.get(0);
	  Iterator<String> it = elem.keySet().iterator();
	  // Let's select the first entry
	  StartPage page = new StartPage(driver);
	  List<WebElement> primaryList = page.getPrimaryListEntries();
	  WebElement nxtElem = primaryList.get(0);
	  nxtElem.click();
	  // Keep the ID to check that the update worked correctly
	  String id = nxtElem.getAttribute("id");
	  WebElement primContContainer = page.getPrimaryContentContainer();

	  // Now we modify each value
	  while (it.hasNext()) {
		  String name = it.next();
		  String value = (String)elem.get(name);
		  System.out.println("Got "+name+" "+value);
		  Calendar cal = Calendar.getInstance();

		  if (name.endsWith("date")) { 
			  value = localizeDate((String)elem.get(name));
			  //SimpleDateFormat df =  new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			  //df.parse((String)elem.get(name));
			  //Calendar calendar = df.getCalendar();
			  //value =  calendar.get(Calendar.DAY_OF_MONTH) + "."+(calendar.get(Calendar.MONTH)+1) + "."+calendar.get(Calendar.YEAR) ;
			  //System.out.println("Localized Date to "+value);
		  }
		  WebElement chf = primContContainer.findElement(By.id(name));
		  chf.click();
		  chf = primContContainer.findElement(By.id("edit-"+name));
		  if (chf.getTagName().equals("select")) {
			  Select selectBox = new Select(chf);
		        selectBox.selectByValue(value);  
		  } else {
			  chf.clear();
			  chf.sendKeys(value);
		  
		  }
		  WebElement chfb=null;
		  if (mode.equals("save")) {
			  chfb = primContContainer.findElement(By.id("save-"+name));	
		  } else {
			  chfb = primContContainer.findElement(By.id("cancel-"+name));
		  }
		  chfb.click();		
	  }
	  // Retrieve the stored value and check against the one which we entered
	  DBObject obj = dbcontroller.getCollectionObject(collection, id);
	  it = elem.keySet().iterator();
	  
	  while (it.hasNext()) {
		  String name = it.next();
		  String value = (String)elem.get(name);
		  if (mode.equals("save")) {
			  Assert.assertEquals(obj.get(name).toString().trim(),value.trim());
		  } else {
			  // TODO: Error here
			  String exp = obj.get(name).toString();
			  Assert.assertFalse(exp.equals(value));			 
		  }
	  }
  }
 
  
  private void checkPrimaryListAgainstPrimayContent(String listvalue1, String listvalue2, String collection, int defaultIndex) throws Exception{
	  StartPage page = new StartPage(driver);
	  List<WebElement> list = page.getPrimaryListEntries();
	  Assert.assertEquals(list.size(), dbcontroller.countCollectionObjects(collection)); 
	  WebElement nxtElem, elem;
	  if (defaultIndex>=0) {
	    // Test if the defaultIndex is already correctly selected
	    nxtElem = list.get(defaultIndex);
	    elem = page.getSelectedListEntry();
	    Assert.assertEquals(nxtElem.getAttribute("id"), elem.getAttribute("id"));  
	    reconPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,elem,page);
	  }
	  // Iterate through all entries and check if the content gets updated
	  for (int i=0; i<list.size();i++) {
		  nxtElem = list.get(i);
		  nxtElem.click();    	  
		  elem = page.getSelectedListEntry();
		  Assert.assertEquals(nxtElem.getAttribute("id"), elem.getAttribute("id"));  
		  reconPrimaryListAgainstPrimayContent(listvalue1,listvalue2,collection,elem,page);
	  }
  }

  private void reconPrimaryListAgainstPrimayContent(String listvalue1, String listvalue2, String collection, WebElement elem, StartPage page) throws Exception{
	  String id = elem.getAttribute("id");
      Assert.assertNotEquals(id, null);
      // Retrieve the value from the DB
      DBObject obj = dbcontroller.getCollectionObject(collection, id);
      // Check if the selected list entry is showing the correct values as 
      // retrieved from the DB
      String listPrefix = (String)obj.get(listvalue1);
      System.out.println("Testing list <-> detail content "+listPrefix);
      Assert.assertEquals(elem.getText().contains(listPrefix), true,"Comparing '"+elem.getText()+"' starts with '"+listPrefix+"'");
      listPrefix = (String)obj.get(listvalue2);
      System.out.println("Testing list <-> detail content "+listPrefix);
      // Check if the H1 entry of the detail page is showing the entry
      // which is selected in the list. We use the id shown in the list
      Iterator<WebElement> it = page.getPrimaryContentH1().iterator();
      while (it.hasNext()) {
    	  WebElement h1 = it.next();
    	  String label = h1.getAttribute("id");
    	  String dbValue = (String) obj.get(label);
    	  Assert.assertEquals(dbValue,h1.getText());
      }
  }
  
}

