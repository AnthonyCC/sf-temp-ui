<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
<%@page import="com.freshdirect.fdstore.content.MediaI"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>

<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<display:InitLayout /> 

<%
 	DepartmentModel dept = currentFolder instanceof DepartmentModel ? (DepartmentModel) currentFolder : null;
 	if (dept == null) {
 		throw new Exception("This layout is only valid for departments!");
 	}
 	List<ProductModel> recommendedProducts=null;
	List<ContentNodeModel> restaurants=sortedCollection;
	List<Html> middleMedia = dept.getDepartmentMiddleMedia();

	String customerId = ( user == null || user.getIdentity() == null ) ? null : user.getIdentity().getErpCustomerPK();
	int counter = FDCustomerManager.decrementCounter( customerId, "4mm-landing-page-views", 5 );
	Integer sessionCounter = (Integer)session.getAttribute( "4mm-landing-page-views" );
	
	if ( sessionCounter == null ) {
		sessionCounter = new Integer( counter );
		session.setAttribute( "4mm-landing-page-views", new Integer(sessionCounter) );
	}
	boolean returning = sessionCounter <=0;
	boolean mostPopularShown = false;
	boolean yourFavoritesShown = false;
%>

<!-- DEBUG INFO - need to remove after QA  
user = <%= user.getUserId() %>
counter = <%= counter %>
sessionCounter = <%= sessionCounter %>
-->

<div class="fourmm landingpage">
	<div style="margin-top:20px;"></div>
	<%@ include file="/includes/layouts/4mm/title.jspf"%>
	
	<% if ( returning ) { %>
		<% if( middleMedia!=null && middleMedia.size()>=2) {  %>
		<fd:IncludeMedia name="<%= middleMedia.get(1).getPath() %>"></fd:IncludeMedia>
		<% } %>
		<%@ include file="/includes/layouts/4mm/your_favorites.jspf"%>
		
		<% if (true || !yourFavoritesShown ) { 
			mostPopularShown = true; %>
			<div class="notopmargin">
			<%@ include file="/includes/layouts/4mm/most_popular.jspf"%>
			</div>
		<% } %> 
		
		<div style="margin-bottom:25px">
		<%@ include file="/includes/layouts/4mm/see_all_button.jspf"%>
		</div>
		<% boolean fourColumnLayout = false; boolean needToFilter = false; boolean enableAllItem=true; %>
		<%@ include file="/includes/layouts/4mm/filter_widget.jspf"%>
		<img src="/media/4mm/dpt_4mm_hdr_brands_cuisines_return.gif" />
		<%@ include file="/includes/layouts/4mm/restaurants.jspf"%>
			
	<% } else { %>
		<% if( middleMedia!=null && middleMedia.size()>=2) {  %>
		<fd:IncludeMedia name="<%= middleMedia.get(0).getPath() %>"></fd:IncludeMedia>
		<% } %>
	
		<div class="separator" style="margin-bottom:20px;"></div>
		
		<%@ include file="/includes/layouts/4mm/restaurants.jspf"%>
		<div style="margin-bottom:25px">
		<%@ include file="/includes/layouts/4mm/see_all_button.jspf"%>	
		</div>
		<% boolean fourColumnLayout = false; boolean needToFilter = false; boolean enableAllItem=true; %>
		<%@ include file="/includes/layouts/4mm/filter_widget.jspf"%>					
	<% } %>
	
	<%@ include file="/includes/layouts/4mm/meal_of_the_week.jspf"%>	
	<div style="clear:both"></div>
	
	<% if ( !mostPopularShown ) { %>
		<%@ include file="/includes/layouts/4mm/most_popular.jspf"%>
	<% } %>
	
	<%@ include file="/includes/layouts/4mm/editorial.jspf"%>
	
	<div style="margin:25px 0px;"><div id="ads">
		<script type="text/javascript">
			OAS_AD('4mmAd1');
		</script>
		<script type="text/javascript">
			OAS_AD('4mmAd2');
		</script>
	</div></div>
	
	
	<%@ include file="/includes/layouts/4mm/view_all.jspf"%>
	<%@ include file="/includes/layouts/4mm/see_all_button.jspf"%>
</div>

