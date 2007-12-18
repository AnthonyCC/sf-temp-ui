<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

				<table cellpadding="0" cellspacing="2" class="home_search_module_field" align="center">
					<tr valign="bottom">
						<td width="55"></td>
						<td width="70">First</td>
						<td width="50">Last</td>
						<td width="20"></td>
						<td width="60"></td>
					</tr>
					<tr>
						<td>Name</td>
						<td><input type="text" name="firstName" value="<%= "null".equalsIgnoreCase(request.getParameter("firstName")) ? "" : request.getParameter("firstName") %>" class="input_text" style="width: 80px;"></td>
						<td colspan="3"><input type="text" name="lastName" value="<%= "null".equalsIgnoreCase(request.getParameter("lastName")) ? "" : request.getParameter("lastName") %>" class="input_text" style="width: 110px;"></td>
					</tr>
					<tr>
						<td>Email</td>
						<td colspan="4"><input type="text" name="email" value="<%= "null".equalsIgnoreCase(request.getParameter("email")) ? "" : request.getParameter("email") %>" class="input_text" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>Phone</td>
						<td colspan="4"><input type="text" name="phone" value="<%= "null".equalsIgnoreCase(request.getParameter("phone")) ? "" : request.getParameter("phone") %>" class="input_text"></td>
					</tr>
					<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH CUSTOMER" class="submit"></td></tr>
				</table>