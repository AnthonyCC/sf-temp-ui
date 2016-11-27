package com.freshdirect.cms.application.draft.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.http.HttpService;

import net.sf.ehcache.config.CacheConfiguration;

public class DraftService {

    public static final String CMS_DRAFT_PATH = "/api/draft";
    public static final String CMS_DRAFT_ACTION_PATH = "/api/draft/{id}";
    public static final String CMS_DRAFT_CHANGE_PATH = "/api/draftchange";
    public static final String CMS_DRAFT_CHANGE_ACTION_PATH = "/api/draftchange/draft/{id}";
    public static final String CMS_DRAFT_CONTEXT_SESSION_NAME = "CMS_DRAFT_CONTEXT_SESSION_NAME";
    public static final String CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME = "CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME";
    public static final Long CMS_DRAFT_DEFAULT_ID = 0L;
    private static final String INVALID_DRAFT_SELECTED_ERROR_MESSAGE = "Selected draft is not valid, you will work with main branch.";
    private static final String CMS_DRAFT_CHANGES_CACHE_NAME = "cmsDraftChangesCache";

    private static final Logger LOG = LoggerFactory.getInstance(DraftService.class);

    private static final DraftService INSTANCE = new DraftService();

    public static final Comparator<DraftChange> ORDER_BY_CREATION_DATE = new Comparator<DraftChange>() {

        @Override
        public int compare(DraftChange o1, DraftChange o2) {
            return Long.valueOf(o1.getCreatedAt()).compareTo(Long.valueOf(o2.getCreatedAt()));
        }
    };

    private DraftService() {
        CacheConfiguration cacheConfiguration = populatePersonaCacheConfiguration();
        EhCacheUtil.createCache(cacheConfiguration);
    }

    public static DraftService defaultService() {
        return INSTANCE;
    }

    public DraftContext getValidDraftContext(Long draftId) {
        DraftContext draftContext = DraftContext.MAIN;
        for (Draft draft : getDrafts()) {
            if (draft.getId().equals(draftId)) {
                draftContext = new DraftContext(draft.getId(), draft.getName());
                break;
            }
        }
        return draftContext;
    }

    public List<Draft> getDrafts() {
        List<Draft> drafts;
        String uri = FDStoreProperties.getCMSAdminServiceURL() + CMS_DRAFT_PATH;
        LOG.info(MessageFormat.format("Load all drafts from uri[{0}]", uri));
        try {
            drafts = HttpService.defaultService().getList(uri, Draft.class);
        } catch (IOException e) {
            drafts = new ArrayList<Draft>();
            LOG.error(MessageFormat.format("Failed to load drafts from uri[{0}]", uri), e);
        }
        return drafts;
    }

    public void setupDraftContext(Long draftId, HttpServletRequest request) {
        DraftContext draftContext = getValidDraftContext(draftId);
        request.getSession().setAttribute(CMS_DRAFT_CONTEXT_SESSION_NAME, draftContext);
        if (!CMS_DRAFT_DEFAULT_ID.equals(draftId) && draftContext.isMainDraft()) {
            request.getSession().setAttribute(CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME, INVALID_DRAFT_SELECTED_ERROR_MESSAGE);
        }
    }

    public void updateDraftStatusForDraft(Long draftId, String status) {
        final String uri = FDStoreProperties.getCMSAdminServiceURL() + CMS_DRAFT_ACTION_PATH.replace("{id}", draftId.toString());
        LOG.info(MessageFormat.format("Updating draftStatus for draftId: {0} to {1}", draftId, status));
        try {
            status = "\"" + status + "\"";
            HttpService.defaultService().postDataWithContentTypeJson(uri, status);
            invalidateDraftChangesCache(draftId);
        } catch (IOException e) {
            LOG.error(MessageFormat.format("Failed to update draftStatus for draftId: {0}", draftId), e);
        }
    }

    public Set<ContentKey> getAllChangedContentKeys(Long draftId) {
        final Set<ContentKey> keySet = new HashSet<ContentKey>();
        for (final DraftChange change : getDraftChanges(draftId)) {
            keySet.add(ContentKey.decode(change.getContentKey()));
        }
        return keySet;
    }

    public List<DraftChange> getFilteredDraftChanges(Long draftId, long threshold, String userName) {
        List<DraftChange> filteredDraftChanges = new ArrayList<DraftChange>();
        for (DraftChange draftChange : getDraftChanges(draftId)) {
            if (threshold < draftChange.getCreatedAt() && userName.equals(draftChange.getUserName())) {
                filteredDraftChanges.add(draftChange);
            }
        }
        return filteredDraftChanges;
    }

