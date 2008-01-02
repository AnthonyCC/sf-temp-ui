<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.fdstore.content.util.*'  %><%@ page import='com.freshdirect.fdstore.*, com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<fd:CheckLoginStatus />
<%! 
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>
<% 
Attribute attrib = null;

String catId = request.getParameter("catId"); 
String deptId = request.getParameter("deptId");
String productId = request.getParameter("productId");
String ratingGroupName = request.getParameter("ratingGroupName");
String unSortLink = null;
boolean isDept = false;
if (deptId!=null) isDept = true;
boolean isGroceryVirtual=false;
ContentNodeModel currentFolder = null;
if(isDept) {
 currentFolder = ContentFactory.getInstance().getContentNodeByName(deptId);
 unSortLink = response.encodeURL("/department.jsp?deptId="+deptId+"&trk=rate");
} else {
 currentFolder = ContentFactory.getInstance().getContentNodeByName(catId);
 unSortLink = response.encodeURL("/category.jsp?catId="+catId+"&trk=rate");
}
String productIdURL = "";
if (productId!=null) {
    productIdURL = "&productId="+productId;
}
boolean noLeftNav = true;
String jspTemplate =null;

attrib = currentFolder.getAttribute("SHOW_SIDE_NAV");
if (attrib!=null) {
    noLeftNav = !((Boolean)attrib.getValue()).booleanValue();
}
// Assign the correct template
if (noLeftNav) {
    jspTemplate = "/common/template/dnav.jsp";
} else {
    jspTemplate = "/common/template/left_dnav.jsp";
}
boolean breakOnSubfolder = false;
attrib = currentFolder.getAttribute("RATING_BREAK_ON_SUBFOLDERS");
if (attrib!=null) {
    breakOnSubfolder = ((Boolean)attrib.getValue()).booleanValue();
}
%>
<tmpl:insert template='<%=jspTemplate%>'>
<%
        if (!noLeftNav) {
%>
<tmpl:put name='leftnav' direct='true'>
</tmpl:put>
<%
        }
%>

    <tmpl:put name='title' direct='true'>FreshDirect - <%= currentFolder.getFullName() %></tmpl:put>
    <tmpl:put name='content' direct='true'>

<% 
int tablewid = 0;
if (noLeftNav==true) {
    tablewid = 733;
} else {
    tablewid = 540;
}

String introTitle = currentFolder.getEditorialTitle();
attrib=currentFolder.getAttribute("SEASON_TEXT");
String containsSeasonText = attrib==null?"":(String)attrib.getValue();
String inSeasonNow = "/media_stat/images/template/in_season_now.gif";
String organicAvailable = "/media_stat/images/template/organic.gif";
String scalesImage = "/media_stat/images/template/scales.gif";

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
int nameColWidth = 0;
int priceColWidth = 0;
int remainColWidth = 0;
boolean reverseOrder = false;
boolean isBooleanDomain = false;
boolean isNumeric = false;
String tdwidth="92";
boolean showRelatedRatingImage = false;
prodSortAttrib = currentFolder.getAttribute("SHOW_RATING_RELATED_IMAGE");
showRelatedRatingImage = prodSortAttrib==null?false:((Boolean)prodSortAttrib.getValue()).booleanValue();

prodSortAttrib = currentFolder.getAttribute(ratingGroupName);
if (prodSortAttrib !=null) {
    ratingAttribs = (List)prodSortAttrib.getValue();
}else ratingAttribs = new ArrayList();
int atrDisplayCount = 0;
if (ratingAttribs !=null) {
   atrDisplayCount = ratingAttribs.size();
}
String orderBy = request.getParameter("orderBy");
if (orderBy==null) {
    orderBy = "name";
}


