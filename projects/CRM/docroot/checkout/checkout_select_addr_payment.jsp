<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import="com.freshdirect.fdstore.FDDepotManager"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderHistory" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<fd:CheckLoginStatus guestAllowed="false" redirectPage="/registration/nw_cst_check_zone.jsp" />

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Select Delivery Address & Payment Method</tmpl:put>

<%  FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    FDIdentity identity = user.getIdentity();%>
<fd:GetCustomerObjs identity='<%= identity %>' fdCustomerId="fdCustomer" erpCustomerId="erpCustomer" erpCustomerInfoId="erpCustomerInfo">

<%  
	// Get user's shipping addresses
	List shipAddresses = erpCustomer.getShipToAddresses();
	
	Map addresses = new HashMap();
	
	for(Iterator i = shipAddresses.iterator(); i.hasNext(); ){
		ErpAddressModel address = (ErpAddressModel) i.next();
		if(EnumServiceType.CORPORATE.equals(address.getServiceType())){
			List cl = (List)addresses.get(EnumServiceType.CORPORATE);
			if(cl == null){
				cl = new ArrayList();
				addresses.put(EnumServiceType.CORPORATE, cl);
			}
			cl.add(address);
		}else {
			List hl = (List)addresses.get(EnumServiceType.HOME);
			if(hl == null){
				hl = new ArrayList();
				addresses.put(EnumServiceType.HOME, hl);
			}
			hl.add(address);
		}
	}
	String shipToDepotLocation = FDCustomerManager.getDefaultDepotLocationPK(identity);
	
	// Get user's payment methods
    List paymentMethods = erpCustomer.getPaymentMethods();
	int numCreditCards = 0;
	int ccNum = 0;	
	int numEChecks = 0;
	int ecNum = 0;	
    
    // Get cart's selected payment method
	String cardStatus ="";
    if(request.getParameter("card")!=null){
            cardStatus = request.getParameter("card");
    }
	
	FDCartModel cart = user.getShoppingCart();
    ErpPaymentMethodI selectedPayment = cart.getPaymentMethod();

	String selectedPaymPKId = request.getParameter("paymentId");
	String addressStatus = request.getParameter("addressStatus");
	
	if (request.getParameter("paymentMethodList") != null){
		selectedPaymPKId = request.getParameter("paymentMethodList");
	} else if(cardStatus.equalsIgnoreCase("new")){
    	selectedPaymPKId = "new";
    } else if (selectedPaymPKId==null ) {
        if (selectedPayment != null && !(cart instanceof FDModifyCartModel)) {
            selectedPaymPKId = ((ErpPaymentMethodModel)selectedPayment).getPK().getId();
        } else {
			selectedPaymPKId = FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity());
		}
    }
	
    // Get cart's selected address
    ErpAddressModel selectedAddress = user.getShoppingCart().getDeliveryAddress();
    
	String selectedAddrPKId = request.getParameter("selectAddressList");

	if("new".equalsIgnoreCase(addressStatus)) {
		selectedAddrPKId = "new";
	/*} else if (selectedAddress != null) {
		selectedAddrPKId = ((ErpAddressModel)selectedAddress).getPK().getId();*/
	} else {
		if(selectedAddrPKId==null){ 
			selectedAddrPKId = request.getParameter("selectAddressList");
		}
		if(selectedAddrPKId==null) { 
			selectedAddrPKId = FDCustomerManager.getDefaultShipToAddressPK(identity);
		}
	}
%>
<%
    // This logic allows us to choose dynamically what action the CheckoutController should perform
    String actionName = request.getParameter("actionName");
    String successPage = null;
    if (actionName==null || actionName.length()<1) actionName = "setDeliveryAddressAndPayment";
    if ("setDeliveryAddressAndPayment".equalsIgnoreCase(actionName)) {
        successPage = "checkout_verify_age.jsp";
        //successPage = "checkout_delivery_time.jsp";
    } else {
        successPage = "checkout_select_addr_payment.jsp";
    }
