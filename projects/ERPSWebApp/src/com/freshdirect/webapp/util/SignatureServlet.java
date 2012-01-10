package com.freshdirect.webapp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.ejb.AirclicManager;

public class SignatureServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String order = request.getParameter("orderId");
		String path = getServletContext().getRealPath(getServletContext().getContextPath());
		if(!new File(path).exists())
		{	
			byte[] signBytes = AirclicManager.getInstance().getSignature(order);
			generateSignature(order, signBytes, getServletContext().getRealPath(getServletContext().getContextPath()));
		}
		
	}

	private void generateSignature(String order, byte[] signBytes, String path)
			throws IOException {
		
		 FileOutputStream file = new FileOutputStream (path+"/media_stat/images/signatures/"+order+".jpg");
		 file.write(signBytes);
	     file.close();
	}

	
}
