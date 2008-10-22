package com.freshdirect.transadmin.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

public class DownloadController extends AbstractMultiActionController  {
			
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView downloadHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {	
		String strFilePath = request.getParameter("filePath");
		File outputFile = new File(strFilePath);
		response.setBufferSize((int)outputFile.length());
		response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
		response.setContentType("application/x-download");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setContentLength((int)outputFile.length());
		try {
			FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block				
			e.printStackTrace();
		}
		return null;
	}
}
	
