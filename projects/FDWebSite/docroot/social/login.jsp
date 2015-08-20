<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.util.List"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>

<html>
<head>
<title></title>

  <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  
<script type="text/javascript">
 
		/* Replace #your_subdomain# by the subdomain of a Site in your OneAll account */    
		var oneall_subdomain = 'fd-test';
 
		/* The library is loaded asynchronously */
		var oa = document.createElement('script');
		oa.type = 'text/javascript'; oa.async = true;
		oa.src = '//' + oneall_subdomain + '.api.oneall.com/socialize/library.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(oa, s);
       
</script>

<!-- enable/disable submit button -->
<script type="text/javascript" language="javascript">
	$jq(function(){
		// Hide all the error arrow images
		//$jq('.error_img').hide();
		// disable the submit button on page load
		$jq('#submit').attr('disabled', true);
		// on keyup we will check every time the form is valid or not
		$jq('#fd_login').bind('change keyup', function() {
		    if($jq(this).validate().checkForm()) { // form is valid
		        $jq('#submit').removeClass('button_disabled').attr('disabled', false);
	
		    } else { // form is invalid
		        $jq('#submit').addClass('button_disabled').attr('disabled', true);
	
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
%>

<fd:LoginController successPage="<%= successPage %>" mergePage="/login/merge_cart.jsp" result='result'>
<%!
//String[] checkLoginForm = {"userid", "email_format", "password"};
String[] checkErrorType = {"authentication", "technical_difficulty"};
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		
</fd:ErrorHandler>

<body bgcolor="#ffffff" text="#333333" class="text10">


		<center>
			

				<div id="sulCont" class="signup-style">


					<div class="form-side" style="width:222px;margin-right:50px;margin-top:15px;">

						<span
							style="font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-bottom:20px;margin-left:25px">Log
							In with Email:</span>

						<div id="form_feilds" style="width:294px;margin-top:10px;">


							<form name="fd_login" id="fd_login" method="post"
								action="<%= request.getRequestURI()+"?"+StringUtil.escapeXssUri(request.getQueryString()) %>" >
								<table border="0" cellpadding="5" cellspacing="8">

									<%
										if (!result.isSuccess()) {
												out.println("<tr><td>&nbsp;</td><td><font color='red' size='2px'>Email/password do not match.</font></td></tr>");%>
										<script type="text/javascript">
											$jq(function(){
												$jq('#email_img').addClass('show_bg_arrow');
												$jq('#email').addClass('error');
												$jq('#password_img').addClass('show_bg_arrow');
												$jq('#password').addClass('error');
											});
																			
										</script>
										<% } %>
																			
									<tr>
									
										<td>
										<!-- span id should be the input box id+"_img" -->
										<span class="error_img" id="email_img"></span>
										</td>
									
										<td><input id="email" name="userid"
											class="padding-input-box text11ref inputDef required" type="email"
											 maxlength="128" size="23" value="<%=userid%>" placeholder="E-mail">
										</td>
									</tr>


									<tr>
										 
										<td>
										<!-- span id should be the input box id+"_img" -->
										<span class="error_img" id="password_img"></span>
										</td>
										
										<td><input id="password" name="password"
											class="padding-input-box text11ref inputDef required" type="password" minlength="4"
											 size="23" placeholder="Password">
									    </td>
									</tr>

									<tr>
										<td>&nbsp;</td>
										<td style="padding-top: 10px;"><a
											onclick="document.fd_login.submit();" href="#"
											class="butText" style="font-weight: bold; font-size: 14px;">
												<input type="submit" id="submit" maxlength="25" size="19"
												value="Log In" >
										</a></td>
									</tr>

									<tr>
									<td>&nbsp;</td>
									<td style="text-align: center"><font class="text13"> <A HREF="/social/forgot_password.jsp">Forgot Password?</a></FONT></td></tr>

 								</table>

							</form>

					</div><!-- form_fields ends here -->
					

	<script type="text/javascript" language="javascript">
	
	 $jq('#fd_login').validate(
			 {
		 		 rules:{
		             email:{
		             required:true,
		             email:true
		            },
		            
		            password1:{
		            	required:true,
		            	password:true,
		            }
		 		},
		 		messages:{
		            email:{
		            required:"",
		            email:"Incomplete e-mail Address"
		            }
		         },
		         highlight: function(element, errorClass, validClass) {
		          $jq(element).addClass(errorClass).removeClass(validClass);
		             $jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');
		         ;

		           },
		           unhighlight: function(element, errorClass, validClass) {
		          $jq(element).removeClass(errorClass).addClass(validClass);
		             $jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
		            ;
		           },
		 		 errorElement: "div",

		     errorPlacement: function(error, element) {
		         error.insertBefore(element);

		     }, 
		 	}
		 );
		 		 
</script> <!-- front end validations end here -->
					
					
           </div><!--  form-side ends here -->
           
  <div class="social-login-headerscr">

	<p style="font-size:12px;font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-right:40px;">Or Log In with:
	</p><br>

</div>         


<div id="social_login_demo" class="social-login">


<script type="text/javascript">
	/* Replace the subdomain with your own subdomain from a Site in your OneAll account */
	var oneall_subdomain = 'fd-test';

	/* Asynchronously load the library */
	var oa = document.createElement('script');
	oa.type = 'text/javascript';
	oa.async = true;
	oa.src = '//' + oneall_subdomain + '.api.oneall.com/socialize/library.js';
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(oa, s);

	/* This is an event */
	var my_on_login_redirect = function(args) {
		
		return true;
	}

	/* Initialise the asynchronous queue */
	var _oneall = _oneall || [];

	/* Social Login Example */
	_oneall.push([ 'social_login', 'set_providers',
			[ 'facebook', 'google' ] ]);
	_oneall.push([ 'social_login', 'set_grid_sizes', [ 4, 4 ] ]);
	/* _oneall.push([ 'social_login', 'set_callback_uri',
			'http://127.0.0.1:7001/social/social_login_success.jsp' ]); */
	_oneall.push([ 'social_login', 'set_callback_uri',
	       		'<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/social/social_login_success.jsp"  %>' ]);

	_oneall.push([ 'social_login', 'set_event', 'on_login_redirect',
			my_on_login_redirect ]);
	_oneall.push([ 'social_login', 'do_render_ui', 'social_login_demo' ]);
</script>

</div> <!-- social login section ends here -->

<div class="clear"></div>

<div class="bottom-contents">
    <span class="bottom-links"> <b>New to FreshDirect? <a href="/social/signup_lite.jsp"

onclick="FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', width: 500, height: 698, opacity: .5}) ">Sign Up</a></b>

       </span>

</div>


<!--</div> container ends here -->
</fd:LoginController>

</center>


</body>
</html>


