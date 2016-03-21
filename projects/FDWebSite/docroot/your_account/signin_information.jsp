<%@page import="com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel"%>
<%@ page import="java.text.MessageFormat" %>
<%@ page import='com.freshdirect.common.customer.*,com.freshdirect.fdstore.*,com.freshdirect.delivery.sms.*,com.freshdirect.sms.*' %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import="com.freshdirect.fdstore.EnumEStoreId" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% //expanded page dimensions
final int W_YA_SIGNIN_INFO = 970;
%>
<%@page import="com.freshdirect.common.address.PhoneNumber"%><fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav_pwdstrng.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - User Name, Password, & Contact Info</tmpl:put>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="signin_info"></fd:SEOMetaTag>
	</tmpl:put>
  <tmpl:put name='customhead' direct='true'>
    <jwr:style src="/your_account.css" media="all"/>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
<fd:javascript src="/assets/javascript/phone_number.js"/>
<script src="/assets/javascript/jquery-1.6.4.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/assets/javascript/webpurify.jQuery.js"></script>

	<script type="text/javascript">
		jQuery.noConflict();
		jQuery(document).ready(function() {
			jQuery.webpurify.init("<%=FDStoreProperties.getProfanityCheckURL()%>","<%=FDStoreProperties.getProfanityCheckPass()%>");
			
		});
		
		function checkForProfanity(){
		if(jQuery("#displayName").val().length>0)
			{
			jQuery.webpurify.check( jQuery("#displayName").val(), function(isProfane){
				if(!isProfane)
					document.updateDisplayName.submit();
				else
					{
					jQuery("#profaneText").html("That Display Name is invalid. Please enter a different Display Name.");
					return false;
					}
			});
			}
		else
			{
				document.updateDisplayName.submit();
			}
		}	
		</script>
		
		<script type="text/javascript" src="/assets/javascript/rounded_corners.inc.js"></script>
					<script language="javascript">
						function curvyCornersHelper(elemId, settingsObj) {
							if (document.getElementById(elemId)) {
								var temp = new curvyCorners(settingsObj, document.getElementById(elemId)).applyCornersToAll();
							}
						}
						
						var ccSettings = {
							tl: { radius: 6 },
							tr: { radius: 6 },
							bl: { radius: 6 },
							br: { radius: 6 },
							topColour: "#FFFFFF",
							bottomColour: "#FFFFFF",
							antiAlias: true,
							autoPad: true
						};
	
						/* display an overlay containing a remote page */
						function doRemoteOverlay(olURL) {
							var olURL = olURL || '';
							if (olURL == '') { return false; }
					
							 Modalbox.show(olURL, {
		                        loadingString: 'Loading Preview...',
		                        title: ' ',
		                        overlayOpacity: .80,
		                        width: 750,
		                        centered: true,
		                        method: 'post',
		                        closeValue: '<img src="/media/editorial/site_access/images/round_x.gif" />',
		                        afterLoad: function() {
                                       $('MB_frame').style.border = '1px solid #CCCCCC';
                                       $('MB_header').style.border = '0px solid #CCCCCC';
                                       $('MB_header').style.display = 'block';
                                       window.scrollTo(0,0);
                                       $('MB_window').style.width = '750';
                                       $('MB_window').style.height = 'auto';
                                       $('MB_window').style.left = parseInt(($('MB_overlay').clientWidth-$('MB_window').clientWidth)/2)+'px';
                                       $('MB_content').style.padding = '0px';

                                       curvyCornersHelper('MB_frame', ccSettings);
		                        },
		                        afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
			                });
						}
				</script>
				
				
				
	<style>
		.accordion > input[type="checkbox"] {
			display: none;
		}
	</style>
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

String displayName = "";

String confirmationMsg = "";		// used in 'i_confirmation_messages.jspf'

String newlyLinkedSocialNetworkProvider = "";

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(identity.getErpCustomerPK());  

FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(identity);
FDCustomerEStoreModel fdCustomerEStore = fdCustomer.getCustomerEStoreModel();
String eStoreId = user.getUserContext().getStoreContext().getEStoreId().toString();
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

displayName = cm.getDisplayName();

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
if (request.getParameter("displayName")!=null) {
	displayName = request.getParameter("displayName");
}
sendNewsLetter = (null !=fdCustomerEStore && null !=fdCustomerEStore.getEmailOptIn() && fdCustomerEStore.getEmailOptIn())?"yes":"no";//cm.isReceiveNewsletter()?"yes":"no";

