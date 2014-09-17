<%@page import="com.freshdirect.customer.EnumATCContext"%>
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
<%@ page import="com.freshdirect.FDCouponProperties"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil"%>
<%@ page import='com.freshdirect.webapp.ajax.browse.FilteringFlowType' %>
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
<fd:SearchRedesignRedirector user="<%=user%>" pageType="<%=FilteringFlowType.ECOUPON%>"/>
<%  

	// copied from the search.jsp:
	
	final String trk = "coupon"; // tracking code
	final String categoryId = FDCouponProperties.getCouponCMSCategory(); // tracking code

	// OAS AD settings
	request.setAttribute("sitePage", "www.freshdirect.com/ecoupon.jsp");
	request.setAttribute("listPos", "SystemMessage,CategoryNote,ECouponTop,ECouponTab1,ECouponTab2,ECouponTab3,ECouponBottom");

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

<%
	SearchResults search = FDCustomerCouponUtil.getCouponsAsSearchResults(user, false);
%>		
<bean:define id="activeTabVal" value='<%= (!search.getProducts().isEmpty() && request.getParameter("recipes")==null) || (search.getProducts().isEmpty() && search.getRecipes().isEmpty()) ? "products" : "recipes" %>' />

<tmpl:insert template='/common/template/ecoupon.jsp'>
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
	</tmpl:put>

	<tmpl:put name="title" direct="true">FreshDirect - Coupon Circular Page</tmpl:put>
	<tmpl:put name="activeView">grid<% //= nav.isListView() && !nav.isRecipes() ? "list" : "grid" %></tmpl:put>
	<tmpl:put name="noResult"><%= search.getProducts().isEmpty() && search.getRecipes().isEmpty() ? "noresult" : "hasresults" %></tmpl:put>
	<tmpl:put name="startPage">resultpage</tmpl:put>
	<tmpl:put name="productsOnly">products-only</tmpl:put>

	
