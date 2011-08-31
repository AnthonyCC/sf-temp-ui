<%@   page import='com.freshdirect.webapp.util.*'%><%@ page
	import="com.freshdirect.fdstore.content.DomainValue"%><%@ page
	import='com.freshdirect.framework.webapp.*'%><%@ page
	import='com.freshdirect.webapp.taglib.fdstore.*'%><%@ page
	import='com.freshdirect.content.attributes.*'%><%@ page
	import="com.freshdirect.fdstore.util.URLGenerator"%><%@ page
	import="com.freshdirect.fdstore.util.NewProductsNavigator"%><%@ page
	import="com.freshdirect.fdstore.*"%><%@ page
	import="com.freshdirect.cms.*"%><%@ page
	import="com.freshdirect.cms.fdstore.FDContentTypes"%><%@ page
	import="com.freshdirect.fdstore.content.*"%><%@ page
	import='com.freshdirect.fdstore.attributes.*'%><%@ page
	import='com.freshdirect.webapp.taglib.fdstore.SessionName'%><%@ page
	import="java.util.*"%><%@ page import="java.net.URLEncoder"%><%@ page
	import="java.text.DecimalFormat"%><%@ page
	import='com.freshdirect.framework.util.NVL'%>
<%@page import="com.freshdirect.fdstore.util.NewProductsDFGSNavigator"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<fd:CheckLoginStatus id="user" />
<%
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");
	request.setAttribute("listPos", "SystemMessage,CategoryNote");

	//set some top-level variables to remove them from includes
	String trk = "newp";
	NewProductsDFGSNavigator nav = new NewProductsDFGSNavigator(request);
	final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #ccc\">&bull;</span>&nbsp;";
%>
<fd:GetNewProducts id="newProds" nav="<%= nav %>" simpleView="true">
	<tmpl:insert template='/common/template/new_products_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - New Products</tmpl:put>
		<tmpl:put name='banner2' direct='true'>
			<tr>
				<td bgcolor="#999966" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" /></td>
				<td colspan="4" align="center"><br />
				<br />
				<a href="/newproducts.jsp"><img src="/media_stat/images/template/newproduct/newprod_findhere.gif" width="660" height="41" border="0" /></a></td>
				<td bgcolor="#999966" width="1"><img src="/media_stat/images/layout/999966.gif" width="1" height="1" /></td>
			</tr>
		</tmpl:put>
		<tmpl:put name="colLeftWidth" direct="true">568</tmpl:put>
		<tmpl:put name="colRightWidth" direct="true">165</tmpl:put>
		<tmpl:put name="categoryPanel" direct="true">
		</tmpl:put>
		<%
			//Make sure to leave a space inside empty tmpl:put tags
		%>
		<tmpl:put name='rightNav' direct='true'>
			<td width="155" colspan="2" align="center" class="rnav"><img src="/media_stat/images/layout/clear.gif" height="10" width="1"><br />
			<%@ include file="/common/template/includes/right_side_nav.jspf"%>
			</td>
		</tmpl:put>
		<tmpl:put name='header_1' direct='true'>
			<td width="743" colspan="4"><%@ include file="/common/template/includes/deptnav.jspf"%></td>
		</tmpl:put>
		<tmpl:put name='header_seperator' direct='true'>
			<tr>
				<td width="745" bgcolor="#999966" colspan="6"><img width="1" height="1" src="/media_stat/images/layout/999966.gif"></td>
			</tr>
		</tmpl:put>
		<tmpl:put name='header_2' direct='true'><br><%@ include file="/includes/i_header_new.jspf"%></tmpl:put>
		<tmpl:put name='content' direct='true'>
			<table width="550" cellpadding="0" cellspacing="0" border="0"
				style="margin-left: 15px; margin-top: 10px;">
				<tr>
					<td>
					<table cellpadding="0" cellspacing="0" style="border: 0; background-color: #E0E3D0; padding: 2px;">
						<tr>
							<td style="width: 100%">
								<span class="text11bold">Sort:</span>
								<%
 									NewProductsNavigator.SortDisplay[] sbar = nav.getSortBar();

 									for (int i = 0; i < sbar.length; i++) {
 								%><a href="<%=nav.getChangeSortAction(sbar[i].sortType)%>" class="<%=sbar[i].isSelected ? "text11bold" : "text11"%>"><%=sbar[i].text%></a><%
										if (i < sbar.length - 1) {
								%><%=SEPARATOR%><%
										}
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
										<option value="<%=cm.getContentKey().getId()%>" <%if (cm.getContentKey().getId().equals(nav.getBrand())) {%>
												selected="selected" <%}%>><%=cm.getFullName()%></option>
										<%
											}
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
						<div style="width: 529px; margin-left: 15px; border-top: 4px solid #ff9933"></div>
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