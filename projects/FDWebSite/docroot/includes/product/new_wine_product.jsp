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
<%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
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
<table id="new_wine_prod_table" border="0" cellspacing="0" cellpadding="0" align="center" style="width: 430px;">
	<tr>
		<td style="width: 265px;">&nbsp;</td>
		<td style="width: 165px;">&nbsp;</td>
	</tr>
	<tr valign="top">
		<td class="text12" style="padding-left: 10px; width: 265px;">
		<% if (!_isModifyCart) { %><div style="padding-bottom: 20px; width: 265px;">
			<display:WineProductBackToLink/>
		</div><% } %>
		
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
		</td>
		<td align="center" class="text11" style="padding-top:3px; width: 165px;">
				<!-- Product transactional area include start -->
				<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
				<%@ include file="/shared/includes/product/i_product_image.jspf" %>
		</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 0px;">
			<%@ include file="/shared/includes/product/usq_wine_info.jspf" %>
		</td>
	</tr>
	<%-- RATINGS LEGEND --%>
	<tr>
		<td colspan="2" style="padding-left:3px;padding-right:2px;">
			<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
		</td>
	</tr>
	<%-- OTHER RATINGS LEGEND --%>
<%
	if (productNode.hasWineOtherRatings()) {
%>	<tr>
		<td colspan="2" style="padding-left:3px;padding-right:2px;">
			<fd:IncludeMedia name="/media/editorial/win_usq/other_ratings_key.html"/>
		</td>
		<td>&nbsp;</td>
	</tr>
<%		
	}
%>
</table>