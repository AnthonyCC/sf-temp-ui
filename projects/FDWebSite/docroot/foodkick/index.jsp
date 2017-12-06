<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ include file="includes/fk_presenter_vars.jspf" %>
<%@ page import="java.util.Calendar" %>
<tmpl:insert template='includes/fklayout_tmpl.jsp'>
   <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Same-Day Food Delivery NYC | FoodKick"/>
   </tmpl:put>
<%-- 	<tmpl:put name='title'>Same-Day Food Delivery NYC | FoodKick</tmpl:put> --%>
	<tmpl:put name='content'>
		<fd:IncludeMedia name="${FK_EDITORIAL_DIR}landing.html" />
	</tmpl:put>
	<tmpl:put name='special_disclaimer'>
	<!-- begin special_disclaimer -->
		<%-- <fd:IncludeMedia name="${FK_EDITORIAL_DIRf}disclaimer.jspf" /> --%>
		<%-- <jsp:include page="${FK_EDITORIAL_DIR}disclaimer.jspf" /> --%>
		<%-- <%@include file="/media/editorial/foodkick/disclaimer.jspf"%> --%>
		
		<fd:IncludeMedia name="${FK_EDITORIAL_DIR}disclaimer.html" />
		
		<%-- <jsp:directive.include file="<%= wut %>disclaimer.jspf" /> --%>
	<!-- end special_disclaimer -->
	</tmpl:put>

	<tmpl:put name='special_js'>
		<!-- begin special_js -->
		<script src="<%=FK_CONFIG_DIRS.get("ANGULAR_DIR") %>/angular.min.js"></script>
		<script src="<%=FK_CONFIG_DIRS.get("ANGULAR_DIR") %>/angular-animate.min.js"></script>
		<script src="<%=FK_CONFIG_DIRS.get("ANGULAR_DIR") %>/angular-sanitize.min.js"></script>
		<script src="<%=FK_CONFIG_DIRS.get("BOOTSTRAP_DIR") %>/js/ui-bootstrap-tpls-0.14.3.min.js"></script>
	
		<script src="<%=FK_CONFIG_DIRS.get("JS_DIR") %>/jquery.slides.min.js"></script>
		<script type="text/javascript">
			window.FK_CONFIG_PATH = "<%=FK_CONFIG_PATH%>";
		
			window.IMAGES_DIR = "<%=FK_CONFIG_DIRS.get("IMAGES_DIR")%>";
			window.CMS_IMAGES_DIR_LP = "<%=FK_CONFIG_DIRS.get("CMS_IMAGES_DIR_LP")%>";
		</script>
		<script src="${FK_EDITORIAL_DIR}lp_top_carousel.js"></script>
		<!-- end special_js -->
	</tmpl:put>
</tmpl:insert>