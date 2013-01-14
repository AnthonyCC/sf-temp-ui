<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import="com.freshdirect.cms.*"%>
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import='com.freshdirect.fdstore.attributes.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.prodconf.DefaultProductConfigurationStrategy"%>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.content.util.QueryParameterCollection"%>
<%@ page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
<%@ page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='oscache' prefix='oscache'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<% //expanded page dimension
final int W_INDEX_TOTAL = 970;
final int W_INDEX_CENTER_PADDING = 20;
final int W_INDEX_RIGHT_CENTER = W_INDEX_TOTAL - 228 - W_INDEX_CENTER_PADDING;
%>

<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" id="user" />
<%  

	// copied from the old search.jsp:
	
	final String trk = "srch"; // tracking code

	// OAS AD settings
	request.setAttribute("sitePage", "www.freshdirect.com/search.jsp");
	request.setAttribute("listPos", "SystemMessage,LittleRandy,CategoryNote");

	// storing the view settings in the session
	FilteringNavigator nav = new FilteringNavigator(request,16);

  request.setAttribute("filternav", nav);

	ConfigurationContext confContext = new ConfigurationContext();
	confContext.setFDUser(user);
	ConfigurationStrategy confStrat = new DefaultProductConfigurationStrategy();
	
	final int hideAfter = 8;
	boolean otherFilters=false;
	
	// default page size
	final int defaultPageSize = nav.getDefaultPageSize();
	QueryParameterCollection qc = QueryParameterCollection.decode(request.getQueryString());
	if ( qc.getParameter("pageSize") == null ) {
		nav.setPageSize(defaultPageSize);
	}
	
%>

<fd:SimpleSearch id="search" nav="<%= nav %>"/>
<bean:define id="activeTabVal" value='<%= (!search.getProducts().isEmpty() && request.getParameter("recipes")==null) || (search.getProducts().isEmpty() && search.getRecipes().isEmpty()) ? "products" : "recipes" %>' />

<tmpl:insert template='/common/template/search.jsp'>
	<tmpl:put name="customCss">
		<fd:css href="/assets/css/common/grid_supplement.css" />
		<fd:css href="/assets/css/common/product_grid.css" />
		<style>
			.product-grid { width:803px; }
			.items { width:804px; }
			.grid-view .grid-item-container { width:201px; }
		</style>
		<fd:css href="/assets/css/search.css"></fd:css>
	</tmpl:put>
	<tmpl:put name="customJs">
	</tmpl:put>
	<tmpl:put name="customJsBottom">
		<fd:javascript src="/assets/javascript/fd/modules/search/seemore.js" />
		<fd:javascript src="/assets/javascript/fd/modules/search/statusupdate.js" />
	</tmpl:put>

	<tmpl:put name="title" direct="true">FreshDirect - Search - <%= nav.getSearchTerm() %></tmpl:put>
	<tmpl:put name="activeView">grid<% //= nav.isListView() && !nav.isRecipes() ? "list" : "grid" %></tmpl:put>
	<tmpl:put name="noResult"><%= search.getProducts().isEmpty() && search.getRecipes().isEmpty() ? "noresult" : "hasresults" %></tmpl:put>
	<tmpl:put name="startPage"><%= nav.getSearchTerm()==null || nav.getSearchTerm().length()==0 ? "startpage" : "resultpage" %></tmpl:put>

	<tmpl:put name="content-header">
		<form class="span-17 last"><span id="searchinput-wrapper" class="middle"><input type="text" name="searchParams" id="searchinput" class="top" autocomplete="off" value="<%= nav.getSearchTerm() %>"/></span><input type="submit" value="search" id="searchbutton" class="button middle brown_bg white bold"/></form>
		<fd:CmPageView wrapIntoScriptTag="true" searchTerm="<%=search.getSearchTerm()%>" searchResultsSize="<%=search.getProducts().size()%>" suggestedTerm="<%=search.getSuggestedTerm()%>" recipeSearchResultsSize="<%=search.getRecipes().size()%>"/>
		<fd:CmElement wrapIntoScriptTag="true" elementCategory="search_filter" searchNavigator="<%= nav %>" />
		<fd:CmElement wrapIntoScriptTag="true" elementCategory="search_sort" searchNavigator="<%= nav %>" />
		<% if (FDStoreProperties.isSearchGlobalnavAutocompleteEnabled()) { %>
		<div id="searchTerms" class="termsStyle" style="position: absolute; background-color: white"></div>
		<script type="text/javascript">YAHOO.util.Event.onDOMReady(autoCompleteFunctionFactory(null, "searchTerms", "searchinput"));</script>
		<% } %>
	</tmpl:put>


	<tmpl:put name="didyoumean"><%@ include file="/includes/search/didyoumean.jspf"%></tmpl:put>
	
