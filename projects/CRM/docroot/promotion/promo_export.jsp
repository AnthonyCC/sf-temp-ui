<%--

	PROMO EXPORT
	[APPDEV-1091]
	
	@author segabor

--%><%@ page import="java.util.List"
%><%@ page import="java.util.ArrayList"
%><%@ page import="com.freshdirect.crm.CrmAgentModel"
%><%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewModel"
%><%@ page import="com.freshdirect.fdstore.promotion.FDPromotionNewModelFactory"
%><%@ page import="com.freshdirect.fdstore.promotion.pxp.PromoPublisher"
%><%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"
%><%@ page import="com.freshdirect.fdstore.util.json.FDPromotionJSONSerializer"
%><%@ page import="com.metaparadigm.jsonrpc.JSONSerializer"
%><%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");

	String action = request.getParameter("action");

	try {
		if ("export".equalsIgnoreCase(action)) {
			String promoCode = request.getParameter("promoCode");

			JSONSerializer ser = new JSONSerializer();

			ser.registerDefaultSerializers();
			ser.registerSerializer(FDPromotionJSONSerializer.getInstance());

			response.setContentType("text/json; charset=\"UTF-8\"");
			response.setHeader("Content-Disposition", "attachment; filename=" + promoCode + ".json");
			
			FDPromotionNewModel promo = FDPromotionNewModelFactory.getInstance().getPromotion(promoCode);
			promo.removeReferences();
			
			String output = ser.toJSON(promo);
			
			if (request.getParameter("reformat") != null) {
				output = output.replace(",", ",\r\n");
				output = output.replace("[{", "[\r\n{");
				output = output.replace("}]", "}\r\n]");
			}
			%><%= output %><%
		}
	} catch (Exception exc) {
		exc.printStackTrace();
		response.setStatus(500);
		%>Internal error occurred.<%
	}
%>