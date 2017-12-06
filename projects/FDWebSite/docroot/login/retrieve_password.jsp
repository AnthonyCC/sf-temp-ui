<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.ContentFactory'%>
<%@ page import='com.freshdirect.fdstore.EnumEStoreId' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<%
String emailAddress = request.getParameter("emailAddress");


%>
<fd:CheckLoginStatus id="user" />
<fd:ForgotPasswordController results="result" successPage='/index.jsp' password="password">	
<tmpl:insert template='/common/template/no_site_nav.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Forgot Your Password? - Enter Security Word"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter Security Word</tmpl:put> --%>
		<tmpl:put name='content' direct='true'>
			<%@ include file="/login/includes/retrieve_password.jspf" %>
		</tmpl:put>
	</tmpl:insert>
</fd:ForgotPasswordController>
