package com.cloudburo.test.load;

import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import org.bson.BSONObject;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.cloudburo.test.domain.DomainDBObject;


public class CSVTestDataLoader {

    public List<BSONObject> loadTestDataSet(String set, DomainDBObject dboClass) throws Exception {
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