%>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>
<fd:CheckoutController actionName="<%=actionName%>" result="result" successPage="<%= successPage%>">
<% // remove after testing....dumps out the errors.
 if (!result.isSuccess()){
    Collection errs = result.getErrors();
    Iterator itr = errs.iterator();
    while( itr.hasNext() ){
        ActionError ae = (ActionError) itr.next();
        JspLogger.CC_CHECKOUT.debug(ae.getType() + " - " + ae.getDescription());
    }
}
%>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START DELIVERY ADDRESS SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= (erpCustomer != null && erpCustomer.isActive()) ? "" : "_warning" %>">
<form name="address_and_payment" method="POST" action="">
    <input type="hidden" name="deleteShipToAddressId" value="">
    <input type="hidden" name="actionName" value="">
    <input type="hidden" name="deletePaymentId" value="">
    <tr>
    <td width="80%">&nbsp;STEP 2 OF 5: Select Address &amp; Payment <% if (erpCustomer != null && !erpCustomer.isActive()) { %>&nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is <a href="<%= response.encodeURL("/customer_account/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a><% } %></td>
    <td align="right"><a href="javascript:address_and_payment.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
    </tr>
</table>
<%@ include file="/includes/i_modifyorder.jspf" %>

        <% String[] checkDlvPaymentForm = { "address", "paymentMethodList", "technical_difficulty", "referencedOrder", 
                                    "notInZone", "matching_addresses", "declinedCCD", 
                                    "undeliverableAddress", "payment", "system",
                                    "order_minimum", "payment_method_fraud", EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(),
                                    PaymentMethodName.ACCOUNT_HOLDER, PaymentMethodName.CARD_BRAND, PaymentMethodName.ACCOUNT_NUMBER,
                                    "expiration", EnumUserInfoName.BIL_ADDRESS_1.getCode(),EnumUserInfoName.BIL_APARTMENT.getCode(),
                                    EnumUserInfoName.BIL_CITY.getCode(),        
                                    EnumUserInfoName.BIL_STATE.getCode(), EnumUserInfoName.BIL_ZIPCODE.getCode(), "pickup_contact_number"
                                    }; %>
        
        <fd:ErrorHandler result='<%=result%>' field='<%=checkDlvPaymentForm%>' id='errorMsg'>
            <div class="content_fixed"><span class="error"><%=errorMsg%></span></div>   
        </fd:ErrorHandler>
            
        <%  if (user.getFailedAuthorizations() > 0) { %>
                    <div class="content_fixed"><span class="error">There was a problem with the credit card you selected.&nbsp;&nbsp;&nbsp;<br>
                    Please choose or add a new payment method.</span></div>
        <%  } %>
        
        <%
        {
            String errorMsg = (String) session.getAttribute(SessionName.SIGNUP_WARNING);
            if (errorMsg==null && user.isPromotionAddressMismatch()) {
                errorMsg = SystemMessageList.MSG_CHECKOUT_NOT_ELIGIBLE;
            }
        
            if (errorMsg!=null) {
                %><div class="content_fixed"><span class="error"><%@ include file="/includes/i_error_messages.jspf" %></span></div><%
            }
        }
        %>



<%
if(user.isAddressVerificationError()) {
       
%>    
<table width="100%" cellspacing="0" cellpadding="0" border="0">
<tr>
    <td rowspan="5" width="20"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_tp_rt_crnr.gif" width="6" height="5" border="0"></td>
    <td rowspan="5"><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>
</tr>
<tr>
    <td rowspan="3" bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td width="18" bgcolor="#CC3300"><img src="/media_stat/images/template/system_msgs/exclaim_CC3300.gif" width="18" height="22" border="0" alt="!"></td>
    <td class="text11rbold" width="100%" bgcolor="#FFFFFF">
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
				<%= SystemMessageList.MSG_GC_SIGNUP_SUCCESS %><br><br>
                <%= user.getAddressVerficationMsg() %>
                
                
			<img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"><br>
	</td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
    <td bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
<tr>
    <td rowspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_lft_crnr.gif" width="18" height="5" border="0"></td>
    <td bgcolor="#FFFFFF"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"></td>
    <td rowspan="2" colspan="2"><img src="/media_stat/images/template/system_msgs/CC3300_bt_rt_crnr.gif" width="6" height="5" border="0"></td>
</tr>
<tr>
    <td colspan="2" bgcolor="#CC3300"><img src="/media_stat/images/layout/cc3300.gif" width="1" height="1"></td>
</tr>
</table>
<br>
<% 
//clear info from session.
user.setAddressVerificationError(false);
}
%>


