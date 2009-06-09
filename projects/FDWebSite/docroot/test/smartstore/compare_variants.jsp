<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.WeakHashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.ContentKey.InvalidContentKeyException"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.content.CategoryModel"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.fdstore.content.YmalSource"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineModel"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.fdstore.customer.FDUser"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.URLGenerator"%>
<%@page import="com.freshdirect.mail.EmailUtil"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.FactorUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.StoreLookup"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.dsl.CompileException"%>
<%@page import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>

<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<fd:CheckLoginStatus noRedirect="true" /><%!
	Map customRecommenders = new WeakHashMap();
%><%
Iterator it;
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

/* generator function */
String generatorFunction = urlG.get("generatorFunction");
if (generatorFunction != null) {
    generatorFunction = generatorFunction.trim();
}

/* scoring function */
String scoringFunction = urlG.get("scoringFunction");
if (scoringFunction != null) {
    scoringFunction = scoringFunction.trim();
}

if (!(scoringFunction != null && scoringFunction.length() > 0)) {
    urlG.remove("scoringFunction");
    scoringFunction = null;
}

RecommendationService scriptedRecServ = null;  
String compileErrorMessage = null;
if (generatorFunction != null && generatorFunction.length() > 0) {
    scriptedRecServ = (RecommendationService) customRecommenders.get(generatorFunction + "@@@" + scoringFunction);
	if (scriptedRecServ == null) {
        //SmartStoreServiceConfiguration.configureSampler(new RecommendationServiceConfig("scripted-test", RecommendationServiceType.SCRIPTED))
    	scriptedRecServ = SmartStoreServiceConfiguration.configure(new Variant("scripted-test", siteFeature, new RecommendationServiceConfig("scripted-test",
    	        RecommendationServiceType.SCRIPTED).set("generator", generatorFunction).set("scoring", scoringFunction)));
    	customRecommenders.put(generatorFunction + "@@@" + scoringFunction, scriptedRecServ);
		if (scriptedRecServ instanceof NullRecommendationService)
    		compileErrorMessage = "Failed to create script recommender. Possible Script Compile Exception.";
	}
} else {
    urlG.remove("generatorFunction");
    urlG.remove("scoringFunction");
    scoringFunction = null;
    generatorFunction = null;
}



/* variant A */
String defaultVariantA = null;


it = varIds.iterator();
if (it.hasNext()) {
	defaultVariantA = (String) it.next();
} else {
	defaultVariantA = null;
}

String variantA = urlG.get("variantA");

if (variantA == null || !varIds.contains(variantA)) {
	variantA = defaultVariantA;
}
if (variantA != null && variantA.equals(defaultVariantA)) {
	urlG.remove("variantA");
} else {
	urlG.set("variantA", variantA);
}

/* variant B */
String variantB = urlG.get("variantB");

String defaultVariantB = null;
it = varIds.iterator();
if (!it.hasNext()) {
	defaultVariantB = null;
}
else while (it.hasNext()) {
	defaultVariantB = (String) it.next();
	if (!defaultVariantB.equals(variantA))
		break;
}
if (defaultVariantB != null && defaultVariantB.equals(variantA))
	defaultVariantB = null;

if (variantB != null && !varIds.contains(variantB)) {
	variantB = null;
}
if (variantB == null || (variantB != null && variantB.equals(variantA) && scriptedRecServ == null)) {
	variantB = defaultVariantB;
}
if (variantB != null && variantB.equals(defaultVariantB)) {
	urlG.remove("variantB");
} else {
	urlG.set("variantB", variantB);
}

/* customer Email */
/* customer Use Logged In */
/* customer Id */
/* primary key */
/* cohort */
boolean useLoggedIn = false;
String defaultCustomerEmail = "";
String customerEmail = defaultCustomerEmail;
FDUserI sessionUser = (FDUserI) session.getAttribute("fd.user");
String customerId = null;
String primaryKey = null;
String cohortId = null;
FDUserI user = null;
String unknown = "<span class=\"not-found\">&lt;unknown&gt;</span>";

