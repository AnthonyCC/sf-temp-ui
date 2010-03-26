<%@   page import='com.freshdirect.webapp.util.*'
%><%@ page import="com.freshdirect.fdstore.content.DomainValue"
%><%@ page import='com.freshdirect.framework.webapp.*'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><%@ page import='com.freshdirect.content.attributes.*'
%><%@ page import="com.freshdirect.fdstore.util.URLGenerator"
%><%@ page import="com.freshdirect.fdstore.util.NewProductsNavigator"
%><%@ page import="com.freshdirect.fdstore.*"
%><%@ page import="com.freshdirect.cms.*"
%><%@ page import="com.freshdirect.cms.fdstore.FDContentTypes"
%><%@ page import="com.freshdirect.fdstore.content.*"
%><%@ page import='com.freshdirect.fdstore.attributes.*'
%><%@ page import='com.freshdirect.webapp.util.SearchResultUtil'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'
%><%@ page import="java.util.*"
%><%@ page import="java.net.URLEncoder"
%><%@ page import="java.text.DecimalFormat"
%><%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts.jsp");
	/*request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");*/
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
    
%>
<%
	//set some top-level variables to remove them from includes
	String deptId = NVL.apply(request.getParameter("deptId"), "");
	String catId = NVL.apply(request.getParameter("catId"), "");
	String catRefUrl ="";
    String trk="newp";
        
	//showViewAll is a boolean for showing the view all text/link
	boolean showViewAll = true;

	//the view all URL
	String viewAllURL = "/newproducts.jsp";

	//showFeatNew is a boolean for showing the featured new include
	boolean showFeatNew = false;

	if ("".equals(deptId)) {
		deptId = null; //no deptId, fallback by using null
	}
	if ("".equals(catId)) {
		catId = FDStoreProperties.getNewProductsCatId(); //no catId, fallback
	}
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId);
	
	CategoryModel currentCAT = null;
	boolean isCat;

	isCat = (currentFolder instanceof CategoryModel);
	if (isCat) {
		currentCAT = (CategoryModel)currentFolder;
		
		catRefUrl = response.encodeURL("/category.jsp?catId="+currentCAT.getContentKey().getId()+"&trk="+trk);
	}
	
	if ((FDStoreProperties.getNewProductsCatId()).equals(catId)) {
		//we're on the newproducts.jsp, or no catId was passed
		showViewAll = false;
	}

	Image catLabel = null;
	//check if current cat has a catLabel
	if (isCat && currentCAT.getCategoryLabel() != null) {
		catLabel = currentCAT.getCategoryLabel();
	}else{
		//get a ref to the new products cat and get it's catLabel
		ContentNodeModel currentFolder_FALLBACK = ContentFactory.getInstance().getContentNode(FDStoreProperties.getNewProductsCatId());
		CategoryModel currentCAT_FALLBACK = null;
		if (currentFolder_FALLBACK instanceof CategoryModel) {
			currentCAT_FALLBACK = (CategoryModel)currentFolder_FALLBACK;
			catLabel = currentCAT_FALLBACK.getCategoryLabel();
		}
	}
	/*
	 *	verify we're not null
	 *		if we are, it would mean the fallback also has no catLabel
	 *		or, the fallback property isn't an existing category id
	 */
	if (catLabel==null) {
		catLabel = new Image("/media_stat/images/clear.gif", 1, 1);
	}

	final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #ccc\">&bull;</span>&nbsp;";
	boolean noNewProduct = false;
	boolean noBackStock = false;
	int days = 120;
	NewProductsNavigator nav = new NewProductsNavigator(request);
