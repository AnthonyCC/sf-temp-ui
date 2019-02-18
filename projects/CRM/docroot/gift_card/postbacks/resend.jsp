<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.giftcard.GiftCardUtil' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SystemMessageList' %>
<%@ page import='com.freshdirect.mail.EmailUtil' %>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>

<%
	boolean isResendFetch = false;
	boolean isResendEmail = false;
	boolean isSendCancellationEmail = false;

	if ( "true".equals((String)request.getParameter("isResendFetch")) ) {
		isResendFetch = true;
	}
	if ( "true".equals((String)request.getParameter("isResendEmail")) ) {
		isResendEmail = true;
	}
	
	if ( "true".equals((String)request.getParameter("isSendCancellationEmail")) ) {
		isSendCancellationEmail = true;
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
                json.put("gcAmount", JspMethods.formatPrice(dlvInfo.getRecepientModel().getAmount())); //this is in the page, displayed
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
            boolean toPurchaser = "true".equals((String)request.getParameter("gcIsPurchaser"))?true:false;
            boolean toLastRecipient = "true".equals((String)request.getParameter("gcIsLastRecip"))?true:false;            
            JSONObject json = new JSONObject();
            if(!EmailUtil.isValidEmailAddress(resendEmailId)){
               	json.put("returnMsg", "<b><span style=\"color: #f00;\">"+SystemMessageList.MSG_EMAIL_FORMAT+"</span></b>");
                json.put("opStatus","error");
            }
            else if(!toPurchaser && !toLastRecipient){
            	json.put("returnMsg", "<b<span style=\"color: #f00;\">Please select atleast one to send gift card email.</span></b>");
            	
            }else{
				boolean success = GiftCardUtil.resendEmail(request, saleId, certNum, resendEmailId, recipName, persMessage);
		        
		        if(success) {
				    //if we return anything, it will de displayed in the overlay (can be html)
		            json.put("returnMsg", "<b><span style=\"color: #f00;\">Gift Card resent successfully.</span></b>");
		            json.put("opStatus","ok");
		        } else {
		            json.put("returnMsg", "<b><span style=\"color: #f00;\">Unable to process your request. Please contact customer service.</span></b>");
		            json.put("opStatus","error");
		        }
            }
		%>  <%=json.toString()%>
		<%
	}
	
	

%>