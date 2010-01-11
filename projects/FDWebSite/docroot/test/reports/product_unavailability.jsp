<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.attributes.*" %>
<%@ page import='java.text.*, java.util.*' %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%!
class DepartmentInfo {

	/** List of SkuModel */
	private final List matchingSkus = new ArrayList();
	private final String contentName;
	private int skuCount = 0;
	
	public DepartmentInfo(String contentName) {
		this.contentName = contentName;
	}
	
	public String getContentName() {
		return this.contentName;
	}
	
	public int getSkuCount() {
		return this.skuCount;
	}
	
	public void incrementSkus() {
		this.skuCount++;
	}
	
	public List getMatchingSkus() {
		return this.matchingSkus;
	}

	public int getMatchingCount() {
		return this.matchingSkus.size();
	}

	public void addMatchingSku(SkuModel sku) {
		this.matchingSkus.add(sku);
	}

	public double getPercentMatching() {
		return skuCount==0 ? 0 : matchingSkus.size() / (double)skuCount;
	}
}

class DepartmentInfoVisitor implements ContentVisitorI {

	private final Set searchSkuCodes = new HashSet();
	private final List deptInfos = new ArrayList();
	private DepartmentInfo currDept = null;
	
	public DepartmentInfoVisitor(Collection skuCodes) {
		System.out.println(skuCodes);
		searchSkuCodes.addAll(skuCodes);
	}
	
	public void visit(StoreModel store) {
	}
	
	public void visit(DepartmentModel dept) {
		this.currDept = new DepartmentInfo(dept.getContentName());
		this.deptInfos.add(currDept);
	}
	
	public void visit(CategoryModel cat) {
	}
	
	public void visit(ProductModel prod) {
	}
	
	public void visit(SkuModel sku) {
		ProductModel prod = (ProductModel)sku.getParentNode();
		CategoryModel ph = prod.getPrimaryHome();
		if (ph != null) {
			if (!ph.getContentKey().getId().equals(prod.getParentNode().getContentName())) {
				// skip SKUs not in their primary homes
				return;
			}
		}
		
		this.currDept.incrementSkus();
		if (searchSkuCodes.contains(sku.getSkuCode())) {
			this.currDept.addMatchingSku(sku);
		}
	}
	
	public List getDepartmentInfos() {
		return this.deptInfos;
	}
	
}
%>
<%!	
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
%>
<%

