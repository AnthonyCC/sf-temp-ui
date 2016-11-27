<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.fdstore.referral.ManageInvitesModel" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/your_account/manage_invites.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
    }	
    

	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	
	List<ManageInvitesModel> mimList = FDReferralManager.getManageInvites(customerIdentity.getErpCustomerPK());
	
	String startIdx = request.getParameter("startIndex");	
	if(startIdx == null)
		startIdx = "0";

	int index = Integer.parseInt(startIdx);
	int endIdx = index + 15;
	if(endIdx > mimList.size()) {
		endIdx = mimList.size();
	}
	
	org.json.JSONObject jobj = new org.json.JSONObject();
	jobj.put("totalRecords", mimList.size());
	jobj.put("startIndex", index);
	jobj.put("sort", "date");
	jobj.put("dir", "asc");
	jobj.put("recordsReturned", 15);
	jobj.put("pageSize", 15);
	org.json.JSONArray jsonItems = new org.json.JSONArray();
	
	for(int i=index;i < endIdx; i++) {
		org.json.JSONObject obj = new org.json.JSONObject();
		ManageInvitesModel mim = (ManageInvitesModel) mimList.get(i);
		obj.put("email", mim.getRecipientEmail());
		obj.put("date", mim.getSentDate());
		obj.put("status", mim.getStatus());
		obj.put("credit", mim.getCredit()==null?"-":mim.getCredit());
		jsonItems.put(obj);
	}
	jobj.put("records", jsonItems);
	
	System.out.println(jobj.toString());
	
    response.getWriter().write(jobj.toString());
	
%>