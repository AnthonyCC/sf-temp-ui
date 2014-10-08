<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	AddressModel address = null;
	if (user != null) { user.getAddress(); }
	request.setAttribute("survey_source","SiteAccess Page");
	String successPage = "/index.jsp";
	String loginlink = "/login/login_main.jsp";
	if("slite".equals(request.getParameter("referrer_page"))) {
		successPage = "#\" onclick=\"window.top.location=\'/index.jsp\'";
		loginlink = "#\" onclick=\"window.top.location=\'/login/login_main.jsp\'";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>FreshDirect</title>
		<% if("slite".equals(request.getParameter("referrer_page"))) { %>
			<%@ include file="/common/template/includes/metatags.jspf" %>
			<%@ include file="/common/template/includes/i_javascripts.jspf" %>
			<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<% } %>
		<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	</head>
	<body>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	
	<%--
		re-use the options object from site access (if it exists, otherwise just create it)
		Put any java-related variables needed by the page into the _page_options object. 
		all the new vars here go in to a sub-variable, so we don't override pre-existing vars
	--%>
	<script type="text/javascript">

		var _page_options = $jq.extend(true, _page_options||{}, {
			altDlvHome: {
				referrer_page: '<%= request.getParameter("referrer_page") %>',
				successPage: '<%= StringEscapeUtils.escapeJavaScript( successPage ) %>',
				loginLink: '/login/login_main.jsp',
				addzipText: '<%= (address != null && address.getAddress1() != null && address.getAddress1().trim().length()>0) ? "address" : "Zip Code" %>',
				addressText: '<%= (address != null && address.getAddress1() != null && address.getAddress1().trim().length()>0) ? ""+address.getAddress1()+", "+address.getApartment()+"<br /> ZIP "+address.getZipCode() : "" %>',
				cosSurveyContentUrl: '/survey/includes/cos.jsp?sa=true'
			}
		});
	</script>
	<fd:IncludeMedia name="/media/editorial/site_access/zipfail/alt_dlv_home.html" />
	</body>
</html>
