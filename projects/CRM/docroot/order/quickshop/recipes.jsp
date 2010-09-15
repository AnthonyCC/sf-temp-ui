<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% request.setAttribute("needsCCL","true"); %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Recipes /</tmpl:put>

<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
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


	String sortBy = request.getParameter("sortBy");
	Comparator comparator = FDCustomerRecipeListLineItem.NAME_COMPARATOR;
	if ("recency".equals(sortBy)) {
		comparator = FDCustomerRecipeListLineItem.LAST_PURCHASE_COMPARATOR;
	} else if ("frequency".equals(sortBy)) {
		comparator = FDCustomerRecipeListLineItem.FREQUENCY_COMPARATOR;
	}

%>

<div class="content_scroll" style="float: left; width: 60%; height: 72%;">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order">
	<TR VALIGN="TOP">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="58%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
				<TR>
				<TD WIDTH="70%">
					<a href="/order/quickshop/index.jsp">Shop From Previous Orders</a>
					&nbsp;&nbsp;|&nbsp;&nbsp;
					<a href="/order/quickshop/every_item.jsp">Shop from Every Item Ordered</a>
					&nbsp;&nbsp;|&nbsp;&nbsp;
					Recipes
				</TD>
				<TD WIDTH="30%" ALIGN="RIGHT"> [ <A HREF="/order/place_order_batch_search_results.jsp?searchIndex=<%= searchIdx %>">back to list in progress</A> ]</TD>
				</TR>
			</TABLE>
			
			<form method="GET">
			<hr class="gray1px">
			<b>Sort by:</b>
			<SELECT NAME="sortBy" onChange="javascript:this.form.submit()" CLASS="text10">
				<OPTION NAME="Ack" <%="name".equals(sortBy)?"selected":""%> value="name">Name</option>
				<OPTION NAME="Ack" <%="recency".equals(sortBy)?"selected":""%> value="recency">Recently Ordered</option>
				<OPTION NAME="Ack" <%="frequency".equals(sortBy)?"selected":""%> value="frequency">Frequently Ordered</option>
			</SELECT>
			<hr class="gray1px">

			<fd:GetCustomerRecipeList id="recipes">
				<%
				List items = recipes.getAvailableLineItems();
				Collections.sort(items, comparator);
				for (Iterator i = items.iterator(); i.hasNext(); ) {
					FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem) i.next();
					Recipe recipe = item.getRecipe();
					%>
					<div>
						<a href="/order/recipe.jsp?recipeId=<%= recipe.getContentName() %>"><%= recipe.getName() %></a>
						<%
						if (recipe.getSource() != null) {
							%>
							<br><span style="color: #777">from "<%= recipe.getSource().getName() %>"</span>
							<%
						}
						%>
					</div
					<%
				}
				%>
			</fd:GetCustomerRecipeList>
			
			</form>

		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">

</tmpl:put>

<tmpl:put name='slot4' direct='true'>
</tmpl:put>
</tmpl:insert>
