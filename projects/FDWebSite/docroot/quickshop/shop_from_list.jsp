<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.util.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspLogger'%>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='java.text.*'%>

<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'  />

<fd:FDCustomerCreatedList id='lists' action='loadLists'>
<%
	RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
    RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");

    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			//--------OAS Page Variables-----------------------
		        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
		        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
			String custFirstName = user.getFirstName();
			int firstNameLength = custFirstName.length();
			int firstNameLastIndex = firstNameLength - 1;

		        String orderId = null;
		    	String lineId = request.getParameter("lineId");
		   	String ccListIdVal = request.getParameter(CclUtils.CC_LIST_ID);        

			String actionName = request.getParameter("fdAction");
			String listName = null;
		    	int listSize=0;    
			for(Iterator<FDCustomerListInfo> I = lists.iterator(); I.hasNext(); ) {
				FDCustomerListInfo list = (FDCustomerListInfo)I.next();
			   if (list.getId().equals(ccListIdVal)) {
			      listName = list.getName();          
		          listSize = list.getCount();
			      break;
			   }
			}
		    
		    	request.setAttribute("listsSize",""+lists.size());
			if (actionName == null) {
				actionName = "";
			}
		%>
	<fd:QuickShopController id="quickCart" ccListId="<%= ccListIdVal %>" action="<%= actionName %>">
		<%
			boolean showDetails = false;
		%>

		<tmpl:insert template='/common/template/quick_shop_nav.jsp'>
			<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Shop from This Order</tmpl:put>
			<tmpl:put name='side_nav' direct='true'>
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
								String selectedListId = ccListIdVal;
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
						<td colspan="1" class="title18" align="left"><b><%=StringUtil.escapeHTML(listName)%> (<%{ int k = quickCart.numberOfProducts(); if (k != 1) {%><%=k%> items<% } else {%>1 item<%}}%>)</b></td>
						<td colspan="3" class="title8" align="right"><nobr><a
							href="/unsupported.jsp"
							onclick="FormChangeUtil.checkSignature('qs_cart',false); return CCL.rename_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(listName))%>', this);">RENAME LIST</a> <%
 if (lists.size() > 1) {
 %> &nbsp;| <a href="/unsupported.jsp" onclick="FormChangeUtil.checkSignature('qs_cart',false);return CCL.delete_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(listName)) %>', this);">DELETE LIST </a> <%
 }
 %>
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
						<!-- 					<td bgcolor="#996699" height="1" colspan="2"><img
						src="/media_stat/images/layout/996699.gif" width="1" height="1"
						border="0"></td>  -->
					</tr>
					<tr>
						<td colspan="9"><img
							src="/media_stat/images/layout/clear.gif" width="1" height="5"
							border="0"></td>
					</tr>
					<tr>
						<td colspan="9" class="text11" valign="top">
						<ul
							style="margin: 0px; padding-left: 2em; list-style-type:disc; list-style-position:outside;">
							<li><b>Enter quantities in boxes on the left</b> (if you
							don't want an item, enter "0" quantity, or click the green button
							to zero all quantities).</li>
							<li><b>Add the selected items to your cart</b> by clicking
							the orange button.</li>
							<li><b>To make permanent changes to an item</b>, click the product name.</li>
						</ul>
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

