<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>

<%
	String successPage = NVL.apply(request.getParameter("successPage"), "").trim();
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
	String corpServiceType = NVL.apply(request.getParameter("corpServiceType"), "").trim();
    boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	
    String failurePage = "/registration/referee_signup.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;	
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>FreshDirect</title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%-- NOT CCL @ include file="/shared/template/includes/ccl.jspf" --%>
</head>
<style>
	.text11 {
		width: 70%;
		height: 20px;
		font-size: 12px;
		line-height: 12px;
		color: #666;
	}
	
	.bodyCopy {
		font-size: 11px;
		color: #000;
		text-align: left;
		font-weight: bold;
		line-height:25px;
	}
	
	.text9 {
		font-size: 11px;
		color: #928F8E;
		text-align: left;
	}
	
	a:active {
		color:#336600;
		text-decoration:none;
	}
	
	.text12 {
		font-family: Verdana,Arial,sans-serif;
		font-size: 11px;
		font-weight: normal;
	}
	
	.star {
		color: #FF9900;
	}
	
	.inputDef {
		width: 70%;
		height: 20px;
		font-size: 12px;
		line-height: 12px;
		color: #999;
	}
	
	.inputUser {
		width: 70%;
		height: 20px;
		font-size: 12px;
		line-height: 12px;
		color: #666;
	}
	
	.hline {
			background: url("/media/editorial/site_access/images/dots_h.gif") repeat-x scroll 0 0 transparent;
			font-size: 1px;
			height: 1px;
			line-height: 1px;
			width: 400px !important;
		}
		
		.fright {
			float: right;
		}
</style>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">
<%
	String email = (String) session.getAttribute("REFERRAL_EMAIL");

		if(FDReferralManager.isReferreSignUpComplete(email)) {
			//phew finally complete
			System.out.println("Did not come here?====================================================================================");
		%>
			<script language="javascript">
				window.location.href="/index.jsp";
			</script>
		<%		 
	} else {
		System.out.println("went to else part?====================================================================================\n" +session.getAttribute("REFERRAL_EMAIL") + "\n" + (String)session.getAttribute("RAFREGISTRATION"));
%>


	<fd:RegistrationController actionName='registerEx' successPage='<%= successPage %>' fraudPage='<%= failurePage %>' result='result'>
	<%
		String repeat_email = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "");
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
		String passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");		

	%>
	<div style="width:400px;height:530px;overflow-y: auto; overflow-x: hide;">
	<div id="top_image" style="float:left;padding-bottom:10px;width:100%;">
		<img src="/media_stat/images/profile/signup_easy.jpg" border="0"/>
		<br/><span class="text9">Sign up now to enjoy great quality food, delivered to your door.</span>
	</div>
	<div class="fright hline" id=""><!-- --></div>
	<div id="form_feilds" style="float:left">
		<form id="refaddress" name="refaddress" method="post" action="">
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
			<br/>
			<span class="bodyCopy">Email Address <span class="star">*</span></span>
			<br/><%=email%>
			
			<br/><br/>
			<span class="bodyCopy">Confirm Email Address <span class="star">*</span> </span>
			<br/><input type="text" class="text11 inputDef" maxlength="128" size="21" name="<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>" value="<%=repeat_email%>" onfocus="fillVals(this.id, '','Verify your email');" onblur="fillVals(this.id, 'Def','Verify your email');" id="confirm_email"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.REPEAT_EMAIL.getCode()%>' id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			
			<br/><br/>
			<span class="bodyCopy">Password <span class="star">*</span> </span> <br/>
			<input type="password"  class="text11" size="21" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" id="password1"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><br/><%=errorMsg%></span></fd:ErrorHandler>
			
			<br/><br/>
			<span class="bodyCopy">First Name <span class="star">*</span> </span> <br/>
			<input type="text" class="text11" maxlength="25" size="21" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" value="<%=firstname%>" onfocus="fillVals(this.id, '','Enter your firstname');" onblur="fillVals(this.id, 'Def','Enter your firstname');" id="first_name"><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_FIRST_NAME.getCode()%>" id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
			
			<br/><br/>
			<span class="bodyCopy">Last Name <span class="star">*</span> </span> <br/>
			<input type="text"  maxlength="25" class="text11" size="21" name="<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>" value="<%=lastname%>" onfocus="fillVals(this.id, '','Enter your lastname');" onblur="fillVals(this.id, 'Def','Enter your lastname');" id="last_name"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_LAST_NAME.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			
			<br/><br/>
			<span class="bodyCopy">Security Question <span class="star">*</span> </span> <br/>
			<span class="text12">What is your town of birth or mother's  maiden name? </span><br/><br/>
			<input type="text"  maxlength="5" class="text11" size="10" name="<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>" onfocus="fillVals(this.id, '','Answer');" onblur="fillVals(this.id, 'Def','Answer');" id="secret_answer"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD_HINT.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			<br/><br/>
			<div class="fright hline" id=""><!-- --></div>
			
			<br/>
			<span class="text9">By signing up, you agree to the <a href="javascript:popup('/registration/user_agreement.jsp','large')" style="font-weight:normal;">Terms of use</a></span><br/><br/>
			<a href="#" onclick="doRemoteOverlay1('referee_signup2.jsp'); return false;""><img src="/media_stat/images/profile/start_shopping.jpg" border="0"></a>
		</form>
	</div>
	</div>
	<script language="javascript">
		setFormDefaults();
	</script>
	</fd:RegistrationController>
	<% } %>
</body>
</html>
