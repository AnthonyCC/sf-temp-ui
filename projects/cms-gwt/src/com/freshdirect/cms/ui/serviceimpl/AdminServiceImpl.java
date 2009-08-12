package com.freshdirect.cms.ui.serviceimpl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.SynonymDictionary;
import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.service.AdminService;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.conf.FDRegistry;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdminServiceImpl extends RemoteServiceServlet implements AdminService {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    AdminProcStatus status = new AdminProcStatus();

    @Override
    public AdminProcStatus getBuildIndexStatus() {
        status.setElapsedTime(status.isRunning() ? System.currentTimeMillis() - status.getStarted() : 0);
        return status;
    }

    @Override
    public AdminProcStatus rebuildIndexes() {
        synchronized (AdminServiceImpl.class) {
            if (!status.isRunning()) {
                status.setRunning(true);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        rebuildIndexImpl();

                        status.setRunning(false);
                    }
                });
            }

        }
        return status;
    }

    private void rebuildIndexImpl() {
        long time = System.currentTimeMillis();
        status.setStarted(time);
        ContentSearchServiceI searchService = (ContentSearchServiceI) FDRegistry.getInstance().getService(ContentSearchServiceI.class);

        Set<ContentKey> keys = new HashSet<ContentKey>();
        CmsManager instance = CmsManager.getInstance();
        for (Iterator<ContentType> i = searchService.getIndexedTypes().iterator(); i.hasNext();) {
            ContentType type = (ContentType) i.next();
            keys.addAll(instance.getContentKeysByType(type));
            status.setCurrent("loading " + keys.size() + " keys");
        }

        status.setCurrent("loading " + keys.size() + " nodes");
        Map nodes = instance.getContentNodes(keys);
        status.setCurrent("setting up synonym dictionary");
        searchService.setDictionary(SynonymDictionary.createFromCms());
        status.setCurrent("indexing " + nodes.values().size() + " nodes");
        searchService.index(nodes.values());
        status.setCurrent("refreshing relevancy scores. ");

        ContentSearch.getInstance().refreshRelevencyScores();
        long elapsed = System.currentTimeMillis() - time;
        status.setCurrent("finished in " + (elapsed / 1000) + " sec");
        status.setLastReindexResult("indexed " + nodes.values().size() + " nodes in " + (elapsed / 1000) + " sec");
    }

    @Override
    public AdminProcStatus validateEditors() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                validateEditorsImpl();
            }
        });
        return status;
    }

    private void validateEditorsImpl() {
        CmsManager instance = CmsManager.getInstance();
        Set editorKeys = instance.getContentKeysByType(ContentType.get("CmsEditor"));
        Map nodes = instance.getContentNodes(editorKeys);
        ContentValidationDelegate delegate = new ContentValidationDelegate();
        ContentValidatorI validator = new CmsFormValidator();
        for (Iterator i = nodes.values().iterator(); i.hasNext();) {
            ContentNodeI e = (ContentNodeI) i.next();
            validator.validate(delegate, instance, e, null);
        }
        System.err.println(delegate);
    }

    /**
     * TODO: this is copied from com.freshdirect.cms.ui.tapestry.page.Admin
     * 
     * @author zsombor
     * 
     */
    private static class CmsFormValidator implements ContentValidatorI {

        public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
            ContentType type = ContentType.get(node.getAttribute("contentType").getValue().toString());
            ContentTypeDefI def = service.getTypeService().getContentTypeDefinition(type);

            Set editorFields = collectFieldNames(delegate, service, node);
            Set attributes = def.getAttributeNames();

            Set extra = new HashSet(editorFields);
            extra.removeAll(attributes);
            if (!extra.isEmpty()) {
                delegate.record(node.getKey(), "Extraneous fields: " + extra);
            }

            Set missing = new HashSet(attributes);
            missing.removeAll(editorFields);
            if (!missing.isEmpty()) {
                delegate.record(node.getKey(), "Missing fields: " + missing);
            }
        }

        private Set collectFieldNames(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI editor) {
            Set<String> names = new HashSet<String>();

            Set<ContentKey> pageKeys = ContentNodeUtil.getChildKeys(editor);

            for (Iterator<ContentNodeI> i = service.getContentNodes(pageKeys).values().iterator(); i.hasNext();) {
                ContentNodeI page = i.next();

                Set<ContentKey> sectionKeys = ContentNodeUtil.getChildKeys(page);

                for (Iterator<ContentNodeI> j = service.getContentNodes(sectionKeys).values().iterator(); j.hasNext();) {
                    ContentNodeI section = (ContentNodeI) j.next();

                    Set<ContentKey> fieldKeys = ContentNodeUtil.getChildKeys(section);
                    for (Iterator<ContentNodeI> k = service.getContentNodes(fieldKeys).values().iterator(); k.hasNext();) {
                        ContentNodeI field = (ContentNodeI) k.next();

                        String fieldName = (String) field.getAttribute("attribute").getValue();
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
