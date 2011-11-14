<%@ taglib uri='template' prefix='tmpl' %><%@ taglib uri='logic' prefix='logic' %><%@ taglib uri='freshdirect' prefix='fd' %><%

    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
    String wave = request.getParameter("wave") != null ? request.getParameter("wave") : " ";
    String route = request.getParameter("route") != null ? request.getParameter("route") : " ";
    String stop1 = request.getParameter("stop1") != null ? request.getParameter("stop1") : " ";
    String stop2= request.getParameter("stop2") != null ? request.getParameter("stop2") : " ";
    
    List routeStopLines = null;
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Date dateParam = null;
    Date dateParam2 = null;
    
    if ("POST".equals(request.getMethod())) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            dateParam = cal.getTime();
         
            routeStopLines = CallCenterServices.getRouteStopReport(dateParam, wave, route, stop1, stop2);
    }
if ("POST".equals(request.getMethod()) && "yes".equalsIgnoreCase(request.getParameter("xPortVS")) && routeStopLines.size() > 0) { 
            response.addHeader ("Content-Disposition","attachment;filename=route_stop.txt");
            response.setContentType("application/Text"); 
            PrintWriter pw = response.getWriter();
			try {
				pw.write("Name,Number\r\n");
				for(Iterator itr = routeStopLines.iterator();itr.hasNext();) {
						RouteStopReportLine routeStopLine  = (RouteStopReportLine)itr.next();
						String unFmtPhone = JspMethods.removeChars(routeStopLine.getPhoneNumber(),"() -");
						if (unFmtPhone==null || unFmtPhone.length() <10) unFmtPhone="";
						pw.write(routeStopLine.getFirstName()+" "+routeStopLine.getLastName()+",");
						pw.write(unFmtPhone+"\r\n");
				}
			} finally {
				pw.flush();
				pw.close();
			}

} else {
	List<CrmVSCampaignModel> campaigns = CallCenterServices.getVSCampaignList();
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Orders by Route & Stop</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/transportation_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">

<form name="routestop_report" method="post">
<input type="hidden" name="startTime" id="startTime" value="">
<input type="hidden" name="endTime" id="endTime" value="">
 <input type="hidden" name="xPortVS" value='no'>
<script language="Javascript">
	function Campaign(campaignId, soundFile, soundFileText) {
		this.campaignId = campaignId;
		this.soundFile = soundFile;
		this.soundFileText = soundFileText;
	}
	
	var camps = new Array();
	
	<%
		for(int i=0;i<campaigns.size();i++) {
			CrmVSCampaignModel model = (CrmVSCampaignModel) campaigns.get(i);
			%>
				camps[<%=i%>] = new Campaign("<%=model.getCampaignId()%>","<%=model.getSoundfileName()%>","<%=model.getSoundFileText()%>");
			<%
		}
	%>
	
	checked = false;
    function checkedAll () {
		if (checked == false){checked = true}else{checked = false}
		for (var i = 0; i < document.getElementById('timePick').elements.length; i++) {
			document.getElementById('timePick').elements[i].checked = checked;
		}
    }
	
</script>

<script language="JavaScript">
    function checkForm(thisForm) {
        var okToSubmit= true;
        var day = thisForm.day.value;
        var month = thisForm.month.value;
        var year = thisForm.year.value;
        var wave = thisForm.wave.value;
        var route = thisForm.route.value;
        var stop1 = thisForm.stop1.value;
        var stop2 = thisForm.stop2.value;
        document.routestop_report.xPortVS.value='no';
        
        
        if (day.length<2) day="0"+day;
        if (month.length<2) month="0"+month;
        var date1=year+month+day;
        
        if (date1.length!=8 ) {
            alert('The DATE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if(isNaN(wave)) {
            alert('The WAVE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }

        if(isNaN(route)) {
            alert('The ROUTE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if(isNaN(stop1) || isNaN(stop2)) {
            alert('The STOP field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if ((route.length==0 || route==null) && ((stop1.length>0 && stop1!=null) || (stop2.length>0 && stop2!=null))) {
            alert('ROUTE field is required with STOP. Please correct and try again.');
            okToSubmit = false;
        }
        
        if (!isNaN(stop1) && !isNaN(stop2)) {
            if (parseFloat(stop1) > parseFloat(stop2)) {
                alert('Minimum range of STOP field cannot be larger than maximum range. Please correct and try again.');
                okToSubmit = false;
            }
        }
        
        if (okToSubmit) {
            thisForm.submit();
        }
    }

    function setXportVSOn() {
        document.routestop_report.xPortVS.value='yes';
        document.routestop_report.submit();
    }
	
	function fillSoundFile() {
		//alert("here");
		var element_value = document.getElementById("campaign_id").value;
		//alert("element_value=" + element_value);
		if(element_value != "-1") {
			for(i=0;i<camps.length;i++) {
				if(camps[i].campaignId == element_value) {
					document.getElementById("sound_file").value = camps[i].soundFile;
					document.getElementById("sound_file_text").value = camps[i].soundFileText;
					break;
				}
			}
		}
	}
	
	
</script>
    <tr>
        <td width="15%"><span class="sub_nav_title">Orders by Route & Stop <% if (routeStopLines!= null && routeStopLines.size()>0) {%>( <span class="result"><%= routeStopLines.size() %></span> ) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></span></td>
        <td width="55%" align="center">
            Date* 
            <select name="month" required="true" class="pulldown">
                <option value="">Month</option>
                            <%  for (int i=0; i<12; i++) {  %>
                            <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                            <%  }   %>
            </select>
            <select name="day" required="true" class="pulldown">
                <option value="">Day</option>
                            <%  for (int i=1; i<=31; i++) { %>
                <option value="<%= i %>" <%= (i==day)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>
            <select name="year" required="true" class="pulldown">
                <option value="">Year</option>
                            <%  for (int i=2005; i<2016; i++) { %>
                <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>
            &nbsp;
            Wave <input type="text" name="wave" size="6" maxlength="6" class="text" value="<%=request.getParameter("wave")%>">
            &nbsp;
            Route <input type="text" name="route" size="6" maxlength="6" class="text" value="<%=request.getParameter("route")%>">
            &nbsp;
            Stop <input type="text" name="stop1" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop1")%>"> to <input type="text" name="stop2" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop2")%>">

            <input type="submit" class="submit" onClick="javascript:checkForm(routestop_report); return false;" value="GO"> 
        </td>
        <td width="30%" align="left"><a href="javascript:setXportVSOn();">Manually Exp. to VoiceShot fmt</a> | <a href="/reports/reports_index.jsp">All Reports >></a></td>
    </tr>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.routestop_report.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>
    </form>
</table>
</div>
<%
  VoiceShotResponseParser vsrp = null;
  if ("POST".equals(request.getMethod())) {
    if (routeStopLines.size() > 0) { 
		if("true".equals(request.getParameter("vssubmit"))) {
			String campaignID = request.getParameter("campaign_id");
			String campaignMenuID = request.getParameter(campaignID + "_menu_id");
			String sFile = request.getParameter("sound_file");
			String sText = request.getParameter("sound_file_text");
			String start_time = request.getParameter("picked0");
			String end_time = request.getParameter("picked1");
			String reason = request.getParameter("reason");
			String _route = request.getParameter("route");
			String _stop1 = request.getParameter("stop1");
			String _stop2 = request.getParameter("stop2");
			String stop_seq = "";
			if(_stop1 != null && _stop1.length() > 0) {
				stop_seq = _stop1 + ", 5, 0";
			} 
			if(_stop2 != null && _stop2.length() > 0) {
				stop_seq = stop_seq + " - " + _stop2 + ", 5, 0";
			}
			
			//Save the campaign info
			CrmVSCampaignModel cModel = new CrmVSCampaignModel();
			cModel.setCampaignId(campaignID);
			cModel.setSoundfileName(sFile);
			cModel.setSoundFileText(sText);
			cModel.setStartTime(start_time);
			cModel.setEndTime(end_time);
			cModel.setReasonId(reason);
			cModel.setRoute(_route);
			cModel.setStopSequence(stop_seq);			
			cModel.setAddByUser(CrmSession.getCurrentAgent(session).getLdapId()); 
			cModel.setCampaignName(request.getParameter(campaignID));
			
			DateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
			String formatter_start_date = formatter2.format(new Date());
			formatter_start_date = formatter_start_date + " " + start_time;
			
			System.out.println("*********formatter_start_date********" + formatter_start_date);
			
			List<String> phonenumbers = new ArrayList<String>();			
			StringBuffer phonesb = new StringBuffer("<phonenumbers>");
			for(int i=0;i<routeStopLines.size();i++) {
				String selValue = request.getParameter("selectphone"+i);
				if(selValue != null && selValue.length() > 0)  {
					phonenumbers.add(selValue);
					phonesb.append("<phonenumber number=\"");
					phonesb.append(selValue.substring(0, selValue.indexOf("|")));
					phonesb.append("\" dateandtime=\"");
					phonesb.append(formatter_start_date);
					phonesb.append("\" />");
				}
			}
			phonesb.append("</phonenumbers>");
			cModel.setPhonenumbers(phonenumbers);
			String call_id = CallCenterServices.saveVSCampaignInfo(cModel);
			
			System.out.println("**********phones*********"+ phonesb.toString());
			
			StringBuffer originalXML = new StringBuffer("<campaign menuid=\"");
			originalXML.append(campaignMenuID);
			originalXML.append("\" action=\"0\"  username=\"mtrachtenberg\" password=\"whitshell\" callid=\"" + call_id + "\">");
			//originalXML.append(phonesb.toString());
			originalXML.append("<phonenumbers><phonenumber number=\"(203) 446-9229\" dateandtime=\""+ formatter_start_date + "\"/><phonenumber number=\"(203) 843-0301\"  dateandtime=\""+ formatter_start_date + "\"/></phonenumbers>");
			originalXML.append("</campaign>");

			System.out.println("OriginalXML:" + originalXML.toString());
			
			//start voiceshot
			//String phone = "<phonenumbers><phonenumber number=\"(203) 446-9229\" /><phonenumber number=\"(203) 843-0301\" /></phonenumbers>";
			//StringBuffer sb = new StringBuffer("<campaign menuid=\"");
			//sb.append("4766-962581812");
			//sb.append("\" action=\"0\"  username=\"mtrachtenberg\" password=\"whitshell\" callid=\"" + call_id + "\" >");
			//sb.append(phone);
			//sb.append("</campaign>");
			
			//System.out.println(sb.toString());
			
			
			java.net.URL programUrl = new java.net.URL("http://apiproxy.voiceshot.com/ivrapi.asp");   //'Do not swap these two URLs. Always post to api.voiceshot.com first.
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
			
			System.out.println(firstresult);
			
			vsrp = new VoiceShotResponseParser(firstresult);
			System.out.println(vsrp.getErrorCode());
			
		}
	
	if("true".equals(request.getParameter("vssubmit")) && vsrp != null) {
	%>	
		<span style="color:red;font-weight:bold;font-size:10pt;"/><br/><br/>&nbsp;&nbsp;Message From VoiceShot: <%= vsrp.getErrorMessage() %></span>	
	<% } else { 
		
	%>
	
	<!-----new Voiceshot box-------->
	<form method='POST' name="timePick" id="timePick">
	<input type="hidden" name="vssubmit" value="true"/>
	<input type="hidden" name="month" value="<%=month%>" />
    <input type="hidden" name="day" value="<%=day%>" />
    <input type="hidden" name="year" value="<%=year%>" />
    <input type="hidden" name="wave" value="<%=wave%>" />
    <input type="hidden" name="route" value="<%=route%>" />
    <input type="hidden" name="stop1" value="<%=stop1%>" />
	<input type="hidden" name="stop2" value="<%=stop2%>" />
	<div class="vs_container">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" class="sub_nav_text" style="padding:7px;">
			<tr>
				<td width="15%"><span class="sub_nav_title">&nbsp;</span></td>
				<td width="55%" align="left" style="background-color:#E0E0E0;">
					<table width="100%"><tr><td width="70%">
					<table width="100%"><tr>
					<td align="right">Campaign Name:</td> <td align="left"><select name="campaign_id" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;" onchange="fillSoundFile()" id="campaign_id"><option value="-1">--Select Campaign--</option>
					<%
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
					<tr><td align="right">Reason:</td> <td align="left"><select name="reason" style="background-color:#E0E0E0;border:1px solid #585858;padding:3px;"><option value="-1">--Select Reason--</option>
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
	
	<div class="list_header">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
		<tr>
			<td width="10%">&nbsp;Order #</td>
			<td width="25%">Customer Name</td>
			<td width="10%">Dlv Phone #</td>
			<td width="25%">Email</td>
			<td width="10%">Wave</td>
			<td width="10%">Route</td>
			<td width="10%">Stop</td>
			<td width="10%"><a href="#" onclick="checkedAll()" style="text-decoration:none;color:#ffffff;font-size:10pt;font-weight:bold;">Select All</a></td>
			<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
		</tr>
	</table>
	</div>
	<div class="list_content" id="result">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
	<logic:iterate id="routeStopLine" collection="<%= routeStopLines %>" type="com.freshdirect.fdstore.customer.RouteStopReportLine" indexId="counter">
			<tr>
				<td width="10%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=routeStopLine.getOrderNumber()%>"><%=routeStopLine.getOrderNumber()%></a>&nbsp;</td>
				<td width="25%" class="border_bottom"><%=routeStopLine.getFirstName()%> <%=routeStopLine.getLastName()%>&nbsp;</td>
				<td width="10%" class="border_bottom"><%=routeStopLine.getPhoneNumber()%>&nbsp;</td>
				<td width="25%" class="border_bottom"><%=routeStopLine.getEmail()%>&nbsp;</td>
				<td width="10%" class="border_bottom"><%=routeStopLine.getWaveNumber()!=null? routeStopLine.getWaveNumber():"&nbsp;"%></td>
				<td width="10%" class="border_bottom"><%=routeStopLine.getTruckNumber()!=null? routeStopLine.getTruckNumber():"&nbsp;"%></td>
				<td width="10%" class="border_bottom"><%=routeStopLine.getStopSequence()!=null ? routeStopLine.getStopSequence() : "&nbsp;"%></td>
				<td width="10%" class="border_bottom"><input type="checkbox" value="<%=routeStopLine.getPhoneNumber()%>|<%=routeStopLine.getOrderNumber()%>|<%=routeStopLine.getCustomerId()%>" name="selectphone<%=counter%>" checked/></td>
			</tr>
		</logic:iterate>
		</table>
	</div>
	</form>
<%      } } else { %>
    <div class="content_fixed" align="center"><br><b>No Deliveries for <%= CCFormatter.formatDate(dateParam) %></b><br><br></div>
<%      }   %>
<%  }  %>
</tmpl:put>

</tmpl:insert>

<% } 
//*** IN order to supress the extra blank lines in the export, we had to move these page import down, which is OK as far as the JSP compiler is concerned
%><%@ page import="java.util.*,java.text.*,java.io.*" %><%@ page import="com.freshdirect.fdstore.customer.*,com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*,com.freshdirect.crm.CrmVSCampaignModel,com.freshdirect.webapp.taglib.crm.CrmSession,com.freshdirect.crm.VoiceShotResponseParser,com.freshdirect.customer.EnumVSReasonCodes,java.util.*" %>
<%! DateFormatSymbols symbols = new DateFormatSymbols(); %>

