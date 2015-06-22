<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<tmpl:get name="seoMetaTag"/> 
		<%@ include file="/common/template/includes/i_javascripts_optimized.jspf" %>
		<%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
		<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	
	<tmpl:get name="customCss"/>
</head>
	<body>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>
	<%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
    <div id="content">
      <center class="text10">
      <!-- content lands here -->
      <tmpl:get name='content'/>
      <!-- content ends above here-->
      </center>
    </div>
	<%@ include file="/common/template/includes/footer.jspf" %>
    <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
	<tmpl:get name='customJsBottom'/>
	</body>
</html> 
