package com.freshdirect.fdstore.ecomm.converter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.crm.CrmClick2CallModel;
import com.freshdirect.crm.CrmClick2CallTimeModel;
import com.freshdirect.crm.CrmOrderStatusReportLine;
import com.freshdirect.crm.CrmSettlementProblemReportLine;
import com.freshdirect.crm.CrmVSCampaignModel;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumPaymentType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.delivery.model.RestrictedAddressModel;
import com.freshdirect.delivery.restriction.AlcoholRestriction;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.customer.CancelReservationData;
import com.freshdirect.ecommerce.data.customer.CreditSummaryData;
import com.freshdirect.ecommerce.data.customer.CrmClick2CallData;
import com.freshdirect.ecommerce.data.customer.CrmClick2CallTimeData;
import com.freshdirect.ecommerce.data.customer.CrmOrderStatusReportLineData;
import com.freshdirect.ecommerce.data.customer.CrmSettlementProblemReportLineData;
import com.freshdirect.ecommerce.data.customer.CrmVSCampaignData;
import com.freshdirect.ecommerce.data.customer.ErpRedeliveryData;
import com.freshdirect.ecommerce.data.customer.ErpReturnOrderData;
import com.freshdirect.ecommerce.data.customer.FDAuthInfoData;
import com.freshdirect.ecommerce.data.customer.FDAuthInfoSearchCriteriaData;
import com.freshdirect.ecommerce.data.customer.FDComplaintInfoData;
import com.freshdirect.ecommerce.data.customer.FDCustomerOrderInfoData;
import com.freshdirect.ecommerce.data.customer.FDCustomerReservationInfoData;
import com.freshdirect.ecommerce.data.customer.FDCutoffTimeInfoData;
import com.freshdirect.ecommerce.data.customer.ItemData;
import com.freshdirect.ecommerce.data.customer.MakeGoodOrderInfoData;
import com.freshdirect.ecommerce.data.customer.MealData;
import com.freshdirect.ecommerce.data.customer.MealItemData;
import com.freshdirect.ecommerce.data.customer.ResubmitPaymentData;
import com.freshdirect.ecommerce.data.customer.ReturnOrderData;
import com.freshdirect.ecommerce.data.customer.RouteStopReportData;
import com.freshdirect.ecommerce.data.customer.SettlementBatchInfoData;
import com.freshdirect.ecommerce.data.customer.SettlementProblemReportData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.ecommerce.data.ecoupon.DiscountData;
import com.freshdirect.ecommerce.data.sap.ErpChargeLineData;
import com.freshdirect.ecommerce.data.sap.ErpInvoiceLineData;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.fdstore.content.meal.EnumMealItemType;
import com.freshdirect.fdstore.content.meal.EnumMealStatus;
import com.freshdirect.fdstore.content.meal.MealItemModel;
import com.freshdirect.fdstore.content.meal.MealModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthInfo;
import com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria;
import com.freshdirect.fdstore.customer.FDComplaintInfo;
import com.freshdirect.fdstore.customer.FDCreditSummary;
import com.freshdirect.fdstore.customer.FDCreditSummary.Item;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.FDCutoffTimeInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.MakeGoodOrderInfo;
import com.freshdirect.fdstore.ecomm.gateway.FDStoreModelConverter;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.SettlementBatchInfo;

public class CallCenterConverter {

	public static List<FDComplaintInfo> buildComplaintInfoList(List<FDComplaintInfoData> pendingComplaintData) {
		 List<FDComplaintInfo> complaintInfoList = new ArrayList<FDComplaintInfo>();
		for (FDComplaintInfoData fdComplaintInfoData : pendingComplaintData) {
			complaintInfoList.add(buildComplaintInfo(fdComplaintInfoData));
		}
		return complaintInfoList;
	}

	private static FDComplaintInfo buildComplaintInfo(FDComplaintInfoData fdComplaintInfoData) {
		FDComplaintInfo complaintInfoModel = new FDComplaintInfo(fdComplaintInfoData.getSaleId());
		complaintInfoModel.setApprovedBy(fdComplaintInfoData.getApprovedBy());
		complaintInfoModel.setComplaintAmount(fdComplaintInfoData.getComplaintAmount());
		complaintInfoModel.setComplaintId(fdComplaintInfoData.getComplaintId());
		complaintInfoModel.setComplaintNote(fdComplaintInfoData.getComplaintNote());
		if(fdComplaintInfoData.getComplaintStatus() != null)
		complaintInfoModel.setComplaintStatus(EnumComplaintStatus.getComplaintStatus(fdComplaintInfoData.getComplaintStatus()));
		complaintInfoModel.setComplaintType(fdComplaintInfoData.getComplaintType());
		if(fdComplaintInfoData.getDeliveryDate() != null)
		complaintInfoModel.setDeliveryDate(new java.sql.Date(fdComplaintInfoData.getDeliveryDate()));
		complaintInfoModel.setEmail(fdComplaintInfoData.getEmail());
		complaintInfoModel.seteStore(fdComplaintInfoData.geteStore());
		complaintInfoModel.setFacility(fdComplaintInfoData.getFacility());
		complaintInfoModel.setFirstName(fdComplaintInfoData.getFirstName());
		complaintInfoModel.setIssuedBy(fdComplaintInfoData.getIssuedBy());
		complaintInfoModel.setLastName(fdComplaintInfoData.getLastName());
		complaintInfoModel.setOrderAmount(fdComplaintInfoData.getOrderAmount());
		if(fdComplaintInfoData.getOrderType() != null)
		complaintInfoModel.setOrderType(fdComplaintInfoData.getOrderType());
		if(fdComplaintInfoData.getSaleStatus() != null)
		complaintInfoModel.setSaleStatus(EnumSaleStatus.getSaleStatus(fdComplaintInfoData.getSaleStatus()));
		return complaintInfoModel;
	}

