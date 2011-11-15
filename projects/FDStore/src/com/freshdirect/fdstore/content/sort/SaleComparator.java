package com.freshdirect.fdstore.content.sort;

import java.util.Comparator;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 * Compares content nodes by their sale percentage.
 * 
 * Sorts by highest deal first.
 *
 * It is actually only a wrapper around the smartstore sale comparator.
 * 
 * Ensures that only ProductModels are passed, as the smartstore sale comparator will break if not.
 * 
 * @author treer
 *
 */

public class SaleComparator implements Comparator<ContentNodeModel> {

	private final com.freshdirect.smartstore.sorting.SaleComparator comp = 
			new com.freshdirect.smartstore.sorting.SaleComparator( false, null, null );

	@Override
	public int compare( ContentNodeModel o1, ContentNodeModel o2 ) {
		ProductModel prod1 = o1 instanceof ProductModel ? (ProductModel)o1 : (o1 instanceof SkuModel ? ((SkuModel)o1).getProductModel() : null); 
		ProductModel prod2 = o2 instanceof ProductModel ? (ProductModel)o2 : (o2 instanceof SkuModel ? ((SkuModel)o2).getProductModel() : null);
		
		if ( prod1 == null && prod2 == null ) 
			return 0;
		else if ( prod1 == null )
			return 1;
		else if ( prod2 == null )
			return -1;
		
		return comp.compare( prod1, prod2 );
	}

	@Override
	public String toString() {
		return "Sale";
	}

}
