/*
 * Copyright 2007 AOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.freshdirect.webapp.oauth.provider;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.oauth.provider.OAuthProvider;
import com.freshdirect.fdstore.survey.EnumSurveyType;
import com.freshdirect.fdstore.survey.FDSurvey;
import com.freshdirect.fdstore.survey.FDSurveyConstants;
import com.freshdirect.fdstore.survey.FDSurveyFactory;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.StoreContextUtil;

/**
 * A servlet to return user data
 *
 * @author Tamas Gelesz
 */
public class UserDataServlet extends HttpServlet {

	private static final long serialVersionUID = -1135796179626623531L;
	private static Logger LOGGER = LoggerFactory.getInstance(UserDataServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, OAuthProvider.getProviderUrl()+request.getServletPath());
            OAuthAccessor accessor = OAuthProvider.getAccessor(requestMessage);
            OAuthProvider.validateMessage(requestMessage, accessor);
            String userId = (String) accessor.getProperty("user");
            StoreContext storeContext =StoreContextUtil.getStoreContext(request.getSession());            
            FDUser fdUser = com.freshdirect.fdstore.customer.FDCustomerManager.getFDUserByEmail(userId, storeContext.getEStoreId());
            
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println(String.format(
            		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
            		"<user>\n" + 
            		"	<id>%1$s</id>\n" + 
            		"	<name>%2$s</name>\n" +
            		"	<screen_name>%2$s</screen_name>\n" +
            		"	<avatar_url>%3$s</avatar_url>\n" +
            		"</user>", fdUser.getUserId(), FDCustomerFactory.getErpCustomerInfo(fdUser.getIdentity()).getDisplayName(), getUserProfileImageUrl(fdUser)));
            out.close();
            
        } catch (Exception e){
            OAuthProvider.handleException(e, request, response, false);
        }
    }
    
    private String getUserProfileImageUrl(FDUserI user){

		String baseUrl = OAuthProvider.getProviderUrl() + "/media_stat/images/profile/";
    	String profileImageUrl = baseUrl + "unset.jpg";
    	
    	EnumServiceType serviceType = FDSurveyFactory.getServiceType(user, (EnumServiceType)null);
		try {
			FDSurvey customerProfileSurvey = FDSurveyFactory.getInstance().getSurvey(EnumSurveyType.CUSTOMER_PROFILE_SURVEY, serviceType);
			FDSurveyResponse surveyResponse= FDSurveyFactory.getCustomerProfileSurveyInfo(user.getIdentity(), serviceType);
			
			if (customerProfileSurvey != null) {
				if (surveyResponse != null && surveyResponse.getAnswer(FDSurveyConstants.PROFILE) != null) {
					String profileImageAnswer = surveyResponse.getAnswer(FDSurveyConstants.PROFILE)[0].toLowerCase();
					
					if (!"".equals(profileImageAnswer)){
						profileImageUrl = baseUrl + profileImageAnswer + ".jpg";
					}
				} 
			}
		} catch (FDResourceException e) {
			LOGGER.error("Error in profile image resolution", e);
			
		}
		return profileImageUrl;
    }
}
