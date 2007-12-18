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
					<tr>
						<td>Order #</td>
						<td colspan="4"><input type="text" name="orderNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("orderNumber")) ? "" : request.getParameter("orderNumber") %>" class="input_text" style="width: 115px;"></td>
					</tr>
					<tr>
						<td>Address</td>
						<td colspan="2"><input type="text" name="address" value="<%= "null".equalsIgnoreCase(request.getParameter("address")) ? "" : request.getParameter("address") %>" class="input_text"></td>
						<td align="right">Apt.</td>
						<td><input type="text" name="apartment" value="<%= "null".equalsIgnoreCase(request.getParameter("apartment")) ? "" : request.getParameter("apartment") %>" class="input_text" style="width: 30px;"></td>
					</tr>
					<tr>
						<td>Zip</td> 
						<td colspan="4"><input type="text" name="zipCode" value="<%= "null".equalsIgnoreCase(request.getParameter("zipCode")) ? "" :  request.getParameter("zipCode") %>" class="input_text"></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="4">Depot Member<br>
						<select name="depotCode" class="pulldown">
    					<option value="" class="inactive" selected>Company Name</option>
						<fd:GetDepots id="depots" includeCorpDepots="true">
                            <logic:iterate collection="<%= depots %>" id="depot" type="com.freshdirect.delivery.depot.DlvDepotModel">
                            <option value="<%= depot.getDepotCode() %>"><%= depot.getName() %></option>
                            </logic:iterate>
                       	</fd:GetDepots>
  						</select></td>
					</tr>
					<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH CUSTOMER" class="submit"></td></tr>
                    </form>
                    <form name="companySearch" method="POST" action="/main/company_search_results.jsp">
                    <tr><td colspan=5>&nbsp;</td></tr>
                    <tr>
                        <td>Company Search</td>
                        <td colspan="4">
                            <input type="text" name="companyName" class="input_text">   &nbsp;&nbsp;&nbsp; <input type="submit" value="SEARCH" name="submit" class="submit">
                        </td>
                    </tr>
                    </form>
				</table>