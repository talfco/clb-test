package com.cloudburo.test.load;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;


import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.StrMinMax;

import com.cloudburo.test.dataobj.DomainDBObject;
import com.mongodb.DBObject;


public class CSVTestDataLoader {
    public CellProcessor[] CustomerDBObjectProcessors = new CellProcessor[] {
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
    public CellProcessor[] ServiceTemplateDBObjectProcessors = new CellProcessor[] {
        new StrMinMax(1,40),  // Expect at least a name with 1-40 characters
        null,  
        null,
        null,
        null
    };
    public CellProcessor[] ProductTypeDBObjectProcessors = new CellProcessor[] {
            new StrMinMax(1,40),  // Expect at least a name with 1-40 characters
            null,  
            null
        };
    public List<DBObject> loadTestDataSet(String set, DomainDBObject dboClass) throws Exception {
    	List<DBObject> list = new ArrayList<DBObject>();
    	ICsvBeanReader inFile = new CsvBeanReader(new FileReader("testdata/"+dboClass.getClass().getName()+"-"+set+".csv"), 
    			    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    	try {
  	      final String[] header = inFile.getHeader(true);
  	      DBObject dbo;
  	      while( (dbo = inFile.read(dboClass.getClass(), header, dboClass.getCSVCellProcessor())) != null) {
  	        list.add(dbo);
  	      }
  	    } finally {
  	      inFile.close();
  	    }
    	return list;
    }    
}