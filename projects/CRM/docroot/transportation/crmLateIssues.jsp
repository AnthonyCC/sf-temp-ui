<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import='com.freshdirect.customer.ErpTruckInfo' %>
<%@ page import="com.freshdirect.framework.util.*"%>
<%@ page import='java.util.HashSet,java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% 
int MAX_FORMS = 4;
int MAX_NOTE = 100;
session.setAttribute(SessionName.USER,null);

CrmManager crmManager = CrmManager.getInstance();
DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
DateFormat df3 = new SimpleDateFormat("EEEEEE, MMMM d, yyyy");
DateFormat df4 = new SimpleDateFormat("MM-dd-yyyy h:mm a");

String route = request.getParameter("route");
Date dlvDate;

boolean logMode = "true".equalsIgnoreCase(request.getParameter("lateLog"));
boolean editMode = (!"".equalsIgnoreCase(NVL.apply(request.getParameter("lateIssuePK"),"")) || 
                    !"".equals(NVL.apply(request.getParameter("editMode"),"")));
List  routesForDate=new ArrayList();
int formsToShow = editMode ? 1 : MAX_FORMS;

		
String frmDelay;
String frmRoute;
String frmStops;
String frmDlvWindow;
String frmNotes;
String frmReportedAt;
String frmReportedBy;
String frmActualStops;

try {
	dlvDate = df.parse(NVL.apply(request.getParameter("dlvDate"),""));
} catch (ParseException pe) {
	dlvDate=null;
}
if (dlvDate == null ) {
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR,0);
	cal.set(Calendar.MINUTE,0);
	cal.set(Calendar.SECOND,0);
	cal.set(Calendar.AM_PM,Calendar.AM);
	dlvDate = cal.getTime();
}

String fmtDlvDate = df.format(dlvDate);
String titleFmtDlvDate = df3.format(dlvDate);
String successPage = "/transportation/crmLateIssues.jsp?lateLog=true&dlvDate="+fmtDlvDate;
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'><%= editMode ? "Edit ":""%><%= !editMode && !logMode ? "Add  ":""%>Late Route<%= logMode ? " Log" : "" %></tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
    		
			<crm:CrmLateIssue id="lateIssues" route="<%=route%>" successPage="<%=successPage%>" deliveryDate="<%=dlvDate%>" result="result">

<%				if (result.isFailure()) { // show err messages that are not field specific %>
				   <div class="content_fixed">
<%				    for (Iterator r = result.getErrors().iterator(); r.hasNext(); ) { 
				       ActionError er= (ActionError) r.next();   
				       if (er.getType().indexOf("frmRoute_")!=-1 || 
				           	er.getType().indexOf("frmDelay_")!=-1 ||
				           	er.getType().indexOf("frmDlvWindow_")!=-1 ||
				           	er.getType().indexOf("frmStops_")!=-1  ||
				           	er.getType().indexOf("frmActualStops_")!=-1  ||
				           	er.getType().indexOf("frmNotes_")!=-1  ||
 							er.getType().indexOf("frmReportedBy")!=-1 ||
 							er.getType().indexOf("frmReportedAt")!=-1) { continue;  }    				       %>
					   <span class="error"><%=er.getDescription()%></span>
<%				    }  %>
				</div>
<%				}    %>
	<table><form name="lateIssues" action="/transportation/crmLateIssues.jsp" successPage="<%=successPage%>" method="post"></table>
