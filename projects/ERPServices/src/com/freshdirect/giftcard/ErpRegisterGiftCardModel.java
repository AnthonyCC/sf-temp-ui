package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;

public class ErpRegisterGiftCardModel extends ErpGiftCardTransModel {
		
	public List getRegisterTranactionList() {
		return registerTranactionList;
	}

	public void setRegisterTranactionList(List registerTranactionList) {
		this.registerTranactionList = registerTranactionList;
	}

	private List registerTranactionList;
	
	public ErpRegisterGiftCardModel()
	{
		super(EnumTransactionType.REGISTER_GIFTCARD);
	}
	
	public void addRegisterTransaction(ErpGiftCardTransactionModel model){
		if(registerTranactionList==null) registerTranactionList=new ArrayList();
		registerTranactionList.add(model);
	}
				
}
