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
			<crm:VSStatusLog id="lateIssues" result="result">

<script type="text/javascript" language="javascript" src="/assets/javascript/scriptaculous.js"></script>
<script type="text/javascript" language="javascript" src="/ccassets/javascript/HelpBalloon.js"></script>
<link rel="stylesheet" type="text/css" href="/ccassets/css/helpBaloonstyle.css" />
<script type="text/javascript">
		<!--
		//
		// Override the default settings to point to the parent directory
		//
		HelpBalloon.Options.prototype = Object.extend(HelpBalloon.Options.prototype, {
			button: '/images/button.png',
			balloonPrefix: '/images/balloon-'
		});
		
		function openwindow(win1, id1) {
			if(win1 == "details") {
				//display details window
				window.open('/transportation/vsDetails.jsp?id='+id1,'','height=300,width=700,menubar=no,status=no,titlebar=no,toolbar=no,scrollbars=yes');
			} else {
				//display redial window
				window.open('/transportation/vsRedial.jsp?id='+id1,'','height=300,width=700,menubar=no,status=no,titlebar=no,toolbar=no,scrollbars=yes');
			}
		}
		
		//-->
		</script>


<table width="100%" cellpadding="0" cellspacing="0" border="0" style="padding:5px;">
<tr class="list_header" style="background: #996666;">
						<td width="5%" align="center" class="list_header_text"><b>Route#</b></td>
						<td width="5%" align="center" class="list_header_text"><b>Campaign</b></td>
						<td width="5%" align="center" class="list_header_text"><b>Reason</b></td>
						<td width="3%" align="center" class="list_header_text"><b>Redial</b></td>
						<td width="5%" align="center" class="list_header_text"><b>Route <br/> Stop</b></td>
						<td width="8%" align="center" class="list_header_text"><b>VoiceShot <br/> Created By</b></td>
						<td width="10%" align="center" class="list_header_text"><b>VoiceShot <br/> Created Date</b></td>
						<td width="10%" align="center" class="list_header_text"><b>Sound File</b></td>
						<td width="8%" align="center" class="list_header_text"><b>Start Time - <br/> End Time</b></td>
						<td width="5%" align="center" class="list_header_text"><b>Scheduled <br/> Calls</b></td>
						<td width="15%" align="center" class="list_header_text"><b>Delivered <br/> Calls</b></td>
						<td width="5%" align="center" class="list_header_text"><b>UnDelivered <br/> Calls</b></td>
						<td width="10%" align="center" class="list_header_text"><b>&nbsp;</b></td>
						<td width="14%" align="center" class="list_header_text"><b>&nbsp;</b></td>
						<td width="14%" align="center" class="list_header_text"><b>&nbsp;</b></td>
					 </tr>

<%
	List<CrmVSCampaignModel> campaigns = CallCenterServices.getVoiceShotLog();
		if (campaigns.size() > 0) { %>
		<%
		for(int i=0;i<campaigns.size();i++) {
			CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
		%>
			<tr id="result" class="list_content" style="position:static;">
				<td width="5%" class="border_bottom" align="center"><%= model.getRoute() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getCampaignName() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getReasonId() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getStopSequence() %></td>
				<td width="3%" class="border_bottom" align="center"><%= "Y".equals(model.getRedial())?"Yes":"No" %></td>
				<td width="8%" class="border_bottom" align="center"><%= model.getAddByUser() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getAddByDate() %></td>
				<td width="10%" class="border_bottom" align="center"><%= model.getSoundfileName() %></td>
				<td width="8%" class="border_bottom" align="center"><%= model.getStartTime() %> - <%= model.getEndTime() %></td>
				<td width="5%" class="border_bottom" align="center"><%= model.getScheduledCalls() %></td>
				<td width="15%" class="border_bottom" align="center"><%= model.getDeliveredCallsLive() %> live | <%= model.getDeliveredCallsAM() %> answering machine</td>
				<td width="5%" class="border_bottom" align="center"><%= model.getUndeliveredCalls() %></td>
				<td width="10" class="border_bottom" align="center"><a href="#" id="mynewanchor<%=i%>" onclick="return false;">Sound file message</a></td>
					<script type="text/javascript">  
						new HelpBalloon({ 
							title: 'Sound File for Campaign:<%=model.getCampaignName()%>', 
							content: '<%=model.getSoundFileText()%>', 
							icon: $('mynewanchor<%=i%>'),
							balloonDimensions: [550,200] 							
							}); 
					</script>
				
				<td width="14%" class="border_bottom" align="center">
					<% if(model.isUpdatable()) {%> 
						<a href="javascript:openwindow('details','<%= model.getVsDetailsID()%>')">details</a> 
					<% } else { %> 
						details 
					<%  } %>
				</td>
				<td width="14%" class="border_bottom" align="center">
					<% if(model.isUpdatable()) { %>
						<a href="javascript:openwindow('redial','<%=model.getVsDetailsID()%>')">redial</a>
					<% } else { %>
						redial 
					<% } %>
				</td>
			</tr>
			<script language="Javascript">
				camps[<%=i%>] = new Campaign("<%=model.getVsDetailsID()%>","<%=model.getSoundfileName()%>","<%=model.getSoundFileText()%>");
			</script>
		<%
		}
		%>
		
<%		
	} else { %>
		<div class="content_fixed" align="center"><br>&nbsp;<b>No VoiceShot log at this time.</b><br><br></div>
<%	}     %>


</table>
			</crm:VSStatusLog>
			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>
