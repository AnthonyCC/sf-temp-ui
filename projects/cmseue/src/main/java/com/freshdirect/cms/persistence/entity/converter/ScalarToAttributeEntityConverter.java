package com.freshdirect.cms.persistence.entity.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.converter.ScalarValueToSerializedValueConverter;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.persistence.entity.AttributeEntity;

@Service
public class ScalarToAttributeEntityConverter {

    @Autowired
    private ScalarValueToSerializedValueConverter scalarSerializer;

    public AttributeEntity convert(ContentKey contentKey, Scalar scalar, Object value) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setContentKey(contentKey.toString());
        attributeEntity.setContentType(contentKey.type.toString());
        attributeEntity.setName(scalar.getName());
        attributeEntity.setOrdinal(0);
        return convert(contentKey, scalar, value, attributeEntity);
    }

    public AttributeEntity convert(ContentKey contentKey, Scalar scalar, Object value, AttributeEntity attributeEntity) {
        attributeEntity.setValue(scalarSerializer.convert(scalar, value));
        return attributeEntity;
    }
}
