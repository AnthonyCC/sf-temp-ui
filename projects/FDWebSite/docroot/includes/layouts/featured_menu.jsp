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

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

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
//Siva-Changed Tracking Code Retrieval
String trkCode = (String)request.getAttribute("trk");


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

    TreeMap deferDisplayProds = new TreeMap(contentNodeComparator);
    HashMap deferDisplayPrices = new HashMap();
    HashMap deferDisplayImages   = new HashMap();
    Image favAllImage = null;
    String imagePath = null;
    String imageDim = "";

	StringBuffer regularProducts = new StringBuffer(300);
    ContentNodeModel prodParent = null;
    Comparator priceComp = new ProductModel.PriceComparator();
%>
 
 <table width="427" cellpadding="0" cellspacing="0" border="0">
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
<%
	// TEST CategoryModel testCat = (CategoryModel) ContentFactory.getInstance().getContentNode("cof_ef_org");
	// System.err.println("TEST CAT: " + testCat);
%>
<fd:FeaturedItemsRecommendations id="recommendations"  currentNode="<%= currentFolder %>" itemCount="4"><%
	if (recommendations != null && recommendations.getProducts().size() > 0) {
	    request.setAttribute("recommendationsRendered","true");
	    int ord=1;
%>
<logic:iterate id='contentNode' collection="<%= recommendations.getProducts() %>" type="com.freshdirect.fdstore.content.ProductModel"><%
			ProductModel productNode = contentNode;
			ProductLabeling pl = new ProductLabeling((FDUserI) session.getAttribute(SessionName.USER), productNode);
			String fiRating = "";
			String fiProdPrice = null;
			String fiSubtitle = productNode.getSubtitle();
%><fd:FDProductInfo id="productInfo" skuCode="<%= productNode.getDefaultSku().getSkuCode() %>"><%
			fiProdPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
%></fd:FDProductInfo><%
			String actionURI = FDURLUtil.getProductURI(productNode, recommendations.getVariant().getId(), "feat", pl.getTrackingCode(), ord, recommendations.getImpressionId(productNode));
%>			<p style="border: 0px; padding: 0px; margin: 0px;">
			<display:ProductImage product="<%= productNode %>" action="<%= actionURI %>"/></p>
			<display:ProductRating product="<%= productNode %>" />
			<display:ProductName product="<%= productNode %>" action="<%= actionURI %>"/><br/>
			<% if (fiSubtitle.length() > 0) { %><span class="text12"><%= fiSubtitle %></span><br><% } %>
			<span class="favoritePrice"><%= fiProdPrice %></span><br>
			<span class="space8pix"><br></span><%
			++ord;
%></logic:iterate><%
	}
%>
</fd:FeaturedItemsRecommendations>
	</td>
			<td></td>
			<td bgcolor="#CCCCCC"></td>
			<td></td>
			<td class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="5"><br><img src="/media/images/headers/menu.gif" width="61" height="14"><br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br>
<%

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