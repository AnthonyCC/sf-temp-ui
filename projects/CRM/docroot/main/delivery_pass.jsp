<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassConstants"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassInfo"%>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassExtendReason"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassCancelReason"%>
<%@ page import='com.freshdirect.deliverypass.EnumDlvPassStatus' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.deliverypass.EnumDPAutoRenewalType' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo' %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>


<%@page import="com.freshdirect.webapp.util.JspMethods"%><script language="javascript">
    	function flipAutoRenewalON() {
	    var form = document.forms['autoRenew'];
	    form.elements['action'].value='FLIP_AUTORENEW_ON';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}
    	function flipAutoRenewalOFF() {
	    var form = document.forms['autoRenew'];
	    form.elements['action'].value='FLIP_AUTORENEW_OFF';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}

	function submitAction(actionName) {
	    var form = document.forms['deliverypass'];
            form.elements['action_name'].value=actionName;
            if(actionName == 'incr_expperiod' || actionName == 'incr_dlvcount'){
            	if(form.orderAssigned.value == ''){
            		alert('Please select an order number involved.');
            		form.orderAssigned.focus();
            		return;
            	}
            	if(form.extendReason.value == ''){
            		alert('Please select a reason code to add or extend a DeliveryPass.');
            		form.extendReason.focus();
            		return;
            	}
			if(actionName == 'incr_expperiod' && form.incrCount.value=='') {
	            	alert('Please select the number of weeks to extend.');
	            	form.incrCount.focus();
      	      	return;

			}
			else if(form.incrCount.value==''){
	            	alert('Please select the number of credits.');
      	      	form.incrCount.focus();
	            	return;
                  }
            	if(form.notes.value == ''){
			alert('Notes required');
			form.notes.focus();
			return;
            	}
            }
            if(actionName == 'cancel_pass'){
            	var doCancel = confirm ('Are you sure you want to cancel this DeliveryPass?');
            	if(doCancel == false){
            		return;
		}            		
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
		form.notes.value=form.notes.value+". The refund amount is "+form.elements['refundAmount'].value;
            	
            }
            form.method='POST';
		
            form.submit();
            return false;

	}
    	function redirectToSignup() {
	    var form = document.forms['signup'];
	    form.elements['action'].value='signup';
	    form.method='POST';
	    form.submit();
	    return false;
    	
    	}
</script>

