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
<%@page import="java.util.HashMap"%>
<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@page import="com.freshdirect.fdstore.customer.FDCartLineI"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="com.freshdirect.cms.ContentKey.InvalidContentKeyException"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.content.BrandModel"%>
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
<%@page import="com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.StoreLookup"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.smartstore.service.RecommendationServiceFactory"%>
<%@page import="com.freshdirect.smartstore.service.VariantRegistry"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.dsl.CompileException"%>
<%@page import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.smartstore.scoring.ScoringAlgorithm"%>
<%@page import="com.freshdirect.smartstore.impl.ScriptedRecommendationService"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="com.freshdirect.fdstore.content.DepartmentModel"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.log4j.Logger"%>

<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<fd:CheckLoginStatus noRedirect="true" />

<%
Map<String,RecommendationService> customRecommenders = new WeakHashMap<String,RecommendationService>();
Iterator<String> it;
URLGenerator urlG = new URLGenerator(request);
String origURL = urlG.build();

/* site feature */
EnumSiteFeature defaultSiteFeature = EnumSiteFeature.DYF;
List<EnumSiteFeature> siteFeatures = EnumSiteFeature.getSmartStoreEnumList();
Collections.sort(siteFeatures);

EnumSiteFeature siteFeature = EnumSiteFeature.getEnum(urlG.get("siteFeature"));

if (siteFeature == null) {
	siteFeature = defaultSiteFeature;
} else if (defaultSiteFeature.equals(siteFeature)) {
	urlG.remove("siteFeature");
}

Map<String,Variant> variants = VariantRegistry.getInstance().getServices(siteFeature);
VariantSelection helper = VariantSelection.getInstance();
final Map<String,String> assignment = helper.getVariantMap(siteFeature);

List<String> varIds = SmartStoreUtil.getVariantNamesSortedInUse(siteFeature);

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

/* sampling function */
String samplingStrategy = urlG.get("samplingStrategy");
if (samplingStrategy != null) {
	samplingStrategy = samplingStrategy.trim();
}

if (!(scoringFunction != null && scoringFunction.length() > 0)) {
    urlG.remove("scoringFunction");
    scoringFunction = null;
}

RecommendationService scriptedRecServ = null;  
String compileErrorMessage = null;
if (generatorFunction != null && generatorFunction.length() > 0) {
	// clear cache
	customRecommenders.clear();
	
    scriptedRecServ = customRecommenders.get(generatorFunction + "@@@" + scoringFunction);
	if (scriptedRecServ == null) {
        //SmartStoreServiceConfiguration.configureSampler(new RecommendationServiceConfig("scripted-test", RecommendationServiceType.SCRIPTED))
    	scriptedRecServ = RecommendationServiceFactory.configure(new Variant("scripted-test", siteFeature, new RecommendationServiceConfig("scripted-test",
    	        RecommendationServiceType.SCRIPTED).set("generator", generatorFunction).set("scoring", scoringFunction).set("sampling_strat", samplingStrategy) ));
    	customRecommenders.put(generatorFunction + "@@@" + scoringFunction, scriptedRecServ);
		if (scriptedRecServ instanceof NullRecommendationService)
    		compileErrorMessage = "Failed to create script recommender. Possible Script Compile Exception.";
	}
} else {
    urlG.remove("generatorFunction");
    urlG.remove("scoringFunction");
    urlG.remove("samplingStrategy");
    scoringFunction = null;
    generatorFunction = null;
}



/* variant A */
String defaultVariantA;

