<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@page import="com.freshdirect.common.address.PhoneNumber"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>FreshDirect</title>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style>
		.trpad {
			padding-left: 10px;
			padding-right: 10px;
			text-align: right;
		}
		
		.smshline {
			background: url("/media/editorial/site_access/images/dots_h.gif") repeat-x scroll 0 0 transparent;
			font-size: 1px;
			height: 1px;
			line-height: 1px;
			width: 700px;
			padding-left: 10px;
			padding-right: 10px;
			float: right;
		}
		
	</style>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>

<fd:RegistrationController actionName='<%=request.getParameter("actionName")%>' result='result'>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
	ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);

	String mobile_number = cm.getMobileNumber()==null?"":cm.getMobileNumber().getPhone();
	boolean text_offers = cm.isOffersNotification();
	if(request.getParameter("text_offers") != null)
		text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
	boolean text_delivery = cm.isDeliveryNotification();
	if(request.getParameter("text_delivery") != null)
		text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false;
	if(request.getParameter("mobile_number") != null)
		mobile_number = request.getParameter("mobile_number");
	boolean go_green = cm.isGoGreen();
	if(request.getParameter("go_green") != null)
		go_green = "Y".equals(request.getParameter("go_green"))?true:false;
		
	
	String phone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
	String phoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
	
	if(user.isCorporateUser()) {
		phone = cm.getBusinessPhone()==null?"":cm.getBusinessPhone().getPhone();
		phoneExt = cm.getBusinessPhone()==null?"":cm.getBusinessPhone().getExtension();
	}
	if (request.getParameter("busphone")!=null) {
		phone = PhoneNumber.format(request.getParameter("busphone"));
	}
	if (request.getParameter("busphoneext")!=null) {
		phoneExt = phone == null || phone.length() == 0 ? "" : request.getParameter("busphoneext");
	}
	final int W_YA_SIGNIN_INFO = 750;
	final int W_DOTTED_LINE = 748;
	boolean form_processed = false;
	final String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	
	if(session.getAttribute("SMSAlert"+orderNumber) != null)
		form_processed = true;
	boolean nothanks=false;
	if(session.getAttribute("noThanksFlag")!=null){
		nothanks=true;
	}
%>
<center>
<% if(form_processed) {
		if(nothanks){
	%>
	<table border="0" cellpadding="0" cellspacing="0">	
		<tr>
			<td class="text16bold trpad" valign="top">
				<img src="/media_stat/images/navigation/tickmark.gif" border="0" />&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td class="title19">Your preferences have been updated.
			<br /><span class="text13gr" style="font-weight:normal;color:gray;">You can update your preferences any time - simply go to <a href="/your_account/signin_information.jsp">My Account</a> section and change your preferences.</span>
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td  colspan="2"><div class="smshline" id=""><!-- --></div></td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td align="center" colspan="2">
				<a class="imgButtonOrange" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Close&nbsp;&nbsp;</a>
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
	</table>
	<%} else {
	%>
	<table border="0" cellpadding="0" cellspacing="0">	
		<tr>
			<td class="text16bold trpad" valign="top">
				<img src="/media_stat/images/navigation/tickmark.gif" border="0" />&nbsp;&nbsp;&nbsp;&nbsp;
			</td>
			<td class="title19">YOU'RE ALMOST DONE...
			<br /><span class="text14bold">We've just sent a text to<%=mobile_number%>.Please check your messages to complete the sign up process! </span>
			<br /><span class="text13gr" style="font-weight:normal;color:gray;">You can update your preferences any time - simply go to <a href="/your_account/signin_information.jsp">My Account</a> section and change your preferences.</span>
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td  colspan="2"><div class="smshline" id=""><!-- --></div></td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td align="center" colspan="2">
				<a class="imgButtonOrange" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Close&nbsp;&nbsp;</a>
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
	</table>
<% }
} else { %>
<div style=" overflow-y: auto; overflow-x: hide;">
<form id="smsalertform" name="smsalertform" method="post" action="">
	<input type="hidden" name="actionName" value="ordersmsalerts" />	
	<table border="0" cellpadding="0" cellspacing="0" style="padding-right: 30px;">
		<tr>
			<td class="title18or" style="text-align: left;"><span class="title18or trpad">&nbsp;</span>THANK YOU FOR YOUR ORDER! </td>
		</tr>
		
		<tr>
			<td>		
				<table width="100%" border="0">
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr><td colspan="2" ><span class="title18or trpad">&nbsp;</span><span class="title18">Want to know <i>EXACTLY</i> when we're coming? </span></td></tr>
					<tr><td colspan="2" ><span class="title18or trpad">&nbsp;</span><span class="title18">SIGN UP FOR OUR NEW TEXT MESSAGE UPDATES </span></td></tr>
					<tr><td colspan="2" ><span class="title18or trpad">&nbsp;</span><span class="text12">Sign up for our enhanced text message* updates now and get notified when your delivery is next on the <br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; driver's route...plus, much more! </span></td></tr>
					<tr><td colspan="2" class="text11rbold trpad" style="text-align: left;">&nbsp;<fd:ErrorHandler result='<%=result%>' name='text_option' id='errorMsg'>&nbsp;<%=errorMsg%></fd:ErrorHandler></td></tr>
					<!-- <tr><td><div class="smshline" id=""></div></td></tr> -->
					<tr><td>&nbsp;</td></tr>
					<tr valign="top">
						<td colspan="2" >
							<table>						
								<tr>						
								<td style="padding-right:5px;font-weight:bold;" class="text12"><span class="title18or trpad">&nbsp;</span>Enter mobile phone number*<br /><br />
									<span class="title18or trpad">&nbsp;</span><input type="text" size="28" maxlength="20" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:175px; padding:1px; height:20px;font-size:13px" /></td>
									<td class="text12" style="color:gray;  text-indent: -10px; padding: 0 5px;" align="left" colspan="2" valign="bottom">* standard text messaging rates apply. You can unsubscribe anytime - <br /> simply go to "My Account" section and update your preferences.</td>
								</tr>						
							</table>
						</td>
					</tr>
					<tr><td colspan="2" class="text11rbold trpad" style="width:500px; text-align: left;padding-left: 15px;height:25px;">&nbsp;<fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><%=errorMsg%></fd:ErrorHandler></td></tr>
					<tr><td colspan="2" style="font-size:11px"><span class="title18or trpad">&nbsp;</span><span class="text11" style="color: grey">By signing up, you agree to these </span><u><a href="javascript:popup('/your_account/terms.jsp', 'large')">Terms and Conditions</a></u></td>  </tr>
				</table>
			</td>
		</tr>
		
		<!-- <tr><td><div class="smshline" id=""></div></td></tr> -->
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" border="0"><tr>
				<td align="left" class="trpad" style="text-align: left; padding-left: 20px;">
					<button class="imgButtonWhite" onclick="doSmsRemoteOverlay1('sms_alerts.jsp?submitbutton=nothanks'); return false;">&nbsp;&nbsp;No, Thanks&nbsp;&nbsp;</button>
				</td>
				<td align="right" style="padding-right:20px;">
					<button class="imgButtonBrown" style="margin-left: 15px;" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Remind Me Later&nbsp;&nbsp;</button>
					<button class="imgButtonOrange" style="margin-left: 10px;" onclick="doSmsRemoteOverlay1('sms_alerts.jsp?submitbutton=update'); return false;">&nbsp;Sign Up&nbsp;</button>
				</td>
				</tr></table>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
	</table>
</form>
</div>
<% } %>
</center>
</fd:RegistrationController>
</body>
</html>
