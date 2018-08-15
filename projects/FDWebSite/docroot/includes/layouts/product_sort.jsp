<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.storeapi.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>


<% //expanded page dimensions
final int W_PRODUCT_SORT_TOTAL = 601;
final int W_PRODUCT_SORT_IMG = 30;
final int W_PRODUCT_SORT_NAME = 191;
final int W_PRODUCT_SORT_PRICE = 90;
%>

<%

//********** Start of Stuff to let JSPF's become JSP's **************
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId"); 
boolean isDepartment = false;

ContentNodeModel currentFolder = null;
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNode(deptId);
	isDepartment = true;
} else {
	currentFolder=ContentFactory.getInstance().getContentNode(catId);
}
final ProductContainer productContainer = (currentFolder instanceof ProductContainer) ? (ProductContainer) currentFolder : null; 

boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");

Collection sortedStuff = (Collection) request.getAttribute("itemGrabberResult");
if (sortedStuff==null) sortedStuff = new ArrayList();

    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
	CategoryModel lastProdFolder = null;
	ProductModel lastProduct = null;
	CategoryModel workFolder = null;
        List ratingAttribs = null;
	String url = null;
	String colLabel = null;
	String colRatingName = null;
        String plusChar = null;
	String minusChar = null;
        int attribCount = 0;
        boolean isBooleanDomain = false;
        boolean isNumeric = true;
	//com.bluemartini.htmlapp.Collection attributeCollection = currentFolder.getCollection("ATR_attr_display_list");
        boolean breakOnSubfolder = productContainer.isRatingBreakOnSubfolders();


        ratingAttribs = productContainer.getRating();

	int atrDisplayCount = 2;
	if (ratingAttribs !=null) {
		atrDisplayCount += ratingAttribs.size();
	}
	String orderBy = request.getParameter("orderBy");
        if (orderBy==null) {
           // get the rating attibute collection and get the first rating thing off of it
            if (ratingAttribs.size() > 0) {
                Domain raDMV = (Domain) ratingAttribs.get(0);
                orderBy = raDMV.getName().toLowerCase();
            }else orderBy="name";
        }

%>
<font class="space4pix"><BR></font>
<%
    int tdwidth=96;
    int nameColWidth=0;
//     int remainColWidth = 0;
    boolean showRelatedRatingImage = false;
    Comparator priceComp = new ProductModel.PriceComparator();
    showRelatedRatingImage = productContainer.isShowRatingRelatedImage();

	boolean hideFi = false;
	if (currentFolder instanceof CategoryModel)
		hideFi = ((CategoryModel) currentFolder).isHideFeaturedItems();    
%><fd:ProductGroupRecommender siteFeature="FEATURED_ITEMS" id="recommendations" facility="cat_feat_items"  currentNode="<%= currentFolder %>" itemCount="5"	hide="<%= hideFi %>"><%
		if (recommendations != null && recommendations.getProducts().size() > 0) {
				
				request.setAttribute("recommendationsRendered","true");
				List products = recommendations.getProducts();
				int ord = 1;
		%>

		<table cellspacing="0" cellpadding="1" border="0" width="<%= W_PRODUCT_SORT_TOTAL %>">
		
			<tr valign="top">
	    		<td CLASS="text12bold" width="<%= W_PRODUCT_SORT_TOTAL %>" colspan="<%= W_PRODUCT_SORT_TOTAL %>">
	    			<%= recommendations.getVariant().getServiceConfig().getFILabel() %> <BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="1" HEIGHT="3">
	    		</td>
			</tr>
		</table>
		<font class="space4pix"><BR></font>
		<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%= W_PRODUCT_SORT_TOTAL %>">
			<TR VALIGN="TOP" ALIGN="CENTER">
				<logic:iterate id='contentNode' collection="<%= recommendations.getProducts() %>" type="com.freshdirect.storeapi.content.ProductModel"><%
						
						ProductModel productNode = contentNode;
						ProductImpression pi = new ProductImpression(productNode);
						ProductLabeling pl = new ProductLabeling(user, productNode, recommendations.getVariant().getHideBursts());
						FDCustomerCoupon curCoupon = null;
						if ( pi.getSku() != null && pi.getSku().getProductInfo() != null ) {
							curCoupon = user.getCustomerCoupon(pi.getSku().getProductInfo(), EnumCouponContext.PRODUCT,productNode.getParentId(),productNode.getContentName());
						}
						
						String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
						
						%>	<%-- display a product --%>
					<td width="<%= tdwidth %>">
						<span class="smartstore-carousel-item">
							<p style="border: 0px; padding: 0px; margin: 0px;">
								<display:ProductImage product="<%= productNode %>" action="<%= actionURI %>" hideBursts="<%= recommendations.getVariant().getHideBursts() %>" coupon="<%= curCoupon %>" /></p>
								<display:ProductRating product="<%= productNode %>" />
				<%			// product name
						if (productNode.isFullyAvailable()) { %>
							<div><display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/></div>
				<%		} else { %>
							<div style="color: #999999"><display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/></div>
				<%		} %>
							<div class="favoritePrice">	<display:ProductPrice impression="<%= pi %>" showDescription="false"/></div>
							<display:FDCoupon coupon="<%= curCoupon %>" contClass="fdCoupon_layProdSort"></display:FDCoupon>
						</span>
					</td>
					
			<%if (ord < recommendations.getProducts().size()) {%>		
				<td width="10">
					<img src="/media_stat/images/layout/clear.gif" alt="" width="8" height="1">
			   </td>
			<%} %>
			<%++ord;%>
			</logic:iterate>
	</tr>
</table>
<font class="space4pix"><BR></font>
<%
	}
