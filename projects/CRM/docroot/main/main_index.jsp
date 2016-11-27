<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.fdlogistics.model.*" %>
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

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Home</tmpl:put>
	
	<crm:GetCurrentAgent id='currentAgent'>

	<tmpl:put name='content' direct='true'>
		<%
			// remove current cust
			if(null ==request.getParameter("clearUser") || "true".equals(request.getParameter("clearUser"))){
				session.setAttribute(SessionName.USER,null);
			}
			session.setAttribute(SessionName.APPLICATION, "CALLCENTER");
			
			CrmAgentRole crmRole = currentAgent.getRole();
			
			boolean showOrderRouteSection = crmRole.getCode().equals(CrmAgentRole.ADM_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.DEV_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.QA_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.SUP_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.SOP_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.COS_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.TRN_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.TRNSP_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.OPS_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.FDX_CODE);
			
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
											|| crmRole.getCode().equals(CrmAgentRole.SEC_CODE)
											|| crmRole.getCode().equals(CrmAgentRole.FDX_CODE);
			
			
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
										|| crmRole.getCode().equals(CrmAgentRole.SEC_CODE)
										|| crmRole.getCode().equals(CrmAgentRole.FDX_CODE);
			
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
		
			String addtlSrchParam = "show=worklist";
			
			String searchErrorMsg = (String) session.getAttribute("error_msg");
			String searchType = (String) session.getAttribute("search_type");
			
			if ( searchErrorMsg != null && !searchErrorMsg.equals("")  && !"order".equalsIgnoreCase(searchType) && !"customer".equalsIgnoreCase(searchType)) {
				session.removeAttribute("error_msg");
			}
			
			if ( searchType != null && !searchType.equals("") && !"order".equalsIgnoreCase(searchType)  && !"customer".equalsIgnoreCase(searchType)) {
				session.removeAttribute("search_type");
			}
			
			boolean hasCase = false;
			
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
			//states.add( CrmCaseState.getEnum("REVW"));
			//states.add( CrmCaseState.getEnum("APVD"));
			template.setStates( states );
		%>
		<crm:GetLockedCase id="cm">
			<% if (cm != null) hasCase = true; %>
		</crm:GetLockedCase>
		<crm:FindCases id='caseList' template='<%= template %>'>
			<% cases = caseList; %>
		</crm:FindCases>
		
		<%@ include file="/includes/queue_subject_populate.jspf" %>
		
		<div style="overflow-x: hidden; background-color: #E7E7D6;">
			<table width="100%" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" border="0">
				<tr>
					<td width="33%">
						<jsp:include page='/includes/order_search.jsp' />
					</td>
				<% if (showCustomerSearchSection) { %>
					<td width="31%">
						<jsp:include page='/includes/customer_search.jsp'/>
					</td>
					<td width="36%">
						<jsp:include page='/includes/check_address.jsp'/>
					</td>
					
				<% } else if (showOrderCutOffReportSection || showOrderSummaryReportSection) { %>
					<%@ include file="/includes/order_cutoff_summary_report.jspf"%>
				<% } %>
				
				</tr>
			</table>
	
	
			<%if(showOrderRouteSection){ %>
				<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>
				<%@ include file="/includes/main_route_stop.jspf"%>
			<% } %>
			
			<%if(showGiftCardSearchSection){ %>			
				<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>
				<%@ include file="/includes/giftcard_search.jspf"%>		
				
			<% } %>
	
						
			
			<% if ( showWorkListSection || (showCustomerSearchSection && (showOrderCutOffReportSection || showOrderSummaryReportSection)) ) { %>
				<div class="separator"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></div>
				
				<div id="monitor" class="home_module" style="width: 100%; border-bottom: 2px solid; height: auto;">
					<% if (showWorkListSection) { %>
						<div class="fleft" style="width: 75%;">
							<%@ include file="/includes/main_worklist.jspf"%>
						</div>
					<% } %>
					<% if (showCustomerSearchSection && showOrderCutOffReportSection) { %>
						<div class="fright" style="width: 25%;">
							<%@ include file="/includes/order_cutoff_report.jspf"%>
						</div>
					<% } %>
					<% if (showCustomerSearchSection && showOrderSummaryReportSection) { %>
						<div class="fright" style="clear: right; width: 25%;">
							<%@ include file="/includes/order_summary_report.jspf"%>
						</div>
					<% } %>
					<br style="clear: both" />
				</div>
			<% } %>
			
			
			
		</div>
	</tmpl:put>
	</crm:GetCurrentAgent>

</tmpl:insert>