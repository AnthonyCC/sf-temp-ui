<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%
	CrmSession.setLockedCase(session, null);
	String redirect = "/main/index.jsp";
	if ("worklist".equalsIgnoreCase(request.getParameter("redirect"))) {
		redirect = "/main/worklist.jsp";
	} else if ("new_case".equalsIgnoreCase(request.getParameter("redirect"))) {
		redirect = "/case/new_case.jsp";
		if (request.getParameter("erpCustId")!=null) {
			redirect += "?erpCustId=" + request.getParameter("erpCustId");
		}	
		if (request.getParameter("orderId")!=null) {
			redirect += "&orderId=" + request.getParameter("orderId");


			// [APPREQ-478] pass selected carton IDs
			if (request.getParameter("cts") != null) {
				int nCartons = ( request.getParameter("cts") != null ? Integer.parseInt(request.getParameter("cts")) : 0);
				
				if (nCartons > 0) {
					redirect += "&cts=" + request.getParameter("cts");
					for (int i=0; i<nCartons; i++) {
						redirect += "&ct["+i+"]=" + request.getParameter("ct["+i+"]");
					}
				}
			}
		}
	} else if ("new_customer".equalsIgnoreCase(request.getParameter("redirect"))) {
		redirect = "/registration/nw_cst_check_zone.jsp";
	} else if ("case_mgmt".equalsIgnoreCase(request.getParameter("redirect"))) {
		redirect = "/case_mgmt/index.jsp";
	}
	
	String successPage = NVL.apply( request.getParameter("successPage"), redirect);
	response.sendRedirect(response.encodeRedirectURL(successPage));
%>
