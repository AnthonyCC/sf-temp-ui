package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.RegistrationTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.RegistrationTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmRegistrationTag extends AbstractCmTag {

	private static final Logger LOGGER = LoggerFactory.getInstance(CmRegistrationTag.class);
	private static final String REGISTRATION_TAG_FS = "cmCreateRegistrationTag(%s,%s,%s,%s,%s,%s,%s);";
	private static final String PENDING_REGISTRATION_EVENT = "pendingCoremetricsRegistrationEvent";
	private static final String REGISTRATION_LOCATION = "coremetricsRegistrationLocation";
	
	private RegistrationTagModelBuilder builder = new RegistrationTagModelBuilder();
	private boolean force;
	
	public static void setPendingRegistrationEvent(HttpSession session){
		session.setAttribute(PENDING_REGISTRATION_EVENT, true);
	}

	public static void setRegistrationLocation(HttpSession session, String location){
		session.setAttribute(REGISTRATION_LOCATION, location);
	}

	@Override
	protected String getTagJs() throws SkipTagException {

		HttpSession session = ((PageContext) getJspContext()).getSession();
		Boolean pendingEventAttr = (Boolean) session.getAttribute(PENDING_REGISTRATION_EVENT);

		if (force || (pendingEventAttr!=null && pendingEventAttr.equals(true))){
			session.removeAttribute(PENDING_REGISTRATION_EVENT);
			
			builder.setUser((FDUserI) session.getAttribute(SessionName.USER));
			builder.setLocation((String)session.getAttribute(REGISTRATION_LOCATION));
			session.removeAttribute(REGISTRATION_LOCATION);
						
			RegistrationTagModel model = builder.buildTagModel();
						
			String setProductScript = String.format(REGISTRATION_TAG_FS,
					toJsVar(model.getRegistrationId()),
					toJsVar(model.getRegistrantEmail()),
					toJsVar(model.getRegistrantCity()),
					toJsVar(model.getRegistrantState()),
					toJsVar(model.getRegistrantPostalCode()),
					toJsVar(model.getRegistrantCountry()),
					toJsVar(mapToAttrString(model.getAttributesMaps())));
			
			LOGGER.debug(setProductScript);
			return setProductScript;

		} else {
			throw new SkipTagException("There is no " + PENDING_REGISTRATION_EVENT);
		}		
	}

	public void setForce(boolean force) {
		this.force = force;
	}
	
	public void setUpdate(boolean update) {
		builder.setUpdate(update);
	}
}