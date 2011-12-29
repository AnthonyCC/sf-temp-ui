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
	<style>
		.trpad {
			padding-left:20px;
		}
	</style>
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
		
	//System.out.println("home phone: " + cm.getHomePhone().getPhone() + cm.getHomePhone().getExtension());
	String phone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
	String phoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
	//System.out.println("Bus phone: " + cm.getBusinessPhone().getPhone() + cm.getBusinessPhone().getExtension());
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
	if(session.getAttribute("SMSSubmission"+orderNumber) != null)
		form_processed = true;
%>
<center>
<% if(form_processed) { %>
	<table border="0" cellpadding="0" cellspacing="0">	
	<tr>
		<td class="text16bold trpad" valign="top">
			<img src="/media_stat/images/navigation/tickmark.gif" border="0" />&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td class="title19">Your preferences have been updated.
		<br/><span class="text13gr" style="font-weight:normal;color:gray;">You can update your preferences anytime - simply go to <a href="/your_account/manage_account.jsp">My Account</a> section and change your preferences.</span>
		</td>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr><td  colspan="2"><img src="/media_stat/images/layout/dotted_line.gif" width="<%= W_DOTTED_LINE %>" height="1" border="0" vspace="5"></td></tr>
	<tr><td colspan="2">&nbsp;</td></tr>
	<tr><td align="center" colspan="2">
		<table class="butCont"> <tr>
				<td class="butOrangeLeft"><!-- --></td>
				<td class="butOrangeMiddle"><a class="butText" style="text-shadow:none;font-weight:bold;padding:0 70px;vertical-align:middle;" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Close&nbsp;&nbsp;</a></td>
				<td class="butOrangeRight"><!-- --></td>
		</tr> </table>
	</td></tr>
	</table>
<% } else { %>
<form id="smsform" name="smsform" method="POST" action="">
	<input type="hidden" name="actionName" value="ordermobilepref">	

<table border="0" cellpadding="0" cellspacing="0">
<tr><td>&nbsp;</td></tr>
<tr>
	<td class="text11 trpad">
		<img src="/media_stat/images/navigation/preferences_title.gif" border="0" /> <br/><br/>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/dotted_line.gif" width="<%= W_DOTTED_LINE %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>
		<table>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" class="trpad"><span class="title18or">1.&nbsp;</span><span class="title18">Verify primary contact information</span></td></tr>						
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" class="text12 trpad">Please verify your emergency contact information below.</td></tr>			
			<tr valign="top">
				<td colspan="2" style="padding-right:5px;font-weight:bold;" class="text14 trpad" valign="center">
					<input type="text" size="28" maxlength="45" class="text9" name="busphone" value="<%=phone%>" style="width:175px; padding:1px; height:30px;font-size:13px"> &nbsp;&nbsp;Ext.<input type="text" size="5" maxlength="5" class="text9" name="busphoneext" value="<%=phoneExt%>" style="width:75px; padding:1px; height:30px;font-size:13px">
				</td>
			</tr>
			<tr><td colspan="2" class="text11rbold trpad" style="width:500px;"><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><%=errorMsg%></fd:ErrorHandler></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
		</table>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/dotted_line.gif" width="<%= W_DOTTED_LINE %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>		
		<table width="100%" border="0">
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" ><span class="title18or trpad">2.&nbsp;</span><span class="title18">Receive text messages:</span></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" class="text11rbold trpad"><fd:ErrorHandler result='<%=result%>' name='text_option' id='errorMsg'><%=errorMsg%></fd:ErrorHandler></td></tr>
			<tr valign="top" colspan="2">
				<td class="text12 trpad" style="padding-right:5px;" colspan="2"><input class="radio" type="checkbox" name="text_delivery" value="Y" <%=text_delivery ? "checked":""%>>&nbsp;&nbsp;Yes please notify me via text message with important information about my delivery.</td>
			</tr>
			
			<tr valign="top">
				<td class="text12 trpad" style="padding-right: 5px;" valign="bottom" colspan="2"><input class="radio" type="checkbox" name="text_offers" value="Y" <%=text_offers ? "checked":""%>>&nbsp;&nbsp;Yes please notify me about <b>offers, discounts</b> and <b>promotions</b> from time to time.</td>
			</tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr valign="top">
				<td colspan="2" class="trpad">
					<table>						
						<tr>						
						<td style="padding-right:5px;font-weight:bold;" class="text12">Enter mobile phone number*<br/>
						<input type="text" size="28" maxlength="45" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:175px; padding:1px; height:30px;font-size:13px"></td>
						<td style="padding-right: 5px;" align="left" colspan="2"><FONT class="text12" style="color:gray;font-style:italic;">* Standard text messaging rates apply. You can unsubscribe anytime - <br/> simply go to "My Account" section and update your preferences.</FONT></td>
						</tr>						
					</table>
				</td>
			</tr>
			<tr><td colspan="2" class="text11rbold trpad" style="width:500px;"><fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><%=errorMsg%></fd:ErrorHandler></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
		</table>
	</td>
</tr>
<tr><td><img src="/media_stat/images/layout/dotted_line.gif" width="<%= W_DOTTED_LINE %>" height="1" border="0" vspace="5"></td></tr>
<tr>
	<td>
		<table width="100%" border="0">
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr><td colspan="2" class="trpad"><span class="title18or">3.&nbsp;</span><span class="title18">Go green!</span>&nbsp;<img src="/media_stat/images/navigation/go_green_leaf.gif" border="0" alt="GO GREEN"></td></tr>
			<tr><td colspan="2" >&nbsp;</td></tr>
			<tr valign="top">
				<td class="text12 trpad" style="padding-right:5px;" colspan="2"><input class="radio" type="checkbox" name="go_green" value="Y" <%=go_green ? "checked":""%>>&nbsp;&nbsp;I want to turn off paper statement delivery and receive my statements online.<br/>
				<FONT class="text12" style="color:gray;font-style:italic;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please note that the following requests will come into effect on your next order.</font></td>				
			</tr>					
			<tr><td colspan="2" >&nbsp;</td></tr>
		</table>
		
		<img src="/media_stat/images/layout/dotted_line.gif" width="<%= W_DOTTED_LINE %>" height="1" border="0" vspace="5">
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
<tr>
	<td>
		<table width="100%" border="0"><tr>
		<td align="left" class="trpad">
			<table class="butCont">
				<tr>
					<td class="butWhiteLeft"><!-- --></td>
					<td class="butWhiteMiddle" valign="center"><a class="butText" style="color:#000000;text-shadow:none;font-weight:bold;vertical-align:middle;" href="#" onclick="doRemoteOverlay1('sms_capture.jsp?submitbutton=nothanks'); return false;">&nbsp;&nbsp;No, Thanks&nbsp;&nbsp;</a></td>
					<td class="butWhiteRight"><!-- --></td>
				</tr>
			</table>
		</td>
		<td align="right" style="padding-right:20px;">
		<table class="butCont fright" style="margin-left: 15px;">
			<tr>
				<td class="butBrownLeft"><!-- --></td>
				<td class="butBrownMiddle"><a class="butText" style="color:#000000;text-shadow:none;font-weight:bold;text-shadow:none;vertical-align:middle;" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Remind Me Later&nbsp;&nbsp;</a></td>
				<td class="butBrownRight"><!-- --></td>
				<td>&nbsp;&nbsp;</td>
				<td class="butOrangeLeft"><!-- --></td>
				<td class="butOrangeMiddle"><a class="butText" style="font-weight:bold;text-shadow:none;vertical-align:middle;" href="#" onclick="doRemoteOverlay1('sms_capture.jsp?submitbutton=update'); return false;">Update Preferences</a></td>
				<td class="butOrangeRight"><!-- --></td>
			</tr>
		</table> 		
		</td>
		</tr></table>
	</td>
</tr>
<tr><td>&nbsp;</td></tr>
</table>
</form>
<% } %>
</center>
</fd:RegistrationController>
</body>
</html>
