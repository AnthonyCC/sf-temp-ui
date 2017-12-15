<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
  	<%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	
	<jwr:style src="/giftcards.css" media="all" />
	<jwr:style src="/timeslots.css" media="all" />
	
	<%@ include file="/shared/template/includes/ccl.jspf" %>
<%		
    {
       String onbeforeunload = (String)request.getAttribute("windowOnBeforeUnload");
       if (onbeforeunload != null && onbeforeunload.length() > 0) {
%>
    <script type="text/javascript">
       window.onbeforeunload = <%= onbeforeunload %>;
    </script>
<%
       } // if
    } // local block
%>
<tmpl:get name="extraHead" />
<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body bgcolor="#ffffff" link="#336600" vlink="#336600" alink="#ff9900" text="#333333" class="text10">
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
<center>
<!-- content lands here -->
<tmpl:get name='content'/>
<!-- content ends above here-->
</center>

</body>
</html>
