<%@ page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.delivery.DlvZipInfoModel" %>
<%@ page import='java.util.*' %>

<%@ taglib uri="template" prefix="tmpl" %>

<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	Map params = new HashMap();

	Enumeration keys = request.getParameterNames();  
	while (keys.hasMoreElements() ) {  
		String key = (String)keys.nextElement(); 

		//To retrieve a single value  
		String value = request.getParameter(key); 

		params.put(key, value);
	
		// If the same key has multiple values (check boxes)  
		String[] valueArray = request.getParameterValues(key);  
		 
		for(int i = 0; i > valueArray.length; i++){
			params.put(key+valueArray[i], valueArray[i]);
		}  
	}

	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	params.put("baseUrl", baseUrl);
	params.put("uri", request.getRequestURI());
	
	boolean fromZipCheck = "yes".equalsIgnoreCase(request.getParameter("zipCheck"));
	params.put("fromZipCheck", fromZipCheck);
	
	/* deliverable zips */
	List<DlvZipInfoModel> zipCodesUnsortedH = FDDeliveryManager.getInstance().getDeliverableZipCodes(EnumServiceType.HOME);
	TreeMap<String, DlvZipInfoModel> zipCodesSortedH = new TreeMap<String, DlvZipInfoModel>();
	for (DlvZipInfoModel zipInfoH : zipCodesUnsortedH) {
		zipCodesSortedH.put(zipInfoH.getZipCode()+(zipInfoH.getCoverage() <= 0.9 ? "*" : ""), zipInfoH);
	}
	List<DlvZipInfoModel> zipCodesH = new ArrayList<DlvZipInfoModel>(zipCodesSortedH.values());
	
	params.put("zipsHome", zipCodesH);
	
	List<DlvZipInfoModel> zipCodesUnsortedC = FDDeliveryManager.getInstance().getDeliverableZipCodes(EnumServiceType.CORPORATE);
	TreeMap<String, DlvZipInfoModel> zipCodesSortedC = new TreeMap<String, DlvZipInfoModel>();
	for (DlvZipInfoModel zipInfoC : zipCodesUnsortedC) {
		zipCodesSortedC.put(zipInfoC.getZipCode()+(zipInfoC.getCoverage() <= 0.9 ? "*" : ""), zipInfoC);
	}
	List<DlvZipInfoModel> zipCodesC = new ArrayList<DlvZipInfoModel>(zipCodesSortedC.values());
	
	params.put("zipsCorp", zipCodesC);
	

	params.put("showZips", true);
%>
<tmpl:insert template='/shared/template/large_pop_no_ads.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Our Delivery Zones</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/pickup/main.ftl" parameters="<%=params%>" withErrorReport="true" />
	</tmpl:put>
</tmpl:insert>


