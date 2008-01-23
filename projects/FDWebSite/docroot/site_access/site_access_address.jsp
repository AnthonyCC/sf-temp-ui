<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>

<%
    String successPage = request.getParameter("successPage");
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
    boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	String moreInfoPage = "site_access_address.jsp?successPage="+ URLEncoder.encode(successPage);
	String failurePage = "delivery.jsp?successPage="+ URLEncoder.encode(successPage);
	String failureCorporatePage	= "/survey/cos_site_access_survey.jsp?successPage="+ URLEncoder.encode(successPage);
%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML>
<HEAD>
<TITLE><%= isBestCellars ? "Best Cellars" : "FreshDirect"%></TITLE>
<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</HEAD>
<BODY BGCOLOR="White" TEXT="#333333" class="text10" leftmargin=0 topmargin=0>
<fd:SiteAccessController action='checkByAddress' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' failureCorporatePage='<%= failureCorporatePage %>' result='result'>
<%
	String fldAddress1 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "");
	String fldAddress2 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "");
	String fldApartment = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "");
	String fldCity = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "");
	String fldState = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "");
	String fldZipCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");

    if ("".equals(fldZipCode)) {
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        if (user != null) {
            fldZipCode = NVL.apply(user.getZipCode(), "");
        }
    }
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
<tr valign="MIDDLE">
	<td width="100%" ALIGN="CENTER">
	
		<table border="0" cellpadding="0" cellspacing="0" >
			<form name="address" method="post" action="<%= request.getRequestURI() %>">
			<input type="hidden" name="successPage" value="<%= successPage %>">
			<input type="hidden" name="serviceType" value="<%= serviceType %>">
		<tr valign="TOP">

			<td  valign="top" <%= isCorporate ? "width=\"450\" align=\"center\" class=\"bodyCopy\"" : "width=\"400\" class=\"text12\""%>>
				<% if (isBestCellars) { %>
					<img src="/media_stat/images/template/wine/bc_logo_home_original.gif" width="336" height="52" alt="Best Cellars">
				<% } else { %>
					<% if (isCorporate) { %>
					<img src="/media_stat/images/logos/fd_cos_logo.gif" width="232" height="67" border="0" alt="FreshDirect At The Office" vspace="6">
					<% } else { %>
					<img src="/media_stat/images/logos/fd_logo_lg.gif" width="245" height="52" border="0" alt="FreshDirect">
					<% } %>
				<% } %>
				<br>
				<img src="/media_stat/images/layout/<%= isCorporate ? "999966" : "clear"%>.gif" width="<%= isCorporate ? "450" : "400"%>" height="1" border="0"><br>
				<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>

					<% if (isCorporate) { %>
						<b>FreshDirect At The Office</b> provides corporate customers with one-stop online shopping for all their corporate office needs, eliminating the need for multiple vendors. To find out more <a href="javascript:popup('/cos_info.jsp','small')">click here</a>.
<br><br>
Please enter your street address so that we can make sure your building is in a FreshDirect At The Office zone.
					<% } else { %>
						<font class="text12bold">We need more information...</font><br>
						Please enter your street address so that we can make sure your building is in our zone. 
					<% } %>
						<br><br>
						<fd:ErrorHandler result='<%=result%>' name='cantGeocode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br><br></fd:ErrorHandler>
						
						<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode()%>' id='errorMsg'>
							<%@ include file="/shared/includes/messages/i_error_suggested_address.jspf" %>
						</fd:ErrorHandler>

						<table border="0" cellspacing="01" cellpadding="0" width="400">
						<tr valign="top">
							<td width="130" ALIGN="RIGHT" class="bodyCopy">* Street Address</td>
							<td><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt=""><input type="text" class="text11" maxlength="50" size="21" name="<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>" value="<%=fldAddress1%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>' id='errorMsg'><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
							<td align="right" class="bodyCopy">&nbsp;&nbsp;Apt./Ste.#</td>
							<td><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt=""><input type="text" class="text11" maxlength="10" size="8" name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>" value="<%=fldApartment%>"><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>" id='errorMsg'><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
						</tr>
						<tr><td colspan="4"></td></tr>
						<tr valign="top">
							<td width="130" ALIGN="RIGHT" class="bodyCopy">* Zip/Postal Code</td>
							<td colspan="3"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt=""><input type="text"  maxlength="5" class="text9" size="6" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=fldZipCode%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
							</td>
							</tr>
						<tr valign="MIDDLE">
							<td colspan="4" <%= isCorporate ? "align=\"center\"" : ""%>>
								<br>
									<input type="image" src="/media_stat/images/buttons/check_my_address.gif" height="16" width="112" name="address_check" vspace="4" hspace="4" value="Check My Address" border="0">		
							</td>
						</tr>
						</form>
						</table>
						
						<fd:ErrorHandler result='<%=result%>' name='cantGeocode' id='errorMsg'>
							<table border="0" cellpadding="1" cellspacing="0" width="350">
							<tr>
								<td width="300" class="bodyCopy">
									To check out, you'll need to enter a valid street address, but to browse our site now, click below.
									<br>
									<form name="site_access" method="post" action="<%= response.encodeURL("/site_access/delivery.jsp?successPage=") +URLEncoder.encode(successPage) %>">
										<input type="image" src="/media_stat/images/template/site_access/just_let_me_in.gif" width="112" height="16"  alt="" border="0" value="submit">
									</form>
								</td>
							</tr>
							</table>
						</fd:ErrorHandler>
						
						<% if (!isBestCellars) { %>
						<%= isCorporate ? "<img src=\"/media_stat/images/layout/999966.gif\" width=\"450\" height=\"1\" border=\"0\" align=\"center\">" : "" %>
							<table border="0" cellpadding="1" cellspacing="0" width="300">
							<tr>
								<td width="<%= isCorporate?"450":"300"%>" class="bodyCopy">
									<span class="text12bold">Already have an account with FreshDirect?</span><br>
									<a href="/login/login_main.jsp">Click here to log in</a>.
								</td>
							</tr>
							</table>
						<% } %>
			
			</td>
			<% if (!isCorporate) { %>
			<td width="10"><img src="/media_stat/images/layout/clear.gif" width="10" height="1" border="0"></td>			
			<td width="160"><img src="/media_stat/images/template/homepages/home_grapes.jpg" width="170" height="141" border="0" alt="Grapes"></td>
			<% } %>
		</tr>
		</table>
		
	</td>
</tr>
</table>

</fd:SiteAccessController>
</body>
</html>
