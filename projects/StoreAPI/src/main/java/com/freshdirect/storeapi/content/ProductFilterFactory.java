/*
 * Created on May 19, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.storeapi.CmsLegacy;


/**
 * @author rgayle
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
@CmsLegacy
public class ProductFilterFactory {
	private static Map<String, ProductFilterI> prodFilterMap = new HashMap<String, ProductFilterI>();

	private static ProductFilterFactory instance = new ProductFilterFactory();

    private final static ProductFilterI AVAILABLE_ITEMS_CONTEXTFILTER = new ProductAvailabilityByContextFilter();

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
                ProductFilterI prodFilter = prodFilterMap.get(iFNames.next().toUpperCase());
                if (prodFilter != null) {
                    prodFilters.add(prodFilter);
                }
            }
        }

        return prodFilters;
    }

    public List<ProductFilterI> getDefaultFilters() {
    	List<ProductFilterI> prodFilters = new ArrayList<ProductFilterI>();
    	prodFilters.add(AVAILABLE_ITEMS_CONTEXTFILTER);
    	return prodFilters;
    }
}
