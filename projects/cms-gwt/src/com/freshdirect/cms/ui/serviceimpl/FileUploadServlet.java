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

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.draft.service.DraftService;

public abstract class FileUploadServlet extends HttpServlet {

    private DraftContextHolder draftContextHolder = CmsServiceLocator.draftContextHolder();

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ServletFileUpload upload;

    @Override
    public void init() throws ServletException {
        super.init();
        upload = new ServletFileUpload(new DiskFileItemFactory());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Enforce setting draft context on each upload request
        final DraftContext draftContext = getDraftContext(request);
        draftContextHolder.setDraftContext(draftContext);

        try {
            if (ServletFileUpload.isMultipartContent(request)) {
                List<FileItem> list = upload.parseRequest(request);

                String handleResponse = handleFileItems(request, list);
                response.setContentType("text/html");
                handleResponse = "<div class='file-upload-response'>" + handleResponse.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>") + "</div>";
                response.getWriter().print(handleResponse);

            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File missing!");
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
            throw new ServletException("Error during upload:" + e.getMessage(), e);
        }
    }

    protected abstract String handleFileItems(HttpServletRequest req, List<FileItem> list) throws ServletException, IOException;

    private DraftContext getDraftContext(HttpServletRequest request) {
        DraftContext draftContext = (DraftContext) request.getSession().getAttribute(DraftService.CMS_DRAFT_CONTEXT_SESSION_NAME);
        if (draftContext == null) {
            draftContext = DraftContext.MAIN;
        }
        return draftContext;
    }
}