it = varIds.iterator();
if (it.hasNext()) {
	defaultVariantA = it.next();
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
while (it.hasNext()) {
	defaultVariantB = it.next();
	if (!defaultVariantB.equals(variantA))
		break;
}
if (defaultVariantB != null && defaultVariantB.equals(variantA))
	defaultVariantB = null;

if (variantB != null && !varIds.contains(variantB)) {
	variantB = null;
}
if (variantB == null || (variantB.equals(variantA) && scriptedRecServ == null)) {
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
		cohortId = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
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
	userVariant = assignment.get(cohortId);
}
if (user != null) {
	try {
		List<String> ovariants = OverriddenVariantsHelper.getOverriddenVariantIds(user);
		OverriddenVariantsHelper.VariantInfoList vInfoList = OverriddenVariantsHelper.consolidateVariantsList(ovariants);
	
		OverriddenVariantsHelper.VariantInfo vi = vInfoList.get(siteFeature);

		if (vi != null) {
			userVariant = vi.variant;
		}	
	} catch (Exception e) {
	}
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
	if (!(category instanceof CategoryModel) && !(category instanceof DepartmentModel)) {
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
si.setTraceMode(true); // trace data sources for each content keys
si.setCurrentNode(category);

String ymalError = "";

// gro_folgers_regular_04
String recentOrderlines = urlG.get("orderlines");
YmalSource source = null;
if (useLoggedIn && user != null) {
	if (("allItems").equals(cartAlgorithm)) {
	    FDStoreRecommender.initYmalSource(si, user, request);
	    source = si.getYmalSource();
	    if (!EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature) 
	    		&& !siteFeature.getName().equals("PROD_GRP_POPULAR")
	    		&& !siteFeature.getName().equals("PROD_GRP_YF"))
	    	si.setCurrentNode(source);
	} else if ("recentlyAdded".equals(cartAlgorithm)) {
		source = YmalUtil.resolveYmalSource(user, request);
	    if (!EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature) 
	    		&& !siteFeature.getName().equals("PROD_GRP_POPULAR")
	    		&& !siteFeature.getName().equals("PROD_GRP_YF"))
			if (YmalUtil.getSelectedCartLine(user) != null)
				si.setCurrentNode(YmalUtil.getSelectedCartLine(user).lookupProduct());
	}
} else if (recentOrderlines != null && !"".equals(recentOrderlines)) {
	List<ProductModel> prods = new ArrayList<ProductModel>();
	Set<String> cartItems = new HashSet<String>();
	StringTokenizer st = new StringTokenizer(recentOrderlines, ", \t\r\n");
	while (st.hasMoreElements()) {
		String cKey = (String) st.nextElement();
		cartItems.add(cKey);
	}
	// transform content keys to prods
	for (Iterator<String> it2 = cartItems.iterator(); it2.hasNext(); ) {
		String node = it2.next();
		try {
			ProductModel o = (ProductModel)ContentFactory.getInstance().getContentNodeByKey(ContentKey.create(FDContentTypes.PRODUCT, node));
			if (o != null)
				prods.add(o);
			else 
				ymalError += (ymalError.length() == 0 ? "" : "<br>") + "Unknown CMS node: " + node;
		} catch (ContentKey.InvalidContentKeyException e) {
			ymalError += (ymalError.length() == 0 ? "" : "<br>") + "Unknown CMS node: " + node;
		}
	}
	source = FDStoreRecommender.resolveYmalSource(prods, request);
	si.setCurrentNode(source);
}
si.setYmalSource(source);
si.setNoShuffle(true);
si.setIncludeCartItems(!useLoggedIn);
si.setMaxRecommendations(EnumSiteFeature.YMAL.equals(siteFeature) ? 6 : 5);

String scopeNodes = urlG.get("scope");
List<ContentNodeModel> scope = new ArrayList<ContentNodeModel>();
if (scopeNodes != null && scopeNodes.length() != 0) {
	Set<String> scopeItems = new HashSet<String>();
	StringTokenizer st = new StringTokenizer(scopeNodes, ", \t\r\n");
	while (st.hasMoreElements()) {
		String cKey = (String) st.nextElement();
		scopeItems.add(cKey);
	}
	// transform content keys to prods
	for (Iterator<String> it2 = scopeItems.iterator(); it2.hasNext(); ) {
		String node = it2.next();
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

final Logger LOG = LoggerFactory.getInstance("compare_variants.jsp");

// debug
if (true) {
	LOG.info("orig URI: " + origURL);
	LOG.info("generated URI: " + newURL);
	LOG.info("site feature: " + siteFeature.getName());
	LOG.info("variant A: " + variantA);
	LOG.info("variant B: " + variantB);
	LOG.info("customer Email: " + customerEmail);
	LOG.info("customer Id: " + customerId);
	LOG.info("cohort Id: " + cohortId);
	LOG.info("user variant: " + userVariant);
	LOG.info("category Id: " + categoryId);
	LOG.info("category name: " + categoryName);
	LOG.info("view: " + view);
}

if (!origURL.equals(newURL)) {
	response.sendRedirect(StringEscapeUtils.unescapeHtml(newURL));	
}

RecommendationService aRecService = (variantA != null) ? variants.get(variantA).getRecommender() : null;

RecommendationService bRecService = (variantB != null) ? variants.get(variantB).getRecommender() : null;

if (scriptedRecServ != null) {
    bRecService = scriptedRecServ;
    variantB = "User Provided Functions";
}
ScoringAlgorithm scoringA = null;
ScoringAlgorithm scoringB = null;

if (aRecService instanceof ScriptedRecommendationService) {
    scoringA = ((ScriptedRecommendationService) aRecService).getScoring();
}

if (bRecService instanceof ScriptedRecommendationService) {
    scoringB = ((ScriptedRecommendationService) bRecService).getScoring();
}


%>
<html>
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
.prod-items .taxonomy{color: #777; font-weight: bold;}
.prod-items .source{color: #888; x-font-weight: bold;}
.prod-items td.info div{position:relative;height:80px;}
.prod-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;margin-right:25px;}
.prod-items .positive{color:#006600;}
.prod-items .negative{color:#990000;}
.prod-items .unknown{color:#FF9900;}
.not-found{color:red;}
.warning{color:#FF6633; !important}
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
	    		Iterator<EnumSiteFeature> it_sf = siteFeatures.iterator();
	    		if (!it_sf.hasNext()) {
			%><span class="disabled">No known site features.</span><%	    			
	    		} else {
	    	%>
			<select name="siteFeature" onchange="this.form.submit();">
			<%
		    		while (it_sf.hasNext()) {
	    				EnumSiteFeature sf = it_sf.next();
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
	    					Zone ID: <%= si.getPricingContext() != null ? si.getPricingContext().getZoneId() : unknown %>
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
	    				<% if (useLoggedIn) { %>
	    				<p class="result warning">
	    					Note, that cart items will be<br>
	    					excluded. To include cart items<br>
	    					uncheck 'use logged in'.
	    				</p>
	    				<% } %>
    				</td>
<% if (EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature) || EnumSiteFeature.BRAND_NAME_DEALS.equals(siteFeature) || siteFeature.getName().equals("PROD_GRP_POPULAR") || siteFeature.getName().equals("PROD_GRP_YF")) { %>
    				<td>
	    				<p class="label">
	    					Category/Department id<br/>(currentNode)
	    				</p>
	    				<p>
	    					<input type="text" name="categoryId" value="<%= categoryId %>"
	    							onfocus="this.select();">
	    				</p>
	    				<% if (categoryName != null) { %>
		    				<p class="result">
		    					Cat/Dept name: <%= categoryName %>
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
			&& !EnumSiteFeature.FEATURED_ITEMS.equals(siteFeature)
			&& !EnumSiteFeature.BRAND_NAME_DEALS.equals(siteFeature)
			&& !siteFeature.getName().equals("PROD_GRP_POPULAR")
			&& !siteFeature.getName().equals("PROD_GRP_YF")) { %>
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
	    					<% List<FDCartLineI> cartItems = sessionUser.getShoppingCart().getRecentOrderLines();
	    					   if (cartItems == null)
	    						   cartItems = Collections.emptyList(); 
							   Iterator<FDCartLineI> it_cl = cartItems.iterator(); %>
	    					<% if (!it_cl.hasNext()) {	%>
	    				<p class="result not-found">
	    						No recent items in cart.
	    				</p>
	    					<% } else { %>
	    				<p class="result">
	    						Items in recent orderlines:
	    				</p>
	    					<% } %>
	    					<% while (it_cl.hasNext()) {
	    						FDCartLineModel cartLine = (FDCartLineModel) it_cl.next();
	    					%>
	    				<p style="font-weight: normal;" title="<%= cartLine.lookupProduct().getFullName() %>"
	    						><%= cartLine.getProductRef().getContentKey().getId() %></p>
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
	    				<p class="result">
	    					Selected YMAL set: <% 
	    						if (source != null && source.getActiveYmalSet() != null ) { 
	    							%><span style="font-weight: normal;" title="<%= source.getActiveYmalSet().getFullName() + " (" +
	    							source.getActiveYmalSet().getContentKey().getType().getName() + ")" %>"><%= source.getActiveYmalSet().getContentKey().getId() %></span><% 
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
	    				<p class="label result">
	    					Sampling Strategy:
	    				</p>
						<p>
							<select name="samplingStrategy">
								<option value=""></option>
								<option value="deterministic" <% if ("deterministic".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>>deterministic</option>
								<option value="power" <% if ("power".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>>power</option>
								<option value="complicated" <% if ("complicated".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>>complicated</option>
								<option value="sqrt" <% if ("sqrt".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>>sqrt</option>
								<option value="cubic <% if ("cubic".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>">cubic</option>
								<option value="harmonic" <% if ("harmonic".equalsIgnoreCase(samplingStrategy)) {%> selected="selected"<%} %>>harmonic</option>
							</select>
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
    					<% Iterator<ContentNodeModel> it_cn = scope.iterator(); %>
    					<% if (!it_cn.hasNext()) {	%>
	    				<p class="result not-found">
	    						No items in scope.
	    				</p>
    					<% } else { %>
	    				<p class="result">
	    						Items in scope:
	    				</p>
    					<% } %>
    					<% while (it_cn.hasNext()) {
	    						ContentNodeModel node = it_cn.next();
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
    List<ContentNodeModel> recsA = null;
	if (aRecService != null) {
		LOG.info("variant A recommender: " + aRecService.getClass().getName());
		try {
			recsA = aRecService.recommendNodes(si);
			LOG.info("Recommender A node count: " + recsA.size());
		} catch (RuntimeException e) {
			LOG.error("exception when recommend for A", e);
		}
	}
	List<ContentNodeModel> recsB = null;
	SessionInput si2 = (SessionInput) si.clone();
	si2.setDataSourcesMap(new HashMap<ContentKey, Set<String>>());
	if (bRecService != null) {
		LOG.info("variant B recommender: " + bRecService.getClass().getName());
		try {
			recsB = bRecService.recommendNodes(si2);
			LOG.info("Recommender B node count: " + recsB.size());
		} catch (RuntimeException e) {
			LOG.error("exception when recommend for B", e);
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
							String varId = it.next();
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
							String varId = it.next();
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
					<% if (aRecService != null) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= aRecService.toString() %></td></tr></table>
					<% } %>

					
					<% if (variantA != null && variantA.equals(userVariant)) { %>
						<p class="not-found"><b>This is the user's variant.</b></p>
					<% } else if (!assignment.containsValue(variantA)) { %>
						<p class="not-found"><i>This variant is not assigned to a cohort.</i></p>
					<% } %>
				<% } else { %>
					<span class="title24">A</span>
				<% }  %>
					<label style="display: block"><input type="checkbox" name="variantAhideBursts" <%= urlG.get("variantAhideBursts") != null ? "checked=\"checked\"" : null %> onchange="this.form.submit();"/>&nbsp; Hide bursts</label>
				</div>
			</td>
			<td class="right">
				<div class="var-cmp-head">
				<% if (!"simple".equals(view)) { %>
					<div class="title14"><% if (scriptedRecServ != null) { %>Custom Script<% } else { %>Variant B<% } %></div>
					<% if (scriptedRecServ != null) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= scriptedRecServ.toString() %></td></tr></table>
					<% } else { // if (scriptedRecServ!=null)  %>
					<select name="variantB" onchange="this.form.submit();">
					<%
						String optGroup = "";
						it = varIds.iterator();
						A: if (it.hasNext()) {
							String varId = it.next();
							while (varId.equals(variantA)) {
								if (it.hasNext()) {
									varId = it.next();
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
							String varId = it.next();
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
					<% if (bRecService != null) {  %>
						<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;"><tr><td style="width: auto; padding: 4px;"><%= bRecService.toString() %></td></tr></table>
					<% } %>
					
					<% if (variantB != null && variantB.equals(userVariant)) { %>
						<p class="not-found"><b>This is the user's variant.</b></p>
					<% } else if (varIds.size() == 1 && varIds.get(0).equals(variantA) && scriptedRecServ == null) { %>
						<p class="not-found"><i>No more variants available.</i></p>
					<% } else if (!assignment.containsValue(variantB)) { %>
						<p class="not-found"><i>This variant is not assigned to a cohort.</i></p>
					<% } %>
					<%  } // if (scriptedRecServ)  %>
				<% } else { %>
					<span class="title24"><% if (scriptedRecServ != null) { %>Custom<% } else { %>B<% } %></span>
				<% }  %>
					<label style="display: block"><input type="checkbox" name="variantBhideBursts" <%= urlG.get("variantBhideBursts") != null ? "checked=\"checked\"" : null %> onchange="this.form.submit();"/>&nbsp; Hide bursts</label>
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
						Map<ContentKey,Set<String>> map = si.getDataSourcesMap();
						if (map == null)
							map = Collections.emptyMap();
						Iterator<ContentNodeModel> it_ra = recsA.iterator();
						int rank = 1;
						while (it_ra.hasNext()) {
							ContentNodeModel cnm = it_ra.next();
							ProductModel pm = (ProductModel) cnm;
							pm = ProductPricingFactory.getInstance().getPricingAdapter(pm, si.getPricingContext() != null ? si.getPricingContext() : PricingContext.DEFAULT);
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
								int d = (int) nsLookup.getVariable(cnm, si.getPricingContext());
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
							<display:ProductImage product="<%= pm %>" action="<%= actionURL %>" hideBursts="<%= urlG.get(\"variantAhideBursts\") != null ? aRecService.getVariant().getHideBursts() : null %>"/>
						</td>
						<td class="info"<%= notFound %>><div>
								<span class="title14" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text12"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<% if ("detailed".equals(view)) { %>
								<div class="score text12">
									<span style="white-space: nowrap">Brand<%= pm.getBrands().size() > 1 ? "s" : "" %>: <%
										String brands = "";
										Iterator<BrandModel> bit = pm.getBrands().iterator();
										if (bit.hasNext()) {
											BrandModel b = bit.next();
											brands += b.getFullName();
										}
										while (bit.hasNext()) {
											BrandModel b = bit.next();
											brands += ", " + b.getFullName();
										}
									%><%= brands %></span><br>
									<span style="white-space: nowrap">Promo Deal: <%= pm.getDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Tiered Deal: <%= pm.getTieredDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Quality Rating: <%= qrLookup.getVariable(cnm, si.getPricingContext()) %></span>&nbsp;
									<span style="white-space: nowrap">Product Age: <%= days %></span>
								</div>
								<% } %>
								<div class="source text12"><%= StringUtil.join(map.get(pm.getContentKey()), ", ") %></div>
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
						Map<ContentKey,Set<String>> map2 = si2.getDataSourcesMap();
						if (map2 == null)
							map2 = Collections.emptyMap();
						Iterator<ContentNodeModel> it_rb = recsB.iterator();
						int idx = 0;
						while (it_rb.hasNext()) {
							ContentNodeModel cnm = it_rb.next();
							ProductModel pm = (ProductModel) cnm;
							pm = ProductPricingFactory.getInstance().getPricingAdapter(pm, si2.getPricingContext() != null ? si2.getPricingContext() : PricingContext.DEFAULT);
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
								int d = (int) nsLookup.getVariable(cnm, si2.getPricingContext());
								if (d < -2000000000)
									days = "&lt;unknown&gt;";
								else
									days = -d + " day" + (-d > 1 ? "s" : "");
							}
				%>
					<tr<%= notFound %>>
						<td class="pic">
							<display:ProductImage product="<%= pm %>" action="<%= actionURL %>" hideBursts="<%= urlG.get(\"variantBhideBursts\") != null ? bRecService.getVariant().getHideBursts() : null  %>"/>
						</td>
						<td class="info"><div>
								<span class="title14" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text12"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<% if ("detailed".equals(view)) { %>
								<div class="score text12">
									<span style="white-space: nowrap">Brand<%= pm.getBrands().size() > 1 ? "s" : "" %>: <%
										String brands = "";
										Iterator<BrandModel> bit = pm.getBrands().iterator();
										if (bit.hasNext()) {
											BrandModel b = bit.next();
											brands += b.getFullName();
										}
										while (bit.hasNext()) {
											BrandModel b = bit.next();
											brands += ", " + b.getFullName();
										}
									%><%= brands %></span><br>
									<span style="white-space: nowrap">Promo Deal: <%= pm.getDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Tiered Deal: <%= pm.getTieredDealPercentage() %>%</span>&nbsp;
									<span style="white-space: nowrap">Quality Rating: <%= qrLookup.getVariable(cnm, si2.getPricingContext()) %></span>&nbsp;
									<span style="white-space: nowrap">Product Age: <%= days %></span>
								</div>
								<% } %>
								<div class="source text12"><%= StringUtil.join(map2.get(pm.getContentKey()), ", ") %></div>
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
