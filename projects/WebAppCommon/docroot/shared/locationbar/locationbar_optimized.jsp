<%@page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel"%>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import='com.freshdirect.logistics.delivery.model.EnumDeliveryStatus' %>
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

<tmpl:insert template="/shared/locationbar/locationbar_layout_optimized.jsp">

<tmpl:put name="zipcode"><span class="zipcode orange"><%= selectedAddress.getZipCode() %></span> </tmpl:put>
<% 
	if(user!=null && user.getLevel() != FDUserI.GUEST) {
		List<ErpAddressModel> allHomeAddresses = user.getAllHomeAddresses();
		List<ErpAddressModel> allCorporateAddresses = user.getAllCorporateAddresses();
		List<FDDeliveryDepotLocationModel> allPickupDepots = (List<FDDeliveryDepotLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);
		
		if( allHomeAddresses.size() + allCorporateAddresses.size() + allPickupDepots.size() > 1 && (disabled == null || !disabled)) {
			%><tmpl:put name="address"><select id="selectAddressList" aria-label="choose address" name="selectAddressList"><%
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
					<logic:iterate id="pickupDepot" collection="<%=allPickupDepots%>" type="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel">
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
		%><tmpl:put name="address"><span class="text"><%="".equals(shortAddress) ?  "" : "("+shortAddress+")" %></span> <span id="newzip"><label for="newziptext"><span class="offscreen">change zip code:</span></label><input type="text" id="newziptext" class="placeholder" placeholder="change zip code" maxlength="5"><button id="newzipgo">go</button></span></tmpl:put><%		
	}
%>
<tmpl:put name="zipdisplay"><tmpl:get name="zipcode" /> <tmpl:get name="address" /></tmpl:put>

<tmpl:put name="cheftable"><%
	if(user!=null && user.isChefsTable() && !user.getChefsTableInduction().equals("0") && user.getChefsTableInduction().length() == 8) { 
		%><a href="/your_account/manage_account.jsp"><img src='<%= "/media_stat/images/navigation/global_nav/global_hdr_ct_"+user.getChefsTableInduction().substring(0,4)+".gif"%>' width="256" height="10" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0" border="0" style="margin: 3px 0 -3px 0;" /></a><% 
	} else if (user!=null && user.isDlvPassActive()) {
		%><a href="/your_account/delivery_pass.jsp"><img src="/media_stat/images/navigation/global_nav/global_hdr_dp.gif" width="217" height="10" alt="CLICK HERE FOR DETAILS ABOUT UNLIMITED DELIVERY PASS MEMBER"  vspace="0" border="0" style="margin: 3px 0 -3px 0;" /></a><% 
	} %> 
</tmpl:put>

<tmpl:put name="loginButton">
	<button class="loginButton" id="locabar_loginButton">login</button>
</tmpl:put>
<tmpl:put name="logoutButton"><button onclick="window.location='/logout.jsp';" class="logoutButton">logout</button></tmpl:put>
<tmpl:put name="signupButton"><% 
	if(FDStoreProperties.isLightSignupEnabled()) {
		%>
		<button id="lsuButton" class="signUpButton">create account</button>
		<script type="text/javascript">
			$jq('#lsuButton').click(function(e) {
				e.preventDefault();
				var lsuDialog = doOverlayDialogByHtml('<iframe id=\'signupframe\' src=\'/registration/signup_lite.jsp\' width=\'480px\' height=\'590px\' frameborder=\'0\' ></iframe>');
				lsuDialog.dialog('option', 'title', '<span class=\'text12\' style=\'color: #000; margin-left: 12px; font-weight: bold;\'>Already have a password? <a href=\'/login/login.jsp\' onclick=\'window.top.location=this.href;return false;\' \'>Log in now</a></div>');
			});
		</script>
		<% 
	} else { 
		%><button class="signUpButton" onclick="window.location='/registration/signup.jsp';">create account</button><% 
	} 
%></tmpl:put>
<%
	if (user!=null && user.getLevel() == FDUserI.SIGNED_IN) {
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="logoutButton" /></tmpl:put><%
	} else if (user!=null && user.getLevel() == FDUserI.RECOGNIZED) {	
		%><tmpl:put name="buttons"><tmpl:get name="cheftable" /><tmpl:get name="loginButton" /></tmpl:put><%
	} else { 
		%><tmpl:put name="buttons"><tmpl:get name="signupButton" /><tmpl:get name="loginButton" /></tmpl:put>
		<tmpl:put name="location_message"><jsp:include page="location_messages.jsp" /></tmpl:put><%
    }

	if(Boolean.TRUE == pageContext.getAttribute(LocationHandlerTag.SERVICE_TYPE_MODIFICATION_ENABLED)){
		if(user.isCorporateUser()){
			%><tmpl:put name="hoicon"><a class="home green" href="/index.jsp?serviceType=HOME">Home delivery?</a></tmpl:put><%
		}else {
			%><tmpl:put name="hoicon"><a class="office green" href="/index.jsp?serviceType=CORPORATE">Office delivery?</a></tmpl:put><%	
		}
	}
%>

</tmpl:insert>
