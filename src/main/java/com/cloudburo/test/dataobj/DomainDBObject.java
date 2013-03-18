package com.cloudburo.test.dataobj;

import com.mongodb.BasicDBObject;
import org.supercsv.cellprocessor.ift.CellProcessor;

public abstract class DomainDBObject extends BasicDBObject {
	public CellProcessor[] csvCellProcessor;
	
	public abstract CellProcessor[]  getCSVCellProcessor();
	
	public abstract String getDBCollectionName();

	public DomainDBObject() {
		this.csvCellProcessor = getCSVCellProcessor();
	}
}
