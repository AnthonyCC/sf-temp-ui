<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%!
String unknown = "<span class=\"not-found\">&lt;unknown&gt;</span>";
String n_a = "<span class=\"not-found\">N/A</span>";
String asc = "&#9650;";
String desc = "&#9660;";

String orderSign(String orderBy, PricingContext orderZone, boolean orderAscending, String nextOrderBy, PricingContext nextOrderZone) {
	if (orderBy.equals(nextOrderBy) && ("product".equals(nextOrderBy) || nextOrderZone.equals(orderZone))) {
		return " " + (orderAscending ? asc : desc); 
	} else {
		return "";
	}
}

List<ProductModel> sort(final String orderBy, final PricingContext orderZone, final boolean orderAscending, Collection<ProductModel> nodes) {
	final boolean byProduct = "product".equals(orderBy);
	final int byDeal = "price".equals(orderBy) ? 0 : ("deal".equals(orderBy) ? 1 : ("tiered".equals(orderBy) ? 2 : ("highest".equals(orderBy) ? 3 : -1)));
	final int sign = orderAscending ? +1 : -1;
	List<ProductModel> sorted = new ArrayList<ProductModel>(nodes.size());
	for (ProductModel node : nodes) {
		if (byProduct) {
			sorted.add(node);
		} else {
			sorted.add(ProductPricingFactory.getInstance().getPricingAdapter(node, orderZone));
		}
	}
	Collections.sort(sorted, new Comparator<ProductModel>() {
		public int compare(ProductModel p1, ProductModel p2) {
			if (byProduct) {
				return sign * p1.getContentKey().getId().compareTo(p2.getContentKey().getId());
			} else {
				int val = 0;
				switch (byDeal) {
					case 0: val = Double.compare(Math.max(0., p1.getPrice(0.)), Math.max(0., p2.getPrice(0.))); break;
					case 1: val = Math.max(0, p1.getDealPercentage()) - Math.max(0, p2.getDealPercentage()); break;
					case 2: val = Math.max(0, p1.getTieredDealPercentage()) - Math.max(0, p2.getTieredDealPercentage()); break;
					case 3: val = Math.max(0, p1.getHighestDealPercentage()) - Math.max(0, p2.getHighestDealPercentage()); break;
				}
				if (val == 0)
					return - sign * p1.getContentKey().getId().compareTo(p2.getContentKey().getId());
				
				return sign * val;
			}
		}
	});
	return sorted;
}

String escape(String text) {
	return StringEscapeUtils.escapeHtml(text);
}

Map<String,DataGenerator> generatorCache = new WeakHashMap<String,DataGenerator>();
Map<String,RecommendationService> recommenderCache = new WeakHashMap<String,RecommendationService>();

