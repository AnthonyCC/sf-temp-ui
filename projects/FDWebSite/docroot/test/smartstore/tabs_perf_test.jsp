<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.text.NumberFormat" %>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.CartTabRecommender"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.TabRecommendation"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri='logic' prefix='logic' %>
<fd:CheckLoginStatus noRedirect="true" />
<%
	URLGenerator urlG = new URLGenerator(request);
	String origURL = urlG.build();

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
	
	String trigError = "";
	ProductModel source = null;
	if (useLoggedIn) {
		if (user != null) {
		    FDStoreRecommender.initYmalSource(input, user);
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
	if (input != null)
		input.setMaxRecommendations(5);
	
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
<%@page import="java.util.Set"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CART TABS PERFORMANCE TEST PAGE</title>
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
.test-page .rec-options table {width:auto;}
.test-page .rec-options table td{vertical-align:top;padding:5px 10px 10px;}
.test-page .rec-options table td p.label{padding-bottom:4px;}
.test-page .rec-options table td p.result{padding-top:4px;}
.test-page .rec-options table div{padding-right:20px;}
.test-page .var-comparator{margin-top:60px;}
.test-page .var-comparator td{width:50%;text-align:center;vertical-align:top;}
.test-page .var-comparator .left{padding-right:20px;}
.test-page .var-comparator .right{padding-left:20px;}
.test-page .var-comparator .var-cmp-head{margin-bottom:20px;}
.test-page .var-comparator .var-cmp-head .title14 {margin-bottom:5px;}
.test-page .prod-items td{border:1px solid black;width:auto;padding:5px;}
.test-page .prod-items td.pic{width:100px;}
.test-page .prod-items td.info{text-align:left;vertical-align:top;}
.test-page .prod-items .taxonomy{font-style:italic;}
.test-page .prod-items td.info div{position:relative;height:80px;}
.test-page .prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.test-page .prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.test-page .prod-items .positive{color:#006600;}
.test-page .prod-items .negative{color:#990000;}
.test-page .prod-items .unknown{color:#FF9900;}
table.priorities {margin: 40px 0px 20px;width: auto;}
table.priorities th {border:1px solid black;vertical-align: top; padding: 8px; font-weight: bold;}
table.priorities td {border:1px solid black;vertical-align: top; padding: 8px;}
.gap {margin-top: 10px}
.n_weight{font-weight:normal;}
.not-found{color:red;}
p.error{color:red !important;margin:20px 0px;}
p.fi{margin:20px 0px;}
	</style>
</head>
<body class="test-page">
	<form method="get" action="<%=request.getRequestURI()%>">
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
   				<td>
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
				<td>
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
						   Iterator it = cartItems.iterator(); %>
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
    			<td colspan="3">
    				<p>
    					<input type="submit" value="Submit">
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
	</form>
	<table class="priorities">
		<thead>
			<tr>
				<th class="text13">Tab #1</th>
				<th class="text13">Tab #2</th>
				<th class="text13">Tab #3</th>
			</tr>
		</thead>
	<%
		EnumSiteFeature[][] tabs = new EnumSiteFeature[i_noOfCycles][];
		if (user != null && input != null) {
			long start = System.currentTimeMillis();
			for (int i = 0; i < i_noOfCycles; i++) {
				TabRecommendation tr = CartTabRecommender.recommendTabs(user, input, null);
				tabs[i] = new EnumSiteFeature[3];
				for (int j = 0; j < tr.size(); j++) {
					Variant variant = tr.get(j);
					EnumSiteFeature siteFeature = variant.getSiteFeature();
					tabs[i][j] = siteFeature;
				}
			}
			long time = System.currentTimeMillis() - start;
			double perReq = i_noOfCycles > 0 ? time / (double) i_noOfCycles : 0.0;
			for (int i = 0; i < i_noOfCycles; i++) {
	%>
		<tr>
	<%
				for (int j = 0; j < 3; j++) {
					EnumSiteFeature siteFeature = tabs[i][j];
					String title = siteFeature != null ? siteFeature.getTitle() : "";
					String name = siteFeature != null ? siteFeature.getName() : "&lt;none&gt;";
	%>
			<td class="text13" title="<%= title %>"><%= name %></td>
	<%
				}
	%>
		</tr>
	<%
			}
	    	NumberFormat format = NumberFormat.getInstance();
	%>
	</table>
	<span class="text13">Total execution time: <%= format.format((double) time) %> ms,
			Average ex. time per request: <%= format.format(perReq) %> ms</span>
	<%
		} else {
	%>
	<span class="text13 not-found">Missing required parameters</span>
	<%
		}
	%>
</body>
</html>