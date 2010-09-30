<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.freshdirect.fdstore.content.util.FourMinuteMealsHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='logic' prefix='logic' %>


<%@page import="com.freshdirect.fdstore.content.util.SortStrategyElement"%><fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<display:InitLayout/>

<%
 	CategoryModel restaurant = currentFolder instanceof CategoryModel ? (CategoryModel) currentFolder : null;
 	if (restaurant == null) {
 		throw new JspException("currentFolder should be a category!");
 	}
 	
 	List<CategoryModel> subcategories = restaurant.getSubcategories();
 	CategoryModel meals = null;
 	CategoryModel sides = null;
 	CategoryModel entrees = null;
 	if (subcategories.size() > 0) {
 		meals = subcategories.get(0);
 		if (!meals.getShowSelf())
 			meals = null;
 	} else {
 		meals = restaurant;
 	}
 	if (subcategories.size() > 1) {
 		sides = subcategories.get(1);
 		if (!sides.getShowSelf())
 			sides = null;
 	}
 	if (subcategories.size() > 2) {
 		entrees = subcategories.get(2);
 		if (!entrees.getShowSelf())
 			entrees = null;
 	}

	// the catDetail is the big picture
	Image catDetail = restaurant.getCategoryDetailImage();
	int leftPadding=0;
	int topPadding=0;
	int blurbWidth=0;
	int sidebarWidth=130;
	String detailPath="";
	// the blutb is the title of the page
	List<Html> blurbList=restaurant.getTopMedia();
	Html blurb = null;
	// the editorial is the html at the right, under the big picture
	Html editorial = restaurant.getEditorial();
	List<Html> topMediaList;
	List<Html> bottomMediaList=restaurant.getBottomMedia();

	
	if(blurbList!=null && blurbList.size()>0) {
		blurb=blurbList.get(0);
	}
	if(editorial!=null) {
		blurbWidth=sidebarWidth;
	}
	if(catDetail!=null) {
		detailPath=catDetail.getPath();
		topPadding=catDetail.getHeight();
		leftPadding=catDetail.getWidth()-sidebarWidth;
	}
	
	List<ContentNodeModel> products = null;
	List<SortStrategyElement> sortStrategy = new ArrayList<SortStrategyElement>();
	sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));
	sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, SortStrategyElement.SORTNAME_FULL, false));	
	
%>

