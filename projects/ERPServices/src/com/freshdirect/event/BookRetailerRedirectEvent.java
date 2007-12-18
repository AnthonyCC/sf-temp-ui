package com.freshdirect.event;

import com.freshdirect.framework.event.FDEvent;

/**
 * BookRetailerRedirectEvent
 * 
 * param_1 = eventValues[0] = bookRetailerId
 * param_2 = eventValues[1] = recipeSourceID
 */
public class BookRetailerRedirectEvent extends FDEvent {

	public String getBookRetailerId() {
		return this.eventValues[0];
	}
	
	public void setBookRetailerId(String bookRetailerId) {
		this.eventValues[0] = bookRetailerId;
	}
	
	public String getRecipeSourceId(){
		return this.eventValues[1];
	}
	
	public void setRecipeSourceId(String recipeSourceId) {
		this.eventValues[1] = recipeSourceId;
	}

}
