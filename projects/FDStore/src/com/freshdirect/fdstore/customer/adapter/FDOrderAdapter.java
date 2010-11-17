package com.freshdirect.fdstore.customer.adapter;

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
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpAuthorizationModel;
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
import com.freshdirect.customer.ErpTransactionI;
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
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpRecipentModel;

public class FDOrderAdapter implements FDOrderI {
	
	private static final long serialVersionUID = -7702575123652018830L;

	private static Category LOGGER = LoggerFactory.getInstance(FDOrderAdapter.class);

	private ErpSaleModel sale;
	private ErpAbstractOrderModel erpOrder;
	private FDInvoiceAdapter firstInvoice;
	private FDInvoiceAdapter lastInvoice;
	private ErpReturnOrderModel returnOrder;
	private ErpRedeliveryModel redeliveryModel;
	private List<FDCartLineI> orderLines;
	private List<FDCartLineI> sampleLines;
	private FDReservation deliveryReservation;
	protected List<FDCartonInfo> cartonInfo;

	public FDOrderAdapter() {
		orderLines = new ArrayList<FDCartLineI>();
	}

	/** Creates new FDOrderAdapter */
	public FDOrderAdapter(ErpSaleModel sale) {
		init(sale);
	}

	public ErpSaleModel getSale() {
		return sale;
	}

	/** Adapt a sale */
	protected void init(ErpSaleModel saleModel) {
		this.sale = saleModel;
		erpOrder = sale.getRecentOrderTransaction();

		ErpDeliveryInfoModel delInfo = erpOrder.getDeliveryInfo();
		try {
			deliveryReservation = FDDeliveryManager.getInstance().getReservation(delInfo.getDeliveryReservationId());
			if (deliveryReservation == null) {
				//!!! this is just a temporary fix until pre-reserve slots is completely implemented
				DlvTimeslotModel t = new DlvTimeslotModel();
				ErpDeliveryInfoModel info = getDeliveryInfo();
				t.setBaseDate(info.getDeliveryStartTime());
				t.setStartTime(new TimeOfDay(info.getDeliveryStartTime()));
				t.setEndTime(new TimeOfDay(info.getDeliveryEndTime()));
				t.setCutoffTime(new TimeOfDay(info.getDeliveryCutoffTime()));

				deliveryReservation = new FDReservation(
					null,
					new FDTimeslot(t),
					info.getDeliveryCutoffTime(),
					EnumReservationType.STANDARD_RESERVATION,
					getCustomerId(),
					null, false,false, sale.getId(),false,null,20);
			}
		} catch (FDResourceException ex) {
			throw new FDRuntimeException(ex);
		}

		ErpInvoiceModel invoice = sale.getFirstInvoice();
		if (invoice != null) {
			firstInvoice = new FDInvoiceAdapter(invoice);
		}
		invoice = sale.getLastInvoice();
		if (invoice != null) {
			lastInvoice = new FDInvoiceAdapter(sale.getLastInvoice());
		}

		List<ErpTransactionModel> txList = new ArrayList<ErpTransactionModel>(sale.getTransactions());
		Collections.sort(txList, ErpTransactionI.TX_DATE_COMPARATOR);
		for ( ErpTransactionModel obj : txList ) {
			
			if (obj instanceof ErpReturnOrderModel) {
				returnOrder = (ErpReturnOrderModel)obj;
				continue;
			}

			if (obj instanceof ErpRedeliveryModel) {
				redeliveryModel = (ErpRedeliveryModel) obj;
				continue;
			}
		}

		orderLines = new ArrayList<FDCartLineI>();
		sampleLines = new ArrayList<FDCartLineI>();
		List<ErpOrderLineModel> erpLines = erpOrder.getOrderLines();
		for (int i = 0; i < erpLines.size(); i++) {
			ErpOrderLineModel ol = erpLines.get(i);
			String olNum = ol.getOrderLineNumber();
			ErpInvoiceLineI firstInvoiceLine = getFirstInvoiceLine(olNum);
			ErpInvoiceLineI lastInvoiceLine = getLastInvoiceLine(olNum);
			ErpReturnLineModel returnLine = getReturnLine(olNum);


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
				sampleLines.add(cartLine);
			} else {
				orderLines.add(cartLine);
			}

		}
		Collections.sort(orderLines, FDCartModel.LINE_COMPARATOR);

