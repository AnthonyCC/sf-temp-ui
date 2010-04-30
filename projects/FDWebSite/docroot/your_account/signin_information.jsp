<%@ page import="java.text.MessageFormat" %>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@page import="com.freshdirect.common.address.PhoneNumber"%><fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - User Name, Password, & Contact Info</tmpl:put>
    <tmpl:put name='content' direct='true'>
<script type="text/javascript" src="/assets/javascript/phone_number.js"></script>

<fd:RegistrationController actionName='<%=request.getParameter("actionName")%>' result='result'>

<%
// go get the customerinformation
String lastName = "";
String firstName = "";
String homePhone = "";
String homePhoneExt = "";
String busPhone = "";
String busPhoneExt = "";
String cellPhone = "";
String cellPhoneExt = "";
String email = "";
String password = "";
String otherEmail = "";
String title = "";
String sendHtmlEmail = "";
String sendPlainTextEmail = "";
String sendNewsletter = "";
String sendOptinNewsletter = "";

String sendNewsLetter = "";
String userName = "";

String workDept = "";
String employeeId = "";

String receive_emailLevel =""; //
String noContactMail = ""; //
String noContactPhone = ""; //


FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
//System.out.println("This is the CM in the JSP:  " + cm.isReceiveNewsletter());

lastName = cm.getLastName();
firstName = cm.getFirstName();
email = cm.getEmail();
otherEmail = cm.getAlternateEmail();
homePhone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
homePhoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();

busPhone = cm.getBusinessPhone()==null?"":cm.getBusinessPhone().getPhone();
busPhoneExt = cm.getBusinessPhone()==null?"":cm.getBusinessPhone().getExtension();

cellPhone = cm.getCellPhone()==null?"":cm.getCellPhone().getPhone();
cellPhoneExt = cm.getCellPhone()==null?"":cm.getCellPhone().getExtension();

title = cm.getTitle();

workDept = cm.getWorkDepartment()==null?"":cm.getWorkDepartment();
employeeId = cm.getEmployeeId();

if (request.getParameter(EnumUserInfoName.EMAIL.getCode())!=null){
    email = request.getParameter(EnumUserInfoName.EMAIL.getCode());
}
if (request.getParameter(EnumUserInfoName.PASSWORD.getCode())!=null){
    password = request.getParameter(EnumUserInfoName.PASSWORD.getCode());
}

if (request.getParameter("last_name")!=null){
    lastName = request.getParameter("last_name");
}
if (request.getParameter("first_name")!=null) {
    firstName = request.getParameter("first_name");
}
if (request.getParameter("title")!=null) {
    title = request.getParameter("title");
}
if (request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode())!=null) {
    otherEmail = request.getParameter(EnumUserInfoName.ALT_EMAIL.getCode());
}

if (request.getParameter("homephone")!=null) {
    homePhone = PhoneNumber.format(request.getParameter("homephone"));
}
if (request.getParameter("ext")!=null) {
    homePhoneExt = homePhone == null || homePhone.length() == 0 ? "" : request.getParameter("ext");
}

if (request.getParameter("busphone")!=null) {
    busPhone = PhoneNumber.format(request.getParameter("busphone"));
}
if (request.getParameter("busphoneext")!=null) {
    busPhoneExt = busPhone == null || busPhone.length() == 0 ? "" : request.getParameter("busphoneext");
}

if (request.getParameter("cellphone")!=null) {
    cellPhone = PhoneNumber.format(request.getParameter("cellphone"));
}
if (request.getParameter("cellphoneext")!=null) {
    cellPhoneExt = cellPhone == null || cellPhone.length() == 0 ? "" : request.getParameter("cellphoneext");
}

if (request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode())!=null) {
    workDept = request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode());
}

if (request.getParameter("isSendHTMLEmail")!=null) {
    sendHtmlEmail = request.getParameter("isSendHTMLEmail");
}
if (request.getParameter("receive_mail")!=null) {
    sendNewsLetter = request.getParameter("receive_mail");
}

sendNewsLetter = cm.isReceiveNewsletter()?"yes":"no";

sendPlainTextEmail= cm.isEmailPlaintext()?"yes":"no";
if ("yes".equalsIgnoreCase(sendPlainTextEmail)) {
    sendPlainTextEmail = "checked";
} else sendPlainTextEmail = "";

sendNewsletter = cm.isReceiveNewsletter() ? "checked" : "";
sendOptinNewsletter = cm.isReceiveOptinNewsletter() ? "checked" : "";

if (request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode()) != null) {
    workDept = request.getParameter(EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode());
}

