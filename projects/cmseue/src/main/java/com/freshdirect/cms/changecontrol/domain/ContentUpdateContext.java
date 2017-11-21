package com.freshdirect.cms.changecontrol.domain;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.draft.domain.DraftContext;
import com.google.common.base.Optional;

public final class ContentUpdateContext {

    private final String author;
    private final Date updatedAt;
    private final Optional<String> note;
    private final DraftContext draftContext;
    private final Set<ContentKey> changedKeys;

    public ContentUpdateContext(String author, Date updatedAt, String note, DraftContext draftContext, Set<ContentKey> changedKeys) {
        if (author == null) {
            throw new IllegalArgumentException("Author is mandatory");
        }
        this.author = author;
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
        this.note = Optional.fromNullable(note);
        this.draftContext = draftContext != null ? draftContext : DraftContext.MAIN;
        this.changedKeys = Collections.unmodifiableSet(changedKeys);
    }

    public String getAuthor() {
        return author;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Optional<String> getNote() {
        return note;
    }

    public DraftContext getDraftContext() {
        return draftContext;
    }

    public Set<ContentKey> getChangedKeys() {
        return changedKeys;
    }
}
