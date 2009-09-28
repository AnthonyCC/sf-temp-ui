package com.freshdirect.webapp.taglib.giftcard;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.giftcard.FDGiftCardInfoList;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetGiftCardRecipientDlvInfoTag extends AbstractGetterTag{
	private String saleId;
	private String certificationNum;
	
	protected Object getResult() throws FDResourceException {
		List dlvPassActivities = null;
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		ErpGCDlvInformationHolder holder = FDCustomerManager.getRecipientDlvInfo(user.getIdentity(), 
														this.saleId, this.certificationNum);
		System.out.println("holder ^^^^^^^^^^^^^^^^"+holder.getCertificationNumber());
		return holder;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "com.freshdirect.giftcard.ErpGCDlvInformationHolder";
		}
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getCertificationNum() {
		return certificationNum;
	}

	public void setCertificationNum(String certificationNum) {
		this.certificationNum = certificationNum;
	}
}
