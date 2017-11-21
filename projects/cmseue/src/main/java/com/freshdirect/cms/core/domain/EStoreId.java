package com.freshdirect.cms.core.domain;

public enum EStoreId {

    FD("FreshDirect"),
    FDX("FDX");

    /**
     * CMS Content ID
     */
    private final String contentId;

    private EStoreId(String contentId) {
        this.contentId = contentId;
    }

    /**
     * @return the ID part of the corresponding CMS content key
     *
     * @see ContentType#Store
     * @see ContentKey
     */
    public String getContentId() {
        return contentId;
    }

    public static EStoreId valueOfContentId(String contentId) {
        if (contentId != null) {
            for (EStoreId value : EStoreId.values()) {
                if (contentId.equals(value.getContentId())) {
                    return value;
                }
            }
        }
        throw new IllegalArgumentException("Cannot resolve EStoreId with contentKey: " + contentId);
    }
}
