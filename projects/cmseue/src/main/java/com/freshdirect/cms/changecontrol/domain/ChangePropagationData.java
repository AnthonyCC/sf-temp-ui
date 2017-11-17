package com.freshdirect.cms.changecontrol.domain;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.draft.domain.DraftContext;

public class ChangePropagationData implements Serializable {

    private static final long serialVersionUID = -9132426529230913123L;

    private Set<ContentKey> contentKeys;
    private DraftContext draftContext;

    public ChangePropagationData() {
    }

    public ChangePropagationData(Set<ContentKey> contentKeys, DraftContext draftContext) {
        this.contentKeys = contentKeys;
        this.draftContext = draftContext;
    }

    public Set<ContentKey> getContentKeys() {
        return contentKeys;
    }

    public void setContentKeys(Set<ContentKey> contentKeys) {
        this.contentKeys = contentKeys;
    }

    public DraftContext getDraftContext() {
        return draftContext;
    }

    public void setDraftContext(DraftContext draftContext) {
        this.draftContext = draftContext;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{[ContentKeys = ").append(StringUtils.join(contentKeys, ", ")).append("], ").append("[DraftContext= ").append(draftContext).append("]}");
        return stringBuilder.toString();
    }

}
