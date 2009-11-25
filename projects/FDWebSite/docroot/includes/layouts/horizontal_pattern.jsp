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
<% 	int maxWidth = isDepartment.booleanValue() ? 550 : 380; %>
<display:HorizontalPattern 
	id="horizontalPattern" 
	itemsToShow="<%= sortedCollection %>" 
	productCellWidth="<%= isDepartment.booleanValue() ? 137 : 100 %>" 
	folderCellWidth="137" 
	currentFolder="<%= currentFolder %>" 
	useLayoutPattern="<%= !useAlternateImages.booleanValue() %>"
	dynamicSize="<%= useAlternateImages.booleanValue() %>" 
	maxColumns="4"
	showCategories="true"
	useAlternateImage="<%= useAlternateImages.booleanValue() %>"
	tableWidth="<%= maxWidth %>"
	>		
	<table cellspacing="0" cellpadding="0" width="<%=tableWidth%>">
		<tr align="center" valign="bottom">			
			<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
			
				<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
					
					ProductModel product = (ProductModel)currentItem;
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					%>
				
					<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 5px;">
						<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="<%= useAlternateImages.booleanValue() %>"/>
					</td> 
					
				<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
					
					CategoryModel category = (CategoryModel) currentItem;
					String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
					%>
					
					<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 5px;">					
						<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>
					</td> 
					
				<% } %>
			
			</display:PatternRow>			
		</tr>
		
		<tr align="center" valign="top">			
			<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
			
				<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
					
					ProductModel product = (ProductModel)currentItem;
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					%>
				
					<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="catPageProdNameUnderImg">
						
						<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductPrice impression="<%= new ProductImpression(product) %>" showDescription="false"/>
						
					</font></td> 
					
				<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
					
					CategoryModel category = (CategoryModel) currentItem;
					String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
					%>
					
					<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="text11">
					
						<display:CategoryName category="<%= category %>" action="<%= actionUrl %>"/>		
						
					</font></td> 
					
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