//  get the rating & ranking stuff
    Attribute tmpAttribute = currentFolder.getAttribute("RATING_GROUP_NAMES");
    StringBuffer rateNRankLinks = new StringBuffer();
    
    if (tmpAttribute !=null) {
        StringTokenizer stRRNames = new StringTokenizer((String)tmpAttribute.getValue(),",");
        while (stRRNames.hasMoreTokens()) {
            String rrName = stRRNames.nextToken().toUpperCase();
            boolean isSameRatingGroup = ratingGroupName.equalsIgnoreCase(rrName);
            String ordrBy = "";
            // go find the attribute with that name and it's label
            tmpAttribute = currentFolder.getAttribute(rrName);
            if (tmpAttribute !=null) {
                // get the rating attibute collection and get the first rating thing off of it
                tmpAttribute = currentFolder.getAttribute(rrName);
                List ra = (List)tmpAttribute.getValue();
                if (ra.size() > 0) {
                    Domain raDMV = ((DomainRef)ra.get(0)).getDomain();
                    ordrBy = "&orderBy="+raDMV.getName().toLowerCase();
                }

                if (rateNRankLinks.length() > 1) rateNRankLinks.append(" | ");
                if (!isSameRatingGroup) {
                    rateNRankLinks.append("<a href=\"");
                    rateNRankLinks.append(response.encodeURL("/rating_ranking.jsp?catId="+currentFolder+"&ratingGroupName="+rrName+ordrBy+productIdURL));
                    rateNRankLinks.append("\">");
                }
            }
                // get the label for this rating group name.
            tmpAttribute = currentFolder.getAttribute(rrName+"_LABEL");
            if (tmpAttribute!=null) {
                rateNRankLinks.append((String)tmpAttribute.getValue());
            } else {
              rateNRankLinks.append(rrName.replace('_',' '));
            }
            if (!isSameRatingGroup) {
                rateNRankLinks.append("</a>");
            }
        }
    } 
%>
<table width="<%=tablewid%>" border="0" cellspacing="0" cellpadding="0">
<% if ( !"nm".equalsIgnoreCase(introTitle) && introTitle!=null && introTitle.trim().length() > 0 && !introTitle.equals("")) { %>
<tr><td align="center"><font class="title16"><%=introTitle%></font></td></tr><%}%>
<%
   if (rateNRankLinks.length() > 0 ) {
%>
<tr><td><img src="/media_stat/images/layout/clear.gif" height="7" width="1"></td></tr>
    <tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" height="1" width="1"></td></tr>
    <tr><td><img src="/media_stat/images/layout/clear.gif" height="4" width="1"></td></tr>
    <tr align="center"><td>
    <table cellpadding="0" cellspacing="0" border="0"><tr><td><img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*"><img src="/media_stat/images/layout/clear.gif" height="1" width="6"></td><td class="text11bold">Compare by:&nbsp;</td><td><b><%=rateNRankLinks%></b> | (<b><a href="<%=unSortLink%>">Unsort</a></b>)</td><td><img src="/media_stat/images/layout/clear.gif" height="1" width="6"><img src="/media_stat/images/template/gstar.gif" width="15" height="14" border="0" alt="*"></td></tr></table></td></tr>
<%}%>
<TR VALIGN="TOP"><TD WIDTH="<%=tablewid%>"><IMG src="/media_stat/images/layout/clear.gif" height="4" width="<%=tablewid%>"></TD></TR>
<TR><TD BGCOLOR="#CCCCCC"><IMG src="/media_stat/images/layout/clear.gif" height="1" width="1"></TD></TR>
<TR><TD><IMG src="/media_stat/images/layout/clear.gif" ALT="" WIDTH="1" HEIGHT="5"></td></tr></TABLE>
<FONT CLASS="space4pix"><BR></FONT>
<fd:ItemGrabber category='<%= (ContentNodeModel)currentFolder %>' id='sortedStuff'  
        depth='3' ignoreShowChildren='true'  returnHiddenFolders='false'>
<font class="space4pix"><BR></font>
<%    ContentFactory contentFactory = ContentFactory.getInstance();  %>

<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" WIDTH="<%= tablewid %>">
<TR VALIGN="BOTTOM">
<%
// get the display labels from the category's "rating" attribute, price is the only hard coded column.  Also we assume that the name is there
// and that it will be the first column (or the longest)
                priceColWidth=90;
                if(atrDisplayCount == 0 ) {
                        nameColWidth = 300;
                }
                else {
                        nameColWidth = 125;
                }
                remainColWidth = (tablewid-(nameColWidth+priceColWidth))/(atrDisplayCount==0?1:atrDisplayCount);

