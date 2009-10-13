<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.TreeSet"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.cms.AttributeI"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.FDException"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.YmalSet"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.impl.ClassicYMALRecommendationService"%>
<%@page import="com.freshdirect.smartstore.impl.SmartYMALRecommendationService"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.framework.util.CSVUtils"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%><%

URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

boolean departments;
if ("true".equalsIgnoreCase(urlG.get("departments"))
		|| "1".equalsIgnoreCase(urlG.get("departments")))
	departments = true;
else
	departments = false;
if (departments)
	urlG.set("departments", "1");
else
	urlG.remove("departments");


boolean categories;
if ("true".equalsIgnoreCase(urlG.get("categories"))
		|| "1".equalsIgnoreCase(urlG.get("categories")))
	categories = true;
else
	categories = false;
if (categories)
	urlG.set("categories", "1");
else
	urlG.remove("categories");


boolean products;
if ("true".equalsIgnoreCase(urlG.get("products"))
		|| "1".equalsIgnoreCase(urlG.get("products")))
	products = true;
else
	products = false;
if (products)
	urlG.set("products", "1");
else
	urlG.remove("products");

Logger LOG = LoggerFactory.getInstance("view_ymals.jsp");

// debug
LOG.info("show departments: " + departments);
LOG.info("show categories: " + categories);
LOG.info("show products: " + products);

/* redirect */
String newURL = urlG.build();

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

