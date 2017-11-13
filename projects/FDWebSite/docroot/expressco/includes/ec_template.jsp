<!DOCTYPE html>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<html lang="en-US" xml:lang="en-US">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <tmpl:get name="seoMetaTag"/>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>
    <jwr:style src="/grid.css" media="all" />
    <jwr:style src="/oldglobal.css" media="all" />
    <jwr:style src="/global.css" media="all" />
    <jwr:style src="/expressco.css" media="all" />
    <jwr:style src="/accessibility.css" media="all" />
    <tmpl:get name="extraCss"/>
    <tmpl:get name="extraJs"/>
    <%@ include file="/shared/template/includes/i_head_end.jspf" %>
  </head>
<!--[if lte IE 9]><body class="ie" data-ec-page="<tmpl:get name="ecpage" />"> <![endif]-->
<!--[if gt IE 9]><!--><body data-ec-page="<tmpl:get name="ecpage" />"><!--<![endif]-->
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <tmpl:get name="globalnav" />

    <div id="content">
      <%-- TODO --%>
      <tmpl:get name="content" />

      <!-- content ends above here-->
    </div>

    <tmpl:get name="bottomnav" />

    <tmpl:get name="soytemplates" />
    <tmpl:get name="jsmodules" />
  </body>
</html>
