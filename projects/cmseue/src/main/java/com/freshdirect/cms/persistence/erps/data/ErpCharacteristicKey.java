package com.freshdirect.cms.persistence.erps.data;

import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;

public class ErpCharacteristicKey {

    private String sapId;
    private String characteristicName;

    public ErpCharacteristicKey(ContentKey contentKey) {
        Assert.notNull(contentKey);
        Assert.isTrue(ContentType.ErpCharacteristic == contentKey.type);

        final String[] compositeId = contentKey.id.split("/");
        this.sapId = compositeId[0];
        this.characteristicName = compositeId[1];
    }

    public ErpCharacteristicKey(String sapId, String characteristicName) {
        Assert.notNull(sapId);
        Assert.notNull(characteristicName);

        this.sapId = sapId;
        this.characteristicName = characteristicName;
    }


    public String getSapId() {
        return sapId;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((characteristicName == null) ? 0 : characteristicName.hashCode());
        result = prime * result + ((sapId == null) ? 0 : sapId.hashCode());
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
        ErpCharacteristicKey other = (ErpCharacteristicKey) obj;
        if (characteristicName == null) {
            if (other.characteristicName != null) {
                return false;
            }
        } else if (!characteristicName.equals(other.characteristicName)) {
            return false;
        }
        if (sapId == null) {
            if (other.sapId != null) {
                return false;
            }
        } else if (!sapId.equals(other.sapId)) {
            return false;
        }
        return true;
    }

    public ErpCharacteristicValueKey createValueKey(String erpValueName) {
        return new ErpCharacteristicValueKey(this, erpValueName);
    }

    @Override
    public String toString() {
        return sapId + "/" + characteristicName;
    }

    public static String sapIdExtractedFromContentId(String contentId) {
        return contentId.split("/")[0];
    }

    public static String nameExtractedFromContentId(String contentId) {
        return contentId.split("/")[1];
    }
}
