<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<%@ page import="java.util.*"
%><%@ page import="com.freshdirect.fdstore.customer.FDIdentity"
%><%@ page import="com.freshdirect.test.TestSupport"
%><%@ page import="com.freshdirect.smartstore.fdstore.VariantSelection"
%><%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@ page import="com.freshdirect.webapp.taglib.fdstore.UserUtil"
%><%@ page import="com.freshdirect.fdstore.customer.FDUser"
%><%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"
%><%@ page import="com.freshdirect.smartstore.fdstore.*"
%><%@ page import="com.freshdirect.smartstore.RecommendationService"
%><%!

static class Buckets {
	// Map<String,Integer>
	Map b = new HashMap();
	int max_weight = 0;
	
	public Buckets(List keys) {
		for (Iterator it=keys.iterator(); it.hasNext();) {
			String key = (String)it.next();
			if (!b.keySet().contains(key)) {
				b.put(key, new Integer(0));
			}
		}
	}

	public boolean add(String key) {
		Integer w = (Integer)b.get(key);
		if (w == null) {
			b.put(key, new Integer(1));
		} else {
			b.put(key, new Integer(w.intValue()+1));
		}
		++max_weight;
		return true;
	}
	
	public int getWeight(String key) {
		if (b.keySet().contains(key)) {
			return ((Integer)b.get(key)).intValue();
		} else {
			return 0;
		}
	}
	
	public float getNormalizedWeight(String key) {
		if (b.keySet().contains(key)) {
			float k = ((Integer)b.get(key)).floatValue();
			return k / max_weight;
		} else {
			return 0f;
		}
	}
	
	public int getTotalWeight() {
		return max_weight;
	}
	
	
	public Iterator getKeysIterator() {
		return b.keySet().iterator();
	}
}

%><%

TestSupport s = TestSupport.getInstance();
VariantSelection helper = VariantSelection.getInstance();
VariantSelector selector = (VariantSelector)VariantSelectorFactory.getInstance(EnumSiteFeature.DYF);

// cohort->weight map
Map cohorts = helper.getCohorts();
// cohort->variant
Map vars = helper.getVariantMap(EnumSiteFeature.DYF);

// list of variant IDs
List variant_ids = helper.getVariants(EnumSiteFeature.DYF);

Buckets b1 = new Buckets(variant_ids);

%>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>untitled</title>
	<meta name="generator" content="TextMate http://macromates.com/">
	<meta name="author" content="Sebestyén Gábor">
	<!-- Date: 2008-06-05 -->
</head>
<body>
<%
/// List customerIDs = s.getDYFEligibleCustomerIDs();
List customerIDs = s.getCustomerIDs();
for (Iterator it=customerIDs.iterator(); it.hasNext();) {
	String cID = ((Long)it.next()).toString();
	

	RecommendationService rs = selector.select(cID);

	String key = rs.getVariant().getId();
	if (!b1.add(key)) {
		%>Invalid key: <%= key %> for customer <%= cID %><br/><%
	}
}
%>
	<h2></h2>
	<table style="border: 1px solid black">
		<tr>
			<th>&nbsp;</th>
<%
	for (Iterator it=b1.getKeysIterator(); it.hasNext();) {
		%>			<th><%= it.next() %></th>
<%
	}
%>
			<th>&nbsp;</th>
		</tr>
		<tr>
			<td>Measured:</td>
<%
	for (Iterator it=b1.getKeysIterator(); it.hasNext();) {
		String key = (String) it.next();
		int perc = Math.round(b1.getNormalizedWeight(key)*100);
		%>			<td style="text-align: center;"><%= perc %>%</td>
<%
	}
%>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
<%
	for (Iterator it=b1.getKeysIterator(); it.hasNext();) {
		String key = (String) it.next();
		%>			<td style="text-align: center;"><%= b1.getWeight(key) %></td>
<%
	}
%>
			<td>Total: <%= b1.getTotalWeight() %></td>
		</tr>
	</table>
</body>
</html>
