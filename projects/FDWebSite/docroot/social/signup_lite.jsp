<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.enums.CaptchaType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.AddressModel"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.mail.EmailUtil"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.CaptchaUtil' %>
<%@ page import='com.freshdirect.webapp.util.FDURLUtil'%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>

<fd:CheckLoginStatus/>
<% 
	boolean showAntsFields = FDStoreProperties.isLightSignupAntsEnabled();
	String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
	String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	
	String successPage = request.getParameter("successPage");
	if (successPage == null) { 
	    successPage = "index.jsp";	
	}
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
	//System.out.println("\n\n\n"+user.getSelectedServiceType().getName()+"\n\n\n");
	if("".equals(serviceType)) {
		if(user != null) {
			serviceType = user.getSelectedServiceType().getName();
			if("PICKUP".equals(user.getSelectedServiceType().getName()))
				serviceType = "HOME";
		} else {
			serviceType = "HOME";
		}
	}
	  
	boolean isCorporate = "CORPORATE".equalsIgnoreCase(serviceType);
	
	String failurePage = "/social/signup_lite.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;
      
	// determine the preSuccessPage from previous workflow
	String preSuccessPage = (String)request.getParameter("preSuccessPage");
	if(preSuccessPage != null && preSuccessPage.length()>0 ){
		session.setAttribute(SessionName.PREV_SUCCESS_PAGE, preSuccessPage);
    }

	String template = "/common/template/no_nav_html5.jsp";

	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
		request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/social/signup_lite.jsp"); //change for OAS
	}
	
	//Captcha.
	boolean showCaptcha = CaptchaUtil.isExcessiveAttempt(FDStoreProperties.getMaxInvalidLoginAttempt(), session, SessionName.SIGNUP_ATTEMPT);
	String publicKey= FDStoreProperties.getRecaptchaPublicKey(CaptchaType.SIGN_UP);
	    
%>



