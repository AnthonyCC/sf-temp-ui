<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.common.address.AddressModel' %>
<%@ page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:LocationHandler/>
<%
AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
%>
<html>
	<head>
		<fd:css href="/assets/css/common/more_info.css" />
	</head>
<% if(user.isCorporateUser()){  %>
	<body class="cos">
<% } else { %>
	<body>	
<% } %>
		<div id="header">Please enter your street address so that we can make sure your building is in our delivery zone.</div>
		<form>
			<fieldset>
				<label>* Street Address</label><input type="text" id="address1" name="address1">
				<div class="errorlabel" id="address1-error"></div> 
				<label>* City</label><input type="text" id="city" name="city"><br>
				<div class="errorlabel" id="city-error"></div> 
				<label>* State</label><input type="text" id="state" name="state" maxlength="2"><br>
				<div class="errorlabel" id="state-error"></div> 
				<label>* Zip/Postal code</label><input type="text" id="zipcode" name="zipcode" maxlength="5" value="<%= selectedAddress.getZipCode() %>">
				<div class="errorlabel" id="zipcode-error" ></div> 
				<input id="chkaddress" type="image" src="/media_stat/images/locationbar/checkaddress.png">
			</fieldset>
			<div class="right">
				<fieldset>
					<label>Floor/Suite #</label><input type="text" id="apartment" name="apartment"><br>
				</fieldset>
			</div>
		</form>
		<div id="footer"><span>Already have an account with FreshDirect?</span><a href="/login/login.jsp" target="_top" id="login"></a><a href="#" target="_top" id="continue"><img src="/media_stat/images/locationbar/more_info_continue.png"></a></div>
		<fd:javascript src="/assets/javascript/jquery/1.7.2/jquery.js" />
		<fd:javascript src="/assets/javascript/more_info.js" />
	</body>
</html>