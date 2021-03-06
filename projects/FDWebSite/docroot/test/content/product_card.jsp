<%--
	Uses the same params as pdp.jsp
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri='template' prefix='tmpl' %>
<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" />
<%-- get all the product data needed for display --%>
<potato:product name="productPotato" extraName="productExtraPotato" productId='${param.productId}' categoryId='${param.catId}' variantId='${param.variantId}' grpId='${param.grpId}' version='${param.version}' />
<%
String pageTemplate = "/common/template/no_nav_html5.jsp";

/* mobweb support (variable for checks) */
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	/* don't actually switch the template since we don't want the mobweb navs */
	//pageTemplate = "/common/template/mobileWeb.jsp"; //default
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com"+request.getRequestURI();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}
%>
<tmpl:insert template="<%= pageTemplate %>">
	<%-- event source must be a valid enum --%>
	<tmpl:put name='eventsource' direct='true'>UNKNOWN</tmpl:put>
	
	<tmpl:put name='extraCss' direct='true'>
	</tmpl:put>
	
	<tmpl:put name='soypackage' direct='true'>
	</tmpl:put>
  
	<tmpl:put name='content' direct='true'>
		<%-- js data to help with debugging in ui --%>
		<script>
			window.FreshDirect = window.FreshDirect || {};
			window.FreshDirect.pdp = window.FreshDirect.pdp || {};
			window.FreshDirect.pdp.data = window.FreshDirect.pdp.data || {};
		
			window.FreshDirect.pdp.data = <fd:ToJSON object="${productPotato}" noHeaders="true"/>;
			window.FreshDirect.pdp.extraData = <fd:ToJSON object="${productExtraPotato}" noHeaders="true"/>;
		</script>
		
		<%-- do actual display --%>
		<section class="container">
			<section class="content transactional"><%-- "transactional" class required for popout --%>
				<soy:render template="common.potatoTransactionalGridItem" data="${productPotato}" />
			</section>
		</section>
		
		<script><%-- bind to show events to remove cart confirm --%>
			$jq(document).on('afterShow.fb', function( e, instance, slide ) {
				instance.current.$content.find('[data-component="ATCButton"]').attr('data-ignoreredirect', true);
			});
		</script>
	</tmpl:put>

	<tmpl:put name="jsmodules">
		<%@ include file="/common/template/includes/i_jsmodules.jspf" %><%-- required for popout --%>
	</tmpl:put>
	<tmpl:put name='extraJsModules'>
	</tmpl:put>
</tmpl:insert>
