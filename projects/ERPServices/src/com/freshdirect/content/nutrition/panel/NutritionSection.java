package com.freshdirect.content.nutrition.panel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NutritionSection  implements Serializable{

	private static final long serialVersionUID = -3328912178220601397L;
	
	private String id;
	private String title;
	private int position;
	private int importance;
	private NutritionSectionType type;
	private List<NutritionItem> items = new ArrayList<NutritionItem>();
	
	/** package protected - used by NutritionPanel.deepCopy() */
	static NutritionSection deepCopy( NutritionSection oldS ) {
		NutritionSection newS = new NutritionSection();
		newS.id = null;
		newS.title = oldS.title;
		newS.position = oldS.position;
		newS.importance= oldS.importance;
		newS.type = oldS.type;		
		for ( NutritionItem i : oldS.items ) {
			newS.items.add( NutritionItem.deepCopy(i) );
		}		
		return newS;
	}
	
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
	
	public NutritionSectionType getType() {
		return type;
	}
	
	public void setType(NutritionSectionType type) {
		this.type = type;
	}
	
	public List<NutritionItem> getItems() {
		return items;
	}
	
	public void setItems(List<NutritionItem> items) {
		this.items = items;
	}

}
