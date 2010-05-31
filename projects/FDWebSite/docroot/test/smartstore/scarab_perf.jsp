<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<%@page
	import="com.freshdirect.cms.ContentKey.InvalidContentKeyException"%>
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
<%@page
	import="com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper"%>
<%@page
	import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.StoreLookup"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page
	import="com.freshdirect.smartstore.service.RecommendationServiceFactory"%>
<%@page import="com.freshdirect.smartstore.service.VariantRegistry"%>
<%@page import="com.freshdirect.fdstore.pricing.ProductPricingFactory"%>
<%@page import="com.freshdirect.common.pricing.PricingContext"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.dsl.CompileException"%>
<%@page
	import="com.freshdirect.smartstore.impl.NullRecommendationService"%>
<%@page import="com.freshdirect.smartstore.ymal.YmalUtil"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="com.freshdirect.smartstore.scoring.ScoringAlgorithm"%>
<%@page
	import="com.freshdirect.smartstore.impl.ScriptedRecommendationService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>

<%@ taglib uri="freshdirect" prefix="fd"%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display'%>

<fd:CheckLoginStatus />
<%!final Logger LOG = LoggerFactory.getInstance("compare_variants.jsp");

	Map<String, RecommendationService> customRecommenders = new WeakHashMap<String, RecommendationService>();%>
