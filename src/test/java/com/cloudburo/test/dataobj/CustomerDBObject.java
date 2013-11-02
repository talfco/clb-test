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

package com.cloudburo.test.dataobj;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.cloudburo.test.base.BaseDBObject;


@SuppressWarnings("serial")
public class CustomerDBObject extends BaseDBObject  {
	
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
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();	  
		this.put("birthdate", fmt.print(dt));
	}
}
