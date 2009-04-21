<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.fdstore.FDException"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
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

EnumSiteFeature siteFeature = EnumSiteFeature.YMAL;

/* customer Email */
String defaultCustomerEmail = "";

String customerEmail = urlG.get("customerEmail");
if (customerEmail == null || customerEmail.length() == 0 || !EmailUtil.isValidEmailAddress(customerEmail)) {
	customerEmail = defaultCustomerEmail;
}
if (defaultCustomerEmail.equals(customerEmail)) {
	urlG.remove("customerEmail");
} else {
	urlG.set("customerEmail", customerEmail);
}

/* customer Id */
String customerId = null;
TestSupport ts = TestSupport.getInstance();
customerId = ts.getErpIDForUserID(customerEmail);


/* cohort */
String defaultCohortId = "&lt;unknown&gt;";
String cohortId = defaultCohortId;
String defaultUserVariant = "&lt;unknown&gt;";
String userVariant = defaultUserVariant;
FDUserI user = null;
if (customerId != null) {
	user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
	cohortId = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
	RecommendationService userRs = SmartStoreUtil.getRecommendationService(user, 
			siteFeature, null);
}

/* No of Cycles */
String defaultNoOfCycles = "5";

String noOfCycles = urlG.get("noOfCycles");
if (noOfCycles == null || noOfCycles.length() == 0
		|| !NumberUtils.isNumber(noOfCycles)) {
	noOfCycles = defaultNoOfCycles;
}
if (defaultNoOfCycles.equals(noOfCycles)) {
	urlG.remove("noOfCycles");
} else {
	urlG.set("noOfCycles", noOfCycles);
}
int i_noOfCycles = 5;
try {
	i_noOfCycles = Integer.parseInt(noOfCycles);
} catch (NumberFormatException e) {
}
if (i_noOfCycles <= 0) {
	noOfCycles = defaultNoOfCycles;
	i_noOfCycles = 5;
}

// debug
System.err.println("# of cycles to simulate: " + noOfCycles);

/* redirect */
String newURL = urlG.build();

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>YMAL PERFORMANCE TEST PAGE</title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">

