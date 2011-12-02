package com.freshdirect.fdstore.content.sort;

import java.util.Comparator;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;


/**
 * Compares content nodes by their sale percentage.
 * 
 * Sorts by highest deal first.
 *
 * It is based on the smartstore sale comparator, without the additional secondary sortings.
 * 
 * @author treer
 *
 */

public class SaleComparator implements Comparator<ContentNodeModel> {
	
    @Override
    public int compare(ContentNodeModel o1, ContentNodeModel o2) {
    	
		ProductModel prod1 = o1 instanceof ProductModel ? (ProductModel)o1 : (o1 instanceof SkuModel ? ((SkuModel)o1).getProductModel() : null); 
		ProductModel prod2 = o2 instanceof ProductModel ? (ProductModel)o2 : (o2 instanceof SkuModel ? ((SkuModel)o2).getProductModel() : null);
		
		if ( prod1 == null && prod2 == null ) 
			return 0;
		else if ( prod1 == null )
			return 1;
		else if ( prod2 == null )
			return -1;
		
    	boolean unav1 = prod1.isUnavailable();
    	boolean unav2 = prod1.isUnavailable();
    	
    	if ( unav1 && unav2 ) 
    		return 0;
    	else if ( unav1 ) 
    		return 1;
    	else if ( unav2 )
    		return -1;
        	
    	int p1 = 0;
    	int p2 = 0;
    	
    	try {            
            FDProductInfo i1 = prod1.getDefaultSku().getProductInfo();
            String zoneId1 = prod1.getPricingContext().getZoneId();            
            p1 = i1.getZonePriceInfo(zoneId1).getHighestDealPercentage();
        } catch (FDException e) {}
    	
    	try {
            FDProductInfo i2 = prod2.getDefaultSku().getProductInfo();
            String zoneId2 = prod2.getPricingContext().getZoneId();
            p2 = i2.getZonePriceInfo(zoneId2).getHighestDealPercentage();
        } catch (FDException e) {}
    	
       	return p1 > p2 ? -1 : (p1 < p2 ? 1 : 0);

    }
    
	@Override
	public String toString() {
		return "Sale";
	}

}
