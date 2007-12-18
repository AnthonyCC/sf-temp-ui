<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.crm.*' %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Workflows</tmpl:put>

<tmpl:put name='content' direct='true'>
	<div class="case_content">
	<%
	CrmCaseSubject subject = CrmCaseSubject.getEnum( request.getParameter("subject") );
	%>
	<form method="GET">
		<select name="subject" class="pulldown" onchange='javascript:this.form.submit();'>
			<option class="title">Select Subject</option>
			<logic:iterate id='queue' collection="<%= CrmCaseQueue.getEnumList() %>" type="com.freshdirect.crm.CrmCaseQueue">
				<option class="header"><%= queue.getCode() %> - <%= queue.getName() %></option>
				<logic:iterate id='subj' collection="<%= queue.getSubjects() %>" type="com.freshdirect.crm.CrmCaseSubject">
					<option value='<%= subj.getCode() %>' <%= subj.equals(subject) ? "selected" : "" %>>
						&nbsp;<%= subj.getName() %>
					</option>
				</logic:iterate>
				<option></option>
			</logic:iterate>
		</select>
	</form>

	<%
	if (subject!=null) {
	%>
		<br>
		<h2><%= subject.getCode() %> - <%= subject.getName() %></h2>
		<%
		CrmAgentRole[] DISPLAY_ROLES = {
			CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
			CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
			CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
			CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
			CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
			CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE)
		};
		%>
		<logic:iterate id='role' collection="<%= DISPLAY_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
			<h3><%= role.getName() %></h3>
			<img src="/images/workflow.png?role=<%= role.getCode() %>&subject=<%= subject.getCode() %>">
		</logic:iterate>
	<%
	}
	%>
	</div>
</tmpl:put>

</tmpl:insert>

