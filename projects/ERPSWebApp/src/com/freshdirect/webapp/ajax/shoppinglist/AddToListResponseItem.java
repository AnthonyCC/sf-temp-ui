package com.freshdirect.webapp.ajax.shoppinglist;

import java.io.Serializable;

/**
 * Simple java bean representing the add to list response JSON structure
 * Holding information for the add to list result popup
 * 
 * @author szabi
 *
 */
public class AddToListResponseItem implements Serializable {
	
	private static final long	serialVersionUID	= 5663007153678387979L;
	
	private String lineId;
	private String listId;
	private String listName;
	private String message;
	private String itemName;
	private boolean listCreated = false;
	
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public boolean isListCreated() {
		return listCreated;
	}
	public void setListCreated(boolean listCreated) {
		this.listCreated = listCreated;
	}

}
