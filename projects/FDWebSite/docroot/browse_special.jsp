<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:CheckLoginStatus id="user"/>
<fd:CheckDraftContextTag/>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>
<c:set var="catId" value='${param.id!=null ? param.id : param.catId}'/>

<potato:browse name="browsePotato" specialLayout="true" nodeId='${catId}'/>
<c:choose>
<c:when test="${browsePotato!=null}">
<tmpl:insert template='/common/template/pdp_template.jsp'>

  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title='${browsePotato.descriptiveContent.pageTitle}'/>
  </tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all"/>
    <jwr:style src="/pdp.css" media="all"/>
  </tmpl:put>


  <tmpl:put name='title'>${browsePotato.descriptiveContent.pageTitle}</tmpl:put>
    
  <tmpl:put name='deptnav' direct='true'>
    <div class="browse-titlebar">
      <soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    </div>
  </tmpl:put>

  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    </div>
  </tmpl:put>
  
  
  <tmpl:put name="extraJs">
    <%@ include file="/shared/template/includes/ccl.jspf"%>
  </tmpl:put>
  
  
  <tmpl:put name="content" direct='true'>
   	<jsp:include page="browse_special_layout.jsp">
   		<jsp:param name="catId" value='${catId}'/>
    </jsp:include>
    <script>
        window.FreshDirect = window.FreshDirect || {};
        window.FreshDirect.browse = window.FreshDirect.browse || {};
        window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};

        window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
        window.FreshDirect.activeDraft = "${activeDraft}"
        window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}"
    </script>

  </tmpl:put>
  
	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</tmpl:put>
  
</tmpl:insert>
</c:when>
<c:otherwise>
	<jsp:include page="category.jsp">
   		<jsp:param name="catId" value='${catId}'/>
    </jsp:include>
</c:otherwise>
</c:choose>
