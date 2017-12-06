<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*,java.util.HashMap"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>


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
    
    CmRegistrationTag.setRegistrationLocation(session,"signup lite");
%>	

<fd:SiteAccessController action='signupLite' successPage='<%= successPage %>' moreInfoPage='' failureHomePage='<%= failurePage %>' result='result'>	

<%
		if(session.getAttribute("morepage") != null) {
			String mPage = (String) session.getAttribute("morepage");
	%>
		<jsp:include page="<%= mPage %>" flush="false"/>
	<%
		} else {
	%>

<!DOCTYPE html>
<html lang="en-US" xml:lang="en-US">
<head>

   <%--  <title>FreshDirect</title> --%>
      <fd:SEOMetaTag title="FreshDirect"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
	<fd:css href="/assets/css/social_connected.css" />	
		
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>

	<%@ include file="/shared/template/includes/i_body_start.jspf" %>


<!-- enable/disable the submit button based on valid entries -->
<script type="text/javascript" language="javascript">

	$jq(function(){
		// Hide all the error arrow images
		//$jq('.error_img').hide();
		// disable the submit button on page load
		$jq('#signupbtn').attr('disabled', true);
		// on keyup we will check every time the form is valid or not
		$jq('#litesignup').bind('change keyup', function() {
		
		    if($jq(this).validate().checkForm()) { // form is valid
		        $jq('#signupbtn').removeClass('button_disabled').attr('disabled', false);
		        
		    } else { // form is invalid
		        $jq('#signupbtn').addClass('button_disabled').attr('disabled', true);
		    }
		});
	
		$jq('#litesignup').valid();
	});
</script>



	
</head>

<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="10" topmargin="10" >

	<center>
	<div class="container">
	<%!
	
	HashMap socialUser = null;
	String email="",firstname="",lastname="",repeat_email="",password="",passwordhint="",zipcode="",posn="",pageHeaderMesssage="",pageHeaderMessage1="",provider="";
	String lastPage =""; 
	
	%>

	<%
	
      socialUser = (HashMap)session.getAttribute(SessionName.SOCIAL_USER);
	  lastPage = (String)session.getAttribute("lastpage");
	  
	 
	  if(lastPage != null)
	  	session.removeAttribute("lastpage");
	  
	  String arrowImg = "<img src='/media_stat/images/common/black_arrow.png' alt='arrow_graphic'>";
		

    	if(socialUser != null)    	{
    		  
    		if(lastPage == null || !lastPage.equals("signup_lite"))
    		{
    			pageHeaderMesssage="<b>Oops. We don't recognize you.</b><br>Fill in the "+arrowImg+" blanks to create a new account";
    		}
    		else if(lastPage.equals("signup_lite"))
    		{
    			pageHeaderMesssage="<b>Connected! Fill in the "+arrowImg+" blanks to finish</b>";
    			
    		}
    		
    		email = (String)socialUser.get("email");
    		provider = (String)socialUser.get("provider");
    		String displayName = (String)socialUser.get("displayName"); 
    		String names[] = displayName.split(" ");
    		firstname = (names.length ==0) ? "" : names[0];
    		lastname = (names.length <= 1) ? "" : names[names.length -1];
    		password="";
    		passwordhint="";
    		zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	
    		posn = "right";

    	}
    	
    	
    	
	
		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {
			//phew finally complete
			//System.out.println("Did not come here on signup_liste.jsp?====================================================================================");
		%>
			<div style="width:500px;">
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" style="align:center;"/>
			</div>
			<script language="javascript">
				window.top.location="/index.jsp";
			</script>
		<%		 
		} else {
			//System.out.println("went to else part  on signup_liste.jsp?====================================================================================\n" );
			
			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
	
	
	<div id="sulCont" class="signup-style" style="height:650px;">
			
			<span style="font-size:13px;font-family:Verdana, Arial, sans-serif;margin-top:20px;margin-right:30px;margin-left:50px;float:left;text-align:left;"> <%= pageHeaderMesssage %></span>	
			
	<div class="form-side" style="margin-top:10px;">
		
		<div id="form_feilds" >

		
			<form id="litesignup" name="litesignup" method="post" action="/social/signup_lite_social.jsp" >
				<input type="hidden" name="submission" value="done" />	
				<input type="hidden" name="actionName" value="ordermobilepref" />	
				<input type="hidden" name="successPage" value="<%= successPage %>" />
				<input type="hidden" name="terms" value="true" />
				<input type="hidden" name="LITESIGNUP" value="true" />
				
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
							<td><input class="padding-input-box text11ref inputUser required" type="number"  maxlength="5" class="" size="21" name="zipcode" value="<%=zipcode%>" id="zipcode" placeholder="Delivery ZIP Code"  >
							</td>
                        </tr>
                        <tr>
							
							<td>&nbsp;</td>
							<td>
							<input type="radio" name="serviceType" value="HOME" <%= (serviceType.equals("HOME"))?"checked":"" %>/>HOME&nbsp;
							<input type="radio" name="serviceType" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %>/>OFFICE
							</td>
							<!-- <td id="zipcodeError" class="red">&nbsp;</td> -->
						</tr>
										
					<% if (result.hasError(EnumUserInfoName.DLV_FIRST_NAME.getCode())) { %><tr><td class="errMsg "><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>
					 <td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="first_name_img">&nbsp;</span></td>	
					<td>
					<input class="padding-input-box text11ref inputUser required" type="text" maxlength="25" size="21" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" id="first_name" placeholder="First Name" style="position:relative">
					</td></tr>
					
					
					<% if (result.hasError(EnumUserInfoName.DLV_LAST_NAME.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>
					 <td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="last_name_img">&nbsp;</span></td>	
					<td><input class="padding-input-box text11ref inputUser required" type="text"  maxlength="25" size="21" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" id="last_name" placeholder="last Name" >
					</td></tr>
					
					
					
					<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %><tr><td>&nbsp;</td><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>													
					<tr>
					 <td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="email_img">&nbsp;</span>
					</td>	
					<td><input class="padding-input-box text11ref inputDef required" aria-label="email" type="email"  maxlength="128" size="21" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" id="email" placeholder="E-mail">
					</td></tr>
					
					
					<% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %><tr><td>&nbsp;</td><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'>
						 <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
				    <tr>
						
						<td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="password1_img">&nbsp;</span>
						</td>
						
						<td>
						 	
						<input class="padding-input-box text11ref inputUser required" aria-label="password" type="password"  size="21" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1" placeholder="Password">	
						</td>						
					</tr>
			
					<tr>
					<td>&nbsp;</td>
					<td><span class="text9 bodyCopySULNote" style="color:#B8B894;font-size:11px;">Security Question:What is your town of birth or mother's maiden name?</span>
					</td></tr>
					<% if (result.hasError(EnumUserInfoName.PASSWORD_HINT.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					<tr>
					 <td>
							<!-- span id should be the input box id+"_img" -->
							<span class="error_img" id="secret_answer_img">&nbsp;</span>
					</td>	
					<td class="bodyCopySUL"><input class="padding-input-box text11ref inputUser required" type="text" maxlength="25"  size="21" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" id="secret_answer"  placeholder="Security Answer">
					</td></tr>

					<tr>
					<td>&nbsp;</td>
						<td style="padding-top: 10px;"><a onclick="document.litesignup.submit();" href="#" class="butText" style="font-weight:bold;font-size:14px;">
						<input type="submit" id="signupbtn" maxlength="25" size="19" value="Create Account"> </a></td>
					</tr>
					
							
				</table>
								
			</form>
			</div> <!-- form_fields ends here -->	
			

<!-- front end form validations starts here-->		

<script type="text/javascript" language="javascript">
	$jq('#litesignup').validate({
		errorElement: "div",
	 	rules:{			
	    	email:{
				required:true,
				email:true,
			},
		},
		messages:{
			required: "Required",
			remote: "E-mail already in use.Log in?.",
			email: "Incomplete e-mail address.",
			maxlength: $.validator.format( "ZIP must be {0} numbers only." ),
			minlength: $.validator.format( "Must be {0} or more characters." )
		},
		highlight: function(element, errorClass,validClass) {
			$jq(element).addClass(errorClass).removeClass(validClass);
			$jq(element.form).find("span[id=" + element.id + "_img]").addClass('show_bg_arrow');        
		},
	    unhighlight: function(element,errorClass,validClass) {
			$jq(element).removeClass(errorClass).addClass(validClass);
			$jq(element.form).find("span[id=" + element.id + "_img]").removeClass('show_bg_arrow');
		},
		wrapper: "div",
		errorPlacement: function(error, element) {
			//error.insertBefore(element);
		},
		submitHandler: function(form) {}
	});
</script> <!-- front end validations end here -->	
			
	
	</div><!--  form-side ends here -->													
		
		
	
	
	<div id="social_login_demo" class="social-login">
								
			<img src="/media_stat/images/common/<%=provider%>.PNG" alt="<%=provider%>_image" width="100px" height="34px" style="margin-top:23px;">
			
		
	
	</div> <!-- social login ends here -->

	

	<div class="clear"></div>
	
	<div class="bottom-contents">
		
			<span class="text12">By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp', 'large')">Customer Agreement</a> & <a href="javascript:popup('/registration/privacy_policy.jsp', 'large')">Privacy Policy</a></span><br><br>
			
			<span class="bottom-links">
   			 <b>Already have a password? 
   			  <a href="#"
							 onclick="window.parent.FreshDirect.components.ifrPopup.close(); window.parent.FreshDirect.modules.common.login.socialLogin(); ">
							 Log in
			  </a>
   			 
   			 </b>
  			 </span>
	</div>
	
	</div>
 		<% if (result.isFailure()) { %>
			<script type="text/javascript">
				setFrameHeightSL('signupframe', $jq('#sulCont').height());
				window.top.Modalbox_setWidthAndPosition();
			</script>
		<% } %>
	<% } %>
	
	
	
	
  </div> <!-- container ends here -->	
	</center>
	

</body>
</html>
<% } %>
</fd:SiteAccessController>
