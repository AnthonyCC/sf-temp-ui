<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSessionStatus' %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
	// remove current cust
session.setAttribute(SessionName.USER,null);
session.setAttribute(SessionName.APPLICATION, "CALLCENTER");
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Home</tmpl:put>
	
<tmpl:put name='content' direct='true'>
<crm:GetCurrentAgent id='currentAgent'>
<% 
CrmAgentRole crmRole = currentAgent.getRole();
boolean showOrderRouteSection = crmRole.getCode().equals(CrmAgentRole.ADM_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.DEV_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.QA_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SUP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SOP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.COS_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.TRN_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.TRNSP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.OPS_CODE);

boolean showGiftCardSearchSection = crmRole.getCode().equals(CrmAgentRole.ADM_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.DEV_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.QA_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.SUP_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.SOP_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.COS_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.MOP_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.NCS_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.CSR_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.SCS_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.FIN_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.BUS_CODE)
								|| crmRole.getCode().equals(CrmAgentRole.SEC_CODE);

boolean showWorkListSection = crmRole.getCode().equals(CrmAgentRole.ADM_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.DEV_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.QA_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SUP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SOP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.COS_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.MOP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.TRN_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.TRNSP_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.OPS_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.NCS_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.CSR_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SCS_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.FIN_CODE)
							|| crmRole.getCode().equals(CrmAgentRole.SEC_CODE);

boolean showCustomerSearchSection = !crmRole.getCode().equals(CrmAgentRole.ERP_CODE)
	&& !crmRole.getCode().equals(CrmAgentRole.ETS_CODE);

//boolean showOrderSearchSection = !crmRole.getCode().equals(CrmAgentRole.ETS_CODE);

boolean showOrderCutOffReportSection = !crmRole.getCode().equals(CrmAgentRole.ETS_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.COS_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.NCS_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.CSR_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.SCS_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.FIN_CODE)
									&& !crmRole.getCode().equals(CrmAgentRole.SEC_CODE);

boolean showOrderSummaryReportSection = !crmRole.getCode().equals(CrmAgentRole.ETS_CODE)
										&& !crmRole.getCode().equals(CrmAgentRole.NCS_CODE)
										&& !crmRole.getCode().equals(CrmAgentRole.CSR_CODE)
										&& !crmRole.getCode().equals(CrmAgentRole.SCS_CODE)
										&& !crmRole.getCode().equals(CrmAgentRole.FIN_CODE)
										&& !crmRole.getCode().equals(CrmAgentRole.SEC_CODE);

		// monitor
		boolean queue_overview = false;
		boolean agent_monitor = false;
		boolean worklist = true;
		
		boolean isGuest = false;		
		
		String pageLink = request.getRequestURI();
		String show = "";//request.getParameter("show"); 
		boolean quickSearch = "quick".equals(request.getParameter("search")) || request.getParameter("search") == null || "null".equals(request.getParameter("search")); 
		
		
		 String currentQueryString = NVL.apply(request.getQueryString(),"");
         String filterQueue = request.getParameter("filterQueue");
         String filterSubject = request.getParameter("filterSubject");
         String filterPriority = request.getParameter("filterPriority");
         String filterState = request.getParameter("filterState");
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
			 
			template.setAssignedAgentPK( currentAgent.getPK() );
			if (filterQueue!=null && !"".equals(filterQueue)) {
                template.setQueue(CrmCaseQueue.getEnum(filterQueue));
            }
            
            if (filterSubject!=null && !"".equals(filterSubject)) {
                template.setSubject(CrmCaseSubject.getEnum(filterSubject));
            }
            
            if (filterPriority!=null && !"".equals(filterPriority)) {
                template.setPriority(CrmCasePriority.getEnum(filterPriority));
            }
			//template.setAssignedAgentId(userId);
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
			<%@ include file="/includes/queue_subject_populate.jspf" %>
		
	<style>.content { background-color: #E7E7D6;}</style>
	<div style="overflow-x: hidden;">
		<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" border="0">
			<tr>
				<td width="33%">
					<div id="order_search" class="home_module" style="width: 100%; float: left; border-top: none; border-right: none; border-bottom: 2px solid; border-left: 1px solid;">
						<%
							String addtlSrchParam = "show=worklist";
							
						%>
						<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
						<tr>
							<td class="home_module_header_text">
								Order Search &nbsp;
								<%=quickSearch?
									"<span class='module_header_note'><b>Quick</b> | </span><a href='"+ pageLink +"?search=advanced"+ "&" + addtlSrchParam +"' class='module_header_note'>Advanced</a>":
									"<a href='"+ pageLink +"?"+ addtlSrchParam +"' class='module_header_note'>Quick</a><span class='module_header_note'> | <b>Advanced</b></span>"%>
							</td>
							<% if(CrmSecurityManager.hasAccessToPage(crmRole.getLdapRoleName(),"refusal_list.jsp")){ %>
							<td align="right">
								<%-- if (currentAgent.isSupervisor() || currentAgent.isMonitor()) { --%>
									<a href="/supervisor/refusal_list.jsp" class="new">VIEW REFUSALS</a>
								<%-- } --%>
							</td>
							<% } %>
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
			<% if (showCustomerSearchSection) { %>
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
							<% if(CrmSecurityManager.hasAccessToPage(crmRole.getLdapRoleName(),"nw_cst_check_zone.jsp")){ %>
							<td align="right">
								<a href="<%=hasCase?"/case/unlock.jsp?redirect=new_customer":"/registration/nw_cst_check_zone.jsp"%>" class="new">
									NEW&nbsp;CUSTOMER
								</a>
							</td>
							<% } %>
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
			
			<% } else if(showOrderCutOffReportSection || showOrderSummaryReportSection) { %>
				<%@ include file="/includes/order_cutoff_summary_report.jspf"%>
			<% } %>
			
			</tr>
		</table>

		<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>

		<%if(showOrderRouteSection){ %>
			<%@ include file="/includes/main_route_stop.jspf"%>
			<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>
		<% } %>
		<%if(showGiftCardSearchSection){ %>			
			<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>
			<%@ include file="/includes/giftcard_search.jspf"%>		
			
		<% } %>

			
		<%@ include file="/includes/main_worklist.jspf"%>		
		
		
		
		
	</div>
</crm:GetCurrentAgent>
</tmpl:put>

</tmpl:insert>