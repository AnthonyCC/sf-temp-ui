<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.util.FilteringNavigator"%>
<%@ page import="com.freshdirect.fdstore.content.*"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%
String activeTabVal = (String)request.getAttribute("activeTabVal");
if ( activeTabVal == null ) {
	activeTabVal = "products";
}

Boolean isNewProdPage = (Boolean)request.getAttribute("newprodpage");
if ( isNewProdPage == null ) {
	isNewProdPage = Boolean.FALSE;
}

FilteringNavigator nav = (FilteringNavigator)request.getAttribute("filternav");
Map<FilteringValue, Map<String, FilteringMenuItem>> menus = (Map<FilteringValue, Map<String, FilteringMenuItem>>)request.getAttribute("filtermenus");

final int hideAfter = 8;
boolean otherFilters = false;

if (menus != null && nav != null) {
%>
<tmpl:insert template="/common/template/filter_navigator_template.jsp">

<% if ( "products".equals( activeTabVal ) ) { // ====================================================== PRODUCTS ============================================================ %>
<tmpl:put name='deparmentFilter'>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.DEPT%>" filters='<%=menus%>' id="filterItems" hideAfter="<%=hideAfter%>">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
<div id="department" class="filterbox sidebar-content">
  <h3>DEPARTMENT</h3>
  <ul>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setDeptFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
  </logic:iterate>
  </ul>
  <%
  	if(filterItems.size() > hideAfter) {
  %>
    <div class="see more">+ See more</div>
    <div class="see less">- See less</div>
  <%
  	}
  %>
</div>
</fd:FilterList>
</tmpl:put>

<tmpl:put name='categoryFilter'>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.CAT%>" filters="<%=menus%>" id="filterItems" hideAfter="<%=hideAfter%>">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
<div id="category" class="filterbox sidebar-content">
  <h3>Category</h3>
  <ul>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setCatFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
  </logic:iterate>
  </ul>
  <%
  	if(filterItems.size() > hideAfter) {
  %>
    <div class="see more">+ See more</div>
    <div class="see less">- See less</div>
  <%
  	}
  %>
</div>
</fd:FilterList>
</tmpl:put>
  
<tmpl:put name='subCategoryFilter'>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.SUBCAT%>" filters='<%=menus%>' id="filterItems" hideAfter="<%=hideAfter%>">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
<div id="subcategory" class="filterbox sidebar-content">
  <h3>Subcategory</h3>
  <ul>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setSubCatFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
  </logic:iterate>
  </ul>
<%
	if(filterItems.size() > hideAfter) {
%>
  <div class="see more">+ See more</div>
  <div class="see less">- See less</div>
<%
	}
%>
</div>
</fd:FilterList>
</tmpl:put>
  
<tmpl:put name='brandFilter'>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.BRAND%>" filters='<%=menus%>' id="filterItems" hideAfter="<%=hideAfter%>">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
<div id="brands" class="filterbox sidebar-content">
  <h3>BRAND</h3>
  <ul>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setBrandFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
  </logic:iterate>
  </ul>
<%
	if(filterItems.size() > hideAfter) {
%>
  <div class="see more">+ See more</div>
  <div class="see less">- See less</div>
<%
	}
%>
</div>
</fd:FilterList>
</tmpl:put>
  
<tmpl:put name='expertRatingFilter'>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.EXPERT_RATING%>" filters='<%=menus%>' id="filterItems">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
  <div id="expertrating" class="filterbox sidebar-content">
    <h3>EXPERT RATING</h3>
    <ul>
		<logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
			<%
				String name = menu.getName(); int idx = Integer.parseInt( name ); int count = menu.getCounter(); String countStr = Integer.toString( count );
				if( nav.isSetExpRatingFilter(name) ) {
			%>
				<%
					nav.removeExpRatingFilter(name);
				%>
				<li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><span class="expertrating-stars"><span class="expertrating-stars-content expertrating-stars-content-<%=idx%>"></span></span></a></li>
				<%
					nav.addExpRatingFilter(name);
				%>
			<%
				} else if ( menu.getCounter() == 0 ) {
			%>
				<li><span class="expertrating-stars"><span class="expertrating-stars-content expertrating-stars-content-<%=idx%>"></span></span><span class="count">(<%=countStr%>)</span></li>
			<%
				} else {
			%>
				<%
					nav.addExpRatingFilter(name);
				%>
				<li><a href="<%=nav.getLink()%>"><span class="expertrating-stars"><span class="expertrating-stars-content expertrating-stars-content-<%=idx%>"></span></span></a><span class="count">(<%=countStr%>)</span></li>
				<%
					nav.removeExpRatingFilter(name);
				%>
			<%
				}
			%>
		</logic:iterate>				
    </ul>
  </div>
</fd:FilterList>
</tmpl:put>

<tmpl:put name='customerRatingFilter'>
	<% if (FDStoreProperties.isBazaarvoiceEnabled()) { %>
		<fd:FilterList domainName="<%=EnumSearchFilteringValue.CUSTOMER_RATING%>" filters='<%=menus%>' id="filterItems">
		<%
			nav.resetState(); nav.setPageOffset(0);
		%>
		  <div id="customerrating" class="filterbox sidebar-content">
		    <h3>CUSTOMER RATING</h3>
		    <ul>
				<logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
					<%
						String name = menu.getName(); int idx = Integer.parseInt( name ); int count = menu.getCounter(); String countStr = Integer.toString( count );
						if( nav.isSetCustRatingFilter(name) ) {
					%>
						<%
							nav.removeCustRatingFilter(name);
						%>
						<li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><span class="bv-customerrating-stars"><span class="bv-customerrating-stars-content bv-customerrating-stars-content-<%=idx%>"></span></span></a></li>
						<%
							nav.addCustRatingFilter(name);
						%>
					<%
						} else if ( menu.getCounter() == 0 ) {
					%>
						<li><span class="bv-customerrating-stars"><span class="bv-customerrating-stars-content bv-customerrating-stars-content-<%=idx%>"></span></span><span class="count">(<%=countStr%>)</span></li>
					<%
						} else {
					%>
						<%
							nav.addCustRatingFilter(name);
						%>
						<li><a href="<%=nav.getLink()%>"><span class="bv-customerrating-stars"><span class="bv-customerrating-stars-content bv-customerrating-stars-content-<%=idx%>"></span></span></a><span class="count">(<%=countStr%>)</span></li>
						<%
							nav.removeCustRatingFilter(name);
						%>
					<%
						}
					%>
				</logic:iterate>				
		    </ul>
		  </div>
		</fd:FilterList>
	<% } %>
</tmpl:put>

<tmpl:put name="otherKosherFilter">
<fd:FilterList domainName="<%=EnumSearchFilteringValue.KOSHER%>" filters='<%=menus%>' id="filterItems">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setKosherFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
    <%
    	otherFilters=true;
    %>
  </logic:iterate>
</fd:FilterList>
</tmpl:put>

<tmpl:put name="otherNewFilter">
<fd:FilterList domainName="<%=EnumSearchFilteringValue.NEW_OR_BACK%>" filters='<%=menus%>' id="filterItems">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setNewOrBackFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
    <%
    	otherFilters=true;
    %>
  </logic:iterate>
</fd:FilterList>
</tmpl:put>

<tmpl:put name="otherGlutenFreeFilter">
<fd:FilterList domainName="<%=EnumSearchFilteringValue.GLUTEN_FREE%>" filters='<%=menus%>' id="filterItems">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setGlutenFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
    <%
    	otherFilters=true;
    %>
  </logic:iterate>
</fd:FilterList>
</tmpl:put>


<tmpl:put name="otherOnSaleFilter">
<fd:FilterList domainName="<%=EnumSearchFilteringValue.ON_SALE%>" filters='<%=menus%>' id="filterItems">
<%
	nav.resetState(); nav.setPageOffset(0);
%>
  <logic:iterate id="menu" collection="<%=filterItems%>" type='FilteringMenuItem'>
    <%
    	if(menu.isSelected()) {
    %>
      <%
      	nav.removeFilter(menu.getFilter(), menu.getFilteringUrlValue());
      %>
      <li class="selected"><a href="<%=nav.getLink()%>" class="remove-selection"></a><a href="<%=nav.getLink()%>"><%=menu.getName()%></a></li>
    <%
    	} else {
    %>
      <%
      	nav.setOnSalelFilter(menu.getFilteringUrlValue());
      %>
      <li class="<%=menu.isHidden() ? "hidden" : ""%>"><a href="<%=nav.getLink()%>"><%=menu.getName()%></a><span class="count">(<%=menu.getCounter()%>)</span></li>
    <%
    	}
    %>
    <%
    	otherFilters=true;
    %>
  </logic:iterate>
</fd:FilterList>
</tmpl:put>

<tmpl:put name="otherFilters">
<%
	if(otherFilters == true) {
%>
    <div id="other" class="filterbox sidebar-content">
    <h3>OTHER</h3>
    <ul>    
    
    <%
            	if ( isNewProdPage ) {
            %>
		<tmpl:get name='otherKosherFilter'/>
		<tmpl:get name='otherGlutenFreeFilter'/>
		<tmpl:get name='otherOnSaleFilter'/>
    <%
    	} else {
    %>
		<tmpl:get name='otherKosherFilter'/>
		<tmpl:get name='otherNewFilter'/>
		<tmpl:get name='otherGlutenFreeFilter'/>
		<tmpl:get name='otherOnSaleFilter'/>
    <%
    	}
    %>
    
    </ul>
    </div>
<%
	}
%>
</tmpl:put>

<%
	} else if ( "recipes".equals( activeTabVal ) ) { // ====================================================== RECIPES ============================================================
%>
	<tmpl:put name='recipesFilter'>
		<div id="recipesfilter" class="filterbox sidebar-content">
			<h3>Recipes</h3>
			<ul>
				<%
					nav.resetState();nav.setRecipes(true); nav.removeAllFilters(); nav.setPageOffset(0);
				%>
				<li><a href="<%=nav.getLink()%>">All</a></li>
<fd:FilterList domainName="<%=EnumSearchFilteringValue.RECIPE_CLASSIFICATION%>" filters='<%= menus %>' id="filterItems">
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

<% } %>

</tmpl:insert>

<% } %>
<tmpl:get name="sidebarOther"/>
