package com.freshdirect.cms.ui.editor.reports.data;

import java.util.Date;

import com.freshdirect.cms.core.domain.ContentKey;

public class HiddenProductByChange {

    private final Date changeDate;

    private final String changedBy;

    private final String changeNote;

    private final ContentKey productKey;

    private final ContentKey primaryHomeKey;

    private final HiddenProductReason reason;

    public HiddenProductByChange(Date changeDate, String changedBy, String changeNote, ContentKey productKey, ContentKey primaryHomeKey, HiddenProductReason reason) {
        this.changeDate = changeDate;
        this.changedBy = changedBy;
        this.changeNote = changeNote;
        this.productKey = productKey;
        this.primaryHomeKey = primaryHomeKey;
        this.reason = reason;
    }


    public Date getChangeDate() {
        return changeDate;
    }


    public String getChangedBy() {
        return changedBy;
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


}