sendPlainTextEmail= cm.isEmailPlaintext()?"yes":"no";
if ("yes".equalsIgnoreCase(sendPlainTextEmail)) {
    sendPlainTextEmail = "checked";
} else sendPlainTextEmail = "";

sendNewsletter = (null !=fdCustomerEStore && null !=fdCustomerEStore.getEmailOptIn() && fdCustomerEStore.getEmailOptIn())? "checked" : "";//cm.isReceiveNewsletter() ? "checked" : "";
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

	//String mobile_number="(609) 647 8354";
/* 	boolean text_offers = cm.isOffersNotification();
	if(request.getParameter("text_offers") != null)
		text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
	boolean text_delivery = cm.isDeliveryNotification();
	if(request.getParameter("text_delivery") != null)
		text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false; */
	String mobile_number=null;
	boolean order_notices=false;
	boolean order_exceptions=false;
	boolean offers=false;
	boolean partner_messages=false;
	boolean text_offers=false;
	boolean text_delivery=false;
	
	if("FDX".equalsIgnoreCase(eStoreId)){
		
	if(request.getParameter("text_offers") != null)
		 text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
	
			if(fdCustomer.getCustomerSmsPreferenceModel().getFdxOffersNotification()!=null)		
 			text_offers = fdCustomer.getCustomerSmsPreferenceModel().getFdxOffersNotification(); 
			
		if(request.getParameter("text_delivery") != null)
				text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false;

		if(fdCustomer.getCustomerSmsPreferenceModel().getFdxdeliveryNotification()!=null)		
 			text_delivery = fdCustomer.getCustomerSmsPreferenceModel().getFdxdeliveryNotification();
		
			if(request.getParameter("mobile_number") != null)
			mobile_number = request.getParameter("mobile_number");
		else	
			mobile_number = fdCustomer.getCustomerSmsPreferenceModel().getFdxMobileNumber()==null ?"":fdCustomer.getCustomerSmsPreferenceModel().getFdxMobileNumber().getPhone();
		
		if(request.getParameter("order_notices") != null)
			order_notices = "Y".equals(request.getParameter("order_notices"))?true:false;
		else
			{
			   if(fdCustomer.getCustomerSmsPreferenceModel().getFdxOrderNotices()!=null)
				order_notices=fdCustomer.getCustomerSmsPreferenceModel().getFdxOrderNotices().equals(EnumSMSAlertStatus.NONE.value())?false:true;
			}
		
		if(request.getParameter("order_exceptions") != null)
			order_exceptions = "Y".equals(request.getParameter("order_exceptions"))?true:false;
		else
		{
		  	if(fdCustomer.getCustomerSmsPreferenceModel().getOrderExceptions()!=null)
			order_exceptions=fdCustomer.getCustomerSmsPreferenceModel().getFdxOrderExceptions().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		}
		
		if(request.getParameter("offers") != null)
			offers = "Y".equals(request.getParameter("offers"))?true:false;
		else
		{
		  if(fdCustomer.getCustomerSmsPreferenceModel().getOffers()!=null)
			offers=fdCustomer.getCustomerSmsPreferenceModel().getFdxOffers().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		}
		if(request.getParameter("partner_messages") != null)
			partner_messages = "Y".equals(request.getParameter("partner_messages"))?true:false;
		
		else
		{
		  if(fdCustomer.getCustomerSmsPreferenceModel().getPartnerMessages()!=null)
			partner_messages=fdCustomer.getCustomerSmsPreferenceModel().getFdxPartnerMessages().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		}
	}
	else{
		if(request.getParameter("text_offers") != null)
		 text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
		
			if(fdCustomer.getCustomerSmsPreferenceModel().getOffersNotification()!=null)		
 			text_offers = fdCustomer.getCustomerSmsPreferenceModel().getOffersNotification();
			
		if(request.getParameter("text_delivery") != null)
				text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false;

		if(fdCustomer.getCustomerSmsPreferenceModel().getDeliveryNotification()!=null)		
 			text_delivery = fdCustomer.getCustomerSmsPreferenceModel().getDeliveryNotification();

		if(request.getParameter("mobile_number") != null)
			mobile_number = request.getParameter("mobile_number");
		else
			mobile_number = fdCustomer.getCustomerSmsPreferenceModel().getMobileNumber()!=null ? fdCustomer.getCustomerSmsPreferenceModel().getMobileNumber().getPhone():"";
		
		 if(request.getParameter("order_notices") != null)
				order_notices = "Y".equals(request.getParameter("order_notices"))?true:false;
		 else{ 
				if(fdCustomer.getCustomerSmsPreferenceModel().getOrderNotices()!=null)
				 order_notices=fdCustomer.getCustomerSmsPreferenceModel().getOrderNotices().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		 	}
		
		 if(request.getParameter("order_exceptions") != null)
				order_exceptions = "Y".equals(request.getParameter("order_exceptions"))?true:false;
		 else {
				if(fdCustomer.getCustomerSmsPreferenceModel().getOrderExceptions()!=null)
				 order_exceptions=fdCustomer.getCustomerSmsPreferenceModel().getOrderExceptions().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		  }	
		 if(request.getParameter("offers") != null)
				offers = "Y".equals(request.getParameter("offers"))?true:false;
		 else{
			if(fdCustomer.getCustomerSmsPreferenceModel().getOffers()!=null)
		  	offers=fdCustomer.getCustomerSmsPreferenceModel().getOffers().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		 }
		
		 if(request.getParameter("partner_messages") != null)
		 {		partner_messages = "Y".equals(request.getParameter("partner_messages"))?true:false;
		 	}
		 else{
			if(fdCustomer.getCustomerSmsPreferenceModel().getPartnerMessages()!=null)
			 partner_messages=fdCustomer.getCustomerSmsPreferenceModel().getPartnerMessages().equals(EnumSMSAlertStatus.NONE.value())?false:true;
		 }
}
	
	boolean go_green = cm.isGoGreen();
	if(request.getParameter("go_green") != null)
		go_green = "Y".equals(request.getParameter("go_green"))?true:false;
