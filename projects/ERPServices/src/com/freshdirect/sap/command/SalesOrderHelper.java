/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.command;

import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.address.BasicAddressI;
import com.freshdirect.common.address.BasicContactAddressI;
import com.freshdirect.common.pricing.CreditMemo;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.sap.PosexUtil;
import com.freshdirect.sap.SapChargeLineI;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.SapOrderI;
import com.freshdirect.sap.SapOrderLineI;
import com.freshdirect.sap.bapi.BapiOrder;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class SalesOrderHelper {

	protected final static String FAKE_SAP_PROMO_CODE = "FAKE_SAP_PROMO_CODE";

	private final BapiOrder bapi;
	
	/**
	 * Get the fake the base for the fake lines.
	 * 
	 * The fake lines are calculated from from the cart line limit (set in erpservices.properties)
	 * and rounded to the next hundred. e.g. 200 -> 200, but 201 -> 300
	 * @return fake line base
	 */
	protected static int getFakePositionBase() {
		int pos =  ErpServicesProperties.getCartOrderLineLimit();
		return pos % 100 == 0 ? pos : pos + 100 - (pos % 100);
	}

	public SalesOrderHelper(BapiOrder bapi) {
		this.bapi = bapi;
	}

	protected void buildOrderText(SapOrderI sapOrder) {
		this.addOrderText(0, "ZH01", sapOrder.getDeliveryInstructions());
		this.addOrderText(0, "ZH02", sapOrder.getMarketingMessage());
		this.addOrderText(0, "ZH03", sapOrder.getCustomerServiceMessage());

		SapOrderLineI[] orderLines = getOrderLineArray(sapOrder);
		for (int i = 0; i < orderLines.length; i++) {
			int posex = PosexUtil.getPosexInt(i);
			this.addOrderText(posex, "ZL01", orderLines[i].getDescription());
			this.addOrderText(posex, "ZL02", orderLines[i].getConfigurationDesc());
			this.addOrderText(posex, "ZL03", orderLines[i].getIngredientsDesc());
		}
	}

	private final static int MAX_TEXT_LENGTH = 130;

	/**
	 * Cuts up the text into multiple lines, calls builder.addOrderText multiple times
	 * I can write a nicer one any day, but not today.. at least it works :)
	 */
	private void addOrderText(int posex, String textId, String text) {
		StringTokenizer st = new StringTokenizer(text, " ", true);
		String lastWord = null;
		do {
			StringBuffer buf = new StringBuffer(MAX_TEXT_LENGTH);
			while (st.hasMoreTokens()) {
				String w = (lastWord == null) ? st.nextToken() : lastWord;
				lastWord = null;
				int wlen = w.length();
				if (wlen > MAX_TEXT_LENGTH) {
					// very long word, cut it
					buf.append(w.substring(0, MAX_TEXT_LENGTH));
					lastWord = w.substring(MAX_TEXT_LENGTH);
					break;
				}
				int blen = buf.length();
				if (wlen + blen > MAX_TEXT_LENGTH) {
					lastWord = w;
					break;
				}
				buf.append(w);
			};
			this.bapi.addOrderText(posex, textId, buf.toString());
		}
		while (st.hasMoreTokens());
	}

	/**
	 * Adds the "extensionin" (RFC_BAPIPAREX) structure, with the credit memos.
	 */
	protected void buildCreditMemos(CreditMemo[] creditMemos) {
		if (creditMemos != null && creditMemos.length != 0) {
			for (int i = 0; i < creditMemos.length; i++) {
				StringBuffer buf = new StringBuffer();
				CreditMemo cm = creditMemos[i];
				buf.append("ZZBMREF=").append(cm.getWebNumber());
				//buf.append(";ZZDESC=").append(cm.getDepartmentName());
				buf.append(";ZZDESC=").append(cm.getAffiliate().getCode());
				buf.append(";AUGRU=").append(cm.getReasonCode());
				buf.append(";ZZCREDIT_REASON=").append(cm.getReasonDesc());
				buf.append(";ZZPLAN_ALLOC_AMT=").append((int) (cm.getAmount() * 100) / 100.0);
				buf.append(";ZZPRIORITY=").append(cm.getPriority());

				this.bapi.addExtension("ZBAPI_CREDIT_MEMO_ALLOCATE", buf.toString());
			}			
		}
	}
	
	protected void markAsFreeOrder(){
		StringBuffer buf = new StringBuffer();
		buf.append("ZZBMREF=").append("123456");
		buf.append(";ZZDESC=").append("FD");
		buf.append(";AUGRU=").append("FREE");
		buf.append(";ZZCREDIT_REASON=").append("Make Good Order");
		buf.append(";ZZPLAN_ALLOC_AMT=").append("100%");
		buf.append(";ZZPRIORITY=").append(1);

		this.bapi.addExtension("ZBAPI_CREDIT_MEMO_ALLOCATE", buf.toString());
	}

	/**
	 * Adds "order_conditions_in"
	 */
	protected void buildConditions(SapOrderI sapOrder, boolean isCreateOrder) {

		int fakePosition = getFakePositionBase(); //FAKEPOS_BASE;
		// order-level discount
		if (sapOrder.getDiscounts() != null && sapOrder.getDiscounts().size() > 0) {
			for (Iterator iter = sapOrder.getDiscounts().iterator(); iter.hasNext();) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				
				
				//if(discountLine.getDiscount() instanceof ZonePromoDiscount)
				//if("WINDOW_STEERING".equals(discountLine.getDiscount().getPromotionCode())/* && isCreateOrder*/)
				if( discountLine!=null && discountLine.getDiscount()!=null 
				   && discountLine.getDiscount().getPromotionCode().startsWith(FDStoreProperties.getWindowSteeringPromoPrefix()))
				{
					if (EnumDiscountType.DOLLAR_OFF.equals(discountLine.getDiscount().getDiscountType())) {
						//orderDiscountAmount += promo.getDiscount().getAmount();
						this.addFakeLine(sapOrder, fakePosition, "000000000000009999", isCreateOrder);
						this.bapi.addCondition(PosexUtil.getPosexInt(fakePosition), "PB00", -1.0 * discountLine.getDiscount().getAmount(), "USD");
						//WS1_ is to identify windows steering promo at sap
						passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, "WS1_"+discountLine.getDiscount().getPromotionCode());
						fakePosition++;
					}
				}
				else
				{
					if (EnumDiscountType.DOLLAR_OFF.equals(discountLine.getDiscount().getDiscountType())) {
						//orderDiscountAmount += promo.getDiscount().getAmount();
						this.addFakeLine(sapOrder, fakePosition, "000000000000009999", isCreateOrder);
						this.bapi.addCondition(PosexUtil.getPosexInt(fakePosition), "PB00", -1.0 * discountLine.getDiscount().getAmount(), "USD");
						passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, discountLine.getDiscount().getPromotionCode());
						fakePosition++;
					}
				}
			}
		} else {  // for backwards compatibility
			Discount orderDiscount = sapOrder.getDiscount();
			if (orderDiscount != null && orderDiscount.getAmount() != 0.0) {
				if (EnumDiscountType.DOLLAR_OFF.equals(orderDiscount.getDiscountType())) {
					this.addFakeLine(sapOrder, fakePosition, "000000000000009999", isCreateOrder);
					this.bapi.addCondition(PosexUtil.getPosexInt(fakePosition), "PB00", -1.0 * orderDiscount.getAmount(), "USD");
					passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, orderDiscount.getPromotionCode());
					fakePosition++;
				} else {
					// !!! other types of header level promotions not supported by SAP now...
					throw new IllegalArgumentException("SAP does not currently support this type of header level promotion");
				}
			}
		}
		
		// line item level
		for (int i = 0; i < sapOrder.numberOfOrderLines(); i++) {
			SapOrderLineI orderLine = sapOrder.getOrderLine(i);

			// pricing condition (PR00)
			MaterialPrice mp = orderLine.getPricingCondition();
			if (orderLine.isZeroBasePrice()) {
				// 1 cent per 100 units
				this.bapi.addCondition(PosexUtil.getPosexInt(i), "PR00", 0.01, "USD", 100.0, mp.getPricingUnit());
			} else {
				if(orderLine.getFixedPrice()>0){
					System.out.println("Setting 50$ fixed price giftcard charges "+orderLine.getFixedPrice());
					this.bapi.addCondition(PosexUtil.getPosexInt(i), "PR00", orderLine.getFixedPrice(), "USD", 1.0, mp.getPricingUnit());
				}else{
				 this.bapi.addCondition(PosexUtil.getPosexInt(i), "PR00", mp.getPrice(), "USD", 1.0, mp.getPricingUnit());
				}
			}

			// tax
			if (orderLine.getTaxRate() != 0.0) {
				this.bapi.addCondition(
					PosexUtil.getPosexInt(i),
					orderLine.getAffiliate().getTaxConditionType(),
					orderLine.getTaxRate() * 100,
					"");
			}

			// deposit
			if (orderLine.getDepositValue() != 0.0) {
				this.bapi.addCondition(
					PosexUtil.getPosexInt(i),
					orderLine.getAffiliate().getDepositConditionType(),
					orderLine.getDepositValue(),
					"USD",
					orderLine.getQuantity(),
					orderLine.getSalesUnit());
			}

			// promotion
			Discount disc = orderLine.getDiscount();
			if (disc != null) {
				this.addPromotionCondition(i, disc);
				//this will set the promocode + GL Code
				passVBAP(sapOrder, PosexUtil.getPosex(i), orderLine.isRecipeItem(), isCreateOrder, disc.getPromotionCode());
			}else{
				//this will set GL Code only
				passVBAP(sapOrder, PosexUtil.getPosex(i), orderLine.isRecipeItem(), isCreateOrder, null);
			}
		}

		// charge lines
		SapChargeLineI[] charges = sapOrder.getChargeLines();
		for (int i=0; i<charges.length; i++) {
			int pos = fakePosition++;
			SapChargeLineI c = charges[i];
			this.addFakeLine(sapOrder, pos, c.getMaterialNumber(), isCreateOrder);
			
			passVBAP(sapOrder, PosexUtil.getPosex(pos), false, isCreateOrder, null);

			if (c.getTaxRate() > 0) {
				this.bapi.addCondition(
					PosexUtil.getPosexInt(pos),
					ErpAffiliate.getEnum(ErpAffiliate.CODE_FD).getTaxConditionType(),
					c.getTaxRate() * 100,
					"");
			}
			
			this.bapi.addCondition(PosexUtil.getPosexInt(pos), "PB00", c.getAmount(), "USD");
			Discount promo = c.getDiscount();
			if (promo!= null && promo.getAmount() != 0.0) {
				this.addPromotionCondition(pos, promo);				
			}
		}

	}

	private void addPromotionCondition(int pos, Discount discount) {
		int posex = PosexUtil.getPosexInt(pos);
		EnumDiscountType pt = discount.getDiscountType();
		if (EnumDiscountType.PERCENT_OFF.equals(pt)) {
			this.bapi.addCondition(posex, "ZD11", discount.getAmount() * 100, "");

		} else if (EnumDiscountType.DOLLAR_OFF.equals(pt)) {
			this.bapi.addCondition(posex, "ZD10", discount.getAmount(), "USD");

		} else if (EnumDiscountType.FREE.equals(pt)) {
			this.bapi.addCondition(posex, "ZD11", 100.0, ""); // base price
			this.bapi.addCondition(posex, "ZVD0", 100.0, ""); // surcharge

		} else if (EnumDiscountType.SAMPLE.equals(pt)) {
			this.bapi.addCondition(posex, "ZF11", 100.0, "");

		}
	}

	private void addFakeLine(final SapOrderI sapOrder, final int pos, final String matNo, boolean isCreateOrder) {

		final Date currDate = new Date();
		final int posex = PosexUtil.getPosexInt(pos);

		BapiOrder.OrderItemIn item = new BapiOrder.OrderItemIn() {
			public String getMaterialNo() {
				return matNo;
			}
			public String getPurchNo() {
				return sapOrder.getWebOrderNumber();
			}
			public String getPoItemNo() {
				return PosexUtil.getPosex(pos);
			}
			public int getItemNumber() {
				return posex;
			}
			public Date getPurchDate() {
				return currDate;
			}
			public Date getPriceDate() {
				return currDate;
			}
			public Double getReqQty() {
				return null;
			}
			public String getSalesUnit() {
				return "EA";
			}
			public int getDeliveryGroup() {
				return 0;
			}
			public int getMaxPartialDlv() {
				return 0;
			}
			public int getPoItemNoS() {
				return 0;
			}
			public String getCustMat35() {
				return "";
			}
			public String getSalesDist() {
				return "100000";
			}
		};

		bapi.addOrderItemIn(item);

		BapiOrder.ScheduleIn schedule = new BapiOrder.ScheduleIn() {
			public int getItemNumber() { return posex; }
			public double getReqQty() { return 1.0; }
			public Date getReqDate() { return sapOrder.getRequestedDate(); }
			public Date getTPDate() { return null; }
			public Date getMSDate() { return null; }
			public Date getLoadDate() { return null; }
			public Date getGIDate() { return null; }
		};
		
		bapi.addSchedule(schedule);
	}

	protected void buildPartners(SapOrderI order) {
		SapCustomerI customer = order.getCustomer();
		String customerNumber = customer.getSapCustomerNumber();
		if (customer.getShipToAddress() != null) {
			// ship to party
			this.buildOrderPartner("WE", customerNumber, order.getCompanyName(), customer.getShipToAddress());
			// sold to party
			this.buildOrderPartner("AG", customerNumber, order.getCompanyName(), customer.getShipToAddress());
		}

		if (customer.getBillToAddress() != null) {
			// bill to party
			this.buildOrderPartner("RE", customerNumber, "", customer.getBillToAddress());
			// payer
			this.buildOrderPartner("RG", customerNumber, "", customer.getBillToAddress());
		}

		if (customer.getAlternateAddress() != null) {
			// alternate delivery address
			this.buildOrderPartner("ZA", customerNumber, "", customer.getAlternateAddress());
		} else if (customer.getShipToAddress() != null) {
			// default alt dlv to ship to address
			this.buildOrderPartner("ZA", customerNumber, order.getCompanyName(), customer.getShipToAddress());
		}
	}

	private void buildOrderPartner(String partnerRole, String partnerNumber, String companyName, BasicContactAddressI address) {
		if (address == null) {
			this.bapi.addPartner(partnerRole, partnerNumber);
		} else {
			this.bapi.addPartner(
				partnerRole,
				partnerNumber,
				address.getFirstName() + " " + address.getLastName(),
				companyName,
				SalesOrderHelper.getSimplifiedStreet(address),
				address.getCity(),
				address.getZipCode(),
				address.getState(),
				address.getCountry(),
				String.valueOf(address.getPhone()));
		}
	}

	public static String getSimplifiedStreet(BasicAddressI address) {
		return address.getAddress1()
			+ ("".equals(NVL.apply(address.getApartment(), "").trim()) ? "" : " Apt: " + address.getApartment());
	}

	/** Get the orderlines into an array from SapOrderI */
	protected static SapOrderLineI[] getOrderLineArray(SapOrderI sapOrder) {
		SapOrderLineI[] orderLines = new SapOrderLineI[sapOrder.numberOfOrderLines()];
		for (int i = 0; i < orderLines.length; i++) {
			orderLines[i] = sapOrder.getOrderLine(i);
		}
		return orderLines;
	}
	
	private void passVBAP(SapOrderI sapOrder, String posex, boolean isRecipeItem, boolean isCreateOrder, String promoCode){
		String recipeFlag = StringUtils.rightPad(isRecipeItem ? "1" : " ", 5);
		String glCode = StringUtils.rightPad(NVL.apply(sapOrder.getGlCode(), "").trim(), 10);
		String promoField = StringUtils.rightPad(NVL.apply(promoCode, "").trim(), 20);
		bapi.addExtension("BAPE_VBAP", StringUtils.repeat(" ", 10) + posex //0-16
			+ recipeFlag
			+ StringUtils.repeat(" ", 20) + glCode //18 - 51
			+ StringUtils.repeat(" ", 40) + promoField); // 61 - 111

		if (!isCreateOrder) {
			bapi.addExtension("BAPE_VBAPX", StringUtils.repeat(" ", 10) + posex // 0-16 
				+ "X" // 17, recipe change flag
				+ StringUtils.repeat(" ", 4) + "X" // 22, tax chg
				+ StringUtils.repeat(" ", 4) + "X"); // 27, promo code chg
		}
	}

}