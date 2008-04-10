/*
 * $Workfile:OrderSummaryTag.java$
 *
 * $Date:6/30/2003 5:27:09 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.callcenter;

import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.customer.CCOrderSummaryI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.customer.EnumSaleType;
/**
 *
 * @version $Revision:8$
 * @author $Author:Kashif Nadeem$
 */
public class OrderSummaryTag extends com.freshdirect.framework.webapp.BodyTagSupport{

	private FDOrderI order;
	private String id;

	public void setOrder(FDOrderI o) {
		this.order = o;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public int doStartTag() throws JspException {
		pageContext.setAttribute(this.id, new OrderSummary());
		return EVAL_BODY_BUFFERED;
	}
	
	private class OrderSummary implements CCOrderSummaryI {

		public String getOrderId() {
			return order.getErpSalesId();
		}

		public double getOrderTotal() throws PricingException {
			if(order.hasInvoice()){
				return order.getInvoicedTotal();
			}else{
				return order.getTotal();
			}
		}

		public String getOrderStatus() {
			if(order instanceof FDOrderAdapter){
				return ((FDOrderAdapter)order).getSaleStatus().getName();
			}else{
				return order.getOrderStatus().getDisplayName();
			}
		}

		public Date getDeliveryDate() {
			return order.getRequestedDate();
		}

		public Date getCreateDate() {
			return order.getDatePlaced();
		}

		public String getCreatedBy() {
		
			return order.getOrderSource().equals(EnumTransactionSource.CUSTOMER_REP) 
			? (order.getTransactionInitiator()!=null 
				 ? order.getTransactionInitiator() : "CSR" )
			: "CUSTOMER";
		}

		public String getCreateSource() {
			return order.getOrderSource().getName();
			
		}

		public Date getLastModifiedDate() {
			return order.getLastModifiedDate();
		}

		public String getLastModifiedBy() {
			return EnumTransactionSource.CUSTOMER_REP.equals(order.getOrderSource("LAST_MODIFIED")) 
			    ? (order.getTransactionInitiator()!=null ? order.getTransactionInitiator("LAST_MODIFIED") : "CSR" )
			    : (EnumTransactionSource.SYSTEM).equals(order.getOrderSource("LAST_MODIFIED")) 
			       ? "SYSTEM" 
			       : "CUSTOMER";
		}

		public String getLastModifiedSource() {
			return order.getOrderSource("LAST_MODIFIED").getName();
		}

		public String creditIssued() {
			return (order.hasCreditIssued() > 0) ? ((order.hasCreditIssued() == 1) ? "YES" : "PENDING" ) : "NO";
		}
		
		public EnumSaleType getOrderType(){
			return order.getOrderType();
		}
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
				"com.freshdirect.fdstore.customer.CCOrderSummaryI", true, VariableInfo.NESTED)
			};
		}
	}
} 
