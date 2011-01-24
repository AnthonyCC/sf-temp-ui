<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.delivery.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.delivery.ExceptionAddress" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<style>
	#exceptions_table td{
		padding-left:10px;
		padding-right:10px;
	}
</style>
<script language="JavaScript">
    function submitAddressForm(addressId){
        document.addressSearch.action.value = "deleteException";
        document.addressSearch.addressId.value = addressId;
        document.addressSearch.submit();
        return true;
    }
</script>

<% String action = request.getParameter("action");%>
<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Address Exceptions</tmpl:put>

<tmpl:put name='content' direct='true'>



<div class="content_fixed" style="padding-left: 1%;">


	<div style="float:left; background-color: #FFFFFF;">
	<div class="sub_nav"><span class="sub_nav_title">Add a valid address to the system</span></div>
		<%if(request.getParameter("success") != null){%>
			<h3>Entry added</h3>
		<%}%>
	<fd:ZipPlus4AddressException result="result" actionName="<%=action%>" successPage="address_exception.jsp?success=true">
		<table>
		<form method="post">
		<input type="hidden" name="action" value="addException"/>
			<tr>
				<td>Street Address</td>
				<td><input type="text" name="streetAddress" value="<%=request.getParameter("streetAddress")%>"></td>
				<fd:ErrorHandler result='<%=result%>' name='streetAddress' id='errorMsg'>
			<td><span class="error_detail"><%=errorMsg%></span></td>
				</fd:ErrorHandler>
			</tr>
			
			<tr>
				<td>Apt Range</td>
				<td><input type="text" name="aptNumLow" size="5" value="<%=request.getParameter("aptNumLow")%>">&nbsp;to&nbsp;<input type="text" name="aptNumHigh" size="5" value="<%=request.getParameter("aptNumHigh")%>"></td>
				<fd:ErrorHandler result='<%=result%>' name='apt' id='errorMsg'>
			<td><span class="error_detail"><%=errorMsg%></span></td>
				</fd:ErrorHandler>
			</tr>
			
			<tr>
				<td>City</td>
				<td><input type="text" size="28" name="city" value="<%=request.getParameter("city")%>">
				<fd:ErrorHandler result='<%=result%>' name='city' id='errorMsg'>
			<td><span class="error_detail"><%=errorMsg%></span></td>
				</fd:ErrorHandler>
			</tr>
			
			<tr>
				<td>State</td>
				<td>
					<select name="state" class="pulldown">
						<option value="NY" <%= "NY".equalsIgnoreCase(request.getParameter("state")) ? "selected" : "" %>>NY</option>
						<option value="NJ" <%= "NJ".equalsIgnoreCase(request.getParameter("state")) ? "selected" : "" %>>NJ</option>
						<option value="CT" <%= "CT".equalsIgnoreCase(request.getParameter("state")) ? "selected" : "" %>>CT</option>
					</select>
				</td>
			</tr>
			
			<tr>
				<td>Zip</td>
				<td><input type="text" size="5" name="zip" value="<%=request.getParameter("zip")%>">
				<fd:ErrorHandler result='<%=result%>' name='zip' id='errorMsg'>
			<td><span class="error_detail"><%=errorMsg%></span></td>
				</fd:ErrorHandler>
			</tr>
			
			</table>
			<br>
			Apartment Type:&nbsp;&nbsp;<fd:ErrorHandler result='<%=result%>' name='aptType' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler><br>
				<%for(Iterator i = EnumAddressType.iterator(); i.hasNext();){
					EnumAddressType addressType = (EnumAddressType) i.next();
					String selected = "";
					if(addressType.getName().equals(request.getParameter("aptType"))){
						selected = "checked";
					}
						%>
					<input type="radio" name="aptType" value="<%=addressType.getName()%>" <%=selected%>><%=addressType.getDescription()%>&nbsp;&nbsp;<i><%=addressType.getExplanation()%></i><br>
				<%}%>
			<br>
			Reason For Adding:&nbsp;&nbsp;<fd:ErrorHandler result='<%=result%>' name='reason' id='errorMsg'><span class="error_detail"><%=errorMsg%></span></fd:ErrorHandler><br>
			<%for(Iterator i = EnumAddressExceptionReason.iterator(); i.hasNext();){
				EnumAddressExceptionReason reason = (EnumAddressExceptionReason) i.next();
				String selected = "";
				if(reason.getName().equals(request.getParameter("reason"))){
					selected = "checked";
				}
			%>
				<input type="radio" name="reason" value="<%=reason.getName()%>" <%=selected%>><%=reason.getDescription()%><br>
			<%}%>
			
		<br>
		<input type="submit" name="submitAddress" value="ADD ADDRESS" class="submit">
		</form>
		</fd:ZipPlus4AddressException>
	</div>

	<div style="float:left;padding-left:10px; margin-left:10px;border-left: 1px solid; background-color: #FFFFFF;">
	<div class="sub_nav"><span class="sub_nav_title">View existing exceptions</span></div><br>
		<form name="addressSearch" method="post">
			Street Address: <input type="text" name="srchAddress" value='<%=request.getParameter("srchAddress")%>'>&nbsp;&nbsp;&nbsp;&nbsp;Zip:&nbsp;<input type="text" name="srchZip" value='<%=request.getParameter("srchZip")%>'>&nbsp;&nbsp;&nbsp;<input type="submit" value="Search"/>
			<input type="hidden" name="action" value="searchAddressExceptions"/>
            <input type="hidden" name="addressId" value="" >
		</form>
		<br>
		<div style="height:400px; overflow:auto;  background-color: #FFFFFF;">
		<fd:AddressExceptionSearch id='exceptions' address='<%=request.getParameter("srchAddress")%>' zipcode='<%=request.getParameter("srchZip")%>'>
		<table id="exceptions_table">
        <%for(Iterator i = exceptions.iterator(); i.hasNext();){
              ExceptionAddress ea = (ExceptionAddress) i.next();
        %>
        <tr>
            <td><%=ea.getStreetAddress()%></td>
            <td><%=ea.getAptNumLow()%></td>
            <td><%=ea.getAptNumHigh()%></td>
            <td><%=ea.getZip()%></td>
            <td><%=ea.getAddressType().getDescription()%></td>
            <td><a href="javascript:submitAddressForm('<%=ea.getId()%>')">REMOVE</a></td>
        </tr>
        <%  }%>
		</table>
		</fd:AddressExceptionSearch>
        <br><br>
		</div>
	</div>
<br clear="all">
</div>

</tmpl:put>

</tmpl:insert>