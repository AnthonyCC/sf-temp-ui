<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.util.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>
<%@ page import='com.freshdirect.framework.util.StringUtil'%>
<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% request.setAttribute("needsCCL","true"); %>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Shop from Order /</tmpl:put>

<%
   RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
   RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
 
%>

<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>
<%	//
	// Get index of current search term
	//
	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR>
		<TD WIDTH="60%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
				<TR>
					<TD WIDTH="75%">
						Previous Order (<a href="/order/quickshop/index.jsp">All</a>)
						&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="/order/quickshop/every_item.jsp">Shop from Every Item Ordered</a>
						<fd:GetCustomerRecipeList id="recipes">
							&nbsp;&nbsp;|&nbsp;&nbsp;
							<a href="/order/quickshop/recipes.jsp">Recipes</a>
						</fd:GetCustomerRecipeList>
					</TD>
					<TD WIDTH="25%" ALIGN="RIGHT"> [ <A HREF="/order/place_order_batch_search_results.jsp?searchIndex=<%= searchIdx %>">back to list in progress</A> ]</TD>
				</TR>
			</TABLE>
			<hr class="gray1px">


<fd:FDCustomerCreatedList id="lists" action="loadLists">
	<%
	//FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	//--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
	String custFirstName = user.getFirstName();
	int firstNameLength = custFirstName.length();
	int firstNameLastIndex = firstNameLength - 1;
	String ccListIdVal = request.getParameter(CclUtils.CC_LIST_ID);        
	String orderId = ccListIdVal;
        String lineId = request.getParameter("lineId");

	String actionName = request.getParameter("fdAction");
	String listName = null;
    int listSize=0;    
	for(Iterator I = lists.iterator(); I.hasNext(); ) {
	   FDCustomerCreatedList list = (FDCustomerCreatedList)I.next();
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
        String qsPage = "shop_from_list.jsp"; 
	String qsDeptId = null;
	boolean hasDeptId = false;
%>

<div align="left">
<b><%=StringUtil.escapeHTML(listName)%> (<%=quickCart.numberOfProducts()%> items)</b>
</div>
<div align="right">
<a href="/unsupported.jsp"
   onclick="return CCL.rename_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(listName))%>', this);">RENAME LIST</a> 
<% if (lists.size() > 1) { %>
   &nbsp;| 
<a href="/unsupported.jsp" 
   onclick="return CCL.delete_list('<%= StringUtil.escapeHTML(StringUtil.escapeJavaScript(listName)) %>', this);">DELETE LIST </a> 
<% } %>
</div>

<%
				if (quickCart.numberOfProducts() > 0) {
				    request.setAttribute("crm_source","true");
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
</fd:QuickShopController>
</fd:FDCustomerCreatedList>
			
		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">

</tmpl:put>
</tmpl:insert>
