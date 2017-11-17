<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.fdstore.FDSkuNotFoundException"%>
<%@page import="com.freshdirect.fdstore.EnumOrderLineRating"%>
<%@page import="com.freshdirect.fdstore.EnumAvailabilityStatus"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.beans.XMLEncoder"%>

<%@page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@page import="com.freshdirect.fdstore.FDProduct"%>
<%@page import="com.freshdirect.fdstore.FDSku"%>
<%@page import="com.freshdirect.fdstore.FDCachedFactory"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.FDMaterialSalesArea"%>
<%@page import="com.freshdirect.fdstore.FDPlantMaterial"%>
<%@page import='com.freshdirect.cms.util.ProductInfoUtil'%>
<%@taglib uri="freshdirect" prefix="fd"%>
<%!String format(Object x) {
        if (x == null) {
            return "<b>Not found!</b>";
        }
        return "<code>" + x.toString().replaceAll("\n", "<br/>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;") + "</code>";
    }%>
<%
    StringBuilder message = new StringBuilder();
    FDProduct product = null;
    FDProductInfo productInfo = null;

    String skuCode = request.getParameter("sku");
    Integer version = request.getParameter("version") != null && request.getParameter("version").trim().length() > 0
            ? Integer.parseInt(request.getParameter("version").trim()) : null;

    if (skuCode != null) {
        skuCode = skuCode.trim();
        try {
            productInfo = FDCachedFactory.getProductInfo(skuCode);
        } catch (FDSkuNotFoundException e) {
            message.append("<ul><li>SKU not found :<i>" + skuCode + "</i></li></ul>");
        }
    }

    if ((skuCode != null) && (version == null) && (productInfo != null)) {
        version = productInfo.getVersion();
    }
    FDSku sku = skuCode != null && version != null ? new FDSku(skuCode, version) : null;
    String plantID = ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
    String pickingPlantId=ProductInfoUtil.getPickingPlantId(productInfo);
    String salesOrg = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getSalesOrg();
    String distrChannel = ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getDistributionChanel();
    if (sku != null) {
        try {
            product = FDCachedFactory.getProduct(sku);
        } catch (FDSkuNotFoundException e) {
            message.append("<ul><li>SKU not found :<i>" + sku.getSkuCode() + "/" + sku.getVersion() + "</i></li></ul>");
        }
    }
    if (productInfo != null && request.getParameter("setStatus") != null) {
        FDMaterialSalesArea sa = productInfo.getAvailability().get(new String(salesOrg + distrChannel).intern());
        EnumAvailabilityStatus e = EnumAvailabilityStatus.valueOf(request.getParameter("newStatus"));
        String newFreshness = request.getParameter("newFreshness");
        if (newFreshness != null) {
            if (newFreshness.trim().length() == 0) {
                newFreshness = null;
            }
        }
        EnumOrderLineRating r = null;
        String tmp = request.getParameter("newRating");
        if (tmp != null && tmp.trim().length() > 0) {
            r = EnumOrderLineRating.valueOf(tmp);
        }
        sa.setUnavailabilityStatus(e.getStatusCode());
        FDPlantMaterial pm = productInfo.getPlantMaterialInfo().get(pickingPlantId);
        if (pm != null) {
            pm.setFreshness(newFreshness);
            pm.setRating(r);
        }
        message.append(
                "<ol>New sku version injected into the system for <i>" + skuCode + "</i>, which <li>status: " + productInfo.getAvailabilityStatus(salesOrg, distrChannel)
                        + "</li><li>freshness:" + productInfo.getFreshness(pickingPlantId) + "</li>" + "</li><li>rating:" + productInfo.getRating(pickingPlantId) + "</li></ol>");
        version = productInfo.getVersion();
    }
    EnumAvailabilityStatus oldStatus = productInfo != null ? productInfo.getAvailabilityStatus(salesOrg, distrChannel) : null;
    EnumOrderLineRating oldRating = productInfo != null ? productInfo.getRating(pickingPlantId) : null;
    String oldFreshness = productInfo != null ? productInfo.getFreshness(pickingPlantId) : null;
%>
<html lang="en-US" xml:lang="en-US">
<head>
<fd:css href="/test/search/config.css" />
<title>Cache Content</title>
</head>
<body>
	<%
	    if (message.length() > 0) {
	%>
	<div class="error"><%=message%></div>
	<%
	    }
	%>
	<form method="get">
		<label for="sku">SKU code:</label> <input id="sku" type="text"
			name="sku" maxlength="50" value="<%=skuCode != null ? skuCode : ""%>" />
		<br /> <label for="version">Version:</label> <input id="version"
			type="text" name="version" maxlength="10"
			value="<%=version != null ? version.toString() : ""%>" /> <br /> <input
			type="submit" value="Check" /> <br />
	</form>
	<form method="post">
		<label for="sku">SKU code:</label> <input id="sku" type="text"
			name="sku" maxlength="50" value="<%=skuCode != null ? skuCode : ""%>" />
		<br /> <label for="newStatus">New status</label> <select
			id="newStatus" name="newStatus">
			<option value="" <%=oldStatus == null ? "selected" : ""%>>NA</option>
			<%
			    for (EnumAvailabilityStatus e : EnumAvailabilityStatus.values()) {
			%>
			<option value="<%=e.name()%>"
				<%=e.equals(oldStatus) ? "selected" : ""%>><%=e.getShortDescription()%></option>
			<%
			    }
			%>
		</select> <br /> <label for="newRating">New rating</label> <select
			id="newRating" name="newRating">
			<option value="" <%=oldRating == null ? "selected" : ""%>>NA</option>
			<%
			    for (EnumOrderLineRating e : EnumOrderLineRating.values()) {
			%>
			<option value="<%=e.name()%>"
				<%=e.equals(oldRating) ? "selected" : ""%>><%=e.getShortDescription()%>
				(<%=e.getStatusCode()%>)
			</option>
			<%
			    }
			%>
		</select> <br /> <label for="newFreshness">New freshness:</label> <input
			id="newFreshness" type="text" name="newFreshness" maxlength="50"
			value="<%=oldFreshness != null ? oldFreshness : ""%>" /> <input
			type="submit" name="setStatus" value="Change status!">
	</form>
	<table>
		<tr>
			<td class="even">Sku</td>
			<td class="even"><%=sku%></td>
		</tr>
		<tr>
			<td class="odd">FDProduct in VM</td>
			<td class="odd"><%=format(product)%></td>
		</tr>

		<tr>
			<td class="even">FDProductInfo in VM</td>
			<td class="even"><%=format(productInfo)%></td>
		</tr>

	</table>
</body>