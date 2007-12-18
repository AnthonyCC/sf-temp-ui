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
    String successPage = "/quickshop/quick_shop_confirm.jsp";
	if (orderId!=null) {
		successPage += "?orderId=" + orderId;
	}

    String tagAction = request.getParameter("action")!=null ? request.getParameter("action") :  "addToCart";


%>
<fd:FDShoppingCart id='cart' result='result' action='<%= tagAction %>' source='Quickshop' successPage='<%= successPage %>'>
<tmpl:insert template='/common/template/quick_shop.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= CartName.ACCEPT_ALTERNATIVE.equals(cartMode) ? "Recommended Alternative" : "Modify Item" %> - <%= productNode.getFullName() %></tmpl:put>
    <%--tmpl:put name='banner' direct='true'><a href="/newproducts.jsp"><img src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif" width="140" height="108" border="0"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10"><br></tmpl:put--%>

<tmpl:put name='content' direct='true'>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
<TR VALIGN="TOP">
<TD WIDTH="400">
<FONT CLASS="space4pix"><BR><BR></FONT>
<% boolean altDisplay = "true".equals(request.getParameter("alt"));
   String headerIm = altDisplay ?
      "/media_stat/images/template/quickshop/rec_alt_hdr_orng.gif":
      "/media_stat/images/navigation/modify_item_in_list.gif";
   String productLink = "/product.jsp?catId=" + request.getParameter("catId") + "&productId=" + request.getParameter("productId") + "&trk=" + ( altDisplay ? "alt" : "qsmod" );
%>

<img src="<%= headerIm %>" border="0" alt="MODIFY ITEM OR ACCEPT ALTERNATIVE" VSPACE="4"><BR>
<% if (altDisplay) { %>
This is our recommended alternative for an unavailable item from your previous purchases. To purchase this item, click "add to cart" below. To find out more information about this alternative item,
<a href="<%=productLink%>">click here</a>.
<% } else { %>
Make changes to this item, then click "add to cart" below. To find out more information about this item, <A href="<%=productLink%>">click here</A>.
<% } %>
<br><br></TD>
</TR>
</TABLE>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
<TR VALIGN="TOP">
    <TD WIDTH="187" ALIGN="RIGHT" CLASS="text11">
        <!-- Product include start -->
		
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
          </TD>
		
</TR>
</TABLE>
<script language="JavaScript" src="/assets/javascript/quickshop.js"></script>
</tmpl:put>

</tmpl:insert>
</fd:FDShoppingCart>
</fd:ProductGroup>