%>
<fd:CheckLoginStatus noRedirect="true" />
<%
	FDUserI sessionUser = (FDUserI) session.getAttribute("fd.user");
	FDUserI user = null;
	boolean useLoggedIn = false;
	if ("true".equalsIgnoreCase(request.getParameter("useLoggedIn"))) {
		user = sessionUser;
		useLoggedIn = true;
	} else if (request.getParameter("customerId") != null) {
		user = FDCustomerManager.getFDUser(new FDIdentity(request.getParameter("customerId")));
	} else if (request.getParameter("customerEmail") != null) {
		TestSupport ts = TestSupport.getInstance();
		try {
			user = FDCustomerManager.getFDUser(new FDIdentity(ts.getErpIDForUserID(request.getParameter("customerEmail"))));
		} catch (Exception e) {
		}
	}

	String customerId = user != null && user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : unknown;
	String customerEmail = user != null ? user.getUserId() : ""; 
	String primaryKey = user != null ? user.getPrimaryKey() : unknown;
	String cohortId = user != null ? CohortSelector.getInstance().getCohortName(primaryKey) : unknown;
	String zoneId = user != null && user.getPricingContext() != null ? user.getPricingContext().getZoneId() : unknown;
	String serviceType = user != null && user.getSelectedServiceType() != null ? user.getSelectedServiceType().name() : unknown;
	
	String selectors = request.getParameter("selectors");
	String generator = request.getParameter("generator");
	String scoring = request.getParameter("scoring");
	selectors = selectors != null ? selectors.trim() : "";
	generator = generator != null ? generator.trim() : "";
	scoring = scoring != null ? scoring.trim() : "";
	List<String> selectorErrors = new ArrayList<String>();
	Set<ProductModel> nodes = new HashSet<ProductModel>();
	String dataGeneratorError = null;
	String scoringError = null;
	List<ProductModel> sorted = null;
	String orderBy = null;
	if (selectors.length() != 0 && generator.length() == 0) {
		StringTokenizer tokenizer = new StringTokenizer(selectors, " \t\r\n,;");
		while (tokenizer.hasMoreTokens()) {
			String nodeId = tokenizer.nextToken();
			ContentNodeModel node = HelperFunctions.lookup(nodeId);
			if (node == null) {
				selectorErrors.add(nodeId);
			} else {
				List collection = HelperFunctions.recursiveNodes(node);
				nodes.addAll(collection);
			}
		}
	} else if (generator.length() != 0 && scoring.length() == 0) {
		try {
			DataGenerator dg = generatorCache.get(generator);
			if (dg == null) {
				dg = GlobalCompiler.getInstance().createDataGenerator(generator);
				generatorCache.put(generator, dg);
			}
			SessionInput input = new SessionInput(user);
			nodes = new HashSet(dg.generate(input, new PrioritizedDataAccess(Collections.emptyList())));
		} catch (CompileException e) {
			dataGeneratorError = "<span class=\"not-found\">Error in generator function!!!</span>";
			e.printStackTrace(System.err);
		}
	} else if (generator.length() != 0 && scoring.length() != 0) {
	    RecommendationService recommendationService = recommenderCache.get(generator + "@@@" + scoring);
		if (recommendationService == null) {
	    	recommendationService = RecommendationServiceFactory.configure(new Variant("scripted-test", EnumSiteFeature.SMART_CATEGORY,
	    			new RecommendationServiceConfig("scripted-test", RecommendationServiceType.SCRIPTED)
	    					.set("generator", generator).set("scoring", scoring)));
			if (recommendationService instanceof NullRecommendationService) {
	    		scoringError = "Failed to create script recommender.<br>Possible Script Compile Exception.";
	    		recommendationService = null;
	    	} else if (recommendationService != null) {
	    	   	recommenderCache.put(generator + "@@@" + scoring, recommendationService);
	    	}
		}
		if (recommendationService != null) {
			SessionInput input = new SessionInput(user);
			sorted = (List<ProductModel>) ((List) recommendationService.recommendNodes(input));
			orderBy = "scripted";
		}
	}
	

	boolean showAllZones = "true".equalsIgnoreCase(request.getParameter("showAllZones"));
	List<String> zoneIds;
	if (showAllZones || zoneId == null || zoneId.equals(unknown)) {
		zoneIds = new ArrayList<String>(FDZoneInfoManager.loadAllZoneInfoMaster());
	} else {
		zoneIds = new ArrayList<String>();
		ErpZoneMasterInfo zone = FDZoneInfoManager.findZoneInfoMaster(zoneId);
		while (zone != null) {
			zoneIds.add(zone.getSapId());
			zone = zone.getParentZone();
		}
	}
	Collections.sort(zoneIds);
	List<PricingContext> zones = new ArrayList<PricingContext>(zoneIds.size());
	for (String z : zoneIds) {
		zones.add(new PricingContext(z));
	}
	
	String order = request.getParameter("order");
	if (order == null)
		order = "product_asc";
	
	PricingContext orderZone = null;
	boolean orderAscending = true;
	if (orderBy == null) {
		String[] orderSplit = order.split("_");
		if (orderSplit.length == 2) {
			boolean tOrderAscending = !orderSplit[1].equals("desc");
			if ("product".equals(orderSplit[0])) {
				orderAscending = tOrderAscending;
				orderBy = "product";
			} else {
				orderSplit = orderSplit[0].split("\\$");
				if (orderSplit.length == 2) {
					if (zoneIds.contains(orderSplit[1])) {
						String tOrderZoneId = orderSplit[1];
						if ("price".equals(orderSplit[0]) || "deal".equals(orderSplit[0]) ||
								"tiered".equals(orderSplit[0]) || "highest".equals(orderSplit[0])) {
							orderBy = orderSplit[0];
							orderZone = new PricingContext(tOrderZoneId);
							orderAscending = tOrderAscending;
						} 
					}
				}
			}
		} 
		order = orderBy + ("product".equals(orderBy) ? "" : "$" + orderZone.getZoneId()) + "_" + (orderAscending ? "asc" : "desc");
		sorted = sort(orderBy, orderZone, orderAscending, nodes);
	}
	
	boolean showFullName = "true".equalsIgnoreCase(request.getParameter("showFullName"));
	boolean showDefaultSku = "true".equalsIgnoreCase(request.getParameter("showDefaultSku"));
	boolean showUnavailable = "true".equalsIgnoreCase(request.getParameter("showUnavailable"));
