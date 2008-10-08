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
    int favoritesToShow		= 0;
    int FAVS_PER_LINE		= 5; //number of products to show per line
	int MAX_FAVSLINES2SHOW	= 2; //number of lines of featured products
	int MIN_FAVS2SHOW		= 3; //minimum featured products required to show featured section
    int	MAX_FAVS2SHOW		= FAVS_PER_LINE * MAX_FAVSLINES2SHOW; //to keep each row matching

	ProductModel product;
	for (Iterator it = favorites.iterator(); it.hasNext(); ) {
		product = (ProductModel) it.next();

		ContentNodeModel prodParent = product.getParentNode(); 
        List skus = product.getSkus();

        if (product.isDiscontinued() || product.isUnavailable() || prodParent==null || !(prodParent instanceof CategoryModel) || skus.size()==0) // || !(product.isDeal())
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
		if ("picks_love".equalsIgnoreCase(catId)) {
			//image for PPicks or null
			groDeptImage = null;
		} else { 
			groDeptImage = (Image)product.getCategoryImage();
		}

        StringBuffer favoriteProducts = new StringBuffer("");
        StringBuffer favoriteProductLinks = new StringBuffer("");
        // append product image
        favoriteProducts.append("<a href=\"" + productPageLink_ + "\">");
        if (groDeptImage !=null) {
            favoriteProducts.append("<img SRC=\"" + groDeptImage.getPath() + "\" ");
            favoriteProducts.append(JspMethods.getImageDimensions(groDeptImage));
            favoriteProducts.append(" border=\"0\" alt=\""+ product.getFullName() + "\">");
        }
        favoriteProducts.append("</a>");

        // append product label
        favoriteProductLinks.append("<a href=\"" + productPageLink_ + "\">");
        String thisProdBrandLabel = product.getPrimaryBrandName();
        if (thisProdBrandLabel.length()>0) {
            favoriteProductLinks.append("<font class=\"text10bold\">" + thisProdBrandLabel + "</font><br />");
        }
        favoriteProductLinks.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
        favoriteProductLinks.append("</a><br />");

        // append product price
        favoriteProductLinks.append("<font class=\"favoritePrice\">" + prodPrice + "</font><font class=\"space4pix\"><br />&nbsp;<br />&nbsp;<br /></font>");
        
        favHTMLPieces.add(favoriteProducts);
        favHTMLPieceLinks.add(favoriteProductLinks);
        
        favoritesToShow++;
        if (favoritesToShow == MAX_FAVS2SHOW) {
        	break;
        }
	}

	if (favoritesToShow > MIN_FAVS2SHOW) {
	%>
	<!-- separator  -->
	<tr>
		<td colspan="6">
			<br /><img src="/media_stat/images/layout/cccccc.gif" width="550" height="1" border="0"><br />
			<font class="space4pix"><br /><br /></font>
		</td>
	</tr>

	<tr>
		<td width="100%" colspan="6">
		    <img src="/media_stat/images/layout/clear.gif" width="200" height="1"><br />
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
	        <font class="space4pix"><br /><br /></font>
		    <table cellpadding="0" cellspacing="0" border="0" width="125">
	        	<tr valign="top" align="center">
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
%>