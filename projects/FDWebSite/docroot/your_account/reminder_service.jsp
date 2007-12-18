<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Delivery Addresses</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%! String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};%>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);%>
<fd:ReminderEmailController actionName="updateReminder" result="result" customerInfo="<%=custInfo%>">
		<table width="675" align="center" border="0" cellpadding="0" cellspacing="0">
		<form name="reminder_signup" method="POST">
			<tr>
				<td colspan="2" class="text11">
					<font class="title18">Reminder Service Preferences</font><br>
					<% if (custInfo.getReminderFrequency() > 0) { %>You are currently signed up to receive an e-mail order reminder <b><%=custInfo.getReminderFrequency() == 7 ? "every week" : "every other week"%></b> on <b><%=dayNames[custInfo.getReminderDayOfWeek() - 1]%></b>.<br><%-- } --%>Change your options below.
					<% } else { %>
					If you ever worry that you'll forget to place your FreshDirect order in time, sign up for our handy, free e-mail reminder service. On the weekday of your choosing, first thing in the morning, you'll receive an e-mail reminding you to place your order for the next day. 
					Just fill out the short form below and click "save changes."<br>You can cancel this free service at any time.
					<% } %>
					<br>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0"><br>
					<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0">
				</td>
			</tr>
			<tr>
				<td></td>
				<td>
					<%@ include file="/includes/your_account/reminder.jspf" %><BR>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<br>
					<a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" tabindex="2" vspace="3" hspace="3" border="0" alt="cancel"></a><input type="image" tabindex="3" name="update_user" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="save changes" vspace="3" hspace="3" border="0">
					<br><br>
					<img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0"><br>
					<font class="space4pix"><br><br></font>
				</td>
			</tr>
			<tr valign="top">
				<td width="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="continue shopping" align="left"></a></td>
				<td width="640"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="continue shopping"></a>
					<br>from <font class="text11bold"><a href="/index.jsp">home page</a></font><br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
				</td>
			</tr>
			</form>
		</table>
		</fd:ReminderEmailController>
	</tmpl:put>
</tmpl:insert>