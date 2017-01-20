<!DOCTYPE html>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge" />
		<title><tmpl:get name="title" /></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		<tmpl:get name="extraCss" />
		<tmpl:get name="extraJs" />
		<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	</head>
	<!--[if lte IE 9]><body class="ie"><![endif]-->
	<!--[if gt IE 9]><!--><body><!--<![endif]-->
		<%@ include file="/shared/template/includes/i_body_start.jspf" %>

		<div id="content">
			<!-- content starts -->
			<tmpl:get name="content" />
			<!-- content ends -->
		</div>

		<tmpl:get name="soytemplates" />
		<tmpl:get name="jsmodules" />
    	<tmpl:get name="extraJsFooter" />
	</body>
</html>
