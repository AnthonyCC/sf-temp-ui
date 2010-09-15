package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**@author ekracoff on Aug 10, 2004*/
public abstract class AbstractVSExportServlet extends HttpServlet{

	private static Category LOGGER = LoggerFactory.getInstance(AbstractVSExportServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			initialize(request);
			List reportData = this.getReportData(request);
			
			response.addHeader ("Content-Disposition","attachment;filename="+getFileName());
			response.setContentType("application/Text"); 
			PrintWriter pw = response.getWriter();
			pw.write("Name,Number\r\n");
			for(Iterator itr = reportData.iterator();itr.hasNext();) {
				Map dataLine  = (Map)itr.next();
				String phoneNumber = (String) dataLine.get("HOME PHONE");
				String unFmtPhone = JspMethods.removeChars(phoneNumber,"() -");
				if (unFmtPhone==null || unFmtPhone.length() <10) unFmtPhone="";
				String firstName = (String)dataLine.get("FIRST NAME");
				String lastName = (String)dataLine.get("LAST NAME");
				pw.write(firstName+" "+lastName+",");
				pw.write(unFmtPhone+"\r\n");
				pw.flush();
			}
		} catch (FDResourceException fe){
			LOGGER.error("System error occurred while performing the operation.", fe);
			throw new ServletException("System error occurred while performing the operation.", fe);
		}catch (Exception exp){
			LOGGER.error("Unknown error occurred while performing the operation.", exp);
			throw new ServletException("System error occurred while performing the operation.", exp);
		}
	}
	
	protected abstract void initialize(HttpServletRequest request);
	
	protected abstract String getFileName();
	
	protected abstract List getReportData(HttpServletRequest request) throws FDResourceException;

}
