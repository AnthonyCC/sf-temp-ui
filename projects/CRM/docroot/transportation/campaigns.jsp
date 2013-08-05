<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.crm.CrmVSCampaignModel"%>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Voiceshot Campaigns</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
 		<crm:GetCurrentAgent id='currAgent'>

<table width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:5px;">
	<tr><td align="right" style="padding-right:30px;padding-top:5px;padding-bottom:5px;"><a href="/transportation/add_campaign.jsp" style="text-decoration:none;background-color:green;color:white;font-size:12pt;font-weight:bold;padding:5px;">Add New Campaign</a></td></tr>
	<tr><td>
		<table width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:5px;">
			<tr class="list_header" style="background: #996666;">
				<td width="10%" align="center" class="list_header_text"><b>Campaign Name</b></td>
				<td width="10%" align="center" class="list_header_text"><b>Date Created</b></td>
				<td width="10%" align="center" class="list_header_text"><b>Created By</b></td>
				<td width="10%" align="center" class="list_header_text"><b>Date Modified</b></td>
				<td width="10%" align="center" class="list_header_text"><b>Modified By</b></td>
				<td width="7%" align="center" class="list_header_text"><b>&nbsp;</b></td>
				<td width="7%" align="center" class="list_header_text"><b>&nbsp;</b></td>
			</tr>
			</table>
			
			
			<%
				List<CrmVSCampaignModel> campaigns = CallCenterServices.getVSCampaignList();
			%>
			<script language="Javascript">
				var cids = new Array();
				var cnames = new Array();
				
				function getSoundFile(id1) {
					var sText = cids[id1];
					var container = document.getElementById("campaign_content");
					container.innerHTML ="<br/><br/><span style=\"background-color:yellow;font-size:14pt;font-weight:bold;\">" + cnames[id1] + "</span><br/><br/>"+sText;
					document.getElementById("CID"+id1).bgColor="yellow";
				}
			</script>
			<div style="height:200px; overflow:auto; align:top;border:1px solid #996666">
			<table width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:5px;background-color: #FFFFFF;color: #000000">
			<%
				//for(int j=0;j<50;j++) {
				for(int i=0;i<campaigns.size();i++) {
					CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
					String style = "background-color: #D8D8D8; color: black;";
					if((i%2) == 0) style="";					
					String msg = model.getSoundFileText();
					msg = msg.replaceAll("[\r\n|\n]+", " ");
					System.out.println(msg);
			%>
			<script language="javascript">				
				cids[<%=model.getCampaignId()%>] = '<%=msg%>';				
				cnames[<%=model.getCampaignId()%>] = '<%=model.getCampaignName()%> - <%=model.getSoundfileName()%>';
			</script>
			<tr id="CID<%=model.getCampaignId()%>" style="<%=style%>;font-size: 10pt;height: 75%;overflow: auto;padding: 0;position: relative;">			
				<td width="10%" class="border_bottom" align="center"><a href="javascript:getSoundFile(<%=model.getCampaignId()%>)" style="text-decoration:none"><%= model.getCampaignName() %></a></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getAddByDate() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getAddByUser() %></td>				
				<td width="10%" class="border_bottom" align="center"><%= model.getChangeByDate() %></td>				
				<td width="10%" class="border_bottom" align="center"><%= model.getChangeByUser() %></td>				
				<td width="7%" class="border_bottom" align="center">
					<a href="/transportation/update_campaign.jsp?cid=<%= model.getCampaignId()%>">modify</a> 
				</td>
				<td width="7%" class="border_bottom" align="center">
					<a href="/transportation/delete_campaign.jsp?cid=<%= model.getCampaignId()%>">delete</a>
				</td>
			</tr>			
			<% } %>
			</table>
			</div>
		</table>
	</td></tr>
	<tr><td><div id="campaign_content"></div></td></tr>
</table>
			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>
