<%@ page import='java.util.HashSet,java.text.*' %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Iterator" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.customer.ErpTruckInfo' %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartonInfo" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCartonDetail" %>
<%@ page import="com.freshdirect.framework.webapp.ActionError" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
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

FDOrderI theOrder;

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
			<crm:CrmTRQIssuesController id="casesForOrder" result="result" action="<%=action%>" saleId="mySaleId">
<%				if (result.isFailure()) {
					for (Iterator r = result.getErrors().iterator(); r.hasNext(); ) { 
				    	ActionError er= (ActionError) r.next();
				    	// Display just the global errors
				    	if (!(er.getType().startsWith("note_") || 
				        	er.getType().startsWith("reported_") ||
				        	er.getType().startsWith("carton_") ||
				        	er.getType().startsWith("actual_") )) {
%>					   <div class="error"><%=er.getDescription()%></div>
<%						}
					}
				}

if (mySaleId != null) {
	theOrder = CrmSession.getOrder(session, mySaleId);
} else {
	theOrder = null;
}



%><script type="text/javascript">

var currentPanel;

function assignCartons(obj) {
	var myPanel = new YAHOO.widget.Panel("selDiv", {
		fixedcenter: true, 
		constraintoviewport: true, 
		underlay: "shadow", 
		close: true, 
		visible: false,
		modal: true,
		resizable: true,
		draggable: true}
	);
	myPanel.cfg.setProperty("underlay","matte");

	myPanel.setHeader("FreshDirect CRM : Carton Information");
	myPanel.setBody(document.getElementById("ctSelDiv").innerHTML);

	myPanel.render(document.body);

	// find FORM and set ID
	var cform = myPanel.body.getElementsByTagName("FORM")[0];
	cform.setAttribute("id", 'formCartons');

	// store carton numbers in the appropriate form field
	YAHOO.util.Event.on('formCartons', 'submit', function(e) {
		YAHOO.util.Event.stopEvent(e);
		formElements = new Array(); 
		for (j=0;j<cform.elements.length;j++) {
			el = cform.elements[j];
			if ("checkbox" == el.getAttribute("type") && el.checked) {
				m = el.name.match(/\d+$/);
				if (m != null) {
				      formElements.push(m[0]);
				}
			}
		}
		document.getElementById(obj).value = formElements.length > 0 ? formElements.join(";") : "";

		// TODO: perform close
		myPanel.hide();
	});

	// fix form input names
	for (j=0;j<cform.elements.length;j++) {
		el = cform.elements[j];
		if (el.type == "checkbox") {
			el.name = "cb"+el.name;
		}
	}

	// check already saved elements
	var storedIds = document.getElementById(obj).value;
	if ("" != storedIds) {
		var cids = storedIds.split(/;/);
		for (j=0; j<cids.length; j++) {
			cform['cb'+cids[j]].checked = true;
		}
	}

	
	currentPanel = myPanel;
	
	// open panel
	myPanel.show();
}





function showCartonInfo(cn) {
	var ctPanel = new YAHOO.widget.Panel("cn_"+cn, {
		fixedcenter: true, 
		constraintoviewport: true, 
		underlay: "shadow", 
		close: true, 
		visible: false,
		modal: true,
		draggable: true}
	);
	ctPanel.cfg.setProperty("underlay","matte");

	ctPanel.setHeader("FreshDirect CRM : Carton Contents");
	ctPanel.setBody(document.getElementById("t_cn_"+cn).innerHTML);

	ctPanel.parent = currentPanel;
	
	ctPanel.render(document.body);

	ctPanel.hideEvent.subscribe(function() {
		this.parent.show();
	});

	currentPanel.hide();
	// open panel
	ctPanel.show();
}


// skin body
document.body.setAttribute("class", "yui-skin-sam"); // non-IE
document.body.setAttribute("className", "yui-skin-sam"); // IE

