package com.freshdirect.webapp.util;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartonDetail;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDOrderI;

/**
 * Special iterator that iterates cart lines wrapped in carton details over
 * the two-level structure of an order.
 * 
 * @author segabor
 *
 */
public class CartonComplaintsIterator implements Iterator {
	FDOrderI order;

	int currentCartonIndex = 0; // first level
	Iterator cartonDetailIterator; // second level
	
	public CartonComplaintsIterator(FDOrderI order) {
		this.order = order;
		
		this.currentCartonIndex = 0;
		relocate();
	}

	/**
	 * Position to the next node that has one or more children.
	 */
	private boolean relocate() {
		while (currentCartonIndex < order.getCartonContents().size()) {
			FDCartonInfo inf = (FDCartonInfo) order.getCartonContents().get(currentCartonIndex);
			if (inf.getCartonDetails().size() > 0) {
				cartonDetailIterator = inf.getCartonDetails().iterator();
				return true;
			}
			currentCartonIndex++;
		}
		
		return false;
	}
	
	
	public boolean hasNext() {
		if (cartonDetailIterator != null && cartonDetailIterator.hasNext())
			return true;
		currentCartonIndex++;
		return relocate();
	}


	/**
	 * @return FDCartonDetail
	 */
	public FDCartonDetail nextDetail() {
		if (!hasNext())
			return null;

		FDCartonDetail cDetail = (FDCartonDetail)  cartonDetailIterator.next();
		return cDetail;
	}


	/**
	 * @return FDCartLineI
	 */
	public Object next() {
		FDCartonDetail d = nextDetail();
		if (d != null)
			return d.getCartLine();
		return null;
	}

	/**
	 * Returns the count of all available items.
	 */
	public int size() {
		int sum = 0;
		for (int k=0; k<order.getCartonContents().size();k++) {
			sum+= ((FDCartonInfo) order.getCartonContents().get(k)).getCartonDetails().size();
		}
		return sum;
	}
	
	
	public void remove() {
		// skip ...
	}
}
