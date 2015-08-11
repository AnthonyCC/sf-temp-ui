<%@page import="com.freshdirect.fdstore.content.CMSWebPageModel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.freshdirect.fdstore.content.CMSPageRequest"%>
<%@page import="com.freshdirect.fdstore.content.CMSContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.CMSSectionModel"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<html>
	<head><title>Feed</title></head>
	<body>
		<div>
			<form action="">
				<input name="pageName" type="text" value="Feed"/>
				<input name="dateTime" type="text" value="2015-07-16T12:12:00.000Z"/>
				<input type="submit"/>			
			</form>
		</div>
		<%
			CMSContentFactory factory = CMSContentFactory.getInstance();
			CMSPageRequest pageRequest = new CMSPageRequest();
			
			
			String pageName = request.getParameter("pageName") != null ? request.getParameter("pageName") : "Feed";
			String date = request.getParameter("dateTime");
			pageRequest.setRequestedDate(new Date());
			
			
			out.println(date);
			out.println(pageName);
			pageRequest.setPageType(pageName);
			List<CMSWebPageModel> pages = factory.getCMSPageByParameters(pageRequest);
			String greetingText = "Welcome";
			
		%>
		<c:set var="pages" value="<%=pages %>"/>
		<div id="moduleContainer">
		<c:forEach var="page" items="${pages}">
		<c:forEach var="section" items="${page.sections}">
			${section.type}
			<c:if test="${section.type eq 'PeakAhead'}">
				<fd:PeekAheadPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.type eq 'Essentials'}">
				<fd:EssentialsPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.type eq 'Greeting'}">
				<fd:GreetingPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.type eq 'Trending'}">
				<fd:TrendingPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.type eq 'FeaturedCategoryLevel'}">
				<fd:FeaturedCategoryPreviewTag section="${section}"/>
			</c:if>
			<c:if test="${section.type eq 'FeaturedProductLevel'}">
				<fd:FeaturedProductPreviewTag section="${section}"/>
			</c:if>
		</c:forEach>
		<div style="height:2px;"><br/></div>
		</c:forEach>
		</div>
	</body>
	<style>
	
	#moduleContainer{
	margin: 0px 500px 0px 500px;
	border: 1px solid black;
}
.moduleGreeting{
background-color: blue;
text-align: center;
color: white;
}

.moduleEssentials1{
}
.ModuleBanner{
	position: relative;
}
.ModuleBanner h6, .ModuleBanner h2, .ModuleBanner h3{
	position: absolute;
  color: #fff;
  font-weight: bold;
  text-align: center;
  
  margin: 0;
}
.ModuleBanner h2{font-size: 25px; top: 50%; left: 10%;}
.ModuleBanner h3{font-size: 20px; top: 20%; left: 0%; background-color: red;}
.ModuleBanner h6{font-size: 15px; top: 40%; left: 25%;}
.moduleFeatCatLevel{}
.modulePeekAhead{}
.moduleFeatProdLevel{}
.moduleTrending{
	background-color: blue;
	text-align: left;
	color: white;
}
.moduleEssentials2{}
.moduleFeatCroCatLevel{}
.moduleFreshModule{}
ul{
	padding: 0;
	margin: 0;
}
ul li{
  list-style-type: none;
  border-bottom: 1px solid #fff;
  padding: 10px 5px 7px 9px;
}
ul li span{
	display: inline-block;
	vertical-align: top;
	width:70%;

}
h2{    padding: 5px 0px 5px 5px;}
	
	
		a{
			color:#ffffff
		}
		
		.topDiv{
			margin:0 200 0 200;width:250px; color:#ffffff;
		}
		.greetingDiv{
			background:#15428b;  padding: 10 10 10 10;
		}
		.gap{
			height: 20px;
			background:#ffffff;
		}
		
		#carousel123 ul li{
			display:inline-block;
			width:44px;	
			vertical-align: top;
			  font-size: 11px;
		}
		#carousel123 ul li img{
			width: inherit;
		}
	
	</style>
</html>