%></fd:ProductGroupRecommender><%
// idealy the following td cells should be created afer we know the location of the full_name column, but..
%>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_PRODUCT_SORT_TOTAL%>">
	<TR VALIGN="TOP">
		<TD WIDTH="<%= W_PRODUCT_SORT_TOTAL %>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%= W_PRODUCT_SORT_TOTAL %>" HEIGHT="5"></TD></TR>
		<TR VALIGN="TOP"><TD WIDTH="<%= W_PRODUCT_SORT_TOTAL %>" CLASS="text9"><FONT CLASS="text10bold">Compare <%= currentFolder.getFullName() %> by: Taste &amp; Price</FONT></TD></TR>
		<TR VALIGN="TOP"><TD WIDTH="<%= W_PRODUCT_SORT_TOTAL %>"><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="10" HEIGHT="3"></TD></TR> <TR VALIGN="TOP">
		<TD WIDTH="<%= W_PRODUCT_SORT_TOTAL %>" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/cccccc.gif" ALT="" WIDTH="10" HEIGHT="1"></TD>
	</TR>
</TABLE>

<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="<%= W_PRODUCT_SORT_TOTAL %>" style="border-spacing: 1px;">
	<TR VALIGN="BOTTOM">
<%
// get the display labels from the category's "rating" attribute, price is the only hard coded column.  Also we assume that the name is there
// and that it will be the first column (or the longest)
//                 remainColWidth=0;
                if(atrDisplayCount == 2 ) {
                        nameColWidth = W_PRODUCT_SORT_TOTAL-W_PRODUCT_SORT_IMG-W_PRODUCT_SORT_PRICE;
                }
                else {
                        nameColWidth = W_PRODUCT_SORT_NAME;
//                         remainColWidth = (W_PRODUCT_SORT_TOTAL-(nameColWidth+W_PRODUCT_SORT_PRICE))/(atrDisplayCount-2);
                }
%>		<TD colspan="2" bgcolor="#eeeeee" <%--WIDTH="<%= nameColWidth+W_PRODUCT_SORT_IMG %>" --%> CLASS="text10bold"><img border="0" src="/media_stat/images/layout/clear.gif" alt="" width="3">
<%
        if(orderBy.equalsIgnoreCase("variety")){
%>
Variety
<%	}else{ %>
<A HREF="<%=response.encodeURL("/category.jsp?catId=" + currentFolder + "&orderBy=name")%>">Variety</A>
<%      }%></TD>
<%
boolean reverseOrderBy = false;
List sortStrategy = new ArrayList();
sortStrategy.add(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY));
for (Iterator aItr=ratingAttribs.iterator();aItr.hasNext();) {
        Domain ratingAttrib = (Domain) aItr.next();
        List domainValues = ratingAttrib.getDomainValues();
        colLabel="";
        minusChar="-&nbsp;";
        plusChar="&nbsp;+";

        colLabel=ratingAttrib.getLabel().toLowerCase();
        colRatingName = ratingAttrib.getName();
        colLabel=colLabel.substring(0,1).toUpperCase()+colLabel.substring(1);
        // we will assume the following: 2 domain Values is  a True/False Domain  anything else is is numeric
        // if the rating type is not numeric then clear the minus & plus chartacters accordingly
        isBooleanDomain = false;
        isNumeric = false;
        if(domainValues.size() > 0) {
            String strValue = (String)((DomainValue)domainValues.get(0)).getValue();
            if ("true".equalsIgnoreCase(strValue) || "false".equalsIgnoreCase(strValue)) {
                isBooleanDomain = true;
            } else {
                try {
                    Integer integer = new Integer(strValue);
                    isNumeric=true;
                }
                catch (NumberFormatException nfe) {
                    isNumeric = false;
                }
            }
        }
        if (isBooleanDomain || !isNumeric ) {
            minusChar = "";
            plusChar = "";
        }
        attribCount++;

 %>
		<TD bgcolor="#eeeeee" <%-- WIDTH="<%=remainColWidth%>" --%> CLASS="text10bold" ALIGN="CENTER">
<%
	//should this column label be linked?.  if currently ordered by it, then No.
        if(orderBy.equalsIgnoreCase(colRatingName)){
            if (isBooleanDomain || isNumeric) reverseOrderBy=true;
             sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_DOMAIN_RATING,reverseOrderBy,orderBy));
