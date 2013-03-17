package com.cloudburo.test.dataobj;

import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class ProductTypeDBObject extends DomainDBObject {
	
	private static final CellProcessor[] csvCellProcessor = new CellProcessor[] {
        new StrMinMax(1,40),  // Expect at least a name with 1-40 characters
        null,  
        null
};

	public  CellProcessor[] getCSVCellProcessor() {
		return csvCellProcessor;
	}
   
   public String getDBCollectionName() {
		return "productTypes";
	}
   
    public void setName ( String name) {
		this.put("name", name);
	}
	
	public String getName() {
		return (String)this.get("name");
	}

	public void setCategory (String category) {
		this.put("category", category);
	}
	
	public String getCategory() {
		return (String)this.get("category");
	}
	
	public void setDescription (String descr) {
		this.put("description", descr);
	}
	
	public String getDescription() {
		return (String)this.get("description");
	}
	
}
