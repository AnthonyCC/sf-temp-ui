<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>


<%
//********** Start of Stuff to let JSPF's become JSP's **************

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if (deptId!=null) {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
    isDepartment = true;
} else {
    currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
String trkCode = (String)request.getAttribute("trkCode");

if (trkCode!=null && !"".equals(trkCode.trim()) ) {
    trkCode = "&trk="+trkCode.trim();
} else {
    trkCode = "";
}

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null)
	sortedColl = new ArrayList();

String BrowseHeader = null;
String FeaturedHeader = null;

String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
String newProdsFldrId = currentFolder.getContentName().toLowerCase()+"_new";
// !!! we should assign the broweHeader item to the folder_label attrib...and create a new one for the feature header
     String currFolderId = currentFolder.getContentName();
    if ( currFolderId.equalsIgnoreCase("DAI") ) {
        BrowseHeader = "/media/images/navigation/department/dairy/dai_browse_categories.gif";
        FeaturedHeader = "/media/images/navigation/department/dairy/dai_featured_items.gif";
    } else if ( currFolderId.equalsIgnoreCase("GRO") ) {
        BrowseHeader = "/media/images/navigation/department/grocery/gro_browse_categories.gif";
        FeaturedHeader = "/media/images/navigation/department/grocery/gro_featured_items.gif";
    } else if ( currFolderId.equalsIgnoreCase("FRO") ) {
        BrowseHeader = "/media/images/navigation/department/frozen/fro_browse_categories.gif";
        FeaturedHeader = "/media/images/navigation/department/frozen/fro_featured_items.gif";
    } else if ( currFolderId.equalsIgnoreCase("SPE") ) {
        BrowseHeader = "/media/images/navigation/department/specialty/spe_browse_categories.gif";
        FeaturedHeader = "/media/images/navigation/department/specialty/spe_featured_items.gif";
    } else if ( currFolderId.equalsIgnoreCase("HBA") ) {
        BrowseHeader = "/media/images/navigation/department/hba/hba_cat/hba_browse_categories.gif";
        FeaturedHeader = "/media/images/navigation/department/hba/hba_cat/hba_featured.gif";
    } else if ( currFolderId.toLowerCase().indexOf("_picks")!=-1 ) {
        BrowseHeader =    "/media_stat/images/navigation/department/our_picks/our_picks_browse.gif";
        FeaturedHeader =  "/media_stat/images/navigation/department/our_picks/our_fav_picks.gif";
    } else {
        BrowseHeader = "/media_stat/images/layout/clear.gif";
        FeaturedHeader =  "/media_stat/images/layout/clear.gif";
    }
