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

@CmsLegacy
public class ProductFilterFactory {

	private static final Map<String, ProductFilterI> PRODUCT_FILTER_MAP = new HashMap<String, ProductFilterI>();
    private static final ProductFilterI AVAILABLE_ITEMS_CONTEXTFILTER = new ProductAvailabilityByContextFilter();

    private static final ProductFilterFactory INSTANCE = new ProductFilterFactory();

	public static ProductFilterFactory getInstance() {
		return INSTANCE;
	}

	private ProductFilterFactory() {
		// load map with the various product filters.
	    PRODUCT_FILTER_MAP.put("ORGANIC",new OrganicFilter());
	    PRODUCT_FILTER_MAP.put("KFP",new KosherForPassoverFilter());
		/** add additional filters here **/
	}

    public List<ProductFilterI> getFilters(List<String> filterNames) {
        List<ProductFilterI> prodFilters = new ArrayList<ProductFilterI>();

        if (filterNames != null && !filterNames.isEmpty()) {
            for (Iterator<String> iFNames = filterNames.iterator(); iFNames.hasNext();) {
                ProductFilterI prodFilter = PRODUCT_FILTER_MAP.get(iFNames.next().toUpperCase());
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