<div class="content_scroll" style="height: 72%;">

<div class="cust_full_module_header" style="margin-top: 0px;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
    <tr>
        <td class="cust_module_header_text">Delivery Addresses</td>
        <td align="right"><a href="checkout_new_address.jsp" class="add">ADD</A></td>
    </tr>
</table>
</div>

<%-- ------------ EDIT/DELETE DELIVERY ADDRESS JAVASCRIPT ------------ --%>
<script language="javascript">
    function setAction(frmObj,actionName) {
        if (frmObj.actionName==null) return;
        frmObj.actionName.value=actionName;
    }

    function setIdToDelete(frmObj,dlvID) {
       if (frmObj.deleteShipToAddressId==null) return;
        frmObj.deleteShipToAddressId.value=dlvID;
    }
    
    function confirmDelete(thisForm,entryType) {
        var doCancel = confirm ("Are you sure you want to delete this " + entryType + "?");
        if (doCancel == true) {
            thisForm.submit();
        }
    }
</script>
<%  int cellCounter = 0;
    boolean showDeleteButtons = shipAddresses.size() > 1 ? true : false;
    boolean showAddressButtons = true;
%>

    <logic:iterate id="dlvAddress" collection="<%= shipAddresses %>" type="com.freshdirect.customer.ErpAddressModel" indexId="addressCounter">
        <%
		String addrPKId = dlvAddress.getPK().getId();
		boolean checkedAddress =false;
		if(shipToDepotLocation == null || shipToDepotLocation.length() == 0){  // This is to deal with preselection of Depot Addresses
			if ("new".equalsIgnoreCase(addressStatus) && addressCounter.intValue() == shipAddresses.size()-1){
				checkedAddress = true;
			} else if (selectedAddrPKId==null) {  
				selectedAddrPKId = addrPKId;
				// !!! should get default pk from customerManager
				checkedAddress = true;
			} else if(addrPKId.equals(selectedAddrPKId)){
				checkedAddress = true;
			}
		}
		%>
		<div class="cust_inner_module" style="width: 33%;<%=addressCounter.intValue() < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="selectAddressList" value="<%= dlvAddress.getPK().getId() %>" <%=checkedAddress?"checked":"" %>> <%=addressCounter.intValue() + 1%></td>
                    <td align="center">
            
<%  boolean isDepotLocation = (dlvAddress instanceof ErpDepotAddressModel); %>
<fd:AltAddressInfo altDeliveryInfo="<%= dlvAddress.getInstructions() %>">
            <table width="100%" cellpadding="2" cellspacing="0" border="0" class="order">
			<%if(dlvAddress.getServiceType().equals(EnumServiceType.CORPORATE)){%>
				<tr>
					<td width="35%" class="order_detail" align="right">Company:</td>
                    <td width="65%"><%=dlvAddress.getCompanyName()%></td>
				</tr>
			<%}%>
                <tr>
                    <td width="35%" class="order_detail" align="right">Name:</td>
                    <td width="65%"><%= dlvAddress.getFirstName() %> <%= dlvAddress.getLastName() %></td>
                </tr>
            <%  if (isDepotLocation) {  
                    ErpDepotAddressModel dAddr = (ErpDepotAddressModel) dlvAddress;  %>
                <tr>
                    <td width="35%" class="order_detail" align="right">Facility:</td>
                    <td width="65%"><%= dAddr.getFacility() %></td>
                </tr>
            <%  } %>
                <tr>
                    <td width="35%" class="order_detail" align="right">Address:</td>
                    <td width="65%"><%= dlvAddress.getAddress1() %>
                     <%  if ((dlvAddress.getAddress2() != null) && !"".equals(dlvAddress.getAddress2().trim())) { %>
                        <br><%= dlvAddress.getAddress2() %>
                    <%  }   
                        if (!isDepotLocation) { %>
                        &nbsp;<%= !"".equals(dlvAddress.getApartment())?"<span class=\"order_detail\">#</span>":"" %><%= dlvAddress.getApartment() %>
                    <%  }   %>
                    </td>
                </tr>
           
                <tr>
                    <td width="35%" class="order_detail" align="right">City, State:</td>
                    <td width="65%"><%= dlvAddress.getCity() %>, <%= dlvAddress.getState() %></td>
                </tr>
                <tr>
                    <td width="35%" class="order_detail" align="right">Zip:</td>
                    <td width="65%"><%= dlvAddress.getZipCode() %></td>
                </tr>
            <%  if (isDepotLocation) {  
                    ErpDepotAddressModel dAddr = (ErpDepotAddressModel) dlvAddress;  %>
                <tr>
                    <td width="35%" class="order_detail">Instructions:</td>
                    <td width="65%"><%= dAddr.getInstructions() %></td>
                </tr>
            <%  }
                if (!"".equals(fldDlvInstructions)) {   %>  
                <tr>
                    <td width="35%" class="order_detail">Special instructions:</td>
                    <td width="65%"><%=fldDlvInstructions%></td>
                </tr>
            <%  }   %>
            </table>

