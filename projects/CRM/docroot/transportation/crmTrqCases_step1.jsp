<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.customer.ErpTruckInfo' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import='java.util.HashSet,java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% 
// remove current cust
session.setAttribute(SessionName.USER,null);
DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
DateFormat df3 = new SimpleDateFormat("EEEEEE, MMMM d, yyyy");
CrmManager crmManager = CrmManager.getInstance();
Date dlvDate = null;
Calendar cal = Calendar.getInstance();

if (cal.get(Calendar.HOUR_OF_DAY) < 5) {  // if current time  before 5am, then use previous day as the delivery date
	cal.add(Calendar.DAY_OF_WEEK,-1);  // look at yesterday
}
cal.set(Calendar.HOUR,0);
cal.set(Calendar.MINUTE,0);
cal.set(Calendar.SECOND,0);
cal.set(Calendar.AM_PM,Calendar.AM);

if (request.getParameter("dlvDate")!=null ) {
	try {
		dlvDate = df.parse(request.getParameter("dlvDate"));
	} catch (ParseException pe) {
		 pe.printStackTrace();
	}
}

if (dlvDate==null) dlvDate = cal.getTime();
String fmtDlvDate = df.format(dlvDate);
String titleFmtDlvDate = df3.format(dlvDate);

String route = NVL.apply(request.getParameter("route"),"");
String successPage = "/transportation/crmTrqCases.jsp?dlvDate="+fmtDlvDate+"&route="+route+"&stopNumber="+NVL.apply(request.getParameter("stopNumber"),"");
%>

<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Issues</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
    		<crm:GetCurrentAgent id='currAgent'>
			<crm:CrmTRQIssuesController id="issueList" result="result" successPage="<%= successPage %>" saleId="mySaleId">
<%				if (result.isFailure()) {  %>
				   <table>
<%				    for (Iterator r = result.getErrors().iterator(); r.hasNext(); ) { 
				       ActionError er= (ActionError) r.next();   
				       %>
					   <tr><td><font color="red"><%=er.getDescription()%></font></td></tr>
<%				    }  %>
					</table>
<%				}
	            List routesForDate = crmManager.getTruckNumbersForDate(dlvDate);
%>
<script type="text/javascript">
	var routeArray = new Array();
			var cnt=0;
<%          for (Iterator r =routesForDate.iterator(); r.hasNext(); ) { 
			  	  ErpTruckInfo ti= (ErpTruckInfo) r.next();          %>
			      routeArray[cnt]="<%=Integer.parseInt(ti.getTruckNumber())%>"
			      cnt++;
<%			}    %>

</script>
<div class="sub_nav">
	<table><form name="trqIssues" action="/transportation/crmTrqCases_step1.jsp" method="post"><tr><td width="95%"><span class="sub_nav_title">
	<span class="sub_nav_title">Step 1: Select route and specify the stop # for Transportation Issues for Delivery Date:</b>&nbsp;<span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate%></span> <input type="hidden" name="dlvDate" id="dlvDate" onChange="dlvDateChange();" size="8"  value="<%=fmtDlvDate %>"><button id="trigger_dlvDate" style="font-size: 9px;">Change Date</button></span>&nbsp;<span class="note"></span>
	</td><td width="5%" align="right"><input type="submit" value="Next Step" class="submit" style="border: solid 2px #FF6600;"></td></tr></table>
</div>

<div style="border-left: solid 4px  #0352C9;">
	<table width="99%" cellspacing="0" cellpadding="0" border="0" bgcolor="#FFFFFF">
		<input type="hidden" name="queueCode" value="<%=CrmCaseQueue.CODE_DSQ %>">
		<tr bgcolor="#0352C9">
			<td colspan="4" class="list_header_text" style="padding: 5px;">
				<b>Route</b> &nbsp;:<input type="text" name="route" value="<%=route%>"  onblur="javascript:checkRoute(this);" size="4" maxLength="6">
				&nbsp;&nbsp;
				<b>Stop #</b> <input type="text" name="stopNumber" size="4" value="<%=NVL.apply(request.getParameter("stopNumber"),"")%>">
			&nbsp;&nbsp;<span class="list_header_detail">Route Stop combination must be valid for delivery date selected.</span>
			</td>
		</tr>
</form>	
	</table>
</div>
<div class="content_fixed" style="padding: 3px; padding-right: 10px; padding-bottom: 6px; text-align:right;"><a class="cancel" href="/case_mgmt/index.jsp?action=searchCase&queue=TRQ&state=OPEN">CANCEL</a></div>

<script type="text/javascript">
	    
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

	function dlvDateChange() {
		var currDate = document.trqIssues.dlvDate.value;
		if (currDate!="<%=fmtDlvDate%>") {
			window.location="/transportation/crmTrqCases_step1.jsp?dlvDate=" +currDate;
		}
	}

	Calendar.setup(
	  {
	   showsTime : false,
	   electric : false,
	   inputField : "dlvDate", 
	   ifFormat : "%m-%d-%Y",           
	   button : "trigger_dlvDate"       
	  }
	);
</script>			</crm:CrmTRQIssuesController>
			
			</crm:GetCurrentAgent>
	    </tmpl:put>
	    

</tmpl:insert>
