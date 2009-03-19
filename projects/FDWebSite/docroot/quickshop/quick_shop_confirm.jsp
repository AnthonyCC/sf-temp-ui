<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.lists.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import="com.freshdirect.customer.ErpDiscountLineModel" %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>

<script language="javascript" src="/assets/javascript/common_javascript.js"></script>
<%! java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);%>
<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>
<%! java.text.DateFormat dateFormatter = new java.text.SimpleDateFormat("EEE MM/dd/yy");%>
<%  
	request.setAttribute("confirmation.location","quickshop");
	String fromPage = null;
	String spacer = "/media_stat/images/layout/clear.gif";
	String servlet_context = request.getContextPath();
	String qsDeptId = request.getParameter("qsDeptId");
	boolean hasDeptId = qsDeptId != null && !"".equals(qsDeptId);
	boolean showLink = false;
	boolean selected = false;
	String qsMenuLink = "";
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	String cartType="";
	String orderId="";
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
	request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='/quickshop/index_guest.jsp' />
<fd:QuickShopController id="quickCart">

<%
	int n = quickCart.numberOfProducts();
	cartType=quickCart.getProductType();
	if(cartType==null) cartType=""; 
%>
<fd:FDCustomerCreatedList id='lists' action='loadLists'>
<%

String pageNav = "";
String pageNavTitle = "";

// ORDER
if (!QuickCart.PRODUCT_TYPE_CCL.equals(cartType) && !QuickCart.PRODUCT_TYPE_STARTER_LIST.equals(cartType)) { 
%>
<fd:OrderHistoryInfo id='orderHistoryInfo'>
	<% 
	if (quickCart.isEveryItemEverOrdered()) {%>
	<%@ include file="/quickshop/includes/department_nav.jspf" %>
	<%
		pageNav = departmentNav.toString();
		pageNavTitle = "EVERYTHING YOU'VE EVER ORDERED";
	} else {
		boolean showDetails = false; 
	%>
	<%@ include file="/quickshop/includes/order_nav.jspf" %>
	<%	  
		pageNav = orderNav.toString();
		pageNavTitle = "YOUR PREVIOUS ORDERS";
	}
%>
</fd:OrderHistoryInfo> 
<%
}
%>
	
<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
<%  if(QuickCart.PRODUCT_TYPE_CCL.equals(cartType) || QuickCart.PRODUCT_TYPE_STARTER_LIST.equals(cartType)) { %> 
	<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from This Order</tmpl:put>
	<tmpl:put name='side_nav' direct='true'>
	<%--
	<tmpl:put name='banner' direct='true'>
	<a href="/newproducts.jsp"><img src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif" width="140" height="108" border="0"></a>
	<br>
	<img src="/media_stat/images/layout/clear.gif" width="1" height="10">
	<br>
	</tmpl:put>
	--%>
	<font class="space4pix"><br/></font>
	<a href="/quickshop/all_lists.jsp">
	<img src="/media_stat/images/template/quickshop/yourlists_catnav.gif" border="0" width="81" height="53"></a>
	<font class="space4pix"><br/></font>
	<% { String selectedListId = orderId; %>
	<%@ include file="/quickshop/includes/cclist_nav.jspf"%>
	<% } %>
	</tmpl:put>
<%  } else {  %>
	<tmpl:put name='title' direct='true'>FreshDirect - Quick Shop Confirm</tmpl:put>
	<tmpl:put name='side_nav' direct='true'><font class="space4pix"><br></font><font class="text10"><b><%= pageNavTitle %></b></font><br><font class="space4pix"><br></font><%= pageNav %><br><br></tmpl:put>
<%  } %>
    
