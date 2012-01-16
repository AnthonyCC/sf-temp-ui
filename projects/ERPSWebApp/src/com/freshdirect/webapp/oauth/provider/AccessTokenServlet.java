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
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.oauth.provider.OAuthProvider;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.server.OAuthServlet;

/**
 * Access Token request handler
 *
 * @author Praveen Alavilli, customized by Tamas Gelesz
 */
public class AccessTokenServlet extends HttpServlet {

	private static final long serialVersionUID = 7892158377685872565L;

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }
        
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, OAuthProvider.getProviderUrl()+request.getServletPath());
            
            OAuthAccessor accessor = OAuthProvider.getAccessor(requestMessage);
            OAuthProvider.validateMessage(requestMessage, accessor);
            
            // make sure token is authorized
            Object authorizedProperty = accessor.getProperty("authorized");
            if (authorizedProperty == null || !"true".equalsIgnoreCase(authorizedProperty.toString())) {
                 OAuthProblemException problem = new OAuthProblemException("permission_denied");
                throw problem;
            }
            // generate access token and secret
            accessor = OAuthProvider.generateAccessToken(accessor);
            
            response.setContentType("text/plain");
            OutputStream out = response.getOutputStream();
            OAuth.formEncode(OAuth.newList("oauth_token", accessor.accessToken, "oauth_token_secret", accessor.tokenSecret), out);
            out.close();
            
        } catch (Exception e){
            OAuthProvider.handleException(e, request, response, true);
        }
    }
}