<fd:ProductsFilter results="<%= search %>" nav="<%= nav %>" domainsId="menus" itemsId="items" filteredItemCountId="itemCount">
<fd:ProductsGroupingAndPaging items="<%= items %>" itemsId="products" nav="<%= nav %>">
<tmpl:put name="productTabItemCount"><%= itemCount %></tmpl:put>
<logic:equal name="activeTabVal" value="products">
	<% ArrayList selection = new ArrayList();  %>
	<tmpl:put name="activeTab">products</tmpl:put>
	<tmpl:put name="search-header">
		<% int productCount = search.getProducts().size(); %>
	</tmpl:put>

	<tmpl:put name="toolbar">
		<div id="sorter" class="span-18">
			<span class="label">Sort:</span>
			<display:SortBar defaultSort="name" sortItems="<%= new SearchSortType[] {SearchSortType.BY_NAME,SearchSortType.BY_PRICE, SearchSortType.BY_PRIORITY, SearchSortType.BY_START_DATE,SearchSortType.BY_EXPIRATION_DATE,SearchSortType.BY_PERC_DISCOUNT,SearchSortType.BY_DOLLAR_DISCOUNT} %>">
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
		<%-- removed content --%>
	</tmpl:put>	
	<%
	// RECOMMENDER for "view 20"
	if ( nav.getPageSize() != 0) { %>
	<tmpl:put name="recommendations" direct="true">
		<%-- removed content --%>
	</tmpl:put>	
	<% } %>
	
	<tmpl:put name="content" direct="true">
		<%
		pageContext.setAttribute("ISONSEARCHPAGE",null);
		pageContext.setAttribute("ATCCONTEXT",EnumATCContext.ECOUPON.getName());
		for (ListIterator<FilteringSortingItem <ProductModel>> it=products.listIterator() ; it.hasNext();) {
			{
			ProductImpression pi = confStrat.configure(it.next().getModel(), confContext);
			%><div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div><%
			}
					
		} 
		%>
		
	</tmpl:put>
	
	<tmpl:put name="dealSpots" direct="true">
   		<%
   			boolean contGro = false;
   			boolean contFro = false;
   			boolean contDai = false;

   			for (ListIterator<FilteringSortingItem <ProductModel>> it=search.getProducts().listIterator() ; it.hasNext();) {

   				ProductModel pm = it.next().getModel();
   				if ("GRO".equalsIgnoreCase(pm.getDepartment().toString())) {
   					contGro = true;
   				}
   				if ("FRO".equalsIgnoreCase(pm.getDepartment().toString())) {
   					contFro = true;
   				}
   				if ("DAI".equalsIgnoreCase(pm.getDepartment().toString())) {
   					contDai = true;
   				}
   			}
   			
   			if (contGro || contFro || contDai) {
   				%><div id="fdCouponDealSpots"><%
   					if (contGro) {
   						%><a href="/ecoupon.jsp?sort=prio&searchParams=&view=grid&genericFilter=dept=gro&deptId=gro&refinement=1" style="<%= (contFro || contDai) ? "margin-right: 10px;" : "" %>"><img src="/media/images/ecoupon/midQuickFilter_GRO.gif" alt="FDCoupons: Grocery" /></a><%
   					}
   					if (contFro) {
   						%><a href="/ecoupon.jsp?sort=prio&searchParams=&view=grid&genericFilter=dept=fro&deptId=fro&refinement=1" style="<%= (contGro) ? "margin-left: 10px;" : "" %><%= (contDai) ? "margin-right: 10px;" : "" %>"><img src="/media/images/ecoupon/midQuickFilter_FRO.gif" alt="FDCoupons: Frozen" /></a><%
   					}
   					if (contDai) {
   						%><a href="/ecoupon.jsp?sort=prio&searchParams=&view=grid&genericFilter=dept=dai&deptId=dai&refinement=1" style="<%= (contGro || contFro) ? "margin-left: 10px;" : "" %>"><img src="/media/images/ecoupon/midQuickFilter_DAI.gif" alt="FDCoupons: Dairy" /></a><%
   					}
   				%></div><%
   			}
   		%>
	</tmpl:put>
	
	<tmpl:put name='bestOffers-carousel'>
  		<%
  			String carPrefix = "fdCoupon";
   			int carNumItems = 5;
   			int carMinItems = 5;
   			int carMaxItems = 0;
   			String carTrackCode = "fdCouponTrk";
   			int carWidth = 814;
   			boolean carUseQuickBuy = false;
   			boolean carIsTransactional = true;
   			boolean carDisableLinks = false;

			String carTableId = carPrefix+"_table";
			String carTdId = carPrefix+"_td";
			String carId = carPrefix+"_car";
			String carViewAllId = carPrefix+"_viewAll";
			ArrayList<ProductModel> carPms = new ArrayList();
			ArrayList<ProductImpression> carPis = new ArrayList();
			ArrayList<FDCustomerCoupon> carCoupons = new ArrayList();
			ProductImpression coupPi = null;
			boolean containsCoupons = false;
			List sortedProducts = search.getProducts();
			Collections.sort(sortedProducts, FDCustomerCouponUtil.COUPON_POPULARITY_COMPARATOR);
			for (ListIterator<FilteringSortingItem <ProductModel>> it=sortedProducts.listIterator() ; it.hasNext();) {
				FDCustomerCoupon curCoupon = null;
				ProductModel pm = it.next().getModel();
				carPms.add(pm);
				coupPi = confStrat.configure(pm, confContext);
				carPis.add(coupPi);
				if (coupPi.getSku() != null && coupPi.getSku().getProductInfo() != null) {
					curCoupon = user.getCustomerCoupon(coupPi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,coupPi.getProductModel().getParentId(),coupPi.getProductModel().getContentName());
					carCoupons.add(curCoupon);
				}
				if (curCoupon != null) {
					/* no coupon logo on landing page */
					//containsCoupons = true;
				}
			}
			int coup_seq = 0;
			String carParentId = "fdCouponCarouselContainer";
			request.setAttribute("inSmallCar", "true");
			request.setAttribute("carNumItems", carNumItems);
  		%>
  		<center>
		<script type="text/javascript">
			var cmPageCarousel = cmPageCarousel || {};
			<% String handlerObj = "cmPageCarousel[\"" + carId + "\"]"; %>
			<%=handlerObj%> = {"afterScroll": <fd:CmElement wrapIntoFunction="true" carouselId='<%=carPrefix%>' elementCategory="carousel"/> };
		</script>
		<div class="search-recommender fdCouponCar">
			<div class="fdCouponCar-title"><img src="/media/images/ecoupon/best_offers.gif" alt="Best Offers" /></div>
			<display:Carousel id="fdCouponCarousel" carouselId="<%=carId%>" width="<%=carWidth%>" numItems="<%=carNumItems%>" showCategories="false" itemsToShow="<%= carPms %>" 
					trackingCode="<%=carTrackCode%>" maxItems="<%=carMaxItems%>" parentId="<%= carParentId %>" eventHandlersObj='<%=handlerObj%>'>
				<span class="smartstore-carousel-item">
					<display:GetContentNodeWebId id="webId" product="<%= currentItem %>" clientSafe="<%= true %>">
						<% 
						//ProductModel curPm = (ProductModel)currentItem;
						ProductImpression pi = carPis.get(coup_seq);
						request.setAttribute("i_product_box_counter", coup_seq);
						%>
						<div class="grid-item-container"><%@ include file="/includes/product/i_product_box.jspf" %></div>
					</display:GetContentNodeWebId>
				</span>
				<%
					coup_seq++;
				%>
			</display:Carousel>
		</div>
		</center>
	</tmpl:put>

  <tmpl:put name='filterNavigator'>
    <% request.setAttribute("filtermenus", menus); %>
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

<tmpl:put name="productTabLink"><% nav.resetState(); nav.removeAllFilters(); nav.setPageOffset(0); nav.setRecipes(false); %><%= nav.getLink() %></tmpl:put>
<% nav.resetState();nav.setPageSize(0); %>
<% nav.resetState(); %>


</tmpl:insert>

