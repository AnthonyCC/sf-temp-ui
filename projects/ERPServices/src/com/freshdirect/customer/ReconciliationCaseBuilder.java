package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.framework.core.PrimaryKey;

public class ReconciliationCaseBuilder {

	private final PrimaryKey customerPk;
	private final PrimaryKey salePk;
	private final ErpAbstractOrderModel order;

	private final StringBuffer details = new StringBuffer();

	private double shortedAmount = 0;

	public ReconciliationCaseBuilder(PrimaryKey customerPk, PrimaryKey salePk, ErpAbstractOrderModel order) {
		this.customerPk = customerPk;
		this.salePk = salePk;
		this.order = order;
	}

	public void reconcile(ErpOrderLineModel orderLine, ErpInvoiceLineModel invoiceLine, Boolean isShorted) {
		double reqQty = orderLine.getQuantity();
		double actQty = invoiceLine.getQuantity();
		if (reqQty > actQty) {
			isShorted = true;
			this.shortedAmount += orderLine.getPrice() - invoiceLine.getPrice();

			this.details.append(orderLine.getDescription() + " was shortshipped.");
			this.details.append(" (Req. ").append(reqQty).append(" Act. ").append(actQty).append(")");
			this.details.append("<BR>");
		}
	}
	public List<CrmSystemCaseInfo> getCases() {
		return getCases(null);
	}
	public List<CrmSystemCaseInfo> getCases(CrmCaseSubject subject) {
		if (this.shortedAmount <= (this.order.getSubTotal() * ErpServicesProperties.getCaseShortshipPercentage())) {
			return Collections.<CrmSystemCaseInfo>emptyList();
		}

		List<CrmSystemCaseInfo> cases = new ArrayList<CrmSystemCaseInfo>();

		if (this.details.length() > 0) {
			subject = subject == null ? CrmCaseSubject.getEnum(CrmCaseSubject.CODE_SHORTOUTITEM) : subject;
			CrmSystemCaseInfo info =
				new CrmSystemCaseInfo(
					customerPk,
					this.salePk,
					subject, // Modified to change OIQ-005(Became Obsolete) to OUT-007
					"order #" + this.salePk.getId() + " was shortshipped");
			
			if(this.details.length()>4000)
				info.setNote(this.details.substring(0, 4000));
			else
				info.setNote(this.details.toString());

			cases.add(info);
		}

		return cases;
	}

}
