package com.freshdirect.webapp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.ejb.AirclicManager;
import com.freshdirect.delivery.model.SignatureVO;

public class SignatureServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String order = request.getParameter("orderId");
		ServletOutputStream out = response.getOutputStream();
		try {
			if(type == null || type.trim().length() == 0) {
				response.setContentType("text/html");
				SignatureVO signatureVO = AirclicManager.getInstance().getSignatureDetails(order);
				StringBuffer strBuf = new StringBuffer();
				strBuf.append("<table width=\"300\" align=\"center\"><tr><td colspan=\"2\" style=\"text-align:center;font-weight:bold;\">Signature Details</td></tr><tr><td colspan=\"2\"></td></tr><tr><td colspan=\"2\"></td></tr>");				
				strBuf.append("<tr><td style=\"text-align:right;font-size: 10pt;font-family: Trebuchet MS,Arial,Verdana,sans-serif; \">Order Number:&nbsp;&nbsp;</td>").append("<td style=\"text-align:left;font-size: 10pt;\">").append(order).append("</td></tr>");
				strBuf.append("<tr><td style=\"text-align:right;font-size: 10pt;font-family: Trebuchet MS,Arial,Verdana,sans-serif; \">Delivered To:&nbsp;&nbsp;</td>").append("<td style=\"text-align:left;font-size: 10pt;\">").append(signatureVO != null ? signatureVO.getDeliveredTo():"").append("</td></tr>");
				strBuf.append("<tr><td style=\"text-align:right;font-size: 10pt;font-family: Trebuchet MS,Arial,Verdana,sans-serif; \">Recipient:&nbsp;&nbsp;</td>").append("<td style=\"text-align:left;font-size: 10pt;\">").append(signatureVO != null ? signatureVO.getRecipient():"").append("</td></tr>");
				strBuf.append("<tr><td style=\"text-align:right;font-size: 10pt;font-family: Trebuchet MS,Arial,Verdana,sans-serif; \">Contails Alcohol:&nbsp;&nbsp;</td>").append("<td style=\"text-align:left;font-size: 10pt;\">").append((signatureVO != null && signatureVO.isContailsAlcohol()) ? "Yes":"No").append("</td></tr>");
				strBuf.append("<tr><td style=\"text-align:right;font-size: 10pt;font-family: Trebuchet MS,Arial,Verdana,sans-serif; \">Signed Time:&nbsp;&nbsp;</td>").append("<td style=\"text-align:left;font-size: 10pt;\">").append(signatureVO != null ? signatureVO.getSignatureTime():"").append("</td></tr>");
				
				strBuf.append("</table>");
				strBuf.append("<table><tr><td><img src=\"");				
				strBuf.append(request.getRequestURI()).append("?orderId=").append(order).append("&type=I").append("\" />");
				strBuf.append("</td></tr></table>");
				out.println(strBuf.toString());
			} else {
				response.setContentType("image/jpeg");
				byte[] _image = AirclicManager.getInstance().getSignature(order);
				out.write(_image);
			}					
			
		} catch (Exception e) {
			out.println("<table><tr><td>There was an error while fetching the signature</table>");
		} finally {
			out.flush();
			out.close();
		}
		
	}
	
}
