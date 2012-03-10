<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.referral.FDReferralManager'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/your_account/credits.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
    }	
    

	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
	
	List<ErpCustomerCreditModel> mimList = FDReferralManager.getUserCredits(customerIdentity.getErpCustomerPK());
	Iterator iter = mimList.iterator();
	double totalAmount = 0;	
	if(mimList.size() > 0) {
		ErpCustomerCreditModel cm = (ErpCustomerCreditModel) mimList.get(mimList.size() - 1);
		totalAmount = cm.getRemainingAmount();
	}
	
	String startIdx = request.getParameter("startIndex");	
	if(startIdx == null)
		startIdx = "0";

	int list_size = mimList.size() - 1;
	int index = list_size - Integer.parseInt(startIdx);
	int endIdx = index - 15;
	if(endIdx < 0) {
		endIdx = 0;
	}
	
	org.json.JSONObject jobj = new org.json.JSONObject();
	jobj.put("totalRecords", mimList.size());
	jobj.put("startIndex", index);
	jobj.put("sort", "date");
	jobj.put("dir", "asc");
	jobj.put("recordsReturned", 15);
	jobj.put("pageSize", 15);
	jobj.put("totalAmount", JspMethods.formatPrice(totalAmount));
	org.json.JSONArray jsonItems = new org.json.JSONArray();
	
	for(int i=index;i > endIdx; i--) {
		org.json.JSONObject obj = new org.json.JSONObject();
		ErpCustomerCreditModel cm = (ErpCustomerCreditModel) mimList.get(i);
		obj.put("date", cm.getcDate());
		obj.put("type", cm.getDepartment());
		obj.put("order", cm.getSaleId());
		obj.put("amount", (cm.getSaleId() == null && cm.getAmount() != 0)?JspMethods.formatPrice(cm.getAmount()):"");
		obj.put("balance", (cm.getSaleId() == null?"":"-") + JspMethods.formatPrice(cm.getRemainingAmount()));
		jsonItems.put(obj);
	}
	jobj.put("records", jsonItems);
	
    response.getWriter().write(jobj.toString());
	
%>