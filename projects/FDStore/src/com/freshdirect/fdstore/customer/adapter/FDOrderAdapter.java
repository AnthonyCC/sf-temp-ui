package com.freshdirect.fdstore.customer.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumPaymentType;
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
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
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
import com.freshdirect.deliverypass.DlvPassAvailabilityInfo;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.atp.FDAvailabilityInfo;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDBulkRecipientModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCartonDetail;
import com.freshdirect.fdstore.customer.FDCartonInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDInvoiceLineI;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDRecipientList;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.WebOrderViewFactory;
import com.freshdirect.fdstore.customer.WebOrderViewI;
import com.freshdirect.fdstore.customer.util.FDCartUtil;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.rules.FDRuleContextI;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.fdstore.services.tax.TaxFactory;
import com.freshdirect.fdstore.services.tax.TaxFactoryImpl;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.payment.EnumPaymentMethodType;

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
	protected Map<String, Integer> cartonMetrics;

	private EnumNotificationType taxationtype;
	
	private FDInvoiceAdapter shipStatus;
	
	private FDCartModel cartModel;
	
	public FDOrderAdapter() {
		orderLines = new ArrayList<FDCartLineI>();
	}

	/** Creates new FDOrderAdapter */
	public FDOrderAdapter(ErpSaleModel sale) {
		init(sale, false);
	}

	public FDOrderAdapter(ErpSaleModel sale, boolean lazy) {
		init(sale, lazy);
	}

	public ErpSaleModel getSale() {
		return sale;
	}

	/** Adapt a sale
	 * @param saleModel Sale object
	 * @param lazy if true, initialize cartline items from orderlines as much as possible
	 * avoiding using product models. Retained for FDX CRM. 
	 */
	protected void init(ErpSaleModel saleModel, final boolean lazy) {
		this.sale = saleModel;
		erpOrder = sale.getRecentOrderTransaction();
		erpOrder.seteStoreId(sale.geteStoreId());

		try {
			List<ErpPaymentMethodI> paymentList = (List<ErpPaymentMethodI>)FDCustomerManager.getPaymentMethods(new FDIdentity(sale.getCustomerPk().getId()));
			
			  if(paymentList!=null && paymentList.size()>0){
				
				a:for(int j=0;j<paymentList.size();j++){
					ErpPaymentMethodI custPayment=paymentList.get(j);
					if(isMatching(custPayment, erpOrder.getPaymentMethod())) {
						EnumPaymentType origPaymentType = erpOrder.getPaymentMethod().getPaymentType();
						String origMakeGoodRefOrder = erpOrder.getPaymentMethod().getReferencedOrder();
						String origBillingRef = erpOrder.getPaymentMethod().getBillingRef();
						erpOrder.setPaymentMethod(custPayment);
						erpOrder.getPaymentMethod().setPaymentType(origPaymentType);
						erpOrder.getPaymentMethod().setReferencedOrder(origMakeGoodRefOrder);
						erpOrder.getPaymentMethod().setBillingRef(origBillingRef);
						break a;
					}
				}
			  }
		} catch (FDResourceException e1) {
			LOGGER.info("payment method not found"+e1);
		}		
		  
		ErpDeliveryInfoModel delInfo = erpOrder.getDeliveryInfo();
		try {
			LOGGER.info("Get Reservation By Id: "+delInfo.getDeliveryReservationId()+ " Sale ID: "+sale.getId());
			deliveryReservation = FDDeliveryManager.getInstance().getReservation(delInfo.getDeliveryReservationId(), sale.getId());
			
			if (deliveryReservation == null) {
				if(!"1".equalsIgnoreCase(delInfo.getDeliveryReservationId())){
					LOGGER.info("RESERVATIONISSUE: RSV IS NULL " + delInfo.getDeliveryReservationId());
				}
				//!!! this is just a temporary fix until pre-reserve slots is completely implemented
				FDTimeslot t = new FDTimeslot();
				ErpDeliveryInfoModel info = getDeliveryInfo();
				t.setDeliveryDate(info.getDeliveryStartTime());
				t.setDlvStartTime(new TimeOfDay(info.getDeliveryStartTime()));
				t.setDlvEndTime(new TimeOfDay(info.getDeliveryEndTime()));
				t.setCutoffTime(new TimeOfDay(info.getDeliveryCutoffTime()));
				
				try{
					Calendar cutoffCal = t.getCutoffTime().getAsCalendar(t.getDeliveryDate());
					cutoffCal.add(Calendar.DATE, -1);
					t.setCutoffDateTime(cutoffCal.getTime());
				}catch(Exception e){
					LOGGER.info("failed to fetch cutoff datetime "+e);
					t.setCutoffDateTime(info.getDeliveryCutoffTime());
				}
				
				deliveryReservation = new FDReservation(
					new PrimaryKey(info.getDeliveryReservationId()),
					t,
					info.getDeliveryCutoffTime(),
					EnumReservationType.STANDARD_RESERVATION,
					getCustomerId(),
					null,false, sale.getId(),20,null,false,null,null);
				
			}
			delInfo.setOriginalCutoffTime(deliveryReservation.getTimeslot().getOriginalCutoffDateTime()); 
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
		ErpDeliveryPlantInfoModel dpi=erpOrder.getDeliveryInfo().getDeliveryPlantInfo();
		if(dpi==null) {
			LOGGER.warn("DeliveryPlantInfo is null for " + sale.getPK()+". Defaulting it.");
			dpi=FDUserUtil.getDefaultDeliveryPlantInfo(erpOrder.geteStoreId());
		}
		List<ErpOrderLineModel> erpLines = erpOrder.getOrderLines();
		for (int i = 0; i < erpLines.size(); i++) {
			ErpOrderLineModel ol = erpLines.get(i);
			if(null == ol.getPlantID()){
				ol.setPlantID(dpi.getPlantId());
			}
			
			String olNum = ol.getOrderLineNumber();
			ErpInvoiceLineI firstInvoiceLine = getFirstInvoiceLine(olNum);
			ErpInvoiceLineI lastInvoiceLine = getLastInvoiceLine(olNum);
			ErpReturnLineModel returnLine = getReturnLine(olNum);


			FDCartLineI cartLine;
			cartLine = new FDCartLineModel(ol, firstInvoiceLine, lastInvoiceLine, returnLine, lazy);
			cartLine.setCouponDiscount(ol.getCouponDiscount());
			cartLine.setEStoreId(erpOrder.geteStoreId());
			cartLine.getUserContext().setStoreContext(StoreContext.createStoreContext(erpOrder.geteStoreId()));
			//if(dpi!=null && !StringUtil.isEmpty(dpi.getSalesOrg()) && !StringUtil.isEmpty(ol.getSalesOrg())  && !ol.getSalesOrg().equals(dpi.getSalesOrg())) {
			if(dpi!=null && !StringUtil.isEmpty(dpi.getSalesOrg()) && !StringUtil.isEmpty(ol.getSalesOrg())) {				
				CatalogKey catalogKey = dpi.getCatalogKey();
				if(null !=catalogKey && null != catalogKey.getPricingZone()){
					cartLine.getUserContext().setPricingContext(new PricingContext(catalogKey.getPricingZone()));
				}else{
					//--Hack for APPDEV-4726 FoodKick pricing errors in order receipt and order details
					if(!ol.getSalesOrg().equals(dpi.getSalesOrg())) {
										
						ZoneInfo z=new ZoneInfo(ol.getPricingZoneId(), dpi.getSalesOrg(), dpi.getDistChannel(), ZoneInfo.PricingIndicator.BASE, new ZoneInfo(ol.getPricingZoneId(), ol.getSalesOrg(), ol.getDistChannel()));
						cartLine.getUserContext().setPricingContext(new PricingContext(z));
					} else if (!FDStoreProperties.getDefaultFdSalesOrg().equals(ol.getSalesOrg())){//for group scale
						ZoneInfo z=new ZoneInfo(ol.getPricingZoneId(), dpi.getSalesOrg(), dpi.getDistChannel(), ZoneInfo.PricingIndicator.BASE, new ZoneInfo(ol.getPricingZoneId(), FDStoreProperties.getDefaultFdSalesOrg(), FDStoreProperties.getDefaultFdDistributionChannel()));
						cartLine.getUserContext().setPricingContext(new PricingContext(z));
					}
				}
			}
			
			//If gift card sku load the fixed frice into cartline.
			if(FDStoreProperties.getGiftcardSkucode().equalsIgnoreCase(ol.getSku().getSkuCode()) || FDStoreProperties.getRobinHoodSkucode().equalsIgnoreCase(ol.getSku().getSkuCode())){
				cartLine.setFixedPrice(ol.getPrice());
			}

			
			
			try {
				if(cartLine.lookupFDProduct()!=null){
					cartLine.refreshConfiguration();					
				}

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
				ErpCartonDetails detail = carton.getDetails().get(j);

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
		
		//set the carton metrics
		cartonMetrics = sale.getCartonMetrics();
	}

	

	private static boolean isMatching(ErpPaymentMethodI custPymt,ErpPaymentMethodI salePymt) {
		
		if(custPymt==null || salePymt==null)
			 return false;
		if(StringUtil.isEmpty(salePymt.getProfileID())) {
			if(EnumPaymentMethodType.GIFTCARD.equals(salePymt.getPaymentMethodType()) || EnumPaymentMethodType.EBT.equals(salePymt.getPaymentMethodType())) {
			    
				if(salePymt.getPaymentMethodType().equals(custPymt.getPaymentMethodType()))
					return salePymt.getAccountNumber().equalsIgnoreCase(custPymt.getAccountNumber());
				else
					return false;
			}
			return false;
		}
		else return salePymt.getProfileID().equals(custPymt.getProfileID());
		
	}
	
	
	@Override
    public boolean isModifiedOrder() {
		for ( ErpTransactionModel m : sale.getTransactions() ) {
			if ( m instanceof ErpModifyOrderModel) {
				return true;
			}
		}
		return false;
	}

    @Override
    public int modifyOrderCount() {
        int count = 0;
        for (ErpTransactionModel m : sale.getTransactions()) {
            if (m instanceof ErpModifyOrderModel) {
                count++;
            }
        }
        return count;
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
			List<ErpReturnLineModel> returnLines = returnOrder.getReturnLines();
			for (int i = 0, size = returnLines.size(); i < size; i++) {
				ErpReturnLineModel returnLine = returnLines.get(i);
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
	@Override
    public String getAuthFailDescription() {
		if (!EnumSaleStatus.AUTHORIZATION_FAILED.equals(getOrderStatus())) {
			
			return "";
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
		String desc="";
		ErpAuthorizationModel authModel = null;
		for ( ErpTransactionModel m : txList ) {
			if (m instanceof ErpAuthorizationModel) {
				authModel = (ErpAuthorizationModel) m;
				if(!authModel.isApproved()) {
					desc=authModel.getDescription();
					break;
				}
				
			}
		}
		return desc;
	}

	@Override
    public Date getPricingDate() {
		return erpOrder.getPricingDate();
	}

	@Override
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
	@Override
    public EnumTransactionSource getOrderSource() {
		return (getOrderSource("CREATE_ORDER"));
	}
	@Override
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
	@Override
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
	
	@Override
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
	@Override
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
	@Override
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

	
	@Override
    public Collection<ErpChargeLineModel> getCharges() {
		return Collections.unmodifiableCollection(erpOrder.getCharges());
	}

	/**
	 * Retrieves the list of complaints recorded for a given order.
	 * @return read-only Collection of ErpComplaintModel objects.
	 */
	@Override
    public Collection<ErpComplaintModel> getComplaints() {
		return sale.getComplaints();
	}

	/**
	 * Retrieves the list of applied credits recorded for a given order.
	 * @return read-only Collection of ErpAppliedCreditModel objects.
	 */
	@Override
    public Collection<ErpAppliedCreditModel> getAppliedCredits() {
		return erpOrder.getAppliedCredits();
	}

	@Override
    public double getCustomerCreditsValue() {
		Collection<ErpAppliedCreditModel> appliedCredits = getAppliedCredits();
		double creditValue = 0;

		for ( ErpAppliedCreditModel ac : appliedCredits ) {
			creditValue += ac.getAmount();
		}
		return creditValue;
	}

	@Override
    public Collection<ErpAppliedCreditModel> getActualAppliedCredits() {
		return hasInvoice() ? lastInvoice.getAppliedCredits() : erpOrder.getAppliedCredits();
	}

	@Override
    public double getActualCustomerCreditsValue() {
		return lastInvoice.getCustomerCreditsValue();
	}

	@Override
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

	@Override
    public EnumDeliveryType getDeliveryType() {
		return erpOrder.getDeliveryInfo().getDeliveryType();
	}

	@Override
    public ErpDeliveryInfoModel getDeliveryInfo() {
		return erpOrder.getDeliveryInfo();
	}

	@Override
    public String getDepotFacility() {
		String depotLocationId = erpOrder.getDeliveryInfo().getDepotLocationId();
		if (depotLocationId == null || "".equals(depotLocationId)) {
			return "";
		}
		try {
			return FDDeliveryManager.getInstance().getDepotFacility(depotLocationId);
		} catch (FDResourceException fdre) {
			LOGGER.error("Unable to look up a depot location", fdre);
		}
		return "";
	}

	@Override
    public Date getDatePlaced() {
		
		for ( ErpTransactionModel trans : sale.getTransactions() ) {
			if ( EnumTransactionType.CREATE_ORDER.equals(trans.getTransactionType()) ) {
				return trans.getTransactionDate();
			}			
		}
		return null;
	}

	@Override
    public ErpPaymentMethodI getPaymentMethod() {
		return erpOrder.getPaymentMethod();
	}

	public String getDeliveryReservationId() {
		return erpOrder.getDeliveryInfo().getDeliveryReservationId();
	}

	@Override
    public Date getRequestedDate() {
		return erpOrder.getRequestedDate();
	}

	@Override
    public String getErpSalesId() {
		return sale.getPK().getId();
	}

	@Override
    public String getCustomerId() {
		return sale.getCustomerPk().getId();
	}

	@Override
    public List<FDCartLineI> getOrderLines() {
		return orderLines;
	}

	public Set<String> getUsedPromotionCodes() {
		return sale.getUsedPromotionCodes();
	}

	@Override
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

	@Override
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
	@Override
    public String getDeliveryZone() {
		return erpOrder.getDeliveryInfo().getDeliveryZone();
	}

	@Override
    public double getDeliverySurcharge() {
		return this.getChargeAmount(EnumChargeType.DELIVERY)+this.getChargeAmount(EnumChargeType.DLVPREMIUM);
	}
	
	@Override
    public double getDeliveryPremium() {
		ErpChargeLineModel charge = erpOrder.getCharge(EnumChargeType.DLVPREMIUM);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	@Override
    public double getInvoicedDeliveryCharge(){
		return lastInvoice.getInvoicedDeliveryCharge();
	}
	
	@Override
    public double getInvoicedDeliverySurcharge(){
		return lastInvoice.getInvoicedDeliverySurcharge();
	}
	@Override
    public double getInvoicedDeliveryPremium(){
		return lastInvoice.getInvoicedDeliveryPremium();
	}
	

	@Override
    public boolean isDeliveryChargeWaived() {
		return this.isChargeWaived(EnumChargeType.DELIVERY) && (erpOrder.getCharge(EnumChargeType.DLVPREMIUM)==null || this.isChargeWaived(EnumChargeType.DLVPREMIUM));
	}
	
	@Override
    public boolean isDeliverySurChargeWaived() {
		return isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	@Override
    public boolean isDeliveryChargeTaxable() {
		return this.isChargeTaxable(EnumChargeType.DELIVERY) || this.isChargeTaxable(EnumChargeType.DLVPREMIUM);
	}

	@Override
    public boolean isChargeWaived(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getDiscount() != null;
	}

	@Override
    public boolean isChargeTaxable(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? false : charge.getTaxRate() > 0;
	}
	@Override
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

	@Override
    public double getTotal() {
		return erpOrder.getAmount();
	}

	@Override
    public double getSubTotal() {
		double subTotal = 0.0;
		for ( FDCartLineI cartline : orderLines ) {
			subTotal += MathUtil.roundDecimal( cartline.getPrice() );
		}
		return MathUtil.roundDecimal(subTotal);
	}

	/**
	 * @return the amount you have saved by item promotion value and coupon
	 */
	@Override
	public double getSaveAmount(boolean includeDiscountSavings) {
		return FDCartUtil.getSaveAmount(orderLines) + (includeDiscountSavings? getTotalDiscountValue() : 0);
	}
	
	@Override
    public double getTaxValue() {
		return erpOrder.getTax();
	}

	@Override
    public double getDepositValue() {
		return erpOrder.getDepositValue();
	}

	@Override
    public int numberOfOrderLines() {
		return orderLines.size();
	}

	@Override
    public FDCartLineI getOrderLine(int idx) {
		return orderLines.get(idx);
	}
	
	public ErpOrderLineModel getOrderLine(String orderlineId) {
		return erpOrder.getOrderLineByPK(orderlineId);
	}

	@Override
    public List<FDCartLineI> getSampleLines() {
		return sampleLines;
	}

	@Override
    public String getCustomerServiceMessage() {
		return erpOrder.getCustomerServiceMessage();
	}

	@Override
    public String getMarketingMessage() {
		return erpOrder.getMarketingMessage();
	}

	@Override
    public String getDeliveryInstructions() {
		return erpOrder.getDeliveryInfo().getDeliveryAddress().getInstructions();
	}

	@Override
    public double getChargeAmount(EnumChargeType type) {
		ErpChargeLineModel charge = hasInvoice() ? lastInvoice.getCharge(type) : erpOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}

	private double getChargeAmountOnReturn(EnumChargeType type) {
		ErpChargeLineModel charge = returnOrder.getCharge(type);
		return charge == null ? 0.0 : charge.getAmount();
	}
	
	@Override
    public double getPhoneCharge() {
		return getChargeAmount(EnumChargeType.PHONE);
	}

	@Override
    public double getMiscellaneousCharge() {
		return getChargeAmount(EnumChargeType.MISCELLANEOUS);
	}

	@Override
    public boolean isMiscellaneousChargeWaived() {
		return isChargeWaived(EnumChargeType.MISCELLANEOUS);
	}
	
	@Override
    public boolean isMiscellaneousChargeTaxable() {
		return isChargeTaxable(EnumChargeType.MISCELLANEOUS);
	}

	@Override
    public double getRestockingCharges() {
		double charge = 0;
		charge += getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.BC_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.USQ_RESTOCKING_FEE);
		charge += getChargeAmount(EnumChargeType.FDW_RESTOCKING_FEE);
		return charge;
	}

	@Override
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
		if (ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW).equals(affiliate)) {
			charge = getChargeAmount(EnumChargeType.FDW_RESTOCKING_FEE);
		}
		return charge;
	}

	@Override
    public double getFDRestockingCharges() {
		return getChargeAmount(EnumChargeType.FD_RESTOCKING_FEE);
	}

	@Override
    public double getWBLRestockingCharges() {
		return getChargeAmount(EnumChargeType.WBL_RESTOCKING_FEE);
	}

	@Override
    public double getCCDeclinedCharge() {
		return getChargeAmount(EnumChargeType.CC_DECLINED);
	}

	@Override
    public List<ErpAuthorizationModel> getAuthorizations() {
		return sale.getAuthorizations();
	}

	@Override
    public List<ErpAuthorizationModel> getFailedAuthorizations() {
		return sale.getFailedAuthorizations();
	}

	@Override
    public boolean isPending() {
		return getOrderStatus().isPending();
	}

	@Override
    public boolean containsAlcohol() {
		for ( FDCartLineI line : orderLines ) {
			if (line.isAlcohol()) {
				return true;
			}
		}
		return false;
	}

	@Override
    public boolean hasInvoice() {
		return firstInvoice != null;
	}

	@Override
    public boolean hasReturn() {
		return returnOrder != null;
	}

	@Override
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

	@Override
    public boolean hasRedelivery() {
		return redeliveryModel != null;
	}

	@Override
    public Date getRedeliveryStartTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryStartTime();
	}

	@Override
    public Date getRedeliveryEndTime() {
		return redeliveryModel.getDeliveryInfo().getDeliveryEndTime();
	}

	@Override
    public ErpShippingInfo getShippingInfo() {
		return sale.getShippingInfo();
	}

	@Override
    public boolean hasRefusedDelivery() {
		return EnumSaleStatus.REFUSED_ORDER.equals(sale.getStatus()) || EnumSaleStatus.RETURNED.equals(sale.getStatus());
	}

	@Override
    public double getInvoicedTotal() {
		return lastInvoice.getInvoicedTotal();
	}

	@Override
    public double getInvoicedSubTotal() {
		return lastInvoice.getInvoicedSubTotal();
	}

	@Override
    public double getInvoicedTaxValue() {
		return lastInvoice.getInvoicedTaxValue();
	}

	@Override
    public double getInvoicedDepositValue() {
		return lastInvoice.getInvoicedDepositValue();
	}

	@Override
    public List<ErpChargeLineModel> getInvoicedCharges() {
		return lastInvoice.getInvoicedCharges();
	}

	@Override
    public double getActualDiscountValue() {
		return lastInvoice.getActualDiscountValue();
	}

	@Override
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

	@Override
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

	@Override
    public WebOrderViewI getOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getOrderView(orderLines, affiliate, false);
	}

	@Override
    public List<WebOrderViewI> getOrderViews() {
		if(hasInvoice()){
			return getInvoicedOrderViews();
		}
		return WebOrderViewFactory.getOrderViews(orderLines, false);
	}
	
	@Override
    public WebOrderViewI getInvoicedOrderView(ErpAffiliate affiliate) {
		return WebOrderViewFactory.getInvoicedOrderView(orderLines, getSampleLines(), affiliate);
	}

	@Override
    public List<WebOrderViewI> getInvoicedOrderViews() {
		return WebOrderViewFactory.getInvoicedOrderViews(orderLines, getSampleLines());
	}

	@Override
    public FDReservation getDeliveryReservation() {
		return deliveryReservation;
	}

	public String getBillingRef() {
		return getPaymentMethod().getBillingRef();
	}

	@Override
    public List<FDCartLineI> getShortedItems() {
		List<FDCartLineI> shortedItems = new ArrayList<FDCartLineI>();

		for ( FDCartLineI line : orderLines ) {
			if (line.getDeliveredQuantity() != null && !"".equals(line.getDeliveredQuantity())) {				
				if (line.isPricedByLb()) {				
					if (new Double(line.getDeliveredQuantity()).doubleValue() == 0 && !FDStoreProperties.getMealKitMaterialGroup().contains(line.getMaterialGroup())) {
						shortedItems.add(line);
					}				
				} else if (!FDStoreProperties.getMealKitMaterialGroup().contains(line.getMaterialGroup()) && new Double(line.getDeliveredQuantity()).doubleValue() < line.getQuantity()) {					
					shortedItems.add(line);					
				} else if (line.getSubstitutedQuantity() != null && !"".equals(line.getSubstitutedQuantity())) {
					shortedItems.add(line);
				}
			}
		}
		return shortedItems;
	}
	
	public boolean getHasSubstitutes() {
		List<FDCartLineI> shortedItems =  new ArrayList<FDCartLineI>();
		shortedItems.addAll(this.getShortedItems());
		shortedItems.addAll(this.getBundleShortItems());
		shortedItems.addAll(this.getBundleCompleteShort());
		
		boolean hasSubstitutes = false;
		
		for ( FDCartLineI item : shortedItems ) {
			if (item.hasInvoiceLine()) {
				FDInvoiceLineI invLine = item.getInvoiceLine();
				if (invLine.getSubstituteProductName() != null && !"".equals(invLine.getSubstituteProductName()) && invLine.getWeight() > 0) {
					hasSubstitutes = true;
					break;
				}
			}
		}
		
		return hasSubstitutes;
	}
	
	public List<FDCartLineI> getBundleShortItems(){
		List<FDCartLineI> bundleShortItems = new ArrayList<FDCartLineI>();
		for( FDCartLineI line : orderLines ){
			if(!line.isPricedByLb() && FDStoreProperties.getMealKitMaterialGroup().contains(line.getMaterialGroup()) 
					&& line.getDeliveredQuantity() != null && !"".equals(line.getDeliveredQuantity())){
				if (new Double(line.getDeliveredQuantity()).doubleValue() != 0 && new Double(line.getDeliveredQuantity()).doubleValue() == line.getQuantity() 
						&& line.getInvoiceLine().getPrice()<line.getPrice()){
					bundleShortItems.add(line);
				}			
			}
		}
			
		return bundleShortItems;
	}
	
	public List<FDCartLineI> getBundleCompleteShort(){
		List<FDCartLineI> bundleCompleteShort = new ArrayList<FDCartLineI>();
		for( FDCartLineI line : orderLines ){
				 if(!line.isPricedByLb()){
					if(FDStoreProperties.getMealKitMaterialGroup().contains(line.getMaterialGroup()) 
							&& line.getDeliveredQuantity() != null && !"".equals(line.getDeliveredQuantity()) && new Double(line.getDeliveredQuantity()).doubleValue() <= 0 
							){
						bundleCompleteShort.add(line);
					}
				}
		}
				
		return bundleCompleteShort;
	}

	@Override
    public String getSapOrderId() {
		return getSale().getSapOrderNumber();
	}

	@Override
    public boolean isPhoneChargeWaived() {
		return isChargeWaived(EnumChargeType.PHONE);
	}

	@Override
    public boolean isPhoneChargeTaxable() {
		return isChargeTaxable(EnumChargeType.PHONE);
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.customer.FDOrderI#getCartonContents()
	 */
	// List<FDCartonInfo>
	@Override
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
	
	@Override
    public Map<String, Integer> getCartonMetrics() {
		return cartonMetrics;
	}

	@Override
    public boolean hasChargeInvoice() {
		return (sale.getChargeInvoice() != null);
    }
	
	@Override
    public double getChargeInvoiceTotal() {
		double chargeInvoiceTotal = 0;
		if ( hasChargeInvoice() && sale.getChargeInvoice().getCharges() != null ) {
			for ( ErpChargeLineModel chargeLine : sale.getChargeInvoice().getCharges() ) {
				chargeInvoiceTotal += chargeLine.getAmount();				
			}
		}
		return chargeInvoiceTotal;
	}
	
	public double getBouncedCheckTotal(){
		double bouncedCheckTotal = 0;
		if(null != sale && null != sale.getChargeInvoice() && sale.getChargeInvoice().getCharge(EnumChargeType.BOUNCED_CHECK) != null){
			bouncedCheckTotal = sale.getChargeInvoice().getCharge(EnumChargeType.BOUNCED_CHECK).getAmount();
		}
		return bouncedCheckTotal;
	}
	
	@Override
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
	
	@Override
    public double getTotalDiscountValue() {
		double totalDiscountAmount = 0.0;
		if ( erpOrder.getDiscounts() != null && erpOrder.getDiscounts().size() > 0 ) {
			for ( ErpDiscountLineModel discountLine : erpOrder.getDiscounts() ) {
				totalDiscountAmount += discountLine.getDiscount().getAmount();
			}
		}
		return MathUtil.roundDecimal(totalDiscountAmount);
	}

	@Override
    public List<ErpDiscountLineModel> getActualDiscounts() { 
		return lastInvoice.getActualDiscounts(); 
	}
	
	@Override
    public String getDeliveryPassId(){
		return sale.getDeliveryPassId();
	}
	
	@Override
    public boolean isDlvPassApplied() {
		return (sale.getDeliveryPassId() != null ? true : false);
	}

	@Override
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
	
	@Override
    public double getDeliverySurchargeOnReturn() {
		return this.getChargeAmountOnReturn(EnumChargeType.DELIVERY) + this.getChargeAmountOnReturn(EnumChargeType.DLVPREMIUM);
	}
	
	@Override
    public double getDeliveryChargeOnReturn() {
		return this.getChargeAmountDiscAppliedOnReturn(EnumChargeType.DELIVERY) + this.getChargeAmountDiscAppliedOnReturn(EnumChargeType.DLVPREMIUM);
	}

	@Override
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
	@Override
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
	
	@Override
    public EnumSaleType getOrderType() {
		return sale.getType();
	}
	
	@Override
    public int getLineItemDiscountCount(String promoCode){
		Set<String> uniqueDiscountedProducts =new HashSet<String>(); 
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.hasDiscount(promoCode)) {
				uniqueDiscountedProducts.add(cartLine.getProductRef().getContentKey().getId());
			}
		}
		return uniqueDiscountedProducts.size();
	}

	@Override
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
	@Override
    public List<ErpGiftCardModel> getGiftcardPaymentMethods() {
		return erpOrder.getSelectedGiftCards();
	}
	
	@Override
    public List<ErpAppliedGiftCardModel> getAppliedGiftCards() {
		if(sale.hasInvoice()){
			return sale.getLastInvoice().getAppliedGiftCards();
		}
		return erpOrder.getAppliedGiftcards();
	}
	
	@Override
    public double getTotalAppliedGCAmount(){
		double amount = 0.0;
		for( ErpAppliedGiftCardModel model : getAppliedGiftCards() ) {
			double appamt = model.getAmount();
			amount += appamt;
		}
		return amount;
	}
	
	@Override
    public double getCCPaymentAmount() {
		if(sale.hasInvoice()){
			return getInvoicedTotal() - getTotalAppliedGCAmount();
		}
		return getTotal() - getTotalAppliedGCAmount();
	}
	@Override
    public double getAppliedAmount(String certificateNum){
		return ErpGiftCardUtil.getAppliedAmount(certificateNum, getAppliedGiftCards());
	}
	
	@Override
    public FDRecipientList getGiftCardRecipients() {
		return new FDRecipientList(erpOrder.getRecipientsList());
	}

	@Override
    public ErpGiftCardDlvConfirmModel getGCDeliveryInfo() {
		return sale.getGCDeliveryConfirmation();
	}
	
	@Override
    public ErpRecipentModel getGCResendInfoFor( String giftCardId ) {
		List<ErpGiftCardDlvConfirmModel> resendTransactions = sale.getGCResendEmailTransaction();
		if ( resendTransactions != null ) {
			for ( ErpGiftCardDlvConfirmModel model : resendTransactions ) {
				for ( ErpGCDlvInformationHolder holder : model.getDlvInfoTranactionList()) {
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

	@Override
    public ErpOrderLineModel getOrderLineByNumber(String orderlineNumber) {
		return erpOrder.getOrderLineByOrderLineNumber(orderlineNumber);
	}
	
	@Override
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
	
	@Override
    public double getBufferAmt() {
		return erpOrder.getBufferAmt();
	}
	
	@Override
    public String getStandingOrderId() {
		return sale.getStandingOrderId();
	}
	
	@Override
    public boolean isDiscountInCart(String promoCode) {
		for ( FDCartLineI cartLine : orderLines ) {
			if(cartLine.getDiscount() !=  null){
				String discountCode = cartLine.getDiscount().getPromotionCode();
				if(discountCode.equals(promoCode)) return true;
			}
		}
        return false;
	}
	
	@Override
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
	
	@Override
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
	@Override
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
	
	public String getWSPromotionCode() {
		String promoCode = null;
		//Show any redeemed extend DP promo if any.
		Set<String> usedCodes = this.sale.getUsedPromotionCodes();
		if ( usedCodes != null) {
			for(Iterator<String> it = usedCodes.iterator(); it.hasNext();) {
				PromotionI promo  = PromotionFactory.getInstance().getPromotion(it.next());
				if (promo != null && null != promo.getOfferType() && promo.getOfferType().equals(EnumOfferType.WINDOW_STEERING))
					promoCode = promo.getPromotionCode();
			}
		}
		
		return promoCode;
	}

	@Override
    public boolean hasSignature() {
		return sale.hasSignature();
	}

	@Override
    public int getLineCnt() {
		return orderLines == null ? 0 : orderLines.size();
	}

	@Override
	public boolean isDlvPassPremiumAllowedTC() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDeliveryCharge() {
		return this.getChargeAmountDiscountApplied(EnumChargeType.DELIVERY)+this.getChargeAmountDiscountApplied(EnumChargeType.DLVPREMIUM);
	}
	@Override
    public double getChargeAmountDiscountApplied(EnumChargeType chargeType) {
		ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
		return charge == null ? 0.0 : charge.getTotalAmount();
	}
	@Override
    public double getChargeAmountDiscAppliedOnReturn(EnumChargeType chargeType) {
		ErpChargeLineModel charge = returnOrder.getCharge(chargeType);
		return charge == null ? 0.0 : charge.getTotalAmount();
	}

	@Override
	public double getPremiumFee(FDRuleContextI ctx) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
    public Double getEbtPurchaseAmount() {
	
		Double result = null;
		if(getPaymentMethod() != null && EnumPaymentMethodType.EBT.equals(getPaymentMethod().getPaymentMethodType())
				&& hasInvoice()) {
			result = (getInvoicedTotal() <= getTotal()) ? getInvoicedTotal(): getTotal();
		}
		return result;
	}
	
	public boolean hasCouponDiscounts(){
		return erpOrder.hasCouponDiscounts();
	}

	@Override
	public boolean isMakeGood() {
		return EnumPaymentType.MAKE_GOOD.equals(getPaymentMethod().getPaymentType());
	}

	@Override
	public Map<String, FDAvailabilityInfo> getUnavailabilityMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FDCartLineI getOrderLineById(int parseInt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getOrderLineIndex(int parseInt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeOrderLine(int cartIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DlvPassAvailabilityInfo> getUnavailablePasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshAll(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTip() {
		return this.getChargeAmountDiscountApplied(EnumChargeType.TIP);
}

	@Override
	public void setTip(double tip) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EnumEStoreId getEStoreId() {
		// TODO Auto-generated method stub
		return sale.geteStoreId();
	}

	@Override
	public ErpDeliveryPlantInfoModel getDeliveryPlantInfo() {
		return erpOrder.getDeliveryInfo().getDeliveryPlantInfo();
	}

	@Override
	public boolean isCustomTip() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCustomTip(boolean isCustomTip) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public double getAvalaraTaxValue(AvalaraContext avalaraContext) {
		if(FDStoreProperties.getAvalaraTaxEnabled()){
			TaxFactory taxFactory = new TaxFactoryImpl();
			taxFactory.getTax(avalaraContext);
		}		
		return getTaxValue();
	}

	@Override
	public boolean isTipApplied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTipApplied(boolean tipApplied) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getStandingOrderName() {
		// TODO Auto-generated method stub
		return sale.getStandingOrderName();
	}

	@Override
	public String getSODeliveryDate() { 
		if(null!=sale.getStandingOrderName()){
			Calendar cl = Calendar.getInstance();
		    StringBuilder buf = new StringBuilder();
	        cl.setTime(erpOrder.getDeliveryInfo().getDeliveryStartTime());
	        buf.append(cl.get(Calendar.MONTH)+1);
			buf.append("/");
			buf.append(cl.get(Calendar.DATE));
			return buf.toString();
		}
     return null;
	}
	
	@Override
    public String getIn_modify() {
		return getSale().getIn_modify();
	}
	
	@Override
    public Date getLock_timestamp() {
		return getSale().getLock_timestamp();
	}
	
	@Override
    public EnumNotificationType getTaxationType(){
		return this.taxationtype;
	}
	
	@Override
    public void setTaxationType(EnumNotificationType taxationType){
		this.taxationtype = taxationType;
	}

	public FDInvoiceAdapter getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(FDInvoiceAdapter shipStatus) {
		this.shipStatus = shipStatus;
	}

	

	@Override
	public boolean containsDlvPassOnly() {
		//return cartModel!=null && cartModel.containsDlvPassOnly();
		boolean deliveryPassOnly = true;
		for ( FDCartLineI line : orderLines ) {
			if (!line.lookupFDProduct().isDeliveryPass()) {
				deliveryPassOnly = false;
				break;
			}
		}
		return deliveryPassOnly;
	}

	@Override
	public boolean isChargeWaivedByDlvPass(EnumChargeType chargeType) {
		boolean isWaivedByDlvPass = false;
		if(isChargeWaived(chargeType)) {
			ErpChargeLineModel charge = erpOrder.getCharge(chargeType);
			String promoCode = charge.getDiscount().getPromotionCode();
			isWaivedByDlvPass = promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE);
		}
		return isWaivedByDlvPass;
	}

	@Override
	public int getDeliveryPassCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
