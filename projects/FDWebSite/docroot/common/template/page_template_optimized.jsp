<!DOCTYPE html>
  
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%-- paulirish.com/2008/conditional-stylesheets-vs-css-hacks-answer-neither/ --%>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]>    <html class="no-js lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]>    <html class="no-js lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en-US" xml:lang="en-US"> <!--<![endif]-->

<head>
  <tmpl:get name="seoMetaTag"/>
    
  <%@ include file="/common/template/includes/i_javascripts.jspf" %>
  <%@ include file="/shared/template/includes/i_stylesheets_optimized.jspf" %>
  <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  <tmpl:get name='extraHead'/>
   
</head>

<body>

  <%@ include file="/common/template/includes/globalnav_optimized.jspf" %>
     
  <div id="main" class="container staticpage">
    <tmpl:get name='nav'/>
    <tmpl:get name='content'/>
  </div>
  
  <%@ include file="/common/template/includes/footer.jspf" %> 
  <%@ include file="/common/template/includes/i_jsmodules_optimized.jspf" %>
    
</body>
</html>
