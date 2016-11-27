package com.freshdirect.dataloader.payment.bin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.payment.BINInfo;

public class BINContext implements java.io.Serializable {
	
	private EnumCardType cardType;
	
	private List<Exception> exceptions=new ArrayList<Exception>();
	
	private List<BINInfo> binInfos;

	public EnumCardType getCardType() {
		return cardType;
	}

	public void setCardType(EnumCardType cardType) {
		this.cardType = cardType;
		binInfos=null;
		exceptions.clear();
	}

	public List<Exception> getExceptions() {
		return Collections.unmodifiableList(exceptions);
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}
	
	public void addException(Exception e) {
		exceptions.add(e);
	}
	
	public List<BINInfo> getBinInfos() {
		return Collections.unmodifiableList(binInfos);
	}

	public void setBinInfos(List<BINInfo> binInfos) {
		this.binInfos = binInfos;
	}
	
	
	

}
