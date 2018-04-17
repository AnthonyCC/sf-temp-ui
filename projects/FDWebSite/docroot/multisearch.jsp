<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<tmpl:insert template='/common/template/browse_template.jsp'>

	<tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="common"/>
    <soy:import packageName="multisrch"/>
	</tmpl:put>

	<tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/multisearch.css" media="all" />
	</tmpl:put>

  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="Search for multiple keywords"></fd:SEOMetaTag>
  </tmpl:put>

	<tmpl:put name='leftnav' direct='true'>
    <div id="multisearch-input">
    </div>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <h1>Multi-search</h1>
    <div id="multisearch-results" class="contentModules">
    </div>
  </tmpl:put>
  
	<tmpl:put name='extraJsModules'>
    <jwr:script src="/multisearch.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
