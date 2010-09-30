<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.freshdirect.fdstore.content.MediaI"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.fdstore.content.ProducerModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.Html"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
<%@page import="com.freshdirect.fdstore.content.EnumPopupType"%>
<%@page import="com.freshdirect.fdstore.content.TitledMedia"%>
<%@page import="com.freshdirect.fdstore.content.Image"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.content.nutrition.ErpNutritionInfoType"%>
<%@page import="com.freshdirect.content.nutrition.EnumClaimValue"%>
<%@page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@page import="com.freshdirect.fdstore.FDProduct"%>
<%@page import="com.freshdirect.fdstore.content.SkuModel"%>
<%@page import="com.freshdirect.fdstore.content.Domain"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.webapp.util.ProductImpression"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" noRedirect="true" />

<tmpl:insert template='/common/template/right_dnav.jsp'>
	<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
	<tmpl:put name='title' direct='true'>FreshDirect - 4-Minute Meals</tmpl:put>
	<%-- SCRIPTS IN HEADER --%>
	<tmpl:put name='head_content'>
	</tmpl:put>
	<%-- CONTENT --%>
	<tmpl:put name='content' direct='true'>
<%
	DepartmentModel dept = FourMinuteMealsHelper.get4mmDepartment(); // 4mm department node
%>
	<div class="fourmm filterpage">
		<%@ include file="/includes/layouts/4mm/title.jspf"%>
		<br/>
		
		<%-- FILTER BOX --%>		
		<% boolean fourColumnLayout = true; boolean needToFilter = true; boolean enableAllItem=false; %>
		<%@ include file="/includes/layouts/4mm/filter_widget.jspf"%>
		
		<%-- BREADCRUMB --%>		
		<% if ( selectedRestaurants.size() > 0 || selectedNutritions.size() > 0 || selectedIngredients.size() > 0 || selectedPrices.size() > 0 ) { %>
		<div class="fourmm-breadcrumb">
		
			<logic:iterate id="breadCrumbItem" indexId="breadCrumbItemIndex" collection="<%= selectedRestaurants %>" type="java.lang.String">
			<% if (breadCrumbItemIndex != 0) { %><span class="fourmm-breadcrumb-with"> with </span><% } %><span class="fourmm-breadcrumb-item"><%= FourMinuteMealsHelper.getFilterInfos().get(breadCrumbItem).getLabel() %></span>
			</logic:iterate>
			
			<% if ((selectedRestaurants.size() > 0 ) && selectedIngredients.size() > 0) { %><span class="fourmm-breadcrumb-bullet">&bull;</span><% } %>
			<logic:iterate id="breadCrumbItem" indexId="breadCrumbItemIndex" collection="<%= selectedIngredients %>" type="java.lang.String">
			<% if (breadCrumbItemIndex != 0) { %><span class="fourmm-breadcrumb-with"> with </span><% } %><span class="fourmm-breadcrumb-item"><%= FourMinuteMealsHelper.getFilterInfos().get(breadCrumbItem).getLabel() %></span>
			</logic:iterate>
			
			<% if ((selectedRestaurants.size() > 0 || selectedIngredients.size() > 0) && selectedPrices.size() > 0) { %><span class="fourmm-breadcrumb-bullet">&bull;</span><% } %>
			<logic:iterate id="breadCrumbItem" indexId="breadCrumbItemIndex" collection="<%= selectedPrices %>" type="java.lang.String">
			<% if (breadCrumbItemIndex != 0) { %><span class="fourmm-breadcrumb-with"> with </span><% } %><span class="fourmm-breadcrumb-item"><%= FourMinuteMealsHelper.getFilterInfos().get(breadCrumbItem).getLabel() %></span>
			</logic:iterate>
			
			<% if ((selectedRestaurants.size() > 0 || selectedIngredients.size() > 0 || selectedPrices.size() > 0) && selectedNutritions.size() > 0) { %><span class="fourmm-breadcrumb-bullet">&bull;</span><% } %>
			<logic:iterate id="breadCrumbItem" indexId="breadCrumbItemIndex" collection="<%= selectedNutritions %>" type="java.lang.String">
			<% if (breadCrumbItemIndex != 0) { %><span class="fourmm-breadcrumb-with"> with </span><% } %><span class="fourmm-breadcrumb-item"><%= FourMinuteMealsHelper.getFilterInfos().get(breadCrumbItem).getLabel() %></span>
			</logic:iterate>
			
		</div>
		<% } %>
