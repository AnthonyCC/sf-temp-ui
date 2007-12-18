<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ include file="/includes/i_order_comparators.jspf"%>

<tmpl:insert template='/template/top_nav.jsp'>

<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDIdentity identity = user.getIdentity();
    // Get return page value
	String successPage = request.getParameter("successPage") != null ? request.getParameter("successPage") : "/main/account_details.jsp";
%>

<fd:PromoEligible actionName='<%= request.getParameter("action") %>' result='result' successPage='<%= successPage %>'>

<fd:GetCustomerObjs identity="<%=identity%>" fdCustomerId="fdCustomer" erpCustomerId="erpCustomer" erpCustomerInfoId="erpCustomerInfo">
<%	
    // Get user's payment methods
	List paymentMethods = erpCustomer.getPaymentMethods();
	// Get user's shipping addresses
	List shipAddresses = erpCustomer.getShipToAddresses();
	
	// allow or deny?
	String action = "deny_promo";
	String titleName = "Remove";
	if (!fdCustomer.isEligibleForSignupPromo()) {
	    action = "allow_promo";
	    titleName = "Restore";
	}
	
%>

<tmpl:put name='title' direct='true'>Account Details > Promotion Eligibility</tmpl:put>

<tmpl:put name='content' direct='true'>

<div class="sub_nav" style="padding: 0px;">
<table width="100%" cellpadding="5" cellspacing="0" border="0">
	<form name="restore_promo" ACTION="promo_eligibility.jsp?successPage=<%=successPage%>" METHOD="post">
	<input type="hidden" name="action" value="<%= action %>">
	<tr>
		<td width="33%" class="sub_nav_title"><%= titleName %> Promotion Eligibility</td>
		<td><input type="submit" class="submit" value="<%= titleName.toUpperCase() %> PROMOTION ELIGIBILITY"></td>
		<td><a href="<%= successPage %>" class="cancel">CANCEL</a></td>
		<td width="33%" align="right"><fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
</table>
</div>
<div class="content_scroll" style="padding-top: 4px;">
	<div class="cust_module" style="width: 33%; margin-right: 6px;">
		<div class="cust_module_header">Name & Contact Info</div>
		<div class="cust_module_content">
			<table width="90%" cellpadding="3" cellspacing="0" class="cust_module_content_text" align="center">
				<tr>
					<td align="right">Name:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getTitle()%>&nbsp;<%=erpCustomerInfo.getFirstName()%>&nbsp;<%=erpCustomerInfo.getMiddleName()%>&nbsp;<%=erpCustomerInfo.getLastName()%></td>
				</tr>
				<tr>
					<td align="right">Home #:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getHomePhone()!=null?erpCustomerInfo.getHomePhone().getPhone():""%></td>
				</tr>
				<tr>
					<td align="right">Work #:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getBusinessPhone()!=null?erpCustomerInfo.getBusinessPhone().getPhone():""%></td>
				</tr>
				<tr>
					<td align="right">Cell #:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getCellPhone()!=null?erpCustomerInfo.getCellPhone().getPhone():""%></td>
				</tr>
				<tr>
					<td align="right">Alt. Email:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getAlternateEmail()%></td>
				</tr>
				<tr>
					<td align="right">Dept/Division:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getWorkDepartment()%></td>
				</tr>
				<tr>
					<td align="right">Employee Id:&nbsp;&nbsp;</td>
					<td><%=erpCustomerInfo.getEmployeeId()%></td>
				</tr>
			</table>
		</div>
		<%--div class="cust_module_footer">Last Modified: 05.24.2003 by Customer</div--%>
	</div>
