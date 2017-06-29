<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.EnumCouponContext"%>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% FDUserI gen_user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER); %>

<% //expanded page dimensions
final int W_GENERIC_LAYOUT_IS_DEPARTMENT = 765;
final int W_GENERIC_LAYOUT_NOT_DEPARTMENT = 601;
%>

<div id='oas_CategoryNote'>
  <script type="text/javascript">
  	OAS_AD('CategoryNote');
  </script>
</div>

<display:InitLayout/>
<%
	//**************************************************************
	//***          the GENERIC_LAYOUT Pattern                    ***
	//**************************************************************

	String altText = null;
	String organicFlag = "";
	String prodNameAttribute = JspMethods.getProductNameToUse( currentFolder );
	int counter = 0;
	int tablewidth = 0;
	int tdwidth = 0;
	int maxCols = 0;
	int maxWidth = 0;
	Image img = null;
	int itemWidth = 0;
	int totalWidth = 0;
	String itemNameFont = null;
	StringBuffer tblCells = new StringBuffer( 600 );

	if ( isDepartment.booleanValue() ) {
		maxCols = 5;
		maxWidth = 550;
		tdwidth = 20;
	} else {
		maxCols = 4;
		maxWidth = 410;
		tdwidth = 25;
	}


	DisplayObject displayObj = null;

	// if we are on the vegetable folder: (not in a subfolder of veg) then don't show any products.
	//changed for APPDEV-3237 to check only SPECIFICALLY for those words as fullname
	boolean showOnlyFolders = false;
	int rowsPainted = 0;
	if ( "VEGETABLES".equals(( (ContentNodeModel)currentFolder ).getFullName().toUpperCase()) || "SEAFOOD".equals(( (ContentNodeModel)currentFolder ).getFullName().toUpperCase()) ) {
		showOnlyFolders = true;
	}

%>
<jsp:include page="/includes/department_peakproduce.jsp" flush="true"/>

<% if ( isDepartment.booleanValue() && "fru".equals( departmentId ) ) { %>
   	<br/><img src="/media_stat/images/layout/ourfreshfruit.gif" name="Our Fresh Fruit" width="535" height="15" border="0">
<% } %>

<%--EXPANDED_PAGE_VERIFY should the first maxColumns be 6?--%>
<display:HorizontalPattern
	id="horizontalPattern"
	itemsToShow="<%=sortedCollection%>"
	showCategories="<%= isDepartment.booleanValue() || showOnlyFolders %>"
	showProducts="<%= !showOnlyFolders %>"
	productCellWidth="<%= isDepartment.booleanValue() ? 137 : 100 %>"
	folderCellWidth="137"
	currentFolder="<%= currentFolder %>"
	useLayoutPattern="false"
	dynamicSize="true"
	maxColumns="<%= isDepartment.booleanValue() ? 6 : 5 %>"
	tableWidth="<%= isDepartment.booleanValue() ? W_GENERIC_LAYOUT_IS_DEPARTMENT : W_GENERIC_LAYOUT_NOT_DEPARTMENT %>"
	useAlternateImage="<%= useAlternateImages.booleanValue() %>"
	>

	<table cellspacing="0" cellpadding="0" width="100%">
		<tr align="center" valign="bottom">
			<display:PatternRow id="patternRow" itemsToShow="<%= rowList %>">

				<% if ( currentItem instanceof ProductModel ) { 				// ===== PRODUCT =====

					ProductModel product = (ProductModel)currentItem;
					String actionUrl = FDURLUtil.getProductURI( product, trackingCode );
					ProductImpression pi = new ProductImpression(product);
					FDCustomerCoupon curCoupon = null;
					if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
						curCoupon = gen_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
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
						gen_user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,product.getParentId(),product.getContentName());
					}
				%>

					<td width="<%= horizontalPattern.getProductCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="catPageProdNameUnderImg">

						<display:ProductRating product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductName product="<%= product %>" action="<%= actionUrl %>"/>
						<display:ProductPrice impression="<%= pi %>" showDescription="false"/>
						<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layGen"></display:FDCoupon>

					</font></td>

				<% } else if ( currentItem instanceof CategoryModel ) { 		// ===== CATEGORY =====

					CategoryModel category = (CategoryModel) currentItem;
					String actionUrl = FDURLUtil.getCategoryURI( category, trackingCode );
					%>

					<td width="<%= horizontalPattern.getFolderCellWidth() %>" style="padding-bottom: 20px; padding-left: 2px; padding-right: 2px;"><font class="text11">

						<display:CategoryName category="<%= category %>" action="<%= actionUrl %>" style="font-style:normal"/>

					</font></td>

				<% } %>

			</display:PatternRow>
		</tr>
	</table>

	<font class="space4pix"><br/>&nbsp;<br/></font>

</display:HorizontalPattern>



<%
	//**** bottom Of Jsp's  *******************
	if ( onlyOneProduct.booleanValue() ) {
		request.setAttribute( "theOnlyProduct", theOnlyProduct );
	}
%>
