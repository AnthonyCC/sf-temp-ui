<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassConstants"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassInfo"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassUsageLine"%>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassExtendReason"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassCancelReason"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassModel"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassStatus"%>


<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>

<script type="text/javascript">
	function openURL(inLocationURL) {

	    self.parent.location.href = inLocationURL;

	}

	function hideshow(obj, index,linkName){

		link = document.getElementById(linkName+index); 
		trowId = document.getElementById('trow'+index); 
		browId = document.getElementById('brow'+index); 
		
		if(navigator.appName.indexOf('Microsoft') != -1){

			if (obj.style.display=="block"){
				obj.style.display="none";
				trowId.style.backgroundColor="#FFFFFF";
				trowId.style.fontWeight="normal";
				browId.style.backgroundColor="#FFFFFF";
				if(linkName=="showLink") {
					link.innerText = "Expand to view details";
				}
				else {
					link.innerText = "Expand to cancel DeliveryPass";
				}
			}
			else {
				obj.style.display="block";
				trowId.style.backgroundColor="#FFFFCE";
				trowId.style.fontWeight="bold";
				browId.style.backgroundColor="#FFFFCE";
				link.innerText = "Hide details";
			}
		} else {
			if (obj.style.display=="block"){
				obj.style.display="none";
				trowId.style.backgroundColor="#FFFFFF";
				trowId.style.fontWeight="normal";
				browId.style.backgroundColor="#FFFFFF";
				if(linkName=="showLink") {
					link.innerText = "Expand to view details";
				}
				else {
					link.innerText = "Expand to cancel DeliveryPass";
				}
			}
			else {
				obj.style.display="block";
				trowId.style.backgroundColor="#FFFFCE";
				trowId.style.fontWeight="bold"
				browId.style.backgroundColor="#FFFFCE";
				link.firstChild.nodeValue = "Hide details";
			}
		}
	}
	function submitAction(counter,actionName) {
	    var form = document.forms['deliverypass_'+counter];
            form.elements['action_name'].value=actionName;
            form.elements['passNum'].value=counter;

            if(actionName == 'cancel_RTU_pass'){
            	if(form.orderAssigned.value == ''){
            		alert('Please select an order number involved.');
            		form.orderAssigned.focus();
            		return;
           		}
           		if(form.cancelReason.value == ''){
            		alert('Please select a reason code to cancel a DeliveryPass.');
            		form.cancelReason.focus();
            		return;
           		}
           		if(form.notes.value == ''){
				alert('Notes required');
				form.notes.focus();
				return;
           		}
            	var doCancel = confirm ("Are you sure you want to cancel this DeliveryPass?");
            	if(doCancel == false){
            		return;
			}            		
           	
            }
            form.notes.value=form.notes.value+". The refund amount is "+form.elements['refundAmount'].value;
            form.method='POST';
            form.submit();
            return false;
	}
	
</script>
<%
	FDUserI	user = (FDSessionUser) session.getAttribute(SessionName.USER);
	
%>

<% boolean editable = false; %>
<crm:GetLockedCase id="cm">
    <% if (cm!=null && cm.getCustomerPK()!=null) { 
        String erpCustId = cm.getCustomerPK().getId();
            if (user.getIdentity().getErpCustomerPK().equals(erpCustId)) {
                editable = true;
            } 
        } %>