	public static List buildCustomerOrderInfoList(List<FDCustomerOrderInfoData> data) {
		 List customerOrderInfo = new ArrayList();
		for (FDCustomerOrderInfoData fdCustomerOrderInfo : data) {
			customerOrderInfo.add(buildFDCustomerOrderInfoData(fdCustomerOrderInfo));
		}
		return customerOrderInfo;
	}

	private static FDCustomerOrderInfo buildFDCustomerOrderInfoData(FDCustomerOrderInfoData fdCustomerOrderInfoData) {
		FDCustomerOrderInfo customerOrderInfo = new FDCustomerOrderInfo();
		customerOrderInfo.setAltPhone(fdCustomerOrderInfoData.getAltPhone());
		customerOrderInfo.setAmount(fdCustomerOrderInfoData.getAmount());
		customerOrderInfo.setChefsTable(fdCustomerOrderInfoData.isChefsTable());
		if(fdCustomerOrderInfoData.getCutoffTime() != null)
		customerOrderInfo.setCutoffTime(new java.sql.Date(fdCustomerOrderInfoData.getCutoffTime()));
		if(fdCustomerOrderInfoData.getDeliveryDate() != null)
		customerOrderInfo.setDeliveryDate(new java.sql.Date(fdCustomerOrderInfoData.getDeliveryDate()));
		customerOrderInfo.setDeliveryType(fdCustomerOrderInfoData.getDeliveryType());
		customerOrderInfo.setEmail(fdCustomerOrderInfoData.getEmail());
		customerOrderInfo.setEmailType(fdCustomerOrderInfoData.getEmailType());
		if(fdCustomerOrderInfoData.getEndTime()  != null)
		customerOrderInfo.setEndTime(new java.sql.Date(fdCustomerOrderInfoData.getEndTime()));
		customerOrderInfo.seteStore(fdCustomerOrderInfoData.geteStore());
		customerOrderInfo.setFacility(fdCustomerOrderInfoData.getFacility());
		customerOrderInfo.setFirstName(fdCustomerOrderInfoData.getFirstName());
		if(fdCustomerOrderInfoData.getIdentity()  != null)
		customerOrderInfo.setIdentity(buildFDIdentity(fdCustomerOrderInfoData.getIdentity()));
		if(fdCustomerOrderInfoData.getLastCroModDate() != null)
		customerOrderInfo.setLastCroModDate(new java.sql.Date(fdCustomerOrderInfoData.getLastCroModDate()));
		customerOrderInfo.setLastName(fdCustomerOrderInfoData.getLastName());
		if(fdCustomerOrderInfoData.getPaymentType() != null)
		customerOrderInfo.setPaymentType(EnumPaymentType.getEnum(fdCustomerOrderInfoData.getPaymentType()));
		customerOrderInfo.setPhone(fdCustomerOrderInfoData.getPhone());
		customerOrderInfo.setPromotionAmt(fdCustomerOrderInfoData.getPromotionAmt());
		customerOrderInfo.setRouteNum(fdCustomerOrderInfoData.getRouteNum());
		if(fdCustomerOrderInfoData.getRsvType() != null)
		customerOrderInfo.setRsvType(EnumReservationType.getEnum(fdCustomerOrderInfoData.getRsvType()));
		customerOrderInfo.setSaleId(fdCustomerOrderInfoData.getSaleId());
		if(fdCustomerOrderInfoData.getStartTime() != null)
		customerOrderInfo.setStartTime(new java.sql.Date(fdCustomerOrderInfoData.getStartTime()));
		if(fdCustomerOrderInfoData.getStatus() != null)
		customerOrderInfo.setOrderStatus(EnumSaleStatus.getSaleStatus(fdCustomerOrderInfoData.getStatus()));
		customerOrderInfo.setStopSequence(fdCustomerOrderInfoData.getStopSequence());
		customerOrderInfo.setSubTotal(fdCustomerOrderInfoData.getSubTotal());
		customerOrderInfo.setVip(fdCustomerOrderInfoData.isVip());
		customerOrderInfo.setWaveNum(fdCustomerOrderInfoData.getWaveNum());
		return customerOrderInfo;
	}
	
	private static FDIdentity buildFDIdentity(FDIdentityData identity) {
		return  new FDIdentity(identity.getErpCustomerPK(),identity.getFdCustomerPK());
	}

	public static ErpReturnOrderData buildErpReturnOrderData(ErpReturnOrderModel returnOrder) {
		ErpReturnOrderData returnOrderData = new ErpReturnOrderData();
		returnOrderData.setContainsDeliveryPass(returnOrder.isContainsDeliveryPass());
		returnOrderData.setDlvPassApplied(returnOrder.isDlvPassApplied());
		returnOrderData.setDlvPassId(returnOrder.getDeliveryPassId());
		returnOrderData.setRestockingApplied(returnOrder.isRestockingApplied());
		returnOrderData.setInvoiceLines(buildInvoiceLineDataList(returnOrder.getInvoiceLines()));
		returnOrderData.setCharges(buildChargeLineModelData(returnOrder.getCharges()));
		returnOrderData.setAmount(returnOrder.getAmount());
		returnOrderData.setCustomerId(returnOrder.getCustomerId());
		returnOrderData.setTransactionId(returnOrder.getId());
		returnOrderData.setInvoiceNumber(returnOrder.getInvoiceNumber());
		returnOrderData.setSubtotal(returnOrder.getSubTotal());
		returnOrderData.setTax(returnOrder.getTax());
		returnOrderData.setTransactionDate(returnOrder.getTransactionDate());
		returnOrderData.setTransactionInitiator(returnOrder.getTransactionInitiator());
		if(returnOrder.getTransactionSource() != null)
		returnOrderData.setTransactionSource(returnOrder.getTransactionSource().getName());
		returnOrderData.setTransactionId(returnOrder.getId());
		return returnOrderData;
	}

