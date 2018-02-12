<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='template' prefix='tmpl' %>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" />
<fd:CheckDraftContextTag/>

<%-- get all the product data needed for display --%>
<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}' variantId='${param.variantId}' grpId='${param.grpId}' version='${param.version}' />
<potato:browse name="browsePotato" pdp="true" nodeId='${param.catId}'/>
<fd:ProductGroup id='productNode' categoryId='${param.catId}' productId='${param.productId}' >

<%
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/no_nav_html5.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

EnumProductLayout prodLayout = productNode.getProductLayout();
String layoutPath = prodLayout.getLayoutPath();

boolean debugIgnoreProductLayout = request.getParameter("debugIgnoreProductLayout")!=null ? "true".equalsIgnoreCase(request.getParameter("debugIgnoreProductLayout")) : false;
boolean forceProductLayout = true; /* force the product to just use the normal layout instead of it's CMS-defined one */

%>
<tmpl:insert template="<%= pageTemplate %>">
	<tmpl:put name='cmeventsource' direct='true'>Lightweight PDP</tmpl:put>
	
	<tmpl:put name='extraCss' direct='true'>
    	<jwr:style src="/quickshop.css" media="all" />
    	<jwr:style src="/pdp.css" media="all" />
  	</tmpl:put>
	
	<tmpl:put name='soypackage' direct='true'>
    	<soy:import packageName="pdp" />
    	<soy:import packageName="browse" />
  	</tmpl:put>
  
	<tmpl:put name='content' direct='true'>
		<%-- js data to help with debugging in ui --%>
		<%@ include file="/includes/product/productDetail.jspf" %>
		<script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.browse = window.FreshDirect.browse || {};
			window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};
			
			window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>;
			window.FreshDirect.activeDraft = "${activeDraft}";
			window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}";
			
			window.FreshDirect.pdp = window.FreshDirect.pdp || {};
			
			window.FreshDirect.pdp.layout = {
				'forced': '<%= forceProductLayout %>', /* param: debugIgnoreProductLayout=true */
				'id': '<%= prodLayout.getId() %>',
				'name': '<%= prodLayout.getName() %>'
				<%= (productNode.getSpecialLayout()!=null) 
					? ", 'special' : { 'id': '"+productNode.getSpecialLayout().getId()+"', 'name': '"+productNode.getSpecialLayout().getName()+"' }" 
					: "" %>
			};
			window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true"/>;
			window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>;
	    </script>
			
	</tmpl:put>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</tmpl:put>
	<tmpl:put name='extraJsModules'>
	</tmpl:put>
</tmpl:insert>
</fd:ProductGroup>
