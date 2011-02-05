<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import='java.util.HashSet' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>

<% 
// remove current cust
session.setAttribute(SessionName.USER,null); %>


<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>My Worklist</tmpl:put>
        
        <tmpl:put name='content' direct='true'>
        	<crm:GetCurrentAgent id='currentAgent'>
            <%             	
            	CrmCaseTemplate template = new CrmCaseTemplate();            
            	template.setAssignedAgentPK( currentAgent.getPK() );
             
            String currentQueryString = NVL.apply(request.getQueryString(),"");
            String filterQueue = request.getParameter("filterQueue");
            String filterSubject = request.getParameter("filterSubject");
            String filterPriority = request.getParameter("filterPriority");
            String filterState = request.getParameter("filterState");
            
            if (filterQueue!=null && !"".equals(filterQueue)) {
                template.setQueue(CrmCaseQueue.getEnum(filterQueue));
            }
            
            if (filterSubject!=null && !"".equals(filterSubject)) {
                template.setSubject(CrmCaseSubject.getEnum(filterSubject));
            }
            
            if (filterPriority!=null && !"".equals(filterPriority)) {
                template.setPriority(CrmCasePriority.getEnum(filterPriority));
            }
            
            Set states = new HashSet();
            if (filterState!=null && !"".equals(filterState)) {
                states.add( CrmCaseState.getEnum(filterState));
            } else {
                states.add( CrmCaseState.getEnum("OPEN"));
                states.add( CrmCaseState.getEnum("REVW"));
                states.add( CrmCaseState.getEnum("APVD"));
            }
            template.setStates( states );
            template.setSortBy(request.getParameter("sortBy"));
            template.setSortOrder(request.getParameter("sortOrder"));
            template.setStartRecord(Integer.parseInt(NVL.apply(request.getParameter("startRecord"), "0")));
            template.setEndRecord(Integer.parseInt(NVL.apply(request.getParameter("endRecord"), FDStoreProperties.getCaseListLength(request.getRequestURI().indexOf("case_history.jsp")>-1))));
            %>

			<%@ include file="/includes/queue_subject_populate.jspf" %>
			
			
			<crm:FindCases id='cases' template='<%= template %>'>

			<div class="sub_nav" style="padding: 0px; padding-left: 6px; padding-right: 6px;">
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
			<form name="case_filter" method="POST" action="/main/worklist.jsp">
				<tr valign="middle">
					<td width="12%"><b>My Worklist</b> [<b><%= cases.size() %></b> / <%= cases.size() %>]</td>
					
			<script language="javascript">
				<!--
					function filterCases(queue,subject,state,priority) {
						var thisQueue = queue;
						var thisSubject = subject;
						var thisState = state;
						var thisPriority = priority;
						var filterUrl = '/main/worklist.jsp?';
							if (thisQueue != null && thisQueue != "") {
								filterUrl += 'filterQueue=' + thisQueue + '&';
							}
							if (thisSubject != null && thisSubject != "") {
								filterUrl += 'filterSubject=' + thisSubject + '&';
							}
							if (thisState != null && thisState != "") {
								filterUrl += 'filterState=' + thisState + '&';
							}
							if (thisPriority != null && thisPriority != "") {
								filterUrl += 'filterPriority=' + thisPriority + '&';
							}
						document.location.href = filterUrl;
					}
			
				//-->
			</script>
			<td  class="download" align="left"><%@ include file="/includes/filter_cases.jspf"%></td>
			
			
					<td width="15%" align="right">
						<b>P</b> <span class="legend">PRIORITY:
						<img src="/media_stat/crm/images/priority_hi.gif" width="11" height="11" hspace="3">HIGH
						<img src="/media_stat/crm/images/priority_md.gif" width="11" height="11" hspace="3">MEDIUM
						<img src="/media_stat/crm/images/priority_lo.gif" width="11" height="11" hspace="3">LOW
						</span>
					</td>
				</tr>
				
			</form>
			</table>
			</div>	
			
			<%@ include file="/includes/case_list.jspf"%>

			</crm:FindCases>
			
			
			
			<iframe id="case_summary" name="case_summary" src="/includes/case_summary.jsp?<%=currentQueryString%>" width="100%" height="280" scrolling="no" FrameBoarder="0"></iframe>
			
			
			</crm:GetCurrentAgent>
	    </tmpl:put>

</tmpl:insert>