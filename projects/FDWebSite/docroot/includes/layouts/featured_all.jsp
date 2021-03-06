<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>
<%@page import="com.freshdirect.common.pricing.ZoneInfo"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<% //expanded page dimensions
final int W_FEATURED_ALL = 601;
final int W_FEATURED_LEFT = 109; 
final int W_FEATURED_PADDING = 14;
%>

<%!
	JspMethods.ContentNodeComparator contentNodeComparator = new JspMethods.ContentNodeComparator();
%>

<display:InitLayout/>

<%	
//**************************************************************
//***         the Featured All Pattern                       ***
//**************************************************************
    FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	boolean onlyOneProduct = false;
	ProductModel theOnlyProduct = null;


    ContentNodeModel owningFolder = currentFolder;

    Image favAllImage = null;
    boolean showPrices = ((ProductContainer)currentFolder).isFavoriteShowPrice();
    String imagePath = null;
    String imageDim = "";
    String clearImage = "/media_stat/images/layout/clear.gif"; 
    String unAvailableImg="/media_stat/images/template/not_available.gif";
    String notAvailImgName = "";
    
    ContentNodeModel parentNode = currentFolder.getParentNode();    
    while ( parentNode != null && !(parentNode instanceof DepartmentModel)) {
        parentNode = parentNode.getParentNode();
    }

    String deptName = parentNode.getFullName(); //getDepartmentPath(webconnect.getFolder().getString("path") );
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    String tdwidth="92";

    if ( deptName.indexOf("coffee") != -1 ) {
		String thisLocation = currentFolder.getFullName();
        if ( thisLocation.endsWith("Roast") || thisLocation.endsWith("Region") ) {
        tdwidth="96";
        }
    }


    LinkedList productLinks = new LinkedList();
    LinkedList productPrices = new LinkedList();
    LinkedList unAvailableProds = new LinkedList();
    StringBuffer col1 = new StringBuffer(300);
    
    String imgName = null;
    String burstImgName = null;
    String burstDivName = null;
    
    boolean folderAsProduct = false;
    ContentNodeModel aliasNode = null;
    ContentNodeModel prodParent = null;
    Comparator priceComp = new ProductModel.PriceComparator();
	
	boolean hideFi = false;
	if (currentFolder instanceof CategoryModel)
		hideFi = ((CategoryModel) currentFolder).isHideFeaturedItems();
%>


<%-- FEATURED ITEMS DISPLAY START --%>

<fd:ProductGroupRecommender siteFeature="FEATURED_ITEMS" facility="cat_feat_items" id="recommendations"  currentNode="<%= currentFolder %>" itemCount="5"
		hide="<%= hideFi %>"><%

	if (recommendations != null && recommendations.getProducts().size() > 0) {
		
	    request.setAttribute("recommendationsRendered","true");

		List products = recommendations.getProducts();
		int ord = 1;
		
		/* check if any products have a coupon */
		boolean productsHaveCoupons = false;
		ArrayList<FDCustomerCoupon> ref_coupons = new ArrayList<FDCustomerCoupon>();
		
		for (Object prod : products) {
			ProductModel curProd = (ProductModel) prod;
			ProductImpression pi = new ProductImpression( curProd );

        	FDCustomerCoupon curCoupon = null;
        	if (pi.getSku() != null && pi.getSku().getProductInfo() != null) {
        		curCoupon = user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,curProd.getParentId(),curProd.getContentName());
        	}
        	
        	if (curCoupon != null) { productsHaveCoupons = true; }
        	ref_coupons.add( curCoupon );
		}
		
		%>
		<table cellspacing="0" cellpadding="1" border="0" width="<%= W_FEATURED_ALL %>">
		
			<tr valign="top" align="center">
	    		<td CLASS="text12bold" width="<%= W_FEATURED_ALL %>" colspan="<%= W_FEATURED_ALL %>">
	    			<%= recommendations.getVariant().getServiceConfig().getFILabel() %>
	    		</td>
			</tr>
			
			<tr valign="top" align="CENTER">
				<logic:iterate id='contentNode' collection="<%= products %>" type="com.freshdirect.storeapi.content.ProductModel" indexId="index"><%
				
					ProductModel productNode = contentNode;
					ProductImpression pi = new ProductImpression( productNode );
					ProductLabeling pl = new ProductLabeling(user, productNode, recommendations.getVariant().getHideBursts());
					String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
					String prodImageClassName = (productsHaveCoupons) ? "couponLogo" : null;
					
					%><%-- display a product --%>
					
					<td width="<%= tdwidth %>">
						<div class="smartstore-carousel-item">
							<display:ProductImage product="<%= productNode %>" action="<%= actionURI %>" showRolloverImage="true" hideBursts="<%= recommendations.getVariant().getHideBursts() %>"  coupon="<%= ref_coupons.get(index) %>" className="<%= prodImageClassName %>" />
							<display:ProductRating product="<%= productNode %>" action="<%= actionURI %>"/>
							<display:ProductName product="<%= productNode %>" action="<%= actionURI%>"/>
							<div class="favoritePrice"><display:ProductPrice impression="<%= pi %>" showDescription="false"/></div>
							<display:FDCoupon coupon="<%= ref_coupons.get(index) %>" contClass="fdCoupon_layFeatAll"></display:FDCoupon>
						</div>
					</td>
					
					<td width="10">
						<img SRC="media_stat/images/layout/clear.gif" width="8" height="1">
					</td>
					<% ++ord;%>
				</logic:iterate>
			</tr>
		</table>
	<% } %>
