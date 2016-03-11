<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.AddressModel"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.mail.EmailUtil"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@ taglib uri="freshdirect" prefix="fd"%>

<fd:CheckLoginStatus />

<% 
	boolean showAntsFields = FDStoreProperties.isLightSignupAntsEnabled();
	String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
	String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
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
    
	CmRegistrationTag.setRegistrationLocation(session,"signup social");
      
	// determine the preSuccessPage from previous workflow
	String preSuccessPage = (String)request.getParameter("preSuccessPage");
	if(preSuccessPage != null && preSuccessPage.length()>0 )
		session.setAttribute(SessionName.PREV_SUCCESS_PAGE, preSuccessPage); 
      
%>

<fd:SiteAccessController action='expressSignup' successPage='<%= successPage %>' moreInfoPage='' failureHomePage='<%= failurePage %>' result='result'>	


	<%
		if(session.getAttribute("morepage") != null) {
			String mPage = (String) session.getAttribute("morepage");
	%>
	<jsp:include page="<%= mPage %>" flush="false" />
	<%
		} else {
	%>

<!DOCTYPE html>
<html>
<head>
<title>FreshDirect</title>

<%@ include file="/common/template/includes/i_javascripts.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
<script type="text/javascript">
        jQuery(function($jq) { $jq('#password1').pwstrength(); });
  	 </script>

    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->

<script type="text/javascript">
 
		/* Replace #your_subdomain# by the subdomain of a Site in your OneAll account */    
		var oneall_subdomain = '<%=site_subdomain%>';
 
		/* The library is loaded asynchronously */
		var oa = document.createElement('script');
		oa.type = 'text/javascript'; oa.async = true;
		oa.src = '//' + oneall_subdomain + '<%=site_post_url%>/socialize/library.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(oa, s);
       
</script>

<!-- submit button disable function -->																
<script type="text/javascript" language="javascript">
	$jq(function(){
		// Hide all the error arrow images
		//$jq('.error_img').hide();
		// Hide the password button
		$jq('.show-password').hide();
		// disable the submit button on page load
		$jq('#signupbtn').attr('disabled', true);
		// on keyup we will check every time the form is valid or not
		$jq('#litesignup').bind('change keyup', function() {
		    if($jq(this).validate().checkForm()) { // form is valid
        		$jq('#signupbtn').removeClass('button_disabled').attr('disabled', false);
        		$jq('.show-password').show();
    		} else { // form is invalid
        		$jq('#signupbtn').addClass('button_disabled').attr('disabled', true);
        		$jq('.show-password').hide();
    		}
		});
	});
</script >
	
	<script type="text/javascript" language="javascript">
		
		$jq(document).on("click",".show-password", function(){
			$jq("#password1").toggleClass("showpwd");
			if($jq("#password1").hasClass("showpwd"))
			{
				$jq("#password1").prop("type","text");
				$jq('.show-password').text('Hide');
			}
			else
			{
				$jq("#password1").prop("type","password");
				$jq('.show-password').text('Show');
			}
		 
		});
	
	</script>
	
	<script type="text/javascript" language="javascript">
	$jq(document).on("click","#password1", function(){

			
 });
	</script>
	



	<%@ include file="/shared/template/includes/i_head_end.jspf"%>
	
	<%@ include file="/shared/template/includes/i_body_start.jspf"%>
</head>

<body style="height: auto;">
	<center>

	<%
	
	session.setAttribute("lastpage","signup_lite");
	
	String email="",firstname="",lastname="",repeat_email="",password="",passwordhint="",zipcode="",posn="", workphone="", companyName="";
	
	if(request.getParameter("is_forwarded") != null)
	{
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
	
	}
    else
    {

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
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			</div>
			<script language="javascript">
				window.top.location="/index.jsp";
			</script>
			<%		 
		} else {
			//System.out.println("went to else part  on signup_lite.jsp?====================================================================================\n" );
			
			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
	
<!-- email validation --> 
			<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
					<script type="text/javascript">
					$jq(function(){
						$jq("#email_img").addClass("show_bg_arrow");
						$jq("#email").addClass("error");						
						document.getElementById("email").style.display='block';
			        	document.getElementById("password1").style.display='block';
			        	document.getElementById("signupbtn").style.display='block';
			        	document.getElementById("emailbtn").style.display='none';
					});
</script>
		<% } %>	
<!-- end email validation --> 

	<div id="sulCont" class="signup-style-social social-singup" style="min-width:523px">
		<div class="form-side-social">
		<div class="form-side-social-header">Create Account:</div>
		<div id="form_feilds">
			<form id="litesignup" name="litesignup" method="post" action="/social/signup_lite.jsp" autocomplete="off" autocomplete="false">
				<input type="hidden" name="submission" value="done" />
				<input type="hidden" name="actionName" value="ordermobilepref" /> 
				<input type="hidden" name="successPage" value="<%= successPage %>" />
				<input type="hidden" name="terms" value="true" />
				<input type="hidden" name="LITESIGNUP" value="true" />
				<input type="hidden" name="EXPRESSSIGNUP_SKIP_VALIDATION" value="true" />
				
				<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
					<div class="errMsg">
						<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'>
							<span class="text11rbold"><%=errorMsg%></span>
						</fd:ErrorHandler>
					</div>
				<% } %>
				
				<% if (showAntsFields) { %>
					<div>
	                   	<div id="sul_type_fields" aria-controls="collapsible-sul_cos_fields" aria-expanded="false">
							<fieldset>
		                 		<span class="legend"><legend>Delivery For:</legend></span>
		                 		<input type="radio" name="serviceType" id="sul_type_fields_HOME" value="HOME" <%= (serviceType.equals("HOME"))?"checked":"" %> tabindex="0" /><label for="sul_type_fields_HOME"><span>Home</span><span class="offscreen"> Delivery</span></label><input type="radio" name="serviceType" id="sul_type_fields_CORPORATE" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %> tabindex="0" /><label for="sul_type_fields_CORPORATE"><span>Business or School</span><span class="offscreen"> Delivery</span></label>
		                 	</fieldset>
	                   	</div>
					</div>
				<% } %>
			    <table border="0" cellpadding="5" cellspacing="8">
					<% if (showAntsFields) { %>
						<%-- ANTS data --%>
						<tr>
							<td>&nbsp;</td>
	                    	<td>
	                    		<div id="sul_cos_fields" style="display: none;" aria-hidden="true">
	                    			<div>
	                    				<input type="text" name="<%= EnumUserInfoName.DLV_COMPANY_NAME.getCode() %>" id="sul_cos_fields-workName" value="<%= companyName %>" class="padding-input-box text11ref inputDef required" placeholder="Company Name" maxlength="120" />
	                    			</div>
	                    			<div>
	                    				<input type="text" name="<%= EnumUserInfoName.DLV_FIRST_NAME.getCode() %>" id="sul_cos_fields-firstName" value="<%= firstname %>" class="padding-input-box text11ref inputDef required" placeholder="First Name" maxlength="25" />
	                    			</div>
	                    			<div>
	                    				<input type="text" name="<%= EnumUserInfoName.DLV_LAST_NAME.getCode() %>" id="sul_cos_fields-lastName" value="<%= lastname %>" class="padding-input-box text11ref inputDef required" placeholder="Last Name" maxlength="25"  />
	                    			</div>
	                    			<div>
	                    				<input type="text" name="<%= EnumUserInfoName.DLV_WORK_PHONE.getCode() %>" id="sul_cos_fields-workPhone" value="<%= workphone %>" class="padding-input-box text11ref inputDef" placeholder="Work Phone #" maxlength="14" />
	                    			</div>
	                    			<div>
	                    				<input type="text" name="<%= EnumUserInfoName.DLV_ZIPCODE.getCode() %>" id="sul_cos_fields-zipcode" value="<%= zipcode %>" class="padding-input-box text11ref inputDef required" placeholder="Zip Code" maxlength="5" />
	                    			</div>
	                    		</div>
	                    	</td>
	                    </tr>
					<% } %>
                    
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="email_img"></span>
						</td>
					<td>
						<input class="padding-input-box text11ref inputDef required" style="display:block;" 
							type="text" maxlength="128" size="23" 
							name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" 
							id="email" placeholder="E-mail" autocomplete="off" autocomplete="false">
					</td>
					</tr>

                    <% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %><tr><td></td><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
                    <tr>
                        <td valign="bottom">&nbsp
							
                        </td>
						<td style="padding-top: 15px;">
							<input type="text" name="prevent_autofill" id="prevent_autofill" value="" style="display:none;" />
							<input type="password" name="password_fake" id="password_fake" value="" style="display:none;" />
						</td>
					</tr>
					<tr>
						<td>
							<!-- span id should be the input box id+"_img" -->
                        	<span class="error_img" id="password1_img"></span>
                        	&nbsp;
						</td>
						<!--  Added for Password Strength Display -->
						<td>
							<div class="container1">		
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
										<input class="padding-input-box text11ref inputUser required" type="password" data-indicator="pwindicator" size="23" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1" placeholder="Password" autocomplete="off" autocomplete="false">
							<div style="position: relative"><a class="show-password">Show</a></div>
								</div>
								<div id="pwindicator">
									   <div class="bar"></div>
									   <div class="label"></div>
								</div>
								
							</div><!-- // .subgroup -->			
							</div><!-- // .content-group -->
							</div><!-- // .container -->
						</td>
						<!-- Added for Password Strength Display -->
					</tr>
					<tr>
						<td></td>
						<td style="padding-top: 15px;">
							<input type="submit" id="signupbtn" maxlength="25" size="19" value="Continue" class="butText social-login-continue-button" style="display:block;" onclick="dlvadrspage();">
						</td>
					</tr>
				
				</table>
			</form>							
		</div>
