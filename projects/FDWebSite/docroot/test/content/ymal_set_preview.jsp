<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<fd:CheckLoginStatus />

<%
/*
 *  A page used for previewing ymal sets.
 *
 *  HTTP request parameters:
 *
 *  activeYmalSetId - the category id of the product.
 */

    String         activeYmalSetId  = request.getParameter("ymalSetId");
    ContentFactory cf               = ContentFactory.getInstance();
    YmalSet        activeYmalSet    = (YmalSet) cf.getContentNode(activeYmalSetId);
    FDUserI        user             = (FDUserI) session.getAttribute(SessionName.USER);
	String         spacer           = "/media_stat/images/layout/clear.gif";
    ProductModel   productNode      = null;

	List   relatedProducts   = null;
	List   relatedRecipes    = null;
	List   relatedCategories = null;
    String ymalHeader        = null;

	if (activeYmalSet != null) {
	    relatedProducts   = activeYmalSet.getYmalProducts();
	    relatedRecipes    = activeYmalSet.getYmalRecipes();
	    relatedCategories = activeYmalSet.getYmalCategories();

        if (activeYmalSet.getProductsHeader() != null) {
            ymalHeader = activeYmalSet.getProductsHeader();
        }
    }
%>
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='title' direct='true'>YMAL set preview for <%= activeYmalSetId %></tmpl:put>
    <tmpl:put name='content' direct='true'>
        <fd:FDShoppingCart id='cart' result='result' action='addMultipleToCart' successPage=''>
            <%
                String      cartMode     = CartName.ADD_TO_CART;
                FDCartLineI templateLine = null ;

                request.setAttribute("actionResult", result);
                request.setAttribute("user", user);
                request.setAttribute("cartMode", cartMode);
                request.setAttribute("templateLine", templateLine);
            %>
            <%@ include file="/includes/i_ymal_lists.jspf" %>
        </fd:FDShoppingCart>
        <hr/>
        NOTE: the 'add to cart' button does not work in ymal set preview.
    </tmpl:put>
</tmpl:insert>