	private static List<ErpChargeLineData> buildChargeLineModelData(List<ErpChargeLineModel> charges) {

		List<ErpChargeLineData> chargeLineModelDataList = new ArrayList<ErpChargeLineData>();
		if(charges != null){
		for (ErpChargeLineModel erpChargeLineModel : charges) {
			chargeLineModelDataList.add(buildChargeLineData(erpChargeLineModel));
		}
		}
		return chargeLineModelDataList;
	
	}

	public  static ErpChargeLineData buildChargeLineData(ErpChargeLineModel erpChargeLineModel) {
		ErpChargeLineData chargeLineData = new ErpChargeLineData();
		chargeLineData.setAmount(erpChargeLineModel.getAmount());
		chargeLineData.setDiscount(buildDiscountData(erpChargeLineModel.getDiscount()));
		chargeLineData.setId(erpChargeLineModel.getId());
		chargeLineData.setReasonCode(erpChargeLineModel.getReasonCode());
		if(erpChargeLineModel.getTaxationType() != null)
		chargeLineData.setTaxationType(erpChargeLineModel.getTaxationType().getName());
		chargeLineData.setTaxRate(erpChargeLineModel.getTaxRate());
		if(erpChargeLineModel.getType()  != null)
		chargeLineData.setType(erpChargeLineModel.getType().getName());
		return chargeLineData;
	}

	private static DiscountData buildDiscountData(Discount discount) {
		DiscountData discountData =  null;
		if(discount != null){
			discountData = new DiscountData();
			discountData.setPromotionCode(discount.getPromotionCode());
			discountData.setAmount(discount.getAmount());
			if(discount.getDiscountType() != null)
			discountData.setDiscountType(String.valueOf(discount.getDiscountType().getId()));
			discountData.setMaxPercentageDiscount(discount.getMaxPercentageDiscount());
			discountData.setPromotionDescription(discount.getPromotionDescription());
			discountData.setSkuLimit(discount.getSkuLimit());
		}
		return discountData;
	}

	private static List<ErpInvoiceLineData> buildInvoiceLineDataList(List<ErpInvoiceLineModel> invoiceLines) {
		List<ErpInvoiceLineData> invoiceLineModelData = null;
		if(invoiceLines != null){
			invoiceLineModelData = new ArrayList<ErpInvoiceLineData>();
		for (ErpInvoiceLineModel erpInvoiceLineModel : invoiceLines) {
			ErpInvoiceLineData invoiceLineData = new ErpInvoiceLineData();
			invoiceLineData.setActualCost(erpInvoiceLineModel.getActualCost());
			invoiceLineData.setActualDiscountAmount(erpInvoiceLineModel.getActualDiscountAmount());
			invoiceLineData.setCouponDiscountAmount(erpInvoiceLineModel.getCouponDiscountAmount());
			invoiceLineData.setCustomizationPrice(erpInvoiceLineModel.getCustomizationPrice());
			invoiceLineData.setDepositValue(erpInvoiceLineModel.getDepositValue());
			invoiceLineData.setInvoiceLineId(erpInvoiceLineModel.getId());
			invoiceLineData.setMaterialNumber(erpInvoiceLineModel.getMaterialNumber());
			invoiceLineData.setOrderLineNumber(erpInvoiceLineModel.getOrderLineNumber());
			invoiceLineData.setPrice(erpInvoiceLineModel.getPrice());
			invoiceLineData.setQuantity(erpInvoiceLineModel.getQuantity());
			invoiceLineData.setTaxValue(erpInvoiceLineModel.getTaxValue());
			invoiceLineData.setWeight(erpInvoiceLineModel.getWeight());
			//invoiceLineData.setSubSkuStatus(erpInvoiceLineModel.getSubSkuStatus());
			//invoiceLineData.setSubstitutedSkuCode(erpInvoiceLineModel.getSubstitutedSkuCode());
			invoiceLineModelData.add(invoiceLineData);
		}
		}
		return invoiceLineModelData;
	}

	public static ResubmitPaymentData buildResubmitPaymentData(ErpPaymentMethodI payment, Collection charges) {
		ResubmitPaymentData resubmitPaymentData = new ResubmitPaymentData();
		
		resubmitPaymentData.setErpChargeLineModel(buildChargeLineModelData((List<ErpChargeLineModel>) charges));
		resubmitPaymentData.setPaymentMethod(SapGatewayConverter.buildPaymentMethodData(payment));
		return resubmitPaymentData;
	}

	public static ErpRedeliveryData buildErpRedeliveryData(ErpRedeliveryModel redeliveryModel) {
		ErpRedeliveryData erpRedeliveryData = new ErpRedeliveryData();
		erpRedeliveryData.setDeliveryInfo(SapGatewayConverter.buildDeliveryInfoData(redeliveryModel.getDeliveryInfo()));
		erpRedeliveryData.setCustomerId(redeliveryModel.getCustomerId());
		erpRedeliveryData.setTransactionId(redeliveryModel.getId());
		erpRedeliveryData.setTransactionDate(redeliveryModel.getTransactionDate());
		erpRedeliveryData.setTransactionInitiator(redeliveryModel.getTransactionInitiator());
		erpRedeliveryData.setTransactionSource(redeliveryModel.getTransactionSource().getCode());
		return erpRedeliveryData;
	}

	public static MealData buildMealData(MealModel mealModel) {
		MealData mealData = new MealData();
		mealData.setDelivery(mealModel.getDelivery());
		mealData.setId(mealModel.getId());
		mealData.setItems(buildMealItemData(mealModel.getItems()));
		mealData.setName(mealModel.getName());
		mealData.setPrice(mealModel.getPrice());
		if(mealModel.getStatus()!= null)
		mealData.setStatus(mealModel.getStatus().getTypeName());
		return mealData;
	}
	
	private static List<MealItemData> buildMealItemData(List<MealItemModel> items) {
		List<MealItemData> mealItemList = new  ArrayList<MealItemData>();
		for (MealItemModel mealItemModel : items) {
			MealItemData mealItemData = new MealItemData();
			mealItemData.setId(mealItemModel.getId());
			mealItemData.setName(mealItemModel.getName());
			mealItemData.setQuantity(mealItemModel.getQuantity());
			if(mealItemModel.getType() != null)
			mealItemData.setType(mealItemModel.getType().getTypeName());
			mealItemData.setUnitPrice(mealItemModel.getUnitPrice());
			mealItemList.add(mealItemData);
		}
		return mealItemList;
	}

