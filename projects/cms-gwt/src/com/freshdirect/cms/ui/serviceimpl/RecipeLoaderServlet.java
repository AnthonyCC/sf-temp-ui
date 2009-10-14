package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.fdstore.recipes.RecipeBulkLoader;
import com.freshdirect.framework.util.StringUtil;

public class RecipeLoaderServlet extends FileUploadServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    @Override
    protected String handleFileItems(HttpServletRequest req, List<FileItem> items) throws IOException, ServletException {
        String type = null;
        FileItem file = null;
        for (FileItem item : items) {
            if ("type".equals(item.getFieldName())) {
                type = item.getString();
            }
            if ("recipeFile".equals(item.getFieldName())) {
                file = item;
            }
        }
        ContentKey key = handleFile(req, type, file);
        
        return "ContentKey : "+ key.getEncoded();
    }

    ContentKey handleFile(HttpServletRequest req, String typeValue, FileItem file) throws IOException, ServletException {
        try {
            int type = Integer.parseInt(typeValue);

            String fileName = StringUtil.parseFilename(file.getName(), true);

            return load(req, new InputStreamReader(file.getInputStream(), "ISO-8859-1"), fileName, type);
        } catch (InvalidContentKeyException e) {
            throw new ServletException("Invalid content key:" + e.getMessage(), e);
        }

    }

    private ContentKey load(HttpServletRequest request, Reader reader, String recipeId, int type) throws IOException, InvalidContentKeyException {

        List<ContentNodeI> list;
        RecipeBulkLoader loader = new RecipeBulkLoader(CmsManager.getInstance(), reader, recipeId, type);

        // parse the input
        loader.parse();

        // process the input
        list = loader.process();

        // create a new request with all nodes, and add it to the content
        // service
        CmsRequest cmsRequest = new CmsRequest(ContentServiceImpl.getCmsUserFromRequest(request));

        for (Iterator<ContentNodeI> it = list.iterator(); it.hasNext();) {
            ContentNodeI node = (ContentNodeI) it.next();

            cmsRequest.addNode(node);
        }

        CmsManager.getInstance().handle(cmsRequest);

        return ((ContentNodeI) list.get(list.size() - 1)).getKey();
    }

}
