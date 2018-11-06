package com.freshdirect.ecomm.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.ecommerce.data.giftcard.ErpGCDlvInformationHolderData;
import com.freshdirect.ecommerce.data.sap.ErpRecipentData;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpRecipentModel;

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
