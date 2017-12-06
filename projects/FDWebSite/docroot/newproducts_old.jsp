<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@page import="com.freshdirect.fdstore.util.NewProductsNavigator"%>
<%@page import="com.freshdirect.storeapi.content.BrandModel"%>

<% //expanded page dimensions
final int W_NEWPRODUCTS_TOTAL = 970;
final int W_NEWPRODUCTS_LEFT = 150;
final int W_NEWPRODUCTS_CENTER_PADDING = 14;
final int W_NEWPRODUCTS_RIGHT = 806;
%>

<fd:CheckLoginStatus id="user" />
<%
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts.jsp");
	request.setAttribute("listPos", "SystemMessage,LittleRandy,CategoryNote");

	String trk = "newp";
	NewProductsNavigator nav = new NewProductsNavigator(request);
	final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #ccc\">&bull;</span>&nbsp;";
%>
<fd:GetNewProducts id="newProds" nav="<%= nav %>">
	<tmpl:insert template='/common/template/new_products_nav.jsp'>
	<tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - New Products"/>
    </tmpl:put>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - New Products</tmpl:put> --%>
		<tmpl:put name='banner2' direct='true'>
			<tr>
				<td colspan="2" align="right"><br />
				<a href="/newproducts.jsp"><img style="margin-right: 58px" src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41" border="0" /></a></td>
			</tr>
		</tmpl:put>
		<tmpl:put name="colLeftWidth" direct="true"><%=W_NEWPRODUCTS_LEFT+W_NEWPRODUCTS_CENTER_PADDING%></tmpl:put>
		<tmpl:put name="colRightWidth" direct="true"><%=W_NEWPRODUCTS_RIGHT%></tmpl:put>
		<tmpl:put name="categoryPanel" direct="true">
			<td width="<%=W_NEWPRODUCTS_LEFT%>"><%@ include
				file="/includes/search/generic_treenav.jspf"%>
			<br />
			<img src="/media_stat/images/layout/clear.gif" height="1" width="<%=W_NEWPRODUCTS_LEFT%>" alt=""></td>
		</tmpl:put>

		<tmpl:put name='rightNav' direct='true'>
		</tmpl:put>
		<%
			//Make sure to leave a space inside empty tmpl:put tags
		%>
		<tmpl:put name='header_1' direct='true'>
			<td colspan="2"><%@ include file="/includes/i_header_new.jspf"%></td>
		</tmpl:put>
		<tmpl:put name='header_seperator' direct='true'>
		</tmpl:put>
		<%
			//Make sure to leave a space inside empty tmpl:put tags
		%>
		<tmpl:put name='header_2' direct='true'>
		</tmpl:put>
		<%
			//Make sure to leave a space inside empty tmpl:put tags
		%>

		<% if (newProds.isShowFeatured()) { %>
		<tmpl:put name='featured' direct='true'><%@ include file="/includes/i_featured_new.jspf"%></tmpl:put>
		<% } %>
		<tmpl:put name='content' direct='true'>
			<table width="<%=W_NEWPRODUCTS_RIGHT%>" cellpadding="0" cellspacing="0" border="0" style="margin-top: 10px;">
				<%
					if (nav.getBrand() == null && (nav.getCategory() != null || nav.getDepartment() != null)) {
						String contentId = nav.getCategory() != null ? nav.getCategory() : nav.getDepartment();
						ContentNodeModel node = ContentFactory.getInstance().getContentNode(nav.getCategory() != null ? "Category" : "Department", contentId);
						if (node != null) {
							String link = nav.getCategory() != null ? "/category.jsp?" : "/department.jsp?";
							link += nav.getCategory() != null ? "catId=" : "deptId=";
							link += contentId;
				%>
				<tr>
					<td style="font-size: 18px; font-weight: normal; padding-bottom: 10px;">
						<strong><%= newProds.getNoOfFilteredProducts() %> New Product<%= newProds.getNoOfFilteredProducts() > 1 ? "s" : "" %></strong> in <a href="<%= link %>&amp;trk=newp"><%= node.getFullName() %></a>
					</td>
				</tr>
				<%
						}
					}
				%>
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0"
						style="border: 0; background-color: #E0E3D0; padding: 7px;">
						<tr>
							<td style="width: 100%">
								<span class="text11bold">Sort:</span>
								<%
								 	NewProductsNavigator.SortDisplay[] sbar = nav.getSortBar();

					 				for (int i = 0; i < sbar.length; i++) {
 								%><a href="<%=nav.getChangeSortAction(sbar[i].sortType)%>" class="<%=sbar[i].isSelected ? "text11bold" : "text11"%>"><%=sbar[i].text%></a><% 
									if (i < sbar.length - 1) { %><%=SEPARATOR%><% }
									}
								%>
							</td>
							<td>
							<form name="form_brand_filter" id="form_brand_filter" method="GET" style="margin: 0;">
								<%
									nav.appendToBrandForm(out);
								%> 
								<select name="brandValue" class="text9" style="width: 140px;" onchange="document.getElementById('form_brand_filter').submit()">
									<option value=""><%=nav.getBrand() == null ? "Filter by brand" : "SHOW ALL PRODUCTS"%></option>
									<%
										for (BrandModel cm : newProds.getBrands()) {
									%>
									<option value="<%=cm.getContentKey().getId()%>"<%if (cm.getContentKey().getId().equals(nav.getBrand())) {%> selected="selected" <%}%>>
										<%=cm.getFullName()%>
									</option>
									<%
										} // for (brandSet...)
									%>
								</select>
							</form>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<%
					if (nav.getBrand() != null) {
						BrandModel brand = (BrandModel) ContentFactory.getInstance().getContentNode("Brand", nav.getBrand());
						if (brand != null) {
				%>
				<tr>
					<td style="font-size: 18px; font-weight: normal; padding: 10px 0;" valign="middle">
						<% if (brand.getLogoSmall() != null) { %>
						<fd:IncludeImage image="<%= brand.getLogoSmall() %>" alt="<%= brand.getFullName() %>" style="border: 0px none;"/>
						<% } %>
						<strong><%= newProds.getNoOfBrandFilteredProducts() %> New Product<%= newProds.getNoOfBrandFilteredProducts() > 1 ? "s" : "" %></strong> from <strong><%= brand.getFullName() %></strong>
						<hr size="1" style="color: #ccc;">
					</td>
				</tr>
				<%
						}
					}

					if (newProds.hasProducts()) {
				%>
				<tr>
					<td>
						<div style="padding: 8px 0px;">
						<% { %><%@ include file="/includes/layouts/basic_layout_new.jspf"%><% } %>
						</div>
						<div style="width: <%=W_NEWPRODUCTS_RIGHT%>px; border-top: 4px solid #ff9933"></div>
						<% { %><%@ include file="/includes/search/generic_pager.jspf"%><% } %>
					</td>
				</tr>
				<%
					}
				%>
			</table>
		</tmpl:put>
	</tmpl:insert>
</fd:GetNewProducts>
