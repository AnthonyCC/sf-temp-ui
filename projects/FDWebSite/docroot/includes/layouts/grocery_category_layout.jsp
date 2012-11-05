<%@ page import="com.freshdirect.webapp.taglib.fdstore.display.GetContentNodeWebIdTag"%>
<%@ page import="com.freshdirect.webapp.taglib.coremetrics.CmMarketingLinkUtil"%>
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
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='org.apache.log4j.Category' %>
<%@ page import='com.freshdirect.framework.util.log.LoggerFactory' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% //expanded page dimensions
final int W_GROCERY_CATEGORY_LAYOUT_TOTAL = 765;
final int W_GROCERY_CATEGORY_LAYOUT_LEFT = 396;
final int W_GROCERY_CATEGORY_LAYOUT_LEFT_COL = 191;
final int W_GROCERY_CATEGORY_LAYOUT_LEFT_PADDING = 14;
final int W_GROCERY_CATEGORY_LAYOUT_CENTER_PADDING = 14;
final int W_GROCERY_CATEGORY_LAYOUT_RIGHT_COL = 109;
final int W_GROCERY_CATEGORY_LAYOUT_RIGHT_PADDING = 14;
%>

<%!

static Category LOGGER = LoggerFactory.getInstance("grocery_category_layout");

public void produceBrandsAndTypes(Set brands, List typesList, Collection sortedColl) {
	boolean checkParent = false;
	ContentNodeModel lastNode = null;

	if (sortedColl == null || sortedColl.size() == 0)
		return;

	Iterator itr = sortedColl.iterator();
	while (itr.hasNext()) {
	    ContentNodeModel item = (ContentNodeModel)itr.next();

	    if (checkParent) {
	        checkParent = false;
	        //if the next item is not a product or not a folder that is a descend
	        if(!(item instanceof ProductModel) && !item.getParentNode().getContentName().equals(lastNode.getContentName()) ) {
	            typesList.remove(typesList.size()-1);
	        }
	    }

	    if (item instanceof CategoryModel) {
	        // check to see if there is any children of this category (prod or cat)
	        typesList.add(item);
	        checkParent = true;
	        lastNode = item;
	    } else {
	        List prodBrands = ((ProductModel)item).getBrands();
	        if (prodBrands!=null && prodBrands.size()>0) {
	            for (Iterator brandItr = prodBrands.iterator();brandItr.hasNext();) {
	                brands.add((BrandModel)brandItr.next());
	            }
	        }
	    }
	}	
}

%>
<%

//********** Start of Stuff to let JSPF's become JSP's **************
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
CategoryModel currentCategory = null;
if (deptId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(deptId);
    isDepartment = true;
} else if (catId != null) {
    currentFolder=ContentFactory.getInstance().getContentNode(catId);
    if (currentFolder != null)
        currentCategory = (CategoryModel) currentFolder;
}

//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");
if (trkCode!=null && !"".equals(trkCode.trim()) ) {
    trkCode = "&trk="+trkCode.trim();
} else {
    trkCode = "";
}

// DO render Editorial (if exists)
// [APPREQ-92] skip Editorial on Brand and on Virtual All pages
boolean doRenderEditorialPartial = (request.getParameter("brandValue") == null && !"All".equals(request.getParameter("groceryVirtual")));


Set brands = new TreeSet(ContentNodeModel.FULL_NAME_COMPARATOR);
List typesList = new ArrayList();

produceBrandsAndTypes(brands, typesList, (Collection) request.getAttribute("itemGrabberResult"));

int typesCount = typesList.size();


%>
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>" BORDER="0">
    <TR VALIGN="TOP">
        <TD WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>">
