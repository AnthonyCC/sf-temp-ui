<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.Attribute' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>


<display:InitLayout/>

<display:HorizontalPattern 
	id="horizontalPattern" 
	itemsToShow="<%= sortedCollection %>" 
	productCellWidth="<%= isDepartment.booleanValue() ? 137 : 100 %>" 
	folderCellWidth="137" 
	currentFolder="<%= currentFolder %>" 
	useLayoutPattern="true" 
	maxColumns="4"
	showCategories="true">		
		
	<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
	<tr align="center" valign="top">	
		
		<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
		
			<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
				
				ProductModel product = (ProductModel)currentItem;
				String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
				%>
			
				<td width="<%= horizontalPattern.getProductCellWidth() %>"><font class="catPageProdNameUnderImg">
					
					<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>"/><br/>
					<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
					<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
					<display:ProductPrice impression="<%= new ProductImpression(product) %>"/>
					
				</font></td> 
				
			<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
				
				CategoryModel category = (CategoryModel) currentItem;
				String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
				%>
				
				<td width="<%= horizontalPattern.getFolderCellWidth() %>"><font class="text11">
				
					<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>
					<display:CategoryName category="<%= category %>" action="<%= actionUrl %>"/>		
					
				</font></td> 
				
			<% } else { %>
				[ ERROR : <%= currentItem.getFullName() %> is a <%= currentItem.getClass().toString() %> ]<br/>				
			<% } %>
		
		</display:PatternRow>			
	</tr>
	</table>
	
	<br/><font class="space4pix"><br/>&nbsp;<br/></font>
	
</display:HorizontalPattern>

<%
	if ( onlyOneProduct.booleanValue() ) {
		request.setAttribute( "theOnlyProduct", theOnlyProduct );
	}

//********** End: of Horizontal Pattern *********************
%>
