<!DOCTYPE html>

<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>

<html>
<head>
  <title><tmpl:get name='title'/></title>

  <%@ include file="/common/template/includes/metatags.jspf" %>
  
  <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  
  <tmpl:get name='facebookmeta'/>

  <jwr:style src="/grid.css"/>
  <jwr:style src="/global.css"/>
  <jwr:style src="/oldglobal.css"/>

  <tmpl:get name='extraCss'/>

  <jwr:script src="/fdlibs.js" useRandomParam="false" />

  <tmpl:get name='extraJs'/>
  <tmpl:get name='nutritionCss'/>
  
  <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  
</head>

<!--[if lt IE 9]><body class="ie8"><![endif]-->
<!--[if gt IE 8]><body><![endif]-->
<!--[if !IE]><!--><body><!--<![endif]-->
  
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
    
<%@ include file="/common/template/includes/globalnav.jspf" %> 

<%-- TODO --%>
<section class="container <tmpl:get name='containerExtraClass'/>">

  <section class="deptnav">
    <!-- start : deptnav -->
    <tmpl:get name='deptnav'/>
    <!-- end : deptnav -->    
  </section>

  <section class="main">
    <nav class="leftnav">
      <!-- start : leftnav -->
      <tmpl:get name='leftnav'/>
      <!-- end : leftnav -->    
    </nav>
    
    <section class="content">
      <!-- start : content -->
      <tmpl:get name='content'/>
      <!-- end : content -->      
    </section>
  </section>

</section>


<%@ include file="/common/template/includes/footer.jspf" %>

<tmpl:get name='soypackage'/>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
<jwr:script src="/fdmodules.js"  useRandomParam="false" />
<jwr:script src="/fdcomponents.js"  useRandomParam="false" />

<jwr:script src="/browse.js"  useRandomParam="false" />

<tmpl:get name='extraJsModules'/>

</body>
</html>