String useLoggedInStr = urlG.get("useLoggedIn");
if ("true".equalsIgnoreCase(useLoggedInStr)) {
	useLoggedIn = true;
}
if (useLoggedIn) {
	urlG.set("useLoggedIn", "true");
} else {
	urlG.remove("useLoggedIn");
}

TestSupport ts = TestSupport.getInstance();
if (useLoggedIn) {
	if (sessionUser != null) {
		user = sessionUser;
		primaryKey = sessionUser.getPrimaryKey();
		cohortId = CohortSelector.getInstance().getCohortName(primaryKey);
		if (sessionUser.getUserId() != null)
			customerEmail = sessionUser.getUserId();
		if (sessionUser.getIdentity() != null)
			customerId = sessionUser.getIdentity().getErpCustomerPK();
	}
	urlG.remove("customerEmail");
} else {
	customerEmail = urlG.get("customerEmail");
	if (customerEmail == null || customerEmail.length() == 0 || !EmailUtil.isValidEmailAddress(customerEmail)) {
		customerEmail = defaultCustomerEmail;
	}
	if (defaultCustomerEmail.equals(customerEmail)) {
		urlG.remove("customerEmail");
	} else {
		urlG.set("customerEmail", customerEmail);
		customerId = ts.getErpIDForUserID(customerEmail); 
		user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
		cohortId = CohortSelector.getInstance().getCohortName(primaryKey);
	}
}

String defaultCartAlgorithm = "allItems"; // "recentlyAdded"
String cartAlgorithm = urlG.get("cartAlgorithm");
if (cartAlgorithm == null || !("allItems".equals(cartAlgorithm)
		|| "recentlyAdded".equals(cartAlgorithm))) {
	cartAlgorithm = defaultCartAlgorithm;
}
if (defaultCartAlgorithm.equals(cartAlgorithm) || !useLoggedIn)
	urlG.remove("cartAlgorithm");

String userVariant = null;
if (assignment.containsKey(cohortId)) {
	userVariant = (String) assignment.get(cohortId);
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

/* session input */
SessionInput si = new SessionInput(user);
si.setCurrentNode(category);

String ymalError = "";

// gro_folgers_regular_04
String recentOrderlines = urlG.get("orderlines");
YmalSource source = null;
if (useLoggedIn && user != null) {
	if (("allItems").equals(cartAlgorithm)) {
	    FDStoreRecommender.initYmalSource(si, user);
	} else {
		source = YmalUtil.resolveYmalSource(user, null);
		if (YmalUtil.getSelectedCartLine(user) != null)
			si.setCurrentNode(YmalUtil.getSelectedCartLine(user).lookupProduct());
	}
} else if (recentOrderlines != null && !"".equals(recentOrderlines)) {
	List prods = new ArrayList();
	Set cartItems = new HashSet();
	StringTokenizer st = new StringTokenizer(recentOrderlines, ", \t\r\n");
	while (st.hasMoreElements()) {
		String cKey = (String) st.nextElement();
		cartItems.add(cKey);
	}
	// transform content keys to prods
	for (Iterator it2 = cartItems.iterator(); it2.hasNext(); ) {
		String node = (String) it2.next();
		try {
			Object o = ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, node));
			if (o != null)
				prods.add(o);
			else 
				ymalError += (ymalError.length() == 0 ? "" : "<br>") + "Unknown CMS node: " + node;
		} catch (ContentKey.InvalidContentKeyException e) {
			ymalError += (ymalError.length() == 0 ? "" : "<br>") + "Unknown CMS node: " + node;
		}
	}
	source = FDStoreRecommender.resolveYmalSource(prods);
	si.setCurrentNode(source);
}
si.setYmalSource(source);
si.setNoShuffle(true);
si.setMaxRecommendations(EnumSiteFeature.YMAL.equals(siteFeature) ? 6 : 5);

