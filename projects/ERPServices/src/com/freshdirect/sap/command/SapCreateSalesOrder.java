/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.common.customer.PaymentMethodI;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiSalesOrderCreate;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapCreateSalesOrder extends SapCommandSupport implements SapOrderCommand {

	private final SapOrderI order;

	private String sapOrderNumber;
	
	private String invoiceNumber;
	
	private EnumSaleType saleType;

	public SapCreateSalesOrder(SapOrderI order,EnumSaleType saleType) {
		this.order = order;
		this.saleType=saleType;
	}

	public String getWebOrderNumber() {
		return this.order.getWebOrderNumber();
	}

	public String getSapOrderNumber() {
		return this.sapOrderNumber;
	}
	
	public EnumSaleType getSaleType() {
		return saleType;
	}

	public void execute() throws SapException {
		BapiSalesOrderCreate bapi = null;
		if(saleType != null) {
			bapi=BapiFactory.getInstance().getSalesOrderCreateBuilder(saleType);
		} else {
			bapi=BapiFactory.getInstance().getSalesOrderPlantChangeBuilder();
		}
		SalesOrderHelper helper = new SalesOrderHelper(bapi);

		this.buildOrderHeader(bapi,saleType);
		helper.buildOrderText(order);
		//this.buildCreditCard( order.getCustomer().getCreditCard() );
		helper.buildCreditMemos(order.getCreditMemos());

		if (order.isFreeOrder()) {
			helper.markAsFreeOrder();
		}
		helper.buildConditions(order, true);

		OrderLineHelper olh = new OrderLineHelper(order.getWebOrderNumber(), order.getRequestedDate(), SalesOrderHelper
			.getOrderLineArray(order));
		olh.buildOrderLines(bapi, false);

		helper.buildPartners(order);

		this.invoke(bapi);

		this.sapOrderNumber = ((BapiSalesOrderCreate) bapi).getSalesDocument();
		if(EnumSaleType.SUBSCRIPTION.equals(saleType) || EnumSaleType.GIFTCARD.equals(saleType) || EnumSaleType.DONATION.equals(saleType)) {
			this.invoiceNumber=((BapiSalesOrderCreate) bapi).getInvoiceNumber();
		}
		if (this.sapOrderNumber == null) {
			throw new SapException("No salesdocument number in response");
		}
	}

	protected void buildOrderHeader(BapiSalesOrderCreate bapi,final EnumSaleType saleType) {
		final Date currentDate = new Date();
		final PaymentMethodI cc = order.getCustomer().getPaymentMethod();
		String referenceOrder=null;

		bapi.setOrderHeaderIn(new BapiSalesOrderCreate.OrderHeaderIn() {
			public String getSalesOrg() {
				
				String salesOrg=order.getSalesOrg();
				/*if("1000".equals(salesOrg))
					return "0001";
				else if("2000".equals(salesOrg))
					return "1300";
				else if("3000".equals(salesOrg))
					return "X200";*/
				return salesOrg;
			}

			public String getDistrChan() {
				String distChannel=order.getDistributionChannel();
				/*if("1000".equals(distChannel))
					return "01";
				else if("2000".equals(distChannel))
					return "02";
				else if("3000".equals(distChannel))
					return "03";*/
				return distChannel;
			}

			public String getDivision() {
				return SapProperties.getDivision();
			}

			public String getDocumentType() {
				if(EnumSaleType.REGULAR.equals(saleType)){
					return "ZOR";
				}
				else if(EnumSaleType.SUBSCRIPTION.equals(saleType) || EnumSaleType.GIFTCARD.equals(saleType) || EnumSaleType.DONATION.equals(saleType)){
					return "XOR";
				}
				else {
					return "";
				}
			}

			public String getPurchaseOrderNumber() {
				return order.getWebOrderNumber();
			}

			public String getCollectiveNo() {
				// delivery zone
				String dlvZone = order.getDeliveryZone();
				if (dlvZone.length() > 10) {
					// limit length to 10 chars
					dlvZone = dlvZone.substring(0, 10);
				}
				return dlvZone;
			}

			public String getCustGrp1() {
				
					return order.getDeliveryModel();
				
			}

			public String getRef1() {
				// Delivery Start & End time as HHmmssHHmmss (eg 120000125959)
					String dlvStart = QuickDateFormat.SHORT_TIME_FORMATTER.format(order.getDeliveryStartTime());
	
					Calendar cal = Calendar.getInstance();
					cal.setTime(order.getDeliveryEndTime());
					cal.add(Calendar.SECOND, -1);
					String dlvEnd = QuickDateFormat.SHORT_TIME_FORMATTER.format(cal);
	
					return dlvStart + dlvEnd;
			}

			public String getRef1S() {
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
					return df.format(order.getCutoffTime());
			}

			public String getPoSupplement() {
				return cc.getType().getSapName();
			}

			public Date getDunDate() {
				return cc.getExpiration();
			}

			public String getName() {
				return cc.getName();
			}

			public String getPurchNoS() {
				return cc.getNumber();
			}

			public Date getRequestedDate() {
				return order.getRequestedDate();
			}

			public Date getPricingDate() {
				return currentDate;
			}

			public Date getCurrentDate() {
				return currentDate;
			}

			public String getTelephone() {
				return order.getCustomerStarRating();
			}

			public String getCustGrp3() {
				return order.getChefsTableMembership();
			}

			public String getCustGrp2() {
				return order.getProfileSegment();
			}

			public String getOrdReason() {
				return order.isFreeOrder() ? "009" : "";
			}

		});

//		System.err.println("DeliveryRegionId: " + order.getDeliveryRegionId());

		String billingRef = StringUtils.left(NVL.apply(order.getBillingRef(), "").trim(), 20);
		billingRef = StringUtils.rightPad(billingRef, 20);
		
		String recipeFlag = StringUtils.rightPad(order.isRecipeOrder() ? "1" : " ", 5);
		
		String goGreen = StringUtils.rightPad(order.getCustomer().isGoGreen() ? "GREEN" : " ", 5);
		String gcAmount = StringUtils.rightPad(String.valueOf(order.getGcAmount()), 10);
		
		// APPDEV-5314 to send un attended delivery flg to SAP
		String unattendedDeliveryFlg=SalesOrderHelper.populateUnattendedDlvFlg(order.getDeliveryAddress().getUnattendedDeliveryFlag());

		
		if(order.isAddOnOrder())
			referenceOrder=order.getReferenceOrderId();
		
		if(referenceOrder==null)
			referenceOrder = StringUtils.rightPad(NVL.apply(referenceOrder, "").trim(), 20);
		
			String emptyStr31Chars = StringUtils.rightPad("", 31); 
		
		// 10 spaces + 5 (flag) + 5 (GREEN) + 65 spaces + 20 chars
		bapi.addExtension("BAPE_VBAK", StringUtils.repeat(" ", 10)
			+ recipeFlag
			+ goGreen
			+ unattendedDeliveryFlg
			+ StringUtils.repeat(" ", 10)
			+ gcAmount // offset 36-45
			+ StringUtils.repeat(" ", 40)
			+ billingRef  // offset 86 --> 105
			+ StringUtils.repeat(" ", 20) // offset 106 - 125
			+ StringUtils.left(NVL.apply(order.getDeliveryRegionId(), "").trim(), 20)  // offset 126  --> 145
			+ emptyStr31Chars
			+ referenceOrder);

	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

}