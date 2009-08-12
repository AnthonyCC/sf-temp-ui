package com.freshdirect.webapp.util;

import java.util.Iterator;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartonDetail;


/**
 * This iterator iterates over orderlines of an order either
 * by their natural order or by carton groups.
 * 
 * Initialize with a simple orderline iterator or instance of CartonComplaintsIterator
 * 
 * @author segabor
 *
 */
public class OLIterator implements Iterator {
	Iterator _it;
	
	String lastDept;
	String lastCartonNumber;
	FDCartonDetail lastCartonDetail;
	boolean isDeptChanged = false;
	boolean isCartonNumberChanged = false;

	final boolean isCartonized;

	public OLIterator(Iterator it) {
		this._it = it;
		this.isCartonized = (this._it instanceof CartonComplaintsIterator);
	}


	public boolean hasNext() {
		return _it.hasNext();
	}

	public Object next() {
		return nextOrderLine();
	}

	public FDCartLineI nextOrderLine() {
		FDCartLineI obj;

		if (isCartonized) {
			lastCartonDetail = ((CartonComplaintsIterator)_it).nextDetail();

			// adjust cart name
			String cn = lastCartonDetail.getCartonInfo().getCartonInfo().getCartonNumber();
			isCartonNumberChanged = lastCartonNumber == null || !cn.equalsIgnoreCase(lastCartonNumber);
			lastCartonNumber = cn;

			obj = lastCartonDetail.getCartLine();
		} else {
			obj = (FDCartLineI) _it.next();
		}

		// adjust dept name
		String deptName = obj.getDepartmentDesc();
		isDeptChanged = lastDept == null || !deptName.equalsIgnoreCase(lastDept);
		lastDept = deptName;
		
		return obj;
	}
	
	public void remove() {
		// skip
	}

	public boolean isCartonized() {
		return this.isCartonized;
	}
	
	public String getLastDepartmentName() {
		return lastDept;
	}
	
	public String getLastCartonNumber() {
		return lastCartonNumber;
	}

	public boolean isDepartmentNameChanged() {
		return isDeptChanged;
	}

	public boolean isCartonNumberChanged() {
		return isCartonized && isCartonNumberChanged;
	}


	public FDCartonDetail getLastCartonDetail() {
		if (isCartonized) {
			return lastCartonDetail;
		}
		return null;
	}
}
