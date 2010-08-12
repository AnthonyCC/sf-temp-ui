<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<% 
// remove current cust
session.setAttribute(SessionName.USER,null); 
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Home</tmpl:put>
	
<tmpl:put name='content' direct='true'>
<crm:GetCurrentAgent id='currentAgent'>
	<% // monitor
		boolean queue_overview = true;
		boolean agent_monitor = false;
		boolean worklist = false;
		
		boolean isGuest = currentAgent.isGuest() || currentAgent.isExec();
		boolean isCsr = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE)) || currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE));
		
		String pageLink = request.getRequestURI();
		String show = request.getParameter("show"); 
		boolean quickSearch = "quick".equals(request.getParameter("search")) || request.getParameter("search") == null || "null".equals(request.getParameter("search")); 
		
		if ("agent".equalsIgnoreCase(show)) {
			queue_overview = false;
			agent_monitor = true;
		} else if ("worklist".equalsIgnoreCase(show) || !currentAgent.isSupervisor()){
			queue_overview = false;
			worklist = true;
		}
		%>
		
		<% boolean hasCase = false; %>
			<crm:GetLockedCase id="cm">
				<% if (cm != null) hasCase = true; %>
			</crm:GetLockedCase>
		
		<% 
			String searchErrorMsg = (String) session.getAttribute("error_msg");
			String searchType = (String) session.getAttribute("search_type");
			
			if ( searchErrorMsg != null && !searchErrorMsg.equals("") ) {
				session.removeAttribute("error_msg");
			}
			
			if ( searchType != null && !searchType.equals("") ) {
				session.removeAttribute("search_type");
			}
		%>

		<% 
			List cases = null;
			CrmCaseTemplate template = new CrmCaseTemplate();
			if (worklist) { 
				if (worklist) {
					template.setAssignedAgentPK( currentAgent.getPK() );
					
					template.setSortBy(request.getParameter("sortBy"));
					template.setSortOrder(request.getParameter("sortOrder"));
					template.setStartRecord(Integer.parseInt(NVL.apply(request.getParameter("startRecord"), "0")));
					template.setEndRecord(Integer.parseInt(NVL.apply(request.getParameter("endRecord"), FDStoreProperties.getCaseListLength(request.getRequestURI().indexOf("case_history.jsp")>-1))));
					Set states = new HashSet();
					states.add( CrmCaseState.getEnum("OPEN"));
					states.add( CrmCaseState.getEnum("REVW"));
					states.add( CrmCaseState.getEnum("APVD"));
					template.setStates( states );
					%>
					<crm:FindCases id='caseList' template='<%= template %>'>
						<% cases = caseList; %>
					</crm:FindCases>
				<%
				}
			%>
			<%@ include file="/includes/queue_subject_populate.jspf" %>
		<% } %>
	<style>.content { background-color: #E7E7D6;}</style>
	<div style="overflow-x: hidden;">
		<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" border="0">
			<tr>
				<td width="33%">
					<div id="order_search" class="home_module" style="width: 100%; float: left; border-top: none; border-right: none; border-bottom: 2px solid; border-left: 1px solid;">
						<%
							String addtlSrchParam = "";
							if (queue_overview) {
								addtlSrchParam = "show=";
							} else if (agent_monitor) {
								addtlSrchParam = "show=agent";
							} else if (worklist || !currentAgent.isSupervisor()) {
								addtlSrchParam = "show=worklist";
							}
						%>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
						<tr>
							<td class="home_module_header_text">
								Order Search &nbsp;
								<%=quickSearch?
									"<span class='module_header_note'><b>Quick</b> | </span><a href='"+ pageLink +"?search=advanced"+ "&" + addtlSrchParam +"' class='module_header_note'>Advanced</a>":
									"<a href='"+ pageLink +"?"+ addtlSrchParam +"' class='module_header_note'>Quick</a><span class='module_header_note'> | <b>Advanced</b></span>"%>
							</td>
							<td align="right">
								<% if (currentAgent.isSupervisor() || currentAgent.isMonitor()) { %>
									<a href="/supervisor/refusal_list.jsp" class="new">VIEW REFUSALS</a>
								<% } %>
							</td>
						</tr>
						</table>
						<div id="monitor_content" class="home_search_module_content" style="height: <%= isGuest?"35":"20"%>em; padding-top: 0px; padding-bottom: 0px;">
							<form name="order_search" method="POST" action="/main/order_search_results.jsp?search=<%=quickSearch?"quick":"advanced"%>">
							<% if (searchErrorMsg != null && !searchErrorMsg.equals("") && "order".equalsIgnoreCase(searchType)) { %>
								<span class="error">&raquo; <%= searchErrorMsg %></span><br />
							<% } %>
							<input type="hidden" name="searchOrderSubmit" value="submit">
								<% if (quickSearch) { %>
									<jsp:include page='/includes/order_quicksearch.jsp'/>
								<% } else { %>
									<jsp:include page='/includes/order_search.jsp'/>
								<% } %>
							</form>
						</div>
					</div>
				</td>
			<% if (!isGuest) { %>
				<td width="33%">
					<div id="customer_search" class="home_module" style="width: 100%; float: left; border-right: none; border-top: none; border-bottom: 2px solid;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
						<tr>
							<td class="home_module_header_text">
								Customer Search &nbsp;
								<%=quickSearch?
									"<span class='module_header_note'><b>Quick</b> | </span><a href='"+ pageLink +"?search=advanced"+ "&" + addtlSrchParam +"' class='module_header_note'>Advanced</a>":
									"<a href='"+ pageLink +"?"+ addtlSrchParam +"' class='module_header_note'>Quick</a><span class='module_header_note'> | <b>Advanced</b></span>"%>
							</td>
							<td align="right">
								<a href="<%=hasCase?"/case/unlock.jsp?redirect=new_customer":"/registration/nw_cst_check_zone.jsp"%>" class="new">
									NEW&nbsp;CUSTOMER
								</a>
							</td>
						</tr>
						</table>
						<div id="monitor_content" class="home_search_module_content" style="height: 20em; padding-top: 0px; padding-bottom: 0px;">
							<form name="customer_search" method="POST" action="/main/customer_search_results.jsp">
							<% if (searchErrorMsg != null && !searchErrorMsg.equals("") && "customer".equalsIgnoreCase(searchType)) { %>
								<span class="error">&raquo; <%= searchErrorMsg %></span><br />
							<% } %>
							<input type="hidden" name="customerSearchSubmit" value="submit">
								<% if (quickSearch) { %>
									<input type="hidden" name="search" value="quick">
									<jsp:include page='/includes/customer_quicksearch.jsp'/>
								<% } else { %>
									<input type="hidden" name="search" value="advanced">
									<jsp:include page='/includes/customer_search.jsp'/>
								<% } %>
							</form>
						</div>
					</div>
				</td>
				<td width="33%">
					<div id="geocode_address" class="home_module" style="width: 100%; float: left; border-top: none; border-bottom: 2px solid;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
						<tr>
							<td class="home_module_header_text">
								Address Validation 
								<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmAddressValiationHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Address Validation Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a> 
								&nbsp; <span class="home_search_module_field" style="font-weight: normal;">*Required</span>&nbsp;
							</td>
							<td align="right" class="home_search_module_field">
								<a href="http://www.usps.com/zip4/" target="usps" class="home_search_module_field" title="USPS ZIP check" style="color:#003399; text-decoration:none;">
									<span style="color:#CC0000;">&raquo;</span> USPS
								</a>
							</td>
						</tr>
						</table>
						<div id="monitor_content" class="home_search_module_content" style="height: 20em; padding-top: 0px; padding-bottom: 0px; overflow-y: auto;">
							<form name="check_address" method="POST"><span class="space4pix"><br /><br /></span>
							<input type="hidden" name="checkAddressSubmit" value="addressCheck">
								<jsp:include page='/includes/check_address.jsp'/>
							</form>
						</div>
					</div>
				</td>
			<% } else if (isGuest) { // show cutoff report %>
				<td width="">
					<div id="cutoff_report" class="home_module" style="width: 100%; float: left; border-top: none; border-bottom: 2px solid;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;"><tr><td class="home_module_header_text">Orders by Cutoff Search</td></tr></table>
						<div id="monitor_content" scrolling="no" class="home_search_module_content" style="height: 35em; padding-top: 0px; padding-bottom: 0px;">
							<form method="POST" name="searchByCutoff"><span class="space4pix"><br /><br /></span>
								<jsp:include page='/includes/order_cutoff_search.jsp'/>
							</form>
							<table width="90%" cellpaddding="0" cellspacing="0" border="0" align="center">
							<tr>
								<td colspan="3"><u>*List of acceptable order statuses for wave drop/extract are:</u></td>
							</tr>
							<tr>
								<td width="33%">&middot; In process</td>
								<td width="33%">&middot; Authorized</td>
								<td width="33%">&middot; Cancelled</td>
							</tr>
							<tr>
								<td width="33%">&middot; Authorization Failed</td>
								<td width="33%">&middot; AVS Exception</td>
								<td width="33%"></td>
							</tr>
							<tr>
								<td colspan="3" style="padding-top:10px;"><u>If cutoff didn't happen:</u><br />
								<strong>Wait 5 minutes for the email.</strong><br />If still not received, alert AppSupport, ERP & SA.  
								<br /><br />
								<u>If cutoff didn't happen and/or there are orders in:</u></td>
							</tr>
							<tr>
								<td width="33%" style="color:#990033;">&middot; Processing</td>
								<td width="33%" style="color:#990033;">&middot; Modified</td>
								<td width="33%" style="color:#990033;">&middot; Modified Canceled</td>
							</tr>
							<tr>
								<td colspan="3" style="padding-top:10px;"><strong>Wait 10 minutes and re-run cutoff report.</strong>
								<br />If no change to order statuses observed, proceed with wave drop/extract and alert AppSupport, ERP & SA.</td>
							</tr>
							</table>
						</div>
					</div>
				</td>
				<td width="25%">
			
					<div id="report" class="home_module" style="width: 100%; float: left; border-top: none; border-bottom: 2px solid;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
							<tr><td class="home_module_header_text">Order Summary Report</td></tr>
						</table>
						<% String curSummaryDate = CCFormatter.formatDateYear(Calendar.getInstance().getTime()); %>
						<input type="hidden" name="summaryDate" id="summaryDate" value="<%=curSummaryDate%>">
						<div id="monitor_content" class="home_search_module_content" style="height: 35em;  padding-top: 0px; padding-bottom: 0px; text-align:center;">
							Summary Date <input type="text" name="newSummaryDate" id="newSummaryDate" size="10" value="<%=curSummaryDate%>" disabled="true" onchange="setSummaryDate(this);"> &nbsp;<a href="#" id="trigger_sumDate" style="font-size: 9px;">>></a>
									<br /><img src="/media_stat/crm/images/clear.gif" width="1" height="13"><br />
									<input type="submit" value="SUBMIT" class="submit" onClick="loadUrl();">
							<script language="javascript"> 
								function loadUrl(){
									URL= "/reports/summary_report.jsp?method=GET&summaryDate="+document.getElementById("summaryDate").value;
									day = new Date();
									id = day.getTime();
									eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,width=300,height=250,left = 540,top = 412');");  
								}
								function setSummaryDate(field){
									document.getElementById("summaryDate").value=field.value;
								}
								
								Calendar.setup(
									{
										showsTime : false,
										electric : false,
										inputField : "newSummaryDate",
										ifFormat : "%Y-%m-%d",
										singleClick: true,
										button : "trigger_sumDate" 
										}
								);
							</script>
						</div>
					</div>
				</td>
			<% } %>
			</tr>
		</table>

		<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>

		<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" border="0">
			<tr>
				<td width="66%">
					<div class="home_module" id="gift_card_search" style="width: 100%; float: left;border-right-width: 0px; border-bottom: 2px solid;">
						<% if (searchErrorMsg != null && !searchErrorMsg.equals("") && "customer".equalsIgnoreCase(searchType)) { %>
							<span class="error">&raquo; <%= searchErrorMsg %></span><br />
						<% } %>
						<form name="giftcard_search" id="giftcard_search" action="/gift_card/giftcard_landing.jsp" style="margin-bottom: 0px;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em; 0px;">
							<tr>
								<td><span class="module_header_text">Gift Card Search <a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmGiftCardHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Gift Card Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a></span></td>
							</tr>
						</table>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="gcSearchModuleContent" style="height: 40px; text-align: center;">
							<tr>
								<td>Gift Card # : <input type="text" style="width: 140px;" class="input_text" name="gcNumber"/> <span class="gcCertFullNumbNote">certificate or full number</span></td>
								<td>Recipient Email : <input type="text" style="width: 140px;" class="input_text" name="recEmail" /></td>
								<td><input class="submit" type="submit" name="SEARCH" value="SEARCH GIFT CARD" /></td>
							</tr>
						</table>
						</form>
					</div>
				</td>
				<td>
					<div class="home_module" id="gift_card_balance_check" style="width: 100%; float: left; border-bottom: 2px solid;">
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em; 0px;">
							<tr>
								<td><span class="module_header_text">Gift Card Balance Check</span></td>
							</tr>
						</table>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="gcSearchModuleContent" style="height: 40px; text-align: center;">
							<tr>
								<td>Gift Card # : <input type="text" id='gcCheckBalanceBox_gcNumb' style="width: 150px;" class="input_text" name="gcNumber"/></td>
								<td><div id="gcCheckBalanceBoxMsg">&nbsp;</div></td>
								<td><input class="submit" type="button" name="chkBalance" onClick="checkBalance(); return false;" value="CHECK BALANCE" />
							</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>

		<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>

		<% if (!isGuest) { %>
			<% String addtlMonitorParam = quickSearch ? "" : "search=advanced"; %>
			
			<div id="monitor" class="home_module" style="width: 100%; border-bottom: 2px solid; height: auto;">
				<crm:CrmCaseDownloadController actionName="downloadCases" agentPK="<%=currentAgent.getPK()%>" result="result">
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em; <%=worklist?"padding: 0px;":""%>">
				<tr>
					<td>
						<% if (currentAgent.isSupervisor()) { %>
							<%= queue_overview ? 
								"<span class=\"module_header_text\">Queue Overview</span>" : 
								"<a href=\""+ pageLink + "?" + addtlMonitorParam +"\" class=\"module_header_text\">Queue Overview</a>" %>|
							<%= agent_monitor ? 
								"<span class=\"module_header_text\">Agent Monitor</span>" : 
								"<a href=\""+ pageLink +"?show=agent"+ "&" + addtlMonitorParam + "\" class=\"module_header_text\">Agent Monitor</a>" %>|
						<% } %>
						<%= worklist ? 
								"<span class=\"module_header_text\">Worklist (<span class='result'>"+ cases.size() +"</span>)</span>" : 
								"<a href=\""+ pageLink +"?show=worklist"+ "&" + addtlMonitorParam +"\" class=\"module_header_text\">Worklist</a>" %>
					</td>
					<% if (worklist) { %>
					<td width="70%">
						<%@ include file="/includes/download_cases.jspf"%>
					</td>
					<% } %>
				</tr>
				</table>
				<div id="home_monitor_content" class="content" style="padding-left: 0px; height: 18em; overflow-y: hidden;">
					<% if (queue_overview) { %>
						<%@ include file="/includes/queue_overview.jsp"%>
					<% } else if (agent_monitor) { %>
						<%@ include file="/includes/agent_monitor.jsp"%>
					<% } else if (worklist || !currentAgent.isSupervisor()) { %>
						<%@ include file="/includes/case_list.jspf"%>
					<% } %>
				</div>
				</crm:CrmCaseDownloadController>
			</div>
		<%	} %>
	</div>

</crm:GetCurrentAgent>
</tmpl:put>

</tmpl:insert>