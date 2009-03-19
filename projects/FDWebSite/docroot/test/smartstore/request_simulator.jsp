<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Trigger"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%
Iterator it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

/* site feature */
EnumSiteFeature defaultSiteFeature = EnumSiteFeature.DYF;
List siteFeatures = EnumSiteFeature.getSmartStoreEnumList();

EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(urlG.get("siteFeature")); 
if (siteFeature == null) {
	siteFeature = defaultSiteFeature;
} else if (defaultSiteFeature.equals(siteFeature)) {
	urlG.remove("siteFeature");
}

Trigger trigger = new Trigger(siteFeature, EnumSiteFeature.YMAL.equals(siteFeature) ? 6 : 5);

Map variants = SmartStoreServiceConfiguration.getInstance().getServices(siteFeature);
VariantSelection helper = VariantSelection.getInstance();
final Map assignment = helper.getVariantMap(siteFeature);

List varIds = SmartStoreUtil.getVariantNamesSortedInUse(siteFeature);

/* variant */
String defaultVariant = null;


it = varIds.iterator();
if (it.hasNext()) {
	defaultVariant = (String) it.next();
} else {
	defaultVariant = null;
}

String variant = urlG.get("variant");

if (variant == null || !varIds.contains(variant)) {
	variant = defaultVariant;
}
if (variant != null && variant.equals(defaultVariant)) {
	urlG.remove("variant");
} else {
	urlG.set("variant", variant);
}

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
if (customerId != null) {
	FDUser user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
	cohortId = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
	RecommendationService userRs = SmartStoreUtil.getRecommendationService(user, 
			siteFeature, null);
	userVariant = userRs.getVariant().getId();
}

/* category Id */
String defaultCategoryId = "";

String categoryId = urlG.get("categoryId");
if (categoryId == null || categoryId.length() == 0) {
	categoryId = defaultCategoryId;
}
if (defaultCategoryId.equals(categoryId)) {
	urlG.remove("categoryId");
} else {
	urlG.set("categoryId", categoryId);
}

/* category */
ContentNodeModel category = defaultCategoryId.equals(categoryId) ? null : ContentFactory.getInstance().getContentNode(categoryId);
if (category != null) {
	if (!(category instanceof CategoryModel)) {
		category = null;
	}
}

