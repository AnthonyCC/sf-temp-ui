<%@page import="com.freshdirect.fdlogistics.model.FDDeliveryDepotLocationModel"%>
<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import='com.freshdirect.logistics.delivery.model.EnumDeliveryStatus' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumFeatureRolloutStrategy' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature' %>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter' %>
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
MasqueradeContext masqueradeContext = user.getMasqueradeContext();
%>

 <!-- Adding Skip to Navigation : Start-->
    <a href="#skip_to_content" class="hidefromView">Skip to Content</a>
    <!-- Adding Skip to Navigation : End-->

<tmpl:insert template="/shared/locationbar/locationbar_layout.jsp">
<%if (masqueradeContext!=null) {
	String makeGoodFromOrderId = masqueradeContext.getMakeGoodFromOrderId();
%>
<tmpl:put name="topwarningbar">
	<div id="topwarningbar">
		You (<%=masqueradeContext.getAgentId()%>) are masquerading as <%=user.getUserId()%> (Store: <%= user.getUserContext().getStoreContext().getEStoreId() %> | Facility: <%= user.getUserContext().getFulfillmentContext().getPlantId() %>)
		<%if (makeGoodFromOrderId!=null) {%>
			<br>You are creating a MakeGood Order from <a href="/quickshop/shop_from_order.jsp?orderId=<%=makeGoodFromOrderId%>">#<%=makeGoodFromOrderId%></a>
			(<a href="javascript:if(FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes', width: 600, height: 800, opacity: .5}) } else {pop('/overlays/carton_contents_view.jsp?showForm=true&orderId=<%= makeGoodFromOrderId %>&scroll=yes','600','800')};">Carton Contents</a>)
			<a class="imgButtonRed" href="/cancelmakegood.jsp">Cancel MakeGood</a>
		<%}%>
	</div>
</tmpl:put>
<%}%>

<tmpl:put name="zipcode"><span class="zipcode orange"><%= selectedAddress.getZipCode() %></span> </tmpl:put>
<% 
	if(user!=null && user.getLevel() != FDUserI.GUEST) {
		List<ErpAddressModel> allHomeAddresses = user.getAllHomeAddresses();
		List<ErpAddressModel> allCorporateAddresses = user.getAllCorporateAddresses();
		List<FDDeliveryDepotLocationModel> allPickupDepots = (List<FDDeliveryDepotLocationModel>) pageContext.getAttribute(LocationHandlerTag.ALL_PICKUP_DEPOTS_ATTR);
		
		if( allHomeAddresses.size() + allCorporateAddresses.size() + allPickupDepots.size() > 1 && (disabled == null || !disabled)) {
			%><tmpl:put name="address"><select id="selectAddressList" name="selectAddressList"><%
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
		%><tmpl:put name="address"><span class="text"><%="".equals(shortAddress) ?  "" : "("+shortAddress+")" %></span><span id="newzip"><label for="newziptext"></label><input type="text" id="newziptext" class="placeholder" placeholder="change zip code" maxlength="5" onkeydown="goButtonFocus(event);"><button id="newzipgo" class="">go</button></span></tmpl:put><%		
	}
%>
<tmpl:put name="zipdisplay"><tmpl:get name="zipcode" /> <tmpl:get name="address" /></tmpl:put>

<tmpl:put name="cheftable"><%
	if(user!=null && user.isChefsTable() && !user.getChefsTableInduction().equals("0") && user.getChefsTableInduction().length() == 8) { 
		%><a href="/your_account/manage_account.jsp"><img src='<%= "/media_stat/images/navigation/global_nav/global_hdr_ct_"+user.getChefsTableInduction().substring(0,4)+".gif"%>' width="256" height="10" alt="CLICK HERE FOR EXCLUSIVE CHEF'S TABLE OFFERS" vspace="0" border="0" border="0" style="margin: 3px 0 -3px 0;" /></a><% 
	} else if (user!=null && user.isDlvPassActive()) {
		%><a href="/your_account/delivery_pass.jsp"><img src="/media_stat/images/navigation/global_nav/global_hdr_dp.gif" width="217" height="10" alt="CLICK HERE FOR DETAILS"  vspace="0" border="0" style="margin: 3px 0 -3px 0;" /></a><% 
	} %> 
</tmpl:put>

<tmpl:put name="loginButton">
	<button class="loginButton" id="locabar_loginButton">log in</button>
</tmpl:put>

<tmpl:put name="logoutButton">
	<% if ( FDStoreProperties.isSocialLoginEnabled() ) { %>
		<button onclick="window.location='/logout.jsp';" class="logoutButton locationbar-social-signout-button">Sign Out</button>
	<% } else { %>
	    <button onclick="window.location='/logout.jsp';" class="logoutButton">logout</button>
	<% } %>
</tmpl:put>

<tmpl:put name="signupButton"><% 
	if(FDStoreProperties.isLightSignupEnabled()) { 
  %><button class="signUpButton" onclick="if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/registration/signup_lite.jsp', width: 480, height: 600, opacity: .5}) } else { doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/signup_lite.jsp\' width=\'480px\' height=\'590px\' frameborder=\'0\' ></iframe>', '<span class=\'text12\' style=\'color: #000; margin-left: -12px;\'><strong>Already have a password? <a href=\'/login/login.jsp\' onclick=\'window.top.location=this.href;return false;\' style=\'text-decoration:none;\'>Log in now</a></strong></span>') }">create account</button><% 
	} else { 
		%><button class="signUpButton" onclick="window.location='/registration/signup.jsp';">create account</button><% 
	} 
%></tmpl:put>

<tmpl:put name="loginButton">
	<% if ( FDStoreProperties.isSocialLoginEnabled() ) { %>
       <button class="loginButton loginButtonSocial locationbar-social-signin-button" id="locabar_loginButton"  onclick="if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/social/login.jsp', height: 580, opacity: .5}) }">Sign In</button>
    <% } else { %>
    	<button class="loginButton" id="locabar_loginButton">log in</button>
    <% } %>
</tmpl:put>

<tmpl:put name="signupButton"><%
	if (FDStoreProperties.isLightSignupEnabled()) {
		if ( FDStoreProperties.isSocialLoginEnabled() ) {
			%><button class="signUpButton locationbar-social-create-account-button" onclick="if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/social/signup_lite.jsp', height: 590, opacity: .5}) }">Create Account</button><% 
		} else {
			%><button class="signUpButton" onclick="if (FreshDirect && FreshDirect.components && FreshDirect.components.ifrPopup) { FreshDirect.components.ifrPopup.open({ url: '/registration/signup_lite.jsp', width: 480, height: 600, opacity: .5}) }">create account</button><%
		}
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
			%><tmpl:put name="hoicon"><a class="home green" href="/index.jsp">Home delivery?</a></tmpl:put><%			
		}else {
			%><tmpl:put name="hoicon"><a class="office green" href="/department.jsp?deptId=COS">Office delivery?</a></tmpl:put><%			
		}
	}
%>

</tmpl:insert>
