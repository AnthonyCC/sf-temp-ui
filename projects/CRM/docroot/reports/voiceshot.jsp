<%@ page import="com.freshdirect.crm.CrmVSCampaignModel,com.freshdirect.customer.VSReasonCodes,java.util.*,com.freshdirect.fdstore.*" %>
<%
String campid = request.getParameter("campaign_id");
String reason = request.getParameter("reason");
int reason_id = 0;
if(reason != null && !"-1".equals(reason)) {
	reason_id = Integer.parseInt(reason);
}
Calendar today = Calendar.getInstance();
int currhour; 
int currmin; 
String shour = request.getParameter("shour");
String sminutes = request.getParameter("sminutes");
String sampm = request.getParameter("sampm");
if(shour == null || "-1".equals(shour)) {
	currhour = today.get(Calendar.HOUR);
} else {
	currhour = Integer.parseInt(shour);
}
if(sminutes == null || "-1".equals(sminutes)) {
	currmin = today.get(Calendar.MINUTE);
} else {
	currmin = Integer.parseInt(sminutes);
}
if(sampm == null) {
	sampm = today.get(Calendar.AM_PM) == 0?"AM":"PM";
}
	
%>
<form method='POST' name="timePick" id="timePick">
<input type="hidden" name="manual" value="false" />	
		<table width="100%" cellpadding="2" cellspacing="0" border="0" class="sub_nav_text">
			<tr>
				<td width="15%"><span class="sub_nav_title">&nbsp;</span></td>
				<td width="85%" align="left" style="background-color:#E0E0E0;">
					<table width="100%" border="0"><tr>
					<td align="right" style="width:auto;"><font color="red">*</font>Campaign Name:</td> <td align="left"><select name="campaign_id" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;" onchange="fillSoundFile()" id="campaign_id"><option value="-1">--Select Campaign--</option>
					<%
						List<CrmVSCampaignModel> campaigns = CallCenterServices.getVSCampaignList();
						StringBuffer inputs = new StringBuffer();
						String sfile = "";
						String sfiletext = "";
						for(int i=0;i<campaigns.size();i++) {
							CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
							inputs.append("<input type=\"hidden\" name=\""+ model.getCampaignId() + "\" value=\"" + model.getCampaignName() + "\"/>");
							inputs.append("<input type=\"hidden\" name=\""+ model.getCampaignId() + "_menu_id\" value=\"" + model.getCampaignMenuId() + "\"/>");						
							inputs.append("<input type=\"hidden\" name=\"delay_"+ model.getCampaignId() + "\" value=\"" + model.getDelay() + "\"/>");							
							if(campid != null && campid.equals(model.getCampaignId())) {
								sfile = model.getSoundfileName();
								sfiletext = model.getSoundFileText();
							%>
								<option value="<%=model.getCampaignId()%>" selected><%=model.getCampaignName()%></option>
							<% } else { %>
								<option value="<%=model.getCampaignId()%>" ><%=model.getCampaignName()%></option>
							<% }
						}
					%>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					Start Time:<font color="red">*</font>&nbsp;&nbsp;
					<select name="shour">
						<option value="-1">Hour</option>
						<%for(int i=1;i<13;i++) { %>							
							<option value="<%=i%>" <%=currhour == i?"selected=\"true\"":""%>><%=i%>
						<%}%>
					</select>&nbsp;
					<select name="sminutes">
						<option value="-1">Minute</option>
						<%for(int i=0;i<61;i++) { %>
							<option value="<%=i%>" <%=currmin == i?"selected=\"true\"":""%>><%=i%>
						<%}%>
					</select>&nbsp;
					<select name="sampm">
						<option value="AM" <%="AM".equals(sampm)?"selected=\"true\"":""%>>AM</option>
						<option value="PM" <%="PM".equals(sampm)?"selected=\"true\"":""%>>PM</option>
					</select></td></tr>
					<tr><td align="right">Reason:<font color="red">*</font></td> <td align="left"><select name="reason" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;"><option value="-1">--Select Reason--</option>
					<%
						List<VSReasonCodes> rCodes = CallCenterServices.getVSReasonCodes();
						Iterator<VSReasonCodes> iter = rCodes.iterator();
						while(iter.hasNext()) {
							VSReasonCodes rCode = (VSReasonCodes) iter.next();
							String val = rCode.getReasonId();
							String name = rCode.getReason();							
							if(val.equals(reason)) {
					%>
						<option value="<%= val %>" selected><%=name%></option>
					<% } else { %>
						<option value="<%= val %>"><%=name%></option>
					<% } } %>
					</select> 
					&nbsp;&nbsp;
					<a href="javascript:document.timePick.submit();" style="background-color:green;text-decoration:none;color:white;padding:5px;font-weight:bold;">Start Voiceshot</a>
					&nbsp;
					<a href="javascript:document.timePick.manual.value=true;document.timePick.submit();" style="background-color:red;text-decoration:none;color:white;padding:5px;font-weight:bold;">Manual Call: DO NOT VOICESHOT</a>
					</td></tr>
					</table>					
				</td>
			</tr>
		</table>
	<%= inputs.toString() %>