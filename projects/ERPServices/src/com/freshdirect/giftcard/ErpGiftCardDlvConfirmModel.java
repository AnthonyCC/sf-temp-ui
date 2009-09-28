package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;

public class ErpGiftCardDlvConfirmModel extends ErpGiftCardTransModel {
	
	private List dlvInfoTranactionList; 
												
	public List getDlvInfoTranactionList() {
		return dlvInfoTranactionList;
	}

	public void setDlvInfoTranactionList(List dlvInfoTranactionList) {
		this.dlvInfoTranactionList = dlvInfoTranactionList;
	}

	public ErpGiftCardDlvConfirmModel(EnumTransactionType type)
	{
		super(type);
	}
		
	
	public void addGiftCardDlvInfo(ErpGCDlvInformationHolder holder){
		if(this.dlvInfoTranactionList==null){		
			this.dlvInfoTranactionList=new ArrayList();
		}
		this.dlvInfoTranactionList.add(holder);
	}
	
}
