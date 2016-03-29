package com.freshdirect.webapp.ajax.shoppinglist;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;

/**
 *	Simple java bean for add to list requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class AddToListRequestData implements Serializable {
	
	private static final long	serialVersionUID	= -7066850496297584160L;
	
	private List<AddToCartItem> items;	// Items to add (if non-recipe)
	
	private String listId;		// For adding items to existing list
	private String listName;	// For creating new list with this name
	private String recipeId;	// For creating new list from a recipe
	private String recipeVariantId;	// For creating new list from a recipe - recipe variant (optional)
	private String recipeName;
	private String standingOrderId; 
	private String actiontype;
	private String alcoholVerified;
//	private Object header;
	
//	public Object getHeader() {
//		return header;
//	}	
//	public void setHeader( Object header ) {
//		this.header = header;
//	}	
	
	public List<AddToCartItem> getItems() {
		return items;
	}	
	public void setItems( List<AddToCartItem> items ) {
		this.items = items;
	}
	
	public String getListId() {
		return listId;
	}	
	public void setListId( String listId ) {
		this.listId = listId;
	}
	
	public String getListName() {
		return listName;
	}	
	public void setListName( String listName ) {
		this.listName = listName;
	}
	
	public String getRecipeId() {
		return recipeId;
	}	
	public void setRecipeId( String recipeId ) {
		this.recipeId = recipeId;
	}
	
	public String getRecipeVariantId() {
		return recipeVariantId;
	}	
	public void setRecipeVariantId( String recipeVariantId ) {
		this.recipeVariantId = recipeVariantId;
	}
	public String getRecipeName() {
		return recipeName;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public String getStandingOrderId() {
		return standingOrderId;
	}
	public void setStandingOrderId(String standingOrderId) {
		this.standingOrderId = standingOrderId;
	}
	public String getActiontype() {
		return actiontype;
	}
	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}
	/**
	 * @return the alcoholVerified
	 */
	public String getAlcoholVerified() {
		return alcoholVerified;
	}
	/**
	 * @param alcoholVerified the alcoholVerified to set
	 */
	public void setAlcoholVerified(String alcoholVerified) {
		this.alcoholVerified = alcoholVerified;
	}


}
