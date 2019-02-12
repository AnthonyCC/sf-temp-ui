<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.crm.CrmVSCampaignModel"%>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Voiceshot Campaigns</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
 		<crm:GetCurrentAgent id='currAgent'>

<%
	CrmVSCampaignModel cModel = null;
	String id = request.getParameter("cid");
	boolean processed = false;
	
%>

<form method="post" name="addcampaign">
<input type="hidden" name="submission" value="done" />
<span style="padding:5px;font-size:20px;font-weight:bold;">Delete Campaign</span>
<br/>
<% if(!processed) { %>
<input type="hidden" name="cid" value="<%=request.getParameter("cid")%>" />
<table cellpadding="5" cellspacing="10" border="0" style="font-size:12pt;">
	<tr><td align="right"  valign="top">Campaign Name:</td>
		<td><%=cModel.getCampaignName()%></td>
	</tr>
	<tr><td align="right" valign="top">Campaign MenuID:</td>
		<td><%=cModel.getCampaignMenuId()%></td>
	</tr>
	<tr><td align="right" valign="top">Sound File Name:</td>
		<td><%=cModel.getSoundfileName()%></td>
	</tr>
	<tr><td align="right" valign="top">Sound File Message:</td>
		<td><%=cModel.getSoundFileText()%></td>
	</tr>
	<tr><td align="right"><span style="padding:5px;font-size:20px;font-weight:bold;">Are you sure?</span></td>
		<td align="center"><a href="/transportation/campaigns.jsp" style="text-decoration:none;background-color:orange;color:white;font-weight:bold;border:1px orange solid;padding:2px;width:60px;">Cancel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="Delete" value="Delete" style="text-decoration:none;background-color:green;color:white;font-weight:bold;border:1px green solid;padding:2px;width:60px;"/></td>
	</tr>
</table>
<% } else { %>
	<br/> <span style="font-size:12pt;font-weight:bold;padding:5px;">Campaign deleted successfully. Click on Campiagns to get back to the Campaign listing page.
	<br/><br/><a href="/transportation/campaigns.jsp" style="text-decoration:none;background-color:green;color:white;font-weight:bold;border:1px green solid;padding:5px;width:60px;"/>Back to Campaigns</a>
	</span>
<% } %>
</form>			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>
