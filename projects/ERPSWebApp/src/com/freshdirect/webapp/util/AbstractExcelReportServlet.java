package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.OutputStream;
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
public abstract class AbstractExcelReportServlet extends HttpServlet{

	private static Category LOGGER = LoggerFactory.getInstance(ExcelReportServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			initialize(request);
			List columns = this.getColumnNames();
			List report = this.getReport(request);
			
			HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = wb.createSheet(this.getWorksheetName());

					// Create a row and put some cells in it. Rows are 0 based.
					HSSFRow headerRow = sheet.createRow((short) 0);
			
					int c = 0;
					for(Iterator i = columns.iterator(); i.hasNext();c++){
						String colHeader = (String) i.next();
						headerRow.createCell((short) c).setCellValue(colHeader);
						System.out.println(colHeader);
					}


					LOGGER.debug("Generating rows");
					c=1;
					for (Iterator i = report.iterator(); i.hasNext(); c++) {
						Map row = (HashMap) i.next();

						HSSFRow xlRow = sheet.createRow((short) c);
						
						int r = 0;
						for(Iterator h = columns.iterator(); h.hasNext();r++){
							String col = (String) h.next();
							xlRow.createCell((short) r).setCellValue((String) row.get(col));
						}

					}
					LOGGER.debug("Writing out file...");
					response.setContentType("application/vnd.ms-excel");
					OutputStream out = response.getOutputStream();
					wb.write(out);
					out.close();			
		} catch (FDResourceException fe){
			LOGGER.error("System error occurred while performing the operation.", fe);
			throw new ServletException("System error occurred while performing the operation.", fe);
		}catch (Exception exp){
			LOGGER.error("Unknown error occurred while performing the operation.", exp);
			throw new ServletException("System error occurred while performing the operation.", exp);
		}
	}
	
	protected abstract void initialize(HttpServletRequest request);
	
	protected abstract String getWorksheetName();
	
	protected abstract List getColumnNames();

	protected abstract List getReport();
	
	protected List getReport(HttpServletRequest request) throws FDResourceException{
		//Default Implementation.
		return this.getReport();
	}

}
