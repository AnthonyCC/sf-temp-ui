<%@ page import='java.util.*, java.text.*' %>

<%@ page import='com.freshdirect.common.pricing.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.lists.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page errorPage='product_error.jsp' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	String catId = request.getParameter("catId");
	String productId = request.getParameter("productId");
	FDCartLineModel templateLine = null;
%>

<fd:ProductGroup id='productNode' categoryId='<%= catId %>' productId='<%= productId %>'>

<%
    String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
    String cartMode = CartName.MODIFY_LIST;

    String orderId = request.getParameter("orderId");
    String ccListIdStr = orderId == null ? (String)request.getParameter(CclUtils.CC_LIST_ID) : orderId;
    String lineId = request.getParameter("lineId");
  
    String successPage = ccListIdStr == null ? "/order/quickshop/all_lists.jsp" : "/order/quickshop/shop_from_list.jsp?" + CclUtils.CC_LIST_ID + "=" + ccListIdStr;
    String tagAction = request.getParameter("action");
    if (tagAction == null) tagAction = "CCL:ItemManipulate";

    // CCL
    request.setAttribute("quickshop.level","index");
    request.setAttribute(CclUtils.CC_LIST_ID,ccListIdStr);
    request.setAttribute("lineId",lineId);

%>

<% request.setAttribute("needsCCL","true"); %>

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
<fd:FDShoppingCart id='cart' result='result' action='<%= tagAction %>' source='CCL' successPage='<%= successPage %>'>
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
</fd:ProductGroup>