%>
<%=minusChar%><%=colLabel%><%=plusChar%>
<%	}else{ %>
<%=minusChar%><A HREF="<%=response.encodeURL("/category.jsp?catId=" + currentFolder + "&orderBy="+colRatingName)%>"><%=colLabel%></A><%=plusChar%>
<%      }%></TD>
<%} // close loop
//Now add the price column
%>		<TD bgcolor="#eeeeee" WIDTH="<%=W_PRODUCT_SORT_PRICE%>" CLASS="text10bold" ALIGN="CENTER">
<%
if(orderBy.equalsIgnoreCase("Price")){%>Price&nbsp;
<%
  sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE));
}else{
%><A HREF="<%=response.encodeURL("/category.jsp?catId=" + currentFolder + "&orderBy=price")%>">Price</a>&nbsp;
<%}%>
		</TD>
	</TR><%// go get the products

  sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME, false));
%>
<fd:ItemSorter nodes='<%= (List)sortedStuff %>' strategy='<%=sortStrategy%>'/>
<%
//List sortedStuff = JspMethods.sorter(sortedColl,orderBy,reverseOrderBy,breakOnSubfolder);
workFolder = (CategoryModel)currentFolder;  // assign the current folder to the working forlder.. will be null in loop when no subfolders remain

int prodCounter = 0;
boolean shownFolderLabel = true;
CategoryModel lastFolder = workFolder;
for(Iterator itmItr = sortedStuff.iterator();itmItr.hasNext();) {
    ContentNodeModel itemToDisplay = (ContentNodeModel)itmItr.next();
    if (itemToDisplay instanceof CategoryModel) continue;
   // must be a product..
    ProductModel prod = (ProductModel)itemToDisplay;
    if (theOnlyProduct!=null) {
        onlyOneProduct=false;
    }else {
        onlyOneProduct=true;
        theOnlyProduct = prod;
    }

   workFolder = (CategoryModel)prod.getParentNode();
    // is the folder displayable or a decaf folder ?
    if (lastFolder==null || !workFolder.getContentKey().equals(lastFolder.getContentKey())) {
        shownFolderLabel = false;
    }

    if ((workFolder.getShowSelf()==true 
        || workFolder.getFullName().toLowerCase().indexOf("decaf")!=-1 )&& shownFolderLabel==false ) {
         shownFolderLabel=true;
         lastFolder = workFolder;
%>
	<TR VALIGN="MIDDLE"><TD colspan="5" WIDTH="<%= W_PRODUCT_SORT_TOTAL %>" CLASS="text10bold" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" alt="" width="3" border="0"><%=workFolder.getFullName()%>:</td>
	</tr>
<%
    }

   // get the price for this thing
        List skus = prod.getSkus(); 
        SkuModel sku = null;
        String prodPrice = null;
        if (skus.size()==0) continue;  // skip this item..it has no skus.  Hmmm?
        if (skus.size()==1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        }
        else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }
%>
        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 
        prodPrice = JspMethods.formatPrice(productInfo, user.getPricingContext());
%>						
        </fd:FDProductInfo>
<%
        String bgcolor = "#ffffff";
        if(prodCounter%2 != 0){
                bgcolor = "#eeeeee";
        }else{
                bgcolor = "#ffffff";
        }
        prodCounter++;

