package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public abstract class FileUploadServlet extends HttpServlet {

    ServletFileUpload upload;

    @Override
    public void init() throws ServletException {
        super.init();
        upload = new ServletFileUpload(new DiskFileItemFactory());
    }

    @SuppressWarnings( "unchecked" )
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            //System.out.println("TYPE:"+ req.getParameter("type"));
            if (ServletFileUpload.isMultipartContent(req)) {
                List<FileItem> list = upload.parseRequest(req);
                    
                String response = handleFileItems(req, list);
                resp.setContentType("text/plain");
                resp.getWriter().print(response);
                
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File missing!");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            throw new ServletException("Error during upload:" + e.getMessage(), e);
        }
    }

    protected abstract String handleFileItems(HttpServletRequest req, List<FileItem> list) throws ServletException, IOException;
    
}
