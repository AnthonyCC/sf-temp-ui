package com.freshdirect.cms.draft.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.draft.domain.Draft;
import com.freshdirect.cms.draft.domain.DraftChange;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.domain.DraftStatus;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
public class DraftService {

    public static final String CMS_DRAFT_PATH = "/api/draft";
    public static final String CMS_DRAFT_ACTION_PATH = "/api/draft/{id}";
    public static final String CMS_DRAFT_CHANGE_PATH = "/api/draftchange";
    public static final String CMS_DRAFT_CHANGE_ACTION_PATH = "/api/draftchange/draft/{id}";
    public static final String CMS_DRAFT_CONTEXT_SESSION_NAME = "CMS_DRAFT_CONTEXT_SESSION_NAME_NEW";
    public static final String CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME = "CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME";
    public static final Long CMS_DRAFT_DEFAULT_ID = 0L;

    public static final Comparator<DraftChange> ORDER_BY_CREATION_DATE_ASC = new Comparator<DraftChange>() {

        @Override
        public int compare(DraftChange o1, DraftChange o2) {
            return Long.valueOf(o1.getCreatedAt()).compareTo(Long.valueOf(o2.getCreatedAt()));
        }
    };

    public static final Comparator<DraftChange> ORDER_BY_CREATION_DATE_DESC = new Comparator<DraftChange>() {

        @Override
        public int compare(DraftChange o1, DraftChange o2) {
            return Long.valueOf(o2.getCreatedAt()).compareTo(Long.valueOf(o1.getCreatedAt()));
        }
    };

    private static final String INVALID_DRAFT_SELECTED_ERROR_MESSAGE = "Selected draft is not valid, you will work with main branch.";
    private static final String CMS_DRAFT_CHANGES_CACHE_NAME = "cmsDraftChangesCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftService.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private DraftChangeToContentNodeApplicator applicator;

