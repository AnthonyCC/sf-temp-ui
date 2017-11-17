package com.freshdirect.cms.core.domain;

import java.util.Arrays;
import java.util.Date;

/**
 * This type represents scalar value definiton
 * of content node.
 *
 * @author segabor
 *
 */
public final class Scalar extends Attribute {

    private static final Class<?>[] SUPPORTED_TYPES = {String.class, Boolean.class, Integer.class, Double.class, Date.class, Time.class};
    private static final Class<?>[] SUPPORTED_ENUM_TYPES = {String.class, Integer.class};

    /**
     * Defines the value type of attribute
     *
     * Supported types are
     * - {@link java.lang.String}
     * - {@link java.lang.Integer}
     * - {@link java.lang.Double}
     * - {@link java.lang.Boolean}
     * - {@link java.lang.Date}
     *
     * Enumerated scalars support only {@link java.lang.String}
     * and {@link java.lang.Integer} types.
     *
     */
    private final Class<?> type;

    /**
     * Set of preset values an attribute can contain.
     * Scalar is regarded enumerated if this field is populated.
     */
    private final Object[] enumeratedValues;

    /**
     * Construct scalar without enumerated values.
     * So attribute can have arbitrary value.
     *
     * @param name Attribute name.
     * @param flags Attribute flags.
     * @param type Value type.
     */
    public Scalar(final String name, final AttributeFlags flags, boolean readOnly, final Class<?> type) {
        this(name, flags, readOnly, type, null);
    }

    /**
     * Constructs scalar with enumerated values.
     *
     * @param name Scalar attribute name
     * @param flags Scalar attribute flags
     * @param type Scalar value type
     * @param enumeratedValues Optional set of values. If set, scalar value can only be one of them.
     */
    public Scalar(final String name, final AttributeFlags flags, boolean readOnly, final Class<?> type, Object[] enumeratedValues) {
        super(name, flags, readOnly);

        final boolean isEnum = enumeratedValues != null;

        // check if specified type is supported
        if (!checkType(type, isEnum ? SUPPORTED_ENUM_TYPES : SUPPORTED_TYPES)) {
            if (type == null) {
                throw new IllegalArgumentException("Scalar type is null!");
            } else {
                throw new IllegalArgumentException("Unsupported scalar type " + type.getName());
            }
        }

        // additional check
        if (isEnum) {
            for (Object value : enumeratedValues) {
                if (value == null) {
                    throw new IllegalArgumentException("Found null enumerated value!");
                } else if (!type.equals(value.getClass())) {
                    throw new IllegalArgumentException("Type of enumerated value must match scalar type!");
                }
            }
        }

        this.type = type;
        this.enumeratedValues = enumeratedValues;
    }

    /**
     * Utility method that checks if type is valid
     *
     * @param candidateType Candidate type
     * @param supportedTypes Set of supported types
     * @return
     */
    private static boolean checkType(Class<?> candidateType, Class<?>[] supportedTypes) {
        if (candidateType == null || supportedTypes == null) {
            return false;
        }

        for (int i = 0; i < supportedTypes.length; i++) {
            if (candidateType.equals(supportedTypes[i])) {
                return true;
            }
        }

        return false;

    }

    /**
     * Return value type of scalar attribute
     * @return
     */
    public Class<?> getType() {
        return type;
    }


    /**
     * When set, value of this attribute can only one of these.
     * @return
     */
    public Object[] getEnumeratedValues() {
        return enumeratedValues;
    }

    /**
     * Returns true if scalar is enumerated
     * @return
     */
    public boolean isEnumerated() {
        return enumeratedValues != null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Scalar [type=" + type.getSimpleName()
            + ", enumeratedValues=" + Arrays.toString(enumeratedValues)
            + ", name=" + name
            + ", flags=" + flags
            + ", readOnly=" + readOnly + "]";
    }
}
