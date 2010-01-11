/*
 * FDOrderAdapter.java
 *
 * Created on December 7, 2001, 7:23 PM
 */

package com.freshdirect.fdstore.customer.adapter;

/**
 *
 * @author  ekracoff
 * @version
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractInvoiceModel;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpCartonDetails;
import com.freshdirect.customer.ErpCartonInfo;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpModifyOrderModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpSubmitFailedModel;
import com.freshdirect.customer.ErpTransactionModel;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCartonDetail;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.WebOrderViewFactory;
import com.freshdirect.fdstore.customer.WebOrderViewI;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpEmailGiftCardModel;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.giftcard.RecipientModel;

public class FDOrderAdapter implements FDOrderI {
	private static final long serialVersionUID = -7702575123652018830L;

	private static Category LOGGER = LoggerFactory.getInstance(FDOrderAdapter.class);

	private ErpSaleModel sale;
	private ErpAbstractOrderModel erpOrder;
	private FDInvoiceAdapter firstInvoice;
	private FDInvoiceAdapter lastInvoice;
	private ErpAbstractInvoiceModel returnOrder;
	private ErpRedeliveryModel redeliveryModel;
	private List<FDCartLineI> orderLines;
	private List<FDCartLineI> sampleLines;
	private FDReservation deliveryReservation;
	// List<FDCartonInfo>
	protected List cartonInfo;

	public FDOrderAdapter() {
		this.orderLines = new ArrayList();
	}

	/** Creates new FDOrderAdapter */
	public FDOrderAdapter(ErpSaleModel sale) {
		this.init(sale);
	}

	public ErpSaleModel getSale() {
		return sale;
	}

	/** Adapt a sale */
	protected void init(ErpSaleModel sale) {
		this.sale = sale;
		System.out.println("FDOrderAdapter : init ");
		this.erpOrder = this.sale.getRecentOrderTransaction();

		ErpDeliveryInfoModel delInfo = erpOrder.getDeliveryInfo();
		try {
			this.deliveryReservation = FDDeliveryManager.getInstance().getReservation(delInfo.getDeliveryReservationId());
			if (this.deliveryReservation == null) {
				//!!! this is just a temporary fix until pre-reserve slots is completely implemented
				DlvTimeslotModel t = new DlvTimeslotModel();
				ErpDeliveryInfoModel info = this.getDeliveryInfo();
				t.setBaseDate(info.getDeliveryStartTime());
				t.setStartTime(new TimeOfDay(info.getDeliveryStartTime()));
				t.setEndTime(new TimeOfDay(info.getDeliveryEndTime()));
				t.setCutoffTime(new TimeOfDay(info.getDeliveryCutoffTime()));

				this.deliveryReservation = new FDReservation(
					null,
					new FDTimeslot(t),
					info.getDeliveryCutoffTime(),
					EnumReservationType.STANDARD_RESERVATION,
					this.getCustomerId(),
					null, false,false, this.sale.getId(),false,null,20);
			}
		} catch (FDResourceException ex) {
			throw new FDRuntimeException(ex);
		}

		ErpInvoiceModel invoice = sale.getFirstInvoice();
		if (invoice != null) {
			this.firstInvoice = new FDInvoiceAdapter(invoice);
		}
		invoice = this.sale.getLastInvoice();
		if (invoice != null) {
			this.lastInvoice = new FDInvoiceAdapter(sale.getLastInvoice());
		}

		List txList = new ArrayList(sale.getTransactions());
		Collections.sort(txList, ErpTransactionModel.TX_DATE_COMPARATOR);
		for (Iterator i = txList.iterator(); i.hasNext();) {
			Object obj = i.next();

			if (obj instanceof ErpReturnOrderModel) {
				this.returnOrder = (ErpAbstractInvoiceModel) obj;
				continue;
			}

			if (obj instanceof ErpRedeliveryModel) {
				this.redeliveryModel = (ErpRedeliveryModel) obj;
				continue;
			}
		}

		this.orderLines = new ArrayList<FDCartLineI>();
		this.sampleLines = new ArrayList<FDCartLineI>();
		List erpLines = erpOrder.getOrderLines();
		for (int i = 0; i < erpLines.size(); i++) {
			ErpOrderLineModel ol = (ErpOrderLineModel) erpLines.get(i);
			String olNum = ol.getOrderLineNumber();
			ErpInvoiceLineI firstInvoiceLine = this.getFirstInvoiceLine(olNum);
			ErpInvoiceLineI lastInvoiceLine = this.getLastInvoiceLine(olNum);
			ErpReturnLineModel returnLine = this.getReturnLine(olNum);


			FDCartLineI cartLine;
			cartLine = new FDCartLineModel(ol, firstInvoiceLine, lastInvoiceLine, returnLine);
			//If gift card sku load the fixed frice into cartline.
			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(ol.getSku().getSkuCode()) || FDStoreProperties.getRobinHoodSkucode().equalsIgnoreCase(ol.getSku().getSkuCode())){
				cartLine.setFixedPrice(ol.getPrice());
			}
			
			try {
				cartLine.refreshConfiguration();

			} catch (FDInvalidConfigurationException e) {
				LOGGER.warn("Difficulty recreating orderline " + i + " in sale " + sale.getPK(), e);

				// salvage original descriptions
				cartLine.setDepartmentDesc(ol.getDepartmentDesc());
				cartLine.setDescription(ol.getDescription());
				cartLine.setConfigurationDesc(ol.getConfigurationDesc());

			} catch (FDResourceException e) {
				throw new FDRuntimeException(e, "Difficulty recreating orderline " + i + " in sale " + sale.getPK());
			}

			if (cartLine.isSample()) {
				this.sampleLines.add(cartLine);
			} else {
				this.orderLines.add(cartLine);
			}

		}
		Collections.sort(this.orderLines, FDCartModel.LINE_COMPARATOR);

		// Now set up carton content info
		this.cartonInfo = new ArrayList(); 
			
		// Get ErpCartonInfo and create our adapters
		List erpCartons = sale.getCartonInfo();

		for(int i = 0; i < erpCartons.size(); i++) {
			ErpCartonInfo carton = (ErpCartonInfo) erpCartons.get(i);
			
			List cartonDetails = new ArrayList();
			FDCartonInfo f = new FDCartonInfo(carton, cartonDetails);
			cartonInfo.add(f);
			for(int j = 0; j < carton.getDetails().size(); j++) {
				ErpCartonDetails detail = (ErpCartonDetails) carton.getDetails().get(j);

				FDCartLineI cartLine = null;
				for(int k = 0; k < orderLines.size(); k++) {
					FDCartLineI curCartLine = (FDCartLineI) orderLines.get(k);
					if(curCartLine.getOrderLineNumber().equals(detail.getOrderLineNumber())) {
						cartLine = curCartLine;
						break;
					}
				}
				//ErpOrderLineModel ol = erpOrder.getOrderLine(detail.getOrderLineNumber());
				if(cartLine != null) {
					FDCartonDetail fdDetail = new FDCartonDetail(f, detail, cartLine);
					cartonDetails.add(fdDetail);
				}
			}
		}
	}

	public boolean isModifiedOrder() {
		for (Iterator i = this.sale.getTransactions().iterator(); i.hasNext();) {
			if (i.next() instanceof ErpModifyOrderModel) {
				return true;
			}
		}
		return false;
	}

	private ErpInvoiceLineI getFirstInvoiceLine(String orderLineNumber) {
		return this.firstInvoice == null ? null : this.firstInvoice.getInvoiceLine(orderLineNumber);
	}

	private ErpInvoiceLineI getLastInvoiceLine(String orderLineNumber) {
		return this.lastInvoice == null ? null : this.lastInvoice.getInvoiceLine(orderLineNumber);
	}
	
	public ErpInvoiceLineI getInvoiceLine(String orderLineNumber) {
		return this.getLastInvoiceLine(orderLineNumber);
	}

	private ErpReturnLineModel getReturnLine(String orderLineNumber) {
		if (this.hasReturn()) {
			List returnLines = this.returnOrder.getInvoiceLines();
			for (int i = 0, size = returnLines.size(); i < size; i++) {
				ErpReturnLineModel returnLine = (ErpReturnLineModel) returnLines.get(i);
				if (orderLineNumber.equalsIgnoreCase(returnLine.getLineNumber())) {
					return returnLine;
				}
			}
		}
		return null;
	}

	/**
	 * @return List of SAP error messages for failed order submission, sorted by most recent. Returns Collections.EMPTY_LIST if order has no such messages.
	 */
	public List getSubmissionFailedMessage() {
		if (!EnumSaleStatus.NOT_SUBMITTED.equals(getOrderStatus())) {
			LOGGER.debug("order not in NOT_SUBMITTED state");
			return Collections.EMPTY_LIST;
		}
		//
		// SORT TRANSACTIONS BY DATE
		//
		List txList = new ArrayList(this.sale.getTransactions());
		Collections.sort(txList, ErpTransactionModel.TX_DATE_COMPARATOR);
		Collections.reverse(txList);
		//
		// FIND ALL SUBMIT FAILED MODELS
		//
		List messages = new ArrayList();
		ErpSubmitFailedModel submitFailedModel = null;
		for (Iterator it = txList.iterator(); it.hasNext();) {
			Object o = it.next();
			if (o instanceof ErpSubmitFailedModel) {
				submitFailedModel = (ErpSubmitFailedModel) o;
				messages.add(submitFailedModel.getMessage());
			}
		}
		return messages.isEmpty() ? Collections.EMPTY_LIST : messages;
	}

	public Date getPricingDate() {
		return this.erpOrder.getPricingDate();
	}

	public EnumSaleStatus getOrderStatus() {
		return sale.getStatus();
	}

	public EnumSaleStatus getSaleStatus() {
		return sale.getStatus();
	}

	/**
	 * Default method for obtaining an order's source. Will call <code>getOrderSource(String criteria)</code>
	 * with a value of "CREATE_ORDER".
	 *
	 * @return EnumTransactionSource indicating the source for the transaction, or <b>null</b> if none are found.
	 */
	public EnumTransactionSource getOrderSource() {
		return (getOrderSource("CREATE_ORDER"));
	}
	public String getTransactionInitiator() {
		return (getTransactionInitiator("CREATE_ORDER"));
	}

	/**
	 * Returns the source of a particular transaction for the order. The transaction is specified by
	 * the String param. Currently, the only valid values for this String are:<br>
	 * &middot; "CREATE_ORDER", returns the source for the CREATE_ORDER transaction<br>
	 * &middot; "LAST_MODIFIED", returns the source for the most recent transaction<br><br>
	 * Specifying any other value will return <b>null</b>.
	 *
	 * @param String criteria - used to designate the particular transaction whose source is to be returned.
	 * @return EnumTransactionSource indicating the source for the transaction, or <b>null</b> if none are found.
	 */
	public EnumTransactionSource getOrderSource(String criteria) {

		ErpTransactionModel lastTransaction = null;
		Date lastTransactionDate = null;

		ErpTransactionModel transaction = null;
		Collection transactions = sale.getTransactions();
		Iterator it = transactions.iterator();
		while (it.hasNext()) {
			transaction = (ErpTransactionModel) it.next();
			if ("CREATE_ORDER".equalsIgnoreCase(criteria)) { // We want the Source for the CREATE_ORDER transaction
				if (EnumTransactionType.CREATE_ORDER.equals(transaction.getTransactionType())) {
					return (transaction.getTransactionSource());
				}
			} else if ("LAST_MODIFIED".equalsIgnoreCase(criteria)) { // We want the Source for the latest transaction
				if (lastTransactionDate == null || lastTransactionDate.before(transaction.getTransactionDate())) {
					lastTransactionDate = transaction.getTransactionDate();
					lastTransaction = transaction;
				}
			}
		}
		if ("LAST_MODIFIED".equalsIgnoreCase(criteria)) {
			return (lastTransaction.getTransactionSource());
		} else {
			return null;
		}
	}
	public String getTransactionInitiator(String criteria) {

		ErpTransactionModel lastTransaction = null;
		Date lastTransactionDate = null;

		ErpTransactionModel transaction = null;
		Collection transactions = sale.getTransactions();
		Iterator it = transactions.iterator();
		while (it.hasNext()) {
			transaction = (ErpTransactionModel) it.next();
			if ("CREATE_ORDER".equalsIgnoreCase(criteria)) { // We want the Source for the CREATE_ORDER transaction
				if (EnumTransactionType.CREATE_ORDER.equals(transaction.getTransactionType())) {
					return (transaction.getTransactionInitiator());
				}
			} else if ("LAST_MODIFIED".equalsIgnoreCase(criteria)) { // We want the Source for the latest transaction
				if (lastTransactionDate == null || lastTransactionDate.before(transaction.getTransactionDate())) {
					lastTransactionDate = transaction.getTransactionDate();
					lastTransaction = transaction;
				}
			}
		}
		if ("LAST_MODIFIED".equalsIgnoreCase(criteria)) {
			return (lastTransaction.getTransactionInitiator());
		} else {
			return null;
		}
	}

	/**
	 * Returns the java.util.Date of the latest transaction for this order.
	 *
	 * @return boolean
	 */
	public Date getLastModifiedDate() {

		Date lastTransactionDate = null;
		ErpTransactionModel transaction = null;
		Collection transactions = sale.getTransactions();
		Iterator it = transactions.iterator();
		while (it.hasNext()) {
			transaction = (ErpTransactionModel) it.next();
			if (lastTransactionDate == null || lastTransactionDate.before(transaction.getTransactionDate())) {
				lastTransactionDate = transaction.getTransactionDate();
			}
		}
		return lastTransactionDate;
	}

	/**
	 * Returns true if the order has an associated credit transaction. Possible values are:
	 * <ul>
	 *   <li>0, signifying NO CREDITS have been issued for this order</li>
	 *   <li>1, signifying ONE OR MORE CREDITS have been issued for this order, with NONE PENDING</li>
	 *   <li>2, signifying a PENDING CREDIT for this order</li>
	 * </ul>
	 *
	 * @return int
	 */
	public int hasCreditIssued() {

		int NO = 0;
		int YES = 1;
		int PENDING = 2;

		int hasCredits = 0;
		Collection<ErpComplaintModel> complaints = this.getComplaints();
		hasCredits = (complaints.size() > 0) ? YES : NO;
		for (ErpComplaintModel complaint : complaints) {
			if (EnumComplaintStatus.PENDING.equals(complaint.getStatus())) {
				hasCredits = PENDING;
				break;
			}
		}
		return hasCredits;
	}


	public static final int IC_FREE2GROUP = -1;
	public static final int IC_GROUP_BY_DEPTS = 1;
	public static final int IC_GROUP_BY_CARTONS = 2;
	public int getComplaintGroupingFashion() {
		Collection<ErpComplaintModel> comps = getComplaints();

		if (comps == null || comps.size() == 0)
			return IC_FREE2GROUP;
		
		for (ErpComplaintModel compl : comps) {
			// skip rejected complaints
			if (!EnumComplaintStatus.REJECTED.equals(compl.getStatus()))
				return compl.isCartonized() ? IC_GROUP_BY_CARTONS : IC_GROUP_BY_DEPTS;
		}
		
		return IC_FREE2GROUP;
	}

	
	public Collection getCharges() {
		return Collections.unmodifiableCollection(erpOrder.getCharges());
	}

	/**
	 * Retrieves the list of complaints recorded for a given order.
	 * @return read-only Collection of ErpComplaintModel objects.
	 */
	public Collection<ErpComplaintModel> getComplaints() {
		return sale.getComplaints();
	}

	/**
	 * Retrieves the list of applied credits recorded for a given order.
	 * @return read-only Collection of ErpAppliedCreditModel objects.
	 */
	public Collection getAppliedCredits() {
		return erpOrder.getAppliedCredits();
	}

	public double getCustomerCreditsValue() {
		Collection appliedCredits = this.getAppliedCredits();
		double creditValue = 0;

		for (Iterator i = appliedCredits.iterator(); i.hasNext();) {
			ErpAppliedCreditModel ac = (ErpAppliedCreditModel) i.next();
			creditValue += ac.getAmount();
		}
		return creditValue;
	}

	public Collection getActualAppliedCredits() {
		return this.hasInvoice() ? this.lastInvoice.getAppliedCredits() : erpOrder.getAppliedCredits();
	}

	public double getActualCustomerCreditsValue() {
		return this.lastInvoice.getCustomerCreditsValue();
	}

	public ErpAddressModel getDeliveryAddress() {
		return this.erpOrder.getDeliveryInfo().getDeliveryAddress();
	}

	/**
	 * !!! ugly impedance mismatch: depot/pickup addresses are not mapped properly on backend...
	 * some properties (eg. alt dlv on depot) would still not be mapped back. 
	 */
	public ErpAddressModel getCorrectedDeliveryAddress() {
		ErpDeliveryInfoModel dlvInfo = this.erpOrder.getDeliveryInfo();
		ErpAddressModel origAddress = dlvInfo.getDeliveryAddress();
		/*
		 * The following block was added to fix COS Promo Bug.
		 * The modified order delivery info object contains the attribute
		 * EnumDeliveryType which defines the service type used fo that
		 * order. But the view cart page itself uses AddressModel.serviceType
		 * which in this case would be NULL, to decide if it was a HOME or 
		 * CORPORATE order. So the following black sets the AddressModel.serviceType
		 * based on the DeliveryInfo.deliveryType so that the system knows
		 * the serviceType way in advance before you go to delivery address page
		 * and come back.
		 *  
		 */
		//BEGIN FIX
		EnumDeliveryType dlvType = dlvInfo.getDeliveryType();
		if(EnumDeliveryType.HOME.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.HOME);
		} else if (EnumDeliveryType.CORPORATE.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.CORPORATE);
		} else if (EnumDeliveryType.PICKUP.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.PICKUP);
		} else if (EnumDeliveryType.DEPOT.equals(dlvType)){
			origAddress.setServiceType(EnumServiceType.DEPOT);
		}
		//END FIX.
		
		if (EnumDeliveryType.DEPOT.equals(dlvType) || EnumDeliveryType.PICKUP.equals(dlvType)) {
			ErpDepotAddressModel depotAddress = new ErpDepotAddressModel(origAddress);
			depotAddress.setFacility(this.getDepotFacility());
			depotAddress.setPickup(EnumDeliveryType.PICKUP.equals(dlvType));
			depotAddress.setLocationId(dlvInfo.getDepotLocationId());
			depotAddress.setInstructions(origAddress.getInstructions());
			depotAddress.setZoneCode(dlvInfo.getDeliveryZone()); 
			return depotAddress;
		}
		return origAddress;
	}

	public EnumDeliveryType getDeliveryType() {
		return this.erpOrder.getDeliveryInfo().getDeliveryType();
	}

	private ErpDeliveryInfoModel getDeliveryInfo() {
		return this.erpOrder.getDeliveryInfo();
	}

	public String getDepotFacility() {
		String depotLocationId = this.erpOrder.getDeliveryInfo().getDepotLocationId();
		if (depotLocationId == null || "".equals(depotLocationId)) {
			return "";
		}
		try {
			return FDDepotManager.getInstance().getDepotFacility(depotLocationId);
		} catch (FDResourceException fdre) {
			LOGGER.error("Unable to look up a depot location", fdre);
		}
		return "";
	}

	public Date getDatePlaced() {
		ErpTransactionModel trans = null;
		for (Iterator i = sale.getTransactions().iterator(); i.hasNext() && trans == null;) {
			trans = (ErpTransactionModel) i.next();
			if (!EnumTransactionType.CREATE_ORDER.equals(trans.getTransactionType())) {
				trans = null;
			}
		}
		return trans.getTransactionDate();
	}

	public ErpPaymentMethodI getPaymentMethod() {
		return this.erpOrder.getPaymentMethod();
	}

	public String getDeliveryReservationId() {
		return this.erpOrder.getDeliveryInfo().getDeliveryReservationId();
	}

	public Date getRequestedDate() {
		return this.erpOrder.getRequestedDate();
	}

	public String getErpSalesId() {
		return this.sale.getPK().getId();
	}

	public String getCustomerId() {
		return this.sale.getCustomerPk().getId();
	}

	public List getOrderLines() {
		return this.orderLines;
	}

	public Set getUsedPromotionCodes() {
		return this.sale.getUsedPromotionCodes();
	}

	public String getDiscountDescription() {
		String desc = "";
		/*
		if (this.erpOrder.getDiscounts() != null && this.erpOrder.getDiscounts().size() > 1) {
			desc = "Free Food";
		 */			
		/*
		} else {  // this for backward compatibility 
			Discount discount = this.getDiscount();
			if (discount != null) {
				String code = discount.getPromotionCode();
				PromotionI promotion = FDPromotionFactory.getInstance().getPromotion(code);
				if (promotion != null) {
					if (EnumPromotionType.REDEMPTION.equals(promotion.getPromotionType())) {
						desc = promotion.getDescription();
					} else {
						desc = "Free Food";
					}
				}
			}
		*/
		//}
		List discounts = this.erpOrder.getDiscounts();
		if ( discounts != null && discounts.size() > 0) {
			for(Iterator i = discounts.iterator(); i.hasNext();){
				ErpDiscountLineModel model = (ErpDiscountLineModel)i.next();
				Discount discount =  model.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null) {
					if (EnumPromotionType.SIGNUP.equals(promotion.getPromotionType())) {
						desc = "FREE FOOD";
						
					} else {
						desc = promotion.getDescription();
					}
				}
			}
		}
		return desc;
	}

	public String getRedeemedSampleDescription() {
		String desc = "NONE";
		//Show any redeemed sample line if any.
		if ( this.sampleLines != null && this.sampleLines.size() > 0) {
			for(Iterator i = this.sampleLines.iterator(); i.hasNext();){
				FDCartLineI cartLine = (FDCartLineI)i.next();
				Discount discount =  cartLine.getDiscount();
				String code = discount.getPromotionCode();
				PromotionI promotion = PromotionFactory.getInstance().getPromotion(code);
				if (promotion != null && promotion.isRedemption()) {
					desc = promotion.getDescription();
				}
			}
		}
		return desc;
	}
	public String getDeliveryZone() {
		return this.erpOrder.getDeliveryInfo().getDeliveryZone();
	}

	public double getDeliverySurcharge() {
		ErpChargeLineModel charge = this.erpOrder.getCharge(EnumChargeType.DELIVERY);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	public double getInvoicedDeliveryCharge(){
		return lastInvoice.getInvoicedDeliveryCharge();
	}

	public boolean isDeliveryChargeWaived() {
		return this.isChargeWaived(EnumChargeType.DELIVERY);
	}
	
	public boolean isDeliveryChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.DELIVERY);
	}

	public boolean isChargeWaived(EnumChargeType chargeType) {
		ErpChargeLineModel charge = this.erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getDiscount() != null;
	}

	public boolean isChargeTaxable(EnumChargeType chargeType) {
		ErpChargeLineModel charge = this.erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getTaxRate() > 0;
	}
	public boolean isEstimatedPrice() {
		if(this.hasInvoice()){
			return false;
		}
		for (Iterator i = orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isEstimatedPrice()) {
				return true;
			}
		}
		return false;
	}

	public double getTotal() {
		return this.erpOrder.getAmount();
	}

	public double getSubTotal() {
		//return this.erpOrder.getSubTotal();
		double subTotal = 0.0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			subTotal += MathUtil.roundDecimal(((FDCartLineI) i.next()).getPrice());
		}
		return MathUtil.roundDecimal(subTotal);
	}

	public double getTaxValue() {
		return this.erpOrder.getTax();
	}

	public double getDepositValue() {
		return this.erpOrder.getDepositValue();
	}

	public int numberOfOrderLines() {
		return this.orderLines.size();
	}

	public FDCartLineI getOrderLine(int idx) {
		return (FDCartLineI) this.orderLines.get(idx);
	}
	
	public ErpOrderLineModel getOrderLine(String orderlineId) {
		return this.erpOrder.getOrderLineByPK(orderlineId);
	}

	public List getSampleLines() {
		return this.sampleLines;
	}

	public String getCustomerServiceMessage() {
		return this.erpOrder.getCustomerServiceMessage();
	}

	public String getMarketingMessage() {
		return this.erpOrder.getMarketingMessage();
	}

	public String getDeliveryInstructions() {
		return this.erpOrder.getDeliveryInfo().getDeliveryAddress().getInstructions();
	}

	private double getChargeAmount(EnumChargeType type) {
		ErpChargeLineModel charge = this.hasInvoice() ? this.lastInvoice.getCharge(type) : this.erpOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}

	public double getPhoneCharge() {
		return this.getChargeAmount(EnumChargeType.PHONE);
	}

	public double getMiscellaneousCharge() {
		return this.getChargeAmount(EnumChargeType.MISCELLANEOUS);
	}

	public boolean isMiscellaneousChargeWaived() {
		return this.isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	public boolean isMiscellaneousChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.MISCELLANEOUS);
	}

	public double getRestockingCharges() {
		double charge = 0;
		charge += this.getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		charge += this.getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		charge += this.getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		charge += this.getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		return charge;
	}

	public double getRestockingCharges(ErpAffiliate affiliate) {

		double charge = 0.0;
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_FD).equals(affiliate)) {
			charge = this.getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL).equals(affiliate)) {
			charge = this.getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_BC).equals(affiliate)) {
			charge = this.getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ).equals(affiliate)) {
			charge = this.getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		}
		return charge;
	}

	public double getFDRestockingCharges() {
		return this.getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
	}

	public double getWBLRestockingCharges() {
		return this.getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
	}

	public double getCCDeclinedCharge() {
		return this.getChargeAmount(EnumChargeType.CC_DECLINED);
	}

	public List getAuthorizations() {
		return this.sale.getAuthorizations();
	}

	public List getFailedAuthorizations() {
		return this.sale.getFailedAuthorizations();
	}

	public boolean isPending() {
		return this.getOrderStatus().isPending();
	}

	public boolean containsAlcohol() {
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.isAlcohol()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasInvoice() {
		return this.firstInvoice != null;
	}

	public boolean hasReturn() {
		return this.returnOrder != null;
	}

	public boolean hasSettledReturn() {
		return this.returnOrder != null && this.firstInvoice != this.lastInvoice;
	}

	public double getReturnSubTotal() {
		return this.returnOrder.getSubTotal();
	}

	public double getReturnTotal() {
		return this.returnOrder.getAmount();
	}

	public List getRetrunCharges() {
		return this.returnOrder.getCharges();
	}

	public boolean hasRedelivery() {
		return this.redeliveryModel != null;
	}

	public Date getRedeliveryStartTime() {
		return this.redeliveryModel.getDeliveryInfo().getDeliveryStartTime();
	}

	public Date getRedeliveryEndTime() {
		return this.redeliveryModel.getDeliveryInfo().getDeliveryEndTime();
	}

	public ErpShippingInfo getShippingInfo() {
		return this.sale.getShippingInfo();
	}

	public boolean hasRefusedDelivery() {
		return EnumSaleStatus.REFUSED_ORDER.equals(this.sale.getStatus()) || EnumSaleStatus.RETURNED.equals(this.sale.getStatus());
	}

	public double getInvoicedTotal() {
		return this.lastInvoice.getInvoicedTotal();
	}

	public double getInvoicedSubTotal() {
		return this.lastInvoice.getInvoicedSubTotal();
	}

	public double getInvoicedTaxValue() {
		return this.lastInvoice.getInvoicedTaxValue();
	}

	public double getInvoicedDepositValue() {
		return this.lastInvoice.getInvoicedDepositValue();
	}

	public List getInvoicedCharges() {
		return this.lastInvoice.getInvoicedCharges();
	}

	public double getActualDiscountValue() {
		return this.lastInvoice.getActualDiscountValue();
	}

	public boolean isChargedWaivedForReturn(EnumChargeType type) {
		if (this.returnOrder == null) {
			return false;
		}
		List waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isWaived = false;
		for (Iterator i = waivedCharges.iterator(); i.hasNext();) {
			ErpChargeLineModel charge = (ErpChargeLineModel) i.next();
			if (charge.getType().equals(type) && charge.getDiscount() != null) {
				if(type == EnumChargeType.DELIVERY || type == EnumChargeType.MISCELLANEOUS){
					//if delivery charge or msc charges
					String promoCode = charge.getDiscount().getPromotionCode();
					//Not waived due to a Delivery Pass .
					isWaived = !(promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE));
				} else {
					//any other charges
					isWaived = true;
				}
				break;
			}
		}
		return isWaived;
	}

	public boolean isChargeWaivedByCSROnReturn(EnumChargeType type) {
		if (this.returnOrder == null) {
			return false;
		}
		List waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isCsrWaived = false;
		for (Iterator i = waivedCharges.iterator(); i.hasNext();) {
			ErpChargeLineModel charge = (ErpChargeLineModel) i.next();
			if (charge.getType().equals(type) && charge.getDiscount() != null) {
				if(type == EnumChargeType.DELIVERY || type == EnumChargeType.MISCELLANEOUS){
					//if delivery charge or msc charges
					String promoCode = charge.getDiscount().getPromotionCode();
					isCsrWaived = (promoCode != null && promoCode.equals("DELIVERY") ? true : false);
				} else {
					//any other charges
					isCsrWaived = true;
				}
				break;
			}
		}
		return isCsrWaived;
	}

	public WebOrderViewI getOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getOrderView(orderLines, affiliate);
	}

	public List getOrderViews() {
		return WebOrderViewFactory.getOrderViews(orderLines);
	}
	
	public WebOrderViewI getInvoicedOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getInvoicedOrderView(orderLines, getSampleLines(), affiliate);
	}

	public List getInvoicedOrderViews() {
		return WebOrderViewFactory.getInvoicedOrderViews(orderLines, getSampleLines());
	}

	public FDReservation getDeliveryReservation() {
		return this.deliveryReservation;
	}

	public String getBillingRef() {
		return getPaymentMethod().getBillingRef();
	}

	public List getShortedItems() {
		List shortedItems = new ArrayList();

		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			
			if (line.getDeliveredQuantity() != null && !"".equals(line.getDeliveredQuantity())) {				
				if (line.isPricedByLb()) {				
					if (new Double(line.getDeliveredQuantity()).doubleValue() == 0) {
						shortedItems.add(line);
					}				
				} else if (new Double(line.getDeliveredQuantity()).doubleValue() < line.getQuantity()) {					
					shortedItems.add(line);					
				}				
			}

		}

		return shortedItems;
	}

	public String getSapOrderId() {
		return this.getSale().getSapOrderNumber();
	}

	public boolean isPhoneChargeWaived() {
		return this.isChargeWaived(EnumChargeType.PHONE);
	}

	public boolean isPhoneChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.PHONE);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.customer.FDOrderI#getCartonContents()
	 */
	// List<FDCartonInfo>
	public List getCartonContents() {
		return cartonInfo;
	}

	public List getCartonContents(String orderlineNumber) {
		List retList = new ArrayList();
		for(Iterator i = cartonInfo.iterator(); i.hasNext(); ) {
			FDCartonInfo cartonInfo = (FDCartonInfo) i.next();
			if(cartonInfo.containsCartonInfo(orderlineNumber) != null) {
				retList.add(cartonInfo);
			}
		}
		return retList;
	}

    public boolean hasChargeInvoice() {
		return (sale.getChargeInvoice() != null);
    }
	
	public double getChargeInvoiceTotal() {
		double chargeInvoiceTotal = 0;
		if (hasChargeInvoice()) {			
			List list = sale.getChargeInvoice().getCharges();
			if (list != null && list.size() > 0) {
				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					ErpChargeLineModel chargeLine = (ErpChargeLineModel) iter.next();
					chargeInvoiceTotal += chargeLine.getAmount();
				}
			}
		}
		return chargeInvoiceTotal;
	}
	
	public List getDiscounts() { return this.erpOrder.getDiscounts(); }

	public List getHeaderDiscounts()
	{
		if (this.erpOrder.getDiscounts() != null && this.erpOrder.getDiscounts().size() > 0) 
		{
			List result=new ArrayList();
			for (Iterator iter = this.erpOrder.getDiscounts().iterator(); iter.hasNext(); ) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				result.add(new DiscountLineModelAdaptor(discountLine));
			}
			return result;
		}
		return null;
	}
	public double getTotalDiscountValue() {		
		double totalDiscountAmount = 0.0;
		if (this.erpOrder.getDiscounts() != null && this.erpOrder.getDiscounts().size() > 0) {
			for (Iterator iter = this.erpOrder.getDiscounts().iterator(); iter.hasNext(); ) {
				ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}			
		}
		return totalDiscountAmount;
	}

	public List getActualDiscounts() { return this.lastInvoice.getActualDiscounts(); }
	
	public String getDeliveryPassId(){
		return this.sale.getDeliveryPassId();
	}
	
	public boolean isDlvPassApplied() {
		return (this.sale.getDeliveryPassId() != null ? true : false);
	}

	public boolean isDlvPassAppliedOnReturn() {
		boolean applied = false;
		if (this.returnOrder == null) {
			return false;
		}	
		List waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}		
		boolean isDlvPassPromo = false;
		ErpChargeLineModel charge = this.returnOrder.getCharge(EnumChargeType.DELIVERY);
		if(charge != null && charge.getDiscount() != null){
			String promoCode = charge.getDiscount().getPromotionCode();
			isDlvPassPromo = (promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE) ? true : false);
		}
		return (this.sale.getDeliveryPassId() != null && isDlvPassPromo) ;

	}
	
	public double getDeliverySurchargeOnReturn() {
		ErpChargeLineModel charge = this.returnOrder.getCharge(EnumChargeType.DELIVERY);
		return charge == null ? 0.0 : charge.getAmount();
	}

	public boolean containsDeliveryPass() {
		boolean deliveryPass = false;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (line.lookupFDProduct().isDeliveryPass()) {
				deliveryPass = true;
				break;
			}
		}
		return deliveryPass;
		
	}

	/*
	 * new API which gives the total without and discounts and applied credit 
	*/ 		
	public double getPreDeductionTotal() {
		// TODO Auto-generated method stub
		double preTotal = 0.0;
        preTotal += MathUtil.roundDecimal(this.getSubTotal());			
        preTotal += MathUtil.roundDecimal(this.getTaxValue());
        preTotal += MathUtil.roundDecimal(this.getDepositValue());

		// apply charges
		for (Iterator i = this.getCharges().iterator(); i.hasNext();) {
			preTotal += MathUtil.roundDecimal(((ErpChargeLineModel) i.next()).getTotalAmount());
		}
       return MathUtil.roundDecimal(preTotal);
	}
	
	public EnumSaleType getOrderType(){
		return this.sale.getType();
	}
	
	public int getLineItemDiscountCount(String promoCode){
		Set uniqueDiscountedProducts =new HashSet(); 
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI)i.next();
			if(cartLine.hasDiscount(promoCode)) {
				uniqueDiscountedProducts.add(cartLine.getProductRef().getContentKey().getId());
			}
		}
		return uniqueDiscountedProducts.size();
	}

	public double getTotalLineItemsDiscountAmount() {
		// TODO Auto-generated method stub
		double discountAmt=0;
		for (Iterator i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI)i.next();
			if(cartLine.getDiscount() !=  null){
				discountAmt+=cartLine.getDiscountAmount();
			}
		}
        return discountAmt;
	}
	
	//Gift cards
	public List getGiftcardPaymentMethods() {
		return erpOrder.getSelectedGiftCards();
	}
	
	public List getAppliedGiftCards() {
		if(sale.hasInvoice()){
			return sale.getLastInvoice().getAppliedGiftCards();
		}
		return erpOrder.getAppliedGiftcards();
	}
	
	public double getTotalAppliedGCAmount(){
		double amount = 0.0;
		for(Iterator it = this.getAppliedGiftCards().iterator();it.hasNext();){
			ErpAppliedGiftCardModel model = (ErpAppliedGiftCardModel)it.next();
			double appamt = model.getAmount();//ErpGiftCardUtil.getAppliedAmount(model.getCertificateNum(), this.getAppliedGiftCards());
			amount += appamt;
		}
		return amount;
	}
	
	public double getCCPaymentAmount() {
		if(sale.hasInvoice()){
			return this.getInvoicedTotal() - this.getTotalAppliedGCAmount();
		}
		return this.getTotal() - this.getTotalAppliedGCAmount();
	}
	public double getAppliedAmount(String certificateNum){
		return ErpGiftCardUtil.getAppliedAmount(certificateNum, this.getAppliedGiftCards());
	}
	
	public FDRecipientList getGiftCardRecipients() {
		return new FDRecipientList(erpOrder.getRecepientsList());
	}

	public ErpGiftCardDlvConfirmModel getGCDeliveryInfo() {
		return sale.getGCDeliveryConfirmation();
	}
	
	public ErpRecipentModel getGCResendInfoFor(String giftCardId) {
		List resendTransactions = sale.getGCResendEmailTransaction();
		if(resendTransactions != null){
			for	(Iterator it = resendTransactions.iterator(); it.hasNext();){
				ErpEmailGiftCardModel model = (ErpEmailGiftCardModel)it.next();
				for	(Iterator j = model.getRecepientsTranactionList().iterator(); j.hasNext();){
					ErpGCDlvInformationHolder holder = (ErpGCDlvInformationHolder)j.next();
					if(holder.getGiftCardId().equals(giftCardId)){
						return holder.getRecepientModel();
					}
				}
			}
		}
		return null;
	}
	
	public FDBulkRecipientList getGiftCardBulkRecipients(){
		List recipientList = erpOrder.getRecepientsList();
		Collections.sort(recipientList, new ErpRecipientModelTemplateComparator());
		FDBulkRecipientModel bulkModel = new FDBulkRecipientModel();
		List recipients = new ArrayList();
		FDRecipientList recpList = new FDRecipientList(recipients);
		
		
		for (Iterator iterator = recipientList.iterator(); iterator.hasNext();) {
			ErpRecipentModel erpRecipentModel = (ErpRecipentModel) iterator.next();
			if( null ==bulkModel.getTemplateId() || (!bulkModel.getTemplateId().equals(erpRecipentModel.getTemplateId())) ||(bulkModel.getTemplateId().equals(erpRecipentModel.getTemplateId()) && bulkModel.getAmount()!= erpRecipentModel.getAmount())){
				bulkModel = new FDBulkRecipientModel();
				bulkModel.setQuantity("1");//Initial qty.
				bulkModel.setAmount(erpRecipentModel.getAmount());
				bulkModel.setTemplateId(erpRecipentModel.getTemplateId());
				recipients.add(bulkModel);
			}else{
				Integer qty = Integer.parseInt(bulkModel.getQuantity());				
				bulkModel.setQuantity((++qty).toString()); //Increase the qty by one.
//				bulkModel.setAmount((bulkModel.getAmount()+erpRecipentModel.getAmount())); //Accumulating the amt for the sample template type.
			}			
		}
		FDBulkRecipientList bulkList = new FDBulkRecipientList(recipients);	
		return bulkList;
	}
	
	private class ErpRecipientModelTemplateComparator implements Comparator<ErpRecipentModel>{

		@Override
		public int compare(ErpRecipentModel recp1, ErpRecipentModel recp2) {
			if(recp1.getTemplateId().compareToIgnoreCase(recp2.getTemplateId())==0){
				return Double.compare(recp1.getAmount(),recp2.getAmount());
			}else{		
				return recp1.getTemplateId().compareToIgnoreCase(recp2.getTemplateId());			
			}
		}
		
	}

	public ErpOrderLineModel getOrderLineByNumber(String orderlineNumber) {
		return this.erpOrder.getOrderLineByOrderLineNumber(orderlineNumber);
	}
	
	public ErpGCDlvInformationHolder getGCDlvInformationHolder(String givexNumber){
		ErpGiftCardDlvConfirmModel model = sale.getGCDeliveryConfirmation();
		if(null != model){
			List dlvInfoList = model.getDlvInfoTranactionList();
			for (Iterator iterator = dlvInfoList.iterator(); iterator.hasNext();) {				
				ErpGCDlvInformationHolder dlvInfoHolder = (ErpGCDlvInformationHolder) iterator.next();
				if(null !=dlvInfoHolder.getGivexNum() && dlvInfoHolder.getGivexNum().equalsIgnoreCase(givexNumber)){
					return dlvInfoHolder;
				}
				
			}
		}
		return null;
	}
	
	public double getBufferAmt() {
		return this.erpOrder.getBufferAmt();
	}
	
}
