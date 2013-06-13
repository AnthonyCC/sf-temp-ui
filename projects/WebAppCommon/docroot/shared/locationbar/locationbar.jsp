<%@page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import='com.freshdirect.delivery.EnumDeliveryStatus' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel' %>
<%@ page import='java.util.List' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<fd:LocationHandler/>
<%
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
String selectedPickupId = (String)pageContext.getAttribute(LocationHandlerTag.SELECTED_PICKUP_DEPOT_ID_ATTR);
Boolean disabled = (Boolean)pageContext.getAttribute(LocationHandlerTag.DISABLED_ATTR);
%>

<tmpl:insert template="/shared/locationbar/locationbar_layout.jsp">

<tmpl:put name="zipcode"><span class="zipcode orange"><%= selectedAddress.getZipCode() %></span> </tmpl:put>
<% 
	if(user!=null && user.getLevel() != FDUserI.GUEST) {
		List<ErpAddressModel> allHomeAddresses = user.getAllHomeAddresses();
		List<ErpAddressModel> allCorporateAddresses = user.getAllCorporateAddresses();
		List<DlvLocationModel> allPickupDepots = (List<DlvLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);
		
		if( allHomeAddresses.size() + allCorporateAddresses.size() + allPickupDepots.size() > 1) {
			%><tmpl:put name="address"><select id="selectAddressList" name="selectAddressList"<%=disabled!=null && disabled?" disabled":""%>><%
				if(allHomeAddresses.size()>0){%>
					<optgroup label="Home Delivery">
						<logic:iterate id="homeAddress" collection="<%=allHomeAddresses%>" type="com.freshdirect.common.address.AddressModel">
							<option<%= selectedAddress.equals(homeAddress) ? " selected='selected'" : "" %> value="<%=homeAddress.getPK().getId()%>"><%=LocationHandlerTag.formatAddressText(homeAddress)%></option>
						</logic:iterate>
					</optgroup>
				<%}
				if(allCorporateAddresses.size()>0){%>
					<optgroup label="Office Delivery">
						<logic:iterate id="corporateAddress" collection="<%=allCorporateAddresses%>" type="com.freshdirect.common.address.AddressModel">
							<option<%= selectedAddress.equals(corporateAddress) ? " selected='selected'" : "" %> value="<%=corporateAddress.getPK().getId()%>"><%=LocationHandlerTag.formatAddressText(corporateAddress)%></option>
						</logic:iterate>
					</optgroup>
				<%}
				if(allPickupDepots.size()>0){%>
				<optgroup label="Pickup">
					<logic:iterate id="pickupDepot" collection="<%=allPickupDepots%>" type="com.freshdirect.delivery.depot.DlvLocationModel">
						<option<%= selectedPickupId!=null && selectedPickupId.equalsIgnoreCase(pickupDepot.getId()) ? " selected='selected'" : "" %> value="DEPOT_<%=pickupDepot.getId()%>"><%=LocationHandlerTag.formatAddressText(pickupDepot.getAddress())%></option>
					</logic:iterate>
				</optgroup>
				<%}
			%></select></tmpl:put><%
		} else { //only one address
			%><tmpl:put name="address"><span class="text"><%=LocationHandlerTag.formatAddressText(selectedAddress)%></span></tmpl:put><%	
		}

	} else { //non-recognized user
		String shortAddress = LocationHandlerTag.formatAddressShortText(selectedAddress);
		%><tmpl:put name="address"><span class="text"><%="".equals(shortAddress) ?  "" : "("+shortAddress+")" %></span> <span id="newzip"><input type="text" id="newziptext" class="placeholder" placeholder="change zip code" maxlength="5"><input type="image" src="/media_stat/images/locationbar/button_go.png" id="newzipgo"></span></tmpl:put><%		
	}
%>
<tmpl:put name="zipdisplay"><tmpl:get name="zipcode" /> <tmpl:get name="address" /></tmpl:put>

<tmpl:put name="cheftable"><%
	if(user!=null && user.isChefsTable() && !user.getChefsTableInduction().equals("0") && user.getChefsTableInduction().length() == 8) { 
		%><a href="/your_account/manage_account.jsp"><img src='<%= "/media_stat/images/navigation/global_nav/global_hdr_ct_"+user.getChefsTableInduction().substring(0,4)+".gif"%>' width="256" height="10" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0"></a><% 
	} else if (user!=null && user.isDlvPassActive()) {
		%><a href="/your_account/delivery_pass.jsp"><img src="/media_stat/images/navigation/global_nav/global_hdr_dp.gif" width="217" height="10" alt="CLICK HERE FOR DETAILS"  vspace="0" border="0"></a><% 
	} %> 
</tmpl:put>

<tmpl:put name="loginButton"><a href="/login/login.jsp" class="loginButton">login</a></tmpl:put>
<tmpl:put name="logoutButton"><a href="/logout.jsp" class="logoutButton">logout</a></tmpl:put>
<tmpl:put name="signupButton"><% 
	if(FDStoreProperties.isLightSignupEnabled()) { 
		%><a href="#" class="signUpButton" onclick="doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/signup_lite.jsp\' width=\'480px\' height=\'590px\' frameborder=\'0\' ></iframe>', '<span class=\'text12\' style=\'color: #000; margin-left: -12px;\'><strong>Already have a password? <a href=\'/login/login.jsp\' onclick=\'window.top.location=this.href;return false;\' style=\'text-decoration:none;\'>Log in now</a></strong></span>')">signup</a><% 
	} else { 
		%><a href="/registration/signup.jsp" class="signUpButton">signup</a><% 
	} 
%></tmpl:put>
<%
	if (user!=null && user.getLevel() == FDUserI.SIGNED_IN) {
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="logoutButton" /></tmpl:put><%
	} else if (user!=null && user.getLevel() == FDUserI.RECOGNIZED) {	
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="loginButton" /></tmpl:put><%
	} else { 
		%><tmpl:put name="buttons"><label>New customer?</label><tmpl:get name="signupButton" /><tmpl:get name="loginButton" /></tmpl:put>
		<tmpl:put name="location_message"><jsp:include page="location_messages.jsp" /></tmpl:put><%
    }

	if(Boolean.TRUE == pageContext.getAttribute(LocationHandlerTag.SERVICE_TYPE_MODIFICATION_ENABLED)){
		if(user.isCorporateUser()){
			%><tmpl:put name="hoicon"><a class="home green" href="/index.jsp">Home delivery?</a></tmpl:put><%			
		}else {
			%><tmpl:put name="hoicon"><a class="office green" href="/department.jsp?deptId=COS">Office delivery?</a></tmpl:put><%			
		}
	}
%>

</tmpl:insert>
