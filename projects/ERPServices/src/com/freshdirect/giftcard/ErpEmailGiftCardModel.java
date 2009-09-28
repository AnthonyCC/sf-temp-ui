package com.freshdirect.giftcard;



import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;

public class ErpEmailGiftCardModel extends ErpGiftCardTransModel {
		

	private List recepientsTranactionList;
	
	public List getRecepientsTranactionList() {
		return recepientsTranactionList;
	}

	public void setRecepientsTranactionList(List recepientsTranactionList) {
		this.recepientsTranactionList = recepientsTranactionList;
	}

	public ErpEmailGiftCardModel()
	{
		super(EnumTransactionType.EMAIL_GIFTCARD);
	}
			
	
	public void addGiftCardDlvInfo(ErpGCDlvInformationHolder holder){
		if(this.recepientsTranactionList==null){		
			this.recepientsTranactionList=new ArrayList();
		}
		this.recepientsTranactionList.add(holder);
	}
}
