<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<%
	String pageId = request.getParameter("pageType") != null ? (String) request.getParameter("pageType"):"search";
	// [APPDEV-3953] Special rule for DDPP Preview Mode
	// Redirect to site access for getting zip code first
	if (null != request.getParameter("ppPreviewId")) {
		/* from category.jsp, for being forwarded here */

		//disable linking
		// disableLinks = true;
		if (request.getParameter("redirected") == null) {
			StringBuffer redirBuf = new StringBuffer();
			//redirBuf.append("/site_access/site_access_lite.jsp?successPage="+request.getRequestURI());

			redirBuf.append("/about/index.jsp?siteAccessPage=aboutus&successPage="
					+ request.getRequestURI());

			String requestQryString = request.getQueryString();

			if ((requestQryString != null)
					&& (requestQryString.trim().length() > 0)) {
				redirBuf.append(URLEncoder.encode("?"
						+ requestQryString));
			}
			redirBuf.append("&redirected=true");
			response.sendRedirect(redirBuf.toString());
			return;
		}
	}
%>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>

<%-- TODO search --%>
<potato:browse/>

<%-- OAS variables --%>
<c:set var="sitePage" scope="request" value="${empty browsePotato.descriptiveContent.oasSitePage ? 'www.freshdirect.com/search.jsp' : browsePotato.descriptiveContent.oasSitePage }" />
<c:set var="listPos" scope="request" value="SystemMessage,LittleRandy,CategoryNote,PPHeader,PPHeader2,PPSuperBuy,PPLeftBottom,PPMidBottom,PPRightBottom,PPSearchContent" />

