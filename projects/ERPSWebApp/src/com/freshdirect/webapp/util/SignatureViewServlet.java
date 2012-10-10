package com.freshdirect.webapp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.ejb.AirclicManager;

public class SignatureViewServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String order = request.getParameter("orderId");
		ServletOutputStream out = response.getOutputStream();
		try {
			if(type == null || type.trim().length() == 0) {
				response.setContentType("image/jpeg");
				byte[] _image = AirclicManager.getInstance().getSignature(order);
				out.write(_image);				
			}					
			
		} catch (Exception e) {
			out.println("<table><tr><td>There was an error while fetching the signature</td></tr></table>");
		} finally {
			out.flush();
			out.close();
		}
		
	}
	
}