	public static MealModel buildMealModel(MealData data) {
		MealModel mealModel = new MealModel();
		mealModel.setDelivery(data.getDelivery());
		mealModel.setId(data.getId());
		mealModel.setItems(buildMealItemModel(data.getItems()));
		mealModel.setName(data.getName());
		mealModel.setPrice(data.getPrice());
		mealModel.setStatus(EnumMealStatus.getType(data.getStatus()));
		return mealModel;
	}
	private static List buildMealItemModel(List<MealItemData> items) {
		List mealItemList = new  ArrayList();
		for (MealItemData mealItemData : items) {
			MealItemModel mealItem = new MealItemModel();
			if(mealItemData.getId() != null)
			mealItem.setId(mealItemData.getId());
			mealItem.setName(mealItemData.getName());
			mealItem.setQuantity(mealItemData.getQuantity());
			mealItem.setType(EnumMealItemType.getType(mealItemData.getType()));
			mealItem.setUnitPrice(mealItemData.getUnitPrice());
			mealItemList.add(mealItem);
		}
		return mealItemList;
	}

	public static List buildCreditSummaryList(List<CreditSummaryData> data) {
		List<FDCreditSummary> creditSummaryList = new ArrayList<FDCreditSummary>();
		for (CreditSummaryData fdCreditSummary : data) {
			FDCreditSummary creditSummary = new FDCreditSummary();
			creditSummary.setCreditAmount(fdCreditSummary.getCreditAmount());
			creditSummary.setCustomerName(fdCreditSummary.getCustomerName());
			creditSummary.setDeliveryDate(fdCreditSummary.getDeliveryDate());
			creditSummary.setInvoiceAmount(fdCreditSummary.getInvoiceAmount());
			creditSummary= buildItem(fdCreditSummary.getItems(),creditSummary);
			creditSummary.setNote(fdCreditSummary.getNote());
			creditSummary.setNumberOfOrders(fdCreditSummary.getNumberOfOrders());
			creditSummary.setOrderNumber(fdCreditSummary.getOrderNumber());
			creditSummary.setPreviousCreditAmount(fdCreditSummary.getPreviousCreditAmount());
			creditSummary.setStatus(fdCreditSummary.getStatus());
		}
		return creditSummaryList;
	}

	private static FDCreditSummary buildItem(List<ItemData> items,FDCreditSummary creditSummary) {
		
		for (ItemData item : items) {
			Item itemData = new Item();
			itemData.setConfiguration(item.getConfiguration());
			itemData.setDescription(item.getDescription());
			itemData.setQuantity(item.getQuantity());
			itemData.setReason(item.getReason());
			itemData.setSkuCode(item.getSkuCode());
			creditSummary.addItem(itemData);
		}
		return creditSummary;
	}

	public static List buildCutOffTimeInfo(List<FDCutoffTimeInfoData> data) {
		List<FDCutoffTimeInfo> cutOffTimeList = new ArrayList<FDCutoffTimeInfo>();
		for (FDCutoffTimeInfoData fdCutoffTimeInfo : data) {
			FDCutoffTimeInfo cutoffTimeInfoData = new FDCutoffTimeInfo(EnumSaleStatus.getSaleStatus(fdCutoffTimeInfo.getStatus()), fdCutoffTimeInfo.getCutoffTime(), fdCutoffTimeInfo.getOrderCount());
			cutOffTimeList.add(cutoffTimeInfoData);
			
		}
		return cutOffTimeList;
	}

	public static RouteStopReportData buildRouteStopReportData(Date date,
			String route, String stop1, String stop2, String store,
			String facility,String call_format) {
		RouteStopReportData routeStopReport = new RouteStopReportData();
		routeStopReport.setCall_format(call_format);
		routeStopReport.setDate(date);
		routeStopReport.setFacility(facility);
		routeStopReport.setRoute(route);
		routeStopReport.setStop1(stop1);
		routeStopReport.setStop2(stop2);
		routeStopReport.setStore(store);
		return routeStopReport;
	}


	public static List buildCrmOrderStatusReportLineList(List<CrmOrderStatusReportLineData> data) {
		 List<CrmOrderStatusReportLine> orderStatusReportLineList = new ArrayList<CrmOrderStatusReportLine>();
		for (CrmOrderStatusReportLineData crmOrderStatusReportLineData : data) {
			CrmOrderStatusReportLine orderStatusReportLine = new CrmOrderStatusReportLine(crmOrderStatusReportLineData.getSaleId(),
					EnumSaleStatus.getSaleStatus(crmOrderStatusReportLineData.getStatus()), crmOrderStatusReportLineData.getSapNumber(),
					crmOrderStatusReportLineData.geteStore(), crmOrderStatusReportLineData.getFacility());
			orderStatusReportLineList.add(orderStatusReportLine);
		}
		return orderStatusReportLineList;
	}

	public static List buildCrmSettlementReportLine(List<CrmSettlementProblemReportLineData> data) {
		List  settlementProblemReportList = new ArrayList();
		for (CrmSettlementProblemReportLineData crmSettlementProblemReportData : data) {
			CrmSettlementProblemReportLine crmSettlementProblemReportLine = new CrmSettlementProblemReportLine(crmSettlementProblemReportData.getSaleId(), crmSettlementProblemReportData.getCustomerName(),
					crmSettlementProblemReportData.getAmount(), new java.sql.Date(crmSettlementProblemReportData.getDeliveryDate()), new java.sql.Date(crmSettlementProblemReportData.getFailureDate()),
					EnumSaleStatus.getSaleStatus(crmSettlementProblemReportData.getStatus()), EnumTransactionType.getTransactionType(crmSettlementProblemReportData.getTransactionType()), 
					EnumPaymentMethodType.getEnum(crmSettlementProblemReportData.getPaymentMethodType()));
			settlementProblemReportList.add(crmSettlementProblemReportLine);
		}
		return settlementProblemReportList;
	}