%>
<%// CONFIRMATION MESSAGE %>
<%if(result.isSuccess() && "POST".equalsIgnoreCase(request.getMethod())){
	
	if(request.getAttribute("NewlyDisconnectedSocialNetworkProvider") != null){
		confirmationMsg = "Social network " + request.getAttribute("NewlyDisconnectedSocialNetworkProvider") + " has been disconnected.";
	} else {
		confirmationMsg = "Your changes have been saved.";     
	}	
	
	/* remove fraud messaging 
	if ( user.isFraudulent()) { 
		confirmationMsg = confirmationMsg +"<br><br>" + MessageFormat.format(SystemMessageList.MSG_NOT_UNIQUE_INFO, new Object[]{user.getCustomerServiceContact()});
	}
    */


} else if(session.getAttribute("NewlyLinkedSocialNetworkProvider") != null){
	
	newlyLinkedSocialNetworkProvider = (String) session.getAttribute("NewlyLinkedSocialNetworkProvider");
	
	confirmationMsg = "Your FD account is now linked to your " + session.getAttribute("NewlyLinkedSocialNetworkProvider") + " account.";
	session.setAttribute("NewlyLinkedSocialNetworkProvider", null);
		
}  else if(session.getAttribute("AlreadyConnectedSocialAccount") != null){
	
	confirmationMsg = "Looks like this social account is already linked to <bold>" + session.getAttribute("AlreadyConnectedSocialAccount") + " </bold>.\n\n\n Please connect with another account.";
	session.setAttribute("AlreadyConnectedSocialAccount", null);
		
} %>



<%	if(confirmationMsg != null && !confirmationMsg.equalsIgnoreCase("")) {%>

	<%@ include file="/includes/i_confirmation_messages.jspf"%>
	
<%	} %>


<%
String[] checkInfoForm = 	{EnumUserInfoName.EMAIL.getCode(), EnumUserInfoName.EMAIL_FORMAT.getCode(),
							EnumUserInfoName.REPEAT_EMAIL.getCode(), EnumUserInfoName.PASSWORD.getCode(),
							EnumUserInfoName.REPEAT_PASSWORD.getCode(), EnumUserInfoName.DLV_HOME_PHONE.getCode(),
							EnumUserInfoName.DLV_FIRST_NAME.getCode(), EnumUserInfoName.DLV_LAST_NAME.getCode(),
							EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode(), EnumUserInfoName.DLV_EMPLOYEE_ID.getCode(),
							EnumUserInfoName.ALT_EMAIL.getCode(), EnumUserInfoName.DLV_WORK_PHONE.getCode(),
							EnumUserInfoName.DLV_CELL_PHONE.getCode(),EnumUserInfoName.DISPLAY_NAME.getCode(), "mobile_number", "text_option"}; 
%>
<fd:ErrorHandler result='<%=result%>' field='<%=checkInfoForm%>'>
	<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>	
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
	<%@ include file="/includes/i_error_messages.jspf" %>	
</fd:ErrorHandler>