</script>
<%-- [TEMPLATE] Modal forms for setting carton contens for a case  (templates) --%>
<div id="cases" style="display: none;">
	<div id="ctSelDiv">
		<form>
			<table style="border: 0;  border-collapse: collapse; padding: 0; margin: 0">
				<tr>
					<th>&nbsp;</th>
					<th style="border-bottom: 1px solid black;">Carton ID</th>
					<th style="border-bottom: 1px solid black">Type</th>
				</tr>
<%
		for (Iterator it=theOrder.getCartonContents().iterator(); it.hasNext(); ) {
			FDCartonInfo ci = (FDCartonInfo) it.next();
%>
				<tr>
					<td><input name="<%= ci.getCartonInfo().getCartonNumber() %>" type="checkbox"></td>
					<td style="padding: 2px 2em; text-align: center;"><a href="#" onclick="showCartonInfo('<%= ci.getCartonInfo().getCartonNumber() %>'); return false;"><%= ci.getCartonInfo().getCartonNumber() %></a></td>
					<td style="padding: 2px 2em; text-align: center;"><%= ci.getCartonInfo().getCartonType() %></td>
				</tr>
<%
		}
%>
				<tr>
					<td colspan="3" style="text-align: right; padding-top: 1em;">
						<button type="submit">Save</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
<%

		for (Iterator it=theOrder.getCartonContents().iterator(); it.hasNext(); ) {
			FDCartonInfo ci = (FDCartonInfo) it.next();
%>	<div id="t_cn_<%= ci.getCartonInfo().getCartonNumber() %>">
		<div style="padding-bottom: 1em">
			Carton: <%= ci.getCartonInfo().getCartonNumber() %> / Type: <%= ci.getCartonInfo().getCartonType() %>
		</div>
		<table style="width: 100%; border: 0; border-collapse: collapse; padding: 0; margin: 0">
			<tr>
				<th>Quantity</th>
				<th>Product</th>
				<th>Final Weight</th>
				<th>Unit Price</th>
			</tr>
<%
			for (Iterator j=ci.getCartonDetails().iterator(); j.hasNext(); ) {
				FDCartonDetail cartonDetail = (FDCartonDetail) j.next();
%>			<tr>
				<td><%= cartonDetail.getCartonDetail().getPackedQuantity() %><% if(cartonDetail.getCartonDetail().getWeightUnit() != null) {	%>&nbsp;<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %><% } %>
				</td>
				<td><%= cartonDetail.getCartLine().getDescription() %> (<%= cartonDetail.getCartLine().getSkuCode() %>)</td>
				<td><%= cartonDetail.getCartonDetail().getNetWeight() %></td>
				<td><%= cartonDetail.getCartLine().getUnitPrice() %></td>
			</tr>
<%
			}
%>		</table>
	</div>
<%
		}	
%>
</div>
<%-- [TEMPLATE] End of modal form templates --%>
<form name="trqIssues" action="/transportation/crmTrqCases.jsp" method="post">
	<input type="hidden" name="action" value="storeCases">
	<input type="hidden" name="route" value="<%=route%>">
	<input type="hidden" name="stopNumber" value="<%=stopNumber%>">
<div class="sub_nav">
	<table>
		<tr>
			<td width="95%">
				<span class="sub_nav_title">Add<%=casesForOrder.size()>0 ? "/Edit" : ""%> Transportation Issue for Delivery Date:&nbsp;<span style="font-size: 11pt; background: #FFFFCE; padding-right: 3px; padding-left: 3px;"><%=titleFmtDlvDate%></span> <input type="hidden" name="dlvDate" id="dlvDate" onChange="dlvDateChange();" size="8"  value="<%=fmtDlvDate %>"><button id="trigger_dlvDate" style="font-size: 9px;">Change Date</button></span><br>&nbsp;<span class="note">Case(s) will be created<%=casesForOrder.size()>0 ?"/updated" :""%> for each issue row with notes filled.</span>
			</td>
			<td width="5%" align="right"><input type="submit" value="CREATE<%=casesForOrder.size()>0 ? "/UPDATE" : ""%> ISSUE(S)" class="submit" style="border: solid 2px #FF6600;">
			</td>
		</tr>
	</table>
