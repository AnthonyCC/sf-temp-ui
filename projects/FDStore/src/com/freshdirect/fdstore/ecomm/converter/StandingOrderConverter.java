package com.freshdirect.fdstore.ecomm.converter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.ecomm.converter.FDActionInfoConverter;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.ecoupon.FDConfigurationData;
import com.freshdirect.ecommerce.data.ecoupon.FDSkuData;
import com.freshdirect.ecommerce.data.list.FDCustomerListData;
import com.freshdirect.ecommerce.data.standingorders.DeleteSOData;
import com.freshdirect.ecommerce.data.standingorders.FDCartLineData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderAltDeliveryDateData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderInfoData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderInfoListData;
import com.freshdirect.ecommerce.data.standingorders.ResultData;
import com.freshdirect.ecommerce.data.standingorders.ResultListData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrderErrorData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrderModifyData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrdersJobConfigData;
import com.freshdirect.ecommerce.data.standingorders.UnAvailabilityDetailsData;
import com.freshdirect.ecommerce.data.standingorders.UnavailabilityDetailsWrapper;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfo;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.SOResult;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.SOResult.ResultList;
import com.freshdirect.fdstore.standingorders.SOResult.Status;
import com.freshdirect.fdstore.standingorders.StandingOrdersJobConfig;
import com.freshdirect.fdstore.standingorders.UnAvailabilityDetails;
import com.freshdirect.fdstore.standingorders.UnavailabilityReason;
import com.freshdirect.payment.service.ModelConverter;

public class StandingOrderConverter {

	public static FDCustomerListData buildFDCustomerListData(FDCustomerList list) {
		return ListConverter.buildCustomerListData(list);
	}

