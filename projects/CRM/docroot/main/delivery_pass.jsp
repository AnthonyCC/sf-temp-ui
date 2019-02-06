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
 <%@ page import="com.freshdirect.crm.CrmAgentRole"%>

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
            	if(form.elements['refundAmount']){
					form.notes.value=form.notes.value+". The refund amount is "+form.elements['refundAmount'].value;
            	}
            	
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
	
boolean hasCustomerCase= false;
	if(CrmAgentRole.COS_CODE.equalsIgnoreCase(CrmSession.getCurrentAgent(session).getRole().getCode()))
   		hasCustomerCase=true;
	else
  	 hasCustomerCase = CrmSession.hasCustomerCase(session); 

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
					<% if(hasCustomerCase) { %>
						<a href="/main/masquerade.jsp?destination=dp_search_results" target="_blank"><span class="cust_header_field"><b>Buy DeliveryPass</b></span></a>, deliverable item required.
					<% } else { %>
						<span class="cust_module_content_edit">-Case required to buy DeliveryPass-</span>
					<% } %>
				<% } %>
				<% if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"buyDeliveryPass")){%>
					<% if( (user.getDlvPassInfo().getAutoRenewUsablePassCount()==0) &&
						user.hasAutoRenewDP().equals(EnumDPAutoRenewalType.YES) ) {
						    if(hasCustomerCase) { %> 
						 		<a href="/main/place_auto_renew_order.jsp"><b>Click here to buy Auto Renew FreshDirect Unlimited.<b></a>
						 	<%  } else {%>
						 		<%=caseRequiredForManualRenewal%>
						 	<% }
						}
					}
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
								<tr><td colspan="2"><% if(hasCustomerCase) { %>
									<a href="/main/masquerade.jsp?destination=dp_search_results" target="_blank"><span class="cust_header_field"><b>Buy DeliveryPass</b></span></a>, deliverable item required.
								<% } else { %>
									<span class="cust_header_field"><b>Buy DeliveryPass</b></span></a>, deliverable item required. <span class="cust_module_content_edit">-Case required to buy DeliveryPass-</span>
								<% } %></td></tr>
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
						
				<%
					} else {
					//BSGS PASS
				%>	
						
				
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

