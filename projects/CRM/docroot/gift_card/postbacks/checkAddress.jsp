<%@ page import='com.freshdirect.webapp.taglib.giftcard.GiftCardUtil' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>

<% JSONObject json = new JSONObject(); %>
	
         <fd:CheckAddress result="result">
    <%      
        if(result.isSuccess()) {
            json.put("message", "<span class=\"gcCheckAddressBoxOrange\"><b>Good news!</b></span> We deliver to the address below.");
        }else {
            if(result.getError("incomplete_addr") != null) {
                //Missing Info.
                json.put("message", "<b>Please enter the complete address.</b>");
            } else if(result.getError("undeliverableAddress") != null) {
                json.put("message", "<b>Sorry, we don''t currently deliver to this address.</b><br />If this is a New York address, may we suggest our Pickup Window in Long Island City?");
            }else if(result.getError("cantGeocode") != null) {
                json.put("message", "Sorry, we''re unable to recognize this <b>address</b>. Please make sure it's entered correctly.");
            }else if(result.getError("technicalDifficulty") != null) {
                json.put("message", "Sorry, we''re experiencing technical difficulties. Please try again later.");
            }
        }
		//return the json string which the js will then push into the overlay box
	%>     </fd:CheckAddress>  <%=json.toString()%>
