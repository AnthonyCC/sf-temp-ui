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
	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String successPage = "index.jsp";
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

<jwr:style src="/assets/css/social_login_signup.css" media="all" />

<script type="text/javascript">
 
		/* Replace #your_subdomain# by the subdomain of a Site in your OneAll account */    
		var oneall_subdomain = 'freshdirect';
 
		/* The library is loaded asynchronously */
		var oa = document.createElement('script');
		oa.type = 'text/javascript'; oa.async = true;
		oa.src = '//' + oneall_subdomain + '.api.oneall.com/socialize/library.js';
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
			}
			else
			{
				$jq("#password1").prop("type","password");
			}
		 
		});
	
	</script>



	<%@ include file="/shared/template/includes/i_head_end.jspf"%>
	
	<%@ include file="/shared/template/includes/i_body_start.jspf"%>
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10">

	<center>

	<%
	
	session.setAttribute("lastpage","signup_lite");
	
	String email="",firstname="",lastname="",repeat_email="",password="",passwordhint="",zipcode="",posn="";
	
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

			<div id="sulCont" class="signup-style-social">
				
				
				
					<div class="form-side-social" style="width:322px; margin-left :100px;margin-top:25px; margin-bottom: 30;">

						<span
							style="font-size: 12px; font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-bottom:20px;margin-left:20px">Create Account:</span>

						<div id="form_feilds" style="width:294px;margin-top:20px; margin-left: 20px;">


						<form id="litesignup" name="litesignup" method="post"
							action="/social/signup_lite.jsp">
							<input type="hidden" name="submission" value="done" /> <input
								type="hidden" name="actionName" value="ordermobilepref" /> <input
								type="hidden" name="successPage" value="<%= successPage %>" />
							<input type="hidden" name="terms" value="true" /> <input
								type="hidden" name="LITESIGNUP" value="true" />

			
				    <table border="0" cellpadding="5" cellspacing="8">

					
					<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
					
					<tr><td>&nbsp;</td>
					<td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' 
					id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>													
							
					<tr>
					<td>
					<!-- span id should be the input box id+"_img" -->
								
					<span class="error_img" id="email_img"></span></td>
					<td >
					<input 	class="padding-input-box text11ref inputDef required" style="box-sizing: border-box; width: 199px;display:block" 
							type="email" maxlength="128" size="23" 
							name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" 
							id="email" placeholder="E-mail" >
					</td>
					</tr><br/>


                    <% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %><tr><td>&nbsp;</td><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
                    <tr>
                        <td>
                        <!-- span id should be the input box id+"_img" -->
                        <span class="error_img" id="password1_img"></span></td>
						<td style="padding-top: 10px;">
						<input 	class="padding-input-box text11ref inputUser required" 
								type="password" style="box-sizing: border-box; width: 199px; padding-top: 10px;display:block" size="23" 
								name="<%=EnumUserInfoName.PASSWORD.getCode()%>" 
							   	id="password1" placeholder="Password" >
						<a class="show-password"  style="top: 130px; right: 145px;">Show</a></td>
					</tr>


									
					<tr>
					<td></td>
						<td style="padding-top: 10px;"><a onclick="dlvadrspage();" class="butText" style="font-weight:bold;font-size:14px;">
						<input type="submit" id="signupbtn" maxlength="25" size="19" value="Continue" style="display:block;"> </a></td>
					</tr>
					<!-- 
					<tr>
								<td></td>
								<td style="padding-top: 10px;"><a onclick="display();"
									class="butText" style="font-weight: bold; font-size: 14px;">
										<input type="submit1" id="emailbtn" maxlength="25" size="19"
										value="Use E-mail">
								</a></td>
							</tr>	
							 -->										
					
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
 
 $jq.validator.addMethod("customemail", 
		 function validateEmail(email) {
	    	var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
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

     errorPlacement: function(error, element) {
         error.insertBefore(element);

     },  
   
 	}
 
 );
		 		 
</script> <!-- front end validations end here -->	

				</div><!--  form-side ends here -->
				
<div class="social-login-headerscr-social"  style="float:none; margin-left: 40px; margin-top: 40px; padding-top: 160px;">

	<p style="font-size:12px;font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-top:40px;">
	<img src="/media_stat/images/navigation/line.png" WIDTH="100" HEIGHT="2" border="0" >
	Or
	<img src="/media_stat/images/navigation/line.png" WIDTH="100" HEIGHT="2" border="0" >
	</p><br>

</div>

				
<div id="social_login_demo" class="social-login-social" style="float:none; margin-left: 120px;">
	<script type="text/javascript">
		/* Replace the subdomain with your own subdomain from a Site in your OneAll account */
		var oneall_subdomain = 'freshdirect';
	
		/* Asynchronously load the library */
		var oa = document.createElement('script');
		oa.type = 'text/javascript';
		oa.async = true;
		oa.src = '//' + oneall_subdomain
				+ '.api.oneall.com/socialize/library.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(oa, s);
	
		/* This is an event */
		var my_on_login_redirect = function(args) {
			return true;
		}
	
		/* Initialise the asynchronous queue */
		var _oneall = _oneall || [];
    	_oneall.push([ 'social_login', 'set_providers',[ 'facebook', 'google' ] ]);
		_oneall.push([ 'social_login', 'set_grid_sizes', [ 2, 2 ] ]);
		_oneall.push([ 'social_login', 'set_callback_uri',
						       		'<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/social/social_login_success.jsp"%>' ]);
		_oneall.push([ 'social_login', 'set_event','on_login_redirect', my_on_login_redirect ]);
		_oneall.push([ 'social_login', 'do_render_ui','social_login_demo' ]);
	</script>
</div> <!-- social login ends here -->


				<div class="clear"></div>

				<div class="bottom-contents" style="margin-left: 50px;">

					<span class="text12">By signing up, you agree to the <a
						href="javascript:popup('/registration/user_agreement.jsp', 'large')"
						style="font-weight: normal; text-decoration: underline">Terms
							of Service</a></span><br> <br> <span class="bottom-links">
						<b>Already have an Account? 
						   <a href="#"
							 onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp', width: 518, height: 518}) ">
							 Log In
						</a>
					
					</b>

					</span>


				</div>
				<!-- signup-style ends here -->
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

	</center>




	<%	
		/*
		 * "EXPRESS_REGISTRATION_COMPLETE" is set in SiteAccessControllerTag after express registration succeed
		 */
		if (pageContext.getSession().getAttribute("EXPRESS_REGISTRATION_COMPLETE") != null) {			
			session.setAttribute("EXPRESS_REGISTRATION_COMPLETE",null);
	%>
			<div style="width: 500px;"><img src="/media_stat/images/navigation/spinner.gif" class="fleft" /></div>		
			<script language="javascript">
				window.top.location = '/index.jsp';   // close popup and return to index page
			</script>				
	<%
		} 
	%>


</body>
	</html>
	<%
		}
	%>
</fd:SiteAccessController>