String scopeNodes = urlG.get("scope");
List scope = new ArrayList();
if (scopeNodes != null && scopeNodes.length() != 0) {
	Set scopeItems = new HashSet();
	StringTokenizer st = new StringTokenizer(scopeNodes, ", \t\r\n");
	while (st.hasMoreElements()) {
		String cKey = (String) st.nextElement();
		scopeItems.add(cKey);
	}
	// transform content keys to prods
	for (Iterator it2 = scopeItems.iterator(); it2.hasNext(); ) {
		String node = (String) it2.next();
		try {
			ContentNodeModel n = ContentFactory.getInstance().getContentNode(node);
			if (n != null && (n.getContentType().equals(ContentNodeModel.TYPE_DEPARTMENT) ||
					n.getContentType().equals(ContentNodeModel.TYPE_CATEGORY) ||
					n.getContentType().equals(ContentNodeModel.TYPE_PRODUCT) ||
					n.getContentType().equals(ContentNodeModel.TYPE_CONFIGURED_PRODUCT) ||
					n.getContentType().equals(ContentNodeModel.TYPE_FAVORITE_LIST)))
				scope.add(n);
		} catch (RuntimeException e) {
		}
	}
	si.setExplicitList(scope);
} else {
	urlG.remove("scope");
}



/* view */
String[] views = { "default", "detailed", "simple" };
String defaultView = views[0];
String view = urlG.get("view");
if (view == null || view.length() == 0) {
	view = defaultView;
} else {
	int i = 0;
	for (; i < views.length; i++) {
		if (view.equals(views[i]))
			break;
	}
	if (!view.equals(views[i]))
		view = defaultView;
}
if (defaultView.equals(view)) {
	urlG.remove("view");
} else {
	urlG.set("view", view);
}

StoreLookup qrLookup = "detailed".equals(view) ? FactorUtil.getDescretizedProduceRatingLookup1() : null;
StoreLookup nsLookup = "detailed".equals(view) ? FactorUtil.getNewnessLookup() : null;

/* redirect */
String newURL = urlG.build();

// debug
if (true) {
	System.err.println("orig URI: " + origURL);
	System.err.println("generated URI: " + newURL);
	System.err.println("site feature: " + siteFeature.getName());
	System.err.println("variant A: " + variantA);
	System.err.println("variant B: " + variantB);
	System.err.println("customer Email: " + customerEmail);
	System.err.println("customer Id: " + customerId);
	System.err.println("cohort Id: " + cohortId);
	System.err.println("user variant: " + userVariant);
	System.err.println("category Id: " + categoryId);
	System.err.println("category name: " + categoryName);
	System.err.println("view: " + view);
}

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

RecommendationService aRecService = (variantA != null) ? (RecommendationService) variants.get(variantA) : null;

RecommendationService bRecService = (variantB != null) ? (RecommendationService) variants.get(variantB) : null;

if (scriptedRecServ != null) {
    bRecService = scriptedRecServ;
    variantB = "User Provided Functions";
}