    @Value("${cms.adminapp.path}")
    private String cmsAdminAppUri;

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
        String uri = cmsAdminAppUri + CMS_DRAFT_PATH;
        LOGGER.info(MessageFormat.format("Load all drafts from uri[{0}]", uri));
        RestTemplate getDraftsRestTemplate = new RestTemplate();
        drafts = Arrays.asList(getDraftsRestTemplate.getForObject(uri, Draft[].class));
        return drafts;
    }

    public void setupDraftContext(Long draftId, HttpServletRequest request) {
        DraftContext draftContext = getValidDraftContext(draftId);
        request.getSession().setAttribute(CMS_DRAFT_CONTEXT_SESSION_NAME, draftContext);
        LOGGER.debug("Received draft context: " + draftContext);
        if (!CMS_DRAFT_DEFAULT_ID.equals(draftId) && draftContext.isMainDraft()) {
            request.getSession().setAttribute(CMS_DRAFT_LOGIN_ERROR_MESSAGE_SESSION_NAME, INVALID_DRAFT_SELECTED_ERROR_MESSAGE);
        }
    }

    public void updateDraftStatusForDraft(Long draftId, DraftStatus status) {
        final String uri = cmsAdminAppUri + CMS_DRAFT_ACTION_PATH.replace("{id}", draftId.toString());
        LOGGER.info(MessageFormat.format("Updating draftStatus for draftId: {0} to {1}", draftId, status));
        RestTemplate updateDraftStatusTemplate = new RestTemplate();
        updateDraftStatusTemplate.postForLocation(uri, status, draftId.toString());
        invalidateDraftChangesCache(draftId);
    }

    public Set<ContentKey> getAllChangedContentKeys(Long draftId) {
        final Set<ContentKey> keySet = new HashSet<ContentKey>();
        for (final DraftChange change : getDraftChanges(draftId)) {
            keySet.add(ContentKeyFactory.get(change.getContentKey()));
        }
        return keySet;
    }

    public Set<ContentKey> collectChildKeys(Long draftId) {
        final Set<ContentKey> keySet = new HashSet<ContentKey>();
        for (final DraftChange change : getDraftChanges(draftId)) {
            ContentKey draftContentKey = ContentKeyFactory.get(change.getContentKey());

            Attribute attr = contentTypeInfoService.findAttributeByName(draftContentKey.type, change.getAttributeName()).orNull();

            if (attr instanceof Relationship) {
                final Relationship relationship = (Relationship) attr;
                List<ContentKey> clientKeys = applicator.getContentKeysFromRelationshipValue( relationship, change.getValue());

                keySet.addAll(clientKeys);
            }
        }
        return keySet;
    }

    public List<DraftChange> getFilteredDraftChanges(Long draftId, Date changedSince, final String userName, Set<ContentKey> contentKeys) {
        List<DraftChange> filteredDraftChanges = new ArrayList<DraftChange>();
        List<DraftChange> draftChanges = new ArrayList<DraftChange>(getDraftChanges(draftId));

        final Long adjustedTreshold = changedSince.getTime() - 3000L;

        // sort draft changes in descending order by date
        Collections.sort(draftChanges, ORDER_BY_CREATION_DATE_DESC);

        for (DraftChange draftChange : draftChanges) {
            // adjusted threshold reached => quit the loop
            if (Long.valueOf(draftChange.getCreatedAt()).compareTo(adjustedTreshold) < 0) {
                break;
            }

            // skip other authors and content keys
            if (!userName.equals(draftChange.getUserName()) || !contentKeys.contains(ContentKeyFactory.get(draftChange.getContentKey()))) {
                continue;
            }

            filteredDraftChanges.add(draftChange);
        }

        return filteredDraftChanges;
    }

    @SuppressWarnings("unchecked")
    public List<DraftChange> getDraftChanges(Long draftId) {
        List<DraftChange> draftChanges = (List<DraftChange>) (cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).get(draftId) == null ? null
                : cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).get(draftId).getObjectValue());
        if (draftChanges == null) {
            final String uri = cmsAdminAppUri + CMS_DRAFT_CHANGE_ACTION_PATH;
            RestTemplate getDraftChangesRestTemplate = new RestTemplate();
            DraftChange[] allDraftChange = getDraftChangesRestTemplate.getForObject(uri, DraftChange[].class, Long.toString(draftId));
            draftChanges = new ArrayList<DraftChange>();
            for (DraftChange draftChange : allDraftChange) {
                draftChanges.add(draftChange);
            }
            Collections.sort(draftChanges, ORDER_BY_CREATION_DATE_ASC);
            cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).put(new Element(draftId, draftChanges));
        }
        return Collections.unmodifiableList(draftChanges);
    }

    public boolean isContentKeyChanged(Long draftId, String nodeId) {
        boolean isKeyChangedOnDraft = false;
        for (final DraftChange change : getDraftChanges(draftId)) {
            if (nodeId.equals(ContentKeyFactory.get(change.getContentKey()).id)) {
                isKeyChangedOnDraft = true;
                break;
            }
        }
        return isKeyChangedOnDraft;
    }

    public boolean isContentKeyChanged(Long draftId, ContentKey contentKey) {
        boolean isKeyChangedOnDraft = false;
        for (final DraftChange change : getDraftChanges(draftId)) {
            if (contentKey.equals(ContentKeyFactory.get(change.getContentKey()))) {
                isKeyChangedOnDraft = true;
                break;
            }
        }
        return isKeyChangedOnDraft;
    }

    public void saveDraftChange(final Collection<DraftChange> draftChanges) {
        final String uri = cmsAdminAppUri + CMS_DRAFT_CHANGE_PATH;

        if (draftChanges != null && !draftChanges.isEmpty()) {
            RestTemplate saveDraftChangeRestTemplate = new RestTemplate();
            DraftChange[] savedDraftChanges = saveDraftChangeRestTemplate.postForObject(uri, draftChanges, DraftChange[].class);
            Draft draftOfChanges = draftChanges.iterator().next().getDraft();
            List<DraftChange> cachedDraftChanges = getDraftChanges(draftOfChanges.getId());
            List<DraftChange> notCachedDraftChanges = new ArrayList<DraftChange>();
            for (DraftChange draftChange : savedDraftChanges) {
                if (draftChange.getDraft().equals(draftOfChanges)) {
                    if (cachedDraftChanges == null || !cachedDraftChanges.contains(draftChange)) {
                        notCachedDraftChanges.add(draftChange);
                    }
                }
            }
            updateDraftChangesCache(notCachedDraftChanges);
        }
    }

    public void invalidateDraftChangesCache(Long draftId) {
        cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).remove(draftId);
    }

    @SuppressWarnings("unchecked")
    private void updateDraftChangesCache(Collection<DraftChange> draftChanges) {
        for (DraftChange draftChange : draftChanges) {
            Long draftId = draftChange.getDraft().getId();
            List<DraftChange> cachedDraftChanges = (List<DraftChange>) cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).get(draftId).getObjectValue();
            if (cachedDraftChanges == null) {
                List<DraftChange> changesOfNewDraft = new ArrayList<DraftChange>();
                changesOfNewDraft.add(draftChange);
                cacheManager.getCache(CMS_DRAFT_CHANGES_CACHE_NAME).put(new Element(draftId, changesOfNewDraft));
            } else {
                synchronized (cachedDraftChanges) {
                    cachedDraftChanges.add(draftChange);
                    Collections.sort(cachedDraftChanges, ORDER_BY_CREATION_DATE_ASC);
                }
            }
        }
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
        if (baseUrl != null && !baseUrl.isEmpty()) {
            final StringBuilder redirectUrl = new StringBuilder(baseUrl);
            if (draftId != null && draftName != null) {
                if (!baseUrl.contains("?")) {
                    redirectUrl.append("?");
                }
                redirectUrl.append("&draftId=").append(safeURLEncode(draftId.toString())).append("&draftName=").append(safeURLEncode(draftName));
            }
            return redirectUrl.toString();
        } else {
            return "";
        }
    }

    public DraftContext getDraftContext(String draftId, String draftName) {
        Long parseDraftId = parseDraftId(draftId);
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
                LOGGER.error("Error parsing draftId " + draftId, e);
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
