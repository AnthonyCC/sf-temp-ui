<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import="com.freshdirect.crm.CrmAgentModel"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.crm.CrmCaseQueue"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>

<tmpl:insert template='/template/user_mgmt.jsp'>

    <tmpl:put name='title' direct='true'>User Management > Create New User</tmpl:put>

	<tmpl:put name='content' direct='true'>
	<%CrmAgentModel newAgent = new CrmAgentModel();%>
	<crm:CrmAgentController agent="<%=newAgent%>" actionName="create_user" result="result" successPage="index.jsp">
		<form method="POST">
		<div class="content_header">
			<table width="100%">
				<tr>
					<td class="user_mgmt_header">Create New User</td>
					<td>&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=ActionError.GENERIC%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					<td align="right"><input type="submit" class="submit" value="CREATE USER" style="align: right;"><input type="reset" class="clear" value="CLEAR" style="align: right;"></td>
				</tr>
			</table>
		</div>
		<div class="content_scroll">
			<table class="user_mgmt_content" cellpadding="0" cellspacing="6" border="0">
				<tr>
					<td><br>Name:</td>
					<td>First<br><input type="text" class="input_text" style="width: 100px;" name="first_name" value="<%=newAgent.getFirstName()%>">&nbsp;<fd:ErrorHandler result="<%= result %>" name="first_name" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					<td></td>
					<td>Last<br><input type="text" class="input_text" style="width: 150px;" name="last_name" value="<%=newAgent.getLastName()%>">&nbsp;<fd:ErrorHandler result="<%= result %>" name="last_name" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td>UserId:</td>
					<td colspan=""><input type="text" class="input_text" style="width: 100px;" name="user_id" value="<%=newAgent.getUserId()%>">&nbsp;<fd:ErrorHandler result="<%= result %>" name="user_id" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					<td></td>
					<td>Active: <input type="checkbox" name="active" <%=newAgent.isActive() ? "checked" : ""%>></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td colspan="3"><input type="password" class="input_text" style="width: 100px;" name="password">&nbsp;<fd:ErrorHandler result="<%= result %>" name="password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td>Verify Password:</td>
					<td><input type="password" class="input_text" style="width: 100px;" name="verify_password">&nbsp;<fd:ErrorHandler result="<%= result %>" name="verify_password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
				</tr>
				<tr>
					<td>User Level:</td>
					<td colspan="3">
					<select class="pulldown" name="role">
						<option></option>
						<%for(Iterator i = CrmAgentRole.getEnumList().iterator(); i.hasNext(); ){
							CrmAgentRole role = (CrmAgentRole)i.next();
						%><% if (!role.equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE))) { %>
							<option <%=role.equals(newAgent.getRole()) ? "selected" : "" %> value="<%=role.getCode()%>"><%=role.getName()%></option>
							<%}%>
						<%}%>
					</select>
					&nbsp;<fd:ErrorHandler result="<%= result %>" name="role" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler>
					</td>
				</tr>
			</table>

			<table width="80%" class="user_mgmt_content" cellpadding="0" cellspacing="6" border="0">
				<tr>
					<td colspan="3"></td>
				</tr>
				<tr>
					<td colspan="3">Downloadable Queues:</td>
				</tr>
				<tr>
				<%int cellCount = 1;
				for(Iterator i = CrmCaseQueue.getEnumList().iterator(); i.hasNext(); ){
					CrmCaseQueue queue = (CrmCaseQueue)i.next();%>
					<td><input type="checkbox" name="queues" <%=newAgent.hasCaseQueue(queue) ? "checked" : "" %> value="<%=queue.getCode()%>"> <b><%=queue.getCode()%></b> - <%=queue.getName()%></td>
					<%if(cellCount%3 == 0){%>
						</tr>
						<tr>
					<%} cellCount++;%>
				<%}%>
				</tr>
			</table>
		</div>
		</form>
		</crm:CrmAgentController>
	</tmpl:put>

</tmpl:insert>