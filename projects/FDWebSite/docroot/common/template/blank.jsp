<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<BODY BGCOLOR="#FFFFFF" LINK="#336600" VLINK="#336600" ALINK="#FF9900" TEXT="#333333" CLASS="text10">
<CENTER>
<!-- content lands here -->
<tmpl:get name='content'/>
<!-- content ends above here-->
</CENTER>

<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>
</body>
</html>
