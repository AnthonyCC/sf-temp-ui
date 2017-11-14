package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;

public class RecipeLoaderServlet extends FileUploadServlet {

    private static final long serialVersionUID = 6175004711716992533L;

    @Override
    protected String handleFileItems(HttpServletRequest request, List<FileItem> items) throws IOException, ServletException {
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
        ContentKey key = handleFile(request, type, file);

        return "ContentKey : " + key.toString();
    }

    private ContentKey handleFile(HttpServletRequest request, String typeValue, FileItem file) throws IOException, ServletException {
        int type = Integer.parseInt(typeValue);

        String fileName = parseFilename(file.getName(), true);

        return load(request, new InputStreamReader(file.getInputStream(), "ISO-8859-1"), fileName, type);

    }

    private ContentKey load(HttpServletRequest request, Reader reader, String recipeId, int type) throws IOException {

        // TODO: this is a call to the old CMS. When Bulkloader story kicks in, implement this.

        // List<Map<ContentKey, Map<Attribute, Object>>> list;
        // RecipeBulkLoader loader = new RecipeBulkLoader(CmsManager.getInstance(), DraftContext.MAIN, reader, recipeId, type);

        // parse the input
        // loader.parse();

        // process the input
        // list = loader.process();

        // create a new request with all nodes, and add it to the content
        // service
        // CmsRequest cmsRequest = new CmsRequest(ContentServiceImpl.getCmsUserFromRequest(request), Source.RECIPE_LOADER);

        // for (Iterator<Map<ContentKey, Map<Attribute, Object>>> it = list.iterator(); it.hasNext();) {
        // Map<ContentKey, Map<Attribute, Object>> node = it.next();

        // cmsRequest.addNode(node);
        // }

        // CmsManager.getInstance().handle(cmsRequest);

        return ContentKeyFactory.get("Recipe:dummy");
    }

    /**
     * Parse a file name from a Unix or Windows path.
     * 
     * @param fileName
     *            path
     * @param removeExtension
     *            whether to remove extension from file name
     * @return filename part
     */
    private static String parseFilename(String fileName, boolean removeExtension) {
        int start = fileName.lastIndexOf('/') + 1;
        if (start == 0) {
            start = fileName.lastIndexOf('\\') + 1;
        }

        int lastDot = removeExtension ? -1 : fileName.lastIndexOf(".");
        if (lastDot == -1) {
            lastDot = fileName.length();
        }
        return fileName.substring(start, lastDot);
    }
}
