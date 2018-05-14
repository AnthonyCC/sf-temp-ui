package com.freshdirect.ecomm.converter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseActionType;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmCasePriority;
import com.freshdirect.crm.CrmCaseQueue;
import com.freshdirect.crm.CrmCaseState;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.ecommerce.data.crm.AuthorisationData;
import com.freshdirect.ecommerce.data.crm.CannedTextData;
import com.freshdirect.ecommerce.data.crm.CrmAgentInfoData;
import com.freshdirect.ecommerce.data.crm.CrmAgentListData;
import com.freshdirect.ecommerce.data.crm.CrmAuthInfoData;
import com.freshdirect.ecommerce.data.crm.CrmAuthSearchCriteriaData;
import com.freshdirect.ecommerce.data.crm.CrmCaseActionData;
import com.freshdirect.ecommerce.data.crm.CrmCaseData;
import com.freshdirect.ecommerce.data.crm.CrmCaseInfoData;
import com.freshdirect.ecommerce.data.crm.CrmCaseOperationData;
import com.freshdirect.ecommerce.data.crm.CrmCaseTemplateData;
import com.freshdirect.ecommerce.data.crm.CrmCustomerHeaderInfoData;
import com.freshdirect.ecommerce.data.crm.CrmDeliveryPassData;
import com.freshdirect.ecommerce.data.crm.CrmLateIssueData;
import com.freshdirect.ecommerce.data.crm.CrmQueueInfoData;
import com.freshdirect.ecommerce.data.crm.CrmStatusData;
import com.freshdirect.ecommerce.data.crm.CrmSystemCaseInfoData;
import com.freshdirect.ecommerce.data.crm.DownloadCaseData;
import com.freshdirect.ecommerce.data.crm.ErpTruckInfoData;
import com.freshdirect.ecommerce.data.crm.IncrCountData;
import com.freshdirect.ecommerce.data.crm.LoginAgentData;
import com.freshdirect.ecommerce.data.crm.UpdateCaseData;
import com.freshdirect.ecommerce.data.crm.ViewAccountData;
import com.freshdirect.ecommerce.data.customer.CrmAgentRoleData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.ecommerce.data.enums.CrmCaseSubjectData;
import com.freshdirect.ecommerce.data.giftcard.ErpGCDlvInformationHolderData;
import com.freshdirect.ecommerce.data.sap.ErpRecipentData;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.payment.service.ModelConverter;

public class GiftCardModelDataConverter {

	public static List getErpGCDlvInformationHolder(
			List<ErpGCDlvInformationHolderData> data) {
		List<ErpGCDlvInformationHolder> model = new ArrayList();
		
		for(ErpGCDlvInformationHolderData dataItem : data){
			model.add(buildErpGCDlvInformationHolder(dataItem));
		}
		
		return model;
	}
	
	

	public static ErpGCDlvInformationHolder buildErpGCDlvInformationHolder(
			ErpGCDlvInformationHolderData dataItem) {

		ErpGCDlvInformationHolder holder = new ErpGCDlvInformationHolder();
		if(null != dataItem.getId())
			holder.setId(dataItem.getId());
		if(null!=dataItem.getRecepientModel())
			holder.setRecepientModel(buildErpRecipentModel(dataItem.getRecepientModel()));
		
		if(null != dataItem.getGiftCardId())
			holder.setGiftCardId(dataItem.getGiftCardId());
		if(null != dataItem.getGivexNum())
			holder.setGivexNum(dataItem.getGivexNum());
		holder.setPurchaseDate(new Date(dataItem.getPurchaseDate()));
		return holder;
	}



	public static ErpRecipentModel buildErpRecipentModel(
			ErpRecipentData recepientModel) {
		ErpRecipentModel recModel = new ErpRecipentModel();
		recModel.setId(recepientModel.getRecipeId());
		recModel.setCustomerId(recepientModel.getCustomerId());
		recModel.setSenderName(recepientModel.getSenderName());
		recModel.setSenderEmail(recepientModel.getSenderEmail());
		recModel.setRecipientName(recepientModel.getRecipientName());
		recModel.setRecipientEmail(recepientModel.getRecipientEmail());
		recModel.setSale_id(recepientModel.getSale_id());
		recModel.setTemplateId(recepientModel.getTemplateId());
		recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(recepientModel.getDeliveryMode()));
		recModel.setPersonalMessage(recepientModel.getPersonalMessage());
		recModel.setAmount(recepientModel.getAmount());
		recModel.setOrderLineId(recepientModel.getOrderLineId());
		recModel.setDonorOrganizationName(recepientModel.getDonorOrganizationName());
		recModel.setGiftCardType(EnumGiftCardType.getEnum(recepientModel.getGiftCardType()));
		return recModel;
	}



}