</crm:GetLockedCase>
<% 
String case_required = "<span class=\"cust_module_content_edit\">-Case required to add/extend or cancel an Active DeliveryPass-</span>"; 
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr><td><br><b>History</b></td></tr>
<%
	//Retreive the usage info from delivery pass info.
	String identity = user.getIdentity().getErpCustomerPK();
	Map dlvPassInfoMap = (Map) session.getAttribute("DLVP" + identity);

	List history = (List)dlvPassInfoMap.get(DlvPassConstants.PASS_HISTORY);
	if(history == null){
	//No History info available
%>
	<tr><td colspan="7" align="center"><b><u>There were no previous history of DeliveryPasses for this customer.</u></b></td></tr>
<%
	} else {
%>	
<crm:CancelDPController passes="<%= history%>" actionName="" result='result'>
<tr class="list_header">
	<td><span class="table_header">Type</span></td>
	<td><span class="table_header">Purchased</span></td>
	<td><span class="table_header">Expired/Last Used</span></td>
	<td align="center"><span class="table_header"># Used</span></td>
	<td align="center"><span class="table_header"># Credited/Extended</span></td>
	<td><span class="table_header">Final Status</span></td>
	<td><span class="table_header">Used on Order #s</span></td>	
</tr>

<logic:iterate id="historyPass" collection="<%= history %>" type="com.freshdirect.deliverypass.DeliveryPassInfo" indexId="counter">
<tr ID="trow<%=counter%>">
	<td class="border_bottom" ><span class="detail_text"><%= historyPass.getName() %>&nbsp;</span></td>
	<td class="border_bottom">
		<span class="detail_text"><%= CCFormatter.defaultFormatDate(historyPass.getPurchaseDate()) %></span>&nbsp;&nbsp;&nbsp;#
		<a onclick="openURL('/main/order_details.jsp?orderId=<%= historyPass.getPurchaseOrderId()  %>')" href="#" style="color: #008800;font-size: 8pt;"><%= historyPass.getPurchaseOrderId() %></a></span>
	</td>
	<td class="border_bottom" >
		<%
		DlvPassUsageLine usageLine = historyPass.getLastUsedOrder();		
		if (historyPass.isUnlimited()){
			Date expDate = historyPass.getExpirationDate();
			String formattedExpDate = expDate != null ? CCFormatter.defaultFormatDate(expDate) : "";
			
		%>
		<span class="detail_text"><%= formattedExpDate %></span>&nbsp;&nbsp;
		<% } else { 

			String dlvDate = (usageLine != null) ? CCFormatter.defaultFormatDate(usageLine.getDeliveryDate()) : "";
		%>
		<span class="detail_text"><%= dlvDate %></span>&nbsp;&nbsp;
		<% } 
		if(usageLine != null) {
		%>
			# <a onclick="openURL('/main/order_details.jsp?orderId=<%= usageLine.getOrderIdUsedFor() %>')" href="#" style="color: #008800;font-size: 8pt;"><%= usageLine.getOrderIdUsedFor() %></a>
		
		<% } else { %>
			<font style="color: #008800;font-size: 8pt;">Never Used</font>	
		<% 
		}
		%>
		
		
	</td>
	<td class="border_bottom"  align="center"><span class="detail_text"><%= historyPass.getUsageCount() %></span></td>
	<td class="border_bottom"  align="center" height="15%"><span class="detail_text"><%= historyPass.isUnlimited() ? historyPass.getExtendedWeeks() : historyPass.getCreditCount() %></span></td>
	<td class="border_bottom" height="15%"><span class="detail_text"><%= historyPass.getStatusName() %></span></td>
	<td class="border_bottom">
		<a id="showLink<%=counter%>" href="javascript:hideshow(document.getElementById('passdetails<%=counter%>'), <%=counter%>,'showLink')" style="color: #008800;font-size: 8pt;">Expand to view details</a>
	</td>	
        <%  DeliveryPassModel dpModel=historyPass.getModel();
	    if((dpModel.getStatus()==EnumDlvPassStatus.READY_TO_USE)&&editable) {%>
		<td class="border_bottom">
			<a id="showLink1<%=counter%>" href="javascript:hideshow(document.getElementById('pass_details<%=counter%>'), <%=counter%>,'showLink1')" style="color: #008800;font-size: 8pt;">Expand to cancel DeliveryPass</a>
		</td>	
	  <%}%>
</tr>

	<tr ID="brow<%=counter%>">
		<td>&nbsp;</td>
		<td colspan="6">
		<DIV id="passdetails<%=counter%>" style="display: none">
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
		<tr>
		<%
			List usageLines = historyPass.getUsageLines();
			if(usageLines != null) {
			//If a delivery pass has usage details.
		%>
			<td width="18%" valign="top"><span class="info_text"><b>Usage Details</b></span><br>
				<table width="175" cellpadding="0" cellspacing="0" border="0">
					<tr class="usage_header">
					<td width="50%" class="border_bold"><span class="his_detail_text"><b>Date</b></span></td>	
					<td width="50%" class="border_bold"><span class="his_detail_text"><b>Order #</b></span></td>	
					</tr>
					<logic:iterate id="usage" collection="<%= usageLines %>" type="com.freshdirect.deliverypass.DlvPassUsageLine" >
					<tr>
					<td width="50%" class="border_bottom"><span class="his_detail_text"><%= CCFormatter.defaultFormatDate(usage.getDeliveryDate()) %></span></td>	
					<td width="50%" class="border_bottom"><a onclick="javascript:openURL('/main/order_details.jsp?orderId=<%= usage.getOrderIdUsedFor()  %>')" href="#" style="color: #008800;font-size: 8pt;"><%= usage.getOrderIdUsedFor()  %></a></td>	
					</tr>
					</logic:iterate>	
				</table>	
			</td>
		<%	
			} else {
			//If a delivery pass has no usage details.
		%>	
			<td align="center"><b>This DeliveryPass has no Orders applied.</b></td>
		<%
			}
		%>	
			<crm:GetDlvPassActivity deliveryPassId="<%= historyPass.getDlvPassId() %>" id="activities">

			<%
				if(activities == null || activities.size() == 0) {
				//No Activities Info	
			%>	
				<td valign="top"><span class="info_text"><b>Activity Log</b></span><br>
					<span class="error">This DeliveryPass currently has no Activities logged.</span>
				</td>
			<%
				} else {

			%>	
				<td valign="top"><span class="info_text"><b>Activity Log</b></span><br>
					<table width="900" cellpadding="0" cellspacing="0" border="0">
						<tr class="activity_header">
						<td valign="bottom" width="15%" class="border_bold"><span class="detail_text"><b>Date | Time</b></span></td>	
						<td class="border_bold">&nbsp;</td>
						<td valign="bottom" width="17%" class="border_bold"><span class="detail_text"><b>Action</b></span></td>	
						<td class="border_bold">&nbsp;</td>
						<td valign="bottom" width="13%" class="border_bold"><span class="detail_text"><b>By</b></span></td>	
						<td class="border_bold">&nbsp;</td>
						<td valign="bottom" width="13%"  align="center"  class="border_bold"><span class="detail_text"><b>Order # related</b></span></td>	
						<td class="border_bold">&nbsp;</td>
						<td valign="bottom" width="22%" class="border_bold"><span class="detail_text"><b>Reason</b></span></td>	
						<td class="border_bold">&nbsp;</td>
						<td valign="bottom" width="20%" class="border_bold"><span class="detail_text"><b>Notes</b></span></td>	
						</tr>
						<logic:iterate id="activity" collection="<%= activities %>" type="com.freshdirect.customer.ErpActivityRecord" indexId="counter1">
						<tr>
						<td class="border_bottom"><span class="detail_text"><%= CCFormatter.formatDateTime(activity.getDate()) %></span></td>	
						<td class="border_bottom">&nbsp;</td>
						<td class="border_bottom"><span class="detail_text"><%= activity.getActivityType().getName() %></span></td>
						<td class="border_bottom">&nbsp;</td>
						<td class="border_bottom"><span class="detail_text"><%= activity.getInitiator() %></span></td>
						<td class="border_bottom">&nbsp;</td>
						<td align="center"  class="border_bottom"><span class="detail_text"><a onclick="javascript:openURL('/main/order_details.jsp?orderId=<%= activity.getChangeOrderId()  %>')" href="#" style="color: #008800;font-size: 8pt;"><%= activity.getChangeOrderId()  %></a></span></td>
						<td class="border_bottom">&nbsp;</td>
						<td class="border_bottom">
							<span class="detail_text">
							<% 
								String reason = null;
								if(activity.getActivityType() == EnumAccountActivityType.CREDIT_DLV_PASS ||
									activity.getActivityType() == EnumAccountActivityType.EXTEND_DLV_PASS) {
									reason = EnumDlvPassExtendReason.getEnum(activity.getReason()).getDisplayName();
								} else if(activity.getActivityType() == EnumAccountActivityType.CANCEL_DLV_PASS) {
									reason = EnumDlvPassCancelReason.getEnum(activity.getReason()).getDisplayName();
								}
							%>	
							<%= reason %>
							</span>
						</td>
						<td class="border_bottom">&nbsp;</td>
						<td class="border_bottom"><span class="detail_text"><%= activity.getNote() %></span></td>
						</tr>
						</logic:iterate>	
					</table>
				</td>	
			<%

				}
			%>	
			</crm:GetDlvPassActivity>
		</tr>
		</table>
		</DIV>
		</td>
	
<td>


                    <DIV id="pass_details<%=counter%>" style="display: none">
						
						<fd:ErrorHandler result='<%=result%>' name='unauthorized_msg' id='errorMsg'>
						   <%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>

			<form name="deliverypass_<%=counter%>" method="POST">
			<input type="hidden" name="action_name" value="cancelPass">
			<input type="hidden" name="passNum" value="">
			

							<table width="100%" cellpadding="2" cellspacing="2" border="0">
							<%
								if(editable) {
							%>
							<tr>
								<td colspan="2">	
									<% Double refundAmt = new Double(DeliveryPassUtil.getPricePaid(historyPass)); %>
									<input type="hidden" name="refundAmount" value=<%=JspMethods.formatPrice(refundAmt.doubleValue())%>>
									<span class="info_text">Current refund amount: <b><%= JspMethods.formatPrice(refundAmt.doubleValue()) %></b> <br>(includes refundable tax)</span>
								</td>
							</tr>	

							<tr>
								<td colspan="2">	
									<select name="orderAssigned" class="combo_text" style="width: 210px;">
										<option value="">Select the order involved</option>
										
										<option value="<%= historyPass.getPurchaseOrderId() %>"><%= historyPass.getPurchaseOrderId() %> *</option>
										
										<crm:GetRecentOrdersByDlvPass id="recentOrders" deliveryPassId="<%= historyPass.getDlvPassId() %>">
										<logic:iterate id="usage" collection="<%= recentOrders %>" type="com.freshdirect.deliverypass.DlvPassUsageLine">
										<%
											if(!usage.getOrderIdUsedFor().equals(historyPass.getPurchaseOrderId())) {
											//Do not add the original purchase order id here.
										%>											
											<option value="<%= usage.getOrderIdUsedFor() %>"><%= usage.getOrderIdUsedFor() %> - <%= usage.getOrderStatusUsedFor().getDisplayName() %> - <%= CCFormatter.defaultFormatDate(usage.getDeliveryDate()) %></option>
										<%
											}
										%>	
										</logic:iterate>
										</crm:GetRecentOrdersByDlvPass>
										
									</select>
								</td>
							</tr>	
							<tr>
								<td>	
									<select name="cancelReason" class="cancel_combo_text" style="width: 210px;">
										<option>Select a reason to cancel</option>
										<logic:iterate id="cancelReason" collection="<%= EnumDlvPassCancelReason.getEnumList() %>" type="com.freshdirect.deliverypass.EnumDlvPassCancelReason">
											<option value="<%= cancelReason.getName() %>"><%= cancelReason.getDisplayName() %></option>
										</logic:iterate>

									</select>
								</td>
								<td>
									<a href="javascript:submitAction(<%=counter%>,'cancel_RTU_pass')" style="color: #CC0000;font-size: 8pt;">Cancel DeliveryPass</a>
								</td>
							</tr>	
							<tr>
								<td colspan="2">	
									<span class="info_text">Enter notes (required):</span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<textarea name="notes" rows="2" wrap="VIRTUAL" style="width: 330px;"></textarea>
								</td>
							</tr>	
							
							<% } else { %>	
								<tr>
									<td colspan="2" align="center">	
										<%= case_required %>
									</td>
								</tr>	

							<% } %>
							</Form>
							</table>
							</DIV>

</td>
</tr>

</logic:iterate>
</crm:CancelDPController>
<%
	}
%>	
</table>
	
<%
	String successMsg = request.getParameter("successMsg");
	if(request.getMethod().equals("GET")&&successMsg != null){

%>
	<script language="JavaScript">
		alert('<%= successMsg %>');
	</script>
<% } %>	