<table class="youraccount" width="<%= W_YA_SIGNIN_INFO %>" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td colspan="6">
    <h1>Your Account Preferences</h1>
		Change your user name, password, and other account preferences.<br>
    <img src="/media_stat/images/layout/ff9933.gif" width="<%= W_YA_SIGNIN_INFO %>" height="1" border="0" vspace="8"><br>
	</td>
</tr>
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="40" height="8" border="0" alt=""></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="100" height="8" border="0" alt=""></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="160" height="8" border="0" alt=""></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="80" height="8" border="0" alt=""></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="135" height="8" border="0" alt=""></td>
	<td><img src="/media_stat/images/layout/clear.gif" width="220" height="8" border="0" alt=""></td>
</tr>

<tr>
	<td colspan="6">
    <h2>Change Your E-mail/User Name <i>* Required Information</i></h2>
	</td>
</tr>

<form name="update_user_name" method="post">
<input type="hidden" name="actionName" value="changeUserID">
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* E-mail Address</label></td>
		
		
		<% 
			if((user.isVoucherHolder() && user.getMasqueradeContext()== null)){	
		%> 
		<td><input class="text9" size="28" style="width:150px; padding:1px;" type="text" maxlength="128" readonly="readonly" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>"></td>
		
		<% } else { %>
		
		<td><input class="text9" size="28" style="width:150px; padding:1px;" type="text" maxlength="128" name="<%=EnumUserInfoName.EMAIL.getCode()%>" value="<%=email%>"></td>
		<%} %>
		
		<td colspan="2">
		<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.EMAIL_FORMAT.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
		
		
		<% 
			if (!(user.isVoucherHolder() && user.getMasqueradeContext()== null)) {
		%>
			<td align="right">
        <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
        <button class="cssbutton small orange">Save Changes</button>
      </td>	
		<%	} %>
		
	</tr>
	<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Repeat E-mail Address</label></td>
		
		
		<% 
			if ((user.isVoucherHolder() && user.getMasqueradeContext() == null)){	
		%> 
		<td><input type="text" class="text9" size="28" style="width:150px; padding:1px;" readonly="readonly"  name="repeat_email"></td>
		
		<% } else { %>
		
		<td><input type="text" class="text9" size="28" style="width:150px; padding:1px;" name="repeat_email"></td>
		<%} %>
		
		
		
		
		
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
	
		<%  boolean isPasswordAddedForSocialUser = UserUtil.isPasswordAddedForSocialUser(identity.getErpCustomerPK());
			if((erpCustomer != null) && (erpCustomer.isSocialLoginOnly()) && !isPasswordAddedForSocialUser){	
		%>
      <h2>Add a password <i>* Required Information</i></h2>
		<%	} else { %>		
      <h2>Change Your Password <i>* Required Information</i></h2>
		<%	} %>	
	</td>
</tr>

<form name="update_change_password" method="post">
<input type="hidden" name="actionName" value="changePassword">

    <!--  Changed for Password Strength Display -->
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Password</label></td>
		
		<!--  Added for Password Strength Display -->
		<td colspan="2">
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
				<input id="password1" name="password" size="28" style="width:150px; padding:1px;" class="password" data-indicator="pwindicator" type="password">
				<span class="case-sensitive">Passwords are case sensitive</span>
			</div>
			<!--  
			<div id="pwindicator">
	               <div class="bar"></div>
	               <div class="label"></div>
	        </div>
	        -->
		</div><!-- // .subgroup -->			
		</div><!-- // .content-group -->
		</div><!-- // .container -->
	    </td>
		<td></td>
	    <!-- Added for Password Strength Display -->
	    
		<!--  
		<td colspan="2">			
			<input class="text9" size="28" type="password" style="width:150px; padding:1px;" name="<%=EnumUserInfoName.PASSWORD.getCode()%>" value="<%=password%>" id="password1" onkeyup="passwordChanged();">
			<span id="strength">Type Password</span>
		</td>
		<td></td>
		-->
    <td align="right">
      <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
      <button class="cssbutton small orange">Save Changes</button>
    </td>	
		
	</tr>
	
	<tr>
		<td colspan="2"></td>
		<td>			
			<div id="pwindicator">
	               <div class="bar"></div>
	               <div class="label"></div>
	        </div>   		    
		</td>
		<td colspan="2"></td>
		<td></td>
	</tr>	

	<tr>
		<td colspan="2" ></td>
		<td>
			<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.PASSWORD.getCode()%>' id='errorMsg'>
				 <span class="text11rbold"><%=errorMsg%></span>
   		    </fd:ErrorHandler>
		</td>
		<td colspan="2"></td>
		<td></td>
	</tr>
	<!--  Changed for Password Strength Display -->

	<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
	<tr valign="top">
		<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Repeat Password</label></td>
		<td><input type="password" class="text9" size="28" style="width:150px; padding:1px;" name="confirmPassword"></td>
		<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='repeat_password' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="2"></td>
		<td colspan="3" class="text9" style="padding-top:3px;">
			Must be at least six characters.<br>
			Passwords are case-sensitive.<br>
			<br><br>
		</td>
		<td></td>
	</tr>