<fd:ProductsFilter results="<%= search %>" nav="<%= nav %>" domainsId="menus" itemsId="items" filteredItemCountId="itemCount">
<fd:ProductsGroupingAndPaging items="<%= items %>" itemsId="products" nav="<%= nav %>">
<tmpl:put name="productTabItemCount"><%= itemCount %></tmpl:put>
<logic:equal name="activeTabVal" value="products">
	<% ArrayList selection = new ArrayList();  %>
	<tmpl:put name="activeTab">products</tmpl:put>
	<tmpl:put name="search-header">
		<% int productCount = search.getProducts().size(); %>
		<% if(search.getSuggestedTerm() == null || search.getSuggestedTerm().length() == 0 ) { %>
		<span class="itemcount"><%= productCount %></span> product<%= productCount > 1 ? "s" : "" %> found for <span class="search-string"><%= nav.getSearchTerm() %></span>
		<% } else { %>
		<span class="itemcount">0</span> product found for <span class="search-string"><%= nav.getSearchTerm() %></span> | Showing <span class="itemcount"><%= itemCount %></span> product<%= productCount > 1 ? "s" : "" %> for <span class="search-string"><%= search.getSuggestedTerm() %></span>
		<% } %>
	</tmpl:put>

	<tmpl:put name="toolbar">
		<div id="sorter" class="span-10">
			<span class="label">Sort:</span>
			<display:SortBar defaultSort="relv" sortItems="<%= new SearchSortType[] {SearchSortType.BY_RELEVANCY,SearchSortType.BY_NAME,SearchSortType.BY_PRICE, SearchSortType.BY_POPULARITY, SearchSortType.BY_SALE} %>">
				<a href="<%= currentUrl %>" class="sortitem <%= isSelected ? "sortitem-selected" : ""%> <%= currentIndex==1 ? "nodot" : ""%>"><%= currentText%></a>
			</display:SortBar>					
		</div>
	</tmpl:put>

	<tmpl:put name="viewAll">
