<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import='org.json.JSONObject' %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

	<%
		JSONObject json = new JSONObject();
		//put in defaults
			/*
			 *	Available status texts:
			 *
			 *		N = default, ("none")
			 *		A = Gift Card status was fetched,
			 *			returned as "Active". Balance is available
			 *		C =	Gift Card status was fetched, returned as "Cancelled"
			 *		U =	Gift Card status was NOT fetched,
			 *			unknown status. May be a connectivity error.
			 *		E = Gift Card status was NOT fetched,
			 *			a definite error occurred
			 */
			json.put("stat", "N");

			/*
			 *	Balance as DOUBLE
			 *
			 *		-1 = not fetched.
			 *			 a fetched balance should never be below 0.0
			 */
			json.put("baln", "-1");
			
			/*
			 *	Status Code
			 *
			 *		   0 = not initialized.
			 *		 	   a non-error value is positive (>0),
			 *			   an error value is negative (<0)
			 *			   any status beside A,C or U is an error
			 *		   1 = active card, balance retrieved
			 *		   2 = cancelled card
			 *		  10 = unknwon error, gcModel != null, probably a connectivity error
			 *		  11 = unknwon error, gcModel == null, probably a connectivity error
			 *		  -1 = unknwon technical difficulties error
			 *		 -10 = invalid card error (print : result.getError("invalidCard").getDescription())
			 *		 -11 = givexNum error (giveX number is missing)
			 */
			json.put("code", "0");
	%>

	<%
		String saleId = NVL.apply(request.getParameter("gcSaleId"), "");
		String certNum = NVL.apply(request.getParameter("gcCertNum"), "");
	%>
	
<%@page import="com.freshdirect.webapp.util.JspMethods"%><fd:GetGiftCardRecipientDlvInfo id="dlvInfo" saleId="<%= saleId %>" certificationNum="<%= certNum %>">
		<%      
			if(dlvInfo != null){ %>
                <crm:CrmGiftCardBalance id="gcModel" result="result" actionName="getGiftCardBalance" givexNum="<%= dlvInfo.getGivexNum() %>">
					<%
						if(result.isSuccess()) {
							if(gcModel != null){
								if(gcModel.isActive()) {
									json.put("stat", "A"); //active
									json.put("baln", JspMethods.formatPrice(gcModel.getBalance()));
									json.put("code", "1") ;
								} else if(gcModel.isCancelled()) {
									json.put("stat", "C"); //cancelled
									json.put("code", "2") ;
								} else {
									json.put("stat", "U");
									json.put("code", "10");
								}
							} else {
								json.put("stat", "U");
								json.put("code", "11");
							}
						}else {
							if(result.getError("technicalDifficulty") != null) {
								json.put("stat", "E");
								json.put("code", "-1");
							} else if(result.getError("invalidCard") != null) {
								json.put("stat", "E");
								json.put("code", "-10");
							}else if(result.getError("givexNum") != null) {
								//Missing Info.
								json.put("stat", "E");
								json.put("code", "-11");
							}
						}
					%>
				</crm:CrmGiftCardBalance>
		<% } %>     
	</fd:GetGiftCardRecipientDlvInfo>
	<%=json.toString()%>