package com.freshdirect.webapp.taglib.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.pxp.PromoPublisher;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class PromoPublishTag extends AbstractControllerTag {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Category LOGGER = LoggerFactory.getInstance(PromoPublishTag.class);

	private String publishResult;
	
	public void setPublishResult(String resultName) {
		this.publishResult = resultName;
	}

	
	
	@Override
	protected boolean performAction(HttpServletRequest request,
			ActionResult actionResult) throws JspException {
		// TODO Auto-generated method stub
		
		String actionName = request.getParameter("action");
		
		if ("publish".equalsIgnoreCase(actionName)) {
			List<FDPromotionNewModel> ppList = new ArrayList<FDPromotionNewModel>();
			Collection<FDPromotionNewModel> promos = FDPromotionNewModelFactory.getInstance().getPromotions();

			for (Enumeration<String> it = request.getParameterNames(); it.hasMoreElements();) {
				String code = it.nextElement();
				
				FDPromotionNewModel promo = FDPromotionNewModelFactory.getInstance().getPromotion(code);
				if (promo != null)
					ppList.add(promo);
			}

			
			if (ppList.size() == 0) {
				actionResult.addError(true, "promo.publish", "Select at least one item to publish");
				return true;
			}


			/** Run Lola Run
			 *	http://www.imdb.com/title/tt0130827/
			 */
			
			HttpSession session = pageContext.getSession();
			CrmAgentModel agent = CrmSession.getCurrentAgent(session);
			
			PromoPublisher publisher = new PromoPublisher();
			publisher.setPromoList(ppList);
			publisher.setAgent(agent);

			final boolean result = publisher.doPublish();

			// Post mortem actions
			if (!result) {
				actionResult.addError(true, "promo.publish", "An error occured during publish");
				return true;
			}
			
			if (publishResult != null) {
				request.setAttribute(this.publishResult, publisher.getLastResult());
			}
			
		}

		return true;
	}

	public static class TagEI extends AbstractControllerTag.TagEI {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("result"),
					"com.freshdirect.framework.webapp.ActionResult",
					true,
					VariableInfo.NESTED),
				new VariableInfo(
					data.getAttributeString("publishResult"),
					"java.util.Map<java.lang.String,java.lang.Boolean>",
					true,
					VariableInfo.NESTED)
			};
		}
	}
}