	public static SettlementProblemReportData buildSettlementReportData(
			String[] statusCodes, String[] transactionTypes,
			Date failureEndDate, Date failureStartDate) {
		SettlementProblemReportData settlementReportData = new SettlementProblemReportData();
		settlementReportData.setFailureEndDate(failureEndDate);
		settlementReportData.setFailureStartDate(failureStartDate);
		settlementReportData.setStatusCodes(statusCodes);
		settlementReportData.setTransactionTypes(transactionTypes);
		return settlementReportData;
	}

	public static List buildAuthInfo(List<FDAuthInfoData> data) {
		List authInfoDataList = new ArrayList();
		for (FDAuthInfoData fdAuthInfoData : data) {
			FDAuthInfo authInfo = new FDAuthInfo(fdAuthInfoData.getSaleId());
			authInfo.setAbaRouteNumber(fdAuthInfoData.getAbaRouteNumber());
			authInfo.setAuthAmount(fdAuthInfoData.getAuthAmount());
			authInfo.setAuthCode(fdAuthInfoData.getAuthCode());
			authInfo.setAuthDescription(fdAuthInfoData.getAuthDescription());
			if(fdAuthInfoData.getBankAccountType()!= null)
			authInfo.setBankAccountType(EnumBankAccountType.getEnum(fdAuthInfoData.getBankAccountType()));
			authInfo.setCardType(fdAuthInfoData.getCardType());
			authInfo.setCCLastFourNum(fdAuthInfoData.getCcLastFourNum());
			authInfo.setDeliveryDate(new java.sql.Date(fdAuthInfoData.getDeliveryDate()));
			authInfo.seteStore(fdAuthInfoData.geteStore());
			authInfo.setFacility(fdAuthInfoData.getFacility());
			authInfo.setFirstName(fdAuthInfoData.getFirstName());
			authInfo.setLastName(fdAuthInfoData.getLastName());
			authInfo.setNameOnCard(fdAuthInfoData.getNameOnCard());
			if(fdAuthInfoData.getOrderType() != null)
			authInfo.setOrderType(fdAuthInfoData.getOrderType());
			if(fdAuthInfoData.getPaymentMethodType()!= null)
			authInfo.setPaymentMethodType(EnumPaymentMethodType.getEnum(fdAuthInfoData.getPaymentMethodType()));
			if(fdAuthInfoData.getSaleStatus()!= null)
			authInfo.setSaleStatus(EnumSaleStatus.getSaleStatus(fdAuthInfoData.getSaleStatus()));
			authInfo.setTransactionDateTime(new java.sql.Date(fdAuthInfoData.getTxDateTime()));
			authInfoDataList.add(authInfo);
		}
		return authInfoDataList;
	}

	public static FDAuthInfoSearchCriteriaData buildAUthInfoSearchCriteriaData(
			FDAuthInfoSearchCriteria criteria) {
		FDAuthInfoSearchCriteriaData authInfoSearchCriteriaData = new FDAuthInfoSearchCriteriaData();
		authInfoSearchCriteriaData.setAbaRouteNumber(criteria.getAbaRouteNumber());
		if(criteria.getBankAccountType() != null)
		authInfoSearchCriteriaData.setBankAccountType(criteria.getBankAccountType().getName());
		if(criteria.getCardType() != null)
		authInfoSearchCriteriaData.setCardType(criteria.getCardType().getName());
		authInfoSearchCriteriaData.setCcKnownNum(criteria.getCCKnownNum());
		authInfoSearchCriteriaData.setChargedAmount(criteria.getChargedAmount());
		if(criteria.getPaymentMethodType()!= null)
		authInfoSearchCriteriaData.setPaymentMethodType(criteria.getPaymentMethodType().getName());
		authInfoSearchCriteriaData.setTransDate(criteria.getTransDate());
		authInfoSearchCriteriaData.setTransMonth(criteria.getTransMonth());
		authInfoSearchCriteriaData.setTransYear(criteria.getTransYear());
		return authInfoSearchCriteriaData;
		
	}

	public static List buildMakeGoodOrder(List<MakeGoodOrderInfoData> data) {
		List<MakeGoodOrderInfo> makeGoodOrderDataList = new ArrayList<MakeGoodOrderInfo>();
		for (MakeGoodOrderInfoData makeGoodOrderInfo : data) {
			MakeGoodOrderInfo makeGoodOrderInfoModel = new MakeGoodOrderInfo(makeGoodOrderInfo.getSaleId());
			makeGoodOrderInfoModel.setAmount(makeGoodOrderInfo.getAmount());
			makeGoodOrderInfoModel.setDeliveryDate(new java.sql.Date(makeGoodOrderInfo.getDeliveryDate()));
			makeGoodOrderInfoModel.setFirstName(makeGoodOrderInfo.getFirstName());
			makeGoodOrderInfoModel.setLastName(makeGoodOrderInfo.getLastName());
			makeGoodOrderInfoModel.setOrderPlacedDate(new java.sql.Date(makeGoodOrderInfo.getOrderPlacedDate()));
			makeGoodOrderInfoModel.setRoute(makeGoodOrderInfo.getRoute());
			if(makeGoodOrderInfo.getSaleStatus() != null)
			makeGoodOrderInfoModel.setSaleStatus(EnumSaleStatus.getSaleStatus(makeGoodOrderInfo.getSaleStatus()));
			makeGoodOrderInfoModel.setStop(makeGoodOrderInfo.getStop());
			makeGoodOrderDataList.add(makeGoodOrderInfoModel);
		}
		return makeGoodOrderDataList;
	}

