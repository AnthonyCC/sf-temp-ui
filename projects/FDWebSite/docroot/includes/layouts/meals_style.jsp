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
if(deptId!=null) {
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
}else {
	trkCode = "";
}

Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>

<%!
//**displayMSProducts*****************************************************
//get elements set table column properties
public String displayMSProducts(LinkedList productLinks, LinkedList productPrices,boolean showPrices,boolean listIsUnavailable){
    if (productLinks.size()<1) return "";
    int productItemsToDisplay = productLinks.size();
	int columnCutoff = productItemsToDisplay;
	if (productItemsToDisplay > 5){
	    columnCutoff = productItemsToDisplay / 2;
			if (productItemsToDisplay % 2 != 0) columnCutoff++;  //adjust for odd number of items
			}
    int imgColRowSpan = 0;
    StringBuffer outputRows = new StringBuffer(2000);
    
            imgColRowSpan = columnCutoff;
            for (int k = 0; k < columnCutoff; k++) {
                    if (k!=0){
                      outputRows.append("<tr>");
                    }
                    else{
                            outputRows.append("<td width=\"450\"><table width=\"450\"cellpadding=\"0\"cellspacing=\"0\"><tr>");
                           if (listIsUnavailable) {
                                outputRows.append("<td colspan=\"3\" width=\"450\"><br><font color=\"#999999\"><b>Currently Unavailable</b></font></td><tr>");
                            }
                    }//left column items
                    outputRows.append("<TD valign=\"top\" width=\"220\" height=\"18\">");
                    outputRows.append(productLinks.get(k));
					outputRows.append("&nbsp;");
					outputRows.append(productPrices.get(k));
                    outputRows.append("</td><TD width=\"10\">");
                    if (k==0) {  //Since this cell is in the first row use the image tag to set the spacing
                            outputRows.append("<IMG SRC=\"");
                            outputRows.append("/media/images/layout/clear.gif");
                            outputRows.append("\" ALT=\"\" WIDTH=\"10\" HEIGHT=\"1\" BORDER=\"0\">");
                    }
                    else {
                            outputRows.append("&nbsp;");
                    }//right column items
                    outputRows.append("</td><TD valign=\"top\" width=\"220\">");
                    if ((columnCutoff+k) < productLinks.size()) {
                            outputRows.append(productLinks.get(columnCutoff+k));
							outputRows.append("&nbsp;");
							outputRows.append(productPrices.get(columnCutoff+k));
                    }
                    else {
                            outputRows.append("&nbsp;");
                    }
                    outputRows.append("</td></tr>");
            }
            outputRows.append("<tr><td colspan=\"3\"><IMG src=\"/media_stat/images/layout/clear.gif\" WIDTH=\"1\" HEIGHT=\"4\"></td></tr></table></td>");

    return outputRows.toString();
}

%>

<%
//**************************************************************
//***               the Meals Style Pattern                  ***
//**************************************************************
    ContentNodeModel owningFolder = currentFolder;
    //Attribute attribInFeatAll = null;

    TreeMap deferDisplayProds = new TreeMap(new JspMethods.ContentNodeComparator());
    HashMap deferDisplayPrices = new HashMap();
    HashMap deferDisplayImages   = new HashMap();
    Image favAllImage = null;
    //attribInFeatAll = currentFolder.getAttribute("FAVORITE_ALL_SHOW_PRICE");
    boolean showPrices = true;
	//attribInFeatAll==null?false:((Boolean)attribInFeatAll.getValue()).booleanValue();
    boolean folderShown = false;
    String imagePath = null;
    String imageDim = "";
    String clearImage = "/media_stat/images/layout/clear.gif";
    String unAvailableImg="/media_stat/images/template/not_available.gif";
    String notAvailImgName = "";
    ContentNodeModel parentNode = currentFolder.getParentNode();
    while (parentNode!=null && !(parentNode instanceof DepartmentModel)) {
        parentNode = parentNode.getParentNode();
    }
	if (parentNode == null) {
	parentNode = currentFolder;
	}
    String deptName = parentNode.getFullName();
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    String tablewidth="550";
    String tdwidth="100";

 // don't display any heading if the favorite collection is empty or the products are unavailable.
    //attribInFeatAll = currentFolder.getAttribute("FEATURED_PRODUCTS");
    int favoritesShow = 0;
    //List favorites = attribInFeatAll==null ?new ArrayList():(List)attribInFeatAll.getValue();;
    int foldersShown =0, productsShown=0;
    LinkedList productLinks = new LinkedList();
    LinkedList productPrices = new LinkedList();
    LinkedList unAvailableProds = new LinkedList();
    LinkedList unAvailablePrices = new LinkedList();
    StringBuffer col1 = new StringBuffer();
    StringBuffer favoriteProducts = new StringBuffer();
    String imgName = null;
    boolean folderAsProduct = false;
    ContentNodeModel aliasNode = null;
    ContentNodeModel prodParent = null;
    ContentFactory contentFactory = ContentFactory.getInstance();
    Comparator priceComp = new ProductModel.PriceComparator();

    CategoryModel displayCategory = null;
    boolean gotAvailProdImg = false;
