<%@ page import='java.util.*' %>
<%@ page import='java.io.*' %>
<%@ page import='com.freshdirect.customer.EnumVSStatus' %>
<%@ page import='com.freshdirect.crm.CrmVSCampaignModel' %>
<%@ page import='com.freshdirect.fdstore.CallCenterServices' %>
<%@ page import='java.util.HashSet,java.text.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.crm.VoiceShotResponseParser' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>



<crm:GetCurrentAgent id='currAgent'>
<%
	String id1 = request.getParameter("id");
	String menuid = request.getParameter("menuid");
	VoiceShotResponseParser vsrp = null;
	if("true".equals(request.getParameter("submission"))) {
		//handle form submission
		int size = Integer.parseInt(request.getParameter("phonesize"));
		CrmVSCampaignModel cModel = new CrmVSCampaignModel();
		String id = request.getParameter("id");
		String lateId = request.getParameter("lateId");
		List<String> phonenumbers = new ArrayList<String>();
		List<String> phonenumbers1 = new ArrayList<String>();
		StringBuffer phonesb = new StringBuffer("<phonenumbers>");
		for(int i=0;i<size;i++) {
			String selValue = request.getParameter("selectphone"+i);
			if(selValue != null && selValue.length() > 0) {
				phonenumbers.add(selValue);
				phonenumbers1.add(selValue.substring(0, selValue.indexOf("|")));
				phonesb.append("<phonenumber number=\"");
				phonesb.append(selValue.substring(0, selValue.indexOf("|")));
				phonesb.append("\" />");
			}
		}
		phonesb.append("</phonenumbers>");
		cModel.setPhonenumbers(phonenumbers);
		cModel.setVsDetailsID(id);
		cModel.setLateIssueId(lateId);
		cModel.setAddByUser(CrmSession.getCurrentAgent(session).getLdapId()); 		 
		String call_id = null;		
		
		StringBuffer originalXML = new StringBuffer("<campaign menuid=\"");
		originalXML.append(menuid);
		originalXML.append("\" action=\"0\"  username=\"");
		originalXML.append(FDStoreProperties.getVSUserName());
		originalXML.append("\" password=\"");
		originalXML.append(FDStoreProperties.getVSPassword());
		originalXML.append("\" callid=\"" + call_id + "\">");
		originalXML.append(phonesb.toString());				
		originalXML.append("</campaign>");

			
		System.out.println("Voiceshot RedialXML:"+originalXML.toString());
		
		//post the calls to vs
		java.net.URL programUrl = new java.net.URL(FDStoreProperties.getVSURL());   
		java.net.HttpURLConnection connection = (java.net.HttpURLConnection)programUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setUseCaches(false); 
		connection.setRequestProperty("Content-Type", "text/xml");
		PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
		output.println(originalXML.toString());
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
		
		System.out.println("voiceshot RedialResult:"+firstresult);
			
		vsrp = new VoiceShotResponseParser(firstresult);
	}	
	String lateId = request.getParameter("lateid");
	List<CrmVSCampaignModel> calldetails = null;

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
		  <input type="hidden" name="lateId" value="<%=lateId%>" />
		  <tr><td>&nbsp;</td></tr>
		  <tr><td>
		  
		  <table width="80%" cellspacing="0" cellpadding="5" bgcolor="#ffffff" id="Table3" style="font-family:Verdana,Arial,Helvetica,sans-serif;font-size:8pt;border-width: 1px;border-style: solid;border-color:#000 ;background-color: white;">
					<tr style="font-weight:bold;background-color:#E7E7D6;"><td align="center">&nbsp;</td>
					<td align="center">Phone#</td>
					<td align="center">Order#</td>
					<td align="center">Route#</td>
					<td align="center">Stop#</td>
					</tr>
					<% for(int i=0;i<calldetails.size();i++) {
						String style = "style=\"background-color: #996666; color: black;\"";
						CrmVSCampaignModel model = (CrmVSCampaignModel) calldetails.get(i);
						if((i%2) == 0) style="";
					%>
					<tr <%=style%>>
						<td align="center"><input type="checkbox" name="selectphone<%=i%>" value="<%=model.getPhonenumber()%>|<%=model.getSaleId()%>|<%=model.getCustomerId()%>" checked></td>
						<td align="center"><%=model.getPhonenumber()%></td>
						<td align="center"><%= model.getSaleId() %></td>
						<td align="center"><%= model.getRoute() %></td>
						<td align="center"><%= model.getStopSequence() %></td>						
					</tr>				
					<% } %>
				</table>
		  
		  
		  
		  </td></tr>
		  <tr><td>&nbsp;</td></tr>

			<tr><td align="center">
			<input type="submit" value="Redial" style="background-color:#996666;border:1px solid #999999;color:#FFFFFF;font-size:8pt;padding:10px 20px 10px 20px;font-weight:bold;"/>
			</td></tr>
		<% } else { %>
			<tr><td align="left">There are no calls eligible to be redialed. To be eligible for redialing, calls must meet the following criteria <br/><br/>
				<ul>
					<li>Be less than 24 hours old.</li>
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