<div class="sortselector">
		<a href="<%= FourMinuteMealsHelper.allPageBaseUrl %>" style="float:left"><img src="/media/4mm/filter_clearall.gif" border="0" /></a>
		<%-- SORT SELECTOR --%>	
		<%
			String selectedSortInfo = (pageContext.getRequest().getParameter(FourMinuteMealsHelper.sortParam)!=null) ? 
					pageContext.getRequest().getParameter(FourMinuteMealsHelper.sortParam) : 
					FourMinuteMealsHelper.sortInfos[0].getId() ;
		%>
		<script>
			function sortInfoChange(s) {
				var q=window.location.search.substr(1),
					currentParams=q.split('&'),
					newParams=[],
					selectedValue=s.options[s.selectedIndex].value,
					i,l=currentParams.length;
		
				for(i=0;i<l;i++) {
					if(currentParams[i] && currentParams[i].indexOf("<%=FourMinuteMealsHelper.sortParam%>")==-1) {
						newParams.push(currentParams[i]);
					}
				};
				newParams.push("<%=FourMinuteMealsHelper.sortParam%>=" + selectedValue);
				location.replace(location.pathname+"?"+newParams.join("&"));
			};
		</script>
		<label>Sort By</label>
		<select onchange="sortInfoChange(this)">
			<logic:iterate id="sortinfo" collection="<%= FourMinuteMealsHelper.sortInfos %>" type="com.freshdirect.fdstore.content.util.FourMinuteMealsHelper.SortInfo">
				<option value="<%= sortinfo.getId() %>" <%= (sortinfo.getId().equals(selectedSortInfo)) ? "selected" : "" %>><%= sortinfo.getLabel() %></option>
			</logic:iterate>		
		</select>
</div>
		<%-- RESULT DISPLAY --%>		
		<% 
			boolean showBrand = true; //!FourMinuteMealsHelper.sortBrand.equals(selectedSortInfo) && selectedRestaurants.size() == 0;
		
			int sectionCounter = 0;
			int sectionNumber = result.getSectionCount();
			while ( sectionCounter < sectionNumber ) {
				List<? extends ProductModel> sectionList = result.getMultiList().get( sectionCounter );
				MediaI sectionMedia = result.getMediaList().get( sectionCounter );
				sectionCounter++;
				if ( sectionList != null && !sectionList.isEmpty() ) {
					%>
					<div class="separator">
						<fd:IncludeMedia media="<%= sectionMedia %>" name="HEADER"/>
					</div>
					<div class="meals" style="text-align:left">
						<display:ContentNodeIterator trackingCode="4mm-filter" itemsToShow="<%= sectionList %>" id="productIterator" showCategories="false"><span class="meal">
							<display:ProductImage product="<%= (ProductModel)currentItem %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="true" className="productimage" enableQuickBuy="true" customer="<%= user %>"/>
							<display:ProductRating product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>"/>
							<display:ProductName product="<%= (ProductModel)currentItem %>" action="<%= actionUrl %>" showBrandName="<%= showBrand %>"/>
							<display:ProductPrice impression="<%= new ProductImpression((ProductModel)currentItem) %>" showDescription="false"/>
						</span></display:ContentNodeIterator>
					</div><% 
				}				
			} %>

	</div>
	</tmpl:put>
</tmpl:insert>
	