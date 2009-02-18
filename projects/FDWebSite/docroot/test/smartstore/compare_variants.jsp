<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.WeakHashMap"%>
<%@page import="java.util.Set"%>
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
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration"%>
<%@page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelection"%>
<%@page import="com.freshdirect.test.TestSupport"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceConfig"%>
<%@page import="com.freshdirect.smartstore.RecommendationServiceType"%>
<%@page import="com.freshdirect.smartstore.dsl.CompileException"%>
<%@page import="com.freshdirect.smartstore.impl.ScriptedRecommendationService"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%!
	Map customRecommenders = new WeakHashMap();
%>
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

Map variants = SmartStoreServiceConfiguration.getInstance().getServices(siteFeature);
VariantSelection helper = VariantSelection.getInstance();
final Map assignment = helper.getVariantMap(siteFeature);

List varIds = SmartStoreUtil.getVariantNamesSortedInUse(siteFeature);

/* generator function */
String generatorFunction = urlG.get("generatorFunction");
if (generatorFunction!=null) {
    generatorFunction = generatorFunction.trim();
}

/* scoring function */
String scoringFunction = urlG.get("scoringFunction");
if (scoringFunction!=null) {
    scoringFunction = scoringFunction.trim();
}

if (!(scoringFunction!=null && scoringFunction.length()>0)) {
    urlG.remove("scoringFunction");
    scoringFunction = null;
}

