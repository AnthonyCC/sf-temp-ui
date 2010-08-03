package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumDCPDContentType;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.PromotionStateGraph;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoContentModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class PromotionStateSwitchTag extends AbstractControllerTag {
	private static final long serialVersionUID = 1L;
	private static Category LOGGER = LoggerFactory.getInstance(PromotionStateSwitchTag.class);

	private PromotionStateGraph graph;
	private FDPromotionNewModel promotion;

	/**
	 * Candidate state
	 */
	private EnumPromotionStatus status;

	public void setGraph(PromotionStateGraph graph) {
		this.graph = graph;
	}
	
	public void setStatus(EnumPromotionStatus status) {
		this.status = status;
	}




	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		try {
			HttpSession session = pageContext.getSession();
			CrmAgentModel agent = CrmSession.getCurrentAgent(session);			
		if ("changeStatus".equalsIgnoreCase(getActionName())){ 
			if(graph.getStates().contains(status)) {
				final FDPromotionNewModel promotion = graph.getPromotion();
					validatePromotion(request,actionResult);
					if(actionResult.isSuccess()){
						promotion.setStatus(status);
						this.promotion.setModifiedBy(agent.getUserId());
						this.promotion.setModifiedDate(new Date());						
						populatePromoChangeModel(promotion);
						FDPromotionNewManager.storePromotionStatus(promotion,status);
					}				 
			} else {
				LOGGER.error("Invalid status " + status.getName());
				actionResult.addError(true, "promo.store", "Could not switch to '" + status.getDescription()+"' from '"+promotion.getStatus().getDescription()+"'");
			}
		}else if("holdStatus".equalsIgnoreCase(getActionName())){
			promotion.setOnHold(!promotion.isOnHold());
			this.promotion.setModifiedBy(agent.getUserId());
			this.promotion.setModifiedDate(new Date());				
			populatePromoChangeModel(promotion);
			FDPromotionNewManager.storePromotionHoldStatus(promotion);			
		}
		} catch (FDResourceException e) {
			throw new JspException(e);
		}
		return true;
	}

	private void populatePromoChangeModel(final FDPromotionNewModel promotion) {
		FDPromoChangeModel changeModel = new FDPromoChangeModel();
		List promoChanges = new ArrayList<FDPromoChangeModel>();
		changeModel.setPromotionId(promotion.getId());
		changeModel.setActionDate(new Date());
		if("changeStatus".equalsIgnoreCase(getActionName())){
			if(EnumPromotionStatus.PROGRESS.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.STATUS_PROGRESS);
			else if(EnumPromotionStatus.TEST.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.STATUS_TEST);
			else if(EnumPromotionStatus.APPROVE.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.APPROVE);
			else if(EnumPromotionStatus.CANCELLING.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.CANCEL);
			else if(EnumPromotionStatus.PUBLISHED.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.PUBLISH);
			else if(EnumPromotionStatus.CANCELLED.equals(promotion.getStatus()))
				changeModel.setActionType(EnumPromoChangeType.CANCELLED);
		}else if("holdStatus".equalsIgnoreCase(getActionName())){
			if(promotion.isOnHold()){
				changeModel.setActionType(EnumPromoChangeType.HOLD);
			}else{
				changeModel.setActionType(EnumPromoChangeType.UNHOLD);
			}
		}
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		changeModel.setUserId(agent.getUserId());
		promoChanges.add(changeModel);
		this.promotion.setAuditChanges(promoChanges);
	}


	private void validatePromotion(HttpServletRequest request,ActionResult result) throws FDResourceException{
		if(null != promotion && null !=status){ 
			if(EnumPromotionStatus.TEST.equals(status)){
				if(null ==promotion.getStartDate()){
					result.addError(true, "startDateEmpty", "Promotion Start Date can't be empty.");
				}
				if(null == promotion.getExpirationDate()){
					result.addError(true, "endDateEmpty", "Promotion End Date can't be empty.");
				}
				if("".equals(NVL.apply(promotion.getMaxUsage(),"").trim()) || "0".equals(promotion.getMaxUsage().trim())){
					result.addError(true, "usageCountEmpty", "Promotion Usage Count is required.");
				}
				if("".equals(NVL.apply(promotion.getPromotionType(),"").trim())){
					result.addError(true, "offerTypeEmpty", "Promotion Offer Type can't be empty.");
				}
				else if(EnumPromotionType.LINE_ITEM.getName().equals(promotion.getPromotionType())){
					if("".equals(NVL.apply(promotion.getPercentOff(),"").trim())){
						result.addError(true, "discountEmpty", "Promotion Discount value is required for LINE ITEM offer.");
					}
					List<FDPromoContentModel> dcpdData = promotion.getDcpdData();
					if(null == dcpdData || dcpdData.isEmpty()){
						result.addError(true, "dcpdEmpty", "One of the Department/Category/Recipe/SKU/Brand fields must be specified for LINE ITEM offer.");
					}else{
						boolean isExcluded = false;
						boolean isDeptCatRecAvailable = false;
						for (Iterator iterator = dcpdData.iterator(); iterator.hasNext();) {
							FDPromoContentModel promoContentModel = (FDPromoContentModel) iterator.next();
							if(null !=promoContentModel && 
									((EnumDCPDContentType.SKU.equals(promoContentModel.getContentType()) && promoContentModel.isExcluded() )
											||(EnumDCPDContentType.BRAND.equals(promoContentModel.getContentType()) && promoContentModel.isExcluded()))){
								isExcluded = true;
								break;
							}
						}
						if(isExcluded){
							for (Iterator iterator = dcpdData.iterator(); iterator.hasNext();) {
								FDPromoContentModel promoContentModel = (FDPromoContentModel) iterator.next();
								if(null != promoContentModel &&
										(EnumDCPDContentType.DEPARTMENT.equals(promoContentModel.getContentType()) || EnumDCPDContentType.CATEGORY.equals(promoContentModel.getContentType())||EnumDCPDContentType.RECIPE.equals(promoContentModel.getContentType()))){
									isDeptCatRecAvailable = true;
									break;
								}
							}
						}
						if(isExcluded && !isDeptCatRecAvailable){
							result.addError(true, "dcrEmpty", "If ineligible SKU or ineligible brand selected, additional department/category/recipe id must be specified for LINE ITEM offer.");
						}
					}
				}else if(EnumPromotionType.SAMPLE.getName().equals(promotion.getPromotionType())){
					if("".equals(NVL.apply(promotion.getProductName(),"").trim())){
				 		result.addError(true, "prodNameEmpty", "Promotion product id is required for SAMPLE offer.");
					}
					if("".equals(NVL.apply(promotion.getCategoryName(),"").trim())){
						result.addError(true, "catNameEmpty", "Promotion category id is required for SAMPLE offer.");
					}
				}else if(EnumPromotionType.HEADER.getName().equals(promotion.getPromotionType()) ){
					if(!"DLV".equalsIgnoreCase(promotion.getWaiveChargeType())){
						if("".equals(NVL.apply(promotion.getPercentOff(),"").trim()) && "".equals(NVL.apply(promotion.getMaxAmount(),"").trim()) && null== promotion.getExtendDpDays()){
							result.addError(true, "maxAmountEmpty", "Discount value is required");
						}
					}
					if(EnumPromotionType.WINDOW_STEERING.getName().equalsIgnoreCase(promotion.getOfferType())){
						if(!"ZONE".equalsIgnoreCase(promotion.getGeoRestrictionType())){					
							result.addError(true, "wsZoneRequired", "For a Window Steering promotion, ZONE type georestriction should be configured.");
						}
						if(!promotion.isCombineOffer()){
							result.addError(true, "combineOfferRequired", "For a Window Steering promotion, 'combine offer' should be selected.");
						}
					}
				}
				List<FDPromoCustStrategyModel> custStrategies = promotion.getCustStrategies();
				if(null!= custStrategies && !custStrategies.isEmpty()){
					FDPromoCustStrategyModel custModel = (FDPromoCustStrategyModel)custStrategies.get(0);
					if(!custModel.isOrderTypeHome() && !custModel.isOrderTypeCorporate() && !custModel.isOrderTypePickup()){
						result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
					}
				}else{
					result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
				}
				if(null == promotion.getMinSubtotal() || "".equals(promotion.getMinSubtotal())){
					result.addError(true, "minSubTotalEmpty", "Minimum Sub Total is required for the promotion.");
				}
				if(FDPromotionNewManager.isRedemptionCodeExists(promotion.getRedemptionCode(),promotion.getId())){
					result.addError(true, "redemptionCodeDuplicate", " An active promotion exists with the same redemption code, please change the redemption code.");				
				}
			}
			
		}
	}
	@Override
	protected boolean performGetAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		// TODO Auto-generated method stub
		return performAction(request, actionResult);
	}


	public FDPromotionNewModel getPromotion() {
		return promotion;
	}

	public void setPromotion(FDPromotionNewModel promotion) {
		this.promotion = promotion;
	}


	public static class TagEI extends AbstractControllerTag.TagEI {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED)
			};
		}
	}
}
