<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.logistics.delivery.model.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDDeliveryApartmentRange' %>

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

<div id="geocode_address" class="home_module home_module home_search_module_container" style="">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="module_header" style="height: 2.2em;">
	<tr>
		<td class="home_module_header_text">
			Address Validation 
			<a href="javascript:popResizeHelp('<%= FDStoreProperties.getCrmAddressValiationHelpLink() %>','715','940','kbit')" onmouseover="return overlib('Click for Address Validation Help.', AUTOSTATUS, WRAP);" onmouseout="nd();" class="help">?</a> 
			&nbsp; <span class="home_search_module_field" style="font-weight: normal;">*Required</span>&nbsp;
		</td>
		<td align="right" class="home_search_module_field">
			<a href="http://www.usps.com/zip4/" target="usps" class="home_search_module_field" title="USPS ZIP check" style="color:#003399; text-decoration:none;">
				<span style="color:#CC0000;">&raquo;</span> USPS
			</a>
		</td>
	</tr>
	</table>
	<div id="monitor_content" class="home_search_module_content" style="height: 20em; padding-top: 0px; padding-bottom: 0px; overflow-y: auto;">
		<form name="check_address" method="POST"><span class="space4pix"><br /><br /></span>
		<input type="hidden" name="checkAddressSubmit" value="addressCheck">
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
				<table width="80%" bgcolor="#EEEEEE" cellpadding="2" cellspacing="0" style="margin-bottom:8px; border: solid 1px #999999;" align="center">
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
	
		<% if (addressOK) { %>
		<span class="correct"><b>ADDRESS_OK</b></span><br>
		<% } %>
		<% if (geocodeOK) { %>
		<span class="correct"><b>GEOCODE_OK</b></span><br>
		<% } %>
	</td>
					<td style="width: 25px;" align="right">
						<span class="module_header_note"><b>LAT:</b></span><br />
						<span class="module_header_note"><b>LON:</b></span>
					</td>
					<td  style="width: 75px;" align="right">
						<%= CCFormatter.formatLatLong(latitude) %><br />
						<%= CCFormatter.formatLatLong(longitude) %>
					</td>
					<td style="width: 55px;" align="right"><a href="https://www.google.com/maps/search/<%= CCFormatter.formatLatLong(latitude) %>,<%= CCFormatter.formatLatLong(longitude) %>" target="_gmap" title="View on Google Maps" class="ui-icon-cc ui-icon-cc-gmap"><span>Google Maps</span></a></td>