</form>


<tr>
	<td colspan="6">
    <h2>Change Your Display Name</h2>
	</td>
</tr>
<form method="post" name="updateDisplayName">
<input type="hidden" name="actionName" value="changeDisplayName">


<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>Display Name</label></td>
	<td><input class="text9" size="28" maxlength="30" type="text" id="displayName" name="displayName" value="<%=displayName%>" style="width:150px; padding:1px;"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DISPLAY_NAME.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
  <span id="profaneText" class="text11rbold" style="display:block;width:250px"></span></td>
  <td align="right">
    <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
    <button type="button" onclick="checkForProfanity();"class="cssbutton small orange">Save Changes</button>
  </td>	
	
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""><br><br></td></tr>
</form>

<form method="post" name="updateContactInformation">
<input type="hidden" name="actionName" value="changeContactInfo">
<tr>
	<td colspan="6">
    <h2>Change Your Contact Information <i>* Required Information</i></h2>
	</td>
</tr>


<tr>
	<td colspan="2" align="right" class="text12" style="padding-right:5px;"><label>Title</label></td>
	<td colspan="3">
	<select class="text9" name="title"	value="">
       <option <%="".equalsIgnoreCase(title)?"selected":""%> ></option>		
       <option <%="MR.".equalsIgnoreCase(title)?"selected":""%> >Mr.</option>
       <option <%="MRS.".equalsIgnoreCase(title)?"selected":""%>>Mrs.</option>
       <option <%="MS.".equalsIgnoreCase(title)?"selected":""%>>Ms</option>
       <option <%="DR.".equalsIgnoreCase(title)?"selected":""%> >Dr.</option>		
	</select></td>
  <td align="right">
    <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
    <button class="cssbutton small orange">Save Changes</button>
  </td>	
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* First Name</label></td>
	<td><input class="text9" size="28" maxlength="20" type="text" name="first_name" value="<%=firstName%>" required="true" style="width:150px; padding:1px;"></td>
	<td colspan="3"><span class="text9">(full name or first initial)</span> <fd:ErrorHandler result='<%=result%>' name='dlvfirstname' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Last Name</label></td>
	<td><input class="text9" size="28" maxlength="20" type="text" name="last_name" value="<%=lastName%>" required="true" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='dlvlastname' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><%=!user.isCorporateUser() ? "* " : "" %><label> Home Phone #</label></td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text9" name="homephone" id="uci_homePhone" title="Home Phone" value="<%=homePhone%>" required="true" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="ext" value="<%=homePhoneExt%>" style="width: 45px;"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='dlvhomephone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><%=user.isCorporateUser() ? "* " : "" %><label>Work Phone #</label></td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text9" name="busphone" id="uci_busPhone" title="Business Phone" value="<%=busPhone%>" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="busphoneext" value="<%=busPhoneExt%>" style="width: 45px;"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>Cell/Alt. #</label></td>
    <td colspan="2" class="text12"><input type="text" size="28" maxlength="20" class="text11" name="cellphone" id="uci_cellPhone" title="Cell Phone" value="<%=cellPhone%>" style="width:150px; padding:1px;">&nbsp;&nbsp;Ext. <input type="text" maxlength="6" size="4" class="text9" name="cellphoneext" value="<%=cellPhoneExt%>" style="width: 45px;"></td>
	<td colspan="2"><fd:ErrorHandler result='<%=result%>' name='dlvcellphone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>Other Email</label></td>
    <td><input type="text" size="28" maxlength="45" class="text9" name="<%=EnumUserInfoName.ALT_EMAIL.getCode()%>" value="<%=otherEmail%>" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.ALT_EMAIL.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr>
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<%  if (user.isDepotUser() || user.isCorporateUser()) { %>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Work Department</label></td>
    <td><input type="text" size="28" maxlength="45" class="text9" name="<%=EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode()%>" value="<%=workDept%>" style="width:150px; padding:1px;"></td>
	<td colspan="3"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_WORK_DEPARTMENT.getCode()%>' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
</tr> 
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
    <%  if(user.isDepotUser()){
            com.freshdirect.fdlogistics.model.FDDeliveryDepotModel depot = FDDeliveryManager.getInstance().getDepot(user.getDepotCode());
            if (depot.getRequireEmployeeId()) { %>
<tr>
	<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Employee Id</label></td>
    <td valign="top" colspan="4"><input type="text" size="28" maxlength="45" class="text9" name="employeeId" value="<%=employeeId%>" style="width:150px; padding:1px;"></td>
</tr> 
<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" alt=""></td></tr>
<%          }
        }
    }    %>