if (request.getParameter("employeeId") != null) {
    employeeId = request.getParameter("employeeId");
}

//email preference level
	if (request.getParameter("receive_emailLevel")!=null) {
		receive_emailLevel = request.getParameter("receive_emailLevel");
	}
	receive_emailLevel = cm.getEmailPreferenceLevel();
	//if (receive_emailLevel == null) { receive_emailLevel = "2"; }

//no contact via mail
	if (request.getParameter("noContactMail")!=null) {
		noContactMail = request.getParameter("noContactMail");
	}
	noContactMail= cm.isNoContactMail()?"yes":"no";
	if ("yes".equalsIgnoreCase(noContactMail)) {
		noContactMail = "checked";
	} else noContactMail = "";
//no contact via phone
	if (request.getParameter("noContactPhone")!=null) {
		noContactPhone = request.getParameter("noContactPhone");
	}
	noContactPhone= cm.isNoContactPhone()?"yes":"no";
	if ("yes".equalsIgnoreCase(noContactPhone)) {
		noContactPhone = "checked";
	} else noContactPhone = "";

%>
<%// CONFIRMATION MESSAGE %>
<%if(result.isSuccess() && "POST".equalsIgnoreCase(request.getMethod())){
    String confirmationMsg = "Your changes have been saved.";
	if ( user.isFraudulent()) { 
		confirmationMsg = confirmationMsg +"<br><br>" + MessageFormat.format(SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[]{user.getCustomerServiceContact()});
	}
%>
<%@ include file="/includes/i_confirmation_messages.jspf"%>
<%	} %>

<%
String[] checkInfoForm = 	{EnumUserInfoName.EMAIL.getCode(), EnumUserInfoName.EMAIL_FORMAT.getCode(),
							EnumUserInfoName.REPEAT_EMAIL.getCode(), EnumUserInfoName.PASSWORD.getCode(),
							EnumUserInfoName.REPEAT_PASSWORD.getCode(), EnumUserInfoName.DLV_HOME_PHONE.getCode(),
							EnumUserInfoName.DLV_FIRST_NAME.getCode(), EnumUserInfoName.DLV_LAST_NAME.getCode(),
							EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode(), EnumUserInfoName.DLV_EMPLOYEE_ID.getCode(),
							EnumUserInfoName.ALT_EMAIL.getCode(), EnumUserInfoName.DLV_WORK_PHONE.getCode(),
							EnumUserInfoName.DLV_CELL_PHONE.getCode()}; 
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkInfoForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<table width="675" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td colspan="6"class="text11">
		<span class="title18">User Name, Password, &amp; Contact Information</span><br>
		Update your user name, password, and contact information.<br>
		<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"><br>
	</td>
</tr>
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="40" height="8" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="100" height="8" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="160" height="8" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="80" height="8" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="135" height="8" border="0"></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="160" height="8" border="0"></td>
</tr>

<tr>
	<td colspan="6">
		<img src="/media_stat/images/navigation/change_your_username.gif" width="214" height="10" border="0" alt="CHANGE YOUR USERNAME" align="absbottom">&nbsp;&nbsp;&nbsp;<FONT class="text9">* Required information</FONT><br>
		<img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0" vspace="5">
	</td>
</tr>

<form name="update_user_name" method="post">
<input type="hidden" name="actionName" value="changeUserID">
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12">* E-mail Address</td>
		<td><input class="text9" size="28" style="width:150px; padding:1px;" type="text" maxlength="128" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>"></td>
		<td colspan="2">
		<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL_FORMAT.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
		<td align="right"><a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16"  vspace="3" hspace="3" border="0" alt="CANCEL"></a><input type="image" name="update_user" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16" alt="Save Changes" vspace="3" hspace="3" border="0"></td>
	</tr>
	<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Repeat E-mail Address</td>
		<td><input type="text" class="text9" size="28" style="width:150px; padding:1px;" name="repeat_email"></td>
		<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='repeat_email' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="2"></td>
		<td colspan="3" class="text9" style="padding-top:3px;">
			Example: you@isp.com.<br> You will use this to access the site.
			<br><br><br>
		</td>
		<td></td>
	</tr>
</form>

<tr>
	<td colspan="6">
	<img src="/media_stat/images/navigation/change_your_password.gif" width="162" height="9" border="0" alt="CHANGE YOUR PASSWORD" align="absbottom">&nbsp;&nbsp;&nbsp;<span class="text9">* Required information</span><br>
	<img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0" vspace="5"><br>
	</td>
