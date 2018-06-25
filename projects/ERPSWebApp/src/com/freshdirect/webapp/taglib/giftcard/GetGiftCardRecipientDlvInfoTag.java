package com.freshdirect.webapp.taglib.giftcard;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class GetGiftCardRecipientDlvInfoTag extends AbstractGetterTag<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -115385334851936525L;
	private String saleId;
	private String certificationNum;
	
	protected Object getResult() throws FDResourceException {
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
