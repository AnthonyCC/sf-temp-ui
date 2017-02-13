<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="fd-features" prefix="features" %>
<features:isActive name="HomepageRedesignEnabled" featureName="homepageredesign" />


<c:if test="${HomepageRedesignEnabled}">
	<%@ include file="/homepage/redesigned_index.jsp" %>
</c:if>
<c:if test="${not HomepageRedesignEnabled}">
	<%@ include file="/homepage/index.jsp" %>
</c:if>