/*
 * Created on May 19, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author rgayle
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ProductFilterFactory {
	private static Map prodFilterMap = new HashMap();

	private static ProductFilterFactory instance = new ProductFilterFactory();

	public static ProductFilterFactory getInstance() {
		return instance;
	}

	private ProductFilterFactory() {
		// load map with the various product filters.
		prodFilterMap.put("ORGANIC",new OrganicFilter());
		prodFilterMap.put("KFP",new KosherForPassoverFilter());

		/** add additional filters here **/
	}



	/**
	 * @return list of ProductFilterI
	 *
	 */
    public List<ProductFilterI> getFilters(List<String> filterNames) {

        List<ProductFilterI> prodFilters = new ArrayList<ProductFilterI>();

        if (filterNames != null && !filterNames.isEmpty()) {
            for (Iterator<String> iFNames = filterNames.iterator(); iFNames.hasNext();) {
                ProductFilterI prodFilter = (ProductFilterI) prodFilterMap.get(iFNames.next().toUpperCase());
                if (prodFilter != null) {
                    prodFilters.add(prodFilter);
                }
            }
        }

        return prodFilters;

    }

}
