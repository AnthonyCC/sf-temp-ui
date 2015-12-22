<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="com.freshdirect.fdstore.EnumEStoreId" %>
<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryServiceSelectionResult" %>
<%@ page import="com.freshdirect.fdlogistics.model.FDDeliveryZipInfo" %>
<%@ page import='java.util.*' %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
String zc = "";
zc = (String)request.getParameter("zipCheck");

FDDeliveryServiceSelectionResult result = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zc, EnumEStoreId.FDX);
Set<EnumServiceType> availServices = result.getAvailableServices();

availServices.remove(EnumServiceType.PICKUP);

for( EnumServiceType service : availServices ){
	out.println( service );
}
%>