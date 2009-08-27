<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
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
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

/* site feature */
EnumSiteFeature defaultSiteFeature = EnumSiteFeature.DYF;
List siteFeatures = EnumSiteFeature.getSmartStoreEnumList();
Collections.sort(siteFeatures);

EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(urlG.get("siteFeature")); 
if (siteFeature == null) {
	siteFeature = defaultSiteFeature;
} else if (defaultSiteFeature.equals(siteFeature)) {
	urlG.remove("siteFeature");
}

Map variants = SmartStoreServiceConfiguration.getInstance().getServices(siteFeature);
VariantSelection helper = VariantSelection.getInstance();
final Map assignment = helper.getVariantMap(siteFeature);

List varIds = SmartStoreUtil.getVariantNamesSortedInUse(siteFeature);

/* variant */
String defaultVariant = null;


Iterator it = varIds.iterator();
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

/* customer Use Logged In */
boolean useLoggedIn = false;

String useLoggedInStr = urlG.get("useLoggedIn");
if ("true".equalsIgnoreCase(useLoggedInStr)) {
	useLoggedIn = true;
}
if (useLoggedIn) {
	urlG.set("useLoggedIn", "true");
} else {
	urlG.remove("useLoggedIn");
}

FDUserI loggedUser = (FDUserI) session.getAttribute("fd.user");

/* customer Email */
String defaultCustomerEmail = "";

String customerEmail = urlG.get("customerEmail");
if (customerEmail == null || customerEmail.length() == 0 || !EmailUtil.isValidEmailAddress(customerEmail)) {
	customerEmail = defaultCustomerEmail;
}
if (defaultCustomerEmail.equals(customerEmail) || useLoggedIn) {
	urlG.remove("customerEmail");
} else {
	urlG.set("customerEmail", customerEmail);
}

/* customer Id */
/* cohort */
String customerId = null;
String primaryKey = null;
String defaultCohortId = "<span class=\"not-found\">&lt;unknown&gt;</span>";
String cohortId = defaultCohortId;
String defaultuserStrategy = "<span class=\"not-found\">&lt;unknown&gt;</span>";
String userStrategy = defaultuserStrategy;
String userServiceType = defaultuserStrategy;
SessionInput input = null;
FDUserI user = null;

TestSupport ts = TestSupport.getInstance();
if (useLoggedIn) {
	if (loggedUser != null) {
		user = loggedUser;
		if (user != null && user.getIdentity() != null)
			customerId = user.getIdentity().getErpCustomerPK();
	}
} else {
	customerId = ts.getErpIDForUserID(customerEmail);
	try {
		user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
	} catch (Exception e) {
		
	}
}
if (user != null) {
	primaryKey = user.getPrimaryKey();
	if (user.getUserServiceType() != null)
		userServiceType = user.getUserServiceType().getName();
	cohortId = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
	userStrategy = VariantSelectorFactory.getInstance(EnumSiteFeature.CART_N_TABS).getService(cohortId).getVariant().getId();
}
input = new SessionInput(user);
input.setMaxRecommendations(EnumSiteFeature.YMAL.equals(siteFeature) ? 6 : 5);

String userVariant = null;
if (assignment.containsKey(cohortId)) {
	userVariant = (String) assignment.get(cohortId);
}

