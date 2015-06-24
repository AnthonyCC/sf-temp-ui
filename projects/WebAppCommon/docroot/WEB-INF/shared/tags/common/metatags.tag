<%@ tag body-content="scriptless" description="SEO Metatag file" %>

<%@ attribute name="metaDescription" type="java.lang.String" required="false" %>
<%@ attribute name="pageId" type="java.lang.String" required="false" %>
<%@ attribute name="language" type="java.lang.String" required="false" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>

<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty language}">
       <c:set var="language" value="en-US" />
</c:if>

<%-- SEO meta content --%>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" lang="${language}"/>
<c:if test="${empty metaDescription}">
       <c:set var="metaDescription" value="Online grocer providing high quality fresh foods and popular grocery and  household items at incredible prices delivered to the New York area."/>
</c:if>
<%--Set the page title --%>
<c:if test="${empty title}">
       <c:set var="title" value="FreshDirect"/>
</c:if>
<title>${title}</title>
<meta name="description" content="${metaDescription}" />
