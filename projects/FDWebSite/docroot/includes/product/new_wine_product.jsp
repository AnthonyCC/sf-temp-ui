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

    boolean isWebApp = "WEB".equalsIgnoreCase(app);
    boolean _isModifyCart = cartMode.equals(CartName.MODIFY_CART);
    
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
<table border="0" cellspacing="0" cellpadding="0" width="430" align="center">
<tr><td><img src="/media_stat/images/layout/clear.gif" width="10" height="8"></td><td><img src="/media_stat/images/layout/clear.gif" width="255" height="8"></td><td><img src="/media_stat/images/layout/clear.gif" width="165" height="8"></td></tr>
	<tr valign="top">
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
		<td class="text12">
		<%-- if(!_isModifyCart ) {% >
			< %@include file="/shared/includes/product/i_wine_rating_review.jspf"%>
		< %}--%>
        <%@ include file="/shared/includes/product/i_show_promo_flag.jspf" %>
		<%@ include file="/shared/includes/product/i_product.jspf" %>
		<% if(qualifies && !productNode.isUnavailable()){%>
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><img src="/media_stat/images/template/offer_icon.gif" alt="Promotion icon"></td>
					<td><font class="title12">Free!<br></font><a href="promotion.jsp?cat=<%=request.getParameter("catId")%>">See our $<%=prefix%> offer</a></td>
				</tr>
			</table>
			<br>
		<%}%>
        <%-- Content start --%>
   		<% if ( FDStoreProperties.useOscache() ) { %> 
	        <oscache:cache time="300">
				<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
			</oscache:cache>
   		<% } else { %>			        
				<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
   		<% } %>
		</td>
		<td align="center" class="text11" style="padding-top:3px;">
				<!-- Product transactional area include start -->
				<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
				<%@ include file="/shared/includes/product/i_product_image.jspf" %>
		</td>
	</tr>
	<% if(!_isModifyCart ) {%>
		<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td><td colspan="2" style="padding-top:10px;"><%@ include file="/shared/includes/product/usq_wine_info.jspf" %></td></tr>
	<%}%>
</table>