	public static CancelReservationData buildCancelReservationData(GenericSearchCriteria resvCriteria, String initiator, String notes) {
		CancelReservationData cancelReservationData = new CancelReservationData();
		cancelReservationData.setInitiator(initiator);
		cancelReservationData.setNotes(notes);
		cancelReservationData.setResvCriteria(resvCriteria.getSearchType().getName());
		cancelReservationData.setCriteriaMap(resvCriteria.getCriteriaMap());
		return cancelReservationData;
	}

	public static ReturnOrderData buildReturnOrderData(FDActionInfo info,
			List returnOrders) {
		ReturnOrderData returnOrder = new ReturnOrderData();
		returnOrder.setInfo(FDActionInfoConverter.buildActionInfoData(info));
		returnOrder.setReturnOrders(buildFDCustomerOrderInfoDataList(returnOrders));
		return returnOrder;
	}

	private static List<FDCustomerOrderInfoData> buildFDCustomerOrderInfoDataList(List<FDCustomerOrderInfo> returnOrders) {
		List<FDCustomerOrderInfoData> orderInfo = new ArrayList<FDCustomerOrderInfoData>();
		for (FDCustomerOrderInfo  fdCustomerOrderInfo: returnOrders) {
			orderInfo.add(buildFDCustomerOrderInfoData(fdCustomerOrderInfo));
		}
		return orderInfo;
		
	}

	private static FDCustomerOrderInfoData buildFDCustomerOrderInfoData(FDCustomerOrderInfo fdCustomerOrderInfo) {
		FDCustomerOrderInfoData customerOrderInfoData = new FDCustomerOrderInfoData();
		customerOrderInfoData.setAltPhone(fdCustomerOrderInfo.getAltPhone());
		customerOrderInfoData.setAmount(fdCustomerOrderInfo.getAmount());
		customerOrderInfoData.setChefsTable(fdCustomerOrderInfo.isChefsTable());
		if(fdCustomerOrderInfo.getCutoffTime()!= null)
		customerOrderInfoData.setCutoffTime(fdCustomerOrderInfo.getCutoffTime().getTime());
		if(fdCustomerOrderInfo.getDeliveryDate() != null)
		customerOrderInfoData.setDeliveryDate(fdCustomerOrderInfo.getDeliveryDate().getTime());
		customerOrderInfoData.setDeliveryType(fdCustomerOrderInfo.getDeliveryType());
		customerOrderInfoData.setEmail(fdCustomerOrderInfo.getEmail());
		customerOrderInfoData.setEmailType(fdCustomerOrderInfo.getEmailType());
		if(fdCustomerOrderInfo.getEndTime() != null)
		customerOrderInfoData.setEndTime(fdCustomerOrderInfo.getEndTime().getTime());
		customerOrderInfoData.seteStore(fdCustomerOrderInfo.geteStore());
		customerOrderInfoData.setFacility(fdCustomerOrderInfo.getFacility());
		customerOrderInfoData.setFirstName(fdCustomerOrderInfo.getFirstName());
		if(fdCustomerOrderInfo.getIdentity() != null)
		customerOrderInfoData.setIdentity(buildFDIdentityData(fdCustomerOrderInfo.getIdentity()));
		if(fdCustomerOrderInfo.getLastCroModDate() != null)
		customerOrderInfoData.setLastCroModDate(fdCustomerOrderInfo.getLastCroModDate().getTime());
		customerOrderInfoData.setLastName(fdCustomerOrderInfo.getLastName());
		if(fdCustomerOrderInfo.getPaymentType() != null)
		customerOrderInfoData.setPaymentType(fdCustomerOrderInfo.getPaymentType().getName());
		customerOrderInfoData.setPhone(fdCustomerOrderInfo.getPhone());
		customerOrderInfoData.setPromotionAmt(fdCustomerOrderInfo.getPromotionAmt());
		customerOrderInfoData.setRouteNum(fdCustomerOrderInfo.getRouteNum());
		if(fdCustomerOrderInfo.getRsvType() != null)
		customerOrderInfoData.setRsvType(fdCustomerOrderInfo.getRsvType().getName());
		customerOrderInfoData.setSaleId(fdCustomerOrderInfo.getSaleId());
		if(fdCustomerOrderInfo.getStartTime() != null)
		customerOrderInfoData.setStartTime(fdCustomerOrderInfo.getStartTime().getTime());
		if(fdCustomerOrderInfo.getOrderStatus() != null)
		customerOrderInfoData.setStatus(fdCustomerOrderInfo.getOrderStatus().getStatusCode());
		customerOrderInfoData.setStopSequence(fdCustomerOrderInfo.getStopSequence());
		customerOrderInfoData.setSubTotal(fdCustomerOrderInfo.getSubTotal());
		customerOrderInfoData.setVip(fdCustomerOrderInfo.isVIP());
		customerOrderInfoData.setWaveNum(fdCustomerOrderInfo.getWaveNum());
		return customerOrderInfoData;
	}
	
	private static FDIdentityData buildFDIdentityData(FDIdentity identity) {
		FDIdentityData identityData = new FDIdentityData();
		identityData.setErpCustomerPK(identity.getErpCustomerPK());
		identityData.setFdCustomerPK(identity.getFDCustomerPK());
		return identityData;
	}

	public static Map buildReturnOrderResponse(Map<String, List<FDCustomerOrderInfoData>> data) {
		Map<String , List<FDCustomerOrderInfo>> orderInfoMap = new HashMap<String, List<FDCustomerOrderInfo>>();
		for (String returnOrder : data.keySet()) {
			List<FDCustomerOrderInfoData> info = data.get(returnOrder);
			List<FDCustomerOrderInfo> orderInfo = buildFDCustomerOrderinfoList(info);
			orderInfoMap.put(returnOrder, orderInfo);
		}
		return orderInfoMap;
	}

	

