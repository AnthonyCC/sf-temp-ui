<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.text.MessageFormat"%>
<%@ page import="java.util.List"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
%>

<html>
<head>
<% if(mobWeb){ %><jwr:style src="/mobileweb.css" media="all" /><% } %>
<title></title>

  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    
<% 
	String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
	String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();
%>	    
    


<!-- enable/disable submit button -->
<script type="text/javascript" language="javascript">
	$jq(function(){
		// Hide all the error arrow images
		//$jq('.error_img').hide();
		// disable the submit button on page load
		$jq('#signinbtn').attr('disabled', true);
		// on keyup we will check every time the form is valid or not
		$jq('#fd_login').bind('change keyup', function() {
		    if($jq(this).validate().checkForm()) { // form is valid
		        $jq('#signinbtn').removeClass('button_disabled').attr('disabled', false);
	
		    } else { // form is invalid
		        $jq('#signinbtn').addClass('button_disabled').attr('disabled', true);
		    }
		});
	});
</script >
	

</head>


<%


    String uri = request.getRequestURI();
	String successPage = request.getParameter("successPage");
	if ( (successPage == null) || (successPage == "undefined") || (successPage == "ndefined") ) { 
	
		    successPage = "/index.jsp";
	}
	
	
	String userid = NVL.apply(request.getParameter("userid"), NVL.apply(user.getUserId(), ""));
	
	String triedToConnect= request.getParameter("triedToConnect");		
	
    // determine the preSuccessPage from previous workflow
    String preSuccessPage = (String)request.getParameter("preSuccessPage");
    if(preSuccessPage != null && preSuccessPage.length()>0 )
    	session.setAttribute(SessionName.PREV_SUCCESS_PAGE, preSuccessPage); 
    
%>

<fd:LoginController successPage="<%= successPage %>" mergePage="/login/merge_cart.jsp" result='result'>
<%!
//String[] checkLoginForm = {"userid", "email_format", "password"};
String[] checkErrorType = {"authentication", "technical_difficulty"};
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		
</fd:ErrorHandler>



<body class="social_login_jsp <%= (mobWeb) ? "mm-page" : "" %>">
	<center>
		<div id="sulCont" class="signup-style-social social-singin">

   			<div class="form-side-social">
   			<div class="form-side-social-header">Sign in with email:</div>
	
			<!-- 
	        <% // The user tried to sign in with an unrecognized social user, and redirected back to sign in with existing recognized social user. if(triedToConnect !=n ull && triedToConnect.length()>0){ %>
		            <div style=>
		                <img src="/media_stat/images/navigation/social_accounts/<%=triedToConnect%>_logo_round.png" width="32" height="26" border="0" alt="<%=triedToConnect%> Round">
		                <font class="text13">sign in to connect with <%=triedToConnect%>.</font>
		            </div>
	
	            <input type="hidden" id="triedToConnectSocialProvider" name="triedToConnectSocialProvider" value="<%=triedToConnect%>">
	        <% //} %>
	        -->
			
	        <div id="form_feilds">
	            <form name="fd_login" id="fd_login" method="post" >	            
	            	<% if (!result.isSuccess()) {	
	            	String errorMsg = "<div class='error-message'>Email and password do not match.</br>Please try again.</div>";
	                String isSocialLoginOnlyUser = (String)session.getAttribute("IS_SOCIAL_LOGIN_USER_VALIDATION");  
	                String connectedProviders = (String)session.getAttribute("CONNECTED_SOCIAL_PROVIDERS");            	           	
	                if(isSocialLoginOnlyUser != null && isSocialLoginOnlyUser.length()>0 ){
	                 session.setAttribute("IS_SOCIAL_LOGIN_USER_VALIDATION", null); 
	                 session.setAttribute("CONNECTED_SOCIAL_PROVIDERS", null);
	                    errorMsg = "<div class='error-message'>User Name & Password do not match</br>Please try again or Sign in with "+connectedProviders+".</div>";	                   	
	                }
	             out.println(errorMsg);
	            %>             
	        <script type="text/javascript">
	    	   	$jq(function() {
	        	   $jq('#email_img').addClass('show_bg_arrow');
	                $jq('#email').addClass('error');
	                $jq('#password_img').addClass('show_bg_arrow');
	                $jq('#password').addClass('error');
	                });
	            </script>
	            <% } %>
	            	<table border="0" cellpadding="5" cellspacing="8">
	                    <tr>
	                	 	<td valign="bottom">
	                    	    <!-- span id should be the input box id+"_img" -->
	                            <span class="error_img" id="email_img"></span>
	                        </td>
                                <%-- This field is named as 'userid' to be consistent with naming rule in LoginControllerTag. The email validation rule is applied to this field as shown below.  --%>
                            <td>
	                            <input id="email" name="userid" class="padding-input-box text11ref inputDef required" type="email" maxlength="128" size="23" value="<%=userid%>" placeholder="E-mail" autocomplete="email">
	                        </td>
	                    </tr>
	                    <tr>
		  	                <td valign="bottom">
	                       		<!-- span id should be the input box id+"_img" -->
	                            <span class="error_img" id="password_img"></span>
	                        </td>
	                        <td style="padding-top: 15px;">
	                            <input id="password" name="password" class="padding-input-box text11ref inputDef required" type="password" size="23" placeholder="Password" autocomplete="email">
	                        </td>
	                    </tr>
	                    <tr>
	                    	<td></td>
	                        <td style="padding-top: 15px;">
	                   		   	<a onclick="document.fd_login.submit();" href="#" class="butText social-login-continue-button">
	                              	<input type="submit" id="signinbtn" maxlength="25" size="19" value="Sign in">
	                            </a>
	                        </td>
	                    </tr>
	                    <tr>
	                       	<td></td>
	                        <td class="social-login-pass-forgot">
	                        	<a href="/social/forgot_password.jsp">Forgot Password?</a>
	                        </td>
	                    </tr>
              		</table>
              	</form>
	        </div>
        <!-- form_fields ends here -->
                
	<script type="text/javascript" language="javascript">
	
	 $jq.validator.addMethod("customemail", 
			 function validateEmail(email) {
		 		var re = /^[a-zA-Z0-9._-]+@([a-zA-Z0-9-_]+\.)+[a-zA-Z]{2,}$/;
		 		return re.test(email);
			} 
	 );
	
	 $jq('#fd_login').validate(
			 {
		 		 rules:{
		             email:{
			             required:true,
			             email:true,
			             customemail:true
		            },
		            userid:{
			             required:true,
			             email:true,
			             customemail:true
			            },		            
		            password:{
		            	required:true,
		            	//password:true,
		            }
		 		},
		 		messages:{
		            email:{
		            	required:"Required",
		            	email:"Incomplete e-mail Address",
		                customemail:"Incomplete e-mail Address"
		            },
		            userid:{
		            	required:"Required",
		            	email:"Incomplete e-mail Address",
		                customemail:"Incomplete e-mail Address"
		            },		 		
		 		
		            password:{
		            	required:"Required",
		            	//password:"Please enter at least 6 characters",
		            }
		         },
		         highlight: function(element, errorClass, validClass) {
		          $jq(element).addClass(errorClass).removeClass(validClass);
		             $jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');
		           },
		           unhighlight: function(element, errorClass, validClass) {
		          $jq(element).removeClass(errorClass).addClass(validClass);
		             $jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
		             
		           },
		         onkeyup: false,
		 		 errorElement: "div",

		     errorPlacement: function(error, element) {
		         error.insertBefore(element);
		     }, 
		 	}
		 );
	 
