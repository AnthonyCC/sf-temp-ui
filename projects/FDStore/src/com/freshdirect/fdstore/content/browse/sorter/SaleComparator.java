package com.freshdirect.fdstore.content.browse.sorter;

import java.util.Comparator;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SaleComparator implements Comparator<FilteringProductItem> {

	private static final Logger LOGGER = LoggerFactory.getInstance( SaleComparator.class ); 
	
	@Override
	public int compare(FilteringProductItem o1, FilteringProductItem o2) {
    	
		int p1 = 0;
		int p2 = 0;
        String zoneId = ContentFactory.getInstance().getCurrentPricingContext().getZoneId();            

		try {            
            FDProductInfo i1 = o1.getFdProductInfo();
            p1 = i1.getZonePriceInfo(zoneId).getHighestDealPercentage();
        } catch (FDException e) {
        	LOGGER.error(e);
        }
    	
    	try {
            FDProductInfo i2 = o2.getFdProductInfo();
            p2 = i2.getZonePriceInfo(zoneId).getHighestDealPercentage();
        } catch (FDException e) {
        	LOGGER.error(e);
        }
    	
       	return p1 > p2 ? -1 : (p1 < p2 ? 1 : 0); //default is desc
	}
}
