<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% FDUserI horiz_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER); %>

<% //expanded page dimensions
final int W_HORIZ_PATTERN_DEPARTMENT_MAX = 765;
final int W_HORIZ_PATTERN_NOT_DEPARTMENT_MAX = 601; 
%>
<display:InitLayout/>
<% 	int maxWidth = isDepartment.booleanValue() ? W_HORIZ_PATTERN_DEPARTMENT_MAX : W_HORIZ_PATTERN_NOT_DEPARTMENT_MAX; %>
<display:HorizontalPattern 
	id="horizontalPattern" 
	itemsToShow="<%= sortedCollection %>" 
	productCellWidth="<%= isDepartment.booleanValue() ? 150 : 120 %>" 
	folderCellWidth="137" 
	currentFolder="<%= currentFolder %>" 
	useLayoutPattern="<%= !useAlternateImages.booleanValue() %>"
	dynamicSize="<%= useAlternateImages.booleanValue() %>" 
	maxColumns="5"
	showCategories="true"
	useAlternateImage="<%= useAlternateImages.booleanValue() %>"
	tableWidth="<%= maxWidth %>"
	>	
	<table align="center" cellspacing="0" cellpadding="0" width="100%">
		<tr align="center" valign="bottom">			
			<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
			
				<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====
					
					ProductModel product = (ProductModel)currentItem;
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					ProductImpression pi = new ProductImpression(product);
					FDCustomerCoupon curCoupon = null;
					if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
						curCoupon = horiz_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
					}
					%>
				
					<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 5px;">
						<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="<%= useAlternateImages.booleanValue() %>" coupon="<%= curCoupon %>" />
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
					ProductImpression pi = new ProductImpression(product);
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					FDCustomerCoupon curCoupon = null;

					if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
						curCoupon = horiz_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
					}
					%>
				
					<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="catPageProdNameUnderImg">
						
						<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductPrice impression="<%= pi %>" showDescription="false"/>
						<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layHorz"></display:FDCoupon>
						
					</font></td> 
					
				<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY ===== 
					
					CategoryModel category = (CategoryModel) currentItem;
					String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode ); 
					%>
					
					<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="text11">
					
						<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-weight: normal" />		
						
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