</tr>

<form name="update_change_password" method="post">
<input type="hidden" name="actionName" value="changePassword">
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Password</td>
		<td><input class="text9" size="28" type="password" style="width:150px; padding:1px;" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" value="<%=password%>"></td>
		<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td align="right"><a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" vspace="3" hspace="3" border="0" alt="CANCEL"></a><input type="image" name="update_password" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="Save Changes" vspace="3" hspace="3" border="0"></td>
	</tr>
	<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
	<tr valign="top">
		<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Repeat Password</td>
		<td><input type="password" class="text9" size="28" style="width:150px; padding:1px;" name="confirmPassword"></td>
		<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='repeat_password' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="2"></td>
		<td colspan="3" class="text9" style="padding-top:3px;">
			Must be at least four characters.<br>
			Passwords are case-sensitive.<br>
			<br><br>
		</td>
		<td></td>
	</tr>
</form>

<form method="post" name="updateContactInformation">
<input type="hidden" name="actionName" value="changeContactInfo">
<tr>
	<td colspan="6">
		<img src="/media_stat/images/navigation/change_your_contact_info.gif" width="234" height="13" border="0" alt="CHANGE YOUR CONTACT INFORMATION" align="absbottom"> &nbsp;&nbsp;&nbsp;&nbsp; <span class="text9">*Required information</span><br>
		<img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0" vspace="5">
	</td>
</tr>

<tr>
	<td colspan="2" align="right" class="text12" style="padding-right:5px;">Title</td>
	<td colspan="3">
	<select class="text9" name="title"	value="">
       <option <%="".equalsIgnoreCase(title)?"selected":""%> ></option>		
       <option <%="MR.".equalsIgnoreCase(title)?"selected":""%> >Mr.</option>
       <option <%="MRS.".equalsIgnoreCase(title)?"selected":""%>>Mrs.</option>
       <option <%="MS.".equalsIgnoreCase(title)?"selected":""%>>Ms</option>
       <option <%="DR.".equalsIgnoreCase(title)?"selected":""%> >Dr.</option>		
	</select></td>
	<td align="right"><a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" vspace="3" hspace="3" border="0" alt="CANCEL"></a><input type="image" name="update_password" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="Save Changes" vspace="3" hspace="3" border="0"></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">* First Name</td>
	<td><input class="text9" size="28" maxlength="20" type="text" name="first_name" value="<%=firstName%>" required="true" style="width:150px; padding:1px;"></td>
	<td colspan="3"><span class="text9">(full name or first initial)</span> <fd:ErrorHandler result='<%=result%>' name='dlvfirstname' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Last Name</td>
	<td><input class="text9" size="28" maxlength="20" type="text" name="last_name" value="<%=lastName%>" required="true" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='dlvlastname' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><%=!user.isCorporateUser() ? "* " : "" %> Home Phone #</td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text9" name="homephone" id="uci_homePhone" title="Home Phone" value="<%=homePhone%>" required="true" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="ext" value="<%=homePhoneExt%>"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='dlvhomephone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><%=user.isCorporateUser() ? "* " : "" %>Work Phone #</td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text9" name="busphone" id="uci_busPhone" title="Business Phone" value="<%=busPhone%>" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="busphoneext" value="<%=busPhoneExt%>"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">Cell/Alt. #</td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text11" name="cellphone" id="uci_cellPhone" title="Cell Phone" value="<%=cellPhone%>" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="cellphoneext" value="<%=cellPhoneExt%>"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='dlvcellphone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">Other Email</td>
    <td><input type="text" size="28" maxlength="45" class="text9" name="<%=EnumUserInfoName.ALT_EMAIL.getCode()%>" value="<%=otherEmail%>" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.ALT_EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<%  if (user.isDepotUser() || user.isCorporateUser()) { %>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Work Department</td>
    <td><input type="text" size="28" maxlength="45" class="text9" name="<%=EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode()%>" value="<%=workDept%>" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr> 
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
    <%  if(user.isDepotUser()){
            com.freshdirect.delivery.depot.DlvDepotModel depot = FDDepotManager.getInstance().getDepot(user.getDepotCode());
            if (depot.getRequireEmployeeId()) { %>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12">* Employee Id</td>
    <td valign="top" colspan="4"><input type="text" size="28" maxlength="45" class="text9" name="employeeId" value="<%=employeeId%>" style="width:150px; padding:1px;"></td>
</tr> 
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0"></td></tr>
<%          }
        }
    }    %>
</form>


