<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html>
<head>
	<title>FreshDirect - Our Freshness Guarantee</title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>

<body>

<%@ include file="/shared/template/includes/i_body_start.jspf" %>

	<%--
		Put any java-related variables needed by the page into the _page_options object. 
	--%>
	<%
		String language = "english";

		if ( request.getParameter("lang") != null) {
			if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) {
				language = "espanol";
			}
		}
	%>
	<script type="text/javascript">
		if (!window['_page_options']) {	var _page_options = {}; };
		_page_options['lang'] = '<%= language %>';
		_page_options['csEmail'] = '<fd:GetServiceEmail />';
		_page_options['csPhone'] = '<% if (user != null) { %><%=user.getCustomerServiceContact()%><%}%>';
	</script>
			
	<% if ( "espanol".equals(language) ) { %>
			<fd:IncludeMedia name="/media/editorial/site_pages/freshness_guarantee_espanol.html" />
	<% } else { %>
		<fd:IncludeMedia name="/media/editorial/site_pages/freshness_guarantee.html" />
	<% } %>
</body>
</html>
