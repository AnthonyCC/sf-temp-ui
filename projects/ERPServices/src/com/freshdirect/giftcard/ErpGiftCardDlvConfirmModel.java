package com.freshdirect.giftcard;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;

public class ErpGiftCardDlvConfirmModel extends ErpGiftCardTransModel {

	private static final long	serialVersionUID	= 2080848141716611951L;
	
	private List<ErpGCDlvInformationHolder>	dlvInfoTranactionList;

	public List<ErpGCDlvInformationHolder> getDlvInfoTranactionList() {
		return dlvInfoTranactionList;
	}

	public void setDlvInfoTranactionList( List<ErpGCDlvInformationHolder> dlvInfoTranactionList ) {
		this.dlvInfoTranactionList = dlvInfoTranactionList;
	}

	public ErpGiftCardDlvConfirmModel( EnumTransactionType type ) {
		super( type );
	}

	public void addGiftCardDlvInfo( ErpGCDlvInformationHolder holder ) {
		if ( this.dlvInfoTranactionList == null ) {
			this.dlvInfoTranactionList = new ArrayList<ErpGCDlvInformationHolder>();
		}
		this.dlvInfoTranactionList.add( holder );
	}

}
