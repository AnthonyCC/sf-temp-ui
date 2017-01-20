<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.giftcard.*" %>
<%@ page import="com.freshdirect.crm.CrmAgentModel" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.delivery.ejb.AirclicManager"%>
<%@ page import="com.metaparadigm.jsonrpc.JSONRPCBridge"%>
<%@ page import="com.freshdirect.delivery.model.*"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter" %>
<%@page import="com.freshdirect.fdstore.FDStoreProperties"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>
<%
	String orderId = request.getParameter("orderId");
%>
<tmpl:put name='title' direct='true'>Order <%= orderId%> Delivery Center</tmpl:put>

<tmpl:put name='content' direct='true'>

	<script type="text/javascript" src="/assets/javascript/jsonrpc_airclic.js"></script>

    <%@ include file="/includes/order_nav.jspf"%>

    <%
		FDOrderI order = CrmSession.getOrder(session, orderId, true);
		session.removeAttribute(SessionName.RECENT_ORDER);
		session.removeAttribute(SessionName.RECENT_ORDER_NUMBER);
		session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderId);
		
		ErpShippingInfo shippingInfo = order.getShippingInfo();
		String source = order.getOrderSource().getName();
		String estore=order.getEStoreId().getContentId();
		

		boolean sendAirclicMsg = false;
		FDOrderAdapter orderAdapter = (FDOrderAdapter) order;
		boolean isGiftCardOrder = (orderAdapter.getSale().getType().getSaleType().equalsIgnoreCase("GCD"))?true:false;
		if(!isGiftCardOrder){
			if(order.getDeliveryReservation() != null && order.getDeliveryReservation().getStartTime()!=null 
						&& order.getShippingInfo()!=null && order.getShippingInfo().getTruckNumber()!=null && order.getShippingInfo().getStopSequence()!=null
						&& FDStoreProperties.isAirclicEnabled())
			{
				sendAirclicMsg  = CCFormatter.defaultFormatDate(order.getDeliveryReservation().getStartTime())
								.equals(CCFormatter.defaultFormatDate(DateUtil.getCurrentTime())) && !EnumSaleStatus.CANCELED.equals(order.getOrderStatus());
			}
		}
		
		JSONRPCBridge j = JSONRPCBridge.getGlobalBridge();
		if(j != null) {
			j.registerObject("manager", AirclicManager.getInstance());
		} 
			
    %>      
    
<script type="text/javascript" >

	var orderNo = '<%= order.getErpSalesId() %>';
	var estoreId = '<%= order.getEStoreId().toString() %>';
	var routeNo = '<%= shippingInfo.getTruckNumber() %>';
	var date =  '<%= CCFormatter.defaultFormatDate(order.getDeliveryReservation().getStartTime()) %>';
	var hasSignature = <%= FDStoreProperties.isAirclicEnabled() && order instanceof FDOrderI && ((FDOrderI)order).hasSignature() %>;
	
</script>

<script type="text/javascript" src="/assets/javascript/delivery_center.js"></script>

<%@ include file="/includes/order_summary.jspf"%>

