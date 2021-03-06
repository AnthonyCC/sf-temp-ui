<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%
/*
    if (session.isNew()){
    	 response.sendRedirect(response.encodeRedirectURL("site_access.jsp"));
			return;
		}*/
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
<%-- 		<title><tmpl:get name='title'/></title> --%>
		<tmpl:get name="seoMetaTag"/>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
    <%@ include file="/shared/template/includes/ccl.jspf" %>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	
	<tmpl:get name="customCss"/>
</head>
	<body>
			<%@ include file="/common/template/includes/globalnav.jspf" %> 
    <div id="content">
      <center class="text10">
      <!-- content lands here -->
      <tmpl:get name='content'/>
      <!-- content ends above here-->
      </center>
    </div>
	<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
	<tmpl:get name='customJsBottom'/>
	</body>
</html> 
