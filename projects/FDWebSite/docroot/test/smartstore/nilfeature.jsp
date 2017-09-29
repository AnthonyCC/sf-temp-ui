<!DOCTYPE html>
<%@page contentType="text/html; charset=UTF-8" %>
<%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@page import="com.freshdirect.smartstore.fdstore.Recommendations"%>
<%@page import="com.freshdirect.smartstore.SessionInput"%>
<%@page import="com.freshdirect.smartstore.fdstore.FDStoreRecommender"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelectorFactory"%>
<%@page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@page import="com.freshdirect.smartstore.Variant"%>
<%@page import="com.freshdirect.smartstore.fdstore.VariantSelector"%>
<html lang="en-US" xml:lang="en-US">
<head>
	<meta charset="utf-8">
	<title>Nil Site Feature test page</title>
</head>
<body>
<%
	EnumSiteFeature feature = EnumSiteFeature.getEnum("NIL");
%>
	<div><span>'NIL'</span> feature: <%= feature %></div>
<%
	EnumSiteFeature feature2 = EnumSiteFeature.getEnum("1234");
%>
	<div><span>'1234'</span> feature: <%= feature2 %></div>
<%
	VariantSelector vs = VariantSelectorFactory.getSelector(feature);
%>
	<div>Variant Selector Factory('NIL') => <%= vs %></div>
<%
	Variant v = vs.getVariant("C1");
%>
	<div>Variant: <%= v != null ? v : "null" %></div>
<%
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

	Recommendations rec = FDStoreRecommender.getInstance().getRecommendations(feature, user, new SessionInput(user));
%>
	<div>Recommendations: <%= rec %></div>
</body>
</html>