</form>

<% if (FDStoreProperties.isEmailOptdownEnabled()) { %>
	<form id="update_email_preference_level" name="update_email_preference_level" method="post">
		<input type="hidden" name="actionName" value="changeEmailPreferenceLevel">
		<tr>
			<td colspan="6"><br /><br />
        <h2>Email Preferences</h2>
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
        <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
        <button class="cssbutton small orange">Save Changes</button>
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
      <h2>Mail And Phone Preferences</h2>
		</td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;">
			<input class="radio" type="checkbox" id="noContactMail" name="noContactMail" value="yes" <%=noContactMail%>>
		</td>
		<td colspan="4" style="padding-top:5px;" class="text12">
			<strong>Please do not send me offers, and marketing messages in the mail.</strong><br /><br />
		</td>
    <td align="right">
      <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
      <button class="cssbutton small orange">Save Changes</button>
    </td>	
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
<% }else{ %>


	<form name="update_email_preference" method="post">
	<input type="hidden" name="actionName" value="changeEmailPreference">
	<tr>
		<td colspan="6"><br><br>
      <h2>Email Preferences</h2>
		</td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;"><input class="radio" type="checkbox" name="receive_mail" value="yes" <%="yes".equalsIgnoreCase(sendNewsLetter) ? "checked":""%>></td>
		<td colspan="4" style="padding-top:5px;" class="text12"><b>Please send me food offers, news and updates from time to time.</b><br>
		You can unsubscribe anytime &mdash; and, of course, we'll never share your information with anyone else.<br><br>
		</td>
    <td align="right">
      <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
      <button class="cssbutton small orange">Save Changes</button>
    </td>	
	</tr>
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="isSendPlainTextEmail" value="yes" <%=sendPlainTextEmail%>></td>
		<td colspan="4" class="text12"><b>Send me plain text e-mail.</b><br>
		Select this option if your e-mail program is unable to receive HTML formatted e-mail.<br><br></td>
		<td></td>
	</tr>
	</form>
<% } %>


<% if(FDStoreProperties.isSocialLoginEnabled()){ %>
	<%@ include file="/includes/i_social_accounts.jspf"%>
<% } %>




<!-- mobile preferences-->
<form name="update_email_preference" method="post">
	<input type="hidden" name="actionName" value="mobilepreferences">
	<input type="hidden" name="mobile_existing" value="<%=mobile_number%>">
	<input type="hidden" name="order_notice_existing" value="<%=order_notices%>">
	<input type="hidden" name="order_exception_existing" value="<%=order_exceptions%>">
	<input type="hidden" name="offer_existing" value="<%=offers%>">
	<input type="hidden" name="partner_existing" value="<%=partner_messages%>">	
	<tr>
		<td colspan="6"><br><br>
      <h2>Mobile Preferences</h2>
		</td>
	</tr>
	<tr>
		<td colspan="6" align="left" style="padding-right:5px;" class="text12"><b>Want to get SMS text notifications about your order? </b> Sign up now, and we'll text you important (and delicious!) information about your delivery. You may opt out at any time by sending STOP to 37374 or simply unchecking all the alert types below.</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr>
		<td colspan="6" align="left" style="padding-right:5px;" class="text12">Messages will be sent to the following mobile number:</td>
	</tr>
	<tr valign="top">
		<td style="padding-right: 5px;" align="left" colspan="6">
			<fd:IncludeMedia name="/media/editorial/site_pages/sms/terms_short.html" />
		</td>
	</tr>
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr>
		<td colspan="2" align="right" style="padding-right:5px;" class="text12"><label>* Mobile Number</label></td>
    	<td><input type="text" size="28" maxlength="20" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:150px; padding:1px;"></td>
		<td colspan="2" width="500"><fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
    <td align="right">
      <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
      <button class="cssbutton small orange">Save Changes</button>
    </td>	
	</tr> 
	<tr><td colspan="6">&nbsp;</td></tr>
	<tr><td colspan="6"><fd:ErrorHandler result='<%=result%>' name='text_option' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
	<%-- <tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;"><input class="radio" type="checkbox" name="text_delivery" value="Y" <%=text_delivery ? "checked":""%>></td>
		<td colspan="4" style="padding-top:5px;" class="text12">Yes please notify me via text message with important information about my delivery.<br><br />
		</td>
		<td align="right"></td>
	</tr>
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="text_offers" value="Y" <%=text_offers ? "checked":""%>></td>
		<td colspan="4" class="text12">Yes please notify me about <b>offers, discounts</b> and <b>promotions</b> from time to time.<br /><br /><br /></td>
		<td></td>
	</tr>--%>
