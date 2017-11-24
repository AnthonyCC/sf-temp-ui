package com.freshdirect.cms.changecontrol.domain;

import java.util.Set;

import org.springframework.context.ApplicationEvent;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.draft.domain.DraftContext;

public class ContentChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5871150335852229128L;

    private final DraftContext draftContext;
    private final Set<ContentKey> contentKeys;

    public ContentChangedEvent(Object source, DraftContext draftContext, Set<ContentKey> contentKeys) {
        super(source);
        this.draftContext = draftContext;
        this.contentKeys = contentKeys;
    }

    public DraftContext getDraftContext() {
        return draftContext;
    }

    public Set<ContentKey> getContentKeys() {
        return contentKeys;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ContentChangedEvent [source = ").append(getSource()).append(", draftContext = ").append(getDraftContext()).append(", contentKeys = ").append(getContentKeys()).append("]");
        return stringBuilder.toString();
    }
}