%>
<img src="/media_stat/images/layout/clear.gif" width="550" height="1">
<table cellpadding="0" cellspacing="0" border="0" width="550">
    <tr valign="top">
    	<td width="550"> <%-- this is the category column --%>
        	<table cellpadding="0" cellspacing="0" border="0" width="100%">
				<tr><td colspan="6">
					<span class="title16"><%= currentFolder.getEditorialTitle() %></span>
				</td></tr>

				<!-- separator  -->
				<tr><td colspan="6"><BR>
				<IMG src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><BR>
				<FONT CLASS="space4pix"><BR><br></FONT>
				</td></tr>

	            <tr>
	                <td colspan="6"><img src="<%= BrowseHeader %>"><br><br></td>
	            </tr><%
    //display the subfolders 

    StringBuffer imageCell = new StringBuffer("");
    StringBuffer listColumn1 = new StringBuffer("");
    StringBuffer listColumn2 = new StringBuffer("");

    int firstRowSize = sortedColl.size()/2 + (sortedColl.size()%2);
    String IMG_NAME = "subfolder_image";

    int count = 0;
    int imgIndex = 0;
    com.freshdirect.fdstore.content.CategoryModel categoryModel = null;
        ContentNodeModel itmNode = null;
        Attribute attribGroDept = null;
        Image groDeptImage = null;
        boolean makeImageCol = true;
        StringBuffer tmpColumn = new StringBuffer();
    	for(Iterator itmIter = sortedColl.iterator();itmIter.hasNext();) {
            itmNode = (ContentNodeModel)itmIter.next();
            if (! (itmNode instanceof CategoryModel)) continue;  //ignore anything that's not a Category
            categoryModel = (CategoryModel)itmNode;
            groDeptImage = null;
            attribGroDept= categoryModel.getAttribute("CAT_PHOTO");
            if (attribGroDept !=null) {
                groDeptImage = (Image)attribGroDept.getValue();
            }

            if (makeImageCol && !categoryModel.getContentName().equalsIgnoreCase(newProdsFldrId)) { //build the image column
                makeImageCol = false;
                imageCell.append("<img name=\"");
                imageCell.append(IMG_NAME);
                imageCell.append("\" src=\"");
                imageCell.append(groDeptImage!=null?groDeptImage.getPath():"");
                imageCell.append("\" ");
                imageCell.append(JspMethods.getImageDimensions(groDeptImage));
                imageCell.append("border=\"0\">"); //<BR><img src=\"
            }
            tmpColumn.setLength(0);
            tmpColumn.append("<a href=\"");
            tmpColumn.append(response.encodeUrl( "/category.jsp?catId=" + categoryModel + "&trk=dpage")); //here
            tmpColumn.append("\" onMouseover='");
            tmpColumn.append("swapImage(\""+IMG_NAME+"\",\""+(groDeptImage!=null?groDeptImage.getPath():"")+"\"");
            tmpColumn.append(")'>");
            tmpColumn.append(categoryModel.getFullName());
            tmpColumn.append("</a>");
            if (categoryModel.getContentName().equalsIgnoreCase(newProdsFldrId)) {
                tmpColumn.append("&nbsp;<img src=\"/media_stat/images/template/newproduct/star_CC0033.gif\" width=\"12\" height=\"12\"  border=\"0\">");
            } 
            tmpColumn.append("<br><img src=\"/media_stat/images/layout/clear.gif\" width=\"1\" height=\"5\"  border=\"0\"><br>");

            if (count < firstRowSize) {
	            listColumn1.append(tmpColumn.toString());
            } else {
	            listColumn2.append(tmpColumn.toString());
            }

            count++;
            imgIndex++;

    } //

    //writing the actual html for displaying all subfolders
%>
			<tr>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
				<td width="100" valign="top"><%= imageCell.toString() %></td>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
				<td width="205" valign="top" class="text13bold"><%= listColumn1.toString() %></td>
				<td width="5"><img src="/media_stat/images/layout/clear.gif" width="5" height="1" border="0"></td>
				<td width="205" valign="top" class="text13bold"><%= listColumn2.toString() %></td>
			</tr><%-- end of categories --%>