<form id="update_email_preference_level" name="update_email_preference_level" method="post">
	<input type="hidden" name="actionName" value="changeEmailPreferenceLevel">
	<tr>
		<td colspan="6"><br /><br />
			<img src="/media_stat/images/navigation/email_preferences.gif" width="122" height="9" border="0" alt="EMAIL PREFERENCES"><br />
			<img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0" vspace="5"><br />
		</td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;">
			<input class="radio" type="radio" id="receive_emailLevel2" name="receive_emailLevel" value="2" <%="2".equals(receive_emailLevel) ? "checked=\"true\"":""%> /></td>
		<td colspan="4" style="padding-top:5px;" class="text12">
			<strong>It's okay for FreshDirect to send me food offers, news and updates from time to time.</strong><br />
			You can unsubscribe anytime &mdash; and, of course, we'll never share your information with anyone else. <a href="/help/privacy_policy.jsp">Click here to view our Privacy Policy.</a><br /><br />
		</td>
		<td align="right">
			<a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" vspace="3" hspace="3" border="0" alt="CANCEL"></a><input type="image" name="update_email_preference" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="Save Changes" vspace="3" hspace="3" border="0">
		</td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;">
			<input class="radio" type="radio" id="receive_emailLevel1" name="receive_emailLevel" value="1" <%="1".equals(receive_emailLevel) ? "checked=\"true\"":""%> /></td>
		<td colspan="4" style="padding-top:5px;" class="text12">
			<strong>It's okay for FreshDirect to email me, but no more than one newsletter or offer each week.</strong><br /><br />
		</td>
		<td><!--  --></td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;">
			<input class="radio" type="radio" id="receive_emailLevel0" name="receive_emailLevel" value="0" <%="0".equals(receive_emailLevel) ? "checked=\"true\"":""%> /></td>
		<td colspan="4" style="padding-top:5px;" class="text12">
			<strong>Please don't send me emails unless they are directly related to my orders or important service information.</strong><br /><br />
		</td>
		<td><!--  --></td>
	</tr>
</form>



<form id="update_mail_phone_preference" name="update_mail_phone_preference" method="post">
<input type="hidden" name="actionName" value="changeMailPhonePreference">
<tr>
	<td colspan="6"><br /><br />
		[THIS IMAGE NEEDS TO BE DEFINED]<!-- <img src="/media_stat/images/navigation/mail_phone_preferences.gif" width="122" height="9" border="0" alt="MAIL & PHONE PREFERENCES"> --><br />
		<img src="/media_stat/images/layout/cccccc.gif" width="675" height="1" border="0" vspace="5"><br />
	</td>
</tr>
<tr valign="top">
	<td align="right" style="padding-top:5px; padding-right:5px;">
		<input class="radio" type="checkbox" id="noContactMail" name="noContactMail" value="yes" <%=noContactMail%>>
	</td>
	<td colspan="4" style="padding-top:5px;" class="text12">
		<strong>Please do not send me offers, and marketing messages in the mail.</strong><br /><br />
	</td>
	<td align="right"><a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" vspace="3" hspace="3" border="0" alt="CANCEL"></a><input type="image" name="update_email_preference" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="Save Changes" vspace="3" hspace="3" border="0"></td>
</tr>
<tr valign="top">
	<td style="padding-right: 5px;" align="right">
		<input class="radio" type="checkbox" id="noContactPhone" name="noContactPhone" value="yes" <%=noContactPhone%>>
	</td>
	<td colspan="4" class="text12">
		<strong>Please do not contact me by phone about offers and other updates.</strong><br />
		We may still attempt to call you about issues or problems with a specific order you have scheduled for delivery.<br /><br />
	</td>
	<td><!--  --></td>
</tr>
</form>

</table>

<script type="text/javascript">
FreshDirect.PhoneValidator.register(document.getElementById("uci_homePhone"));
FreshDirect.PhoneValidator.register(document.getElementById("uci_busPhone"));
FreshDirect.PhoneValidator.register(document.getElementById("uci_cellPhone"));
</script>

<br>
<table cellpadding="0" cellspacing="0" border="0" width="675">
	<tr valign="top" BGCOLOR="#FF9933"><td width="675" colspan="2"><img src="/media_stat/images/layout/ff9933.gif" HSPACE="0" width="1" height="1" border="0"></td></tr>
</table>
<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="675">
<tr valign="top">
	<td width="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" align="LEFT"></a></td>
	<td width="640">
		<a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
		<br>from <FONT class="text11bold"><a HREF="/index.jsp">Home Page</a></FONT><br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
	</td>
</tr>
</table>
</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
