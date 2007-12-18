<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.util.JspLogger' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='java.text.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false'/>
<% 	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
	request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
	
	String sortBy = request.getParameter("sortBy");
	Comparator comparator = FDCustomerRecipeListLineItem.NAME_COMPARATOR;
	if ("recency".equals(sortBy)) {
		comparator = FDCustomerRecipeListLineItem.LAST_PURCHASE_COMPARATOR;
	} else if ("frequency".equals(sortBy)) {
		comparator = FDCustomerRecipeListLineItem.FREQUENCY_COMPARATOR;
	}

%>
<tmpl:insert template='/common/template/quick_shop_nav.jsp'>

<tmpl:put name='title' direct='true'>FreshDirect - Quickshop - Your Recipes</tmpl:put>

<tmpl:put name='side_nav' direct='true'>
	<font class="space4pix"><br></font>
	<img src="/media_stat/images/template/quickshop/qs_your_recipes_catnav.gif" width="80" height="38" border="0">
	<br>
</tmpl:put>

<tmpl:put name='content' direct='true'>

	<form method="GET">
	<%
	DateFormat dateFormatter = new SimpleDateFormat("EEE. MMM d, yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
	%>		
	<%! java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##"); %>

	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="bodyCopy">
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
	<tr>
		<td class="text12"><b><span class="title18"><%= user.getFirstName() %>'s Recipes</span></b></td>
		<td align="right"><b>Sort by:</b>
			<SELECT NAME="sortBy" onChange="javascript:this.form.submit()" CLASS="text10">
				<OPTION NAME="Ack" <%="name".equals(sortBy)?"selected":""%> value="name">Name</option>
				<OPTION NAME="Ack" <%="recency".equals(sortBy)?"selected":""%> value="recency">Recently Ordered</option>
				<OPTION NAME="Ack" <%="frequency".equals(sortBy)?"selected":""%> value="frequency">Frequently Ordered</option>
			</SELECT>
		</td>
	</tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" alt="" border="0"></td></tr>
	<tr><td bgcolor="#996699" height="1" colspan="2"><img src="/media_stat/images/layout/996699.gif" width="1" height="1" border="0"></td></tr>
	<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0"></td></tr>
	</table>
	<br>
	<fd:GetCustomerRecipeList id="recipes">
		<%
		List items = recipes.getAvailableLineItems();
		Collections.sort(items, comparator);
		for (Iterator i = items.iterator(); i.hasNext(); ) {
			FDCustomerRecipeListLineItem item = (FDCustomerRecipeListLineItem) i.next();
			Recipe recipe = item.getRecipe();
			%>
			<div style="margin-bottom: 1em;">
				<b><a href="/recipe.jsp?recipeId=<%= recipe.getContentName() %>&trk=qsrec"><%= recipe.getName() %></a></b>
				<%
				if (recipe.getSource() != null) {
					%>
					<br><span style="color: #777">from "<%= recipe.getSource().getName() %>"</span>
					<%
				}
				%>
			</div>
			<%
		}
		%>
	</fd:GetCustomerRecipeList>

	</form>
	
</tmpl:put>

</tmpl:insert>

