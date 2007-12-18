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
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ include file="/shared/includes/product/i_product_methods.jspf" %>

<%
    FDUserI user = 		  (FDUserI) request.getAttribute("user");
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
    ContentNodeModel aliasNode = parentCat.getAttribute("ALIAS")!=null  
         ? ((CategoryRef) parentCat.getAttribute("ALIAS").getValue()).getCategory()
         : null;
    String alignment="align=\"right\"";
    String prodPageRatingStuff = getProdPageRatings(productNode,response); // get and format the product page ratings
%>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
        <tr><td><a href="/category.jsp?catId=<%=(aliasNode!=null ? aliasNode.getContentName() : request.getParameter("catId"))%>">Back to <%=(aliasNode!=null ? aliasNode.getFullName() : productNode.getParentNode().getFullName().toLowerCase())%></a></td></tr>
	<TR VALIGN="TOP">
	<TD WIDTH="250" ALIGN="center" CLASS="text11">
		<!-- Product transactional area include start -->
                <img src="/media_stat/images/layout/clear.gif" border="0" width="180" height="1"><br><br>
		<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
		<%@ include file="/shared/includes/product/i_product_image.jspf" %>
        </TD>

		<TD WIDTH="10"><IMG SRC="/media_stat/images/layout/clear.gif" WIDTH="10" HEIGHT="1" BORDER="0" HSPACE="0" VSPACE="0"></TD>

		<TD "<%=alignment%>" WIDTH="250" CLASS="text12">
                <img src="/media_stat/images/layout/clear.gif" border="0" width="250" height="1"><br>
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
        <oscache:cache time="300">
		<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
	</oscache:cache>
<%
	if (productNode.getAttribute("WINE_TYPE")!=null   || 
            productNode.getAttribute("WINE_REGION")!=null || 
	    productNode.getAttribute("WINE_FYI")!=null) {  %>
        <br><img src="/media_stat/images/layout/cccccc.gif" border="0" width="250" height="1"><br><br>
	<%@ include file="/shared/includes/product/wine_info.jspf" %><br>
<%  } %>
	<!-- Content end -->
		</td>
	</tr>
</table>
