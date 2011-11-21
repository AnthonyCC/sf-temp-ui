<%@ page import="com.freshdirect.crm.CrmVSCampaignModel,com.freshdirect.customer.EnumVSReasonCodes,java.util.*,com.freshdirect.fdstore.*" %>

<form method='POST' name="timePick" id="timePick">	
	<div class="vs_container">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" class="sub_nav_text" style="padding:7px;">
			<tr>
				<td width="15%"><span class="sub_nav_title">&nbsp;</span></td>
				<td width="55%" align="left" style="background-color:#E0E0E0;">
					<table width="100%"><tr><td width="70%">
					<table width="100%"><tr>
					<td align="right">Campaign Name:</td> <td align="left"><select name="campaign_id" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;" onchange="fillSoundFile()" id="campaign_id"><option value="-1">--Select Campaign--</option>
					<%
						List<CrmVSCampaignModel> campaigns = CallCenterServices.getVSCampaignList();
						StringBuffer inputs = new StringBuffer();
						for(int i=0;i<campaigns.size();i++) {
							CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
							inputs.append("<input type=\"hidden\" name=\""+ model.getCampaignId() + "\" value=\"" + model.getCampaignName() + "\"/>");
							inputs.append("<input type=\"hidden\" name=\""+ model.getCampaignId() + "_menu_id\" value=\"" + model.getCampaignMenuId() + "\"/>");
							%>
								<option value="<%=model.getCampaignId()%>"><%=model.getCampaignName()%></option>
							<%
						}
					%>
					</select></td>
					<td align="right">Sound File: </td><td align="left"><input type="text" name="sound_file" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;" id="sound_file" readonly="readonly"/></td></tr>
					<tr><td align="right">Reason:</td> <td align="left"><select name="reason" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;width:160px;"><option value="-1">--Select Reason--</option>
					<%
						Map rCodes = EnumVSReasonCodes.getEnumMap();
						Set keys = rCodes.keySet();
						Iterator iter = keys.iterator();
						while(iter.hasNext()) {
							String key = (String) iter.next();
							EnumVSReasonCodes value = (EnumVSReasonCodes) rCodes.get(key);
							String name = value.getName();
							int val = value.getValue();
					%>
						<option value="<%=val%>"><%=name%></option>
					<% } %>
					</select> </td>
					<td align="right" colspan="2">					
					<script language="JavaScript" type="text/javascript">					
							var howMany=2;//(number of times to be picked)
							var pickerName=new Array('Start Time', 'End Time');//must contain entries as much as is value of howMany
							var hCol='red';//hour hand color
							var mCol='green';//minute hand color
							var bgCol='#ff9966';//background color
							var showMin=1;//possible values: 1,5,10,15,20,30
							var show24=0;//set to 1, if 00:00 o'clock should be displayed as 24:00

							if(document.getElementById){
								document.write(writeAll());
								document.close();
							}
							else{
								document.write('Your browser doesn\'t support getElementById. Due to that, the time picker is not displayed.<br>');
								document.close();
							}
							
					</script>
					</td></tr>
					<tr><td align="right">Sound File Message:</td><td colspan="3"> <textarea style="outline:none;width: 600px;height: 120px;border: 2px solid #cccccc;padding: 5px;" id="sound_file_text" name="sound_file_text"></textarea></td></tr></table>
					</td>
					<td width="30%" valign="bottom">
						<a href="javascript:document.timePick.submit();" style="background-color:green;text-decoration:none;color:white;padding:5px;font-weight:bold;">Start Voiceshot</a>
					</td>
					</tr></table>
				</td>
			</tr>
		</table>
	</div>
	<%= inputs.toString() %>