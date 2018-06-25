package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;
import com.freshdirect.mobileapi.controller.data.Message;

public class ExternalAccountLoginResponse extends Message {
	private boolean loggedInSuccess;
	private List<String> dpskulist;
	private boolean fdxdpenabled;
	public boolean isLoggedInSuccess() {
		return loggedInSuccess;
	}
	public void setLoggedInSuccess(boolean loggedInSuccess) {
		this.loggedInSuccess = loggedInSuccess;
	}
	
	private boolean isPurchaseDlvPassEligible;

    public boolean isPurchaseDlvPassEligible() {
		return isPurchaseDlvPassEligible;
	}

	public void setPurchaseDlvPassEligible(boolean isPurchaseDlvPassEligible) {
		this.isPurchaseDlvPassEligible = isPurchaseDlvPassEligible;
	}
	
	public List<String> getDpskulist() {
		return dpskulist;
	}

	public void setDpskulist(List<String> dpskulist) {
		this.dpskulist = dpskulist;
	}
	public boolean isFdxdpenabled() {
		return fdxdpenabled;
	}
	public void setFdxdpenabled(boolean fdxdpenabled) {
		this.fdxdpenabled = fdxdpenabled;
	}
	
}
