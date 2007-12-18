/*
 * Created on Aug 12, 2005
 */
package com.freshdirect.webapp.taglib.promotion;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.promotion.management.FDPromoCustomerInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import java.util.List;
import java.util.ArrayList;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author jng
 */
public class GetPromoCustomerInfoListTag extends AbstractGetterTag {

	private String promotionId;
	private String customerId;

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();		
		String reload =  pageContext.getRequest().getParameter("reload");		
		boolean isLoadFromCustomerId = (this.customerId != null && !"".equals(this.customerId));		
		FDPromoCustomerInfo firstPromoCustomerInfo = null;
		List promoCustomerInfoList = (List) session.getAttribute(SessionName.EDIT_PROMO_CUSTOMER_INFO_LIST);
		if (promoCustomerInfoList != null && promoCustomerInfoList.size() > 0) {
			firstPromoCustomerInfo = (FDPromoCustomerInfo)promoCustomerInfoList.get(0); 
		}
		if ("true".equalsIgnoreCase(reload) || firstPromoCustomerInfo == null 
				|| (isLoadFromCustomerId && !firstPromoCustomerInfo.getCustomerId().equals(this.customerId))
				|| (!isLoadFromCustomerId && !firstPromoCustomerInfo.getPromotionId().equals(this.promotionId))
				|| isLoadFromCustomerId != firstPromoCustomerInfo.getIsLoadedFromCustomerId()) {			
			try {
				if (isLoadFromCustomerId) {				
					promoCustomerInfoList = FDPromotionManager.getPromoCustomerInfoListFromCustomerId(this.customerId);
				} else if (this.promotionId != null && !"".equals(this.promotionId)) {
					promoCustomerInfoList = FDPromotionManager.getPromoCustomerInfoListFromPromotionId(this.promotionId);
				} else {
					promoCustomerInfoList = new ArrayList();					
				}
				session.setAttribute(SessionName.EDIT_PROMO_CUSTOMER_INFO_LIST, promoCustomerInfoList);
			}catch(FDResourceException e){
				throw new JspException(e);
			}
		}
		return promoCustomerInfoList;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}

}
