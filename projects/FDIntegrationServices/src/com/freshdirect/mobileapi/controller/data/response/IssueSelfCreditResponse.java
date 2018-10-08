package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class IssueSelfCreditResponse extends Message {

	private String message;
	private boolean success;

    public IssueSelfCreditResponse(com.freshdirect.backoffice.selfcredit.data.IssueSelfCreditResponse issueSelfCreditResponse) {
        this.message = issueSelfCreditResponse.getMessage();
        this.success = issueSelfCreditResponse.isSuccess();
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

}
