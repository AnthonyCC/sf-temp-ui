package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

/**
 * @author rgayle
 */
public abstract class AbstractProductFilter implements ProductFilterI {
	public abstract boolean applyTest(ProductModel prod) throws FDResourceException;
	
	public List<ProductModel> apply(Collection<ProductModel> productNodes) throws FDResourceException {
		List<ProductModel> rtnProds = new ArrayList<ProductModel>();
		 for ( ProductModel prodModel : productNodes ) {
			if (applyTest(prodModel)) {
				rtnProds.add(prodModel);
			}
		}
		return rtnProds;
	}
}
