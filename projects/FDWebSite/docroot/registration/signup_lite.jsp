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
	System.out.println("\n\n\n"+user.getSelectedServiceType().getName()+"\n\n\n");
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
<html>
<head>
	<title>FreshDirect</title>
	<style>
		.star {
			color:orange;
		}
	</style>
	
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.1/jquery.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.1/jquery-ui.min.js"></script>
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.0/themes/base/jquery-ui.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.css" />
	<link rel="stylesheet" type="text/css" href="/assets/css/common/footer.css">
  <link rel="stylesheet" type="text/css" href="/assets/css/common/freshdirect.css">
  <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav_and_footer.css">

  
  <!--[if IE]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/footer.ie.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->
  <!--[if lte IE 6]>
  <link rel="stylesheet" type="text/css" href="/assets/css/common/globalnav.ie6.css?buildver=5b224e7e-1f1b-4429-902f-7dee6d79d5aa">
  <![endif]-->

  
  <link rel="stylesheet" type="text/css" href="/assets/css/global.css">
	<link rel="stylesheet" type="text/css" href="/assets/css/pc_ie.css">
	<script src="/assets/javascript/jquery/1.7.2/jquery.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/jquery/ui/1.8.18/jquery-ui.min.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/jquery/corner/jquery.corner.js" type="text/javascript" language="javascript"></script>
	<script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
	<script type="text/javascript" src="/assets/javascript/prototype.js"></script>
	
	<script src="/assets/javascript/scriptaculous/1.9.0/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
	<script type="text/javascript" src="/assets/javascript/modalbox.js"></script>
	
	<script type="text/javascript">

		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-20535945-1']);
		_gaq.push(['_setDomainName', '.freshdirect.com']);
		_gaq.push(['_trackPageview']);

		(function() { var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true; ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js'; var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s); })();

	</script>
	
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">
	<center>
	<%
		String email = NVL.apply(request.getParameter(EnumUserInfoName.EMAIL.getCode()), "");
		String repeat_email = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "");
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
		String passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");	
		String zipcode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");	

		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {
			//phew finally complete
			System.out.println("Did not come here on signup_liste.jsp?====================================================================================");
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
			System.out.println("went to else part  on signup_liste.jsp?====================================================================================\n" );
			
			if(user != null && "".equals(zipcode)) {
				zipcode = user.getZipCode();
			}

	%>
	<div style="width:450px;height:auto;overflow-y: auto; overflow-x: hide;">
	<div id="top_image" style="float: left; padding-bottom: 10px; width: 100%;">
		<span class="text12" style="float:left;">Already have a password? <a href="/login/login.jsp" style="text-decoration:none;">Log in now</a></span> <br/><br/>
		<img src="/media_stat/images/profile/signup_easy.jpg" border="0" style="float:left;"/>
		<br /><span class="text9" style="color:gray;float:left;width:370px;">Sign up now and receive promotional materials or to place your first order.</span>
	</div>
	<div class="fright hline" id="" style="width:100%;"><!-- --></div>
	<div id="form_feilds" style="float:left;">
		<form id="litesignup" name="litesignup" method="post" action="/registration/signup_lite.jsp" style="padding: 0; margin: 0;">
			<input type="hidden" name="submission" value="done" />	
			<input type="hidden" name="actionName" value="ordermobilepref" />	
			<input type="hidden" name="successPage" value="<%= successPage %>" />
			<input type="hidden" name="terms" value="true" />
			<input type="hidden" name="LITESIGNUP" value="true" />
			
			<table border="0" cellpadding="0" cellspacing="0">
			<tr><td><span class="bodyCopy" style="font-size:9px;">First Name <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="text" class="text11ref inputUser" maxlength="25" size="31" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" id="first_name"></td></tr>
			<tr><td><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Last Name <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" id="last_name"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Delivery Zip Code <span class="star">*</span> </span> </td></tr>
			<tr><td>
				<table><tr><td>
					<input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="zipcode" value="<%=zipcode%>" id="zipcode">
					</td><td>			
					&nbsp;<span class="bodyCopy" style="font-size:9px;"><input type="radio" name="serviceType" value="HOME" <%= (serviceType.equals("HOME"))?"checked":"" %>/>HOME&nbsp;<input type="radio" name="serviceType" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %>/>OFFICE</span>
				</td></tr></table>
			</td></tr>
			<tr><td>
				<table><tr><td valign="top">
					<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
					</td><td valign="top">	
					 <fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_SERVICE_TYPE.getCode()%>' id='errorMsg'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
				</td></tr></table>
			</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Email Address <span class="star">*</span> </span></td></tr>			
			<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" id="email">
			<br/><span class="text9" style="color:gray;">This will be your User Name. You will use this to access the site. </span></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'>
			<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Confirm Email Address <span class="star">*</span> </span></td></tr>
			<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>" value="<%=repeat_email%>" id="confirm_email"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>' id='errorMsg'>
			<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Choose Password <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1">
			<br/><span class="text9" style="color:gray;">Must be atleast 4 characters. Passwords are case sensitive. </span></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Confirm Password <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>" id="password1"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy" style="font-size:9px;">Security Question <span class="star">*</span> </span> <br/>
			<span class="text9" style="color:gray;">What is your town of birth or mother's  maiden name? </span></td></tr>
			<tr><td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>"  id="secret_answer"><br /></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><div class="fright hline" id="" style="width:100%;"><!-- --></div></td></tr>
			
			<tr><td><span class="text9" style="color:gray;">By signing up, you agree to the <a href="javascript:popWithInterval('/registration/user_agreement.jsp', false, '585', '400', 'Terms', false, true, false, false)" style="font-weight:normal;">Terms of use</a></span></td></tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td>
			<table style="" class="butCont fleft">
				<tbody><tr>
					<td class="butOrangeLeft"><!-- --></td>
					<td class="butOrangeMiddle"><a onclick="document.litesignup.submit();" href="#" class="butText" style="font-weight:bold;font-size:14px;">Sign Up &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="/media/images/buttons/button_orange_arrow.gif"></a></td>
					<td class="butOrangeRight"><!-- --></td>
				</tr>
			</tbody></table>
			</td></tr>
			</table>
		</form>
	</div>
	</div>
	<% } %>
	</center>
</body>
</html>
<% } %>
</fd:SiteAccessController>