<%				if (logMode) {  %>
<div class="sub_nav">
<span class="sub_nav_title">Reported Late Routes for: <span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate %></span></span>&nbsp;&nbsp;<input type="hidden" name="dlvDate" id="dlvDate" onChange="dlvDateChange();" size="12"  value="<%=fmtDlvDate %>"><button id="trigger_dlvDate"  style="font-size: 9px;">Change Date</button> <span class="note">Changing date loads reported Late Route of selected day; defaults to today.</span>
</div>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text" style="background: #996666;">
<tr>
						<td width="5%"><b><br>Route</b></td>
						<td width="10%"><b><br>Stop(s)</b></td>
						<td width="10%"><b>Delay</b><br>(minutes.)</td>
						<td width="10%"><b><br>Schedule</b></td>
						<td width="8%"><b>Reported<br>By</b></td>
						<td width="12%"><b>Reported<br>At</b></td>
						<td width="31%"><b>Comments</b></td>
						<td colspan="2"width="6%" align="left"><b>Actual<br>Count</b></td>
					 </tr>
</table>
</div>
<%					if (lateIssues.size() > 0) { %>
<div id="result" class="list_content">
				 <table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_content_text">
						<% for (Iterator i = lateIssues.iterator(); i.hasNext(); ) {
							CrmLateIssueModel li = (CrmLateIssueModel) i.next();       %>
							<tr>
							<td width="5%" class="border_bottom"><%=li.getRoute()%></b></td>
							<% String stopTxt = li.getReportedStopsText().length() > 20 ? 
							       li.getReportedStopsText().substring(0,20)+"..." : li.getReportedStopsText();
							 %>
							<td width="10%" class="border_bottom"><%=stopTxt%></td>
							<td width="10%" class="border_bottom"><%=li.getDelayMinutes()%></td>
							<td width="10%" class="border_bottom"><%=li.getDelivery_window()%></td>
							<td width="8%" class="border_bottom"><%=li.getReportedBy()%></td>
							<td width="12%" class="border_bottom"><span class="time_stamp"><%=df4.format(li.getReportedAt())%></span></td>
							<td width="31%" class="border_bottom"><%=li.getComments()%></td>
							<td width="4%" align="left" class="border_bottom"><%=li.getActualStopCount()%></td>
							<td width="1%" class="border_bottom"><a href="/transportation/crmLateIssues.jsp?dlvDate=<%=df.format(li.getDeliveryDate()).substring(0,10)%>&route=<%=li.getRoute()%>&lateIssuePK=<%=li.getPK().getId()%>">Edit</a></td>
						   </tr><% } %></table></div>
<%
					} else { %>
						<div class="content_fixed" align="center"><br>&nbsp;<b>No Late Incidents found for the delivery date of: <%=fmtDlvDate%></b><br><br>
<%					}     %>
					</div>
<%				} else {
					//if in edit mode, remove all the items from the list that does not match the pk
					String frmPK= null;
					if (editMode) {
						frmPK= NVL.apply(request.getParameter("frmPK_0"),request.getParameter("lateIssuePK"));
						for (Iterator itr = lateIssues.iterator(); itr.hasNext(); ){
							CrmLateIssueModel lim = (CrmLateIssueModel) itr.next();
							if (!frmPK.equalsIgnoreCase(lim.getPK().getId())) {
								itr.remove();
							}
						}
					}

					if (!editMode){
					    routesForDate = crmManager.getTruckNumbersForDate(dlvDate);
					}
%>
<div class="sub_nav">
<table><tr><td width="95%"><span class="sub_nav_title">
		<input type="hidden" name="formsOnPage" value="<%=formsToShow%>">
<%        if (!editMode) {%>
			<b>Late Orders for Delivery Date:</b>&nbsp;<span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate %></span></span> <input type="hidden"  <%=editMode ? "readonly" : "" %> name="dlvDate" id="dlvDate"onChange="dlvDateChange();" size="8"  value="<%=fmtDlvDate %>">
			<button id="trigger_dlvDate" style="font-size: 9px;">Change Date</button> 
			<span class="note">All fields required. Enter stops comma-separated individually or as a range; e.g. 1-9, 14, 18 </span>
<% 		   } else { %>
			<b>Edit Incident Log for Delivery Date:</b>&nbsp;<span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate %></span></span> <input type="hidden"  <%=editMode ? "readonly" : "" %> name="dlvDate" id="dlvDate"onChange="dlvDateChange();" size="8"  value="<%=fmtDlvDate %>">
			<input type="hidden" name="editMode" value="true">
			<input type="hidden" name="frmPK_0" value="<%=frmPK%>">
<%         }%>
</td><td width="5%" align="right"><% if (editMode) { %><a class="save" href="javascript:document.lateIssues.submit()">SAVE</a><% } else { %><input type="submit" value="ADD LATE ROUTES" class="submit" style="border: solid 2px #FF6600;"><% } %></td></tr></table>
</div>
<div class="content_fixed" style="padding: 0px;">
<script type="text/javascript">
	var routeArray = new Array();
<% 		int cnt=0;
		for (Iterator r =routesForDate.iterator(); r.hasNext(); ) { 
			  	  ErpTruckInfo ti= (ErpTruckInfo) r.next();          %>
			      routeArray[<%=cnt%>]="<%=Integer.parseInt(ti.getTruckNumber())%>"
<%			      cnt++;
		}    %>

</script>
<%
		CrmLateIssueModel li = null;
		for (int i = 0; i < formsToShow; i++) { 
			 if (editMode && lateIssues.size() >0 ) {
		         li = (CrmLateIssueModel)lateIssues.get(i);
				 frmDelay= li.getDelayMinutes()+"";
				 frmRoute = li.getRoute();
				 frmStops = li.getReportedStopsText();
				 frmActualStops = li.getActualStopsText();
				 frmDlvWindow = li.getDelivery_window();
				 frmNotes = li.getComments();
				 frmReportedAt =df2.format(li.getReportedAt());
				 frmReportedBy =li.getReportedBy();
			} else {
				 frmDelay		= request.getParameter("frmDelay_"+i);
				 frmRoute 		= NVL.apply(request.getParameter("frmRoute_"+i),"");
				 frmStops		= NVL.apply(request.getParameter("frmStops_"+i),"");
				 frmActualStops	= NVL.apply(request.getParameter("frmActualStops_"+i),"");
				 frmDlvWindow	= NVL.apply(request.getParameter("frmDlvWindow_"+i),"");
				 frmNotes		= NVL.apply(request.getParameter("frmNotes_"+i),"");
				 frmReportedAt	= NVL.apply(request.getParameter("frmReportedAt_"+i),"");
				 frmReportedBy  = NVL.apply(request.getParameter("frmReportedBy_"+i),"");
			}                  		%>	
			<div style="border-left: #0352C9 solid 4px; background: #D3EEFE; margin-bottom: 10px; padding-left: 5px; border-bottom: solid 1px #0352C9;" >
		<table width="99%" cellpadding="0" cellspacing="0" border="0" bgcolor="#D3EEFE">
				  <tr>
				  	<td align="right"><b>Route:</b>&nbsp;</td>
				  	<td><%				if (editMode) {  %>
				  	<input type="hidden" name="frmRoute_<%=i%>" value="<%=frmRoute%>"><b><%=frmRoute%></b>
<%				} else {  %><input type="text" name="frmRoute_<%=i%>" type="text" value="<%=frmRoute%>" onblur="javascript:checkRoute(this);" size="4" maxLength="6">
<%				}  %></td>
					<td align="right"><b>Delay:</b>&nbsp;</td>
					<td><select name="frmDelay_<%=i%>">
							<option value="10"  <%=("10".equals(frmDelay) ?"selected" : "")%>>10 minutes</option>
							<option value="20"  <%=("20".equals(frmDelay) ?"selected" : "")%>>20 minutes</option>
							<option value="30"  <%=("30".equals(frmDelay) ?"selected" : "")%>>30 minutes</option>
							<option value="40"  <%=("40".equals(frmDelay) ?"selected" : "")%>>40 minutes</option>
							<option value="50"  <%=("50".equals(frmDelay) ?"selected" : "")%>>50 minutes</option>
							<option value="60"  <%=("60".equals(frmDelay) ?"selected" : "")%>>1 hour</option>
							<option value="90"  <%=("90".equals(frmDelay) ?"selected" : "")%>>1 & 1/2 hours</option>
							<option value="120" <%=("120".equals(frmDelay) ?"selected" : "")%>>2 hours</option>
							<option value="180" <%=("180".equals(frmDelay) ?"selected" : "")%>>3 hours</option>
							<option value="240" <%=("240".equals(frmDelay) ?"selected" : "")%>>4 hours</option>
				  		</select>
				  	</td>
					<td colspan="4" align="left"><b>&nbsp;&nbsp;Notes:</b>&nbsp;
					   <input type="text" name="frmNotes_<%=i%>" size="42" maxLength="<%=MAX_NOTE%>" value="<%=frmNotes%>">(limited to 100 characters)
					 </td>
					</tr>  
					<tr>
					<td align="right"><b>Reported Stop(s):</b>&nbsp;</td>
					<td> <input type="text" name="frmStops_<%=i%>"  maxLength="100" value="<%=frmStops%>"></td>
					<td align="right"><b>Actual Stop(s):</b>&nbsp;</td>
					<td> <input type="text" name="frmActualStops_<%=i%>"  maxLength="100" value="<%=frmActualStops%>"></td>
					<td align="right"><b>Delivery Window:</b>&nbsp;</td>
					<td><input type="text" size='8' maxlength="10" name="frmDlvWindow_<%=i%>" value="<%=frmDlvWindow%>"></td>
					<td align="right"><b>Reported By:</b>&nbsp;</td>
				  	<td><input type="hidden" name="frmAgentUID_<%=i%>" value="<%= userId%>">
				  	    <select name="frmReportedBy_<%=i%>">
				  	    	<option value="">Select one</option>
				  			<option <%=  "driver".equalsIgnoreCase(frmReportedBy) ? "selected" : "" %> value="Driver">Driver</option>
				  			<option <%="customer".equalsIgnoreCase(frmReportedBy) ? "selected" : "" %> value="Customer">Customer</option
				  		</select> 	<b>At:</b>
				  	 <input type="text" name="frmReportedAt_<%=i%>"   onKeypress="javascript:alert('please use button');return false;" id="frmReportedAt_<%=i%>" size="18" value="<%=frmReportedAt%>">
				  	 <button id="trigger_<%=i%>" style="font-size: 9px;">Select Time</button></td>
					</tr>
<%		           if (result.hasError("frmDelay_"+i) || result.hasError("frmDlvWindow_"+i) ||
					   result.hasError("frmStops_"+i) || result.hasError("frmNotes_"+i)  ||
					   result.hasError("frmActualStops_"+i)  || 
					   result.hasError("frmReportedAt_"+i) || result.hasError("frmReportedBy_"+i) || 
					   result.hasError("frmRoute_"+i)) { %>
					   <tr><td colspan="8">
						<fd:ErrorHandler result='<%= result %>' name='<%="frmRoute_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmStops_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmActualStops_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmDelay_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmDlvWindow_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmNotes_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmReportedAt_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						<fd:ErrorHandler result='<%= result %>' name='<%="frmReportedBy_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
						</td></tr>
<%      			}   %>
				 </table>
				 </div>
<%      		}  		%>
				<div class="content_fixed" style="padding: 3px; padding-right: 10px; padding-bottom: 6px; text-align:right;"><a class="cancel" href="/transportation/crmLateIssues.jsp?lateLog=true&dlvDate=<%=fmtDlvDate%>">CANCEL</a></div>
				</div>
<%       	}   %>			
			<table></form></table>
