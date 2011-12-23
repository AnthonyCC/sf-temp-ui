<%@ page import='com.freshdirect.webapp.taglib.giftcard.GiftCardUtil' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>

<%
	boolean isResendFetch = false;
	boolean isResendEmail = false;
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );

	if ( "true".equals((String)request.getParameter("isResendFetch")) ) {
		isResendFetch = true;
	}
	if ( "true".equals((String)request.getParameter("isResendEmail")) ) {
		isResendEmail = true;
	}

	//if we're fetching the info for the overlay display
	if (isResendFetch) {
		JSONObject json = new JSONObject();
	
		/*
		 *	assume an error here by default since we're fetching data
		 *	this is also the minimum we'll return (a status)
		 */
		json.put("status", "error");
        String saleId = request.getParameter("saleId");
        String certNum = request.getParameter("certNum");
        
        if (saleId == null || saleId.trim().length() <= 0 || certNum == null || certNum.trim().length() <= 0) {
            json.put("status", "error");
        } else {
    %>
         <fd:GetGiftCardRecipientDlvInfo id="dlvInfo" saleId="<%= saleId %>" certificationNum="<%= certNum %>">
    <%      
            if(dlvInfo != null){
                json.put("status", "ok"); //chamge status to OK (js will just check for != "error")
                json.put("gcRecipName", dlvInfo.getRecepientModel().getRecipientName());
                json.put("gcRecipEmail",dlvInfo.getRecepientModel().getRecipientEmail());
                json.put("gcAmount", currencyFormatter.format(dlvInfo.getRecepientModel().getAmount())); //this is in the page, displayed
                json.put("gcMessage",dlvInfo.getRecepientModel().getPersonalMessage());
                json.put("gcSaleId",saleId);
                json.put("gcCertNum",dlvInfo.getCertificationNumber());
            }
		//return the json string which the js will then push into the overlay box
	%>     </fd:GetGiftCardRecipientDlvInfo>  <%=json.toString()%>
    <%
    	}
     }   

	if (isResendEmail) {
            String saleId = request.getParameter("gcSaleId");
            String certNum = request.getParameter("gcCertNum");
            String resendEmailId = request.getParameter("gcRecipEmail");
            String recipName = request.getParameter("gcRecipName");
            String persMessage = request.getParameter("gcMessage");

		boolean success = GiftCardUtil.resendEmail(request, saleId, certNum, resendEmailId, recipName, persMessage);
        JSONObject json = new JSONObject();
        if(success) {
		    //if we return anything, it will de displayed in the overlay (can be html)
            json.put("returnMsg", "<b><span style=\"color: #f00;\">Gift Card resent successfully.</span></b>");
        } else {
            json.put("returnMsg", "<b><span style=\"color: #f00;\">Unable to process your request. Please contact customer service.</span></b>");
        }
		%>  <%=json.toString()%>
		<%
	}

%>