package com.freshdirect.cms.core.domain;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.cms.core.converter.ContentKeyJsonDeserializer;

@JsonDeserialize(using = ContentKeyJsonDeserializer.class)
public final class ContentKey implements Serializable {

    public static final String SEPARATOR = ":";

    private static final long serialVersionUID = 5252721058341157752L;

    public final ContentType type;
    public final String id;

    /**
     * Please do not use the constructor directly
     * Instead provide keys via factory
     *
     * @param type
     * @param id
     */
    ContentKey(ContentType type, String id) {
        this.type = type;
        this.id = id;
    }

    // legacy support
    public String getId() {
        return id;
    }

    // legacy support
    public ContentType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name() + SEPARATOR + id;
    }

    // legacy support
    public String getEncoded() {
        return toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        ContentKey other = (ContentKey) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }
}