<!-- form_fields ends here -->
						
					
<script type="text/javascript">
        function display() {
        	document.getElementById("email").style.display='block';
        	document.getElementById("password1").style.display='block';
        	document.getElementById("signupbtn").style.display='block';
        	document.getElementById("emailbtn").style.display='none';
        }
</script>					
					
<script type="text/javascript" language="javascript">
	 $jq('#sul_type_fields input[type="radio"]').click(function(event){
		 $cosFields = $jq('#sul_cos_fields');
		 if ($jq(this).val() === 'CORPORATE' && $jq(this).prop('checked')) {
			 $cosFields.show();
			 $cosFields.attr('aria-hidden', false);
			 $jq('#sul_type_fields').attr('aria-expanded', true);
		 } else {
			 $cosFields.hide();
			 $cosFields.attr('aria-hidden', true);
			 $jq('#sul_type_fields').attr('aria-expanded', false);
		 }
	 });
	 $jq('#sul_type_fields_<%=serviceType %>').click();
 
 $jq.validator.addMethod("customemail", 
		 function validateEmail(email) {
	    	var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
	    	return re.test(email);
		} 
 );

 
 $jq('#litesignup').validate(
 {
 	
	 errorElement: "div",
	 	rules:{
             email:{
	             required:true,
	             email:true,
	             customemail:true
             }, 
             password:{
	             required:true,
	             minlength: 6
             }
 		},
 		messages:{
             email:{
	             required:"Required",
	             email:"Incomplete e-mail Address",
	             customemail:"Incomplete e-mail Address"
             },
             password:{
	             required:"Required",
				 minlength: $jq.validator.format( "Must be {0} or more characters" )
            }
         },
         highlight: function(element, errorClass, validClass) {
          $jq(element).addClass(errorClass).removeClass(validClass);
             $jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');  
             
             //highlight the security answer text on no input.
             if( element.id == 'secret_answer')
             {                            
            	  $jq('.bodyCopySULNote').css("color","red"); 
            	  $jq(errorElement).hide();
              }                           	 	            	
           },
           unhighlight: function(element, errorClass, validClass) {
          $jq(element).removeClass(errorClass).addClass(validClass);
             $jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
            	
           //un-highlight the security answer text on no input.
             if( element.id == 'secret_answer')
             {
               $jq('.bodyCopySULNote').css("color","");
              }           
           },
           onkeyup: false,
     errorPlacement: function(error, element) {
         error.insertBefore(element);
     }
 	}
 );
		 		 
