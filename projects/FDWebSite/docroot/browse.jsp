<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>

<%--Might be useless
 <potato:globalnav/> --%> 
<potato:browse/>

<%-- OAS variables --%>
<c:set var="sitePage" scope="request" value="${browsePotato.descriptiveContent.oasSitePage}" />
<c:set var="listPos" scope="request" value="SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2" />

<tmpl:insert template='/common/template/browse_template.jsp'>
  <tmpl:put name='cmeventsource' direct='true'>BROWSE</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css"/>
    <jwr:style src="/browse.css"/>
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>browse</tmpl:put>

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

  <tmpl:put name='content' direct='true'>
    <div class="browse-breadcrumbs">
      <soy:render template="browse.breadCrumb" data="${browsePotato.breadCrumbs}" />
    </div>

    <div class="pager-holder">
      <soy:render template="browse.pager" data="${browsePotato.pager}" />
    </div>

    <div id="sorter">
      <soy:render template="browse.sortBar" data="${browsePotato.sortOptions}" />
    </div>

    <soy:render template="browse.superDepartment" data="${browsePotato.sections}" />

    <div class="browse-media">
      <soy:render template="browse.media" data="${browsePotato.descriptiveContent}" />
    </div>

    <div class="browse-carousels-top">
      <soy:render template="browse.topCarousels" data="${browsePotato.carousels}" />
    </div>

    <div class="oas-cnt" id="oas_b_CategoryNote"><script type="text/javascript">OAS_AD('CategoryNote');</script></div>
    <div class="oas-cnt" id="oas_b_BrowseTop1"><script type="text/javascript">OAS_AD('BrowseTop1');</script></div>
    <div class="oas-cnt left" id="oas_b_BrowseTop2"><script type="text/javascript">OAS_AD('BrowseTop2');</script></div>
    <div class="oas-cnt right" id="oas_b_BrowseTop3"><script type="text/javascript">OAS_AD('BrowseTop3');</script></div>

    <div class="browse-filtertags">
      <soy:render template="browse.filterTags" data="${browsePotato.filterLabels}" />
    </div>

    <div class="browse-sections transactional">
      <soy:render template="browse.content" data="${browsePotato.sections}" />
    </div>
    
    <div class="oas-cnt left" id="oas_b_BrowseBottom1"><script type="text/javascript">OAS_AD('BrowseBottom1');</script></div>
    <div class="oas-cnt right" id="oas_b_BrowseBottom2"><script type="text/javascript">OAS_AD('BrowseBottom2');</script></div>

    <div class="browse-carousels-bottom">
      <soy:render template="browse.bottomCarousels" data="${browsePotato.carousels}" />
    </div>

    <div class="pager-holder">
      <soy:render template="browse.pager" data="${browsePotato.pager}" />
    </div>
    
    <script>
      window.FreshDirect = window.FreshDirect || {};
      window.FreshDirect.browse = window.FreshDirect.browse || {};
      window.FreshDirect.globalnav = window.FreshDirect.globalnav || {};

      window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
      window.FreshDirect.globalnav.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
      window.FreshDirect.coremetricsData = window.FreshDirect.browse.data.coremetrics;
    </script>
  </tmpl:put>

</tmpl:insert>