<div class="content_scroll" style="height:72%;">
<table cellspacing="0" cellpadding="0" border="0" class="gc_table1" width="100%" >
	<tbody>
		<tr valign="top">
			<td colspan="2">
				<img width="100%" height="15" border="0" src="/media_stat/images/layout/clear.gif">
			</td> 
		</tr>
		<tr valign="top">
		    <td width="69%">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_table3">
					<tbody>
							<tr>
								<th style="text-align:center;">COMMUNICATION</th>
							</tr>
							<tr>
								<td width="10"><img width="10" height="5" border="0" src="/media_stat/images/layout/clear.gif"></th>
							</tr>
							<tr>
								<td class="gc_table_footer">
									<table width="99%" cellspacing="0" cellpadding="0" border="0" class="gc_table3footer">
										<tr>
											<td width="60%" colspan="4"><b>List of Nextels</b></td><td width="40%" align="right"><input type="button" onclick="lookupNextels();";" class="button" value="Refresh" name="Refresh Nextels"></td>
										
										</tr>
										<tr class="gc_colHeader">
										    <td width="30">&nbsp;&nbsp;</td>
											<td width="115">CN #</td>
											<td width="205">Employee</td>
											<td width="117">EmployeeId</td>
											<td style="background-color:#fff" id="ac_info">&nbsp;</td>
										</tr>
										<tr>
											<td colspan="4"><div style="overflow-y:auto;height:120px;" id="nextelInfo"></div></td>
											<td>

											<input name="ddate" id="ddate" type="hidden" value="<%=  CCFormatter.defaultFormatDate(order.getDeliveryReservation().getStartTime()) %>">
											<input name="route" id="route" type="hidden" value="<%= shippingInfo.getTruckNumber() %>">
											<input name="stop" id="stop" type="hidden" value="<%= shippingInfo.getStopSequence()%>">
											<input type="hidden" name="source" id="source" value="CRM" />
											<input type="hidden" name="userId" id="userId" value="<%=CrmSession.getCurrentAgent(request.getSession()).getPK().getId()%>" />
											<input type="hidden" name="orderId" id="orderId" value="<%=order.getErpSalesId()%>" />
											<input type="hidden" name="customerId" id="customerId" value="<%=order.getCustomerId()%>" />
											<textarea value="" name="messageDesc" id="messageDesc" style="width: 220px;height:70px;padding"></textarea><br/><br/>
											
											<input <%= sendAirclicMsg ? "" : "disabled=disabled"%> type="button" style="text-align: center;" onclick="addEntry($F('ddate'), $F('route'), $F('stop'), $F('messageDesc'), $F('source'), $F('userId'), $F('orderId'),$F('customerId'));" class="button" value="SEND MESSAGE" name="SEND MESSAGE">
																						
											
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><img width="1" height="8" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td class="gc_table_footer">
									<table width="99%" cellspacing="0" cellpadding="0" border="0" class="gc_table3footer">
										<tr>
											<td colspan="6"><b>Messages</b></td><td align="right"><input type="button" onclick="lookupAirclicMessages();" class="button" value="Refresh" name="Refresh Message"></td>
										</tr>
										<tr class="gc_colHeader">
											<td width="130">Create Date</td>
											<td width="80">Sender</td>
											<td width="250">Message</td>
											<td width="200">Route</td>
											<td width="30">Stop</td>
											<td width="30">Source</td>
											<td width="80">Sent to Airclic</td>
										</tr>
										<tr>
											<td colspan="7"><div style="overflow-y:scroll;height:150px;" id="airclicMessage"></div></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><img width="1" height="8" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td class="gc_table_footer">
									<table width="99%" cellspacing="0" cellpadding="0" border="0" class="gc_table3footer">
										<tr>
											<td colspan="8"><b>CARTON SCANNING HISTORY</b></td><td align="right"><input type="button" onclick="lookupCartonScanHistory();" class="button" value="Refresh" name="Refresh Cartons"></td>
										</tr>
										<tr class="gc_colHeader">
											<td width="45">Nextel</td>
											<td width="100">Employee</td>
											<td width="100">CartonID</td>
											<td width="30">Type</td>
											<td width="150">Scan Time</td>
											<td width="80">Status</td>
											<td width="80">Delivered To</td>
											<td width="130">Return Reason</td>
											<td width="130">MOT Driver</td>
										</tr>
										<tr>
											<td colspan="9">
												<div style="overflow-y:auto;height:350px;" id="cartonScanInfo"></div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td><img width="1" height="8" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td class="gc_table_footer">
									<table width="99%" cellspacing="0" cellpadding="0" border="0" class="gc_table3footer">
										<tr>
											<td colspan="6"><b>IVR DETAILS</b></td><td align="right"><input type="button" onclick="lookupOrderCallLog();" class="button" value="Refresh" name="Refresh Calllog"></td>
										</tr>
										<tr class="gc_colHeader">
											<td  width="100">Caller ID</td>
											<td width="115">Call Start</td>
											<td width="115">Call Duration</td>
											<td width="115">Talk Time</td>
											<td width="115">Phone Number</td>
											<td width="115">Menu Option</td>
											<td width="115">Call Result</td>
										</tr>
										<tr>
											<td colspan="7"><div id="callLogInfo" style="overflow-y:auto;height:200px;"></div></td>
										</tr>
									</table>
								</td>
							</tr>
					</tbody>
				</table>
			</td>
			<td width="1%">&nbsp;</td>
			<td width="35%">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_table2">
					<tbody>
							<%-- <tr>
								<th style="text-align:center;">DELIVERY SUMMARY</th>
							</tr>
							<tr>
								<td width="10"><img width="10" height="5" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td>
									<%@ include file="/includes/delivery_summary.jspf" %>
								</td>
							</tr> --%>
							<tr>
								<th style="text-align:center;">MANIFEST DETAILS</th>
							</tr>
							<tr>
								<td width="10"><img width="10" height="5" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td class="gc_table_footer">
									<div align="right"><input type="button" onclick="lookupDeliveryManifest();" class="button" value="Refresh" name="Refresh Manifest"></div>
									<div id="manifestDetail" style="height:650px;text-align:left;">
										<script>
										
										</script>
									
									</div>
								
								</td>
							</tr>
							
					</tbody>
				</table>
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_table4">
					<tbody>
							
							<tr>
								<th style="text-align:center;"><%=estore%> SMS MESSAGES</th>
							</tr>
							<tr>
								<td width="10"><img width="10" height="5" border="0" src="/media_stat/images/layout/clear.gif"></td>
							</tr>
							<tr>
								<td class="gc_table_footer " >
									<div align="right"><input type="button" onclick="lookupSmsMessages();" class="button" value="Refresh" name="Refresh Manifest"></div>
									<table width="99%" cellspacing="0" cellpadding="0" border="0" class="gc_table4footer">
									<tr class="gc_smsColHeader">
											<td width="117">Time Sent</td>
											<td width="122">Type</td>
											<td width="120">Message</td>
											<td width="117">Result</td>
											<td width="117">Mobile Number</td>
										</tr>
										<tr>
											<td colspan="5"><div id="smsInfo" style="overflow-y:auto;height:200px;"></div></td>
										</tr>
									</table>
								</td>
							</tr>
							
					</tbody>
				</table>
			</td>
		</tr>
	</tbody>
</table>
</div>

<style>
.list_odd_row {
    background-color: #EFEFEF;
}

iframe {
    border: 1px;
}

#ac_error
{
text-align:left;
font-weight:bold;
color:red;
}

#ac_info
{
text-align:left;
font-weight:bold;
color:blue;
}

.gc_table3footer .button {
    background-color: #000000;
    border: 1px solid #999999;
    color: #FFFFFF;
    font-size: 8pt;  
}

.gc_table_footer .button {
    background-color: #000000;
    border: 1px solid #999999;
    color: #FFFFFF;
    font-size: 8pt;  
}
</style>



</tmpl:put>

</tmpl:insert>
