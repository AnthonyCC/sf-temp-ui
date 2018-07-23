<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import="com.freshdirect.storeapi.content.ContentFactory" %>
<%@ page import="com.freshdirect.fdstore.EnumEStoreId" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%
	String successPage = NVL.apply(request.getParameter("successPage"), "").trim();
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
	String corpServiceType = NVL.apply(request.getParameter("corpServiceType"), "").trim();
    boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	
    String failurePage = "/registration/referee_signup.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;	
%>
<fd:RegistrationController actionName='registerEx' successPage='<%= successPage %>' fraudPage='<%= failurePage %>' result='result'>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
    <%--  <title>FreshDirect</title>  --%>
     <fd:SEOMetaTag title="FreshDirect"/>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">

	
	<%
		String email = (String) session.getAttribute("REFERRAL_EMAIL");
		String repeat_email = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "");
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
		String passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");		
		EnumEStoreId storeid = ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId();
		if(FDReferralManager.isReferreSignUpComplete(email, storeid)) {
			//phew finally complete
			System.out.println("Did not come here?====================================================================================");

			//set a session attribute so we know registration completed successfully
			session.setAttribute("regSuccess", true);
		%>
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			<script language="javascript">
				window.location.href="/index.jsp";
			</script>
		<%		 
		} else {
			System.out.println("went to else part?====================================================================================\n" +session.getAttribute("REFERRAL_EMAIL") + "\n" + (String)session.getAttribute("RAFREGISTRATION"));
			
			if(session.getAttribute("MSG_FOR_LOGIN_PAGE") != null) {

			%>
				<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
				<script language="javascript">
					window.location.href="/login/login_main.jsp?successPage=%2Findex.jsp";
				</script>
			<%	
			} else {

	%>
	<div style="width:400px;height:530px;overflow-y: auto; overflow-x: hide;">
	<div id="top_image" style="float: left; padding-bottom: 10px; width: 100%;">
		<img src="/media_stat/images/profile/signup_easy.jpg" border="0" alt="Sign Up, It's Easy"/>
		<br /><span class="text9">Sign up now to enjoy great quality food, delivered to your door.</span>
	</div>
	<div class="fright hline" id="" style="background-color: #ccc;"><!-- --></div>
	<div id="form_feilds" style="">
		<form id="refaddress" name="refaddress" method="post" action="" style="padding: 0; margin: 0;">
			<input type="hidden" name="submission" value="done" />	
			<input type="hidden" name="actionName" value="ordermobilepref" />	
			<input type="hidden" name="successPage" value="<%= successPage %>" />
			<input type="hidden" name="serviceType" value="<%= serviceType %>" />
			<input type="hidden" name="corpServiceType" value="<%= serviceType %>" />
			<input type="hidden" name="email" value="<%= email %>" />
			<input type="hidden" name="terms" value="true" />
			<input type="hidden" name="<%=EnumUserInfoName.REPEAT_PASSWORD.getCode()%>" />
			<%
				if(session.getAttribute("REFERRAL_ADDRESS") != null) {
					AddressModel address = (AddressModel) session.getAttribute("REFERRAL_ADDRESS");
					String address1 = address.getAddress1();
					String add2 = address.getAddress2();
					String city = address.getCity();
					String state = address.getState();
					String zipcode = address.getZipCode();
			%>
				<input type="hidden" name="address1" value="<%= address1 %>" />
				<input type="hidden" name="address2" value="<%= add2 %>" />
				<input type="hidden" name="city" value="<%= city %>" />
				<input type="hidden" name="state" value="<%= state %>" />
				<input type="hidden" name="zipcode" value="<%= zipcode %>" />
			<% } %>
			<br />
			<span class="bodyCopy">Email Address <span class="star">*</span></span>
			<br /><%=email%>
			
			<br /><br />
			<span class="bodyCopy">Confirm Email Address <span class="star">*</span> </span>
			<br /><input type="text" class="text11ref inputDef" maxlength="128" size="21" name="<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>" value="<%=repeat_email%>" onfocus="fillVals(this.id, '','Verify your email');" onblur="fillVals(this.id, 'Def','Verify your email');" id="confirm_email">
			<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>' id='errorMsg'>
			<br /><div class="text11rbold" style="width:300px;text-align: left;"><%=errorMsg%></div>
			</fd:ErrorHandler>
			
			<br /><br />
			<span class="bodyCopy">Password <span class="star">*</span> </span> <br />
			<input type="password"  class="text11ref inputUser" size="21" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			
			<br /><br />
			<span class="bodyCopy">First Name <span class="star">*</span> </span> <br />
			<input type="text" class="text11ref inputUser" maxlength="25" size="21" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" onfocus="fillVals(this.id, '','Enter your first name');" onblur="fillVals(this.id, 'Def','Enter your first name');" id="first_name"><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			
			<br /><br />
			<span class="bodyCopy">Last Name <span class="star">*</span> </span> <br />
			<input type="text"  maxlength="25" class="text11ref inputUser" size="21" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" onfocus="fillVals(this.id, '','Enter your last name');" onblur="fillVals(this.id, 'Def','Enter your last name');" id="last_name"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			
			<br /><br />
			<span class="bodyCopy">Security Question <span class="star">*</span> </span> <br />
			<span class="text12">What is your town of birth or mother's  maiden name? </span><br /><br />
			<input type="text"  maxlength="25" class="text11ref inputUser" size="10" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" onfocus="fillVals(this.id, '','Answer');" onblur="fillVals(this.id, 'Def','Answer');" id="secret_answer"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			<br /><br />
			<div class="fright hline" id=""><!-- --></div>
			
			<br />
			<span class="text9">By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp','large')" style="font-weight:normal;">Terms of use</a></span><br /><br />
			<a href="#" onclick="doRemoteOverlay1('referee_signup2.jsp'); return false;"><img src="/media_stat/images/profile/start_shopping.jpg" border="0"></a>
		</form>
	</div>
	</div>
	<% } } %>
	
</body>
</html>
</fd:RegistrationController>