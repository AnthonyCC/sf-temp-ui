package com.freshdirect.webapp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.ejb.AirclicManager;

public class SignatureServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpeg");
		ServletOutputStream out = response.getOutputStream();
		try
		{
			String order = request.getParameter("orderId");
			byte[] signBytes = AirclicManager.getInstance().getSignature(order);
			out.write(signBytes);
		}
		catch(Exception e)
		{
			out.println("<table><tr><td>There was an error while fetching the signature</table>");
		}
		finally
		{
			out.flush();
			out.close();
		}
		
	}
	
}
