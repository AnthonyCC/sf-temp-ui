<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.freshdirect.fdstore.FDVariation"%>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.ContentType"%>
<%@page import="java.util.Set"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.cms.node.ContentNodeUtil"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.SkuModel"%>
<%@page import="java.util.List"%>
<%@page import="com.freshdirect.fdstore.content.Domain"%>
<%@page import="com.freshdirect.fdstore.content.DomainValue"%>
<%@page import="com.freshdirect.webapp.util.FDURLUtil"%>
<%@page import="java.util.HashSet"%><html>
<%@ taglib uri='freshdirect' prefix='fd'%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Revamp Local Dept Test Page - APPDEV-660</title>
</head>
<body>
<%
	CmsManager mgr = CmsManager.getInstance();
	final Set<ContentKey> keys = mgr.getContentKeysByType(ContentType.get("Sku"));

	Set<SkuModel> goodSkus = new HashSet<SkuModel>();
	for (ContentKey aSku : keys) {
		SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(aSku.getType(), aSku.getId());
		boolean hasMatrix = sku.getVariationMatrix().size() > 0;
		boolean hasLocalAttr = false;
		boolean hasOrgAttr = false;

		if (hasMatrix) {
			for (DomainValue dv : sku.getVariationMatrix()) {
				if ("source_Organic".equalsIgnoreCase(dv.getContentName())) {
					hasOrgAttr = true;
				} else if ("source_Local".equalsIgnoreCase(dv.getContentName())) {
					hasLocalAttr = true;
				}
			}
			
			boolean isAvailable = false;
			try {
				isAvailable = !sku.isUnavailable();
			} catch (NullPointerException exc) {
				System.err.println("SKU " + aSku + " is broken?");
			}
			
			if (isAvailable && (hasOrgAttr || hasLocalAttr)) {
				goodSkus.add(sku);
			}
		}
	}
%>
	<table>
		<tr>
			<th>SKU (<%= goodSkus.size() %>)</th>
			<th>Product</th>
		</tr>
<%
	for (SkuModel sku : goodSkus) {
%>		<tr>
			<td style="font-weight: bold;"><%= sku %></td>
			<td><a href="<%= FDURLUtil.getProductURI(sku.getProductModel(), "test") %>"><%= sku.getProductModel().getFullName() %></a></td>
		</tr>
<%				
	}
%>
	</table>
</body>
</html>