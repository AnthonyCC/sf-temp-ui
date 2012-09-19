package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.RegistrationTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ConversionEventTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.RegistrationTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmRegistrationTag extends AbstractCmTag implements SessionName{

	private static final Logger LOGGER = LoggerFactory.getInstance(CmRegistrationTag.class);
	private static final String REGISTRATION_TAG_FS = "cmCreateRegistrationTag(%s,%s,%s,%s,%s,%s,%s);";
	
	private RegistrationTagModelBuilder tagModelBuilder = new RegistrationTagModelBuilder();
	private boolean force;
	
	public static void setPendingRegistrationEvent(HttpSession session){
		session.setAttribute(PENDING_REGISTRATION_EVENT, true);
	}

	public static void setPendingLoginEvent(HttpSession session){
		session.setAttribute(PENDING_LOGIN_EVENT, true);
	}
	
	public static void setRegistrationLocation(HttpSession session, String location){
		session.setAttribute(REGISTRATION_LOCATION, location);
	}

	public static void setRegistrationOrigZipCode(HttpSession session, String origZipCode){
		session.setAttribute(REGISTRATION_ORIG_ZIP_CODE, origZipCode);
	}

	@Override
	protected String getTagJs() throws SkipTagException {

		HttpSession session = ((PageContext) getJspContext()).getSession();
		boolean pendingRegistration = Boolean.TRUE.equals(session.getAttribute(PENDING_REGISTRATION_EVENT));
		boolean pendingLogin = Boolean.TRUE.equals(session.getAttribute(PENDING_LOGIN_EVENT));

		if (force || pendingRegistration || pendingLogin){
			
			session.removeAttribute(PENDING_REGISTRATION_EVENT);
			session.removeAttribute(PENDING_LOGIN_EVENT);
			
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
			tagModelBuilder.setUser(user);
			
			String registrationLocation = (String)session.getAttribute(REGISTRATION_LOCATION); 
			session.removeAttribute(REGISTRATION_LOCATION);
			tagModelBuilder.setLocation(registrationLocation);
			
			tagModelBuilder.setOrigZipCode((String)session.getAttribute(REGISTRATION_ORIG_ZIP_CODE));
			session.removeAttribute(REGISTRATION_ORIG_ZIP_CODE);
			
			RegistrationTagModel regModel = tagModelBuilder.buildTagModel();
						
			String tagJs = String.format(REGISTRATION_TAG_FS,
					toJsVar(regModel.getRegistrationId()),
					toJsVar(regModel.getRegistrantEmail()),
					toJsVar(regModel.getRegistrantCity()),
					toJsVar(regModel.getRegistrantState()),
					toJsVar(regModel.getRegistrantPostalCode()),
					toJsVar(regModel.getRegistrantCountry()),
					toJsVar(mapToAttrString(regModel.getAttributesMaps())));
			
			
			//append registration conversion event to tagJs
			if (pendingRegistration){
				ConversionEventTagModelBuilder regConversionEventBuilder = new ConversionEventTagModelBuilder();
				
				regConversionEventBuilder.setEventId(registrationLocation);
				regConversionEventBuilder.setCategoryId(ConversionEventTagModelBuilder.CAT_REGISTRATION);
				regConversionEventBuilder.setZipCode(regModel.getRegistrantPostalCode());
				
				tagJs += "\n" + getConversionEventTagJs(regConversionEventBuilder);
			} 

			//append login conversion event to tagJs
			if (pendingRegistration || pendingLogin){
				ConversionEventTagModelBuilder logConversionEventBuilder = new ConversionEventTagModelBuilder();
				
				logConversionEventBuilder.setEventId(ConversionEventTagModelBuilder.EVENT_LOGIN);
				logConversionEventBuilder.setCategoryId(ConversionEventTagModelBuilder.CAT_LOGIN);
				logConversionEventBuilder.setUser(user);
				
				tagJs += "\n" + getConversionEventTagJs(logConversionEventBuilder);
			}
			
			LOGGER.debug(tagJs);
			return tagJs;

		} else {
			throw new SkipTagException("There is no pending event and force is false");
		}		
	}

	private String getConversionEventTagJs(ConversionEventTagModelBuilder conversionEventBuilder) throws SkipTagException{
		ConversionEventTagModel conversionEventModel = conversionEventBuilder.buildTagModel();
		return CmConversionEventTag.getTagJs(conversionEventModel);
	}
	
	public void setForce(boolean force) {
		this.force = force;
	}
	
	protected boolean insertTagInCaseOfCrmContext(){
		return true;
	}
}