%>
<fd:GetNewProducts searchResults="results" productList="products" categorySet="categorySet" brandSet="brandSet" categoryTree="categoryTree" filteredCategoryTreeName="filteredCategoryTree">
<tmpl:insert template='/common/template/new_products_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - New Products</tmpl:put>
<tmpl:put name='banner2' direct='true'>
<tr>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
<td colspan="4" align="center"><a href="/newproducts.jsp"><img src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41" border="0"></a></td>
<td bgcolor="#999966" width="1"><IMG src="/media_stat/images/layout/999966.gif" width="1" height="1"></td>
</tr></tmpl:put>
<%
// show category panel if found products 
if (results != null && results.numberOfResults() > 0) {
%>
<%-- CATEGORY TREE NAVIGATOR --%>
<tmpl:put name="categoryPanel" direct="true">
<%
	if ( categoryTree != null ) {
%><%@ include file="/includes/search/generic_treenav.jspf" %><%
	}
%>
</tmpl:put>
<%
} else {
	if (FDStoreProperties.isAdServerEnabled()) { %>
		<tmpl:put name="categoryPanel" direct="true">
		<div style="width:155px; margin-top: 1em">
		<script type="text/javascript">
			OAS_AD('LittleRandy');
		</script>
		</div>
		</tmpl:put>
<%
	}
}
%>
<tmpl:put name='header' direct='true'><%@ include file="/includes/i_header_new.jspf" %></tmpl:put>
<% if (showFeatNew) { %>
	<tmpl:put name='featured' direct='true'><%@ include file="/includes/i_featured_new.jspf" %></tmpl:put>
<% } %>
<tmpl:put name='content' direct='true'>

<table width="550" cellpadding="0" cellspacing="0" border="0">


	<%
		/*
			I don't think we need any of these 
				-Bryan 2010.03.24_05.52.49.PM

			<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="14"></td></tr>

			<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="8"></td></tr>

			<tr><td>
				 <SCRIPT LANGUAGE="JavaScript">
					<!--
					OAS_AD('CategoryNote');
					//-->
				</SCRIPT>
			</td></tr>
		*/
	%>

	<tr><td>
		<table cellpadding="0" cellspacing="0" style="width: 529px; border: 0; background-color: #E0E3D0; padding:2px; margin-left: 15px; margin-top: 15px;">
			<tr>
				<td style="width: 100%">	<%--

				  ************
				  * Sort Bar *
				  ************
				  
				--%><span class="text11bold">Sort:</span>
				<%
					NewProductsNavigator.SortDisplay[] sbar = nav.getSortBar();
					
					for (int i=0; i<sbar.length; i++) {
						%><a href="<%= nav.getChangeSortAction(sbar[i].sortType) %>" class="<%= sbar[i].isSelected ? "text11bold" : "text11" %>"><%= sbar[i].text %></a><%
						if (i < sbar.length-1) {
							%><%= SEPARATOR %><%
						}
					}
				%>
				</td>
				<td>
					<form name="form_brand_filter" id="form_brand_filter" method="GET" style="margin: 0;">
				<% nav.appendToBrandForm(out); %>
						<select name="brandValue" class="text9" style="width: 140px;" onchange="document.getElementById('form_brand_filter').submit()">
							<option value=""><%= nav.getBrand() == null ? "Filter by brand" : "SHOW ALL PRODUCTS"%></option>
				<% 		if (brandSet != null) {
							for (Iterator iter = brandSet.iterator(); iter.hasNext();) {
								BrandModel cm = (BrandModel) iter.next();  
				%>			<option value="<%= cm.getContentKey().getId() %>" <%
								if (cm.getContentKey().getId().equals(nav.getBrand())) {%>
									selected="true"
							<%	}
						%>><%= cm.getFullName() %></option>
				<%			} // for (brandSet...)
						} // if brandSet
				%>
					   </select>
					</form>
				</td>
			</tr>
		</table>
	</td></tr>
<%
System.out.println("Product size $$$$$$$$$$$ "+products.size());
if (products.size()!=0){
%>
	<tr><td>
		<%
			// group by department
			String deptImageSuffix="_np";
		%>

		<%@ include file="/includes/layouts/basic_layout_new.jspf" %>
		
		<%
				// Don't show pager for text view!
				if (!nav.isTextView()) {
		%>
		<%@ include file="/includes/search/generic_pager.jspf" %>
		<%
				} // view != 'text'
		%>
	</td></tr>
<%
} else noNewProduct = true;
%>

</table>
</tmpl:put>

</tmpl:insert>
</fd:GetNewProducts>