.test-page{margin:20px 60px;color:#333333;background-color:#fff;}
.test-page input{font-weight:normal;}
.test-page p{margin:0px;padding:0px;}
.test-page .bull{color:#cccccc;}
.test-page a{color:#336600;}.test-page a:VISITED{color:#336600;}
.test-page table{border-collapse:collapse;border-spacing:0px;width:100%;}
.test-page .rec-chooser{margin:0px 0px 6px;text-align:right;}
.test-page .rec-options{border:1px solid black;font-weight:bold;}
.test-page .rec-options .view-label{text-transform:capitalize;}
.test-page .rec-options table td{vertical-align: top; padding: 5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
table.rec-inner td {padding: 0px 2px !important; vertical-align: top !important;}
.test-page .var-comparator{margin-top:60px;margin-bottom:50px;}
.test-page .var-comparator td{width:50%;text-align:center;vertical-align:top;padding:10px;}
.test-page .var-comparator .left{padding-right:20px;}
.test-page .var-comparator .right{padding-left:20px;}
.test-page .var-comparator .var-cmp-head{margin-bottom:20px;}
.test-page .var-comparator .var-cmp-head .title14 {margin-bottom:5px;}
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

	</style>
</head>
<body class="test-page">
	<form method="get" action="<%= request.getRequestURI() %>">
    <div class="rec-options" class="rec-chooser title14">
    	<table style="width: auto;">
    		<tr>
    			<td class="text12">
    				<p class="label">
    					Customer email
    				</p>
    				<p>
    					<input type="text" name="customerEmail" value="<%= customerEmail %>"
    							title="Press &lt;Enter&gt; to activate the entered customer">
    				</p>
    				<% if (customerId != null) { %>
    				<p class="result">
    					Customer ID: <%= customerId %>
    				</p>
    				<p class="result">
    					Cohort ID: <%= cohortId %>
    				</p>
    				<% } else { %>
    				<p class="not-found result">
    					&lt;not found&gt;
    				</p>
    				<% } %>
				</td>
    			<td class="text12">
    				<p class="label">
    					<span
    							title="The # of test cycles the simulation will perform"
    							># of test cycles</span>
    				</p>
    				<p>
    					<input type="text" name="noOfCycles" value="<%= noOfCycles %>"
    							onfocus="this.select();"
    							onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
    							title="Press &lt;Enter&gt; to activate the entered value">
    				</p>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="2">
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
    <%
		VariantSelector vs = VariantSelectorFactory.getInstance(siteFeature);
	    List cohortNames = CohortSelector.getCohortNames();
	    RecommendationService smart = null;
	    Set recommenders = new HashSet(2);
	    for (int i = 0; i < cohortNames.size(); i++)
	    	recommenders.add(vs.getService((String) cohortNames.get(i)));
	    Iterator rIt = recommenders.iterator();
	    while (rIt.hasNext()) {
	    	RecommendationService rs = (RecommendationService) rIt.next();
	    	if (rs instanceof SmartYMALRecommendationService)
	    		smart = (SmartYMALRecommendationService) rs;
	    }
    %>
    <% if (user == null) { %>
    <table id="message" class="var-comparator"><tr><td>
    	<p class="title13 not-found" style="text-align: center;">Valid Customer Email is required. Please specify one to proceed tests!</p>
    </td></tr></table>
    <% } else if (smart == null) { %>
    <table id="message" class="var-comparator"><tr><td>
    	<p class="title13 not-found" style="text-align: center;">No Smart YMAL variant is configured!</p>
    </td></tr></table>
    <% } else { %>
    <table id="message" class="var-comparator"><tr><td>
    	<p class="text13 not-found" style="text-align: center;">Searching for Smart YMAL sets... <span id="message-result"></span></p>
    </td></tr></table>
    <%
    	pageContext.getOut().flush();
    
    	Map sets = new HashMap();
    	Set keys = CmsManager.getInstance().getContentKeysByType(ContentType.get("Product"));
		System.err.println("found " + keys.size() + " products");
    	Iterator it = keys.iterator();
    	while (it.hasNext()) {
    		ContentKey key = (ContentKey) it.next();
    		ProductModel p = (ProductModel) ContentFactory.getInstance().getContentNode(key.getId());
    		YmalSet set = p.getActiveYmalSet();

    		if (p != null && p.isDisplayable() && set != null
    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0
    				&& !sets.containsKey(set))
    			sets.put(set, p);
    	}

    	Set orphans = CmsManager.getInstance().getContentKeysByType(ContentType.get("YmalSet"));
		System.err.println("found " + orphans.size() + " YMAL sets");
    	it = orphans.iterator();
    	while (it.hasNext()) {
    		ContentKey key = (ContentKey) it.next();
    		YmalSet set = (YmalSet) ContentFactory.getInstance().getContentNode(key.getId());

    		if (set != null
    				&& set.getRecommenders() != null && set.getRecommenders().size() != 0
    				&& !sets.containsKey(set))
    			sets.put(set, null);
    	}
    	
    	NumberFormat format = NumberFormat.getInstance();
    %>
    <script type="text/javascript">
    	document.getElementById("message-result").innerHTML = "found <%= sets.size() %> set(s)";
    </script>
    <%
		pageContext.getOut().flush();
    %>
	<table class="var-comparator">
		<%
			it = sets.keySet().iterator();
			while (it.hasNext()) {
				YmalSet set = (YmalSet) it.next();
				ProductModel product = (ProductModel) sets.get(set);
				
			
		    	long runtime = Long.MIN_VALUE;
		    	long start = System.currentTimeMillis();
		    	int count = 0;
		    	for (int i = 0; i < i_noOfCycles; i++) {
	    			SessionInput input = new SessionInput(user);
	    			input.setCurrentNode(product);
	    			input.setYmalSource(set);
	    			input.setMaxRecommendations(6);
	    			List nodes = smart.recommendNodes(input);
	    			count += Math.min(6, nodes.size());
		    	}
		    	long end = System.currentTimeMillis();
		    	runtime = end - start;	
		%>
		<tr>
			<td class="left">
				<p class="title13">
					Smart Recommendations Runtime for <%= product == null ? " <span class=\"not-found\">orphan</span> " : "active" %> YMAL set <%= set.getAttribute("title", "") %>
					<% if (product != null) { %>with Product <%= product.getFullName() %><% } else { %>without product<% } %>:
					<span style="font-weight: normal"><%= format.format(runtime) %> milliseconds 
					(<%= format.format(runtime / i_noOfCycles) %> milliseconds / request)</span>
					- <%= format.format(((double) count) / i_noOfCycles) %> recommendations / request 
				</p>
			</td>
		</tr>
		<%
				pageContext.getOut().flush();
			}
		%>
	</table>
	<% } %>
	</form>
</body>
</html>