<%  int orderCount = user.getAdjustedValidOrderCount();
    if ((orderCount < AddressName.DLV_ALTERNATE_THRESHHOLD) && !isDepotLocation) {
        if (leaveWithDoorman.booleanValue()) { %>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td colspan="2" class="order_detail"><b>Alternate Delivery</b></td>
                </tr>
                <tr>
                    <td ALIGN="left" colspan="2" class="order_detail">Leave package with doorman</td>
                </tr>
            </table>
<%      }
    }
    if ((orderCount >= AddressName.DLV_ALTERNATE_THRESHHOLD) && !isDepotLocation) {
    
        if (dlvAddress.getAltDelivery() != null && dlvAddress.getAltDelivery().getId() > 0) { %>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td colspan="2" class="order_detail"><b>Alternate Delivery</b><br><%= dlvAddress.getAltDelivery().getName()%></td>
                </tr>
<%           if("NEIGHBOR".equalsIgnoreCase(dlvAddress.getAltDelivery().getDeliveryCode())) {%>

                <tr>
                    <td width="35%" ALIGN="RIGHT" class="order_detail">Name:</td>
                    <td width="65%" class="order_detail"><%=dlvAddress.getAltFirstName()%> <%=dlvAddress.getAltLastName()%></td>
                </tr>
                <tr>
                    <td width="35%" ALIGN="RIGHT" class="order_detail">Apartment #&nbsp;</td>
                    <td width="65%" class="order_detail"><%=dlvAddress.getAltApartment()%></td>
                </tr>
                <tr>
                    <td width="35%" ALIGN="RIGHT" class="order_detail">Contact #&nbsp;</td>
                    <td width="65%" class="order_detail">
                    <% if (dlvAddress.getAltPhone()!=null) {%><%=dlvAddress.getAltPhone().getPhone()%> <%if( !"".equals(dlvAddress.getAltPhone().getExtension()) ){%>Ext. <%= dlvAddress.getAltPhone().getExtension() %> <%} }%></td>
                </tr>

            <%}%>
            </table>
        <%}%>               

        <%if(dlvAddress.getInstructions()!= null && !dlvAddress.getInstructions().equals("") && !"NONE".equalsIgnoreCase(dlvAddress.getInstructions())){%>  
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td ALIGN="left" colspan="2" class="order_detail"><b>Special delivery instructions:</b><br><%=dlvAddress.getInstructions()%></td>
                </tr>
            </table>
<%          }
    } %>
</fd:AltAddressInfo>
            </td></tr>
