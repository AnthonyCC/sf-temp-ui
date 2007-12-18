<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>

<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%! String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};%>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);%>
<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - E-mail Reminder</tmpl:put>
		<tmpl:put name='content' direct='true'>
		<script language="JavaScript">
		<!--
		function linkTo(url){
			redirectUrl = "http://" + location.host + url;
			parent.opener.location = redirectUrl;
		}
		-->
		</script>
		<table width="520" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td align="center" class="text12"><br><br><br>
				<div style="font-size: 48px; font-face:Arial,Verdana,Helvetica;"><b>THANK YOU.</b></div>
				We will send you an e-mail reminder <b><%=custInfo.getReminderFrequency() == 7 ? "every week" : "every other week"%></b> on <b><%=dayNames[custInfo.getReminderDayOfWeek() - 1]%></b>.<br>
				You can change or cancel this service in <a href="javascript:linkTo('/your_account/reminder_service.jsp')">Your Account</a>.
				<br><img src="/media_stat/images/layout/clear.gif" width="1" height="14"><br><img src="/media_stat/images/template/tell_a_friend/confirm_berry.jpg" width="70" height="70"><br><br><br><br></td></tr>
		</table>
	</tmpl:put>
</tmpl:insert>