</script> <!-- front end validations end here -->
					
					
           </div><!--  form-side ends here -->
           
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
	oa.src = '//' + oneall_subdomain + '<%=site_post_url%>/socialize/library.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(oa, s);

	/* This is an event */
	var my_on_login_redirect = function(args) {
		
		return true;
	}

	/* Initialise the asynchronous queue */
	var _oneall = _oneall || [];

	/* Social Login Example */
	//_oneall.push([ 'social_login', 'set_providers', [ 'facebook', 'google' ] ]);

	// *** dynamically show the social providers - starts
	var triedProvider = jQuery("#triedToConnectSocialProvider").val();
	if(triedProvider){		
		if(triedProvider == 'facebook'){
			_oneall.push([ 'social_login', 'set_providers',[ 'google' ] ]);
		} else if(triedProvider == 'google'){
			_oneall.push([ 'social_login', 'set_providers',[ 'facebook' ] ]);
		}					
	} else{		
		_oneall.push([ 'social_login', 'set_providers',[ 'facebook', 'google' ] ]);
	}	
	// ***  dynamically show the social providers - ends
	
	_oneall.push([ 'social_login', 'set_force_re_authentication', true]);
	_oneall.push([ 'social_login', 'set_grid_sizes', [ 1, 2 ] ]);
	_oneall.push([ 'social_login', 'set_custom_css_uri', '//www.freshdirect.com/media/social_login/social_login_media.css']);
	
	<% if(FDStoreProperties.isLocalDeployment()){ %>
		_oneall.push([ 'social_login', 'set_callback_uri', document.location.protocol + '<%= "//" + request.getServerName() + ":" + request.getServerPort()+"/social/social_login_success.jsp"  %>' ]);
	<% } else { %>
		_oneall.push([ 'social_login', 'set_callback_uri', 'https://' + document.location.host + '/social/social_login_success.jsp' ]);
	<% } %>
	
	_oneall.push([ 'social_login', 'set_event', 'on_login_redirect', my_on_login_redirect ]);
	_oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);
	
	$jq( document ).ready(function() {
		setTimeout(function(){ 
			FreshDirect.components.ifrPopup.reposition();
		}, 100);
		
		//set focus
		$jq('#<%= ("".equals(userid)) ? "email" : "password" %>').focus();
	});
</script>

</div> <!-- social login section ends here -->



		<div class="clear"></div>

		<div class="bottom-contents">
			<div class="bottom-links">
				New to FreshDirect?
        <a href="/social/signup_lite.jsp?successPage=<%=successPage%>" onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp?successPage=<%=successPage%>', height: 590, opacity: .5});">
					Create Account
				</a>
			</div>
		</div>

</div>
<!-- container ends here -->
</fd:LoginController>

</center>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
</body>
</html>


