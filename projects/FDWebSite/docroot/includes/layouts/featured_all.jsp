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
<%

//********** Start of Stuff to let JSPF's become JSP's **************

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


boolean onlyOneProduct = false;
ProductModel theOnlyProduct = null;
String trkCode = (String)request.getAttribute("trk");


Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>
<%!
        JspMethods.ContentNodeComparator contentNodeComparator = new JspMethods.ContentNodeComparator();
%>

<%
//**************************************************************
//***         the Featured All Pattern                       ***
//**************************************************************
    if (trkCode!=null && !"".equals(trkCode.trim()) ) {
        trkCode = "&trk="+trkCode.trim();
    }else {
        trkCode = "";
    }


    ContentNodeModel owningFolder = currentFolder;
    Attribute attribInFeatAll = null;

    TreeMap deferDisplayProds = new TreeMap(contentNodeComparator);
    HashMap deferDisplayPrices = new HashMap();
    HashMap deferDisplayImages   = new HashMap();
    Image favAllImage = null;
    attribInFeatAll = currentFolder.getAttribute("FAVORITE_ALL_SHOW_PRICE");
    boolean showPrices = attribInFeatAll==null?false:((Boolean)attribInFeatAll.getValue()).booleanValue();
    String imagePath = null;
    String imageDim = "";
    String clearImage = "/media_stat/images/layout/clear.gif";
    String unAvailableImg="/media_stat/images/template/not_available.gif";
    String notAvailImgName = "";
    ContentNodeModel parentNode = currentFolder.getParentNode();
    while (parentNode!=null && !(parentNode instanceof DepartmentModel)) {
        parentNode = parentNode.getParentNode();
    }

    String deptName = parentNode.getFullName(); //getDepartmentPath(webconnect.getFolder().getString("path") );
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    String tablewidth="400";
    String tdwidth="92";

    if (deptName.indexOf("coffee") != -1) {
            String thisLocation = currentFolder.getFullName();
                    //if (thisLocation.endsWith("department/coffee_tea/roast.jsp") || thisLocation.endsWith("department/coffee_tea/region.jsp" )) {
                    if (thisLocation.endsWith("Roast") || thisLocation.endsWith("Region")) {
                    tablewidth="425";
                    tdwidth="96";
                    }
    }




    LinkedList productLinks = new LinkedList();
    LinkedList productPrices = new LinkedList();
    LinkedList unAvailableProds = new LinkedList();
    StringBuffer col1 = new StringBuffer(300);
    String imgName = null;
    boolean folderAsProduct = false;
    ContentNodeModel aliasNode = null;
    ContentNodeModel prodParent = null;
    Comparator priceComp = new ProductModel.PriceComparator();
    
	// TEST CATEGORY: ContentNodeModel testCat = ContentFactory.getInstance().getContentNode("cof_ef_org");
%>
<%-- FEATURED ITEMS DISPLAY START --%>
<fd:FeaturedItemsRecommendations id="recommendations"  currentNode="<%= currentFolder %>" itemCount="4"><%
	if (recommendations != null && recommendations.getContentNodes().size() > 0) {
	    request.setAttribute("recommendationsRendered","true");

		List products = recommendations.getContentNodes();
		int ord = 1;
%><table cellspacing="0" cellpadding="1" border="0" width="<%= tablewidth %>">
	<tr valign="top">
	    <td CLASS="text12bold" width="<%= tablewidth %>" colspan="<%= products.size()*2 %>">Our Favorites</td>
	</tr>
	<tr valign="top" align="CENTER">
<logic:iterate id='contentNode' collection="<%= products %>" type="com.freshdirect.fdstore.content.ProductModel"><%
			ProductModel productNode = contentNode;
			ProductLabelling pl = new ProductLabelling((FDUserI) session.getAttribute(SessionName.USER), productNode);
			String fiRating = "";
			String fiProdPrice = null;
%><fd:ProduceRatingCheck><%
    		fiRating = JspMethods.getProductRating(productNode);
%></fd:ProduceRatingCheck><fd:FDProductInfo id="productInfo" skuCode="<%= productNode.getDefaultSku().getSkuCode() %>"><%
			fiProdPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
%></fd:FDProductInfo><%
			String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord);
