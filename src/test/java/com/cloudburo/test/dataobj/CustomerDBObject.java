package com.cloudburo.test.dataobj;

import java.util.Date;
import org.joda.time.DateTime;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;


public class CustomerDBObject extends DomainDBObject  {
	
	// The CSV Cell Processor
	private static final CellProcessor[] csvCellProcessor = new CellProcessor[] {
		new StrMinMax(1,40),  // Expect at least a name with 1-40 characters
		new StrMinMax(1,40),  // Expect at least a surname with 1-40 characters
		null,
		null,
		null,
		null,
		null,
		null,
		null,
		new ParseDate("dd.MM.yyyy")
	};
	
	public  CellProcessor[] getCSVCellProcessor() {
		return csvCellProcessor;
	}
	
	public String getDBCollectionName() {
		return "customers";
	}
	
	public void setName(String name) {
		this.put("name", name);
	}
	public String getName() {
		return (String)this.get("name");
	}
	public void setSurname(String surname) {
		this.put("surname", surname);
	}
	public void setAddress(String addr) {
		this.put("address", addr);
	}
	public void setLocation(String location) {
		this.put("location", location);
	}
	public void setPlz(String plz) {
		this.put("plz", plz);
	}
	public void setTelephone(String telephone) {
		this.put("telephone", telephone);
	}
	public void setMobile(String mobile) {
		this.put("mobile", mobile);
	}
	public void setEmail(String email) {
		this.put("email", email);
	}
	public void setCitizenship(String cz) {
		this.put("citizenship", cz);
	}
	public void setBirthdate(Date btd) {
		DateTime dt = new DateTime(btd);
	
		  // Calendar calendar = javax.xml.bind.DatatypeConverter.parseDate(btd.toString());
		  // String value =  calendar.get(Calendar.DAY_OF_MONTH) + "."+calendar.get(Calendar.MONTH) + "."+calendar.get(Calendar.YEAR) ;
		  
		 this.put("birthdate", btd.toString());
		//  this.put("birthdate", btd.toString());
	}
}
