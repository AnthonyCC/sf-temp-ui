<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/reminder/step_1_reminder_signup.jsp";
String redirectPage = "/login/login_popup.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
%>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - E-mail Reminder</tmpl:put>
		<tmpl:put name='content' direct='true'>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);%>
<fd:ReminderEmailController actionName="updateReminder" result="result" customerInfo="<%=custInfo%>" successPage="/reminder/step_2_reminder_confirmation.jsp">

	<table width="520" cellpadding="0" cellspacing="0" border="0">
		<form name="reminder_signup" method="POST">
		<tr>
			<td colspan="2"><img src="/media_stat/images/template/reminder/email_reminder.gif" width="287" height="21" vspace="4"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
			If you ever worry that you'll forget to place your FreshDirect order in time, sign up for our handy, free e-mail reminder service. On the weekday of your choosing, first thing in the morning, you'll receive an e-mail reminding you to place your order for the next day. 
			Just fill out the short form below and click "submit." You can cancel this free service at any time.
			</td>
		</tr>
		<tr valign="top">
			<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1"><br></td>
			<td><br>
				<%@ include file="/includes/your_account/reminder.jspf" %>
			</td>
		<tr>
			<td colspan="2" align="center"><br><input type="image" src="/media_stat/images/template/reminder/cancel.gif" width="63" height="19" onClick="javascript:window.close()"> <input type="image" src="/media_stat/images/template/reminder/submit.gif" width="60" height="19" onClick="javascript:document.reminder_signup.submit()"></td>
		</tr>
		</form>
	</table>
</fd:ReminderEmailController>

	</tmpl:put>
</tmpl:insert>
