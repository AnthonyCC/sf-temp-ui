<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%

 String orderIdParam = request.getParameter("orderId");
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' />
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
	String catId = request.getParameter("catId");
	String productId = request.getParameter("productId");
	
	FDCartLineModel templateLine = null;
%>
<fd:ProductGroup id='productNode' categoryId='<%= catId %>' productId='<%= productId %>'>
<%
	
    String snowFlakeImage = "/media_stat/images/template/snwflk_icon.gif";
    String cartMode = (String)request.getParameter("cartMode");
    if (cartMode == null || cartMode.length() == 0) cartMode = CartName.QUICKSHOP;

    String orderId = request.getParameter("orderId");
    String successPage = "/order/quickshop/shop_from_order.jsp";
	if (orderId!=null) {
		successPage += "?orderId=" + orderId;
	}

    String tagAction = request.getParameter("action")!=null ? request.getParameter("action") :  "addToCart";


%>

<% request.setAttribute("needsCCL","true"); %>

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Item Modify</tmpl:put>

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

<fd:FDShoppingCart id='cart' result='result' action='<%= tagAction %>' source='Quickshop' successPage='<%= successPage %>'>
<%
EnumProductLayout prodPageLayout = productNode.getProductLayout();
// if this is the wine product layout, then modification always uses the perishable product layout
if (prodPageLayout.equals(EnumProductLayout.WINE)) prodPageLayout= EnumProductLayout.PERISHABLE;

String productPage = prodPageLayout.getLayoutPath();

	request.setAttribute("actionResult", result);
	request.setAttribute("user", user);
	request.setAttribute("productNode", productNode);
	request.setAttribute("cartMode",cartMode);
	request.setAttribute("templateLine",templateLine);

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
