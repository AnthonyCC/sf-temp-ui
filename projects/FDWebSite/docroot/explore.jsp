<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<potato:globalnav/>
<%
	String template = "/common/template/no_shell.jsp";
	
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user)
			&& JspMethods.isMobile(request.getHeader("User-Agent"));
	if (mobWeb) {
		template = "/common/template/mobileWeb.jsp"; //mobWeb template
	}
%>
<tmpl:insert template='<%=template%>'>
	<tmpl:put name='customCss' direct='true'>
    	<jwr:style src="/explore.css" media="all" />
	</tmpl:put>

	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="Explore"></fd:SEOMetaTag> <%-- TODO superdepartment SEO data--%>
	</tmpl:put>

	<tmpl:put name='mobileSubMenu' direct='true'>
    	<%-- TODO remove? --%>
	</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<div class="container explore">
			<h1 class="explore__title">Shop by category</h1>
			<h2 class="explore__subtitle">Select a category below to browse our assortment.</h2>
			<div id="explore-superdepartment-list">
			</div>
			<div id="explore-department-list">
			</div>
			<div id="explore-category-list">
			</div>
		</div>
	</tmpl:put>
  
	<tmpl:put name='customJsBottom'>
		<soy:import packageName="common"/>
    	<soy:import packageName="explore"/>
	    <script>
			var FreshDirect = window.FreshDirect || {};
			
			FreshDirect.explore = window.FreshDirect.explore || {};
			FreshDirect.explore.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
	    </script>
    	<jwr:script src="/explore.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
