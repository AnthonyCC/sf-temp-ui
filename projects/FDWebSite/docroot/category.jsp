<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import="com.freshdirect.fdstore.util.RatingUtil"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<fd:CheckLoginStatus />

<%
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
	     deptId=((CategoryModel)currentFolder).getDepartment().getContentName();
	}
	
	
	ProductModel prodModel = ContentFactory.getInstance().getProductByName(request.getParameter("prodCatId"), request.getParameter("productId")); 
	
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", prodModel == null ? currentFolder.getPath() : prodModel.getPath());
	request.setAttribute("listPos", "LittleRandy,SystemMessage,CategoryNote,ProductNote,SideCartBottom");

	
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
	{
	    String redirectURL = (currentFolder instanceof HasRedirectUrl ? ((HasRedirectUrl)currentFolder).getRedirectUrl() : null); 
		if (redirectURL!=null && !"nm".equalsIgnoreCase(redirectURL)  && !"".equals(redirectURL)) {
			redirectURL = response.encodeRedirectURL(redirectURL);
			if (redirectURL.toUpperCase().indexOf("/CATEGORY.JSP?")==-1) {
				response.sendRedirect(redirectURL);
		        return;
			}       
		}
	}
	boolean containsBeer = (categoryModel != null && categoryModel.isHavingBeer()); 
	FDSessionUser yser = (FDSessionUser)session.getAttribute(SessionName.USER);
	if(containsBeer && !yser.isHealthWarningAcknowledged()){
		String redirectURL = "/health_warning.jsp?successPage=/category.jsp"+URLEncoder.encode("?"+request.getQueryString());
		response.sendRedirect(response.encodeRedirectURL(redirectURL));
		return;
	}
	
	if (productContainer!=null) {
	    noLeftNav = !productContainer.isShowSideNav();
	}
	Html alternateContent = categoryModel != null ? categoryModel.getAlternateContent() : null;
	if (alternateContent!=null) {
		showAlternateContent = true;
		alternateContentFile = alternateContent.getPath();
	}
	
	int templateType = productContainer != null ? productContainer.getTemplateType(1) : 1;

	int layouttype = productContainer != null ? productContainer.getLayoutType(-1) : -1;
	
	if ( noLeftNav && layouttype==EnumLayoutType.GROCERY_PRODUCT.getId() ) 
		noLeftNav= false;
	//need to change the noLeftNav setting, if this is virtual grocery or the coffee_by region layout or grocery_category
	//!!! Note: except for the virtual grocery folders, this should really be controlled by the show_Side_Nav attib, but
	//    due to the painfull manual process of editing all the necessary folders, we've opted, temporarily, for this hack
	if (!isGroceryVirtual && (layouttype==EnumLayoutType.COFFEE_BY_REGION.getId() || layouttype==EnumLayoutType.GROCERY_CATEGORY.getId() || layouttype==EnumLayoutType.THANKSGIVING_CATEGORY.getId())){
	    noLeftNav=true;
	} else if ( isGroceryVirtual ) 
		noLeftNav=false;
	
	//smart category for meat deals and edlp
	if(layouttype==EnumLayoutType.MEAT_DEALS.getId()){
		noLeftNav=true;
	}
	
	
	// [APPREQ-77] Page uses include media type layout
	boolean isIncludeMediaLayout = (layouttype == EnumLayoutType.MEDIA_INCLUDE.getId()); // [APPREQ-77]
	
	                                                                               
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
	        jspTemplate = "/common/template/both_dnav.jsp";
	    }
	}