<div class="fourmm restaurantpage">
	<div class="meals" style="background:url('<%= detailPath %>') left top no-repeat;margin-left:-<%= leftPadding %>px;padding-left:<%= leftPadding %>px">

		<% if (meals != null) { %>
		<display:ItemGrabber id="prods" category="<%= meals %>" depth="0" filterDiscontinued="true" returnSkus="false"
				ignoreDuplicateProducts="true" ignoreShowChildren="false" returnHiddenFolders="false" returnSecondaryFolders="false">
		<% products = prods; %>
		</display:ItemGrabber>
		<display:ItemSorter nodes="<%= products %>" strategy="<%= sortStrategy %>"/>		
		<div style="">
			<% if(editorial!=null){ %>
				<div class="meals-sidebar" style="width:<%= blurbWidth %>px;padding-top:<%= topPadding %>px">
					<div class="meals-sidebartext"><fd:IncludeMedia name="<%= editorial.getPath() %>" /></div>
				</div>
			<% }  %>
			<% if(blurb!=null || FourMinuteMealsHelper.isSidesInASnapCategory( currentFolder ) ){ %>
				<div class="meals-title" style="position:relative;left:-<%= blurbWidth/2 %>px">
					<%	if(blurb!=null) { %><fd:IncludeMedia name="<%= blurb.getPath() %>" /><% } %>
					<% // show first section top media only for Sides In A Snap   
					if ( FourMinuteMealsHelper.isSidesInASnapCategory( currentFolder ) ) {	
						topMediaList = meals.getTopMedia();
						if ( topMediaList != null && !topMediaList.isEmpty() ) { %>
							<fd:IncludeMedia name="<%= topMediaList.get(0).getPath() %>"></fd:IncludeMedia>
						<% topMediaList=null;
						}
					} %>
				</div>
			<% }  %>
		</div>
		
		<display:ContentNodeIterator itemsToShow="<%= products %>" id="meals1" showProducts="true" showCategories="false" trackingCode="<%= trackingCode %>"><span class="meal">
			<display:ProductImage product="<%= (ProductModel) currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage" enableQuickBuy="true" customer="<%= user %>"/>
			<display:ProductRating product="<%= (ProductModel) currentItem %>" action="<%= actionUrl %>"/>
			<display:ProductName product="<%= (ProductModel) currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
			<display:ProductPrice impression="<%= new ProductImpression((ProductModel) currentItem) %>" showDescription="false"/>
		</span></display:ContentNodeIterator>
		
		<% } %>
	</div>

	<% if ( sides != null ) { %>
		<display:ItemGrabber id="prods" category="<%= sides %>" depth="0" filterDiscontinued="true" returnSkus="false"
				ignoreDuplicateProducts="true" ignoreShowChildren="false" returnHiddenFolders="false" returnSecondaryFolders="false">
		<% products = prods; %>
		</display:ItemGrabber>
		<display:ItemSorter nodes="<%= products %>" strategy="<%= sortStrategy %>"/>
		<% if (!products.isEmpty()) { %>		
		<div class="meals">
			<% 	topMediaList=sides.getTopMedia();
			if(topMediaList!=null && !topMediaList.isEmpty()) { %>
				<fd:IncludeMedia name="<%= topMediaList.get(0).getPath() %>"></fd:IncludeMedia>
				<% topMediaList=null;
			} %>
			<display:ContentNodeIterator trackingCode="<%= trackingCode %>" itemsToShow="<%= products %>" id="sides1" showCategories="false"><span class="meal">
				<display:ProductImage product="<%= (ProductModel)currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage"  enableQuickBuy="true" customer="<%= user %>"/>
				<display:ProductRating product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
				<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>" showDescription="false"/>
			</span></display:ContentNodeIterator>
		</div>
		<% } %>
	<% } %>
	<% if ( entrees != null ) { %>
		<display:ItemGrabber id="prods" category="<%= entrees %>" depth="0" filterDiscontinued="true" returnSkus="false"
				ignoreDuplicateProducts="true" ignoreShowChildren="false" returnHiddenFolders="false" returnSecondaryFolders="false">
		<% products = prods; %>
		</display:ItemGrabber>
		<display:ItemSorter nodes="<%= products %>" strategy="<%= sortStrategy %>"/>
		<% if (!products.isEmpty()) { %>		
		<div class="meals">
			<% 	topMediaList=entrees.getTopMedia();
			if(topMediaList!=null && !topMediaList.isEmpty()) { %>
				<fd:IncludeMedia name="<%= topMediaList.get(0).getPath() %>"></fd:IncludeMedia>
				<% topMediaList=null;
			} %>
			<display:ContentNodeIterator trackingCode="<%= trackingCode %>" itemsToShow="<%= products %>" id="entrees1" showCategories="false"><span class="meal">
				<display:ProductImage product="<%= (ProductModel)currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage" enableQuickBuy="true" customer="<%= user %>"/>
				<display:ProductRating product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
				<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>" showDescription="false"/>
			</span></display:ContentNodeIterator>
		</div>
		<% } %>
	<% } %>

<%	if(bottomMediaList!=null) {%>
				<logic:iterate id="bottomHtml" collection="<%= bottomMediaList %>">
				<fd:IncludeMedia name="<%= ((Html) bottomHtml).getPath() %>"></fd:IncludeMedia>
				</logic:iterate>				
<% } %>
	<img src="/media_stat/images/4mm/brandpage_header_morebrands.gif"  alt="More Brands and Cuisines"/>
	
	<% 
		List<CategoryModel> restaurants = new ArrayList<CategoryModel>(FourMinuteMealsHelper.getRestaurants());
		restaurants.remove( restaurant ); 
	%>
	<%@ include file="/includes/layouts/4mm/restaurants.jspf"%>
	
	<a href="<%= FourMinuteMealsHelper.allPageBaseUrl %>" style="text-decoration:none"><img src="/media_stat/images/4mm/dpt_4mm_viewall.gif"  alt="View all 4-Minute Meals" border="0" /></a>	
	<%@ include file="/includes/layouts/4mm/see_all_button.jspf"%>
	
</div>
