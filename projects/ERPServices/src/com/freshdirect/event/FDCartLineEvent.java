package com.freshdirect.event;

import com.freshdirect.framework.event.FDEvent;

/**
 * FDAddToCartEvent
 * 
 * param_1  = eventValues[0]  = productId
 * param_2  = eventValues[1]  = skuCode
 * param_3  = eventValues[2]  = categoryId
 * param_4  = eventValues[3]  = department
 * param_5  = eventValues[4]  = cartlineId
 * param_6  = eventValues[5]  = Quantity
 * param_7  = eventValues[6]  = SalesUnit
 * param_8  = eventValues[7]  = Configuration
 * param_9  = eventValues[8]  = Category TX YMAL was obtained from, only for TX YMALs
 * param_10 = eventValues[9]  = product the TX YMAL was obtained from ("originating product")
 * param_11 = eventValues[10] = YMAL set id, if the product is added through a YMAL set  
 * param_12 = eventValues[11] = Customer Created List id
 * @author Skrishnasamy Date April 26, 2006
 */


public class FDCartLineEvent extends FDEvent {
	

	public String getProductId() {
		return this.eventValues[0];
	}
	
	public void setProductId(String productId) {
		this.eventValues[0] = productId;
	}
	
	public String getSkuCode(){
		return this.eventValues[1];
	}
	
	public void setSkuCode(String skuCode) {
		this.eventValues[1] = skuCode;
	}
	
	public String getCategoryId() {
		return this.eventValues[2];
	}
	
	public void setCategoryId(String categoryId) {
		this.eventValues[2] = categoryId;
	}
	
	public String getDepartment() {
		return this.eventValues[3];
	}
	
	public void setDepartment(String department) {
		this.eventValues[3] = department;
	}
	
	public String getCartlineId() {
		return this.eventValues[4];
	}
	
	public void setCartlineId(String cartlineId) {
		this.eventValues[4] = cartlineId;
	}

	public String getQuantity() {
		return this.eventValues[5];
	}

	public void setQuantity(String quantity) {
		this.eventValues[5] = quantity;
    }
	
	public String getSalesUnit() {
		return this.eventValues[6];
	}

	public void setSalesUnit(String salesUnit) {
		this.eventValues[6] = salesUnit;
	}
	
	public String getConfiguration() {
		return this.eventValues[7];
	}

	public void setConfiguration(String configuration) {
		this.eventValues[7] = configuration;
	}
	
	/**
	 *  Set the category id for the YMAL product was obtained from.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @param catId the id of the category displayed when the TX YMAL was
	 *         added to the cart.
	 */
	public void setYmalCategory(String catId) {
		eventValues[8] = catId;
	}
	
	/**
	 *  Get the category id for the YMAL product was obtained from.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @return the id of the category displayed when the TX YMAL was
	 *         added to the cart.
	 */
	public String getYmalCategory() {
		return eventValues[8];
	}	

	/**
	 *  Set the originating product for the YMAL product added to the cart.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @param originatingProductId the id of the product for which the TX YMAL
	 *         item was displayed for.
	 */
	public void setOriginatingProduct(String originatingProductId) {
		eventValues[9] = originatingProductId;
	}
	
	/**
	 *  Get the originating product for the YMAL product added to the cart.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @return the id of the product for which the TX YMAL item was displayed for.
	 */
	public String getOriginatingProduct() {
		return eventValues[9];
	}	

	/**
	 *  Set the YMAL set id for the YMAL product was obtained from.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @param ymalSetId the id of the YmalSet object from which the TX YMAL
	 *         product was shown when added to the cart.
	 */
	public void setYmalSet(String ymalSetId) {
		eventValues[10] = ymalSetId;
	}
	
	/**
	 *  Get the YMAL set id for the YMAL product was obtained from.
	 *  Only applicable for transactional YMAL items, added from a YMAL display.
	 *  
	 *  @return the id of the YmalSet object from which the TX YMAL
	 *         product was shown when added to the cart.
	 */
	public String getYmalSet() {
		return eventValues[10];
	}	

	/**
	 *  Get the Customer Created List id, if the item selected was
	 *  selected from a Customer Created List.
	 *  
	 *  @return the id of the Customer Created List this item was selected from,
	 *          or null if the item was not selected from a CCL.
	 */
	public String getCclId() {
		return eventValues[11];
	}
	
	/**
	 *  Set the Customer Created List id, if the item selected was
	 *  selected from a Customer Created List.
	 *  
	 *  @param cclId the id of the Customer Created List the item was selected
	 *         from.
	 */
	public void setCclId(String cclId) {
		eventValues[11] = cclId;
	}
}
