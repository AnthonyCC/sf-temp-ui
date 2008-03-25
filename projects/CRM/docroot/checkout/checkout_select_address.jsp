<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.FDDepotManager"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.FDReservation" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="crm" prefix="crm" %>

<fd:CheckLoginStatus guestAllowed="false" redirectPage="/registration/nw_cst_check_zone.jsp" />

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Checkout > Select Delivery Address</tmpl:put>

<%  
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    FDIdentity identity = user.getIdentity();%>
<crm:GetErpCustomer id="customer" user="<%=user%>">
<%  
    // Get user's shipping addresses
    List shipAddresses = customer.getShipToAddresses();
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
    
    FDCartModel cart = user.getShoppingCart();
    
    String addressStatus = request.getParameter("addressStatus");
    
    // Get cart's selected address
    ErpAddressModel selectedAddress = user.getShoppingCart().getDeliveryAddress();
    
    String selectedAddrPKId = request.getParameter("selectAddressList");
    
    if("new".equalsIgnoreCase(addressStatus)) {
        selectedAddrPKId = "new";
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
    if (actionName==null || actionName.length()<1) actionName = "setDeliveryAddress";
    if ("setDeliveryAddress".equalsIgnoreCase(actionName)) {
        successPage = "checkout_verify_age.jsp";
        //successPage = "checkout_delivery_time.jsp";
    } else {
        successPage = "checkout_select_address.jsp";
    }
    System.out.println("Action name "+actionName);
%>
<%@ page import="com.freshdirect.webapp.util.JspLogger"%>
<fd:CheckoutController actionName="<%=actionName%>" result="result" successPage="<%= successPage%>" noContactPhonePage="/checkout/checkout_edit_address.jsp">
<%

	if (result.hasError("error_dlv_pass_only")) {
%>	
	     <jsp:forward page="/order/place_order_build.jsp?dlvpasserror=true" /> 
<%	     
	    return;
	}
%>	

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~ START DELIVERY ADDRESS SECTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~ --%>
<form name="address" method="POST" action="">
<table width="100%" cellpadding="2" cellspacing="0" border="0" class="checkout_header<%= ( user.isActive()) ? "" : "_warning" %>">
    <tr>

    <td>
        &nbsp;
        Step 1 of 4: Select Delivery Address 
        <a href="javascript:popResize('/kbit/policy.jsp?show=Delivery','715','940','kbit')" 
            onmouseover="return overlib('Select a delivery address for this order. Residential and Commercial addresses differ in delivery cost.&lt;br&gt;Click for Address Selection Help.', AUTOSTATUS, WRAP);" 
            onmouseout="nd();" class="help">?</a>
        <% if (!user.isActive()) { %>
            &nbsp;&nbsp;&nbsp;!!! Checkout prevented until account is 
                <a href="<%= response.encodeURL("/main/deactivate_account.jsp?successPage="+request.getRequestURI()) %>" class="new">REACTIVATED</a>
        <% } %>
    </td>
    <td></td>
	<td align="right"><a href="javascript:address.submit()" class="checkout">CONTINUE CHECKOUT >></a></td>
    </tr>
</table>
<%@ include file="/includes/i_modifyorder.jspf" %>

        <% String[] checkDlvPaymentForm = { "address", "apartment","paymentMethodList", "technical_difficulty", "referencedOrder", 
                                    "notInZone", "matching_addresses", "declinedCCD", 
                                    "undeliverableAddress", "payment", "system",
                                    "order_minimum", "credit_card_fraud", EnumUserInfoName.DLV_NOT_IN_ZONE.getCode(),
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

<div class="content_scroll" style="height: 72%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_full_module_header" style="margin-top: 0px;">
    <tr>
        <td class="cust_module_header_text">Delivery Addresses</td>
        <td align="right"><a href="checkout_new_address.jsp" class="add">ADD</A></td>
    </tr>
</table>

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
    <input type="hidden" name="deleteShipToAddressId" value="">
    <input type="hidden" name="actionName" value="">
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
    
        if(dlvAddress.getInstructions()!= null && !dlvAddress.getInstructions().equals("") && !"NONE".equalsIgnoreCase(dlvAddress.getInstructions())){%>  
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td ALIGN="left" colspan="2" class="order_detail"><b>Special delivery instructions:</b><br><%=dlvAddress.getInstructions()%></td>
                </tr>
            </table>
<%          } 
          if (EnumUnattendedDeliveryFlag.OPT_IN.equals(dlvAddress.getUnattendedDeliveryFlag())) { %>
            <table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
                <tr>
                    <td ALIGN="left" colspan="2" class="order_detail"><b>Unattended delivery instructions:</b><br>
		        <%=NVL.apply(dlvAddress.getUnattendedDeliveryInstructions(),"OK")%></td>
                </tr>
            </table>

<%        }

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
        <%}
    } %>