<script type="text/javascript">

	function dlvDateChange() {
		var currDate = document.lateIssues.dlvDate.value;
		if (currDate!="<%=fmtDlvDate%>") {
			window.location="/transportation/crmLateIssues.jsp?dlvDate=" +currDate+"<%=logMode==true ? "&lateLog=true" : ""%>";
		}
	}
	
<%  if (!editMode) {  %>
		Calendar.setup(
		  {
		   showsTime : false,
		   electric : false,
		   inputField : "dlvDate", 
		   ifFormat : "%m-%d-%Y",           
		   button : "trigger_dlvDate"       
		  }
		);

<%   } 		 

for (int i = 0; i < formsToShow && !logMode; i++) { %>
			Calendar.setup(
			  {
			   showsTime : true,
			   timeFormat : "12",
			   inputField : "frmReportedAt_<%=i%>", // ID of the input field
			   ifFormat : "%m-%d-%Y %I:%M %p",           // the date format
			   button : "trigger_<%=i%>"       // ID of the button
			  }
			);
<%		} 	 %>

	function  checkRoute(field) {
		var pos=-1;
		
		if (field.value=="" || field.value==null) return true;
		
	
		var fldValue = field.value;
		
		if (!IsNumericNoDecimal(fldValue)){
			alert("invalid character in route number")
			field.focus();
			field.select();
			return false;
		}
		
		fldValue = parseInt(fldValue,10);

		var validStops="";

		for (var chk=0;chk<routeArray.length && pos==-1; chk++) {
			validStops=validStops + (chk>0 ? ", ":"") + routeArray[chk];
			if (routeArray[chk]==fldValue) 	pos=chk;
		}
		
		if (pos==-1) {
			alert("the route you entered, ("+field.value+"), is invalid\n Valid Routes are:\n"+validStops);
			field.focus();
			field.select();
			return false;
		}
	}
	

</script>
				</crm:CrmLateIssue>
			

	    </tmpl:put>

</tmpl:insert>
