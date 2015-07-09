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
</c:if>

<meta name="verify-v1" content="2MXiorvt33Hqj6QEBylmr/TwpVMfiUQArG0etUIxV2c=" />
<meta name="msvalidate.01" content="2E163086C8383686A98EE1B694357FE7" />
<meta name="p:domain_verify" content="d522a86369f72195972bf60549401f4d"/>

<meta http-equiv="x-dns-prefetch-control" content="off" />
<link rel="dns-prefetch" href="//accounts.google.com" />
<link rel="dns-prefetch" href="//adfarm.mediaplex.com" />
<link rel="dns-prefetch" href="//apis.google.com" />
<link rel="dns-prefetch" href="//connect.facebook.net" />
<link rel="dns-prefetch" href="//ct1.addthis.com" />
<link rel="dns-prefetch" href="//data.coremetrics.com" />
<link rel="dns-prefetch" href="//display.ugc.bazaarvoice.com" />
<link rel="dns-prefetch" href="//freshdirect.com" />
<link rel="dns-prefetch" href="//google.com" />
<link rel="dns-prefetch" href="//hits.convergetrack.com" />
<link rel="dns-prefetch" href="//i1.ytimg.com" />
<link rel="dns-prefetch" href="//libs.coremetrics.com" />
<link rel="dns-prefetch" href="//m.addthis.com" />
<link rel="dns-prefetch" href="//platform.twitter.com" />
<link rel="dns-prefetch" href="//promo.freshdirect.com" />
<link rel="dns-prefetch" href="//s-static.ak.facebook.com" />
<link rel="dns-prefetch" href="//s.ytimg.com" />
<link rel="dns-prefetch" href="//s7.addthis.com" />
<link rel="dns-prefetch" href="//sb.scorecardresearch.com" />
<link rel="dns-prefetch" href="//secure.img-cdn.mediaplex.com" />
<link rel="dns-prefetch" href="//ssl.google-analytics.com" />
<link rel="dns-prefetch" href="//tmscdn.coremetrics.com" />
<link rel="dns-prefetch" href="//twitter.com" />
<link rel="dns-prefetch" href="//youtube.com" />
<link rel="dns-prefetch" href="//4394129.fls.doubleclick.net" />
<link rel="dns-prefetch" href="//insight.adsrvr.org" />

<script src="//cdn.optimizely.com/js/325803703.js" type="text/javascript"></script>