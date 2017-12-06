<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUser" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	boolean notServiceable = false;
	String successPage = request.getParameter("successPage");
	String serviceType = request.getParameter("serviceType");
	
	if (user == null || user.isNotServiceable()) {
		notServiceable = true;
	}
	/* obsolete, removed from media and page title
		boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	*/
	boolean emailSent = request.getParameter("email") != null && "sent".equalsIgnoreCase(request.getParameter("email"));
	String diff_zip_url = response.encodeURL("/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp");

	if("slite".equals(request.getParameter("referrer_page"))) {
		successPage = "#\" onclick=\"window.top.location=\'/index.jsp\'";
		diff_zip_url = "#\" onclick=\"window.top.location=\'/about/index.jsp?siteAccessPage=aboutus&successPage=/index.jsp\'";
	}
	
	String refPage = request.getParameter("referrer_page");
%>
<%!
    java.text.SimpleDateFormat dFormat = new java.text.SimpleDateFormat("MMMMMMMM d");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
<head>
<%-- 	<title>FreshDirect Delivery Check</title><% /* if this title changes, you need to change the media JS as well */ %> --%>
    <fd:SEOMetaTag title="FreshDirect Delivery Check"/>
	<% if("slite".equals(request.getParameter("referrer_page"))) { %>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<% } %>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	
	<fd:SiteAccessController action='saveEmail' successPage='<%= successPage %>' result='result' serviceType='<%=serviceType%>'>
		<%-- put error msgs in to a string first, so it's easier to read --%>
		<%
			String errMsgs = "";
			if ( result.hasError("technicalDifficulty") ) { 
				errMsgs += "<span class=\"text11rbold\">" + result.getError("technicalDifficulty").getDescription() + "</span>";
			}
			if ( result.hasError("email") ) { 
				errMsgs += "<span class=\"text11rbold\">" + result.getError("email").getDescription() + "</span>";
			}
		%>
		<%--
			re-use the options object from site access (if it exists, otherwise just create it)
			Put any java-related variables needed by the page into the _page_options object. 
			all the new vars here go in to a sub-variable, so we don't override pre-existing vars
		--%>
		<script type="text/javascript">
		var _page_options = $jq.extend(true, _page_options||{}, {
				noService: {
					referrer_page: '<%= refPage %>',
					successPage: '<%= StringEscapeUtils.escapeJavaScript( successPage ) %>',
					serviceType: '<%= serviceType %>',
					actionURI: '<%= request.getRequestURI() %>',
					notServiceable: <%= notServiceable %>,
					serviceClass: '<%= (notServiceable) ? "serviceable" : "notServiceable" %>',
					enteredEmail: '<%= request.getParameter("email")!=null?request.getParameter("email"):"" %>',
					emailSent: <%= emailSent %>,
					diffZipUrl: '<%= StringEscapeUtils.escapeJavaScript( diff_zip_url ) %>',
					errMsgs: '<%= StringEscapeUtils.escapeJavaScript( errMsgs ) %>'
				}
			});
		</script>
		<fd:IncludeMedia name="/media/editorial/site_access/zipfail/delivery.html" />
	</fd:SiteAccessController>

</body>
</html>