<%if("FD".equals(eStoreId))
	{%>	
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="order_notices" value="Y" <%=order_notices ? "checked":""%>></td>
		<td colspan="5" class="text12bold">FreshDirect Order Notices</td>
	</tr> 
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top">
					<td align="left">
						<div class="accordion"><input type="checkbox" id="order_notices" />
							<label for="order_notices" class="text12bold" style="margin-left: 0;"><div style="display:none;">Examples of messages (depending on your area)</div><pre class="text12bold">Examples of messages (depending on your area)</pre></label>
							<div class="text12" id="article" align="left">
								<strong>Estimated Time of Delivery: </strong> We'll narrow down your two-hour window to just one hour on the day of delivery.  
								<br /><br /><strong>You're Next! </strong>Receive an alert when you are the next customer on our driver's route. Your food is on the way! 
							<br /><br /><br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="order_exceptions" value="Y" <%=order_exceptions ? "checked":""%>></td>
		<td colspan="5" class="text12bold">FreshDirect Order Alerts</td>
	</tr>
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top" >
					<td align="left">
						<div class="accordion"><input type="checkbox" id="order_exceptions"> 
							<label for="order_exceptions" class="text12bold" style="margin-left: 0;"><div style="display:none;">Examples of messages (depending on your area)</div><pre class="text12bold">Examples of messages (depending on your area)</pre></label>
							<div class="text12"   id="article1" align="left">
								<strong>Cancellation</strong> Get an alert when your order has to be cancelled because of unforeseen circumstances. 
								<br /><br />
								<strong>Delivery Attempt: </strong> Uh oh, did we just miss you? We'll text you after an unsuccessful delivery attempt to your place.  
								<br /><br />
								<strong>Unattended/Doorman: </strong> Know when your order has been left for you at your preferred location or with your doorman.
								<br /><br /><br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="offers" value="Y" <%=offers ? "checked":""%>></td>
		<td colspan="5" class="text12bold">FreshDirect Perks</td>
	</tr>
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top" >
					<td align="left">
						<div class="accordion"><input type="checkbox" id="offers"> 
							<label for="offers" class="text12bold" style="margin-left: 0;"><div style="display:none;">Description</div><pre class="text12bold">Description</pre></label>
							<div class="text12" id="article2" align="left">
								Get the inside scoop on exciting new features, exclusive promotions, and more!
							<br />&nbsp;&nbsp;&nbsp;&nbsp;<br />&nbsp;&nbsp;&nbsp;&nbsp;<br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%} 
