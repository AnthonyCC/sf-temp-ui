package com.freshdirect.customer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpOrderHistory implements Serializable {

	/** Collection of ErpSaleInfo */
	private final Collection erpSaleInfos;

	public ErpOrderHistory(Collection erpSaleInfos) {
		this.erpSaleInfos = erpSaleInfos;
	}

	public Collection getErpSaleInfos() {
		return this.erpSaleInfos;
	}

	/**
	 * Get the creation date of the earliest non-canceled, home or depot (not pickup) order.
	 *
	 * @return null if no non-canceled order exists
	 */
	public Date getFirstNonPickupOrderDate() {
		Date date = null;

		for (Iterator i = this.erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			if (!saleInfo.getStatus().isCanceled() && !EnumDeliveryType.PICKUP.equals(saleInfo.getDeliveryType()) ) {
				Date createDate = saleInfo.getCreateDate();
				if (date==null || createDate.before(date)) {
					date = createDate;
				}
			}
		}

		return date;
	}
	
	public int getPromotionUsageCount(String promotionCode, String ignoreSaleId) {
		int count = 0;
		for (Iterator i=this.erpSaleInfos.iterator(); i.hasNext(); ) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			if (saleInfo.getSaleId().equals(ignoreSaleId)) {
				continue;
			}
			if (saleInfo.getUsedPromotionCodes().contains(promotionCode)) {
				count++;
			}
		}
		return count;
	}

	public Set getUsedPromotionCodes(String ignoreSaleId) {
		Set allPromos = new HashSet();
		for (Iterator i=this.erpSaleInfos.iterator(); i.hasNext(); ) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			if (saleInfo.getSaleId().equals(ignoreSaleId)) {
				continue;
			}
			allPromos.addAll(saleInfo.getUsedPromotionCodes());
		}
		return allPromos;
	}
	
	public String getLastOrderId() {
		ErpSaleInfo lastOrder = this.getLastSale();
		return lastOrder==null ? null : lastOrder.getSaleId();
	}

	public Date getLastOrderCreateDate() {
		ErpSaleInfo lastOrder = this.getLastSale();
		return lastOrder==null ? null : lastOrder.getCreateDate();
	}

	public ErpSaleInfo getLastSale() {
		return this.getLastSaleBefore(null);
	}
	
	public ErpSaleInfo getSale(String saleId) {
		for (Iterator i=this.erpSaleInfos.iterator(); i.hasNext(); ) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			if (saleInfo.getSaleId().equals(saleId)) {
				return saleInfo;
			}
		}
		return null;
	}

	private ErpSaleInfo getLastSaleBefore(Date maxCreateDate) {
		ErpSaleInfo lastOrder = null;
		Date date = null;
		for (Iterator i = this.erpSaleInfos.iterator(); i.hasNext();) {
			ErpSaleInfo saleInfo = (ErpSaleInfo) i.next();
			Date createDate = saleInfo.getCreateDate();
			if ((date == null || createDate.after(date)) && (maxCreateDate==null || maxCreateDate.after(createDate))) {
				date = createDate;
				lastOrder = saleInfo;
			}
		}
		return lastOrder;
	}

	public ErpSaleInfo getSecondToLastSale() {
		ErpSaleInfo lastSale = this.getLastSale();
		return lastSale == null ? null : this.getLastSaleBefore(lastSale.getCreateDate());
	}

	/**
	 * Get total number of all the orders placed by this customer
	 */
	public int getTotalOrderCount() {
		return this.erpSaleInfos.size();
	}

	/**
	 * Get total number of all the orders placed by this customer excluding the canceled orders
	 */
	public int getValidOrderCount() {
		int ret = 0;
		for(Iterator i = this.erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled()) {
				ret++;
			}
		}
		return ret;
	}
	
	/**
	 * Get total number of delivered orders placed by this customer
	 */
	public int getDeliveredOrderCount() {
		int ret = 0;
		for(Iterator i = this.erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (saleInfo.isDelivered()) {
				ret++;
			}
		}
		return ret;
	}

	/**
	 * Get total number of all the orders placed by this customer that count towards the signup promo
	 */
	public int getSignupPromoOrderCount() {
		int ret = 0;
		for(Iterator i = this.erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled() && !EnumDeliveryType.PICKUP.equals(saleInfo.getDeliveryType())) {
				ret++;
			}
		}
		return ret;
	}

	public int getValidPhoneOrderCount() {
		int ret = 0;
		for(Iterator i = this.erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled() && EnumTransactionSource.CUSTOMER_REP.equals(saleInfo.getSource())) {
				ret++;
			}
		}
		return ret;
	}
	
	public int getValidECheckOrderCount() {
		int ret = 0;
		for(Iterator i = this.erpSaleInfos.iterator(); i.hasNext(); ){
			ErpSaleInfo saleInfo = (ErpSaleInfo)i.next();
			if (!saleInfo.getStatus().isCanceled() && EnumPaymentMethodType.ECHECK.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}

}
