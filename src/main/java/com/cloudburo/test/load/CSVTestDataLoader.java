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

package com.cloudburo.test.load;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import org.bson.BSONObject;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.cloudburo.test.base.BaseDBObject;

/**
 * CSVTestDataLoader is utility class which is capable to load test data out of an Excel CSV File
 * The first row of the Excel will define the attribute names which must defined as set operations in a BaseDBObject based class
 * E.g. 'surName' --> 'setSurName' operations.
 */
public class CSVTestDataLoader {

    public List<BSONObject> loadTestDataSet(String set, BaseDBObject dboClass) throws Exception {
    	List<BSONObject> list = new ArrayList<BSONObject>();
    	ICsvBeanReader inFile = new CsvBeanReader(new FileReader("testdata/"+dboClass.getClass().getName()+"-"+set+".csv"), 
    			    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    	try {
  	      final String[] header = inFile.getHeader(true);
  	      BSONObject dbo;
  	      while( (dbo = inFile.read(dboClass.getClass(), header, dboClass.getCSVCellProcessor())) != null) {
  	        list.add(dbo);
  	      }
  	    } finally {
  	      inFile.close();
  	    }
    	return list;
    }    
}