</fd:AltAddressInfo>
            </td></tr>
<%  if (showAddressButtons) { %>
            <tr><td colspan="2" align="center" style="padding-top:10px;">
                <% if (showDeleteButtons) { %> 
                    <a href="javascript:setAction(address,'deleteDeliveryAddress');setIdToDelete(address,'<%= dlvAddress.getPK().getId() %>');confirmDelete(address,'Delivery Address')" class="delete">
                        DELETE
                    </a> 
                <% } %>
                <A HREF="<%= response.encodeURL("/checkout/checkout_edit_address.jsp?addressId=" + dlvAddress.getPK().getId()) %>" class="edit">EDIT</A>
<%  } %>
            </td></tr>
            <% if (user.isEligibleForPreReservation()) { %>
                        <tr><td colspan="2" height="10"></td></tr>
                        <tr><td colspan="2" style="border-top: 1px dashed #CCCCCC;" align="center" class="order_detail">
                        <%
                            FDReservation rsv = user.getReservation();
                                if (rsv != null && rsv.getAddressId().equals(dlvAddress.getPK().getId())) {%>
                                    <span style="color:#FF6600;">&raquo;</span> 
                                    Reserved timeslot: 
                                    <b>
                                        <%=CCFormatter.formatShortDlvDate(rsv.getStartTime())%>, 
                                        <%=CCFormatter.formatTime(rsv.getStartTime())%> - 
                                        <%=CCFormatter.formatDeliveryTime(rsv.getEndTime())%>
                                    </b> (<a href="/customer_account/reserve_timeslot.jsp?addressId=<%=dlvAddress.getPK().getId()%>&from=checkout">Edit</a>)
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
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_full_module_header">
    <tr>
        <td class="cust_module_header_text"><%=user.isCorporateUser() ? "Delivery" : "Depot" %> Locations</td>
    </tr>
</table>
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
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="cust_full_module_header">
    <tr>
        <td class="cust_module_header_text">Pickup Locations</td>
    </tr>
</table>
<table width="100%" cellpadding="0" cellspacing="0" border="0" ALIGN="CENTER" class="order">
<%  cellCounter = 0; %>
    <tr VALIGN="TOP">
    <%  Date now = new Date();  %>
    <logic:iterate id="depot" collection="<%= FDDepotManager.getInstance().getPickupDepots() %>" type="com.freshdirect.delivery.depot.DlvDepotModel">
    <logic:iterate id="location" collection="<%= depot.getLocations() %>" type="com.freshdirect.delivery.depot.DlvLocationModel" indexId="locationCounter">
        <%
			if ("HAM".equalsIgnoreCase(depot.getDepotCode())) continue; //skip it
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
            <%ErpCustomerInfoModel customerInfo = customer.getCustomerInfo(); %> 
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
</form>
<br>
</div>
</tmpl:put>
</fd:CheckoutController>
</crm:GetErpCustomer>
</tmpl:insert>
