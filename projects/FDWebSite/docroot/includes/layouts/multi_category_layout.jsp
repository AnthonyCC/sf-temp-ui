<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% FDUserI multi_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER); %>


<% //expanded page dimensions
final int W_MULTI_CATEGORY_IS_DEPARTMENT = 765;
final int W_MULTI_CATEGORY_NOT_DEPARTMENT = 601; 
%>
<display:InitLayout/>
<%
	final boolean noLeftNav=request.getAttribute("noLeftNav") == null ? false : (Boolean)request.getAttribute("noLeftNav");
	final int maxWidth = isDepartment.booleanValue() ? W_MULTI_CATEGORY_IS_DEPARTMENT : noLeftNav ? W_MULTI_CATEGORY_IS_DEPARTMENT : W_MULTI_CATEGORY_NOT_DEPARTMENT;
	
	boolean useAlternate = useAlternateImages.booleanValue();
%><fd:MultiCategoryLayout parentKey="<%= currentFolder.getContentKey() %>" sortedCollection="<%= sortedCollection %>"><%
	{
		/*
		 * Render category header (if available)
		 */
		if ( mcl_category != null && !mcl_nodes.isEmpty() ) {
			
			// show separator line except the first category
			if ( ! mcl_first ) {
				%><div style="padding: 0; margin: 2em 0 8px 0; border-top: 1px solid #cccccc"></div>
				<%
			}	    

			// get the category_top attribute to display
			List<Html> catTop = mcl_category.getTopMedia();
			if (catTop != null && catTop.size() > 0) {
				%><div style="padding-bottom: 2em;"><fd:IncludeMedia name="<%= catTop.get(0).getPath()%>" /></div>
				<%
			}
			
			useAlternate = mcl_category.isUseAlternateImages();
		}


		/*
		 * Render nodes
		 */
		{
			%>
				<display:HorizontalPattern 
					id="horizontalPattern" 
					currentFolder="<%= currentFolder %>" 
					folderCellWidth="136" 
					productCellWidth="105" 
					itemsToShow="<%= mcl_nodes %>" 
					useLayoutPattern="<%= mcl_containsProduct && !useAlternate %>"
					dynamicSize="<%= useAlternate || !mcl_containsProduct %>"
					tableWidth="<%= maxWidth %>"
					showCategories="true" 
					showProducts="true"
					maxColumns="5"
					useAlternateImage="<%= useAlternate %>"
				>
				
					<table align="center" cellspacing="0" cellpadding="0" width="100%">
						<tr align="center" valign="bottom">
						<%
						int cellPercentage=100/rowList.size();						
						%>				
							<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
							
								<% if ( currentItem instanceof ProductModel ) {		
									ProductModel product = (ProductModel)currentItem;
									String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
									ProductImpression pi = new ProductImpression(product);
									FDCustomerCoupon curCoupon = null;
									if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
										curCoupon = multi_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
									}
									String productImageClassName = "productImage";
									if (curCoupon != null) {
										productImageClassName += " couponLogo";
									}
									%>
								
									<td width="<%= cellPercentage %>%" style="padding-bottom: 5px;">										
										<display:ProductImage product="<%= product %>" showRolloverImage="true" action="<%= actionUrl %>" useAlternateImage="<%= useAlternate %>" className="<%= productImageClassName %>" coupon="<%= curCoupon %>" />										
									</td>
								
								<% } else if ( currentItem instanceof CategoryModel ) { // is a category 
									CategoryModel category = (CategoryModel)currentItem;
									String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode );
									%>
									
									<td width="<%= cellPercentage %>%" style="padding-bottom: 5px;">									
										<display:CategoryImage category="<%= category %>" action="<%= actionUrl %>"/>										
									</td>
								
								<% } %> 
									
							</display:PatternRow>							
						</tr>
						
						<tr align="center" valign="top">						
							<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">
							
								<% if ( currentItem instanceof ProductModel ) {		
									ProductModel product = (ProductModel)currentItem;
									ProductImpression pi = new ProductImpression( product );
									String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
									FDCustomerCoupon curCoupon = null;
									if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
										curCoupon = multi_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
									}
									%>
								
									<td width="<%= cellPercentage %>%" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="catPageProdNameUnderImg">
										
										<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
										<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
										<display:ProductPrice impression="<%= pi %>" showDescription="false"/>
										<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layMultiCat"></display:FDCoupon>
										
									</font></td>
								
								<% } else if ( currentItem instanceof CategoryModel ) { // is a category 
									CategoryModel category = (CategoryModel)currentItem;
									String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode );
									%>
									
									<td width="<%= cellPercentage %>%" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="text11">
										
										<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-weight:normal;"/>
										
									</font></td>
								
								<% } %> 
									
							</display:PatternRow>							
						</tr>
					</table>
				</display:HorizontalPattern>
			<%	
		}
	}
%></fd:MultiCategoryLayout>