%>
<%-- //start prod display --%>
    <logic:iterate id='contentNode' collection="<%=sortedColl%>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<%         if (displayCategory==null) {
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
				Image catImg = (Image)displayCategory.getAttribute("CAT_LABEL").getValue();
%>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="550">
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" border="0"></TD>
</TR>
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4" CLASS="text11"><a href="/category.jsp?catId=<%=displayCategory%><%=trkCode%>"><img src="<%=catImg.getPath()%>" width="<%=catImg.getWidth()%>" height="<%=catImg.getHeight()%>" border="0"></a>&nbsp;&nbsp;<%=displayCategory.getBlurb()%></TD>
</TR>
<TR VALIGN="MIDDLE"><TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="2" ></TD></TR>
<TR VALIGN="MIDDLE"><TD COLSPAN="4" BGCOLOR="#DDDDDD"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD></TR>
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8"></TD>
</TR>
</TABLE>
<%
              String outputProducts= displayMSProducts(productLinks, productPrices,showPrices,false);
			  //don't show unavailable products
              //String outputUnavailProds= displayMSProducts(unAvailableProds, unAvailablePrices,showPrices,true);
%>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="550">
<TR VALIGN="TOP"><TD width="100"><%=col1.toString()%></td><%=outputProducts%></TR>
<TR>
<TD width="100"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"></TD><%//=outputUnavailProds%></TR>
</TABLE>
<%
            } else if (unAvailableProds.size()>0) {
                //place the currentFolder and the display stringBuffer into the hash
                deferDisplayProds.put(displayCategory,unAvailableProds);
                deferDisplayPrices.put(displayCategory,unAvailablePrices);
                deferDisplayImages.put(displayCategory,col1);
                unAvailableProds = new LinkedList();
                unAvailablePrices = new LinkedList();
                col1 = new StringBuffer();
            }
            col1.setLength(0);
            productLinks.clear();
            productPrices.clear();
            unAvailableProds.clear();
            unAvailablePrices.clear();
            gotAvailProdImg = false;

            displayCategory=(CategoryModel)contentNode;
	}//end of folder instance check
        else {  // must be a product. we'll need the usual stuff, and possibly the price
            ProductModel product = null;
            imageDim = "";
            imagePath = "";
            imgName = "Img"+displayCategory;
            notAvailImgName = imgName+"_unavailable";
            imagePath = null;
            imageDim = null;

            if (folderAsProduct ){
                if (aliasNode instanceof CategoryModel){
                    if (aliasNode.getAttribute("CAT_PHOTO") !=null) {
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
                        col1.append(imageDim);
                        col1.append(">");
                }
                col1.append("<br><img src=\""+clearImage+"\" width=\"1\" height=\"8\"><br><img name=\"");
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

            StringBuffer appendColumn = new StringBuffer(50);
            StringBuffer appendColumnPrices = new StringBuffer(20);
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
                    lstUnitPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
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
                        appendColumn.append("onMouseover='");
                        appendColumn.append("swapImage(\""+imgName+"\",\""+imagePath+"\"");
                        appendColumn.append(")");
                        appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\")'");
                    }
                     appendColumn.append(">");
                    appendColumn.append(aliasNode.getFullName());
                    appendColumn.append("</A>");
                    appendColumnPrices.append(lstUnitPrice);
                }
            } else {
                String prodURL = response.encodeURL("/product.jsp?productId=" + product + "&catId=" + owningFolder+trkCode);
                appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
                appendColumn.append(prodURL);
                appendColumn.append("\" onMouseover='");
                appendColumn.append("swapImage(\""+imgName+"\",\""+((Image)product.getCategoryImage()).getPath()+"\"");
                appendColumn.append(")");
                if(product.isUnavailable()){
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+unAvailableImg+"\"");
                } else {
                    appendColumn.append(";swapImage(\""+notAvailImgName+"\",\""+clearImage+"\"");
                }
                appendColumn.append(")'>");
                if(product.isUnavailable()){
                    appendColumn.append("<font color=\"#999999\">");
                    appendColumn.append(JspMethods.getDisplayName(product,prodNameAttribute));
                    appendColumn.append("</font>");
                    appendColumn.append("</A>");
                    appendColumnPrices.append("<font color=\"#999999\">");
                    appendColumnPrices.append(lstUnitPrice);
                    appendColumnPrices.append("</font>");
                } else {
                    appendColumn.append(JspMethods.getDisplayName(product,prodNameAttribute));
                    appendColumn.append("</A>");
					appendColumnPrices.append("-&nbsp;");
                    appendColumnPrices.append(lstUnitPrice);
                }
            }
            if (product!=null && product.isUnavailable()){
                unAvailableProds.add(appendColumn.toString());
                unAvailablePrices.add(appendColumnPrices);
            } else {
                productLinks.add(appendColumn.toString());
                productPrices.add(appendColumnPrices);
            }
			appendColumn.append("</div>");
}// end of Product instance check
%>
    </logic:iterate>