%>
<tmpl:insert template='<%=jspTemplate%>'>
	
	<% if (!noLeftNav) { %>
		<tmpl:put name='leftnav' direct='true'> <%-- <<< some whitespace is needed here --%></tmpl:put>
	<% } %>
	
	<tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
		<%
		// TODO duplicated -- boolean virtualGrocerySpecified = request.getParameter("groceryVirtual")!=null;
		//if (layouttype==EnumLayoutType.FEATURED_ALL.getId()) layouttype=19;
		boolean noCache =  (EnumLayoutType.GROCERY_PRODUCT.getId()==layouttype
		                    || isGroceryVirtual /* virtualGrocerySpecified */
		                    || EnumLayoutType.BULK_MEAT_PRODUCT.getId()==layouttype
		                    || EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layouttype
		                    || EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layouttype
		                    || EnumLayoutType.THANKSGIVING_CATEGORY.getId()==layouttype
		                    || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layouttype
		                    || EnumLayoutType.HOLIDAY_MENU.getId()==layouttype
		                    || EnumLayoutType.WINE_CATEGORY.getId()==layouttype
		                    || EnumLayoutType.VALENTINES_CATEGORY.getId()==layouttype
		                    || EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()==layouttype);
		                    
		
		String keyPrefix="catLayout_";
		int ttl=300;
		FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
		if(deptId != null && ("fru".equals(deptId)||"veg".equals(deptId)||"orgnat".equals(deptId)||"local".equals(deptId)||"mea".equals(deptId))) {
		    if(user.isProduceRatingEnabled()) {
		        keyPrefix=keyPrefix+user.isProduceRatingEnabled()+"_";
		        ttl=180;
		    }
		}
		
		int tablewid = noLeftNav ? 550 : 425;
		
		
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
		    
			boolean showLine = false;   // if true, the last gray line prior to the categories-display will be printed
			
			//  get the rating & ranking stuff
		    StringBuffer rateNRankLinks = new StringBuffer();
		
		    if ( !isIncludeMediaLayout 
		    		&& EnumLayoutType.BULK_MEAT_PRODUCT.getId() != layouttype
		            && EnumLayoutType.VALENTINES_CATEGORY.getId() != layouttype 
		            && EnumLayoutType.PARTY_PLATTER_CATEGORY.getId() != layouttype ) { // don't paint intro stuff if we'll be using bulkMeat layout
		    	
		        rateNRankLinks.append(RatingUtil.buildCategoryRatingLink(productContainer, response));
		            
				if ( !noCache 
					|| EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layouttype 
					|| EnumLayoutType.HOLIDAY_MENU.getId()==layouttype  
					|| EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layouttype 
					|| EnumLayoutType.WINE_CATEGORY.getId()==layouttype 
					|| EnumLayoutType.TRANSAC_MULTI_PAIRED_ITEMS.getId()==layouttype 
					|| ( isWineLayout && EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId()==layouttype ) ) {
					
					if (FDStoreProperties.isAdServerEnabled()) { %>
						<script type="text/javascript">
							OAS_AD('CategoryNote');
						</script> <%
					}
				} 				
				 
				if ( !noCache 
					|| EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId()==layouttype
				    || EnumLayoutType.HOLIDAY_MENU.getId()==layouttype  
				    || EnumLayoutType.PARTY_PLATTER_CATEGORY.getId()==layouttype ) { //not DFGS %>
				    
					<%-- start header stuff --%>
					<table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0">
						 
						<% 
						if ( !"nm".equalsIgnoreCase(introTitle) && introTitle != null && introTitle.trim().length() > 0 
								&& EnumLayoutType.FOURMM_CATEGORY.getId() != layouttype ) {
							
							showLine=true; %>
							<tr><td align="center"><% 
								if ( !introTitle.equals("") ) { %>
									<font class="title16"><%=introTitle%></font>
								<% }
								String seasonText = productContainer.getSeasonText();
								if (seasonText != null ) { %>
									<br/><img src="/media_stat/images/layout/clear.gif" height="4" width="1"><br/>
									<font class="text12orbold"><%= seasonText %></font>
								<% } %>
							</td></tr>
						<% }
	
						
						if ( layouttype == EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId() ) {
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
								&& !( layouttype == EnumLayoutType.GROCERY_CATEGORY.getId() 
										&& currentFolder.getEditorial() != null 
										&& (currentFolder instanceof CategoryModel && ((CategoryModel)currentFolder).getCategoryLabel() != null )) 
								&& EnumLayoutType.FOURMM_CATEGORY.getId() != layouttype
							) { 
							
							if ( layouttype != EnumLayoutType.TRANSAC_MULTI_CATEGORY.getId() ) 
								showLine=true;
							
							if ( introCopy != null && introCopy.trim().length() > 0 ) {
								%><tr><td>
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
							<tr align="center"><td>						
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
						
						if ( showLine && layouttype != EnumLayoutType.HOLIDAY_MENU.getId() && layouttype != EnumLayoutType.FEATURED_MENU.getId() ) { %>
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
					if ( rateNRankLinks.length() > 0 || ( "hmr".equals( currentFolder.getParentNode().toString() ) && currentFolder.getBlurb() == null ) ) { 
						%><br/><%
					}
					
				} else if ( EnumLayoutType.TRANSAC_GROUPED_ITEMS.getId() == layouttype ) {
				 	//paint the category_detail_image, intro copy and full name
					MediaModel catDetailImg = categoryModel != null ? categoryModel.getCategoryDetailImage() : null;
					
				    
				    Html editorialAttribute = currentFolder.getEditorial();
					String catEditorialPath = editorialAttribute==null ? "" : editorialAttribute.getPath();
					        					    
					%>
					
					<table width="<%= tablewid %>" border="0" cellspacing="0" cellpadding="0">
								
						<tr><td align="center" colspan="3"><br/><FONT CLASS="title18"><%=currentFolder.getFullName()%></font><br/><br/></td></tr>
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
			if ( "hmr".equals( currentFolder.getParentNode().toString() ) && currentFolder.getBlurb() != null ) { %>
				<table width="<%= tablewid %>" border="0" cellspacing="0" cellpadding="0">
					<tr><td>
						<%= currentFolder.getBlurb() %><FONT CLASS="space4pix"><br/><br/></FONT>
					</td></tr>
				</table>
				<br/><% 
			}
			
			if ( EnumTemplateType.WINE.equals( EnumTemplateType.getTemplateType(templateType) ) ) { 
				%><%@ include file="/includes/wine/i_wine_category.jspf" %><%    
			} else {  
				%><%@ include file="/common/template/includes/catLayoutManager.jspf" %><%      
			}     
		} // else AlternateContent
		
		/* Layout may have put a request attribute called brandsList, of type set...get it into the brands var */
	  	if ( request.getAttribute("brandsList") != null ) {
			brands = (Set)request.getAttribute("brandsList");
	   	}
	   	if ( EnumLayoutType.BULK_MEAT_PRODUCT.getId() != layouttype && EnumLayoutType.VALENTINES_CATEGORY.getId() != layouttype && EnumLayoutType.FOURMM_CATEGORY.getId() != layouttype ) { 
	   		%><%@ include file="/includes/i_bottom_template.jspf" %><%
		} %>
			
	</tmpl:put>
	<%//@ include file="/includes/i_promotion_counter.jspf" %>
</tmpl:insert>
