<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.storeapi.fdstore.*" %>
<%@ page import="com.freshdirect.cms.core.domain.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.pricing.*" %>
<%@ page import="com.freshdirect.fdstore.rollout.*"%>
<%@ page import="com.freshdirect.fdstore.customer.*"%>
<%@ page import="com.freshdirect.smartstore.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.ajax.*"%>
<%@ page import="com.freshdirect.webapp.ajax.filtering.*"%>
<%@ page import="com.freshdirect.webapp.ajax.browse.*"%>
<%@ page import="com.freshdirect.webapp.ajax.browse.data.*"%>
<%@ page import="com.freshdirect.webapp.ajax.product.*"%>
<%@ page import="com.freshdirect.webapp.ajax.product.data.*"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.*"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="unbxd" prefix='unbxd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<%
	//Sample URL : http://localhost:7001/test/product/fp.jsp?pageType=browse&id=fp&rbl=3&dbl=10&pbl=10000&cn=false&cbis=false
	// https://dev1.nj01/test/product/fp.jsp?pageType=browse&id=fp&rbl=4&dbl=40&pbl=10000&cn=false&cbis=false
	// http://localhost:7001/test/product/fp.jsp?pageType=browse&id=fp&rbl=4&dbl=40&pbl=10000&cn=false&cbis=false
	//http://localhost:7001/test/product/fp.jsp?sp=true&mnp=5
	double ratingBaseLine = 4;
	double popularityBaseLine = 10000;
	double dealsBaseLine = 20;
	boolean considerNew = false;
	boolean considerBackInStock = false;
	boolean sortProducts = true;
	int maxNoOfProducts = 50;
	
	String customerId = null;
	try {
		if(request.getParameter("rbl") != null) {
			ratingBaseLine = Double.parseDouble(request.getParameter("rbl"));
		}
		
		if(request.getParameter("pbl") != null) {
			popularityBaseLine = Double.parseDouble(request.getParameter("pbl"));
		}
		
		if(request.getParameter("dbl") != null) {
			dealsBaseLine = Double.parseDouble(request.getParameter("dbl"));
		}
		
		if(request.getParameter("cn") != null) {
			considerNew = Boolean.parseBoolean(request.getParameter("cn"));
		}
		
		if(request.getParameter("cbis") != null) {
			considerBackInStock = Boolean.parseBoolean(request.getParameter("cbis"));
		}
		
		if(request.getParameter("sp") != null) {			
			sortProducts = Boolean.parseBoolean(request.getParameter("sp"));
			
		}
		
		if(request.getParameter("mnp") != null) {			
			maxNoOfProducts = Integer.parseInt(request.getParameter("mnp"));
			
		}
		
		final CmsFilteringNavigator nav = CmsFilteringNavigator.createInstance(request, user);
		nav.setPageTypeType(FilteringFlowType.BROWSE);
		BrowseData browseData = CmsFilteringFlow.getInstance().getWeLoveYouLoveData(user, nav, ratingBaseLine, dealsBaseLine, popularityBaseLine
																						, considerNew, considerBackInStock, sortProducts, maxNoOfProducts) ;

		pageContext.setAttribute("browsePotato", DataPotatoField.digBrowse(browseData));
		
	} catch (Exception e) {
		throw e;
		//User no found or invalid parameters
	}		
%>

<tmpl:insert template='/common/template/browse_template.jsp'>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - My Sale Items" pageId="my_sale_items"></fd:SEOMetaTag>
	</tmpl:put>
   
   <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
    <soy:import packageName="srch" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all" />
    <jwr:style src="/browse.css" media="all" />
    <jwr:style src="/srch.css" media="all" />
  </tmpl:put>
  
  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <div data-component="menu">
      	<div class="menuBox">
      		<h2>TOP-RATED</h2>
      		<ul>
      			<% for (int ratingVal = 5; ratingVal > 0; ratingVal--) { %>
	      			<li>
	      				<label><input id="" type="radio" data-uriparam="rbl" name="expertrating-menu" value="<%= ratingVal %>" <%= (ratingBaseLine==ratingVal) ? "checked=\"checked\"" : "" %>><span><span>
	      					<div class="rating"><b class="expertrating smallrating rating-<%= ratingVal*2 %>">Rating <%= ratingVal %> out of 5</b><%= (ratingVal < 5) ? " & Up" : "" %></div>
						</span></span></label>
	      			</li>
	      		<% } %>
      		</ul>
      		
      		<h2>DEAL %-OFF</h2>
      		<ul>
      			<% for (int i = 80; i >= 10; i=i-10) { %>
	      			<li>
	      				<label><input id="" type="radio" data-uriparam="dbl" name="discount-menu" value="<%= i %>" <%= (dealsBaseLine==i) ? "checked=\"checked\"" : "" %>><span><span>
	      					<%= i %>% off<%= (i < 100) ? " & Up" : "" %>
						</span></span></label>
	      			</li>
	      		<% } %>
      		</ul>

      		<ul>
      			<li>
      				<label><input id="" type="checkbox" data-uriparam="cbis" name="cbis-menu" <%= (considerBackInStock) ? "checked=\"checked\"" : "" %>><span><span>
      					<strong>Back In Stock</strong>
					</span></span></label>
      			</li>
      		</ul>
      	</div>
      </div>
    </div>
    <script>
    	//handle fake menu
		function updateQueryStringParameter(uri, key, value) {
			var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
			var separator = uri.indexOf('?') !== -1 ? "&" : "?";
			if (uri.match(re)) {
				return uri.replace(re, '$1' + key + "=" + value + '$2');
			} else {
				return uri + separator + key + "=" + value;
			}
		}
    	$jq('[data-uriparam]').on('click', function(event) {
    		var curVal = ($jq(this).attr('type') === 'checkbox') ? $jq(this).is(':checked') : $jq(this).val();
    		window.location = window.location.pathname + updateQueryStringParameter(window.location.search, $jq(this).attr('data-uriparam'), curVal);
    	});
    </script>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    	<div class="browse-sections transactional"><%-- this does the main prod grid --%>
	        <soy:render template="browse.content" data="${browsePotato.sections}" />
	    </div>
	    <script>
	      window.FreshDirect = window.FreshDirect || {};
	      window.FreshDirect.browse = window.FreshDirect.browse || {};
	      window.FreshDirect.globalnav = window.FreshDirect.globalnav || {};

	      window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
	      window.FreshDirect.globalnav.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
	      window.FreshDirect.coremetricsData = window.FreshDirect.browse.data.coremetrics;
	      window.FreshDirect.browse.data.searchParams = window.FreshDirect.browse.data.searchParams || {};

	      window.FreshDirect.browse.data.sortOptions = window.FreshDirect.browse.data.sortOptions || {};
	    </script>
	    <style>
	    	.browseContent h2 { font-size: 23px; }
	    </style>
  </tmpl:put>

  <tmpl:put name='extraJsModules'>
    <jwr:script src="/browse.js"  useRandomParam="false" />
    <jwr:script src="/srch.js"  useRandomParam="false" />
  </tmpl:put>
</tmpl:insert>