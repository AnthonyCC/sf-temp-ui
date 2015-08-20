<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.common.address.AddressModel"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
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
	
	  
	
      String failurePage = "/registration/signup_lite.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;
    
      CmRegistrationTag.setRegistrationLocation(session,"signup lite");
%>


<fd:SiteAccessController action='signupLite' successPage='<%= successPage %>' moreInfoPage='' failureHomePage='<%= failurePage %>' result='result'>	


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
  
					});
</script>
		<% } %>	
<!-- end email validation --> 

			<div id="sulCont" class="signup-style" style="height:698px;">
				
				
				<div class="form-side" >

					<p style="font-size:12px;font-weight:bold;margin-bottom:2px;font-family: Verdana, Arial, sans-serif;text-align:left;margin-left:46px">Sign Up with Email:</p>


					<div id="form_feilds">


						<form id="litesignup" name="litesignup" method="post"
							action="/social/signup_lite.jsp">
							<input type="hidden" name="submission" value="done" /> <input
								type="hidden" name="actionName" value="ordermobilepref" /> <input
								type="hidden" name="successPage" value="<%= successPage %>" />
							<input type="hidden" name="terms" value="true" /> <input
								type="hidden" name="LITESIGNUP" value="true" />

			
				    <table border="0" cellpadding="5" cellspacing="8">

						<% if (result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) || result.hasError(EnumUserInfoName.DLV_SERVICE_TYPE.getCode())) { %>
						<tr>
						 <td>&nbsp;</td>						  
							<td class="errMsg">
								<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
							</td>
                        </tr>
                        
                        <tr>
                        <td>&nbsp;</td>                       
                            <td class="errMsg">
                            <fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_SERVICE_TYPE.getCode()%>' id='errorMsg'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
                            </td>
                        </tr>

					<% } %>
						<tr>							
							<td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="zipcode_img"></span></td>							
							<td><input class="padding-input-box text11ref inputUser required" type="number"  maxlength="5" class="" size="20" name="zipcode" value="<%=zipcode%>" id="zipcode" placeholder="Delivery ZIP Code" >
							</td>
                        </tr>
                        <tr>
							
							<td></td>
							<td>
							<input type="radio" name="serviceType" value="HOME" <%= (serviceType.equals("HOME"))?"checked":"" %>/>HOME&nbsp;
							<input type="radio" name="serviceType" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %>/>OFFICE
							</td>
						</tr>
					
					
					<% if (result.hasError(EnumUserInfoName.DLV_FIRST_NAME.getCode())) { %><tr><td>&nbsp;</td><td class="errMsg "><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>				
					<td>
					<!-- span id should be the input box id+"_img" -->
					<span class="error_img" id="first_name_img"></span></td>
					<td>
					<input class="padding-input-box text11ref inputUser required" type="text"  maxlength="25" size="20" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" id="first_name" placeholder="First Name" ></td>
					</tr>
					
					
					<% if (result.hasError(EnumUserInfoName.DLV_LAST_NAME.getCode())) { %><tr><td>&nbsp;</td><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>
					<td>
					<!-- span id should be the input box id+"_img" -->
					<span class="error_img" id="last_name_img"></span></td>
					<td><input class="padding-input-box text11ref inputUser required" type="text"  maxlength="25" size="20" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" id="last_name" placeholder="Last Name" ></td>
					</tr>
					
					
					<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %>
					
					<tr><td>&nbsp;</td><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>													
							
					<tr>
					<td>
					<!-- span id should be the input box id+"_img" -->
								
					<span class="error_img" id="email_img"></span></td>
					<td>
					<input class="padding-input-box text11ref inputDef required" type="email"  maxlength="128" size="20" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" id="email" placeholder="E-mail" >
					</td>
					</tr><br/>


                    <% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %><tr><td>&nbsp;</td><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
                    <tr>
                        <td>
                        <!-- span id should be the input box id+"_img" -->
                        <span class="error_img" id="password1_img"></span></td>
						<td>
						<input class="padding-input-box text11ref inputUser required" type="password"  minlength="6"  size="20" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1" placeholder="Password" >
						<a class="show-password">Show</a></td>
					</tr>

					<tr>
					<td></td>
					<td><span class="bodyCopySULNote" style="color:#B8B894;font-size:11px;">Security Question:What is your town of birth or mother's maiden name?</span>
					</td>
					</tr>
					<% if (result.hasError(EnumUserInfoName.PASSWORD_HINT.getCode())) { %><tr><td>&nbsp;</td><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>
					<td>
					<!-- span id should be the input box id+"_img" -->
					<span class="error_img" id="secret_answer_img"></span></td>
					<td class="bodyCopySUL"><input class="padding-input-box text11ref inputUser required" type="text" maxlength="25"  size="20" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" id="secret_answer"  placeholder="Security Answer" >
					</td>
					</tr>
									
					<tr>
					<td></td>
						<td style="padding-top: 10px;"><a onclick="document.litesignup.submit();"  class="butText" style="font-weight:bold;font-size:14px;">
						<input type="submit" id="signupbtn" maxlength="25" size="19" value="Create Account"> </a></td>
					</tr>
					
				</table>
				</form>
												
					</div>
					<!-- form_fields ends here -->
					
 <script type="text/javascript" language="javascript">
	
 $jq('#litesignup').validate(
 {
 		
	 errorElement: "div",
	 rules:{
             email:{
             required:true,
             email:true,
             },          
 		},
 		messages:{
            email:{
            required:"Required",
            email:"Incomplete e-mail Address",
            },
            
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
				
<div class="social-login-header">

	<p style="font-size:12px;font-weight: bold; font-family: Verdana, Arial, sans-serif;margin-right:35px">Or Sign Up with:
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
					oa.src = '//' + oneall_subdomain
							+ '.api.oneall.com/socialize/library.js';
					var s = document.getElementsByTagName('script')[0];
					s.parentNode.insertBefore(oa, s);

					/* This is an event */
					var my_on_login_redirect = function(args) {
						//alert("You have logged in with " + args.provider.name);
						return true;
					}

					/* Initialise the asynchronous queue */
					var _oneall = _oneall || [];

					/* Social Login Example */
					_oneall.push([ 'social_login', 'set_providers',
							[ 'facebook', 'google' ] ]);
					_oneall
							.push([ 'social_login', 'set_grid_sizes', [ 2, 2 ] ]);
					/* _oneall
							.push([ 'social_login', 'set_callback_uri',
									'http://127.0.0.1:7001/social/social_login_success.jsp' ]); */
				 _oneall.push([ 'social_login', 'set_callback_uri',
									       		'<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/social/social_login_success.jsp"  %>' ]);
					_oneall.push([ 'social_login', 'set_event',
							'on_login_redirect', my_on_login_redirect ]);
					_oneall.push([ 'social_login', 'do_render_ui',
							'social_login_demo' ]);
				</script>
               </div>
				<!-- social login ends here -->

				<div class="clear"></div>

				<div class="bottom-contents">

					<span class="text12">By signing up, you agree to the <a
						href="javascript:popup('/registration/user_agreement.jsp', 'large')"
						style="font-weight: normal; text-decoration: underline">Terms
							of Service</a></span><br> <br> <span class="bottom-links">
						<b>Already have an Account? 
						   <a href="#"
							 onclick="window.parent.FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp', width: 500, height: 350}) ">
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


</body>
	</html>
	<%
		}
	%>
</fd:SiteAccessController>
