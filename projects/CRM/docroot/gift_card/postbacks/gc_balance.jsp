<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import='org.json.JSONObject' %>

<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
		<%String givexNum = NVL.apply(request.getParameter("givexNum"), "");%>
        <% JSONObject json = new JSONObject(); %>
		<crm:CrmGiftCardBalance id="gcModel" givexNum="<%= givexNum %>" result="result" actionName="getGiftCardBalance">
        <%
            if(result.isSuccess()) {
                java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
                if(gcModel != null){
                    if(gcModel.isActive()) {
                        json.put("message", "<b>= "+currencyFormatter.format(gcModel.getBalance())+"</b>") ;
                    } else if(gcModel.isCancelled()) {
                        json.put("message", "<b>Certificate Cancelled</b>") ;
                    } else {
                        json.put("message", "<span style=\"color: FF0000;\"><b>Sorry, we''re experiencing technical difficulties. Please try again later.</b></span>");
                    }
                } else {
                    json.put("message", "<span style=\"color: FF0000;\"><b>Sorry, we''re experiencing technical difficulties. Please try again later.</b></span>");
                }
            }else {
                if(result.getError("givexNum") != null) {
                    //Missing Info.
                    json.put("message", "<span style=\"color: FF0000;\"><b>Givex Number is required.</b></span>");
                } else if(result.getError("invalidCard") != null) {
                    json.put("message", "<span style=\"color: FF0000;\"><b>"+result.getError("invalidCard").getDescription()+"</b></span>");
                }else if(result.getError("technicalDifficulty") != null) {
                    json.put("message", "<span style=\"color: FF0000;\"><b>Sorry, we''re experiencing technical difficulties. Please try again later.</b></span>");
                }
            }
        %>
		</crm:CrmGiftCardBalance>
        <%=json.toString()%>
