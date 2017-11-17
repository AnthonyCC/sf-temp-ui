package com.freshdirect.cms.persistence.erps.data;

import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;

public class ErpCharacteristicValueKey {

    private String sapId;
    private String characteristicName;
    private String characteristicValueName;

    public ErpCharacteristicValueKey(ContentKey contentKey) {
        Assert.notNull(contentKey);
        Assert.isTrue(ContentType.ErpCharacteristicValue == contentKey.type);

        final String[] compositeId = contentKey.id.split("/");
        this.sapId = compositeId[0];
        this.characteristicName = compositeId[1];
        this.characteristicValueName = compositeId[2];
    }

    public ErpCharacteristicValueKey(ErpCharacteristicKey charKey, String erpValueName) {
        Assert.notNull(charKey);
        Assert.notNull(erpValueName);

        this.sapId = charKey.getSapId();
        this.characteristicName = charKey.getCharacteristicName();
        this.characteristicValueName = erpValueName;
    }

    public ErpCharacteristicValueKey(String erpCharacteristicValueId) {
        Assert.notNull(erpCharacteristicValueId);

        final String[] compositeId = erpCharacteristicValueId.split("/");
        this.sapId = compositeId[0];
        this.characteristicName = compositeId[1];
        this.characteristicValueName = compositeId[2];
    }

    public static String nameExtractedFromContentId(String contentId) {
        return contentId.split("/")[1];
    }

    public static String valueNameExtractedFromContentId(String contentId) {
        return contentId.split("/")[2];
    }

    public String getSapId() {
        return sapId;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public String getCharacteristicValueName() {
        return characteristicValueName;
    }

    public ErpCharacteristicKey getCharacteristicKey() {
        return new ErpCharacteristicKey(this.sapId, this.characteristicName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((characteristicName == null) ? 0 : characteristicName.hashCode());
        result = prime * result + ((characteristicValueName == null) ? 0 : characteristicValueName.hashCode());
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
        ErpCharacteristicValueKey other = (ErpCharacteristicValueKey) obj;
        if (characteristicName == null) {
            if (other.characteristicName != null) {
                return false;
            }
        } else if (!characteristicName.equals(other.characteristicName)) {
            return false;
        }
        if (characteristicValueName == null) {
            if (other.characteristicValueName != null) {
                return false;
            }
        } else if (!characteristicValueName.equals(other.characteristicValueName)) {
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

    @Override
    public String toString() {
        return sapId + "/" + characteristicName + "/" + characteristicValueName;
    }
}