</script>

</div>
				
<div class="social-login-headerscr-social">
	<div class="social-login-line-separator"></div>
	<span class="social-login-or-separator">or</span>
	<div class="social-login-line-separator"></div>
</div>

				
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
		_oneall.push([ 'social_login', 'set_custom_css_uri', '//www.freshdirect.com/media/social_login/social_login_media.css']);
		
		<% if(FDStoreProperties.isLocalDeployment()){ %>
			_oneall.push([ 'social_login', 'set_callback_uri', '<%= "http://" + request.getServerName()  + ":" + request.getServerPort() + "/social/social_login_success.jsp"%>' ]);
		<% } else { %>
			_oneall.push([ 'social_login', 'set_callback_uri', '<%= "https://" + request.getServerName() + "/social/social_login_success.jsp"%>' ]);
		<% } %>
		
		_oneall.push([ 'social_login', 'set_event','on_login_redirect', my_on_login_redirect ]);
		_oneall.push([ 'social_login', 'do_render_ui','social_login_demo' ]);
		
		$jq( document ).ready(function() {
			setTimeout(function(){ 
				FreshDirect.components.ifrPopup.reposition();
			}, 100);
		});
	</script>
</div>


				<div class="clear"></div>

				<div class="bottom-contents">
					<div class="bottom-contents-terms">
						<div class="bottom-contents-terms-text">
							By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp', 'large')">Customer Agreement</a> & <a href="javascript:popup('/registration/privacy_policy.jsp', 'large')">Privacy Policy</a>
						</div>
					</div>
					<div class="bottom-links">
						Already have an account? 
            <a href="/social/login.jsp?successPage=<%=successPage%>" onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp?successPage=<%=successPage%>', opacity: .5})">
							Sign In
						</a>
					</div>
				</div>
				
			
				<%
					if (result.isFailure()) {
									/*
									for(ActionError error : result.getErrors()) {
										 out.println("error:"+error.getDescription()); 
									}
									 */
				%>
				<script type="text/javascript">
					setFrameHeightSL('signupframe', $jq('#sulCont').height());
					window.top.Modalbox_setWidthAndPosition();
				</script>
				<%
					}
				%>
				<%
					}
				%>
		</div>
	</center>
	<%	
		/*
		 * "EXPRESS_REGISTRATION_COMPLETE" is set in SiteAccessControllerTag after express registration succeed
		 */
		if (pageContext.getSession().getAttribute("EXPRESS_REGISTRATION_COMPLETE") != null) {			
			session.setAttribute("EXPRESS_REGISTRATION_COMPLETE",null);
			
			String preSuccessPageFromSession = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
			
			if(preSuccessPageFromSession == null) {
	%>
				<div style="width: 500px;"><img src="/media_stat/images/navigation/spinner.gif" class="fleft" /></div>		
				<script language="javascript">
					//window.top.location = '/index.jsp';   // close popup and return to index page
					window.location = '/social/AccountCreateSuccess.jsp';  // forward to this page
				</script>				
	<%
			} else {				
				session.setAttribute(SessionName.PREV_SUCCESS_PAGE,null);			
	%>
				<div style="width: 500px;"><img src="/media_stat/images/navigation/spinner.gif" class="fleft" /></div>		
				<script language="javascript">
					window.top.location = '<%= preSuccessPageFromSession %>';   // close popup and return to  preSuccessPageFromSession 
				</script>				
	<%	
			}
		} 
	%>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
	</html>
	<%
		}
	%>
</fd:SiteAccessController>
