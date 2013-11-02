package com.cloudburo.test.base;

import com.mongodb.BasicDBObject;

import org.supercsv.cellprocessor.ift.CellProcessor;

@SuppressWarnings("serial")
public abstract class BaseDBObject extends BasicDBObject {
	public CellProcessor[] csvCellProcessor;
	
	public abstract CellProcessor[]  getCSVCellProcessor();
	
	public abstract String getDBCollectionName();

	public BaseDBObject() {
		this.csvCellProcessor = getCSVCellProcessor();
	}
}
