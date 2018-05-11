<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<%
			String template = "/common/template/browse_template.jsp";

			boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user)
					&& JspMethods.isMobile(request.getHeader("User-Agent"));
			if (mobWeb) {
				template = "/common/template/mobileWeb.jsp"; //mobWeb template
			}
%>
<tmpl:insert template='<%=template%>'>

	<tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="common"/>
    <soy:import packageName="multisrch"/>
	</tmpl:put>

	<tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/multisearch.css" media="all" />
	</tmpl:put>

  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="Search for multiple products" pageId="express_search"></fd:SEOMetaTag>
  </tmpl:put>

	<tmpl:put name='mobileSubMenu' direct='true'>
    <div class="refine-btn-cont">
      <button class="cssbutton green transparent modify-list-btn">Modify <span class="offscreen">list</span></button>
    </div>
  </tmpl:put>

	<tmpl:put name='leftnav' direct='true'>
    <div id="multisearch-input">
    </div>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <h2>Type in a list of products and we will find them for you</h2>
    <div id="multisearch-results" class="contentModules">
    </div>
    <div id="multisearch-tutorial">
      <fd:IncludeMedia name="/media/editorial/site_pages/informational/multisearch.html" />
    </div>
  </tmpl:put>
  
	<tmpl:put name='extraJsModules'>
    <script>
      var FreshDirect = window.FreshDirect || {};
      FreshDirect.multisearch = FreshDirect.multisearch || {};

      FreshDirect.multisearch.limit = +'<%= FDStoreProperties.getMultiSearchLimit() %>';
      FreshDirect.multisearch.defaultList = '<%= FDStoreProperties.getMultiSearchDefaultList() %>';
      FreshDirect.multisearch.list = '<%= user.getMultiSearchList() %>';
      if (FreshDirect.multisearch.list === 'null') {
        FreshDirect.multisearch.list = "";
      }
    </script>
    <jwr:script src="/multisearch.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
