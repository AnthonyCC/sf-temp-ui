<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.util.*' %>
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
if(deptId!=null) {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(deptId);
	isDepartment = true;
} else {
	currentFolder=ContentFactory.getInstance().getContentNodeByName(catId);
}


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
        Attribute prodSortAttrib = null;
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
        boolean breakOnSubfolder = false;
        prodSortAttrib = currentFolder.getAttribute("RATING_BREAK_ON_SUBFOLDERS");
        if (prodSortAttrib!=null) {
            breakOnSubfolder = ((Boolean)prodSortAttrib.getValue()).booleanValue();
        }


        prodSortAttrib = currentFolder.getAttribute("RATING");
        if (prodSortAttrib !=null) {
            ratingAttribs = (List)prodSortAttrib.getValue();
        }else ratingAttribs = new ArrayList();

	int atrDisplayCount = 2;
	if (ratingAttribs !=null) {
		atrDisplayCount += ratingAttribs.size();
	}
	String orderBy = request.getParameter("orderBy");
        if (orderBy==null) {
           // get the rating attibute collection and get the first rating thing off of it
            if (ratingAttribs.size() > 0) {
                Domain raDMV = ((DomainRef)ratingAttribs.get(0)).getDomain();
                orderBy = raDMV.getName().toLowerCase();
            }else orderBy="name";
        }

%>
<font class="space4pix"><BR></font><%//if(f.getCollection("ATR_featured_products").getProducts().size() > 0){
%>
<%
    int tablewidth=400;
    int tdwidth=92;
    int  nameColWidth=0;
    int priceColWidth = 0;
    int remainColWidth = 0;
    boolean showRelatedRatingImage = false;
    Comparator priceComp = new ProductModel.PriceComparator();
    prodSortAttrib = currentFolder.getAttribute("SHOW_RATING_RELATED_IMAGE");
    showRelatedRatingImage = prodSortAttrib==null?false:((Boolean)prodSortAttrib.getValue()).booleanValue();
    if (showRelatedRatingImage) {
		tablewidth=425;
	  	tdwidth=96; 
		showRelatedRatingImage = true; 
    } 

	
    Attribute featProdAttrib = null;
    List favorites = null;
    
    if(currentFolder instanceof DepartmentModel) {
    	favorites = ((DepartmentModel) currentFolder).getFeaturedProducts();
    } else if (currentFolder instanceof CategoryModel) {
    	favorites = ((CategoryModel) currentFolder).getFeaturedProducts();
    } else {
    	featProdAttrib = currentFolder.getAttribute("FEATURED_PRODUCTS");
    	if(featProdAttrib != null) {
    	    favorites = (List) featProdAttrib.getValue();
    	}
    }
    
    if(favorites == null) {
        favorites = new ArrayList();
    }
    StringBuffer favoriteProducts = new StringBuffer();
    ContentFactory contentFactory = ContentFactory.getInstance();
    int favoritesShown = 0;
%>
    <logic:iterate id='contentRef' collection="<%=favorites%>" type="java.lang.Object">
<% 
		ProductModel product;
        if (contentRef instanceof ProductRef) {
          	product = ((ProductRef)contentRef).lookupProduct();
        } else if (contentRef instanceof ProductModel) {
            product = (ProductModel) contentRef;
        } else {
        	continue;
        }
        if (product.isDiscontinued() || product.isUnavailable()) continue;

        CategoryModel prodParent = (CategoryModel)product.getParentNode(); 
        List skus = product.getSkus(); 
        if (prodParent==null || !(prodParent instanceof CategoryModel)) continue;
	for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
		SkuModel sku = (SkuModel)li.next();
       	if ( sku.isUnavailable() ) {
			li.remove();
		}
	}
        int skuSize = skus.size();

        SkuModel sku = null;
        String prodPrice = null;
        if (skuSize==0) continue;  // skip this item..it has no skus.  Hmmm?
        if (skuSize==1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        }
        else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }
%>

        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 
        prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
%>						
        </fd:FDProductInfo>