ScriptedRecommendationService scriptedRecServ = null;  
String compileErrorMessage = null;
if (generatorFunction != null && generatorFunction.length() > 0) {
    scriptedRecServ = (ScriptedRecommendationService) customRecommenders.get(generatorFunction + "@@@" + scoringFunction);
	if (scriptedRecServ == null) {
	    try {
	    	scriptedRecServ = new ScriptedRecommendationService(new Variant("scripted-test", siteFeature, new RecommendationServiceConfig("scripted-test", RecommendationServiceType.SCRIPTED)), generatorFunction, scoringFunction);
	    	customRecommenders.put(generatorFunction + "@@@" + scoringFunction, scriptedRecServ);
	    } catch (CompileException ce) {
	        ce.printStackTrace();
	        compileErrorMessage = ce.getMessage();
	    }
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
if (customerId != null) {
	FDUser user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
	cohortId = CohortSelector.getInstance().getCohortName(user.getPrimaryKey());
}

String defaultUserVariant = "&lt;unknown&gt;";
String userVariant = defaultUserVariant;
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
SessionInput si = new SessionInput(customerId);
si.setCartContents(Collections.EMPTY_SET);
si.setCurrentNode(category);
si.setNoShuffle(true);

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

/* redirect */
String newURL = urlG.build();

// debug
if (false) {
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

if (scriptedRecServ!=null) {
    aRecService = scriptedRecServ;
    variantA = "User Provided Functions";
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
body{margin:20px 60px;color:#333333;background-color:#fff;}
input{font-weight:normal;}
p{margin:0px;padding:0px;}
.bull{color:#cccccc;}
a{color:#336600;}a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
.rec-chooser{margin:0px 0px 6px;text-align:right;}
.rec-options{border:1px solid black;font-weight:bold;}
.rec-options .view-label{text-transform:capitalize;}
.rec-options table td{vertical-align: bottom; padding: 5px 10px 10px;}
.rec-options table td p.label{padding-bottom:4px;}
.rec-options table td p.result{padding-top:4px;}
.rec-options table div{padding-right:20px;}
.var-comparator{margin-top:60px;}
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
.prod-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.prod-items .positive{color:#006600;}
.prod-items .negative{color:#990000;}
.prod-items .unknown{color:#FF9900;}
.not-found{color:red;}
.disabled{color:gray;font-style:italic;}
.selected{font-weight:bold;color:blue;}
	</style>
</head>
<body>
	<form method="get" action="<%= request.getRequestURI() %>">
	<%= urlG.buildHiddenField("siteFeature") %>
	<%= urlG.buildHiddenField("view") %>
	<% if (!"simple".equals(view)) { %>
    <div class="rec-chooser title14">
    	<span style="text-transform: uppercase;">Recommendation type:</span>&nbsp;<%
    		it = siteFeatures.iterator();
    		if (it.hasNext()) {
    			EnumSiteFeature sf = (EnumSiteFeature) it.next();
    			if (!sf.equals(siteFeature)) {
    	%>
		<a href="<%= urlG.set("siteFeature", sf.getName()).build() %>"><%
    			}
    	%><span><%= sf.getTitle() %></span><%
				if (!sf.equals(siteFeature)) {
    	%></a><%
    			}
    		}
    		while (it.hasNext()) {
    			EnumSiteFeature sf = (EnumSiteFeature) it.next();
    	%>
    	<span class="bull">&bull;</span>
    	<%
    			if (!sf.equals(siteFeature)) {
    	%>
    	<a href="<%= urlG.set("siteFeature", sf.getName()).build() %>"><%
    			}
    	%><span><%= sf.getTitle() %></span><%
				if (!sf.equals(siteFeature)) {
    	%></a><%
    			}
    		}
    		// restore site feature
    		if (defaultSiteFeature.equals(siteFeature)) {
    			urlG.remove("siteFeature");
    		} else {
    			urlG.set("siteFeature", siteFeature.getName());
    		}
    	%>
    </div>
    <div class="rec-options" class="rec-chooser title14"<%= "simple".equals(view) ? " style=\"display: none;\"" : "" %>>
    	<table>
    		<tr>
    			<td class="text12">
    				<div style="float: left;">
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
    				</div>
    				<div style="float: left;">
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
    				</div>
    				<div style="float: left;">
	    				<p class="label">
	    					Generator function:
	    				</p>
						<p>
	    					<input type="text" name="generatorFunction" value="<%= StringEscapeUtils.escapeHtml(generatorFunction) %>"
									onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
	    							title="generator function">

						</p>
	    				<p class="label result">
	    					Scoring function:
	    				</p>
						<p>
	    					<input type="text" name="scoringFunction" value="<%= StringEscapeUtils.escapeHtml(scoringFunction) %>"
									onkeypress="if ((event.which || event.keyCode) == 13) this.form.submit();"
	    							title="scoring function">
						</p>
<%							if (compileErrorMessage!=null) { %>					
	    				<p class="not-found result">
	    					&lt;<%= compileErrorMessage %>&gt;
	    				</p>
<%						    } %>
					</div>
    				<div style="clear: both;"></div>
    			</td>
    			<td style="text-align: right;" class="title14">
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
		} catch (RuntimeException e) {
			e.printStackTrace(System.err);
		}
	}
	List recsB = null;
	if (bRecService != null) {
		System.err.println("variant B recommender: " + bRecService.getClass().getName());
		try {
			recsB = bRecService.recommendNodes(si);
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
					<% if (scriptedRecServ!=null) {  %>
						<%= scriptedRecServ.getDescription() %>
					<% } else { // if (scriptedRecServ!=null)  %>
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

					<p class="not-found">
					<% if (variantA != null && variantA.equals(userVariant)) { %>
						<b>This is the user's variant.</b>
					<% } else if (!assignment.containsValue(variantA)) { %>
						<i>This variant is not assigned to a cohort.</i>
					<% } else { %>
						&nbsp;
					<% } %>
					</p>
					<%  } // if (scriptedRecServ)  %>
				<% } else { %>
					<span class="title24">A</span>
				<% }  %>
				</div>

			</td>
			<td class="right">
				<div class="var-cmp-head">
				<% if (!"simple".equals(view)) { %>
					<div class="title14">Variant B</div>
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
					<p class="not-found">
					<% if (variantB != null && variantB.equals(userVariant)) { %>
						<b>This is the user's variant.</b>
					<% } else if (!assignment.containsValue(variantB)) { %>
						<i>This variant is not assigned to a cohort.</i>
					<% } else { %>
						&nbsp;
					<% } %>
					</p>
				<% } else { %>
					<span class="title24">B</span>
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
							boolean found = recsB != null && recsB.indexOf(cnm) >= 0;
							String notFound = "";
							if ("simple".equals(view)) {
								notFound = "";
							} else if (!found) {
								notFound = " style=\"background-color: #DFD;\"";								
							}
				%>
					<tr>
						<% if ("detailed".equals(view)) { %>
						<td class="rank title24">
							<%= rank %>
						</td>
						<% } %>
						<td class="pic"<%= notFound %>>
							<fd:ProductImage product="<%= pm %>" hideBurst="true" />
						</td>
						<td class="info"<%= notFound %>><div>
								<span class="title16" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text13"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<!-- <div class="score text12">Score: &lt;currently no data available&gt;</div> -->
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
				%>
					<tr<%= notFound %>>
						<td class="pic">
							<fd:ProductImage product="<%= pm %>" hideBurst="true" />
						</td>
						<td class="info"><div>
								<span class="title16" title="<%= cnm.getContentName() %>"><%= cnm.getFullName() %></span><br>
								<span class="taxonomy text13"><%= JspMethods.getTaxonomy(pm, true) %></span>
								<!-- <div class="score text12">Score: &lt;currently no data available&gt;</div> -->
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