	private static List<FDCustomerOrderInfo> buildFDCustomerOrderinfoList(List<FDCustomerOrderInfoData> info) {
		 List<FDCustomerOrderInfo> customerOrderInfoList = new ArrayList<FDCustomerOrderInfo>();
		 for (FDCustomerOrderInfoData fdCustomerOrderInfoData : info) {
			 customerOrderInfoList.add(buildFDCustomerOrderInfoData(fdCustomerOrderInfoData));
		}
		return customerOrderInfoList;
	}

	public static CrmClick2CallData buildClick2CallData(CrmClick2CallModel click2CallModel) {
		CrmClick2CallData callModelData = new CrmClick2CallData();
		callModelData.setCroModDate(click2CallModel.getCroModDate());
		callModelData.setDays(buildClick2CallTimeModel(click2CallModel.getDays()));
		callModelData.setDeliveryZones(click2CallModel.getDeliveryZones());
		callModelData.setEligibleCustomers(click2CallModel.getEligibleCustomers());
		callModelData.setId(click2CallModel.getId());
		callModelData.setNextDayTimeSlot(click2CallModel.isNextDayTimeSlot());
		callModelData.setStatus(click2CallModel.isStatus());
		callModelData.setUserId(click2CallModel.getUserId());
		return callModelData;
	}
	
	private static CrmClick2CallTimeData[] buildClick2CallTimeModel(CrmClick2CallTimeModel[] days) {
		CrmClick2CallTimeData[] click2Calldata = new CrmClick2CallTimeData[7];
		for (CrmClick2CallTimeModel crmClick2CallTime : days) {
			int i = 0;
			CrmClick2CallTimeData data = new CrmClick2CallTimeData();
			data.setClick2CallId(crmClick2CallTime.getClick2CallId());
			data.setDayName(crmClick2CallTime.getDayName());
			data.setEndTime(crmClick2CallTime.getEndTime());
			data.setId(crmClick2CallTime.getId());
			data.setShow(crmClick2CallTime.isShow());
			data.setStartTime(crmClick2CallTime.getStartTime());
			click2Calldata[i++] = data;
		}
		return click2Calldata;
	}

	public static List<CrmVSCampaignModel> buildCrmVSCampaignModelList(
			List<CrmVSCampaignData> data) {
		List<CrmVSCampaignModel> modelList = new ArrayList<CrmVSCampaignModel>();
		for (CrmVSCampaignData crmVSCampaignData : data) {
			modelList.add(buildCrmVSCampaignModel(crmVSCampaignData));
		}
		return modelList;
	}


	public static CrmVSCampaignModel buildCrmVSCampaignModel(
			CrmVSCampaignData data) {
		CrmVSCampaignModel campaignModel = new CrmVSCampaignModel();
		campaignModel.setAddByDate(data.getAddByDate());
		campaignModel.setAddByUser(data.getAddByUser());
		campaignModel.setCallId(data.getCallId());
		campaignModel.setCampaignId(data.getCampaignId());
		campaignModel.setCampaignMenuId(data.getCampaignMenuId());
		campaignModel.setCampaignName(data.getCampaignName());
		campaignModel.setChangeByDate(data.getChangeByDate());
		campaignModel.setChangeByUser(data.getChangeByUser());
		campaignModel.setCustomerId(data.getCustomerId());
		campaignModel.setDelay(data.getDelay());
		campaignModel.setDelayMinutes(data.getDelayMinutes());
		campaignModel.setDeliveredCallsAM(data.getDeliveredCallsAM());
		campaignModel.setDeliveredCallsLive(data.getDeliveredCallsLive());
		campaignModel.setEndTime(data.getEndTime());
		campaignModel.setLateIssueId(data.getLateIssueId());
		campaignModel.setManual(data.isManual());
		campaignModel.setPhonenumber(data.getPhonenumber());
		campaignModel.setReasonId(data.getReasonId());
		campaignModel.setRedial(data.getRedial());
		campaignModel.setRoute(data.getRoute());
		campaignModel.setSaleId(data.getSaleId());
		campaignModel.setScheduledCalls(data.getScheduledCalls());
		campaignModel.setSoundfileName(data.getSoundfileName());
		campaignModel.setSoundFileText(data.getSoundFileText());
		campaignModel.setStartTime(data.getStartTime());
		campaignModel.setStatus(data.getStatus());
		campaignModel.setStopSequence(data.getStopSequence());
		campaignModel.setUndeliveredCalls(data.getUndeliveredCalls());
		campaignModel.setUpdatable(data.isUpdatable());
		campaignModel.setVsDetailsID(data.getVsDetailsID());
		campaignModel.setPhonenumbers(data.getPhonenumbers());
		campaignModel.setRouteList(data.getRouteList());
		if(data.getId() != null)
		campaignModel.setId(data.getId());
		return campaignModel;
	}

	public static CrmVSCampaignData buildCrmVSCampaignData(CrmVSCampaignModel model) {
		CrmVSCampaignData campaign = new CrmVSCampaignData();
		campaign.setAddByDate(model.getAddByDate());
		campaign.setAddByUser(model.getAddByUser());
		campaign.setCallId(model.getCallId());
		campaign.setCampaignId(model.getCampaignId());
		campaign.setCampaignMenuId(model.getCampaignMenuId());
		campaign.setCampaignName(model.getCampaignName());
		campaign.setChangeByDate(model.getChangeByDate());
		campaign.setChangeByUser(model.getChangeByUser());
		campaign.setCustomerId(model.getCustomerId());
		campaign.setDelay(model.getDelay());
		campaign.setDelayMinutes(model.getDelayMinutes());
		campaign.setDeliveredCallsAM(model.getDeliveredCallsAM());
		campaign.setDeliveredCallsLive(model.getDeliveredCallsLive());
		campaign.setEndTime(model.getEndTime());
		campaign.setCampaignId(model.getId());
		campaign.setLateIssueId(model.getLateIssueId());
		campaign.setManual(model.getManual());
		campaign.setPhonenumber(model.getPhonenumber());
		campaign.setReasonId(model.getReasonId());
		campaign.setRedial(model.getRedial());
		campaign.setRoute(model.getRoute());
		campaign.setSaleId(model.getSaleId());
		campaign.setScheduledCalls(model.getScheduledCalls());
		campaign.setSoundfileName(model.getSoundfileName());
		campaign.setSoundFileText(model.getSoundFileText());
		campaign.setStartTime(model.getStartTime());
		campaign.setStatus(model.getStatus());
		campaign.setStopSequence(model.getStopSequence());
		campaign.setUndeliveredCalls(model.getUndeliveredCalls());
		campaign.setUpdatable(model.isUpdatable());
		campaign.setVsDetailsID(model.getVsDetailsID());
		campaign.setPhonenumbers(model.getPhonenumbers());
		campaign.setRouteList(model.getRouteList());
		campaign.setId(model.getId());
		return campaign;
	}

