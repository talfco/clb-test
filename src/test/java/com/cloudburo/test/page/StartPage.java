package com.cloudburo.test.page;

import java.util.List; 

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.testng.Assert;

public class StartPage {
	
	private WebDriver driver;

	public StartPage (WebDriver driver) {
		Assert.assertEquals(driver.getTitle(), "Cloudburo App Center");
        this.driver = driver;
    }
	
	// Returns the selected ListEntry.
	// We expect that there is always one and only one entry selected
	public WebElement getSelectedListEntry() {
		List<WebElement> list = driver.findElements(By.className("selectedListEntry"));
		// Test that we have only one entry as selected
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public List<WebElement> getPrimaryListEntries() {
		List<WebElement> list = driver.findElements(By.className("primaryContentList"));
		return list;
	}
	
	public WebElement getPrimaryContentContainer() {
		List<WebElement> list = driver.findElements(By.id("primaryContentContainer"));
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public WebElement getNavigationBarLevel2() {
		List<WebElement> list = driver.findElements(By.xpath("//ul[@class='nav nav-tabs']"));
		// Test that we have only one navigation bar
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public List<WebElement> getPrimaryContentH1() {
		List<WebElement> list = driver.findElements(By.xpath("//span[@class='editTrigger, content-h1']"));
		// Test that only two entries are selected
		Assert.assertEquals(list.size(),2);
		return list;
	}
	
	public WebElement getPrimaryContentForm() {
		List<WebElement> list = getPrimaryContentContainer().findElements(By.id("busobj_form"));
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	
	public WebElement getActiveLevel2MenuEntry() {
		WebElement elem = getNavigationBarLevel2();
		List<WebElement> list = elem.findElements(By.className("active"));
		// Test that we have only one entry selected
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public WebElement getActiveLevel2MenuNewRecord() {
		WebElement elem = getActiveLevel2MenuEntry();
		List<WebElement> list = elem.findElements(By.id("cun"));
		// Test that we have only one entry selected
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public WebElement getActiveLevel2MenuDeleteRecord() {
		WebElement elem = getActiveLevel2MenuEntry();
		List<WebElement> list = elem.findElements(By.id("cud"));
		// Test that we have only one entry selected
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	
	public WebElement getMessageAreaCloseIcon(){
		WebElement elem = driver.findElement(By.id("msgarea"));
		Assert.assertNotNull(elem);
		elem = driver.findElement(By.className("close"));
		Assert.assertNotNull(elem);
		return elem;
	}
	
	public WebElement getDeleteModalOKButton() {
		WebElement elem = driver.findElement(By.id("deleteModal"));
		elem = elem.findElement(By.className("primary"));
		Assert.assertNotNull(elem);
		return elem;
	}
	
	public WebElement getDeleteModalCancelButton() {
		WebElement elem = driver.findElement(By.id("deleteModal"));
		elem = elem.findElement(By.className("secondary"));
		Assert.assertNotNull(elem);
		return elem;
	}
	

	public WebElement getActiveLevel2MenuDropDown() {
		WebElement elem = getActiveLevel2MenuEntry();
		List<WebElement> list = elem.findElements(By.id("cuh"));
		// Test that we have only one entry selected
		Assert.assertEquals(list.size(),1);
		return list.get(0);
	}
	

	
	

}