<%-- Featured Products column --%>
<%  
    Attribute attribFeatProds = currentFolder.getAttribute("FEATURED_PRODUCTS");
    List favorites;
    // List validFavorites = new ArrayList();
    List favHTMLPieces = new ArrayList();
    List favHTMLPieceLinks = new ArrayList();
    if (attribFeatProds!=null && !((List)attribFeatProds.getValue()).isEmpty() ) {
        List prodList = new ArrayList();
        for (Iterator itrFav = ((List)attribFeatProds.getValue()).iterator(); itrFav.hasNext();) {
            ContentRef cr = (ContentRef) itrFav.next();
            if (!(cr instanceof ProductRef)) continue;
            ProductModel pm = (ProductModel)JspMethods.getFeaturedProduct(cr);
            prodList.add(pm);
        }
        
        List filterNames = (List)currentFolder.getAttribute("FILTER_LIST",Collections.EMPTY_LIST);
        favorites = prodList;
    } else {
        favorites = Collections.EMPTY_LIST;
    }
    
    /// StringBuffer favoriteProducts = new StringBuffer("");
    /// ContentNodeModel prodParent = null;
    ContentFactory contentFactory = ContentFactory.getInstance();
    Comparator priceComp = new ProductModel.PriceComparator();
    int favoritesToShow = 0;
    int	MAX_FAVS2SHOW	= 10;
    int FAVS_PER_LINE	= 5;

	ProductModel product;
	for (Iterator it = favorites.iterator(); it.hasNext(); ) {
		product = (ProductModel) it.next();

		ContentNodeModel prodParent = product.getParentNode(); 
        List skus = product.getSkus();

        if (product.isDiscontinued() || product.isUnavailable() || prodParent==null || !(prodParent instanceof CategoryModel) || skus.size()==0)
        	continue;

        // validFavorites.add(product);	

        SkuModel sku = null;
        String prodPrice = null;
        /// if (skus.size()==0)
        ///	  continue;  // skip this item..it has no skus.  Hmmm?
        if (skus.size() == 1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        } else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }

        	
       	%><fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>"><% 
        prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); //+"/"+ productInfo.getDisplayableDefaultPriceUnit();
		%></fd:FDProductInfo><%
        String productPageLink_ = response.encodeURL("/product.jsp?catId=" + prodParent
            +"&prodCatId="+prodParent
            +"&productId="+product+"&trk=feat");

        //productDisplayName = getDisplayName(product,prodNameAttribute);
        groDeptImage = (Image)product.getCategoryImage();

        StringBuffer favoriteProducts = new StringBuffer("");
        StringBuffer favoriteProductLinks = new StringBuffer("");
        // append product image
        favoriteProducts.append("<A HREF=\"" + productPageLink_ + "\">");
        if (groDeptImage !=null) {
            favoriteProducts.append("<img SRC=\"" + groDeptImage.getPath() + "\" ");
            favoriteProducts.append(JspMethods.getImageDimensions(groDeptImage));
            favoriteProducts.append(" border=\"0\" alt=\""+ product.getFullName() + "\">");
        }
        favoriteProducts.append("</A>");

        // append product label
        favoriteProductLinks.append("<A HREF=\"" + productPageLink_ + "\">");
        String thisProdBrandLabel = product.getPrimaryBrandName();
        if (thisProdBrandLabel.length()>0) {
            favoriteProductLinks.append("<FONT CLASS=\"text10bold\">" + thisProdBrandLabel + "</font><BR>");
        }
        favoriteProductLinks.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
        favoriteProductLinks.append("</A><BR/>");

        // append product price
        favoriteProductLinks.append("<font class=\"favoritePrice\">" + prodPrice + "</font><font class=\"space4pix\"><br/>&nbsp;<br/>&nbsp;<br/></font>");
        
        favHTMLPieces.add(favoriteProducts);
        favHTMLPieceLinks.add(favoriteProductLinks);
        
        favoritesToShow++;
        if (favoritesToShow == MAX_FAVS2SHOW) {
        	break;
        }
	}

	if (favoritesToShow > 0) {
	%>
	<!-- separator  -->
	<tr><td colspan="6"><BR>
	<IMG src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><BR>
	<font CLASS="space4pix"><br/><br/></font>
	</td></tr>

	<tr>
		<td width="100%" colspan="6">
		    <img src="/media_stat/images/layout/clear.gif" width="200" height="1"><BR>
	        <%
			// display department specific media
			if (isDepartment) {
				DepartmentModel department = (DepartmentModel) currentFolder;
				if (department.getDepartmentMiddleMedia() != null) {
					for(Iterator deptBotItr = department.getDepartmentMiddleMedia().iterator(); deptBotItr.hasNext();) {
						Html piece = (Html)deptBotItr.next();
						if (piece != null) {
				        	String deptBotItm = piece.getPath();
				  			%><fd:IncludeMedia name='<%= deptBotItm %>' />
<%
				    	}
					}
				}
			}
	        %>
	        <FONT CLASS="space4pix"><br/><br/></FONT>
		    <table cellpadding="0" cellspacing="0" border="0" width="125">
	        	<tr VALIGN="TOP" ALIGN="CENTER">
	            	<td>
	            		<table id="fav-prds-table" width="550" border="0" cellspacing="0" cellpadding="0">
	            		<tr>
		            	<%  
		            	List row = favHTMLPieces;
		            	
		            	for (int k=0; k<row.size(); ++k) {
		            		%><td align="center" valign="<%= row == favHTMLPieces ? "bottom" : "top" %>" width="110">
		            			<img src="/media_stat/images/layout/clear.gif" width="100" height="1"><br/>
		            			<%= row.get(k) %>
		            		</td><%
		            			// start a new row if it has enough content
		            			if (k>0 && (k%FAVS_PER_LINE) == FAVS_PER_LINE-1) {
		            				row = row == favHTMLPieces ? favHTMLPieceLinks : favHTMLPieces;
		            				if (row == favHTMLPieceLinks) k -= FAVS_PER_LINE;
		            				%></tr><tr><%
		            			} else if (k == row.size() -1) {
		            				for(int j=0; j < FAVS_PER_LINE-(favHTMLPieces.size()%FAVS_PER_LINE); ++j) {
		            					%><td>&nbsp;</td><%
		            				}
		            				if (row == favHTMLPieces) { 
		            					%></tr><tr><%
		            					row = favHTMLPieceLinks;
		            					k -= (favHTMLPieces.size()%FAVS_PER_LINE);
		            				}
		            			}
		            		
		            	}
		            	/*
		            	// put placeholder TD-s if a row is not filled in entirely
		            	for (int k=0; k<FAVS_PER_LINE-(favHTMLPieces.size()%FAVS_PER_LINE); k++) {
		            		%><td>&nbsp;</td><%
		            	}
		            	*/
		            	%>
		            	</tr>
		            	</table>
	            	</td>
	        	</tr>
	    	</table>
		</td>
	</tr><%-- close the featured product column --%><%
    	}

