<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:CheckLoginStatus guestAllowed="false" recognizedAllowed="false" />
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Your Account - Newsletter Signup</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%! String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};%>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
FDIdentity identity  = user.getIdentity();
ErpCustomerInfoModel custInfo = FDCustomerFactory.getErpCustomerInfo(identity);%>
<fd:NewsLetterController actionName="updateOptinNewsletter" result="result" customerInfo="<%=custInfo%>">
		<table width="675" align="center" border="0" cellpadding="0" cellspacing="0">
		<form name="reminder_signup" method="POST">
			<tr>
				<td colspan="2" class="text12" valign="top">
					<font class="title18">President's Picks: Great deals, direct to your inbox</font><br>
					Each week, we choose a few of our favorite products and decree savings up to 50% off. 
					Some might call it an abuse of pricing power. You'll call it a great way to shop and save. 
					Sign up here, and we'll email you our picks &mdash; and special prices &mdash; each week.
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0"><br>
					<span class="title14"><strong>SUBSCRIBE TO PRESIDENT'S PICKS</strong></span>
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="15" border="0">
				</td>
				<td rowspan="4" align="center" style="padding-left:20px;"><a href="/newsletter.jsp?catId=picks_love"><img src="/media/editorial/picks/pres_picks/img/pres_picks_preview.jpg" border="0" alt="President's Picks Newsletter"><br><br><strong>Click here for this week's picks.</strong></a></td>
			</tr>
			<tr valign="top">
				<td class="text12" align="right" style="padding-right:5px;"><input type="radio" name="sendOptinNewsletter" value="yes" <%= custInfo.isReceiveOptinNewsletter() ? "checked=\"true\"" : "" %>/></td>
				<td class="text12" style="padding-top:3px; padding-right:50px;">
					<b><%= custInfo.isReceiveOptinNewsletter() ? "You are currently subscribed." : "Yes, please! Sign me up." %></b>
					<br>You can unsubscribe anytime &mdash; and, of course, we'll never share your information with anyone else.<br><br>
				</td>
			</tr>
			<tr valign="top">
				<td class="text12" align="right" style="padding-right:5px;"><input type="radio" name="sendOptinNewsletter" value="no" <%= custInfo.isReceiveOptinNewsletter() ? "" : "checked='true'" %>/></td>
				<td class="text12" style="padding-top:3px; padding-right:50px;"><%= custInfo.isReceiveOptinNewsletter() ? "Please cancel my subscription." : "No thanks, I don't really enjoy low prices." %>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left:10px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="25" border="0"><br>
					<a href="<%=response.encodeURL("/your_account/manage_account.jsp")%>"><img src="/media_stat/images/buttons/cancel.gif" width="54" height="16" tabindex="2" vspace="3" hspace="3" border="0" alt="cancel"></a><input type="image" tabindex="3" name="update_user" src="/media_stat/images/buttons/save_changes.gif" width="84" height="16"  alt="save changes" vspace="3" hspace="3" border="0">
				</td>
			</tr>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="35" height="20" border="0"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="480" height="20" border="0"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="160" height="20" border="0"></td>
			</tr>
			<tr><td colspan="3"><img src="/media_stat/images/layout/ff9933.gif" width="675" height="1" border="0" vspace="8"></td></tr>
			<tr valign="top">
				<td><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="continue shopping" align="left"></a></td>
				<td colspan="2"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="continue shopping"></a>
					<br>from <font class="text11bold"><a href="/index.jsp">home page</a></font><br><img src="/media_stat/images/layout/clear.gif" width="340" height="1" border="0">
				</td>
			</tr>
			</form>
		</table>
		</fd:NewsLetterController>
	</tmpl:put>
</tmpl:insert>
