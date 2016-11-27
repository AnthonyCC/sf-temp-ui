<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<tmpl:get name="seoMetaTag"/>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<% if (request.getRequestURI().contains("/myfd")) { %>
	<fd:javascript src="/assets/javascript/cufon-yui.js"/>
	<fd:javascript src="/assets/javascript/EagleCufon.font.js"/>
	<fd:javascript src="/assets/javascript/EagleCufonBold.font.js"/>
	<script type="text/javascript" language="javascript">
		Cufon.replace('.myfd-header-text', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.myfd-category a', { fontFamily: 'EagleCufon' });
		Cufon.replace('.myfd-category a strong', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.myfd-category a b', { fontFamily: 'EagleCufonBold' });
		Cufon.replace('.eagle-bold', { fontFamily: 'EagleCufonBold' });
	</script>
	<% } %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<fd:css href="/assets/css/giftcards.css"/>
	<fd:css href="/assets/css/timeslots.css"/>
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