<br clear="all">
<div class="cust_full_module_header">
&nbsp;&nbsp;Delivery Addresses
</div>
<%	int cellCounter = 0;
	boolean showAddressButtons = false;
	boolean showDeleteButtons = false;
	boolean showCCButtons = false; %>
	
	<logic:iterate id="dlvAddress" collection="<%= shipAddresses %>" type="com.freshdirect.customer.ErpAddressModel" indexId="addressCounter">
		<div class="cust_inner_module" style="width: 33%;<%=addressCounter.intValue() < 3 ?"border-top: none;":""%>">
				<div class="cust_module_content">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" class="cust_module_content_text">
		<tr valign="top">
		<td width="32%">
		<%=addressCounter.intValue() +1 %>
		<%  boolean isDepotLocation = (dlvAddress instanceof ErpDepotAddressModel); %>
		<fd:AltAddressInfo altDeliveryInfo="<%= dlvAddress.getInstructions() %>">
			<table width="100%" cellpadding="2" cellspacing="0" border="0" class="cust_module_content_text">
				<tr>
					<td width="35%">First Name&nbsp;</td>
					<td width="65%"><%= dlvAddress.getFirstName() %></td>
				</tr>
				<tr>
					<td width="35%">Last Name&nbsp;</td>
					<td width="65%"><%= dlvAddress.getLastName() %></td>
				</tr>
            <%  if (isDepotLocation) {  
                    ErpDepotAddressModel dAddr = (ErpDepotAddressModel) dlvAddress;  %>
                <tr>
					<td width="35%">Facility&nbsp;</td>
					<td width="65%"><%= dAddr.getFacility() %></td>
				</tr>
			<%  } %>
				<tr>
					<td width="35%">Address&nbsp;</td>
					<td width="65%"><%= dlvAddress.getAddress1() %></td>
				</tr>
            <%  if ((dlvAddress.getAddress2() != null) && !"".equals(dlvAddress.getAddress2().trim())) { %>
				<tr>
					<td width="35%">Addr. Line 2&nbsp;</td>
					<td width="65%"><%= dlvAddress.getAddress2() %></td>
				</tr>
            <%  }   
                if (!isDepotLocation) { %>
				<tr>
					<td width="35%">Apt./Floor&nbsp;</td>
					<td width="65%"><%= dlvAddress.getApartment() %></td>
				</tr>
            <%  }   %>
				<tr>
					<td width="35%">City&nbsp;</td>
					<td width="65%"><%= dlvAddress.getCity() %></td>
				</tr>
				<tr>
					<td width="35%">State&nbsp;</td>
					<td width="65%"><%= dlvAddress.getState() %></td>
				</tr>
				<tr>
					<td width="35%">Zip&nbsp;</td>
					<td width="65%"><%= dlvAddress.getZipCode() %></td>
				</tr>
            <%  if (isDepotLocation) {  
                    ErpDepotAddressModel dAddr = (ErpDepotAddressModel) dlvAddress;  %>
                <tr>
					<td width="35%">Instructions&nbsp;</td>
					<td width="65%"><%= dAddr.getInstructions() %></td>
				</tr>
			<%  }
                if (!"".equals(fldDlvInstructions)) {   %>	
				<tr>
					<td width="35%">Special instructions&nbsp;</td>
					<td width="65%"><%=fldDlvInstructions%></td>
				</tr>
			<%  }   %>
			</table>

		<% 	int orderCount = user.getAdjustedValidOrderCount();
			if ((orderCount < AddressName.DLV_ALTERNATE_THRESHHOLD) && !isDepotLocation) {
				if (leaveWithDoorman.booleanValue()) { %>
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_module_content_text">
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<B>Alternate Delivery</B><BR><FONT CLASS="space4pix"><BR></FONT></td>
						</tr>
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;Leave package with doorman</td>
						</tr>
					</table>
		<%		}
			}
			if ((orderCount >= AddressName.DLV_ALTERNATE_THRESHHOLD) && !isDepotLocation) {
			
				if (dlvAddress.getAltDelivery() != null && dlvAddress.getAltDelivery().getId() > 0) { %>
					<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_module_content_text">
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<B>Alternate Delivery</B><BR><FONT CLASS="space4pix"><BR></FONT></td>
						</tr>
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<%= dlvAddress.getAltDelivery().getName()%></td>
						</tr>
		<%           if("NEIGHBOR".equalsIgnoreCase(dlvAddress.getAltDelivery().getDeliveryCode())) {%>
		
						<tr>
							<td width="35%" align="right">First Name&nbsp;</td>
							<td width="65%"><%=dlvAddress.getAltFirstName()%></td>
						</tr>
						<tr>
							<td width="35%" align="right">Last Name&nbsp;</td>
							<td width="65%"><%=dlvAddress.getAltLastName()%></td>
						</tr>
						<tr>
							<td width="35%" align="right">Apartment #&nbsp;</td>
							<td width="65%"><%=dlvAddress.getAltApartment()%></td>
						</tr>
						<tr>
							<td width="35%" align="right">Contact #&nbsp;</td>
							<td width="65%"><%=dlvAddress.getAltPhone().getPhone()%> <%if( !"".equals(dlvAddress.getAltPhone().getExtension()) ){%>Ext. <%= dlvAddress.getAltPhone().getExtension() %> <%}%></td>
						</tr>
		
					<%}%>
					</table>
					<br>
				<%}%>				
		
				<%if(dlvAddress.getInstructions()!= null && !dlvAddress.getInstructions().equals("") && !"NONE".equalsIgnoreCase(dlvAddress.getInstructions())){%>	
					<table width="100%" cellpadding="0" cellspacing="0" border="0" align="CENTER" class="cust_module_content_text">
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<B>Special delivery instructions:</B><BR><FONT CLASS="space4pix"><BR></FONT></td>
						</tr>
						<tr>
							<td align="left" colspan="2">&nbsp;&nbsp;&nbsp;<%=dlvAddress.getInstructions()%></td>
						</tr>
					</table>
		<%          }
		 	} %>
		</fd:AltAddressInfo>
		</td></tr>
		</table>
	</div>
		</div>
		<%if(addressCounter.intValue() != 0 && (addressCounter.intValue() + 1) % 3 == 0 && ((addressCounter.intValue() + 1) < shipAddresses.size())){%>
			<br clear="all">
		<%}%>
	</logic:iterate>
	<br clear="all">
<div class="cust_full_module_header">
&nbsp;&nbsp;Payment Information
</div>

	<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
	<div class="cust_inner_module" style="width: 33%;<%=ccCounter.intValue() < 3 ?"border-top: none;":""%>">
		<div class="cust_module_content">
		<table width="100%" cellpadding="2" cellspacing="0" border="0" class="cust_module_content_text">
		<tr>
		<td width="32%">
		<%=ccCounter.intValue() + 1%>
			<%@ include file="/includes/i_payment_select.jspf"%>
		</td>
		</tr>
		</table>
	</div>
	</div>
		<%if(ccCounter.intValue() != 0 && (ccCounter.intValue() + 1) % 3 == 0 && ((ccCounter.intValue() + 1) < paymentMethods.size())){%>
			<br clear="all">
		<%}%>
	</logic:iterate>
	</form>
</div>
</tmpl:put>

</fd:GetCustomerObjs>

</fd:PromoEligible>

</tmpl:insert>


