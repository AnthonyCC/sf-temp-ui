<%@page import="com.freshdirect.fdstore.customer.FDUserI"
		import="com.freshdirect.webapp.taglib.fdstore.SessionName"
		import="com.freshdirect.fdstore.content.DepartmentModel"
		import="com.freshdirect.fdstore.content.ContentFactory"
		import="com.freshdirect.fdstore.FDStoreProperties"
		import="com.freshdirect.fdlogistics.model.FDReservation"
		import="com.freshdirect.webapp.util.CCFormatter"
		import="com.freshdirect.fdlogistics.model.FDTimeslot"
		import="com.freshdirect.webapp.util.FDURLUtil"%><%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %><% 
//expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;

// YUI is not required for cos.jsp
request.setAttribute("noyui", true);

%><fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" /><%

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	DepartmentModel dept = (DepartmentModel) ContentFactory.getInstance().getContentNode("Department", "COS");
    request.setAttribute("sitePage", "www.freshdirect.com/cos.jsp");
    String trkCode = "dpage";
    int validOrderCount = user.getAdjustedValidOrderCount();
%>

<tmpl:insert template='/common/template/no_shell_optimized.jsp'>
	<tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="cos"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name="customCss">
		<fd:css href="/assets/css/homepage/homepage.css"/>
	</tmpl:put>
	<tmpl:put name="customJsBottom">
		<fd:javascript src="/assets/javascript/fd/modules/search/seemore.js" />
		<fd:javascript src="/assets/javascript/fd/modules/search/statusupdate.js" />
	</tmpl:put>
	
	<tmpl:put name='content' direct='true'>

<fd:GetSegmentMessage id='segmentMessage' user="<%=user%>">

<%
	boolean location2Media = false;
	if(null != segmentMessage && segmentMessage.isLocation2()) {
        	location2Media = true;
    }
   	request.setAttribute("listPos", "SystemMessage,HPFeatureTop,HPFeature,HPTab1,HPTab2,HPTab3,HPTab4,HPFeatureBottom,HPWideBottom,COSBottom,HPLeftBottom,HPMiddleBottom,HPRightBottom");
%>

<% 
boolean showAltHome = false;
if (FDStoreProperties.IsHomePageMediaEnabled() && (!user.isHomePageLetterVisited() || 
	(request.getParameter("show") != null && request.getParameter("show").indexOf("letter") > -1))) 
		showAltHome = true;
%>
 	<div class="holder">
		<%-- MAIN CONTENT--%>
			<div class="content cos"> 
<% if (showAltHome && !location2Media) { 
	%><comp:homePageLetter user="<%= user %>" />
<%} else if (!showAltHome && location2Media) { 
	%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>"  isCosPage="<%=true%>"/>
	 <% 
} else if (!showAltHome && !location2Media) { 
	%><comp:welcomeMessage user="<%= user %>" segmentMessage="<%= segmentMessage %>"  isCosPage="<%=true%>"/>
	  <comp:deliverySlotReserved user="<%= user %>" /><%
}
	   	
if (location2Media) { %><comp:location2Media user="<%= user %>" /><% } 
%>		<img src="/media_stat/images/cos/banner-at-the-office.jpg" alt="At the Office">
		<comp:OASFeature 
			top="HPFeatureTop"
			left="HPFeature"
			tab1="HPTab1"
			tab2="HPTab2"
			tab3="HPTab3"
			tab4="HPTab4"
			bottom="HPFeatureBottom"
			hpBottomLeft="HPLeftBottom"
			hpBottomMiddle="HPMiddleBottom"
			hpBottomRight="HPRightBottom"
		/>
        
        <% 
        
        	   			if (validOrderCount<=3){
%>
		   			<div id="most-popular" class="">
			   			<h2 class="homepage-categories-header">
							<span>most popular products</span>
						</h2>
		   				<potato:recommender siteFeature="FAVORITES" name="deals" maxItems="24" cmEventSource="BROWSE"  sendVariant="true" />
		   				<soy:render template="common.ymalCarousel" data="${deals}" />
		   			</div>
<%
          } else  {
          %>
             <div id="top-items" class="">
                <potato:recommender siteFeature="TOP_ITEMS_QS" name="topItems" maxItems="24" cmEventSource="BROWSE" sendVariant="true" />
                <soy:render template="common.yourTopItemsCarousel" data="${topItems}" />
            </div> 
          <%
          }
        %>
		<div id="categories">
			<h2 class="homepage-categories-header">
				<span>food, drinks & supplies for your office</span>
			</h2>
			<logic:iterate collection="<%= dept.getDeptNav() %>" id="cat" type="com.freshdirect.fdstore.content.CategoryModel">
			<logic:equal name="cat" property="showSelf"  value="true" >
				<span class="category figure"><a class="category-name caption" href="<%= FDURLUtil.getCategoryURI(cat,trkCode) %>"><fd:IncludeImage image="<%= cat.getCategoryPhoto() %>" alt="<%= cat.getFullName() %>" /><br><%= cat.getFullName() %></a></span>
			</logic:equal>
			</logic:iterate>	   			
		</div>
		<div id="editorial"><logic:iterate 
			collection="<%= dept.getDepartmentBottom() %>" 
			id="media" 
			type="com.freshdirect.fdstore.content.Html" ><fd:IncludeHtml html="<%= media %>"></fd:IncludeHtml>
		</logic:iterate></div>
		<div class="oas_home_bottom"><script type="text/javascript">OAS_AD('HPWideBottom');</script></div>
	</div>
</div>
</fd:GetSegmentMessage>
</tmpl:put>
</tmpl:insert>