%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>VARIANT COMPARISON PAGE - <%= siteFeature.getName() %><%=
		variantA != null ? " - " + variantA : ""
	%><%=
		variantB != null ? " - " + variantB : ""
	%></title>
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">
body{margin:20px 60px;color:#333333;background-color:#fff;height:auto;}
input{font-weight:normal;}
p{margin:0px;padding:0px;}
.bull{color:#cccccc;}
a{color:#336600;}a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
.rec-chooser{margin:0px 0px 6px;}
.rec-options{border:1px solid black;font-weight:bold;padding: 5px 10px 10px;}
.rec-options .view-label{text-transform:capitalize;}
.rec-options table td p.label{padding-bottom:4px;}
.rec-options table td p.result{padding-top:4px;}
.rec-options table td{padding-right:20px;vertical-align:top;}
.var-comparator{margin-top:60px;margin-bottom: 40px;}
.var-comparator td{width:50%;text-align:center;vertical-align:top;}
.var-comparator .left{padding-right:20px;}
.var-comparator .right{padding-left:20px;}
.var-comparator .var-cmp-head{margin-bottom:20px;}
.var-comparator .var-cmp-head .title14 {margin-bottom:5px;}
.prod-items td{border:1px solid black;width:auto;padding:5px;}
.prod-items td.rank{border:0px none !important;vertical-align:middle;color:gray;}
.prod-items td.pic{width:100px;}
.prod-items td.info{text-align:left;vertical-align:top;}
.prod-items .taxonomy{font-style:italic;}
.prod-items td.info div{position:relative;height:80px;}
.prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;margin-right:25px;}
.prod-items .positive{color:#006600;}
.prod-items .negative{color:#990000;}
.prod-items .unknown{color:#FF9900;}
.not-found{color:red;}
.disabled{color:gray;font-style:italic;}
.selected{font-weight:bold;color:blue;}
	</style>
</head>
<body>
	<form method="get" action="<%= request.getRequestURI() %>" id="f1">

	<% if (!"simple".equals(view)) { %>
    <div class="rec-chooser title14">
    	<div style="float: left;">
    		<%  if (sessionUser != null) {
    				if (sessionUser.getIdentity() != null) {
    		%>
    		Logged in as <%= sessionUser.getUserId() %>.
    		<%      } else { %>
    		Not logged in. You are in area  <%= sessionUser.getZipCode() %>.
    		<% 		}
    			} else { %>
    		Not logged in.
    		<% 	} %>
    	</div>
    	<div style="float: right; text-align:right;">
	    	<span style="text-transform: uppercase;">Recommendation type:</span>&nbsp;<%
	    		it = siteFeatures.iterator();
	    		if (!it.hasNext()) {
			%><span class="disabled">No known site features.</span><%	    			
	    		} else {
	    	%>
			<select name="siteFeature" onchange="this.form.submit();">
			<%
		    		while (it.hasNext()) {
	    				EnumSiteFeature sf = (EnumSiteFeature) it.next();
	    	%>
	    		<option value="<%= sf.getName() %>"<%= 
	    				sf.equals(siteFeature) ?
	    				" selected=\"selected\"" : "" %>><%= sf.getTitle() %></option>
	    	<%
		    		}
			%>
	    	</select>
			<%
	    		}
	    	%>
	    </div>
	    <div style="clear: both;"></div>
    </div>
    <div class="rec-options" class="rec-chooser title14"<%= "simple".equals(view) ? " style=\"display: none;\"" : "" %>>
    	<table>
    		<tr>
    			<td class="text12" colspan="2">
    				<table style="width: auto;"><tr>
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
	    						onchange="document.getElementById('customerEmail').disabled = this.checked; if (document.getElementById('orderlines')) document.getElementById('orderlines').disabled = this.checked;
	    						if (document.getElementById('useLoggedCart')) document.getElementById('useLoggedCart').checked = this.checked;
	    						if (document.getElementById('div-cartAlg')) { document.getElementById('div-cartAlg').className = this.checked ? '' : 'disabled';
	    						document.getElementById('cartAlg-1').disabled = !this.checked;
	    						document.getElementById('cartAlg-2').disabled = !this.checked; }"
	    						value="true"<%= useLoggedIn ? " checked" : ""%>>
	    				</p>
	    				<p class="result">
	    					Customer ID: <%= customerId != null ? customerId : unknown %>
	    				</p>
	    				<p class="result">
	    					Cohort ID: <%= cohortId != null ? cohortId : unknown %>
	    				</p>
	    				<p class="result">
	    					User Variant: <%= userVariant != null ? userVariant : unknown %>
	    				</p>
	    				<p class="result">
	    					Service Type: <%= user != null ? user.getUserServiceType().getName() : unknown %>
	    				</p>
    				</td>
<% if (EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature)) { %>
    				<td>
	    				<p class="label">
	    					Category id
	    				</p>
	    				<p>
	    					<input type="text" name="categoryId" value="<%= categoryId %>"
	    							onfocus="this.select();">
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
<% } %>
<% if (!EnumSiteFeature.DYF.equals(siteFeature)
			&& !EnumSiteFeature.FAVORITES.equals(siteFeature)
			&& !EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature)) { %>
					<td>
	    				<p class="label">Recent Orderlines:</p>
						<p>
							<textarea name="orderlines" id="orderlines" rows="4" 
								cols="30"<%= useLoggedIn ? " disabled=\"disabled\"" : "" %>><%= urlG.get("orderlines") %></textarea>
						</p>
	    				<p class="label">
	    					or use logged user's cart: <input type="checkbox" name="useLoggedIn" id="useLoggedCart"
	    						onchange="document.getElementById('orderlines').disabled = this.checked; document.getElementById('customerEmail').disabled = this.checked;
	    						document.getElementById('useLoggedIn').checked = this.checked;
	    						document.getElementById('div-cartAlg').className = this.checked ? '' : 'disabled';
	    						document.getElementById('cartAlg-1').disabled = !this.checked;
	    						document.getElementById('cartAlg-2').disabled = !this.checked;"
	    						value="true"<%= useLoggedIn ? " checked" : ""%>>
	    				</p>
	    				<div id="div-cartAlg"<%= useLoggedIn ? "" : " class=\"disabled\"" %>>
	    				<p class="label">
	    					Cart Item Selection Algorithm:
	    				</p>
	    				<p class="label" style="font-weight: normal;">
	    					<input type="radio" name="cartAlgorithm" id="cartAlg-1"
	    							value="allItems"<%= useLoggedIn ? "" : " disabled"%>
	    							<%= "allItems".equals(cartAlgorithm) ? " checked" : "" %>>Select from all items in cart<br>
	    					<input type="radio" name="cartAlgorithm" id="cartAlg-2"
	    							value="recentlyAdded"<%= useLoggedIn ? "" : " disabled"%>
	    							<%= "recentlyAdded".equals(cartAlgorithm) ? " checked" : "" %>>Select from recently added items
	    				</p>
	    				</div>
	    				<% if (useLoggedIn && sessionUser != null) { %>
	    					<% List cartItems = sessionUser.getShoppingCart().getRecentOrderLines();
	    					   if (cartItems == null)
	    						   cartItems = Collections.EMPTY_LIST; 
							   it = cartItems.iterator(); %>
	    					<% if (!it.hasNext()) {	%>
	    				<p class="result not-found">
	    						No recent items in cart.
	    				</p>
	    					<% } else { %>
	    				<p class="result">
	    						Items in recent orderlines:
	    				</p>
	    					<% } %>
	    					<% while (it.hasNext()) {
	    						FDCartLineModel cartLine = (FDCartLineModel) it.next();
	    					%>
	    				<p style="font-weight: normal;" title="<%= cartLine.lookupProduct().getFullName() %>"
	    						><%= cartLine.getProductRef().getProductName() %></p>
	    					<% } %>
	    				<% } %>
	    				<p class="result">
	    					Triggering item: <% 
	    						if (source != null) { 
	    						%><span style="font-weight: normal;" title="<%= ((ContentNodeModel) source).getFullName() + " (" +
	    								source.getContentKey().getType().getName() + ")" %>"><%= source.getContentKey().getId() %></span><% 
	    						} else { 
	    						%><span class="not-found">&lt;unidentified&gt;</span><% 
	    						} %>
	    				</p>
	    				<%
	    				   if (ymalError.length() != 0) { %>
	    				<p class="not-found result">
	    					<%= ymalError %>
	    				</p>
	    				<% } %>
					</td>
<% } %>
    				<td>
	    				<p class="label">
	    					Generator function:
	    				</p>
						<p>
	    					<input type="text" name="generatorFunction" value="<%= StringEscapeUtils.escapeHtml(generatorFunction) %>"
	    							title="generator function">

						</p>
	    				<p class="label result">
	    					Scoring function:
	    				</p>
						<p>
	    					<input type="text" name="scoringFunction" value="<%= StringEscapeUtils.escapeHtml(scoringFunction) %>"
	    							title="scoring function">
						</p>
<%							if (compileErrorMessage!=null) { %>					
	    				<p class="not-found result">
	    					&lt;<%= compileErrorMessage %>&gt;
	    				</p>
<%						    } %>
	    				<p class="label result">Scope (for explicitList):</p>
						<p>
							<textarea name="scope" id="scope" rows="4"><%= urlG.get("scope") %></textarea>
						</p>
    					<% it = scope.iterator(); %>
    					<% if (!it.hasNext()) {	%>
	    				<p class="result not-found">
	    						No items in scope.
	    				</p>
    					<% } else { %>
	    				<p class="result">
	    						Items in scope:
	    				</p>
    					<% } %>
    					<% while (it.hasNext()) {
	    						ContentNodeModel node = (ContentNodeModel) it.next();
    					%>
	    				<p style="font-weight: normal;" title="<%= node.getFullName() + " (" + node.getContentKey().getType().getName() + ")" %>"
	    						><%= node.getContentName() %></p>
    					<% } %>
					</td>
    				</tr></table>
    			</td>
    		</tr>
			<tr>
				<td>
					<p class="result">
						<input type="submit" value="Submit">
					</p>
				</td>
    			<td style="text-align: right; vertical-align: bottom; padding-right: 0px;" class="title14">
    				<%
    					for (int i = 0; i < views.length; i++) {
    						if (i != 0) {
    				%>
    				<span class="bull">&bull;</span>
    				<%
    						}
    						if (views[i].equals(view)) {
    				%>
    						<span class="view-label"><%= views[i] %></span>
    				<%
    						} else {
    				%>
    						<span class="view-label"><a href="<%= urlG.set("view", views[i]).build() %>"><%= views[i] %></a></span>
    				<%
    						}
    					}
    				%>
    			</td>
			</tr>
    	</table>
    </div>
    <% } // end if "simple".equals...
    List recsA = null;
	if (aRecService != null) {
		System.err.println("variant A recommender: " + aRecService.getClass().getName());
		try {
			recsA = aRecService.recommendNodes(si);
			recsA = FDStoreRecommender.getInstance().filterProducts(recsA, null, true);
			System.err.println("Recommender A node count: " + recsA.size());
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
		}
	}
	List recsB = null;
	if (bRecService != null) {
		System.err.println("variant B recommender: " + bRecService.getClass().getName());
		try {
			recsB = bRecService.recommendNodes(si);
			recsB = FDStoreRecommender.getInstance().filterProducts(recsB, null, true);
			System.err.println("Recommender B node count: " + recsB.size());
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
		}	
	}	
    %>
	<table class="var-comparator">
		<tr>
			<td class="left">
				<div class="var-cmp-head">
				<% if (!"simple".equals(view)) { %>
					<div class="title14">Variant A</div>
					<select name="variantA" onchange="this.form.submit();">
					<%
						String optGroup = "";
						it = varIds.iterator();
						if (it.hasNext()) {
							String varId = (String) it.next();
							optGroup = assignment.containsValue(varId) ? "In Use" : "Not in Use";
					%>
					<optgroup label="<%= optGroup %>">
						<option <%= varId.equals(userVariant) ? "class=\"selected\" " : (assignment.containsValue(varId) ? "" : "class=\"disabled\" ") 
								%>value="<%= varId %>"<%= varId.equals(variantA) ? " selected=\"selected\"" : ""%>><%= varId %></option>
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
								%>value="<%= varId %>"<%= varId.equals(variantA) ? " selected=\"selected\"" : ""%>><%= varId %></option>
					<%
							if (!it.hasNext()) {
					%>
					</optgroup>
					<%
							}
						}
					%>
					</select>
					<% if (aRecService.getVariant().getServiceConfig().getType().equals(RecommendationServiceType.SCRIPTED) ) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= aRecService.getDescription() %></td></tr></table>
					<% } %>

					<p class="not-found">
					<% if (variantA != null && variantA.equals(userVariant)) { %>
						<b>This is the user's variant.</b>
					<% } else if (!assignment.containsValue(variantA)) { %>
						<i>This variant is not assigned to a cohort.</i>
					<% } else { %>
						&nbsp;
					<% } %>
					</p>
				<% } else { %>
					<span class="title24">A</span>
				<% }  %>
				</div>

			</td>
			<td class="right">
				<div class="var-cmp-head">
				<% if (!"simple".equals(view)) { %>
					<div class="title14"><% if (scriptedRecServ != null) { %>Custom Script<% } else { %>Variant B<% } %></div>
					<% if (scriptedRecServ != null) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= scriptedRecServ.getDescription() %></td></tr></table>
					<% } else { // if (scriptedRecServ!=null)  %>
					<select name="variantB" onchange="this.form.submit();">
					<%
						String optGroup = "";
						it = varIds.iterator();
						A: if (it.hasNext()) {
							String varId = (String) it.next();
							while (varId.equals(variantA)) {
								if (it.hasNext()) {
									varId = (String) it.next();
								} else {
									break A;
								}
							}
							optGroup = assignment.containsValue(varId) ? "In Use" : "Not in Use";
					%>
					<optgroup label="<%= optGroup %>">
						<option  <%= varId.equals(userVariant) ? "class=\"selected\" " : (assignment.containsValue(varId) ? "" : "class=\"disabled\" ") 
								%>value="<%= varId %>"<%= varId.equals(variantB) ? " selected=\"selected\"" : ""%>><%= varId %></option>
					<%
							if (!it.hasNext()) {
					%>
					</optgroup>
					<%
							}
						}
						while (it.hasNext()) {
							String varId = (String) it.next();
							if (varId.equals(variantA))
								continue;
							String newGroup = assignment.containsValue(varId) ? "In Use" : "Not in Use";
							if (!newGroup.equals(optGroup)) {
								optGroup = newGroup;
					%>
					</optgroup>
					<optgroup label="<%= optGroup %>">
					<%
							}
					%>
						<option  <%= varId.equals(userVariant) ? "class=\"selected\" " : (assignment.containsValue(varId) ? "" : "class=\"disabled\" ") 
								%>value="<%= varId %>"<%= varId.equals(variantB) ? " selected=\"selected\"" : ""%>><%= varId %></option>
					<%
							if (!it.hasNext()) {
					%>
					</optgroup>
					<%
							}
						}
					%>
					</select>
					<% if (bRecService != null && bRecService.getVariant().getServiceConfig().getType().equals(RecommendationServiceType.SCRIPTED) ) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= bRecService.getDescription() %></td></tr></table>
					<% } %>
					<p class="not-found">
					<% if (variantB != null && variantB.equals(userVariant)) { %>
						<b>This is the user's variant.</b>
					<% } else if (varIds.size() == 1 && varIds.get(0).equals(variantA) && scriptedRecServ == null) { %>
						<i>No more variants available.</i>
					<% } else if (!assignment.containsValue(variantB)) { %>
						<i>This variant is not assigned to a cohort.</i>
					<% } else { %>
						&nbsp;
					<% } %>
					</p>
					<%  } // if (scriptedRecServ)  %>
				<% } else { %>
					<span class="title24"><% if (scriptedRecServ != null) { %>Custom<% } else { %>B<% } %></span>
				<% }  %>
				</div>
			</td> 
		</tr>
		<tr>
			<td class="left">
				<%
					if (recsA != null) {
				%> 
				<table class="prod-items">
				<%
						it = recsA.iterator();
						int rank = 1;
						while (it.hasNext()) {
							ContentNodeModel cnm = (ContentNodeModel) it.next();
							ProductModel pm = (ProductModel) cnm;
							String actionURL = FDURLUtil.getProductURI(pm, "preview");
							boolean found = recsB != null && recsB.indexOf(cnm) >= 0;
							String notFound = "";
							if ("simple".equals(view)) {
								notFound = "";
							} else if (!found) {
								notFound = " style=\"background-color: #DFD;\"";								
							}
							String days = "";
							if ("detailed".equals(view)) {
								int d = (int) nsLookup.getVariable(cnm);
								if (d < -2000000000)
									days = "&lt;unknown&gt;";
								else
									days = -d + " day" + (-d > 1 ? "s" : "");
							}
				%>
					<tr>
						<% if ("detailed".equals(view)) { %>
						<td class="rank title24">
							<%= rank %>
						</td>
						<% } %>
						<td class="pic"<%= notFound %>>
							<display:ProductImage product="<%= pm %>" action="<%= actionURL %>"/>
						</td>
						<td class="info"<%= notFound %>><div>
								<span class="title16" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text13"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<% if ("detailed".equals(view)) { %>
								<div class="score text12">
									<span style="white-space: nowrap">Deals Percentage: <%= pm.getDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Quality Rating: <%= qrLookup.getVariable(cnm) %></span>&nbsp;
									<span style="white-space: nowrap">Product Age: <%= days %></span>
								</div>
								<% } %>
						</div></td>
					</tr> 
				<%
							rank++;
						} // end while
				%>
				</table>
				<%
					} // end if recs != null (display table)
				%>
		</td>
		<td class="right">
				<%
					if (recsB != null) {
				%>
				<table class="prod-items">
				<%
						it = recsB.iterator();
						int idx = 0;
						while (it.hasNext()) {
							ContentNodeModel cnm = (ContentNodeModel) it.next();
							ProductModel pm = (ProductModel) cnm;
							String actionURL = FDURLUtil.getProductURI(pm, "preview");
							Integer change = recsA != null && recsA.indexOf(cnm) >= 0 ? new Integer(recsA.indexOf(cnm) - idx) : null;
							String changeString = "N/A";
							String changeColor = "unknown";
							String notFound = "";
							if ("simple".equals(view)) {
								notFound = "";
							} else if (change != null) {
								int ch = change.intValue();
								if (ch > 0) {
									changeString = "+" + ch;
									changeColor = "positive";
								} else if (ch == 0) {
									changeString = change.toString();
									changeColor = "no_change";
								} else {
									changeString = change.toString();
									changeColor = "negative";
								}
							} else {
								notFound = " style=\"background-color: #DFD;\"";
							}
							String days = "";
							if ("detailed".equals(view)) {
								int d = (int) nsLookup.getVariable(cnm);
								if (d < -2000000000)
									days = "&lt;unknown&gt;";
								else
									days = -d + " day" + (-d > 1 ? "s" : "");
							}
				%>
					<tr<%= notFound %>>
						<td class="pic">
							<display:ProductImage product="<%= pm %>" action="<%= actionURL %>"/>
						</td>
						<td class="info"><div>
								<span class="title16" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text13"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<% if ("detailed".equals(view)) { %>
								<div class="score text12">
									<span style="white-space: nowrap">Deals Percentage: <%= pm.getDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Quality Rating: <%= qrLookup.getVariable(cnm) %></span>&nbsp;
									<span style="white-space: nowrap">Product Age: <%= days %></span>
								</div>
								<% } %>
								<% if (!"simple".equals(view)) { %>
								<div class="position text12 <%= changeColor %>"><%= changeString %></div>
								<% } %>
						</div></td>
					</tr> 
				<%
							idx++;
						} // end while
				%>
				</table>
				<%
					} // end if recs != null (display table)
				%>
			</td>
		</tr>
	</table>
	</form>
</body>
</html>
