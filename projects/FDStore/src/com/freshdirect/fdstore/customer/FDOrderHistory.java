package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderInfoAdapter;

public class FDOrderHistory extends ErpOrderHistory {

	private final List fdOrderInfos;

	public FDOrderHistory(Collection erpSaleInfos) {
		super(erpSaleInfos);
		this.fdOrderInfos = new ArrayList(erpSaleInfos.size());
		for (Iterator i = erpSaleInfos.iterator(); i.hasNext();) {
			fdOrderInfos.add(new FDOrderInfoAdapter((ErpSaleInfo) i.next()));
		}

	}

	/** @return Collection of FDOrderInfoI */
	public Collection getFDOrderInfos() {
		return fdOrderInfos;
	}

	/**
	 * @return Collection of FDOrderInfoI where status allows creating make-good
	 */
	public Collection getMakeGoodReferenceInfos() {
		List l = new ArrayList();
		for (Iterator i = this.fdOrderInfos.iterator(); i.hasNext();) {
			FDOrderInfoI o = (FDOrderInfoI) i.next();
			EnumSaleStatus s = o.getOrderStatus();
			if (EnumSaleStatus.NEW.equals(s)
				|| EnumSaleStatus.SUBMITTED.equals(s)
				|| EnumSaleStatus.CANCELED.equals(s)
				|| EnumSaleStatus.NOT_SUBMITTED.equals(s)) {
				continue;
			}
			l.add(o);	
		}
		return l;
	}

}