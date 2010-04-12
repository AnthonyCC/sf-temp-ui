<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>


<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<% 

String actionName = ""; 
if ("post".equalsIgnoreCase(request.getMethod()) && request.getParameter("addApartment") != null) {
	actionName = "addApartment";
} else if ("post".equalsIgnoreCase(request.getMethod()) && request.getParameter("checkAddressSubmit") != null) {
	actionName = "addressCheck";
}
%>
<crm:GetCurrentAgent id='currentAgent'>
<fd:ZipPlus4Address actionName='<%=actionName%>' result='result' id='dlvAddress'>
<%
	boolean addressOK = "post".equalsIgnoreCase(request.getMethod()) && ("addressCheck".equals(actionName) || "addApartment".equals(actionName)) && !result.hasError("dlv_address");
	
	boolean geocodeOK = "post".equalsIgnoreCase(request.getMethod()) && ("addressCheck".equals(actionName) || "addApartment".equals(actionName)) && !result.hasError("dlv_address_geocode");

	boolean addApartment = false;
	boolean suggestAddress = false;
        
	double longitude = 0;
    double latitude = 0;
    if(dlvAddress != null){
		longitude = dlvAddress.getLongitude();
        latitude = dlvAddress.getLatitude();
	}
	//handle empty apt error when add apartment performed:
%>
<% if ("post".equalsIgnoreCase(request.getMethod()) && !"".equals(actionName)) { %>
		<fd:ErrorHandler result='<%=result%>' name='main_error' id='errorMsg'>
			<span class="error"><%=errorMsg%></span>
		</fd:ErrorHandler>
<table width="80%" bgcolor="#EEEEEE" cellpadding="3" cellspacing="0" style="margin-bottom:8px; border: solid 1px #999999;" align="center">
<tr>
	<td>
		<fd:ErrorHandler result='<%=result%>' name='dlv_address' id='errorMsg'>
                <span class="error"><%=errorMsg%>
		<% if (errorMsg.equalsIgnoreCase(EnumAddressVerificationResult.ADDRESS_BAD.getCode())) {
				geocodeOK = false;
			} else if (errorMsg.equalsIgnoreCase(EnumAddressVerificationResult.APT_WRONG.getCode()) && !"".equals(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()))) {
				addApartment = true;
			} else if (errorMsg.equalsIgnoreCase(EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE.getCode())){
				suggestAddress = true;
			}
		%></span><br>

		</fd:ErrorHandler>
		
		<fd:ErrorHandler result='<%=result%>' name='dlv_address_geocode' id='errorMsg'><span class="error"><%=errorMsg%></span><br></fd:ErrorHandler>
	
		<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_APARTMENT.getCode() %>' id='errorMsg'>
			<%
			addressOK = false; 
			geocodeOK = false;
			%>
		</fd:ErrorHandler>
		<% if (addressOK) { %>
		<span class="correct"><b>ADDRESS_OK</b></span><br>
		<% } %>
		<% if (geocodeOK) { %>
		<span class="correct"><b>GEOCODE_OK</b></span><br>
		<% } %>
	</td>
	<td width="60%" align="right"><span class="module_header_note"><b>X:</b></span>  <%= CCFormatter.formatLatLong(longitude) %><br><span class="module_header_note"><b>Y:</b></span> <%= CCFormatter.formatLatLong(latitude) %></td>
</tr>
</table>
<% } %>
<table border="0" cellspacing="3" cellpadding="0" align="center" class="home_search_module_field">
<input type="hidden" name="action" value="geocode">
	<tr>
	    <td align="right">* Address</td>
		<td colspan="2"><input type="text" style="width: 150px;" class="input_text" name="<%= EnumUserInfoName.DLV_ADDRESS_1.getCode() %>" value="<%= dlvAddress.getScrubbedStreet() == null || "".equals(dlvAddress.getScrubbedStreet()) ? dlvAddress.getAddress1() : dlvAddress.getScrubbedStreet() %>"></td>
		<td align="right">Apt. </td>
        <td>&nbsp<input type="text" class="input_text" size="5" name="<%= EnumUserInfoName.DLV_APARTMENT.getCode() %>" value="<%= dlvAddress.getApartment() %>"> <fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_APARTMENT.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
	
	<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_ADDRESS_1.getCode() %>' id='errorMsg'>
		<tr><td></td><td colspan="5"><span class="error_detail"><%=errorMsg%></span></td></tr>
	</fd:ErrorHandler>
	
	<tr>
	    <td align="right">Addr. Line 2</td>
		<td colspan="4"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_ADDRESS_2.getCode() %>" value="<%= dlvAddress.getAddress2() %>"> <fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_ADDRESS_2.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></td>

	</tr>
	<tr>
	    <td align="right">* City</td>
		<td colspan="4"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_CITY.getCode()%>" required="true" value="<%= dlvAddress.getCity() %>"></td>
	</tr>
	<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_CITY.getCode() %>' id='errorMsg'>
		<tr>
		    <td align="right"></td>
			<td colspan="4"><span class="error_detail"><%=errorMsg%></span></td>
		</tr>
	</fd:ErrorHandler>
	<tr>
		<td align="right">* State</td>
		<td><select name="<%= EnumUserInfoName.DLV_STATE.getCode()%>" class="pulldown">
            <option value="NY" <%= "NY".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>NY</option>
			<option value="NJ" <%= "NJ".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>NJ</option>
			<option value="CT" <%= "CT".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>CT</option>
  		</select>
		<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_STATE.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
		<td align="right">* Zip Code</td>
        <td colspan="2"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_ZIPCODE.getCode() %>" required="true" size="6" value="<%= dlvAddress.getZipCode() %>"></td>
    </tr>
	<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_ZIPCODE.getCode() %>' id='errorMsg'>
		<tr>
		<td></td>
		<td></td>
        <td colspan="3"><span class="error_detail"><%=errorMsg%></span></td>
    	</tr>
	</fd:ErrorHandler>
    <tr>
        <td colspan="5" align="center"><img src="/media_stat/crm/images/clear.gif" width="1" height="8"><br>
		<input type="submit" name="checkAddress" value="CHECK ADDRESS" class="submit">
                <%=dlvAddress.getAddressType() != null ? "<br>Address Type = " + dlvAddress.getAddressType().getDescription() : ""%> 
			<% if (currentAgent.isSupervisor() && addApartment) {%>
				<br><br>
				<input type="submit" name="addApartment" value="ADD APARTMENT" class="new">
				<br><br>
			<% } %>
		</td>
	</tr>