%><%-- display a product --%>
		<td width="<%= tdwidth %>">
			<div><fd:ProductImage product="<%= productNode %>" action="<%= actionURI %>"/></div>
<%			if (fiRating != null && fiRating.trim().length()>0) { %>
			<div class="center"><img name="<%= fiRating %>" src="/media_stat/images/ratings/<%= fiRating %>.gif" width="59" height="11" alt="" border="0"></div>
<%			}

			// product name
			if (productNode.isDisplayable()) { %>
			<div><a href="<%= actionURI %>"><%@ include file="/includes/product/i_prd_name.jspf" %></a></div>
<%			} else { %>
			<div><a style="color: #999999" href="<%= actionURI %>"><%@ include file="/includes/product/i_prd_name.jspf" %></a></div>
<%			} %>
			<div class="favoritePrice"><%= fiProdPrice %></div>
		</td>
		<td width="10"><IMG SRC="media_stat/images/layout/clear.gif" width="8" height="1"></td><%
			++ord;
%></logic:iterate>
	</tr>
</table>
<%
	}
%></fd:FeaturedItemsRecommendations>
<%-- FEATURED ITEMS DISPLAY END --%>
<font class="space4pix"><BR></font>
<%


    CategoryModel displayCategory = null;
    boolean gotAvailProdImg = false;
    StringBuffer appendColumn = new StringBuffer(200);
    StringBuffer appendColumnPrices = new StringBuffer(200);

