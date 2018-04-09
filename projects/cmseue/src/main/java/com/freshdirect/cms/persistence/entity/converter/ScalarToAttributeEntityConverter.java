package com.freshdirect.cms.persistence.entity.converter;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.persistence.entity.AttributeEntity;

@Service
public class ScalarToAttributeEntityConverter {

    public AttributeEntity convert(ContentKey contentKey, Scalar scalar, Object value) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setContentKey(contentKey.toString());
        attributeEntity.setContentType(contentKey.type.toString());
        attributeEntity.setName(scalar.getName());
        attributeEntity.setOrdinal(0);
        attributeEntity.setValue(ScalarValueConverter.serializeToString(scalar, value));
        return attributeEntity;
    }
}
