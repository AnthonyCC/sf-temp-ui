<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import='java.util.HashSet' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
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
        
            <% CrmCaseTemplate template = new CrmCaseTemplate(); %>
            <crm:GetCurrentAgent id='currentAgent'>
            <% template.setAssignedAgentPK( currentAgent.getPK() ); %>
            <% 
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
			
			<crm:CrmCaseDownloadController actionName="downloadCases" agentPK="<%=currentAgent.getPK()%>" result="result">
			<crm:FindCases id='cases' template='<%= template %>'>

			<div class="sub_nav" style="padding: 0px; padding-left: 6px; padding-right: 6px;">
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
				<tr valign="middle">
					<td width="20%"><b>My Worklist</b> [<b><%= cases.size() %></b> / <%= cases.size() %>]</td>
					<td width="56%" align="center">
						<%@ include file="/includes/download_cases.jspf"%>
					</td>
					<td width="24%" align="right">
						<b>P</b> <span class="legend">PRIORITY:
						<img src="/media_stat/crm/images/priority_hi.gif" width="11" height="11" hspace="3">HIGH
						<img src="/media_stat/crm/images/priority_md.gif" width="11" height="11" hspace="3">MEDIUM
						<img src="/media_stat/crm/images/priority_lo.gif" width="11" height="11" hspace="3">LOW
						</span>
					</td>
				</tr>
				<%if(result.isFailure()){%>
					<tr>
						<td colspan="3" class="error">
						<%for(Iterator i = result.getErrors().iterator(); i.hasNext(); ){
							ActionError error = (ActionError) i.next();
						%>
						&raquo;	<%=error.getDescription()%><br>
						<%}%>
						</td>
					</tr>
				<%}%>
			</table>
			</div>	
			
			<%@ include file="/includes/case_list.jspf"%>

			</crm:FindCases>
			</crm:CrmCaseDownloadController>
			<div class="download">
			<table align="center"><form name="case_filter" method="POST" action="/main/worklist.jsp">
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
			<tr>
			<td><strong>Show only:</strong></td>
			<% template = CrmSession.getSearchTemplate(session); %>
			<td>
				<select name="queue" class="pulldown" onChange="populateSubject(this.form.queue.value,this.form.subject)">
					<option class="title" value="">-Select Queue-</option>
					<logic:iterate id='queue' collection="<%=CrmCaseQueue.getEnumList()%>" type="com.freshdirect.crm.CrmCaseQueue">
						<option value='<%= queue.getCode() %>' <%= queue.equals(template.getQueue()) ? "selected" : "" %>><%= queue.getCode() %> - <%= queue.getName() %></option>
					</logic:iterate>
				</select>
			</td>
			<td>
				<select name="subject" class="pulldown">
					<option class="title" value="">-Select Queue then Subject-</option>
				</select>
			</td>
				<% if (template.getQueue() != null) { %>
				<script language="JavaScript">
				<!--
					populateSubject('<%= template.getQueue().getCode()%>', case_search.subject);
					<% if (template.getSubject() != null) { %>
						case_search.subject.value='<%= template.getSubject().getCode() %>';
					<% } %>
				//->
				</script>
				<% } %>
			<td>
				<select name="state" class="pulldown">
					<option class="title" value="">-Select Status-</option>
					<logic:iterate id='state' collection="<%=CrmCaseState.getEnumList()%>" type="com.freshdirect.crm.CrmCaseState">
						<option value='<%= state.getCode() %>' <%= state.getCode().equalsIgnoreCase(filterState) ? "selected" : "" %>><%= state.getName() %></option>
					</logic:iterate>
				</select>
			</td>
			<td>
				<select name="priority" class="pulldown">
					<option class="title" value="">-Select Priority-</option>
					<logic:iterate id='priority' collection="<%=CrmCasePriority.getEnumList()%>" type="com.freshdirect.crm.CrmCasePriority">
						<option value='<%= priority.getCode() %>' <%= priority.equals(template.getPriority()) ? "selected" : "" %>><%= priority.getName() %></option>
					</logic:iterate>
				</select>
			</td>
			<td><a href="javascript:filterCases(document.case_filter.queue.value, document.case_filter.subject.value, document.case_filter.state.value, document.case_filter.priority.value)"><div class="submit" text-align="center" style="text-decoration:none;">&nbsp;&nbsp;FILTER&nbsp;&nbsp;</div></a>
			</td>
			<td>|</td>
			<td><a href="/main/worklist.jsp"><div class="clear" text-align="center" style="text-decoration:none;">&nbsp;&nbsp;SHOW ALL&nbsp;&nbsp;</div></a></td>
			</tr>
			</form>
			</table>
			</div>
			
			<iframe id="case_summary" name="case_summary" src="/includes/case_summary.jsp?<%=currentQueryString%>" width="100%" height="280" scrolling="no" FrameBoarder="0"></iframe>
			
			</crm:GetCurrentAgent>
			
	    </tmpl:put>

</tmpl:insert>