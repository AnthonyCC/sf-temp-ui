/*
 * Created on Aug 12, 2005
 */
package com.freshdirect.webapp.taglib.promotion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.promotion.management.FDPromoCustomerInfo;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * @author jng
 */
public class PromoCustomerControllerTag extends AbstractControllerTag {

	private static SimpleDateFormat SDF = new SimpleDateFormat("MMM-dd-yyyy");

	private List promoCustomerInfoList;

	public void setPromoCustomerInfoList(List promoCustomersInfoList) {
		this.promoCustomerInfoList = promoCustomersInfoList;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			if ("edit_promo_custs".equalsIgnoreCase(this.getActionName())) {
				if (this.promoCustomerInfoList == null) {
					getPromoCustomerInfoListInSession();
				}
				this.populatePromoCustomerInfoList(request, actionResult);
				if (!actionResult.isSuccess()) {
					savePromoCustomerInfoListInSession();
					return true;
				}
				removePromoCustomerInfoListFromSession();
				FDPromotionManager.updatePromoCustomers(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.promoCustomerInfoList);					
			} else if ("add_promo_custs".equalsIgnoreCase(this.getActionName())) {
				this.promoCustomerInfoList = new ArrayList();
				this.populatePromoCustomerInfoList(request, actionResult);
				if (!actionResult.isSuccess()) {
					savePromoCustomerInfoListInSession();
					return true;
				}
				removePromoCustomerInfoListFromSession();
				FDPromotionManager.insertPromoCustomers(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.promoCustomerInfoList);										
			}else if ("delete_promo_custs".equalsIgnoreCase(this.getActionName())) {
				this.promoCustomerInfoList = new ArrayList();
				this.populatePromoCustomerInfoList(request, actionResult);
				if (!actionResult.isSuccess()) {
					savePromoCustomerInfoListInSession();
					return true;
				}
				removePromoCustomerInfoListFromSession();
				FDPromotionManager.removePromoCustomers(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.promoCustomerInfoList);										
			}
			return true;
		}catch (FDResourceException e) {
			throw new JspException(e);
		}
		
	}
	
	private void populatePromoCustomerInfoList(HttpServletRequest request, ActionResult actionResult) {
				
		boolean done = false;
		for(int i = 0; !done; i++) {
			if ("add_promo_custs".equalsIgnoreCase(this.getActionName()) || "edit_promo_custs".equalsIgnoreCase(this.getActionName())) {				
				String promotionId = NVL.apply(request.getParameter("promotion_id_"+i), "");
				String customerId = NVL.apply(request.getParameter("customer_id_"+i), "");			
				if (!"".equals(promotionId) && !"".equals(customerId)) {
					FDPromoCustomerInfo promoCustomerInfo =  null;
					if ("add_promo_custs".equalsIgnoreCase(this.getActionName())) {
						promoCustomerInfo = new FDPromoCustomerInfo(); 
						promoCustomerInfo.setPromotionId(promotionId);
						promoCustomerInfo.setCustomerId(customerId);
						try {
							String promoUsageCnt = request.getParameter("promo_usage_cnt_"+i);
							if (promoUsageCnt != null) {
								promoCustomerInfo.setPromoUsageCount(Integer.parseInt(promoUsageCnt));
							}
						} catch (NumberFormatException nfe) { }
						try {
							String promoExpirationDate = request.getParameter("promo_expiration_date_"+i);
							if (promoExpirationDate != null) {
								promoCustomerInfo.setPromoExpirationDate(SDF.parse(promoExpirationDate));
							}
						} catch (ParseException pe) { }
						this.promoCustomerInfoList.add(promoCustomerInfo);
					} else if ("edit_promo_custs".equalsIgnoreCase(this.getActionName())) {
						promoCustomerInfo = findPromoCustomerInfo(promotionId, customerId);					
					}
					
					if (promoCustomerInfo != null) {
						promoCustomerInfo.setUsageCountStr(NVL.apply(request.getParameter("usage_cnt_"+i), ""));
						promoCustomerInfo.setExpirationMonth(NVL.apply(request.getParameter("expiration_month_"+i), ""));
						promoCustomerInfo.setExpirationDay(NVL.apply(request.getParameter("expiration_day_"+i), ""));
						promoCustomerInfo.setExpirationYear(NVL.apply(request.getParameter("expiration_year_"+i), ""));					
						validatePromoCustomerInfo(promoCustomerInfo, i, actionResult);
						if (actionResult.isSuccess()) {
							try {
								// convert to native types
								if (!"".equals(promoCustomerInfo.getUsageCountStr())) { 
									promoCustomerInfo.setUsageCount(Integer.parseInt(promoCustomerInfo.getUsageCountStr()));
								}
							} catch (NumberFormatException nfe) { }
							try {
								if (isCompleteDate(promoCustomerInfo.getExpirationDay(), promoCustomerInfo.getExpirationMonth(), promoCustomerInfo.getExpirationYear())) {
									Date expirationDate = SDF.parse(promoCustomerInfo.getExpirationMonth() + "-" + promoCustomerInfo.getExpirationDay() + "-" + promoCustomerInfo.getExpirationYear());
									promoCustomerInfo.setExpirationDate(expirationDate);
								} else {
									promoCustomerInfo.setExpirationDate(null);								
								}
							} catch (ParseException pe) {}					
						}
					}
				} else {
					done = true;				
				}
			} else if ("delete_promo_custs".equalsIgnoreCase(this.getActionName())){
				String delPromotionId = NVL.apply(request.getParameter("delete_promotion_id_"+i), "");
				String delCustomerId = NVL.apply(request.getParameter("delete_customer_id_"+i), "");
				if (!"".equals(delPromotionId) && !"".equals(delCustomerId)) {
					String deleteFlag = NVL.apply(request.getParameter("delete_flg_"+i), "");
					if ("Y".equals(deleteFlag)) {
						FDPromoCustomerInfo promoCustomerInfo = new FDPromoCustomerInfo(); 
						promoCustomerInfo.setPromotionId(delPromotionId);
						promoCustomerInfo.setCustomerId(delCustomerId);						
						this.promoCustomerInfoList.add(promoCustomerInfo);
					}
				} else {
					done = true;
				}
			}
		}		
	}

	private void validatePromoCustomerInfo(FDPromoCustomerInfo promoCustomerInfo, int index, ActionResult actionResult) {
		
		String suffix = "";
		if ("edit_promo_custs".equalsIgnoreCase(this.getActionName())) {
			suffix = "_edit";
		} else if ("add_promo_custs".equalsIgnoreCase(this.getActionName())) {
			suffix = "_add";			
		}
		
		if (!"".equals(promoCustomerInfo.getUsageCountStr())) {
			if (!isInteger(promoCustomerInfo.getUsageCountStr())) {
				actionResult.addError(true, "usage_cnt_"+index+suffix, "must be a number");			
			} else {
				try {
					int usageCnt = Integer.parseInt(promoCustomerInfo.getUsageCountStr());
					actionResult.addError(usageCnt > promoCustomerInfo.getPromoUsageCount(), "usage_cnt_"+index+suffix, "must be equal to or less than " + promoCustomerInfo.getPromoUsageCount());			
				} catch (NumberFormatException e) {}
			}
		}
		if (hasDateField(promoCustomerInfo.getExpirationDay(), promoCustomerInfo.getExpirationMonth(), promoCustomerInfo.getExpirationYear())) {
			if (!isCompleteDate(promoCustomerInfo.getExpirationDay(), promoCustomerInfo.getExpirationMonth(), promoCustomerInfo.getExpirationYear())) {
				actionResult.addError(true, "expiration_year_"+index+suffix, "incomplete date");				
			} else if (promoCustomerInfo.getPromoExpirationDate() != null) {
				try {
					Date expirationDate = SDF.parse(promoCustomerInfo.getExpirationMonth() + "-" + promoCustomerInfo.getExpirationDay() + "-" + promoCustomerInfo.getExpirationYear());
					actionResult.addError(promoCustomerInfo.getPromoExpirationDate() != null && expirationDate.after(promoCustomerInfo.getPromoExpirationDate()), "expiration_year_"+index+suffix, "no later than " + SDF.format(promoCustomerInfo.getPromoExpirationDate()));			
				} catch (ParseException e) {}				
			}
		}
	}

	private FDPromoCustomerInfo findPromoCustomerInfo(String promotionId, String customerId) {
		
		if (this.promoCustomerInfoList != null) {
			for (Iterator i = this.promoCustomerInfoList.iterator(); i.hasNext();) {
				FDPromoCustomerInfo promoCustomerInfo = (FDPromoCustomerInfo) i.next();
				if (promoCustomerInfo.getPromotionId().equals(promotionId) && promoCustomerInfo.getCustomerId().equals(customerId)) {
					return promoCustomerInfo;
				}
			}			
		}
		return null;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}


	private void savePromoCustomerInfoListInSession() {
		HttpSession session = pageContext.getSession();
		session.setAttribute(SessionName.EDIT_PROMO_CUSTOMER_INFO_LIST, promoCustomerInfoList);
	}

	private void getPromoCustomerInfoListInSession() {
		HttpSession session = pageContext.getSession();
		setPromoCustomerInfoList((List)session.getAttribute(SessionName.EDIT_PROMO_CUSTOMER_INFO_LIST));
	}

	private void removePromoCustomerInfoListFromSession() {
		HttpSession session = pageContext.getSession();
		session.removeAttribute(SessionName.EDIT_PROMO_CUSTOMER_INFO_LIST);
	}

	private boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	private boolean hasDateField (String day, String month, String year) {		
		return (!"".equals(day) || !"".equals(month) || !"".equals(year));
	}
	
	private boolean isCompleteDate (String day, String month, String year) {		
		return (!"".equals(day) && !"".equals(month) && !"".equals(year));
	}

}
