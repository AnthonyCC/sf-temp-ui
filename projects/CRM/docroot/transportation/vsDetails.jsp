<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.customer.EnumVSStatus' %>
<%@ page import='com.freshdirect.crm.CrmVSCampaignModel' %>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>
<%@ page import='java.util.HashSet,java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<crm:GetCurrentAgent id='currAgent'>
<%	
	String id = request.getParameter("id");
	String dmsg = request.getParameter("dmsg");
	if("sfile".equals(dmsg)) {
		String message = CallCenterServices.getSoundFileMessage(id);
%>	
	<table cellspacing="15" cellpadding="0" border="0" align="center" width="600" 
		style="font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;border-width: 1px;border-spacing: 2px;border-style: solid;border-color:#CCCC99 #999966 #CCCC99 #CCCC99 ;border-collapse: separate;background-color: white;">
		<tbody><tr> 
            <td style="background-color:#E7E7D6;height:50px;font-weight:bold;">Sound File Message</td>
          </tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr> <td align="center"> <%= message %> </td> </tr>
		 </tbody>
	</table>		  
<%	} else {
		String lateId = request.getParameter("lateid");
		List<CrmVSCampaignModel> calldetails = CallCenterServices.getVoiceShotCallDetails(id, lateId);	
%>
	<table cellspacing="15" cellpadding="0" border="0" align="center" width="600" 
		style="font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;border-width: 1px;border-spacing: 2px;border-style: solid;border-color:#CCCC99 #999966 #CCCC99 #CCCC99 ;border-collapse: separate;background-color: white;">
		<tbody><tr> 
            <td style="background-color:#E7E7D6;height:50px;font-weight:bold;">Call Results</td>
          </tr>
		  <tr><td>&nbsp;</td></tr>
		  <% if(calldetails.size() > 0) { %>		
          <tr> <td align="center">
				<table cellspacing="0" cellpadding="5" bgcolor="#ffffff" id="Table3" style="font-family:Verdana,Arial,Helvetica,sans-serif;font-size:8pt;border-width: 1px;border-style: solid;border-color:#000 ;background-color: white;">
					<tr style="font-weight:bold;background-color:#E7E7D6;"><td align="center">Scheduled Calls</td>
					<td  align="center" colspan="2">Successful Calls</td>
					<td align="center">Unsuccessful Calls</td>
					<td align="center">Order#</td>
					<td align="center">Route#</td>
					<td align="center">Stop#</td>
					</tr>
					<tr  style="font-weight:bold;background-color:#E7E7D6;"><td align="center">&nbsp;</td>
					<td align="center">Live</td><td align="center">Machine</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					</tr>
					<% for(int i=0;i<calldetails.size();i++) {
						String style = "style=\"background-color: #996666; color: black;\"";
						CrmVSCampaignModel model = (CrmVSCampaignModel) calldetails.get(i);
						if((i%2) == 0) style="";
					%>
					<tr <%=style%>>
						<td align="center"><%=model.getPhonenumber()%></td>
						<td align="center"><%= model.getStatus() == EnumVSStatus.LIVE_ANS.getValue()?"&#8730;":"&nbsp;" %></td>
						<td align="center"><%= model.getStatus() == EnumVSStatus.ANS_MACHINE.getValue()?"&#8730;":"&nbsp;" %></td>
						<td align="center"><%= model.getStatus() == EnumVSStatus.UNSUCCESSFUL.getValue()?"&#8730;":"&nbsp;" %></td>
						<td align="center"><%= model.getSaleId() %></td>
						<td align="center"><%= model.getRoute() %></td>
						<td align="center"><%= model.getStopSequence() %></td>						
					</tr>				
					<% } %>
				</table>
			</td></tr>
			<% } %>
			<tr><td>&nbsp;</td></tr>
		</tbody>
	</table>
<% } %>
			
			

			</crm:GetCurrentAgent>
