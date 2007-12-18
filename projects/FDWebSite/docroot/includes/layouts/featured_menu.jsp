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


Collection sortedColl = (Collection) request.getAttribute("itemGrabberResult");
if (sortedColl==null) sortedColl = new ArrayList();
%>
<%!
        JspMethods.ContentNodeComparator contentNodeComparator = new JspMethods.ContentNodeComparator();
%>

<%
//**************************************************************
//***         the Featured Menu Pattern                       ***
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

    String deptName = parentNode.getFullName(); //getDepartmentPath(webconnect.getFolder().getString("path") );
    String prodNameAttribute = JspMethods.getProductNameToUse(currentFolder);
    String tablewidth="427";

 // don't display any heading if the favorite collection is empty or the products are unavailable.
    int favoritesShow = 0;
    Attribute attribFeatProds = currentFolder.getAttribute("FEATURED_PRODUCTS");

    List favorites = Collections.EMPTY_LIST; 
    if(currentFolder instanceof DepartmentModel) {
    	favorites = ((DepartmentModel) currentFolder).getFeaturedProducts();
    } else if (currentFolder instanceof CategoryModel) {
    	favorites = ((CategoryModel) currentFolder).getFeaturedProducts();
    } else {
    	if(attribFeatProds != null) {
    	    favorites = (List) attribFeatProds.getValue();
    	}
    }

    
    int foldersShown =0, productsShown=0;
    LinkedList productLinks = new LinkedList();
    LinkedList productPrices = new LinkedList();
    LinkedList unAvailableProds = new LinkedList();
    //LinkedList unAvailablePrices = new LinkedList();
    StringBuffer col1 = new StringBuffer(300);
    StringBuffer favoriteProducts = new StringBuffer(300);
	StringBuffer regularProducts = new StringBuffer(300);
    String imgName = null;
    boolean folderAsProduct = false;
    ContentNodeModel aliasNode = null;
    ContentNodeModel prodParent = null;
    ContentFactory contentFactory = ContentFactory.getInstance();
    Comparator priceComp = new ProductModel.PriceComparator();
%>
    <logic:iterate id='contentNode' collection="<%=favorites%>" type="com.freshdirect.fdstore.content.ProductModel">
<% 
        ProductModel product = contentNode; //(ProductModel)contentFactory.getProduct(contentRef.getCategoryId(),contentRef.getProductId());
        if (product.isDiscontinued() || product.isUnavailable()) continue;
        prodParent = product.getParentNode(); 
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

    String thisProdBrandLabel = product.getPrimaryBrandName();
        String productPageLink_ = response.encodeURL("product.jsp?catId=" + prodParent + "&productId=" + product+"&trk=feat");
        favAllImage = (Image)product.getDetailImage();
        favoriteProducts.append("<a href=\"");
        favoriteProducts.append(productPageLink_);
        favoriteProducts.append("\">");
        if (favAllImage !=null) {
            favoriteProducts.append("<img src=\"");
            favoriteProducts.append(favAllImage.getPath());
            favoriteProducts.append("\"");
            favoriteProducts.append(JspMethods.getImageDimensions(favAllImage));
            favoriteProducts.append(" border=\"0\" alt=\"");
            favoriteProducts.append(product.getFullName());
            favoriteProducts.append("\">");
        }
        favoriteProducts.append("</a><br>");
        favoriteProducts.append("<a href=\"");
        favoriteProducts.append(productPageLink_);
        favoriteProducts.append("\">");
        if (thisProdBrandLabel.length()>0) {
            favoriteProducts.append("<font class=\"text12bold\">");
            favoriteProducts.append(thisProdBrandLabel);
            favoriteProducts.append("</font><br>");
        }
		favoriteProducts.append("<font class=\"text12bold\">");
        favoriteProducts.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
		favoriteProducts.append("</font>");
        favoriteProducts.append("</a><br>");
		
		String subtitle = product.getAttribute("SUBTITLE","");
		 if (subtitle.length()>0) {
			favoriteProducts.append("<font class=\"text12\">");
			favoriteProducts.append(subtitle);
        	favoriteProducts.append("</font><br>");
        }
		favoriteProducts.append("<font class=\"favoritePrice\">");
        favoriteProducts.append(prodPrice);
        favoriteProducts.append("</font>");
        favoriteProducts.append("<br><span class=\"space8pix\"><br></span>");
        //favoritesShow++;
        //if (favoritesShow==2) break;  // only show 2 favorites.
%>
 </logic:iterate>
 
 <table width="<%=tablewidth%>" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="195" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="13" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="13" height="1"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="205" height="1"></td>
		</tr>
		<tr valign="top">
			
    <td align="center" class="text12">
	<img src="/media/images/headers/chefs_picks.gif" width="129" height="23"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="6"><br>
	<% if (favoriteProducts.length()>0) { %><%=favoriteProducts.toString()%><% } %>
	  </td>
			<td></td>
			<td bgcolor="#CCCCCC"></td>
			<td></td>
			<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br><img src="/media/images/headers/menu.gif" width="61" height="14"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
<%
    favoriteProducts=null;
    CategoryModel displayCategory = null;
    boolean gotAvailProdImg = false;
    StringBuffer appendColumn = new StringBuffer(200);
    StringBuffer appendColumnPrices = new StringBuffer(200);

%>
    <logic:iterate id='contentNode' collection="<%=sortedColl%>" type="com.freshdirect.fdstore.content.ContentNodeModel">
<% 
        ProductModel product;
		if (contentNode instanceof ProductModel) {
			 product = (ProductModel)contentNode;
		} else {
			continue;
		}
        if (product.isDiscontinued() || product.isUnavailable()) continue;
        prodParent = product.getParentNode(); 
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

    String thisProdBrandLabel = product.getPrimaryBrandName();
        String productPageLink_ = response.encodeURL("product.jsp?catId=" + prodParent + "&productId=" + product+"&trk=feat");
        
        regularProducts.append("<a href=\"");
        regularProducts.append(productPageLink_);
        regularProducts.append("\">");
        if (thisProdBrandLabel.length()>0) {
            regularProducts.append("<font class=\"text12bold\">");
            regularProducts.append(thisProdBrandLabel);
            regularProducts.append("</font><br>");
        }
		regularProducts.append("<font class=\"text12bold\">");
        regularProducts.append(product.getFullName().substring(thisProdBrandLabel.length()).trim()); 
		regularProducts.append("</font>");
        regularProducts.append("</a><br>");
		
		String subtitle = product.getAttribute("SUBTITLE","");
		 if (subtitle.length()>0) {
			regularProducts.append("<font class=\"text11\">");
			regularProducts.append(subtitle);
        	regularProducts.append("</font><br>");
        }
		regularProducts.append("<font class=\"favoritePrice\">");
        regularProducts.append(prodPrice);
        regularProducts.append("</font>");
        regularProducts.append("<br><span class=\"space8pix\"><br></span>");
%>
 </logic:iterate>
<% if (regularProducts.length()>0) { %><%=regularProducts.toString()%><% } %>
</td>
		</tr>
	</table>