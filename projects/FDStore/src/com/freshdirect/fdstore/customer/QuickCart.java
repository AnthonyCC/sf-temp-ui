/*
 * $Workfile:FDCartModel.java$
 *
 * $Date:8/23/2003 7:26:19 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ProductModel;

/**
 * QuickCart class.
 *
 * @version	$Revision:$
 * @author	 $Author:$
 */
public class QuickCart implements FDProductCollectionI {

	private final List orderLines = new ArrayList();
	private String orderId = null;
	private Date deliveryDate = null;
	private String productType="";
	private String name = null;
	private String userZoneId=null;
	
	public static final String PRODUCT_TYPE_CCL="CCL";
	public static final String PRODUCT_TYPE_PRD="PRODUCT";
	public static final String PRODUCT_TYPE_STARTER_LIST="STARTER_LIST";
	

	public QuickCart() {
		super();
	}
	
	public void setName(String name) {
	   this.name = name;
	}
	
	public String getName() { return name; }
	

	public void setOrderId(String oid) {
		this.orderId = oid;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public boolean isEveryItemEverOrdered() {
		return "every".equalsIgnoreCase(this.orderId);
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDeliveryDate() {
		return this.deliveryDate;
	}

	public void addProduct(FDProductSelectionI orderLine) {
		if(getUserZoneId()!=null) orderLine.setPricingContext(new PricingContext(getUserZoneId()));
		
		this.orderLines.add(orderLine);
		//this.reSort();
	}

	public void addProducts(Collection cartLines) {
		this.orderLines.addAll(cartLines);
	}

	public int numberOfProducts() {
		return this.orderLines.size();
	}

	public int numberOfProducts(String deptId) {
		return this.getProducts(deptId).size();
	}

	public FDProductSelectionI getProduct(int index) {
		return (FDProductSelectionI) this.orderLines.get(index);
	}

	public FDProductSelectionI getProduct(int index, String deptId) {
		return (FDProductSelectionI) this.getProducts(deptId).get(index);
	}

	// List<FDProductSelectionI>
	public void setProducts(List lines) {
		this.orderLines.clear();
		for (Iterator i = lines.iterator(); i.hasNext();) {
			FDProductSelectionI product = (FDProductSelectionI) i.next();
			if(getUserZoneId()!=null) product.setPricingContext(new PricingContext(getUserZoneId()));
			this.orderLines.add(product);
		}
		this.sort(PRODUCT_COMPARATOR);
	}

	public void sort(Comparator comparator) {
		Collections.sort(orderLines, comparator);
	}

	public void setProduct(int index, FDProductSelectionI orderLine) {
		throw new UnsupportedOperationException("QuickCart.setProduct(int,FDProductSelectionI)");
	}

	public void zeroAllQuantities() {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDProductSelectionI product = (FDProductSelectionI) i.next();
			if (product.isSoldBySalesUnits()) {
				product.setSalesUnit("");
			} else {
				product.setQuantity(0.0);
			}
		}
	}

	public void removeProduct(int index) {
		this.orderLines.remove(index);
	}

	public void clearProducts() {
		this.orderLines.clear();
	}

	// List<FDProductSelectionI>
	public List getProducts() {
		return Collections.unmodifiableList(this.orderLines);
	}

	// List<FDProductSelectionI>
	public List getProducts(String deptId) {
		List deptProducts = new ArrayList();
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDProductSelectionI productSelection = (FDProductSelectionI) i.next();
			ProductModel product = productSelection.lookupProduct();
			if (product.getDepartment().getContentName().equalsIgnoreCase(deptId)) {
				deptProducts.add(productSelection);
			}
		}
		return Collections.unmodifiableList(deptProducts);
	}

	private final static Comparator PRODUCT_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			///order by Department and then by product.
			FDProductSelectionI product1 = (FDProductSelectionI) o1;
			FDProductSelectionI product2 = (FDProductSelectionI) o2;
			
			int retValue = product1.getDepartmentDesc().compareTo(product2.getDepartmentDesc());
			if (retValue == 0) {
				retValue = product1.getDescription().compareTo(product2.getDescription());
				if (retValue == 0) {
					retValue = product1.getConfigurationDesc().compareTo(product2.getConfigurationDesc());
					if (retValue == 0) { //dept * desc * configDesc matches, check quantity
						if (product1.getQuantity() <= product2.getQuantity()) {
							retValue = -1;
						} else {
							retValue = 1;
						}
					}
				}
			}
			return retValue;
		}
	};

	public String getProductType() {
		return productType;
	}

	public boolean isEmpty() { return orderLines.size() == 0; }

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("QuickCart:type=").append(productType);
		if (name != null) sb.append(",name=").append(name);
		if (orderId != null) sb.append(",id=").append(orderId);
		sb.append(",itemCount=").append(orderLines.size());
		return sb.toString();
	}

	public String getUserZoneId() {
		return userZoneId;
	}

	public void setUserZoneId(String userZoneId) {
		this.userZoneId = userZoneId;
	}

}