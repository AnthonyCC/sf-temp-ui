<%@page import="com.freshdirect.storeapi.content.CMSWebPageModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="com.freshdirect.storeapi.content.CMSPageRequest"%>
<%@page import="com.freshdirect.storeapi.content.CMSSectionModel"%>
<%@page import="com.freshdirect.wcms.CMSContentFactory"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html lang="en-US" xml:lang="en-US">
	<head><title>Feed</title></head>
	<body>
		
		<%
			//CMSContentFactory factory = CMSContentFactory.getIn7stance();
			CMSContentFactory factory = new CMSContentFactory();
			CMSPageRequest pageRequest = new CMSPageRequest();
			String pageName = request.getParameter("pageName") != null ? request.getParameter("pageName") : "Feed";
			String date = request.getParameter("dateTime");
			if(date != null){
			SimpleDateFormat format = new SimpleDateFormat(
				    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
			pageRequest.setRequestedDate(format.parse(date));
			} else {
			pageRequest.setRequestedDate(new Date());
			}
			pageRequest.setPageType(pageName);
			pageRequest.setPreview(true);
			pageRequest.setIgnoreSchedule(request.getParameter("ignoreSchedule") != null);
			List<CMSWebPageModel> pages = factory.getCMSPageByParameters(pageRequest);
		%>
		<c:set var="pages" value="<%=pages %>"/>
		<div>
		<div id="forms">
			<form action="">
				Name: <input name="pageName" type="text" value="Feed"/>
				Date: <input name="dateTime" type="text" value="2015-07-16T12:12:00.000Z"/>
				<input type="submit"/>			
			</form>
		</div>
		<div id="moduleContainer" style="width:30%">
		<c:forEach var="page" items="${pages}">
		<c:forEach var="section" items="${page.sections}">
			<c:if test="${section.displayType eq 'Greeting'}">
				<fd:GreetingPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.displayType eq 'HorizontalPickList'}">
				<fd:EssentialsPreviewTag section="${section}"/>
			</c:if> 
			<c:if test="${section.displayType eq 'VerticalPickList'}">
				<fd:FeaturedCategoryPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.displayType eq 'FeaturedProduct'}">
				<fd:FeaturedProductPreviewTag section="${section}"/>
			</c:if> 
			<c:if test="${section.displayType eq 'ShortBanner'}">
				<fd:TrendingPreviewTag section="${section}"/>
			</c:if>
		</c:forEach>
		<div style="height:2px;"><br/></div>
		</c:forEach>
		</div>
		</div>
	</body>
<style>
/* Needs to moved to separate css file */	
#moduleContainer{margin: 0px 0px 0px 0px;border: 1px solid black;}
.moduleGreeting{background-color: blue;text-align: center;color: white;}
.moduleEssentials1{}
.ModuleBanner{position: relative;}
.ModuleBanner h6, .ModuleBanner h2, .ModuleBanner h3{position: absolute;color: #fff;font-weight: bold;text-align: center;margin: 0;}
.ModuleBanner h2{font-size: 25px; top: 50%; left: 10%;}
.ModuleBanner h3{font-size: 20px; top: 20%; left: 0%; background-color: red;}
.ModuleBanner h6{font-size: 15px; top: 40%; left: 25%;}
.moduleFeatCatLevel{}
.modulePeekAhead{}
.moduleFeatProdLevel{}
.moduleTrending{background-color: blue;text-align: left;color: white;}
.moduleEssentials2{}
.moduleFeatCroCatLevel{}
.moduleFreshModule{}
ul{padding: 0;margin: 0;}
ul li{list-style-type: none;border-bottom: 1px solid #fff;padding: 10px 5px 7px 9px;}
ul li span{display: inline-block;vertical-align: top;width:70%;}
h2{padding: 5px 0px 5px 5px;}
a{color:#ffffff;}
.topDiv{margin:0 200 0 200;width:250px; color:#ffffff;}
.greetingDiv{background:#15428b;  padding: 10 10 10 10;}
.gap{height: 20px;background:#ffffff;}
#carousel123 ul li{display:inline-block;width:44px;vertical-align: top;font-size: 11px;}
#carousel123 ul li img{width: inherit;}
.blueButton{display: inline;background: #0099FF;padding: 5px 5px 5px 5px; color:#ffffff;}
</style>
</html>