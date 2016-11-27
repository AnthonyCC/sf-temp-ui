<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' 
%><%@ page import='com.freshdirect.fdstore.content.*'
%><%@ page import='com.freshdirect.fdstore.promotion.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*' 
%><%@ page import='com.freshdirect.fdstore.attributes.*' 
%><%@ page import='java.net.URLEncoder'
%><%@ page import="com.freshdirect.fdstore.util.RatingUtil" 
%><%@ page import="com.freshdirect.fdstore.rollout.*" 
%><%@ taglib uri='template' prefix='tmpl' 
%><%@ taglib uri='logic' prefix='logic' 
%><%@ taglib uri='freshdirect' prefix='fd' 
%><%@ taglib uri='oscache' prefix='oscache' 
%>
<fd:CheckLoginStatus id = "user"/>
<fd:CheckDraftContextTag/>
<fd:PDPRedirector user="<%=user %>"/>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" oldToNewDirection="true" id="${param.catId}"/>

<%

//expanded page dimensions
final int W_CATEGORY_WITH_LEFT_NAV = 601;
final int W_CATEGORY_NO_LEFT_NAV = 765;
	
	boolean disableLinks = false;
	if (null !=request.getParameter("ppPreviewId")) {
		/* manipulate layout for preview mode */
		
		//disable linking
		disableLinks = true;
		if(request.getParameter("redirected")==null){			
			 StringBuffer redirBuf = new StringBuffer();
             //redirBuf.append("/site_access/site_access_lite.jsp?successPage="+request.getRequestURI());
             
             redirBuf.append("/about/index.jsp?siteAccessPage=aboutus&successPage=" +
                 request.getRequestURI());

             String requestQryString = request.getQueryString();

             if ((requestQryString != null) &&
                     (requestQryString.trim().length() > 0)) {
                 redirBuf.append(URLEncoder.encode("?" +
                         request.getQueryString()));
             }             
            redirBuf.append("&redirected=true");
			response.sendRedirect( redirBuf.toString());
			return;
		}
		}
	
	Set brands = null ; // set in the grocery_category_layout page. will be referenced by  i_bottom_template
	
	String catId = request.getParameter("catId");
	boolean isGroceryVirtual=false;
	boolean isWineLayout = false;
	String deptId = null;
	// it should be CategoryModel ... 
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId);
	final ProductContainer productContainer = (currentFolder instanceof ProductContainer) ? (ProductContainer) currentFolder : null;
	final CategoryModel categoryModel = (currentFolder instanceof CategoryModel) ? (CategoryModel) currentFolder : null;
	if (categoryModel != null) {
		deptId = (((CategoryModel)currentFolder).getDepartment() != null) ? ((CategoryModel)currentFolder).getDepartment().getContentName() : "";
	}
	
	ProductModel prodModel = ContentFactory.getInstance().getProductByName(request.getParameter("prodCatId"), request.getParameter("productId")); 
	
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", prodModel == null ? currentFolder.getPath() : prodModel.getPath());
	//generic ad spots for [APPDEV-2370]
	request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom,CategoryGen01,CategoryGen02,CategoryGen03,CategoryGen04,CategoryGen05");

	
	String title = "FreshDirect - " + currentFolder.getFullName();
	String seoMetaDescription = "";
	if(productContainer != null){
		title = productContainer.getPageTitle();	
		seoMetaDescription = productContainer.getSEOMetaDescription();
	} 
	if(categoryModel != null){
		title = categoryModel.getPageTitle();
		seoMetaDescription = categoryModel.getSEOMetaDescription();
	}
	
	
			
	boolean noLeftNav = false;
	String jspTemplate = null;
	boolean showAlternateContent = false;
	String alternateContentFile = null;
	if (request.getParameter("groceryVirtual")!=null ) {
	    isGroceryVirtual=true;
	}
	String hideUrl = currentFolder.getHideUrl();
	if (!ContentFactory.getInstance().getPreviewMode()) {
	    if (hideUrl!=null) {
	        String redirectURL = response.encodeRedirectURL(hideUrl);
		   if (redirectURL.toUpperCase().indexOf("/CATEGORY.JSP?")==-1) {
	           response.sendRedirect(redirectURL);
	           return;
		   }       
	    }
	}
	if( request.getParameter("ppPreviewId")==null &&( categoryModel.getProductPromotionType()==null ||!ContentFactory.getInstance().isEligibleForDDPP())){
	    String redirectURL = (currentFolder instanceof HasRedirectUrl ? ((HasRedirectUrl)currentFolder).getRedirectUrl() : null); 
		if (redirectURL!=null && !"nm".equalsIgnoreCase(redirectURL)  && !"".equals(redirectURL)
				) {
			redirectURL = response.encodeRedirectURL(redirectURL);
			if (redirectURL.toUpperCase().indexOf("/CATEGORY.JSP?")==-1) {
				response.sendRedirect(redirectURL);
		        return;
			}       
		}
	}
	boolean containsBeer = (categoryModel != null && categoryModel.isHavingBeer()); %>
	<fd:IsAlcoholic noProduct="true">
		<%
		if(containsBeer /*&& !yser.isHealthWarningAcknowledged()*/){
			String redirectURL = "/health_warning.jsp?successPage=/category.jsp"+URLEncoder.encode("?"+request.getQueryString());
			response.sendRedirect(response.encodeRedirectURL(redirectURL));
			return;
		}%>
	</fd:IsAlcoholic>
	<%
	
	if (productContainer!=null) {
	    noLeftNav = !productContainer.isShowSideNav();
	}
	Html alternateContent = categoryModel != null ? categoryModel.getAlternateContent() : null;
	if (alternateContent!=null) {
		showAlternateContent = true;
		alternateContentFile = alternateContent.getPath();
	}
	
	int templateType = productContainer != null ? productContainer.getTemplateType(1) : 1;

	int layoutType = productContainer != null ? productContainer.getLayoutType(-1) : -1;
	
	if ( noLeftNav && layoutType==EnumLayoutType.GROCERY_PRODUCT.getId() ) 
		noLeftNav= false;
	//need to change the noLeftNav setting, if this is virtual grocery or the coffee_by region layout or grocery_category
	//!!! Note: except for the virtual grocery folders, this should really be controlled by the show_Side_Nav attib, but
	//    due to the painfull manual process of editing all the necessary folders, we've opted, temporarily, for this hack
	if (!isGroceryVirtual && (layoutType==EnumLayoutType.COFFEE_BY_REGION.getId() || layoutType==EnumLayoutType.GROCERY_CATEGORY.getId() || layoutType==EnumLayoutType.THANKSGIVING_CATEGORY.getId())){
	    noLeftNav=true;
	} else if ( isGroceryVirtual ) 
		noLeftNav=false;
	
	//smart category for meat deals and edlp
	if(layoutType==EnumLayoutType.MEAT_DEALS.getId()){
		noLeftNav=true;
	}
	// [APPREQ-77] Page uses include media type layout
	boolean isIncludeMediaLayout = (layoutType == EnumLayoutType.MEDIA_INCLUDE.getId() || layoutType == EnumLayoutType.TEMPLATE_LAYOUT.getId()); // [APPREQ-77] || [APPDEV-2370]
                                                            
	// Assign the correct template
	if (isIncludeMediaLayout) {
		jspTemplate = noLeftNav ? "/common/template/top_nav_only.jsp" : "/common/template/left_dnav.jsp";
	} else if (noLeftNav) {
	    jspTemplate = "/common/template/right_dnav.jsp";
	} else {
	    if (EnumTemplateType.WINE.equals(EnumTemplateType.getTemplateType(templateType))) {
			// assuming only 1 wine store at a time
	        jspTemplate = "/common/template/usq_sidenav.jsp";
	    } else { //assuming the default (Generic) Template
			//unless it's data-driven
			if (layoutType == EnumLayoutType.PRESIDENTS_PICKS.getId() || EnumLayoutType.PRODUCTS_ASSORTMENTS.getId() == layoutType) {
				//css jawr optimizing test
				if (layoutType == EnumLayoutType.PRESIDENTS_PICKS.getId()) {
					if(EnumFeatureRolloutStrategy.NONE.equals(FeatureRolloutArbiter.getFeatureRolloutStrategy(EnumRolloutFeature.pplayout2014, user))) {
						jspTemplate = "/common/template/top_nav_only_optimized.jsp";
					} else {
						/* forward to new jsp, include won't work due to byte size on compilation */
						jspTemplate = "/common/template/blank.jsp";
						RequestDispatcher rd = request.getRequestDispatcher("/ddpp.jsp");
						rd.forward(request, response);
						return; //stop processing
					}
				} else {
					jspTemplate = "/common/template/top_nav_only.jsp";
				}
			} else {
				jspTemplate = "/common/template/both_dnav.jsp";
			}
	    }
	}	
	request.setAttribute("layoutType", layoutType); //make layoutType available in jspTemplate
	request.setAttribute("noLeftNav",noLeftNav);
	request.setAttribute("jspTemplate",jspTemplate);
	String folderFullName = currentFolder.getFullName().replaceAll("<[^>]*>", "");
	%><tmpl:insert template='<%=jspTemplate%>'><%
		if (!noLeftNav) {
			%><tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put><%
		} 
		%>
		<tmpl:put name="seoMetaTag" direct="true">
	    	<fd:SEOMetaTag  title="<%= title%>" metaDescription="<%=seoMetaDescription%>"/>
	    </tmpl:put>
	
		<%-- <tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put> --%>
		<tmpl:put name='facebookmeta' direct='true'>
			<meta property="og:title" content="FreshDirect - <%= folderFullName%>"/>
			<meta property="og:site_name" content="FreshDirect"/>
	
	<% if (prodModel!=null) {
	
		Image productImage = prodModel.getDetailImage();
		Image zoomImage = (Image) prodModel.getZoomImage();
	
		Object useProdImageObj = pageContext.getAttribute("useProdImage");
		boolean useProdImage = useProdImageObj == null ? false : (Boolean)useProdImageObj;
		boolean isWineProduct = prodModel.getDepartment() != null ? JspMethods.getWineAssociateId().toLowerCase().equals(prodModel.getDepartment().getContentKey().getId()) : false;
	
		if ( zoomImage != null && zoomImage.getPath().indexOf("clear.gif") == -1 && !useProdImage && !isWineProduct ) {
		%>
			<meta property="og:image" content="https://www.freshdirect.com<%= prodModel.getZoomImage().getPathWithPublishId() %>"/>
		<%
		} else {
		%>
			<meta property="og:image" content="https://www.freshdirect.com<%= prodModel.getDetailImage().getPathWithPublishId() %>"/>
		<%
		}
	}%>
			<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
			<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
			<meta property="og:image" content="https://www.freshdirect.com/media_stat/images/logos/FD-logo-300.jpg"/>
		</tmpl:put>
	
		<tmpl:put name='content' direct='true'>
			<fd:CmPageView wrapIntoScriptTag="true" currentFolder="<%=currentFolder%>"/>
			<%
			if(layoutType == EnumLayoutType.GROCERY_PRODUCT.getId()){
				%><fd:CmProductView quickbuy="false" wrapIntoScriptTag="true" productModel="<%=prodModel%>"/> <%
			}
		// TODO duplicated -- boolean virtualGrocerySpecified = request.getParameter("groceryVirtual")!=null;
		//if (layoutType==EnumLayoutType.FEATURED_ALL.getId()) layoutType=19;
		boolean noCache =  (EnumLayoutType.GROCERY_PRODUCT.getId()==layoutType
		                    || isGroceryVirtual /* virtualGrocerySpecified */
		                    || EnumLayoutType.BULK_MEAT_PRODUCT.getId()==layoutType
		                    || EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layoutType
		                    || EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layoutType
		                    || EnumLayoutType.THANKSGIVING_CATEGORY.getId()==layoutType
		                    || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layoutType
		                    || EnumLayoutType.HOLIDAY_MENU.getId()==layoutType
		                    || EnumLayoutType.WINE_CATEGORY.getId()==layoutType
		                    || EnumLayoutType.VALENTINES_CATEGORY.getId()==layoutType
		                    || EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()==layoutType);
		                    
		
		String keyPrefix="catLayout_";
		int ttl=300;
		if(deptId != null && ("fru".equals(deptId)||"veg".equals(deptId)||"orgnat".equals(deptId)||"local".equals(deptId)||"mea".equals(deptId))) {
		    if(user.isProduceRatingEnabled()) {
		        keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";
		        ttl=180;
		    }
		}
		
		int tablewid = noLeftNav ? W_CATEGORY_NO_LEFT_NAV : W_CATEGORY_WITH_LEFT_NAV;
		
		
		// Beginning of ifAlternateContent
		if( alternateContentFile != null ) {
			%><fd:IncludeMedia name='<%= alternateContentFile %>' /><%
		} else {
		
			Html introCopyAttribute = currentFolder.getEditorial();
			String introCopy = introCopyAttribute==null?"":introCopyAttribute.getPath();
		            
			String introTitle = currentFolder.getEditorialTitle();
		    
		    // no other option wine trouble
		    if ( EnumTemplateType.WINE.equals( EnumTemplateType.getTemplateType(templateType) ) ) {
		      introCopy="";
		      introTitle="";
			  isWineLayout=true;
		    }
		 	// remove intro from ddpp/ddpa
		    if ( layoutType == EnumLayoutType.PRESIDENTS_PICKS.getId() || layoutType == EnumLayoutType.PRODUCTS_ASSORTMENTS.getId()) {
		      introCopy="";
		      introTitle="";
		    }
		    
			boolean showLine = false;   // if true, the last gray line prior to the categories-display will be printed
				
				//  get the rating & ranking stuff
			    StringBuffer rateNRankLinks = new StringBuffer();
	
			    if ( !isIncludeMediaLayout 
			    		&& EnumLayoutType.BULK_MEAT_PRODUCT.getId() != layoutType
			            && EnumLayoutType.VALENTINES_CATEGORY.getId() != layoutType 
			            && EnumLayoutType.PARTY_PLATTER_CATEGORY.getId() != layoutType ) { // don't paint intro stuff if we'll be using bulkMeat layout
	
					if (productContainer != null) { rateNRankLinks.append(RatingUtil.buildCategoryRatingLink(productContainer, response)); }
	
					if ( !noCache 
						|| EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layoutType 
						|| EnumLayoutType.HOLIDAY_MENU.getId()==layoutType  
						|| EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layoutType 
						|| EnumLayoutType.WINE_CATEGORY.getId()==layoutType 
						|| EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()==layoutType 
						|| ( isWineLayout && EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layoutType ) ) {
						
						if (FDStoreProperties.isAdServerEnabled()) { %>
							<script type="text/javascript">
								OAS_AD('CategoryNote');
							</script> <%
						}
					} 				
	
					if ( !noCache 
						|| EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layoutType
					    || EnumLayoutType.HOLIDAY_MENU.getId()==layoutType  
					    || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layoutType ) { //not DFGS %>
	
						<%-- start header stuff --%>
						<table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0">
							 
							<% 
							if ( !"nm".equalsIgnoreCase(introTitle) && introTitle != null && introTitle.trim().length() > 0 
									&& EnumLayoutType.FOURMM_CATEGORY.getId() != layoutType ) {
								
								showLine=true; %>
								<tr><td align="center"><% 
									if ( !introTitle.equals("") ) { %>
										<font class="title16"><%=introTitle%></font>
									<% }
									String seasonText = (productContainer==null) ? null : productContainer.getSeasonText();
									if (seasonText != null ) { %>
										<br/><img src="/media_stat/images/layout/clear.gif" height="4" width="1"><br/>
										<font class="text12orbold"><%= seasonText %></font>
									<% } %>
								</td></tr>
							<% }
							
							if ( layoutType == EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId() ) {
								showLine=false;						
								%>
								<tr><td><img src="/media_stat/images/layout/clear.gif" height="5" width="1"></td></tr>
								<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></td></tr>
								<% 
							} 
	
							// bypass beer
							if ( introCopy != null 
									&& introCopy.trim().length() > 0 
									&& introCopy.indexOf("blank_file.txt") == -1 
									&& !( layoutType == EnumLayoutType.GROCERY_CATEGORY.getId() 
											&& currentFolder.getEditorial() != null 
											&& (currentFolder instanceof CategoryModel && ((CategoryModel)currentFolder).getCategoryLabel() != null )) 
									&& EnumLayoutType.FOURMM_CATEGORY.getId() != layoutType
								) { 
								
								if ( layoutType != EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId() ) 
									showLine=true;
								
								if ( introCopy != null && introCopy.trim().length() > 0 ) {
									%><tr><td align="center">
										<img src="/media_stat/images/layout/clear.gif" height="5" width="1"><br/>
										<fd:IncludeMedia name='<%= introCopy %>'/><br/>
										<img src="/media_stat/images/layout/clear.gif" height="4" width="1">
									</td></tr><%  
								}
							}
	
							if ( rateNRankLinks.length() > 0 ) {
								
								showLine=true; %>
								<tr><td><img src="/media_stat/images/layout/clear.gif" height="7" width="1"></td></tr>
								<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></td></tr>
								<tr><td><img src="/media_stat/images/layout/clear.gif" height="4" width="1"></td></tr>
								<tr><td align="center">
									<table cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td>
												<img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*">
												<img src="/media_stat/images/layout/clear.gif" height="1" width="6">
											</td>
											<td class="text11bold">Compare by:&nbsp;</td>
											<%=rateNRankLinks%>
											<td>
												<img src="/media_stat/images/layout/clear.gif" height="1" width="6">
												<img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*">
											</td>
										</tr>
									</table>
								</td></tr>
							<% }
	
							if ( showLine && layoutType != EnumLayoutType.HOLIDAY_MENU.getId() && layoutType != EnumLayoutType.FEATURED_MENU.getId() ) { %>
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
								<tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
								<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="5"></td></tr>
								
								<%
								List brandAttrib = (currentFolder instanceof CategoryModel) ? ((CategoryModel)currentFolder).getTopMedia() : null; 
								if (brandAttrib != null && brandAttrib.size() > 0) { %>
									<tr><td><fd:IncludeMedia name="<%= ( (Html)brandAttrib.get(0) ).getPath() %>" /></td></tr>
								<% } 
							} // end of if intro copy set to blank, do nothing
							
							//place the products in out temporary list..then assign it to the array itemsToDisplayArray
							%>
						</table>
						
						
						<% 
						//if ( rateNRankLinks.length() > 0 || ( "hmr".equals( currentFolder.getParentNode().toString() ) && currentFolder.getBlurb() == null ) ) {
						//null check
						if ( rateNRankLinks.length() > 0 || ( "hmr".equals( (currentFolder.getParentNode() != null) ? currentFolder.getParentNode().toString() : "" ) && currentFolder.getBlurb() == null ) ) { 
							%><br/><%
						}
						
					} else if ( EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId() == layoutType ) {
					 	//paint the category_detail_image, intro copy and full name
						MediaModel catDetailImg = categoryModel != null ? categoryModel.getCategoryDetailImage() : null;
	
						Html editorialAttribute = currentFolder.getEditorial();
						String catEditorialPath = editorialAttribute==null ? null : editorialAttribute.getPath();
						%>
						
						<table width="<%= tablewid %>" border="0" cellspacing="0" cellpadding="0">
									
							<tr><td align="center" colspan="3"><br/><font class="title18"><%=currentFolder.getFullName()%></font><br/><br/></td></tr>
							<tr valign="top">
								<td width="<%= catDetailImg.getWidth() + 2 %>">
									<%	if(catDetailImg!=null) { %>
										<img src="<%=catDetailImg.getPath()%>" width="<%=catDetailImg.getWidth()%>" height="<%=catDetailImg.getHeight()%>" border="0">
									<%  } else { %>
										<img src="/media_stat/images/layout/clear.gif" width="100" HEIGHT="1" border="0">
									<% } %>
								</td>
								<td><img src="/media_stat/images/layout/clear.gif" width="8" HEIGHT="1" border="0"></td>
								<td>
									<% if ( catEditorialPath != null ) { %>
										<fd:IncludeMedia name='<%= catEditorialPath %>'/>
									<% } else {	%>
										&nbsp;
									<% } %>
								</td>
							</tr>
						</table>
					<% 
					}
				} %>
						
				<%-- end header stuff --%>
	
				<% 
				//if ( "hmr".equals( currentFolder.getParentNode().toString() ) && currentFolder.getBlurb() != null ) { 
				//null check
				if ( "hmr".equals( (currentFolder.getParentNode() != null) ? currentFolder.getParentNode().toString() : "" ) && currentFolder.getBlurb() != null ) { 
				%>
					<table width="<%= tablewid %>" border="0" cellspacing="0" cellpadding="0">
						<tr><td>
							<%= currentFolder.getBlurb() %><FONT CLASS="space4pix"><br/><br/></FONT>
						</td></tr>
					</table>
					<br/><% 
				}
				
				%><%@ include file="/common/template/includes/catLayoutManager.jspf" %><%      
			} // else AlternateContent
			
			/* Layout may have put a request attribute called brandsList, of type set...get it into the brands var */
		  	if ( request.getAttribute("brandsList") != null ) {
				brands = (Set)request.getAttribute("brandsList");
		   	}
		   	if (
		   			EnumLayoutType.BULK_MEAT_PRODUCT.getId() != layoutType && 
		   			EnumLayoutType.VALENTINES_CATEGORY.getId() != layoutType && 
		   			EnumLayoutType.FOURMM_CATEGORY.getId() != layoutType &&
		   			EnumLayoutType.PRESIDENTS_PICKS.getId() != layoutType && EnumLayoutType.PRODUCTS_ASSORTMENTS.getId() != layoutType
		   		) { 
		   		%><%@ include file="/includes/i_bottom_template.jspf" %><%
			} %>
				
		</tmpl:put><%
			//@ include file="/includes/i_promotion_counter.jspf"
	%></tmpl:insert>