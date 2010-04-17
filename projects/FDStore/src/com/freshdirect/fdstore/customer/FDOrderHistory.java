package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpOrderHistory;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderInfoAdapter;

public class FDOrderHistory extends ErpOrderHistory {

	private static final long	serialVersionUID	= -4179034756408883935L;
	
	private final List<FDOrderInfoAdapter> fdOrderInfos;

	public FDOrderHistory(Collection<ErpSaleInfo> erpSaleInfos) {
		super(erpSaleInfos);
		this.fdOrderInfos = new ArrayList<FDOrderInfoAdapter>(erpSaleInfos.size());
		for ( ErpSaleInfo esi : erpSaleInfos ) {
			fdOrderInfos.add( new FDOrderInfoAdapter( esi ) );
		}

	}

	/** @return Collection of FDOrderInfoI */
	public Collection<FDOrderInfoAdapter> getFDOrderInfos() {
		return fdOrderInfos;
	}

	public Collection<FDOrderInfoI> getFDOrderInfos(EnumSaleType saleType) {

		List<FDOrderInfoI> l = new ArrayList<FDOrderInfoI>();
		for (Iterator<FDOrderInfoAdapter> i = this.fdOrderInfos.iterator(); i.hasNext();) {
			FDOrderInfoI o = i.next();
			if(saleType.equals(o.getSaleType())) {
				l.add(o);
			}
		}
		return l;
	}
	/**
	 * @return Collection of FDOrderInfoI where status allows creating make-good
	 */
	public Collection<FDOrderInfoI> getMakeGoodReferenceInfos() {
		List<FDOrderInfoI> l = new ArrayList<FDOrderInfoI>();
		for (Iterator<FDOrderInfoAdapter> i = this.fdOrderInfos.iterator(); i.hasNext();) {
			FDOrderInfoI o = i.next();
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
	
	/**
	 * This method returns a list of orders that used the given delivery pass.
	 * @ return Collection of FDOrderInfoI.
	 */
	public Collection<FDOrderInfoI> getDlvPassOrderInfos(String dlvPassId) {
		List<FDOrderInfoI> l = new ArrayList<FDOrderInfoI>();
		for (Iterator<FDOrderInfoAdapter> i = this.fdOrderInfos.iterator(); i.hasNext();) {
			FDOrderInfoI o = i.next();
			if (o.getDlvPassId() != null && o.getDlvPassId().equals(dlvPassId)) {
				//This Order used the delivery pass. So add it to the list.
				l.add(o);				
			}
		}
		return l;
	}
		
}