<%
        String productPageLink_ = response.encodeURL("product.jsp?catId="+prodParent+"&productId="+product+"&trk=feat");
        Image favAllImage = (Image)product.getCategoryImage();
        favoriteProducts.append("<TD WIDTH=\"");
        favoriteProducts.append(tdwidth);
        favoriteProducts.append("\"><A HREF=\"");
        favoriteProducts.append(productPageLink_);
        favoriteProducts.append("\">");
        if (favAllImage !=null) {
            favoriteProducts.append("<img SRC=\"");
            favoriteProducts.append(favAllImage.getPath());
            favoriteProducts.append("\"");
            favoriteProducts.append(JspMethods.getImageDimensions(favAllImage));
            favoriteProducts.append(" border=\"0\" alt=\"");
            favoriteProducts.append(product.getFullName());
            favoriteProducts.append("\">");
        }
        favoriteProducts.append("</A><BR>");
        favoriteProducts.append("<A HREF=\"");
        favoriteProducts.append(productPageLink_);
        favoriteProducts.append("\">");
		String thisProdBrandLabel = product.getPrimaryBrandName();
        if (thisProdBrandLabel.length()>0) {
            favoriteProducts.append("<FONT CLASS=\"text10bold\">");
            favoriteProducts.append(thisProdBrandLabel);
            favoriteProducts.append("</font><BR>");
        }
        favoriteProducts.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
        //favoriteProducts.append(product.getFullName());
        favoriteProducts.append("</A><BR>");
        favoriteProducts.append(prodPrice);
        favoriteProducts.append("</TD>");
        favoriteProducts.append("<TD WIDTH=\"10\">");
        favoriteProducts.append("<IMG SRC=\"");
        favoriteProducts.append("/media_stat/images/layout/clear.gif");
        favoriteProducts.append("\" WIDTH=\"8\" HEIGHT=\"1\"></TD>");
        favoritesShown++;
        if (favoritesShown==4) break;
%>
    </logic:iterate>
<%
    if (favoriteProducts.length()>1) {
      onlyOneProduct=false;

%>
<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%=tablewidth%>">
	<TR VALIGN="TOP"><TD CLASS="text12bold" WIDTH="<%=tablewidth%>"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"><BR>Our Favorites<BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="3"></TD>
	</TR>
</TABLE>
<font class="space4pix"><BR></font>
<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%=tablewidth%>">
	<TR VALIGN="TOP" ALIGN="CENTER"><%=favoriteProducts.toString()%></TR>
</TABLE>
<font class="space4pix"><BR></font>
<%
    }
// }
// idealy the following td cells should be created afer we know the location of the full_name column, but..%>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=tablewidth%>">
	<TR VALIGN="TOP">
		<TD WIDTH="400"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="400" HEIGHT="5"></TD></TR>
		<TR VALIGN="TOP"><TD WIDTH="400" CLASS="text9"><FONT CLASS="text10bold">Compare <%= currentFolder.getFullName() %> by: Taste &amp; Price</FONT></TD></TR>
		<TR VALIGN="TOP"><TD WIDTH="400"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="3"></TD></TR> <TR VALIGN="TOP">
		<TD WIDTH="400" BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="10" HEIGHT="1"></TD>
	</TR>
</TABLE>

<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="<%= tablewidth %>">
	<TR VALIGN="BOTTOM">
<%
// get the display labels from the category's "rating" attribute, price is the only hard coded column.  Also we assume that the name is there
// and that it will be the first column (or the longest)
                priceColWidth=90;
                remainColWidth=0;
                if(atrDisplayCount == 2 ) {
                        nameColWidth = 280;
                }
                else {
                        nameColWidth = 105;
                        remainColWidth = (tablewidth-(nameColWidth+priceColWidth))/(atrDisplayCount-2);
                }
%>		<TD colspan="2" bgcolor="#eeeeee" WIDTH="<%= nameColWidth+30 %>" CLASS="text10bold"><img border="0" src="/media_stat/images/layout/clear.gif" width="3">
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
        Domain ratingAttrib = ((DomainRef)aItr.next()).getDomain();
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
		<TD bgcolor="#eeeeee" WIDTH="<%=remainColWidth%>" CLASS="text10bold" ALIGN="CENTER">
<%
	//should this column label be linked?.  if currently ordered by it, then No.
        if(orderBy.equalsIgnoreCase(colRatingName)){
            if (isBooleanDomain || isNumeric) reverseOrderBy=true;
             sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_ATTRIBUTE,"RATING",orderBy,reverseOrderBy));