</div>

<div style="border: solid 4px #0352C9;">
	<table width="100%" cellspacing="0" cellpadding="0" border="0" bgcolor="#FFFFFF">
		<tr bgcolor="#0352C9">
			<td colspan="5" class="list_header_text" style="padding: 5px;">
				<b>Route: <%=route%></b> &nbsp;&nbsp;<b>Stop #: <%=stopNumber%></b> 
			</td>
		</tr>
		<tr valign="bottom" style="color: #666666;">
			<td style="font-weight: bold; padding-left: 5px;">Subject Name</td>
			<td style="font-weight: bold;">Cartons</td>
			<td style="font-weight: bold;">Notes</td>
			<td style="font-weight: bold; text-align: center;">Reported #</td>
			<td style="font-weight: bold; text-align: center;">Actual #</td>
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
String frmCartonNumbers="";

CrmCaseSubject crmSubject;
CrmCaseSubject lastSubject=null;
int lastSubjectCount=0;
int i=0;

for (int j=0; j < loopControl; j++) {
	CrmCaseModel thisCase = null;

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
				thisCase = cm;
				
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
		frmCartonNumbers = NVL.apply(request.getParameter("cartonNumbers_"+i),"");
	}

	
	boolean rowHasError = (result.getError("note_"+i)!=null || 
			result.getError("reported_"+i)!=null ||
			result.getError("carton_"+i)!=null ||
			result.getError("actual_"+i)!=null);
	
	// find a case in our case list, if found then use latest note
	
%>	
		<tr style="background-color: <%= rowHasError ? "#f99" : "#D3EEFE" %>">
			<td style="padding-left: 5px; border-top: solid 4px #0352C9;">
				<span style="font-weight: bold;"><%=crmSubject.getName()%></span>
				<input type="hidden" name="PK_<%=i%>" value="<%=frmPK%>"> 
				<input type="hidden" name="origNote_<%=frmPK%>" value="<%=origNote%>"> 
				<input type="hidden" name="origRQty_<%=frmPK%>" value="<%=origRQty%>"> 
				<input type="hidden" name="origAQty_<%=frmPK%>" value="<%=origAQty%>"> 
				<input type="hidden" name="subject_<%=i%>" value="<%=crmSubject.getCode()%>">
				<input id="cts_<%= i %>" type="hidden" name="cartonNumbers_<%=i%>" value="<%= frmCartonNumbers %>">
			</td>
		 	<td style="border-top: solid 4px #0352C9; text-align: center;">
				<a href="#" onclick="assignCartons('cts_<%= i %>'); return false;"><img style="border: 0" src="/media_stat/crm/images/cartons.png"></a>
		 	</td>
			<td style="border-top: solid 4px #0352C9;"><input type="text" size="80" name="note_<%=i%>" value="<%=frmNote%>"></td>
			<td style="border-top: solid 4px #0352C9; text-align: center;"><input type="text" size='2' name="reported_<%=i%>" maxlength="3" value="<%=frmRQty%>"></td>
			<td style="border-top: solid 4px #0352C9; text-align: center;"><input type="text" size='2' name="actual_<%=i%>" maxlength="3" value="<%=frmAQty%>"></td>
		 </tr>
<%		
	if (rowHasError) { %>
		<tr>
			<td colspan="5" style="padding-bottom: 0.3em; text-align: left;">
				<fd:ErrorHandler result='<%= result %>' name='<%="note_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='<%="reported_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='<%="actual_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
				<fd:ErrorHandler result='<%= result %>' name='<%="carton_"+i%>' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
			</td>
		</tr>
<% 	}
	i++;
}
%>

	</table>
</div>
	<input type="hidden" name="formsOnPage" value="<%=i%>">
</form>
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
</script>
</crm:CrmTRQIssuesController>
			</crm:GetCurrentAgent>
	    </tmpl:put>
</tmpl:insert>
