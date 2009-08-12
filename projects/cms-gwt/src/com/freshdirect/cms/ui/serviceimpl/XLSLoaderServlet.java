package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.ProductBulkLoader;

public class XLSLoaderServlet extends FileUploadServlet {

    @Override
    protected String handleFileItems(HttpServletRequest req, List<FileItem> list) throws ServletException, IOException  {

        FileItem file = null;

        for (FileItem item : list) {
            if ("xlsFile".equals(item.getFieldName())) {
                file = item;
            }
        }

        if (file != null) {
            StringBuilder builder = processFile(req, file);
            return builder.toString();
        }
        throw new ServletException("xlsFile missing!");
    }

    private StringBuilder processFile(HttpServletRequest req, FileItem file) {

        StringBuilder response = new StringBuilder();

        List<ContentKey> successes = new java.util.LinkedList<ContentKey>();
        Map<String, String> failures = new TreeMap<String, String>();
        try {
            ProductBulkLoader.XLSBulkLoad(file.getInputStream(), ContentServiceImpl.getUserFromRequest(req).getName(), successes, failures);
        } catch (Exception e) {
            response.append("Exception occured: " + e.toString() + "\n");
            StringWriter w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            response.append(w.getBuffer().toString());
            e.printStackTrace();
        } finally {
            for (ContentKey ck : successes) {
                response.append("Success:" + ck.getEncoded());
            }
            if (failures.size() > 0) {
                for (Iterator<Map.Entry<String, String>> i = failures.entrySet().iterator(); i.hasNext();) {
                    Map.Entry<String, String> ei = i.next();
                    response.append("Failed to insert " + ei.getKey() + " because " + ei.getValue() + "!\n");
                }
            }
        }
        return response;
    }

}