<% 	if( itemCount > defaultPageSize) {
		if(nav.getPageSize() == 0) { 
			nav.resetState();
			nav.setPageSize(defaultPageSize);
%>
		<a href="<%= nav.getLink() %>" class="button middle white bold view-all">Show <%= defaultPageSize %></a>
<%		} else {
			nav.resetState();
			nav.setPageSize(0);
%>
		<a href="<%= nav.getLink() %>" class="button middle white bold view-all">Show all</a>
<%	} 

		nav.resetState();
}	%>
	</tmpl:put>
	
	<tmpl:put name="pagerTop">
		<div class="results">
			<span>Results: </span>
			<span class="results-current"><%= ((nav.getPageNumber())*nav.getPageSize())+1  %>-<%= nav.getPageSize()==0 ? 
						itemCount : Math.min((nav.getPageNumber()+1)*nav.getPageSize(),itemCount)  %></span>
			<span>of</span>
			<span class="results-all"><%= itemCount %></span>
		</div>
		<div class="pager-content">
		<% if ( nav.getPageSize() > 0 && itemCount > nav.getPageSize()) { %>
			<display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/>
		<% } %>
			<tmpl:get name="viewAll" />
		</div>
	</tmpl:put>

	<tmpl:put name="pagerBottom">
		<div class="pager-content">
		<% if ( nav.getPageSize() > 0 && itemCount > nav.getPageSize()) { %>
			<display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/>
		<% } %>
			<tmpl:get name="viewAll" />
		</div>
		<div class="back-to-top"><a href="#content_top">back to top</a></div>
	</tmpl:put>
	
	<tmpl:put name="recommendations-content" direct="true">
		<fd:ProductGroupRecommender itemCount="16" siteFeature="SRCH" facility="default" id="recommendedProducts">
		<div class="search-recommender">
			<h3><%= recommendedProducts.getVariant().getServiceConfig().getPresentationTitle() %></h3>
			<script type="text/javascript">
				var search_recommender_events = {"afterScroll":  <fd:CmElement wrapIntoFunction="true" siteFeature="SRCH" elementCategory="carousel"/>} 
			</script>
			<display:Carousel id="cat1_carousel" carouselId="cat1_carousel" width="816" numItems="4" showCategories="false" itemsToShow="<%= recommendedProducts.getProducts() %>" trackingCode="<%= trk %>" maxItems="32" eventHandlersObj="search_recommender_events">
				<span class="smartstore-carousel-item">
					<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
					<% ProductImpression pi = confStrat.configure((ProductModel)currentItem, confContext); %>
					<a href="<%=CmMarketingLinkUtil.getSmartStoreLink(FDURLUtil.getProductURI(pi.getProductModel(), trk), recommendedProducts)%>" hidden style="display: none;" class="product-name-link"></a> <%-- For Coremetrics impression tracking --%>
					<%pageContext.setAttribute("PRODUCT_BOX_VARIANT",recommendedProducts.getVariant().getId());%>
					<div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div>
					</display:GetContentNodeWebId>
				</span>
			</display:Carousel>
		</div>
		</fd:ProductGroupRecommender>	
	</tmpl:put>	
	<%
	// RECOMMENDER for "view 20"
	if ( nav.getPageSize() != 0) { %>
	<tmpl:put name="recommendations" direct="true">
		<tmpl:get name="recommendations-content" />
	</tmpl:put>	
	<% } %>
	
	<tmpl:put name="content" direct="true">
		<%
		pageContext.setAttribute("ISONSEARCHPAGE",true);
		for (ListIterator<FilteringSortingItem <ProductModel>> it=products.listIterator() ; it.hasNext();) {
			{
			ProductImpression pi = confStrat.configure(it.next().getModel(), confContext);
			%><div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div><%
			}		
			// RECOMMENDER for "show all"
			if ( nav.getPageSize() == 0 && it.nextIndex() == defaultPageSize ) {
			%>
			<tmpl:get name="recommendations-content" />
			<%
			}
					
		} 
		%>
		
	</tmpl:put>

  <tmpl:put name='filterNavigator'>
    <% 
    	request.setAttribute("filtermenus", menus);
		request.setAttribute("activeTabVal", activeTabVal);    
    %>
    <tmpl:insert template='/common/template/filter_navigator.jsp'>
    </tmpl:insert>
  </tmpl:put>

	<tmpl:put name="selection-header" direct="true">		
	</tmpl:put>

	<tmpl:put name="selection-list" direct="true">		
	</tmpl:put>
	
	<tmpl:put name="pagerTop">
		<% nav.resetState(); %>
		<div class="results">
			<span>Results: </span>
			<span class="results-current"><%= ((nav.getPageNumber())*nav.getPageSize())+1  %>-<%= nav.getPageSize()==0 ? 
						itemCount : Math.min((nav.getPageNumber()+1)*nav.getPageSize(),itemCount)  %></span>
			<span>of</span>
			<span class="results-all"><%= itemCount %></span>
		</div>
		<div class="pager-content"><display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/><tmpl:get name="viewAll" /></div>
	</tmpl:put>

	<tmpl:put name="pagerBottom">
		<% nav.resetState(); %>
		<div class="pager-content"><display:Pager productsSize="<%= itemCount %>" nav="<%= nav %>"/><tmpl:get name="viewAll" /></div>
		<div class="back-to-top"><a href="#content_top">back to top</a></div>
	</tmpl:put>

