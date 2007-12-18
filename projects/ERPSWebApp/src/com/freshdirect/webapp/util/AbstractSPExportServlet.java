package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**@author ekracoff on Aug 10, 2004*/
public abstract class AbstractSPExportServlet extends HttpServlet{

	private static Category LOGGER = LoggerFactory.getInstance(AbstractSPExportServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			initialize(request);
			List reportData = this.getReportData(request);
			
			response.addHeader ("Content-Disposition","attachment;filename="+getFileName());
			response.setContentType("application/Text"); 
			PrintWriter pw = response.getWriter();
			pw.write("Email,EmailType,FirstName\r\n");
			for(Iterator itr = reportData.iterator();itr.hasNext();) {
				Map dataLine  = (Map)itr.next();
				String email = (String)dataLine.get("EMAIL");
				if (email == null || "".equals(email)) continue;
				String firstName = (String)dataLine.get("FIRST NAME");
				if (firstName == null || "".equals(firstName)) {
					firstName = "FreshDirect Customer";
				}
				String emailType = (String)dataLine.get("EMAIL TYPE");
				pw.write(email+"\t"+emailType+"\t"+firstName+"\r\n");
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
