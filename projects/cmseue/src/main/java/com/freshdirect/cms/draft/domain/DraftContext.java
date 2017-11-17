package com.freshdirect.cms.draft.domain;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.cms.draft.converter.DraftContextJsonDeserializer;

/**
 * Simple context class to record currently used draft for a user
 *
 * @author segabor
 */
@JsonDeserialize(using = DraftContextJsonDeserializer.class)
public final class DraftContext implements Serializable {

    public static final long MAIN_DRAFT_ID = 0xDEADFACECAFEBABEL;
    public static final String MAIN_DRAFT_NAME = "MAIN";

    public static final DraftContext MAIN = new DraftContext(MAIN_DRAFT_ID, MAIN_DRAFT_NAME);

    private static final long serialVersionUID = 557670715398054124L;

    /**
     * Draft Identifier
     */
    private long draftId;

    /**
     * Descriptive name of draft
     */
    private String draftName;

    public DraftContext() {
        this.draftId = MAIN_DRAFT_ID;
        this.draftName = MAIN_DRAFT_NAME;
    }

    public DraftContext(long draftId, String draftName) {
        this.draftId = draftId;
        this.draftName = draftName;
    }

    public String getDraftName() {
        return draftName;
    }

    public void setDraftName(String draftName) {
        this.draftName = draftName;
    }

    public long getDraftId() {
        return draftId;
    }

    public void setDraftId(long draftId) {
        this.draftId = draftId;
    }

    public boolean isMainDraft() {
        return MAIN_DRAFT_ID == draftId && MAIN_DRAFT_NAME.equals(draftName);
    }

    @Override
    public String toString() {
        return "id=" + draftId + "; name='" + draftName + "'";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (draftId ^ (draftId >>> 32));
        result = prime * result + ((draftName == null) ? 0 : draftName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DraftContext other = (DraftContext) obj;
        if (draftId != other.draftId) {
            return false;
        }
        if (draftName == null) {
            if (other.draftName != null) {
                return false;
            }
        } else if (!draftName.equals(other.draftName)) {
            return false;
        }
        return true;
    }
}