%>
    <logic:iterate id='contentNode' collection="<%=sortedColl%>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%      if (displayCategory==null) {
            if (contentNode instanceof ProductModel) {
            displayCategory=(CategoryModel)currentFolder;
            } else {
                displayCategory=(CategoryModel)contentNode;
            }
        }
        folderAsProduct = false;
        //figure out if this item is to be treated like a product..(if the folder link is set)
        //!!! not a cool way.  should have some flag called treatAsProduct
       
        if (contentNode instanceof CategoryModel) {
            folderAsProduct = contentNode.getAttribute("TREAT_AS_PRODUCT")==null?false:((Boolean)contentNode.getAttribute("TREAT_AS_PRODUCT").getValue()).booleanValue();
            
            Attribute aliasAttr = contentNode.getAttribute("ALIAS");
            if (aliasAttr !=null ) {
                ContentRef aliasRef = (ContentRef)aliasAttr.getValue();
                if (aliasRef instanceof ProductRef) {
                    aliasNode = ((ProductRef)aliasRef).lookupProduct();
                } else if(aliasRef instanceof CategoryRef) {
                    aliasNode = ((CategoryRef)aliasRef).getCategory();
                }
                else if(aliasRef instanceof DepartmentRef){
                    aliasNode = ((DepartmentRef)aliasRef).getDepartment();
                }
            }
        }
        if (contentNode instanceof CategoryModel && !folderAsProduct) {
            owningFolder = (CategoryModel)contentNode;
            onlyOneProduct = false;
            if (productLinks.size() > 0 ) { //display the stuff we
%>
<table cellspacing="0" cellpadding="0" border="0" width="400">
<tr valign="middle">
    <td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></td>
</tr>
<tr valign="middle">
    <td width="400" COLSPAN="4" CLASS="title10"><font color="#515151"><%=displayCategory.getFullName().toUpperCase()%></font></td>
</tr>
<tr valign="middle"><td width="400" COLSPAN="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0"></td></tr>
<tr valign="middle"><td width="400" COLSPAN="4" BGCOLOR="#DDDDDD"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr valign="middle">
    <td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
</table>
<%
              String outputProducts= JspMethods.displayFAProducts(productLinks, productPrices,showPrices,false);
            //  String outputUnavailProds= JspMethods.displayFAProducts(unAvailableProds, showPrices,true);
%>
<table cellspacing="0" cellpadding="1" border="0" width="400">
<tr valign="top">
    <td width="90"><%=col1.toString()%></td><%=outputProducts%>
</tr>
</table>
<%
            } else if (unAvailableProds.size()>0) {
                //place the currentFolder and the display stringBuffer into the hash
                //deferDisplayProds.put(displayCategory,unAvailableProds);
                //deferDisplayPrices.put(displayCategory,unAvailablePrices);
                //deferDisplayImages.put(displayCategory,col1);
                //unAvailableProds = new LinkedList();
                //unAvailablePrices = new LinkedList();
                col1 = new StringBuffer();
            }
            col1.setLength(0);
            productLinks.clear();
            productPrices.clear();
            //unAvailableProds.clear();
            //unAvailablePrices.clear();
            gotAvailProdImg = false;

            displayCategory=(CategoryModel)contentNode;
    }//end of folder instance check
        else {  // must be a product. we'll need the usual stuff, and possibly the price
            ProductModel product = null;
            favAllImage = null;
            imageDim = "";
            imagePath = "";
            imgName = "Img"+displayCategory;
            notAvailImgName = imgName+"_unavailable";
            imagePath = null;
            imageDim = null;

            if (folderAsProduct ){
                if (aliasNode instanceof CategoryModel){
                    if (aliasNode.getAttribute("CAT_PHOTO") !=null) {
                        favAllImage = (Image)aliasNode.getAttribute("CAT_PHOTO").getValue();
                        imagePath = favAllImage.getPath();
                        imageDim = JspMethods.getImageDimensions(favAllImage);
                        onlyOneProduct = false;
                    }
                } 
            } else if (contentNode instanceof ProductModel){
                if (theOnlyProduct!=null) {
                    onlyOneProduct=false;
                }else {
                    onlyOneProduct=true;
                    theOnlyProduct = (ProductModel)contentNode;
                }

                product = (ProductModel)contentNode;
                owningFolder = (CategoryModel) product.getParentNode();
                imagePath = product.getCategoryImage().getPath();
                imageDim = JspMethods.getImageDimensions(product.getCategoryImage());
            }

            if(col1.length() == 0  || (col1.length()!=0 && !gotAvailProdImg)){
                col1.setLength(0);
                col1.append("<img name=\"");
                col1.append(imgName);
                col1.append("\" SRC=\"");
                col1.append(imagePath);
                if ("".equals(imageDim)) {
                        col1.append("\" width=\"70\" height=\"70\" border=\"0\">");
                }
                else {
                        col1.append("\"");
                        //this call is pretty much useless
						//col1.append(imageDim);
						
						//get a copy of the image dimensions to manipulate
						String imgS = imageDim;
						//add a comma delim
						imgS = imgS.replaceFirst("\" ", ",");
						//remove everything except "W,H"
						imgS = imgS.replaceAll("[ =\"a-z]", "");
						//split, 0=width;1=height
						String imgSarr[] = imgS.split(",");
						//check if original width is over 90, and if so change to 90
						//this is the same value that we're using in common_javascript.js [swapImage2sup]
						if ( Integer.valueOf( imgSarr[0] ).intValue() > 90 ){ imgSarr[0] = "90"; }
						//output width
                        col1.append(" width=\""+imgSarr[0]+"\">");
                }
                col1.append("<br /><img name=\"");
                col1.append(notAvailImgName);
                col1.append("\" SRC=\"");
                if (folderAsProduct || (product!=null && !product.isUnavailable())) {
                    col1.append(clearImage);
                    gotAvailProdImg = true;
                } else {
                    col1.append(unAvailableImg);
                }
                col1.append("\"");
                col1.append("width=\"70\" height=\"9\"");
                col1.append(">");
            }

            appendColumn.setLength(0);
            appendColumnPrices.setLength(0);
            String lstUnitPrice = null; //product.getDisplayableListPrice(webconnect.getPriceList());


            if (showPrices && !folderAsProduct) {
                List skus = product.getSkus();
                for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
                        SkuModel sku = (SkuModel)li.next();
                if ( sku.isUnavailable() ) {
                                li.remove();
                        }
                }
                int skuSize = skus.size();

                SkuModel sku = null;
                String prodPrice = null;
                  // skip this item..it has no skus.  Hmmm?
                if (skuSize==1) {
                    sku = (SkuModel)skus.get(0);  // we only need one sku
                }
                else if (skus.size() > 1) {
                    sku = (SkuModel) Collections.min(skus, priceComp);
                }
              if (sku!=null) {
%>
                    <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 

                    lstUnitPrice = "<font class=\"price\">"+JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase()+"</font>";
%>                      
                    </fd:FDProductInfo>
<%              }
            }
            if (lstUnitPrice==null ) {
                lstUnitPrice = "&nbsp;";
            } else if (product.isUnavailable()){
                lstUnitPrice = "Not Avail.";
            }
            if (folderAsProduct ) {
                if (aliasNode !=null) {
                    String fldrURL = response.encodeURL("/category.jsp?catId=" + aliasNode+trkCode);
                    appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
                    appendColumn.append(fldrURL);
                    appendColumn.append("\" ");
                    if (aliasNode.getAttribute("CAT_PHOTO") !=null) {
                        favAllImage = (Image)aliasNode.getAttribute("CAT_PHOTO").getValue();
                        imagePath = favAllImage.getPath();
                        appendColumn.append("onMouseover='");
                        appendColumn.append("swapImage(\""+imgName+"\",\""+imagePath+"\"");
                        appendColumn.append(")");
                        appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\")'");
                    }
                     appendColumn.append(">");
                    appendColumn.append(aliasNode.getFullName());
                    appendColumn.append("</A>");
                    appendColumnPrices.append("&nbsp;");
                }
            } else {
                String theProductName = JspMethods.getDisplayName(product,prodNameAttribute);
                String theBrandName = product.getPrimaryBrandName();
               // String theBrandName = product.getPrimaryBrandName(theProductName);
                if (theBrandName!=null && theBrandName.length()>0 && theProductName.startsWith(theBrandName)) {
                    String shortenedProductName = theProductName.substring(theBrandName.length()).trim();
                    //theProductName = "<b>"+theBrandName + "</b> " + theProductName.substring(theBrandName.length()).trim(); 
                    theProductName = "<b>"+theBrandName + "</b> " + shortenedProductName;
                }

                String prodURL = response.encodeURL("/product.jsp?productId=" + product + "&catId=" + owningFolder+trkCode);
                appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
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
					//change to new swapImage function and append dimensions to func call
					appendColumn.append("swapImage2(\""+imgName+"\",\""+((Image)product.getCategoryImage()).getPath()+"\","+imgS);
                //appendColumn.append("images['"+imgName+"'].src=\"");
                //appendColumn.append(product.getContent("ATR_image_product").getString("url")); notAvailImgName
                appendColumn.append(")");
                if(product.isUnavailable()){
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+unAvailableImg+"\"");
                } else {
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\"");
                }
                appendColumn.append(")'>");
                if(product.isUnavailable()){
                    appendColumn.append("<font color=\"#999999\">");
                    appendColumn.append(theProductName);
                    appendColumn.append("</font>");
                    appendColumn.append("</A>");
                    appendColumn.append(lstUnitPrice);
                    appendColumn.append("</div>");
                    appendColumnPrices.append("<font color=\"#999999\">");
                    appendColumnPrices.append(lstUnitPrice);
                    appendColumnPrices.append("</font>");
                } else {
                    appendColumn.append(theProductName);
                    appendColumn.append("</A></div>");
                    appendColumnPrices.append(lstUnitPrice);
                }
            }
            if (product!=null && product.isUnavailable()){
                unAvailableProds.add(appendColumn.toString());
            } else {
                productLinks.add(appendColumn.toString());
                productPrices.add(appendColumnPrices.toString());
            }
}// end of Product instance check
%>
    </logic:iterate>
