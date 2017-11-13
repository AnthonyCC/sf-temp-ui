<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US">
<head>
    <title><tmpl:get name='title'/></title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	    
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body>
<%@ include file="/shared/template/includes/i_body_start.jspf" %>

	<%-- content lands here --%>
	<tmpl:get name='content'/>
	<%-- content ends above here--%>
	
</body>
</html>
