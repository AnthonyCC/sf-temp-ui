<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.common.customer.EnumServiceType'%>
<html lang="en-US" xml:lang="en-US">
<head>
    <title>FreshDirect</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
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
	var ifr = window.parent.$jq('#ifrPopup iframe');
	 ifr.css({
         height:404
       });
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
	
	HashMap socialUser = null;
	String provider="";
	String providers="";
	String userid ="";
	String[] checkErrorType = {"authentication", "technical_difficulty"};

	
%>


<%
	socialUser = (HashMap)session.getAttribute(SessionName.SOCIAL_USER);
	
	if(socialUser != null)
	{
		userid = (String)socialUser.get("email");
		session.setAttribute(userid,socialUser);
	}
	
    session.setAttribute("lastpage","social_login_merge");
    String uri = request.getRequestURI();
	String successPage = request.getParameter("successPage");
	if (successPage == null) {      		
		    successPage = "/login/index.jsp";        
	}
	
	if(socialUser != null)
		provider = (String)socialUser.get("provider");
	
	
%>

<fd:LoginController successPage="<%= successPage %>" mergePage="/login/merge_cart.jsp" result='result'>

<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
		
</fd:ErrorHandler>
	
<body bgcolor="#ffffff" text="#333333" class="text10" >

		<center>
			

				<div id="sulCont" class="signup-style" style="height:404px">
				
					
						<p style="font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-left:45px;margin-right:30px;text-align:left;">Hey, you look familiar!</p>
						
						<font class="text13" style="margin-left:45px;text-align:left;float:left"> There is already a FreshDirect account using using your <br />email. Log in to continue connecting to <%=provider%>.<br><br>
						</font>
					
					
					
					<div class="form-side" style="width:222px;margin-right:50px;">
						
						<div id="form_feilds" style="width:294px;">


							<form name="fd_login" id="fd_login" method="post"
								action="<%= request.getRequestURI()+"?"+StringUtil.escapeXssUri(request.getQueryString()) %>">


								<table border="0" cellpadding="5" cellspacing="8">

									<%
										if (!result.isSuccess()) {
												out.println("<tr><td><font color='red'>Email/password do not match.</font></td></tr>");

										}
									%>
									<tr>
										<td>
										 <input id="userid" name="userid" value="<%=userid %>" class="padding-input-box text11ref inputDef" type="email"  maxlength="128" size="21" /> 
										<%-- <input id="userid" name="userid" value="<%=userid %>" > --%>
 										</td>
										
										
									</tr>


									<tr>

										<td><input id="password" name="password"
											class="padding-input-box text11ref inputUser" type="password" minlength="4"
											 size="21" placeholder="Password"
											></td>
									</tr>


									<tr>
										<td style="padding-top: 10px;"><a
											onclick="document.fd_login.submit();" href="#"
											class="butText" style="font-weight: bold; font-size: 14px;">
												<input type="submit" id="submit" maxlength="25" size="25"
												value="log In">
										</a></td>
									</tr>

									<tr>
										<td style="text-align: center">
										<font class="text13"> <A
												HREF="/social/forgot_password.jsp">Forgot Password?</a>
										</FONT>
										</td>
									</tr>

								</table>

							</form>

					</div><!-- form_fields ends here -->
<script type="text/javascript" language="javascript">
	$jq('#fd_login').validate({
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
			required: "Required",
			remote: "E-mail already in use.Log in?.",
			email: "Incomplete e-mail address.",
			maxlength: $.validator.format( "ZIP must be {0} numbers only." ),
			minlength: $.validator.format( "Must be {0} or more characters." )
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
	});
		 		 
</script> <!-- front end validations end here -->			
					
           </div><!--  form-side ends here -->

		  <div id="current_social_login"  class="social-login">

			<img  src="/media_stat/images/common/double_arrow.PNG" alt="arrow" width="22px" height="20px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
	      
			<img src="/media_stat/images/common/<%=provider%>.PNG" alt="<%=provider%>_image" width="100px" height="34px"><br><br><br>
			
		</div>	
		  

<div class="clear"></div>
  <fd:GetConnectedProvidersTag id="result" >
  	
 </fd:GetConnectedProvidersTag>

 
	
<div>
	
	<%
	String providerStr = (String)session.getAttribute("providerStr");
	
	if(providerStr != null && providerStr.length() > 5)
	{
		%>
		<div class="fright hline" id="" style="width:100%; float:left;"></div>
		<span class="alignment text12"> Or log in with your previously connected accounts:</span><br>
		<div id="social_login_demo" style="margin-left:25px;"> </div>
		<%
	}
    %>

<script type="text/javascript">
	/* Replace the subdomain with your own subdomain from a Site in your OneAll account */
	var oneall_subdomain = '<%=site_subdomain%>';

	/* Asynchronously load the library */
	var oa = document.createElement('script');
	oa.type = 'text/javascript';
	oa.async = true;
	oa.src = '//' + oneall_subdomain + '<%=site_post_url%>/socialize/library.js'
	var s = document.getElementsByTagName('script')[0];
	s.parentNode.insertBefore(oa, s)

	/* This is an event */
	var my_on_login_redirect = function(args) {
		//alert("You have logged in with " + args.provider.name);
		return true;
	}

	/* Initialise the asynchronous queue */
	var _oneall = _oneall || [];

	/* Social Login  */
	_oneall.push([ 'social_login', 'set_force_re_authentication', true]);
	_oneall.push([ 'social_login', 'set_providers',
			[ <%=session.getAttribute("providerStr")%> ] ]);
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
	
			
</div>


<!--</div> container ends here -->
</fd:LoginController>

</center>


</body>
</html>


