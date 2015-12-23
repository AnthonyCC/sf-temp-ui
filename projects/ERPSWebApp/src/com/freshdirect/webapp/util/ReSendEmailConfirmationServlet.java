package com.freshdirect.webapp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import com.freshdirect.fdstore.customer.FDCustomerManager;
public class ReSendEmailConfirmationServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String order = request.getParameter("orderId");
		JSONObject json = null;
		boolean ReSendEmailConfirmation=false;
		try {
			 ReSendEmailConfirmation= FDCustomerManager.reSendInvoiceEmail(order);
			 response.setContentType("application/json");
			 response.setCharacterEncoding("UTF-8");
			
			 if(ReSendEmailConfirmation)
			 {
				json = new JSONObject();
				json.put("result","Y");
				response.getWriter().print(json);
			 }
			 else
			 {
				 json = new JSONObject();
				 json.put("result","N");
				 response.getWriter().print(json);
			 }
				
			} catch (Exception e) {
		}
	}
 }
	




