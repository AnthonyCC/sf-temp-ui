package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.ui.editor.bulkloader.BulkLoadPreviewSessionStore;
import com.freshdirect.cms.ui.editor.bulkloader.BulkUploadResult;
import com.freshdirect.cms.ui.editor.bulkloader.service.ContentBulkLoaderService;

public class BulkLoaderXlsUploadServlet extends FileUploadServlet {

    private static final long serialVersionUID = 782726764466391767L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkLoaderXlsUploadServlet.class);

    private ContentBulkLoaderService contentBulkLoaderService = EditorServiceLocator.contentBulkLoaderService();

    @Override
    protected String handleFileItems(HttpServletRequest request, List<FileItem> list) throws ServletException, IOException {
        String uploadStatus = null;

        FileItem file = null;

        for (FileItem item : list) {
            if ("xlsFile".equals(item.getFieldName())) {
                file = item;
                break;
            }
        }

        if (file != null) {
            try {

                BulkUploadResult result = contentBulkLoaderService.doUpload(file.getInputStream());

                uploadStatus = result.getStatus();
                if ("OK".equals(uploadStatus)) {

                    BulkLoadPreviewSessionStore.setPreviewHeader(request.getSession(), result.getHeader());
                    BulkLoadPreviewSessionStore.setPreviewRows(request.getSession(), result.getPreviewRows());

                }

            } catch (RuntimeException e) {
                LOGGER.error("error during parsing XLS file: " + file.getName(), e);
                uploadStatus = e.getMessage();
            } catch (IOException e) {
                LOGGER.error("error reading XLS file: " + file.getName(), e);
                uploadStatus = e.getMessage();
            }
        } else {
            uploadStatus = "XML file was not found in upload content";
        }

        return uploadStatus;
    }
}
