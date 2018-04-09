package com.freshdirect.cms.ui.editor.reports.data;

import java.util.Date;

import com.freshdirect.cms.core.domain.ContentKey;

public class HiddenProductByChange {

    private final Date changeDate;

    private final String author;

    private final String changeNote;

    private final ContentKey productKey;

    private final ContentKey primaryHomeKey;

    private final HiddenProductReason reason;

    public HiddenProductByChange(Date changeDate, String author, String changeNote, ContentKey productKey, ContentKey primaryHomeKey, HiddenProductReason reason) {
        this.changeDate = changeDate;
        this.author = author;
        this.changeNote = changeNote;
        this.productKey = productKey;
        this.primaryHomeKey = primaryHomeKey;
        this.reason = reason;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public String getAuthor() {
        return author;
    }


    public String getChangeNote() {
        return changeNote;
    }


    public ContentKey getProductKey() {
        return productKey;
    }


    public ContentKey getPrimaryHomeKey() {
        return primaryHomeKey;
    }


    public HiddenProductReason getReason() {
        return reason;
    }


    @Override
    public String toString() {
        return "{author:'"+author+"'; product:'"+productKey.id+"'; primaryHome:'"+primaryHomeKey.id+"'; reason: '"+reason.name()+"'; at:'"+changeDate.toString()+"'}";
    }
}
