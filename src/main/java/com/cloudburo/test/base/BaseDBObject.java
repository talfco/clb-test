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

package com.cloudburo.test.base;

import java.util.Date;

import com.mongodb.BasicDBObject;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * For each csv file loaded, a BaseDBObject implementation class must be provided, which defines a csvCellProcessor and provides the
 * relevant set operations
 */
@SuppressWarnings("serial")
public abstract class BaseDBObject extends BasicDBObject {
	public CellProcessor[] csvCellProcessor;
	
	public abstract CellProcessor[]  getCSVCellProcessor();
	
	public abstract String getDBCollectionName();

	public BaseDBObject() {
		this.csvCellProcessor = getCSVCellProcessor();
	}
	
	public String convertDate(Date btd) {
		DateTime dt = new DateTime(btd);
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();	  
		return fmt.print(dt);
	}
}
