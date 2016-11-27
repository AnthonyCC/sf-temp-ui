<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Summer Delivery - FreshDirect</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_summer_service"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/summer_services/landing_page/summer_service_delivery.html" />
	</tmpl:put>
</tmpl:insert>