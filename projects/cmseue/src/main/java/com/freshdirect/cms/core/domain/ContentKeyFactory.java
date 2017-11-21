package com.freshdirect.cms.core.domain;

import org.springframework.util.Assert;

public final class ContentKeyFactory {

    private ContentKeyFactory() {
    }

    /**
     * The default way to provide ContentKey based on type and ID Method may throw {@link IllegalArgumentException} if type does not exist or ID is invalid
     *
     * @param type
     *            ContentType
     * @param id
     *            the ID part of key
     *
     * @return content key
     */
    public static ContentKey get(ContentType type, String id) {
        Assert.notNull(type, "ContentType cannot be null");
        Assert.notNull(id, "ID cannot be null");

        return new ContentKey(type, id);
    }

    public static ContentKey get(String type, String id) {
        return get(ContentType.valueOf(type), id);
    }

    public static ContentKey get(String contentKey) {
        Assert.notNull(contentKey, "Content Key cannot be null");

        String typePart = contentKey.substring(0, contentKey.indexOf(ContentKey.SEPARATOR));
        String idPart = contentKey.substring(contentKey.indexOf(ContentKey.SEPARATOR) + 1, contentKey.length());

        return get(typePart, idPart);
    }

}
