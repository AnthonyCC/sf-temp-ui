<%@ page import='java.util.*' %>
<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>

<%@ taglib uri="freshdirect" prefix="fd" %>
<fd:SiteAccessController action="checkByAddress" successPage="/index.jsp" moreInfoPage="" failureHomePage="/site_access/delivery.jsp" failureCorporatePage="/survey/cos_site_access_survey.jsp" result="result">
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
	<%--  <title>FreshDirect</title>  --%>
	 <fd:SEOMetaTag title="FreshDirect"/>
	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
	<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
	<style>
		.star {
			color:orange;
		}
	</style>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" text="#333333" class="text10" leftmargin="0" topmargin="0" style="overflow: hidden">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>


	<%
		if(session.getAttribute("LITESIGNUP_COMPLETE") != null) {
			//phew finally complete
			System.out.println("Did not come here  on site_access_address_lite.jsp?====================================================================================");
		%>
			<img src="/media_stat/images/navigation/spinner.gif" class="fleft" />
			<script language="javascript">				
				//	alert('in site_access_address_lite.jsp');
				  //  if (top === window) {
				//		alert("this page is not in an iframe");
					//} else {
						//alert("the url of the top is" + top.location.href + "\nand not the url of this one is " + window.location.href );
					//}
				  top.location.href ="/index.jsp";
			</script>
		<%		 
		} else {		
		
			if(session.getAttribute("morepage") != null) {
				String mPage = "/site_access/signup_lite_more_page.jsp?forward=" + (String) session.getAttribute("morepage");
			%>
				
				<jsp:include page="<%= mPage %>" flush="false"/>
			<%	
				
			
				} else {			

			String serviceType = request.getParameter("serviceType");			
			String successPage = "/index.jsp";
			boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
			boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);

			System.out.println("went to else part on site_access_address_lite.jsp ?====================================================================================\n" );

		String fldAddress1 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "");
		String fldAddress2 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "");
		String fldApartment = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "");
		String fldCity = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "");
		String fldState = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "");
		String fldZipCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");
		
		if (isCorporate) {
			fldZipCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()), "");
		}

		if ("".equals(fldZipCode)) {
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
			if (user != null) {
				fldZipCode = NVL.apply(user.getZipCode(), "");
			}
		}
	%>
	<center>
	<div style="padding: 10px 10px 20px 10px; width: 570px; height: auto;">
			<form name="litesignupaddress" id="litesignupaddress" method="post" action="/site_access/site_access_address_lite.jsp">
				<input type="hidden" name="serviceType" value="<%= serviceType %>" />
				<input type="hidden" name="corpServiceType" value="<%= serviceType %>" />
				<input type="hidden" name="LITESIGNUP" value="true" />
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr valign="top">
						<td  valign="top" <%= isCorporate ? "width=\"450\" align=\"center\" class=\"bodyCopy\"" : "width=\"400\" class=\"text12\""%>>
							<% if (isBestCellars) { %>
								<img src="/media_stat/images/template/wine/bc_logo_home_original.gif" width="336" height="52" alt="Best Cellars" />
							<% } else { %>
								<% if (isCorporate) { %>
								<img src="/media_stat/images/logos/fd_cos_logo.gif" width="232" height="67" border="0" alt="FreshDirect At The Office" vspace="6" />
								<% } else { %>
								<img src="/media_stat/images/logos/fd_logo_lg.gif" width="245" height="52" border="0" alt="FreshDirect" />
								<% } %>
							<% } %>
							<br />
							<img src="/media_stat/images/layout/<%= isCorporate ? "999966" : "clear"%>.gif" width="450" height="1" border="0" /><br />
							<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0" /><br />
							<% if (isCorporate) { %>
								<b>FreshDirect At The Office</b> provides corporate customers with one-stop online shopping for all their corporate office needs, eliminating the need for multiple vendors. To find out more <a href="javascript:popup('/cos_info.jsp','small')">click here</a>.
								<br /><br />
								Please enter your street address so that we can make sure your building is in a FreshDirect At The Office zone.
							<% } else { %>
								<font class="text12bold">We need more information...</font><br />
								Please enter your street address so that we can make sure your building is in our zone. 
							<% } %>
							<br /><br />
							<fd:ErrorHandler result='<%=result%>' name='cantGeocode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br /><br /></fd:ErrorHandler>
							<fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode()%>' id='errorMsg'><%@ include file="/shared/includes/messages/i_error_suggested_address.jspf" %></fd:ErrorHandler>

							<table border="0" cellspacing="1" cellpadding="0" width="100%">
								<% if (isCorporate) { 
									String fldCompanyName 		= NVL.apply(request.getParameter(EnumUserInfoName.DLV_COMPANY_NAME.getCode()), "");
									String fldBusinessPhone 	= request.getParameter("busphone");
								%>
									<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* Company Name</td>
									<td colspan="3" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text" class="text11" maxlength="50" size="21" name="<%= EnumUserInfoName.DLV_COMPANY_NAME.getCode() %>" value="<%=fldCompanyName%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_COMPANY_NAME.getCode()%>' id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
								</tr>
								
									<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* Contact Phone #</td>
									<td colspan="3" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text" class="text11" maxlength="50" size="21" name="<%= EnumUserInfoName.DLV_WORK_PHONE.getCode() %>" value="<%=fldBusinessPhone%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_WORK_PHONE.getCode()%>' id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
								</tr>
									
									
								<% } %>
								<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* Street Address</td>
									<td  ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text" class="text11" maxlength="50" size="21" name="<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>" value="<%=fldAddress1%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>' id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
									<td align="right" class="bodyCopy">&nbsp;&nbsp;Floor/Suite #</td>
									<td ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text" class="text11" maxlength="10" size="8" name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>" value="<%=fldApartment%>"><fd:ErrorHandler result="<%=result%>" name="<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>" id='errorMsg'><br /><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
								</tr>
								<tr><td colspan="4"><!--  --></td></tr>
								<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* City </td>
									<td colspan="3" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text"  maxlength="50" class="text9" size="21" name="<%=EnumUserInfoName.DLV_CITY.getCode()%>" value="<%=fldCity%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_CITY.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</td>
								</tr>
								<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* State </td>
									<td colspan="3" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text"  maxlength="2" class="text9" size="2" name="<%=EnumUserInfoName.DLV_STATE.getCode()%>" value="<%=fldState%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_STATE.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</td>
								</tr>
								<tr valign="top">
									<td width="130" ALIGN="RIGHT" class="bodyCopy">* Zip/Postal Code</td>
									<td colspan="3" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="7" border="0" height="10" alt="" /><input type="text"  maxlength="5" class="text9" size="6" name="<%= EnumUserInfoName.DLV_ZIPCODE.getCode()%>" value="<%=fldZipCode%>"><fd:ErrorHandler result='<%=result%>' name='<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>' id='errorMsg'> <span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
									</td>
									</tr>
								<tr valign="MIDDLE">
									<td colspan="4" <%= isCorporate ? "align=\"center\"" : ""%>>
										<br />
											<a href="#" onclick="document.litesignupaddress.submit();"><img src="/media_stat/images/buttons/check_my_address.gif" height="16" width="112" border="0"></a>		
									</td>
								</tr>
							</table>
						
							<fd:ErrorHandler result='<%=result%>' name='cantGeocode' id='errorMsg'>
								<table border="0" cellpadding="1" cellspacing="0" width="350">
								<tr>
									<td width="300" class="bodyCopy">
										To check out, you'll need to enter a valid street address, but to browse our site now, click below.
										<br />
										<form name="site_access" method="post" action="<%= response.encodeURL("/site_access/delivery.jsp?successPage=") +URLEncoder.encode(successPage) %>">
										<input type="hidden" name="serviceType" value="<%= serviceType %>">
											<input type="image" src="/media_stat/images/template/site_access/just_let_me_in.gif" width="112" height="16"  alt="" border="0" value="submit">
										</form>
									</td>
								</tr>
								</table>
							</fd:ErrorHandler>
						
							<% if (!isBestCellars) { %>
							<%= isCorporate ? "<img src=\"/media_stat/images/layout/999966.gif\" width=\"450\" height=\"1\" border=\"0\" align=\"center\" alt=\"\" />" : "" %>
								<table border="0" cellpadding="1" cellspacing="0" width="300">
								<tr>
									<td width="<%= isCorporate?"450":"300"%>" class="bodyCopy">
										<span class="text12bold">Already have an account with FreshDirect?</span><br />
										<a href="#" onclick="window.top.location='/login/login_main.jsp'">Click here to log in</a>.
									</td>
								</tr>
								</table>
							<% } %>
						</td>
						<% if (!isCorporate) { %>
							<td width="10"><img src="/media_stat/images/layout/clear.gif" alt="" width="10" height="1" border="0" /></td>			
							<td width="160"><img src="/media_stat/images/template/homepages/home_grapes.jpg" width="170" height="141" border="0" alt="Grapes" /></td>
						<% } %>
					</tr>
				</table>
			</form>
		</div>
	</center>
	<% } } %>

<script>

	function resizeFrame() {
		setFrameHeightSL('signupframe', 425);
		setFrameWidthSL('signupframe',700);
		if(navigator.userAgent.indexOf('MSIE 9') > 0) {
			window.parent.document.getElementById('MB_window').style.left=200 + 'px';
			window.parent.document.getElementById('MB_window').style.width=730 + 'px';
		} else {
			window.parent['Modalbox'].resizeToContent();
			window.parent['Modalbox']._setWidthAndPosition();
		}
	}

	try {
		window.onload = resizeFrame();
	
		FreshDirect.components.ifrPopup.popup.reposition();
	} catch (e) {
		
	}
</script>
</body>
</html>
</fd:SiteAccessController>