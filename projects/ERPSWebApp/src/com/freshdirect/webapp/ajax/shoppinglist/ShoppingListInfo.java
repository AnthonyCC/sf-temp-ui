package com.freshdirect.webapp.ajax.shoppinglist;

import java.io.Serializable;


public class ShoppingListInfo implements Serializable {
	
	private static final long	serialVersionUID	= -3913154264269253209L;
	
	private String listId = null;
	private String name = null;
	private int count = 0;	
	private boolean isDefault = false;
	private String recipeId = null;
	
	public String getListId() {
		return listId;
	}	
	public void setListId( String listId ) {
		this.listId = listId;
	}	
	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}	
	public int getCount() {
		return count;
	}	
	public void setCount( int count ) {
		this.count = count;
	}	
	public boolean isDefault() {
		return isDefault;
	}	
	public void setDefault( boolean isDefault ) {
		this.isDefault = isDefault;
	}	
	public String getRecipeId() {
		return recipeId;
	}	
	public void setRecipeId( String recipeId ) {
		this.recipeId = recipeId;
	}	

}