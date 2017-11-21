package com.freshdirect.cms.core.domain;

/**
 * Generic content attribute type holding common properties like 'name', 'required', 'inheritable'
 */
public abstract class Attribute {

    /**
     * Value of 'name' field refers to a distinct attribute name.
     */
    final String name;

    /**
     * Flags set on attribute
     */
    final AttributeFlags flags;

    final boolean readOnly;

    Attribute(final String name, final AttributeFlags flags, boolean readOnly) {
        if (name == null) {
            throw new IllegalArgumentException("Missing name parameter");
        }

        this.name = name;
        this.flags = flags == null ? AttributeFlags.NONE : flags;

        this.readOnly = readOnly;
    }

    /**
     * Attribute name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Attribute flags
     *
     * @return
     */
    public AttributeFlags getFlags() {
        return flags;
    }

    /**
     * Returns if attribute is not allowed to be edited in CMS Editor Defaulted to false.
     *
     * @return
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Attribute other = (Attribute) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