%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.freshdirect.smartstore.scoring.HelperFunctions"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.freshdirect.fdstore.content.ContentNodeModel"%>
<%@page import="java.util.Collection"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductModelPricingAdapter"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.fdstore.zone.FDZoneInfoManager"%>
<%@page import="java.util.Collections"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.freshdirect.customer.ErpZoneMasterInfo"%>
<%@page import="java.util.Map"%>
<%@page import="com.freshdirect.smartstore.scoring.DataGenerator"%>
<%@page import="java.util.WeakHashMap"%>
<%@page import="com.freshdirect.smartstore.impl.ScriptedRecommendationService"%>
<%@page import="com.freshdirect.smartstore.impl.GlobalCompiler"%>
<%@page import="com.freshdirect.smartstore.dsl.CompileException"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.scoring.PrioritizedDataAccess"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.smartstore.service.RecommendationServiceFactory"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.RecommendationService"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PRODUCT ZONE PRICING PAGE</title>
<%@ include file="../../shared/template/includes/style_sheet_detect.jspf"%>
<style type="text/css">
body {
	margin: 20px 60px;
	color: #333333;
	background-color: #fff;
	height: auto;
}

input {
	font-weight: normal;
}

p {
	margin: 0px;
	padding: 0px;
}

a {
	color: #336600;
}

a:VISITED {
	color: #336600;
}

table {
	border-collapse: collapse;
	border-spacing: 0px;
	width: 100%;
}

.rec-options {
	border: 1px solid black;
	font-weight: bold;
	padding: 5px 10px 10px;
}

.rec-options .view-label {
	text-transform: capitalize;
}

.rec-options table td p.label {
	padding-bottom: 4px;
}

.rec-options table td p.result {
	padding-top: 4px;
}

.rec-options table td {
	padding-right: 20px;
	vertical-align: top;
}

.var-comparator {
	margin-top: 60px;
	margin-bottom: 40px;
}

.error, .not-found {
	color: red;
}

.warning {
	color: #FF6633;
}

.comment {
	color: gray;
	font-weight: normal;
	font-style: italic;
}

.products {
	width: auto;
	margin: 0px;
	padding: 0px;
}

.products td,.products th {
	border: 1px solid black;
	padding: 4px;
}

.products td.b-left,.products th.b-left {
	border-left: 2px solid black;
}

.products td.b-right,.products th.b-right {
	border-right: 2px solid black;
}

.products td.b-top,.products th.b-top {
	border-top: 2px solid black;
}

.products td.b-bottom,.products th.b-bottom {
	border-bottom: 2px solid black;
}

.products th {
	font-weight: bold;
}

.right {
	text-align: right;
}

.center {
	text-align: center;
}

.user-zone {
	color: #C94747;
	background-color: #FFCC99;
}

.no-wrap {
	white-space: nowrap;
}
</style>
<script type="text/javascript">
function orderLink(orderBy, orderZone, orderAscending, nextOrderBy) {
	if (nextOrderBy === orderBy) {
		orderAscending = !orderAscending;
	} else {
		orderBy = nextOrderBy;
		orderAscending = "product" === orderBy;
	} 
	document.getElementById('order').value = orderBy + (orderBy === "product" ? "" : "$" + orderZone) + "_" + (orderAscending ? "asc" : "desc");
	document.getElementById('form').submit();
	return false;
}
</script>
</head>
<body>
<form method="get" action="<%= request.getRequestURI() %>" id="form">
<input type="hidden" name="order" id="order" value="<%=order%>">
<div class="rec-chooser title14">
<div>
<%
	if (sessionUser != null) {
		if (sessionUser.getIdentity() != null) {
%> Logged in as <%=sessionUser.getUserId()%>. <%
		} else {
%> Not logged in. You are in area <%=sessionUser.getZipCode()%>. <%
		}
	} else {
%> Not logged in. <%
	}