	public static FDStandingOrder buildStandingOrder(FDStandingOrderData so) {
		if(null !=so){
			FDStandingOrder standingOrderModel = new FDStandingOrder();
			standingOrderModel.setActivate(so.getActivate());
			standingOrderModel.setAddressId(so.getAddressId());
			standingOrderModel.setAlcoholAgreement(so.isAlcoholAgreement());
			standingOrderModel.setAltDeliveryInfo(buildStandingOrderAltdeliveryDate(so.getAltDeliveryInfo()));
			standingOrderModel.setCustomerEmail(so.getCustomerEmail());
			standingOrderModel.setCustomerId(so.getCustomerId());
			standingOrderModel.setCustomerIdentity(buildFdIdentity(so.getCustomerIdentity()));
			standingOrderModel.setCustomerListId(so.getCustomerListId());
			standingOrderModel.setCustomerListName(so.getCustomerListName());
			standingOrderModel.setDefault(so.isDefault());
			standingOrderModel.setDeleted(so.isDeleted());
			if(so.getDeleteDate() != null)
			standingOrderModel.setDeleteDate(new Date(so.getDeleteDate()));
			if(so.getEndTime() != null)
			standingOrderModel.setEndTime(new Date(so.getEndTime()));
			standingOrderModel.setFrequency(so.getFrequency());
			standingOrderModel.setId(so.getId());
			standingOrderModel.setLastError(so.getLastError(), so.getErrorHeader(), so.getErrorDetail());
			standingOrderModel.setNewSo(so.isNewSo());
			if(so.getNextDeliveryDate() != null)
			standingOrderModel.setNextDeliveryDate(new Date(so.getNextDeliveryDate()));
			standingOrderModel.setPaymentMethodId(so.getPaymentMethodId());
			if(so.getPreviousDeliveryDate() != null)
			standingOrderModel.setPreviousDeliveryDate(new Date(so.getPreviousDeliveryDate()));
			standingOrderModel.setReminderOverlayForNewSo(so.isReminderOverlayForNewSo());
			if(so.getStartTime() != null)
			standingOrderModel.setStartTime(new Date(so.getStartTime()));
			standingOrderModel.setTipAmount(so.getTipAmount());
			if(null != so.getListData()){
				FDCustomerList fdStandingOrderList =ListConverter.buildFDCustomerList(so.getListData());
				try {
					populateSODetails(standingOrderModel,fdStandingOrderList);
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return standingOrderModel;
			}
		return null;
	}
	
	
	public static FDStandingOrderAltDeliveryDate buildStandingOrderAltdeliveryDate(FDStandingOrderAltDeliveryDateData data) {
		if(data != null){
		FDStandingOrderAltDeliveryDate dlvDate = new FDStandingOrderAltDeliveryDate();
		dlvDate.setActionType(data.getActionType());
		if(data.getAltDate() != null)
		dlvDate.setAltDate(new Date(data.getAltDate()));
		if(data.getAltEndTime() != null)
		dlvDate.setAltEndTime(new Date(data.getAltEndTime()));
		dlvDate.setAltEndTimeStr(data.getAltEndTimeStr());
		if(data.getAltStartTime() != null)
		dlvDate.setAltStartTime(new Date(data.getAltStartTime()));
		dlvDate.setAltStartTimeStr(data.getAltStartTimeStr());
		dlvDate.setCreatedBy(data.getCreatedBy());
		if(data.getCreatedTime() != null)
		dlvDate.setCreatedTime(new Date(data.getCreatedTime()));
		dlvDate.setDescription(data.getDescription());
		dlvDate.setModifiedBy(data.getModifiedBy());
		if(data.getModifiedTime() != null)
		dlvDate.setModifiedTime(new Date(data.getModifiedTime()));
		if(data.getOrigDate() != null)
		dlvDate.setOrigDate(new Date(data.getOrigDate()));
		if(data.getOrigEndTime() != null)
		dlvDate.setOrigEndTime(new Date(data.getOrigEndTime()));
		dlvDate.setOrigEndTimeStr(data.getOrigEndTimeStr());
		if(data.getOrigStartTime() != null)
		dlvDate.setOrigStartTime(new Date(data.getOrigStartTime()));
		dlvDate.setOrigStartTimeStr(data.getOrigStartTimeStr());
		dlvDate.setSoId(data.getSoId());
		if(data.getId() != null)
		dlvDate.setId(data.getId());
		return dlvDate;
		}
		return null;
		
	}
	
	public static FDIdentity buildFdIdentity(FDIdentityData customerIdentity) {
		return new FDIdentity(customerIdentity.getErpCustomerPK(),customerIdentity.getFdCustomerPK());
	}


	public static Collection<FDStandingOrder> buildStandingOrderList(Collection<FDStandingOrderData> data) {
		List<FDStandingOrder> standingOrderList = new ArrayList<FDStandingOrder>();
		for (FDStandingOrderData fdStandingOrderData : data) {
			standingOrderList.add(buildStandingOrder(fdStandingOrderData));
		}
		return standingOrderList;
	}

	public static StandingOrderModifyData buildStandingOrdermodifyData(FDActionInfo info, FDStandingOrder so , String saleId) throws FDResourceException {
		StandingOrderModifyData soModiftData = new StandingOrderModifyData();
		soModiftData.setInfo(FDActionInfoConverter.buildActionInfoData(info));
		soModiftData.setSaleId(saleId);
		soModiftData.setSo(buildStandingOrderData(so));
		return soModiftData;
		
	}

	public static FDStandingOrderData buildStandingOrderData(FDStandingOrder standingOrder) throws FDResourceException {
		if(standingOrder !=null){
		FDStandingOrderData standingOrderData = new FDStandingOrderData();
		standingOrderData.setActivate(standingOrder.getActivate());
		standingOrderData.setAddressId(standingOrder.getAddressId());
		standingOrderData.setAlcoholAgreement(standingOrder.isAlcoholAgreement());
		standingOrderData.setAltDeliveryInfo(buildStandingOrderAltDlvDateData(standingOrder.getAltDeliveryInfo()));
		standingOrderData.setCustomerEmail(standingOrder.getCustomerEmail());
		standingOrderData.setCustomerId(standingOrder.getCustomerId());
		standingOrderData.setCustomerIdentity(buildFdIdentityData(standingOrder.getCustomerIdentity()));
		standingOrderData.setCustomerListId(standingOrder.getCustomerListId());
		standingOrderData.setCustomerListName(standingOrder.getCustomerListName());
		standingOrderData.setDefault(standingOrder.isDefault());
		standingOrderData.setDeleted(standingOrder.isDeleted());
		if(standingOrder.getDeleteDate() != null)
		standingOrderData.setDeleteDate(standingOrder.getDeleteDate().getTime());
		if(standingOrder.getEndTime()!= null)
		standingOrderData.setEndTime(standingOrder.getEndTime().getTime());
		standingOrderData.setErrorDetail(standingOrder.getErrorDetail());
		standingOrderData.setErrorHeader(standingOrder.getErrorHeader());
		standingOrderData.setFrequency(standingOrder.getFrequency());
		standingOrderData.setId(standingOrder.getId());
		if(standingOrder.getLastError() != null)
		standingOrderData.setLastError(standingOrder.getLastError().getErrorHeader());
		standingOrderData.setNewSo(standingOrder.isNewSo());
		if(standingOrder.getNextDeliveryDate() !=null)
		standingOrderData.setNextDeliveryDate(standingOrder.getNextDeliveryDate().getTime());
		standingOrderData.setPaymentMethodId(standingOrder.getPaymentMethodId());
		if(standingOrder.getPreviousDeliveryDate() != null)
		standingOrderData.setPreviousDeliveryDate(standingOrder.getPreviousDeliveryDate().getTime());
		standingOrderData.setReminderOverlayForNewSo(standingOrder.isReminderOverlayForNewSo());
		if(standingOrder.getStartTime() != null)
		standingOrderData.setStartTime(standingOrder.getStartTime().getTime());
		standingOrderData.setTipAmount(standingOrder.getTipAmount());
		return standingOrderData;
		}
		return null ; 
	}

	public static FDIdentityData buildFdIdentityData(FDIdentity customerIdentity) {
		FDIdentityData identity = new FDIdentityData();
		identity.setErpCustomerPK(customerIdentity.getErpCustomerPK());
		identity.setFdCustomerPK(customerIdentity.getFDCustomerPK());
		return identity;
	}
	
	public static FDStandingOrderAltDeliveryDateData buildStandingOrderAltDlvDateData(FDStandingOrderAltDeliveryDate altDeliveryInfo) {
		if (altDeliveryInfo != null) {
			FDStandingOrderAltDeliveryDateData dlvDateData = new FDStandingOrderAltDeliveryDateData();
			dlvDateData.setActionType(altDeliveryInfo.getActionType());
			if(altDeliveryInfo.getAltDate() != null)
			dlvDateData.setAltDate(altDeliveryInfo.getAltDate().getTime());
			if(altDeliveryInfo.getAltEndTime() != null)
			dlvDateData.setAltEndTime(altDeliveryInfo.getAltEndTime().getTime());
			dlvDateData.setAltEndTimeStr(altDeliveryInfo.getAltEndTimeStr());
			if(altDeliveryInfo.getAltStartTime() != null)
			dlvDateData.setAltStartTime(altDeliveryInfo.getAltStartTime()
					.getTime());
			dlvDateData
					.setAltStartTimeStr(altDeliveryInfo.getAltStartTimeStr());
			dlvDateData.setCreatedBy(altDeliveryInfo.getCreatedBy());
			if(altDeliveryInfo.getCreatedTime()  != null)
			dlvDateData.setCreatedTime(altDeliveryInfo.getCreatedTime()
					.getTime());
			dlvDateData.setDescription(altDeliveryInfo.getDescription());
			dlvDateData.setModifiedBy(altDeliveryInfo.getModifiedBy());
			if(altDeliveryInfo.getModifiedTime()  != null)
			dlvDateData.setModifiedTime(altDeliveryInfo.getModifiedTime()
					.getTime());
			if(altDeliveryInfo.getOrigEndTime()  != null)
			dlvDateData.setOrigDate(altDeliveryInfo.getOrigDate().getTime());
			if(altDeliveryInfo.getOrigEndTime() != null)
			dlvDateData.setOrigEndTime(altDeliveryInfo.getOrigEndTime()
					.getTime());
			dlvDateData.setOrigEndTimeStr(altDeliveryInfo.getOrigEndTimeStr());
			if(altDeliveryInfo.getOrigStartTime()  != null)
			dlvDateData.setOrigStartTime(altDeliveryInfo.getOrigStartTime()
					.getTime());
			dlvDateData.setOrigStartTimeStr(altDeliveryInfo
					.getOrigStartTimeStr());
			dlvDateData.setSoId(altDeliveryInfo.getSoId());
			dlvDateData.setId(altDeliveryInfo.getId());
			return dlvDateData;
		}
		return null;
		
	}

	public static ErpActivityRecordData buildErpActivityRecord(ErpActivityRecord record) {
		return ModelConverter.buildErpActivityRecordData(record);
	}

	public static FDStandingOrderInfoList buildFdStandingOrderInfoList(FDStandingOrderInfoListData data) {
		FDStandingOrderInfoList standingOrderListInfo = new FDStandingOrderInfoList(buildStandingOrderInfoList(data.getStandingOrdersInfo()));
		return standingOrderListInfo;
	}

	private static List<FDStandingOrderInfo> buildStandingOrderInfoList(List<FDStandingOrderInfoData> standingOrdersInfo) {
		List<FDStandingOrderInfo> standingorderInfoList = new ArrayList<FDStandingOrderInfo>();
		for (FDStandingOrderInfoData standingOrderInfo : standingOrdersInfo) {
			standingorderInfoList.add(buildStandingOrderInfo(standingOrderInfo));
		}
		return standingorderInfoList;
	}

	private static FDStandingOrderInfo buildStandingOrderInfo(FDStandingOrderInfoData standingOrderInfo) {
		FDStandingOrderInfo standingOrderInfoData = new FDStandingOrderInfo();
		standingOrderInfoData.setAddress(standingOrderInfo.getAddress());
		standingOrderInfoData.setBusinessPhone(standingOrderInfo.getBusinessPhone());
		standingOrderInfoData.setCellPhone(standingOrderInfo.getCellPhone());
		standingOrderInfoData.setCompanyName(standingOrderInfo.getCompanyName());
		standingOrderInfoData.setCustomerId(standingOrderInfo.getCustomerId());
		if(standingOrderInfo.getEndTime() != null)
		standingOrderInfoData.setEndTime(new Date(standingOrderInfo.getEndTime()));
		standingOrderInfoData.setErrorHeader(standingOrderInfo.getErrorHeader());
		if(standingOrderInfo.getFailedOn() != null)
		standingOrderInfoData.setFailedOn(new Date(standingOrderInfo.getFailedOn()));
		standingOrderInfoData.setFrequency(standingOrderInfo.getFrequency());
		standingOrderInfoData.setLastError(standingOrderInfo.getLastError());
		if(standingOrderInfo.getNextDate() != null)
		standingOrderInfoData.setNextDate(new Date(standingOrderInfo.getNextDate()));
		standingOrderInfoData.setPaymentMethod(standingOrderInfo.getPaymentMethod());
		standingOrderInfoData.setSoID(standingOrderInfo.getSoID());
		standingOrderInfoData.setSoName(standingOrderInfo.getSoName());
		if(standingOrderInfo.getStartTime() != null)
		standingOrderInfoData.setStartTime(new Date(standingOrderInfo.getStartTime()));
		standingOrderInfoData.setUserId(standingOrderInfo.getUserId());
		standingOrderInfoData.setZone(standingOrderInfo.getZone());
		return standingOrderInfoData;
	}

	public static StandingOrderErrorData buildStandingOrderErrorData(String[] soIDs, String agentId) {
		StandingOrderErrorData satndingOrderError = new StandingOrderErrorData();
		satndingOrderError.setAgentId(agentId);
		satndingOrderError.setSoIDs(soIDs);
		return satndingOrderError;
	}

	public static Map<Date, Date> buildMapOfDate(Map<Long, Long> data) {
		Map<Date, Date> altDlvDateResponse = new HashMap<Date, Date>();
		for (Long key : data.keySet()) {
			altDlvDateResponse.put(new Date(key), new Date(data.get(key)));
		}
		return altDlvDateResponse;
	}

	public static List<FDStandingOrderAltDeliveryDate> buildfdstandinOrderAltDlvDateList(
			List<FDStandingOrderAltDeliveryDateData> data) {
		List<FDStandingOrderAltDeliveryDate> list = new ArrayList<FDStandingOrderAltDeliveryDate>();
		for (FDStandingOrderAltDeliveryDateData fdStandingOrderAltDeliveryDateData : data) {
			list.add(buildStandingOrderAltdeliveryDate(fdStandingOrderAltDeliveryDateData));
		}
		return list;
	}

	public static List<FDStandingOrderAltDeliveryDateData> buildfdstandinOrderAltDlvDateDataList(
			List<FDStandingOrderAltDeliveryDate> altDeliveryDates) {
		List<FDStandingOrderAltDeliveryDateData> soDlvDateList = new ArrayList<FDStandingOrderAltDeliveryDateData>();
		for (FDStandingOrderAltDeliveryDate fdStandingOrderAltDeliveryDate : altDeliveryDates) {
			soDlvDateList.add(buildStandingOrderAltDlvDateData(fdStandingOrderAltDeliveryDate));
		}
		return soDlvDateList;
	}


	public static Collection<FDStandingOrderData> buildStandingOrderDataList(Collection<FDStandingOrder> fdStandingOrders) throws FDResourceException {
		List<FDStandingOrderData> soList = new ArrayList<FDStandingOrderData>();
		for (FDStandingOrder fdStandingOrder : fdStandingOrders) {
			soList.add(buildStandingOrderData(fdStandingOrder));
		}
		return soList;
	}

	public static DeleteSOData buildDeleteSOData(FDActionInfo info,FDStandingOrder so, String deleteDate) throws FDResourceException {
		DeleteSOData deleteSoData = new DeleteSOData();
		deleteSoData.setDeleteDate(deleteDate);
		deleteSoData.setInfo(FDActionInfoConverter.buildActionInfoData(info));
		deleteSoData.setSo(buildStandingOrderData(so));
		return deleteSoData;
	}

	
	public static ResultListData buildResultListData(ResultList resultList) {
		ResultListData resultListData = new ResultListData();
		resultListData.setErrorOccured(resultList.isErrorOccured());
		resultListData.setFailedCount(resultList.getFailedCount());
		resultListData.setSkippedCount(resultList.getSkippedCount());
		resultListData.setSuccessCount(resultList.getSuccessCount());
		resultListData.setResult(buildResultListDataList(resultList));
		return resultListData;
	}

	public static List<ResultData> buildResultListDataList(ResultList res) {
		List<ResultData> resultDataList = new ArrayList<ResultData>();
		List<Result> resultList = res.getResultsList();
		for (Result result : resultList) {
			resultDataList.add(buildResultData(result));
		}
		return resultDataList;
	}
	
	public static ResultData buildResultData(Result result) {
		ResultData data = new ResultData();
		data.setCustomerId(result.getCustId());
		if(result.getErrorCode() != null)
		data.setErrorCode(result.getErrorCode().toString());
		data.setSaleId(result.getSaleId());
		data.setSoId(result.getSoId());
		data.setStatus(result.getStatus().toString());
		if(null !=result.getRequestedDate())
			data.setRequestedDate(result.getRequestedDate().getTime());
		data.setUnavailabilityDetails(buildUnavailabilityDetailsData(result.getUnavailabilityDetails()));
		return data;
		
	}


	private static List<UnavailabilityDetailsWrapper> buildUnavailabilityDetailsData(
			Map<FDCartLineI, UnAvailabilityDetails> unavailabilityDetails) {
		List<UnavailabilityDetailsWrapper> unavailabilityMap = new ArrayList<UnavailabilityDetailsWrapper>();
		for (FDCartLineI cartLine : unavailabilityDetails.keySet()) {
			UnavailabilityDetailsWrapper wrapper = new UnavailabilityDetailsWrapper();
			FDCartLineData cartLineData  = new FDCartLineData();
			cartLineData.setSkuData(buildFdskuData(cartLine.getSku()));
			cartLineData.setSalesUnit(cartLine.getSalesUnit());
			cartLineData.setDescription(cartLine.getDescription());
			cartLineData.setMaterialNumber(cartLine.getMaterialNumber());
			cartLineData.setConfigurationData(buildConfigurationData(cartLine.getConfiguration()));
			wrapper.setCartLine(cartLineData);
			wrapper.setUnavData(buildUnavailabilityDeatils(unavailabilityDetails.get(cartLine)));
			unavailabilityMap.add(wrapper);
		}
		return unavailabilityMap;
	}

	private static FDConfigurationData buildConfigurationData(FDConfigurableI configuration) {
		FDConfigurationData configurationData = new FDConfigurationData();
		configurationData.setQuantity(configuration.getQuantity());
		configurationData.setSalesUnit(configuration.getSalesUnit());
		return configurationData;
	}

	public static UnAvailabilityDetailsData buildUnavailabilityDeatils(UnAvailabilityDetails unAvailabilityDetails) {
		UnAvailabilityDetailsData data = new UnAvailabilityDetailsData();
		data.setAltSkucode(unAvailabilityDetails.getAltSkucode());
		data.setReason(unAvailabilityDetails.getReason().toString());
		data.setUnavailQty(unAvailabilityDetails.getUnavailQty());
		return data;
	}
	
	public static ResultList buildResultList(ResultListData data) {
		ResultList resultList = new ResultList();
		resultList.setErrorOccured(data.isErrorOccured());
		resultList.setFailedCount(data.getFailedCount());
		resultList.setSkippedCount(data.getSkippedCount());
		resultList.setSuccessCount(data.getSuccessCount());
		resultList.setResults(buildResultList(data.getResult()));
		return resultList;
	}
	
	public static List<Result> buildResultList(List<ResultData> list) {
		List<Result> resultList = new ArrayList<SOResult.Result>();
		for (ResultData resultData : list) {
			resultList.add(buildResult(resultData));
		}
		return resultList;
		
	}
	
	public static  Result buildResult(ResultData resultData) {
		Result result = new Result();
		result.setStatus(Status.valueOf(resultData.getStatus()));
		result.setSoId(resultData.getSoId());
		result.setCustomerId(resultData.getCustomerId());
		result.setSaleId(resultData.getSaleId());
		result.setErrorCode(ErrorCode.valueOf(resultData.getErrorCode()));
		result.setRequestedDate(new Date(resultData.getRequestedDate()));
		result.setUnavailabilityDetails(buildUnavailabilityDetails(resultData.getUnavailabilityDetails()));
		return result;
	}
	
	private static Map<FDCartLineI, UnAvailabilityDetails> buildUnavailabilityDetails(
			List<UnavailabilityDetailsWrapper> unavailabilityDetails) {
		Map<FDCartLineI, UnAvailabilityDetails> unavailabilityMap = new HashMap<FDCartLineI, UnAvailabilityDetails>();
		for (UnavailabilityDetailsWrapper wrapper : unavailabilityDetails) {
			FDCartLineI cartline = (FDCartLineI) new FDCartModel();
			cartline.setSku(buildFdsku(wrapper.getCartLine().getSkuData()));
			cartline.setSalesUnit(wrapper.getCartLine().getSalesUnit());
			cartline.setDescription(wrapper.getCartLine().getDescription());
			cartline.setConfiguration(buildConfiguration(wrapper.getCartLine().getConfigurationData()));
			ErpOrderLineModel model = new ErpOrderLineModel();
			model.setMaterialNumber(wrapper.getCartLine().getMaterialNumber());
			cartline = (FDCartLineI) model;
			unavailabilityMap.put(cartline, buildUnavailabilityDeatils(wrapper.getUnavData()));
		}
		return unavailabilityMap;
	}
	private static FDSku buildFdsku(FDSkuData skuData) {
		return new FDSku(skuData.getSkuCode(), skuData.getVersion());
	}

	public static UnAvailabilityDetails buildUnavailabilityDeatils(UnAvailabilityDetailsData unAvailabilityDetailsData) {
		return new UnAvailabilityDetails(unAvailabilityDetailsData.getUnavailQty(), UnavailabilityReason.valueOf(unAvailabilityDetailsData.getReason()), unAvailabilityDetailsData.getAltSkucode());
	}

	public static List<ResultData> buildResultDataList(List<Result> resultsList) {
		List<ResultData> resultList = new ArrayList<ResultData>();
		for (Result result : resultsList) {
			resultList.add(buildResultData(result));
		}
		return resultList;
		
	}
	
	private static  FDSkuData buildFdskuData(FDSku sku) {
		FDSkuData skuData = new FDSkuData();
		skuData.setSkuCode(sku.getSkuCode());
		skuData.setVersion(sku.getVersion());
		return skuData;
	}
	private static FDConfiguration buildConfiguration(FDConfigurationData configurationData) {
		FDConfiguration configuration = new FDConfiguration(configurationData.getQuantity(), configurationData.getSalesUnit());
		return configuration;
	}


	private static void populateSODetails(FDStandingOrder so,FDCustomerList fdStandingOrderList) throws FDResourceException{
		
		List<FDProductSelectionI> productSelectionList = null;
		Connection conn = null;
		if (null != fdStandingOrderList) {			
			productSelectionList = OrderLineUtil.getValidProductSelectionsFromCCLItems(fdStandingOrderList.getLineItems());
			for (FDProductSelectionI fdSelection : productSelectionList) {
				FDCartLineI cartLine = new FDCartLineModel(fdSelection);
				if (!cartLine.isInvalidConfig()) {
					// cartLine.refreshConfiguration();
					if(fdSelection.getCustomerListLineId()!=null)
					cartLine.setCustomerListLineId(fdSelection.getCustomerListLineId());
					so.getStandingOrderCart().addOrderLine(cartLine);
				}
			}

			if (null != productSelectionList && !productSelectionList.isEmpty()) {
				try {
					so.getStandingOrderCart().refreshAll(true);
				} catch (FDInvalidConfigurationException e) {
//					LOGGER.warn("Exception in populateSODetails() ",e);
				}
			}
			
		}
		
	}
	
	public static StandingOrdersJobConfigData buildStandingOrdersJobConfigData(StandingOrdersJobConfig jobConfig){
		StandingOrdersJobConfigData jobConfigData = new StandingOrdersJobConfigData();
		if(null !=jobConfig){
			jobConfigData.setSendReportEmail(jobConfig.isSendReportEmail());
			jobConfigData.setSendReminderNotificationEmail(jobConfig.isSendReminderNotificationEmail());
			jobConfigData.setCreateIfSoiExistsForWeek(jobConfig.isCreateIfSoiExistsForWeek());
			jobConfigData.setForceCapacity(jobConfig.isForceCapacity());
		}		
		return jobConfigData;		
	}
}