<%  if (showAddressButtons) { %>
            <tr><td colspan="2" align="center" style="padding-top:10px;">
                <% if (showDeleteButtons) { %> <a href="javascript:setAction(address_and_payment,'deleteDeliveryAddress');setIdToDelete(address_and_payment,'<%= dlvAddress.getPK().getId() %>');confirmDelete(address_and_payment,'Delivery Address')" class="delete">DELETE</a> <% } %><A HREF="<%= response.encodeURL("/checkout/checkout_edit_address.jsp?addressId=" + dlvAddress.getPK().getId()) %>" class="edit">EDIT</A>
<%  } %>
            </td></tr>
            <% if (user.isEligibleForPreReservation()) { %>
                        <tr><td colspan="2" height="10"></td></tr>
                        <tr><td colspan="2" style="border-top: 1px dashed #CCCCCC;" align="center" class="order_detail">
                        <%
                            FDReservation rsv = user.getReservation();
                                if (rsv != null && rsv.getAddressId().equals(dlvAddress.getPK().getId())) {%><span style="color:#FF6600;">&raquo;</span> Reserved timeslot: <b><%=CCFormatter.formatShortDlvDate(rsv.getStartTime())%>, <%=CCFormatter.formatTime(rsv.getStartTime())%> - <%=CCFormatter.formatDeliveryTime(rsv.getEndTime())%></b> (<a href="/customer_account/reserve_timeslot.jsp?addressId=<%=dlvAddress.getPK().getId()%>&from=checkout">Edit</a>)
                            <% } %>
                        </td></tr>
            <% } %>


            </table>
        </div>
    </div>
        <%if(addressCounter.intValue() != 0 && (addressCounter.intValue() + 1) % 3 == 0 && ((addressCounter.intValue() + 1) < shipAddresses.size())){%>
            <br clear="all">
        <%}%>
    </logic:iterate>
    <br clear="all">
    </tr>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END DELIVERY ADDRESS SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>