%>
</div>
</div>
<div class="rec-options" class="rec-chooser title14">
<table>
	<tr>
		<td class="text12" colspan="2">
		<table style="width: auto;">
			<tr>
				<td>
				<p class="label">Customer email</p>
				<p><input type="text" name="customerEmail" id="customerEmail" value="<%=customerEmail%>"
					onfocus="this.select();" <%=useLoggedIn ? " disabled=\"disabled\"" : "" %>></p>
				<p class="label">or use logged in <input type="checkbox" name="useLoggedIn" id="useLoggedIn"
						onchange="document.getElementById('customerEmail').disabled = this.checked" value="true"<%=useLoggedIn ? " checked" : ""%>></p>
				<p class="result">Customer ID: <%=customerId%></p>
				<p class="result">Zone ID: <%=zoneId%></p>
				<p class="result">Service Type: <%=serviceType%></p>
				<p class="result">Cohort ID: <%=cohortId%></p>
  				<% if (useLoggedIn && generator.length() != 0) { %>
  				<p class="result warning">
  					Note, that cart items will be<br>
  					excluded. To include cart items<br>
  					uncheck 'use logged in'.
  				</p>
  				<% } %>
				</td>
				<td>
    				<p class="label">Product selectors (comma separated)</p>
					<p class="label"><textarea name="selectors" id="selectors" rows="4" cols="30"<%=generator.length() != 0 ? " disabled" : ""%>><%=selectors%></textarea></p>
					<p class="result comment">Its functionality similar to of RecursiveNodes()<br>function</p>
    				<p class="label result">or specify a (scripted) generator function</p>
					<p class="label"><input type="text" name="generator" value="<%=escape(generator)%>"
							onkeyup="document.getElementById('selectors').disabled = this.value !== ''; return true;"></p>
					<% if (dataGeneratorError != null) { %>
					<p class="label"><%=dataGeneratorError%></p>
					<% } %>
    				<p class="label result">with (optional) scoring function</p>
					<p class="label"><input type="text" name="scoring" value="<%=escape(scoring)%>"></p>
					<% if (scoringError != null) { %>
					<p class="label"><%=scoringError%></p>
					<% } %>
				</td>
				<td>
					<p class="label">Miscellaneous options</p>
					<p class="label">Show full name <input type="checkbox" name="showFullName" value="true"<%=showFullName ? " checked" : ""%>></p>
					<p class="label">Show default SKU <input type="checkbox" name="showDefaultSku" value="true"<%=showDefaultSku ? " checked" : ""%>></p>
					<p class="label">Do not hide unavailable <input type="checkbox" name="showUnavailable" value="true"<%=showUnavailable ? " checked" : ""%>></p>
					<p class="label">Show all zones <input type="checkbox" name="showAllZones" value="true"<%=showAllZones ? " checked" : ""%>></p>
					<p class="result comment">By default showing only zones<br>relevant to the customer</p>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
			<p class="label result">
				<input type="submit" value="Submit">
			</p>
		</td>
	</tr>
