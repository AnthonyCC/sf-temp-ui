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
import com.freshdirect.ecommerce.data.customer.CreditSummaryData;
import com.freshdirect.ecommerce.data.customer.CrmOrderStatusReportLineData;
import com.freshdirect.ecommerce.data.customer.CrmSettlementProblemReportLineData;
import com.freshdirect.ecommerce.data.customer.ErpRedeliveryData;
import com.freshdirect.ecommerce.data.customer.ErpReturnOrderData;
import com.freshdirect.ecommerce.data.customer.FDComplaintInfoData;
import com.freshdirect.ecommerce.data.customer.FDCustomerOrderInfoData;
import com.freshdirect.ecommerce.data.customer.FDCustomerReservationInfoData;
import com.freshdirect.ecommerce.data.customer.ItemData;
import com.freshdirect.ecommerce.data.customer.ResubmitPaymentData;
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
import com.freshdirect.fdstore.customer.FDComplaintInfo;
import com.freshdirect.fdstore.customer.FDCreditSummary;
import com.freshdirect.fdstore.customer.FDCreditSummary.Item;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecomm.gateway.FDStoreModelConverter;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
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

}
