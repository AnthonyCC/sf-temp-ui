<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@page import="com.freshdirect.common.address.PhoneNumber"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
  <head>
    <title>FreshDirect</title>
    <%@ include file="/common/template/includes/seo_canonical.jspf" %>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<%-- NOT CCL @ include file="/shared/template/includes/ccl.jspf" --%>
	<style>
		.trpad {
			padding-left: 10px;
			text-align: right;
		}
		
		.smshline {
			background: url("/media/editorial/site_access/images/dots_h.gif") repeat-x scroll 0 0 transparent;
			font-size: 1px;
			height: 1px;
			line-height: 1px;
			width: 730px;
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
<% } else { %>
<div style="height:550px; overflow-y: auto; overflow-x: hide;">
<form id="smsform" name="smsform" method="post" action="">
	<input type="hidden" name="actionName" value="ordermobilepref" />	

	<table border="0" cellpadding="0" cellspacing="0">
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td class="text11" style="padding-left: 10px;">
				<img src="/media_stat/images/navigation/preferences_title.gif" border="0" /> <br/><br/>
			</td>
		</tr>
		<tr><td><div class="smshline" id=""><!-- --></div></td></tr>
		<tr>
			<td>
				<table>
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr><td colspan="2"><span class="title18or trpad">1.&nbsp;</span><span class="title18">Verify primary contact information</span></td></tr>						
					<tr><td colspan="2" >&nbsp;</td></tr>					
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr valign="top">
						<td colspan="2" style="padding-right:5px; font-weight: bold; text-align: left;" class="text14 trpad" valign="middle">
							<input type="text" size="28" maxlength="45" class="text9" name="busphone" value="<%=phone%>" style="width:175px; padding:1px; height:30px;font-size:13px" /> &nbsp;&nbsp;Ext.<input type="text" size="5" maxlength="5" class="text9" name="busphoneext" value="<%=phoneExt%>" style="width:75px; padding:1px; height:30px;font-size:13px" />
						</td>
					</tr>
					<tr><td colspan="2" class="text11rbold trpad" style="width:600px;text-align: left;height:25px;"><fd:ErrorHandler result='<%=result%>' name='busphone' id='errorMsg'><%=errorMsg%></fd:ErrorHandler>&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<%-- <tr><td><div class="smshline" id=""><!-- --></div></td></tr>
		<tr>
			<td>		
				<table width="100%" border="0">
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr><td colspan="2" ><span class="title18or trpad">2.&nbsp;</span><span class="title18">Receive text messages:</span></td></tr>
					<tr><td colspan="2" class="text11rbold trpad" style="text-align: left;">&nbsp;<fd:ErrorHandler result='<%=result%>' name='text_option' id='errorMsg'>&nbsp;<%=errorMsg%></fd:ErrorHandler></td></tr>
					<tr valign="middle">
						<td class="text12 trpad" style="width: 30px;"><input class="radio" type="checkbox" name="text_delivery" value="Y" <%=text_delivery ? "checked":""%> /></td>
						<td class="text12" style="padding-right: 10px;">Yes please notify me via text message with important information about my delivery.</td>
					</tr>
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr valign="middle">
						<td class="text12 trpad" style="width: 30px;"><input class="radio" type="checkbox" name="text_offers" value="Y" <%=text_offers ? "checked":""%> /></td>
						<td class="text12" style="padding-right: 10px;">Yes please notify me about <b>offers, discounts</b> and <b>promotions</b> from time to time.</td>
					</tr>
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr valign="top">
						<td colspan="2" class="trpad">
							<table>						
								<tr>						
								<td style="padding-right:5px;font-weight:bold;" class="text12">Enter mobile phone number*<br />
									<input type="text" size="28" maxlength="45" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:175px; padding:1px; height:30px;font-size:13px" /></td>
									<td class="text12" style="color:gray; font-style:italic; text-indent: -10px; padding: 0 5px;" align="left" colspan="2" valign="center">* Standard text messaging rates apply. You can unsubscribe anytime - <br /> simply go to "My Account" section and update your preferences.</td>
								</tr>						
							</table>
						</td>
					</tr>
					<tr><td colspan="2" class="text11rbold trpad" style="width:500px; text-align: left;padding-left: 15px;height:25px;">&nbsp;<fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><%=errorMsg%></fd:ErrorHandler></td></tr>
				</table>
			</td>
		</tr> --%>
		<tr><td><div class="smshline" id=""><!-- --></div></td></tr>
		<tr>
			<td>
				<table width="100%" border="0">
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr><td colspan="2" ><span class="title18or trpad">2.&nbsp;</span><span class="title18">Go green!</span>&nbsp;<img src="/media_stat/images/navigation/go_green_leaf.gif" border="0" alt="GO GREEN" /></td></tr>
					<tr><td colspan="2" >&nbsp;</td></tr>
					<tr valign="middle">
						<td class="text12 trpad" style="width: 30px;"><input class="radio" type="checkbox" name="go_green" value="Y" <%=go_green ? "checked":""%> /></td>
						<td class="text12" style="padding-right: 10px;">I want to turn off paper statement delivery and receive my statements online.<br /></td>
					</tr>
					<tr valign="top">
						<td class="text12" style="width: 30px;">&nbsp;</td>
						<td><span class="text12" style="color:gray; font-style:italic; padding-right: 10px;">Please note that the following requests will come into effect on your next order.</span></td>				
					</tr>					
					<tr><td colspan="2" >&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr><td><div class="smshline" id=""><!-- --></div></td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" border="0"><tr>
				<td align="left" class="trpad" style="text-align: left; padding-left: 20px;">
					<button class="imgButtonWhite" onclick="doRemoteOverlay1('sms_capture.jsp?submitbutton=nothanks'); return false;">&nbsp;&nbsp;No, Thanks&nbsp;&nbsp;</button>
				</td>
				<td align="right" style="padding-right:20px;">
					<button class="imgButtonBrown" style="margin-left: 15px;" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Remind Me Later&nbsp;&nbsp;</button>
					<button class="imgButtonOrange" style="margin-left: 10px;" onclick="doRemoteOverlay1('sms_capture.jsp?submitbutton=update'); return false;">&nbsp;Update Preferences&nbsp;</button>
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
