<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>

<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	int cartLine = Integer.parseInt(request.getParameter("cartLine"));
	String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
	FDCartLineI templateLine = null;
	String catId = request.getParameter("catId");
	String productId = request.getParameter("productId");
    boolean isCallCenterApp = "callCenter".equalsIgnoreCase((String)session.getAttribute(SessionName.APPLICATION));

	if (catId==null) {
		// it's a new request (only cartLine was specified)

		// !!! ugly, should use some tag to get cartline (but FDShoppingCart is a controller, not really suited for this)
        FDCartModel cart = (FDCartModel) user.getShoppingCart();
        int orderLineIndex = cart.getOrderLineIndex(cartLine);
        if (orderLineIndex<0) {
        	// stale request, orderline no longer in cart, go to view cart
        	response.sendRedirect(response.encodeRedirectURL("/view_cart.jsp?trk=pmod"));
        	return;
       	}

		templateLine = (FDCartLineI) cart.getOrderLine(orderLineIndex);
		catId = templateLine.getCategoryName();
		productId = templateLine.getProductName();
	}

%>
<fd:ProductGroup id='productNode' categoryId='<%= catId %>' productId='<%= productId %>'>
<%
	if (productNode==null) {
		throw new JspException("Product not found in Content Management System");
	} else if (productNode.isDiscontinued()) {
		throw new JspException("Product Discontinued");
	}

	String cartMode = CartName.MODIFY_CART;
	String successPage;
	if (isCallCenterApp) {
		//tack on the product id and cat ID
		successPage = "/order/product.jsp?"+request.getQueryString()+"&catId="+productNode.getParentNode()+"&productId="+productNode;
	} else if (request.getParameter("returnPage") != null) {
		successPage = request.getParameter("returnPage");
	} else {
		successPage = "/view_cart.jsp";
	}
	String tagAction = request.getParameter("action")!=null ? request.getParameter("action") :  "changeOrderLine" ;
//	String formAction = request.getRequestURI() + "?" + request.getQueryString();

%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tagAction %>' successPage='<%= successPage %>'>
<tmpl:insert template='/common/template/no_nav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Modify Item in Cart <% // productNode.getFullName() %></tmpl:put>

    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>

<tmpl:put name='content' direct='true'>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
<TR VALIGN="TOP">
<TD WIDTH="400">
<img src="/media_stat/images/navigation/modify_item_in_cart.gif" width="367" height="25" border="0" alt="MODIFY ITEM IN CART"><BR>
<FONT CLASS="space4pix"><BR></FONT>
This item is now in your cart. After making changes to it, click "save changes" below. To remove it from your cart, click "remove item." To return to the page where you bought it, <A HREF="/product.jsp?productId=<%= productNode %>&catId=<%= productNode.getParentNode() %>&trk=pmod">click here</A>.<br><br>
<%@ include file="/includes/product/cutoff_notice.jspf" %>
<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
<br>
</TD>
</TR>
</TABLE>
<%
	request.setAttribute("actionResult", result);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);

EnumProductLayout prodPageLayout = productNode.getProductLayout();
// if this is the wine product layout, then modification always uses the perishable product layout
if (prodPageLayout.equals(EnumProductLayout.WINE)) prodPageLayout= EnumProductLayout.PERISHABLE;

// if this is configuredProduct layout, then use the ComponentGroup layout to render the modify screen
if (prodPageLayout.equals(EnumProductLayout.CONFIGURED_PRODUCT)) prodPageLayout= EnumProductLayout.COMPONENTGROUP_MEAL;

String productPage = prodPageLayout.getLayoutPath();
%>
<jsp:include page="<%=productPage%>" flush="false"/>
</tmpl:put>

</tmpl:insert>
</fd:FDShoppingCart>

</fd:ProductGroup>
