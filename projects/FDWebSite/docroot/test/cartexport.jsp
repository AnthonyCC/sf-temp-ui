<%@ page contentType="application/vnd.ms-excel"
	import='com.freshdirect.fdstore.customer.*'
	import='com.freshdirect.webapp.taglib.fdstore.SessionName'
	import='com.freshdirect.customer.ejb.ErpOrderLineUtil' %><%
/*
String DOC_BEGIN = "<table width='100%' border='0' style='font-family: Tahoma, Verdana, sans-serif; font-size: 8pt;'>\n";
String DOC_END = "</table>";
String ROW_BEGIN = "<tr>";
String ROW_END = "</tr>";
String HEAD_PREFIX = "<th align='left'>";
String HEAD_SUFFIX = "</th>";
String PREFIX = "<td>";
String SUFFIX = "</td>";
*/
String DOC_BEGIN = "";
String DOC_END = "";
String ROW_BEGIN = "";
String ROW_END = "";
String HEAD_PREFIX = "";
String HEAD_SUFFIX = "\t";
String PREFIX = "";
String SUFFIX = "\t";

out.print(DOC_BEGIN);
out.print(ROW_BEGIN);
out.print(HEAD_PREFIX);
out.print("ORD");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("REQ");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("SECTION");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("SKU_CODE");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("QUANTITY");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("SALES_UNIT");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("CONFIGURATION");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("DESCRIPTION");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("CONFIGURATION_DESC");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("MATERIAL_ID");
out.print(HEAD_SUFFIX + HEAD_PREFIX);
out.print("PRICE");
out.print(HEAD_SUFFIX);
out.println(ROW_END);

FDCartModel cart = ((FDUserI) session.getAttribute(SessionName.USER)).getShoppingCart();
int count = 0;
for (Iterator i = cart.getOrderLines().iterator(); i.hasNext(); count++) {
	FDCartLineModel cartLine = (FDCartLineModel) i.next();
	out.print(ROW_BEGIN);
	out.print(PREFIX);
	out.print("");	// count+1
	out.print(SUFFIX + PREFIX);
	out.print("YES");
	out.print(SUFFIX + PREFIX);
	out.print("Main");
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getSkuCode());
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getQuantity());
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getSalesUnit());
	out.print(SUFFIX + PREFIX);
	out.print(ErpOrderLineUtil.convertHashMapToString(cartLine.getConfiguration().getOptions()));
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getDescription());
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getConfigurationDesc());
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.lookupFDProduct().getMaterial().getMaterialNumber());
	out.print(SUFFIX + PREFIX);
	out.print(cartLine.getUnitPrice());
	out.print(SUFFIX);
	out.println(ROW_END);
}
out.println(DOC_END);
%>