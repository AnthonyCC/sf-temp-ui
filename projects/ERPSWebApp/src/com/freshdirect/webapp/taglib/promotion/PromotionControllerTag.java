package com.freshdirect.webapp.taglib.promotion;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.ejb.FinderException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction;
import com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam;
import com.freshdirect.fdstore.promotion.management.FDPromotionManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class PromotionControllerTag extends AbstractControllerTag {
	private FDPromotionModel promotion;
	private TreeMap zipRestrictionMap;
	private final static Category LOGGER = LoggerFactory.getInstance(PromotionControllerTag.class);
		
	public void setPromotion(FDPromotionModel promotion) {
		this.promotion = promotion;
	}

	public void setZipRestrictionMap(TreeMap zipRestrictionMap) {
		this.zipRestrictionMap = zipRestrictionMap;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try{
			if ("new_promotion".equalsIgnoreCase(this.getActionName()) ||
					"edit_promotion".equalsIgnoreCase(this.getActionName())) {
				this.populatePromotionModel(request);
				this.validatePromotion(actionResult);
				if (!actionResult.isSuccess()) {
					return true;
				}
				savePromotionInSession();
			}
			if ("create_promotion".equalsIgnoreCase(this.getActionName())) {
				getPromotionInSession();
				//this.promotion.setAssignedCustomerUserIds(this.promotion.getTmpAssignedCustomerUserIds());
				FDPromotionManager.createPromotion(this.promotion);
				removePromotionFromSession();
			}
			if ("update_promotion".equalsIgnoreCase(this.getActionName())) {
				getPromotionInSession();
				//this.promotion.setAssignedCustomerUserIds(this.promotion.getTmpAssignedCustomerUserIds());
				String promoId = request.getParameter("promoId");
				FDPromotionModel oldPromotion = FDPromotionManager.loadPromotion(promoId);
				FDPromotionModel newPromotion = this.promotion;
				FDPromotionManager.storePromotion(this.promotion);
				sendEmail(oldPromotion, newPromotion);
				removePromotionFromSession();
			}
			if ("search_customer_restriction".equalsIgnoreCase(this.getActionName())) {
				
				String userId = request.getParameter("userId");
				boolean isAssigned = this.promotion.isCustomerInAssignedList(userId);
				request.setAttribute("IS_USER_ASSIGNED", new Boolean(isAssigned));
			}
			return true;
		}catch (FDResourceException e) {
			throw new JspException(e);
		}catch (FDDuplicatePromoFieldException e) {
			actionResult.setError(e.getMessage());
			return true;
		}catch (FDPromoTypeNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		}catch (FDPromoCustNotFoundException e) {
			actionResult.setError(e.getMessage());
			return true;
		}catch(FinderException e){
			actionResult.setError(e.getMessage());
			return true;
		}
		
	}
	
	private void sendEmail(FDPromotionModel oldPromotion, FDPromotionModel newPromotion) throws FDResourceException{
		String body = calculatePromoDifferences(oldPromotion, newPromotion);
		if(!"".equals(body)){
			body = "<table border=1><tr><th>Field Name</th><th>Old Value</th><th>New Value</th></tr>" + body;
			Date currentDate = new Date();
			String subject = newPromotion.getPromotionCode();
			body = "<br> <b>Modified Time: </b>\t"+currentDate+"\r\n <br><br>"+body;
			body = "<br> <b>Modified by:</b>\t"+this.promotion.getModifiedBy()+" <br>"+body + "</table>";
			try{
				ErpMailSender mailer = new ErpMailSender();
			
				mailer.sendMail(FDStoreProperties.getPromotionEmail(), 
								FDStoreProperties.getPromotionEmail(), 
								"", 
								subject, body, true, "");
			} catch (MessagingException e) {
				LOGGER.warn("Error Sending Promotion Change email: ", e);
			}
		}
	}
	
	private String calculatePromoDifferences(FDPromotionModel oldPromotion, FDPromotionModel newPromotion){
		final StringBuffer body = new StringBuffer();
		String br = "<br>";
		String row = "<tr>";
		String column = "<td>";
		String eRow = "</tr>";
		String eColumn = "</td>";
		
		if(!oldPromotion.getName().equals(newPromotion.getName())){
			body.append(row).
				append(column+"Name"+eColumn).
				append(column+oldPromotion.getName()+eColumn).
				append(column+newPromotion.getName()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getDescription().equals(newPromotion.getDescription())){
			body.append(row).
				append(column+"Description"+eColumn).
				append(column+oldPromotion.getDescription()+eColumn).
				append(column+newPromotion.getDescription()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getPromotionType().equals(newPromotion.getPromotionType())){
			body.append(row).
				append(column+"Promotion"+eColumn).
				append(column+oldPromotion.getPromotionType()+eColumn).
				append(column+newPromotion.getPromotionType()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getMinSubtotal().equals(newPromotion.getMinSubtotal())){
			body.append(row).
				append(column+"Minimum Sub Total"+eColumn).
				append(column+oldPromotion.getMinSubtotal()+eColumn).
				append(column+newPromotion.getMinSubtotal()+eColumn).
				append(eRow);
		}
		if( !oldPromotion.getStartMonth().equals(newPromotion.getStartMonth()) ||
			!oldPromotion.getStartDay().equals(newPromotion.getStartDay()) ||
			!oldPromotion.getStartYear().equals(newPromotion.getStartYear())
		  ){
			body.append(row).
				append(column+"Start Date"+eColumn).
				append(column+oldPromotion.getStartYear()+"-"+oldPromotion.getStartMonth()+"-"+oldPromotion.getStartDay()+eColumn).
				append(column+newPromotion.getStartYear()+"-"+newPromotion.getStartMonth()+"-"+newPromotion.getStartDay()+eColumn).
				append(eRow);
		}
		if( !NVL.apply(oldPromotion.getExpirationMonth(), "").equals(newPromotion.getExpirationMonth()) ||
			!NVL.apply(oldPromotion.getExpirationDay(), "").equals(newPromotion.getExpirationDay()) ||
			!NVL.apply(oldPromotion.getExpirationYear(), "").equals(newPromotion.getExpirationYear())
		  ){
			body.append(row).
				append(column+"Expiration Date"+eColumn).
				append(column+oldPromotion.getExpirationYear()+"-"+oldPromotion.getExpirationMonth()+"-"+oldPromotion.getExpirationDay()+eColumn).
				append(column+newPromotion.getExpirationYear()+"-"+newPromotion.getExpirationMonth()+"-"+newPromotion.getExpirationDay()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isOrderTypeCorporateAllowed() ^ newPromotion.isOrderTypeCorporateAllowed() ){
			body.append(row).
				append(column+"Order Type:Corporate"+eColumn).
				append(column+oldPromotion.isOrderTypeCorporateAllowed()+eColumn).
				append(column+newPromotion.isOrderTypeCorporateAllowed()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isOrderTypeDepotAllowed() ^ newPromotion.isOrderTypeDepotAllowed() ){
			body.append(row).
				append(column+"Order Type:Depot"+eColumn).
				append(column+oldPromotion.isOrderTypeDepotAllowed()+eColumn).
				append(column+newPromotion.isOrderTypeDepotAllowed()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isOrderTypeHomeAllowed() ^ newPromotion.isOrderTypeHomeAllowed() ){
			body.append(row).
				append(column+"Order Type:Home"+eColumn).
				append(column+oldPromotion.isOrderTypeHomeAllowed()+eColumn).
				append(column+newPromotion.isOrderTypeHomeAllowed()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isOrderTypePickupAllowed() ^ newPromotion.isOrderTypePickupAllowed() ){
			body.append(row).
				append(column+"Order Type:Pickup"+eColumn).
				append(column+oldPromotion.isOrderTypePickupAllowed()+eColumn).
				append(column+newPromotion.isOrderTypePickupAllowed()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getMaxUsage().equals(newPromotion.getMaxUsage())){
			body.append(row).
				append(column+"Max. Limit"+eColumn).
				append(column+oldPromotion.getMaxUsage()+eColumn).
				append(column+newPromotion.getMaxUsage()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getRedemptionCode().equals(newPromotion.getRedemptionCode())){
			body.append(row).
				append(column+"Redemption code"+eColumn).
				append(column+oldPromotion.getRedemptionCode()+eColumn).
				append(column+newPromotion.getRedemptionCode()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getAssignedCustomerUserIds().equals(newPromotion.getAssignedCustomerUserIds())){
			body.append(row).
				append(column+"Customer Restriction"+eColumn).
				append(column+oldPromotion.getAssignedCustomerUserIds()+eColumn).
				append(column+newPromotion.getAssignedCustomerUserIds()+eColumn).
				append(eRow);
		}
		if(oldPromotion.getNeedDryGoods() ^ newPromotion.getNeedDryGoods()){
			body.append(row).
				append(column+"Need Dry Goods"+eColumn).
				append(column+oldPromotion.getNeedDryGoods()+eColumn).
				append(column+newPromotion.getNeedDryGoods()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getOrderCount().equals(newPromotion.getOrderCount())){
			body.append(row).
				append(column+"Order Count"+eColumn).
				append(column+oldPromotion.getOrderCount()+eColumn).
				append(column+newPromotion.getOrderCount()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getExcludeSkuPrefix().equals(newPromotion.getExcludeSkuPrefix())){
			body.append(row).
				append(column+"Exclude SKU Prefix"+eColumn).
				append(column+oldPromotion.getExcludeSkuPrefix()+eColumn).
				append(column+newPromotion.getExcludeSkuPrefix()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getNeedItemsFrom().equals(newPromotion.getNeedItemsFrom())){
			body.append(row).
				append(column+"Need-Items-from"+eColumn).
				append(column+oldPromotion.getNeedItemsFrom()+eColumn).
				append(column+newPromotion.getNeedItemsFrom()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getNeedBrands().equals(newPromotion.getNeedBrands())){
			body.append(row).
				append(column+"Need-Brands"+eColumn).
				append(column+oldPromotion.getNeedBrands()+eColumn).
				append(column+newPromotion.getNeedBrands()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getExcludeBrands().equals(newPromotion.getExcludeBrands())){
			body.append(row).
				append(column+"Exclude-Brands"+eColumn).
				append(column+oldPromotion.getExcludeBrands()+eColumn).
				append(column+newPromotion.getExcludeBrands()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getValueType().equals(newPromotion.getValueType())){
			body.append(row).
				append(column+"Value Type"+eColumn).
				append(column+oldPromotion.getValueType()+eColumn).
				append(column+newPromotion.getValueType()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getMaxAmount().equals(newPromotion.getMaxAmount())){
			body.append(row).
				append(column+"Max. Amount"+eColumn).
				append(column+oldPromotion.getMaxAmount()+eColumn).
				append(column+newPromotion.getMaxAmount()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getPercentOff().equals(newPromotion.getPercentOff())){
			body.append(row).
				append(column+"Percent Off"+eColumn).
				append(column+oldPromotion.getPercentOff()+eColumn).
				append(column+newPromotion.getPercentOff()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getWaiveChargeType().equals(newPromotion.getWaiveChargeType())){
			body.append(row).
				append(column+"Waive Charge"+eColumn).
				append(column+oldPromotion.getWaiveChargeType()+eColumn).
				append(column+newPromotion.getWaiveChargeType()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getCategoryName().equals(newPromotion.getCategoryName())){
			body.append(row).
				append(column+"Product Category"+eColumn).
				append(column+oldPromotion.getCategoryName()+eColumn).
				append(column+newPromotion.getCategoryName()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getProductName().equals(newPromotion.getProductName())){
			body.append(row).
				append(column+"Product Name"+eColumn).
				append(column+oldPromotion.getProductName()+eColumn).
				append(column+newPromotion.getProductName()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isRuleBasedPromotion() ^ newPromotion.isRuleBasedPromotion()){
			body.append(row).
				append(column+"Rule Based Promotion"+eColumn).
				append(column+oldPromotion.isRuleBasedPromotion()+eColumn).
				append(column+newPromotion.isRuleBasedPromotion()+eColumn).
				append(eRow);
		}
		if(!NVL.apply(oldPromotion.getRefProgCampaignCode(), "").equals(NVL.apply(newPromotion.getRefProgCampaignCode(), ""))){
			body.append(row).
				append(column+"Reference Program CampaignCode"+eColumn).
				append(column+oldPromotion.getRefProgCampaignCode()+eColumn).
				append(column+newPromotion.getRefProgCampaignCode()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isMaxUsagePerCustomer() ^ newPromotion.isMaxUsagePerCustomer()){
			body.append(row).
				append(column+"Max Usage/Customer"+eColumn).
				append(column+oldPromotion.isMaxUsagePerCustomer()+eColumn).
				append(column+newPromotion.isMaxUsagePerCustomer()+eColumn).
				append(eRow);
		}
		if(oldPromotion.getRollingExpirationDays()!=null && newPromotion.getRollingExpirationDays()!=null && 
				oldPromotion.getRollingExpirationDays().intValue() != newPromotion.getRollingExpirationDays().intValue()){
			body.append(row).
				append(column+"Rolling Exp. Days"+eColumn).
				append(column+oldPromotion.getRollingExpirationDays()+eColumn).
				append(column+newPromotion.getRollingExpirationDays()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isUniqueUse() ^ newPromotion.isUniqueUse()){
			body.append(row).
				append(column+"Unique Use"+eColumn).
				append(column+oldPromotion.isUniqueUse()+eColumn).
				append(column+newPromotion.isUniqueUse()+eColumn).
				append(eRow);
		}
		if(!oldPromotion.getNotes().equals(newPromotion.getNotes())){
			body.append(row).
				append(column+"Notes"+eColumn).
				append(column+oldPromotion.getNotes()+eColumn).
				append(column+newPromotion.getNotes()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isActive() ^ newPromotion.isActive()){
			body.append(row).
				append(column+"Active status"+eColumn).
				append(column+oldPromotion.isActive()+eColumn).
				append(column+newPromotion.isActive()+eColumn).
				append(eRow);
		}
		if(oldPromotion.isApplyFraud() ^ newPromotion.isApplyFraud()){
			body.append(row).
				append(column+"Donot Apply Fraud"+eColumn).
				append(column+(!oldPromotion.isApplyFraud())+eColumn).
				append(column+(!newPromotion.isApplyFraud())+eColumn).
				append(eRow);
		}
		TreeMap oldMap = oldPromotion.getZipRestrictions();
		TreeMap newMap = newPromotion.getZipRestrictions();
		boolean zipChanged = false;
		StringBuffer oldZipRestrictions = new StringBuffer();
		StringBuffer newZipRestrictions = new StringBuffer();
		for(Iterator i=oldMap.keySet().iterator(); i.hasNext(); ){
			Date currentKey = (Date)i.next();
			String newCurrentValue = (newMap.get(currentKey)!=null)?((FDPromoZipRestriction)newMap.get(currentKey)).toString():"";
			String oldCurrentValue = ((FDPromoZipRestriction)oldMap.get(currentKey)).toString();
			oldZipRestrictions.append(oldCurrentValue).append(br);
			if(!newCurrentValue.equals(oldCurrentValue)){
				zipChanged = true;
			}			
		}
		for(Iterator i=newMap.keySet().iterator(); i.hasNext(); ){
			Date currentKey = (Date)i.next();
			String newCurrentValue = ((FDPromoZipRestriction)newMap.get(currentKey)).toString();
			String oldCurrentValue = (oldMap.get(currentKey)!=null)?((FDPromoZipRestriction)oldMap.get(currentKey)).toString():"";
			newZipRestrictions.append(newCurrentValue).append(br);
			if(!newCurrentValue.equals(oldCurrentValue)){
				zipChanged = true;
			}
		}
		if(zipChanged){
			body.append(row).
				append(column+"Zip Code Restrictions"+eColumn).
				append(column+oldZipRestrictions+eColumn).
				append(column+newZipRestrictions+eColumn).
				append(eRow);
		}
		return body.toString();
	}

	protected boolean performGetAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		if ("create_promotion".equalsIgnoreCase(this.getActionName()) ||
				"update_promotion".equalsIgnoreCase(this.getActionName())) {
			checkWarnings(actionResult);
		}
		return true;
	}
	
	private void populatePromotionModel(HttpServletRequest request) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		this.promotion.setId(NVL.apply(request.getParameter("promo_id"), "").trim());
		this.promotion.setName(NVL.apply(request.getParameter("promo_name"), "").trim());
		this.promotion.setPromotionCode(NVL.apply(request.getParameter("code_name"), "").trim());
		this.promotion.setDescription(NVL.apply(request.getParameter("promo_description"), "").trim());
		this.promotion.setPromotionType(NVL.apply(request.getParameter("campaign_type"), "").trim());
		this.promotion.setMinSubtotal(NVL.apply(request.getParameter("min_subtotal"), "").trim());
		this.promotion.setStartMonth(NVL.apply(request.getParameter("start_month"), "").trim());
		this.promotion.setStartDay(NVL.apply(request.getParameter("start_date"), "").trim());
		this.promotion.setStartYear(NVL.apply(request.getParameter("start_year"), "").trim());
		this.promotion.setStartDate(getDate(this.promotion.getStartDay(), this.promotion.getStartMonth(), this.promotion.getStartYear(),0,0,0,"AM"));
		
		this.promotion.setExpirationMonth(NVL.apply(request.getParameter("end_month"), "").trim());
		this.promotion.setExpirationDay(NVL.apply(request.getParameter("end_date"), "").trim());
		this.promotion.setExpirationYear(NVL.apply(request.getParameter("end_year"), "").trim());		
		this.promotion.setExpirationDate(getDate(this.promotion.getExpirationDay(), this.promotion.getExpirationMonth(), this.promotion.getExpirationYear(),11,59,59,"PM"));
		
		String orderTypeCorporate = NVL.apply(request.getParameter("dlv_corp"), "").trim();
		if (!"".equals(orderTypeCorporate)) {		
			this.promotion.setIsOrderTypeCorporateAllowed(true);
		} else {
			this.promotion.setIsOrderTypeCorporateAllowed(false);			
		}
		String orderTypeDepot = NVL.apply(request.getParameter("dlv_depot"), "").trim();
		if (!"".equals(orderTypeDepot)) {		
			this.promotion.setIsOrderTypeDepotAllowed(true);
		} else {
			this.promotion.setIsOrderTypeDepotAllowed(false);			
		}
		String orderTypeHome = NVL.apply(request.getParameter("dlv_home"), "").trim();
		if (!"".equals(orderTypeHome)) {		
			this.promotion.setIsOrderTypeHomeAllowed(true);
		} else {
			this.promotion.setIsOrderTypeHomeAllowed(false);			
		}
		String orderTypePickup = NVL.apply(request.getParameter("dlv_pickup"), "").trim();
		if (!"".equals(orderTypePickup)) {		
			this.promotion.setIsOrderTypePickupAllowed(true);
		} else {
			this.promotion.setIsOrderTypePickupAllowed(false);			
		}
		this.promotion.setMaxUsage(NVL.apply(request.getParameter("usage_limit"), "").trim());
		this.promotion.setRedemptionCode(NVL.apply(request.getParameter("redemption_code"), "").trim());
		String userIds =NVL.apply(request.getParameter("user_ids"), "").replaceAll(" ", "").replaceAll("\n", "").replaceAll("\r", "");
		//this.promotion.setAssignedCustomerUserIds(userIds.trim());
		this.promotion.setTmpAssignedCustomerUserIds(userIds.trim());
		String needDryGoods = NVL.apply(request.getParameter("need_dry_goods"), "").trim();
		if ("yes_dry_goods".equals(needDryGoods)) {		
			this.promotion.setNeedsDryGoods(true);
		} else {
			this.promotion.setNeedsDryGoods(false);			
		}
		
		this.promotion.setOrderCount(NVL.apply(request.getParameter("order_number"), "").trim());
		this.promotion.setExcludeSkuPrefix(NVL.apply(request.getParameter("excluded_sku"), "").trim());
		this.promotion.setNeedItemsFrom(NVL.apply(request.getParameter("required_items"), "").trim());
		this.promotion.setNeedBrands(NVL.apply(request.getParameter("required_brands"), "").trim());
		this.promotion.setExcludeBrands(NVL.apply(request.getParameter("excluded_brands"), "").trim());
		this.promotion.setValueType(NVL.apply(request.getParameter("value_type"), "").trim());
		this.promotion.setMaxAmount(NVL.apply(request.getParameter("maximum_discount"), "").trim());
		this.promotion.setPercentOff(NVL.apply(request.getParameter("percentOff"), "").trim());
		this.promotion.setWaiveChargeType(NVL.apply(request.getParameter("waiveChargeType"), "").trim());
		this.promotion.setCategoryName(NVL.apply(request.getParameter("prod_category"), "").trim());
		this.promotion.setProductName(NVL.apply(request.getParameter("prod_name"), "").trim());
		
		this.promotion.setRuleBasePromotion(request.getParameter("ruleBased") != null);
		
		this.promotion.setRefProgCampaignCode(NVL.apply(request.getParameter("ref_prog_campaign_code"), "").trim());

		this.promotion.setIsMaxUsagePerCustomer(request.getParameter("is_max_usage_per_cust") != null);

		String rollingExpirationDays = NVL.apply(request.getParameter("rolling_expiration_days"), "").trim();
		if (!"".equals(rollingExpirationDays) && isInteger(rollingExpirationDays)) {
			this.promotion.setRollingExpirationDays(new Integer(rollingExpirationDays));
		} else {
			this.promotion.setRollingExpirationDays(null);			
		}
		
		this.promotion.setUniqueUse(request.getParameter("unique_use") != null);
		this.promotion.setNotes(NVL.apply(request.getParameter("notes"),"").trim());
		this.promotion.setActive(request.getParameter("active")!=null);
		this.promotion.setApplyFraud(request.getParameter("dontApplyFraud")==null);
		this.promotion.setProfileOperator(request.getParameter("profileOperator"));
		
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		this.promotion.setModifiedBy(agent.getUserId());
		
		if(zipRestrictionMap == null){
			zipRestrictionMap = new TreeMap();			
		}
		
		if(request.getParameter("newZipCheck")!=null && 
		   request.getParameter("newZipCheck").equals("yes") && 
		   request.getParameter("newZipDate")!=null){
			FDPromoZipRestriction zipRestriction = new FDPromoZipRestriction();
			try{
				zipRestriction.setStartDate(DateUtil.toCalendar(format.parse(request.getParameter("newZipDate"))).getTime());
				zipRestriction.setType(request.getParameter("newZipRestrictionType"));
				zipRestriction.setZipCodes(NVL.apply(request.getParameter("newZipCodes"), "").replaceAll("[\r\n | \"]","").toUpperCase());
				zipRestrictionMap.put(DateUtil.toCalendar(format.parse(request.getParameter("newZipDate"))).getTime(), zipRestriction);
			}
			catch(ParseException pe){/*left blank*/}
			
		}
		
		if(request.getParameter("previousZip")!=null && request.getParameter("editedZipDate")!=null){
			FDPromoZipRestriction zipRestriction = new FDPromoZipRestriction();
			try{
				zipRestrictionMap.remove(DateUtil.toCalendar(format.parse(request.getParameter("previousZip"))).getTime());
				//User by mistake / purposefully is trying to replace a zip restriction that already exists
				if(zipRestrictionMap.containsKey(DateUtil.toCalendar(format.parse(request.getParameter("editedZipDate"))).getTime())){
					this.promotion.setZipValidationCheckWarningMessage("You are trying to replace Zip Restriction "+request.getParameter("editedZipDate")+" that already exists");
				}
				zipRestriction.setStartDate(DateUtil.toCalendar(format.parse(request.getParameter("editedZipDate"))).getTime());
				zipRestriction.setType(request.getParameter("zipRestrictionType"));
				zipRestriction.setZipCodes(NVL.apply(request.getParameter("zipCodes"), "").replaceAll("[\r\n | \"]","").toUpperCase());
				zipRestrictionMap.put(DateUtil.toCalendar(format.parse(request.getParameter("editedZipDate"))).getTime(), zipRestriction);
			}
			catch(ParseException pe){/*left blank*/}
		}
		this.promotion.setZipRestrictions(zipRestrictionMap);
		
				
		List attrParamList = getAttributeParamList(request);
		Collections.sort(attrParamList);
		promotion.setAttributeList(attrParamList);
		
		promotion.setAssignedCategories(NVL.apply(request.getParameter("category_ids"), "").replaceAll("[\r\n | \"]","").toLowerCase());
		promotion.setAssignedDepartments(NVL.apply(request.getParameter("department_ids"), "").replaceAll("[\r\n | \"]","").toLowerCase());
		promotion.setAssignedRecipes(NVL.apply(request.getParameter("recipe_ids"), "").replaceAll("[\r\n | \"]","").toLowerCase());
		
	}
	
	private List getAttributeParamList(HttpServletRequest request) {
		
		Enumeration paramNames = request.getParameterNames();
		
		List dataList = new ArrayList();
		List dataTmpKeyList = new ArrayList();
		
		String paramName = null;
				
		String tmpIndex = null;
		int tmpBracesIndex = -1;
		int endBracesIndex = -1;
				
	    while(paramNames.hasMoreElements()) {
	      paramName = (String)paramNames.nextElement();
	      if(paramName.startsWith("attributeList")) {	    	  
	       	  tmpBracesIndex = paramName.indexOf("[");
	       	  endBracesIndex =	paramName.indexOf("]", tmpBracesIndex);
	    	  if(tmpBracesIndex != -1 && endBracesIndex != -1) {
	    		  tmpIndex = paramName.substring(tmpBracesIndex+1,endBracesIndex);
	    		  
	    		  if(!dataTmpKeyList.contains(tmpIndex)) {
	    			  dataList.add(getAttributeParam(request, tmpIndex));
	    			  dataTmpKeyList.add(tmpIndex);
	    		  }	    		  
	    	  }
	      }
	    }	    
	    return dataList;
	}


	
	private FDPromotionAttributeParam getAttributeParam(HttpServletRequest request, String index) {
			
		FDPromotionAttributeParam tmpParam = new FDPromotionAttributeParam();
		String key = "attributeList["+index+"].";
		tmpParam.setAttributeIndex(index);
		tmpParam.setAttributeName(request.getParameter(key+"attributeName"));
		tmpParam.setDesiredValue(request.getParameter(key+"desiredValue").trim());// Fix to trim invalid profiles with space.
		//tmpParam.setOperator(request.getParameter(key+"operator"));
		return tmpParam;			
	}

		

	private void validatePromotion(ActionResult result) {
		result.addError("".equals(this.promotion.getName()), "promo_name", "required");
		result.addError(this.promotion.getName() != null && this.promotion.getName().length() > 32, "promo_name", "Max Length is 32 Characters");
		result.addError("".equals(this.promotion.getPromotionCode()), "code_name", "required");
		result.addError("".equals(this.promotion.getDescription()), "promo_description", "required");
		result.addError(this.promotion.getDescription() != null && this.promotion.getDescription().length() > 256, "promo_description", "Max Length is 256 Characters");
		result.addError(this.promotion.getNotes() != null && this.promotion.getNotes().length() > 255, "notes", "Max Length is 255 Characters");
		result.addError("".equals(this.promotion.getPromotionType()), "campaign_type", "required");
		result.addError(StringUtil.isEmpty(this.promotion.getMinSubtotal()), "min_subtotal", "required");
		result.addError(!"".equals(this.promotion.getMinSubtotal()) && !isDouble(this.promotion.getMinSubtotal()), "min_subtotal", "must be a number");
		result.addError(!"".equals(this.promotion.getMaxAmount()) && !isDouble(this.promotion.getMaxAmount()), "maximum_discount", "must be a number");
		result.addError("".equals(this.promotion.getMaxUsage()), "usage_limit", "required");
		result.addError(!"".equals(this.promotion.getMaxUsage()) && !isInteger(this.promotion.getMaxUsage()), "usage_limit", "must be a number");
		result.addError(!"".equals(this.promotion.getOrderCount()) && !isInteger(this.promotion.getOrderCount()), "order_number", "must be a number");
		String sday = this.promotion.getStartDay();
		String smonth = this.promotion.getStartMonth();
		String syear = this.promotion.getStartYear();
		result.addError(hasDateField(sday, smonth, syear) && !isCompleteDate(sday, smonth, syear), "start_year", "incomplete date");
		result.addError(this.promotion.getStartDate() == null, "start_year", "required");
		String eday = this.promotion.getExpirationDay();
		String emonth = this.promotion.getExpirationMonth();
		String eyear = this.promotion.getExpirationYear();
		result.addError(hasDateField(eday, emonth, eyear) && !isCompleteDate(eday, emonth, eyear), "end_year", "incomplete date");	
		result.addError(this.promotion.getExpirationDate() == null, "end_year", "required");
		result.addError(checkDate(promotion.getStartDate(), promotion.getExpirationDate())
							,"start_year","Start date cannot be greater than end date");
		result.addError("".equals(this.promotion.getValueType()), "value_type", "required");
		if(!this.promotion.getZipRestrictions().isEmpty()){
			TreeMap map = (TreeMap)this.promotion.getZipRestrictions();
			for(Iterator i = map.keySet().iterator(); i.hasNext(); ){
				FDPromoZipRestriction zipRestriction = (FDPromoZipRestriction) map.get((Date)i.next());
				String zipCodes = NVL.apply(zipRestriction.getZipCodes(), "");
				result.addError(zipCodes.equals(""), "zip_codes", "ZipCodes field should not be empty");
				result.addError((zipCodes.toUpperCase().indexOf("ALL")!=-1 && zipCodes.trim().length()>3), "zip_codes", "Zip Codes can only contain ALL");
				if(!zipCodes.toUpperCase().trim().equals("ALL") && !zipCodes.trim().equals("")){
					StringTokenizer st = new StringTokenizer(zipCodes.trim(), ",");
					int count = st.countTokens(); 
					for (int x = 0; x < count; x++) {
						String token = st.nextToken().trim();
						result.addError(token.length()!=5,"zip_length","One or more zip codes are not of the right length (5)");
						result.addError(!Pattern.compile("[0-9]{5}").matcher(token).find(), "zip_pattern","One or more zip codes are not numbers");
					}
				}
			}
		}
		
		result.addError(EnumPromotionType.DCP_DISCOUNT.getName()
				.equals(this.promotion.getPromotionType()) && !("percentOff".equals(this.promotion.getValueType()))
										, "campaign_type", "DCPD Promotion can only be percent off");
		
		result.addError(EnumPromotionType.DCP_DISCOUNT.getName()
				.equals(this.promotion.getPromotionType()) && (StringUtil.isEmpty(this.promotion.getAssignedDepartments()) && 
						StringUtil.isEmpty(this.promotion.getAssignedCategories()) && StringUtil.isEmpty(this.promotion.getAssignedRecipes()))
										, "percentOff", "DCPD Promotion should have atleast one department,category or recipe restriction");
		
		result.addError(EnumPromotionType.SAMPLE.getName()
				.equals(this.promotion.getPromotionType()) && !("sample".equals(this.promotion.getValueType()))
										, "campaign_type", "Sample Promotion can only be sample items");
		
		result.addError( (((this.promotion.getAssignedDepartments() != null && !StringUtil.isEmpty(this.promotion.getAssignedDepartments()))
					|| (this.promotion.getAssignedCategories() != null && !StringUtil.isEmpty(this.promotion.getAssignedCategories()))
					|| 	(this.promotion.getAssignedRecipes() != null && !StringUtil.isEmpty(this.promotion.getAssignedRecipes())))
					&& !(EnumPromotionType.DCP_DISCOUNT.getName().equals(this.promotion.getPromotionType())))	
				, "percentOff", "Below fields can be used only for DCPD promotion");
		
		if ("sample".equals(this.promotion.getValueType())) {
			result.addError("".equals(this.promotion.getCategoryName()), "prod_category", "required");
			result.addError("".equals(this.promotion.getProductName()), "prod_name", "required");
			result.addError(!"".equals(this.promotion.getMaxAmount()), "maximum_discount", "no value expected");

		} else {
			result.addError(!"".equals(this.promotion.getCategoryName()), "prod_category", "no value expected");
			result.addError(!"".equals(this.promotion.getProductName()), "prod_name", "no value expected");

			if ("discount".equals(this.promotion.getValueType())) {
				result.addError("".equals(this.promotion.getMaxAmount()), "maximum_discount", "required");
				result.addError((!StringUtil.isEmpty(this.promotion.getMaxAmount()) && 
						!StringUtil.isDecimal(this.promotion.getMaxAmount())), "maximum_discount", "must be a number");

			} else if ("percentOff".equals(this.promotion.getValueType())) {
				result.addError("".equals(this.promotion.getPercentOff()), "percentOff", "required");
				result.addError((!StringUtil.isEmpty(this.promotion.getPercentOff()) && 
						!StringUtil.isNumeric(this.promotion.getPercentOff())), "percentOff", "must be a number");
				result.addError((!StringUtil.isEmpty(this.promotion.getPercentOff()) && 
						(StringUtil.isNumeric(this.promotion.getPercentOff()) 
								&& Integer.parseInt(this.promotion.getPercentOff()) > 100) ), "percentOff", "must be a number between 0-100");
				
			} else if ("waiveCharge".equals(this.promotion.getValueType())) {
				result.addError("".equals(this.promotion.getWaiveChargeType()), "waiveChargeType", "required");
			}
		}								
		
	}

	private void checkWarnings(ActionResult result) {
		String warningMessage = "";
		if (EnumPromotionType.getEnum(this.promotion.getPromotionType()) == EnumPromotionType.SAMPLE && 
				//EnumPromotionType.SAMPLE.getDescription().equals(EnumPromotionType.getEnum(this.promotion.getPromotionType()).getDescription()) &&
				"".equals(this.promotion.getOrderCount()) &&
				"".equals(this.promotion.getExcludeBrands()) &&
				"".equals(this.promotion.getAssignedCustomerUserIds()) &&
				"".equals(this.promotion.getExcludeSkuPrefix()) &&
				"".equals(this.promotion.getNeedBrands()) &&
				"".equals(this.promotion.getRedemptionCode()) &&
				"".equals(this.promotion.getNeedItemsFrom()) &&
				!this.promotion.getNeedDryGoods()) {
					warningMessage += "Too few criteria for a sample promotion.  ";
		}
		if((this.promotion.getAssignedCustomerUserIds().length()==0 && StringUtil.isEmpty(this.promotion.getTmpAssignedCustomerUserIds())) 
				&& this.promotion.getRedemptionCode().equals("") && this.promotion.getZipRestrictions().isEmpty()){
			warningMessage += "Promotion doesnt have Customer nor Redemption Code nor Zip Restriction.   ";
		}
		
		if(this.promotion.getZipValidationCheckWarningMessage()!=null){
			warningMessage += this.promotion.getZipValidationCheckWarningMessage();
		}

		if (Integer.parseInt(this.promotion.getMaxUsage()) > 1) {
			warningMessage += "Usage limit is more than 1.  ";								
		}
		if (!"".equals(warningMessage)) result.setError("Warning: " + warningMessage);
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		//default implementation
	}


	private void savePromotionInSession() {
		HttpSession session = pageContext.getSession();
		session.setAttribute(SessionName.EDIT_PROMOTION, promotion);
	}

	private void getPromotionInSession() {
		HttpSession session = pageContext.getSession();
		setPromotion((FDPromotionModel)session.getAttribute(SessionName.EDIT_PROMOTION));
	}

	private void removePromotionFromSession() {
		HttpSession session = pageContext.getSession();
		session.removeAttribute(SessionName.EDIT_PROMOTION);
	}

	private boolean isDouble(String d) {
		try {
			Double.parseDouble(d);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
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
	
	private Date getDate(String day, String month, String year, int hour, int minute, int second, String am_pm){
		if (isCompleteDate(day, month, year)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
				Date date = sdf.parse(month + "-" + day + "-" + year);
				Calendar cal = DateUtil.toCalendar(date);
				cal.set(Calendar.HOUR, hour);
				cal.set(Calendar.MINUTE, minute);
				cal.set(Calendar.SECOND, second);
				if(am_pm.equals("AM")){
					cal.set(Calendar.AM_PM, Calendar.AM);
				} else {
					cal.set(Calendar.AM_PM, Calendar.PM);
				}
				return cal.getTime();
			} catch (ParseException pe) { }
		}
		
		return null;
	}
	
	//Check if start date is greater than end date
	private boolean checkDate(Date startDate, Date endDate) {
		boolean result = false;
		if(startDate != null && endDate != null) {
			return startDate.after(endDate);
		}
		return result;
		
	}
		

}