</tr>
</table>
<% } %>
<input type="hidden" name="action" value="geocode">
			<table cellpadding="0" cellspacing="2" class="home_search_module_field" border="0" width="100%">
	<tr>
				    <td width="90"></td>
				    <td width="140"></td>
				    <td width="75"></td>
				    <td width="30"></td>
				    <td width=""></td>
				</tr>
				<tr>
				    <td>*&nbsp;Address</td>
					<td colspan="2"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_ADDRESS_1.getCode() %>" value="<%= dlvAddress.getScrubbedStreet() == null || "".equals(dlvAddress.getScrubbedStreet()) ? dlvAddress.getAddress1() : dlvAddress.getScrubbedStreet() %>"></td>
		<td align="right">Apt. </td>
			        <td><input type="text" class="input_text" size="5" name="<%= EnumUserInfoName.DLV_APARTMENT.getCode() %>" value="<%= dlvAddress.getApartment() %>"> <fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_APARTMENT.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></td>
	</tr>
	
	<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_ADDRESS_1.getCode() %>' id='errorMsg'>
		<tr><td></td><td colspan="5"><span class="error_detail"><%=errorMsg%></span></td></tr>
	</fd:ErrorHandler>
	
	<tr>
				    <td>&nbsp;&nbsp;Addr.&nbsp;Line&nbsp;2</td>
		<td colspan="4"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_ADDRESS_2.getCode() %>" value="<%= dlvAddress.getAddress2() %>"> <fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_ADDRESS_2.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler></td>

	</tr>
	<tr>
				    <td>*&nbsp;City</td>
		<td colspan="4"><input type="text" class="input_text" name="<%= EnumUserInfoName.DLV_CITY.getCode()%>" required="true" value="<%= dlvAddress.getCity() %>"></td>
	</tr>
	<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_CITY.getCode() %>' id='errorMsg'>
		<tr>
		    <td align="right"></td>
			<td colspan="4"><span class="error_detail"><%=errorMsg%></span></td>
		</tr>
	</fd:ErrorHandler>
	<tr>
					<td>*&nbsp;State</td>
		<td><select name="<%= EnumUserInfoName.DLV_STATE.getCode()%>" class="pulldown">
            <option value="NY" <%= "NY".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>NY</option>
			<option value="NJ" <%= "NJ".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>NJ</option>
			<option value="CT" <%= "CT".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>CT</option>
			<option value="PA" <%= "PA".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>PA</option>			
			<option value="DC" <%= "DC".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>DC</option>
			<option value="DE" <%= "DE".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>DE</option>
			<option value="MD" <%= "MD".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>MD</option>
			<option value="VA" <%= "VA".equalsIgnoreCase(dlvAddress.getState()) ? "selected" : "" %>>VA</option>			
  		</select>
		<fd:ErrorHandler result='<%=result%>' name='<%= EnumUserInfoName.DLV_STATE.getCode() %>' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler>
		</td>
					<td align="right">*&nbsp;Zip&nbsp;Code</td>
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
			<% if (/*currentAgent.isSupervisor() && */!CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"skipAddApartment")&& addApartment) {%>
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
<% List<FDDeliveryZoneInfo> zoneInfo=(List<FDDeliveryZoneInfo>)pageContext.getAttribute("zoneInfo");
   String county=(String)pageContext.getAttribute("county");
   String COSStatus="", zoneCode="", bulkZone="", facility="";
   if(zoneInfo!=null){
	   for(FDDeliveryZoneInfo zInfo : zoneInfo){
			if(!zInfo.isBulkZone()){ 
	 				if(zInfo.getZoneCode()!=null && !" ".equals(zInfo.getZoneCode())){
	 					zoneCode = zInfo.getZoneCode();
	 				}
	 				if (zInfo.isCosEnabled()){
		  				COSStatus="YES";
		  		  	}else{
		  				COSStatus="NO";
		  		  	}
		  	}else{ 
		  		if(zInfo.getZoneCode()!=null && !" ".equals(zInfo.getZoneCode())){
		  			bulkZone = zInfo.getZoneCode();
					}
			}
			if (zInfo.getFulfillmentInfo() != null) {
				facility = zInfo.getFulfillmentInfo().getPlantCode();
			}
		}
   }
   %>
		<table align="center" cellspacing="4">
 		<tr>
			<% if(zoneCode!="") { %>
				<td>(Zone Code=<%=zoneCode%>)</td>
				<% if (bulkZone != "" || true) { %>
			 		<td><span class="bulkZone"><%= bulkZone %></span></td>
 				<% } %> 
				<td>(County:&nbsp;<%= county %>)</td>
				<td><span class="cosEnabled">(COS Enabled:&nbsp;<%= COSStatus %>)</span></td>
			<% } %>
			
		</tr>
  		</table>
  
<% availServices = (Set)pageContext.getAttribute("availServices");
   EnumDeliveryStatus deliveryStatus=(EnumDeliveryStatus)pageContext.getAttribute("deliveryStatus");

   if(availServices != null) {
	   if(availServices.size() > 0){ %>
	   <table align="center">
	       <tr><td>Available Services:</td><td>&nbsp;&nbsp;</td>
	           <td>
	            <% for(Iterator svc = availServices.iterator(); svc.hasNext(); ) { 
	                EnumServiceType s = (EnumServiceType) svc.next(); 
	                if ("CORPORATE".equals(s.getName()) && EnumDeliveryStatus.COS_ENABLED.equals(deliveryStatus)){  %>
	              		<span class="correct">(<%= s.getName() %>)</span>
	              	<% } else if ("FDX".equals(s.getName())) { %>
		              	(<%= s.getName() %>)<% if (facility != null && facility != "") { %>&nbsp;(Facility:&nbsp;<%= facility %>)<% } %>
	              	<% } else { %>
	              		(<%= s.getName() %>) 
	            	<% }
				} %>
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
            	FDDeliveryApartmentRange range = (FDDeliveryApartmentRange) rIter.next(); %>
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

		</form>
	</div>
</div>