<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.framework.webapp.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.content.attributes.*'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.fdstore.util.SearchNavigator" %>
<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<%
	FDUserI user = (FDUserI) session.getAttribute( SessionName.USER );
	//--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/newproducts_dfgs.jsp");
	request.setAttribute("listPos", "SystemMessage,CategoryNote");
    
%>
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
if (true) {
%>
<%-- CATEGORY TREE NAVIGATOR --%>
<tmpl:put name="categoryPanel" direct="true">
<%
	if (true ) {
%><!-- Category Tree to go here --><%
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
<%
	//set some top-level variables to remove them from includes
	String deptId = NVL.apply(request.getParameter("deptId"), "");
	String catId = NVL.apply(request.getParameter("catId"), "");
	String catRefUrl ="";
    String trk="newp";

	//showViewAll is a boolean for showing the view all text/link
	boolean showViewAll = true;

	//the view all URL
	String viewAllURL = "http://www.freshdirect.com/newproducts.jsp";

	//showFeatNew is a boolean for showing the featured new include
	boolean showFeatNew = true;

	if ("".equals(deptId)) {
		deptId = null; //no deptId, fallback by using null
	}
	if ("".equals(catId)) {
		catId = "newproduct_cat"; //no catId, fallback
	}
	ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(catId);
	
	CategoryModel currentCAT = null;
	boolean isCat;

	isCat = (currentFolder instanceof CategoryModel);
	if (isCat) {
		currentCAT = (CategoryModel)currentFolder;
		
		catRefUrl = response.encodeURL("/category.jsp?catId="+currentCAT.getContentKey().getId()+"&trk="+trk);
	}
	
	if ("newproduct_cat".equals(catId)) {
		//we're on the newproducts.jsp, or no catId was passed
		showViewAll = false;
	}

%>

<tmpl:put name='content' direct='true'>
<%
    final String SEPARATOR = "&nbsp;<span class=\"text12\" style=\"color: #CCCCCC\">&bull;</span>&nbsp;";
	boolean noNewProduct = false;
	boolean noBackStock = false;
    int days = 120;
    SearchNavigator nav = new SearchNavigator(request);
    Set brandSet = new HashSet();
%>
<fd:GetNewProducts id="products" days='<%=days%>' department='<%=deptId%>'>

<table width="550" cellpadding="0" cellspacing="0" border="0">


<tmpl:put name='header' direct='true'><%@ include file="/includes/i_header_new.jspf" %></tmpl:put>
<% if (showFeatNew) { %>
	<tmpl:put name='featured' direct='true'><%@ include file="/includes/i_featured_new.jspf" %></tmpl:put>
<% } %>

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
<table cellpadding="0" cellspacing="0" style="width: 529px; border: 0; background-color: #E0E3D0; padding:2px;margin-top: 10px;line-height: 25px;">
<tr>
<td style="width: 100%"><%--

  ************
  * Sort Bar *
  ************
  
--%><span class="text11bold">Sort:</span>
<%
	SearchNavigator.SortDisplay[] sbar = nav.getSortBar();
    
	for (int i=0; i<sbar.length; i++) {
		%><a href="#" class="<%= sbar[i].isSelected ? "text11bold" : "text11" %>"><%= sbar[i].text %></a><%
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
if (products.size()!=0){
%>
<tr><td>
<%
//	if (products.size()>10) { 
		// group by department
		String deptImageSuffix="_np";
	%>
	<%-- <%@ include file="/includes/layouts/newproductlist_layout.jspf" %> --%>
	<%
	//} else { %>
	<%@ include file="/includes/layouts/basic_layout_new.jspf" %>
	<%
	//}
%>
</td></tr>

<%
} else noNewProduct = true;
%>
</fd:GetNewProducts>


</table>
</tmpl:put>

</tmpl:insert>
