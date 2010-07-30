package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionSection;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.NumberUtil;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class PromotionOfferControllerTag extends AbstractControllerTag {

	private FDPromotionNewModel promotion;
	
	public void setPromotion(FDPromotionNewModel promotion) {
		this.promotion = promotion;
	}

	
	public FDPromotionNewModel getPromotion() {
		return promotion;
	}
	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		
		try {			
			this.populatePromotionModel(request,actionResult);
			if(actionResult.isSuccess()){
				populatePromoChangeModel();
				savePromotion();
			}
		} catch (FDResourceException e) {
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
		}catch(FinderException fe){
			throw new JspException(fe);
		}
		// TODO Auto-generated method stub
		return true;
	}


	private void savePromotion() throws FDResourceException,
			FDDuplicatePromoFieldException, FDPromoTypeNotFoundException,
			FDPromoCustNotFoundException {
		if("promoOffer".equalsIgnoreCase(this.getActionName())){
			FDPromotionNewManager.storePromotionOfferInfo(promotion);
		}else if("promoCart".equalsIgnoreCase(this.getActionName())){
			FDPromotionNewManager.storePromotionCartInfo(promotion);
		}
	}
	
	private void populatePromotionModel(HttpServletRequest request,ActionResult actionResult)throws FDResourceException {
		
		if("promoOffer".equalsIgnoreCase(this.getActionName())){
			//TODO: Validations
		String promotionType = NVL.apply(request.getParameter("discount_type"), "").trim();
		if(EnumPromotionType.HEADER.getName().equalsIgnoreCase(promotionType)){
			this.promotion.setMaxAmount("");
			this.promotion.setPercentOff(NVL.apply(request.getParameter("hd_perc"), "").trim());
			String headerDiscountType = NVL.apply(request.getParameter("header_discount_type"), "").trim();
			String offerType = NVL.apply(request.getParameter("header_discount_type_all"), "").trim();
			this.promotion.setCombineOffer(!"".equalsIgnoreCase(NVL.apply(request.getParameter("hd_allow_offer"), "").trim()));
			if("".equals(headerDiscountType)){
				actionResult.addError(true, "discountEmpty", " Please select one discount type under HEADER.");
			}
			else if("perc".equalsIgnoreCase(headerDiscountType)){				
				String percentOff = NVL.apply(request.getParameter("hd_perc"), "").trim();
				this.promotion.setPercentOff(percentOff);
				this.promotion.setMaxAmount("");
				this.promotion.setWaiveChargeType("");
				this.promotion.setExtendDpDays(null);
				this.promotion.setOfferType(EnumOfferType.GENERIC.getName());
				if(!NumberUtil.isInteger(percentOff)){
					actionResult.addError(true, "percentOffNumber", " Discount % value for HEADER should be integer.");
				}
			}else if("amount".equalsIgnoreCase(headerDiscountType)){
				String maxAmount = NVL.apply(request.getParameter("hd_amt"), "").trim();
				this.promotion.setMaxAmount(maxAmount);
				this.promotion.setPercentOff("");
				this.promotion.setWaiveChargeType("");
				this.promotion.setExtendDpDays(null);
				this.promotion.setOfferType(offerType);
				if(!NumberUtil.isDouble(maxAmount)){
					actionResult.addError(true, "maxAmountNumber", " Discount $ value should be number.");
				}
			}else if("hd_free".equalsIgnoreCase(headerDiscountType)){
				this.promotion.setWaiveChargeType("DLV");
				this.promotion.setMaxAmount("");
				this.promotion.setPercentOff("");
				this.promotion.setExtendDpDays(null);
				this.promotion.setOfferType(EnumOfferType.WAIVE_DLV_CHARGE.getName());
				this.promotion.setCombineOffer(true);
			}else if("hd_extend_dp".equalsIgnoreCase(headerDiscountType)){
				String extendDpDays = NVL.apply(request.getParameter("extendDpDays"), "").trim();
				if(NumberUtil.isInteger(extendDpDays)){
					this.promotion.setExtendDpDays(Integer.parseInt(extendDpDays));
					this.promotion.setMaxAmount("");
					this.promotion.setWaiveChargeType("");
					this.promotion.setPercentOff("");
				}else{
					actionResult.addError(true, "extendDpDaysRequired", " Extend Delivery Pass Days value should be integer.");
				}
				this.promotion.setOfferType(EnumOfferType.DP_EXTN.getName());
				this.promotion.setCombineOffer(true);
			}
			
//			this.promotion.setCombineOffer(!"".equalsIgnoreCase(NVL.apply(request.getParameter("hd_allow_offer"), "").trim()));
			this.promotion.setPromotionType(promotionType);
			setWSPromotionCode();
			clearSampleTypeInfo();
			clearLineItemTypeInfo();
		}else if(EnumPromotionType.LINE_ITEM.getName().equalsIgnoreCase(promotionType)){
			this.promotion.setMaxAmount("");
			this.promotion.setOfferType(EnumOfferType.LINE_ITEM.getName());
			String percentOff = NVL.apply(request.getParameter("li_discount"), "").trim();
			this.promotion.setPercentOff(percentOff);
			if(!NumberUtil.isInteger(percentOff)){
				actionResult.addError(true, "liPercentOffNumber", " Discount % value for LINE ITEM should be integer.");
			}
			String maxItems = NVL.apply(request.getParameter("li_maxItems"), "").trim();
			if(!"".equals(maxItems)){
				if(NumberUtil.isInteger(maxItems)){
					this.promotion.setMaxItemCount(Integer.parseInt(maxItems));
				}else{
					actionResult.addError(true, "maxItemsNumber", " Max # items value should be integer.");
				}
			}else{
				this.promotion.setMaxItemCount(null);
			}
			populateDcpdData(request);

			validateDcpdData(request, actionResult);
			this.promotion.setCombineOffer(!"".equalsIgnoreCase(NVL.apply(request.getParameter("li_allowOffer"), "").trim()));
			this.promotion.setFavoritesOnly(!"".equalsIgnoreCase(NVL.apply(request.getParameter("li_favorites"), "").trim()));
			this.promotion.setPerishable(!"".equalsIgnoreCase(NVL.apply(request.getParameter("li_perishables"), "").trim()));
			this.promotion.setPromotionType(promotionType);
			clearHeaderTypeInfo();
			clearSampleTypeInfo();
//			this.promotion.setOfferType("");
			setWSPromotionCode();
		}else if(EnumPromotionType.SAMPLE.getName().equalsIgnoreCase(promotionType)){
			this.promotion.setOfferType(EnumOfferType.SAMPLE.getName());
			this.promotion.setCategoryName(NVL.apply(request.getParameter("categoryName"), "").trim());
			this.promotion.setProductName(NVL.apply(request.getParameter("productName"), "").trim());
			ContentFactory contentFactory = ContentFactory.getInstance();
			if(!"".equalsIgnoreCase(promotion.getCategoryName())){
				if(null == contentFactory.getContentNode(FDContentTypes.CATEGORY, promotion.getCategoryName().toLowerCase())){
					actionResult.addError(true, "invalidCategoryName", promotion.getCategoryName()+" is invalid category Id." );
				}
			}
			if(!"".equalsIgnoreCase(promotion.getProductName())){
				if(null == contentFactory.getContentNode(FDContentTypes.PRODUCT, promotion.getProductName().toLowerCase())){
					actionResult.addError(true, "invalidProductName", promotion.getProductName()+" is invalid product Id" );
				}
			}
			
			
			this.promotion.setPromotionType(promotionType);
//			this.promotion.setOfferType("");
			this.promotion.setCombineOffer(true);
			clearHeaderTypeInfo();
			clearLineItemTypeInfo();
			this.promotion.setPercentOff("");
			setWSPromotionCode();
		}
		}else if("promoCart".equalsIgnoreCase(this.getActionName())){
			//TODO:Validations
			String subTotal = NVL.apply(request.getParameter("subTotal"), "").trim();
			String needDryGoods = NVL.apply(request.getParameter("edit_cartreq_needDryGoods"), "").trim();			
			String skuQuantity = NVL.apply(request.getParameter("skuQuantity"), "").trim();
			String excFromSubtotal = NVL.apply(request.getParameter("excFromSubtotal"), "").trim();
			this.promotion.setSubTotalExcludeSkus(excFromSubtotal);
			populateDcpdData(request);
			validateDcpdData(request, actionResult);
			if(!"".equals(excFromSubtotal)){
				String[] excFromSubtotalArr = excFromSubtotal.split(",");
				ContentFactory contentFactory = ContentFactory.getInstance();
				List invalidExcFromSubtotal = new ArrayList();
				for (int i = 0; i < excFromSubtotalArr.length; i++) {
					if(null==contentFactory.getContentNode(FDContentTypes.SKU, excFromSubtotalArr[i].toUpperCase())){
						invalidExcFromSubtotal.add(excFromSubtotalArr[i]);
					}
				}
				if(!invalidExcFromSubtotal.isEmpty()){
					actionResult.addError(true, "invalidExcludeSkus", invalidExcFromSubtotal.toString()+" are invalid SKUs for excluding from SubTotal." );
				}
			}
			this.promotion.setSkuQuantity(Integer.parseInt(skuQuantity));
			if(!NumberUtil.isDouble(subTotal)){
				this.promotion.setMinSubtotal(subTotal);			
				actionResult.addError(true, "subtotalNumber", " Subtotal value should be a number.");
			}else{
				this.promotion.setMinSubtotal(FormatterUtil.formatToTwoDecimal(Double.parseDouble(subTotal)));
			}
			this.promotion.setNeedDryGoods("YES".equalsIgnoreCase(needDryGoods));
			
		}
//		this.promotion.setRuleBased(request.getParameter("ruleBased") != null);
//		this.promotion.setApplyFraud(request.getParameter("dontApplyFraud")==null);
//		this.promotion.setNeedCustomerList(request.getParameter("eligibilityList")==null);
//		this.promotion.setRuleBased(request.getParameter("ruleBased")==null);


		
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		this.promotion.setModifiedBy(agent.getUserId());
		this.promotion.setModifiedDate(new Date());
		
	}


	private void clearSampleTypeInfo() {
		this.promotion.setCategoryName("");
		this.promotion.setProductName("");
	}


	private void clearLineItemTypeInfo() {
		this.promotion.setMaxItemCount(null);
		this.promotion.setDcpdData(Collections.EMPTY_LIST);
		this.promotion.setPerishable(false);
		this.promotion.setFavoritesOnly(false);
	}


	private void clearHeaderTypeInfo() {		
		this.promotion.setExtendDpDays(null);
		this.promotion.setWaiveChargeType("");
		this.promotion.setMaxAmount("");
	}


	private void setWSPromotionCode() {
		if(EnumPromotionType.WINDOW_STEERING.getName().equalsIgnoreCase(this.promotion.getOfferType())){
			this.promotion.setPromotionCode(this.promotion.getPromotionCode().replace("CD", "WS"));
		}else{
			this.promotion.setPromotionCode(this.promotion.getPromotionCode().replace("WS", "CD"));
		}
	}


	private void populateDcpdData(HttpServletRequest request) {
		List<FDPromoContentModel> dcpdData = new ArrayList<FDPromoContentModel>();
		String departments = NVL.apply(request.getParameter("departments"), "").trim();
		if(!"".equals(departments)){
			String[] departmentsArr = departments.split(",");
			for (int i = 0; i < departmentsArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.DEPARTMENT);
				contentModel.setContentId(departmentsArr[i].trim());
				contentModel.setExcluded(false);		
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		String categories = NVL.apply(request.getParameter("categories"), "").trim();
		if(!"".equals(categories)){
			String[] categoriesArr = categories.split(",");
			for (int i = 0; i < categoriesArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.CATEGORY);
				contentModel.setContentId(categoriesArr[i].trim());
				contentModel.setExcluded(false);		
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		String recipes = NVL.apply(request.getParameter("recipes"), "").trim();
		if(!"".equals(recipes)){
			String[] recipesArr = recipes.split(",");
			for (int i = 0; i < recipesArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.RECIPE);
				contentModel.setContentId(recipesArr[i].trim());
				contentModel.setExcluded(false);		
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		String eligibleSku = NVL.apply(request.getParameter("eligibleSku"), "").trim();
		if(!"".equals(eligibleSku)){
			String[] eligibleSkuArr = eligibleSku.split(",");
			for (int i = 0; i < eligibleSkuArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.SKU);
				contentModel.setContentId(eligibleSkuArr[i].trim());
				if("on".equals(NVL.apply(request.getParameter("isInelgSkus"), "off")))
						contentModel.setExcluded(true);		
				else
					contentModel.setExcluded(false);
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		String eligibleBrand = NVL.apply(request.getParameter("eligibleBrand"), "").trim();
		if(!"".equals(eligibleBrand)){
			String[] eligibleBrandArr = eligibleBrand.split(",");
			for (int i = 0; i < eligibleBrandArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.BRAND);
				contentModel.setContentId(eligibleBrandArr[i].trim());
				if("on".equals(NVL.apply(request.getParameter("isInelgBrands"), "off")))
					contentModel.setExcluded(true);		
				else
					contentModel.setExcluded(false);	
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		/*String ineligibleSku = NVL.apply(request.getParameter("ineligibleSku"), "").trim();
		if(!"".equals(ineligibleSku)){
			String[] ineligibleSkuArr = ineligibleSku.split(",");
			for (int i = 0; i < ineligibleSkuArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.SKU);
				contentModel.setContentId(ineligibleSkuArr[i].trim());
				contentModel.setExcluded(true);		
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}
		String excludeBrand = NVL.apply(request.getParameter("excludeBrand"), "").trim();
		if(!"".equals(excludeBrand)){
			String[] excludeBrandArr = excludeBrand.split(",");
			for (int i = 0; i < excludeBrandArr.length; i++) {
				FDPromoContentModel contentModel = new FDPromoContentModel();
				contentModel.setContentType(EnumDCPDContentType.BRAND);
				contentModel.setContentId(excludeBrandArr[i].trim());
				contentModel.setExcluded(true);		
				contentModel.setPromotionId(this.promotion.getId());
				dcpdData.add(contentModel);
			}
		}*/
		if("promoOffer".equalsIgnoreCase(this.getActionName())){
			this.promotion.setDcpdData(dcpdData);
		}else if("promoCart".equalsIgnoreCase(this.getActionName())){
			this.promotion.setCartStrategies(dcpdData);
		}
		
	}
	
	private void validateDcpdData(HttpServletRequest request,ActionResult result) throws FDResourceException{
		ContentFactory contentFactory = ContentFactory.getInstance();
		String departments = NVL.apply(request.getParameter("departments"), "").trim();
		if(!"".equals(departments)){
			List invalidDepts = new ArrayList();
			String[] departmentsArr = departments.split(",");
			for (int i = 0; i < departmentsArr.length; i++) {
				if(null==contentFactory.getContentNode(FDContentTypes.DEPARTMENT, departmentsArr[i].toLowerCase())){
					invalidDepts.add(departmentsArr[i]);
				}
			}
			if(!invalidDepts.isEmpty()){
				result.addError(true, "invalidDepts", invalidDepts.toString()+" are invalid Departments." );
			}
		}
		String categories = NVL.apply(request.getParameter("categories"), "").trim();
		if(!"".equals(categories)){
			List invalidCats = new ArrayList();
			String[] categoriesArr = categories.split(",");
			for (int i = 0; i < categoriesArr.length; i++) {
				if(null==contentFactory.getContentNode(FDContentTypes.CATEGORY, categoriesArr[i].toLowerCase())){
					invalidCats.add(categoriesArr[i]);
				}
			}
			if(!invalidCats.isEmpty()){
				result.addError(true, "invalidCats", invalidCats.toString()+" are invalid Categories." );
			}
		}
		String recipes = NVL.apply(request.getParameter("recipes"), "").trim();
		if(!"".equals(recipes)){
			List invalidRecipes = new ArrayList();
			String[] recipesArr = recipes.split(",");
			for (int i = 0; i < recipesArr.length; i++) {
				if(null==contentFactory.getContentNode(FDContentTypes.RECIPE, recipesArr[i].toLowerCase())){
					invalidRecipes.add(recipesArr[i]);
				}
			}
			if(!invalidRecipes.isEmpty()){
				result.addError(true, "invalidRecipes", invalidRecipes.toString()+" are invalid Recipes." );
			}
		}
		String eligibleSku = NVL.apply(request.getParameter("eligibleSku"), "").trim();
		if(!"".equals(eligibleSku)){
			List invalidSkus = new ArrayList();
			String[] eligibleSkuArr = eligibleSku.split(",");
			for (int i = 0; i < eligibleSkuArr.length; i++) {
				if(null==contentFactory.getContentNode(FDContentTypes.SKU, eligibleSkuArr[i].toUpperCase())){
					invalidSkus.add(eligibleSkuArr[i]);
				}
			}
			if(!invalidSkus.isEmpty()){
				result.addError(true, "invalidSKUs", invalidSkus.toString()+" are invalid SKUs." );
			}
		}
		String eligibleBrand = NVL.apply(request.getParameter("eligibleBrand"), "").trim();
		if(!"".equals(eligibleBrand)){
			List invalidBrands = new ArrayList();
			String[] eligibleBrandArr = eligibleBrand.split(",");
			for (int i = 0; i < eligibleBrandArr.length; i++) {
				if(null==contentFactory.getContentNode(FDContentTypes.SKU, eligibleBrandArr[i].toUpperCase())){
					invalidBrands.add(eligibleBrandArr[i]);
				}
			}
			if(!invalidBrands.isEmpty()){
				result.addError(true, "invalidBrands", invalidBrands.toString()+" are invalid Brands." );
			}
		}
			
	}
	
	
	private void populatePromoChangeModel() throws FDResourceException,
	FinderException {
		List promoChanges = new ArrayList<FDPromoChangeModel>();
		List promoChangeDetails = new ArrayList<FDPromoChangeDetailModel>();
		FDPromoChangeModel changeModel = new FDPromoChangeModel();
		changeModel.setActionDate(promotion.getModifiedDate());
		//changeModel.setActionType(EnumPromoChangeType.MODIFY);
		changeModel.setUserId(promotion.getModifiedBy());
		changeModel.setPromotionId(promotion.getId());
		changeModel.setChangeDetails(promoChangeDetails);
		promoChanges.add(changeModel);
		promotion.setAuditChanges(promoChanges);
		
		FDPromotionNewModel oldPromotion = FDPromotionNewManager.loadPromotion(promotion.getPromotionCode());
		if("promoCart".equalsIgnoreCase(this.getActionName())){
			populateCartChangeModel(promoChangeDetails, changeModel,
					oldPromotion);
			
		}else if("promoOffer".equalsIgnoreCase(this.getActionName())){
			populateOfferChangeModel(promoChangeDetails, changeModel,
					oldPromotion);
		}		
		
	}


	private void populateOfferChangeModel(List promoChangeDetails,
			FDPromoChangeModel changeModel, FDPromotionNewModel oldPromotion) {
		changeModel.setActionType(EnumPromoChangeType.OFFER_INFO);
		if(!promotion.getPromotionType().equalsIgnoreCase(oldPromotion.getPromotionType())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Promotion Type");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getPromotionType(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getPromotionType(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getOfferType().equalsIgnoreCase(oldPromotion.getOfferType())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Offer Type");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getOfferType(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getOfferType(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(EnumPromotionType.HEADER.getName().equals(promotion.getPromotionType()) && EnumPromotionType.HEADER.getName().equals(oldPromotion.getPromotionType())){							
			populateOfferHeaderChangeModel(promoChangeDetails, oldPromotion);
		}else if(EnumPromotionType.LINE_ITEM.getName().equals(promotion.getPromotionType()) && EnumPromotionType.LINE_ITEM.getName().equals(oldPromotion.getPromotionType())){
			populateOfferLineItemChangeModel(promoChangeDetails, oldPromotion);
		}else if(EnumPromotionType.SAMPLE.getName().equals(promotion.getPromotionType()) && EnumPromotionType.SAMPLE.getName().equals(oldPromotion.getPromotionType())){
			if(!promotion.getCategoryName().equalsIgnoreCase(oldPromotion.getCategoryName())){
				FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
				changeDetailModel.setChangeFieldName("Category Id");
				changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getCategoryName(),"").trim());
				changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getCategoryName(),"").trim());
				changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
				promoChangeDetails.add(changeDetailModel);
			}
			if(!promotion.getProductName().equalsIgnoreCase(oldPromotion.getProductName())){
				FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
				changeDetailModel.setChangeFieldName("Product Id");
				changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getProductName(),"").trim());
				changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getProductName(),"").trim());
				changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
				promoChangeDetails.add(changeDetailModel);	
			}
		}
	}


	private void populateOfferLineItemChangeModel(List promoChangeDetails,
			FDPromotionNewModel oldPromotion) {
		if(!promotion.getPercentOff().equalsIgnoreCase(oldPromotion.getPercentOff())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Percent Off");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getPercentOff(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getPercentOff(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getMaxItemCount().equals(oldPromotion.getMaxItemCount())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Max Items#");
			changeDetailModel.setChangeFieldOldValue(""+oldPromotion.getMaxItemCount());
			changeDetailModel.setChangeFieldNewValue(""+promotion.getMaxItemCount());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(promotion.isFavoritesOnly()!=oldPromotion.isFavoritesOnly()){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Favorites Only");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.isFavoritesOnly()?"Y":"X");
			changeDetailModel.setChangeFieldNewValue(promotion.isFavoritesOnly()?"Y":"X");
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(promotion.isPerishable()!=oldPromotion.isPerishable()){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Perishable Only");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.isPerishable()?"Y":"X");
			changeDetailModel.setChangeFieldNewValue(promotion.isPerishable()?"Y":"X");
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		promotion.getDcpdDataString();
		oldPromotion.getDcpdDataString();
		if(!promotion.getDcpdDepts().equalsIgnoreCase(oldPromotion.getDcpdDepts())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Line Item - Departments");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getDcpdDepts());
			changeDetailModel.setChangeFieldNewValue(promotion.getDcpdDepts());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getDcpdCats().equalsIgnoreCase(oldPromotion.getDcpdCats())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Line Item - Categories");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getDcpdCats());
			changeDetailModel.setChangeFieldNewValue(promotion.getDcpdCats());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getDcpdRecps().equalsIgnoreCase(oldPromotion.getDcpdRecps())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Line Item - Recipes");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getDcpdRecps());
			changeDetailModel.setChangeFieldNewValue(promotion.getDcpdRecps());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getDcpdSkus().equalsIgnoreCase(oldPromotion.getDcpdSkus())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Line Item - Skus");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getDcpdSkus());
			changeDetailModel.setChangeFieldNewValue(promotion.getDcpdSkus());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getDcpdBrands().equalsIgnoreCase(oldPromotion.getDcpdBrands())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Line Item - Brands");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getDcpdBrands());
			changeDetailModel.setChangeFieldNewValue(promotion.getDcpdBrands());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
	}


	private void populateOfferHeaderChangeModel(List promoChangeDetails,
			FDPromotionNewModel oldPromotion) {
		if(!promotion.getPercentOff().equalsIgnoreCase(oldPromotion.getPercentOff())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Header - Percent Off");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getPercentOff(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getPercentOff(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);	
		}
		if(!promotion.getMaxAmount().equalsIgnoreCase(oldPromotion.getMaxAmount())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Max Amount");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getMaxAmount(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getMaxAmount(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);	
		}
		if(!promotion.getWaiveChargeType().equalsIgnoreCase(oldPromotion.getWaiveChargeType())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Waive Charge Type");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getWaiveChargeType(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getWaiveChargeType(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);	
		}
		if(promotion.getExtendDpDays()!= oldPromotion.getExtendDpDays()){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Extend DP Days");
			changeDetailModel.setChangeFieldOldValue(null !=oldPromotion.getExtendDpDays()?""+oldPromotion.getExtendDpDays():"");
			changeDetailModel.setChangeFieldNewValue(null !=promotion.getExtendDpDays()?""+promotion.getExtendDpDays():"");
			changeDetailModel.setChangeSectionId(EnumPromotionSection.OFFER_INFO);
			promoChangeDetails.add(changeDetailModel);	
		}		
	}


	private void populateCartChangeModel(List promoChangeDetails,
			FDPromoChangeModel changeModel, FDPromotionNewModel oldPromotion) {
		changeModel.setActionType(EnumPromoChangeType.CART_REQ_INFO);
		if(promotion.getMinSubtotal().equalsIgnoreCase(oldPromotion.getMinSubtotal())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Promotion Type");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getPromotionType(),"").trim());
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getPromotionType(),"").trim());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(promotion.isNeedDryGoods()!= oldPromotion.isNeedDryGoods()){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Need Dry Goods");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.isNeedDryGoods()?"X":"");
			changeDetailModel.setChangeFieldNewValue(promotion.isNeedDryGoods()?"X":"");
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!NVL.apply(promotion.getSubTotalExcludeSkus(),"").equalsIgnoreCase(NVL.apply(oldPromotion.getSubTotalExcludeSkus(),""))){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Exclude SKUs from Subtotal");
			changeDetailModel.setChangeFieldOldValue(NVL.apply(oldPromotion.getSubTotalExcludeSkus(),""));
			changeDetailModel.setChangeFieldNewValue(NVL.apply(promotion.getSubTotalExcludeSkus(),""));
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		
		promotion.getCartContentString();
		oldPromotion.getCartContentString();
		if(!promotion.getCartDepts().equalsIgnoreCase(oldPromotion.getCartDepts())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Cart Info - Departments");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getCartDepts());
			changeDetailModel.setChangeFieldNewValue(promotion.getCartDepts());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getCartCats().equalsIgnoreCase(oldPromotion.getCartCats())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Cart Info - Categories");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getCartCats());
			changeDetailModel.setChangeFieldNewValue(promotion.getCartCats());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}		
		if(!promotion.getCartSkus().equalsIgnoreCase(oldPromotion.getCartSkus())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Cart Info - Skus");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getCartSkus());
			changeDetailModel.setChangeFieldNewValue(promotion.getCartSkus());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(!promotion.getCartBrands().equalsIgnoreCase(oldPromotion.getCartBrands())){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Cart Info - Brands");
			changeDetailModel.setChangeFieldOldValue(oldPromotion.getCartBrands());
			changeDetailModel.setChangeFieldNewValue(promotion.getCartBrands());
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
		if(promotion.getSkuQuantity()!= oldPromotion.getSkuQuantity()){
			FDPromoChangeDetailModel changeDetailModel = new FDPromoChangeDetailModel();
			changeDetailModel.setChangeFieldName("Min. Sku Quantity");
			changeDetailModel.setChangeFieldOldValue(null!=oldPromotion.getSkuQuantity()?""+oldPromotion.getSkuQuantity():"");
			changeDetailModel.setChangeFieldNewValue(null!=promotion.getSkuQuantity()?""+promotion.getSkuQuantity():"");
			changeDetailModel.setChangeSectionId(EnumPromotionSection.CART_REQ_INFO);
			promoChangeDetails.add(changeDetailModel);
		}
	}
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
