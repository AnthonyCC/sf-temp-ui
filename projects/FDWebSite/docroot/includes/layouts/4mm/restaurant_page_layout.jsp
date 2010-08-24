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

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<display:InitLayout/>

<%
 	CategoryModel restaurant = currentFolder instanceof CategoryModel ? (CategoryModel) currentFolder : null;
 	if (restaurant == null) {
 		throw new Exception("currentFolder should be a category!");
 	}

	List<ContentNodeModel> products[]  = new ArrayList[3];
	CategoryModel subCategories[] = new CategoryModel[3];	
	int sections = FourMinuteMealsHelper.separateCategoryListBySubcategories( sortedCollection, (CategoryModel)currentFolder, products, subCategories );
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
%>

<div class="fourmm restaurantpage">
	<div class="meals" style="background:url('<%= detailPath %>') left top no-repeat;margin-left:-<%= leftPadding %>px;padding-left:<%= leftPadding %>px">
		
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
						topMediaList = subCategories[0].getTopMedia();
						if ( topMediaList != null ) { %>
							<logic:iterate id="topHtml" collection="<%= topMediaList %>">
							<fd:IncludeMedia name="<%= ((Html) topHtml).getPath() %>"></fd:IncludeMedia>
							</logic:iterate>				
							<% topMediaList=null;
						}
					} %>
				</div>
			<% }  %>
		</div>
		
		<display:ContentNodeIterator itemsToShow="<%=products[0] %>" id="meals" showProducts="true" showCategories="false" trackingCode="<%= trackingCode %>"><span class="meal">
			<display:ProductImage product="<%= (ProductModel) currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage" enableQuickBuy="true" customer="<%= user %>"/>
			<display:ProductRating product="<%= (ProductModel) currentItem %>" action="<%= actionUrl %>"/>
			<display:ProductName product="<%= (ProductModel) currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
			<display:ProductPrice impression="<%= new ProductImpression((ProductModel) currentItem) %>" showDescription="false"/>
		</span></display:ContentNodeIterator>
	</div>

	<% if ( sections > 1 ) { %>
		<div class="meals">
			<% 	topMediaList=subCategories[1].getTopMedia();
			if(topMediaList!=null) { %>
				<logic:iterate id="topHtml" collection="<%= topMediaList %>">
				<fd:IncludeMedia name="<%= ((Html) topHtml).getPath() %>"></fd:IncludeMedia>
				</logic:iterate>				
				<% topMediaList=null;
			} %>
			<display:ContentNodeIterator trackingCode="<%= trackingCode %>" itemsToShow="<%= products[1] %>" id="sides" showCategories="false"><span class="meal">
				<display:ProductImage product="<%= (ProductModel)currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage"  enableQuickBuy="true" customer="<%= user %>"/>
				<display:ProductRating product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
				<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>" showDescription="false"/>
			</span></display:ContentNodeIterator>
		</div>
	<% } %>
	<% if ( sections > 2 ) { %>
		<div class="meals">
			<% 	topMediaList=subCategories[2].getTopMedia();
			if(topMediaList!=null) { %>
				<logic:iterate id="topHtml" collection="<%= topMediaList %>">
				<fd:IncludeMedia name="<%= ((Html) topHtml).getPath() %>"></fd:IncludeMedia>
				</logic:iterate>				
				<% topMediaList=null;
			} %>
			<display:ContentNodeIterator trackingCode="<%= trackingCode %>" itemsToShow="<%= products[2] %>" id="entrees" showCategories="false"><span class="meal">
				<display:ProductImage product="<%= (ProductModel)currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage" enableQuickBuy="true" customer="<%= user %>"/>
				<display:ProductRating product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>"/>
				<display:ProductName product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>" showBrandName="false"/>
				<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>" showDescription="false"/>
			</span></display:ContentNodeIterator>
		</div>
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