<%
	String defaultOption = "noop";
	String option = request.getParameter("option");
	if (option == null || option.trim().length() == 0)
		option = defaultOption;

	String generator = "";
	String scoring = "";
	RecommendationService recommender = null;
	String compileErrorMessage = null;
	if (option.equals("scripted")) {
		generator = request.getParameter("generator");
		if (generator != null)
			generator = generator.trim();
		scoring = request.getParameter("scoring");
		if (scoring != null)
			scoring = scoring.trim();

		recommender = customRecommenders.get(generator + "@@@"
				+ scoring);
		if (recommender == null) {
			try {
				recommender = RecommendationServiceFactory
						.configure(new Variant("scripted-test",
								EnumSiteFeature.SMART_CATEGORY,
								new RecommendationServiceConfig(
										"scripted-test",
										RecommendationServiceType.SCRIPTED)
										.set("generator", generator).set(
												"scoring", scoring)));
				if (recommender instanceof NullRecommendationService) {
					compileErrorMessage = "Failed to create script recommender. Possible Script Compile Exception.";
					recommender = null;
				} else {
					customRecommenders.put(generator + "@@@" + scoring,	recommender);
					
				}
			} catch (Exception e) {
				compileErrorMessage = "Error when creating script recommender. See logs for details.";
			}
		}
	}

	Variant variant = null;
	if (option.equals("variant")) {
		String variantId = request.getParameter("variant");
		if (variantId != null) {
			variant = VariantRegistry.getInstance().getService(variantId.trim());
		}
		if (variant != null)
			recommender = variant.getRecommender();
	}

	FDUserI sessionUser = (FDUserI) session.getAttribute("fd.user");
	SessionInput sessionInput = new SessionInput(sessionUser);
	
	{
		String text = request.getParameter("currentNode");
		if (text != null) {
			ContentNodeModel node = ContentFactory.getInstance().getContentNode(text.trim());
			if (node != null)
				sessionInput.setCurrentNode(node);
			if (node instanceof YmalSource)
				sessionInput.setYmalSource((YmalSource) node);
		}
	}
	{
		List<ContentNodeModel> explicitList = new ArrayList<ContentNodeModel>();
		String text = request.getParameter("explicitList");
		if (text != null) {
			String[] split = text.split("[ ,;]");
			for (String item : split) {
				ContentNodeModel node = ContentFactory.getInstance().getContentNode(item);
				if (node != null) {
					explicitList.add(node);
				}
			}
		}
		if (!explicitList.isEmpty())
			sessionInput.setExplicitList(explicitList);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCARAB PERFORMANCE TEST PAGE</title>
<%@ include file="/shared/template/includes/style_sheet_detect.jspf"%>
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

.bull {
	color: #cccccc;
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

td {
	padding: 0px;
}

.rec-chooser {
	margin: 0px 0px 6px;
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

table.options td {
	vertical-align: baseline;
}

.var-comparator {
	margin-top: 60px;
	margin-bottom: 40px;
}

.var-comparator td {
	text-align: center;
	vertical-align: top;
}

.var-comparator .var-cmp-head {
	margin-bottom: 20px;
}

.var-comparator .var-cmp-head .title14 {
	margin-bottom: 5px;
}

.prod-items {
	width: auto;
}

.prod-items td {
	border: 1px solid black;
	width: auto;
	padding: 5px;
}

.prod-items td.rank {
	border: 0px none !important;
	vertical-align: middle;
	color: gray;
}

.prod-items td.pic {
	width: 100px;
}

.prod-items td.info {
	text-align: left;
	vertical-align: top;
}

.prod-items .taxonomy {
	font-style: italic;
}

.prod-items td.info div {
	position: relative;
	height: 80px;
}

.prod-items .position {
	font-weight: bold;
	position: absolute !important;
	height: auto !important;
	bottom: 0px;
	right: 0px;
}

.prod-items .score {
	font-weight: bold;
	position: absolute !important;
	height: auto !important;
	bottom: 0px;
	left: 0px;
	margin-right: 25px;
}

.prod-items .positive {
	color: #006600;
}

.prod-items .negative {
	color: #990000;
}

.prod-items .unknown {
	color: #FF9900;
}

.not-found {
	color: red;
}

.warning {
	color: #FF6633;
	!
	important
}

.disabled {
	color: gray;
	font-style: italic;
}

.selected {
	font-weight: bold;
	color: blue;
}

.regular {
	font-weight: normal;
}
</style>
</head>
<body>
<form method="get" action="<%=request.getRequestURI()%>" id="f1">
<div class="rec-chooser title14">
<div style="float: left;">
<%
	if (sessionUser != null) {
		if (sessionUser.getIdentity() != null) {
%> Logged in as <%=sessionUser.getUserId()%>. <%
		} else {
%> Not logged in. You are in area <%=sessionUser.getZipCode()%>.<%
		}
	} else {
%> Not logged in. <%
	}
%>
</div>
<div style="clear: both;"></div>
</div>
<div class="rec-options" class="rec-chooser title14">
<table>
	<tr>
		<td class="text12" colspan="2">
		<table style="width: auto;">
			<tr>
				<td>
    				<p class="label">Current node (product or category)</p>
    				<p>
    					<input type="text" name="currentNode" value="<%= request.getParameter("currentNode") %>" onfocus="this.select();">
    				</p>
    				<p class="label result">Explicit List (separated by space,<br>comma, or semi-colon):</p>
					<p>
						<textarea name="explicitList" id="explicitList" rows="4"><%= request.getParameter("explicitList") %></textarea>
					</p>
				</td>
				<td>
					<table class="options">
						<tr>
							<td class="text12">
								<input type="radio" name="option" value="noop" <% if ("noop".equals(option)) { %>checked="checked"<% } %>
										onclick="document.getElementById('generator').disabled = this.checked;
												document.getElementById('scoring').disabled = this.checked;
												document.getElementById('variant').disabled = this.checked;">
							</td>
							<td class="text12">
								 No recommendations
							</td>
						</tr>
						<tr>
							<td class="text12">
								<input type="radio" name="option" value="scripted" <% if ("scripted".equals(option)) { %>checked="checked"<% } %>
										onclick="document.getElementById('generator').disabled = !this.checked;
												document.getElementById('scoring').disabled = !this.checked;
												document.getElementById('variant').disabled = this.checked;">
							</td>
							<td class="text12">
								 Scripted recommendations
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
			    				<p class="label result">Generator:</p>
								<p>
			    					<input type="text" name="generator" id="generator" value="<%= StringEscapeUtils.escapeHtml(generator) %>" <% if (!"scripted".equals(option)) { %>disabled="disabled"<% } %>>
								</p>
			    				<p class="label result">Scoring:</p>
								<p>
			    					<input type="text" name="scoring" id="scoring" value="<%= StringEscapeUtils.escapeHtml(scoring) %>" <% if (!"scripted".equals(option)) { %>disabled="disabled"<% } %>>
								</p>
								<% if (compileErrorMessage != null) { %>					
			    				<p class="not-found result">
			    					&lt;<%= compileErrorMessage %>&gt;
			    				</p>
								<% } %>
							</td>
						</tr>
						<tr>
							<td class="text12">
								<input type="radio" name="option" value="variant" <% if ("variant".equals(option)) { %>checked="checked"<% } %>
										onclick="document.getElementById('generator').disabled = this.checked;
												document.getElementById('scoring').disabled = this.checked;
												document.getElementById('variant').disabled = !this.checked;">
							</td>
							<td class="text12">
								 Variant
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
			    				<p class="label result">Variant:</p>
								<p>
			    					<input type="text" name="variant" id="variant" value="<%= request.getParameter("variant") %>" <% if (!"variant".equals(option)) { %>disabled="disabled"<% } %>>
								</p>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<p class="result"><input type="submit" value="Submit"></p>
		</td>
	</tr>
</table>
</div>
<%
	List<ContentNodeModel> recommendations = null;
	if (recommender != null) {
		LOG.info("recommender: " + recommender.getClass().getName());
		try {
			recommendations = recommender.recommendNodes(sessionInput);
			recommendations = recommendations.subList(0, Math.min(5, recommendations.size()));
			LOG.info("recommender item count: " + recommendations.size());
		} catch (RuntimeException e) {
			LOG.error("exception when calling recommender", e);
		}
	}
	if (recommendations != null) {
%>
<table class="var-comparator">
	<tr>
		<td>
			<div class="var-cmp-head">
				<div class="title14"><%= variant != null ? "Variant" : "Recommender" %> <% if (variant != null) { %>(<%= variant.getId() %>)<% } %></div>
				<table class="text11" style="border: 1px solid black; margin: 2px auto; width: auto;">
					<tr>
						<td style="width: auto; padding: 4px;"><%= recommender.toString() %></td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<table class="prod-items">
			<%
				{
					Iterator<ContentNodeModel> it = recommendations.iterator();
					int rank = 1;
					while (it.hasNext()) {
						ContentNodeModel cnm = it.next();
						ProductModel pm = (ProductModel) cnm;
						String actionURL = FDURLUtil.getProductURI(pm, "preview");
			%>
				<tr>
					<td class="rank title24">
						<%= rank %>
					</td>
					<td class="pic">
						<display:ProductImage product="<%= pm %>" action="<%= actionURL %>" />
					</td>
					<td class="info">
						<div>
							<span class="title16" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
							<span class="taxonomy text13"><%= JspMethods.getTaxonomy(pm, true) %></span>
						</div>
					</td>
				</tr> 
			<%
						rank++;
					} // end while
				}
			%>
			</table>
		</td>
	</tr>
</table>
<%
	}
%>
</form>
</body>
</html>