%><%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VIEW SMART YMALS PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:right;}
.test-page .rec-options{border:1px solid black;font-weight:bold;margin-bottom:50px;}
.test-page .rec-options .view-label{text-transform:capitalize;}
.test-page .rec-options table td{vertical-align: top; padding: 5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
table.rec-inner td {padding: 0px 2px !important; vertical-align: top !important;}
.test-page .msg-panel{margin-bottom:50px;}
.test-page .msg-panel td{text-align:center;vertical-align:top;}
.test-page .var-comparator{margin-bottom:50px;width:auto;}
.test-page .var-comparator td{border:1px solid black;text-align:left;vertical-align:top;padding:10px;}
.test-page .prod-items {width:auto;}
.test-page .prod-items td{border:1px solid black;width:auto;padding:5px;}
.test-page .prod-items td.pic{width:100px;}
.test-page .prod-items td.info{text-align:left;vertical-align:top;}
.test-page .prod-items td.value{text-align:right;vertical-align:middle;}
.test-page .prod-items .taxonomy{font-style:italic;}
.test-page .prod-items td.info div{position:relative;height:80px;}
.test-page .prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.test-page .prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.test-page .prod-items .positive{color:#006600;}
.test-page .prod-items .negative{color:#990000;}
.test-page .prod-items .unknown{color:#FF9900;}
.not-found{color:red;}
.disabled{color:gray;font-style:italic;}
.selected{font-weight:bold;color:blue;}
.inactive{color:#999 !important;border-color:#999 !important;}

	</style>
</head>
<body class="test-page">
	<form method="get" action="<%= request.getRequestURI() %>">
    <div class="rec-options" class="rec-chooser title14">
    	<table style="width: auto;">
    		<tr>
    			<td class="text12">
    				<p class="label">
    					View options (which relations to show)
    				</p>
    				<p>
    					<input type="checkbox" name="departments" value="1"<%= departments ? " checked=\"checked\"" : "" %>> Departments<br>
    					<input type="checkbox" name="categories" value="1"<%= categories ? " checked=\"checked\"" : "" %>> Categories<br>
    					<input type="checkbox" name="products" value="1"<%= products ? " checked=\"checked\"" : "" %>> Products (and Configured Products)
    				</p>
    				<p class="not-found result">
    					WARNING: Checking Products can cause serious performance degradation and extremely high memory consumption!!!
    				</p>
				</td>
    		</tr>
    		<tr>
    			<td>
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
    <table class="msg-panel"><tr><td>
    	<p id="message" class="text13 not-found" style="text-align: center;">Searching for Smart YMAL sets...</p>
    </td></tr></table>
    <%
    	pageContext.getOut().flush();
    
    	Map sets = new TreeMap(new Comparator() {
    		public int compare(Object o1, Object o2) {
    			YmalSet y1 = (YmalSet) o1;
    			YmalSet y2 = (YmalSet) o2;
    			
    			if (y1.isActive() != y2.isActive())
    				return y2.isActive() ? +1 : -1;
    			else
    				return y1.getAttribute("title", "").compareToIgnoreCase(y2.getAttribute("title", ""));
    		}
    	});

    	Set keys = CmsManager.getInstance().getContentKeysByType(ContentType.get("YmalSet"));
		LOG.info("found " + keys.size() + " YMAL sets");
    	Iterator it = keys.iterator();
    	while (it.hasNext()) {
    		ContentKey key = (ContentKey) it.next();
    		YmalSet set = (YmalSet) ContentFactory.getInstance().getContentNode(key.getId());

    		if (set != null
    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0
    				&& !sets.containsKey(set))
    			sets.put(set, new TreeSet(new Comparator() {
    				public int compare(Object o1, Object o2) {
    					ContentNodeModel n1 = (ContentNodeModel) o1;
    					ContentNodeModel n2 = (ContentNodeModel) o2;
    					
    					return n1.getFullName().compareToIgnoreCase(n2.getFullName());
    				}
    			}));
    	}
	%>
	<script type="text/javascript">
		document.getElementById("message").innerHTML = "Searching for Departments...";
	</script>
	<%
		pageContext.getOut().flush();

    	if (departments) {
	    	keys = CmsManager.getInstance().getContentKeysByType(ContentType.get("Department"));
			LOG.info("found " + keys.size() + " departments");
	    	it = keys.iterator();
	    	while (it.hasNext()) {
	    		ContentKey key = (ContentKey) it.next();
	    		DepartmentModel node = (DepartmentModel) ContentFactory.getInstance().getContentNode(key.getId());
	    		AttributeI attr = node.getNotInheritedAttribute("ymalSets");
	    		if (attr != null) {
	    			Object obj = attr.getValue();
	    			if (obj instanceof List) {
	    				List ymals = (List) obj;
	    				Iterator it2 = ymals.iterator();
	    				while (it2.hasNext()) {
	    					ContentKey key1 = (ContentKey) it2.next();
	    					YmalSet set = (YmalSet) ContentFactory.getInstance().getContentNode(key1.getId());
	    					if (set != null
	    		    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0)
	    						((TreeSet) sets.get(set)).add(node);
	    				}
	    			}
	    		}
	    	}
    	}
	%>
	<script type="text/javascript">
		document.getElementById("message").innerHTML = "Searching for Categories...";
	</script>
	<%
		pageContext.getOut().flush();

    	if (categories) {
	    	keys = CmsManager.getInstance().getContentKeysByType(ContentType.get("Category"));
			LOG.info("found " + keys.size() + " categories");
	    	it = keys.iterator();
	    	while (it.hasNext()) {
	    		ContentKey key = (ContentKey) it.next();
	    		CategoryModel node = (CategoryModel) ContentFactory.getInstance().getContentNode(key.getId());
	    		AttributeI attr = node.getNotInheritedAttribute("ymalSets");
	    		if (attr != null) {
	    			Object obj = attr.getValue();
	    			if (obj instanceof List) {
	    				List ymals = (List) obj;
	    				Iterator it2 = ymals.iterator();
	    				while (it2.hasNext()) {
	    					ContentKey key1 = (ContentKey) it2.next();
	    					YmalSet set = (YmalSet) ContentFactory.getInstance().getContentNode(key1.getId());
	    					if (set != null
	    		    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0)	    							
	    						((TreeSet) sets.get(set)).add(node);
	    				}
	    			}
	    		}
	    	}
    	}
	%>
	<script type="text/javascript">
		document.getElementById("message").innerHTML = "Searching for Products...";
	</script>
	<%
		pageContext.getOut().flush();

		if (products) {
	    	keys = new HashSet(CmsManager.getInstance().getContentKeysByType(ContentType.get("Product")));
	    	keys.addAll(CmsManager.getInstance().getContentKeysByType(ContentType.get("ConfiguredProduct")));
			LOG.info("found " + keys.size() + " products");
	    	it = keys.iterator();
	    	while (it.hasNext()) {
	    		ContentKey key = (ContentKey) it.next();
	    		ProductModel node = (ProductModel) ContentFactory.getInstance().getContentNode(key.getId());
	    		AttributeI attr = node.getNotInheritedAttribute("ymalSets");
	    		if (attr != null) {
	    			Object obj = attr.getValue();
	    			if (obj instanceof List) {
	    				List ymals = (List) obj;
	    				Iterator it2 = ymals.iterator();
	    				while (it2.hasNext()) {
	    					ContentKey key1 = (ContentKey) it2.next();
	    					YmalSet set = (YmalSet) ContentFactory.getInstance().getContentNode(key1.getId());
	    					if (set != null
	    		    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0)
	    						((TreeSet) sets.get(set)).add(node);
	    				}
	    			}
	    		}
	    	}
    	}
    %>
    <script type="text/javascript">
    	document.getElementById("message").style.display = "none";
    </script>
    <%
		pageContext.getOut().flush();
    %>
	<table class="var-comparator">
		<%
			it = sets.keySet().iterator();
			while (it.hasNext()) {
				YmalSet set = (YmalSet) it.next();
		%>
		<tr>
			<td class="title13<%= !set.isActive() ? " inactive" : "" %>" title="<%= set.isActive() ? "ACTIVE" : "INACTIVE" %>">
				<%= set.getAttribute("title", "") %> <span style="font-weight: normal;">(<%= set.getContentKey().getId() %>)</span>
			</td>
			<td class="text13<%= !set.isActive() ? " inactive" : "" %>">
				<%	Iterator it2 = ((TreeSet) sets.get(set)).iterator();
					while (it2.hasNext()) {
						ContentNodeModel node = (ContentNodeModel) it2.next();						
				%>
				<span title="<%= node.getContentKey().getType() %>"><%= node.getFullName() %> (<%= node.getContentKey().getId() %>)</span><br>
				<%	}
				%>
			</td>
		</tr>
		<%
			}
		%>
	</table>
	</form>
</body>
</html>