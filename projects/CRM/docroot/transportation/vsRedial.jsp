<%@ page import='com.freshdirect.customer.EnumVSStatus' %>
<%@ page import='com.freshdirect.crm.CrmVSCampaignModel' %>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>
<%@ page import='java.util.HashSet,java.text.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.crm.VoiceShotResponseParser' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<crm:GetCurrentAgent id='currAgent'>
<%
	String id1 = request.getParameter("id");
	VoiceShotResponseParser vsrp = null;
	if("true".equals(request.getParameter("submission"))) {
		//handle form submission
		int size = Integer.parseInt(request.getParameter("phonesize"));
		CrmVSCampaignModel cModel = new CrmVSCampaignModel();
		String id = request.getParameter("id");
		List<String> phonenumbers = new ArrayList<String>();
		List<String> phonenumbers1 = new ArrayList<String>();
		for(int i=0;i<size;i++) {
			String selValue = request.getParameter("selectphone"+i);
			if(selValue != null && selValue.length() > 0) {
				phonenumbers.add(selValue);
				phonenumbers1.add(selValue.substring(0, selValue.indexOf("|")));
			}
		}
		cModel.setPhonenumbers(phonenumbers);
		System.out.println(phonenumbers1);
		cModel.setVsDetailsID(id);
		cModel.setAddByUser(CrmSession.getCurrentAgent(session).getLdapId()); 
		String call_id = "CID_"+id1;
		CallCenterServices.saveVSRedialInfo(cModel);
		
		String phone = "<phonenumbers><phonenumber number=\"(203) 446-9229\" /><phonenumber number=\"(203) 843-0301\" /></phonenumbers>";
		StringBuffer sb = new StringBuffer("<campaign menuid=\"");
		sb.append("4766-962581812");
		sb.append("\" action=\"0\"  username=\"mtrachtenberg\" password=\"whitshell\" callid=\"");
		sb.append(call_id);
		sb.append("\" >");
		sb.append(phone);
		sb.append("</campaign>");
			
		System.out.println(sb.toString());
		
		//post the calls to vs
		java.net.URL programUrl = new java.net.URL("http://apiproxy.voiceshot.com/ivrapi.asp");   //'Do not swap these two URLs. Always post to api.voiceshot.com first.
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection)programUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false); 
		connection.setRequestProperty("Content-Type", "text/xml");
		PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
		output.println(sb.toString());
		output.close(); 
		connection.connect();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line = null;
		String firstresult = "";
		
		while ((line = br.readLine()) != null) {
			 firstresult += "\n" + line;
		}
		
		System.out.println(firstresult);
			
		vsrp = new VoiceShotResponseParser(firstresult);
		System.out.println(vsrp.getErrorCode());
	}	
	List<CrmVSCampaignModel> calldetails = CallCenterServices.getVSRedialList(id1);

	if("true".equals(request.getParameter("submission")) && vsrp != null) {
	%>	
		<span style="color:red;font-weight:bold;font-size:10pt;"/><br/><br/>&nbsp;&nbsp;Message From VoiceShot: <%= vsrp.getErrorMessage() %></span>	
	<% } else { %>
<form name="redial" method="POST">
<input type="hidden" name="submission" value="true"/>
	<table cellspacing="15" cellpadding="0" border="0" align="center" width="600" style="font-family:Verdana,Arial,Helvetica,sans-serif;font-size:9pt;border-width: 1px;border-spacing: 2px;border-style: solid;border-color:#CCCC99 #999966 #CCCC99 #CCCC99 ;border-collapse: separate;background-color: white;">
		<tbody><tr> 
            <td style="background-color:#E7E7D6;height:50px;font-weight:bold;">Redial Select Unsuccessful Calls</td>
          </tr>
		  <tr><td>&nbsp;</td></tr>
		  <tr> <td align="left">
				Numbers eligible to be redialed.
		  </td></tr>		  
		  <% if(calldetails.size() > 0) { %>
		  <input type="hidden" name="phonesize" value="<%=calldetails.size()%>" />
		  <input type="hidden" name="id" value="<%=id1%>" />
		  <tr><td>&nbsp;</td></tr>
		  <%			
			for(int i=0;i<calldetails.size();i++) {
				CrmVSCampaignModel model = (CrmVSCampaignModel) calldetails.get(i);
			%>
				<tr> <td align="left">
					<input type="checkbox" name="selectphone<%=i%>" value="<%=model.getPhonenumber()%>|<%=model.getSaleId()%>|<%=model.getCustomerId()%>" checked>&nbsp;<%=model.getPhonenumber()%>
				</td></tr>			
			<% } %>
			<tr><td align="center">
			<input type="submit" value="Redial" style="background-color:#996666;border:1px solid #999999;color:#FFFFFF;font-size:8pt;padding:10px 20px 10px 20px;font-weight:bold;"/>
			</td></tr>
		<% } else { %>
			<tr><td align="left">There are no calls eligible to be redialed. To be eligible for redialing, calls must meet the following criteria <br/><br/>
				<ul>
					<li>Be less than 15 days old.</li>
					<li>Have one of these call reason routes: Busy, No answer, Operator Intercept.</li>
					<li>Cannot already be dialing or scheduled for dialing.
				</ul>
			</td></tr>
		<% } %>
			<tr><td>&nbsp;</td></tr>
		</tbody>
	</table>
</form>
<% } %>
</crm:GetCurrentAgent>