<%
if (currentCategory != null) {
    //
    // Category Label (Image)
    //
    if (currentCategory.getCategoryLabel() != null) {
        String categoryLabelPath = currentCategory.getCategoryLabel().getPath() /* ((Image)anAttib.getValue()).getPath() */;
%>
            <IMG SRC="<%= categoryLabelPath %>" border="0" ALT="<%= currentCategory.getFullName() %>"><br>
<%
    }
    
    //
    // Render Editorial (partial HTML)
    //
    Html editorialMedia = currentFolder.getEditorial();
    if (doRenderEditorialPartial &&
    		editorialMedia != null &&
    		currentCategory.getCategoryLabel() != null &&
    		!editorialMedia.isBlank()) {
%>
            <img src="/media_stat/images/layout/cccccc.gif" height="1" width="100%" vspace="5"><br>
            <fd:IncludeMedia name='<%= editorialMedia.getPath() %>'/><br>
<%
    }
}
%>
            <IMG SRC="media/images/layout/clear.gif" WIDTH="1" HEIGHT="8">
        </TD>
    </TR>
</TABLE>
<%


	//
	// Favorite Products
	//
	boolean hideFi = false;
	if (currentFolder instanceof CategoryModel)
		hideFi = ((CategoryModel) currentFolder).isHideFeaturedItems();
%>
<fd:ProductGroupRecommender siteFeature="FEATURED_ITEMS" id="recommendations" facility="cat_feat_items"  currentNode="<%= currentFolder %>" itemCount="6"
		hide="<%= hideFi %>"><%
	if (recommendations != null && recommendations.getProducts().size() > 0) {
		request.setAttribute("recommendationsRendered","true");
	
		List products = recommendations.getProducts();
		int ord=1;
%>
		<table cellpadding="0" cellspacing="0" width="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>" border="0">
			<tr>
				<td class="title14" colspan="<%= products.size()*2 %>"><%= recommendations.getVariant().getServiceConfig().getFILabel() %><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td>
			</tr>
			<tr valign="bottom">
<logic:iterate id='contentNode' collection="<%= products %>" type="com.freshdirect.fdstore.content.ProductModel"><%
		ProductModel productNode = contentNode;
		ProductLabeling pl = new ProductLabeling(user, productNode, recommendations.getVariant().getHideBursts());

	String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
%><%-- display a product --%>
		<td align="center" width="105" valign="bottom" class="smartstore-carousel-item-grocery-layout">
			<display:ProductImage product="<%=productNode%>" action="<%=actionURI%>" hideBursts="<%= recommendations.getVariant().getHideBursts() %>" webId="<%=GetContentNodeWebIdTag.getWebId(null, productNode, true, false)%>"/>
		</td>
		<td width="10">
			<img src="/media/images/layout/clear.gif" width="8" height="1">
		</td><%
			++ord;
		%></logic:iterate>
	</tr>
	<tr valign="top"><%
		ord=1;
	%><logic:iterate id='contentNode' collection="<%=products%>" type="com.freshdirect.fdstore.content.ProductModel"><%
		ProductModel productNode = contentNode;
			ProductLabeling pl = new ProductLabeling(user, productNode, recommendations.getVariant().getHideBursts());
			String fiProdPrice = null;
			String fiProdBasePrice=null;
			boolean fiIsDeal=false;	// is deal?
	%><fd:FDProductInfo id="productInfo" skuCode="<%=productNode.getDefaultSku().getSkuCode()%>"><%
		fiProdPrice = JspMethods.formatPrice(productInfo, user.getPricingContext());
			
			fiIsDeal=productInfo.getZonePriceInfo(user.getPricingContext().getZoneId()).isItemOnSale();
			if (fiIsDeal) {
				 fiProdBasePrice = JspMethods.formatSellingPrice(productInfo, user.getPricingContext());
			}
	%></fd:FDProductInfo><%
String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
String linkId = GetContentNodeWebIdTag.getWebId(null, productNode, true, false) + "_product_name";
%><%-- display a product --%>
		<td align="center" WIDTH="105">
			<display:ProductRating product="<%= productNode %>"/>
<%		if (productNode.isFullyAvailable()) { %>
			<div><display:ProductName id="<%=linkId%>" product="<%= productNode %>" action="<%= CmMarketingLinkUtil.getSmartStoreLink(actionURI,recommendations) %>"/></div>
<%		} else { %>
			<div style="color: #999999;"><display:ProductName id="<%=linkId%>"  product="<%= productNode %>" action="<%= CmMarketingLinkUtil.getSmartStoreLink(actionURI,recommendations) %>" style="color: #999999;"/></div>
<%		}
		if (fiIsDeal) {
%>			<div style="FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #c00"><%= fiProdPrice %></div>
			<div style="FONT-SIZE: 7pt; COLOR: #888">(was <%= fiProdBasePrice %>)</div>
<%		} else {
%>			<div class="favoritePrice"><%= fiProdPrice %></div>
<%		} %>
		</td>
		<td width="10">
			<img src="/media/images/layout/clear.gif" width="8" height="1">
		</td><%
		++ord;
%></logic:iterate>
	</tr>
</table>
<%
	}