<%
  if (productLinks.size() > 0) { %>
  <table cellspacing="0" cellpadding="0" border="0" width="400">
<tr valign="middle">
<td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></td>
</tr>
<tr valign="middle">
    <td width="400" COLSPAN="4" CLASS="title10"><font color="#515151"><%=displayCategory.getFullName().toUpperCase()%></font></td>
</tr>
<tr valign="middle"><td width="400" COLSPAN="4" BGCOLOR="#DDDDDD" ><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr valign="middle">
<td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
</table>
<%
  //loop completed, but we may have accumulated some prods to display..so
   if (productLinks.size() > 0) { //display the stuff we
     String outputProducts= JspMethods.displayFAProducts(productLinks, productPrices,showPrices,false);
     //String outputUnavailProds= JspMethods.displayFAProducts(unAvailableProds, showPrices,true);
%><table cellspacing="0" cellpadding="1" border="0" width="400"><tr valign="top">
<td width="90"><%=col1.toString()%></td><%=outputProducts%></tr>
</table>
<% }
} else if (unAvailableProds.size() > 0) { // move the stuff into the deferred list.
    //deferDisplayProds.put(displayCategory,unAvailableProds);
    //deferDisplayPrices.put(displayCategory,unAvailablePrices);
    //deferDisplayImages.put(displayCategory,col1);
}
/** handle the deffered Unavailable stuff **/
if (deferDisplayProds.size() > 0 ) {
    String unAvailableHdrImg = "/media_stat/images/template/currently_not_available21.gif";

%>
<Table width="400">
<tr valign="top"><td width="400"><br><IMG src="/media_stat/images/layout/cccccc.gif" height="1" width="400"></td></tr>
<tr><td width="400"><IMG src="/media_stat/images/layout/clear.gif" ALT="" width="1" height="1"></td></tr>
<tr><td width="400" align = "center"><BR><img src="<%=unAvailableHdrImg%>" border="0" alt=""><br><br></td></tr></table>
<%
    for (Iterator keyItr = deferDisplayProds.keySet().iterator();keyItr.hasNext(); ) {
        CategoryModel catModl = (CategoryModel)keyItr.next();
        onlyOneProduct=false;
%>
 <table cellspacing="0" cellpadding="0" border="0" width="400">
<tr valign="middle">
<td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0"></td>
</tr>
<tr valign="middle">
    <td width="400" COLSPAN="4" CLASS="title10"><Font color="#999999"><%=catModl.getFullName().toUpperCase()%></font></td>
</tr>
<tr valign="middle"><td width="400" COLSPAN="4" BGCOLOR="#DDDDDD" CLASS="title11"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr valign="middle">
<td width="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" width="1" height="4"></td>
</tr>
</table>
<%
   String deferedoutput= JspMethods.displayFAProducts((LinkedList)deferDisplayProds.get(catModl), showPrices,false);
   String imgColl = ((StringBuffer)deferDisplayImages.get(catModl)).toString();
%><table cellspacing="0" cellpadding="1" border="0" width="400"><tr valign="top">
<td width="90"><%=imgColl%></td><%=deferedoutput%></tr>
</table>
<%
   }
}
if (onlyOneProduct) {
    request.setAttribute("theOnlyProduct",theOnlyProduct);
}
deferDisplayProds=null;
deferDisplayPrices=null;
deferDisplayImages=null;
// end of featured_all_Layout
%>
