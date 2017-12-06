<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% //expanded page dimensions
final int W_YA_REMINDER_SERVICE = 970;
%>

<jwr:style src="/your_account.css" media="all"/>

<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Delivery Addresses</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Account - Delivery Addresses" pageId="remainder_service"></fd:SEOMetaTag>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%! String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};%>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);%>
<fd:ReminderEmailController actionName="updateReminder" result="result" customerInfo="<%=custInfo%>">
  <table width="<%= W_YA_REMINDER_SERVICE %>" align="center" border="0" cellpadding="0" cellspacing="0">
		<form name="reminder_signup" method="POST">
			<tr>
				<td colspan="2" class="text11">
					<h1 tabindex="0" class="title18">Reminder Service Preferences</h1>
					<% if (custInfo.getReminderFrequency() > 0) { %>
            <p class="no-margin">You are currently signed up to receive an e-mail order reminder <b><%=custInfo.getReminderFrequency() == 7 ? "every week" : "every other week"%></b> on <b><%=dayNames[custInfo.getReminderDayOfWeek() - 1]%></b>.</p>
            <p class="no-margin">Change your options below.</p>
					<% } else { %>
					If you ever worry that you'll forget to place your FreshDirect order in time, sign up for our handy, free e-mail reminder service. On the weekday of your choosing, first thing in the morning, you'll receive an e-mail reminding you to place your order for the next day. 
					Just fill out the short form below and click "save changes."<br>You can cancel this free service at any time.
					<% } %>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0"><br>
          <img src="/media_stat/images/layout/ff9933.gif" alt="" width="<%= W_YA_REMINDER_SERVICE %>" height="1" border="0"><br>
					<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0">
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
          <a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>" class="no-decor">
            <button tabindex="-1" type="button" class="cssbutton green transparent">Cancel</button>
          </a>
          <button type="submit" name="update_user" class="cssbutton orange normal">Save Changes</button>
				</td>
			</tr>
			<tr valign="top">
        <td colspan="2">
          <div class="continue-shopping text-left">
            <a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
             CONTINUE SHOPPING
            <BR>from <FONT CLASS="text11bold">Home Page</A></FONT>
          </div>
        </td>
			</tr>
			</form>
		</table>
		</fd:ReminderEmailController>
	</tmpl:put>
</tmpl:insert>
