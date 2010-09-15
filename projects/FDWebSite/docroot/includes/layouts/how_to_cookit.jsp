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
<%@ page import='com.freshdirect.fdstore.pricing.ProductPricingFactory' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%!
        JspMethods.ContentNodeComparator contentNodeComparator = new JspMethods.ContentNodeComparator();
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


// boolean onlyOneProduct = false;
// ProductModel theOnlyProduct = null;
//Siva-Changed Tracking Code Retrievel
String trkCode = (String)request.getAttribute("trk");

Collection<CategoryModel> sortedColl = (Collection<CategoryModel>) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = Collections.<CategoryModel>emptyList();
//**************************************************************
//***         the How To cook it Patterrn                    ***
//**************************************************************
    ContentNodeModel owningFolder = currentFolder;

    // TreeMap deferDisplayProds = new TreeMap(contentNodeComparator);
    // HashMap deferDisplayPrices = new HashMap();
    // HashMap deferDisplayImages   = new HashMap();
    // Image favAllImage = null;
    boolean showPrices = ((ProductContainer)currentFolder).isFavoriteShowPrice();
    boolean folderShown = false;
    String imagePath = null;
    String imageDim = "";
    /// String clearImage = "/media_stat/images/layout/clear.gif";
    /// String unAvailableImg="/media_stat/images/template/not_available.gif";
    String notAvailImgName = "";
    ContentNodeModel parentNode = currentFolder.getParentNode();
    while (parentNode!=null && !(parentNode instanceof DepartmentModel)) {
        parentNode = parentNode.getParentNode();
    }

    String deptName = parentNode.getFullName(); //getDepartmentPath(webconnect.getFolder().getString("path") );
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    String tablewidth="400";
    String tdwidth="92";


 // don't display any heading if the favorite collection is empty or the products are unavailable.
    int favoritesShow = 0;
    List<ProductModel> favorites =  ((ProductContainer)currentFolder).getFeaturedProducts();
    /// int foldersShown =0, productsShown=0;
    LinkedList<String> productLinks = new LinkedList<String>();
    LinkedList<String> productPrices = new LinkedList<String>();
    LinkedList<String> unAvailableProds = new LinkedList<String>();
    LinkedList<String> unAvailablePrices = new LinkedList<String>();
    StringBuffer col1 = new StringBuffer();
    StringBuffer favoriteProducts = new StringBuffer();
    String imgName = null;
    boolean folderAsProduct = false;
    // ContentNodeModel aliasNode = null;
    // ContentNodeModel prodParent = null;

    Comparator<SkuModel> priceComp = new ProductModel.PriceComparator();
%>
    <logic:iterate id='contentRef' collection="<%=favorites%>" type="com.freshdirect.fdstore.content.ProductModel">
<% 
        ProductModel product = ProductPricingFactory.getInstance().getPricingAdapter(contentRef, user.getPricingContext()); //(ProductModel)contentFactory.getProduct(contentRef.getCategoryId(),contentRef.getProductId());
        if (product.isDiscontinued() || product.isUnavailable()) continue;
        ContentNodeModel prodParent = product.getParentNode(); 
        List<SkuModel> skus = product.getSkus(); 
        if (prodParent==null || !(prodParent instanceof CategoryModel)) continue;

	for (ListIterator<SkuModel> li=skus.listIterator(); li.hasNext(); ) {
		SkuModel sku = li.next();
       	if ( sku.isUnavailable() ) {
			li.remove();
		}
	}
        int skuSize = skus.size();

        SkuModel sku = null;
        String prodPrice = null;
        if (skuSize==0) continue;  // skip this item..it has no skus.  Hmmm?
        if (skuSize==1) {
            sku = skus.get(0);  // we only need one sku
        }
        else {
            sku = Collections.min(skus, priceComp);
        }
%>
        <fd:FDProductInfo id="productInfo" skuCode="<%= sku.getSkuCode() %>">
<% 
        prodPrice = JspMethods.formatPrice(productInfo, user.getPricingContext());
%>						
        </fd:FDProductInfo>
<%
        String productPageLink_ = response.encodeURL("product.jsp?catId=" + prodParent + "&productId=" + product+"&trk=htci");
        Image favAllImage = product.getCategoryImage();
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
        favoriteProducts.append("<font class=\"favoritePrice\">");
        favoriteProducts.append(prodPrice);
        favoriteProducts.append("</font>");
        favoriteProducts.append("</TD>");
        favoriteProducts.append("<TD WIDTH=\"10\">");
        favoriteProducts.append("<IMG SRC=\"");
        favoriteProducts.append("media_stat/images/layout/clear.gif");
        favoriteProducts.append("\" WIDTH=\"8\" HEIGHT=\"1\"></TD>");
        favoritesShow++;
        if (favoritesShow==4) break;  // only show 4 favoites.