<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START DEPOT LOCATION SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%  if (user.isDepotUser() ) { 

	String  defaultDepotLocation = "";
	String fldDepotLocation = request.getParameter("selectAddressList");
	boolean checkedAddress = false;
	
	if (user !=null && user.getIdentity() != null){
	 	defaultDepotLocation = FDCustomerManager.getDefaultDepotLocationPK(user.getIdentity() );
	}
	
	if(fldDepotLocation != null && fldDepotLocation.startsWith("DEPOT_")){
		fldDepotLocation = fldDepotLocation.substring("DEPOT_".length());
	}else{
		fldDepotLocation = defaultDepotLocation;
	} 
%>
<div class="cust_full_module_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="cust_module_header_text"><%=user.isCorporateUser() ? "Delivery" : "Depot" %> Locations</td>
    </tr>
</table>
</div>
<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
<%  cellCounter = 0; %>
    <tr VALIGN="TOP">
    <fd:GetDepotLocations id='locations' depotCode='<%=user.getDepotCode()%>'>
    <%  Date now = new Date();  %>
    <logic:iterate id="location" collection="<%= locations %>" type="com.freshdirect.delivery.depot.DlvLocationModel" indexId="locationCounter">
        <%
            checkedAddress = ("DEPOT_"+location.getPK().getId()).equalsIgnoreCase(request.getParameter("selectAddressList"));
            
			if (location.getPK().getId().equalsIgnoreCase(fldDepotLocation)){
				checkedAddress = true;
		    }
			// skip locations that aren't ready yet or have expired
			if (now.before(location.getStartDate()) || now.after(location.getEndDate())) continue;
			
			if(locations.size() == 1 && selectedAddrPKId == null){
				checkedAddress = true;
			}
        %>
        <td width="32%">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td><input type="radio" name="selectAddressList" class="radio" value="DEPOT_<%=location.getPK().getId()%>" <%=checkedAddress?"checked":"" %>> <%= (locationCounter.intValue()+1) %>
                    </td>
                </tr>
            </table>
            <table class="order"><tr>
            <td valign="top" width="195">
                <b><%=location.getFacility()%></b><br>
                <%=location.getAddress().getAddress1()%>
                <% if(location.getAddress().getApartment() != null){%>
                    &nbsp;&nbsp;Suite/Floor: <%=location.getAddress().getApartment()%>
                <% } %>
                <br>
                <% if(location.getAddress().getAddress2()!= null ){%>
                    <%=location.getAddress().getAddress2()%><br>
                <%}%>
                <%=location.getAddress().getCity()%> <%=location.getAddress().getState()%>, <%=location.getAddress().getZipCode()%><br>
                <br>
                <%=location.getInstructions()%><br>
                </td>
            </tr></table>
        </td>
        <td width="2%">&nbsp;</td>
<%  if (cellCounter == 2) { %>
    </tr>
    <tr>
<%      cellCounter = 0;
    } else {
        cellCounter++;
    }
%>
    </logic:iterate>
    </fd:GetDepotLocations>
<%  for (int i = cellCounter % 2; i < 2; i++) { %>
        <td>&nbsp;</td>
<%  } %>
    </tr>
</table>
<%  } %>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END DEPOT LOCATION SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START PICKUP LOCATION SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%  if (user.isPickupUser()) { 

	Collection pickups = FDDepotManager.getInstance().getPickupDepots();
	String  defaultDepotLocation = "";
	if (user !=null && user.getIdentity() != null){
	 	defaultDepotLocation = FDCustomerManager.getDefaultDepotLocationPK(user.getIdentity() );
	}
	String fldDepotLocation = request.getParameter("selectAddressList");
	boolean checkedAddress = false;
	
	if(fldDepotLocation != null && fldDepotLocation.startsWith("DEPOT_")){
		fldDepotLocation = fldDepotLocation.substring("DEPOT_".length());
	}else{
		fldDepotLocation = defaultDepotLocation;
	}
%>
<div class="cust_full_module_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="cust_module_header_text">Pickup Locations</td>
    </tr>
</table>
</div>
<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
<%  cellCounter = 0; %>
    <tr VALIGN="TOP">
    <%  Date now = new Date();  %>
    <logic:iterate id="depot" collection="<%= FDDepotManager.getInstance().getPickupDepots() %>" type="com.freshdirect.delivery.depot.DlvDepotModel">
    <logic:iterate id="location" collection="<%= depot.getLocations() %>" type="com.freshdirect.delivery.depot.DlvLocationModel" indexId="locationCounter">
        <%
            if (location.getPK().getId().equalsIgnoreCase(fldDepotLocation) || (user.isPickupOnly() && pickups.size() == 1)) {
				checkedAddress = true;
   			}
        %>
        <td width="32%">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td><input type="radio" name="selectAddressList" class="radio" value="DEPOT_<%=location.getPK().getId()%>" <%=checkedAddress?"checked":"" %>> <%= (locationCounter.intValue()+1) %></td>
                </tr>
            </table>
            <table class="order"><tr>
            <td valign="top" width="195">
                <b><%=location.getFacility()%></b><br>
                <%=location.getAddress().getAddress1()%><br>
                <% if(location.getAddress().getAddress2()!= null ){%>
                    <%=location.getAddress().getAddress2()%><br>
                <%}%>
                <%=location.getAddress().getCity()%> <%=location.getAddress().getState()%>, <%=location.getAddress().getZipCode()%><br>
                <br>
                <%=location.getInstructions()%><br>
                <b>Local Contact Number/Cell Phone</b><br>
            <%ErpCustomerInfoModel customerInfo = FDCustomerFactory.getErpCustomerInfo(user.getIdentity()); %> 
            <input type="text" maxlength="14" size="21" class="text11" name="contact_phone_<%=location.getPK().getId()%>" value="<%=customerInfo.getOtherPhone()%>"><br>
                </td>
            </tr></table>
        </td>
        <td width="2%">&nbsp;</td>
<%  if (cellCounter == 2) { %>
    </tr>
    <tr>
<%      cellCounter = 0;
    } else {
        cellCounter++;
    }
%>
    </logic:iterate>
    </logic:iterate>
<%  for (int i = cellCounter % 2; i < 2; i++) { %>
        <td>&nbsp;</td>
<%  } %>
    </tr>
</table>
<%}%>

<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END PICKUP LOCATION SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>



<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START PAYMENT METHOD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%  cellCounter = 0;
    boolean showCCButtons = true;
    showDeleteButtons = paymentMethods.size() > 1 ? true : false; %>
<%-- ------------ EDIT/DELETE PAYMENT METHOD JAVASCRIPT ------------ --%>
<script language="javascript">
	function confirmDeletePayment(frmObj,payId) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (frmObj == null) {
			frmObj = document.forms[0];
		}
		if (doDelete == true) {
			setDeletePaymentId(frmObj, payId);
			setActionName(frmObj,'deletePaymentMethod');
			frmObj.submit();
		}
	}
    
