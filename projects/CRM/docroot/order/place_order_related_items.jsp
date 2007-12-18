<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
    String searchIndex = request.getParameter("searchIndex");
	if (searchIndex == null) {
		searchIndex="0";
	}
	//String index = String.valueOf(idx);
	String rawList = request.getParameter("search_pad");
%>

<fd:ParseSearchTerms searchParams="<%= rawList %>" isBulkSearch="true" searchFor="criteria" searchList="searchTerms">
<fd:Search searchFor="<%= criteria %>" searchResults="searchResults" errorPage="/order/place_order_build.jsp">

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Order > Related Items</tmpl:put>

<%!	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>

<tmpl:put name='content' direct='true'>
<%	String productId = request.getParameter("productId");
	String categoryId = request.getParameter("catId");
	
	ContentFactory contentFactory = ContentFactory.getInstance();
	CategoryModel origCat = (CategoryModel) contentFactory.getContentNode(categoryId);
	ProductModel origProduct = (new ProductRef(categoryId, productId)).lookupProduct();
%>

<jsp:include page='/includes/order_header.jsp'/>

<div class="content_scroll" style="float: left; width: 60%; height: 72%;">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<form name="build_list" method="post">
	<TR VALIGN="TOP">
		<TD WIDTH="1%">&nbsp;</TD>
		<TD WIDTH="59%" CLASS="text7gr">
			<%-- =============== BEGIN BATCH SEARCH ITEMS SECTION ============ --%>
			<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0">
				<TR VALIGN="TOP">
					<TD WIDTH="100%" CLASS="text7gr">
<%	int termCounter = 0; %>
						<logic:iterate id="term" collection="<%= searchTerms %>" type="java.lang.String">
<%	if ( term.equalsIgnoreCase(criteria) ) { %>
						&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>" class="text8"><b><%= term %></b></a>&nbsp;
<%	} else {  %>
						&nbsp;<a href="<%= response.encodeURL("/order/place_order_batch_search_results.jsp?searchIndex=" +  termCounter ) %>"><%= term %></a>&nbsp;
<% 	} 
	termCounter++; %>
						</logic:iterate><br><FONT CLASS="space8pix"><BR></FONT>
					</TD>
				</TR>
			</TABLE>
			<%-- =============== END BATCH SEARCH ITEMS SECTION ============ --%>
			
			<%-- ~~~~~~~~~~~~~ BEGIN RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"></TD>
				</TR>
			</TABLE>
			<%-- ~~~~~~~~~~~~~ END RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>

			<%-- ~~~~~~~~~~~~~ BEGIN PRODUCT DISPLAY SECTION ~~~~~~~~~~~~~ --%>
<%
	if ( origProduct != null && origProduct.hasAttribute("RELATED_PRODUCTS") ) {
		//int productCounter = 0;
		//int minimumQuantity = 1;
		//int itemsShown = 0;
		//String lastCatName = null;
		
		Attribute relatedItemsAtt = origProduct.getAttribute("RELATED_PRODUCTS");
		Collection relatedItems = (Collection) relatedItemsAtt.getValue();
		List recipes = searchResults.getRecipes();
		List products = new ArrayList();
		for (Iterator it = relatedItems.iterator(); it.hasNext(); ) {
                        ContentRef cf = (ContentRef)it.next();
                        if (cf instanceof ProductRef) {
                            ProductModel product = contentFactory.getProduct( (ProductRef)cf );
                            if ( product != null && !product.isDiscontinued() ) {
                                    products.add(product);
                            }
                        } else if (cf instanceof CategoryRef) {
                           // products.add( ((CategoryRef)cf).getCategory());
                        }
		}
		String offSet = "0";
 %>
		<%@ include file="/includes/i_search_results.jspf"%>
<% 	} else { %>
		<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0">
			<tr>
				<td width="30%"></td>
				<td width="70%">Your search for <span class="text7grbold"><%= criteria %></span> produced no results.</td>
			</tr>
		</TABLE>
<% 	} %>
			<%-- ~~~~~~~~~~~~~ END PRODUCT DISPLAY SECTION ~~~~~~~~~~~~~ --%>
			
			<%-- ~~~~~~~~~~~~~ BEGIN RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
			<FONT CLASS="space4pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT>
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"></TD>
				</TR>
			</TABLE>
			<FONT CLASS="space4pix"><BR></FONT>
            <FONT CLASS="space4pix"><BR></FONT>
			<%-- ~~~~~~~~~~~~~ END RESULTS NAV SECTION ~~~~~~~~~~~~~ --%>
			</form>
			
			<BR>
			<BR>
		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

</tmpl:put>
</tmpl:insert>
</fd:Search>
</fd:ParseSearchTerms>