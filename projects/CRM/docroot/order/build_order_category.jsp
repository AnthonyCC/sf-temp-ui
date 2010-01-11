<%@ page import='java.text.*' %>

<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!	
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );

    Comparator priorityComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			
			if (o1 instanceof DepartmentModel && o2 instanceof DepartmentModel) {
				DepartmentModel item1 = (DepartmentModel) o1;
				DepartmentModel item2 = (DepartmentModel) o2;
				Integer p1 = new Integer(item1.getPriority());
				Integer p2 = new Integer(item2.getPriority());
				return p1.compareTo(p2);
			} else { // assume they're CategoryModels
				CategoryModel item1 = (CategoryModel) o1;
				CategoryModel item2 = (CategoryModel) o2;
				Integer p1 = new Integer(item1.getPriority());
				Integer p2 = new Integer(item2.getPriority());
				return p1.compareTo(p2);
			}
        }
	};
	
%>

<%
	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
%>

<%
	//
	// Get current catgory Id, defaulting to top level if no category Id is found
	//
	ContentFactory contentFactory = ContentFactory.getInstance();
	ContentNodeModel requestNode = null;
	Collection folderNodes = null;
	Collection productNodes = null;
	
	String catId = request.getParameter("catId");
	if (catId == null) {
		StoreModel storeModel = contentFactory.getStore();
		folderNodes = storeModel.getDepartments();
	} else {
		requestNode = contentFactory.getContentNode(catId);
		if (requestNode instanceof DepartmentModel) {
			DepartmentModel deptModel = (DepartmentModel) requestNode;
			folderNodes = deptModel.getCategories();
		} else if (requestNode instanceof CategoryModel) {
			CategoryModel catModel = (CategoryModel) requestNode;
			boolean containsBeer = catModel.isHavingBeer();
			if(user != null && containsBeer && !user.isHealthWarningAcknowledged()){
				String redirectURL = "/order/health_warning.jsp?successPage=/order/build_order_category.jsp"+URLEncoder.encode("?"+request.getQueryString());
				response.sendRedirect(response.encodeRedirectURL(redirectURL));
			}
			folderNodes = catModel.getSubcategories();
			productNodes = contentFactory.getProducts(catModel);
		}
	}
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > View Items</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page='/includes/order_header.jsp'/>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order">
	<TR VALIGN="TOP">
		<TD WIDTH="60%">
                    <%@ include file="/includes/i_bread_crumbs.jspf"%>
			
<%	if (folderNodes != null && folderNodes.size() > 0) {
		List sortedFolders = new ArrayList(folderNodes);

		// filter hidden nodes
		for (ListIterator li=sortedFolders.listIterator(); li.hasNext(); ) {
			ContentNodeModel cn = (ContentNodeModel)li.next();
			if (cn.isHidden()) {
				li.remove();
			}
		}

		Collections.sort(sortedFolders, priorityComparator);
%>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
			<TR>
				<TD WIDTH="87%"><FONT CLASS="text8blackbold">Department / Category</FONT><BR><br></TD>
				<TD WIDTH="13%" COLSPAN="2">&nbsp;<BR></TD>
			</TR>
			</TABLE>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
			<TR valign="bottom">
<%		int folderIdx = -1; %>
<logic:iterate id="folderNode" collection="<%= sortedFolders %>" type="com.freshdirect.fdstore.content.ContentNodeModel" indexId="folderCount">
				<TD WIDTH="24%" align="center">
<%		 
		String imgSrc = "/media/images/temp/soon_70x70.gif";
		int imgHeight = 70;
		int imgWidth = 70;
		if (folderNode instanceof DepartmentModel) {
			DepartmentModel deptModel = (DepartmentModel) folderNode;
			Image deptImage = deptModel.getPhoto();
			if (deptImage != null) {
				imgSrc = deptImage.getPath();
				imgHeight = deptImage.getHeight();
				imgWidth = deptImage.getWidth();
			}
%>
					<A HREF="/order/build_order_browse.jsp?catId=<%= folderNode %>"><img src="<%= imgSrc %>" width="<%= imgWidth %>" height="<%= imgHeight %>" border="0" valign="middle"></A>
<%		} else if (folderNode instanceof CategoryModel) {
			CategoryModel catModel = (CategoryModel) folderNode;
			Image catImage = catModel.getCategoryPhoto();
			
			if (catImage == null) {
				//
				// No img for Category, so let's try using one from the first product we find
				//
				Collection prods = contentFactory.getProducts(catModel);
				if (prods.size() > 0) {
					Image productImage = null;
					for (Iterator it = prods.iterator(); it.hasNext() && (productImage == null || productImage.getPath().indexOf("soon") != -1); ) {
						ProductModel prod = (ProductModel) it.next();
						productImage = prod.getCategoryImage();
					}
					if (productImage != null) {
						imgSrc = productImage.getPath();
						imgHeight = productImage.getHeight();
						imgWidth = productImage.getWidth();
						System.out.println("found img (" + imgSrc + ") for product!");
					} else {
						System.out.println("no img for product :(");
					}
				} else {
					System.out.println("no products in this cat :(");
				}
			} else {
				imgSrc = catImage.getPath();
				imgHeight = catImage.getHeight();
				imgWidth = catImage.getWidth();
			}
%>
					<A HREF="/order/build_order_category.jsp?catId=<%= folderNode %>"><img src="<%= imgSrc %>" width="<%= imgWidth %>" height="<%= imgHeight %>" border="0" valign="middle"></A>
<%		} else { %>
					<A HREF="/order/build_order_category.jsp?catId=<%= folderNode %>"><img src="/media_stat/images/buttons/folder_icon.gif" width="12" height="9" border="0"></A>
<%		} %>
					<br><a href="/order/build_order_browse.jsp?catId=<%= folderNode %>"><%= folderNode.getFullName() %></a><BR><br>
				</TD>
				<TD WIDTH="1%">&nbsp;</TD>
<%		if (folderIdx++ > 0 && folderIdx % 3 == 0) {
			folderIdx = -1; %>
			</TR>
			<TR>
<%		} %>
		</logic:iterate>
			</TR>
			</TABLE>
<%	} %>
<%	if ( (folderNodes != null && folderNodes.size() > 0) && (productNodes != null && productNodes.size() > 0)) { %>
			<hr class="gray1px">
			
<%	} %>
<%	if (productNodes != null && productNodes.size() > 0) {
		String offSet = "0";
		String searchIndex = "0";
		List recipes = new ArrayList(); //satisfies includes requirement.
		List products = new ArrayList(productNodes);

		// filter hidden nodes
		for (ListIterator li=products.listIterator(); li.hasNext(); ) {
			ContentNodeModel cn = (ContentNodeModel)li.next();
			if (cn.isHidden()) {
				li.remove();
			}
		}
%>
	<table width="100%" class="order">
	<form name="build_list" method="post">
	<tr><td><%@ include file="/includes/i_search_results.jspf"%></td></tr>
	</form>
	</table>

<%	} %>
		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">
</tmpl:put>

</tmpl:insert>

