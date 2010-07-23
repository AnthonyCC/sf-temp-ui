package com.freshdirect.webapp.taglib.promotion;

import java.util.Calendar;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.management.FDDuplicatePromoFieldException;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeDetailModel;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromoCustNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromoTypeNotFoundException;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;

public class StoreImportedPromotionTag extends AbstractControllerTag {
	private static final long serialVersionUID = 1L;

	private static Category LOGGER = LoggerFactory.getInstance(StoreImportedPromotionTag.class);

	FDPromotionNewModel	promotion;
	CrmAgentModel		agent;
	
	public void setPromotion(FDPromotionNewModel promotion) {
		this.promotion = promotion;
	}

	public void setAgent(CrmAgentModel agent) {
		this.agent = agent;
	}



	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		
		final String actionName = getActionName();
		
		if (promotion == null) {
			actionResult.addError(true, "store.promo", "Promotion is NULL");
			return true;
		}
		
		if ("store".equalsIgnoreCase(actionName)) {

			promotion.setCreatedBy(agent.getUserId());
			promotion.setCreatedDate(Calendar.getInstance().getTime());
			
			FDPromoChangeModel changeModel = new FDPromoChangeModel();
			changeModel.setChangeDetails(Collections.<FDPromoChangeDetailModel>emptyList());
			
			changeModel.setActionDate(promotion.getCreatedDate());
			changeModel.setActionType(EnumPromoChangeType.CREATE);
			changeModel.setUserId(promotion.getCreatedBy());

			promotion.addAuditChange(changeModel);
			
			
			try {
				PrimaryKey pk = FDPromotionNewManager.createPromotionBasic(promotion);
				if (pk != null) {
					promotion.setPK(pk);
					FDPromotionNewManager.storePromotion(promotion);
				}
			} catch (FDResourceException e) {
				LOGGER.error("Failed to save promotion with code " + promotion.getPromotionCode(), e);
				actionResult.addError(true, "store.promo", e.getNestedException().getMessage());
			} catch (FDDuplicatePromoFieldException e) {
				LOGGER.error("Failed to save promotion with code " + promotion.getPromotionCode(), e);
				actionResult.addError(true, "store.promo", e.getMessage());
			} catch (FDPromoTypeNotFoundException e) {
				LOGGER.error("Failed to save promotion with code " + promotion.getPromotionCode(), e);
				actionResult.addError(true, "store.promo", e.getMessage());
			} catch (FDPromoCustNotFoundException e) {
				LOGGER.error("Failed to save promotion with code " + promotion.getPromotionCode(), e);
				actionResult.addError(true, "store.promo", e.getMessage());
			}

			// return true;
		} else {
			actionResult.addError(true, "store.promo", "Invalid action");
		}
		
		return true;
	}



	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
					 new VariableInfo(
						data.getAttributeString("result"),
						"com.freshdirect.framework.webapp.ActionResult",
						true,
						VariableInfo.NESTED)};
		}
	}
}