<tmpl:put name='content' direct='true'>
	<fd:FDShoppingCart id='cart' source='Quickshop' result='result'  successPage='/checkout/view_cart.jsp'>
	<%
	FDCartLineModel orderLine = (FDCartLineModel)cart.getRecentOrderLines().get(0);
	Recipe recipe = null;
	String catIdParam = null;
	if (orderLine.getRecipeSourceId() != null) {
		recipe = (Recipe) ContentFactory.getInstance().getContentNode(orderLine.getRecipeSourceId());
		RecipeSubcategory rcpSubcat = recipe.getPrimarySubcategory();
		RecipeCategory rcpCategory = rcpSubcat!=null ? (RecipeCategory)rcpSubcat.getParentNode() : null;
		catIdParam=rcpCategory.getContentName();
	}

	boolean groupByDepartments = true;
	%>

	<%@ include file="/includes/i_add_to_cart_confirmation.jspf" %>
	<TABLE CELLPADDING="0" CELLSPACING="0" border="0" WIDTH="500">
		<tr><td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
		<TR VALIGN="MIDDLE">
		<% if (!quickCart.isEveryItemEverOrdered()) { %>
			<% if (QuickCart.PRODUCT_TYPE_CCL.equals(cartType)) { %>
				<TD WIDTH="35"><A HREF="/quickshop/shop_from_list.jsp?<%=CclUtils.CC_LIST_ID%>=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/l_arrow_996699.gif" width="29" height="28" border="0" alt="CONTINUE SHOPPING"></A></TD>
				<TD>
					<A HREF="/quickshop/shop_from_list.jsp?<%=CclUtils.CC_LIST_ID%>=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/continue_shopping996699.gif" width="117" height="8" border="0" alt="CONTINUE SHOPPING IN QUICKSHOP" VSPACE="2"></A>
					<BR>from <FONT CLASS="text11bold">
					<A HREF="/quickshop/shop_from_list.jsp?<%=CclUtils.CC_LIST_ID%>=<%=quickCart.getOrderId()%>">
						<b><%= StringUtil.escapeHTML(quickCart.getName())%> (<%=n%> <%=(n == 1 ? " Item" : " Items")%>)</b>							
					</A></FONT>
				</TD>
			<% } else if (QuickCart.PRODUCT_TYPE_STARTER_LIST.equals(cartType)) { %>
				<TD WIDTH="35"><A HREF="/quickshop/starter_list.jsp?<%=CclUtils.STARTER_LIST_ID%>=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/l_arrow_996699.gif" width="29" height="28" border="0" alt="CONTINUE SHOPPING"></A></TD>
				<TD>
					<A HREF="/quickshop/starter_list.jsp?<%=CclUtils.STARTER_LIST_ID%>=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/continue_shopping996699.gif" width="117" height="8" border="0" alt="CONTINUE SHOPPING IN QUICKSHOP" VSPACE="2"></A>
					<BR>from <FONT CLASS="text11bold">
					<A HREF="/quickshop/starter_list.jsp?<%=CclUtils.STARTER_LIST_ID%>=<%=quickCart.getOrderId()%>">
						<b><%= StringUtil.escapeHTML(quickCart.getName())%></b>							
					</A></FONT>
				</TD>

			<% } else {%> 
				<TD WIDTH="35"><A HREF="/quickshop/shop_from_order.jsp?orderId=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/l_arrow_996699.gif" width="29" height="28" border="0" alt="CONTINUE SHOPPING"></A></TD>
				<TD>
					<A HREF="/quickshop/shop_from_order.jsp?orderId=<%=quickCart.getOrderId()%>"><img src="/media_stat/images/template/quickshop/continue_shopping996699.gif" width="117" height="8" border="0" alt="CONTINUE SHOPPING IN QUICKSHOP" VSPACE="2"></A>
					<BR>from your order on <FONT CLASS="text11bold">
					<A HREF="/quickshop/shop_from_order.jsp?orderId=<%=quickCart.getOrderId()%>">
						<%= dateFormatter.format( quickCart.getDeliveryDate() ) %>
					</A></FONT>
				</TD>
			<% } %>
		<% } else {
			String href = "/quickshop/every_item.jsp?action=clearValues" + (hasDeptId ? "&qsDeptId=" + qsDeptId : "");
			ContentNodeModel currDept = null;
			if (!"all".equalsIgnoreCase(qsDeptId)) {
				currDept = ContentFactory.getInstance().getContentNodeByName(qsDeptId);
			}
		%>
		<TD WIDTH="35"><A HREF="<%= href %>"><img src="/media_stat/images/template/quickshop/l_arrow_996699.gif" width="29" height="28" border="0" alt="CONTINUE SHOPPING"></A></TD>
		<TD><A HREF="<%= href %>"><img src="/media_stat/images/template/quickshop/continue_shopping996699.gif" width="117" height="8" border="0" alt="CONTINUE SHOPPING IN QUICKSHOP" VSPACE="2"></A><BR>
			<FONT CLASS="text11bold"><A HREF="<%= href %>">From <%= currDept != null?currDept.getFullName():"Every Item Ordered"%></A></FONT>
		</TD>
		<% } %>

		<TD ALIGN="RIGHT" CLASS="text10bold" VALIGN="MIDDLE">
			<table width="100%" cellpadding="0" cellpadding="2" border=0>
			<tr>
				<td colspan=2 align="right"><A HREF="/checkout/view_cart.jsp?trk=qslist"><img src="/media_stat/images/template/confirmation/checkout_text.gif" width="57" height="9" border="0" alt="CHECKOUT" VSPACE="2"></A></td>
			</tr>
			<%
			{
			int validOrderCount = user.getAdjustedValidOrderCount();
			if (validOrderCount<2) { %>
				<tr>
					<td align=right>Est. Subtotal<% if (validOrderCount>=1) {%> ($40 Min.)<% } %>: </td>
					<td align=right><%= currencyFormatter.format(cart.getSubTotal()) %><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
				</tr>
			<%
				if (cart.getTotalDiscountValue() > 0) {
					List discounts = cart.getDiscounts();
					for (Iterator iter = discounts.iterator(); iter.hasNext();) {
						ErpDiscountLineModel discountLine = (ErpDiscountLineModel) iter.next();
						Discount discount = discountLine.getDiscount();                                
			%>
				<tr>
					<td align=right CLASS="text10bold"><A HREF="<%= servlet_context %>/promotion.jsp?fr=qs">FREE FOOD</a>: </td>
					<td align=right CLASS="text10bold">(-<%= currencyFormatter.format(discount.getAmount()) %>)</td>
				</tr>
			<% 		}
				}
			} // ordercount<2
			} // local block
