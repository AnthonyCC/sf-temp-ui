<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUser"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="crm" prefix="crm" %>

<tmpl:insert template='/template/top_nav.jsp'> 

    <tmpl:put name='title' direct='true'>Account Details > Add/Edit Depot Info</tmpl:put>

    	<tmpl:put name='content' direct='true'>
			<crm:GetFDUser id="user">
			<crm:CrmDepotMembershipController user="<%=user%>" result="result" actionName="updateDepotMembership" successPage="/main/account_details.jsp">
			<div class="cust_module" style="width: 60%;">
				<form name="depotForm" method="POST">
				<div class="cust_module_header">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="cust_module_header_text">Add/ Edit Depot Info</td>
							<td width="50%"><a href="/main/account_details.jsp" class="cancel">CANCEL</a>&nbsp;&nbsp;<a href="javascript:document.depotForm.submit();" class="save">SAVE</a></td>
							<td><fd:ErrorHandler result="<%=result%>" name="case_not_attached" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
							<td align="center" class="note">* Required</td>
						</tr>
					</table>
				</div>
				<div class="cust_module_content">
					<table cellpadding="3" cellspacing="5" class="cust_module_text" align="center">
						<tr>
							<td align="right">Select Company:&nbsp;&nbsp;</td>
							<td>
								<select class="pulldown" name="depotCode">
									<option value="">Depot name</option>
									<fd:GetDepots id="depots">
										<logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
											<%if(!depot.isDeactivated()){%>
												<option value="<%= depot.getDepotCode() %>" <%=depot.getDepotCode().equals(user.getDepotCode()) ? "selected" : ""%>><%= depot.getName() %></option>
											<%}%>
										</logic:iterate>
									</fd:GetDepots>
								</select>
							</td>
						</tr>
						<tr>
							<td align="right">Enter Access Code:&nbsp;&nbsp;</td>
							<td><input type="text" class="input_text" style="width: 100px;" name="accessCode"><fd:ErrorHandler result="<%=result%>" name="accessCode" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
						</tr>
					</table><br>
				</div>
				</form>
			</div>
			<br clear="all">
			</crm:CrmDepotMembershipController>
			</crm:GetFDUser>
	    </tmpl:put>
</tmpl:insert>