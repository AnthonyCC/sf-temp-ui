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

<%-- TODO search --%>
<potato:browse/>

<%-- OAS variables --%>
<c:set var="sitePage" scope="request" value="www.freshdirect.com/search.jsp" />
<c:set var="listPos" scope="request" value="SystemMessage,LittleRandy,CategoryNote" />

<tmpl:insert template='/common/template/browse_template.jsp'>
  <tmpl:put name='cmeventsource' direct='true'>SEARCH</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
    <soy:import packageName="srch" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css"/>
    <jwr:style src="/browse.css"/>
    <jwr:style src="/srch.css"/>
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>srch</tmpl:put>

  <tmpl:put name='title'>${browsePotato.descriptiveContent.pageTitle}</tmpl:put>

  <tmpl:put name='deptnav' direct='true'>
    <div class="srch-header">
      <h1>Search Results</h1>
      <div class="oas-cnt" id="oas_b_CategoryNote"><script type="text/javascript">OAS_AD('CategoryNote');</script></div>
    </div>
  </tmpl:put>

  <tmpl:put name='tabs' direct='true'>
    <section class="itemcount">
      <b>321</b> products found for <b>apple</b>
    </section>
    <nav class="tabs">
      <li class="active" data-type="products"><span>Products <i>(12)</i></span></li>
      <li data-type="recipes"><span>Recipes <i>(4)</i></span></li>
    </nav>
    <section class="tabcontent">
      <div class="search-input">
        <input type="search" placeholder="Search the store" data-component="autocomplete" autocomplete="off"/>
        <button class="cssbutton khaki">search</button>
      </div>
      <div class="pager-holder">
        <soy:render template="browse.pager" data="${browsePotato.pager}" />
      </div>
    </section>
  </tmpl:put>

  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    </div>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div id="sorter">
      <soy:render template="browse.sortBar" data="${browsePotato.sortOptions}" />
    </div>

    <div class="browse-filtertags">
      <soy:render template="browse.filterTags" data="${browsePotato.filterLabels}" />
    </div>

    <div class="browse-sections transactional">
      <soy:render template="browse.content" data="${browsePotato.sections}" />
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

  <tmpl:put name='bottom' direct='true'>
    <div class="pager-holder">
      <soy:render template="browse.pager" data="${browsePotato.pager}" />
    </div>
    
    <div class="srch-carousel">
      <soy:render template="srch.carouselWrapper" data="${browsePotato.carousels}" />
    </div>
  </tmpl:put>

  <tmpl:put name='extraJsModules'>
    <jwr:script src="/browse.js"  useRandomParam="false" />
    <jwr:script src="/srch.js"  useRandomParam="false" />
  </tmpl:put>
</tmpl:insert>