		// Now set up carton content info
		cartonInfo = new ArrayList<FDCartonInfo>(); 
			
		// Get ErpCartonInfo and create our adapters
		List<ErpCartonInfo> erpCartons = sale.getCartonInfo();

		for(int i = 0; i < erpCartons.size(); i++) {
			ErpCartonInfo carton = erpCartons.get(i);
			
			List<FDCartonDetail> cartonDetails = new ArrayList<FDCartonDetail>();
			FDCartonInfo f = new FDCartonInfo(carton, cartonDetails);
			cartonInfo.add(f);
			for(int j = 0; j < carton.getDetails().size(); j++) {
				ErpCartonDetails detail = (ErpCartonDetails) carton.getDetails().get(j);

				FDCartLineI cartLine = null;
				for(int k = 0; k < orderLines.size(); k++) {
					FDCartLineI curCartLine = orderLines.get(k);
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
		for ( ErpTransactionModel m : sale.getTransactions() ) {
			if ( m instanceof ErpModifyOrderModel) {
				return true;
			}
		}
		return false;
	}

	private ErpInvoiceLineI getFirstInvoiceLine(String orderLineNumber) {
		return firstInvoice == null ? null : firstInvoice.getInvoiceLine(orderLineNumber);
	}

	private ErpInvoiceLineI getLastInvoiceLine(String orderLineNumber) {
		return lastInvoice == null ? null : lastInvoice.getInvoiceLine(orderLineNumber);
	}
	
	public ErpInvoiceLineI getInvoiceLine(String orderLineNumber) {
		return getLastInvoiceLine(orderLineNumber);
	}

	private ErpReturnLineModel getReturnLine(String orderLineNumber) {
		if (hasReturn()) {
			// FIXME types are mixed up a little here! ( ErpInvoiceLineModel <-> ErpReturnLineModel )
			// getInvoiceLines() should return ErpInvoiceLineModels-s, 
			// why are there ErpReturnLineModel-s in the list??
			// code is not type-safe this way!
			List returnLines = returnOrder.getInvoiceLines();
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
	public List<String> getSubmissionFailedMessage() {
		if (!EnumSaleStatus.NOT_SUBMITTED.equals(getOrderStatus())) {
			LOGGER.debug("order not in NOT_SUBMITTED state");
			return Collections.<String>emptyList();
		}
		//
		// SORT TRANSACTIONS BY DATE
		//
		List<ErpTransactionModel> txList = new ArrayList<ErpTransactionModel>(sale.getTransactions());
		Collections.sort(txList, ErpTransactionI.TX_DATE_COMPARATOR);
		Collections.reverse(txList);
		//
		// FIND ALL SUBMIT FAILED MODELS
		//
		List<String> messages = new ArrayList<String>();
		ErpSubmitFailedModel submitFailedModel = null;
		for ( ErpTransactionModel m : txList ) {
			if (m instanceof ErpSubmitFailedModel) {
				submitFailedModel = (ErpSubmitFailedModel) m;
				messages.add(submitFailedModel.getMessage());
			}
		}
		return messages.isEmpty() ? Collections.<String>emptyList() : messages;
	}

	public Date getPricingDate() {
		return erpOrder.getPricingDate();
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

		for ( ErpTransactionModel transaction : sale.getTransactions() ) {
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
		if ( lastTransaction != null && "LAST_MODIFIED".equalsIgnoreCase(criteria) ) {
			return lastTransaction.getTransactionSource();
		} else {
			return null;
		}
	}
	
	public String getTransactionInitiator(String criteria) {

		ErpTransactionModel lastTransaction = null;
		Date lastTransactionDate = null;

		for ( ErpTransactionModel transaction : sale.getTransactions() ) {
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
		if ( lastTransaction != null && "LAST_MODIFIED".equalsIgnoreCase(criteria) ) {
			return lastTransaction.getTransactionInitiator();
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
		for ( ErpTransactionModel transaction : sale.getTransactions() ) {
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
		Collection<ErpComplaintModel> complaints = getComplaints();
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

	
	public Collection<ErpChargeLineModel> getCharges() {
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
	public Collection<ErpAppliedCreditModel> getAppliedCredits() {
		return erpOrder.getAppliedCredits();
	}

	public double getCustomerCreditsValue() {
		Collection<ErpAppliedCreditModel> appliedCredits = getAppliedCredits();
		double creditValue = 0;

		for ( ErpAppliedCreditModel ac : appliedCredits ) {
			creditValue += ac.getAmount();
		}
		return creditValue;
	}

	public Collection<ErpAppliedCreditModel> getActualAppliedCredits() {
		return hasInvoice() ? lastInvoice.getAppliedCredits() : erpOrder.getAppliedCredits();
	}

	public double getActualCustomerCreditsValue() {
		return lastInvoice.getCustomerCreditsValue();
	}

	public ErpAddressModel getDeliveryAddress() {
		return erpOrder.getDeliveryInfo().getDeliveryAddress();
	}

	/**
	 * !!! ugly impedance mismatch: depot/pickup addresses are not mapped properly on backend...
	 * some properties (eg. alt dlv on depot) would still not be mapped back. 
	 */
	public ErpAddressModel getCorrectedDeliveryAddress() {
		ErpDeliveryInfoModel dlvInfo = erpOrder.getDeliveryInfo();
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
			depotAddress.setFacility(getDepotFacility());
			depotAddress.setPickup(EnumDeliveryType.PICKUP.equals(dlvType));
			depotAddress.setLocationId(dlvInfo.getDepotLocationId());
			depotAddress.setInstructions(origAddress.getInstructions());
			depotAddress.setZoneCode(dlvInfo.getDeliveryZone()); 
			return depotAddress;
		}
		return origAddress;
	}

	public EnumDeliveryType getDeliveryType() {
		return erpOrder.getDeliveryInfo().getDeliveryType();
	}

	private ErpDeliveryInfoModel getDeliveryInfo() {
		return erpOrder.getDeliveryInfo();
	}

	public String getDepotFacility() {
		String depotLocationId = erpOrder.getDeliveryInfo().getDepotLocationId();
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
		
		for ( ErpTransactionModel trans : sale.getTransactions() ) {
			if ( EnumTransactionType.CREATE_ORDER.equals(trans.getTransactionType()) ) {
				return trans.getTransactionDate();
			}			
		}
		return null;
	}

	public ErpPaymentMethodI getPaymentMethod() {
		return erpOrder.getPaymentMethod();
	}

	public String getDeliveryReservationId() {
		return erpOrder.getDeliveryInfo().getDeliveryReservationId();
	}

	public Date getRequestedDate() {
		return erpOrder.getRequestedDate();
	}

	public String getErpSalesId() {
		return sale.getPK().getId();
	}

	public String getCustomerId() {
		return sale.getCustomerPk().getId();
	}

	public List<FDCartLineI> getOrderLines() {
		return orderLines;
	}

	public Set<String> getUsedPromotionCodes() {
		return sale.getUsedPromotionCodes();
	}

	public String getDiscountDescription() {
		String desc = "";
		List<ErpDiscountLineModel> discounts = erpOrder.getDiscounts();
		if ( discounts != null && discounts.size() > 0) {
			for ( ErpDiscountLineModel model : discounts ) {
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
		if ( sampleLines != null && sampleLines.size() > 0) {
			for ( FDCartLineI cartLine : sampleLines ) {
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
	
	public String getRedeemedExtendDPDescription() {
		String desc = "NONE";
		
		//Show any redeemed sample line if any.
		if ( sampleLines != null && sampleLines.size() > 0) {
			for ( FDCartLineI cartLine : sampleLines ) {
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
		return erpOrder.getDeliveryInfo().getDeliveryZone();
	}

	public double getDeliverySurcharge() {
		ErpChargeLineModel charge = erpOrder.getCharge(EnumChargeType.DELIVERY);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	public double getInvoicedDeliveryCharge(){
		return lastInvoice.getInvoicedDeliveryCharge();
	}

	public boolean isDeliveryChargeWaived() {
		return isChargeWaived(EnumChargeType.DELIVERY);
	}
	
	public boolean isDeliveryChargeTaxable() {
		return isChargeTaxable(EnumChargeType.DELIVERY);
	}

	public boolean isChargeWaived(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getDiscount() != null;
	}

	public boolean isChargeTaxable(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getTaxRate() > 0;
	}
	public boolean isEstimatedPrice() {
		if(hasInvoice()){
			return false;
		}
		for ( FDCartLineI line : orderLines ) {
			if (line.isEstimatedPrice()) {
				return true;
			}
		}
		return false;
	}

	public double getTotal() {
		return erpOrder.getAmount();
	}

	public double getSubTotal() {
		double subTotal = 0.0;
		for ( FDCartLineI cartline : orderLines ) {
			subTotal += MathUtil.roundDecimal( cartline.getPrice() );
		}
		return MathUtil.roundDecimal(subTotal);
	}

	public double getTaxValue() {
		return erpOrder.getTax();
	}

	public double getDepositValue() {
		return erpOrder.getDepositValue();
	}

	public int numberOfOrderLines() {
		return orderLines.size();
	}

	public FDCartLineI getOrderLine(int idx) {
		return orderLines.get(idx);
	}
	
	public ErpOrderLineModel getOrderLine(String orderlineId) {
		return erpOrder.getOrderLineByPK(orderlineId);
	}

	public List<FDCartLineI> getSampleLines() {
		return sampleLines;
	}

	public String getCustomerServiceMessage() {
		return erpOrder.getCustomerServiceMessage();
	}

	public String getMarketingMessage() {
		return erpOrder.getMarketingMessage();
	}

	public String getDeliveryInstructions() {
		return erpOrder.getDeliveryInfo().getDeliveryAddress().getInstructions();
	}

	private double getChargeAmount(EnumChargeType type) {
		ErpChargeLineModel charge = hasInvoice() ? lastInvoice.getCharge(type) : erpOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}

	public double getPhoneCharge() {
		return getChargeAmount(EnumChargeType.PHONE);
	}

	public double getMiscellaneousCharge() {
		return getChargeAmount(EnumChargeType.MISCELLANEOUS);
	}

	public boolean isMiscellaneousChargeWaived() {
		return isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	public boolean isMiscellaneousChargeTaxable() {
		return isChargeTaxable(EnumChargeType.MISCELLANEOUS);
	}

	public double getRestockingCharges() {
		double charge = 0;
		charge += getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		return charge;
	}

	public double getRestockingCharges(ErpAffiliate affiliate) {

		double charge = 0.0;
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_FD).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_BC).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		}
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_USQ).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		}
		return charge;
	}

	public double getFDRestockingCharges() {
		return getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
	}

	public double getWBLRestockingCharges() {
		return getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
	}

	public double getCCDeclinedCharge() {
		return getChargeAmount(EnumChargeType.CC_DECLINED);
	}

	public List<ErpAuthorizationModel> getAuthorizations() {
		return sale.getAuthorizations();
	}

	public List<ErpAuthorizationModel> getFailedAuthorizations() {
		return sale.getFailedAuthorizations();
	}

	public boolean isPending() {
		return getOrderStatus().isPending();
	}

	public boolean containsAlcohol() {
		for ( FDCartLineI line : orderLines ) {
			if (line.isAlcohol()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasInvoice() {
		return firstInvoice != null;
	}

	public boolean hasReturn() {
		return returnOrder != null;
	}

	public boolean hasSettledReturn() {
		return returnOrder != null && firstInvoice != lastInvoice;
	}

	public double getReturnSubTotal() {
		return returnOrder.getSubTotal();
	}

	public double getReturnTotal() {
		return returnOrder.getAmount();
	}

	public List<ErpChargeLineModel> getReturnCharges() {
		return returnOrder.getCharges();
	}

	public boolean hasRedelivery() {
		return redeliveryModel != null;
	}

	public Date getRedeliveryStartTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryStartTime();
	}

	public Date getRedeliveryEndTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryEndTime();
	}

	public ErpShippingInfo getShippingInfo() {
		return sale.getShippingInfo();
	}

	public boolean hasRefusedDelivery() {
		return EnumSaleStatus.REFUSED_ORDER.equals(sale.getStatus()) || EnumSaleStatus.RETURNED.equals(sale.getStatus());
	}

	public double getInvoicedTotal() {
		return lastInvoice.getInvoicedTotal();
	}

	public double getInvoicedSubTotal() {
		return lastInvoice.getInvoicedSubTotal();
	}

	public double getInvoicedTaxValue() {
		return lastInvoice.getInvoicedTaxValue();
	}

	public double getInvoicedDepositValue() {
		return lastInvoice.getInvoicedDepositValue();
	}

	public List<ErpChargeLineModel> getInvoicedCharges() {
		return lastInvoice.getInvoicedCharges();
	}

	public double getActualDiscountValue() {
		return lastInvoice.getActualDiscountValue();
	}

	public boolean isChargedWaivedForReturn(EnumChargeType type) {
		if (returnOrder == null) {
			return false;
		}
		List<ErpChargeLineModel> waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isWaived = false;
		for ( ErpChargeLineModel charge : waivedCharges ) {
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
		if (returnOrder == null) {
			return false;
		}
		List<ErpChargeLineModel> waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}
		boolean isCsrWaived = false;
		for ( ErpChargeLineModel charge : waivedCharges ) {
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

	public List<WebOrderViewI> getOrderViews() {
		return WebOrderViewFactory.getOrderViews(orderLines);
	}
	
	public WebOrderViewI getInvoicedOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getInvoicedOrderView(orderLines, getSampleLines(), affiliate);
	}

	public List<WebOrderViewI> getInvoicedOrderViews() {
		return WebOrderViewFactory.getInvoicedOrderViews(orderLines, getSampleLines());
	}

	public FDReservation getDeliveryReservation() {
		return deliveryReservation;
	}

	public String getBillingRef() {
		return getPaymentMethod().getBillingRef();
	}

	public List<FDCartLineI> getShortedItems() {
		List<FDCartLineI> shortedItems = new ArrayList<FDCartLineI>();

		for ( FDCartLineI line : orderLines ) {
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
		return getSale().getSapOrderNumber();
	}

	public boolean isPhoneChargeWaived() {
		return isChargeWaived(EnumChargeType.PHONE);
	}

	public boolean isPhoneChargeTaxable() {
		return isChargeTaxable(EnumChargeType.PHONE);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.customer.FDOrderI#getCartonContents()
	 */
	// List<FDCartonInfo>
	public List<FDCartonInfo> getCartonContents() {
		return cartonInfo;
	}

	public List<FDCartonInfo> getCartonContents(String orderlineNumber) {
		List<FDCartonInfo> retList = new ArrayList<FDCartonInfo>();
		for ( FDCartonInfo cInf : cartonInfo ) {
			if(cInf.containsCartonInfo(orderlineNumber) != null) {
				retList.add(cInf);
			}
		}
		return retList;
	}

    public boolean hasChargeInvoice() {
		return (sale.getChargeInvoice() != null);
    }
	
	public double getChargeInvoiceTotal() {
		double chargeInvoiceTotal = 0;
		if ( hasChargeInvoice() && sale.getChargeInvoice().getCharges() != null ) {
			for ( ErpChargeLineModel chargeLine : sale.getChargeInvoice().getCharges() ) {
				chargeInvoiceTotal += chargeLine.getAmount();				
			}
		}
		return chargeInvoiceTotal;
	}
	
	public List<ErpDiscountLineModel> getDiscounts() { 
		return erpOrder.getDiscounts(); 
	}

	public List<DiscountLineModelAdaptor> getHeaderDiscounts() {
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			List<DiscountLineModelAdaptor> result = new ArrayList<DiscountLineModelAdaptor>();
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				result.add( new DiscountLineModelAdaptor( discountLine ) );
			}
			return result;
		}
		return null;
	}
	
	public double getTotalDiscountValue() {
		double totalDiscountAmount = 0.0;
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}
		}
		return MathUtil.roundDecimal(totalDiscountAmount);
	}

	public List<ErpDiscountLineModel> getActualDiscounts() { 
		return lastInvoice.getActualDiscounts(); 
	}
	
	public String getDeliveryPassId(){
		return sale.getDeliveryPassId();
	}
	
	public boolean isDlvPassApplied() {
		return (sale.getDeliveryPassId() != null ? true : false);
	}

	public boolean isDlvPassAppliedOnReturn() {
		if (returnOrder == null) {
			return false;
		}	
		List<ErpChargeLineModel> waivedCharges = returnOrder.getCharges();
		if (waivedCharges == null) {
			return false;
		}		
		boolean isDlvPassPromo = false;
		ErpChargeLineModel charge = returnOrder.getCharge(EnumChargeType.DELIVERY);
		if(charge != null && charge.getDiscount() != null){
			String promoCode = charge.getDiscount().getPromotionCode();
			isDlvPassPromo = (promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE) ? true : false);
		}
		return (sale.getDeliveryPassId() != null && isDlvPassPromo);
	}
	
	public double getDeliverySurchargeOnReturn() {
		ErpChargeLineModel charge = returnOrder.getCharge(EnumChargeType.DELIVERY);
		return charge == null ? 0.0 : charge.getAmount();
	}

	public boolean containsDeliveryPass() {
		boolean deliveryPass = false;
		for ( FDCartLineI line : orderLines ) {
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
		double preTotal = 0.0;
		preTotal += MathUtil.roundDecimal( getSubTotal() );
		preTotal += MathUtil.roundDecimal( getTaxValue() );
		preTotal += MathUtil.roundDecimal( getDepositValue() );

		// apply charges
		for ( ErpChargeLineModel m : getCharges() ) {
			preTotal += MathUtil.roundDecimal( m.getTotalAmount() );
		}
		return MathUtil.roundDecimal( preTotal );
	}
	
	public EnumSaleType getOrderType() {
		return sale.getType();
	}
	
	public int getLineItemDiscountCount(String promoCode){
		Set<String> uniqueDiscountedProducts =new HashSet<String>(); 
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.hasDiscount(promoCode)) {
				uniqueDiscountedProducts.add(cartLine.getProductRef().getContentKey().getId());
			}
		}
		return uniqueDiscountedProducts.size();
	}

	public double getTotalLineItemsDiscountAmount() {
		double discountAmt=0;
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.getDiscount() !=  null){
				discountAmt+=cartLine.getDiscountAmount();
			}
		}
        return discountAmt;
	}
	
	//Gift cards
	public List<ErpGiftCardModel> getGiftcardPaymentMethods() {
		return erpOrder.getSelectedGiftCards();
	}
	
	public List<ErpAppliedGiftCardModel> getAppliedGiftCards() {
		if(sale.hasInvoice()){
			return sale.getLastInvoice().getAppliedGiftCards();
		}
		return erpOrder.getAppliedGiftcards();
	}
	
	public double getTotalAppliedGCAmount(){
		double amount = 0.0;
		for( ErpAppliedGiftCardModel model : getAppliedGiftCards() ) {
			double appamt = model.getAmount();
			amount += appamt;
		}
		return amount;
	}
	
	public double getCCPaymentAmount() {
		if(sale.hasInvoice()){
			return getInvoicedTotal() - getTotalAppliedGCAmount();
		}
		return getTotal() - getTotalAppliedGCAmount();
	}
	public double getAppliedAmount(String certificateNum){
		return ErpGiftCardUtil.getAppliedAmount(certificateNum, getAppliedGiftCards());
	}
	
	public FDRecipientList getGiftCardRecipients() {
		return new FDRecipientList(erpOrder.getRecipientsList());
	}

	public ErpGiftCardDlvConfirmModel getGCDeliveryInfo() {
		return sale.getGCDeliveryConfirmation();
	}
	
	public ErpRecipentModel getGCResendInfoFor( String giftCardId ) {
		List<ErpEmailGiftCardModel> resendTransactions = sale.getGCResendEmailTransaction();
		if ( resendTransactions != null ) {
			for ( ErpEmailGiftCardModel model : resendTransactions ) {
				for ( ErpGCDlvInformationHolder holder : model.getRecipientsTransactionList() ) {
					if ( holder.getGiftCardId().equals( giftCardId ) ) {
						return holder.getRecepientModel();
					}
				}
			}
		}
		return null;
	}
	
	public FDBulkRecipientList getGiftCardBulkRecipients(){
		List<ErpRecipentModel> recipientList = erpOrder.getRecipientsList();
		Collections.sort(recipientList, new ErpRecipientModelTemplateComparator());
		FDBulkRecipientModel bulkModel = new FDBulkRecipientModel();
		List<FDBulkRecipientModel> recipients = new ArrayList<FDBulkRecipientModel>();
		
		for ( ErpRecipentModel erpRecipentModel : recipientList ) {
			if ( null == bulkModel.getTemplateId() || 
					( !bulkModel.getTemplateId().equals( erpRecipentModel.getTemplateId() ) ) || 
					( bulkModel.getTemplateId().equals( erpRecipentModel.getTemplateId() ) && bulkModel.getAmount() != erpRecipentModel.getAmount() ) ) {
				bulkModel = new FDBulkRecipientModel();
				bulkModel.setQuantity( "1" );// Initial qty.
				bulkModel.setAmount( erpRecipentModel.getAmount() );
				bulkModel.setTemplateId( erpRecipentModel.getTemplateId() );
				recipients.add( bulkModel );
			} else {
				Integer qty = Integer.parseInt( bulkModel.getQuantity() );
				bulkModel.setQuantity( ( ++qty ).toString() ); // Increase the qty by one.
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
		return erpOrder.getOrderLineByOrderLineNumber(orderlineNumber);
	}
	
	public ErpGCDlvInformationHolder getGCDlvInformationHolder( String givexNumber ) {
		ErpGiftCardDlvConfirmModel model = sale.getGCDeliveryConfirmation();
		if ( null != model ) {
			List<ErpGCDlvInformationHolder> dlvInfoList = model.getDlvInfoTranactionList();
			for ( ErpGCDlvInformationHolder dlvInfoHolder : dlvInfoList ) {
				if ( null != dlvInfoHolder.getGivexNum() && dlvInfoHolder.getGivexNum().equalsIgnoreCase( givexNumber ) ) {
					return dlvInfoHolder;
				}

			}
		}
		return null;
	}
	
	public double getBufferAmt() {
		return erpOrder.getBufferAmt();
	}
	
	public String getStandingOrderId() {
		return sale.getStandingOrderId();
	}
	
	public boolean isDiscountInCart(String promoCode) {
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.getDiscount() !=  null){
				String discountCode = cartLine.getDiscount().getPromotionCode();
				if(discountCode.equals(promoCode)) return true;
			}
		}
        return false;
	}
	
	public double getLineItemDiscountAmount(String promoCode){
		double discountAmt=0;
		for (Iterator<FDCartLineI> i = this.orderLines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = i.next();
			if(cartLine.getDiscount() !=  null && cartLine.getDiscount().getPromotionCode().equals(promoCode)){
				discountAmt+=cartLine.getDiscountAmount();
			}
		}
        return discountAmt;
	}
	
	public double getDiscountValue(String promoCode) {
		double totalDiscountAmount = 0.0;
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				if(promoCode.equals(discountLine.getDiscount().getPromotionCode())) {
					totalDiscountAmount += discountLine.getDiscount().getAmount();
				}
			}
		}
		return MathUtil.roundDecimal(totalDiscountAmount);
	}
	
	/**
	 * This returns a redeem extend DP promo desc if any otherwise returns Empty String.
	 * @return java.lang.String.
	 */
	public String getExtendDPDiscountDescription() {
		String desc = "NONE";
		//Show any redeemed extend DP promo if any.
		Set<String> usedCodes = this.sale.getUsedPromotionCodes();
		if ( usedCodes != null) {
			for(Iterator<String> it = usedCodes.iterator(); it.hasNext();) {
				PromotionI promo  = PromotionFactory.getInstance().getPromotion(it.next());
				if (promo != null && promo.isExtendDeliveryPass())
					desc = promo.getDescription();
			}
		}
		
		return desc;
	}

	@Override
	public boolean hasClientCodes() {
		for (FDCartLineI cl : orderLines)
			if (!cl.getClientCodes().isEmpty())
				return true;
		return false;
	}
}