	public static List buildServiceGenericModel(List data) {
		List genericmodel = new ArrayList (); 
		for (Object object : data) {
			if(object instanceof FDCustomerOrderInfoData){
				genericmodel.add(buildFDCustomerOrderInfoData((FDCustomerOrderInfoData) object));
			}
			else if(object instanceof FDCustomerReservationInfoData){
				genericmodel.add(buildCustomerReservation((FDCustomerReservationInfoData) object));
			}
			else if(object instanceof SettlementBatchInfoData){
				genericmodel.add(buildSettlementBatchInfo((SettlementBatchInfoData) object));
				
			}
			else if(object instanceof RestrictionData){
				genericmodel.add(buildRestriction((RestrictionData) object));
			}
			else if(object instanceof AlcoholRestrictionData){
				genericmodel.add(buildAlcoholRestriction((AlcoholRestrictionData) object));
			}
			else if(object instanceof RestrictedAddressModelData){
				genericmodel.add(buildRestrictedAddressModel((RestrictedAddressModelData) object));
			}
			else{
				  genericmodel.add(object);
			}
		}
		return genericmodel;
		
	}

	private static SettlementBatchInfo buildSettlementBatchInfo(SettlementBatchInfoData settlementBatchInfo) {
		SettlementBatchInfo batchInfo = new SettlementBatchInfo();
		batchInfo.setBatch_id(settlementBatchInfo.getBatch_id());
		batchInfo.setBatch_response_msg(settlementBatchInfo.getBatch_response_msg());
		batchInfo.setBatch_status(settlementBatchInfo.getBatch_status());
		batchInfo.setMerchant_id(settlementBatchInfo.getMerchant_id());
		batchInfo.setProcessor_batch_id(settlementBatchInfo.getProcessor_batch_id());
		batchInfo.setReturn_amount(settlementBatchInfo.getReturn_amount());
		batchInfo.setReturn_transactions(settlementBatchInfo.getReturn_transactions());
		batchInfo.setSales_amount(settlementBatchInfo.getSales_amount());
		batchInfo.setSales_transactions(settlementBatchInfo.getSales_transactions());
		batchInfo.setSettle_date_time(new Timestamp(settlementBatchInfo.getSettle_date_time()));
		batchInfo.setSubmission_id(settlementBatchInfo.getSubmission_id());
		return batchInfo;
	}

	private static AlcoholRestriction buildAlcoholRestriction(AlcoholRestrictionData alcoholRestrictionData) {
		AlcoholRestriction alcoholRestriction = new AlcoholRestriction(alcoholRestrictionData.getId(), EnumDlvRestrictionCriterion.getEnum(alcoholRestrictionData.getCriterion()), 
				EnumDlvRestrictionReason.getEnum(alcoholRestrictionData.getReason()),alcoholRestrictionData.getName(), alcoholRestrictionData.getMessage(),
				alcoholRestrictionData.getDateRange().getStartdate(), alcoholRestrictionData.getDateRange().getEndDate(), EnumDlvRestrictionType.getEnum(alcoholRestrictionData.getType()),
				alcoholRestrictionData.getPath(), alcoholRestrictionData.getState(), alcoholRestrictionData.getCounty(),
				alcoholRestrictionData.getCity(), alcoholRestrictionData.getMunicipalityId(), alcoholRestrictionData.isAlcoholRestricted());
		return alcoholRestriction;
	}

	private static RestrictedAddressModel buildRestrictedAddressModel(RestrictedAddressModelData data) {
		return  FDStoreModelConverter.buildRestrictedAddressModel(data);
	}

	private static RestrictionI buildRestriction(RestrictionData restrictionData) {
		return FDStoreModelConverter.buildRestriction(restrictionData);
		
	}
		

	private static FDCustomerReservationInfo buildCustomerReservation(FDCustomerReservationInfoData fdCustomerReservationInfoData) {
			FDCustomerReservationInfo customerReservationInfo = new FDCustomerReservationInfo(fdCustomerReservationInfoData.getId(),
					fdCustomerReservationInfoData.getBaseDate(), fdCustomerReservationInfoData.getCutoffTime(), fdCustomerReservationInfoData.getFirstName(),
					fdCustomerReservationInfoData.getLastName(), new FDIdentity(fdCustomerReservationInfoData.getIdentity().getErpCustomerPK(),fdCustomerReservationInfoData.getIdentity().getFdCustomerPK()),
					fdCustomerReservationInfoData.getEmail(), fdCustomerReservationInfoData.getPhone(), fdCustomerReservationInfoData.getAltPhone(),
					fdCustomerReservationInfoData.getBusinessPhone(), fdCustomerReservationInfoData.getStartTime(), fdCustomerReservationInfoData.getEndTime(),
					fdCustomerReservationInfoData.getZone(), EnumReservationType.getEnum(fdCustomerReservationInfoData.getType()));
			customerReservationInfo.setAddress(SapGatewayConverter.buildContactAddressModel(fdCustomerReservationInfoData.getAddress()));
		return customerReservationInfo;
		
		
	}

	public static List buildMealModelList(List<MealData> data) {
		List list = new ArrayList();
		for (MealData mealData : data) {
			list.add(buildMealModel(mealData));
		}
		
		return list;
	}

	

}
