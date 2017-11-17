package com.freshdirect.cms.mediaassociation.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.changecontrol.domain.ContentUpdateContext;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.mediaassociation.domain.MediaAssociationRule;
import com.google.common.base.Optional;

@Service
public class MediaAssociatorService {

    private static final String CHUNK_SEPARATOR = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String PATH_SEPARATOR = "/";

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private MediaAssociationRuleService ruleService;

    public boolean canAssociateMedia(Media media) {
        Assert.notNull(media, "Missing media item");
        Assert.isTrue(Media.isMediaType(media.getContentKey()), "Not media type");

        if (ContentType.MediaFolder == media.getContentKey().type) {
            return false;
        }

        Map<ContentKey, Attribute> mediaAssociation = getAssociation(media.getUri());
        return mediaAssociation != null;
    }

    public void autoAssociateMedia(Media media, String userId) {
        Map<ContentKey, Attribute> mediaAssociation = getAssociation(media.getUri());
        if (mediaAssociation != null) {
            LinkedHashMap<ContentKey, Map<Attribute, Object>> contentToUpdate = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();
            Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
            for (Attribute attribute : mediaAssociation.values()) {
                attributes.put(attribute, media.getContentKey());
            }
            contentToUpdate.put(mediaAssociation.keySet().iterator().next(), attributes);
            DraftContext originalContext = draftContextHolder.getDraftContext();
            draftContextHolder.setDraftContext(DraftContext.MAIN);
            contentProviderService.updateContent(contentToUpdate, new ContentUpdateContext(userId, null, "Media auto association", DraftContext.MAIN, contentToUpdate.keySet()));
            draftContextHolder.setDraftContext(originalContext);
        }
    }

    private Map<ContentKey, Attribute> getAssociation(String uri) {
        Map<ContentKey, Attribute> mediaAssociation = null;

        String fullName = uri.substring(uri.lastIndexOf(PATH_SEPARATOR) + 1, uri.length());
        int extensionSeparatorPosition = fullName.lastIndexOf(EXTENSION_SEPARATOR);

        if (extensionSeparatorPosition != -1) {
            String name = fullName.substring(0, extensionSeparatorPosition);

            String prefix = null;
            String suffix = null;
            String id = "";

            StringTokenizer stringTokenizer = new StringTokenizer(name, CHUNK_SEPARATOR, true);
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                boolean isTokenChunkSeparator = CHUNK_SEPARATOR.equals(token);
                if (prefix == null && !isTokenChunkSeparator && stringTokenizer.hasMoreTokens() && !ruleService.getRulesByPrefix(token).isEmpty()) {
                    prefix = token;
                    id += token;
                } else if (!"".equals(id) && !isTokenChunkSeparator && !stringTokenizer.hasMoreTokens()) {
                    suffix = token;
                } else {
                    id += token;
                }
            }
            id = StringUtils.strip(id, CHUNK_SEPARATOR);

            if (id != null && suffix != null) {
                Optional<MediaAssociationRule> associationRule = ruleService.getRuleByPrefixAndSuffix(prefix, suffix);
                if (associationRule.isPresent()) {
                    MediaAssociationRule rule = associationRule.get();
                    mediaAssociation = new HashMap<ContentKey, Attribute>();
                    mediaAssociation.put(ContentKeyFactory.get(rule.getContentType(), id), rule.getRelationship());
                }
            } else {
                mediaAssociation = null;
            }
        }

        return mediaAssociation;
    }
}
