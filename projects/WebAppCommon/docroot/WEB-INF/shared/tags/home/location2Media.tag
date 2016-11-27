<%@ 
	attribute
	name="user" required="true" rtexprvalue="true"
	type="com.freshdirect.fdstore.customer.FDUserI"
%><%@ taglib uri='freshdirect' prefix='fd' 
%>			<table width="100%" cellpadding="0" cellspacing="0" border="0" id="index_table_ordModify_2">
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="310" height="6"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="6"></td>
				</tr>
				<tr>
					<td colspan="2" bgcolor="#ccc"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></td>
				</tr>
				<tr>
					<td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" alt="" /></td>
				</tr>
			</table>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td>
						<fd:IncludeMedia name="/media/editorial/hp_notes/winback/lapsed.html" />
						<% user.setCampaignMsgViewed(user.getCampaignMsgViewed() + 1); %>
					</td>
				</tr>
			</table>
