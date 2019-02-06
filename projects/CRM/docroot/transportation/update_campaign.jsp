<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.crm.CrmVSCampaignModel"%>
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
	String camp_name = request.getParameter("camp_name");
	String camp_id = request.getParameter("camp_id");
	String sound_file = request.getParameter("sound_file_name");
	String sound_file_text = request.getParameter("sound_file_text");		
	String camp_minutes = request.getParameter("camp_minutes");		
	String camp_name_err = "";
	String camp_id_err = "";
	String sound_file_err = "";
	String sound_file_text_err = "";
	String camp_minutes_err = "";
	boolean errors_found = false;
	boolean processed = false;
	CrmVSCampaignModel cModel = null;
	String id = request.getParameter("cid");
	if("done".equals(request.getParameter("submission"))) {
		//Handle form submission		
		if(camp_name == null || camp_name.length() == 0) {
			camp_name_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Campaign Name cannot be empty. Please enter the name as it appears in Voiceshot.com.</span>";
			errors_found = true;
		}
		if(camp_id == null || camp_id.length() == 0) {
			camp_id_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Campaign MenuID cannot be empty. Voiceshot cannot be completed without the correct MenuID. Please follow the instructions to copy and paste the Campaign MenuID. </span>";
			errors_found = true;
		}
		if(camp_minutes == null || camp_minutes.length() == 0) {
			camp_minutes_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Delay in Minutes cannot be empty. </span>";
			errors_found = true;
		}
		if(camp_minutes != null && camp_minutes.length() > 0) {
			try {
				int min = Integer.parseInt(camp_minutes);
			} catch (Exception e ) {
				camp_minutes_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Delay in Minutes should be delay time in minutes. </span>";
				errors_found = true;
			}
		}
		if(sound_file == null || sound_file.length() == 0) {
			sound_file_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Sound File Name cannot be empty. Please make sure a sound file has been attached in voiceshot.com for this campaign. Enter the sound file name. </span><br/>";
			errors_found = true;
		}
		if(sound_file_text == null || sound_file_text.length() == 0) {
			sound_file_text_err = "<span style=\"font-size:9pt;font-weight:bold;color:red;\"><br/>Sound File Text cannot be empty. Please enter the text for the sound file used in this campaign. </span><br/>";
			errors_found = true;
		}
		
		if(errors_found == false) {
			//Save the campaign data
			CrmVSCampaignModel model = new CrmVSCampaignModel();
			model.setCampaignName(camp_name);
			model.setCampaignMenuId(camp_id);
			model.setSoundfileName(sound_file);
			model.setSoundFileText(sound_file_text);
			model.setChangeByUser(CrmSession.getCurrentAgent(session).getLdapId()); 
			model.setCampaignId(request.getParameter("cid"));
			model.setDelay(Integer.parseInt(camp_minutes));
			
			processed = true;
		} else {
			cModel = new CrmVSCampaignModel();
			cModel.setCampaignName(camp_name);
			cModel.setCampaignMenuId(camp_id);
			cModel.setSoundfileName(sound_file);
			cModel.setSoundFileText(sound_file_text);		
		}
	} else {		
		camp_minutes = cModel.getDelay() + "";
	}
%>

<form method="post" name="addcampaign">
<input type="hidden" name="submission" value="done" />
<span style="padding:5px;font-size:20px;font-weight:bold;">Modify Campaign</span>
<br/>
<% if(!processed) { %>
<input type="hidden" name="cid" value="<%=request.getParameter("cid")%>" />
<table cellpadding="5" cellspacing="10" border="0" style="font-size:12pt;">
	<tr><td align="right"  valign="top">Campaign Name</td>
		<td><input name="camp_name" value="<%=cModel.getCampaignName()%>" size="60"/><%=camp_name_err%>
		<br/><span style="font-size:8pt;color:green">Voiceshot will be unable to recognize the campaign name, <br/>if the name and ID#'s are not properly entered as it appears in Voiceshot.com.</span>
		</td>
	</tr>
	<tr><td align="right" valign="top">Campaign MenuID</td>
		<td><input name="camp_id" value="<%=cModel.getCampaignMenuId()%>" size="60"/><%=camp_id_err%>
		<br/><span style="font-size:8pt;color:green;"><font style="color:red">**</font>Its very important that this Campaign ManuID matches with Campaign MenuID from Voiceshot. <br/>Login to voiceshot.com, choose the specific outbound campaign, choose "Campaign Options" in the left hand side menu. <br/>Click "Developer Options" tab and copy the "Campaign MenuID". <br/>That Campain MenuID needs to be pasted in this text box.</span></td>
	</tr>
	<tr><td align="right" valign="top">Delay in Minutes</td>
		<td><input name="camp_minutes" value="<%=camp_minutes%>" size="60"/><%=camp_minutes_err%>
		<br/><span style="font-size:8pt;color:green;"><font style="color:red">**</font>Must convert hours to minutes</span></td>
	</tr>
	<tr><td align="right" valign="top">Sound File Name</td>
		<td><input name="sound_file_name" value="<%=cModel.getSoundfileName()%>" size="60"/><%=sound_file_err%></td>
	</tr>
	<tr><td align="right" valign="top">Sound File Message</td>
		<td><textarea name="sound_file_text" rows="5" cols="80"><%=cModel.getSoundFileText()%></textarea><%=sound_file_text_err%></td>
	</tr>
	<tr><td align="right">&nbsp;</td>
		<td align="center"><a href="/transportation/campaigns.jsp" style="text-decoration:none;background-color:orange;color:white;font-weight:bold;border:1px orange solid;padding:2px;width:60px;">Cancel</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="Modify" value="Modify" style="text-decoration:none;background-color:green;color:white;font-weight:bold;border:1px green solid;padding:2px;width:60px;"/></td>
	</tr>
</table>
<% } else { %>
	<br/> <span style="font-size:12pt;font-weight:bold;padding:5px;">Campaign "<%=request.getParameter("camp_name")%>" is modified successfully. Click on Campiagns to get back to the Campaign listing page.
	<br/><br/><a href="/transportation/campaigns.jsp" style="text-decoration:none;background-color:green;color:white;font-weight:bold;border:1px green solid;padding:5px;width:60px;"/>Back to Campaigns</a>
	</span>
<% } %>
</form>			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>
