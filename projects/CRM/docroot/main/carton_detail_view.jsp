<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartonInfo"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI"%>
<%@ page import="com.freshdirect.customer.ErpCartonDetails"%>
<%@ page import="com.freshdirect.framework.webapp.*"%>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<%	String orderId = request.getParameter("orderId"); %>

<tmpl:insert template='/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>Carton Detail View</tmpl:put>
		<tmpl:put name='content' direct='true'>

		<fd:GetOrder id='order' saleId='<%= orderId %>' crm="<%= true %>">

<%
	List cartonInfo = null;
%>

<% int idx = 0; %>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
	<tr valign="top">
  <% if(cartonInfo != null && cartonInfo.size() > 0 ) { %>
	  <th align="left" width="150px">Quantity</th>
	  <th align="left">Product</th>
	  <th align="left" width="90px">Final Weight</th>
	  <th align="left" width="75px">Unit Price</th>
	<tr>
		<td colspan="4">
			<logic:iterate id="carton" collection="<%= cartonInfo %>" type="com.freshdirect.fdstore.customer.FDCartonInfo" indexId="counter"> 
			   <% if(!"0000000000".equalsIgnoreCase(carton.getCartonInfo().getCartonNumber())){ %>
				<table width="100%" class="order" cellspacing="0" cellpadding="0">
					<tr valign="top" class="list_odd_row">
						<td>
							Carton: <%= carton.getCartonInfo().getCartonNumber() %>
							<input type="hidden" name="itemCount" value="<%= carton.getCartonDetails().size() %>">
							<input type="hidden" name="cartUpdated" value="true">
						</td>
						<td>Type: <%= carton.getCartonInfo().getCartonType() %></td>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="4" class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
					</tr>
					
					<% idx = 0; %>
					<logic:iterate id="cartonDetail" collection="<%= carton.getCartonDetails() %>" type="com.freshdirect.fdstore.customer.FDCartonDetail" indexId="det"> 
						<% if(cartonDetail.getCartLine() != null) { %>
							<tr valign="top">
								<td width="150px"> 
									<input type="hidden" name="skuCode_<%=idx%>" value="<%= cartonDetail.getCartLine().getSkuCode() %>">
									<input type="hidden" name="quantity_<%=idx%>" value="<%= cartonDetail.getCartonDetail().getActualQuantity() %>">
									<input type="hidden" name="salesUnit_<%=idx%>" value="<%= cartonDetail.getCartLine().getSalesUnit() %>">
									<input type="hidden" name="estPrice_<%=idx%>" value="">
									<input type="hidden" name="originalOrderLineId_<%=idx%>" value="<%= cartonDetail.getCartLine().getOrderLineId() %>">

									<logic:iterate id="entry" collection="<%= cartonDetail.getCartLine().getConfiguration().getOptions().entrySet() %>" type="java.util.Map.Entry">
										<input type="hidden" name='<%= entry.getKey() + "_" + idx %>' value="<%= entry.getValue() %>">
									</logic:iterate>

									<%= cartonDetail.getCartonDetail().getActualQuantity() %>&nbsp;
									<% if(cartonDetail.getCartonDetail().getWeightUnit() != null) { %>
										<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %>
									<% } %>
								</td>
								<td>
									<span style="margin-left: 20px;"><%= cartonDetail.getCartLine().getDescription() %>
									(<%= cartonDetail.getCartLine().getSkuCode() %>)</span>
								</td>
								<td width="90px"> 
									<%= cartonDetail.getCartonDetail().getNetWeight() %>
								</td>
								<td width="75px">
									<%= cartonDetail.getCartLine().getUnitPrice() %>
								</td>
							</tr>
							
							<% 
								if(cartonDetail.getCartonDetail().getComponents().size() > 0 ) { 
									for (int j = 0; j < cartonDetail.getCartonDetail().getComponents().size(); j++) {
										ErpCartonDetails component = cartonDetail.getCartonDetail().getComponents().get(j);
							
							%>
								
								<tr valign="top" class="list_component_row">
									<td width="150px">										
										<%= component.getActualQuantity() %>&nbsp;
										<% if(component.getWeightUnit() != null) { %>
											<%= component.getWeightUnit().toLowerCase() %>
										<% } %>
									</td>
									<td>
										<span style="margin-left: 20px;"><%= component.getMaterialDesc() %>
										(<%= component.getSkuCode() %>)</span>
									</td>
									<td width="90px"> 
										<%= component.getNetWeight() %>
									</td>
									<td width="75px">
										
									</td>
								</tr>							
							
							<% 		}
								} 
							%>
							<%
							idx++;
						} // cartonDetail.getCartLine() != null %>
					</logic:iterate>
				</table>
				<% } %>
			</logic:iterate>
		</td>
	</tr>
  <% } else { %>
	<tr valign="top">
	  No cartons found!
	</tr>
  <% } %>
