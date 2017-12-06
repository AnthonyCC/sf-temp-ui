package com.freshdirect.fdstore.customer;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.storeapi.content.ProductModel;

public interface FDInvoiceLineI extends ErpInvoiceLineI{
	
	public UserContext getUserContext();

	//return the ProductModel of the substituted sku
//	public ProductModel getSubstituteProduct();
	
	//return only the Product Name of the substituted sku
	public String getSubstituteProductName();
	
	//return the default price of the substituted sku
	public String getSubstituteProductDefaultPrice();
	
	public String getSubstituteProductId();

	}