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
  <tmpl:get name="seoMetaTag"/> 
  <%@ include file="/common/template/includes/i_javascripts_browse.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
  <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
  
  <tmpl:get name='facebookmeta'/>

  <jwr:style src="/grid.css" media="all" />
  <jwr:style src="/global.css" media="all" />
  <jwr:style src="/oldglobal.css" media="all" />

  <tmpl:get name='extraCss'/>

  <jwr:script src="/fdlibs.js" useRandomParam="false" />

  <tmpl:get name='extraJs'/>
  <tmpl:get name='nutritionCss'/>
  
  <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  
</head>

<!--[if lt IE 9]><body class="ie8" data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" data-department="<tmpl:get name='department'/>"><![endif]-->
<!--[if gt IE 8]><body data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" data-department="<tmpl:get name='department'/>"><![endif]-->
<!--[if !IE]><!--><body data-cmeventsource="<tmpl:get name='cmeventsource'/>" data-pagetype="<tmpl:get name='pageType'/>" data-department="<tmpl:get name='department'/>"><!--<![endif]-->
  
<%@ include file="/shared/template/includes/i_body_start.jspf" %>
    
<%@ include file="/common/template/includes/globalnav.jspf" %> 

<section class="container <tmpl:get name='containerExtraClass'/>">

  <section class="deptnav">
    <!-- start : deptnav -->
    <tmpl:get name='deptnav'/>
    <!-- end : deptnav -->    
  </section>

  <section class="tabs">
    <!-- start : tabs -->
    <tmpl:get name='tabs'/>
    <!-- end : tabs -->      
  </section>

  <section class="main full">
    <section class="fullcontent">
      <!-- start : content -->
      <tmpl:get name='content'/>
      <!-- end : content -->      
    </section>
  </section>

  <section class="bottom">
    <!-- start : bottom -->
    <tmpl:get name='bottom'/>
    <!-- end : bottom -->    
  </section>
</section>


<%@ include file="/common/template/includes/footer.jspf" %>

<tmpl:get name='soypackage'/>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
<tmpl:get name='extraJsModules'/>

</body>
</html>
