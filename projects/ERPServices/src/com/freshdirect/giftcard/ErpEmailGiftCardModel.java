package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;

public class ErpEmailGiftCardModel extends ErpGiftCardTransModel {

	private static final long	serialVersionUID	= -3843168766775401424L;
	
	private List<ErpGCDlvInformationHolder>	recipientsTransactionList;

	public List<ErpGCDlvInformationHolder> getRecipientsTransactionList() {
		return recipientsTransactionList;
	}

	public void setRecipientsTransactionList( List<ErpGCDlvInformationHolder> recepientsTranactionList ) {
		this.recipientsTransactionList = recepientsTranactionList;
	}

	public ErpEmailGiftCardModel() {
		super( EnumTransactionType.EMAIL_GIFTCARD );
	}

	public void addGiftCardDlvInfo( ErpGCDlvInformationHolder holder ) {
		if ( this.recipientsTransactionList == null ) {
			this.recipientsTransactionList = new ArrayList<ErpGCDlvInformationHolder>();
		}
		this.recipientsTransactionList.add( holder );
	}
}