</script>
<div class="cust_full_module_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="cust_module_header_text">Payment Information: Credit Cards</td>
        <td align="right"><A HREF="checkout_new_credit_card.jsp" class="add">ADD</A></td>
    </tr>
</table>
</div>

<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%  
	if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) { 
		numCreditCards++;
	} else if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) { 
		numEChecks++;
	}	
%>
</logic:iterate>

<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">
<%
    if (EnumPaymentMethodType.CREDITCARD.equals(payment.getPaymentMethodType())) {
%>
    <div class="cust_inner_module" style="width: 33%;<%=ccNum < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">

<%          
		String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
		
		String methodChecked = "";
	   
	    if (paymentPKId.equals(selectedPaymPKId) && numCreditCards>1){
	        methodChecked = "checked";
	    }
		else if("new".equalsIgnoreCase(selectedPaymPKId) && ccCounter.intValue() == numCreditCards-1){
		    methodChecked = "checked";
		}
		if ( methodChecked.equals("") && numCreditCards==1 )
			methodChecked = "checked";
%>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ccNum + 1%></td>
                    <td><%@ include file="/includes/i_payment_select.jspf"%></td>
                </tr>
            </table>
        </div>
    </div>
        <%if(ccNum != 0 && (ccNum + 1) % 3 == 0 && ((ccNum + 1) < numCreditCards)){%>
            <br clear="all">
        <%}
		ccNum++;	
	} %>        
    </logic:iterate>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ END CREDIT CARD SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<%-- START CHECKING ACCT --%>
<% if (user.isCheckEligible()) { %>
			<table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-bottom: solid 1px #999999;"><tr><td bgcolor="#E8FFE8" width="20%" style="padding: 4px; border-top: solid 1 px #666666; border-left: solid 1 px #666666; border-right: solid 1 px #666666;"><b>Checking accounts</b></td><td width="59%" class="field_note">&nbsp;A valid credit card is required as a guarantee for orders using check as payment. <a href="#">View check usage promotion</a></td><td align="right" width="20%"><a href="/checkout/checkout_new_checkacct.jsp" class="add">ADD</a></td><td width="1%"></td></tr></table>
        	 <%-- using cc data for display purposes --%>
<logic:iterate id="payment" collection="<%= paymentMethods %>" type="com.freshdirect.customer.ErpPaymentMethodI" indexId="ccCounter">

<%      if (EnumPaymentMethodType.ECHECK.equals(payment.getPaymentMethodType())) { 			
%>
    <div class="cust_inner_module" style="width: 33%;<%=ecNum < 3 ?"border-top: none;":""%>">
                <div class="cust_module_content">
<%
            String paymentPKId = ((ErpPaymentMethodModel)payment).getPK().getId();
			
			String methodChecked = "";
           
            if (paymentPKId.equals(selectedPaymPKId) && numEChecks>1){
                methodChecked = "checked";
            }
			else if("new".equalsIgnoreCase(selectedPaymPKId) && ccCounter.intValue() == numEChecks-1){
			    methodChecked = "checked";
			}
			if ( methodChecked.equals("") && numEChecks==1 )
				methodChecked = "checked";
             %>
	<crm:CrmGetIsBadAccount id="isRestrictedAccount" paymentMethod="<%=payment%>">
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr valign="top">
                <% if(!isRestrictedAccount.booleanValue()) { %>
                    <td class="note"><input type="radio" name="paymentMethodList" value="<%= paymentPKId %>" <%= methodChecked %>> <%=ecNum + 1%></td>
                 <% } else { %>
                    <td class="note"><%=ecNum + 1%></td>
                 <% } %>
                    <td><%@ include file="/includes/checking_account_select.jspf"%></td>
                </tr>
            </table>
	</crm:CrmGetIsBadAccount>
        </div>
    </div>
        <%if(ecNum != 0 && (ecNum+1) % 3 == 0 && (ecNum+1 < numEChecks)){%>
            <br clear="all">
        <%}
			ecNum++;
	} %>
        
    </logic:iterate>
	<% } %>
	<%--END CHECKING--%>

</form>
</div>
</tmpl:put>
</fd:CheckoutController>

</fd:GetCustomerObjs>
</tmpl:insert>