String trigError = "";
ProductModel source = null;
if (useLoggedIn) {
	if (user != null) {
	    FDStoreRecommender.initYmalSource(input, user, request);
	}
} else {
	String triggeringProduct = urlG.get("triggeringProduct");
	if (triggeringProduct != null && triggeringProduct.trim().length() != 0) {
		ContentNodeModel node = ContentFactory.getInstance().getContentNode(triggeringProduct.trim());
		if (node != null && node instanceof ProductModel)
			source = (ProductModel) node; 
	}
}
if (input != null && source != null) {
	input.setYmalSource(source);
	input.setCurrentNode(source);
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

/* redirect */
String newURL = urlG.build();

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
.test-page .rec-chooser{margin:0px 0px 6px;text-align:left;}
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
    <div class="rec-chooser title14">
    		<%  if (loggedUser != null) {
    				if (loggedUser.getIdentity() != null) {
    		%>
    		Logged in as <%= loggedUser.getUserId() %>.
    		<%      } else { %>
    		Not logged in. You are in area  <%= loggedUser.getZipCode() %>.
    		<% 		}
    			} else { %>
    		Not logged in.
    		<% 	} %>
    </div>
    <div class="rec-options" class="rec-chooser title14">
    	<table style="width: auto;">
    		<tr>
   				<td class="text12">
    				<p class="label">
    					Customer email
    				</p>
    				<p>
    					<input type="text" name="customerEmail" id="customerEmail" value="<%= customerEmail %>"
    							onfocus="this.select();"<%= useLoggedIn ? " disabled=\"disabled\"" : "" %>>
    				</p>
    				<p class="label">
    					or use logged in: <input type="checkbox" name="useLoggedIn" id="useLoggedIn"
    						onchange="document.getElementById('customerEmail').disabled = this.checked;
    						document.getElementById('triggeringProduct').disabled = this.checked;
    						document.getElementById('useLoggedCart').checked = this.checked;"
    						value="true"<%= useLoggedIn ? " checked" : ""%>>
    				</p>
    				<% if (customerId != null) { %>
    				<p class="result">
    					Customer ID: <%= customerId %>
    				</p>
    				<% } else { %>
    				<p class="result">
    					Customer ID: <span class="not-found">&lt;not found&gt;</span>
    				</p>
    				<% } %>
    				<p class="result">
    					Cohort ID: <%= cohortId %>
    				</p>
    				<p class="result">
    					Cart Strategy: <%= userStrategy %>
    				</p>
    				<p class="result">
    					Customer Service Type: <%= userServiceType %>
    				</p>
   				</td>
				<td class="text12">
	   				<p class="label">Triggering product:</p>
					<p>
						<input type="text" name="triggeringProduct" id="triggeringProduct"
							value="<%= source != null ? source.getContentKey().getId() : "" %>"<%= useLoggedIn ? " disabled=\"disabled\"" : "" %>>
					</p>
	   				<p class="label">
	   					or use logged user's cart: <input type="checkbox" name="useLoggedIn" id="useLoggedCart"
	   						onchange="document.getElementById('triggeringProduct').disabled = this.checked;
	   						document.getElementById('customerEmail').disabled = this.checked;
	   						document.getElementById('useLoggedIn').checked = this.checked;"
	   						value="true"<%= useLoggedIn ? " checked" : ""%>>
	   				</p>
	   				<% if (useLoggedIn && user != null) { %>
	   					<% Set cartItems = FDStoreRecommender.getShoppingCartContents(user);
	   					   if (cartItems == null)
	   						   cartItems = Collections.EMPTY_SET; 
						   it = cartItems.iterator(); %>
	   					<% if (!it.hasNext()) {	%>
	   				<p class="result not-found">
	   						No recent items in cart.
	   				</p>
	   					<% } else { %>
	   				<p class="result">
	   						Items in cart:
	   				</p>
	   					<% } %>
	   					<% while (it.hasNext()) {
	   						ProductModel product = (ProductModel) it.next();
	   					%>
	   				<p style="font-weight: normal;" title="<%= product.getFullName() %>"
	   						><%= product.getContentKey().getId() %></p>
	   					<% } %>
	   				<% } %>
	   				<p class="result">
	   					Triggering item: <% 
	   						if (source != null) { 
	   						%><span style="font-weight: normal;" title="<%= source.getFullName() + " (" +
	   								source.getContentKey().getType().getName() + ")" %>"><%= source.getContentKey().getId() %></span><% 
	   						} else { 
	   						%><span class="not-found">&lt;unidentified&gt;</span><% 
	   						} %>
	   				</p>
	   				<%
	   				   if (trigError.length() != 0) { %>
	   				<p class="not-found result">
	   					<%= trigError %>
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
    		<tr>
    			<td colspan="5">
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
    <%
    final Map clone = new HashMap();
	long start = System.currentTimeMillis();
	if (variant != null) {
		RecommendationService rs = (RecommendationService) variants.get(variant);
		for (int i = 0; i < i_noOfCycles; i++ ) {
			try {
				List recs = rs.recommendNodes(input);
				if (recs.size() > input.getMaxRecommendations())
					recs = recs.subList(0, input.getMaxRecommendations());
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
	double time = variant != null ? System.currentTimeMillis() - start : Double.NaN;
	double perReq = i_noOfCycles > 0 ? time / (double) i_noOfCycles : Double.NaN;
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
	NumberFormat format = NumberFormat.getInstance();
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
							<span class="text13" title="Total execution time">Total execution time</span>
						</td>
						<td class="value">
							<span class="text13" title="Total execution time"><%= format.format((double) time) %> ms</span>
						</td>
						<td class="value"></td><td class="value"></td>
					</tr> 
					<tr>
						<td class="info">
							<span class="text13" title="Average execution time">Avg. exec. time per request</span>
						</td>
						<td class="value">
							<span class="text13" title="Average execution time"><%= format.format((double) perReq) %> ms</span>
						</td>
						<td class="value"></td><td class="value"></td>
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