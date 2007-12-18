/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */

package com.freshdirect.fdstore.customer;

import java.util.Collection;
import java.util.List;

/**
 * FDProductCollectionI
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public interface FDProductCollectionI {
	
	public void addProduct(FDProductSelectionI Product);
	public void addProducts(Collection cartLines);
	public int numberOfProducts();
	public FDProductSelectionI getProduct(int index);
	public void setProducts(List lines);
	public void setProduct(int index, FDProductSelectionI Product);
	public void removeProduct(int index);
	public void clearProducts();
	public List getProducts();
	
}