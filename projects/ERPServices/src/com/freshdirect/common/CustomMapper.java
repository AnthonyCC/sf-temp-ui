package com.freshdirect.common;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.ecommerce.data.dlvpass.DlvPassUsageLineData;
import com.freshdirect.ecommerce.data.order.DlvSaleInfoData;
import com.freshdirect.ecommerce.data.order.ErpSaleInfoData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class CustomMapper {

	
	public static DlvSaleInfo map(DlvSaleInfoData data){
		
		return new DlvSaleInfo(data.getSaleId(), data.getErpCustomerId(), EnumSaleStatus.getSaleStatus(data.getStatus()), 
				data.getStopSequence(), data.getFirstName(), data.getLastName(), data.isEBT());
		
	}

	public static List<ErpSaleInfo> map(List<ErpSaleInfoData> data) {
		if(data!=null){
			List<ErpSaleInfo> orderList = new ArrayList<ErpSaleInfo>();
			for(ErpSaleInfoData elem : data){
				orderList.add(map(elem));
			}
			return orderList;
		}
		
		return null;
	}
	
	public static ErpSaleInfo map(ErpSaleInfoData elem) {
		if(elem!=null){
			ErpSaleInfo orderInfo =	new ErpSaleInfo(elem.getSaleId(), elem.getErpCustomerId(), EnumSaleStatus.getSaleStatus(elem.getStatus()), elem.getAmount(), elem.getSubTotal()
						, elem.getRequestedDate(), elem.getCreateRequestedDate(), 
						EnumTransactionSource.getTransactionSource(elem.getSource()), elem.getCreateDate(), elem.getCreateBy(), 
						EnumTransactionSource.getTransactionSource(elem.getModSource()), elem.getModDate(), 
						elem.getModBy(), elem.getDeliveryStart(), elem.getDeliveryEnd(), elem.getCutoffTime(), 
						EnumDeliveryType.getDeliveryType(elem.getDeliveryType()), elem.getPendingCreditAmount(), elem.getAppliedCreditAmount(), 
						elem.getZone(), EnumPaymentMethodType.getEnum(elem.getPaymentMethodType()), 
						elem.getDlvPassId(), EnumSaleType.getSaleType(elem.getSaleType()), 
						elem.getTruckNumber(), elem.getStopSequence(), elem.isMakeGood(), elem.getStandingOrderId(), elem.isSoHolidayMovement(), 
						EnumEStoreId.valueOfContentId(elem.geteStore()), elem.getPlantId(), elem.getSalesOrg(), elem.getDistributionChanel(), 
						EnumEwalletType.getEnum(elem.getEwalletType()));
						return orderInfo;
		}
		
		return null;
	}

	public static List<DlvPassUsageLine> mapDeliverypass(
			List<DlvPassUsageLineData> data) {
		if(data!=null){
			List<DlvPassUsageLine> dlvpassList = new ArrayList<DlvPassUsageLine>();
			for(DlvPassUsageLineData elem : data){
				dlvpassList.add(new DlvPassUsageLine(elem.getDlvPassIdUsed(), elem.getOrderIdUsedFor(), elem.getDeliveryDate(), 
						EnumSaleStatus.getSaleStatus(elem.getOrderStatusUsedFor())));
			}
			return dlvpassList;
	}
		return null;
	}

	public static List<DlvSaleInfo> mapDeliveryOrders(
			List<DlvSaleInfoData> parseResponse) {
		// TODO Auto-generated method stub
		return null;
	}
}
