<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>

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
						<td>Order #</td>
						<td colspan="4"><input type="text" name="orderNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("orderNum")) ? "" : request.getParameter("orderNum") %>" class="input_text" style="width: 115px;"></td>
					</tr>
					<tr>
						<td>Phone</td>
						<td colspan="4"><input type="text" name="phone" id="ord_s_phone" title="Phone" value="<%= "null".equalsIgnoreCase(request.getParameter("phone")) ? "" : request.getParameter("phone") %>" class="input_text"></td>
					</tr>
					<tr>
						<% String curDate = CCFormatter.formatDateYear(Calendar.getInstance().getTime()); %>
						<td>Delivery</td>
						<td colspan="4">
							<input type="hidden" name="deliveryDate" id="deliveryDate" value="<%=curDate%>">
                            <input type="text" name="newDeliveryDate" id="newDeliveryDate" size="10" value="<%=curDate%>" disabled="true" onchange="setDate(this);"> &nbsp;<a href="#" id="trigger_dlvDate" style="font-size: 9px;">>></a>
                            <script language="javascript">
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
							<input type="checkbox" name="any" onClick="javascript:toggleDate(); flipCorporate();"> Any
						</td>
					</tr>
                    <tr>
						<td>Gift Card</td>
						<td colspan="4"><input type="text" name="gcNumber" value="<%= "null".equalsIgnoreCase(request.getParameter("gcNumber")) ? "" : request.getParameter("gcNumber") %>" class="input_text"></td>
					</tr>                    
					<tr>
						<td>Depot</td>
						<td colspan="4">
							<select name="depotLocationId" class="pulldown">
                            <option value="" class="inactive" selected>Company Location</option>
                            <%
                                LinkedList dList = new LinkedList();
                                dList.addAll(FDDepotManager.getInstance().getDepots());
                                dList.addAll(FDDepotManager.getInstance().getPickupDepots());
                                for (Iterator dIter = dList.iterator(); dIter.hasNext(); ) {
                                    DlvDepotModel depot = (DlvDepotModel) dIter.next();
                                    for (Iterator lIter = depot.getLocations().iterator(); lIter.hasNext(); ) {
                                        DlvLocationModel location = (DlvLocationModel) lIter.next();
                            %>
                            <option value="<%= location.getPK().getId() %>"><%= location.getFacility() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
						</td>
					</tr>
					<tr>
						<td>Corporate</td>
						<td><input type="checkbox" name="corporate" value="true"></td>
					</tr>
					<tr>
						<td>Email</td>
						<td colspan="4"><input type="text" name="email" value="<%= "null".equalsIgnoreCase(request.getParameter("email")) ? "" : request.getParameter("email") %>" class="input_text" style="width: 200px;"></td>
					</tr>
					<tr>
						<td>Chefs Table</td>
						<td><input type="checkbox" name="chefsTable" value="true" <%="true".equalsIgnoreCase(request.getParameter("chefsTable")) ? "CHECKED" : "" %>></td>
					</tr>
					<tr><td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH ORDER" class="submit"></td></tr>
				</table>
				<script type="text/javascript">
				FreshDirect.PhoneValidator.register(document.getElementById("ord_s_phone"), true);
				</script>
