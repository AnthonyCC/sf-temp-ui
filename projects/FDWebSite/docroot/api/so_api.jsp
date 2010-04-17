<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@page import="java.util.Date"
%><%@page import="java.text.SimpleDateFormat"
%><%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"
%><%@page import="com.freshdirect.framework.util.StringUtil"
%><%@page import="com.freshdirect.framework.core.PrimaryKey"
%><%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"
%><%@page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"
%><%@page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"
%><%@page import="org.json.JSONArray"
%><%@page import="org.json.JSONObject"
%><%@page import="com.metaparadigm.jsonrpc.SerializerState"
%><%@page import="com.metaparadigm.jsonrpc.JSONSerializer"
%><%@page import="com.freshdirect.webapp.util.json.EnumStandingOrderFrequencyJSONSerializer"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		String action = request.getParameter("action");
		
		try {
			if ("getFrequencyList".equalsIgnoreCase(action)) {
				final JSONSerializer ser = new JSONSerializer();
				ser.registerDefaultSerializers();
				ser.registerSerializer(EnumStandingOrderFrequencyJSONSerializer.getInstance());
		
				JSONArray jsObj = (JSONArray) ser.marshall(new SerializerState(), EnumStandingOrderFrequency.values());
				%><%= jsObj.toString(4) %><%
			} else if ("setFrequency".equalsIgnoreCase(action)) {
				// Standing Order ID
				String soId = request.getParameter("soId");
				// frequency enum ordinal
				String freqOrd = request.getParameter("freqOrd");
				
				if (soId != null && !"".equals(soId) &&  freqOrd != null && !"".equals(freqOrd)) {
					FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					int ord = Integer.parseInt(freqOrd);
	
					for (EnumStandingOrderFrequency item : EnumStandingOrderFrequency.values()) {
						if (item.ordinal() == ord) {
							
							so.recalculateFrequency( item.getFrequency() );
							FDStandingOrdersManager.getInstance().save( so );
	
							%><div><%= StringUtil.escapeHTML(so.getCustomerListName()) %> will be now delivered</div><div style="padding-top: 1em; font-weight: bold"><%= item.getTitle() %></div><%
							
							break;
						}
					}
				}
			} else if ("setFrequencyCur".equalsIgnoreCase(action)) {
				// frequency enum ordinal
				String freqOrd = request.getParameter("freqOrd");
				
				if (freqOrd != null && !"".equals(freqOrd)) {
					int ord = Integer.parseInt(freqOrd);

					FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
					
					user.getCurrentStandingOrder().setFrequency(ord);
				}
			} else if ("delete".equalsIgnoreCase(action)) {
				// Standing Order ID
				String soId = request.getParameter("soId");
	
				if (soId != null && !"".equals(soId)) {
					FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					if (!so.isDeleted()) {
						FDStandingOrdersManager.getInstance().delete(so);
					}
				}
				
			} else if ("skip".equalsIgnoreCase(action)) {
				// Standing Order ID
				String soId = request.getParameter("soId");
	
				if (soId != null && !"".equals(soId)) {
					FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					if (!so.isDeleted()) {
						Date d = so.getNextDeliveryDate();
						
						so.skipDeliveryDate();
						FDStandingOrdersManager.getInstance().save( so );

						%>You will not receive a delivery on <%= new SimpleDateFormat("EEEE, MMMM d.").format( d ) %><%
					}
				}
				
			}
		} catch (Exception exc) {
			response.setStatus(500);
			%><div class="error">Internal error occurred.</div><%
		}
	}
%>