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
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.common.customer.PaymentMethodI;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.sap.PosexUtil;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapProperties;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiSalesOrderChange;
import com.freshdirect.sap.ejb.SapException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SapChangeSalesOrder extends SapCommandSupport implements SapOrderCommand {

	private final String webOrderNumber;

	/** SAP sales document number */
	private final String salesDocumentNumber;

	private final SapOrderI order;

	public SapChangeSalesOrder(String webOrderNumber, String salesDocumentNumber, SapOrderI order) {
		this.webOrderNumber = webOrderNumber;
		this.salesDocumentNumber = salesDocumentNumber;
		this.order = order;
	}

	public String getWebOrderNumber() {
		return this.webOrderNumber;
	}

	public void execute() throws SapException {

		BapiSalesOrderChange bapi = BapiFactory.getInstance().getSalesOrderChangeBuilder();
		SalesOrderHelper helper = new SalesOrderHelper(bapi);

		this.buildOrderHeader(bapi);

		helper.buildOrderText(order);

		helper.buildCreditMemos(order.getCreditMemos());
		if (order.isFreeOrder()) {
			helper.markAsFreeOrder();
		}
		helper.buildConditions(order, false);

		OrderLineHelper olh = new OrderLineHelper(order.getWebOrderNumber(), order.getRequestedDate(), SalesOrderHelper
			.getOrderLineArray(order));
		olh.buildOrderLines(bapi, false);

		helper.buildPartners(order);

		bapi.setSalesDocumentNumber(this.salesDocumentNumber);

		for (int i = 0; i < order.numberOfOrderLines(); i++) {
			bapi.addOrderItemInX(PosexUtil.getPosex(i));
			bapi.addOrderScheduleInX(PosexUtil.getPosexInt(i));
		}

		int fakePosition = SalesOrderHelper.getFakePositionBase(); //SalesOrderHelper.FAKEPOS_BASE;
		
		

		// for chargelines
		int numCharges = order.getChargeLines().length;
		for (int i = 0; i < numCharges; i++) {
			int pos = fakePosition;
			bapi.addOrderItemInX(PosexUtil.getPosex(pos));
			bapi.addOrderScheduleInX(PosexUtil.getPosexInt(pos));
			fakePosition++;
		}
		// add the "X" for the fake lines
		if (/*order.getDiscount() != null ||*/ (order.getDiscounts() != null && order.getDiscounts().size() > 0)) {
			// for promo
			int count=1;
			for (Iterator iter = order.getDiscounts().iterator(); iter.hasNext();) {
				Discount d=((ErpDiscountLineModel)iter.next()).getDiscount();
				count++;
				bapi.addOrderItemInX(PosexUtil.getPosex(fakePosition));
				bapi.addOrderScheduleInX(PosexUtil.getPosexInt(fakePosition));
				fakePosition++;
			}
			
			// 10 spaces + 6 chars + 75 spaces + 20 Chars
			
			
			bapi.addExtension("BAPE_VBAPX", StringUtils.repeat(" ", 26)	+ "X");
		}

		this.invoke(bapi);

	}
	
	protected void buildOrderHeader(BapiSalesOrderChange bapi) {
		final PaymentMethodI cc = order.getCustomer().getPaymentMethod();

		bapi.setOrderHeaderIn(new BapiSalesOrderChange.OrderHeaderIn() {

			public String getSalesOrg() {
				
				return order.getSalesOrg();
			}

			public String getDistrChan() {
				return order.getDistributionChannel();
				
			}

			public String getDivision() {
				return SapProperties.getDivision();
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
		
		// order no (10) + 75 spaces + billing ref (20)
		String billingRef = StringUtils.left(NVL.apply(order.getBillingRef(), "").trim(), 20);
		billingRef = StringUtils.rightPad(billingRef, 20);
		
		String recipeFlag = StringUtils.rightPad(order.isRecipeOrder() ? "1" : " ", 5);
		
		String goGreen = StringUtils.rightPad(order.getCustomer().isGoGreen() ? "GREEN" : " ", 5);
		String gcAmount = StringUtils.rightPad(String.valueOf(order.getGcAmount()), 10);
	
		String unattendedDeliveryFlg=SalesOrderHelper.populateUnattendedDlvFlg(order.getDeliveryAddress().getUnattendedDeliveryFlag());
		
		// 10 spaces + 5 (flag) + 5 (GREEN) + 65 spaces + 20 chars
		bapi.addExtension("BAPE_VBAK", salesDocumentNumber
			+ recipeFlag
			+ goGreen
			+ unattendedDeliveryFlg
			+ StringUtils.repeat(" ", 10)
			+ gcAmount
			+ StringUtils.repeat(" ", 40)
			+ billingRef
			+ StringUtils.repeat(" ", 20) // offset 106 - 125
			+ StringUtils.left(NVL.apply(order.getDeliveryRegionId(), "").trim(), 20)  // offset 126  --> 145
			);
		
		// order no (10) + X + 9 spaces + X + 1 space + X
		bapi.addExtension("BAPE_VBAKX", salesDocumentNumber
				+ "X" // recipe chg flag
				+ "X" // goGreen flag
				+ "X" // unattendedDeliveryFlag
				+ "X" //gcAmount
				+ StringUtils.repeat(" ", 4)
				+ "X"
				+ " "
				+ "X");

	}



}