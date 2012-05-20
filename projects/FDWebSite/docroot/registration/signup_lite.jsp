<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>

<%
	String successPage = "index.jsp";
	String serviceType = NVL.apply(request.getParameter("serviceType"), "HOME").trim();
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
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">
	
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
				window.location.href="/index.jsp";
			</script>
		<%		 
		} else {
			System.out.println("went to else part  on signup_liste.jsp?====================================================================================\n" );


	%>
	<div style="width:400px;height:auto;overflow-y: auto; overflow-x: hide;">
	<div id="top_image" style="float: left; padding-bottom: 10px; width: 100%;">
		<span class="text12">Already have a password? <a href="/login/login.jsp" style="text-decoration:none;">Log in now</a></span> <br/><br/>
		<img src="/media_stat/images/profile/signup_easy.jpg" border="0"/>
		<br /><span class="text9" style="color:gray;">Sign up now and receive promotional materials or to place your first order.</span>
	</div>
	<div class="fright hline" id="" style="width:100%;"><!-- --></div>
	<div id="form_feilds" style="">
		<form id="litesignup" name="litesignup" method="post" action="" style="padding: 0; margin: 0;">
			<input type="hidden" name="submission" value="done" />	
			<input type="hidden" name="actionName" value="ordermobilepref" />	
			<input type="hidden" name="successPage" value="<%= successPage %>" />
			<input type="hidden" name="terms" value="true" />
			<input type="hidden" name="LITESIGNUP" value="true" />
			
			<br /><br/>
			
			<table border="0" cellpadding="0" cellspacing="0">
			<tr><td><span class="bodyCopy">First Name <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="text" class="text11ref inputUser" maxlength="25" size="31" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" onfocus="fillVals(this.id, '','Enter your first name');" onblur="fillVals(this.id, 'Def','Enter your first name');" id="first_name"></td></tr>
			<tr><td><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Last Name <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" onfocus="fillVals(this.id, '','Enter your last name');" onblur="fillVals(this.id, 'Def','Enter your last name');" id="last_name"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Delivery Zip Code <span class="star">*</span> </span> </td></tr>
			<tr><td>
				<table><tr><td>
					<input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="zipcode" value="<%=zipcode%>" onfocus="fillVals(this.id, '','Enter your zip code');" onblur="fillVals(this.id, 'Def','Enter your zip code');" id="zipcode">
					</td><td>			
					&nbsp;<span class="bodyCopy"><input type="radio" name="serviceType" value="HOME" <%= (serviceType.equals("HOME"))?"checked":"" %>/>HOME&nbsp;<input type="radio" name="serviceType" value="CORPORATE" <%= (serviceType.equals("CORPORATE"))?"checked":"" %>/>OFFICE</span>
				</td></tr></table>
			</td></tr>
			<tr><td>
				<table><tr><td valign="top">
					<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
					</td><td valign="top">	
					 <fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_SERVICE_TYPE.getCode()%>' id='errorMsg'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;
				</td></tr></table>
			</td></tr>
			
			<tr><td><span class="bodyCopy">Email Address <span class="star">*</span> </span></td></tr>			
			<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>" onfocus="fillVals(this.id, '','Enter your email');" onblur="fillVals(this.id, 'Def','Enter your email');" id="email">
			<br/><span class="text9" style="color:gray;">This will be your User Name. You will use this to access the site. </span></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'>
			<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Confirm Email Address <span class="star">*</span> </span></td></tr>
			<tr><td><input type="text" class="text11ref inputDef" maxlength="128" size="31" name="<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>" value="<%=repeat_email%>" onfocus="fillVals(this.id, '','Verify your email');" onblur="fillVals(this.id, 'Def','Verify your email');" id="confirm_email"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>' id='errorMsg'>
			<span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Choose Password <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1">
			<br/><span class="text9" style="color:gray;">Must be atleast 4 characters. Passwords are case sensitive. </span></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Confirm Password <span class="star">*</span> </span> </td></tr>
			<tr><td><input type="password"  class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>" id="password1"></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><span class="bodyCopy">Security Question <span class="star">*</span> </span> <br/>
			<span class="text9" style="color:gray;">What is your town of birth or mother's  maiden name? </span></td></tr>
			<tr><td><input type="text"  maxlength="25" class="text11ref inputUser" size="31" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" onfocus="fillVals(this.id, '','Answer');" onblur="fillVals(this.id, 'Def','Answer');" id="secret_answer"><br /></td></tr>
			<tr><td><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>&nbsp;</td></tr>
			
			<tr><td><div class="fright hline" id="" style="width:100%;"><!-- --> </div></td></tr>
			
			<tr><td><span class="text9" style="color:gray;">By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp','large')" style="font-weight:normal;">Terms of use</a></span></td></tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td>
			<table style="" class="butCont fleft">
				<tbody><tr>
					<td class="butOrangeLeft"><!-- --></td>
					<td class="butOrangeMiddle"><a onclick="doOverlayWindowFormSubmit('/registration/signup_lite.jsp','litesignup'); return false;" href="#" class="butText" style="font-weight:bold;font-size:14px;">Sign Up &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="/media/images/buttons/button_orange_arrow.gif"></a></td>
					<td class="butOrangeRight"><!-- --></td>
				</tr>
			</tbody></table>
			</td></tr>
			</table>
		</form>
	</div>
	</div>
	<% } %>
	
</body>
</html>
<% } %>
</fd:SiteAccessController>
