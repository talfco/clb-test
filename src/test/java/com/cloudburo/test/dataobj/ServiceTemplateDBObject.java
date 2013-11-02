package com.cloudburo.test.dataobj;
import org.supercsv.cellprocessor.constraint.StrMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.cloudburo.test.base.BaseDBObject;

public class ServiceTemplateDBObject extends BaseDBObject {
	
   private static final CellProcessor[] csvCellProcessor = new CellProcessor[] {
		        new StrMinMax(1,40),  // Expect at least a name with 1-40 characters
		        null,  
		        null,
		        null,
		        null
	};
	
   public  CellProcessor[] getCSVCellProcessor() {
		return csvCellProcessor;
	}
   
   public String getDBCollectionName() {
		return "services";
	}
   
    public void setName ( String name) {
		this.put("name", name);
	}
	
	public String getName() {
		return (String)this.get("name");
	}
	
	public void setKind(String type) {
		this.put("kind",type);
	}
	
	public String getKind() {
		return (String)this.get("kind");
	}
	
	public void setCategory (String category) {
		this.put("category", category);
	}
	
	public String getCategory() {
		return (String)this.get("category");
	}
	
	public void setPrize(String prize) {
		this.put("prize", prize);
	}
	
	public String getPrize() {
		return (String)this.get("prize");
	}
	
	public void setDuration(String duration) {
		this.put("duration", duration);
	}
	
	public String getDuration() {
		return (String)this.get("duration");
	}
}
