package com.freshdirect.referral.extole;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.referral.extole.model.FDRafTransModel;

public class RafUtil implements Serializable {

	public static FDRafTransModel getPurchaseTransModel(){
		Date rafTransDate = new Date();
		FDRafTransModel rafTransModel=new FDRafTransModel();
		rafTransModel.setTransStatus(EnumRafTransactionStatus.PENDING);
		rafTransModel.setTransType(EnumRafTransactionType.purchase);
		rafTransModel.setCreateTime(rafTransDate);
		rafTransModel.setTransTime(rafTransDate);
		return rafTransModel;
	}
	
	public static FDRafTransModel getApproveTransModel(){
		Date rafTransDate = new Date();
		FDRafTransModel rafTransModel=new FDRafTransModel();
		rafTransModel.setTransStatus(EnumRafTransactionStatus.PENDING);
		rafTransModel.setTransType(EnumRafTransactionType.approve);
		rafTransModel.setCreateTime(rafTransDate);
		rafTransModel.setTransTime(rafTransDate);
		return rafTransModel;
	}
}
