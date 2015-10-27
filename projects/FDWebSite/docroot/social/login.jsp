<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.util.List"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<html>
<head>
<title></title>

  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    
<% 
	String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
	String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();
%>	    
    
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
	if (successPage == null) { 
	
		    successPage = "/login/index.jsp";
	}
	
	
	
	/*String userFromSession = null;
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	if(user != null)
		userFromSession = user.getUserId();*/
	String userid= request.getParameter("userid");
	String triedToConnect= request.getParameter("triedToConnect");		
%>

<fd:LoginController successPage="<%= successPage %>" mergePage="/login/merge_cart.jsp" result='result'>
<%!
//String[] checkLoginForm = {"userid", "email_format", "password"};
String[] checkErrorType = {"authentication", "technical_difficulty"};
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		
</fd:ErrorHandler>

<body>
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
	            <form name="fd_login" id="fd_login" method="post" action="<%= request.getRequestURI()+"?"+StringUtil.escapeXssUri(request.getQueryString()) %>">
	            	<table border="0" cellpadding="5" cellspacing="8">
		                <% if (!result.isSuccess()) { out.println( "<tr><td>&nbsp;</td><td><font color='red' size='2px'>Email and password do not match.</font></td></tr><tr><td></td><td><font color='red' size='2px'>Please try again.</font></td></tr>");%>
	    	                <script type="text/javascript">
	        	            	$jq(function() {
	            	            	$jq('#email_img').addClass('show_bg_arrow');
	                                $jq('#email').addClass('error');
	                                $jq('#password_img').addClass('show_bg_arrow');
	                                $jq('#password').addClass('error');
	                            });
	                       	</script>
	                    <% } %>
	                    <tr>
	                	 	<td valign="bottom">
	                    	    <!-- span id should be the input box id+"_img" -->
	                            <span class="error_img" id="email_img"></span>
	                        </td>
                                <!-- This field is named as 'userid' to be consistent with naming rule in LoginControllerTag. The email validation rule is applied to this field as shown below.  -->
                            <td>
	                            <input id="email" name="userid" class="padding-input-box text11ref inputDef required" type="email" maxlength="128" size="23" value="<%=userid%>" placeholder="E-mail">
	                        </td>
	                    </tr>
	                    <tr>
		  	                <td valign="bottom">
	                       		<!-- span id should be the input box id+"_img" -->
	                            <span class="error_img" id="password_img"></span>
	                        </td>
	                        <td style="padding-top: 15px;">
	                            <input id="password" name="password" class="padding-input-box text11ref inputDef required" type="password" size="23" placeholder="Password">
	                        </td>
	                    </tr>
	                    <tr>
	                    	<td></td>
	                        <td style="padding-top: 15px;">
	                   		   	<a onclick="document.fd_login.submit();" href="#" class="butText">
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
	
	 $jq('#fd_login').validate(
			 {
		 		 rules:{
		             email:{
		             required:true,
		             email:true
		            },
		            userid:{
			             required:true,
			             email:true
			            },		            
		            password:{
		            	required:true,
		            	//password:true,
		            }
		 		},
		 		messages:{
		            email:{
		            	required:"Required",
		            	email:"Incomplete e-mail Address"
		            },
		 		
		            userid:{
		            	required:"Required",
		            	email:"Incomplete e-mail Address"
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
	_oneall.push([ 'social_login', 'set_custom_css_uri', '//freshdirect.com/media/social_login/social_login_media.css']);
	_oneall.push([ 'social_login', 'set_callback_uri',
	       		'<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/social/social_login_success.jsp"  %>' ]);

	_oneall.push([ 'social_login', 'set_event', 'on_login_redirect', my_on_login_redirect ]);
	_oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);
	
	$jq( document ).ready(function() {
		FreshDirect.components.ifrPopup.reposition();
	});
</script>

</div> <!-- social login section ends here -->



		<div class="clear"></div>

		<div class="bottom-contents">
			<div class="bottom-links">
				New to FreshDirect?
				<a href="/social/signup_lite.jsp" onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', opacity: .5});">
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