</table>
</div>
<div class="var-comparator">
	<table class="products">
		<thead>
			<tr>
				<th class="text12 b-top b-left b-right b-bottom" rowspan="2">
					<a href="#" onclick="return orderLink('<%=orderBy%>',null,<%=orderAscending%>,'product');">
						Product<%=orderSign(orderBy, orderZone, orderAscending, "product", null)%>
					</a>
				</th>
				<% if (showFullName) { %>
				<th class="text12 b-top b-right b-bottom no-wrap" rowspan="2">
						Full Name
				</th>
				<% } %>
				<%
					for (PricingContext zone : zones) {
						boolean isUserZone = zone.getZoneId().equals(zoneId);
						ErpZoneMasterInfo zoneInfo = FDZoneInfoManager.findZoneInfoMaster(zone.getZoneId());
				%>
				<th class="text12 b-top b-right<%=isUserZone ? " user-zone" : ""%>" colspan="<%=showDefaultSku ? " 5" : "4" %>">
					<span title="<%=zoneInfo.getParentZone() != null ? "Parent: " + zoneInfo.getParentZone().getSapId(): "No parent"%>">Zone <%=zone.getZoneId()%></span>
				</th>
				<%
					}
				%>
			</tr>
			<tr>
				<%
					for (PricingContext zone : zones) {
						boolean isUserZone = zone.getZoneId().equals(zoneId);
				%>
				<th class="text12 center b-bottom<%=isUserZone ? " user-zone" : ""%>">
					<a href="#" onclick="return orderLink('<%=orderBy%>','<%=zone.getZoneId()%>',<%=orderAscending%>,'price');">
						Price<%=orderSign(orderBy, orderZone, orderAscending, "price", zone)%>
					</a>
				</th>
				<th class="text12 center b-bottom<%=isUserZone ? " user-zone" : ""%>">
					<a href="#" onclick="return orderLink('<%=orderBy%>','<%=zone.getZoneId()%>',<%=orderAscending%>,'deal');">
						Deal (%)<%=orderSign(orderBy, orderZone, orderAscending, "deal", zone)%>
					</a>
				</th>
				<th class="text12 center b-bottom<%=isUserZone ? " user-zone" : ""%>">
					<a href="#" onclick="return orderLink('<%=orderBy%>','<%=zone.getZoneId()%>',<%=orderAscending%>,'tiered');">
						Tiered (%)<%=orderSign(orderBy, orderZone, orderAscending, "tiered", zone)%>
					</a>
				</th>
				<th class="text12 center<%=!showDefaultSku ? " b-right" : "" %> b-bottom<%=isUserZone ? " user-zone" : ""%>">
					<a href="#" onclick="return orderLink('<%=orderBy%>','<%=zone.getZoneId()%>',<%=orderAscending%>,'highest');">
						Highest (%)<%=orderSign(orderBy, orderZone, orderAscending, "highest", zone)%>
					</a>
				</th>
				<% if (showDefaultSku) { %>
				<th class="text12 center b-right b-bottom<%=isUserZone ? " user-zone" : ""%>">
						SKU
				</th>
				<% } %>
				<%
					}
				%>
			</tr>
		</thead>
		<tbody>
			<%
			for (ProductModel product : sorted) {
				if (!showUnavailable && product.isUnavailable())
					continue;
			 %>
			<tr>
				<td class="text12 b-left b-right b-bottom"><%=product.getContentKey().getId()%></td>
				<% if (showFullName) { %>
				<td class="text12 b-right b-bottom no-wrap"><%=product.getFullName()%></td>
				<% } %>
				<%
					for (PricingContext zone : zones) {
						boolean isUserZone = zone.getZoneId().equals(zoneId);
						ProductModel pa = ProductPricingFactory.getInstance().getPricingAdapter(product, zone);
				%>
				<td class="text12 right b-bottom<%=isUserZone ? " user-zone" : ""%>"><%=pa.isUnavailable() ? n_a : pa.getPriceFormatted(0.)%></td>
				<td class="text12 right b-bottom<%=isUserZone ? " user-zone" : ""%>"><%=pa.isUnavailable() ? n_a : pa.getDealPercentage()%></td>
				<td class="text12 right b-bottom<%=isUserZone ? " user-zone" : ""%>"><%=pa.isUnavailable() ? n_a : pa.getTieredDealPercentage()%></td>
				<td class="text12 right<%=!showDefaultSku ? " b-right" : "" %> b-bottom<%=isUserZone ? " user-zone" : ""%>"><%=pa.isUnavailable() ? n_a : pa.getHighestDealPercentage()%></td>
				<% if (showDefaultSku) { %>
				<td class="text12 center b-right b-bottom<%=isUserZone ? " user-zone" : ""%>"><%=pa.getDefaultSku() != null ? pa.getDefaultSku().getSkuCode() : n_a%></td>
				<% } %>
				<%
					}
				%>
			</tr>
			<%
			}
			 %>
		</tbody>
	</table>
</div>
</form>
</body>
</html>