<tmpl:insert template='/common/template/browse_template.jsp'>
  <tmpl:put name='cmeventsource' direct='true'>BROWSE</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
    <soy:import packageName="srch" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all" />
    <jwr:style src="/browse.css" media="all" />
    <jwr:style src="/srch.css" media="all" />
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>srch ${empty browsePotato.menuBoxes.menuBoxes ? 'emptymenu' : ''}</tmpl:put>
 	<c:choose>
 	<c:when test="${empty pageId or 'search' eq pageId}">
 	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="${browsePotato.descriptiveContent.pageTitle}" metaDescription="${browsePotato.descriptiveContent.metaDescription}" ></fd:SEOMetaTag>
	</tmpl:put>
	</c:when>
	<c:otherwise>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="${pageId}"></fd:SEOMetaTag>
	</tmpl:put>
	</c:otherwise>
  	</c:choose>
  <tmpl:put name='pageType'>${browsePotato.searchParams.pageType}</tmpl:put>

  <tmpl:put name='deptnav' direct='true'>
    <div class="srch-header">
      <soy:render template="srch.header" data="${browsePotato.searchParams}" />
    </div>
  </tmpl:put>

  <tmpl:put name='tabs' direct='true'>
    <div id="listsearch">
        <soy:render template="srch.listSearch" data="${browsePotato.searchParams}" />
    </div>
    <section class="page-type">
        <soy:render template="browse.pageType" data="${browsePotato.searchParams}" />
    </section>
    <section class="srch-ddpp">
        <soy:render template="srch.ddppWrapper" data="${browsePotato.ddppproducts}" />
    </section>
    <section class="ddpp-oas">
      <div class="oas-cnt PPSuperBuy" id="oas_b_PPSuperBuy">
        <script type="text/javascript">
            OAS_AD('PPSuperBuy');
        </script>
      </div>
    </section>
    <nav class="tabs">
      <soy:render template="srch.searchTabs" data="${browsePotato.searchParams}" />
    </nav>
    <section class="itemcount">
      <soy:render template="srch.searchSuggestions" data="${browsePotato.searchParams}" />
    </section>
    <div class="search-input">
      <soy:render template="srch.searchParams" data="${browsePotato.searchParams}" />
    </div>
  </tmpl:put>

  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    </div>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div class="oas-cnt PPSearchContent" id="oas_b_PPSearchContent">
     <script type="text/javascript">
       OAS_AD('PPSearchContent');
     </script>
    </div>

    <soy:render template="browse.topMedia" data="${browsePotato.descriptiveContent}" />

    <div class="pager-holder top">
      <c:if test="${not empty browsePotato.pager}">
        <soy:render template="browse.pager" data="${browsePotato.pager}" />
      </c:if>
    </div>

    <div id="sorter">
      <soy:render template="browse.sortBar" data="${browsePotato.sortOptions}" />
    </div>

    <div class="browse-filtertags">
      <soy:render template="browse.filterTags" data="${browsePotato.filterLabels}" />
    </div>

    <c:choose>
      <c:when test="${browsePotato.searchParams.pageType == 'SEARCH'}">
        <div class="browse-sections-top transactional">
          <soy:render template="srch.topContent" data="${browsePotato.sections}" />
        </div>

        <div class="srch-carousel">
          <soy:render template="srch.carouselWrapper" data="${browsePotato.carousels}" />
        </div>
        
        <div class="browse-sections-bottom transactional">
          <soy:render template="srch.bottomContent" data="${browsePotato.sections}" />
        </div>
      </c:when>
      <c:otherwise>
        <div class="browse-sections transactional">
          <soy:render template="browse.content" data="${browsePotato.sections}" />
        </div>
      </c:otherwise>
    </c:choose>

    <div class="pager-holder bottom">
      <c:if test="${not empty browsePotato.pager}">
        <soy:render template="browse.pager" data="${browsePotato.pager}" />
      </c:if>
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
    <c:choose>
	    <c:when test="${browsePotato.searchParams.pageType != 'SEARCH'}">
	      <div class="srch-carousel">
	        <soy:render template="srch.carouselWrapper" data="${browsePotato.carousels}" />
	      </div>
	    </c:when>
	    <c:otherwise>
    		<fd:CmPageView wrapIntoScriptTag="true" searchTerm="${browsePotato.searchParams.searchParams}" searchResultsSize="${browsePotato.searchParams.tabs[0].hits}" suggestedTerm="${browsePotato.searchParams.searchTerm}" recipeSearchResultsSize="${browsePotato.searchParams.tabs[1].hits}"/>
		</c:otherwise>
    </c:choose>
    <div class="ddpp-bottom">
            <hr class="ddpp-hr top" />
            <table class="ddppBotAds">
                <tr>
                    <td align="center" colspan="3" class='ddppBotAd-width'>
                    </td>
                </tr>
                <tr>
                    <td align="center">
                        <div class="oas-cnt PPLeftBottom" id="oas_b_PPLeftBottom">
                            <script type="text/javascript">
                                    OAS_AD('PPLeftBottom');
                            </script>
                        </div>
                    </td>
                    <td align="center" class="ddppBotAd-sep">
                        <div class="PPMidBottom" id="oas_b_PPMidBottom">
                            <script type="text/javascript">
                                    OAS_AD('PPMidBottom');
                            </script>
                        </div>
                    </td>
                    <td align="center">
                        <div class="PPRightBottom" id="oas_b_PPRightBottom">
                            <script type="text/javascript">
                                    OAS_AD('PPRightBottom');
                            </script>
                        </div>
                    </td>
                </tr>
            </table>
            <hr class="ddpp-hr bottom" />
            <center class="legaltext">
            New President's Picks items are available weekly. Prices and the selection of items are subject to change without notice. Sorry, no rain checks. Offers expire each Wednesday at 11:59 p.m. and promotional pricing will be removed from orders after that time. Offer is nontransferable.<br><br>
            Wines and spirits sold by FreshDirect Wines &amp; Spirits. Alcohol cannot be delivered outside of New York state. Beer, wine and spirits will be removed from your cart during checkout if an out of state address is selected for delivery. The person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature.
            </center>
    </div>
  </tmpl:put>

  <tmpl:put name='extraJsModules'>
    <jwr:script src="/browse.js"  useRandomParam="false" />
    <jwr:script src="/srch.js"  useRandomParam="false" />
  </tmpl:put>
</tmpl:insert>