<fd:SiteAccessController action='expressSignup' successPage='<%= successPage %>' moreInfoPage='' failureHomePage='<%= failurePage %>' result='result'>

	<% if (mobWeb) { %>
		<%-- TEMP FIX FOR IOS 11 --%>
		<style>.mm-page-ifr.login-ajax-overlay { position:absolute; }</style>
	<% } %>
	
	<% if (session.getAttribute("morepage") != null) {
		String mPage = (String) session.getAttribute("morepage");
	%>
		<jsp:include page="<%= mPage %>" flush="false" />
	<% } else { %>
		<%
		
		session.setAttribute("lastpage","signup_lite");
		
		String email="",firstname="",lastname="",repeat_email="",password="",passwordhint="",zipcode="",posn="", workphone="", companyName="";
		
		if(request.getParameter("is_forwarded") != null) {
			HashMap userProp = (HashMap)session.getAttribute("userProp");
			email = (String)userProp.get("email");
			
			String displayName = (String)userProp.get("displayName"); 
			String names[] = displayName.split(" ");
			firstname = names[0];
			lastname = names[1];
			password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
			passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");	
			zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	
			posn = "right";
		} else {
	    	email = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "");
			firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
			lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
			password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
			passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");	
			zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	
			posn = "right";
	    }
		
		workphone = NVL.apply(request.getParameter(EnumUserInfoName.DLV_WORK_PHONE.getCode()), "");
		companyName = NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "");
	
				
		/*  out.println("LITESIGNUP_COMPLETE:"+session.getAttribute("LITESIGNUP_COMPLETE")); */
		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {
		%>
			<div style="width:500px;">
				<img src="/media_stat/images/navigation/spinner.gif" alt="spinner" class="fleft" />
			</div>
			<script language="javascript">
				window.top.location="/index.jsp";
			</script>
		<% } else {
			//System.out.println("went to else part  on signup_lite.jsp?====================================================================================\n" );
			
			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}
			
		%><% } %>
		<!-- email validation --> 
		<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
			<script type="text/javascript">
				$jq(function(){
					$jq("#email_img").addClass("show_bg_arrow");
					$jq("#email").addClass("error");						
					$jq("#email").css({ 'display': 'block' });
					$jq("#password1").css({ 'display': 'block' });
					$jq("#signupbtn").css({ 'display': 'block' });
					$jq("#emailbtn").css({ 'display': 'none' });
				});
			</script>
		<% } %>	
		<!-- end email validation -->

		
		
		<div id="sulCont" class="signup-style-social social-singup">
			<div class="social-login-spinner hidden">
				<img src="/media_stat/images/navigation/spinner.gif"
					alt="spinner" class="fleft" />
			</div>
			<div id="signup-success" class="signin-social-account-create-success hidden" data-signup-success="false">
				<div class="form-side-social-header">Congratulations!</div>
				<div class="signin-social-forgot-pass-header-message">Your account has been created!</div>
				<center>
					<button onclick="close_window_new_account()" class="social-login-green-button" id="social-login-green-button_begin-shopping">Begin Shopping</button>
				</center>
			</div>
			<div id="signup-content">
				<div class="form-side-social">
					<div class="form-side-social-header">Create Account:</div>
					<div class="bottom-links">
						Already have an account? <a id="signin-link" href="/social/login.jsp?successPage=<%=successPage%>" data-hasevent="false">Sign In</a>
					</div>
					<!-- form_fields start here -->
					<div id="form_feilds" style="text-align: center;">
						<form id="litesignup" name="litesignup" method="post" autocomplete="off" autocomplete="false">
							<input type="hidden" name="submission" value="done" />
							<input type="hidden" name="actionName" value="ordermobilepref" /> 
							<input type="hidden" name="successPage" id="success-target" value="<%= successPage %>" />
							<input type="hidden" name="terms" value="true" />
							<input type="hidden" name="LITESIGNUP" value="true" />
							<input type="hidden" name="EXPRESSSIGNUP_SKIP_VALIDATION" value="true" />
							
							<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
								<div class="errMsg">
									<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'>
												<span class="errortext"><%=errorMsg%></span>
									</fd:ErrorHandler>
								</div>
							<% } %>

							<div class='error-message hidden'></div>

							<% if (showAntsFields) { %>
								<div>
									<fieldset>	
				                   		<div id="sul_type_fields" aria-controls="collapsible-sul_cos_fields" aria-expanded="false">
					                 		<span class="legend"><legend>Delivery For:</legend></span>
					                 		<input type="radio" name="serviceType" id="sul_type_fields_HOME" value="HOME" <%= (	serviceType.equals("HOME"))?"checked":"" %> tabindex="0" /><label for="sul_type_fields_HOME"><span>Home</span><span class="offscreen"> Delivery</span></label><input type="radio" name="serviceType" id="sul_type_fields_CORPORATE" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %> tabindex="0" /><label for="sul_type_fields_CORPORATE"><span>Business or School</span><span class="offscreen"> Delivery</span></label>
				                   		</div>
				                 	</fieldset>
								</div>
							<% } %>
						    <div id="collapsible-sul_cos_fields" class="signup-form">
								<% if (showAntsFields) { %>
									<%-- ANTS data --%>
									<fieldset>
			                    		<div class="signup-form-row" id="sul_cos_fields" style='<%= (serviceType.equals("HOME"))?"display:none":"" %>' aria-hidden="true">
			                    			<div class="form-element">
			                    				<span class="error_img" id="companyName_img"></span>
		                    					<div id="companyName_msg" class="error error-msg"></div>
		                    					<input type="text" name="<%= EnumUserInfoName.DLV_COMPANY_NAME.getCode() %>" id="companyName" value="<%= companyName %>" class="padding-input-box text11ref inputDef required" aria-label="company name" placeholder="Company Name" maxlength="120" />
			                    			</div>
			                    			<div class="form-element">
			                    				<span class="error_img" id="sul_cos_fields-firstName_img"></span>
		                    					<div id="firstName_msg" class="error error-msg"></div>
		                    					<input type="text" name="<%= EnumUserInfoName.DLV_FIRST_NAME.getCode() %>" id="firstName" value="<%= firstname %>" class="padding-input-box text11ref inputDef required" aria-label="first name" placeholder="First Name" maxlength="25" />
			                    			</div>
			                    			<div class="form-element">
			                    				<span class="error_img" id="sul_cos_fields-lastName_img"></span>
		                    					<div id="lastName_msg" class="error error-msg"></div>
		                    					<input type="text" name="<%= EnumUserInfoName.DLV_LAST_NAME.getCode() %>" id="lastName" value="<%= lastname %>" class="padding-input-box text11ref inputDef required" aria-label="last name" placeholder="Last Name" maxlength="25"  />
			                    			</div>
			                    			<div class="form-element">
			                    				<span class="error_img" id="sul_cos_fields-workPhone_img"></span>
		                    					<div id="workPhone_msg" class="error error-msg"></div>
		                    					<input type="text" name="<%= EnumUserInfoName.DLV_WORK_PHONE.getCode() %>" id="workPhone" value="<%= workphone %>" class="padding-input-box text11ref inputDef" aria-label="work phone" placeholder="Work Phone #" minlength="10" maxlength="14" />
			                    			</div>
			                    			<div class="form-element">
			                    				<span class="error_img" id="sul_cos_fields-zipCode_img"></span>
		                    					<div id="zipCode_msg" class="error error-msg"></div>
		                    					<input type="text" name="<%= EnumUserInfoName.DLV_ZIPCODE.getCode() %>" id="zipCode" value="<%= zipcode %>" class="padding-input-box text11ref inputDef required" aria-label="zip code" placeholder="Zip Code" maxlength="5" />
			                    			</div>
			                    		</div>
				                    </fieldset>
								<% } %>
			                    
								<fieldset>
									<div class="signup-form-row">
										<div class="form-element">
											<span class="error_img" id="email_img"></span>
											<input class="padding-input-box text11ref inputDef required" style="display:block;" 
											aria-label="email" type="text" maxlength="128" size="23" 
											name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" 
											id="email" placeholder="E-mail" autocomplete="off" autocomplete="false">
										</div>
										<div class="form-element">

				                    <% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %>
				                    <fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="errortext"><%=errorMsg%></span>
				                    	</fd:ErrorHandler><% } %>
				                            <label for="prevent_autofill"><span class="offscreen">prevent autofill</span></label>
											<input type="text" name="prevent_autofill" id="prevent_autofill" value="" style="display:none;" />
											<label for="password_fake"><span class="offscreen">password fake</span></label>
											<input type="password" name="password_fake" id="password_fake" value="" style="display:none;" />
										
									
											<span class="error_img" id="password1_img"></span>
										<!--  Added for Password Strength Display -->
											<div class="content-group password">
												<div class="subgroup">
													<div class="password-hinter">
														<div class="password-instructions">
															<ul>
																<li id="pw-length" class="invalid"><strong>6</strong> or more characters <strong>(Required)</strong></li>
																<li class="subhead"><strong>Make your password stronger with:</strong></li>
																<li id="pw-letter" class="invalid"><strong>1</strong> or more letters</li>
																<li id="pw-number" class="invalid"><strong>1</strong> or more numbers</li>
																<li id="pw-capital" class="invalid"><strong>1</strong> or more capital letters</li>
																<li id="pw-special" class="invalid"><strong>1</strong> or more special characters</li>
															</ul>
														</div>
													</div><!-- // .password-hinter -->
													<div>
														<input class="padding-input-box text11ref inputUser required" type="password" data-indicator="pwindicator" aria-label="password" size="23" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1" placeholder="Password" autocomplete="off" autocomplete="false">
														<div style="position: relative"><a class="show-password">Show</a></div>
													</div>
													<div id="pwindicator">
													   <div class="bar"></div>
													   <div class="label"></div>
													</div>
													
												</div><!-- // .subgroup -->			
											</div><!-- // .content-group -->
										<!-- Added for Password Strength Display -->
										</div>
									</div>
								</fieldset>
			
								<jwr:script src="/assets/javascript/fd/captcha/captchaWidget.js" useRandomParam="false" />
								
								<div class="signup-form-row" id="sign-up-g-recaptcha-container" data-captcha="<%=publicKey%>" data-captcha-init="<%=showCaptcha%>">
									<td>&nbsp;</td>
									<td align="center">
										<div id="sign-up-g-recaptcha" class="g-recaptcha"></div>
									</td>
								</div>
									
								
								<div class="signup-form-row">
									<div class="bottom-contents">
										<div class="bottom-contents-terms-text">
											By creating an account, you agree to the FreshDirect<br /> <a href="javascript:popup('/registration/user_agreement.jsp', 'large')">Customer Agreement</a> & <a href="javascript:popup('/registration/privacy_policy.jsp', 'large')">Privacy Policy</a>
										</div>
									</div>
									<input type="submit" id="signupbtn" maxlength="25" size="19" value="Create Account" class="butText social-login-continue-button" style="display:block;"disabled>
								</div>
							
							</div>
						</form>							
					</div>

					<!-- form_fields ends here -->
				</div>
				
				
				<% if (mobWeb) { /* TODO: this design could replace the old ver, but for safety, leaving both for now */ %>
					<div class="social-login-or-separator-cont-v2">
						<div class="social-login-line-separator-v2"></div>
						<div class="social-login-or-separator-v2">or</div>
					</div>
				<% } else { %>
					<div class="social-login-headerscr-social">
						<div class="social-login-line-separator"></div>
						<span class="social-login-or-separator">or</span>
						<div class="social-login-line-separator"></div>
					</div>
				<% } %>
				
								
				<div id="social_login_demo" class="social-login-social">
					<script type="text/javascript">
						/* Replace the subdomain with your own subdomain from a Site in your OneAll account */
						var oneall_subdomain = '<%=site_subdomain%>';
					
						/* Asynchronously load the library */
						var oa = document.createElement('script');
						oa.type = 'text/javascript';
						oa.async = true;
						oa.src = '//' + oneall_subdomain
								+ '<%=site_post_url%>/socialize/library.js';
						var s = document.getElementsByTagName('script')[0];
						s.parentNode.insertBefore(oa, s);
					
						/* This is an event */
						var my_on_login_redirect = function(args) {
							return true;
						}
					
						/* Initialise the asynchronous queue */
						var _oneall = _oneall || [];
						_oneall.push([ 'social_login', 'set_force_re_authentication', true]);
				    	_oneall.push([ 'social_login', 'set_providers',[ 'facebook', 'google' ] ]);
						_oneall.push([ 'social_login', 'set_grid_sizes', [ 1, 2 ] ]);
						_oneall.push([ 'social_login', 'set_custom_css_uri', '//'+window.location.host+'/media/social_login/social_login_media.css']);
						
						<% if(FDStoreProperties.isLocalDeployment()){ %>
							_oneall.push([ 'social_login', 'set_callback_uri', '<%= "http://" + request.getServerName()  + ":" + request.getServerPort() + "/social/social_login_success.jsp?successPage=" + successPage %>' ]);
						<% } else { %>
							_oneall.push([ 'social_login', 'set_callback_uri', '<%= "https://" + request.getServerName() + "/social/social_login_success.jsp?successPage=" + successPage %>' ]);
						<% } %>
						
						_oneall.push([ 'social_login', 'set_event','on_login_redirect', my_on_login_redirect ]);
						_oneall.push([ 'social_login', 'do_render_ui','social_login_demo' ]);
						
						$jq( document ).ready(function() {
							setTimeout(function(){
								if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) {
									FreshDirect.components.ifrPopup.reposition();
								}
							}, 100);
						});
					</script>
				</div>
				
				<div class="clear"></div>
				
				<% if (result.isFailure()) { %>
					<script type="text/javascript">
						setFrameHeightSL('signupframe', $jq('#sulCont').height());
						if (window.top['Modalbox_setWidthAndPosition']) {
							window.top.Modalbox_setWidthAndPosition();
						}
					</script>
				<% } %>
			</div>
		</div>
	<% } %>

	<fd:javascript src="/assets/javascript/jquery.hint.js"/>
	<fd:javascript src="/assets/javascript/jquery.pwstrength.js"/>
	<fd:javascript src="/assets/javascript/scripts.js"/>

	
	<!-- submit button disable function -->																
	<script type="text/javascript" language="javascript">
		$jq(function(){
			// Hide all the error arrow images
			$jq('.error_img').hide();
 			// Hide the password button
 			if ($jq('#litesignup #password1').val().length === 0) {
                $jq('.show-password').hide();
            }
			
			// disable the submit button on page load
			// on keyup we will check every time the form is valid or not
		});
	</script >			
				
	<script>
		if ($jq('#signin-link[data-hasevent="false"]').length) {
			$jq('#signin-link').on('click', function(event){									
				event.preventDefault();
				$jq(this).attr('data-hasevent', 'true');
				var fd = window.FreshDirect;
				fd.components.ajaxPopup.close();
				 
				//if we're on the signup page already, exit here
				if (window.location.pathname === '/social/signup_lite.jsp') { 
					return;
				}
				
				if (fd.mobWeb) { /* send user to page instead of popup */
				    var API = $jq("#nav-menu").data("mmenu");
					window.top.location = '/social/login.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;
					if (API) {
						API.close();
					}
				  } else {
					    fd.modules.common.login.socialLogin('<%=successPage%>');
				  }
			});
		}
	</script>
	<script type="text/javascript">
		$jq('#password1').pwstrength();
	</script>
</fd:SiteAccessController> 


