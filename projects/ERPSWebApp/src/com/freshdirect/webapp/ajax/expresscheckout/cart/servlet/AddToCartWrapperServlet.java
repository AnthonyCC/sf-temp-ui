package com.freshdirect.webapp.ajax.expresscheckout.cart.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.cart.AddToCartServlet;

public class AddToCartWrapperServlet extends AddToCartServlet {

	private static final long serialVersionUID = 995628914356527337L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		super.doPost(request, response, user);
	}
}
