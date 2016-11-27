<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.fdlogistics.model.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
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
		
		if ( searchErrorMsg != null && !searchErrorMsg.equals("")  && "order".equalsIgnoreCase(searchType)) {
			session.removeAttribute("error_msg");
		}
		
		if ( searchType != null && !searchType.equals("") && "order".equalsIgnoreCase(searchType) ) {
			session.removeAttribute("search_type");
		}
	
		String pageLink = request.getRequestURI();
		String addtlSrchParam = "show=worklist";
		
	%>
	<div id="order_search" class="home_module home_search_module_container">
		<div class="home_search_module_header_content home_module_header_text">
			Order Search &nbsp;
			<%=quickSearch?
				"<span class='module_header_note'><b>Quick</b> | </span><a href='"+ pageLink +"?search=advanced"+ "&" + addtlSrchParam +"' class='module_header_note'>Advanced</a>":
				"<a href='"+ pageLink +"?"+ addtlSrchParam +"' class='module_header_note'>Quick</a><span class='module_header_note'> | <b>Advanced</b></span>"%>
			
			<% if(CrmSecurityManager.hasAccessToPage(crmRole.getLdapRoleName(),"refusal_list.jsp")){ %>
				<a href="/supervisor/refusal_list.jsp" class="fright whiteGreenBtn">VIEW REFUSALS</a>
			<% } %>
			
		</div>
		<div id="monitor_content" class="home_search_module_content">
			<form name="order_search" method="POST" action="/main/order_search_results.jsp?search=<%=quickSearch?"quick":"advanced"%>">
				<% if (searchErrorMsg != null && !searchErrorMsg.equals("") && "order".equalsIgnoreCase(searchType)) { %>
					<span class="error">&raquo; <%= searchErrorMsg %></span><br />
				<% } %>
				<input type="hidden" name="searchOrderSubmit" value="submit">
			
				<table cellpadding="0" cellspacing="2" class="home_search_module_field" border="0" width="100%">
					<tr valign="bottom">
						<td width="80"></td>
						<td width="60">First</td>
						<td width="60"></td>
						<td width="140">Last</td>
						<td width="20"></td>
					</tr>
					<tr>
						<td>Name</td>
						<td colspan="2"><input type="text" name="firstName" value="<%= "null".equalsIgnoreCase(request.getParameter("firstName")) ? "" : request.getParameter("firstName") %>" class="input_text"></td>
						<td colspan="2"><input type="text" name="lastName" value="<%= "null".equalsIgnoreCase(request.getParameter("lastName")) ? "" : request.getParameter("lastName") %>" class="input_text"></td>
					</tr>
					<tr>
						<td>Order #</td>
						<td colspan="2"><input type="text" name="orderNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("orderNum")) ? "" : request.getParameter("orderNum") %>" class="input_text"></td>
						<% if (quickSearch) { %>
							<td colspan="2"></td>
						<% } else { %>
							<td align="right">Corporate</td>
							<td><input type="checkbox" name="corporate" value="true"></td>
						<% } %>
					</tr>
					<tr>
						<td>Phone</td>
						<td colspan="2">
							<input type="text" name="phone" id="ord_<%=(quickSearch)? "q":"" %>s_phone" title="Phone" value="<%= "null".equalsIgnoreCase(request.getParameter("phone")) ? "" : request.getParameter("phone") %>" class="input_text">
						</td>
						<% if (quickSearch) { %>
							<td colspan="2"></td>
						<% } else { %>
							<td align="right">Chefs Table</td>
							<td><input type="checkbox" name="chefsTable" value="true" <%="true".equalsIgnoreCase(request.getParameter("chefsTable")) ? "CHECKED" : "" %>></td>
						<% } %>
					</tr>
					<tr>
						<% String curDate = CCFormatter.formatDateYear(Calendar.getInstance().getTime()); %>
						<td>Delivery</td>
						<td colspan="4">
							<input type="hidden" name="deliveryDate" id="deliveryDate" value="<%=curDate%>">
                            <input type="text" name="newDeliveryDate" id="newDeliveryDate" size="10" value="<%=curDate%>" disabled="true" onchange="setDate(this);"> &nbsp;<a href="#" id="trigger_dlvDate" style="font-size: 9px;">>></a>
							<input type="checkbox" name="any" onClick="javascript:toggleDate(); flipCorporate();"> Any
						</td>
					</tr>
				    <tr>
						<td>Gift Card</td>
						<% if (quickSearch) { %>
							<td colspan="4"><input type="text" name="giftCardNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("giftCardNumber")) ? "" : request.getParameter("giftCardNumber") %>" class="input_text"></td>
						<% } else { %>
							<td colspan="4"><input type="text" name="gcNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("gcNumber")) ? "" : request.getParameter("gcNumber") %>" class="input_text"></td>
						<% } %>
					</tr>
					<% if (!quickSearch) { %>
						<tr>
							<td>Depot</td>
							<td colspan="4">
								<select name="depotLocationId" class="pulldown">
									<option value="" class="inactive" selected>Company Location</option>
									<%
									    LinkedList dList = new LinkedList();
									    dList.addAll(FDDeliveryManager.getInstance().getDepots());
									    dList.addAll(FDDeliveryManager.getInstance().getPickupDepots());
									    for (Iterator dIter = dList.iterator(); dIter.hasNext(); ) {
									        FDDeliveryDepotModel depot = (FDDeliveryDepotModel) dIter.next();
									        for (Iterator lIter = depot.getLocations().iterator(); lIter.hasNext(); ) {
									            FDDeliveryDepotLocationModel location = (FDDeliveryDepotLocationModel) lIter.next();
												%><option value="<%= location.getAddress().getId() %>"><%= location.getFacility() %></option><%
									        }
									    }
									%>
								</select>
							</td>
						</tr>
						
						<tr>
							<td>Email</td>
							<td colspan="4"><input type="text" name="email" value="<%= "null".equalsIgnoreCase(request.getParameter("email")) ? "" : request.getParameter("email") %>" class="input_text"></td>
						</tr>
					<% } %>
					<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH ORDER" class="submit"></td></tr>
				</table>
			
			</form>
		</div>
	</div>
	
	<script type="text/javascript">
		FreshDirect.PhoneValidator.register(document.getElementById("ord_<%=(quickSearch)? "q":"" %>s_phone"), true);
	
                            function toggleDate() {
                                document.getElementById("newDeliveryDate").value="";
                                document.getElementById("deliveryDate").value="";
                            }    
                            
                            function setDate(field){
                                document.getElementById("deliveryDate").value=field.value;
                                toggleAny();
                            }
                            
                            function toggleAny() {
                                if (document.order_search.any.checked) {
                                    document.order_search.any.checked = false;
                                }
                            }
                            
                            function flipCorporate() {
                                if (document.order_search.any.checked) {
                                    document.order_search.corporate.checked = false;
                                    document.order_search.corporate.disabled = true;
                                    
                                    document.order_search.corporate.checked = false;
                                    document.order_search.chefsTable.disabled = true;
                                } else {
                                    document.order_search.corporate.disabled = false;
                                    document.order_search.chefsTable.disabled = false;
                                }
                            }
                            
                             Calendar.setup(
                              {
                               showsTime : false,
                               electric : false,
                               inputField : "newDeliveryDate",
                               ifFormat : "%Y-%m-%d",
                               singleClick: true,
                              button : "trigger_dlvDate" 
                              }
                            );
                        </script>

</crm:GetCurrentAgent>
