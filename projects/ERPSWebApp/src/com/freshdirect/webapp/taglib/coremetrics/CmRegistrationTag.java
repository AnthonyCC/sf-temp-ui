package com.freshdirect.webapp.taglib.coremetrics;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.fdstore.coremetrics.builder.ConversionEventTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.RegistrationTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.RegistrationTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmRegistrationTag extends AbstractCmTag implements SessionName{

	private static final Logger LOGGER = LoggerFactory.getInstance(CmRegistrationTag.class);
	private RegistrationTagModelBuilder tagModelBuilder = new RegistrationTagModelBuilder();
	private boolean force;
	
	public static void setPendingAddressChangeEvent(HttpSession session){
		session.setAttribute(PENDING_ADDRESS_CHANGE_EVENT, true);
	}
	
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
	protected String getFunctionName() {
		return "cmCreateRegistrationTag";
	}
	
	@Override
	public String getTagJs() throws SkipTagException {

		HttpSession session = getSession();
		boolean pendingRegistration = Boolean.TRUE.equals(session.getAttribute(PENDING_REGISTRATION_EVENT));
		boolean pendingLogin = Boolean.TRUE.equals(session.getAttribute(PENDING_LOGIN_EVENT));
		boolean pendingAddressChange = Boolean.TRUE.equals(session.getAttribute(PENDING_ADDRESS_CHANGE_EVENT));

		if (force || pendingRegistration || pendingLogin || pendingAddressChange){
			
			session.removeAttribute(PENDING_REGISTRATION_EVENT);
			session.removeAttribute(PENDING_LOGIN_EVENT);
			session.removeAttribute(PENDING_ADDRESS_CHANGE_EVENT);
			
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
			tagModelBuilder.setUser(user);
			
			tagModelBuilder.setSocialUserProfile((HashMap<String, String>) session.getAttribute(SessionName.SOCIAL_USER));
			
			
			String registrationLocation = (String)session.getAttribute(REGISTRATION_LOCATION); 
			session.removeAttribute(REGISTRATION_LOCATION);
			tagModelBuilder.setLocation(registrationLocation);
			
			tagModelBuilder.setOrigZipCode((String)session.getAttribute(REGISTRATION_ORIG_ZIP_CODE));
			session.removeAttribute(REGISTRATION_ORIG_ZIP_CODE);
			
			if (pendingAddressChange){
				tagModelBuilder.setAddress(user.getSelectedAddress());
			}
			
			RegistrationTagModel regModel = tagModelBuilder.buildTagModel();
			LOGGER.debug("cmregistration values @ @@ @ @@"+regModel.getRegistrationProfileValue());
			LOGGER.debug("cmregistration COunty @ @@ @ @@"+regModel.getRegistrationCounty());

			String tagJs = getFormattedTag(
					toJsVar(regModel.getRegistrationId()),
					toJsVar(regModel.getRegistrantEmail()),
					toJsVar(regModel.getRegistrantCity()),
					toJsVar(regModel.getRegistrantState()),
					toJsVar(regModel.getRegistrantPostalCode()),
					toJsVar(regModel.getRegistrantCountry()),					
					toJsVar(mapToAttrString(regModel.getAttributesMaps())));
			LOGGER.debug("first tagjs"+tagJs);
			
			//append registration conversion event to tagJs
			if (pendingRegistration){
				CmConversionEventTag regConversionEventTag = new CmConversionEventTag();
				regConversionEventTag.setSession(getSession());
				regConversionEventTag.setReturnAsJson(returnAsJson);
				regConversionEventTag.setOnlyFormatModel(true);
				regConversionEventTag.setEventId(registrationLocation);
				regConversionEventTag.setCategoryId(ConversionEventTagModelBuilder.CAT_REGISTRATION);
				regConversionEventTag.setZipCode(regModel.getRegistrantPostalCode());
				regConversionEventTag.setParamValue(regModel.getRegistrationProfileValue());
				regConversionEventTag.setParamCounty(regModel.getRegistrationCounty());
				tagJs += getTagDelimiter() + regConversionEventTag.getTagOutput();
				
				LOGGER.debug("second tagjs"+tagJs);
			} 

			//append login conversion event to tagJs
			if (pendingRegistration || pendingLogin){
				CmConversionEventTag logConversionEventTag = new CmConversionEventTag();
				logConversionEventTag.setSession(getSession());
				logConversionEventTag.setReturnAsJson(returnAsJson);
				logConversionEventTag.setOnlyFormatModel(true);
				logConversionEventTag.setEventId(ConversionEventTagModelBuilder.EVENT_LOGIN);
				logConversionEventTag.setCategoryId(ConversionEventTagModelBuilder.CAT_LOGIN);
				logConversionEventTag.setUser(user);
				
				tagJs += getTagDelimiter() + logConversionEventTag.getTagOutput();
				LOGGER.debug("third tagjs"+tagJs);
				
			}
			
			LOGGER.debug(tagJs);
			return tagJs;

		} else {
			throw new SkipTagException("There is no pending event and force is false");
		}		
	}

	public void setForce(boolean force) {
		this.force = force;
	}
	
	public void setEmail(String email) {
		tagModelBuilder.setEmail(email);
	}
	
	public void setAddress(AddressModel address){
		tagModelBuilder.setAddress(address);
	}
	
	
	protected boolean insertTagInCaseOfCrmContext(){
		return true;
	}
}