/* category name */
String categoryName = null;
if (category != null) {
	categoryName = category.getFullName();
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

/* session input */
SessionInput si = new SessionInput(customerId);
si.setCartContents(Collections.EMPTY_SET);
si.setCurrentNode(category);
si.setNoShuffle(false);

/* redirect */
String newURL = urlG.build();

// debug
System.err.println("orig URI: " + origURL);
System.err.println("generated URI: " + newURL);
System.err.println("site feature: " + siteFeature.getName());
System.err.println("variant: " + variant);
System.err.println("customer Email: " + customerEmail);
System.err.println("customer Id: " + customerId);
System.err.println("cohort Id: " + cohortId);
System.err.println("user variant: " + userVariant);
System.err.println("category Id: " + categoryId);
System.err.println("category name: " + categoryName);
System.err.println("# of cycles to simulate: " + noOfCycles);

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>REQUEST SIMULATOR PAGE - <%= siteFeature.getName() %><%=
		variant != null ? " - " + variant : ""
	%></title>
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
.test-page .var-comparator{margin-top:60px;}
.test-page .var-comparator td{width:50%;text-align:center;vertical-align:top;}
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
    							onfocus="this.select();"
    							onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
    							title="Press &lt;Enter&gt; to activate the entered customer">
    				</p>
    				<% if (customerId != null) { %>
    				<p class="result">
    					Customer ID: <%= customerId %>
    				</p>
    				<p class="result">
    					Cohort ID: <%= cohortId %>
    				</p>
    				<p class="result">
    					User Variant: <%= userVariant %>
    				</p>
    				<% } else { %>
    				<p class="not-found result">
    					&lt;not found&gt;
    				</p>
    				<% } %>
				</td>
				<td class="text12">
    				<p class="label">
    					Category id
    				</p>
    				<p>
    					<input type="text" name="categoryId" value="<%= categoryId %>"
    							onfocus="this.select();"
    							onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
    							title="Press &lt;Enter&gt; to activate the entered category">
    				</p>
    				<% if (categoryName != null) { %>
    				<p class="result">
    					Category name: <%= categoryName %>
    				</p>
    				<% } else { %>
    				<p class="not-found result">
    					&lt;not found&gt;
    				</p>
    				<% } %>
				</td>
				<td class="text12">
					<p class="label">
						Feature
					</p>
					<p>
						<select name="siteFeature" onchange="this.form.submit();">
						<%
							it = siteFeatures.iterator();
							while (it.hasNext()) {
				    			EnumSiteFeature sf = (EnumSiteFeature) it.next(); 
						%>
							<option value="<%= sf.getName() %>"<%= sf.equals(siteFeature) ? " selected=\"selected\"" : ""%>><%= sf.getTitle() %></option>
						<%
							}
						%>
						</select>
					</p>
    			</td>
    			<td class="text12">
					<p class="label">Variant</p>
					<p>
					<select name="variant" onchange="this.form.submit();">
					<%
						String optGroup = "";
						it = varIds.iterator();
						if (it.hasNext()) {
							String varId = (String) it.next();
							optGroup = assignment.containsValue(varId) ? "In Use" : "Not in Use";
					%>
					<optgroup label="<%= optGroup %>">
						<option <%= varId.equals(userVariant) ? "class=\"selected\" " : (assignment.containsValue(varId) ? "" : "class=\"disabled\" ") 
								%>value="<%= varId %>"<%= varId.equals(variant) ? " selected=\"selected\"" : ""%>><%= varId %></option>
					<%
							if (!it.hasNext()) {
					%>
					</optgroup>
					<%
							}
						}
						while (it.hasNext()) {
							String varId = (String) it.next();
							String newGroup = assignment.containsValue(varId) ? "In Use" : "Not in Use";
							if (!newGroup.equals(optGroup)) {
								optGroup = newGroup;
					%>
					</optgroup>
					<optgroup label="<%= optGroup %>">
					<%
							}
					%>
						<option <%= varId.equals(userVariant) ? "class=\"selected\" " : (assignment.containsValue(varId) ? "" : "class=\"disabled\" ") 
								%>value="<%= varId %>"<%= varId.equals(variant) ? " selected=\"selected\"" : ""%>><%= varId %></option>
					<%
							if (!it.hasNext()) {
					%>
					</optgroup>
					<%
							}
						}
					%>
					</select>
					</p>
					<p class="not-found result">
					<% if (variant != null && variant.equals(userVariant)) { %>
						<b>This is the user's variant.</b>
					<% } else if (!assignment.containsValue(variant)) { %>
						<i>This variant is not assigned to a cohort.</i>
					<% } else { %>
						&nbsp;
					<% } %>
					</p>
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
    	</table>
    </div>
    <%
    final Map clone = new HashMap();
	if (variant != null) {
		RecommendationService rs = (RecommendationService) variants.get(variant);
		for (int i = 0; i < i_noOfCycles; i++ ) {
			try {
				List recs = rs.recommendNodes(trigger, si);
				if (recs.size() > trigger.getMaxRecommendations())
					recs = recs.subList(0, trigger.getMaxRecommendations());
				Iterator it2 = recs.iterator();
				while (it2.hasNext()) {
					ProductModel pm = (ProductModel) it2.next();
					int count = clone.containsKey(pm) ? ((Integer) clone.get(pm)).intValue() + 1 : 1;
					clone.put(pm, new Integer(count));
				}
			} catch (RuntimeException e) {
				e.printStackTrace(System.err);
			}
		}
	}
	System.err.println("clone size: " + clone.size());
    Map products = new TreeMap(new Comparator() {
    	public int compare(Object o1, Object o2) {
			if (o1 == null) {
				if (o2 == null)
					return 0;
				else
					return -1;
			} else {
				if (o2 == null)
					return -1;
				else {
					ProductModel pm1 = (ProductModel) o1;
					ProductModel pm2 = (ProductModel) o2;
					int i1 = ((Integer) clone.get(pm1)).intValue();
					int i2 = ((Integer) clone.get(pm2)).intValue();
					System.err.println("pm1: " + pm1.getContentName() + ", " + i1);
					System.err.println("pm2: " + pm2.getContentName() + ", " + i2);
					if (i2 == i1) {
						int c = pm1.getFullName().compareToIgnoreCase(pm2.getFullName());
						if (c == 0)
							return pm1.getContentName().compareTo(pm2.getContentName());
						else
							return c;
					} else
						return i2 - i1;
				}
			}
    	}
    });
    products.putAll(clone);
	System.err.println("products size: " + products.size());
    %>
	<table class="var-comparator">
		<tr>
			<td class="left">
				<table class="prod-items">
					<tr>
						<td class="info">
							<span class="text13bold" style="text-transform: uppercase;" title="Site feature name"><%= siteFeature.getTitle() %></span>
							<span class="text13bold" title="Variant name">(<%= variant %>)</span>
						</td>
						<td class="info"></td><td class="value"></td><td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13" title="Date">
								<%= SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(new Date()) %>
							</span>
						</td>
						<td class="info"></td><td class="value"></td><td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13" title="Customer email"><%= customerEmail.length() != 0 ? customerEmail : "&lt;user unknown&gt;" %></span>
						</td>
						<td class="info"></td><td class="value"></td><td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13" title="No. of requests"><%= noOfCycles %> requests</span>
						</td>
						<td class="info"></td><td class="value"></td><td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13">&nbsp;</span>
						</td>
						<td class="info"></td>
						<td class="value"></td>
						<td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13bold" title="Full name">Full name</span>
						</td>
						<td class="info">
							<span class="text13bold" title="Content id">ProductID</span>
						</td>
						<td class="value">
							<span class="text13bold" title="Impression count">Impressions</span>
						</td>
						<td class="value">
							<span class="text13bold" title="Percentage">% Impr.</span>
						</td>
					</tr> 
				<%
						int sum = 0;
						it = products.keySet().iterator();
						while (it.hasNext()) {
							ProductModel pm = (ProductModel) it.next();
							int count = ((Integer) products.get(pm)).intValue();
							sum += count;
						}
						it = products.keySet().iterator();
						DecimalFormat pctFmt = new DecimalFormat("#0.00%");
						while (it.hasNext()) {
							ProductModel pm = (ProductModel) it.next();
							int count = ((Integer) products.get(pm)).intValue();
							String percent = pctFmt.format(((double) count) / ((double) sum));
				%>
					<tr>
						<td class="info">
								<span class="text13" title="Full name"><%= pm.getFullName() %></span>
						</td>
						<td class="info">
								<span class="text13" title="Content id"><%= pm.getContentName() %></span>
						</td>
						<td class="value">
							<span class="text13" title="Impression count"><%= count %></span>
						</td>
						<td class="value">
							<span class="text13" title="Percentage"><%= percent %></span>
						</td>
					</tr> 
				<%
						} // end while
				%>
					<tr>
						<td class="info" style="text-align: right;">
								<span class="text13">TOTAL</span>
						</td>
						<td class="info">
						</td>
						<td class="value">
							<span class="text13" title="Impression count"><%= sum %></span>
						</td>
						<td class="value">
						</td>
					</tr> 
				</table>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>