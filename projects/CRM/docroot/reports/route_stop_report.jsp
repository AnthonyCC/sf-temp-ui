<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="/includes/i_globalcontext.jspf" %>
<%

    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
   // String wave = request.getParameter("wave") != null ? request.getParameter("wave") : " ";
    String route = request.getParameter("route") != null ? request.getParameter("route") : null;
    String stop1 = request.getParameter("stop1") != null ? request.getParameter("stop1") : " ";
    String stop2= request.getParameter("stop2") != null ? request.getParameter("stop2") : " ";
	String vs_format = request.getParameter("vs_format");
    
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
			
			boolean route_error = false;
			
			String route_date1 = (month+1) + "/" + day + "/" + year;
			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
			Date user_date1 = format1.parse(route_date1);
			
			if(route != null) {
				//routeStopLines = CallCenterServices.getRouteStopReport(user_date1, wave, route, stop1, stop2, vs_format, globalContextStore, globalContextFacility);
				  routeStopLines = CallCenterServices.getRouteStopReport(user_date1, route, stop1, stop2, vs_format, globalContextStore, globalContextFacility);
			} else {
				route_error = true;
			}
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
	List<CrmVSCampaignModel> campaigns = null;
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Orders by Route & Stop</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/transportation_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">

<form name="routestop_report" method="post">
 <input type="hidden" name="xPortVS" value='no'>

