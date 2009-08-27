<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.SortedMap"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.freshdirect.cms.ContentNodeI"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.CartTabRecommender"%>
<%@page import="com.freshdirect.smartstore.CartTabStrategyPriority"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
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
			user.updateUserState();
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
	if (input != null)
		input.setMaxRecommendations(5);
	
	/* redirect */
	String newURL = urlG.build();


	if (!origURL.equals(newURL)) {
		response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));
	}
%>
<%@page import="com.freshdirect.smartstore.TabRecommendation"%>
<%@page import="java.util.Set"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CART TABS TEST PAGE</title>
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
table.priorities {float: left;margin: 40px 0px 20px;width: auto;}
table.priorities th {border:1px solid black;vertical-align: top; padding: 8px; font-weight: bold;}
table.priorities td {border:1px solid black;vertical-align: top; padding: 8px;}
.gap {margin-left: 20px !important;}
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
	</form>
	<% 
	RecommendationService rs = VariantSelectorFactory.getInstance(EnumSiteFeature.CART_N_TABS).getService(cohortId);
	SortedMap priorities = rs != null ? rs.getVariant().getTabStrategyPriorities() : new TreeMap();
	if (priorities != null) {
	%>
	<table class="priorities">
		<thead>
			<tr>
				<th class="text13">Primary priority</th>
				<th class="text13">Secondary priority</th>
				<th class="text13">Site Feature</th>
			</tr>
		</thead>
	<%
		Iterator it = priorities.entrySet().iterator();
		while (it.hasNext()) {
			SortedMap.Entry entry = (SortedMap.Entry) it.next();
			Integer p1 = (Integer) entry.getKey();
			SortedMap sp = (SortedMap) entry.getValue();
	%>
		<tr>
			<td class="text13" rowspan="<%= sp.size() %>"><%= p1.toString() %></td>
	<%
			Iterator it2 = sp.entrySet().iterator();
			while (it2.hasNext()) {
				SortedMap.Entry e2 = (SortedMap.Entry) it2.next();
				Integer p2 = (Integer) e2.getKey();
				CartTabStrategyPriority priority = (CartTabStrategyPriority) e2.getValue();
				EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(priority.getSiteFeatureId());
	%>
			<td class="text13"><%= p2.toString() %></td>
			<td class="text13" title="<%= siteFeature.getTitle() %>"><%= siteFeature.getName() %></td>
		</tr>
	<%
			}
		}
	%>
	</table>
	<table class="priorities gap">
		<thead>
			<tr>
				<th class="text13">Recommended Tabs / Site Features</th>
			</tr>
		</thead>
	<%
		if (user != null && input != null) {
			TabRecommendation tr = CartTabRecommender.recommendTabs(user, input, null);
			for (int i = 0; i < tr.size(); i++) {
				Variant variant = tr.get(i);
				EnumSiteFeature siteFeature = variant.getSiteFeature();
				String url = "/test/smartstore/compare_variants.jsp";
				url += "?siteFeature=" + URLEncoder.encode(siteFeature.getName(), "utf-8");
				if (useLoggedIn)
					url += "&useLoggedIn=true";
				else
					url += "&customerEmail=" + URLEncoder.encode(customerEmail, "utf-8");
				url += "&variantA=" + URLEncoder.encode(variant.getId(), "utf-8");
				if (!useLoggedIn && source != null) {
					if (EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature))
						url += "&categoryId=" + URLEncoder.encode(source.getParentNode().getContentKey().getId(), "utf-8");
					else
						url += "&orderlines=" + URLEncoder.encode(source.getContentKey().getId(), "utf-8");
				}
	%>
		<tr>
			<td class="text13" title="<%= siteFeature.getTitle() %>"><%= "" + (i + 1) %>. <a href="<%= url %>"><%= siteFeature.getName() %></a></td>
		</tr>
	<%
			}
		} else {
	%>
			<tr>
				<td class="text13">&lt;No recommendations are returned.&gt;</td>
			</tr>
	<%
		}
	%>
	</table>
	<div style="clear: both;"></div>
	<%
	}
	%>
</body>
</html>