%>
</fd:ProductGroupRecommender>
<font class="space4pix"><BR></font>
<%



	int typeSpan = (currentCategory != null ? currentCategory.getColumnSpan(2) : 2);
	int brandSpan = 5 - typeSpan;

%>
<BR><BR>
    
<IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>" HEIGHT="1"><BR>
<FONT CLASS="space4pix"><BR></FONT>
<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>">
<tr VALIGN="TOP">
    <td WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_TOTAL%>" colspan="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="5"></td>
</tr>
<TR VALIGN="TOP">
<!--     <TD WIDTH="27"><BR></TD> -->
    <TD ALIGN="CENTER" width="<%=W_GROCERY_CATEGORY_LAYOUT_LEFT%>">
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td colspan="<%=typeSpan+(typeSpan-1)%>">
                <IMG SRC="/media/images/navigation/department/grocery/gro_choose_type.gif" WIDTH="107" HEIGHT="10" ALT="CHOOSE A TYPE"><BR>
                <IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" >
                </td>
            </tr>
            <tr VALIGN="TOP"><%
    //
    // "CHOOSE A TYPE" COLUMNS
    //
                

    int[] columnRows = new int[4];
    boolean makeFakeAllLink = (currentCategory != null ? currentCategory.getFakeAllFolders() : false);
    if (typesCount > 1 && makeFakeAllLink) {
         typesCount++;
    }

    int visitingParentCount = 0;    
    int currentRow = 0;

%>
                <TD WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_LEFT_COL%>"><%
    
    if (makeFakeAllLink) {
    	%><div style="margin-left: 8px; text-indent: -8px;">
    	   <a href="/category.jsp?catId=<%= currentFolder.getContentName() %>&groceryVirtual=All<%= trkCode %>"><b>All <%= currentFolder.getFullName() %></b></a>
    	   <br/>
        </div>
<%
        currentRow = 1;
        visitingParentCount++;
    }

    int currentColumn = 0;

    int numInColsDesired = 0;
    for(int h=0; h<typeSpan;h++){
        numInColsDesired += columnRows[h];
    }

    int typeProdsCount = 0;
    CategoryModel type = null;
    //skip through the product
    typeProdsCount = 0;

    boolean unlink =  false;
    boolean indent =  false;
    boolean canBreak = true;
    int skipIdx = 0;
    int typesShown=0;
    CategoryModel lastFolder = null;
    int[] breakPoints = new int[typeSpan];
    int breakPoint = 0;
    for (int i = 0; i<typeSpan;i++) {
        breakPoints[i]=typesCount/typeSpan;
    }

    //adjust numbers if not evenly divisible by the typeSpan
    for (int i = 0; i<(typesCount % typeSpan);i++) {
        breakPoints[i]+=1;
    }
    

    //assign first breakpoint
    breakPoint = breakPoints[currentColumn];
    for (int idx = 0; idx < typesList.size();idx++) {
        ContentNodeModel contentItem = (ContentNodeModel) typesList.get(idx);
        type = (CategoryModel) contentItem;
        typesShown++;
        typeProdsCount = 0;
        unlink =  false;
        indent =  false;
        canBreak = false;
        currentRow++;

        if (type.getParentNode() != null && lastFolder != null &&
        	type.getParentNode().getContentName().equals(lastFolder.getContentName())) {
            indent = true;
        } else if (currentRow > breakPoint) {
            canBreak = true;
        }

        if (canBreak) {
        	%></TD>
        	<TD WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_LEFT_PADDING%>"><IMG SRC="media/images/layout/clear.gif" HEIGHT="1" WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_LEFT_PADDING%>"></TD>
<%
            if (idx != typesCount) {
            	%>
            	<TD WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_LEFT_COL%>">
<%
            }
            currentRow = 0;
	     try {
		breakPoint = breakPoints[++currentColumn];
	     }catch(Exception e){break;}
	     
	     
        }


        // display type entry
        if (indent) {
        	if (unlink) {
        		// indented text
        		%><div style="margin-left: 12px; text-indent: -12px;">&nbsp;&nbsp;<%= type.getFullName() %><br/>
        		</div>
<%
        	} else {
        		// indented link
                %><div style="margin-left: 12px; text-indent: -12px;">&nbsp;&nbsp;<a href="/category.jsp?catId=<%= type.getContentName() %>&trk=cpage"><%= type.getFullName() %></a><br/>
                </div>
<%
        	}
        } else {
            if (unlink) {
                // unindented text
                %><div style="margin-left: 8px; text-indent: -8px;"><b><%= type.getFullName() %></b><br/>
                </div>
<%
            } else {
                // unindented link
                %><div style="margin-left: 8px; text-indent: -8px;"><a href="/category.jsp?catId=<%= type.getContentName() %>&trk=cpage"><b><%= type.getFullName() %></b></a><br/>
                </div>
<%
            }
        }



        if (lastFolder==null ||
        		(type.getParentNode() != null && lastFolder != null && !type.getParentNode().getContentName().equals(lastFolder.getContentName()) )) {
            lastFolder = type;
        }
    }
    // if we dont use the other col, when the span is > 1 then assign the rest to the brand span
    if (currentColumn<(typeSpan-1)) {
        brandSpan += (typeSpan-1)-currentColumn;
    }
