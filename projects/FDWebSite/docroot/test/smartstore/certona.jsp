<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.fdstore.util.CertonaTransitionUtil"%>
<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@page import="com.freshdirect.smartstore.fdstore.CohortSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelector"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.smartstore.external.certona.CertonaUserContextHolder"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@taglib uri='freshdirect' prefix='fd' %>
<%@taglib uri='fd-certona-tag' prefix='certona' %>
<%!
	static class Inf {
		public Variant variant;
		public String scheme;

		public Set<String> cohorts;
	}
%>
<fd:CheckLoginStatus id="user" noRedirect="<%= true %>" guestAllowed="<%= true %>" />
<certona:resonanceJSObject action="init"/>
<html>
<head>
	<meta charset="utf-8">
	<title>Certona Test Page</title>
	
	<style>
	</style>
</head>
<body>
	<table class="infobox">
		<tr>
			<td>Customer</td>
			<td><% if (user == null) { %>--<% } else { %><%= user.getUserId() %><% } %></td>
		</tr>
		<tr>
			<td>Cohort</td>
			<td><% if (user == null && user.getFDCustomer() != null) { %>--<% } else { %><%= CohortSelector.getInstance().getCohortName( user.getFDCustomer().getErpCustomerPK() ) %><% } %></td>
		</tr>
		<tr>
			<td>Tracking ID</td>
			<td><%= CertonaUserContextHolder.getTrackingId() %></td>
		</tr>
		<tr>
			<td>Session ID</td>
			<td><%= CertonaUserContextHolder.getSessionId() %></td>
		</tr>
	</table>
	
	<h2>Certona backed variants</h2>	
	<table class="info">
		<tr>
			<th>Site Feature</th>
			<th>Variant ID</th>
			<th>Certona Scheme</th>
			<th>Assigned Cohorts</th>
		</tr>
	<% for (EnumSiteFeature sf : EnumSiteFeature.getEnumList()) {
		final VariantSelector vs = VariantSelectorFactory.getSelector(sf);

		Map<String, Inf> valami = new HashMap<String, Inf>();
		
		for (final String cohortName : CohortSelector.getCohortNames()) {
			CertonaTransitionUtil.CertonaInfo ci = CertonaTransitionUtil.getCertonaInfo(sf, cohortName);
			if (ci != null) {
				final String key = ci.variant.getId() + "#" + ci.certonaScheme;

				if (!valami.keySet().contains(key)) {
					Inf inf = new Inf();
					valami.put(key, inf);
					inf.variant = ci.variant;
					inf.scheme = ci.certonaScheme;
					inf.cohorts = new HashSet<String>();
				}
				valami.get(key).cohorts.add(ci.cohortName);
			}
		}
		
		if (valami.keySet().size() > 0) {
			for (String key : valami.keySet()) {
				Inf inf = valami.get(key);
%>
				<tr>
					<td><%= inf.variant.getSiteFeature().getName() %></td>
					<td><%= inf.variant.getId() %></td>
					<td><%= inf.scheme %></td>
					<td><%= StringUtil.join(inf.cohorts, ";") %></td>
				</tr>
				<%
			}
		}		
	%>
	<% } %></table>
	
	<!--  script src="//edge1.certona.net/cd/4234f569/freshdirect.com/scripts/resonance.js"></script -->
	<!--  certona:resonanceJSObject action="create"/ -->
</body>
</html>