// display the featured brands, based on each of the featured categories
        List featuredCats = null;
        List featuredBrands = null;
        int trimAt=10;

        Attribute dptBottAttrib = currentFolder.getAttribute("FEATURED_CATEGORIES");
        if (dptBottAttrib!=null) {
            featuredCats = (List)dptBottAttrib.getValue();
        }
        
        int brandsShown=0;

        if (featuredCats != null && featuredCats.size() > 0) { %>
			<!-- separator  -->
            <tr><td colspan="6"><BR/>
            <IMG src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><BR>
            <FONT CLASS="space4pix"><BR/><BR/></FONT>
            <IMG src="/media_stat/images/layout/dfgs_featured_brands.gif" width="115" height="10" border="0"><BR/><BR/>
            </td></tr>

            <tr><td colspan="6">
<%
			for (int fc = 0; fc < featuredCats.size(); fc++) {
                CategoryRef catRef= (CategoryRef)featuredCats.get(fc);
                String catRefUrl = response.encodeURL("/category.jsp?catId="+catRef.getCategoryName()+"&trk=feat");
                CategoryModel catMod = catRef.getCategory();
                dptBottAttrib = catMod.getAttribute("FEATURED_BRANDS");

                if (dptBottAttrib !=null) {
                    featuredBrands = (List)dptBottAttrib.getValue();
                    brandsShown=0;
                    %>
                    <%-- // do not display featured category name anymore
                    <br>
           			<FONT CLASS="text11bold"><a href="<%= catRefUrl %>"><%= catMod.getFullName() %></A>
           			<br/><br/>
           			</FONT>
           			--%>
           			<%
           			for(int i=0; i<featuredBrands.size();i++){
                    //&& brandsShown<10 - show all avail prods
                        BrandModel brandMod= ((BrandRef)featuredBrands.get(i)).getBrand();

                        if (brandMod==null) continue;
                        dptBottAttrib = brandMod.getAttribute("BRAND_LOGO_MEDIUM");
                        //if (dptBottAttrib==null) continue; //no image
                        Image bLogo = new Image();
                        if (dptBottAttrib==null) {
                            bLogo.setPath("/media_stat/images/layout/clear.gif");
                            bLogo.setWidth(12);
                            bLogo.setHeight(30);
                         } else bLogo = (Image)dptBottAttrib.getValue();

                        String brandLink = response.encodeURL("/category.jsp?catId="+catMod+"&brandValue="+brandMod.getContentName()+"&groceryVirtual="+catMod+"&trk=feat");
                        brandsShown++;
%>
						
                        <a href="<%= brandLink %>"><img src="<%= bLogo.getPath() %>" width="<%= bLogo.getWidth() %>" height="<%= bLogo.getHeight() %>" alt="<%= brandMod.getFullName() %>" border="0"></a><%= (brandsShown%trimAt)==0 ? "<br/>": "" %>
<%                      
                }
                    
				// put a break between categories, if any brand images were displayed
                if (brandsShown > 0) { 
					%><font class="space4pix"><br></font><%
				}
            }
        }
    }
%>
					</td>
				</tr>
			</table>
		</td><%-- end the category column --%>
	</tr>
</table>
