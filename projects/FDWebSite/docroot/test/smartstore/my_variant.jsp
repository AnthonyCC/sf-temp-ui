<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="java.util.*"
%><%@ page import="com.freshdirect.fdstore.customer.FDIdentity"
%><%@ page import="com.freshdirect.test.TestSupport"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"
%><%@ page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@ page import="com.freshdirect.smartstore.fdstore.VariantSelector"
%><%@ page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"
%><%@ page import="com.freshdirect.smartstore.RecommendationService"
%><%@ page import="com.freshdirect.smartstore.fdstore.SmartStoreUtil"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ taglib uri="freshdirect" prefix="fd"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%@page import="com.freshdirect.smartstore.fdstore.OverriddenVariantsHelper"
%><%@page import="com.freshdirect.webapp.util.DYFUtil"
%><%@page import="com.freshdirect.mail.EmailUtil"
%><%@page import="org.apache.commons.lang.math.NumberUtils"%>
<%@page import="com.freshdirect.fdstore.FDException"%><html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>VARIANT LOOKUP</title>
	<meta name="author" content="Sebestyén Gábor, Somogyi Csongor">
	<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<style type="text/css">
body{margin:20px 60px;color:#333333;background-color:#fff;}
input{font-weight:normal;}
p{margin:0px;padding:0px;}
.bull{color:#cccccc;}
a{color:#336600;}a:VISITED{color:#336600;}
table{border-collapse:collapse;border-spacing:0px;width:100%;}
.rec-chooser{margin:0px 0px 6px;text-align:right;}
.rec-options{margin-bottom:40px;border:1px solid black;}
.rec-options .view-label{text-transform:capitalize;}
.rec-options table{width:auto;}
.rec-options table td{vertical-align: top; padding: 5px 10px 10px;}
.rec-options table td p.label{padding-bottom:4px;font-weight:bold;}
.rec-options table td p.result{padding-top:4px;}
.rec-options table div{padding-right:20px;}
.user-items {border:1px solid black;width:auto;}
.user-items td{border:1px solid black;width:auto;padding:4px;white-space:nowrap;}
.user-items td.rank{border:0px none !important;vertical-align:middle;color:gray;}
.user-items td.pic{width:100px;}
.user-items td.info{text-align:left;vertical-align:top;}
.user-items .taxonomy{font-style:italic;}
.user-items td.info div{position:relative;height:80px;}
.user-items .position{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;right:0px;}
.user-items .score{font-weight:bold;position:absolute !important;height:auto !important;bottom:0px;left:0px;}
.user-items .positive{color:#006600;}
.user-items .negative{color:#990000;}
.user-items .unknown{color:#FF9900;}
.not-found{color:red;}
.original{color:gray;font-style:italic;}
.overridden{font-weight:bold;}
	</style>
</head>
<body>
<%

/* customers */
String customers = request.getParameter("customers");
if (customers == null ) {
	customers = "";
}

/* Process customers */
String[] custStrArr = customers.split("[, \r\n]");
Map custMap = new HashMap();
TestSupport ts = TestSupport.getInstance();
int validUserCount = 0;
int invalidUserCount = 0;
for (int i = 0; i < custStrArr.length; i++) {
	String customerId = custStrArr[i].trim();
	if (customerId.length() == 0)
		continue;
	FDUserI user = null;
	if (EmailUtil.isValidEmailAddress(customerId)) {
		String erpId = ts.getErpIDForUserID(customerId);
		try {
			user = FDCustomerManager.getFDUser(new FDIdentity(erpId));
		} catch (FDException e) {
			
		}
	} else if (NumberUtils.isNumber(customerId)) {
		try {
			user = FDCustomerManager.getFDUser(new FDIdentity(customerId));
		} catch (FDException e) {
			
		}
	}

	custMap.put(customerId, user);
	if (user != null)
		validUserCount++;
	else
		invalidUserCount++;
}

final String LOGGED_IN = "&lt;logged in user&gt;";

if (validUserCount == 0) {
	%><fd:CheckLoginStatus/><%
	custMap.clear();
	custMap.put(LOGGED_IN, session.getAttribute("fd.user"));
}

%>
	<form method="get" action="<%=request.getRequestURI()%>">
    <div class="rec-options" class="rec-chooser title14">
    	<table>
    		<tr>
    			<td class="text12">
    				<p class="label">
    					Customer emails and/or ids
    				</p>
    				<p>
    					<textarea name="customers" cols="30" rows="5"
    							onfocus="this.select();"
    							title="Specify email addresses and/or customer ids comma separated and press &lt;Enter&gt; to activate them"
    					><%= customers %></textarea>
    				</p>
    				<p class="result right">
    					<input type="submit" name="submit" value="Submit">
    				</p>
    			</td>
    			<td class="text12">
    				<p class="label">
    					<span title="The list of the results of the lookup">Status</span>
    				</p>
    				<p>
    				<%
    					if (validUserCount == 0) {
    				%>
    					<span class="not-found">No valid user has been identified. Showing logged in user.</span>
    				<%
    					} else { 
		    		%>
   		    			<span><%= validUserCount %> user(s) has been identified.</span>
   		    		<%
    					}
   		    			if (invalidUserCount != 0) {
   		    		%>
   		    		</p>
   		    		<p class="result">
   		    		<%
    					}
   		    		
    					Iterator it2 = custMap.keySet().iterator();
	    				if (invalidUserCount != 0 && custMap.keySet().size() > 1) {
		    		%>
		    			<span>Unidentified users:</span>
					</p>
					<p class="result">
		    		<%
		    				while (it2.hasNext()) {
		    					String name = (String) it2.next();
		    					FDUserI user = (FDUserI) custMap.get(name);
		    					if (LOGGED_IN.equals(name))
		    						continue;
		    					if (user != null)
		    						continue;
    				%>
    					<span><%= name %> - </span><span class="not-found">not recognized</span>
    				<%
    							if (it2.hasNext()) {
    				%> 
    					<br>
    				<%
		    					}
	    					}
	    				}
    				%>
    				</p>
    			</td>
    		</tr>
    	</table>
    </div>
	</form>
	<table class="user-items">
		<tr>
			<td class="text12bold left">Email</td>
			<td class="text12bold left">Cohort</td>
			<td class="text12bold left">Customer ID</td>
			<td class="text12bold left">YF Eligible</td>
			<%
				for (Iterator it = EnumSiteFeature.getSmartStoreEnumList().iterator(); it.hasNext();) {
					EnumSiteFeature feature = (EnumSiteFeature) it.next();
			%>
			<td class="text12bold left"><%= feature.getTitle() %></td>
			<%
				}
			%>
		</tr>
		<%
			it2 = custMap.keySet().iterator();
			while (it2.hasNext()) {
				String name = (String) it2.next();
				FDUserI user = (FDUserI) custMap.get(name);
				
				if (user == null)
					continue;
				
				OverriddenVariantsHelper ovHelper = new OverriddenVariantsHelper(user);
		
				List ovariants = ovHelper.getOverriddenVariants();
				OverriddenVariantsHelper.VariantInfoList vInfoList = ovHelper.consolidateVariantsList(ovariants);
		%>
		<tr>
			<td class="text12"><%= user.getUserId() != null ? user.getUserId() : "&lt;anonymous user&gt;" %></td>
			<td class="text12 right"><%= user.getCohortName() %></td>
			<td class="text12 right"><%= user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : "&lt;anonymous user&gt;" %></td>
			<td class="text12 right"><%= DYFUtil.isCustomerEligible(user) ? "yes" : "no" %></td>
			<%
				for (Iterator it = EnumSiteFeature.getSmartStoreEnumList().iterator(); it.hasNext();) {
					EnumSiteFeature feature = (EnumSiteFeature) it.next();
					
					OverriddenVariantsHelper.VariantInfo vi = vInfoList.get(feature);
					String origVariant = VariantSelectorFactory.getInstance(feature).getService(user.getCohortName()).getVariant().getId();
					String overridden = origVariant;
					if (vi != null) {
						overridden = "<span class=\"overridden\">" + vi.variant + "</span> <span class=\"original\">(" + origVariant + ")</span>";
					}
			%>
			<td class="text12 left"><%= overridden %></td>
			<%
				}
			%>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>
