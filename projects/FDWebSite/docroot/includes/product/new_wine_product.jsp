<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.io.*'%>
<%@ page import='java.text.SimpleDateFormat'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.content.view.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.storeapi.fdstore.FDContentTypes"%>
<%@ page import='java.net.URLEncoder' %>
<%@ page import='java.util.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%@ page import='com.freshdirect.storeapi.util.ProductInfoUtil'%>

<% //expanded page dimensions
final int W_NEW_WINE_PRODUCT_TOTAL = 601;
final int W_NEW_WINE_PRODUCT_LEFT = 428;
final int W_NEW_WINE_PRODUCT_CENTER_PADDING = 14;
final int W_NEW_WINE_PRODUCT_RIGHT = 165;
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
<table id="new_wine_prod_table" border="0" cellspacing="0" cellpadding="0" align="center" style="width: <%=W_NEW_WINE_PRODUCT_TOTAL%>px;">
	<tr>
		<td style="width: <%=W_NEW_WINE_PRODUCT_LEFT%>px;">&nbsp;</td>
		<td style="width: <%=W_NEW_WINE_PRODUCT_RIGHT%>px;"></td>
	</tr>
	<tr>
		<td colspan="2" nowrap="nowrap">
		 <table style="width:100%"><tr><td>
		 <% if (!_isModifyCart && (null==app || isWebApp)) { %><span style="padding-bottom: 20px; width: <%=W_NEW_WINE_PRODUCT_LEFT%>px;">
			<display:WineProductBackToLink/>
		</span><% } %>
		</td>
		<td width="360px"><%@ include file="/includes/product/i_product_soc_buttons.jspf" %></td>
		</tr></table>
		</td>
	</tr>
	<tr valign="top">
	<td <%=alignment%> class="text12" style="width: <%=W_NEW_WINE_PRODUCT_LEFT%>px;  padding-top: 20px;">
		
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
		<td align="center" class="text11" style="padding-top:3px; width: <%=W_NEW_WINE_PRODUCT_RIGHT%>px;">
				<!-- Product transactional area include start -->
        <c:set var="useProdImage" value="${true}"/>
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
	<%


	EnumWineRating __rating = EnumWineRating.NOT_RATED;
	try {
		__rating = EnumWineRating.getEnumByRating( productNode.getProductRatingEnum() );
	} catch (FDResourceException e) {
	}
	
	if (__rating != EnumWineRating.NOT_RATED && (parentCat != null && !parentCat.isHideWineRatingPricing()) ) {
	%>
	<tr>
		<td colspan="2">
			<%@ include file="/shared/includes/wine/i_wine_expert_ratings_key.jspf" %>
		</td>
	</tr>

	<%
	}
	%>
	<%-- OTHER RATINGS LEGEND --%>
<%
	if (productNode.hasWineOtherRatings()) {
		String otherRatingsMediapath = "/media/editorial/win_"+JspMethods.getWineAssociateId().toLowerCase()+"/other_ratings_key.html";
%>	<tr>
		<td colspan="2">
			<fd:IncludeMedia name="<%= otherRatingsMediapath %>"/>
		</td>
	</tr>
<%		
	}
%>
</table>
