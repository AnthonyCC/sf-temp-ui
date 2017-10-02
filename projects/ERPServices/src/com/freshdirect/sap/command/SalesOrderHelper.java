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
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
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
						passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, "WS1_"+discountLine.getDiscount().getPromotionCode(),null,null);
						fakePosition++;
					}
				}
				else
				{
					if (EnumDiscountType.DOLLAR_OFF.equals(discountLine.getDiscount().getDiscountType())) {
						//orderDiscountAmount += promo.getDiscount().getAmount();
						this.addFakeLine(sapOrder, fakePosition, "000000000000009999", isCreateOrder);
						this.bapi.addCondition(PosexUtil.getPosexInt(fakePosition), "PB00", -1.0 * discountLine.getDiscount().getAmount(), "USD");
						passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, discountLine.getDiscount().getPromotionCode(),null,null);
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
					passVBAP(sapOrder, PosexUtil.getPosex(fakePosition), false, isCreateOrder, orderDiscount.getPromotionCode(),null,null);
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
			if (mp.getPrice()<=0) {
				// 1 cent per 100 units
				this.bapi.addCondition(PosexUtil.getPosexInt(i), "PR00", 0.01, "USD", 1.0, mp.getPricingUnit());
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
			ErpCouponDiscountLineModel couponDiscount = orderLine.getCouponDiscount();
			if (disc != null || couponDiscount!=null) {
				if(disc != null){
					this.addPromotionCondition(i, disc, orderLine);
				}
				if(couponDiscount!=null){
					this.addCouponCondition(i, couponDiscount, orderLine.getTaxationType());
				}
				//this will set the promocode + GL Code
				passVBAP(sapOrder, PosexUtil.getPosex(i), orderLine.isRecipeItem(), isCreateOrder, (null!=disc? disc.getPromotionCode():null),orderLine.getPricingZoneId(),(null!=couponDiscount?couponDiscount.getCouponId():null));
			}else{
				//this will set GL Code only
				passVBAP(sapOrder, PosexUtil.getPosex(i), orderLine.isRecipeItem(), isCreateOrder, null,orderLine.getPricingZoneId(),null);
			}
		}

		// charge lines
		SapChargeLineI[] charges = sapOrder.getChargeLines();
		for (int i=0; i<charges.length; i++) {
				int pos = fakePosition++;
				SapChargeLineI c = charges[i];
				this.addFakeLine(sapOrder, pos, c.getMaterialNumber(), isCreateOrder);

				passVBAP(sapOrder, PosexUtil.getPosex(pos), false, isCreateOrder, null,null,null);

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
						this.addPromotionCondition(pos, promo, null);
				}
		}

	}

	private void addPromotionCondition(int pos, Discount discount,SapOrderLineI orderLine) {
		int posex = PosexUtil.getPosexInt(pos);
		EnumDiscountType pt = discount.getDiscountType();
		if(null == orderLine && discount.getPromotionCode().startsWith(FDStoreProperties.getWindowSteeringPromoPrefix())){//For chargelines, Window Steering 'Free Delivery' promotion for FDX.
			this.bapi.addCondition(posex, "ZFSH", discount.getAmount() * 100, "");
		}else{
			if (EnumDiscountType.PERCENT_OFF.equals(pt)) {
				if(discount.getSkuLimit() > 0 && orderLine != null && !"LB".equalsIgnoreCase(orderLine.getPricingCondition().getPricingUnit())){//For Percent-off line item promotions with Sku Limit and for non-weight(lb) based items.
					double pricePerQuantity = orderLine.getPricingCondition().getPrice();
					double discountableQuantity = orderLine.getQuantity() > discount.getSkuLimit() ?discount.getSkuLimit():orderLine.getQuantity();
					double finalDiscountAmount = (discountableQuantity*pricePerQuantity)*discount.getAmount();
					this.bapi.addCondition(posex, "ZDFA", finalDiscountAmount, "USD");
				}else{
					this.bapi.addCondition(posex, "ZD11", discount.getAmount() * 100, "");
				}

			} else if (EnumDiscountType.DOLLAR_OFF.equals(pt)) {
				if(discount.getSkuLimit() >0){//For Dollar-off line item promotions with Sku Limit.
					this.bapi.addCondition(posex, "ZDFA", discount.getSkuLimit()*discount.getAmount(), "USD");
				}else{
					this.bapi.addCondition(posex, "ZD10", discount.getAmount(), "USD");
				}

			} else if (EnumDiscountType.FREE.equals(pt)) {
				this.bapi.addCondition(posex, "ZD11", 100.0, ""); // base price
				if(null == orderLine){//no surcharge discount for orderlines
					this.bapi.addCondition(posex, "ZVD0", 100.0, ""); // surcharge
				}

			} else if (EnumDiscountType.SAMPLE.equals(pt)) {
				this.bapi.addCondition(posex, "ZF11", 100.0, "");

			} else if (null != orderLine && orderLine.getPricingCondition().getPrice() <=0.0){
				this.bapi.addCondition(posex, "ZD11", 100.0, ""); // base price
			}
		}
	}

	private void addCouponCondition(int pos, ErpCouponDiscountLineModel couponDiscount,EnumTaxationType taxationType) {
		int posex = PosexUtil.getPosexInt(pos);
		if(null !=taxationType){
			this.bapi.addCondition(posex, taxationType.getCode(), couponDiscount.getDiscountAmt(), "USD");
		}else{
			this.bapi.addCondition(posex, EnumTaxationType.TAX_AFTER_ALL_DISCOUNTS.getCode(), couponDiscount.getDiscountAmt(), "USD");
		}
	}

	private void addFakeLine(final SapOrderI sapOrder, final int pos, final String matNo, boolean isCreateOrder) {

		final Date currDate = new Date();
		final int posex = PosexUtil.getPosexInt(pos);

		BapiOrder.OrderItemIn item = new BapiOrder.OrderItemIn() {
			@Override
			public String getMaterialNo() {
				return matNo;
			}
			@Override
			public String getPurchNo() {
				return sapOrder.getWebOrderNumber();
			}
			@Override
			public String getPoItemNo() {
				return PosexUtil.getPosex(pos);
			}
			@Override
			public int getItemNumber() {
				return posex;
			}
			@Override
			public Date getPurchDate() {
				return currDate;
			}
			@Override
			public Date getPriceDate() {
				return currDate;
			}
			@Override
			public Double getReqQty() {
				return null;
			}
			@Override
			public String getSalesUnit() {
				return "EA";
			}
			@Override
			public int getDeliveryGroup() {
				return 0;
			}
			@Override
			public int getMaxPartialDlv() {
				return 0;
			}
			@Override
			public int getPoItemNoS() {
				return 0;
			}
			@Override
			public String getCustMat35() {
				return "";
			}
			@Override
			public String getSalesDist() {
				return "100000";
			}

			@Override
			public String getPickingPlantId(){
				return sapOrder.getPlant();
			}
		};

		bapi.addOrderItemIn(item);

		BapiOrder.ScheduleIn schedule = new BapiOrder.ScheduleIn() {
			@Override
			public int getItemNumber() { return posex; }
			@Override
			public double getReqQty() { return 1.0; }
			@Override
			public Date getReqDate() { return sapOrder.getRequestedDate(); }
			@Override
			public Date getTPDate() { return null; }
			@Override
			public Date getMSDate() { return null; }
			@Override
			public Date getLoadDate() { return null; }
			@Override
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
			String state=address.getState();
			if("RE".equals(partnerRole) || "RG".equals(partnerRole)) {
				BillingCountryInfo bc=BillingCountryInfo.getEnum(address.getCountry());
				if(bc!=null) {
					BillingRegionInfo br =bc.getRegion(address.getState());
					if(br!=null)
						state=br.getCodeExt();
				}
			}
			this.bapi.addPartner(
				partnerRole,
				partnerNumber,
				address.getFirstName() + " " + address.getLastName(),
				companyName,
				SalesOrderHelper.getSimplifiedStreet(address),
				address.getCity(),
				"US".equalsIgnoreCase(address.getCountry())? address.getZipCode().substring(0,5):address.getZipCode(),
				state,
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

	private void passVBAP(SapOrderI sapOrder, String posex, boolean isRecipeItem, boolean isCreateOrder, String promoCode,String zoneId,String couponCode){
		String recipeFlag = StringUtils.rightPad(isRecipeItem ? "1" : " ", 5);
		String glCode = StringUtils.rightPad(NVL.apply(sapOrder.getGlCode(), "").trim(), 10);
		String promoField = StringUtils.rightPad(NVL.apply(promoCode, "").trim(), 20);
		String zoneField = "";
		if(zoneId==null){
			zoneField=StringUtils.rightPad(NVL.apply(zoneId, "").trim(), 10);
		}else{
			zoneField=zoneId;
		}
		String couponField = StringUtils.rightPad(NVL.apply(couponCode, "").trim(), 20);

		bapi.addExtension("BAPE_VBAP", StringUtils.repeat(" ", 10) + posex //0-16
			+ recipeFlag //17-21
			+ StringUtils.repeat(" ", 20) + glCode //22-51
			+ zoneField //52-61
			+ StringUtils.repeat(" ", 30) + promoField // 62 - 111
			+ couponField); //112-131


		if (!isCreateOrder) {


			bapi.addExtension("BAPE_VBAPX", StringUtils.repeat(" ", 10) + posex // 0-16
					+ "X" // 17, recipe change flag
					+ StringUtils.repeat(" ", 4) + "XX" // 22, tax chg
					+ StringUtils.repeat(" ", 3) + "X" // 27, promo code chg
					+ "X"); //28 ecoupon code chg
		}

	}

	/**
	 *   APPDEV-5314 : get unatteded delivery flg to send to SAP
	 *   pass "X" character along with 4 spaces when unatteded delivery flg is opted in otherwise pass 5 spaces
	 */
	public static String populateUnattendedDlvFlg(EnumUnattendedDeliveryFlag unattendedDeliveryFlg) {

		//if(ErpServicesProperties.isSAPUnattendedDelivery()) {
		    if(EnumUnattendedDeliveryFlag.OPT_IN.equals(unattendedDeliveryFlg)){
			   return StringUtils.rightPad("X", 5, " ");
		   } else {return StringUtils.rightPad(" ", 5);}
	   // } return "";

	}
}