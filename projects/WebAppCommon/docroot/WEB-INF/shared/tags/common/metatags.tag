<%@ tag body-content="scriptless" description="SEO Metatag file" %>

<%@ tag import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@ tag import="com.freshdirect.cms.application.CmsManager" %>
<%@ tag import="com.freshdirect.cms.ContentKey"%>
<%@ tag import="com.freshdirect.cms.ContentNodeI"%>

<%@ attribute name="metaDescription" type="java.lang.String" required="false" %>
<%@ attribute name="pageId" type="java.lang.String" required="false" %>
<%@ attribute name="language" type="java.lang.String" required="false" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%@ attribute name="includeSiteSearchLink" type="java.lang.Boolean" required="false" %>

<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty language}">
       <c:set var="language" value="en-US" />
</c:if>

<%-- SEO meta content --%>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8" lang="${language}"/>
<c:if test="${empty metaDescription}">
	<c:set var="metaDescription" value="Online grocer providing high quality fresh foods and popular grocery and  household items at incredible prices delivered to the New York area."/>
	<c:if test="${not empty pageId}">
	<%
		ContentKey key = new ContentKey(FDContentTypes.WEBPAGE, pageId); 
		ContentNodeI webPage = CmsManager.getInstance().getContentNode(key);
		if(webPage != null){ 
			String metaDescription = (String) webPage.getAttributeValue("SEO_META_DESC");
			String title = (String) webPage.getAttributeValue("PAGE_TITLE");		
		%>
			<c:set var="metaDescription" value="<%=metaDescription%>"/>
			<c:set var="title" value="<%=title%>"/>
	<% 	} 
	%>
	</c:if>
</c:if>
<%--Set the page title --%>
<c:if test="${empty title}">
       <c:set var="title" value="FreshDirect"/>
</c:if>
<title>${title}</title>
<meta name="description" content="${metaDescription}" />
<%-- Site link Search Box --%>
<c:if test="${includeSiteSearchLink}">
<script type="application/ld+json">
{
   "@context": "http://schema.org",
   "@type": "WebSite",
   "url": "https://www.freshdirect.com/",
   "potentialAction": {
       "@type": "SearchAction",
       "target": "https://www.freshdirect.com/srch.jsp?pageType=search&searchParams={search_term_string}&pageSize=30&all=false&activePage=1&sortBy=Sort_Relevancy&orderAsc=true&activeTab=product",
       "query-input": "required name=search_term_string"
   }
}
</script>
<script type="application/ld+json">
{
   "@context": "http://schema.org",
   "@type": "Organization",
   "url": "https://www.freshdirect.com/",
   "name": "FreshDirect",
   "logo": "https://www.freshdirect.com/media/images/navigation/global_nav/fd_logo_on.png",
   "sameAs" : [ "https://twitter.com/FreshDirect",
    "https://www.facebook.com/FreshDirect",
    "https://www.pinterest.com/freshdirect",
	"https://plus.google.com/+freshdirect/videos",
	"https://www.youtube.com/FreshDirect"] 
}
</script>

<c:import var="metadataMediaFile" url="/media/editorial/site_pages/metadata.html"/>
<c:out value="${metadataMediaFile}" escapeXml="false" />

</c:if>