%>

			<tr>
				<td align=right colspan=2><A HREF="javascript:popup('/help/estimated_price.jsp','small')">Estimated </A>Total:<img src="/media_stat/images/layout/clear.gif" width="4" height="1" alt="" border="0"><%=currencyFormatter.format(cart.getTotal())%><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
			</tr>
			</table>
		</TD>
		<TD WIDTH="35" ALIGN="RIGHT" valign="top"><A HREF="/checkout/view_cart.jsp?trk=qslist"><img src="/media_stat/images/buttons/checkout_arrow.gif" width="29" height="29" border="0" alt="CHECKOUT"></A></TD>
	</TR>
	</TABLE>

	<font class="space4pix"><br><br></font>
<%--
	<table cellpadding="0" cellspacing="0" border="0" width="500">
		<tr>
			<td background="/media_stat/images/template/confirmation/linedot_horz.gif" colspan="5"><img src="<%=spacer%>" width="1" height="2"></td>
		</tr>
		<tr>
			<td background="/media_stat/images/template/confirmation/linedot_vert.gif" rowspan="3"><img src="<%=spacer%>" width="2" height="1"></td>
			<td><img src="<%=spacer%>" width="18" height="1"></td>
			<td><img src="<%=spacer%>" width="400" height="8"></td>
			<td><img src="<%=spacer%>" width="18" height="1"></td>
			<td background="/media_stat/images/template/confirmation/linedot_vert.gif" rowspan="3"><img src="<%=spacer%>" width="2" height="1"></td>
		</tr>
		<tr valign="top">
			<td><img src="<%=spacer%>" width="1" height="1"></td>
			<td align="center" class="text12"><a href="/index.jsp"><img src="/media_stat/images/buttons/home.gif" width="45" height="35" alt="Continue shopping from our home page!" vspace="6" border="0"><br><b>Click here to continue shopping from our home page!</b></a></td>
			<td><img src="<%=spacer%>" width="1" height="1"></td>
		</tr>
		<tr>
			<td colspan="3"><img src="<%=spacer%>" width="1" height="15"></td>
		</tr>
		<tr>
			<td background="/media_stat/images/template/confirmation/linedot_horz.gif" colspan="5"><img src="<%=spacer%>" width="1" height="2"></td>
		</tr>
	</table>
--%>
	<table width="100%" cellpadding="0" border="0">
	<tbody>
	<tr>
		<td align="center">
		<% ProductModel productNode = null; %>
		<fd:ProductGroup id='prodNode' categoryId='<%= orderLine.getCategoryName() %>' productId='<%= orderLine.getProductName() %>'>  
			<%  productNode = prodNode; %>
		</fd:ProductGroup>
		<%@ include file="/includes/i_ymal_lists.jspf"%>
		</td>
	</tr>
	</tbody>
	</table>

<br>
	</fd:FDShoppingCart>
</tmpl:put>

</tmpl:insert>
</fd:FDCustomerCreatedList> 
</fd:QuickShopController>
