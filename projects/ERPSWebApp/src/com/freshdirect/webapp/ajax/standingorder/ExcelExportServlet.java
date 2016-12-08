package com.freshdirect.webapp.ajax.standingorder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;

public class ExcelExportServlet extends HttpServlet {

	private final String filename = "SKU_DATA_GRID.xls";

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String extension = request.getParameter("extension");
		String excel = request.getParameter("excel");
	
		if (extension != null) {
			if (extension.equals("csv") || extension.equals("xml")) {
				
				File file = new File(filename);

				// if file doesn't exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(excel);
				bw.close();
			}
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		File outputFile = new File(filename);
		response.setBufferSize((int) outputFile.length());

		response.setHeader("Content-Disposition", "attachment; filename="+ outputFile.getName() );
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setContentLength((int) outputFile.length());
		
		FileInputStream is = new FileInputStream(outputFile);
		IOUtils.copy(is, response.getOutputStream());
		is.close();
		response.getOutputStream().close();
	}
}
