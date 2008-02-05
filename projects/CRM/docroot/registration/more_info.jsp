<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Customer > More Information Required</tmpl:put>

<% 
    String address1 = request.getParameter("address1");
    String zipcode = request.getParameter("zipcode");
    String apartment = request.getParameter("apartment");
    String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
    
   	String city = NVL.apply(request.getParameter("city"), "");
	String state = NVL.apply(request.getParameter("state"), "");

    
    if ((zipcode == null) || "".equals(zipcode)) {
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        if (user != null) {
            zipcode = user.getZipCode();
        }
    }
    if (zipcode == null) zipcode = "";
    String failurePage = "delivery.jsp?successPage=nw_cst_enter_details.jsp&serviceType="+serviceType;
    
%>
<fd:SiteAccessController action='checkByAddress' successPage='nw_cst_enter_details.jsp' moreInfoPage='more_info.jsp' failureHomePage='<%=failurePage%>' result='result'>


<tmpl:put name='content' direct='true'>

<div class="sub_nav">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="MIDDLE">
		<TD WIDTH="50%">&nbsp;<b>Create New Customer: 1a Extra Info</b></TD>
		<TD WIDTH="50%" CLASS="error" ALIGN="RIGHT"><fd:ErrorHandler result='<%= result %>' name='notInZone' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler><fd:ErrorHandler result='<%= result %>' name='DeliveryServerDown' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></TD>
	</TR>
</TABLE>
</div>

<div class="register_header">&nbsp;&nbsp;More information requred: Please enter the street address to make sure the building is in our zone.</div>
<div class="content_fixed" style="padding-top: 12px;">
<TABLE WIDTH="40%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="register">
	<form name="site_access_address_check" method="post">
		<tr>
			<td ALIGN="RIGHT">* Street Address&nbsp;</td>
			<td><input type="text" class="text11" maxlength="50" size="21" name="address1" value="<%=address1%>"> <fd:ErrorHandler result='<%=result%>' name='address1' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
                <tr>
			<td ALIGN="RIGHT">Apartment&nbsp;</td>
			<td><input type="text" class="text11" maxlength="50" size="21" name="apartment" value="<%=apartment%>"> <fd:ErrorHandler result='<%=result%>' name='apartment' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
        <tr>
			<td ALIGN="RIGHT">* City&nbsp;</td>
			<td><input type="text"  maxlength="16" class="text9" size="10" name="city" value="<%=city%>"> <fd:ErrorHandler result='<%=result%>' name='state' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td></tr>		

        <tr>
			<td ALIGN="RIGHT">* State&nbsp;</td>
			<td><input type="text"  maxlength="2" class="text9" size="2" name="state" value="<%=state%>"> <fd:ErrorHandler result='<%=result%>' name='state' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td></tr>		
		<tr>
			<td ALIGN="RIGHT">* Zip/Postal Code&nbsp;</td>
			<td><input type="text"  maxlength="5" class="text9" size="6" name="zipcode" value="<%=zipcode%>"> <fd:ErrorHandler result='<%=result%>' name='zipcode' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td></tr>
		<tr>
		<td></td><td><br><input type="submit" name="address_check" value="CHECK ADDRESS" class="submit"></td></tr>
		<TR>
		<TD colspan="2"><fd:ErrorHandler result='<%=result%>' name='ambigiousAddress' id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
		</tr>
	</TR>
	</form>
</TABLE><br><br>
</div>
</tmpl:put>

</fd:SiteAccessController>

</tmpl:insert>
