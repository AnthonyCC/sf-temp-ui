package com.freshdirect.cms.ui.serviceimpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.ui.editor.ReportAttributes;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;
import com.freshdirect.cms.ui.editor.service.ContentLoaderService;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.attributes.TableAttribute;

public class CsvExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvExportServlet.class);

    private ContentLoaderService contentLoaderService = EditorServiceLocator.contentLoaderService();

    private ReportingService reportingService = EditorServiceLocator.reportingService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nodeKey;

        List<Map<Attribute, Object>> results = null;

        ContentKey key = null;

        try {
            nodeKey = request.getParameter("nodeKey");

            key = ContentKeyFactory.get(nodeKey);

            if (ContentType.CmsQuery == key.type) {
                Map<Attribute, Object> allAttributes = new HashMap<Attribute, Object>();
                reportingService.performCmsQuery(key, allAttributes);

                results = (List<Map<Attribute, Object>>) allAttributes.get(ReportAttributes.CmsQuery.results);
            } else if (ContentType.CmsReport == key.type) {
                Map<Attribute, Object> allAttributes = new HashMap<Attribute, Object>();
                reportingService.performCmsReport(key, allAttributes);
                results = (List<Map<Attribute, Object>>) allAttributes.get(ReportAttributes.CmsReport.results);
            }

        } catch (Exception e) {
            LOGGER.error("Failed to export tabular data for " + request.getParameter("nodeKey"), e);

            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Content not found. Cannot export to CSV.");
            return;
        }

        if (results == null || results.isEmpty()) {
            LOGGER.error("No or empty content found");

            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Content not found. Cannot export to CSV.");
            return;
        }

        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + nodeKey.replace(':', '-') + ".csv\"");
        PrintWriter out = response.getWriter();

        TableAttribute table = contentLoaderService.translateCmsReport(key, results);

        processTableData(table, out);
    }

    private void processTableData(TableAttribute table, PrintWriter out) {
        for (Serializable[] row : table.getRows()) {
            for (int i = 0; i < row.length; i++) {
                out.append('"');
                StringBuffer buf = new StringBuffer();
                Serializable item = row[i];
                if (item instanceof ContentNodeModel) {
                    buf.append(((ContentNodeModel) item).getLabel());
                    buf.append('[');
                    buf.append(((ContentNodeModel) item).getKey());
                    buf.append(']');
                } else if (item != null) {
                    buf.append(item.toString());
                } else {
                    buf.append("-N/A-");
                }
                escapeQuotes(buf);
                out.append(buf);
                out.append('"');
                if (i != row.length - 1)
                    out.append(',');
            }
            out.println();
        }
    }

    private void escapeQuotes(StringBuffer buf) {
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '"') {
                buf.insert(i, '"');
                i++;
            }
        }
    }
}
