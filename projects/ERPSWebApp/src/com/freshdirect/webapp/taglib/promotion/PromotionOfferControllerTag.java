package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
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
			if(actionResult.isSuccess())
				savePromotion();
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
			}
			
			this.promotion.setCombineOffer(!"".equalsIgnoreCase(NVL.apply(request.getParameter("hd_allow_offer"), "").trim()));
			this.promotion.setPromotionType(promotionType);
			setWSPromotionCode();
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
//			this.promotion.setOfferType("");
			setWSPromotionCode();
		}else if(EnumPromotionType.SAMPLE.getName().equalsIgnoreCase(promotionType)){
			this.promotion.setMaxAmount("");
			this.promotion.setOfferType(EnumOfferType.SAMPLE.getName());
			this.promotion.setCategoryName(NVL.apply(request.getParameter("categoryName"), "").trim());
			this.promotion.setProductName(NVL.apply(request.getParameter("productName"), "").trim());
			this.promotion.setPromotionType(promotionType);
//			this.promotion.setOfferType("");
			this.promotion.setCombineOffer(true);
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
	public static class TagEI extends AbstractControllerTag.TagEI {
		// default impl
	}
}
