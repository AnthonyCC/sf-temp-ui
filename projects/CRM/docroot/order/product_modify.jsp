<%@ page import='java.util.*, java.text.*' %>

<%@ page import='com.freshdirect.common.pricing.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page errorPage='product_error.jsp' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	int cartLineNumber = Integer.parseInt(request.getParameter("cartLine"));
	
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);

	FDCartLineI templateLine = null;
	String catId = request.getParameter("catId");
	String productId = request.getParameter("productId");

	if (catId==null) {
		// it's a new request (only cartLine was specified)
		// !!! ugly, should use some tag to get cartline (but FDShoppingCart is a controller, not really suited for this)


        int orderLineIndex = user.getShoppingCart().getOrderLineIndex(cartLineNumber);
        if (orderLineIndex<0) {
        	// stale request, orderline no longer in cart, go to view cart
        	response.sendRedirect(response.encodeRedirectURL("/checkout/checkout_review_items.jsp"));
        	return;
       	}

		templateLine = (FDCartLineI) user.getShoppingCart().getOrderLine(orderLineIndex);

		catId = templateLine.getCategoryName();
		productId = templateLine.getProductName();
	}
%>

<fd:ProductGroup id='productNode' categoryId='<%= catId %>' productId='<%= productId %>'>

<%
    EnumProductLayout prodPageLayout = productNode.getProductLayout();
	
	String rawList = request.getParameter("search_pad");
%>

<fd:ParseSearchTerms searchParams="<%= rawList %>" isBulkSearch="true" searchFor="criteria" searchList="searchTerms">

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Product Modify</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="TOP">
		<TD WIDTH="1%">&nbsp;</TD>
		<TD WIDTH="59%" CLASS="text7gr"><BR>
			<!-- batch search items table-->
			<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0">
				<TR VALIGN="TOP">
					<TD WIDTH="100%" CLASS="text7gr">
<%	int termCounter = 0; %>
						<logic:iterate id="term" collection="<%= searchTerms %>" type="java.lang.String">
<%	if ( term.equalsIgnoreCase(criteria) ) { %>
						&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>" class="text8"><b><%= term %></b></a>&nbsp;
<%	} else {  %>
						&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>"><%= term %></a>&nbsp;
<% 	}
	termCounter++; %>
						</logic:iterate>
					</TD>
				</TR>
			</TABLE>
			<FONT CLASS="space4pix"><BR></FONT>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"><FONT STYLE="font-size:1px;color:CCCCCC">.</FONT></TD>
				</TR>
			</TABLE>
			<FONT CLASS="space4pix"><BR></FONT>
			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="550" ALIGN="CENTER">
				<TR VALIGN="TOP">
					<TD WIDTH="150" ALIGN="CENTER" CLASS="text14orangebold">&nbsp; <BR>MODIFY ITEM IN CART:</TD>
					<TD WIDTH="400">
					<!-- product page content goes here -->
						<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="400" ALIGN="CENTER">
						    <TR VALIGN="TOP">
							    <TD WIDTH="180" ALIGN="RIGHT" CLASS="text11">
									<!-- Product include start -->
<%
	String cartMode = CartName.MODIFY_CART;
    	String tgAction = request.getParameter("action")!=null ? request.getParameter("action") :  "changeOrderLine";
	String successPage;
	successPage = "/order/product.jsp?"+request.getQueryString()+"&catId="+productNode.getParentNode()+"&productId="+productNode;
%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tgAction %>' successPage='<%= successPage %>'>
<%	request.setAttribute("actionResult", result);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);
//     *** The wine product layout will be replaced with the  perishable product layout when doing modifications ***
	if (prodPageLayout.equals(EnumProductLayout.WINE)) prodPageLayout= EnumProductLayout.PERISHABLE;

// if this is configuredProduct layout, then use the ComponentGroup layout to render the modify screen
	if (prodPageLayout.equals(EnumProductLayout.CONFIGURED_PRODUCT)) prodPageLayout= EnumProductLayout.COMPONENTGROUP_MEAL;
%>
   <jsp:include page="<%=prodPageLayout.getLayoutPath()%>" flush="false"/>
</fd:FDShoppingCart>
									<!-- Product include end -->
								</TD>
							</tr>
						</table>
						<!-- end product page content -->
					</TD>
				</TR>
			</TABLE>
			<BR>
			<BR>
		</TD>
	</TR>
</TABLE>

</TABLE>
</div>

<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>
</fd:ParseSearchTerms>
</fd:ProductGroup>