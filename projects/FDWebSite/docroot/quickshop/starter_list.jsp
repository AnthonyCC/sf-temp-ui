<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.util.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspLogger'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='com.freshdirect.fdstore.lists.CclUtils'%>
<%@ page import='java.text.*'%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' />

<%
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    //--------OAS Page Variables-----------------------
    request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
    request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");

%>

<fd:FDCustomerCreatedList id='lists' action='loadLists'>
<%
    String actionName = (String)request.getParameter("fdAction");
    if (actionName == null) actionName = "";

%>
	<fd:QuickShopController id="quickCart" starterListId="<%= request.getParameter(CclUtils.STARTER_LIST_ID)%>" action="<%= actionName %>">
		<%
		boolean showDetails = false;
		String listName = quickCart.getName();
		String orderId = "_NO_ID_";
		%>

		<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
			<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from This Order</tmpl:put>
			<tmpl:put name='side_nav' direct='true'>
				<tmpl:put name='extrahead' direct='true'>
					<script language="javascript"
						src="/assets/javascript/common_javascript.js"></script>
				</tmpl:put>

				<%--
				<tmpl:put name='banner' direct='true'>
					<a href="/newproducts.jsp"><img
						src="/media_stat/images/template/quickshop/qs_banner_newproduct.gif"
						width="140" height="108" border="0"></a>
					<br>
					<img src="/media_stat/images/layout/clear.gif" width="1"
						height="10">
					<br>
				</tmpl:put>
				--%>

				<font class="space4pix"><br />
				</font>
				<a href="/quickshop/all_lists.jsp"> <img
					src="/media_stat/images/template/quickshop/yourlists_catnav.gif"
					border="0" width="81" height="53"></a>
				<font class="space4pix"><br />
				</font>

				<%
						{
						String style = "text11";
						String selectedListId = "";
				%>
				<%@ include file="/quickshop/includes/cclist_nav.jspf"%>
				<%
				}
				%>

			</tmpl:put>
			<tmpl:put name='content' direct='true'>

				<%
					DateFormat dateFormatter = new SimpleDateFormat("EEE MM/dd/yy");
					NumberFormat currencyFormatter = java.text.NumberFormat
							.getCurrencyInstance(Locale.US);
					java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat(
							"0.##");
				%>


				<%@ include file="/quickshop/includes/i_confirm_single.jspf"%>
				<table width="100%" cellpadding="0" cellspacing="0" border="0"
					class="bodyCopy">
					<tr>
						<td colspan="2"><img
							src="/media_stat/images/layout/clear.gif" width="1" height="10"
							alt="" border="0"></td>
					</tr>
					<tr>
						<td colspan="2" class="title18" align="left"><b>Our Favorites: <%=StringUtil.escapeHTML(listName.toUpperCase())%></b>
						</nobr></td>
					</tr>
					<tr>
						<td colspan="2"><img
							src="/media_stat/images/layout/clear.gif" width="1" height="5"
							alt="" border="0"></td>
					</tr>
					<tr>
						<td colspan="9" bgcolor="#996699"><img
							src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
					</tr>
					<tr>
						<td colspan="9"><img
							src="/media_stat/images/layout/clear.gif" width="1" height="5"
							border="0"></td>
					</tr>
					<tr>
						<td colspan="9" class="text11" valign="top">
						Need a jump start to your lists? Use the "Save selected to shopping list" link to start your list
						with a few of these items. <b>Want the entire list?</b> Adjust quantities to your liking and click
						<b>"ADOPT THIS LIST"</b> to make it your own.
						</td>
					</tr>
				</table>
				<%
					String qsPage = "shop_from_list.jsp";
					String qsDeptId = null;
					boolean hasDeptId = false;
				%>


				<%
				if (quickCart.numberOfProducts() > 0) {
				%>
				<%@ include file="/shared/quickshop/includes/i_vieworder.jspf"%>
				<br>
				<%
				} else {
				%>
				<br />
				<table width="100%" cellpadding="0" cellspacing="0" border="0">
					<tbody>
						<tr>
							<td colspan="1" bgcolor="#996699"><img width="1" height="1"
								border="0" src="/media_stat/images/layout/clear.gif" /></td>
						</tr>
						<tr>
							<td valign="center" align="center" height="120"><font
								class="text13rbold">There are no available items on this
							list.</font></td>
						</tr>
						<tr>
							<td colspan="1" bgcolor="#996699"><img width="1" height="1"
								border="0" src="/media_stat/images/layout/clear.gif" /></td>
						</tr>
						<tr>
							<td height="60" />
						</tr>
					</tbody>
				</table>
				<%
				}
				%>

			</tmpl:put>
		</tmpl:insert>
	</fd:QuickShopController>
</fd:FDCustomerCreatedList>

