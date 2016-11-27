<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<crm:GetCurrentAgent id='currentAgent'>
	<%
		CrmAgentRole crmRole = currentAgent.getRole();
		boolean quickSearch = "quick".equals(request.getParameter("search")) || request.getParameter("search") == null || "null".equals(request.getParameter("search"));
	
		String searchErrorMsg = (String) session.getAttribute("error_msg");
		String searchType = (String) session.getAttribute("search_type");
		
		if ( searchErrorMsg != null && !searchErrorMsg.equals("") && "customer".equalsIgnoreCase(searchType)) {
			session.removeAttribute("error_msg");
		}
		
		if ( searchType != null && !searchType.equals("") && "customer".equalsIgnoreCase(searchType) ) {
			session.removeAttribute("search_type");
		}
		
		String pageLink = request.getRequestURI();
		String addtlSrchParam = "show=worklist";
		boolean hasCase = false;
%>
	<crm:GetLockedCase id="cm">
		<% if (cm != null) hasCase = true; %>
	</crm:GetLockedCase>
	<div id="customer_search" class="home_module home_search_module_container">
		
		<div class="home_search_module_header_content home_module_header_text">
			Customer Search &nbsp;
			<%=quickSearch?
				"<span class='module_header_note'><b>Quick</b> | </span><a href='"+ pageLink +"?search=advanced"+ "&" + addtlSrchParam +"' class='module_header_note'>Advanced</a>":
				"<a href='"+ pageLink +"?"+ addtlSrchParam +"' class='module_header_note'>Quick</a><span class='module_header_note'> | <b>Advanced</b></span>"%>
				
			<% if(CrmSecurityManager.hasAccessToPage(crmRole.getLdapRoleName(),"nw_cst_check_zone.jsp")){ %>
				<a href="<%=hasCase?"/case/unlock.jsp?redirect=new_customer":"/registration/nw_cst_check_zone.jsp"%>" class="fright whiteGreenBtn">
					NEW&nbsp;CUSTOMER
				</a>
			<% } %>
		</div>
		
		<div id="monitor_content" class="home_search_module_content">
			<form name="customer_search" method="POST" action="/main/customer_search_results.jsp">
			<% if (searchErrorMsg != null && !searchErrorMsg.equals("") && "customer".equalsIgnoreCase(searchType)) { %>
				<span class="error">&raquo; <%= searchErrorMsg %></span><br />
			<% } %>
			<input type="hidden" name="customerSearchSubmit" value="submit">
			<input type="hidden" name="search" value="<%=(quickSearch)? "quick":"advanced" %>">
				<table cellpadding="0" cellspacing="2" class="home_search_module_field" border="0" width="100%">
					<tr valign="bottom">
						<td width="50"></td>
						<td width="65">First</td>
						<td width="60"></td>
						<td width="45">Last</td>
						<td width="140"></td>
					</tr>
					<tr>
						<td>Name</td>
						<td colspan="2"><input type="text" name="firstName" value="<%= "null".equalsIgnoreCase(request.getParameter("firstName")) ? "" : request.getParameter("firstName") %>" class="input_text"></td>
						<td colspan="2"><input type="text" name="lastName" value="<%= "null".equalsIgnoreCase(request.getParameter("lastName")) ? "" : request.getParameter("lastName") %>" class="input_text"></td>
					</tr>
					<tr>
						<td>Email</td>
						<td colspan="4"><input type="text" name="email" value="<%= "null".equalsIgnoreCase(request.getParameter("email")) ? "" : request.getParameter("email") %>" class="input_text"></td>
						
					</tr>
					<tr>
						<td>Phone</td>
						<td colspan="2"><input type="text" name="phone" id="cust_<%=(quickSearch)? "q":"" %>s_phone" title="Phone" value="<%= "null".equalsIgnoreCase(request.getParameter("phone")) ? "" : request.getParameter("phone") %>" class="input_text"></td>
						
						<td colspan="1" align="right">Cust.ID</td>
						<td colspan="1"><input type="text" name="customerId" id="cust_qs_id" title="Phone" value="<%= "null".equalsIgnoreCase(request.getParameter("customerId")) ? "" : request.getParameter("customerId") %>" class="input_text"></td>
					</tr>
					<%if(quickSearch) { %>
						<tr>
						<td> &nbsp</td>
						<td colspan="2"> &nbsp</td>
						<td colspan="1" align="right"> SAP.ID</td>
						<td colspan="1"><input type="text" name="customerSAPId" id="cust_sap_id" title="sapId" value="<%= "null".equalsIgnoreCase(request.getParameter("customerId")) ? "" : request.getParameter("customerId") %>" class="input_text"></td>
						</tr>
					<%} %>
					
					<% if (!quickSearch) { %>
						<tr>
							<td>Order #</td>
							<td colspan="2"><input type="text" name="orderNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("orderNumber")) ? "" : request.getParameter("orderNumber") %>" class="input_text"></td>
							<td align="right">Depot</td>
							<td colspan="1">
								<select name="depotCode" class="pulldown">
									<option value="" class="inactive" selected>Company Location</option>
									<fd:GetDepots id="depots" includeCorpDepots="true">
									<logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.fdlogistics.model.FDDeliveryDepotModel">
										<option value="<%= depot.getDepotCode() %>"><%= depot.getName() %></option>
									</logic:iterate>
									</fd:GetDepots>
								</select>
							</td>
						</tr>
						<tr>
							<td>Address</td>
							<td colspan="4"><input type="text" name="address" value="<%= "null".equalsIgnoreCase(request.getParameter("address")) ? "" : request.getParameter("address") %>" class="input_text"></td>
							
						</tr>
						<tr>
							<td>Apt.</td>
							<td colspan="2"><input type="text" name="apartment" value="<%= "null".equalsIgnoreCase(request.getParameter("apartment")) ? "" : request.getParameter("apartment") %>" class="input_text"></td>
							<td align="right">Zip</td> 
							<td><input type="text" name="zipCode" value="<%= "null".equalsIgnoreCase(request.getParameter("zipCode")) ? "" :  request.getParameter("zipCode") %>" class="input_text"></td>
							
						</tr>
					<% } %>
					
					<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH CUSTOMER" class="submit"></td></tr>
				</table>
			</form>
			<% if (!quickSearch) { %>
				<form name="companySearch" method="POST" action="/main/company_search_results.jsp">
					<table cellpadding="0" cellspacing="2" class="home_search_module_field" border="0" width="100%">
						<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"></td></tr>
						<tr>
					    	<td width="100">Company Name</td>
					        <td colspan="4">
					        	<input type="text" name="companyName" class="input_text" />
							</td>
						</tr>
						<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="submit" value="SEARCH COMPANY" name="submit" class="submit"></td></tr>
					</table>
				</form>
			<% } %>
		</div>
	</div>

	<script type="text/javascript">
		FreshDirect.PhoneValidator.register(document.getElementById("cust_<%=(quickSearch)? "q":"" %>s_phone"), true);
	</script>

</crm:GetCurrentAgent>