%><TD bgcolor="#eeeeee" WIDTH="<%= nameColWidth %>" CLASS="text10bold">
<%
        if(orderBy.equalsIgnoreCase("variety")){
%>
Variety
<%  }else{ %>
<A HREF="<%=response.encodeURL("/rating_ranking.jsp?ratingGroupName="+ratingGroupName+"&catId=" + currentFolder + "&orderBy=name"+productIdURL)%>">Variety</A>
<%      }%></TD>
<%
List sortStrategy = new ArrayList();
if (breakOnSubfolder) {
  sortStrategy.add(new SortStrategyElement(SortStrategyElement.GROUP_BY_CATEGORY_PRIORITY));
}
for (Iterator aItr=ratingAttribs.iterator();aItr.hasNext();) {
        Domain ratingAttrib = ((DomainRef)aItr.next()).getDomain();
        List domainValues = ratingAttrib.getDomainValues();
        colLabel="";
        minusChar = "";
        plusChar = "";
        colRatingName = ratingAttrib.getName();
        colLabel=ratingAttrib.getLabel();
        colLabel=colLabel.substring(0,1).toUpperCase()+colLabel.substring(1);

        // we will assume the following: 2 domain Values is  a True/False Domain  anything else is is numeric
        // if the rating type is not numeric then clear the minus & plus chartacters accordingly
        isNumeric = false;
        isBooleanDomain = false;

        if(domainValues.size() > 0) {
            String strValue = (String)((DomainValue)domainValues.get(0)).getValue();
            if ("true".equalsIgnoreCase(strValue) || "false".equalsIgnoreCase(strValue)) {
                isBooleanDomain = true;
            } else {
                try {
                    Integer integer = new Integer((String)((DomainValue)domainValues.get(0)).getValue());
                    isNumeric = true;
                }
                catch (NumberFormatException nfe) {
                    isNumeric = false;
                }
            }
        }
        
        if (isNumeric ) {
            minusChar="-&nbsp;";
            plusChar="&nbsp;+";
        }

        /// set the length of the column ot 350 if it is the full name attribute
 %>
<TD BGCOLOR="#eeeeee"  WIDTH="<%=remainColWidth%>" CLASS="text10bold" ALIGN="CENTER">
<%
    //should this column label be linked?.  if currently ordered by it, then No.
        if(orderBy.equalsIgnoreCase(colRatingName)){
            if(isBooleanDomain || isNumeric) reverseOrder=true;
             sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_ATTRIBUTE,"RATING",orderBy,reverseOrder));
%>
<%=minusChar%><%=colLabel%><%=plusChar%>
<%  }else{ %>
<%=minusChar%><A HREF="<%=response.encodeURL("/rating_ranking.jsp?ratingGroupName="+ratingGroupName+"&catId=" + currentFolder + "&orderBy="+colRatingName+productIdURL)%>"><%=colLabel%></A><%=plusChar%>
<%      }%></TD>
<%} // close loop
//Now add the price column
%><TD BGCOLOR="#eeeeee" WIDTH="<%=priceColWidth%>" CLASS="text10bold" ALIGN="CENTER">
<%
if(orderBy.equalsIgnoreCase("Price")){%>Price&nbsp;
<%
  sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRICE));
}else{
%><A HREF="<%=response.encodeURL("/rating_ranking.jsp?ratingGroupName="+ratingGroupName+"&catId=" + currentFolder + "&orderBy=price"+productIdURL)%>">Price</a>&nbsp;
<%}%>
</TD></TR>
<%// go get the products
  sortStrategy.add(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_NAME));
%>  
<fd:ItemSorter nodes='<%= (List)sortedStuff %>' strategy='<%=sortStrategy%>'/>
<%
//** sort the collection accordingly   **/
//List sortedStuff = JspMethods.sorter(sortedColl,orderBy,reverseOrder,breakOnSubfolder);
Comparator priceComp = new ProductModel.PriceComparator();

int prodCounter = 0;
boolean shownFolderLabel = true;
ContentNodeModel lastFolder = workFolder;
for(Iterator itmItr = sortedStuff.iterator();itmItr.hasNext();) {
    ContentNodeModel itemToDisplay = (ContentNodeModel)itmItr.next();
    if (itemToDisplay instanceof CategoryModel) continue;
   // must be a product..
   ProductModel prod = (ProductModel)itemToDisplay;
   workFolder = (CategoryModel)prod.getParentNode();
   if (breakOnSubfolder) {
        // is the folder displayable or a decaf folder ?
        if (lastFolder==null || !workFolder.getPK().equals(lastFolder.getPK())) {
            shownFolderLabel = false;
        }

        if ((workFolder.getShowSelf()==true 
            || workFolder.getFullName().toLowerCase().indexOf("decaf")!=-1 )&& shownFolderLabel==false ) {
             shownFolderLabel=true;
             lastFolder = workFolder;
%>
<TR VALIGN="MIDDLE"><TD colspan="4" WIDTH="400" CLASS="text10bold" ALIGN="LEFT""><%=workFolder.getFullName()%>:</td></tr>
<%
    }
}

   // get the price for this thing

        List skus = prod.getSkus(); 
        if (skus.size()<1) continue;
    // VSZ - is this "sku filtering" neccessary?
    // MR - Yes, this is necessary.  For products with multiple skus, the default sku is the lowest priced sku.
    // If the lowest priced sku is discontinued, this causes errors.  Please leave this turned on.

        SkuModel oneSku = (SkuModel)skus.get(0);  // in case there are non in the list...
    for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
        SkuModel sku = (SkuModel)li.next();
        if ( sku.isUnavailable() ) {
            li.remove();
        }
    }
        int skuSize = skus.size();

        String prodPrice = null;
        SkuModel sku = null;
        if (skuSize==1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        }
        else if (skuSize >1) {
            sku = (SkuModel) Collections.min(skus, priceComp);
        } else sku = oneSku;
