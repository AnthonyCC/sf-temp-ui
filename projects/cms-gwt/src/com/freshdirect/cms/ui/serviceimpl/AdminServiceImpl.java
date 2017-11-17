package com.freshdirect.cms.ui.serviceimpl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.publish.service.PublishFlowControlService;
import com.freshdirect.cms.publish.service.impl.BasicPublishFlowControlService;
import com.freshdirect.cms.search.BackgroundStatus;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AdminServiceImpl extends GwtServiceBase implements AdminService {

    private static final long serialVersionUID = 1263043539819341529L;

    private static final Logger LOG = LoggerFactory.getInstance(AdminServiceImpl.class);

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private PublishFlowControlService publishFlowControlService = BasicPublishFlowControlService.defaultInstance();

    public AdminServiceImpl() {
        super();
    }

    @Override
    public AdminProcStatus getBuildIndexStatus() {
        IBackgroundProcessor search = getAdminSearch();
        BackgroundStatus status = search.getStatus();
        return createAdminProcStatus(status);
    }

    private AdminProcStatus createAdminProcStatus(BackgroundStatus status) {
        AdminProcStatus visibleStatus = new AdminProcStatus(status.getCurrent(), status.getLastReindexResult(), status.isRunning(), status.getStarted(),
                status.isRunning() ? System.currentTimeMillis() - status.getStarted() : 0);
        return visibleStatus;
    }

    @Override
    public AdminProcStatus rebuildIndexes() {

        synchronized (AdminServiceImpl.class) {
            IBackgroundProcessor tool = getAdminSearch();
            BackgroundStatus status = tool.getStatus();

            LOG.info("rebuild index called (" + status.isRunning() + ")");
            if (!status.isRunning() && isNodeModificationEnabled(getDraftContext())) {
                tool.backgroundReindex();

            }

        }
        return getBuildIndexStatus();
    }

    @Override
    public AdminProcStatus rebuildWineIndexes() {

        synchronized (AdminServiceImpl.class) {
            IBackgroundProcessor tool = getAdminSearch();
            BackgroundStatus status = tool.getStatus();
            LOG.info("rebuild index called (" + status.isRunning() + ")");
            if (!status.isRunning() && isNodeModificationEnabled(getDraftContext())) {
                tool.rebuildWineIndex();
            }

        }
        return getBuildIndexStatus();
    }

    private IBackgroundProcessor getAdminSearch() {
        return (IBackgroundProcessor) FDRegistry.getInstance().getService("com.freshdirect.cms.backgroundProcessor", IBackgroundProcessor.class);
    }

    @Override
    public AdminProcStatus validateEditors() {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    LOG.info("validate editors started");
                    validateEditorsImpl();
                    LOG.info("validate editors finished");
                } catch (Exception e) {
                    LOG.error("validate editors failed:" + e.getMessage(), e);
                }
            }
        });
        return getBuildIndexStatus();
    }

    private void validateEditorsImpl() {
        Set<ContentKey> editorKeys = contentService.getContentKeysByType(ContentType.get("CmsEditor"), getDraftContext());
        Map<ContentKey, ContentNodeI> nodes = contentService.getContentNodes(editorKeys, getDraftContext());
        ContentValidationDelegate delegate = new ContentValidationDelegate();
        ContentValidatorI validator = new CmsFormValidator();
        for (ContentNodeI e : nodes.values()) {
            validator.validate(delegate, contentService, getDraftContext(), e, null, null);
        }
        LOG.warn("editor validation:" + delegate);
    }

    private static class CmsFormValidator implements ContentValidatorI {

        @Override
        public void validate(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
            ContentType type = ContentType.get(node.getAttributeValue("contentType").toString());
            ContentTypeDefI def = service.getTypeService().getContentTypeDefinition(type);

            Set<String> editorFields = collectFieldNames(delegate, service, draftContext, node);
            Set<String> attributes = def.getAttributeNames();

            Set<String> extra = new HashSet<String>(editorFields);
            extra.removeAll(attributes);
            if (!extra.isEmpty()) {
                delegate.record(node.getKey(), "Extraneous fields: " + extra);
            }

            Set<String> missing = new HashSet<String>(attributes);
            missing.removeAll(editorFields);
            if (!missing.isEmpty()) {
                delegate.record(node.getKey(), "Missing fields: " + missing);
            }
        }

        private Set<String> collectFieldNames(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI editor) {
            Set<String> names = new HashSet<String>();

            Set<ContentKey> pageKeys = ContentNodeUtil.getChildKeys(editor);

            for (Iterator<ContentNodeI> i = service.getContentNodes(pageKeys, draftContext).values().iterator(); i.hasNext();) {
                ContentNodeI page = i.next();

                Set<ContentKey> sectionKeys = ContentNodeUtil.getChildKeys(page);

                for (Iterator<ContentNodeI> j = service.getContentNodes(sectionKeys, draftContext).values().iterator(); j.hasNext();) {
                    ContentNodeI section = j.next();

                    Set<ContentKey> fieldKeys = ContentNodeUtil.getChildKeys(section);
                    for (Iterator<ContentNodeI> k = service.getContentNodes(fieldKeys, draftContext).values().iterator(); k.hasNext();) {
                        ContentNodeI field = k.next();

                        String fieldName = (String) field.getAttributeValue("attribute");
                        if (!names.add(fieldName)) {
                            delegate.record(editor.getKey(), "Duplicate field definition: " + fieldName);
                        }
                    }

                }
            }

            return names;
        }

    }

    @Override
    public AdminProcStatus abortStuckPublishFlows() {
        publishFlowControlService.abortStuckPublishFlows();

        return getBuildIndexStatus();
    }

}
