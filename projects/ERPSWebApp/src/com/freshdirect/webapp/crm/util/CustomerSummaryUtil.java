package com.freshdirect.webapp.crm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class CustomerSummaryUtil {
	public static final String ERP_CUSTOMER_PAGE_CACHE = "erpCustomer.page.cache";

	HttpServletRequest request;
	FDUserI				user;

	private ErpCustomerModel erpCustomer;


	public CustomerSummaryUtil(HttpServletRequest request, FDUserI user) throws FDResourceException {
		this.request = request;
		this.user = user;
		
		/* retrieve ErpCustomer (code borrowed from CrmGetErpCustomerTag) */
		{
			erpCustomer = (ErpCustomerModel) request.getAttribute(ERP_CUSTOMER_PAGE_CACHE);
			if(erpCustomer == null || !erpCustomer.getPK().getId().equals(user.getIdentity().getErpCustomerPK())){
				erpCustomer = FDCustomerFactory.getErpCustomer(this.user.getIdentity());
				request.setAttribute(ERP_CUSTOMER_PAGE_CACHE, erpCustomer);
			}
		}
	}


	

	public ErpCustomerInfoModel getCustomerInfo() {
		return erpCustomer.getCustomerInfo();
	}


	/* @return List<CrmCaseModel> */
	public List<CrmCaseModel> getRecentCases(int n) throws FDResourceException {
		CrmCaseTemplate template = new CrmCaseTemplate();
		template.setCustomerPK( new PrimaryKey(user.getIdentity().getErpCustomerPK()));
		// template.setSortBy(request.getParameter("sortBy"));
		// template.setSortOrder(request.getParameter("sortOrder"));
		template.setStartRecord(0);
		template.setEndRecord(n);
		
		List<CrmCaseModel> recentCases = CrmSession.findCases(this.request.getSession(), template);
		
		return recentCases;
	}

	public static final Comparator<FDOrderInfoI> COMP_BY_PLACE_DATE = new Comparator<FDOrderInfoI>() {
		public int compare(FDOrderInfoI arg0, FDOrderInfoI arg1) {
			FDOrderInfoI o1 = (FDOrderInfoI) arg0;
			FDOrderInfoI o2 = (FDOrderInfoI) arg1;

			// revers order
			return -1*(o1.getCreateDate().compareTo(o2.getCreateDate() ) );
		}
	};

	/* @return List<FDOrderInfoI> */
	public List<FDOrderInfoI> getRecentOrders(int n) throws FDResourceException {
		List<FDOrderInfoI> orderInfos = new ArrayList<FDOrderInfoI>( ((FDOrderHistory)user.getOrderHistory()).getFDOrderInfos() );

		// sort orders in descent order
		Collections.sort(orderInfos, COMP_BY_PLACE_DATE);

		// return the first N recent items if more than N in the list
		return orderInfos.size() > n ? orderInfos.subList(0, n) : orderInfos;
	}

	public FDOrderInfoI getFDOrderInfo(String orderId) throws FDResourceException {
		return  ((FDOrderHistory)user.getOrderHistory()).getFDOrderInfo(orderId) ;
	}
	
	/* @return late issue for the specific order. Can be null */
	public CrmLateIssueModel getLateIssueForOrder(FDOrderI order) throws FDResourceException {
		return order != null ?
				CrmManager.getInstance().getRecentLateIssueForOrder(order.getErpSalesId()) : null;
	}

	

	public FDOrderInfoI getLastOrderInfo() throws FDResourceException {
		List<FDOrderInfoI> orderInfos = new ArrayList<FDOrderInfoI>( ((FDOrderHistory)user.getOrderHistory()).getFDOrderInfos() );

		// sort orders in descent order
		Collections.sort(orderInfos, COMP_BY_PLACE_DATE);
		return (FDOrderInfoI) orderInfos.get(0);
	}


	String sourceToMethod(EnumTransactionSource source) {
		switch(source) {
		case CUSTOMER_REP:
			return "CSR";
		case SYSTEM:
			return "SYSTEM";
		case STANDING_ORDER:
			return "STANDING ORDER";
		default:
			return "CUSTOMER";
		}

		/* return EnumTransactionSource.CUSTOMER_REP.equals(source) ?
				"CSR" :
				EnumTransactionSource.SYSTEM.equals(source) ?
						"SYSTEM" : "CUSTOMER"; */
	}
    
    public String getCreatedBy(FDOrderInfoI order) {
		return sourceToMethod(order.getOrderSource()) +
			(order.getCreatedBy() != null ? " / "+order.getCreatedBy() : "");
    }





    
	static final Comparator<CrmCaseAction> COMP_ACTIONS_BY_RECENCY = new Comparator<CrmCaseAction>() {
		public int compare(CrmCaseAction arg0, CrmCaseAction arg1) {
			CrmCaseAction o1 = (CrmCaseAction) arg0;
			CrmCaseAction o2 = (CrmCaseAction) arg1;

			// reverse order
			return -1*(o1.getTimestamp().compareTo(o2.getTimestamp() ) );
		}
	};


    public List<CrmCaseAction> getRecentActions(String caseId, int maxActions) throws FDResourceException {
		CrmCaseModel thisCase = CrmManager.getInstance().getCaseByPk(caseId);
		
		List<CrmCaseAction> actions = new ArrayList<CrmCaseAction>(thisCase.getActions());
		// sort orders in descent order
		Collections.sort(actions, COMP_ACTIONS_BY_RECENCY);
    	
		// return the first N recent items if more than N in the list
		return actions.size() > maxActions ? actions.subList(0, maxActions) : actions;
    }

    public int getNumberOfActions(String caseId) throws FDResourceException {
		CrmCaseModel thisCase = CrmManager.getInstance().getCaseByPk(caseId);
		return thisCase.getActions().size();
    }
    

    public FDOrderI getLastDeliveredOrder() {
    	FDOrderI anOrder = null;
    	
    	try {
			String saleId = CrmManager.getInstance().getLastDeliveredOrder(user.getIdentity().getErpCustomerPK());

			if (saleId != null)
				anOrder = FDCustomerManager.getOrder( saleId );
		} catch (FDResourceException e) {
		}

    	return anOrder;
    }


    public ErpComplaintModel getTheMostImportantComplaint(FDOrderI anOrder) {
    	ErpComplaintModel c = null;
    	int pri = 0;
    	
    	for (ErpComplaintModel aComplaint : anOrder.getComplaints()) {
    		if (!EnumComplaintStatus.APPROVED.equals(aComplaint.getStatus()))
    			continue;

    		int complaintPriority = aComplaint.getPriority();
    		if (complaintPriority > pri) {
    			c = aComplaint;
    			pri = complaintPriority;
    		}
    	}

    	return c;
    }


    /**
     * Returns reasons for a complaint reverse ordered by their priorities
     * @param comp
     * @return List<ErpComplaintReason>
     */
    public List<ErpComplaintReason> getSortedReasons(ErpComplaintModel comp) {
    	Set<ErpComplaintReason> reasons = new HashSet<ErpComplaintReason>();
    	
    	for (Iterator<ErpComplaintLineModel> it=comp.getComplaintLines().iterator(); it.hasNext();) {
    		ErpComplaintLineModel line = it.next();
    		
    		reasons.add(line.getReason());
    	}

    	List<ErpComplaintReason> sortedReasons = new ArrayList<ErpComplaintReason>(reasons);
    	
    	Collections.sort(sortedReasons, new Comparator<ErpComplaintReason>() {
			public int compare(ErpComplaintReason arg0, ErpComplaintReason arg1) {
				final int i1 = ((ErpComplaintReason) arg0).getPriority();
				final int i2 = ((ErpComplaintReason) arg1).getPriority();
				return i1 < i2 ? 1 : i1 > i2 ? -1 : 0;
			}
		});

    	return sortedReasons;
    }
}
