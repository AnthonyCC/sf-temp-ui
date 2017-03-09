<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="fd-features" prefix="features" %>
<features:isActive name="homepageRedesignEnabled" featureName="homepageredesign" />


<c:if test="${homepageRedesignEnabled}">
	<%@ include file="/homepage/redesigned_index.jsp" %>
</c:if>
<c:if test="${not homepageRedesignEnabled}">
	<%@ include file="/homepage/index.jsp" %>
</c:if>