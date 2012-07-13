package com.freshdirect.webapp.coremetrics;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CdfModelGenerator;
import com.freshdirect.fdstore.coremetrics.CdfRowModel;
import com.freshdirect.framework.util.log.LoggerFactory;


public class CdfGeneratorServlet extends HttpServlet {

	private static final long serialVersionUID = -4457472670947131040L;
	private static Logger LOGGER = LoggerFactory.getInstance(CdfGeneratorServlet.class);

	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		response.setHeader("content-type", "text/csv");
		response.setHeader("content-disposition", "attachment;filename=\"CDF_" + FDStoreProperties.getCoremetricsClientId() + ".csv\"");

		StringBuilder sb = new StringBuilder();
		for (CdfRowModel cdfRow : new CdfModelGenerator().generateCdfModel()){
			sb.append(cdfRow).append("\n");
		}

		try {
			byte[] bytes = sb.toString().getBytes("UTF-8"); 
			response.setContentLength(bytes.length);

			OutputStream out = response.getOutputStream();
			for (byte b : bytes){
				out.write((char)b);
			}
			
			out.flush();
			out.close();
		} catch (IOException e) {
			LOGGER.error("Coremetrics CDF generation failed",e);
		}
	}
}
