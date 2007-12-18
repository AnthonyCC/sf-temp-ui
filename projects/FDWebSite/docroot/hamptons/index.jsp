<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri="freshdirect" prefix="fd" %>

<%
	String successPage = request.getParameter("successPage");
	String zipcode = NVL.apply(request.getParameter("zipCode"), "");
	
	if (successPage == null) {
  		// null, default to index.jsp
  		successPage = "/index.jsp";
 	}

	String moreInfoPage = "/site_access/site_access_address.jsp?successPage="+ URLEncoder.encode(successPage);
	String failurePage = "/site_access/delivery.jsp?successPage="+ URLEncoder.encode(successPage);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>FreshDirect Hamptons Service</title>
		<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="white" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20" onLoad="window.document.site_access_home.<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>.focus();">
		<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
		<div align="center"><br>
			<table border="0" cellspacing="0" cellpadding="0" width="720">
				<tr>
					<td align="center">
					<fd:IncludeMedia name="/media/editorial/hamptons/site_access.html" />
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="18"><br>
						<% if ( result.hasError("technicalDifficulty") ) { %>
							<font class="text11rbold"><%=result.getError("technicalDifficulty").getDescription() %></font><br><br>
						<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							<font class="text11rbold"><%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %></font><br><br>
						<%}%>
					</td>
				</tr>
				<tr>
					<td align="center">
						<table cellpadding="0" cellspacing="0" border="0">
							<form name="site_access_home" method="post" action="<%= request.getRequestURI() %>">
							<input type="hidden" name="serviceType" value="<%= EnumServiceType.HOME.getName()%>">
							<tr>
								<td align="center" colspan="5"><img src="/media_stat/images/template/site_access/hdr_home_dlv.gif" width="236" height="24"></td>
							</tr>
							<tr>
								<td rowspan="2" bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="212" height="10"></td>
								<td><img src="/media_stat/images/layout/clear.gif" width="11" height="10"></td>
								<td rowspan="2" bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
							</tr>
							<tr valign="middle">
								<td colspan="3" align="center" class="text12">
                                 Enter your ZIP CODE in the Hamptons:<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4"><br>
                                 <input class="text11" type="text" size="13" style="width: 122px" value="<%= zipcode%>" maxlength="5" name="<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>" required="true" tabindex="1">
                                 <br><img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br><input type="image" src="/media_stat/images/template/site_access/go_home.gif" width="39" height="21" name="site_access_home_go" border="0" value="Check My Area" alt="GO" hspace="4" tabindex="2">
                                </td>
							</tr>
							
							<tr>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_green_left.gif" width="12" height="11"></td>
								<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td>
								<td rowspan="2" colspan="2" valign="bottom"><img src="/media_stat/images/template/site_access/corner_green_right.gif" width="12" height="11"></td>
							</tr>
							<tr>
								<td bgcolor="#669933"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
							</tr>
							</form>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center" class="text12">
						<br><br><br><b>Current customer? <a href="/login/login_main.jsp">Click here to log in</a>.</b>
					</td>
				</tr>
				<tr>
					<td align="center" class="text12">
						<img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
							<%@ include file="/shared/template/includes/copyright.jspf"%>
						<br><br><br>
					</td>
				</tr>
			</table>
		</div>
		</fd:SiteAccessController>
	</body>
</html>
