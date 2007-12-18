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

boolean editMode = request.getParameter("formsOnPage")==null ? false : true;
int formsOnPage;
try {
	formsOnPage = Integer.parseInt(NVL.apply(request.getParameter("formsOnPage"),""));
} catch (NumberFormatException nfe) {
	formsOnPage=0;
}

if (cal.get(Calendar.HOUR_OF_DAY) < 5) {  // if current time  before 5am, then use previous day as the delivery date
	cal.add(Calendar.DAY_OF_WEEK,-1);  // look at yesterday
}

cal.set(Calendar.HOUR,0);
cal.set(Calendar.MINUTE,0);
cal.set(Calendar.SECOND,0);
cal.set(Calendar.AM_PM,Calendar.AM);

List crmSubjects =CrmCaseSubject.getSubjectsForQueue(CrmCaseQueue.CODE_DSQ);//Changed to replace obsolete queue TRQ
String route = NVL.apply(request.getParameter("route"),"");
String stopNumber=NVL.apply(request.getParameter("stopNumber"),"");
Collections.sort(crmSubjects,CrmCaseSubject.COMP_NAME);
String action = NVL.apply(request.getParameter("action"),"getCases");

if (request.getParameter("dlvDate")!=null) {
	try {
		dlvDate = df.parse(request.getParameter("dlvDate"));
	} catch (ParseException pe) {
		pe.printStackTrace();
	}
}

if (dlvDate==null) dlvDate = cal.getTime();
String fmtDlvDate = df.format(dlvDate);
String titleFmtDlvDate = df3.format(dlvDate);

for (Iterator itr = crmSubjects.iterator(); itr.hasNext();) {  
	CrmCaseSubject crmSubject = (CrmCaseSubject)itr.next(); 
	if (!crmSubject.getPriorityCode().equals(CrmCasePriority.CODE_MEDIUM)) {
		itr.remove();
	}
}

%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Issues</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
<%@ include file="/includes/transportation_nav.jsp"%>
    		<crm:GetCurrentAgent id='currAgent'>
			<crm:CrmTRQIssuesController id="casesForOrder" result="result" action="<%=action%>" successPage="/main/worklist.jsp">
<%				if (result.isFailure()) {  %>
				   <table>
<%				    for (Iterator r = result.getErrors().iterator(); r.hasNext(); ) { 
				       ActionError er= (ActionError) r.next();   
				       if (er.getType().indexOf("note_")!=-1 || 
				           er.getType().indexOf("reported_")!=-1 ||
				           er.getType().indexOf("actual_")!=-1) { continue;  }
				       %>
					   <tr><td class="error"><%=er.getDescription()%></font></td></tr>
<%				    }  %>
					</table>
<%				}    %>
<div class="sub_nav">
<table><form name="trqIssues" action="/transportation/crmTrqCases.jsp" method="post"><tr><td width="95%"><span class="sub_nav_title">
		<input type="hidden" name="action" value="storeCases">
		<input type="hidden" name="route" value="<%=route%>">
		<input type="hidden" name="stopNumber" value="<%=stopNumber%>">
<span class="sub_nav_title">Add<%=casesForOrder.size()>0 ? "/Edit" : ""%> Transportation Issue for Delivery Date:</b>&nbsp;<span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate%></span> <input type="hidden" name="dlvDate" id="dlvDate" onChange="dlvDateChange();" size="8"  value="<%=fmtDlvDate %>"><button id="trigger_dlvDate" style="font-size: 9px;">Change Date</button></span><br>&nbsp;<span class="note">Case(s) will be created<%=casesForOrder.size()>0 ?"/updated" :""%> for each issue row with notes filled.</span>
</td><td width="5%" align="right"><input type="submit" value="CREATE<%=casesForOrder.size()>0 ? "/UPDATE" : ""%> ISSUE(S)" class="submit" style="border: solid 2px #FF6600;"></td></tr></table>
</div>
<div style="border-left: solid 4px  #0352C9;">
	<table width="99%" cellspacing="0" cellpadding="0" border="0" bgcolor="#FFFFFF">
		<tr bgcolor="#0352C9">
			<td colspan="4" class="list_header_text" style="padding: 5px;">
				<b>Route: <%=route%></b> &nbsp;&nbsp;<b>Stop #: <%=stopNumber%></b> 
			</td>
		</tr>
		<tr valign="bottom" style="color: #666666;">
			<td style="padding-left: 5px;"><b>Subject Name</b></td>
			<td><b>Notes</b></td>
			<td align="center"><b>Reported #</b></td>
			<td align="center"><b>Actual #</b></td>
		</tr>
			
<%		

int loopControl = editMode ? formsOnPage : crmSubjects.size();
String frmNote;
String frmRQty;
String frmAQty;
String frmPK;
String origNote;
String origRQty="";
String origAQty="";

