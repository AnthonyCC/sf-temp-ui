<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>
<%@ page import="com.freshdirect.customer.ErpCustomerInfoModel" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerFactory" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@page import="com.freshdirect.common.address.PhoneNumber"%>
<%@page import="com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.EnumEStoreId" %>
<%@ page import ='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>
<%@ page import='com.freshdirect.framework.webapp.ActionResult' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="false" redirectPage='/checkout/view_cart.jsp' />
<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - SMS Settings</tmpl:put>
<tmpl:put name="extraHead">
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
			padding-left: 10px;
			padding-right: 10px;
		}
		.smsErrorMsgCont {
			margin: 10px 0 5px 10px;
		}
		#MB_caption {
			color: #333;
		}
		
	</style>
</tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:RegistrationController actionName='<%=request.getParameter("actionName")%>' result='result'>
		<%
			FDIdentity identity  = user.getIdentity();
			ErpCustomerInfoModel cm = FDCustomerFactory.getErpCustomerInfo(identity);
			FDCustomerModel fdCustomer= FDCustomerFactory.getFDCustomer(identity);
			String eStoreId = user.getUserContext().getStoreContext().getEStoreId().toString();	
			String mobile_number=null;
			boolean text_offers = false;
			boolean text_delivery =false;
			
			if("FDX".equalsIgnoreCase(eStoreId) && fdCustomer.getCustomerSmsPreferenceModel()!=null){
				
				 mobile_number=fdCustomer.getCustomerSmsPreferenceModel().getFdxMobileNumber()==null ?"":fdCustomer.getCustomerSmsPreferenceModel().getFdxMobileNumber().getPhone();
				 text_delivery=fdCustomer.getCustomerSmsPreferenceModel().getFdxdeliveryNotification();
				 text_offers = fdCustomer.getCustomerSmsPreferenceModel().getFdxOffersNotification();
				
				 if(request.getParameter("text_offers") != null)
					text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
				if(request.getParameter("text_delivery") != null)
					text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false;
				if(request.getParameter("mobile_number") != null)
					mobile_number = request.getParameter("mobile_number");
				}
			else{
				if(fdCustomer.getCustomerSmsPreferenceModel()!=null){
				
					mobile_number=fdCustomer.getCustomerSmsPreferenceModel().getMobileNumber()!=null ? fdCustomer.getCustomerSmsPreferenceModel().getMobileNumber().getPhone():"";
					text_offers=fdCustomer.getCustomerSmsPreferenceModel().getOffersNotification();
					text_delivery=fdCustomer.getCustomerSmsPreferenceModel().getFdxdeliveryNotification();
				}
					if(request.getParameter("text_offers") != null)
						text_offers = "Y".equals(request.getParameter("text_offers"))?true:false;
					if(request.getParameter("text_delivery") != null)
						text_delivery = "Y".equals(request.getParameter("text_delivery"))?true:false;
					if(request.getParameter("mobile_number") != null)
						mobile_number = request.getParameter("mobile_number");
				
			}
			
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
				if (nothanks) {	%>
					<script type="text/javascript">
						$jq('#MB_caption').html($jq('#sms_header').html());
					</script>
					<div id="sms_header" style="display: none;">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td width="35"><img src="/media_stat/images/navigation/tickmark.gif" border="0" style="margin-right: 10px;" /></td>
								<td><span class="title19">Your preferences have been updated.</span></td>
							</tr>
						</table>
					</div>
					
					<div style="overflow-y: auto; overflow-x: hidden; margin: 0 30px; width: 450px;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr><td>&nbsp;</td></tr>
							<tr><td>
								<span class="text13gr" style="font-weight:normal; color:gray;">You can update your preferences any time by heading to <a href="/your_account/signin_information.jsp">My Account</a></span>
							</td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td><div class="smshline" width="100%"><!-- --></div></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td align="center">
									<a class="imgButtonOrange" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Close&nbsp;&nbsp;</a>
								</td>
							</tr>
						</table>
					</div>
				<%} else {
					%>
					<script type="text/javascript">
						$jq('#MB_caption').html($jq('#sms_header').html());
					</script>
					<div id="sms_header" style="display: none;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr>
								<td width="35"><img src="/media_stat/images/navigation/tickmark.gif" border="0" style="margin-right: 10px;" /></td>
								<td><span class="title19">YOU'RE ALMOST DONE...</span></td>
							</tr>
						</table>
					</div>
					
					<div style="overflow-y: auto; overflow-x: hidden; margin: 0 30px; width: 700px;">
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<span class="text14bold">We've just sent a text to <%=mobile_number%>. Please check your messages to complete the sign up process!</span>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td>
									<span class="text13gr" style="font-weight:normal;color:gray;">Your preferences have been updated. <br />
									You can update your preferences or change your mobile phone number any time - simply go to <a href="/your_account/signin_information.jsp">My Account</a></span>
								</td>
							</tr>
							<tr><td>&nbsp;</td></tr>
							<tr><td><div class="smshline" style="width: 100%;"><!-- --></div></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td align="center">
									<a class="imgButtonOrange" href="#" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Close&nbsp;&nbsp;</a>
								</td>
							</tr>
						</table>
					</div>
				<% }
			} else { %>
				<div style=" overflow-y: auto; overflow-x: hidden; margin: 0 30px; width: 600px;">
					<form id="smsalertform" name="smsalertform" method="post" action="" style="overflow:hidden">
						<input type="hidden" name="actionName" value="ordersmsalerts" />	
						<table border="0" cellpadding="0" cellspacing="0" width="100%">
							<tr><td><fd:IncludeMedia name="/media/editorial/site_pages/sms/co_head.html" /></td></tr>
							<tr><td><fd:IncludeMedia name="/media/editorial/site_pages/sms/terms_short.html" /></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr valign="top">
								<td style="padding-right:5px;font-weight:bold;" class="text12">
									Enter mobile phone number *<br /><br />
									<input type="text" size="28" maxlength="20" class="text9" name="mobile_number" value="<%=mobile_number%>" style="width:175px; padding:1px; height:20px;font-size:13px" />
								</td>
							</tr>
							<tr><td class="text11rbold" style="text-align: left; padding-left: 15px;"><fd:ErrorHandler result='<%=result%>' name='mobile_number' id='errorMsg'><div class="smsErrorMsgCont"><%=errorMsg%></div></fd:ErrorHandler></div></td></tr>
							<tr><td><fd:IncludeMedia name="/media/editorial/site_pages/sms/terms_medium.html" /></td></tr>
							<tr><td>&nbsp;</td></tr>
							<tr>
								<td>
									<table width="100%" border="0"><tr>
									<td align="left" class="" style="text-align: left; padding-left: 20px;">
										<button class="imgButtonWhite" onclick="doSmsRemoteOverlay1('sms_alerts.jsp?submitbutton=nothanks'); return false;">&nbsp;&nbsp;No, Thanks&nbsp;&nbsp;</button>
									</td>
									<td align="right" style="padding-right:20px;">
										<button class="imgButtonBrown" style="margin-left: 15px;" onclick="Modalbox.hide(); return false;">&nbsp;&nbsp;Remind Me Later&nbsp;&nbsp;</button>
										<button class="imgButtonOrange" style="margin-left: 10px;" onclick="doSmsRemoteOverlay1('sms_alerts.jsp?submitbutton=update'); return false;">&nbsp;Sign Up&nbsp;</button>
									</td>
									</tr></table>
								</td>
							</tr>
						</table>
					</form>
				</div>
			<% } %>
		</center>
	</fd:RegistrationController>
</tmpl:put>
</tmpl:insert>
