package com.freshdirect.webapp.taglib.coremetrics;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ConversionEventTagModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmConversionEventTag extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmConversionEventTag.class);
	private static final String PENDING_HELP_EMAIL_EVENT = "pendingCoremetricsHelpEmailEvent";
	private static final String CONVERSION_EVENT_TAG_FS = "cmCreateConversionEventTag(%s,%s,%s,%s,%s);";
	private static final String PENDING_CONVERSION_ORDER_MODIFIED_MODELS = "pendingCoremetricsOrderModifiedConversionModels";

	private ConversionEventTagModelBuilder tagModelBuilder = new ConversionEventTagModelBuilder();
	private String subject;
	private FDOrderI order;
	private boolean orderModified;

	
	public static void setPendingHelpEmailSubject(HttpSession session, String subject){
		session.setAttribute(PENDING_HELP_EMAIL_EVENT, subject);
	}
	
	public static void buildPendingOrderModifiedModels(HttpSession session, FDCartModel cartS){
		ConversionEventTagModelBuilder tagModelBuilder = new ConversionEventTagModelBuilder();
		session.setAttribute(PENDING_CONVERSION_ORDER_MODIFIED_MODELS, tagModelBuilder.buildOrderModifiedModels(cartS));
	}
	
	public static String getTagJs(ConversionEventTagModel tagModel){
		
		String tagJs = String.format(CONVERSION_EVENT_TAG_FS, 
				toJsVar(tagModel.getEventId()), 
				toJsVar(tagModel.getActionType()), 
				toJsVar(tagModel.getEventCategoryId()), 
				toJsVar(tagModel.getPoints()), 
				toJsVar(mapToAttrString(tagModel.getAttributesMaps())));

		LOGGER.debug(tagJs);
		return tagJs;
	}
	
	@Override
	protected String getTagJs() throws SkipTagException {
		
		HttpSession session = ((PageContext) getJspContext()).getSession();

		@SuppressWarnings("unchecked")
		List<ConversionEventTagModel> models = (List<ConversionEventTagModel>)session.getAttribute(PENDING_CONVERSION_ORDER_MODIFIED_MODELS);

		if(orderModified && models != null) {
			session.removeAttribute(PENDING_CONVERSION_ORDER_MODIFIED_MODELS);
			StringBuilder tagJsBuilder = new StringBuilder();
			
			for(ConversionEventTagModel model : models){
				ConversionEventTagModelBuilder.completeOrderModifiedModel(model, order);
				tagJsBuilder.append("\n" + getTagJs(model));				
			}

			return tagJsBuilder.toString();
		
		} else {

			tagModelBuilder.setUser((FDUserI) session.getAttribute(SessionName.USER));
	
			if (subject == null){
				subject = (String) session.getAttribute(PENDING_HELP_EMAIL_EVENT); 
				session.removeAttribute(PENDING_HELP_EMAIL_EVENT);
			}
			tagModelBuilder.setSubject(subject);
			
			ConversionEventTagModel tagModel = tagModelBuilder.buildTagModel();
			return getTagJs(tagModel);
		}
	}
	
	public void setEventId(String eventId) {
		tagModelBuilder.setEventId(eventId);
	}

	public void setCategoryId(String categoryId) {
		tagModelBuilder.setCategoryId(categoryId);
	}

	public void setFirstPhase(boolean firstPhase) {
		tagModelBuilder.setFirstPhase(firstPhase);
	}

	public void setUser(FDUserI user) {
		tagModelBuilder.setUser(user);
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setOrder(FDOrderI order) {
		this.order = order;
	}
	
	public void setOrderModified(boolean orderModified) {
		this.orderModified = orderModified;
	}

}