<script language="JavaScript">
	checked = false;
    function checkedAll () {
		if (checked == false){checked = true}else{checked = false}
		for (var i = 0; i < document.getElementById('timePick').elements.length; i++) {
			document.getElementById('timePick').elements[i].checked = checked;
		}
    }
	
    function checkForm(thisForm) {
        var okToSubmit= true;
        var day = thisForm.day.value;
        var month = thisForm.month.value;
        var year = thisForm.year.value;
      <!--  var wave = thisForm.wave.value; -->
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
		/* 
		if(isNaN(wave)) {
            alert('The WAVE field is invalid. Please correct and try again.');
            okToSubmit = false;
        } */

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
	
	var monthtext=['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];

    function populatedropdown(dayfield1, monthfield1, yearfield1){
		var today=new Date();
        var dayfield=document.getElementById(dayfield1);
		var monthfield=document.getElementById(monthfield1);
		var yearfield=document.getElementById(yearfield1);
		for (var i=0; i<31; i++) {
			var aday = <%= day %>
			if(aday != (i+1)) {
				dayfield.options[i]=new Option(i+1, i+1);                                      
			}
		}

		var activeday1 = <%= day %>;
		dayfield.options[activeday1 - 1]=new Option(activeday1, activeday1, true, true); //select today's day

		for (var m=0; m<12; m++)
			monthfield.options[m]=new Option(monthtext[m], m+1);
		var activemonth1 = <%= month %>;
		monthfield.options[activemonth1]=new Option(monthtext[activemonth1], activemonth1, true, true); //select today's month

		var thisyear=2005;

        for (var y=0; y<15; y++){			
			var cyear = <%= year %>;
			if(cyear == thisyear)
				yearfield.options[y]=new Option(thisyear, thisyear, true, true);			
			else
				yearfield.options[y]=new Option(thisyear, thisyear);			
			thisyear+=1;
		}

	}

	function daysInMonth(month, year) {
		return new Date(year, month, 0).getDate();
	}

	function changedays(sopt, dayfield) {
		var date = new Date();
		var value = sopt.options[sopt.selectedIndex].value;
		var totaldays = daysInMonth(value, date.getFullYear());
		var dayfield=document.getElementById(dayfield);
		dayfield.options.length = 0;
		for (var i=0; i<totaldays; i++)
			dayfield.options[i]=new Option(i+1, i+1);
	}
	
	window.onload = function() {  
      populatedropdown('daydropdown','monthdropdown','yeardropdown');
    };  

</script>
	
	
</script>
    <tr>
        <td width="15%"><span class="sub_nav_title">Orders by Route & Stop <% if (routeStopLines!= null && routeStopLines.size()>0) {%>( <span class="result"><%= routeStopLines.size() %></span> ) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></span></td>
        <td width="55%" align="center" colspan="2">
            Date<font color="red">*</font>			
			<select id="monthdropdown" name="month" onchange="changedays(this,'daydropdown')">
			</select> 
			<select id="daydropdown" name="day">
			</select> 
			<select id="yeardropdown" name="year">
			</select> 
            &nbsp;
          <!--  <% if ((globalContextStore).equals("FDX")) { %><span style="display:none;"><% } %>
            Wave <input type="text" name="wave" size="6" maxlength="6" class="text" value="<%=request.getParameter("wave")%>"> -->
            <% if ((globalContextStore).equals("FDX")) { %></span><% } %>
            &nbsp;
            <%= ((globalContextStore).equals("FDX")) ? "Trip" : "Route" %>
             <input type="text" name="route" size="6" maxlength="6" class="text" value="<%=request.getParameter("route")%>">
            &nbsp;
            Stop <input type="text" name="stop1" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop1")%>"> to <input type="text" name="stop2" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop2")%>">
			&nbsp;
			Voiceshot Format <select name="vs_format" id="vs_format">
			<option value="REG" <%= !("SMS".equals(vs_format))?"selected=\"yes\"":""%>>Voiceshot with Sound File</option>
			<option value="SMS" <%= "SMS".equals(vs_format)?"selected=\"yes\"":""%>>Voiceshot with SMS Message</option>
			</select>

            <input type="submit" class="submit" onClick="javascript:checkForm(routestop_report); return false;" value="GO"> 			
        </td>
	</tr>
	<tr>
        <td colspan="3" width="30%" align="right"><a href="javascript:setXportVSOn();">Manually Exp. to VoiceShot fmt</a> | <a href="/reports/reports_index.jsp">All Reports >></a></td>
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
  boolean errors = false;
  String errMsg = "Enter required fields - ";
  if ("POST".equals(request.getMethod())) {
    if (routeStopLines.size() > 0) { 
		
		if("true".equals(request.getParameter("vssubmit"))) {			
			String campaignID = request.getParameter("campaign_id");
			String campaignMenuID = request.getParameter(campaignID + "_menu_id");
			String sFile = request.getParameter("sound_file");
			String sText = request.getParameter("sound_file_text");
			String shour = request.getParameter("shour");
			String sminutes = request.getParameter("sminutes");
			String sampm = request.getParameter("sampm");
			String reason = request.getParameter("reason");
			String _route = request.getParameter("route");
			String _stop1 = request.getParameter("stop1");
			String _stop2 = request.getParameter("stop2");
			String stop_seq = "";
			if(_stop1 != null && _stop1.length() > 0) {
				stop_seq = _stop1;
			} 
			if(_stop2 != null && _stop2.length() > 0) {
				stop_seq = stop_seq + " - " + _stop2;
			}
			
			if(campaignID == null  || campaignID.length() == 0 || "-1".equals(campaignID)) {
				errMsg += "Campaign";
				errors = true;
			}
			
			if("-1".equals(reason)) {
				errMsg += ", Reason";
				errors = true;
			}
			
			if("-1".equals(shour)) {
				errMsg += ", Start time:Hours";
				errors = true;
			}
			
			if("-1".equals(sminutes)) {
				errMsg += ", Start time:Minutes";
				errors = true;
			}
			
			String start_time = shour + ":" + sminutes + " " + sampm;	
			DateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
			String formatter_start_date = formatter2.format(new Date());
			formatter_start_date = formatter_start_date + " " + start_time;
			
			List<String> phonenumbers = new ArrayList<String>();			
			StringBuffer phonesb = new StringBuffer("<phonenumbers>");
			Hashtable<String, List> vroutes = new Hashtable<String, List>();
			for(int i=0;i<routeStopLines.size();i++) {
				String selValue = request.getParameter("selectphone"+i);
				if(selValue != null && selValue.length() > 0)  {
					phonenumbers.add(selValue);
					//System.out.println(selValue);
					phonesb.append("<phonenumber number=\"");
					String phno = selValue.substring(0, selValue.indexOf("|"));
					if(phno == null  ||phno.length() == 0) {
						errors = true;
						errMsg = ", One of the users selected in the list has no phone number. Please select users with phone numbers to voiceshot and submit again.";
						break;
					}
					phonesb.append(phno);
					phonesb.append("\" dateandtime=\"");
					phonesb.append(formatter_start_date);
					phonesb.append("\" />");
						
					String route_1 = selValue.substring(selValue.lastIndexOf("|") + 1);
					if(vroutes.containsKey(route_1)) {
						List phones = (List) vroutes.get(route_1);
						phones.add(selValue);
					} else {
						List<String> phones = new ArrayList<String>();
						phones.add(selValue);
						vroutes.put(route_1, phones);
					}
				}
			}
			
			if(!errors) {	
				
				//Save the campaign info
				CrmVSCampaignModel cModel = new CrmVSCampaignModel();
				cModel.setCampaignId(campaignID);
				cModel.setStartTime(start_time);
				cModel.setReasonId(reason);
				//cModel.setRoute(_route);
				cModel.setStopSequence(stop_seq);			
				cModel.setAddByUser(CrmSession.getCurrentAgent(session).getLdapId()); 
				cModel.setCampaignName(request.getParameter(campaignID));
				cModel.setDelayMinutes(request.getParameter("delay_"+campaignID));
				
				phonesb.append("</phonenumbers>");								
				cModel.setPhonenumbers(phonenumbers);
				cModel.setRouteList(vroutes);
				cModel.setManual("true".equals(request.getParameter("manual"))?true:false);
				String call_id = null;
				
				StringBuffer originalXML = new StringBuffer("<campaign menuid=\"");
				originalXML.append(campaignMenuID);
				originalXML.append("\" action=\"0\"  username=\"");
				originalXML.append(FDStoreProperties.getVSUserName());
				originalXML.append("\" password=\"");
				originalXML.append(FDStoreProperties.getVSPassword());
				originalXML.append("\" callid=\"" + call_id + "\">");
				originalXML.append(phonesb.toString());				
				originalXML.append("</campaign>");
				
				if("false".equals(request.getParameter("manual"))) {
					System.out.println("VoiceshotXML:" + originalXML.toString());
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
					
					System.out.println("VoiceshotResult:"+firstresult);
					
					vsrp = new VoiceShotResponseParser(firstresult);
				} else {
					vsrp = new VoiceShotResponseParser("<?xml version=\"1.0\"?><campaign errorid=\"0\" comment=\"ok\" />");
				}
			}
		}
	
	if("true".equals(request.getParameter("vssubmit")) && vsrp != null) {
		if("false".equals(request.getParameter("manual"))) {
	%>	
		<span style="color:red;font-weight:bold;font-size:10pt;"/><br/><br/>&nbsp;&nbsp;Message From VoiceShot: <%= vsrp.getErrorMessage() %></span>	
	<%  } else { %>
		<span style="color:red;font-weight:bold;font-size:10pt;"/><br/><br/>&nbsp;&nbsp;Manual Voiceshot is created successfully.</span>	
	<%  }
		} else { 
		String route_date = (month+1) + "/" + day + "/" + year;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date user_date = format.parse(route_date);
		Calendar rDate = Calendar.getInstance();
		rDate.setTime(user_date);
		Date _today = new Date();
		Calendar today_date = Calendar.getInstance();
		today_date.setTime(_today);
		long timDiff = (rDate.getTimeInMillis() - today_date.getTimeInMillis());
		long hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(timDiff);
		
		if (hours > -24 && hours < 0) {
			if(!"SMS".equals(vs_format)) {
	%>
	
	<!-----new Voiceshot box-------->
	<form method='POST' name="timePick" id="timePick">
	<input type="hidden" name="vssubmit" value="true"/>
	<input type="hidden" name="month" value="<%=month%>" />
    <input type="hidden" name="day" value="<%=day%>" />
    <input type="hidden" name="year" value="<%=year%>" />
    <input type="hidden" name="route" value="<%=route%>" />
    <input type="hidden" name="stop1" value="<%=stop1%>" />
	<input type="hidden" name="stop2" value="<%=stop2%>" />
	<input type="hidden" name="vs_format" value="<%=vs_format%>" />
	<% String userRole = CrmSession.getCurrentAgent(request.getSession()).getRole().getLdapRoleName();
	   if(CrmSecurityManager.hasAccessToPage(userRole,"voiceshot.jsp")){ %>
	   <div class="vs_container">
	   <% if(errors) { %>	   
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
			<tr>
				<td width="15%"><span class="sub_nav_title">&nbsp;</span></td>
				<td width="85%" align="left" style="background-color:#E0E0E0;">
					<font style="color:red;font-weight:bold;"><%= errMsg %></font>
				</td>
			</tr>
		</table>	   
	   <% } %>
	<jsp:include page="voiceshot.jsp" />
		</div>
	<% } } }%>
	
	<div class="list_header">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
		<tr>
			<td width="10%">&nbsp;Order #</td>
			<td width="25%">Customer Name</td>
			<td width="10%">Dlv Phone #</td>
			<td width="25%">Email</td>
		<!--<td width="10%">Wave</td>-->
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
			<!--	<td width="10%" class="border_bottom"><%-- <%=routeStopLine.getWaveNumber()!=null? routeStopLine.getWaveNumber():"&nbsp;"%> --%></td>-->
				<td width="10%" class="border_bottom"><%=routeStopLine.getTruckNumber()!=null? routeStopLine.getTruckNumber():"&nbsp;"%></td>
				<td width="10%" class="border_bottom"><%=routeStopLine.getStopSequence()!=null ? Integer.parseInt(routeStopLine.getStopSequence()) : "&nbsp;"%></td>
				<td width="10%" class="border_bottom"><input type="checkbox" value="<%=routeStopLine.getPhoneNumber()%>|<%=routeStopLine.getOrderNumber()%>|<%=routeStopLine.getCustomerId()%>|<%=routeStopLine.getStopSequence()%>|<%=routeStopLine.getTruckNumber()%>" name="selectphone<%=counter%>" checked/></td>
			</tr>
		</logic:iterate>
		</table>
	</div>
	</form>
<%  }    } else { %>
    <div class="content_fixed" align="center"><br><b>No Deliveries for <%= CCFormatter.formatDate(dateParam) %></b><br><br></div>
<%      }   %>
<%  }  %>
</tmpl:put>

</tmpl:insert>

<% } 
//*** IN order to supress the extra blank lines in the export, we had to move these page import down, which is OK as far as the JSP compiler is concerned
%><%@ page import="java.util.*,java.text.*,java.io.*" %><%@ page import="com.freshdirect.fdstore.customer.*,com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*,com.freshdirect.crm.CrmVSCampaignModel,com.freshdirect.webapp.taglib.crm.CrmSession,com.freshdirect.crm.VoiceShotResponseParser,com.freshdirect.customer.EnumVSReasonCodes,java.util.*,com.freshdirect.webapp.crm.security.CrmSecurityManager" %>
<%! DateFormatSymbols symbols = new DateFormatSymbols(); %>

