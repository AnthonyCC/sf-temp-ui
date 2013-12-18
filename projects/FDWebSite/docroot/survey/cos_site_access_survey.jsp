<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<fd:CheckLoginStatus id="user" guestAllowed="true" /> 

<%
    String successPage = NVL.apply(request.getParameter("successPage"), "");
    // default to index.jsp
    if ("".equals(successPage)) {
	    successPage = "/index.jsp";
    }
    request.setAttribute("survey_source","SiteAccess Page");

	if("slite".equals(request.getParameter("referrer_page"))) {
		successPage = "#\" onclick=\"window.top.location=\'/index.jsp\'";
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
			noServiceCOS: {
				referrer_page: '<%= request.getParameter("referrer_page") %>',
				successPage: '<%= StringEscapeUtils.escapeJavaScript( successPage ) %>',
				cosSurveyContentUrl: '/survey/includes/cos_catering_survey.jsp?survey=cos_catering_survey'
			}
		});
	</script>
	<fd:IncludeMedia name="/media/editorial/site_pages/survey/cos_catering_survey.html" />
</body>
</html>