%>
 </logic:iterate>
<%
    if (favoriteProducts.length()>0) {
%>
<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%=tablewidth%>">
<TR VALIGN="TOP">
	<TD CLASS="text12bold" WIDTH="<%=tablewidth%>">
		Our Favorites
	</TD>
</TR>
</TABLE>
<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="<%=tablewidth%>">
<TR VALIGN="TOP" ALIGN="CENTER">
	<%=favoriteProducts.toString()%>
</TR>
</TABLE>
<font class="space4pix"><BR></font>
<%
    }
    favoriteProducts=null;
    boolean gotAvailProdImg = false;
%>
    <logic:iterate id='displayCategory' collection="<%= sortedColl %>" type="com.freshdirect.fdstore.content.CategoryModel">
<%
		//CategoryModel displayCategory = null;
		//displayCategory=(CategoryModel)contentNode;
%>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" WIDTH="400">
<TR VALIGN="MIDDLE">
	<TD WIDTH="400" COLSPAN="4"><IMG src="/media_stat/images/layout/clear.gif" WIDTH="1" HEIGHT="4" border="0"></TD>
</TR>
<TR VALIGN="MIDDLE">
	<TD WIDTH="400" COLSPAN="4" CLASS="title11"><%=displayCategory.getFullName()%></td></tr>
<%
        Html introCpyAttrib = displayCategory.getEditorial();
        String introCopyPath = introCpyAttrib==null?"":introCpyAttrib.getPath();
        if ( introCopyPath!=null && introCopyPath.trim().length() > 0) {
%>
<%//gray line%>
<tr><td width="400" colspan="4" class="text11"><img src="/media_stat/images/layout/cccccc.gif" width="400" height="1" alt="" border="0"><br>
<img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"><br>

<fd:IncludeMedia name='<%= introCopyPath %>' /><br><br></TD></TR>
<%  }%>
</TABLE>
<%
               // get the list of products that are eligible to be prepared for this group
               List<ProductModel> htciProdList = displayCategory.getHowToCookItProducts();
               if (htciProdList==null || htciProdList.size() ==0) continue;
                col1.setLength(0);
                productLinks.clear();
                productPrices.clear();
                gotAvailProdImg = false;
%>                
    <logic:iterate id='product' collection="<%=htciProdList%>" type="com.freshdirect.fdstore.content.ProductModel">
<% 
        if (product.isDiscontinued() || product.isUnavailable()) continue;
        CategoryModel prodCategory = (CategoryModel)product.getParentNode(); 

            //favAllImage = null;
            imageDim = "";
            imagePath = "";
            imgName = "Img"+displayCategory;
            notAvailImgName = imgName+"_unavailable";
            imagePath = null;
            imageDim = null;

            imagePath = product.getCategoryImage().getPath();
            imageDim = JspMethods.getImageDimensions(product.getCategoryImage());

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
                    gotAvailProdImg = true;                        
                    col1.append("\"");
                    col1.append(imageDim);
                    col1.append(">");
                }
            }

            StringBuffer appendColumn = new StringBuffer(50);
            StringBuffer appendColumnPrices = new StringBuffer(20);
            String lstUnitPrice = "&nbsp;"; //product.getDisplayableListPrice(webconnect.getPriceList());

            String prodURL = response.encodeURL("/product.jsp?productId=" + product + "&catId=" + prodCategory+"&trk=htci");
            appendColumn.append("<div style=\"margin-left: 8px; text-indent: -8px;\"><A HREF=\"");
            appendColumn.append(prodURL);
            appendColumn.append("\" onMouseover='");
            appendColumn.append("swapImage(\""+imgName+"\",\""+((Image)product.getCategoryImage()).getPath()+"\"");
            appendColumn.append(")'>");
            appendColumn.append(JspMethods.getDisplayName(product,prodNameAttribute));
            appendColumn.append("</A></div>");
            appendColumnPrices.append(lstUnitPrice);

            productLinks.add(appendColumn.toString());
            productPrices.add(appendColumnPrices.toString());
	%></logic:iterate>
<%
                String outputProducts= JspMethods.displayFAProducts(productLinks, productPrices,showPrices,false);
%>
<TABLE CELLSPACING="0" CELLPADDING="1" BORDER="0" WIDTH="400">
<TR VALIGN="TOP">
	<TD width="90"><%=col1.toString()%></td><%=outputProducts%></TR>
</Table>
</logic:iterate>