    public List<DraftChange> getDraftChanges(Long draftId) {
        List<DraftChange> draftChanges = EhCacheUtil.getObjectFromCache(CMS_DRAFT_CHANGES_CACHE_NAME, draftId);
        if (draftChanges == null) {
            final String uri = FDStoreProperties.getCMSAdminServiceURL() + CMS_DRAFT_CHANGE_ACTION_PATH.replace("{id}", Long.toString(draftId));
            try {
                draftChanges = (HttpService.defaultService().getList(uri, DraftChange.class));
                Collections.sort(draftChanges, ORDER_BY_CREATION_DATE);
                EhCacheUtil.putObjectToCache(CMS_DRAFT_CHANGES_CACHE_NAME, draftId, draftChanges);
            } catch (IOException e) {
                LOG.error("Failed to load draft changes for draftId: " + draftId, e);
                draftChanges = Collections.emptyList();
            }
        }
        return Collections.unmodifiableList(draftChanges);
    }

    public boolean isContentKeyChanged(Long draftId, String nodeId) {
        boolean isKeyChangedOnDraft = false;
        for (final DraftChange change : getDraftChanges(draftId)) {
            if (nodeId.equals(ContentKey.decode(change.getContentKey()).getId())) {
                isKeyChangedOnDraft = true;
                break;
            }
        }
        return isKeyChangedOnDraft;
    }

    public void saveDraftChange(final Collection<DraftChange> draftChanges) {
        final String uri = FDStoreProperties.getCMSAdminServiceURL() + CMS_DRAFT_CHANGE_PATH;

        if (draftChanges != null && !draftChanges.isEmpty()) {
            try {
                HttpService.defaultService().postList(uri, draftChanges);
                updateDraftChangesCache(draftChanges);
            } catch (IOException e) {
                LOG.error("Failed to send draft changes", e);
            }
        }
    }

    public void invalidateDraftChangesCache(Long draftId) {
        EhCacheUtil.removeFromCache(CMS_DRAFT_CHANGES_CACHE_NAME, draftId);
    }

    private void updateDraftChangesCache(Collection<DraftChange> draftChanges) {
        for (DraftChange draftChange : draftChanges) {
            Long draftId = draftChange.getDraft().getId();
            List<DraftChange> cachedDraftChanges = EhCacheUtil.getObjectFromCache(CMS_DRAFT_CHANGES_CACHE_NAME, draftId);
            if (cachedDraftChanges == null) {
                List<DraftChange> changesOfNewDraft = new ArrayList<DraftChange>();
                changesOfNewDraft.add(draftChange);
                EhCacheUtil.putObjectToCache(CMS_DRAFT_CHANGES_CACHE_NAME, draftId, changesOfNewDraft);
            } else {
                synchronized (cachedDraftChanges) {
                    cachedDraftChanges.add(draftChange);
                    Collections.sort(cachedDraftChanges, ORDER_BY_CREATION_DATE);
                }
            }
        }
    }

    private CacheConfiguration populatePersonaCacheConfiguration() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CMS_DRAFT_CHANGES_CACHE_NAME, 20);
        cacheConfiguration.setTimeToLiveSeconds(300l);
        return cacheConfiguration;
    }

    public String decorateUrlWithDraft(String baseUrl, String draftId, String draftName) {
        String currentUrl;
        try {
            if (draftId != null && draftName != null) {
                currentUrl = decorateUrlWithDraft(baseUrl, Long.parseLong(draftId), draftName);
            } else {
                currentUrl = baseUrl;
            }
        } catch (NumberFormatException e) {
            currentUrl = baseUrl;
        }
        return currentUrl;
    }

    public String decorateUrlWithDraft(String baseUrl, Long draftId, String draftName) {
        final StringBuilder redirectUrl = new StringBuilder(baseUrl);
        if (baseUrl != null && !baseUrl.isEmpty() && draftId != null && draftName != null) {
            if (!baseUrl.contains("?")) {
                redirectUrl.append("?");
            }
            redirectUrl.append("&draftId=").append(safeURLEncode(draftId.toString())).append("&draftName=").append(safeURLEncode(draftName));
        }
        return redirectUrl.toString();
    }

    public DraftContext getDraftContext(String draftId, String draftName) {
        Long parseDraftId = DraftService.defaultService().parseDraftId(draftId);
        return getDraftContext(parseDraftId, draftName);
    }

    public DraftContext getDraftContext(Long draftId, String draftName) {
        DraftContext currentDraftContext;
        if (draftId != null && draftName != null && DraftContext.MAIN_DRAFT_ID != draftId) {
            currentDraftContext = new DraftContext(draftId, draftName);
        } else {
            currentDraftContext = DraftContext.MAIN;
        }
        return currentDraftContext;
    }

    public Long parseDraftId(String draftId) {
        Long parsedDraftId = null;
        if (draftId != null) {
            try {
                parsedDraftId = Long.parseLong(draftId);
            } catch (NumberFormatException e) {
                LOG.error("Error parsing draftId " + draftId, e);
            }
        }
        return parsedDraftId;
    }

    private String safeURLEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
