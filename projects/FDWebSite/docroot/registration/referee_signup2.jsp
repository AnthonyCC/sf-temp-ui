<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
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
<%@ include file="/shared/template/includes/i_head_end.jspf" %>

	<!--  Added for Password Strength Display -->
    <script type="text/javascript" src="/assets/javascript/jquery-2.1.0.min.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.hint.js"></script>
	<script type="text/javascript" src="/assets/javascript/jquery.pwstrength.js"></script>
	<script type="text/javascript" src="/assets/javascript/scripts.js"></script>
	
	<script type="text/javascript">
        jQuery(function($) { $('#password1').pwstrength(); });
  	 </script>
    <!--  Added for Password Strength Display -->
    
    <!--  Added for Password Strength Display -->
    <link rel="stylesheet" type="text/css" href="/assets/css/common/reset1.css"/>
	<link rel="stylesheet" type="text/css" href="/assets/css/common/styles.css"/>
	<!--  Added for Password Strength Display -->
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>

<%CmRegistrationTag.setRegistrationLocation(session,"referee"); %>

	<fd:RegistrationController actionName='registerEx' successPage='<%= successPage %>' fraudPage='<%= failurePage %>' result='result'>
	<%
		String email = (String) session.getAttribute("REFERRAL_EMAIL");
		String repeat_email = NVL.apply(request.getParameter(EnumUserInfoName.REPEAT_EMAIL.getCode()), "");
		String firstname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_FIRST_NAME.getCode()), "");
		String lastname = NVL.apply(request.getParameter(EnumUserInfoName.DLV_LAST_NAME.getCode()), "");
		String password = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD.getCode()), "");
		String passwordhint = NVL.apply(request.getParameter(EnumUserInfoName.PASSWORD_HINT.getCode()), "");		

		if(FDReferralManager.isReferreSignUpComplete(email)) {
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
	
		<!--  Added for Password Strength Display -->
		<script language="javascript">
			var pass_strength;
			function IsEnoughLength(str,length)
			{
				if ((str == null) || isNaN(length))
		           return false;
		        else if (str.length < length)return false;
		        return true;
			}
			function HasMixedCase(passwd)
			{
		 		if(passwd.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/))
		           return true;
		        else 
		           return false;
			}
			function HasNumeral(passwd)
			{
				if(passwd.match(/[0-9]/))
		          return true;
		        else
		          return false;
			}
			function HasSpecialChars(passwd)
			{
		 		if(passwd.match(/.[!,@,#,$,%,^,&,*,?,_,~]/))return true;else return false;
			}
			function passwordChanged()
			{
				var pwd = document.getElementById("password1");
				if (pwd.value.length==0) pass_strength = "<span style='color:black'>Type Password</span>";
				else if (IsEnoughLength(pwd.value,6) && HasMixedCase(pwd.value) && HasNumeral(pwd.value) && HasSpecialChars(pwd.value))pass_strength = "<b><span style='color:green'/>Very Strong!</span></b>";
		        else if (IsEnoughLength(pwd.value,6) && HasMixedCase(pwd.value) && (HasNumeral(pwd.value) || HasSpecialChars(pwd.value)))pass_strength = "<b><span style='color:green'/>Strong!</span></b>";
		 		else if (IsEnoughLength(pwd.value,6) && HasNumeral(pwd.value))pass_strength = "<b><span style='color:orange'>Medium!</span></b>";
		 		else pass_strength = "<b><span style='color:red'>Weak!</span></b>";         
	            document.getElementById('strength').innerHTML = pass_strength;
			}
		</script>
		<!--  Added for Password Strength Display -->
	
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
			
			<!--  Added for Password Strength Display -->
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
					<input id="password1" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" required="true" size="21" class="password" title="Choose Password" data-indicator="pwindicator" type="password" style="height: 20px;">
					<span class="case-sensitive">Passwords are case sensitive</span>
				</div>
				<div id="pwindicator">
					   <div class="bar"></div>
					   <div class="label"></div>
				</div>
			</div><!-- // .subgroup -->			
			</div><!-- // .content-group -->
			</div><!-- // .container -->
			<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'> <span class="text11rbold"><br /><%=errorMsg%></span></fd:ErrorHandler>
			<!-- Added for Password Strength Display -->	
				
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
	</fd:RegistrationController>
	
</body>
</html>
