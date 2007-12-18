<%@ page import="com.freshdirect.crm.CrmCaseQueue"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%String agentPk = request.getParameter("agent_pk");%>
<crm:GetAgent id="agent" agentId="<%=agentPk%>">
<crm:CrmAgentController agent="<%=agent%>" actionName="update_user" result="result" successPage="index.jsp">
<tmpl:insert template='/template/user_mgmt.jsp'>

    <tmpl:put name='title' direct='true'>User Management > User Profile: Account Setting</tmpl:put>

    	<tmpl:put name='content' direct='true'>

			<div class="content_scroll" style="height: 75%;">
				<form method="POST">
				<table class="user_mgmt_content" cellpadding="0" cellspacing="6" border="0">
					<tr>
						<td width="10%">User Level:</td>
						<td width="20%">
						<select class="pulldown" name="role">
							<%for(Iterator i = CrmAgentRole.getEnumList().iterator(); i.hasNext(); ){
                            CrmAgentRole role = (CrmAgentRole)i.next();
							if (role.equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE))) continue;
						    %>
							    <option <%=role.equals(agent.getRole()) ? "selected" : "" %> value="<%=role.getCode()%>"><%=role.getName()%></option>
							<%}%>
						</select>
						</td>
						<td width="10%"></td>
						<td width="10%" align="center" <%=agent.isActive() ? "class='user_mgmt_selected'" : ""%>>Active: <input type="checkbox" name="active" <%=agent.isActive() ? "checked" : ""%>></td>
						<td width="50%"></td>
					</tr>
					<tr><td colspan="5"></td></tr>
					
				</table>

				<table width="80%" class="user_mgmt_content" cellpadding="0" cellspacing="6" border="0">
					<tr>
						<td colspan="3">Downloadable Queues:</td>
					</tr>
					<tr>
					<%int cellCount = 1;
                    for(Iterator i = CrmCaseQueue.getEnumList().iterator(); i.hasNext(); ){
                        CrmCaseQueue queue = (CrmCaseQueue)i.next();
                        %>
                        <td width="33%" <%=agent.hasCaseQueue(queue) ? "class='user_mgmt_selected'" : ""%>>
                            <input type="checkbox" name="queues" value="<%=queue.getCode()%>" <%=agent.hasCaseQueue(queue) ? "checked" : ""%>> 
                            <b><%=queue.getCode()%></b> - <%=queue.getName()%>
                        </td>
                        <%if(cellCount%3 == 0){%>
                            </tr>
                            <tr>
                        <%} cellCount++;%>
                    <%}%>
					</tr>
				</table>
				<br>
				<table class="user_mgmt_content" cellpadding="0" cellspacing="6" border="0">
					<tr>
						<td align="right">Reset Password:</td>
						<td><input type="password" class="input_text" style="width: 100px;" name="password"><fd:ErrorHandler result="<%= result %>" name="password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
					<tr>
						<td align="right">Verify:</td>
						<td><input type="password" class="input_text" style="width: 100px;" name="verify_password"><fd:ErrorHandler result="<%= result %>" name="verify_password" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
					</tr>
				</table>

				<input type="hidden" name="agent_pk" value="agentPk">
                <div align="center"><input type="submit" class="submit" value="SAVE CHANGES" style="padding-left: 6px; padding-right: 6px;"><br><br>
				* Agents need to logout and log back in for changes to take place.
				</div>
				</form>
			</div>

	    </tmpl:put>

</tmpl:insert>
</crm:CrmAgentController>
</crm:GetAgent>