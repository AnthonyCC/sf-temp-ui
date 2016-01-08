package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
/**
 * @author Aniwesh Vatsal
 *
 */
public class ScrubbedCSVReportServlet extends HttpServlet {
	
	private static Category LOGGER = LoggerFactory.getInstance(ScrubbedCSVReportServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.debug("Inside doPost of ScrubbedCSVReportServlet");
		try{
			// Add Null Check here
			Future futureCSVReportTask = null;
			if(request.getAttribute("future") != null){
				futureCSVReportTask = (Future)request.getAttribute("future");
			}
			if(futureCSVReportTask == null){
				futureCSVReportTask = (Future)request.getSession().getAttribute("future");
			}
			if(futureCSVReportTask != null ){
				request.getSession().removeAttribute("future");
				response.setContentType("text/plain");
				response.setHeader("Content-Disposition",
			                     "attachment;filename=csvReport.csv");
				OutputStream os = response.getOutputStream();
				String report = (String)futureCSVReportTask.get();
				byte[] bytes = report.getBytes();
				os.write(bytes, 0, report.length());
				os.flush();
				os.close();
				getServletContext().getRequestDispatcher("/scrubbingtool/address_scrubbing_success.jsp").forward(request, response);
			}else{
				getServletContext().getRequestDispatcher("/scrubbingtool/address_scrubbing_success.jsp").forward(request, response);
			}
		}catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception while downloading the CSV Report "+e.getMessage());
		}
		
		LOGGER.debug("Exit doPost of ScrubbedCSVReportServlet");
	}
		
	
}
