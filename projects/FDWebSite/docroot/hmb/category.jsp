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
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>
<c:set var="catId" value='${param.id!=null ? param.id : param.catId}'/>

<potato:browse name="browsePotato" specialLayout="true" nodeId='${catId}'/>

<tmpl:insert template='/common/template/browse_noleftnav_template.jsp'>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/browse.css" media="all"/>
  </tmpl:put>
  
  <tmpl:put name='containerExtraClass' direct='true'>hmb</tmpl:put>

  <tmpl:put name='title'>${browsePotato.descriptiveContent.pageTitle}</tmpl:put>   
    
  <tmpl:put name='deptnav' direct='true'>
    <div class="browse-titlebar">
      <soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    </div>
  </tmpl:put>

  <tmpl:put name="extraJs">
  </tmpl:put>
  
  <tmpl:put name="content" direct='true'>
    <soy:render template="browse.specialLayoutMedia" data="${browsePotato}" />
  
    <soy:render template="browse.specialLayoutContent" data="${browsePotato}" />
	
    <script>
        window.FreshDirect = window.FreshDirect || {};
        window.FreshDirect.browse = window.FreshDirect.browse || {};

        window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
    </script>
  </tmpl:put>
  
</tmpl:insert>
