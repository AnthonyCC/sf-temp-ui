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
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.search.BackgroundStatus;
import com.freshdirect.cms.search.IBackgroundProcessor;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService {
	private static final long serialVersionUID = 1263043539819341529L;

	final static Logger LOG = LoggerFactory.getInstance(AdminServiceImpl.class);
    
    ExecutorService executor = Executors.newSingleThreadExecutor();

    

    @Override
    public AdminProcStatus getBuildIndexStatus() {
        
        IBackgroundProcessor search = getAdminSearch();
        BackgroundStatus status = search.getStatus();
        return createAdminProcStatus(status);
    }

    /**
     * @param status
     * @return
     */
    AdminProcStatus createAdminProcStatus(BackgroundStatus status) {
        AdminProcStatus visibleStatus = new AdminProcStatus(status.getCurrent(), status.getLastReindexResult(), status.isRunning(), status.getStarted(), status.isRunning() ? System.currentTimeMillis() - status.getStarted() : 0);
        return visibleStatus;
    }

    @Override
    public AdminProcStatus rebuildIndexes() {
        
        synchronized (AdminServiceImpl.class) {
            IBackgroundProcessor tool = getAdminSearch();
            BackgroundStatus status = tool.getStatus();
            
            LOG.info("rebuild index called ("+status.isRunning()+")");
            if (!status.isRunning()) {
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
            LOG.info("rebuild index called ("+status.isRunning()+")");
            if (!status.isRunning()) {
                tool.rebuildWineIndex();
            }

        }
        return getBuildIndexStatus();
    }

    /**
     * @return
     */
    IBackgroundProcessor getAdminSearch() {
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
        CmsManager instance = CmsManager.getInstance();
        Set<ContentKey> editorKeys = instance.getContentKeysByType(ContentType.get("CmsEditor"));
        Map<ContentKey, ContentNodeI> nodes = instance.getContentNodes(editorKeys);
        ContentValidationDelegate delegate = new ContentValidationDelegate();
        ContentValidatorI validator = new CmsFormValidator();
        for (Iterator<ContentNodeI> i = nodes.values().iterator(); i.hasNext();) {
            ContentNodeI e = i.next();
            validator.validate(delegate, instance, e, null, null);
        }
        LOG.warn("editor validation:"+delegate);
    }

    /**
     * TODO: this is copied from com.freshdirect.cms.ui.tapestry.page.Admin
     * 
     * @author zsombor
     * 
     */
    private static class CmsFormValidator implements ContentValidatorI {

        @Override
        public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {
            ContentType type = ContentType.get(node.getAttributeValue("contentType").toString());
            ContentTypeDefI def = service.getTypeService().getContentTypeDefinition(type);

            Set<String> editorFields = collectFieldNames(delegate, service, node);
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

        private Set<String> collectFieldNames(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI editor) {
            Set<String> names = new HashSet<String>();

            Set<ContentKey> pageKeys = ContentNodeUtil.getChildKeys(editor);

            for (Iterator<ContentNodeI> i = service.getContentNodes(pageKeys).values().iterator(); i.hasNext();) {
                ContentNodeI page = i.next();

                Set<ContentKey> sectionKeys = ContentNodeUtil.getChildKeys(page);

                for (Iterator<ContentNodeI> j = service.getContentNodes(sectionKeys).values().iterator(); j.hasNext();) {
                    ContentNodeI section = j.next();

                    Set<ContentKey> fieldKeys = ContentNodeUtil.getChildKeys(section);
                    for (Iterator<ContentNodeI> k = service.getContentNodes(fieldKeys).values().iterator(); k.hasNext();) {
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

}
