<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*"%>
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
	
    String failurePage = "/registration/signup_lite.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;
    
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>FreshDirect</title>
	<style>
		.star {
			color: #F99E40;
		}
		.bodyCopySUL {
			font-size: 11px;
			font-weight: bold;
			padding-top: 4px;
			padding-bottom: 4px;
			
		}
		.bodyCopySULNote {
			color: #808080;
		}
	</style>
	
	
	
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.1/jquery-ui.min.js"></script>

    <link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/base/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/common/footer.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/freshdirect.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav_and_footer.css">
    
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->
  
  <!--[if IE]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/footer.ie.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->
  <!--[if lte IE 6]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.ie6.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->

  
  <link rel="stylesheet" type="text/css" href="/assets/css/global.css">
  <link rel="stylesheet" type="text/css" href="/assets/css/pc_ie.css">
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
 
    <!--  Referred  jquery-1.7.2.min.js from FD code base-->
	<!-- <script type="text/javascript" src="/docroot/common/js/jquery-1.7.2.min.js"></script> -->
	<!--<script type="text/javascript" src="/assets/javascript/jquery.hint.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.pwstrength.js"></script>-->
	
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	
	<script type="text/javascript">
        jQuery(function($jq) { $jq('#password1').pwstrength(); });
  	 </script>
    <!--  Added for Password Strength Display -->
    
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0" style="">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>


	<center>
  <div style="text-align: left; width: 450px; margin-bottom: 12px;" class="text12">
    <b>Already have a password? <a href="javascript:(function() { (window.parent || window).location.href='/login/login.jsp'; return false; })()">Log in now</a></b>
  </div>
	<%
		String email = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "");
		String repeat_email = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "");
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
		String passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");	
		String zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	
		String posn = "right";

		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {
			//phew finally complete
			//System.out.println("Did not come here on signup_liste.jsp?====================================================================================");
		%>
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			<script language="javascript">
				//alert('in signup_lite.jsp');
				  //  if (top === window) {
					//	alert("this page is not in an iframe");
					///} else {
						//alert("the url of the top is" + top.location.href + "\nand not the url of this one is " + window.location.href );
					//}
				window.top.location="/index.jsp";
			</script>
					
		<%		 
		} else {
			//System.out.println("went to else part  on signup_liste.jsp?====================================================================================\n" );
			
			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
			
		<div id="sulCont" style="width:450px; height:auto; overflow-y: auto; overflow-x: hidden;">
		<div id="top_image" style="padding-bottom: 10px; width: 100%; text-align: left;">
			<img src="/media_stat/images/profile/signup_easy.jpg" border="0" alt="Sign Up, It's Easy"/><br />
			<span class="text9" style="color:gray; width:370px;">Sign up now to receive promotional materials or place your first order.</span>
		</div>
		<div class="fright hline" id="" style="width:100%; float:left;"><!-- --></div>
		<div id="form_feilds" style="float:left;">
			<form id="litesignup" name="litesignup" method="post" action="/registration/signup_lite.jsp" style="padding: 0; margin: 0;">
				<input type="hidden" name="submission" value="done" />	
				<input type="hidden" name="actionName" value="ordermobilepref" />	
				<input type="hidden" name="successPage" value="<%= successPage %>" />
				<input type="hidden" name="terms" value="true" />
				<input type="hidden" name="LITESIGNUP" value="true" />
				
				<table border="0" cellpadding="0" cellspacing="0">
					<tr><td class="bodyCopySUL"><span><label for="first_name">First Name </label><span class="star">*</span> </span> </td></tr>
					<tr><td><input type="text" class="text11ref inputUser" maxlength="25" size="31" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" id="first_name"></td></tr>
					<% if (result.hasError(EnumUserInfoName.DLV_FIRST_NAME.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					<tr><td class="bodyCopySUL"><span><label for="last_name">Last Name </label><span class="star">*</span></span></td></tr>
					<tr><td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" id="last_name"></td></tr>
					<% if (result.hasError(EnumUserInfoName.DLV_LAST_NAME.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					<tr><td class="bodyCopySUL"><span><label for="zipcode">Delivery Zip Code </label><span class="star">*</span></span></td></tr>
					<tr><td>
						<table>
						<tr>
							<td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="zipcode" value="<%=zipcode%>" id="zipcode"></td>
							<td>&nbsp;<span class="bodyCopySUL"><input type="radio" name="serviceType" value="HOME" id="home_delivery" <%= (serviceType.equals("HOME"))?"checked":"" %>/><label for="home_delivery">HOME&nbsp;</label><input type="radio" name="serviceType" value="CORPORATE" id="office_delivery" <%= (serviceType.equals("CORPORATE"))?"checked":"" %>/><label for="office_delivery">OFFICE</label></span></td>
						</tr>
						</table>
					</td></tr>
					<% if (result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) || result.hasError(EnumUserInfoName.DLV_SERVICE_TYPE.getCode())) { %>
						<tr><td class="errMsg">
							<table>
								<tr>
									<td valign="top"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td>
									<td valign="top"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_SERVICE_TYPE.getCode()%>' id='errorMsg'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
							</table>
							
						</td></tr>
					<% } %>
					
					<tr><td class="bodyCopySUL"><span><label for="email">Email Address </label><span class="star">*</span> </span></td></tr>			
					<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" id="email">
					<br/><span class="text9 bodyCopySULNote">This will be your User Name. You will use this to access the site. </span></td></tr>
					<% if (result.hasError(EnumUserInfoName.EMAIL.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					<tr><td class="bodyCopySUL"><span><label for="confirm_email">Confirm Email Address </label><span class="star">*</span> </span></td></tr>
					<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>" value="<%=repeat_email%>" id="confirm_email"></td></tr>
					<% if (result.hasError(EnumUserInfoName.REPEAT_EMAIL.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><%posn="center";%></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					
					
					<tr><td class="bodyCopySUL"><span><label for="password1">Choose Password </label><span class="star">*</span> </span> </td></tr>
					
					<tr>
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
									<input id="password1" name="password" size="31" class="password" data-indicator="pwindicator" type="password" style="height: 20px;">
									<span class="case-sensitive">Passwords are case sensitive</span>
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
					<!--  
					<tr>
					    <td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1" onkeyup="passwordChanged();" >
						<span id="strength">Type Password</span>
						<br/><span class="text9 bodyCopySULNote">Must be at least 6 characters. Passwords are case sensitive. </span></td>
					</tr>
					-->
					<% if (result.hasError(EnumUserInfoName.PASSWORD.getCode())) { %><tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					
					
					<tr><td class="bodyCopySUL"><span><label for="password2">Confirm Password </label><span class="star">*</span> </span> </td></tr>
					<tr><td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>" id="password2"></td></tr>
					<% if (result.hasError(EnumUserInfoName.REPEAT_PASSWORD.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span><%posn="center";%></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					<tr><td class="bodyCopySUL"><span><label for="secret_answer">Security Question </label><span class="star"></span></span></td></tr>
					<tr><td><span class="text9 bodyCopySULNote">What is your town of birth or mother's maiden name?</span></td>
					</tr>
					
					<tr><td class="bodyCopySUL"><input type="text" maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" id="secret_answer"></td></tr>
					<% if (result.hasError(EnumUserInfoName.PASSWORD_HINT.getCode())) { %><tr><td class="errMsg"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr><% } %>
					
					<tr><td style="padding-top: 4px;"><!-- --></td></tr>
					<tr><td><div class="fright hline" id="" style="width:100%;"><!-- --></div></td></tr>
					
					<tr><td style="padding-top: 10px;"><span class="text11 bodyCopySULNote">By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp', 'large')">Customer Agreement</a> & <a href="javascript:popup('/registration/privacy_policy.jsp', 'large')">Privacy Policy</a></span></td></tr>
					
					<tr>
						<td style="padding-top: 10px;"><a onclick="document.litesignup.submit();" href="#" class="butText" style="font-weight:bold;font-size:14px;"><img alt="Sign Up" src="/media_stat/images/buttons/signup.gif"></a></td>
					</tr>
					<tr><td style="font:Verdana;font-weight:bold;font-size:10px;padding:10px;">Having problems signing up? Call <%=(user == null)?"1-212-796-8002":user.getCustomerServiceContact()%></td></tr>
				</table>
			</form>
		</div>
	</div>
		<% if (result.isFailure()) { %>
			<script type="text/javascript">
				setFrameHeightSL('signupframe', $jq('#sulCont').height());
				window.top.Modalbox_setWidthAndPosition();
			</script>
		<% } %>
	<% } %>
	</center>
</body>
</html>
<% } %>
</fd:SiteAccessController>