CrmCaseSubject crmSubject;
CrmCaseSubject lastSubject=null;
int lastSubjectCount=0;
int i=0;

for (int j=0; j < loopControl; j++) {
	if (!editMode) {
		crmSubject = (CrmCaseSubject) crmSubjects.get(j); 
		if (!crmSubject.equals(lastSubject)) {
			lastSubjectCount=1;
			lastSubject = crmSubject;
		} else {
			lastSubjectCount++;
		}
		
		frmNote = "";
		frmRQty = "";
		frmAQty = "";
		frmPK="";
		origNote="";
		origRQty="";
		origAQty="";
		boolean foundCase = false;
		for( Iterator itrCase = casesForOrder.iterator(); !foundCase && itrCase.hasNext();) { 
			CrmCaseModel cm = (CrmCaseModel) itrCase.next();
			if (cm.getSubject().equals(crmSubject)) {
				itrCase.remove();  
				List actions =new ArrayList(cm.getActions());

				Collections.reverse(actions);
				CrmCaseAction ca = (CrmCaseAction) actions.get(0);
				foundCase=true;
				frmPK = cm.getPK().getId();
				frmNote = ca.getNote();
				frmRQty = cm.getProjectedQuantity()+"";
				frmAQty = cm.getActualQuantity()+"";
				origNote = ca.getNote();
			 	origRQty = frmRQty;
			 	origAQty = frmAQty;
				j--; // some people will cringe at this.!!!!
			}
		}
		if ("".equals(frmNote) && lastSubjectCount>1) {
			continue;  //we painted at least one  form for this subject
		}
	} else {
		crmSubject = CrmCaseSubject.getEnum(request.getParameter("subject_"+i));
		frmNote = request.getParameter("note_"+i);
		frmRQty= request.getParameter("reported_"+i);
		frmAQty = request.getParameter("actual_"+i);
		frmPK = NVL.apply(request.getParameter("PK_"+i),"");
		origNote = NVL.apply(request.getParameter("origNote_"+frmPK),"");
		origRQty = NVL.apply(request.getParameter("origRQty"+frmPK),"");
		origAQty = NVL.apply(request.getParameter("origAQty"+frmPK),"");
	}
	
	// find a case in our case list, if found then use latest note
	
    if (i > 0) { %>
			<tr font bgcolor="black"><td colspan="4" height="4"></td></tr>
<%  } %>	
				 <tr bgcolor="#D3EEFE" style="padding-top: 3px; padding-bottom: 3px;">
				 	<% if (!"".equals(frmPK) ) { %>
				 	   <input type="hidden" name="PK_<%=i%>" value="<%=frmPK%>"> 
				 	   <input type="hidden" name="origNote_<%=frmPK%>" value="<%=origNote%>"> 
				 	   <input type="hidden" name="origRQty_<%=frmPK%>" value="<%=origRQty%>"> 
				 	   <input type="hidden" name="origAQty_<%=frmPK%>" value="<%=origAQty%>"> 
				 	   
				 	<% } %>
				  	<td style="padding-left: 5px; border-bottom: solid 1px #0352C9;"><b><%=crmSubject.getName()%></b><input type="hidden" name="subject_<%=i%>" value="<%=crmSubject.getCode()%>"></td>
				  	<td style="border-bottom: solid 1px #0352C9;"><input type="text" size="80" name="note_<%=i%>" value="<%=frmNote%>"></td>
				  	 <td style="border-bottom: solid 1px #0352C9;" align="center"><input type="text" size='2' name="reported_<%=i%>" maxlength="3" value="<%=frmRQty%>"></td>
				  	 <td style="border-bottom: solid 1px #0352C9;" align="center"><input type="text" size='2' name="actual_<%=i%>" maxlength="3" value="<%=frmAQty%>"></td>
				 </tr>
<%		
	if (result.hasError("note_"+i) || result.hasError("reported_"+i) ||result.hasError("actual_"+i)) { %>
		<tr><td>&nbsp;</td><td colspan="3">
			<fd:ErrorHandler result='<%= result %>' name='<%="note_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			<fd:ErrorHandler result='<%= result %>' name='<%="reported_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			<fd:ErrorHandler result='<%= result %>' name='<%="actual_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
		</td></tr>
<% 	}
	i++;
}      %>

	 <input type="hidden" name="formsOnPage" value="<%=i%>">
    </form>	
	</table>
</div>
<div class="content_fixed" style="padding: 3px; padding-right: 10px; padding-bottom: 6px; text-align:right;"><a class="cancel" href="/case_mgmt/index.jsp?action=searchCase&queue=TRQ&state=OPEN">CANCEL</a></div>
<script type="text/javascript">
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