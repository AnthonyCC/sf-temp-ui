<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.List"%>
<%@ page import="com.freshdirect.enums.CaptchaType" %>
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
<%@ page import='com.freshdirect.webapp.util.CaptchaUtil' %>
<%@ page import='com.freshdirect.webapp.util.FDURLUtil'%>

<fd:CheckLoginStatus id="user" guestAllowed='true'
	recognizedAllowed='true' />
<%
	String uri = request.getRequestURI();
	String successPage = request.getParameter("successPage");
	if (successPage == null) {
		successPage = "/index.jsp";
	}

	String userid = NVL.apply(request.getParameter("userid"), NVL.apply(user.getUserId(), ""));

	String triedToConnect = request.getParameter("triedToConnect");

	// determine the preSuccessPage from previous workflow
	String preSuccessPage = (String) request.getParameter("preSuccessPage");
	if (preSuccessPage != null && preSuccessPage.length() > 0){
		session.setAttribute(SessionName.PREV_SUCCESS_PAGE, preSuccessPage);
	}
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	
	//Captcha.
	boolean showCaptcha = CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidLoginAttempt(), session, SessionName.LOGIN_ATTEMPT);
	String publicKey= FDStoreProperties.getRecaptchaPublicKey(CaptchaType.SIGN_IN);
    
%>

<fd:LoginController successPage="<%=successPage%>"
	mergePage="/login/merge_cart.jsp" result='result'>
	<%!//String[] checkLoginForm = {"userid", "email_format", "password"};
	String[] checkErrorType = { "authentication", "technical_difficulty" };%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>'
		id='errorMsg'>

	</fd:ErrorHandler>

	<% if (mobWeb) { %>
		<%-- TEMP FIX FOR IOS 11 --%>
		<style>.mm-page-ifr.login-ajax-overlay { position:absolute; }</style>
	<% } %>


	<div class="social_login_jsp">
		<center>
			<div id="sulCont" class="signup-style-social social-singin">

				<div class="form-side-social">
					<div class="form-side-social-header">Sign in with email:</div>

					<div id="form_feilds">
						<form name="fd_login" id="fd_login" class="fd-login-overlay" method="post">
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
							<input type="hidden" id="success-target" value="<%=successPage%>" />
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
									<td><input id="email" name="userid" aria-label="enter your email"
										class="padding-input-box text11ref inputDef required"
										type="email" maxlength="128" size="23" value="<%=userid%>"
										placeholder="E-mail" autofocus autocomplete="email" tabindex="1" /></td>
								</tr>
								<tr>
									<td valign="bottom">
										<!-- span id should be the input box id+"_img" --> <span
										class="error_img" id="password_img"></span>
									</td>
									<td style="padding-top: 15px;"><input id="password"
										name="password" aria-label="enter your password"
										class="padding-input-box text11ref inputDef required"
										type="password" size="23" placeholder="Password"
										autocomplete="email" tabindex="2" /></td>
								</tr>
								
							<% if (showCaptcha) { %>
							<jwr:script src="/assets/javascript/fd/captcha/captchaWidget.js" useRandomParam="false" />
								<tr id="social-login-g-recaptcha-container">
									<td colspan="2">
										<div id="social-login-g-recaptcha" class="g-recaptcha"></div>
									</td>
									
								</tr>
								<script type="text/javascript">
								  FreshDirect.components.captchaWidget.init('social-login-g-recaptcha', '<%=publicKey%>', function() {
									  FreshDirect.components.loginForm.onCaptchaLoadCallback('<%=publicKey%>');  
								  });
								 
								</script>
							<% } %>
								<tr>
									<td></td>
									<td style="padding-top: 15px;">
										
											<input
												type="submit" id="signinbtn" class="button_disabled"
												maxlength="25" size="19" value="Sign in" tabindex="3" disabled/>
										
									</td>
								</tr>
								<tr>
									<td></td>
									<td class="social-login-pass-forgot">
										<a href="/social/forgot_password.jsp" tabindex="4"
											onclick="event.preventDefault();window.FreshDirect.components.ajaxPopup.close(); window.FreshDirect.components.ifrPopup.open({ url: '/social/forgot_password.jsp', height: 590, width:560, opacity: .5});">
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


				<div id="social_login_demo" class="social-login-social" tabindex="5">
					<input type="hidden" id="social-login-callback-uri" 
					value="<%=FDStoreProperties.isLocalDeployment()?"//" + request.getServerName() + ":" + request.getServerPort() + "/social/social_login_success.jsp?successPage=" + FDURLUtil.safeURLEncode(request.getParameter("successPage")): "" %>" />
					<script type="text/javascript">
				    	/* The library is loaded asynchronously */
					    var oa = document.createElement('script');
					    oa.type = 'text/javascript'; oa.async = true;
					    oa.src = '//<%=site_subdomain%><%=site_post_url%>/socialize/library.js';
					    var s = document.getElementById('social_login_demo');
					    s.appendChild(oa);
				         
				  	</script>
					
				</div>
				<% }
				%>
				<!-- social login section ends here -->



				<div class="clear"></div>

				<div class="bottom-contents">
					<div class="bottom-links">
						New to FreshDirect? <a id="social_login_signup_link" href="/social/signup_lite.jsp" tabindex="6" data-hasevent="false">Create Account</a>
							<script>
								if ($jq('#social_login_signup_link[data-hasevent="false"]').length) {
									$jq('#social_login_signup_link').on('click', function(event){									
										event.preventDefault();
										$jq(this).attr('data-hasevent', 'true');
										var fd = window.FreshDirect;
										fd.components.ajaxPopup.close();
										
										//if we're on the signup page already, exit here
										if (window.location.pathname === '/social/signup_lite.jsp') { return; }
										
										if (fd.mobWeb) { /* send user to page instead of popup */
											  var API = $jq("#nav-menu").data("mmenu");
												window.top.location = '/social/signup_lite.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
												if (API) {
													API.close();
												}
										  } else {
											    if (fd.components && fd.components.ifrPopup) {
											      fd.components.ifrPopup.open({ url: '/social/signup_lite.jsp?successPage=encodeURIComponent(<%=successPage%>)', height: 590, width:560, opacity: .5});
											    }
										  }
									});
								}
							</script>
					</div>
				</div>

			</div>
			<!-- container ends here -->
</center>
</fd:LoginController>