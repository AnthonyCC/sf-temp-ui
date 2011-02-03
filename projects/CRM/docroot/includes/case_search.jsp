<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%!
final static CrmAgentRole[] DISPLAY_ROLES = {
	CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.CSR_CODE),
	//CrmAgentRole.getEnum(CrmAgentRole.CSRH_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE),
	//CrmAgentRole.getEnum(CrmAgentRole.ASV_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.DEV_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.QA_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SEC_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.FIN_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.ETS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.OPS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.SCS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.COS_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.MOP_CODE),
	CrmAgentRole.getEnum(CrmAgentRole.TRNSP_CODE)};
%>
<%
CrmCaseTemplate template = CrmSession.getSearchTemplate(session);
%>

<div class="case_mgmt_search_header">
	<table width="100%" cellpadding="0" cellspacing="0" class="case_mgmt_text">
	<form name="case_search" method="GET" action="/case_mgmt/case_mgmt_index.jsp">
	<input type="hidden" name="action" value="searchCase">
		<tr>
			<td width="50%"><span class="case_mgmt_header">Case search:</span> (from current list)</td>
			<td align="right"><input type="submit" value="SEARCH CASE" class="submit"><input type="reset" onClick="javascript:document.location='/case_mgmt/index.jsp?action=searchCase'" value="CLEAR" class="clear"></td>
		</tr>
	</table>
</div>

