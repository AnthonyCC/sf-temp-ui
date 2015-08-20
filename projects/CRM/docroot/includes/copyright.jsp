<%@ page import='java.util.*' %>
<table width="100%" cellspacing="4" cellpadding="0" class="cust_module_content_note">
<tr>
	<td width="300">
		<table class="cust_module_content_note">
			<tr valign="bottom">
				<td><a href="https://www.freshdirect.com" target="freshdirect"><img src="/media_stat/crm/images/fd_logo.gif" width="72" height="16" border="0" alt="FreshDirect" vspace="0"></a></td><td style="line-height: 8pt;">Customer Relationship Manager</td>
			</tr>
		</table>
	</td>
	<td width="" align="center">
		<span class="bookend_l">&nbsp;</span>
		<span id="help_overview" class="help_overview"><a href="javascript:void(0);" onmouseover="return overlib('Mouseover text links for helpful topics &raquo;', AUTOSTATUS, WRAP, REF,'help_overview',REFP,'LL');" onmouseout="nd();">?</a></span>
		<div class="help_topic" style="height: 18px;"><%@ include file="/includes/help_topics.jsp" %></div>
		<span class="help_link"><a name="news" onclick="hoverHelp('news','News & Updates','/includes/news.jsp',250,300); return false;" href="javascript:void(0);">News</a></span>
		<span class="bookend_r">&nbsp;</span>
	</td>
	<td width="300" align="right" valign="middle">&copy; 2002 - <%= Calendar.getInstance().get(Calendar.YEAR) %> Fresh Direct, LLC. All Rights Reserved.</td>
</tr>
</table>