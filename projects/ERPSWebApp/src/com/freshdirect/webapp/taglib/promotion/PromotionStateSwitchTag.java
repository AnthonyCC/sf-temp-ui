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
					validatePromotion(request,actionResult, this.promotion);
					if(actionResult.isSuccess()){
						promotion.setStatus(status);
						this.promotion.setModifiedBy(agent.getUserId());
						this.promotion.setModifiedDate(new Date());						
						populatePromoChangeModel(promotion);
						FDPromotionNewManager.storePromotionStatus(promotion,status,true);
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
		} else if("changeStatusForBatch".equalsIgnoreCase(getActionName())) {
			//Test all batch promotions
			if(graph.getStates().contains(status)) {
				final FDPromotionNewModel promotion = graph.getPromotion();
				//get all promotions in the batch
				List<FDPromotionNewModel> batchPromotions = FDPromotionNewManager.getBatchPromotions(promotion.getBatchId());
				Iterator iter = batchPromotions.iterator();
				StringBuffer sb = new StringBuffer();
				
				//For approving the batch promotions, check if any promotion is in Inprogress status
				if(status.equals(EnumPromotionStatus.APPROVE)) {
					while(iter.hasNext()) {
						FDPromotionNewModel bpromo = (FDPromotionNewModel) iter.next();
						if(bpromo.getStatus().equals(EnumPromotionStatus.PROGRESS)) {
							sb.append(bpromo.getPromotionCode());
						}
					}
				}
				
				if(sb.length() > 0) {
					actionResult.addError(true, "batchpromoError", "Following promotions in this batch are InProgress status. Please complete the Test for these, before approving the entire batch.   " + sb.toString());
				}
				
				if(actionResult.isSuccess()) {
					sb = new StringBuffer();
					iter = batchPromotions.iterator();
					while(iter.hasNext()) {
						FDPromotionNewModel bpromo = (FDPromotionNewModel) iter.next();
						if(!validatePromotion(request,actionResult, bpromo)) {
							sb.append(bpromo.getPromotionCode());
							sb.append(", ");
						}
						
					}
					
					if(sb.length() > 0 && !actionResult.isSuccess()) {
						actionResult.addError(true, "batchpromoError", "Following promotions in this batch are incomplete. Please complete the setup for these, before testing the entire batch.   " + sb.toString());
					}
				}
				
				if(actionResult.isSuccess()){
					promotion.setStatus(status);
					this.promotion.setModifiedBy(agent.getUserId());
					this.promotion.setModifiedDate(new Date());
					this.promotion.setBatchPromo(true);
					populatePromoChangeModel(promotion);					
					FDPromotionNewManager.storePromotionStatus(promotion,status,true);
				}				 
			} else {
				LOGGER.error("Invalid status " + status.getName());
				actionResult.addError(true, "promo.store", "Could not switch to '" + status.getDescription()+"' from '"+promotion.getStatus().getDescription()+"'");
			}
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
		if("changeStatus".equalsIgnoreCase(getActionName()) || "changeStatusForBatch".equalsIgnoreCase(getActionName())) {
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


	private boolean validatePromotion(HttpServletRequest request,ActionResult result, FDPromotionNewModel lPromotion) throws FDResourceException{
		boolean valid = true;
		if(null != lPromotion && null !=status){ 
			if(EnumPromotionStatus.TEST.equals(status)){
				if(null ==lPromotion.getStartDate()){
					result.addError(true, "startDateEmpty", "Promotion Start Date can't be empty.");
					valid = false;
				}
				if(null == lPromotion.getExpirationDate()){
					result.addError(true, "endDateEmpty", "Promotion End Date can't be empty.");
					valid = false;
				}
				if("".equals(NVL.apply(lPromotion.getMaxUsage(),"").trim()) || "0".equals(lPromotion.getMaxUsage().trim())){
					result.addError(true, "usageCountEmpty", "Promotion Usage Count is required.");
					valid = false;
				}
				if("".equals(NVL.apply(lPromotion.getPromotionType(),"").trim())){
					result.addError(true, "offerTypeEmpty", "Promotion Offer Type can't be empty.");
					valid = false;
				}
				else if(EnumPromotionType.LINE_ITEM.getName().equals(lPromotion.getPromotionType())){
					if("".equals(NVL.apply(lPromotion.getPercentOff(),"").trim()) && "".equals(NVL.apply(lPromotion.getMaxAmount(),"").trim())){
						result.addError(true, "discountEmpty", "Promotion Discount value is required for LINE ITEM offer.");
						valid = false;
					}
					List<FDPromoContentModel> dcpdData = lPromotion.getDcpdData();
					if(null == dcpdData || dcpdData.isEmpty()){
						result.addError(true, "dcpdEmpty", "One of the Department/Category/Recipe/SKU/Brand fields must be specified for LINE ITEM offer.");
						valid = false;
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
							valid = false;
						}
					}
				}else if(EnumPromotionType.SAMPLE.getName().equals(lPromotion.getPromotionType())){
					if("".equals(NVL.apply(lPromotion.getProductName(),"").trim())){
				 		result.addError(true, "prodNameEmpty", "Promotion product id is required for SAMPLE offer.");
				 		valid = false;
					}
					if("".equals(NVL.apply(lPromotion.getCategoryName(),"").trim())){
						result.addError(true, "catNameEmpty", "Promotion category id is required for SAMPLE offer.");
						valid = false;
					}
				}else if(EnumPromotionType.HEADER.getName().equals(lPromotion.getPromotionType()) ){
					if(!"DLV".equalsIgnoreCase(lPromotion.getWaiveChargeType())){
						if("".equals(NVL.apply(lPromotion.getPercentOff(),"").trim()) && "".equals(NVL.apply(lPromotion.getMaxAmount(),"").trim()) && null== lPromotion.getExtendDpDays() && lPromotion.getDollarOffList() == null){
							result.addError(true, "maxAmountEmpty", "Discount value is required");
							valid = false;
						}
					}
					if(EnumPromotionType.WINDOW_STEERING.getName().equalsIgnoreCase(lPromotion.getOfferType())){
						if(!"ZONE".equalsIgnoreCase(lPromotion.getGeoRestrictionType())){					
							result.addError(true, "wsZoneRequired", "For a Window Steering promotion, ZONE type georestriction should be configured.");
							valid = false;
						}
						if(!lPromotion.isCombineOffer()){
							result.addError(true, "combineOfferRequired", "For a Window Steering promotion, 'combine offer' should be selected.");
							valid = false;
						}
					}
				}
				List<FDPromoCustStrategyModel> custStrategies = lPromotion.getCustStrategies();
				if(null!= custStrategies && !custStrategies.isEmpty()){
					FDPromoCustStrategyModel custModel = (FDPromoCustStrategyModel)custStrategies.get(0);
					if(!custModel.isOrderTypeHome() && !custModel.isOrderTypeCorporate() && !custModel.isOrderTypePickup() && !custModel.isOrderTypeFDX()){
						result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
						valid = false;
					}
				}else{
					result.addError(true, "addressTypeEmpty", "Promotion delivery address type must be selected.");
					valid = false;
				}
				if(null == lPromotion.getMinSubtotal() || "".equals(lPromotion.getMinSubtotal())){
					result.addError(true, "minSubTotalEmpty", "Minimum Sub Total is required for the promotion.");
					valid = false;
				}
				if(FDPromotionNewManager.isRedemptionCodeExists(lPromotion.getRedemptionCode(),lPromotion.getId())){
					result.addError(true, "redemptionCodeDuplicate", " An active promotion exists with the same redemption code, please change the redemption code.");
					valid = false;
				}
			}			
		}
		return valid;
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
