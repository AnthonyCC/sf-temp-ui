package com.freshdirect.cms.persistence.entity.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.domain.Time;
import com.freshdirect.cms.persistence.entity.AttributeEntity;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;

@Service
public class AttributeEntityToValueConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttributeEntityToValueConverter.class);

    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.date();
    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.hourMinute();

    private static final Comparator<RelationshipEntity> ORDINAL_COMPARATOR = new Comparator<RelationshipEntity>() {

        @Override
        public int compare(RelationshipEntity o1, RelationshipEntity o2) {
            CompareToBuilder compareToBuilder = new CompareToBuilder();
            compareToBuilder.append(o1.getOrdinal(), o2.getOrdinal());
            return compareToBuilder.toComparison();
        }
    };

    public Object convert(Attribute attribute, AttributeEntity entity) {
        Object retVal = null;
        final String value = entity.getValue();
        if (value == null) {
            return null;
        }
        if (attribute instanceof Scalar) {
            Scalar scalarAttribute = (Scalar) attribute;
            Class<?> scalarType = scalarAttribute.getType();
            if (scalarType.isAssignableFrom(String.class)) {
                retVal = String.valueOf(value);
            } else if (scalarType.isAssignableFrom(Boolean.class)) {
                retVal = Boolean.parseBoolean(value);
            } else if (scalarType.isAssignableFrom(Integer.class)) {
                retVal = Integer.parseInt(value);
            } else if (scalarType.isAssignableFrom(Date.class)) {
                retVal = DATE_FORMAT.parseDateTime(value).toDate();
            } else if (scalarType.isAssignableFrom(Time.class)) {
                retVal = TIME_FORMAT.parseDateTime(value).toDate();
            } else if (scalarType.isAssignableFrom(Double.class)) {
                retVal = Double.parseDouble(value);
            }
        }
        return retVal;
    }

    public Object convert(Attribute attribute, RelationshipEntity entity) {
        Object retVal = null;
        if (attribute instanceof Relationship) {
            String typeStr = entity.getRelationshipDestinationType();
            if (!"Null".equals(typeStr)) {
                retVal = ContentKeyFactory.get(ContentType.valueOf(typeStr), entity.getRelationshipDestination().split(":")[1]);
            }
        }
        return retVal;
    }

    public Object convert(Attribute attribute, List<RelationshipEntity> entities) {
        Object retVal = null;
        if (attribute instanceof Relationship) {
            List<ContentKey> relationshipTargets = new ArrayList<ContentKey>();
            Collections.sort(entities, ORDINAL_COMPARATOR);
            for (RelationshipEntity entity : entities) {
                final String destinationType = entity.getRelationshipDestinationType();

                ContentType type = null;
                try {
                    type = ContentType.valueOf(destinationType);

                    relationshipTargets.add(ContentKeyFactory.get(type, entity.getRelationshipDestination().split(":")[1]));

                } catch (IllegalArgumentException exc) {
                    LOGGER.error("Skipping undefined Content Type " + destinationType);
                }
            }
            retVal = relationshipTargets;
        }
        return retVal;
    }
}
