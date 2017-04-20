<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.cms.util.ProductInfoUtil'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<% //expanded page dimensions
final int W_WINE_PRODUCT_TOTAL = 601;
final int W_WINE_PRODUCT_LEFT = 273;
final int W_WINE_PRODUCT_CENTER_PADDING = 14;
final int W_WINE_PRODUCT_RIGHT = 314;
%>

<%@ include file="/shared/includes/product/i_product_methods.jspf" %>

<%
    FDUserI user = 		  (FDUserI) request.getAttribute("user");
	String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId(); 
    ProductModel productNode= (ProductModel)request.getAttribute("productNode");
    String cartMode = 	  (String) request.getAttribute("cartMode");
    FDCartLineI templateLine =(FDCartLineI) request.getAttribute("templateLine");
    ActionResult result = 	  (ActionResult)request.getAttribute("actionResult");
    if (result == null || productNode==null || cartMode==null || user==null ){
        throw new JspException(" One or several required request attributes are missing. ");
     }

     
    String app = (String)session.getAttribute(SessionName.APPLICATION);

    if (productNode==null) {
            throw new JspException("Product not found in Content Management System");
    } else if (productNode.isDiscontinued()) {
            throw new JspException("Product Discontinued");
    }

    CategoryModel parentCat = (CategoryModel)productNode.getParentNode();
    ContentNodeModel aliasNode = parentCat.getAlias();
    String alignment="align=\"left\"";
    String prodPageRatingStuff = getProdPageRatings(productNode,response); // get and format the product page ratings
%>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%=W_WINE_PRODUCT_TOTAL%>">
        <tr><td><a href="/category.jsp?catId=<%=(aliasNode!=null ? aliasNode.getContentName() : request.getParameter("catId"))%>">Back to <%=(aliasNode!=null ? aliasNode.getFullName() : productNode.getParentNode().getFullName().toLowerCase())%></a></td></tr>
	<TR VALIGN="TOP">
	<TD WIDTH="<%=W_WINE_PRODUCT_LEFT%>" ALIGN="center" CLASS="text11">
		<!-- Product transactional area include start -->
                <img src="/media_stat/images/layout/clear.gif" alt="" border="0" width="<%=W_WINE_PRODUCT_LEFT%>" height="1"><br><br>
		<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
		<%@ include file="/shared/includes/product/i_product_image.jspf" %>
        </TD>

		<TD WIDTH="<%=W_WINE_PRODUCT_CENTER_PADDING%>"><IMG SRC="/media_stat/images/layout/clear.gif" alt="" WIDTH="<%=W_WINE_PRODUCT_CENTER_PADDING%>" HEIGHT="1" BORDER="0" HSPACE="0" VSPACE="0"></TD>

		<TD <%=alignment%> WIDTH="<%=W_WINE_PRODUCT_RIGHT%>" CLASS="text12">
                <img src="/media_stat/images/layout/clear.gif" alt="" border="0" width="<%=W_WINE_PRODUCT_RIGHT%>" height="1"><br>
                <%@ include file="/shared/includes/product/i_show_promo_flag.jspf" %>
		<%@ include file="/shared/includes/product/i_product.jspf" %>
		<% if(qualifies && !productNode.isUnavailable()){%>
			<table>
				<tr>
					<td><img src="/media_stat/images/template/offer_icon.gif" alt="Promotion icon"></td>
					<td><font class="title12">Free!<br></font><A HREF="promotion.jsp?cat=<%=request.getParameter("catId")%>">See our $<%=prefix%> offer</a></td>
				</tr>
			</table>
			<br>
		<%} %>
        <!-- Content start -->
  		<% if ( FDStoreProperties.useOscache() ) { %> 
	        <oscache:cache time="300">
				<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
			</oscache:cache>
  		<% } else { %>			        
				<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
  		<% } %>
<%
	if (productNode.getWineType()!=null   || 
            productNode.getWineRegion()!=null || 
	    productNode.getWineFyi()!=null) {  %>
        <br><img src="/media_stat/images/layout/cccccc.gif" alt="" border="0" width="<%=W_WINE_PRODUCT_RIGHT%>" height="1"><br><br>
	<%@ include file="/shared/includes/product/wine_info.jspf" %><br>
<%  } %>
	<!-- Content end -->
		</td>
	</tr>
</table>