</logic:equal>
</fd:ProductsGroupingAndPaging>
</fd:ProductsFilter>

<% nav.resetState();nav.setPageSize(0); %>
<fd:SearchRecipeFilter results="<%= search %>"  nav="<%= nav %>" domainsId="menus" itemsId="recipes" filteredItemCountId="itemCount">
<tmpl:put name="recipeTabItemCount"><%= itemCount %></tmpl:put>

<% if(itemCount <=0) {%>
<tmpl:put name="productsOnly">products-only</tmpl:put>
<% }%>

<logic:equal name="activeTabVal" value="recipes">
	<tmpl:put name="search-header">
		<% int recipeCount = search.getRecipes().size(); %>
		<% if(search.getSuggestedTerm() == null || search.getSuggestedTerm().length() == 0) { %>
		<span><span class="itemcount"><%= recipeCount %></span> recipe<%= recipeCount > 1 ? "s" : "" %> found for <span class="search-string"><%= nav.getSearchTerm() %></span></span>
		<% } else { %>
		<span>Showing <span class="itemcount"><%= recipeCount %></span> recipe<%= recipeCount > 1 ? "s" : "" %> for <span class="search-string"><%= search.getSuggestedTerm() %></span></span>
		<% } %>
	</tmpl:put>
	<tmpl:put name="activeTab">recipes</tmpl:put>
	<tmpl:put name='recipesFilter'>
		<div id="recipesfilter" class="filterbox sidebar-content">
			<h3>Recipes</h3>
			<ul>
				<% nav.resetState();nav.setRecipes(true); nav.removeAllFilters(); nav.setPageOffset(0); %>
				<li><a href="<%= nav.getLink() %>">All</a></li>
<fd:FilterList domainName="<%= EnumFilteringValue.RECIPE_CLASSIFICATION %>" filters='<%= menus %>' id="filterItems">
	<% nav.resetState(); %>
	<logic:iterate id="menu" collection="<%= filterItems %>" type='FilteringMenuItem'>
			<% if(menu.isSelected()) { %>
				<% nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue()); %>
				<li class="selected"><a href="<%= nav.getLink() %>" class="remove-selection"></a><a href="<%= nav.getLink() %>"><%= menu.getName() %></a></li>
			<% } else { %>
				<% nav.setRecipeFilter(menu.getFilteringUrlValue()); nav.setRecipes(true); nav.setPageOffset(0); %>
				<li><a href="<%= nav.getLink() %>"><%= menu.getName() %></a><span class="count">(<%= menu.getCounter() %>)</span></li>
			<% } %>
	</logic:iterate>
</fd:FilterList>
			</ul>
		</div>
	</tmpl:put>
	<tmpl:put name="content" direct="true">
		<logic:iterate id="item" collection="<%= recipes %>" type='FilteringSortingItem<Recipe>'>
			<% Recipe recipe = item.getModel(); %>
			<div class="grid-item-container"><%@ include file="/includes/product/i_recipe_box.jspf" %></div>
		</logic:iterate>	
	</tmpl:put>
	
  <tmpl:put name='filterNavigator'>
    <% 
    	request.setAttribute("filtermenus", menus);
		request.setAttribute("activeTabVal", activeTabVal);    
    %>
    <tmpl:insert template='/common/template/filter_navigator.jsp'>
    </tmpl:insert>
  </tmpl:put>

	
</logic:equal>
</fd:SearchRecipeFilter>


<tmpl:put name="productTabLink"><% nav.resetState(); nav.removeAllFilters(); nav.setPageOffset(0); nav.setRecipes(false); %><%= nav.getLink() %></tmpl:put>
<tmpl:put name="recipesTabLink"><% nav.resetState(); nav.removeAllFilters(); nav.setPageOffset(0); nav.setRecipes(true);  %><%= nav.getLink() %></tmpl:put>
<% nav.resetState(); %>


</tmpl:insert>

