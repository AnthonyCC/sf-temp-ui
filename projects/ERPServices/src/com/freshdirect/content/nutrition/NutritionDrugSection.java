package com.freshdirect.content.nutrition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.framework.core.ModelSupport;

public class NutritionDrugSection  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3328912178220601397L;
	
	private String id;
	private String title;
	private int position;
	private int importance;
	private DrugSectionTypeEnum type;
	private List<NutritionDrugItem> items = new ArrayList<NutritionDrugItem>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getImportance() {
		return importance;
	}
	
	public void setImportance(int importance) {
		this.importance = importance;
	}
	
	public DrugSectionTypeEnum getType() {
		return type;
	}
	
	public void setType(DrugSectionTypeEnum type) {
		this.type = type;
	}
	
	public List<NutritionDrugItem> getItems() {
		return items;
	}
	
	public void setItems(List<NutritionDrugItem> items) {
		this.items = items;
	}

}
