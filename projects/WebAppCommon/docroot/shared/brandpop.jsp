<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.util.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
String brandId = request.getParameter("brandId");
String brandName = "";
BrandModel brandNode = null;
StringBuffer featProdStringBuffer = new StringBuffer();
int widthValue=585; //assume large for now.
String mediaPath = null;
String show = request.getParameter("show");

if (brandId!=null) {
    brandNode = (BrandModel)ContentFactory.getInstance().getContentNode(brandId);
    if (brandNode!=null) {
        brandName = brandNode.getFullName();
        Html _popupContent = brandNode.getPopupContent();
        if (_popupContent!=null) {
            TitledMedia tm = (TitledMedia)_popupContent;
            EnumPopupType popupType=EnumPopupType.getPopupType(tm.getPopupSize());
            if (show != null && !"".equals(show)) {
				mediaPath = show;
			} else {
				mediaPath = ((Html)tm.getMedia()).getPath() ;
			}
            widthValue = popupType.getWidth();
        }
        // Build the featured item list, if any
        List<ProductModel> favorites = brandNode.getFeaturedProducts();
        String imgName = null;
        boolean folderAsProduct = false;
        ContentNodeModel aliasNode = null;
        ContentNodeModel prodParent = null;
        ContentFactory contentFactory = ContentFactory.getInstance();
        Comparator priceComp = new ProductModel.PriceComparator();
        int favItemCount = favorites.size();
        int imgSizeSum = 0;
        int itemCount = 0;
        FDUserI sessionuser = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        PricingContext pc = sessionuser != null ? sessionuser.getPricingContext() : PricingContext.DEFAULT;
        
    %>
        <logic:iterate id='contentRef' collection="<%=favorites%>" type="java.lang.Object">
    <% 
            if ( !(contentRef instanceof ProductModel) && !(contentRef instanceof SkuModel)) { 
                continue;
            }
    		ProductModel product = null;
            String skuCode = null;
            if (contentRef instanceof SkuModel) {
                SkuModel sku = (SkuModel) contentRef;
                product = sku.getProductModel();
            } else {
                product = (ProductModel) contentRef;
            }
            
            if (product.isDiscontinued() || product.isUnavailable()) {
				favItemCount--;
				continue;
			}

            SkuModel sku = product.getDefaultSku(pc);
            if (sku==null) {
			favItemCount--;
			continue;
			}
			
            String prodPrice = null;
%>
            <fd:FDProductInfo id="productInfo" skuCode="<%=  sku.getSkuCode() %>">
<%   
            prodPrice = JspMethods.formatPrice(productInfo, pc);
%>  					
            </fd:FDProductInfo>
<%
            Image favAllImage = (Image)product.getCategoryImage();

            String productPageLink = "javascript:backtoWin('" + response.encodeURL("/product.jsp?catId=" + product.getParentNode()  + "&productId=" + product + (skuCode==null?"":"&skuCode="+skuCode))+"&trk=bpop"+"')";
               if (itemCount == 0) { //first prod
               featProdStringBuffer.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr valign=\"top\">");
               }
            featProdStringBuffer.append("<td align=\"center\"  width=\"20%\">");
            featProdStringBuffer.append("<a href=\"");
            featProdStringBuffer.append(productPageLink);
            featProdStringBuffer.append("\">");
            if (favAllImage !=null) {
                featProdStringBuffer.append("<img src=\"");
                featProdStringBuffer.append(favAllImage.getPath());
                featProdStringBuffer.append("\"");
                featProdStringBuffer.append(JspMethods.getImageDimensions(favAllImage));
                featProdStringBuffer.append(" border=\"0\" alt=\"");
                featProdStringBuffer.append(product.getFullName());
                featProdStringBuffer.append("\">");
            }
            featProdStringBuffer.append("</a><br>");
            featProdStringBuffer.append("<a href=\"");
            featProdStringBuffer.append(productPageLink);
            featProdStringBuffer.append("\">");
            featProdStringBuffer.append(product.getFullName()); 
            featProdStringBuffer.append("</a><br><b>");
            featProdStringBuffer.append(prodPrice);
            featProdStringBuffer.append("</b></td>");
            itemCount++;
            if (itemCount < 5 && itemCount < favItemCount ) {
            featProdStringBuffer.append("<td width=\"8\">");
            featProdStringBuffer.append("<img src=\"");
            featProdStringBuffer.append("media_stat/images/layout/clear.gif");
            featProdStringBuffer.append("\" width=\"8\" height=\"1\"></td>");
            }
			
            if (itemCount == 5 || itemCount == favItemCount) { //last prod
            break;
            } 
    %>
     </logic:iterate>
	 <% if (featProdStringBuffer.length() > 0) {
	 	featProdStringBuffer.append("</tr></table>");
		}
    }
}
%>
<title>FreshDirect - about <%= brandName %></title>

	<%@ include file="/common/template/includes/metatags.jspf" %>
	<%@ include file="/common/template/includes/i_javascripts.jspf" %>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body bgcolor="#FFFFFF" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.focus();" topmargin="10" marginheight="10">
<center>
<a name="top"></a>
<table border="0" cellpadding="0" cellspacing="0" width="520">
<tr>
	<td colspan="2" align="center">
	<%if( mediaPath!=null){%>
            <fd:IncludeMedia name='<%= mediaPath %>' />
     <%}%>
	</td>
</tr>
<%
    if (featProdStringBuffer.length()>0) {
%>
<tr><td colspan="2"><img src="/media_stat/images/template/about/brand_featured_items.gif" width="113" height="14" alt="Featured Items"><br><img src="/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="3"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="3"></td></tr>
<tr><td colspan="2" align="center"><%=featProdStringBuffer.toString()%><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"></td></tr>
<%
    }
%>
<tr>
	<td><img src="/media_stat/images/layout/clear.gif" width="260" height="20"><br><a href="#top">Back to top</a></td>
	<td align="right"><img src="/media_stat/images/layout/clear.gif" width="260" height="20"><br><a href="javascript:window.close();">Close window</a></td>
</tr>
</table>
</center>
<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>
</body>
</html>
