<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
ContentFactory cf = ContentFactory.getInstance();
ProductModel pm = cf.getProductByName(request.getParameter("catId"), request.getParameter("prodId"));
Image prodImage = (Image)pm.getCategoryImage();
System.out.println(request.getParameter("prodId"));
SkuModel sku = pm.getDefaultSku();
String prodPrice = null;
    if (sku!=null) { 
            
%>
    <fd:FDProductInfo id="productInfo" skuCode="<%=  sku.getSkuCode() %>">
<%   
    prodPrice = JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())+"/"+ productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
%>  					
    </fd:FDProductInfo>
<%        }%>
document.write('<td width="92">');
document.write('    <A HREF="product.jsp?<%=request.getParameter("catId")%>&productId=<%=request.getParameter("prodId")%>&trk=feat">');
document.write('<img SRC="<%=prodImage.getPath()%>" <%=JspMethods.getImageDimensions(prodImage)%>  border="0">');
document.write('    </A>');
document.write('<BR>');
document.write("    <A HREF='product.jsp?<%=request.getParameter("catId")%>&<%=request.getParameter("prodId")%>'><%=pm.getFullName()%></A><BR>");
document.write('    <%if(prodPrice != null){%><font class="favoritePrice"><%=prodPrice%></font><%}%></td>');
document.write('<td width="10"><IMG SRC="media_stat/images/layout/clear.gif" width="8" height="1"></td>');
