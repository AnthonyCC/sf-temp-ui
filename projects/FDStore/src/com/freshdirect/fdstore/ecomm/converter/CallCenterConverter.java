package com.freshdirect.fdstore.ecomm.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.crm.CrmOrderStatusReportLine;
import com.freshdirect.crm.CrmSettlementProblemReportLine;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.ecomm.converter.SapGatewayConverter;
import com.freshdirect.ecommerce.data.customer.CreditSummaryData;
import com.freshdirect.ecommerce.data.customer.CrmOrderStatusReportLineData;
import com.freshdirect.ecommerce.data.customer.CrmSettlementProblemReportLineData;
import com.freshdirect.ecommerce.data.customer.ErpRedeliveryData;
import com.freshdirect.ecommerce.data.customer.ErpReturnOrderData;
import com.freshdirect.ecommerce.data.customer.FDComplaintInfoData;
import com.freshdirect.ecommerce.data.customer.ItemData;
import com.freshdirect.ecommerce.data.customer.RouteStopReportData;
import com.freshdirect.ecommerce.data.customer.SettlementProblemReportData;
import com.freshdirect.ecommerce.data.ecoupon.DiscountData;
import com.freshdirect.ecommerce.data.sap.ErpChargeLineData;
import com.freshdirect.ecommerce.data.sap.ErpInvoiceLineData;
import com.freshdirect.fdstore.customer.FDComplaintInfo;
import com.freshdirect.fdstore.customer.FDCreditSummary;
import com.freshdirect.fdstore.customer.FDCreditSummary.Item;
import com.freshdirect.payment.EnumPaymentMethodType;

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


}
