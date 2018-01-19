<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.service.OAuth2Service" %>
<%@ page import="com.freshdirect.webapp.ajax.oauth2.util.ClientDataValidator" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<%-- Validation--%>
<% 
	String errMsg = null;
	String clientId = request.getParameter("client_id");
	String redirectUri = request.getParameter("redirect_uri");
	String responseType = request.getParameter("response_type");
	if (responseType == null || responseType.isEmpty()){
		errMsg = "Response type is required";
	}else if (clientId == null || clientId.isEmpty() || !ClientDataValidator.validateClientId(clientId)){
		errMsg = "The application is not registered";
	} else if (redirectUri == null || redirectUri.isEmpty() || !ClientDataValidator.validateClientRedirectUri(clientId, redirectUri)) {
		errMsg = "The redirect_uri does not match the registered value";
	}
	
	if (errMsg != null) {

%>

<tmpl:insert template="/common/template/oAuth.jsp">
	<tmpl:put name="content">
		<div class="error-container">
			<span class="error-message"><%=errMsg %></span>
		</div>
	</tmpl:put>
</tmpl:insert>
<%-- Passed Validation--%>
<%
	} else {
		String encodedUrlQuery = request.getQueryString() != null? (URLEncoder.encode("?requireDecode=false&" + request.getQueryString(), "UTF-8")): ""; 
		pageContext.setAttribute("encodedUrlQuery", encodedUrlQuery);
	
%>
	<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage="/login/login.jsp?successPage=/oauth/auth.jsp${encodedUrlQuery}" />
	<%
		OAuth2Service authService = OAuth2Service.defaultService();
		
	%>
	<tmpl:insert template="/common/template/oAuth.jsp">
		<tmpl:put name="content">
			<div class="permisson-container">
				<div class="hidden">
					<input id="client-id" type="hidden" value="${param.client_id}" />
					<input id="redirect_uri" type="hidden" value="${param.redirect_uri}" />
					<input id="state" type="hidden" value="${param.state }" />
				</div>
				<div class="description">
					<fd:IncludeMedia name="/media/ouath/vendor_premission_details.ftl">
						<h2 class="header"><strong class="app-name">StorePower</strong> will use the info below under its privacy policy:</h2>
						<ul>
							<li>Read User Info</li>
							<li>Update Shopping Cart</li>
						</ul>
					</fd:IncludeMedia>
				</div>
				<div class="action">
					<button id="accept-permission" class="green cssbutton">Allow</button>
					<button id="decline-permission" class="grey cssbutton">Cancel</button>
				</div>
			</div>
		</tmpl:put>
	</tmpl:insert>

<% } %>