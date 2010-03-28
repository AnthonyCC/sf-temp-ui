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
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
    request.setAttribute("notreenav", "true");
%>
<%
	//set some top-level variables to remove them from includes
	String deptId = NVL.apply(request.getParameter("deptId"), "");
	String catId = NVL.apply(request.getParameter("catId"), "");
	String catRefUrl ="";
	String trk="newp";

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

	//useSmallBurst determines is we should show the large or small(er) new burst in the header
	boolean useSmallBurst = true;

	//showViewAll is a boolean for showing the view all text/link
	boolean showViewAll = true;

	//the view all URL
	String viewAllURL = "/newproducts.jsp";

	
	if ((FDStoreProperties.getNewProductsCatId()).equals(catId)) {
		//we're on the newproducts.jsp, or no catId was passed
		showViewAll = false;
		useSmallBurst = true;
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

	//showFeatNew is a boolean for showing the featured new include
	boolean showFeatNew = true;

	
	//do logic to hide feats
	String brandValue = NVL.apply(request.getParameter("brandValue"), "");
	if (!"".equals(brandValue)) {
		showFeatNew = false;
	}

    final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #CCCCCC\">&bull;</span>&nbsp;";
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
		<td bgcolor="#999966" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" /></td>
		<td colspan="4" align="center"><br /><br /><a href="/newproducts.jsp"><img src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41" border="0" /></a></td>
		<td bgcolor="#999966" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" /></td>
	</tr>
</tmpl:put>
<tmpl:put name="colLeftWidth" direct="true">568</tmpl:put>
<tmpl:put name="colRightWidth" direct="true">165</tmpl:put>
<tmpl:put name="categoryPanel" direct="true"> </tmpl:put><% //Make sure to leave a space inside empty tmpl:put tags %>
<tmpl:put name='rightNav' direct='true'>
	<td width="155" colspan="2" align="center" class="rnav">
		<img src="/media_stat/images/layout/clear.gif" height="10" width="1"><br />
		<%@ include file="/common/template/includes/right_side_nav_new.jspf" %>
	</td>
</tmpl:put>
<tmpl:put name='header_1' direct='true'><td width="743" colspan="4"><%@ include file="/common/template/includes/deptnav.jspf" %></td></tmpl:put>
<tmpl:put name='header_seperator' direct='true'><tr>
	<td width="745" bgcolor="#999966" colspan="6"><img width="1" height="1" src="/media_stat/images/layout/999966.gif"></td>
</tr></tmpl:put>
<tmpl:put name='header_2' direct='true'>
	<%@ include file="/includes/i_header_new.jspf" %></tmpl:put>
<% if (showFeatNew) { %>
	<tmpl:put name='featured' direct='true'><%@ include file="/includes/i_featured_new.jspf" %></tmpl:put>
<% } %>

<tmpl:put name='content' direct='true'>
	<table width="550" cellpadding="0" cellspacing="0" border="0" style="margin-left: 15px;">
		<tr>
			<td>
				<table cellpadding="0" cellspacing="0" style="border: 0; background-color: #E0E3D0; padding:2px;margin-top: 10px;line-height:     ;">
					<tr>
						<td style="width: 100%"><%--
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
			</td>
		</tr>
		<%
		//System.out.println("Product size $$$$$$$$$$$ "+products.size());
		if (products.size()!=0){
		%>
		<tr>
			<td>
				<%
					// group by department
					String deptImageSuffix="_np";
				%>
				<%@ include file="/includes/layouts/basic_layout_new.jspf" %>
				<div style="width: 529px; margin-left: 15px; border-top: 4px solid #ff9933"></div>
				<%
						// Don't show pager for text view!
						if (!nav.isTextView()) {
				%>
				<%@ include file="/includes/search/generic_pager.jspf" %>
				<%
						} // view != 'text'
				%>
			</td>
		</tr>
		<%
		} else noNewProduct = true;
		%>
	</table>
</tmpl:put>
</tmpl:insert>
</fd:GetNewProducts>