</table>

<%  
suggestions = (ArrayList)pageContext.getAttribute("suggestions");
if (suggestions != null) {  %>
<table align="center">
    <tr><td><br><i>Suggested addresses:</i></td></tr>
<%      for (Iterator sIter = suggestions.iterator(); sIter.hasNext(); ) {
            AddressModel suggestion = (AddressModel) sIter.next();  %>
    <tr><td>
        <%= suggestion.getAddress1() %> <% if (!"".equals(suggestion.getApartment())) { %>Apt # <%= suggestion.getApartment() %><% } %><br>
        <% if (!"".equals(suggestion.getAddress2())) { %><%= suggestion.getAddress2() %><br><%   } %>
        <%= suggestion.getCity() %> <%= suggestion.getState() %> <%= suggestion.getZipCode() %>
        <br><br>
    </td></tr>
<%      }   %>
</table>
<% } %>
<% DlvZoneInfoModel zoneInfo=(DlvZoneInfoModel)pageContext.getAttribute("zoneInfo");
   String county=(String)pageContext.getAttribute("county");
  
	if(zoneInfo!=null){
%>
  <table align="center">
  	<tr>
  		<td>(Zone Code=<%=zoneInfo.getZoneCode()%>)</td><td>&nbsp;</td>
  		<td>(County:</td>&nbsp;<td><%=county%>)</td>
	  		<%String COSStatus="";
	  		  if(zoneInfo.isCosEnabled()){
	  			COSStatus="YES";
	  		  }else{
	  			COSStatus="NO";
	  		  }
	  		%>
  		<td><span class="correct">(COS Enabled:&nbsp;<%=COSStatus%>)</span></td>
  	</tr>
  </table>
 <%} %>
<% availServices = (Set)pageContext.getAttribute("availServices");
   EnumDeliveryStatus deliveryStatus=(EnumDeliveryStatus)pageContext.getAttribute("deliveryStatus");

   if(availServices != null) {
	   if(availServices.size() > 0){ %>
	   <table align="center">
	       <tr><td>Available Services:</td><td>&nbsp;&nbsp;</td>
	           <td>
	            <% for(Iterator svc = availServices.iterator(); svc.hasNext(); ) { 
	                   EnumServiceType s = (EnumServiceType) svc.next(); 
	                   if("CORPORATE".equals(s.getName()) && EnumDeliveryStatus.COS_ENABLED.equals(deliveryStatus)){  %>
	              			<span class="correct">(<%= s.getName() %>)</span>
	             		<%}else{ %> 			
	              			(<%= s.getName() %>) &nbsp;
	            <% } }%>
	           </td></tr>
	   </table>
<%  } }%>

<% aptRanges = (List)pageContext.getAttribute("aptRanges");
    if (aptRanges != null) { 
		if (aptRanges.size() > 0) {
	%>
<table align="center">
    <tr><td colspan="5" align="center">Valid Apartment Number Ranges:</td></tr>
    <tr><td>&nbsp;&nbsp;</td><td align="center">LOW</td><td>&nbsp;&nbsp;</td><td align="center">HIGH</td><td>&nbsp;&nbsp;</td><td align="center">TYPE</td><td>&nbsp;&nbsp;</td></tr>
<%      	for (Iterator rIter = aptRanges.iterator(); rIter.hasNext(); ) {
            	DlvApartmentRange range = (DlvApartmentRange) rIter.next(); %>
     <tr><td>&nbsp;&nbsp;</td><td align="right"><%= range.getLow() %></td>
         <td>&nbsp;&nbsp;</td><td align="right"><%= range.getHigh() %></td>
           <% if(range.getAddressTypeDetailed() != null) { %>
             <td>&nbsp;&nbsp;</td><td align="right"><SPAN title=" <%= range.getAddressTypeDetailed().getDescription() %> - <%= range.getAddressTypeDetailed().getExplanation() %>" class="popup"><%= range.getAddressType() %></td><td>&nbsp;&nbsp;</td></tr>
           <% } %>
<%      	}   %>
</table>
<%  	} else { %>
	<br>
	<table cellspacing="0" cellpadding="5" align="center" style="border: solid 1px #999999;">
		<tr>
			<td>No Apartment required for this building.</td>
		</tr>
	</table><br>
<% 		}
	} %>
</fd:ZipPlus4Address>
</crm:GetCurrentAgent>