</fd:ProductGroupRecommender>

<%-- FEATURED ITEMS DISPLAY END --%>



<font class="space4pix"><br/></font>

<%
    CategoryModel displayCategory = null;
    boolean gotAvailProdImg = false;
    StringBuffer appendColumn = new StringBuffer(200);
    StringBuffer appendColumnPrices = new StringBuffer(200);
%>

<logic:iterate id='contentNode' collection="<%=sortedCollection%>" type="com.freshdirect.storeapi.content.ContentNodeModel">

	<%      
		if ( displayCategory == null ) {
            if ( contentNode instanceof ProductModel ) {
            displayCategory = (CategoryModel)currentFolder;
            } else {
                displayCategory = (CategoryModel)contentNode;
            }
        }
        folderAsProduct = false;
        
        //figure out if this item is to be treated like a product..(if the folder link is set)
        //!!! not a cool way.  should have some flag called treatAsProduct
       
        if ( contentNode instanceof CategoryModel ) {
            CategoryModel categoryModel = (CategoryModel) contentNode;
            folderAsProduct = categoryModel.getTreatAsProduct();
            
            ContentNodeModel alias = categoryModel.getAlias();
            if ( alias != null ) {
                if (alias instanceof ProductModel) {
                    aliasNode = alias;
                } else if(alias instanceof CategoryModel) {
                    aliasNode = alias;
                }
                else if(alias instanceof DepartmentModel){
                    aliasNode = alias;
                }
            }
        }
        
        if ( contentNode instanceof CategoryModel && !folderAsProduct ) {
            owningFolder = (CategoryModel)contentNode;
            onlyOneProduct = false;
            
            if (productLinks.size() > 0 ) { //display the stuff we %>
            
				<table cellspacing="0" cellpadding="0" border="0" width="<%=W_FEATURED_ALL%>">
					<tr valign="middle">
					    <td width="<%=W_FEATURED_ALL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" border="0"></td>
					</tr>
					<tr valign="middle">
					    <td width="<%=W_FEATURED_ALL%>" COLSPAN="4" CLASS="title10"><font color="#515151"><%=displayCategory.getFullName().toUpperCase()%></font></td>
					</tr>
					<tr valign="middle">
						<td width="<%=W_FEATURED_ALL%>" COLSPAN="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></td>
					</tr>
					<tr valign="middle">
						<td width="<%=W_FEATURED_ALL%>" COLSPAN="4" BGCOLOR="#DDDDDD"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td>
					</tr>
					<tr valign="middle">
					    <td width="<%=W_FEATURED_ALL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"></td>
					</tr>
				</table>
				
				<% String outputProducts= JspMethods.displayFAProducts( productLinks, productPrices, showPrices, false ); %>
				
				<table cellspacing="0" cellpadding="1" border="0" width="<%=W_FEATURED_ALL%>">
					<tr valign="top">
					    <td width="<%=W_FEATURED_LEFT%>"><%=col1.toString()%></td><td width="<%=W_FEATURED_PADDING%>"></td><%=outputProducts%>
					</tr>
				</table>
				
			<% } else if ( unAvailableProds.size() > 0 ) {
                col1 = new StringBuffer();
            }
            
            col1.setLength(0);
            productLinks.clear();
            productPrices.clear();
            gotAvailProdImg = false;

            displayCategory=(CategoryModel)contentNode;
         
          //end of folder instance check
          
    	} else {  // must be a product. we'll need the usual stuff, and possibly the price
    		
            ProductModel product = null;
            favAllImage = null;
            imagePath = null;
            imageDim = null;
            
            imgName = "Img" + displayCategory;       
            burstImgName = "BurstImg" + displayCategory;
            burstDivName = "BurstDiv" + displayCategory;
            notAvailImgName = imgName + "_unavailable";
            
        	int imgWidth = 80, imgHeight = 80;

        	
            if ( folderAsProduct ) {
                if ( aliasNode instanceof CategoryModel ) {
                    final Image catPhoto = ((CategoryModel) aliasNode).getCategoryPhoto();
                    if ( catPhoto != null ) {
                        favAllImage = catPhoto;
                        imagePath = favAllImage.getPath();
                        imageDim = JspMethods.getImageDimensions(favAllImage);
                        imgWidth = favAllImage.getWidth();
                        imgHeight = favAllImage.getHeight();
                        onlyOneProduct = false;
                    }
                } 
            } else if ( contentNode instanceof ProductModel ) {
                if ( theOnlyProduct != null ) {
                    onlyOneProduct = false;
                } else {
                    onlyOneProduct = true;
                    theOnlyProduct = (ProductModel)contentNode;
                }

                product = (ProductModel)contentNode;
                owningFolder = (CategoryModel) product.getParentNode();
                imagePath = product.getCategoryImage().getPath();
                imageDim = JspMethods.getImageDimensions(product.getCategoryImage());
                imgWidth = product.getCategoryImage().getWidth();
                imgHeight = product.getCategoryImage().getHeight();
            }
            

            if ( col1.length() == 0  || !gotAvailProdImg ) {
            	
                col1.setLength(0);             
                
                // burst related stuff ->
                
                col1.append("<div class=\"imgcontainer\" style=\"overflow: hidden; padding: 0px; border: 0px; margin: 0px auto; "
    					+ "width: 80px; "
    					+ "height: 80px; "
    					+ "position: relative;\">\n");
                
                // <- burst related stuff
                
                
                col1.append("<img style=\"margin: 0 auto;\" name=\"");
                col1.append(imgName);
                col1.append("\" src=\"");
                col1.append(imagePath);
                
                if ( "".equals(imageDim) ) {
                	col1.append("\" width=\"80\" height=\"80\" border=\"0\">");
                } else {
                    col1.append("\"");
					
					//get a copy of the image dimensions to manipulate
					String imgS = imageDim;
					//add a comma delim
					imgS = imgS.replaceFirst("\" ", ",");
					//remove everything except "W,H"
					imgS = imgS.replaceAll("[ =\"a-z]", "");
					//split, 0=width;1=height
					String imgSarr[] = imgS.split(",");
					//check if original width is over W_FEATURED_LEFT, and if so change to W_FEATURED_LEFT //EXPANDED_PAGE_VERIFY should this be W_FEATURED_LEFT?
					//this is the same value that we're using in common_javascript.js [swapImage2sup]
					if ( Integer.valueOf( imgSarr[0] ).intValue() > W_FEATURED_LEFT ){ imgSarr[0] = Integer.toString(W_FEATURED_LEFT); }
					//output width
                    col1.append(" width=\""+imgSarr[0]+"\">");
                }
                col1.append("<br/><img name=\"");
                col1.append(notAvailImgName);
                col1.append("\" src=\"");
                
                if ( folderAsProduct || ( product != null && !product.isUnavailable() ) ) {
                    col1.append(clearImage);
                    gotAvailProdImg = true;
                } else {
                    col1.append(unAvailableImg);
                }
                col1.append("\"");
                col1.append("width=\"70\" alt=\"\" height=\"9\"");
                col1.append(">");
                
                
                
                // burst related stuff ->
                
                int deal = (product == null) ? 0 : product.getHighestDealPercentage();
		
			if(product!=null){
				ProductLabeling prodLabel = new ProductLabeling(user, product);
				
				col1.append("<div style=\"position: absolute; top: 0px; left: 0px\">\n");
                
				String burstImage = deal > 0 ? "/media_stat/images/deals/brst_sm_" + deal + ".png" : prodLabel.isDisplayFave()? "/media_stat/images/bursts/brst_sm_fave.png" :prodLabel.isDisplayNew() ? "/media_stat/images/bursts/brst_sm_new.png":clearImage;

				col1.append("<img name=\"" + burstImgName + "\" src=\"" + burstImage + "\" width=\"35px\" height=\"35px\" style=\"border: 0; " + ( deal > 0 ? "" : "display: none;" ) + "\">");                
                
                col1.append( "</div>" );                
                col1.append( "</div>" );
                
                // <- burst related stuff
                }
            }

            appendColumn.setLength(0);
            appendColumnPrices.setLength(0);
            String lstUnitPrice = null;


            if ( showPrices && !folderAsProduct ) {
                List skus = product.getSkus();
                for ( ListIterator li=skus.listIterator(); li.hasNext(); ) {
                	SkuModel sku = (SkuModel)li.next();
                	if ( sku.isUnavailable() ) {
                    	li.remove();
                    }
                }
                int skuSize = skus.size();

                SkuModel sku = null;
                String prodPrice = null;
                // skip this item..it has no skus.  Hmmm?
                if ( skuSize == 1 ) {
                    sku = (SkuModel)skus.get(0);  // we only need one sku
                } else if ( skus.size() > 1 ) {
                    sku = (SkuModel) Collections.min(skus, priceComp);
                }
                
              	if ( sku != null ) {
				%>
					<fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
						<%
							lstUnitPrice = "<font class=\"price\"" + ( productInfo.getZonePriceInfo(user.getPricingContext().getZoneInfo()).isItemOnSale() ? " style=\"color:#C94747\"" : "" ) + ">" + 
							                    	JspMethods.formatPrice(productInfo, user.getPricingContext()) + "</font>";
						%>
					</fd:FDProductInfo>
				<%              
				}
            }
            
            if ( lstUnitPrice == null ) {
                lstUnitPrice = "&nbsp;";
            } else if ( product.isUnavailable() ) {
                lstUnitPrice = "Not Avail.";
            }
            
            if ( folderAsProduct ) {
                if ( aliasNode != null ) {
                	String fldrURL = FDURLUtil.getCategoryURI((CategoryModel)aliasNode,trackingCode);
                	
                    appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px; \"><a href=\"");
                    appendColumn.append(fldrURL);
                    appendColumn.append("\" ");
                    final Image catPhoto = ((CategoryModel)aliasNode).getCategoryPhoto();
                    if ( catPhoto != null ) {
                        favAllImage = catPhoto;
                        imagePath = favAllImage.getPath();
                        appendColumn.append("onMouseover='");
                        appendColumn.append("swapImage(\""+imgName+"\",\""+imagePath+"\"");
                        appendColumn.append(")");
                        appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\")'");
                    }
                    appendColumn.append(">");
                    appendColumn.append(aliasNode.getFullName());
                    appendColumn.append("</a>");
                    appendColumnPrices.append("&nbsp;");
                }
            } else {
                String theProductName = JspMethods.getDisplayName(product,prodNameAttribute);
                String theBrandName = product.getPrimaryBrandName();
                if ( theBrandName != null && theBrandName.length() > 0 && theProductName.startsWith(theBrandName) ) {
                    String shortenedProductName = theProductName.substring(theBrandName.length()).trim();
                    theProductName = "<b>"+theBrandName + "</b> " + shortenedProductName;
                }

            	String prodURL = FDURLUtil.getProductURI( product, trackingCode );
            	
                appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px; \"><a href=\"");
                appendColumn.append(prodURL);
                appendColumn.append("\" onMouseover='");
                
				//We're getting the W,H of the product image and passing it to the swapImage2 javascript function.
				//H isn't needed, but we'll pass it now in case we use it later.
				
				//get a copy of the image dimensions to manipulate
				String imgS = JspMethods.getImageDimensions(product.getCategoryImage());
				//add a comma delim
				imgS = imgS.replaceFirst("\" ", ",");
				//remove everything except "W,H"
				imgS = imgS.replaceAll("[ =\"a-z]", "");
				
				
                // burst related stuff ->
                
				int deal = (product == null) ? 0 : product.getHighestDealPercentage();	
				if(product!=null){

				ProductLabeling prdLbl = new ProductLabeling(user, product);
				
				String burstUrl = deal > 0 ? "/media_stat/images/deals/brst_sm_" + deal + ".png" : prdLbl.isDisplayFave()? "/media_stat/images/bursts/brst_sm_fave.png":prdLbl.isDisplayNew() ? "/media_stat/images/bursts/brst_sm_new.png": clearImage;

				appendColumn.append( "swapImageAndBurst(\"" + imgName + "\",\"" + ((Image)product.getCategoryImage()).getPath() + "\"," + imgS + ",\"" + 
						(deal > 0) + "\",\"" + burstImgName  + "\",\"" + burstUrl + "\"" + ", 80, 80)" );
				}
                // <- burst related stuff				
				
                if( product.isUnavailable() ) {
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+unAvailableImg+"\"");
                } else {
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\"");
                }
                appendColumn.append(")'>");
                    
                if ( product.isUnavailable() ) {
                    appendColumn.append("<font color=\"#999999\">");
                    appendColumn.append(theProductName);
                    appendColumn.append("</font>");
                    appendColumn.append("</a>");
                    appendColumn.append(lstUnitPrice);
                    appendColumn.append("</div>");
                    
                    appendColumnPrices.append("<font color=\"#999999\">");
                    appendColumnPrices.append(lstUnitPrice);
                    appendColumnPrices.append("</font>");
                } else {
                    appendColumn.append(theProductName);
                    appendColumn.append("</a></div>");
                    
                    appendColumnPrices.append(lstUnitPrice);
                }
            }
            if ( product != null && product.isUnavailable() ) {
                unAvailableProds.add(appendColumn.toString());
            } else {
                productLinks.add(appendColumn.toString());
                productPrices.add(appendColumnPrices.toString());
            }
		}// end of Product instance check
		%>
