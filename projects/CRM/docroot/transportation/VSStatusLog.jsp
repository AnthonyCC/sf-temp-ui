<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import='com.freshdirect.customer.ErpTruckInfo' %>
<%@ page import='com.freshdirect.crm.CrmVSCampaignModel' %>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>
<%@ page import='java.util.HashSet,java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Voiceshot log</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
    		<crm:GetCurrentAgent id='currAgent'>

<script type="text/javascript">		
		
		function openwindow(win1, id1) {
			if(win1 == "details") {
				//display details window
				window.open('/transportation/vsDetails.jsp?id='+id1,'','height=300,width=700,menubar=no,status=no,titlebar=no,toolbar=no,scrollbars=yes');
			} else {
				//display redial window
				window.open('/transportation/vsRedial.jsp?id='+id1,'','height=300,width=700,menubar=no,status=no,titlebar=no,toolbar=no,scrollbars=yes');
			} 
		}
		
</script>


<table width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:5px;">
<tr class="list_header" style="background: #996666;">
						<td width="5%" align="center" class="list_header_text"><b>Route#</b></td>
						<td width="5%" align="center" class="list_header_text"><b>Campaign</b></td>
						<td width="10%" align="center" class="list_header_text"><b>Reason</b></td>
						<td width="10%" align="center" class="list_header_text"><b>Route <br/> Stop</b></td>
						<td width="3%" align="center" class="list_header_text"><b>Redial</b></td>						
						<td width="8%" align="center" class="list_header_text"><b>VoiceShot <br/> Created By</b></td>
						<td width="10%" align="center" class="list_header_text"><b>VoiceShot <br/> Created Date</b></td>
						<td width="4%" align="center" class="list_header_text"><b>VoiceShot/ <br/> Manual</b></td>
						<td width="6%" align="center" class="list_header_text"><b>Start Time </b></td>
						<td width="5%" align="center" class="list_header_text"><b>Scheduled <br/> Calls</b></td>
						<td width="15%" align="center" class="list_header_text"><b>Delivered <br/> Calls</b></td>
						<td width="5%" align="center" class="list_header_text"><b>UnDelivered <br/> Calls</b></td>
						<td width="10%" align="center" class="list_header_text"><b>&nbsp;</b></td>
						<td width="14%" align="center" class="list_header_text"><b>&nbsp;</b></td>
						<td width="14%" align="center" class="list_header_text"><b>&nbsp;</b></td>
					 </tr>

<%
	Calendar cal = Calendar.getInstance();  
	cal.add(Calendar.DAY_OF_MONTH, -15);
	List<CrmVSCampaignModel> campaigns = CallCenterServices.getVoiceShotLog(cal.getTime());
		if (campaigns.size() > 0) { %>
		<%
		for(int i=0;i<campaigns.size();i++) {
			CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
		%>
			<tr id="result" class="list_content" style="position:static;">
				<td width="5%" class="border_bottom" align="center"><%= model.getRoute() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getCampaignName() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getReasonId() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getStopSequence() %></td>
				<td width="3%" class="border_bottom" align="center"><%= "Y".equals(model.getRedial())?"Yes":"" %></td>
				<td width="8%" class="border_bottom" align="center"><%= model.getAddByUser() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getAddByDate() %></td>
				<td width="4%" class="border_bottom" align="center"><%= model.getManual()?"Manual":"Voiceshot" %></td>
				<td width="6%" class="border_bottom" align="center"><%= model.getStartTime() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getScheduledCalls() %></td>
				<td width="15%" class="border_bottom" align="center"><%= model.getManual()?"&nbsp;":model.getDeliveredCallsLive()+" live | "+model.getDeliveredCallsAM()+" answering machine" %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getManual()?"&nbsp;":model.getUndeliveredCalls() %></td>
				<td width="10" class="border_bottom" align="center">
				<% if(!model.getManual()) { %>
					<a href="javascript:openwindow('details','<%= model.getCampaignId()%>&dmsg=sfile')" id="mynewanchor<%=i%>">Sound file message</a>
				<% } else { %>
					&nbsp;
				<% } %>
				</td>
				<td width="14%" class="border_bottom" align="center">					
					<% if(!model.getManual()) {
						if(model.isUpdatable()) {%> 
						<a href="javascript:openwindow('details','<%= model.getVsDetailsID()%>&lateid=<%=model.getLateIssueId() %>')">details</a> 
					<% } else { %> 
						details 
					<%  } } else { %>
						&nbsp;
					<% } %>
				</td>
				<td width="14%" class="border_bottom" align="center">
					<% if(!model.getManual()) {
						if(model.isUpdatable()) { %>
						<a href="javascript:openwindow('redial','<%=model.getVsDetailsID()%>&menuid=<%=model.getCampaignMenuId()%>&lateid=<%=model.getLateIssueId() %>')">redial</a>
					<% } else { %>
						redial 
					<% } } else { %>
						&nbsp;
					<% } %>
				</td>
			</tr>			
		<%
		}
		%>
		
<%		
	} else { %>
		<div class="content_fixed" align="center"><br>&nbsp;<b>No VoiceShot log at this time.</b><br><br></div>
<%	}     %>


</table>
			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>
