<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.fdstore.ecoupon.*"%>

<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<% //expanded page dimensions
final int W_QUICKSHOP_CCL_ITEM_MODIFY_LEFT = 218;
final int W_QUICKSHOP_CCL_ITEM_MODIFY_TOTAL = 601;
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
    if (cartMode == null || cartMode.length() == 0) cartMode = CartName.MODIFY_LIST;

    //String orderId = null; // needs to be declared for i_vieworder
    String ccListIdStr = (String)request.getParameter(CclUtils.CC_LIST_ID);
    String lineId = request.getParameter("lineId");
	String qcType = request.getParameter("qcType");
    
	String successPage;
	if (ccListIdStr == null) {
		successPage = "/quickshop/all_lists.jsp";
	} else {
		if (QuickCart.PRODUCT_TYPE_SO.equals(qcType)) {
			successPage = "/quickshop/so_details.jsp?" + CclUtils.CC_LIST_ID + "=" + ccListIdStr;
		} else {
			successPage = "/quickshop/shop_from_list.jsp?" + CclUtils.CC_LIST_ID + "=" + ccListIdStr;
		}
	}
	
    // String successPage = ccListIdStr == null ? "/quickshop/all_lists.jsp" : "/quickshop/shop_from_list.jsp?" + CclUtils.CC_LIST_ID + "=" + ccListIdStr;
    String tagAction = request.getParameter("action");
    if (tagAction == null) tagAction = "CCL:ItemManipulate";

    // CCL
    //request.setAttribute("quickshop.level","index");
    request.setAttribute(CclUtils.CC_LIST_ID,ccListIdStr);
    request.setAttribute("lineId",lineId);
%>
<fd:FDShoppingCart id='cart' result='result' source='CCL' action='<%= tagAction %>' successPage='<%= successPage %>'>
 <tmpl:insert template='/common/template/quick_shop.jsp'> 
    <tmpl:put name='title' direct='true'>
       FreshDirect - <%= CartName.ACCEPT_ALTERNATIVE.equals(cartMode) ? "Recommended Alternative" : "Modify Shopping List Item" %> - <%= productNode.getFullName() %>
    </tmpl:put>
<%--   <tmpl:put name="seoMetaTag" direct="true"> --%>
<%--     <fd:SEOMetaTag title=" FreshDirect - <%= CartName.ACCEPT_ALTERNATIVE.equals(cartMode) ? 'Recommended Alternative' : 'Modify Shopping List Item' %> - <%= productNode.getFullName() %>"></fd:SEOMetaTag> --%>
<%--   </tmpl:put> --%>

<%
   String productLink = (String)request.getAttribute("productLink");
   if (productLink == null || "".equals(productLink)) productLink = (String)request.getParameter("productLink");
   if (productLink == null || "".equals(productLink)) productLink = request.getQueryString();
   request.setAttribute("productLink",productLink);
%>
<tmpl:put name='content' direct='true'>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_QUICKSHOP_CCL_ITEM_MODIFY_TOTAL %>">
<TR VALIGN="TOP">
<TD WIDTH="<%= W_QUICKSHOP_CCL_ITEM_MODIFY_TOTAL %>">
<FONT CLASS="space4pix"><BR><BR></FONT>
<% String headerIm = CartName.ACCEPT_ALTERNATIVE.equals(cartMode) ? 
       "/media_stat/images/template/quickshop/rec_alt_hdr_purp.gif":
       "/media_stat/images/template/quickshop/list_modify_hdr.gif";
%>
<img src="<%= headerIm %>" border="0" alt="MODIFY ITEM OR ACCEPT ALTERNATIVE" VSPACE="4"><BR>
<% if (CartName.ACCEPT_ALTERNATIVE.equals(cartMode)) { %>
This is our recommended alternative for an unavailable item in your shopping list. Click the "replace item" button to remove the 
unavailable item from your shopping list and replace it with this alternative. To find our more information about this item, 
<a href="/product.jsp?<%=productLink%>">click here</a>.
<% } else { %>
This item is on your shopping list. After making changes to it, click "save changes" below.
To remove it from your list click "remove item". To return to the page where you bought it, <a href="/product.jsp?<%=productLink%>">click here</a>.
<% } %>
<br><br></TD>
</TR>
</TABLE>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="407">
<TR VALIGN="TOP">
    <TD WIDTH="<%= W_QUICKSHOP_CCL_ITEM_MODIFY_LEFT %>" ALIGN="RIGHT" CLASS="text11">
        <!-- Product include start -->
		
<%
request.setAttribute("actionResult", result);
request.setAttribute("user", user);
request.setAttribute("productNode", productNode);
request.setAttribute("cartMode",cartMode);
request.setAttribute("templateLine",templateLine);
request.setAttribute("qcType", request.getParameter("qcType"));
FDCustomerCoupon custCoupon = null;
if (productNode != null && productNode.getDefaultSku() != null && productNode.getDefaultSku().getProductInfo() != null) {
	custCoupon = user.getCustomerCoupon(productNode.getDefaultSku().getProductInfo(), EnumCouponContext.PRODUCT,productNode.getParentId(),productNode.getContentName());
}
request.setAttribute("custCoupon", custCoupon); //set coupon in to request for includes/tags to use

EnumProductLayout prodPageLayout = productNode.getProductLayout();
// if this is the wine product layout, then modification always uses the perishable product layout
if (prodPageLayout.equals(EnumProductLayout.WINE)) prodPageLayout= EnumProductLayout.PERISHABLE;

// if this is configuredProduct layout, then use the ComponentGroup layout to render the modify screen
if (prodPageLayout.equals(EnumProductLayout.CONFIGURED_PRODUCT)) prodPageLayout= EnumProductLayout.COMPONENTGROUP_MEAL;

String productPage = prodPageLayout.getLayoutPath();

%>
<% if (request.getParameter("recipeId") != null) {
%>
   <input type="hidden" name="recipeId" value="<%=request.getParameter("recipeId")%>"/>
<%
   }
%>
<jsp:include page="<%=productPage%>" flush="false"/>
          </TD>
		
</TR>
</TABLE>
<fd:javascript src="/assets/javascript/quickshop.js"/>
</tmpl:put>

</tmpl:insert>
</fd:FDShoppingCart>
</fd:ProductGroup>
