<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory" %>
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
	<%-- NOT CCL @ include file="/shared/template/includes/ccl.jspf" --%>
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0">

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
		
	System.out.println(cm.getHomePhone().getPhone() + cm.getHomePhone().getExtension());
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
	final int W_YA_SIGNIN_INFO = 650;
	boolean form_processed = false;
	final String orderNumber = (String)session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
	if(session.getAttribute("SMSSubmission"+orderNumber) != null)
		form_processed = true;
%>
<center>
<% if(form_processed) { %>
	<table width="<%= W_YA_SIGNIN_INFO %>" border="0" cellpadding="0" cellspacing="0">
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td class="text11">
			&nbsp;<img src="/media_stat/images/navigation/preferences_title.gif" border="0" /> <br/><br/>
		</td>
	</tr>
	<tr style="background-color:#EEEEEE;">
		<td><br/><br/><span class="title18">&nbsp;Your preferences have been updated. </span><br/><br/></td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td></tr>
	</table>
<% } else { %>
<form id="smsform" name="smsform" method="POST" action="">
	<input type="hidden" name="actionName" value="ordermobilepref">	

<table border="0" cellpadding="0" cellspacing="0">
<tr><td>&nbsp;</td></tr>
<tr>
	<td class="text11">
		&nbsp;&nbsp;<img src="/media_stat/images/navigation/preferences_title.gif" border="0" /> <br/><br/>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/cccccc.gif" width="<%= W_YA_SIGNIN_INFO %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>
		<table>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" ><span class="title18or">1.&nbsp;</span><span class="title18">Verify primary contact information</span></td></tr>						
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" style="padding-right:5px;" class="text12" style="font-weight:bold;">&nbsp;Please verify your emergency contact informationb below.</td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr valign="top">
				<td colspan="2" style="padding-right:5px;font-weight:bold;" class="text12">&nbsp;
					<input type="text" size="28" maxlength="45" class="text9" name="busphone" value="<%=phone%>" style="width:175px; padding:1px; height:20px;"> &nbsp;&nbsp;Ext.<input type="text" size="5" maxlength="5" class="text9" name="busphoneext" value="<%=phoneExt%>" style="width:75px; padding:1px; height:20px;">
				</td>
			</tr>
			<tr><td colspan="2"><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
		</table>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/cccccc.gif" width="<%= W_YA_SIGNIN_INFO %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>		
		<table>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" ><span class="title18or">2.&nbsp;</span><span class="title18">Receive text messages:</span></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr valign="top">
				<td align="right" style="padding-top:5px; padding-right:5px;"><input class="radio" type="checkbox" name="text_delivery" value="Y" <%=text_delivery ? "checked":""%>></td>
				<td style="padding-top:5px;" class="text12">Yes please notify me via text message with important information about my delivery.<br><br/>
				</td>
			</tr>
			<tr valign="top">
				<td style="padding-right: 5px;" align="right"><input class="radio" type="checkbox" name="text_offers" value="Y" <%=text_offers ? "checked":""%>></td>
				<td class="text12">Yes please notiofy me about <b>offers, discounts</b> and <b>promotions</b> from time to time.<br/><br/><br/></td>
			</tr>
			<tr valign="top">
				<td colspan="2">
					<table>						
						<tr>						
						<td style="padding-right:5px;font-weight:bold;" class="text12">Enter mobile phone number*<br/>
						<input type="text" size="28" maxlength="45" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:175px; padding:1px; height:30px;"></td>
						<td style="padding-right: 5px;" align="left" colspan="2"><FONT class="text12" style="color:gray;font-style:italic;">* Standard text messaging rates apply. You can unsubscribe anytime - <br/> simply go to "My Account" section and update your preferences.</FONT></td>
						</tr>
						<tr><td colspan="2"><fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
					</table>
				</td>
			</tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
		</table>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/cccccc.gif" width="<%= W_YA_SIGNIN_INFO %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>
		<table>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td colspan="2">
					<span class="title18or">3.&nbsp;</span><span class="title18">Go green!</span>&nbsp;<img src="/media_stat/images/navigation/go_green_leaf.gif" border="0" alt="GO GREEN"><br>
				</td>
			</tr>
			<td colspan="2">&nbsp;</td></tr>
			<tr valign="top">
				<td align="right" style="padding-top:5px; padding-right:5px;"><input class="radio" type="checkbox" name="go_green" value="Y" <%=go_green ? "checked":""%>></td>
				<td style="padding-top:5px;" class="text12">I want to turn off paper statement delivery and receive my statements online.<br><br/>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
		</table>
		<img src="/media_stat/images/layout/cccccc.gif" width="<%= W_YA_SIGNIN_INFO %>" height="1" border="0" vspace="5">
	</td>
</tr>
<tr>
	<td>
		<table width="100%" border="0"><tr>
		<td align="left">
			<table class="butCont">
				<tr>
					<td class="butWhiteLeft"><!-- --></td>
					<td class="butWhiteMiddle"><a class="butText" style="color:#000000;text-shadow:none;" href="#" onclick="Modalbox.show('sms_capture.jsp?submitbutton=nothanks', {title: '', params: Form.serialize('smsform') }); return false;">&nbsp;&nbsp;No, thanks&nbsp;&nbsp;</a></td>
					<td class="butWhiteRight"><!-- --></td>
				</tr>
			</table>
		</td>
		<td align="right">
		<table class="butCont fright" style="margin-left: 15px;">
			<tr>
				<td class="butBrownLeft"><!-- --></td>
				<td class="butBrownMiddle"><a class="butText" style="color:#000000;text-shadow:none;" href="#" onclick="Modalbox.show('sms_capture.jsp?submitbutton=remind', {title: '', params: Form.serialize('smsform') }); return false;">&nbsp;&nbsp;Remind Me Later&nbsp;&nbsp;</a></td>
				<td class="butBrownRight"><!-- --></td>
				<td>&nbsp;</td>
				<td class="butOrangeLeft"><!-- --></td>
				<td class="butOrangeMiddle"><a class="butText" href="#" onclick="Modalbox.show('sms_capture.jsp?submitbutton=update', {title: '', params: Form.serialize('smsform') }); return false;">Update Preferences</a></td>
				<td class="butOrangeRight"><!-- --></td>
			</tr>
		</table> 		
		</td>
		</tr></table>
	</td>
</tr>
</table>
</form>
<% } %>
</center>
</fd:RegistrationController>
</body>
</html>