%>
        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 
        prodPrice = currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
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
        String boldOn = "";
        String boldOff = "";
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
        if (productId!=null && prod.getContentName().equals(productId)) {
            boldOn = "<B>";
            boldOff = "</B>";
        }
        url = response.encodeURL("product.jsp?productId=" + prod + "&catId=" + workFolder +"&trk=rate");
        if (prod.isUnavailable()) {
            prodPrice = "Not Available";
%><TD WIDTH="<%= nameColWidth %>"><%=itmImage%><A HREF="<%=url%>"><font color="#999999"><%=boldOn%><%=prod.getFullName()%><%=boldOff%></font></A></TD>
<%     } else { %>
<TD WIDTH="<%= nameColWidth %>"><%=itmImage%><A HREF="<%=url%>"><%=boldOn%><%=prod.getFullName()%><%=boldOff%></A></TD>
<%     }
    //We've got a products. Now locate each display attribute on the product
        MultiAttribute prodSortMAttrib = (MultiAttribute)prod.getAttribute("RATING");
        for (Iterator aItr=ratingAttribs.iterator();aItr.hasNext();) {
            Domain ratingAttrib = ((DomainRef)aItr.next()).getDomain();
            
            List prodDomainValues = null;
            String prodDomainValue = null;

            if (prodSortMAttrib !=null) {
                prodDomainValues = (List)prodSortMAttrib.getValues();
            }else prodDomainValues = new ArrayList();

            
            boolean foundDomain = false;
           // get the matching domainvalue off the prod for this Domain.
            for(Iterator dvItr = prodDomainValues.iterator();dvItr.hasNext() && !foundDomain ;) {
                Object obj = dvItr.next();
                if (!(obj instanceof DomainValueRef))  continue;

                DomainValue dmv = ((DomainValueRef)obj).getDomainValue(); //dvItr.next();
                Domain dom = dmv.getDomain();
                if (ratingAttrib.getPK().equals(dom.getPK())) {
                    prodDomainValue = dmv.getValue();
                    foundDomain = true;
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
            isBooleanDomain=false;
            isNumeric = false;
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

            String atrImgSrc = null;
            if(!isNumeric && !isBooleanDomain){
                //!!! assume that a non-numeric value is a string.
%>
<%=prodDomainValue%></td>
<%
            } else if (isNumeric){
                    atrImgSrc = "/media_stat/images/template/rating3_"+cellColor.substring(1,cellColor.length())+"_05_0"+prodDomainValue+".gif";
%><img src="<%=atrImgSrc%>"></td>
<%
            } else if(isBooleanDomain) {
               if ("true".equalsIgnoreCase(prodDomainValue)){ 
%><img src="/media_stat/images/layout/orangedot.gif"></td>
<%             } else { %>&nbsp;</td>
<%             } 
          }
    }// end of for loop on attrb-display list
    if(orderBy.equalsIgnoreCase("Price")){%>
<TD WIDTH="<%=priceColWidth%>" ALIGN="CENTER" bgcolor="#dddddd">
<%      }else{%>
<TD WIDTH="<%=priceColWidth%>" ALIGN="Center" bgcolor="<%=bgcolor%>">
<%      }%>
<%= prodPrice %>&nbsp;</TD></TR>
<%
} // end of loop on the sortedStuff
%>
</table>

</fd:ItemGrabber>
<FONT CLASS="space8pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT>
</tmpl:put>
</tmpl:insert>
