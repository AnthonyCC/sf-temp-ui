<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.delivery.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.delivery.ExceptionAddress" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<style>
	#exceptions_table td{
		padding-left:10px;
		padding-right:10px;
	}
    table.exceptions_table {
        border-width: 1px 1px 1px 1px;
        border-spacing: 2px;
        border-style: outset outset outset outset;
        border-color: gray gray gray gray;
        border-collapse: collapse;
        background-color: white;
    }
    table.exceptions_table th {
        border-width: 1px 1px 1px 1px;
        padding: 1px 1px 1px 1px;
        border-style: inset inset inset inset;
        border-color: gray gray gray gray;
        background-color: white;
        -moz-border-radius: 3px 3px 3px 3px;
    }
    table.exceptions_table td {
        border-width: 1px 1px 1px 1px;
        padding: 1px 1px 1px 1px;
        border-style: inset inset inset inset;
        border-color: gray gray gray gray;
        background-color: white;
        -moz-border-radius: 3px 3px 3px 3px;
    }
</style>
<script language="JavaScript">
    function submitAddressForm(address, zip){
        document.addressSearch.action.value = "deleteGeocodeException";
        document.addressSearch.addressId.value = address;
        document.addressSearch.zip.value = zip;
        document.addressSearch.submit();
        return true;
    }
    function linkGeocodePostion(){
        var street1 = document.getElementById('streetAddress').value;
        var zipVal = document.getElementById('zip').value;
        if(street1.length == 0 || zipVal.length == 0) {
        	alert('Street and ZipCode are required');
        } else {
        	javascript:pop('http://mygeoposition.com/loc/'+street1+' '+zipVal+'/?zoomLevel=17&mapType=', 600,800)
        }                
    }
</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Geocode Exceptions</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page="/includes/supervisor_nav.jsp" />
<div style="background-color: #FFFFFF;">
    <div style="float:left;width:30%; background-color: #FFFFFF;">
        <div class="sub_nav">
            <span class="sub_nav_title">Add Geocode Exception</span>
        </div>
        <%if(request.getParameter("success") != null){ %>
            <h3>Entry added</h3>
        <% } %>
        <% String action = request.getParameter("action");%>
        <fd:GeocodeException result="result" actionName="<%=action%>" successPage="geocode_exception.jsp?success=true">
            
            <fd:ErrorHandler result='<%=result%>' name='mainError' id='errorMsg'>
                <span class="error_detail"><%=errorMsg%></span><br><br>
            </fd:ErrorHandler>
            
            <form method="post">
            <input type="hidden" name="action" value="addGeocodeException"/>
            <table>
                <tr>
                    <td>Street Address</td>
                    <td><input type="text" id="streetAddress" name="streetAddress" value="<%=request.getParameter("streetAddress")%>"></td>
                    <fd:ErrorHandler result='<%=result%>' name='streetAddress' id='errorMsg'>
                        <td><span class="error_detail"><%=errorMsg%></span></td>
                    </fd:ErrorHandler>
                </tr>
                
                <tr>
                    <td>Zip</td>
                    <td><input type="text" size="5" id="zip"  name="zip" value="<%=request.getParameter("zip")%>">
                    <fd:ErrorHandler result='<%=result%>' name='zip' id='errorMsg'>
                        <td><span class="error_detail"><%=errorMsg%></span></td>
                    </fd:ErrorHandler>
                </tr>
                
                <tr>
                    <td align="center">
                        <br>
                        <a href='javascript:pop("<%=FDStoreProperties.getGeocodeLink()%>", 400,720)'>
                            Geocode
                        </a>
                        <br><br>
                    </td>
                    <td align="center">
                        <br>
                        <a href='javascript:linkGeocodePostion()'>
                            Geocode(Recommended)
                        </a>
                        <br><br>
                    </td>
                </tr>
                
                <tr>
                    <td>Longitude</td>
                    <td><input type="text" size="10" name="longitude" value="<%=request.getParameter("longitude")%>">
                    <fd:ErrorHandler result='<%=result%>' name='longitude' id='errorMsg'>
                        <td><span class="error_detail"><%=errorMsg%></span></td>
                    </fd:ErrorHandler>
                </tr>
                
                <tr>
                    <td>Latitude</td>
                    <td><input type="text" size="10" name="latitude" value="<%=request.getParameter("latitude")%>">
                    <fd:ErrorHandler result='<%=result%>' name='latitude' id='errorMsg'>
                        <td><span class="error_detail"><%=errorMsg%></span></td>
                    </fd:ErrorHandler>
                </tr>
                
            </table>
                <br>
            <input type="submit" name="submitAddress" value="ADD EXCEPTION" class="submit">
            </form>
        </fd:GeocodeException>
        <img height="230px" width="1px" border="0" src="spacer.gif">
    </div>

    <div style="float:left;padding-left:10px; margin-left:10px;border-left: 1px solid; background-color: #FFFFFF;">
        <div class="sub_nav"><span class="sub_nav_title">View existing exceptions</span></div><br>
            <form name="addressSearch" method="post">
                Street Address: <input type="text" name="srchAddress" value='<%=request.getParameter("srchAddress")%>'>&nbsp;&nbsp;&nbsp;&nbsp;Zip:&nbsp;<input type="text" name="srchZip" value='<%=request.getParameter("srchZip")%>'>&nbsp;&nbsp;&nbsp;<input type="submit" value="Search"/>
                <input type="hidden" name="action" value="searchGeocodeExceptions"/>
                <input type="hidden" name="addressId" value="" >
                <input type="hidden" name="zip" value="" >
            </form>
            <br>
            <div style="height:400px; overflow:auto;">
                <fd:AddressExceptionSearch id='exceptions' address='<%=request.getParameter("srchAddress")%>' zipcode='<%=request.getParameter("srchZip")%>'>
                    <table id="exceptions_table">
                    <%if(!exceptions.isEmpty()){ %>
                    <tr><th>Address</th><th>Zip</th><th>Latitude</th><th>Longitude</th><th>&nbsp;</th></tr> 
                    <% } %>
                <%for(Iterator i = exceptions.iterator(); i.hasNext();){
                          ExceptionAddress ea = (ExceptionAddress) i.next();
                    %>
                    <tr>
                        <td><%=ea.getScrubbedAddress()%></td>
                        <td><%=ea.getZip()%></td>
                        <td><%=ea.getLatitude()%></td>
                        <td><%=ea.getLongitude()%></td>
                        <td><a href="javascript:submitAddressForm('<%=ea.getScrubbedAddress()%>', '<%=ea.getZip()%>')">REMOVE</a></td>
                    </tr>
                    <%  }%>
                    </table>
                </fd:AddressExceptionSearch>
            </div>
        </div>
        <br clear="all">
    </div>
    
</div>
</tmpl:put>

</tmpl:insert>