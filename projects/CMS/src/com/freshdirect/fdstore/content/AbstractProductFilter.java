/*
 * Created on Jun 14, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

/**
 * @author rgayle
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractProductFilter implements ProductFilterI {
	public abstract boolean applyTest(ProductModel prod) throws FDResourceException;
	
	public List apply(Collection productNodes) throws FDResourceException {
		List rtnProds = new ArrayList();
		 for (Iterator pi=productNodes.iterator(); pi.hasNext(); ) {
			ProductModel prodModel = (ProductModel) pi.next();
			if (applyTest(prodModel)) {
				rtnProds.add(prodModel);
			}
		}
		return rtnProds;
	}
}