%>
	       </TD></tr>
    	</table>
    </TD>
    <td bgcolor="#cccccc" width="1"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" /></TD>
    <td width="<%=W_GROCERY_CATEGORY_LAYOUT_CENTER_PADDING-1%>"><BR></TD>
    <td width="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_COL*brandSpan+((brandSpan-1)*W_GROCERY_CATEGORY_LAYOUT_RIGHT_PADDING)%>">
		<%-- Everything inside this td is the brand section --%>
		<%
			//APPDEV-1308

			boolean useBrandMedia = false;
			String brandIds = FDStoreProperties.getBrandMediaIds();
			ContentNodeModel curTest;
			List mediaBottom = null;

			if ( !("NONE").equalsIgnoreCase(brandIds) ) {
				if ( !("ALL").equalsIgnoreCase(brandIds) ) {
					//check for specific ids
					String[] brandIdsArray = brandIds.split(",");
					
					for (int i=0; i<brandIdsArray.length; i++) {
						if ( (brandIdsArray[i]).equalsIgnoreCase(catId) ) {
							//we have a match
							useBrandMedia = true;
							LOGGER.debug("currentCategory("+currentCategory+") is in brandIds");
						}else{
							//no match, see if it's a department
							curTest = ContentFactory.getInstance().getContentNode(brandIdsArray[i]);
							if (curTest instanceof DepartmentModel) {
								//it is, see if our current cat is a child of it
								if ((currentCategory.getDepartment().toString()).equalsIgnoreCase(brandIdsArray[i])) {
									//it is, match
									useBrandMedia = true;
									LOGGER.debug("currentCategory("+currentCategory+") is a child of a deptId("+brandIdsArray[i]+") in brandIds");
								}
							}
						}
						if (useBrandMedia) { break; } //break out once we have a match
					}
				}else{
					//any id is ok
					useBrandMedia = true;
				}
				//now check that we HAVE media to use
				mediaBottom	= (currentFolder instanceof CategoryModel) ? ((CategoryModel)currentFolder).getBottomMedia() : null;
			}

			LOGGER.debug("useBrandMedia: "+useBrandMedia);
			LOGGER.debug("brandIds: "+brandIds);

			//if we have a bottom media, and we want to use it, include it here
			if (useBrandMedia && mediaBottom != null && mediaBottom.size() > 0) {
				for (int i=0; i < mediaBottom.size(); i++) {
					if ( ((Html)mediaBottom.get(i)).getPath() != null ) {
						LOGGER.debug("((Html)mediaBottom.get("+i+")).getPath(): "+((Html)mediaBottom.get(i)).getPath());
						%><fd:IncludeMedia name='<%= ((Html)mediaBottom.get(i)).getPath() %>' /><%
					}
				}
				//create a js object to be available to the media that contains the brands
				%>
					<script type="text/javascript">
					<!--
						var _page_brands = new Object({
							avail: []
						});

						<% for (Iterator i = brands.iterator(); i.hasNext(); ) {
							BrandModel brand = (BrandModel) i.next();
						%>
							_page_brands.avail[_page_brands.avail.length] = new Object({id: "<%=brand.getContentName()%>", name: "<%=brand.getFullName()%>"});
						<% } %>
						
					//-->
					</script>
				<%
			}else{
				LOGGER.debug("mediaBottom is empty.");
			}
		%>
		<% if (!useBrandMedia) { %>
			<%-- START brand section --%>
				<table WIDTH="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_COL*brandSpan+((brandSpan-1)*W_GROCERY_CATEGORY_LAYOUT_RIGHT_PADDING)%>" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<td colspan="<%=(2*brandSpan)+1%>">
							<img src="/media/images/navigation/department/grocery/gro_choose_brand.gif" width="123" height="10" alt="CHOOSE A BRAND" border="0" /><br />
							<img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" />
						</td>
					</tr>
					<tr valign="top">
						<td width="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_COL%>"><%
						//
						// "CHOOSE A BRAND" COLUMN
						//
				
						columnRows = new int[4];
						int availableBrands = brands.size();
						int brandSize = availableBrands;
						int brandsRows = availableBrands / 3;
						if (availableBrands % 3 != 0) brandsRows++;
						columnRows[0] = brandsRows;

						availableBrands -= brandsRows;
						brandsRows = availableBrands / 2;
						if (availableBrands % 2 != 0) brandsRows++;
						columnRows[1] = brandsRows;
						columnRows[2] = availableBrands - brandsRows;

						currentColumn = 0;
						currentRow = 0;
						brandsRows = columnRows[0];

						numInColsDesired = 0;

						for(int h=0; h<brandSpan;h++){
							numInColsDesired += columnRows[h];
						}

						if (numInColsDesired != brandSize){
							int remainder = brandSize - numInColsDesired;
							switch (brandSpan){
								case 1:
									brandsRows = brandSize;
									break;
								case 2:
									int remainMod2 = remainder % 2;
									int remainDiv2 = remainder / 2;

									if(remainMod2 !=0){
										brandsRows = columnRows[0] + (remainder - remainDiv2);
										columnRows[1] += remainDiv2;
									}
									else{
										brandsRows = columnRows[0] + remainDiv2;
										columnRows[1] += remainDiv2;
									}
									break;
							}
						}
						StringBuffer brandLink = new StringBuffer();
						for (Iterator i = brands.iterator(); i.hasNext(); ) {
							BrandModel brand = (BrandModel) i.next();
							%>
								<div style="margin-left: 8px; text-indent: -8px;">
									<a href="/category.jsp?catId=<%= currentFolder.getContentName() %>&brandValue=<%= brand.getContentName() %>&groceryVirtual=<%= currentFolder.getContentName() %>&trk=cpage"><%= brand.getFullName() %></a>
									<br/>
								</div>
							<%
							if (++currentRow == brandsRows) {
								%></td>
								<% if (i.hasNext()) { %>
									<td width="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_PADDING%>"><img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_PADDING%>" alt="" /></td>
									<td width="<%=W_GROCERY_CATEGORY_LAYOUT_RIGHT_COL%>"><%
								}

								currentRow = 0;
								brandsRows = columnRows[++currentColumn];
							}
						}
						%>
						</td>
						<%
							if (brands.size() > 0 ) {
								request.setAttribute("brandsList",brands);
							}
						%>
					</tr>
				</table>
			<%-- END brand section --%>
		<% } %>
	</td>
</tr>
<tr>
	<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" /></td>
</tr>
</table>
<fd:javascript src="/assets/javascript/fd/modules/coremetrics/carousel_grocery_layout.js"/>
