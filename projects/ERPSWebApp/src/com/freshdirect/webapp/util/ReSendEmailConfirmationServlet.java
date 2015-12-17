package com.freshdirect.webapp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDCustomerManager;

public class ReSendEmailConfirmationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String order = request.getParameter("orderId");
		try {
			 FDCustomerManager.reSendInvoiceEmail(order);
			} catch (Exception e) {
		}
		
	}
 }
	