%><TR VALIGN="MIDDLE" BGCOLOR="<%=bgcolor%>">
<%
		Image ratingImage = prod.getRatingRelatedImage();
        String itmImage = "";
        if (ratingImage !=null && showRelatedRatingImage) {
                StringBuffer flagTag = new StringBuffer();
                flagTag.append("<img src=\"");
                flagTag.append(ratingImage.getPath());
                flagTag.append("\" width=\""+W_PRODUCT_SORT_IMG+"\" height=\"20\" border=\"0\" alt=\"");
                flagTag.append(prod.getFullName());
                flagTag.append("\" ALIGN=\"left\" HSPACE=\"5\">");
                itmImage = flagTag.toString();
        }

        url = response.encodeURL("product.jsp?productId=" + prod + "&catId=" + workFolder+"&trk=rate");

        String nameColSpan="";        
        if(itmImage.length()>0){

%><td align="left" width="<%=W_PRODUCT_SORT_IMG%>"><%=itmImage%></td><%

        } else {
	        	nameColSpan="colspan=\"2\"";
        }

if (prod.isUnavailable()) {
            prodPrice = "Not Available";
%><TD <%=nameColSpan%> WIDTH="<%=nameColWidth %>"><font color="#999999"><display:ProductName product="<%= prod %>" action="<%= url %>" showNew="true" showFavourite="true"/></font></TD>
<%      } else { %><TD <%=nameColSpan%> WIDTH="<%= nameColWidth %>"><display:ProductName product="<%= prod %>" action="<%= url %>" showNew="true" showFavourite="true"/></TD>
<%      }
    //We've got a products. Now locate each display attribute on the product
        int colCounter = 2;
        for (Iterator aItr=ratingAttribs.iterator();aItr.hasNext();) {
            Domain ratingAttrib = (Domain) aItr.next();
            List domainValues = ratingAttrib.getDomainValues();
            List prodDomainValues = prod.getRating();
            String prodDomainValue = null;

            
            boolean foundDomain = false;
           // get the matching domainvalue off the prod for this Domain.
            isBooleanDomain = false;
            isNumeric = false;

            prodDomainValue = HowToCookItUtil.getProductDomainValue(prodDomainValues, ratingAttrib.getContentKey().getId(), null);
            foundDomain = prodDomainValue != null;
            if(foundDomain) {
                if ("true".equalsIgnoreCase(prodDomainValue) || "false".equalsIgnoreCase(prodDomainValue)) {
                    isBooleanDomain = true;
                } else {
                    try {
                        Integer integer = new Integer(prodDomainValue);
                        isNumeric=true;
                    }
                    catch (NumberFormatException nfe) {
                        isNumeric = false;
                    }
                }
            }

            String colAttrName = ratingAttrib.getName();
            String cellColor = bgcolor;

            // get the imgae that should be displayed to right of this thing

            if (orderBy.equalsIgnoreCase(colAttrName)) {
                cellColor = "#dddddd";
            }
%><TD <%--WIDTH="<%=remainColWidth%>"--%> ALIGN="CENTER" bgcolor="<%=cellColor%>"><%
            if (!foundDomain) { //show a hyphen in the cell, since this attribute Value is not on this product%>
&nbsp;&#8212;&nbsp</td>
<%
                continue; //skip this product.
            }
            String atrImgSrc = null;
            if(!isNumeric  && !isBooleanDomain){
%>
<%=prodDomainValue%></td>
<%
           } else if(isNumeric){
                    atrImgSrc = "/media_stat/images/template/rating3_"+cellColor.substring(1,cellColor.length())+"_05_0"+prodDomainValue+".gif";
%><img src="<%=atrImgSrc%>"></td>
<%
            } else if(isBooleanDomain) {
               if ("true".equalsIgnoreCase(prodDomainValue)){ 
%><img src="/media_stat/images/layout/orangedot.gif"></td>
<%             } else { %>&nbsp;</td>
<%             } 
          }
          colCounter++;
	}// end of for loop on attrb-display list
	if(orderBy.equalsIgnoreCase("Price")){%>
<TD WIDTH="<%=W_PRODUCT_SORT_PRICE%>" ALIGN="CENTER" bgcolor="#dddddd">
<%      }else{%>
<TD WIDTH="<%=W_PRODUCT_SORT_PRICE%>" ALIGN="CENTER" bgcolor="<%=bgcolor%>">
<%      }%>
	<div><display:ProductPrice impression="<%= new ProductImpression(prod) %>" showDescription="false" tableContent="true"/></div>&nbsp;</TD></TR>
<%
} // end of loop on the sortedColl
%>
</table>
