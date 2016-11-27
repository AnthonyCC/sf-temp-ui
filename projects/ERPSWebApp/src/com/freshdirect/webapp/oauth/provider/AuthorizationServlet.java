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
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.oauth.provider.OAuthProvider;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * Autherization request handler.
 *
 * @author Praveen Alavilli, customized by Tamas Gelesz
 */
public class AuthorizationServlet extends HttpServlet {

	private static final long serialVersionUID = 2531406606049547691L;

	@SuppressWarnings("deprecation")
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, OAuthProvider.getProviderUrl()+request.getServletPath());
            
            OAuthAccessor accessor = OAuthProvider.getAccessor(requestMessage);
           
            if (Boolean.TRUE.equals(accessor.getProperty("authorized"))) {
                // already authorized send the user back
                returnToConsumer(request, response, accessor, false);
            
            } else { 
            	FDSessionUser user = (FDSessionUser)request.getSession().getAttribute(SessionName.USER);
            	if (user == null || user.getUserId() == null || user.getLevel() < FDUserI.SIGNED_IN){

            		if("true".equalsIgnoreCase(request.getParameter("autoauth"))){
                        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
                        response.setHeader("Location", request.getParameter("oauth_callback"));
            			return;
            		}
            		
            		StringBuffer redirBuf = new StringBuffer("/login/login_main.jsp?successPage=");
					redirBuf.append(request.getRequestURI());
					
					String requestQryString = request.getQueryString();
					
					if ((requestQryString != null) && (requestQryString.trim().length() > 0)) {
					    redirBuf.append(URLEncoder.encode("?" + request.getQueryString()));
					}
					
					response.sendRedirect(response.encodeRedirectURL(redirBuf.toString()));

            	} else {
            		
            		//authorization fails if display name is not set - disables commenting 
            		String displayName = FDCustomerFactory.getErpCustomerInfo(user.getIdentity()).getDisplayName();

            		if (displayName == null || displayName.length() == 0){
                        returnToConsumer(request, response, accessor, true);
            			
            		} else {
                		accessor = OAuthProvider.markAsAuthorized(accessor, user.getUserId());
                        returnToConsumer(request, response, accessor, false);
            		}
            		
            	}
            }
        
        } catch (Exception e){
            OAuthProvider.handleException(e, request, response, true);
        }
    }
    
    @Override 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
    	doGet(request, response);
    }
    
    private void returnToConsumer(HttpServletRequest request, HttpServletResponse response, OAuthAccessor accessor, boolean noDisplayName) throws IOException, ServletException{

    	// send the user back to site's callBackUrl
        String callback = request.getParameter("oauth_callback");
        
        if("none".equals(callback) && accessor.consumer.callbackURL != null && accessor.consumer.callbackURL.length() > 0){
            // first check if we have something in our properties file
            callback = accessor.consumer.callbackURL;
        }
        
        if( "none".equals(callback) ) {
            // no call back it must be a client
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("You have successfully authorized '" + accessor.consumer.getProperty("description") + "'. Please close this browser window and click continue" + " in the client.");
            out.close();

        } else {
            // if callback is not passed in, use the callback from config
            if(callback == null || callback.length() <=0 )
                callback = accessor.consumer.callbackURL;
            String token = accessor.requestToken;
            if (token != null) {
                callback = OAuth.addParameters(callback, "oauth_token", token);
                if (noDisplayName){
                	callback += "&nodisplayname";
                }
            }

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", callback);
        }
    }
}
