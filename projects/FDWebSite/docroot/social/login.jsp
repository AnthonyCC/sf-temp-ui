<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.List"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>

<fd:CheckLoginStatus id="user" guestAllowed='true'
	recognizedAllowed='true' />

<%
	String uri = request.getRequestURI();
	String successPage = request.getParameter("successPage");
	if ((successPage == null) || (successPage == "undefined") || (successPage == "ndefined")) {

		successPage = "/index.jsp";
	}

	String userid = NVL.apply(request.getParameter("userid"), NVL.apply(user.getUserId(), ""));

	String triedToConnect = request.getParameter("triedToConnect");

	// determine the preSuccessPage from previous workflow
	String preSuccessPage = (String) request.getParameter("preSuccessPage");
	if (preSuccessPage != null && preSuccessPage.length() > 0)
		session.setAttribute(SessionName.PREV_SUCCESS_PAGE, preSuccessPage);
%>

<fd:LoginController successPage="<%=successPage%>"
	mergePage="/login/merge_cart.jsp" result='result'>
	<%!//String[] checkLoginForm = {"userid", "email_format", "password"};
	String[] checkErrorType = { "authentication", "technical_difficulty" };%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>'
		id='errorMsg'>

	</fd:ErrorHandler>



	<div class="social_login_jsp">
		<center>
			<div id="sulCont" class="signup-style-social social-singin">

				<div class="form-side-social">
					<div class="form-side-social-header">Sign in with email:</div>

					<div id="form_feilds">
						<form name="fd_login" id="fd_login" method="post">
							<%
								if (!result.isSuccess()) {
										String errorMsg = "<div class='error-message'>Email and password do not match.</br>Please try again.</div>";
										String isSocialLoginOnlyUser = (String) session.getAttribute("IS_SOCIAL_LOGIN_USER_VALIDATION");
										String connectedProviders = (String) session.getAttribute("CONNECTED_SOCIAL_PROVIDERS");
										if (isSocialLoginOnlyUser != null && isSocialLoginOnlyUser.length() > 0) {
											session.setAttribute("IS_SOCIAL_LOGIN_USER_VALIDATION", null);
											session.setAttribute("CONNECTED_SOCIAL_PROVIDERS", null);
											errorMsg = "<div class='error-message'>User Name & Password do not match</br>Please try again or Sign in with "
													+ connectedProviders + ".</div>";
										}
										out.println(errorMsg);
									}
							%>
							<div class='error-message hidden'>
								Email and password do not match.</br>Please try again.
							</div>
							<div class="social-login-spinner hidden">
								<img src="/media_stat/images/navigation/spinner.gif"
									class="fleft" />
							</div>
							<table border="0" cellpadding="5" cellspacing="8">
								<tr>
									<td valign="bottom">
										<!-- span id should be the input box id+"_img" --> <span
										class="error_img" id="email_img"></span>
									</td>
									<%-- This field is named as 'userid' to be consistent with naming rule in LoginControllerTag. The email validation rule is applied to this field as shown below.  --%>
									<td><input id="email" name="userid"
										class="padding-input-box text11ref inputDef required"
										type="email" maxlength="128" size="23" value="<%=userid%>"
										placeholder="E-mail" autofocus autocomplete="email"></td>
								</tr>
								<tr>
									<td valign="bottom">
										<!-- span id should be the input box id+"_img" --> <span
										class="error_img" id="password_img"></span>
									</td>
									<td style="padding-top: 15px;"><input id="password"
										name="password"
										class="padding-input-box text11ref inputDef required"
										type="password" size="23" placeholder="Password"
										autocomplete="email"></td>
								</tr>
								<tr>
									<td></td>
									<td style="padding-top: 15px;"><input
											type="submit" id="signinbtn" class="button_disabled"
											maxlength="25" size="19" value="Sign in" disabled />
									</td>
								</tr>
								<tr>
									<td></td>
									<td class="social-login-pass-forgot">
										<a href="javascript:void(0)"
											onclick="window.FreshDirect.components.ajaxPopup.close(); window.FreshDirect.components.ifrPopup.open({ url: '/social/forgot_password.jsp', height: 590, opacity: .5});">
											Forgot Password?
											</a>
									</td>
								</tr>
							</table>
						</form>
					</div>
					<!-- form_fields ends here -->



				</div>
				<!--  form-side ends here -->
			<% if (FDStoreProperties.isSocialLoginEnabled()) 
			{

				String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
				String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();

			%>
				<div class="social-login-headerscr-social">
					<div class="social-login-line-separator"></div>
					<span class="social-login-or-separator">or</span>
					<div class="social-login-line-separator"></div>
				</div>


				<div id="social_login_demo" class="social-login-social">
					<input type="hidden" id="social-login-callback-uri" 
					value="<%=FDStoreProperties.isLocalDeployment()?"//" + request.getServerName() + ":" + request.getServerPort() + "/social/social_login_success.jsp" : "" %>" />
					<script type="text/javascript" async
						src="//<%=site_subdomain%><%=site_post_url%>/socialize/library.js"></script>

				</div>
				<% }
				%>
				<!-- social login section ends here -->



				<div class="clear"></div>

				<div class="bottom-contents">
					<div class="bottom-links">
						New to FreshDirect? <a
							href="javascript:void(0)"
							onclick="window.FreshDirect.components.ajaxPopup.close(); window.FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp?successPage=<%=successPage%>', height: 590, opacity: .5});">
							Create Account </a>
					</div>
				</div>

			</div>
			<!-- container ends here -->
</fd:LoginController>
</center>