%>
<%=minusChar%><%=colLabel%><%=plusChar%>
<%	}else{ %>
<%=minusChar%><A HREF="<%=response.encodeURL("/category.jsp?catId=" + currentFolder + "&orderBy="+colRatingName)%>"><%=colLabel%></A><%=plusChar%>
<%      }%></TD>
<%} // close loop
//Now add the price column
%>		<TD bgcolor="#eeeeee" WIDTH="<%=priceColWidth%>" CLASS="text10bold" ALIGN="CENTER">
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
    if (lastFolder==null || !workFolder.getPK().equals(lastFolder.getPK())) {
        shownFolderLabel = false;
    }

    if ((workFolder.getShowSelf()==true 
        || workFolder.getFullName().toLowerCase().indexOf("decaf")!=-1 )&& shownFolderLabel==false ) {
         shownFolderLabel=true;
         lastFolder = workFolder;
%>
	<TR VALIGN="MIDDLE"><TD colspan="5" WIDTH="400" CLASS="text10bold" ALIGN="LEFT"><img src="/media_stat/images/layout/clear.gif" width="3" border="0"><%=workFolder.getFullName()%>:</td>
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
        prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
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
        prodSortAttrib = prod.getAttribute("RATING_RELATED_IMAGE");
        String itmImage = "";
        if (prodSortAttrib !=null && showRelatedRatingImage) {
                Image ratingImage = (Image)prodSortAttrib.getValue();
                StringBuffer flagTag = new StringBuffer();
                flagTag.append("<img src=\"");
                flagTag.append(ratingImage.getPath());
                flagTag.append("\" width=\"30\" height=\"20\" border=\"0\" alt=\"");
                flagTag.append(prod.getFullName());
                flagTag.append("\" ALIGN=\"left\" HSPACE=\"5\">");
                itmImage = flagTag.toString();
        }

        url = response.encodeURL("product.jsp?productId=" + prod + "&catId=" + workFolder+"&trk=rate");
%><td align="left"><%=itmImage%></td><%
        if (prod.isUnavailable()) {
            prodPrice = "Not Available";
%><TD WIDTH="<%= nameColWidth %>"><A HREF="<%=url%>"><font color="#999999"><%=prod.getFullName()%></font></A></TD>
<%      } else { %><TD WIDTH="<%= nameColWidth %>"><A HREF="<%=url%>"><%=prod.getFullName()%></A></TD>
<%      }
    //We've got a products. Now locate each display attribute on the product
        int colCounter = 2;
        for (Iterator aItr=ratingAttribs.iterator();aItr.hasNext();) {
            Domain ratingAttrib = ((DomainRef)aItr.next()).getDomain();
            List domainValues = ratingAttrib.getDomainValues();
            MultiAttribute prodSortMAttrib = (MultiAttribute)prod.getAttribute("RATING");
            List prodDomainValues = null;
            String prodDomainValue = null;

            if (prodSortMAttrib !=null) {
                prodDomainValues = (List)prodSortMAttrib.getValues();
            }else prodDomainValues = new ArrayList();
            
            boolean foundDomain = false;
           // get the matching domainvalue off the prod for this Domain.
            isBooleanDomain = false;
            isNumeric = false;

            
            for(Iterator dvItr = prodDomainValues.iterator();dvItr.hasNext() && !foundDomain ;) {
                Object obj = dvItr.next();
                if (!(obj instanceof DomainValueRef)) continue;
                DomainValue dmv = ((DomainValueRef)obj).getDomainValue(); //dvItr.next();
                Domain dom = dmv.getDomain();
                if (ratingAttrib.getPK().equals(dom.getPK())) {
                    prodDomainValue = dmv.getValue();
                    foundDomain = true;
                } 
            }
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
%><TD WIDTH="<%=remainColWidth%>" ALIGN="CENTER" bgcolor="<%=cellColor%>"><%
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
<TD WIDTH="<%=priceColWidth%>" ALIGN="CENTER" bgcolor="#dddddd">
<%      }else{%>
<TD WIDTH="<%=priceColWidth%>" ALIGN="CENTER" bgcolor="<%=bgcolor%>">
<%      }%>
<%= prodPrice %>&nbsp;</TD></TR>
<%
} // end of loop on the sortedColl
%>
</table>