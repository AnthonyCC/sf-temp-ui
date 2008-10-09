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
    int FAVS_PER_LINE		= DealsHelper.getMaxFeaturedDealsPerLine(); //number of products to show per line
    int MAX_FAVSLINES2SHOW	= DealsHelper.getMaxFeaturedDealsForPage()/FAVS_PER_LINE; //number of lines of featured products
    int MIN_FAVS2SHOW		= DealsHelper.getMinFeaturedDealsForPage(); //minimum featured products required to show featured section
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
        String prodBasePrice=null;
        boolean isDeal=false;
        int deal=0;

        /// if (skus.size()==0)
        ///	  continue;  // skip this item..it has no skus.  Hmmm?
        if (skus.size() == 1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        } else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }

        	
       	%><fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>"><% 
        prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice()); //+"/"+ productInfo.getDisplayableDefaultPriceUnit();
        isDeal=productInfo.isDeal();
        if(isDeal) {
            prodBasePrice=JspMethods.currencyFormatter.format(productInfo.getBasePrice()); //+"/"+ productInfo.getBasePriceUnit().toLowerCase();
            deal=productInfo.getDealPercentage();
        }  
        
		%></fd:FDProductInfo><%
        
        if(!isDeal) {
            continue;
        }
        String productPageLink_ = response.encodeURL("/product.jsp?catId=" + prodParent
            +"&prodCatId="+prodParent
            +"&productId="+product+"&trk=feat");
        String dealImage=new StringBuffer("/media_stat/images/deals/brst_sm_").append(deal).append(".gif").toString();
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
        favoriteProducts.append("<DIV id=prod_container style=\"WIDTH: 100px; HEIGHT: 100px; TEXT-ALIGN: left\"><A id=prod_link_img style=\"DISPLAY: block; CURSOR: hand\" HREF=\"" + productPageLink_ + "\" name=prod_link_img>");
        if (groDeptImage !=null) {
            favoriteProducts.append("<DIV id=prod_image style=\"PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 0px; LINE-HEIGHT: 0px; PADDING-TOP: 10px; POSITION: absolute; HEIGHT: 0px\">")
                            .append("<IMG style=\"BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px\" ")
                            .append(JspMethods.getImageDimensions(groDeptImage))
                            .append(" alt=\"").append(product.getFullName()).append("\" src=\"").append(groDeptImage.getPath()).append("\" ></DIV>");
            favoriteProducts.append("<DIV id=sale_star style=\"POSITION: absolute\"><IMG style=\"BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px\"  alt=\"SAVE ").append(deal).append("%\" src=\"").append(dealImage).append("\"> </DIV></A></DIV>");                        
        }
        

        // append product label
        favoriteProductLinks.append("<DIV id=prod_container style=\"WIDTH: 100px; HEIGHT: 100px; TEXT-ALIGN: left\"><A id=prod_link_img style=\"DISPLAY: block; CURSOR: hand\" HREF=\"" + productPageLink_ + "\">");
        favoriteProductLinks.append("<DIV id=prod_container_text style=\"FONT-WEIGHT: normal; FONT-SIZE: 8pt; WIDTH: 100px; TEXT-ALIGN: center\">");
        favoriteProductLinks.append("<DIV id=prod_text_container><A id=prod_link style=\"COLOR: #360\" href=\"")
                            .append(productPageLink_).append("\">");

        String thisProdBrandLabel = product.getPrimaryBrandName();
        if (thisProdBrandLabel.length()>0) {
            favoriteProductLinks.append("<B>" + thisProdBrandLabel + "</B><BR>");
        }
        favoriteProductLinks.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
        favoriteProductLinks.append("</A></DIV>");

        // append product price
        favoriteProductLinks.append("<DIV id=prod_price_d_container style=\"FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00\">").append(prodPrice).append("</DIV>");
        favoriteProductLinks.append("<DIV id=prod_price_b_container style=\"FONT-SIZE: 7pt; COLOR: #888\">(was ").append(prodBasePrice).append(")</DIV></DIV>");        

        favHTMLPieces.add(favoriteProducts);
        favHTMLPieceLinks.add(favoriteProductLinks);
        
        favoritesToShow++;
        if (favoritesToShow == MAX_FAVS2SHOW) {
        	break;
        }
	}

	if (favoritesToShow >= MIN_FAVS2SHOW) {
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
                if (department.getDepartmentBottom() != null) {
					for(Iterator deptBotItr = department.getDepartmentBottom().iterator(); deptBotItr.hasNext();) {
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