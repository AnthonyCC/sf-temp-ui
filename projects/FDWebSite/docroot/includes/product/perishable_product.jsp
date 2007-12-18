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
//*** get needed vars from request attributes, they must exist or else we throw jsp error ***/

FDUserI user = 		  (FDUserI) request.getAttribute("user");
ProductModel productNode= (ProductModel)request.getAttribute("productNode");
String cartMode = 	  (String) request.getAttribute("cartMode");
FDCartLineI templateLine =(FDCartLineI) request.getAttribute("templateLine");
ActionResult result = 	  (ActionResult)request.getAttribute("actionResult");
if (result == null || productNode==null || cartMode==null || user==null ){
   throw new JspException(" One or several required request attributes are missing. ");
}

//String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
String prodPageRatingStuff = getProdPageRatings(productNode,response); // get and format the product page ratings
int templateType = productNode.getAttribute("TEMPLATE_TYPE", 1);

    if (productNode==null) {
            throw new JspException("Product not found in Content Management System");
    } else if (productNode.isDiscontinued()) {
            throw new JspException("Product Discontinued");
    }
    
    String app = (String)session.getAttribute(SessionName.APPLICATION);
    boolean isWebApp = app==null || "WEB".equalsIgnoreCase(app);
%>



<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
	<TR VALIGN="TOP">
	<TD WIDTH="187" ALIGN="RIGHT" CLASS="text11">
		<!-- Product include start -->

		<%@ include file="/shared/includes/product/i_product.jspf" %>

    </TD>

		<TD WIDTH="20"><IMG SRC="/media_stat/images/layout/clear.gif" WIDTH="20" HEIGHT="1" BORDER="0" HSPACE="0" VSPACE="0"></TD>
		<TD WIDTH="200" CLASS="text12">&nbsp;<BR>
<% if (cartMode.equals(CartName.ADD_TO_CART) && isWebApp) { %>

         <%@ include file="/shared/includes/product/i_show_promo_flag.jspf" %>
        <!-- Content start -->
        <oscache:cache time="300">
		<%@ include file="/shared/includes/product/i_also_sold_as.jspf" %>
		<%@ include file="/shared/includes/product/i_product_image.jspf" %>
		<%@ include file="/shared/includes/product/i_product_descriptions.jspf" %>
	</oscache:cache>
<% } else { 
	          SkuModel dfltSku =(SkuModel)productNode.getSkus().get(0);
		  FDProduct fdprod = dfltSku.getProduct();

		  boolean hasNutriOrIngrd = ( fdprod.hasNutritionFacts() || fdprod.hasIngredients());

%>
		<%@ include file="/shared/includes/product/i_product_image.jspf" %>

		<%	Html productDesc = productNode.getProductDescription();	%>

<%               if(productDesc != null){%>
		     <b>About</b> 
<%               }   %>
		<%@ include file="/shared/includes/product/i_product_about.jspf" %>

<% 		if (hasNutriOrIngrd) { %>
                       <br><A HREF="javascript:pop('/nutrition_info.jsp?catId=<%=productNode.getParentNode().getContentName() %>&productId=<%=productNode.getContentName() %>',335,375)">Nutrition, Ingredients, and Allergens</A>
<% 		} else { %><br>Please check product label for nutrition, ingredients, and allergens.<% } %><br><br>

<% }  %>

	<!-- Content end -->
		</td>
	</tr>
</table>
