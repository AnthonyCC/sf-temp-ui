package com.freshdirect.cms.application.draft.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.cms.application.draft.service.DraftService;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DraftPopulatorServlet extends HttpServlet {

    private static final long serialVersionUID = -6690717213918407108L;

    private static final Logger LOG = LoggerFactory.getInstance(DraftPopulatorServlet.class);

    public static final String CMS_DRAFT_CONTEXT_SESSION_NAME = "CMS_DRAFT_CONTEXT_SESSION_NAME";
    public static final String CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME = "CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME";

    private static final String CMS_DRAFT_ID_SESSION_NAME = "draft";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Draft> drafts = DraftService.defaultService().getDrafts();
        try {
            writeResponseData(response, drafts);
        } catch (HttpErrorResponse e) {
            response.sendError(e.getErrorCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String draftId = request.getParameter(CMS_DRAFT_ID_SESSION_NAME);
        Long parserDraftId;
        try {
            parserDraftId = Long.parseLong(draftId);
            DraftService.defaultService().setupDraftContext(parserDraftId, request);
        } catch (NumberFormatException e) {
            LOG.info("Can not set to draftcontext given draftId: " + draftId, e);
        }
        response.sendRedirect(response.encodeRedirectURL("cmsgwt.html?rnd=" + new Random().nextInt()));
    }

    private static <T> void writeResponseData(HttpServletResponse response, T responseData) throws HttpErrorResponse {
        configureJsonResponse(response);
        try {
            Writer writer = new StringWriter();
            new ObjectMapper().writeValue(writer, responseData);
            ServletOutputStream out = response.getOutputStream();
            String responseStr = writer.toString();
            out.print(responseStr);
            out.flush();
        } catch (JsonGenerationException e) {
            returnHttpError(500, "Error writing JSON response", e);
        } catch (JsonMappingException e) {
            returnHttpError(500, "Error writing JSON response", e);
        } catch (IOException e) {
            returnHttpError(500, "Error writing JSON response", e);
        } catch (Exception e) {
            returnHttpError(500, "Error writing JSON response", e);
        }
    }

    private static void configureJsonResponse(HttpServletResponse response) {
        // Set common response properties for JSON response
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("application/json");
        response.setLocale(Locale.US);
        response.setCharacterEncoding("ISO-8859-1");
    }

    private static void returnHttpError(int errorCode, String errorMessage, Throwable e) throws HttpErrorResponse {
        LOG.error(errorMessage, e);
        throw new HttpErrorResponse(errorCode);
    }

    private final static class HttpErrorResponse extends Exception {

        private static final long serialVersionUID = -4320607318778165536L;

        private int errorCode;

        public HttpErrorResponse(int errorCode) {
            this.errorCode = errorCode;
        }

        public int getErrorCode() {
            return errorCode;
        }
    }
}
