<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter"%>
<%@ page import="com.freshdirect.fdstore.customer.*"%>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.delivery.ejb.AirclicManager"%>
<%@ page import="com.metaparadigm.jsonrpc.JSONRPCBridge"%>
<%@ page import="com.freshdirect.logistics.delivery.model.DeliverySummary"%>
<%@ page import="com.freshdirect.crm.CrmAgentModel" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.delivery.model.*"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.customer.ErpShippingInfo"%>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<%	
	String orderId = request.getParameter("orderId");
	FDOrderI order = CrmSession.getOrder(session, orderId, true);
	ErpShippingInfo shippingInfo = order.getShippingInfo();
%>

<tmpl:insert template='/template/large_pop.jsp'>

	<script type="text/javascript" src="/assets/javascript/jsonrpc_airclic.js"></script>
	
	<tmpl:put name='title' direct='true'>Carton Exceptions View</tmpl:put>
		<tmpl:put name='content' direct='true'>
	
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
	<%
		List cartonInfo = ((FDOrderAdapter)order).getCartonContents();
		DeliverySummary model = AirclicManager.getInstance().lookUpDeliverySummary(order.getErpSalesId(),shippingInfo.getTruckNumber(),
				CCFormatter.defaultFormatDate(order.getDeliveryReservation().getStartTime()),order.getSapOrderId(),order.getEStoreId().getContentId());
		Map<String, List<String>> cartonExceptionMap = model.getExceptions();
		
		if( (cartonInfo != null) && (cartonInfo.size() > 0) ) { 
    %>
		
	
			<tr valign="top"><td id="ac_info" colspan="5"></td> </tr>
			<tr valign="top"><td id="ac_error" colspan="5"></td> </tr>
		
			<tr valign="top">
				<th style="text-align: left; width: 2em;">&nbsp;</th>
				<th align="left">Quantity</th>
				<th align="left">Product</th>
				<th align="left">Final Weight</th>
				<th align="left">Unit Price</th>
		    </tr>
		    
	<%
		for(Map.Entry<String, List<String>> cartonEntry : cartonExceptionMap.entrySet()){
			List<String> cartonNoLst = cartonEntry.getValue();
	%>
		<tr valign="top" class="list_odd_row">
			<td>&nbsp;</td>
			<td colspan="4">Carton Status: <%= cartonEntry.getKey() %> </td>
		 </tr>	
	<%
			for (Iterator itr=cartonNoLst.iterator(); itr.hasNext(); ) {
				String cartonNo = (String) itr.next();			
			
	
			for (Iterator it = cartonInfo.iterator(); it.hasNext(); ) {
				FDCartonInfo carton = (FDCartonInfo) it.next();
				if(carton.getCartonInfo().getCartonNumber().equals(cartonNo)){
		%>
				<tr valign="top" class="list_odd_row">
					<td>&nbsp;</td>
					<td colspan="4">Carton: <%= carton.getCartonInfo().getCartonNumber() %> Type: <%= carton.getCartonInfo().getCartonType() %></td>
				</tr>
				<tr>
					<td colspan="5" class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				</tr>
		<%
				int ix = 0;
				for (Iterator it2 = carton.getCartonDetails().iterator(); it2.hasNext();) {
					FDCartonDetail cartonDetail = (FDCartonDetail) it2.next();
					if (cartonDetail.getCartLine() != null) {
		%>		<tr valign="top">
	                <td>&nbsp;</td>               
					<td>
						<%= cartonDetail.getCartonDetail().getPackedQuantity() %>&nbsp;
						<% if(cartonDetail.getCartonDetail().getWeightUnit() != null) { %>
	 						<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %>
	 					<% } %>
					</td>
					<td><%= cartonDetail.getCartLine().getDescription() %> (<%= cartonDetail.getCartLine().getSkuCode() %>)</td>
					<td><%= cartonDetail.getCartonDetail().getNetWeight() %></td>
					<td><%= cartonDetail.getCartLine().getUnitPrice() %></td>
				</tr>
		<%			
						ix++;
					} // cartonDetail.getCartLine() != null
				}
		%>
	<%
			}
		} // cartonInfo iterator
		
		} // exceptionCarton iterator
			
		} // exception Carton map itr
	%>

 <% } else { %>
	<tr valign="top">
	  No cartons found!
	</tr>
  <% } %>
  </table>
		</tmpl:put>
</tmpl:insert>