Map deptInfos = (Map)session.getAttribute("departmentInfos");
if (deptInfos==null) {
	
	DepartmentInfoVisitor visitor = new DepartmentInfoVisitor(FDCachedFactory.getOutOfStockSkuCodes());
	ContentWalker w = new ContentWalker(visitor);
	w.walk(ContentFactory.getInstance().getStore());
	
	deptInfos = new HashMap();
	for (Iterator i=visitor.getDepartmentInfos().iterator(); i.hasNext(); ) {
		DepartmentInfo di = (DepartmentInfo)i.next();
		deptInfos.put(di.getContentName(), di);
	}
	session.setAttribute("departmentInfos", deptInfos);
}
String selectedDept = request.getParameter("dept");
DepartmentInfo selectedDeptInfo = null;
if ((selectedDept != null || !"".equals(selectedDept )) && !"null".equals(selectedDept)) {
	selectedDeptInfo = (DepartmentInfo)deptInfos.get( selectedDept );
}
%>
<html>
<head>
<title>/ FD Product Unavailability Report /</title>
<style type="text/css">
	body {
	margin: 6px;
	height: 100%;
	
	background-color: #E7E7D6;
	background-attachment: fixed;
	background-repeat: no-repeat;
	background-position: bottom right;
	
	font-family:  Trebuchet MS, Arial, Verdana, sans-serif;
	color: #000000;
	font-size: 10pt;
	scrollbar-base-color: #FFFFFF; 
	scrollbar-face-color: #FFFFFF; 
	scrollbar-track-color: #FFFFFF; 
	scrollbar-arrow-color: #666666;
	scrollbar-highlight-color: #CCCCCC; 
	scrollbar-3dlight-color: #999999; 
	scrollbar-shadow-color: #666666;
	scrollbar-darkshadow-color: #666666;
	}
	
	a:link		{ color: #336600; text-decoration: none; }
	a:visited	{ color: #336600; text-decoration: none; }
	a:active 	{ color: #FF9933; text-decoration: none; }
	a:hover 	{ color: #336600; text-decoration: underline; }
	
	/* SECTIONS */
	.overview {
	height: 90%; width: 39%; float: left;
	}
	
	.overview_print {
	height: auto; width: 100%; background-color: #FFFFFF; padding-top: 4px; padding-bottom: 4px; 
	}
	
	.details {
	height: 90%; width: 60%; float: right; padding-left: 4px; padding-right: 4px; background-color: #FFFFFF; border-left: 1px solid #666666;
	}
	
	.details_print {
	height: auto; width: 100%; padding-left: 4px; padding-right: 4px; background-color: #FFFFFF; 
	}
	
	/* TEXT */
	table			{ font-size: 10pt;  font-family: Trebuchet MS, Arial, Verdana, sans-serif; }
	
	.report_header {
	font-size: 16pt;
	}
	
	.detail_header {
	font-size: 14pt;
	}
	
	.detail {
	font-size: 9pt;
	padding-top: 4px;
	padding-bottom: 3px;
	background-color: white;
	}
	
	.timestamp {
	font-size: 8pt;
	}
	
	/* LINES */
	.black1px {
	height: 1px;
	color: #000000;
	}
	
	/* LIST */
	.list_header {
	background-color: #CCCC99;
	color: #333300;
	font-size: 10pt;
	font-weight: bold;
	}
	
	.separator {
	padding: 0px;
	background-color: #FFFFFF;
	border-bottom: 1px solid #999999;
	}
	
	.selected {
	background-color: #FFFFFF;
	font-weight: bold;
	}
	
	
	/* FORM */
	.submit {
	background-color: #000000;
	font-size: 8pt;
	color: #FFFFFF;
	border: 1px #999999 solid;
	padding: 2px;
	padding-left: 0px;
	padding-right: 0px;
	margin-left: 5px;
	margin-right: 5px;
	}
</style>
</head>
<body>
<% 	boolean overview = "overview".equalsIgnoreCase(request.getParameter("show"));
	boolean printview = "printview".equalsIgnoreCase(request.getParameter("show"));
	
	String current_dept = (selectedDept != null || !"".equals(selectedDept )) && !"null".equals(selectedDept) ? "dept=" + request.getParameter("dept") : "";
%>
<div style="width: 100%; height: auto; padding-bottom: 4px; border-bottom: solid 1px #000000;">
<table width="100%">
	<tr valign="bottom">
		<td width="20%"><% if (overview) { %><a href="?">Overview & Details</a><% } else { %><a href="?show=overview">Overview Only</a><% } %></td>
		<td width="60%" class="report_header" align="center">FreshDirect Product Unavailability Report</td>
		<td width="20%" align="right"><% if (printview) { %><a href="?<%=current_dept%>">Overview & Details</a><% } else if (!overview) { %><a href="?show=printview&<%=current_dept%>">Details Print Format</a><% } %></td>
	</tr>
</table>
</div>
<% if (!printview) { %>
<div id="overview" class="overview<%= overview ? "_print" : "" %>">
	<table width="95%" cellpadding="4" cellspacing="0" border="0" align="center">
		<tr align="right">
			<td width="32%">&nbsp;</td>
			<td width="2%">&nbsp;</td>
			<td width="22%">Unavail.</td>
			<td width="22%">Total</td>
			<td width="22%">Percent</td>
		</tr>
		<% 
		int total_unavailable = 0; 
		int total_in_department = 0;
		%>
		<logic:iterate id="dept" indexId="i" collection="<%= ContentFactory.getInstance().getStore().getDepartments() %>" type="com.freshdirect.fdstore.content.DepartmentModel">
			<% DepartmentInfo di = (DepartmentInfo)deptInfos.get(dept.getContentName()); %>
			<tr style="cursor: hand;" <%= di==selectedDeptInfo ? "class=\"selected\"" : "" %>
			onClick="document.location='?dept=<%= dept.getContentName() %>'" align="right">
				<td colspan="2" align="left"><a href="?dept=<%= dept.getContentName() %>"><%= dept.getFullName() %></a></td>
				<td><%= di.getMatchingCount() %></td>
				<% total_unavailable += di.getMatchingCount(); %>
				<td><%= di.getSkuCount() %></td>
				<% total_in_department += di.getSkuCount(); %>
				<td><%= NumberFormat.getPercentInstance().format(di.getPercentMatching()) %></td>
			</tr>
			<% if (overview) { %><tr><td colspan="5" class="separator"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr><%}%>
		</logic:iterate>
		<%-- if (!overview) { %><tr><td></td><td colspan="4"><hr class="black1px" noshade></td></tr><%}--%>
		<%--tr style="font-weight: bold;" align="right">
			<td colspan="2" align="left">Total</td>
			<td><%=total_unavailable%></td>
			<td><%=total_in_department%></td>
			<td><%= NumberFormat.getPercentInstance().format(total_unavailable/total_in_department) %></td>
		</tr--%>
		<tr>
			<td colspan="5"></td>
		</tr>
	</table>
</div>
<% } %>
<% if (!overview) { %>
<div id="detail" class="details<%= printview ? "_print" : "" %>">
	<% if (selectedDeptInfo==null) { %>	
			<b>&laquo; Select a department to view it's details</b>
	<% } else { 
			DepartmentModel dept = (DepartmentModel)ContentFactory.getInstance().getContentNode(selectedDeptInfo.getContentName());
	%>
		<table width="100%" cellpadding="0" cellspacing="4" border="0" align="center">
			<tr>
				<td valign="bottom"><span class="detail_header"><%= dept.getFullName() %></span> &nbsp;&nbsp; unavailable: <b><%= selectedDeptInfo.getMatchingCount() %></b></td>
				<% Date currentDateTime = new Date(); %>
				<td align="right" class="timestamp">Date: <%= dateFormat.format(currentDateTime)%> &nbsp;&nbsp; Time: <%= timeFormat.format(currentDateTime)%></td>
			</tr>
		</table>
		<table width="100%" cellpadding="0" cellspacing="2" class="list_header">
			<tr>
				<td width="50%">Product Name</td>
				<td width="22%">SAP #</td>
				<td width="28%">Message</td>
				<%-- 28 16 28 td width="28%" align="right">Last Time Available</td --%>
				<% if (!printview) { %><td><img src="/media_stat/images/layout/clear.gif" width="16" height="1"></td><% } %>
			</tr>
		</table>
		<% if (!printview) { %><div id="detail_list" style="width: 100%; height: 90%; overflow-y: scroll;"><% } %>
			<table width="100%" cellpadding="0" cellspacing="2" class="detail">
			<logic:iterate id="sku" collection="<%= selectedDeptInfo.getMatchingSkus() %>" type="com.freshdirect.fdstore.content.SkuModel">
			<tr valign="top">
				<td width="50%"><%= sku.getFullName() %></td>
				<td width="22%"><%= sku.getProduct().getMaterial().getMaterialNumber() %></td>
				<td width="28%"><%= sku.getEarliestAvailabilityMessage() %></td>
				<%-- td width="28%" align="right">8/27/2003 11:16 PM</td --%>
			</tr>
			<tr><td colspan="3" class="separator"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td></tr>
			</logic:iterate>
			</table>
		<% if (!printview) { %></div><% } %>
	<% } %>
</div>
<% } %>
<%= !printview && !overview ? "<br clear=\"all\">" : ""%>
<div style="width: 100%; height: 1px; border-top: solid 1px #000000;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></div>
</body>
</html>