</table>
<br/><b>
Short shipped items:
</b><br/>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
	<tr valign="top">
  <% if(cartonInfo != null && cartonInfo.size() > 0 ) { %>
	  <th align="left" width="150px">Quantity</th>
	  <th align="left">Product</th>
	  <th align="left" width="90px">Final Weight</th>
	  <th align="left" width="75px">Unit Price</th>
	<tr>
		<td colspan="4">
			<logic:iterate id="carton" collection="<%= cartonInfo %>" type="com.freshdirect.fdstore.customer.FDCartonInfo" indexId="counter"> 
			 <% if("0000000000".equalsIgnoreCase(carton.getCartonInfo().getCartonNumber())){ %>
				<table width="100%" class="order" cellspacing="0" cellpadding="0">
					<tr valign="top" class="list_odd_row">
						<td>
							Carton: <%= carton.getCartonInfo().getCartonNumber() %>
							<input type="hidden" name="itemCount" value="<%= carton.getCartonDetails().size() %>">
							<input type="hidden" name="cartUpdated" value="true">
						</td>
						<td>Type: <%= carton.getCartonInfo().getCartonType() %></td>
						<td colspan="2">
							&nbsp;
						</td>
					</tr>
					<tr>
						<td colspan="4" class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
					</tr>
					
					<% idx = 0; %>
					<logic:iterate id="cartonDetail" collection="<%= carton.getCartonDetails() %>" type="com.freshdirect.fdstore.customer.FDCartonDetail" indexId="det"> 
						<% if(cartonDetail.getCartLine() != null && cartonDetail.isShortShipped() ) { %>
							<tr valign="top">
								<td width="150px"> 
									<input type="hidden" name="skuCode_<%=idx%>" value="<%= cartonDetail.getCartLine().getSkuCode() %>">
									<input type="hidden" name="quantity_<%=idx%>" value="<%= cartonDetail.getCartonDetail().getActualQuantity() %>">
									<input type="hidden" name="salesUnit_<%=idx%>" value="<%= cartonDetail.getCartLine().getSalesUnit() %>">
									<input type="hidden" name="estPrice_<%=idx%>" value="">
									<input type="hidden" name="originalOrderLineId_<%=idx%>" value="<%= cartonDetail.getCartLine().getOrderLineId() %>">

									<logic:iterate id="entry" collection="<%= cartonDetail.getCartLine().getConfiguration().getOptions().entrySet() %>" type="java.util.Map.Entry">
										<input type="hidden" name='<%= entry.getKey() + "_" + idx %>' value="<%= entry.getValue() %>">
									</logic:iterate>

									<%= cartonDetail.getCartonDetail().getActualQuantity() %>&nbsp;
									<% if(cartonDetail.getCartonDetail().getWeightUnit() != null) { %>
										<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %>
									<% } %>
								</td>
								<td>
									<span style="margin-left: 20px;"><%= cartonDetail.getCartLine().getDescription() %>
									(<%= cartonDetail.getCartLine().getSkuCode() %>)</span>
								</td>
								<td width="90px"> 
									<%= cartonDetail.getCartonDetail().getNetWeight() %>
								</td>
								<td width="75px">
									<%= cartonDetail.getCartLine().getUnitPrice() %>
								</td>
							</tr>
							
							<% 
								if(cartonDetail.getCartonDetail().getComponents().size() > 0 ) { 
									for (int j = 0; j < cartonDetail.getCartonDetail().getComponents().size(); j++) {
										ErpCartonDetails component = cartonDetail.getCartonDetail().getComponents().get(j);
							
							%>
								
								<tr valign="top" class="list_component_row">
									<td width="150px">										
										<%= component.getActualQuantity() %>&nbsp;
										<% if(component.getWeightUnit() != null) { %>
											<%= component.getWeightUnit().toLowerCase() %>
										<% } %>
									</td>
									<td>
										<span style="margin-left: 20px;"><%= component.getMaterialDesc() %>
										(<%= component.getSkuCode() %>)</span>
									</td>
									<td width="90px"> 
										<%= component.getNetWeight() %>
									</td>
									<td width="75px">
										
									</td>
								</tr>							
							
							<% 		}
								} 
							%>
							<%
							idx++;
						} // cartonDetail.getCartLine() != null %>
					</logic:iterate>
				</table>
				<% } %>
			</logic:iterate>
		</td>
	</tr>  
  <% } %>
</table>
</fd:GetOrder>
		</tmpl:put>
</tmpl:insert>