<div id="search_fields" class="case_mgmt_search" style="height: auto;">
	<table width="100%" class="case_mgmt_text">
		<%-- spacer rows --%>
		<tr>
			<td width="13%"></td>
			<td width="25%"></td>
			<td width="14%"></td>
			<td width="13%"></td>
			<td width="10%"></td>
			<td width="25%"></td>
		</tr>
		<%@ include file="/includes/queue_subject_populate.jspf" %>
		<tr>
			<td>Queue</td>
			<td>
				<select name="queue" class="pulldown" onChange="populateSubject(this.form.queue.value,this.form.subject)">
					<option class="title" value="">-Select Queue-</option>
					<logic:iterate id='queue' collection="<%=CrmCaseQueue.getEnumList()%>" type="com.freshdirect.crm.CrmCaseQueue">
						<% if (!queue.isObsolete()) {%>
							<option value='<%= queue.getCode() %>' <%= queue.equals(template.getQueue()) ? "selected" : "" %>><%= queue.getCode() %> - <%= queue.getName() %></option>
						<%}%>
					</logic:iterate>					
				</select>
			</td>
			<td colspan="2"></td>
			<td colspan="2" valign="top">
				<table cellspacing="0">
					<tr>
						<td class="case_mgmt_search_hilite">
							<b>Case #</b>
						</td>
						<td class="case_mgmt_search_hilite">
							<input type="text" class="input_text" name="pk" value='<%= template.getPK()==null ? "" : template.getPK().getId() %>'>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>Subject</td>
			<td colspan="2">
				<select name="subject" class="pulldown">
					<option class="title" value="">-Select Queue then Subject-</option>
					
				</select>
			</td>
            <td align="right">From&nbsp;&nbsp;</td>
			<td colspan="2">
                <table>
                <tr>
                    <td>
                        <input type="hidden" name="startDate" id="startDate" value="<%=(template.getStartDate() != null)?CCFormatter.formatDateYear(template.getStartDate()) : ""%>">
                        <input type="text" name="newStartDate" id="newStartDate" size="10" onchange="setStartDate(this);" value="<%=(template.getStartDate() != null)?CCFormatter.formatDateYear(template.getStartDate()) : ""%>" disabled="true"> &nbsp;<a href="#" id="trigger_startDate"  style="font-size: 9px;">>></a>
                        <script language="javascript">
                                                
                         Calendar.setup(
                          {
                           showsTime : false,
                           electric : false,
                           inputField : "newStartDate",
                           ifFormat : "%Y-%m-%d",
                           singleClick: true,
                           button : "trigger_startDate" 
                          }
                        );
                        </script>
                    </td>
                    <td>
                        To 
                    </td>                
                    <td>
                        <input type="hidden" name="endDate" id="endDate" value="<%=(template.getEndDate() != null)?CCFormatter.formatDateYear(template.getEndDate()): ""%>">
                        <input type="text" name="newEndDate" id="newEndDate" size="10" onchange="setEndDate(this)" value="<%=(template.getEndDate() != null)?CCFormatter.formatDateYear(template.getEndDate()): ""%>" disabled="true"> 
                        &nbsp;<a href="#" id="trigger_endDate" style="font-size: 9px;">>></a>
                        &nbsp;&nbsp;&nbsp;&nbsp; <a href="#" onclick="clearDates();">Clear Dates</a>
                        <script language="javascript">
                         Calendar.setup(
                          {
                           showsTime : false,
                           electric : false,
                           inputField : "newEndDate",
                           ifFormat : "%Y-%m-%d",
                           singleClick: true,
                           button : "trigger_endDate" 
                          }
                        );

                        function clearDates(){
                            document.getElementById("startDate").value="";
                            document.getElementById("newStartDate").value="";
                            document.getElementById("endDate").value="";
                            document.getElementById("newEndDate").value="";
                        }
                        
                        function setStartDate(field){
                            document.getElementById("startDate").value=field.value;
                        }
                        
                        function setEndDate(field){
                            document.getElementById("endDate").value=field.value;
                        }

						<% if (template.getQueue() != null) { %>
							populateSubject('<%= template.getQueue().getCode()%>', case_search.subject);
							<% if (template.getSubject() != null) { %>
								case_search.subject.value='<%= template.getSubject().getCode() %>';
							<% } %>
						<% } %>
						</script>
                    </td>
                </tr>
                </table>
            </td>
        </tr>
		<tr>
			<td>Status</td>
			<td colspan="2">
				<table cellpadding="0" cellspacing="2" border="0">
					<tr>
					<logic:iterate id='state' collection="<%=CrmCaseState.getEnumList()%>" type="com.freshdirect.crm.CrmCaseState">
						<% if (! CrmCaseState.CODE_NEW.equals(state.getCode()) && !CrmCaseState.CODE_REVIEW.equalsIgnoreCase(state.getCode()) && !CrmCaseState.CODE_APPROVED.equalsIgnoreCase(state.getCode())) { %>
							<td><input type="checkbox" name="state" value="<%= state.getCode() %>" <%= template.getStates().contains(state) ? "checked" : "" %>> <%= state.getName() %>&nbsp;&nbsp;&nbsp;</td>
						<% } %>
					</logic:iterate>
					</tr>
				</table>
			</td>
			<td align="right">Origin&nbsp;&nbsp;</td>
			<td colspan="2">
				<select name="origin" class="pulldown">
					<option class="title" value="">-Select Origin-</option>
					<logic:iterate id='origin' collection="<%=CrmCaseOrigin.getEnumList()%>" type="com.freshdirect.crm.CrmCaseOrigin">
						<option value='<%= origin.getCode() %>' <%= origin.equals(template.getOrigin()) ? "selected" : "" %>><%= origin.getName() %></option>
					</logic:iterate>
				</select>
			</td>
		</tr>
		<tr>
			<td>Priority</td>
			<td>
				<select name="priority" class="pulldown">
					<option class="title" value="">-Select Priority-</option>
					<logic:iterate id='priority' collection="<%=CrmCasePriority.getEnumList()%>" type="com.freshdirect.crm.CrmCasePriority">
						<option value='<%= priority.getCode() %>' <%= priority.equals(template.getPriority()) ? "selected" : "" %>><%= priority.getName() %></option>
					</logic:iterate>
				</select>
			</td>
			<td></td>
			<td align="right">Assigned</td>
			<td colspan="2">
				<select name="assignedAgent" class="pulldown">
					<option value="" class="title" value="">-Select Agent-</option>
					<crm:GetAllAgents id="agentList">
					<logic:iterate id='role' collection="<%= DISPLAY_ROLES %>" type="com.freshdirect.crm.CrmAgentRole">
					<% if (!role.equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE))) {%>
						<option value="" class="header"><%= role.getName() %></option>
					<% } %>
						<logic:iterate id='agent' collection="<%= agentList.getAgents(role) %>" type="com.freshdirect.crm.CrmAgentModel">
							<% if (role.equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE))) {%><option value=""></option><%}%>
							<option value="<%= agent.getPK().getId() %>" <%= agent.getPK().equals(template.getAssignedAgentPK()) ? "selected" : "" %>>
								&nbsp;<%= role.equals(CrmAgentRole.getEnum(CrmAgentRole.SYS_CODE)) ? "Unassigned" : agent.getUserId() %>
							</option>
						</logic:iterate>
						<option value=""></option>
					</logic:iterate>
					</crm:GetAllAgents>
				</select>
			</td>
		</tr>
		</form>
	</table>
</div>
