package com.freshdirect.webapp.search.unbxd.dto;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;

public class UnbxdSearchResponseProduct {

    @JsonProperty("uniqueId")
    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getProductId());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnbxdSearchResponseProduct) {
            UnbxdSearchResponseProduct other = (UnbxdSearchResponseProduct) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(getProductId(), other.getProductId());
            return builder.isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[productId = " + productId + "]";
    }

    public ContentKey getContentKey() {
        return ContentKey.getContentKey(ContentType.get("Product"), productId);
    }
}