<%
  if (productLinks.size() > 0) { 
  Image catImg = new Image(); //(Image)displayCategory.getAttribute("CAT_LABEL").getValue();
%>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="550">
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" border="0"></TD>
</TR>
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4" CLASS="text11"><a href="/category.jsp?catId=<%=displayCategory%><%=trkCode%>"><img src="<%=catImg.getPath()%>" width="<%=catImg.getWidth()%>" height="<%=catImg.getHeight()%>" border="0"></a>&nbsp;&nbsp;<%=displayCategory.getBlurb()%></TD>
</TR>
<TR VALIGN="MIDDLE"><TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="2" ></TD></TR>
<TR VALIGN="MIDDLE"><TD COLSPAN="4" BGCOLOR="#DDDDDD"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="1"></TD></TR>
<TR VALIGN="MIDDLE">
	<TD COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="8"></TD>
</TR>
</TABLE>
<%
  //loop completed, but we may have accumulated some prods to display..so
   if (productLinks.size() > 0) { //display the stuff we
     String outputProducts= displayMSProducts(productLinks, productPrices,showPrices,false);
     //String outputUnavailProds= displayMSProducts(unAvailableProds, unAvailablePrices,showPrices,true);
%>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="550">
<TR VALIGN="TOP"><TD width="100"><%=col1.toString()%></td><%=outputProducts%></TR>
<TR>
<TD width="100"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="10"></TD><%//=outputUnavailProds%></TR>
</TABLE>
<% }
} else if (unAvailableProds.size() > 0) { // move the stuff into the deferred list.
    deferDisplayProds.put(displayCategory,unAvailableProds);
    deferDisplayPrices.put(displayCategory,unAvailablePrices);
    deferDisplayImages.put(displayCategory,col1); 
	}
/** handle the deffered Unavailable stuff **/
/* don't show unavailable products
if (deferDisplayProds.size() > 0 ) {
    String unAvailableHdrImg = "/media_stat/images/template/currently_not_available21.gif";

%><!--
<table width="550">
<TR VALIGN="TOP"><TD WIDTH="550"><br><IMG src="/media_stat/images/layout/cccccc.gif" height="1" width="550"></TD></TR>
<TR><TD WIDTH="550"><IMG src="/media_stat/images/layout/clear.gif" ALT="" WIDTH="1" HEIGHT="1"></td></tr>
<TR><TD WIDTH="550" align = "center"><BR><img src="<%//=unAvailableHdrImg%>" border="0" alt=""><br><br></td></tr></table>-->
<%/*
    for (Iterator keyItr = deferDisplayProds.keySet().iterator();keyItr.hasNext(); ) {
        CategoryModel catModl = (CategoryModel)keyItr.next();
        onlyOneProduct=false;
		Image catImg = (Image)catModl.getAttribute("CAT_LABEL").getValue();
		*/
%><!--
 <TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="550">
<TR VALIGN="MIDDLE">
<TD WIDTH="550" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" border="0"></TD>
</TR>
<TR VALIGN="MIDDLE">
	<TD WIDTH="550" COLSPAN="4" CLASS="text11"><Font color="#999999"><a href="/category.jsp?catId=<%//=catModl%><%//=trkCode%>"><img src="<%//=catImg.getPath()%>" width="<%//=catImg.getWidth()%>" height="<%//=catImg.getHeight()%>" border="0"></a>&nbsp;&nbsp;<%//=catModl.getBlurb()%></font></TD>
</TR>
<TR VALIGN="MIDDLE"><TD WIDTH="550" COLSPAN="4" BGCOLOR="#DDDDDD" CLASS="title11"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></TD></TR>
<TR VALIGN="MIDDLE">
<TD WIDTH="550" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4"></TD>
</TR>
</TABLE>-->
<%/*
     String deferedoutput= displayMSProducts((LinkedList)deferDisplayProds.get(catModl), (LinkedList)deferDisplayPrices.get(catModl),showPrices,false);
     String imgColl = ((StringBuffer)deferDisplayImages.get(catModl)).toString();
	 */
%><!-- <TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="550"><TR VALIGN="TOP">
<TD width="100"><%//=imgColl%></td><%//=deferedoutput%></tr>
</table> -->
<%/*
   }
}
deferDisplayProds=null;
deferDisplayPrices=null;
deferDisplayImages=null;
*/
// end of meals_style_Layout %>