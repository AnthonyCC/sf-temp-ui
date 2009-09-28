package com.freshdirect.fdstore.customer;

import java.util.Random;

import com.freshdirect.giftcard.RecipientModel;

public class SavedRecipientModel extends RecipientModel {
	
	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	private String fdUserId;

	public SavedRecipientModel() {
		super();
	}

}