</logic:iterate>





<% if (productLinks.size() > 0) { %>
	<table cellspacing="0" cellpadding="0" border="0" width="<%=W_FEATURED_ALL%>">
		<tr valign="middle">
			<td width="<%=W_FEATURED_ALL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" border="0"></td>
		</tr>
		<tr valign="middle">
		    <td width="<%=W_FEATURED_ALL%>" COLSPAN="4" CLASS="title10"><font color="#515151"><%=displayCategory.getFullName().toUpperCase()%></font></td>
		</tr>
		<tr valign="middle">
			<td width="<%=W_FEATURED_ALL%>" COLSPAN="4" BGCOLOR="#DDDDDD" ><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td>
		</tr>
		<tr valign="middle">
			<td width="<%=W_FEATURED_ALL%>" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4"></td>
		</tr>
	</table>
	
	<%
	//loop completed, but we may have accumulated some prods to display..so
	if ( productLinks.size() > 0 ) { //display the stuff we
    	String outputProducts= JspMethods.displayFAProducts(productLinks, productPrices,showPrices,false);
		%>


		<table cellspacing="0" cellpadding="1" border="0" width="<%=W_FEATURED_ALL%>">
			<tr valign="top">
				<td width="<%=W_FEATURED_LEFT%>"><%=col1.toString()%></td><td width="<%=W_FEATURED_PADDING%>"></td><%=outputProducts%>
			</tr>
		</table>
	<% }
	
} 

if (onlyOneProduct) {
    request.setAttribute("theOnlyProduct",theOnlyProduct);
}
// end of featured_all_Layout
%>