else{  %>
	<!--start: Added by Sathishkumar Merugu for SMS FDX alert. -->

	
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="order_notices" value="Y" <%=order_notices ? "checked":""%>></td>
		<td colspan="5" class="text12bold">Delivery Updates </td>
	</tr> 
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top">
					<td align="left">
						<div class="accordion"><input type="checkbox" id="order_notices" />
							<label for="order_notices" class="text12bold" style="margin-left: 0;"><div style="display:none;">Examples of messages (depending on your area)</div><pre class="text12bold">Examples of messages (depending on your area)</pre></label>
							<div class="text12" id="article" align="left">
								<strong>Out for Delivery:</strong> 
								<br /><br />
								<strong>You are Nex: </strong> deferred until delivery personnel are equipped with handhelds.  
								<br /><br /><strong>You're Next! </strong>Receive an alert when you are the next customer on our driver's route. Your food is on the way! 
							<br /><br /><br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="order_exceptions" value="Y" <%=order_exceptions ? "checked":""%>></td>
		<td colspan="5" class="text12bold">Order Status </td>
	</tr>
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top" >
					<td align="left">
						<div class="accordion"><input type="checkbox" id="order_exceptions"> 
							<label for="order_exceptions" class="text12bold" style="margin-left: 0;"><div style="display:none;">Examples of messages (depending on your area)</div><pre class="text12bold">Examples of messages (depending on your area)</pre></label>
							<div class="text12"   id="article1" align="left">
								<strong>Cancellation</strong> Get an alert when your order has to be cancelled because of unforeseen circumstances. 
								<br /><br />
								<strong>Delivery Attempt: </strong> Uh oh, did we just miss you? We'll text you after an unsuccessful delivery attempt to your place.  
								<br /><br />
								<strong>Unattended/Doorman: </strong> Know when your order has been left for you at your preferred location or with your doorman.
								<br /><br /><br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="offers" value="Y" <%=offers ? "checked":""%>></td>
		<td colspan="5" class="text12bold"> Offers</td>
	</tr>
	<tr valign="top">
		<td>&nbsp;</td>
		<td colspan="5">
			<table border="0" cellpadding="0" cellspacing="0" style="width: 100%">
				<tr valign="top" >
					<td align="left">
						<div class="accordion"><input type="checkbox" id="offers"> 
							<label for="offers" class="text12bold" style="margin-left: 0;"><div style="display:none;">Description</div><pre class="text12bold">Description</pre></label>
							<div class="text12" id="article2" align="left">
								Get the inside scoop on exciting new features, exclusive promotions, and more!
							<br />&nbsp;&nbsp;&nbsp;&nbsp;<br />&nbsp;&nbsp;&nbsp;&nbsp;<br />
							</div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<% }%>
	
	<!--End: Added by Sathishkumar Merugu for SMS FDX alert. -->
	
	 <%-- <tr valign="top">
		<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="partner_messages" value="Y" <%=partner_messages ? "checked":""%>></td>
		<td colspan="4" class="text12bold">FreshDirect Partner Messages</td>
		<td></td>
	</tr>
	<tr valign="top">
		<td  align="right" colspan="3" style="padding-left: 90px;">
		<table  border="0" cellpadding="0" cellspacing="0" >
			<tr valign="top" ><td  align="left" style="width: 100%">
			<div class="accordion"><input type="checkbox" id="partner_messages"> 
				<label for="partner_messages" class="text12bold">Description</label>
				<div class="text12"   id="article3" align="left">
					Get relevant exclusive promotions, news, discounts and information from FreshDirect trusted partners
				<br /><br /><br />
				</div>
			</div>
			</td></tr>
			</table>
		</td>
	</tr>  --%>
	<tr valign="top">
		<td style="padding-right: 5px;" align="left" colspan="6">
			<fd:IncludeMedia name="/media/editorial/site_pages/sms/terms_medium.html" />
		</td>
	</tr>	
	</form>
	
	<form name="go_green" method="post">
	<input type="hidden" name="actionName" value="otherpreferences">	
	<tr>
		<td colspan="6"><br><br>
      <h2>Other</h2>
		</td>
	</tr>
	<tr>	
    <td align="right" valign="top" colspan="6">
      <a class="cssbutton green small transparent border" href="/your_account/manage_account.jsp">Cancel</a>
      <button class="cssbutton small orange">Save Changes</button>
    </td>	
	</tr>
	<tr>
		<td colspan="6">
			<span class="title18">Go green!</span>&nbsp;<img src="/media_stat/images/navigation/go_green_leaf.gif" border="0" alt="GO GREEN"><br>
		</td>
	</tr>
	<tr valign="top">
		<td align="right" style="padding-top:5px; padding-right:5px;">
		<input class="radio" type="checkbox" name="go_green" value="Y" <%=go_green ? "checked":""%>></td>
		<td colspan="4" style="padding-top:5px;" class="text12">I want to turn off paper statement delivery and receive my statements online.<br><br />
		</td>
		<td></td>
	</tr>
	</form>

</table>

<script type="text/javascript">
FreshDirect.PhoneValidator.register(document.getElementById("uci_homePhone"));
FreshDirect.PhoneValidator.register(document.getElementById("uci_busPhone"));
FreshDirect.PhoneValidator.register(document.getElementById("uci_cellPhone"));
</script>

<br>
<table cellpadding="0" cellspacing="0" border="0" width="<%= W_YA_SIGNIN_INFO %>">
	<tr valign="top" BGCOLOR="#FF9933"><td width="<%= W_YA_SIGNIN_INFO %>" colspan="2"><img src="/media_stat/images/layout/ff9933.gif" HSPACE="0" width="1" height="1" border="0"></td></tr>
</table>
<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>
<table border="0" cellspacing="0" cellpadding="0" width="<%= W_YA_SIGNIN_INFO %>">
<tr valign="top">
	<td width="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" align="LEFT"></a></td>
	<td width="<%= W_YA_SIGNIN_INFO - 35 %>">
		<a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
		<br>from <FONT class="text11bold"><a HREF="/index.jsp">Home Page</a></FONT><br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0" alt="">
	</td>
</tr>
</table>

</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