<%
	FDUserI	user = (FDSessionUser) session.getAttribute(SessionName.USER);
	boolean hasCustomerCase = CrmSession.hasCustomerCase(session);
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
String caseRequiredForRenewal = "<span class=\"cust_module_content_edit\">-Case required to change renewal settings-</span>"; 
String caseRequiredForManualRenewal = "<span class=\"cust_module_content_edit\">-Case required to place auto-renewal order manually-</span>"; 
%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>DeliveryPass</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id="currentAgent">
	<fd:DlvPassSignupController result="result" callCenter="true">
		<fd:ErrorHandler result='<%=result%>' name='dlvpass_discontinued' id='errorMsg'>
		   <%@ include file="/includes/i_error_messages.jspf" %>   
		</fd:ErrorHandler>
	
		<fd:GetDeliveryPasses id="dlvPassInfoMap">
		
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="content_fixed">	
			<%
				Object obj = dlvPassInfoMap.get(DlvPassConstants.ACTIVE_ITEM);
				if(obj == null) {
				//There is no active item. 
			%>
				<tr>
				<td width="28%"><b>Customer has </b><span class="error">NO</span> <b>DeliveryPass</b></td>
				<td width="44%">
				<%
					EnumDlvPassStatus status = user.getDeliveryPassStatus();
					if(user.isEligibleForDeliveryPass() && DeliveryPassUtil.isEligibleStatus(status)) {
				%>	
				<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){%>
				<form name="signup" method="POST">
				<input type="hidden" name="action" value="">
					<a href="#" onClick="javascript:redirectToSignup()"><span class="cust_header_field"><b>Buy DeliveryPass</b></span></a>, deliverable item required.
				</form>
				<% } %>
				<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){%>
				<% if( (user.getDlvPassInfo().getAutoRenewUsablePassCount()==0) &&
				     user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.YES) ) {
				     if(hasCustomerCase) {
				 %> 
				 <a href="/main/place_auto_renew_order.jsp"><b>Click here to buy Auto Renew FreshDirect Unlimited.<b></a>
				 <%  } else {%>
				 <%=caseRequiredForManualRenewal%>
				 <%  }
				   } }
				 %>  
                
				<%
				} else {
				%>
				Customer is not eligible for DeliveryPass.
				<%}%>
				</td>
				<td width="28%">
					<form name="autoRenew" method="POST">
						<input type="hidden" name="action" value="">

						<% if(user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.YES) && (user.getDlvPassInfo().getAutoRenewUsablePassCount()>0)) {%>
							<table border="0"><tr><td bgcolor=#00FF00><b>Auto-Renewal option: ON </b></td>
							<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
							    <%if(editable){%> 
							    	<td><A HREF="#" onClick="javascript:flipAutoRenewalOFF()"><font class="text12bold">Click here to turn off renewal.</A></td>
							    <%} else {%> 
							    	<td><%=caseRequiredForRenewal%></td>
							    <%}%>
							    <% } %>
							    </tr>
							</table>                            
						<%} else if(user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.NO) && (user.getDlvPassInfo().getAutoRenewUsablePassCount()>0)) {%>
							<table border="0"><tr><td bgcolor=#FF0000><b>Auto-Renewal option: OFF</b></td>
							<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
								<%if(editable){%> 
									<td><A HREF="#" onClick="javascript:flipAutoRenewalON()"><font class="text12bold">Click here to turn renewal ON.</A></td>
								<%} else {%> 
							    		<td><%=caseRequiredForRenewal%></td>
								<%}%>
								<% } %>
								</tr>
							</table>
						<%}%>
					</form>
                        </td>  
				</tr>
			<%	
				} else {
                   %>
                             <% if(user.isEligibleForDeliveryPass() && (user.getUsableDeliveryPassCount()<FDStoreProperties.getMaxDlvPassPurchaseLimit())) {
                              %>
                              <% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){%>
				<form name="signup" method="POST">
				<input type="hidden" name="action" value="">
					<a href="#" onClick="javascript:redirectToSignup()"><span class="cust_header_field"><b>Buy DeliveryPass</b></span></a>, deliverable item required.
				</form>
				<% } %>
				     <% }%>
				     <tr>
				     
					<form name="autoRenew" method="POST">
						<input type="hidden" name="action" value="">

						<% if(user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.YES)&& (user.getDlvPassInfo().getAutoRenewUsablePassCount()>0))  {%>
						<td width="40%">
							<table border="0"><tr><td bgcolor=#00FF00><b>Auto-Renewal option: ON </b></td>
							<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
							    <%if(editable){%> 
							    	<td><A HREF="#" onClick="javascript:flipAutoRenewalOFF()"><font class="text12bold">Click here to turn off renewal.</A></td>
							    <%} else {%> 
							    	<td><%=caseRequiredForRenewal%></td>
							    <%}%>
							    <% } %>
							    </tr>
							</table>
							
					        </td>
						<%} else if(user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.NO)&& (user.getDlvPassInfo().getAutoRenewUsablePassCount()>0)) {%>
						<td width="40%">
							<table border="0"><tr><td bgcolor=#FF0000><b>Auto-Renewal option: OFF</b></td>
							<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
								<%if(editable){%> 
									<td><A HREF="#" onClick="javascript:flipAutoRenewalON()"><font class="text12bold">Click here to turn renewal ON.</A></td>
								<%} else {%> 
							    		<td><%=caseRequiredForRenewal%></td>
								<%}%>
								<% } %>
								</tr>
							</table>	
						</td>	
						<%}%>
				   
				   </tr>
				   <% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
                        				<% if( (user.getDlvPassInfo().getAutoRenewUsablePassCount()==0) &&
							     user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.YES) ) {
							     if(hasCustomerCase) {
							 %> 
							 <a href="/main/place_auto_renew_order.jsp"><b>Click here to buy Auto Renew FreshDirect Unlimited.<b></a>
							 <%  } else {%>
							 <%=caseRequiredForManualRenewal%>
							 <%  }
							   } }
							 %>  

					</form>
						
					
					<% //Contain an active item.
					DeliveryPassInfo activeItem = (DeliveryPassInfo) obj;
					String actionName = request.getParameter("action_name");
					if (actionName == null) {
						if(activeItem.isUnlimited())
							actionName = "extend_week";
						else 
							actionName = "extend_delivery";
					} 
					
			%>
					<tr>
					<td width="28%" valign="top">
							
			<%					
					if(activeItem.isUnlimited()) {
					//UNLIMITED PASS
					Date expDate = activeItem.getExpirationDate();
					Map monthAndDays =DeliveryPassUtil.getRemainingMonthsAndDays(expDate);
			%>
						<crm:DeliveryPassController dlvPass="<%= activeItem.getModel() %>" actionName="<%=actionName%>" noOfWeeks= "1" result='result'>
						<fd:ErrorHandler result='<%=result%>' name='unauthorized_msg' id='errorMsg'>
						   <%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<table width="100%" cellpadding="2" cellspacing="2" border="0" class="<%= activeItem.getStatusName() %>_pass">

							<tr bgcolor="#FFFFFF">
								<td colspan="2">
									<span class="dlv_pass_header"><b>Customer has </b><span class="dlv_pass_name"><%= activeItem.getName() %></span><b> DeliveryPass</b></span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span class="info_text">Expiration Date: <b><%= CCFormatter.defaultFormatDate(expDate) %></b></span>	
								</td>
							</tr>
							<tr>
								<td width="65%">
									<span class="info_text">Remaining days: <b><%= monthAndDays.get("MONTHS") %></b> months <b><%= monthAndDays.get("DAYS") %></b> days</span>
								</td>
								<td>
									<span class="info_text">Status: <b><%= activeItem.getStatusName() %></b>
								</td>	
							</tr>
							<tr>
								<td>
									<span class="info_text">Used: <b><%= activeItem.getUsageCount() %></b> times</span>
								</td>
							
								<td>
									<span class="info_text">Extended: <b><%= activeItem.getExtendedWeeks() %></b> weeks </span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span class="info_text">Purchased: <b><%= CCFormatter.defaultFormatDate(activeItem.getPurchaseDate()) %></b> order #: <a href="/main/order_details.jsp?orderId=<%= activeItem.getPurchaseOrderId() %>" style="color: #008800;font-size: 8pt;"><%= activeItem.getPurchaseOrderId() %></a></span>
								</td>
							</tr>
							<tr>
								<td colspan="2">	
									<% Double refundAmt = (Double) dlvPassInfoMap.get(DlvPassConstants.REFUND_AMOUNT); %>
									<span class="info_text">Current refund amount: <b><%= JspMethods.formatPrice(refundAmt.doubleValue()) %></b> <br>(includes refundable tax)</span>
								</td>
							</tr>	
							<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){ %>
							<%
								if(editable && user.isDlvPassActive()) {
							%>
							<form name="deliverypass" method="POST">
							<input type="hidden" name="action_name" value="extend_week">
							<input type="hidden" name="refundAmount" value=<%=JspMethods.formatPrice(refundAmt.doubleValue())%>>
							<tr>
								<td colspan="2">	
									<select name="orderAssigned" class="combo_text" style="width: 210px;">
										<option value="">Select the order involved</option>
										
										<option value="<%= activeItem.getPurchaseOrderId() %>"><%= activeItem.getPurchaseOrderId() %> *</option>
										
										<crm:GetRecentOrdersByDlvPass id="recentOrders" deliveryPassId="<%= activeItem.getDlvPassId() %>">
										<logic:iterate id="usage" collection="<%= recentOrders %>" type="com.freshdirect.deliverypass.DlvPassUsageLine">
										<%
											if(!usage.getOrderIdUsedFor().equals(activeItem.getPurchaseOrderId())) {
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
								<td colspan="2">	
									<span class="info_text">then</span>
								</td>
							</tr>	
							<tr>
								<td>	
									<select name="extendReason" class="extend_combo_text" style="width: 210px;">
										<option value="">Select a reason to extend</option>
										<logic:iterate id="extendReason" collection="<%= EnumDlvPassExtendReason.getEnumList() %>" type="com.freshdirect.deliverypass.EnumDlvPassExtendReason">
											<option value="<%= extendReason.getName() %>"><%= extendReason.getDisplayName() %></option>
										</logic:iterate>
									</select>
								</td>
                                       
								<td>	
  									<select name="incrCount" class="extend_combo_text" style="width: 210px;">
										<option value="">Select number of weeks</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
									</select>
								</td>

							</tr>	
							<tr>
								<td>
									<a href="javascript:submitAction('incr_expperiod')" style="color: #008800;font-size: 8pt;">Extend DeliveryPass</a>
								</td>
							</tr>

							<tr>
								<td colspan="2">	
									<span class="info_text">or</span>
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
									<a href="javascript:submitAction('cancel_pass')" style="color: #CC0000;font-size: 8pt;">Cancel DeliveryPass</a>
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
							</form>
							
							<% } else { %>	
								<tr>
									<td colspan="2" align="center">	
										<%= case_required %>
									</td>
								</tr>	

							<% } %>
							<% } %>	
						</table>
						</crm:DeliveryPassController>	
				<%
					} else {
					//BSGS PASS
				%>	
						<crm:DeliveryPassController dlvPass="<%= activeItem.getModel() %>" actionName="<%=actionName%>" result='result' increment="1" >
						<fd:ErrorHandler result='<%=result%>' name='unauthorized_msg' id='errorMsg'>
						   <%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						
						<table width="100%" cellpadding="2" cellspacing="2" border="0" class="<%= activeItem.getStatusName() %>_pass">
				
							<tr bgcolor="#FFFFFF">
								<td colspan="2">
									<span class="dlv_pass_header"><b>Customer has </b><span class="dlv_pass_name"><%= activeItem.getName() %></span><b> DeliveryPass</b></span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span class="info_text">Remaining deliveries: <b><%= activeItem.getRemainingDlvs() %></b></span>
									&nbsp;&nbsp;&nbsp;	

									<span class="info_text">Status: <b><%= activeItem.getStatusName() %></b>
								</td>	
							</tr>
							<tr>
								<td width="40%">
									<span class="info_text" >Used: <b><%= activeItem.getUsageCount() %></b> times</span>
								</td>
							
								<td width="60%">
									<span class="info_text" >Credited: <b><%= activeItem.getCreditCount() %></b> deliveries</span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span class="info_text">Purchased: <b><%= activeItem.getTotalDlvs() %></b> deliveries <b><%= CCFormatter.defaultFormatDate(activeItem.getPurchaseDate()) %></b> order #: <a href="/main/order_details.jsp?orderId=<%= activeItem.getPurchaseOrderId() %>" ><span class="pur_order_number"><%= activeItem.getPurchaseOrderId() %></span></a></span>
								</td>
							</tr>
							<tr>
								<td colspan="2">	
									<% Double refundAmt = (Double) dlvPassInfoMap.get(DlvPassConstants.REFUND_AMOUNT); %>
									<span class="info_text">Current refund amount: <b><%= JspMethods.formatPrice(refundAmt.doubleValue()) %></b> <br>(includes refundable tax)</span>

								</td>
							</tr>	
							<%
								if(editable &&  user.isDlvPassActive()) {
							%>
							<form name="deliverypass" method="POST">
							<input type="hidden" name="action_name" value="extend_delivery">

							<tr>
								<td colspan="2">	
									<select name="orderAssigned" class="combo_text" style="width: 210px;">
										<option value="">Select the order involved</option>
										
										<option value="<%= activeItem.getPurchaseOrderId() %>"><%= activeItem.getPurchaseOrderId() %> *</option>
										
										<crm:GetRecentOrdersByDlvPass id="recentOrders" deliveryPassId="<%= activeItem.getDlvPassId() %>">
										<logic:iterate id="usage" collection="<%= recentOrders %>" type="com.freshdirect.deliverypass.DlvPassUsageLine">
										<%
											if(!usage.getOrderIdUsedFor().equals(activeItem.getPurchaseOrderId())) {
											//Do not add the original purchase order id here.
										%>											
											<option value="<%= usage.getOrderIdUsedFor() %>"><%= usage.getOrderIdUsedFor() %> - <%= usage.getOrderStatusUsedFor() %> - <%= CCFormatter.defaultFormatDate(usage.getDeliveryDate()) %></option>
										<%
											}
										%>	
										</logic:iterate>
										</crm:GetRecentOrdersByDlvPass>
										
									</select>
								</td>
							</tr>	
							<tr>
								<td colspan="2">	
									<span class="info_text">then</span>
								</td>
							</tr>	
							<tr>
								<td colspan="2">	
									<select name="extendReason" class="extend_combo_text" style="width: 210px;">
										<option value="">Select a reason to add</option>
										<logic:iterate id="extendReason" collection="<%= EnumDlvPassExtendReason.getEnumList() %>" type="com.freshdirect.deliverypass.EnumDlvPassExtendReason">
											<option value="<%= extendReason.getName() %>"><%= extendReason.getDisplayName() %></option>
										</logic:iterate>

									</select>&nbsp;&nbsp;
								</td>
								<td>	
  									<select name="incrCount" class="extend_combo_text" style="width: 210px;">
										<option value="">Select number of credits</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
									</select>
								</td>

							</tr>	
							<tr>
								<td>
									<a href="javascript:submitAction('incr_dlvcount')" style="color: #008800;font-size: 8pt;">Credit DeliveryPass</a>
								</td>
							</tr>

							<tr>
								<td colspan="2">	
									<span class="info_text">or</span>
								</td>
							</tr>
							<tr>
								<td colspan="2">	
									<select name="cancelReason" class="cancel_combo_text" style="width: 210px;">
										<option value="">Select a reason to cancel</option>
										<logic:iterate id="cancelReason" collection="<%= EnumDlvPassCancelReason.getEnumList() %>" type="com.freshdirect.deliverypass.EnumDlvPassCancelReason">
											<option value="<%= cancelReason.getName() %>"><%= cancelReason.getDisplayName() %></option>
										</logic:iterate>
									</select>&nbsp;&nbsp;
									<a href="javascript:submitAction('cancel_pass')" style="color: #CC0000;font-size: 8pt;">Cancel DeliveryPass</a>
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
							</form>
							<% } else { %>		
							<tr>
								<td colspan="2" align="center">	
									<%= case_required %>
								</td>
							</tr>	
							
							<% } %>
						</table>
						</crm:DeliveryPassController>	
				
				<%	
					}
				%>	
				 
					</td>
					<td width="72%" valign="top">
						<iframe id="active_pass_usage" name="active_pass_usage" src="/includes/deliverypass/active_pass_usage.jsp" width="100%" height="350" scrolling="auto" FrameBorder="0"></iframe>
					</td>
					
					</tr>
				
			<%	
				}
			%>	
			</table>
			<%-- <%@ include file="/includes/deliverypass/pass_history_usage.jspf" %>  --%>
			<iframe id="history_pass_usage" name="history_pass_usage" src="/includes/deliverypass/pass_history_usage.jsp" width="100%" height="280" scrolling="auto" FrameBorder="0"></iframe>
		</fd:GetDeliveryPasses>
	</fd:DlvPassSignupController>
	</crm:GetCurrentAgent>
	</tmpl:put>

</tmpl:insert>

<%
	String successMsg = request.getParameter("successMsg");
	if(request.getMethod().equals("GET")&&successMsg != null){

%>
	<script language="JavaScript">
		alert('<%= successMsg %>');
	</script>
<% } %>	

