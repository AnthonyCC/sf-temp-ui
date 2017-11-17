package com.freshdirect.cms.media.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;

public class Media {

    private ContentKey contentKey;
    private String uri;

    // Image fields
    private Integer width;
    private Integer height;

    private String mimeType;
    private Date lastModified;

    // MediaFolder fields
    private List<ContentKey> files;
    private List<ContentKey> subFolders;

    public Media(ContentKey contentKey) {
        this.contentKey = contentKey;
    }

    public static boolean isMediaType(ContentKey contentKey) {
        Assert.notNull(contentKey, "contentKey parameter required!");

        if (ContentType.Html == contentKey.type || ContentType.Image == contentKey.type
                || ContentType.Template == contentKey.type || ContentType.MediaFolder  == contentKey.type) {
            return true;
        }

        return false;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public String getContentType() {
        return contentKey.type.toString();
    }

    public String getContentId() {
        return contentKey.id;
    }

    public void setContentKey(ContentKey contentKey) {
        this.contentKey = contentKey;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Can be null!
     *
     * @return width of the media, or null
     */
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Can be null!
     *
     * @return height of the media, or null
     */
    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<ContentKey> getFiles() {
        return files;
    }

    public void setFiles(List<ContentKey> files) {
        this.files = files;
    }

    public List<ContentKey> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<ContentKey> subFolders) {
        this.subFolders = subFolders;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Media)) {
            return false;
        }
        Media other = (Media) obj;
        return new EqualsBuilder()
            .append(getContentKey(), other.getContentKey())
            .append(getMimeType(), other.getMimeType())
            .append(getWidth(), other.getWidth())
            .append(getHeight(), other.getHeight())
            .append(getUri(), other.getUri())
            .isEquals();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contentKey == null) ? 0 : contentKey.hashCode());
        result = prime * result + ((height == null) ? 0 : height.hashCode());
        result = prime * result + ((mimeType == null) ? 0 : mimeType.hashCode());
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        result = prime * result + ((width == null) ? 0 : width.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("[MEDIA: {")
            .append("contentKey=")
            .append(getContentKey())
            .append("} {mime=")
            .append(getMimeType())
            .append("} {uri=")
            .append(getUri())
            .append("} {size=")
            .append(getWidth())
            .append("x")
            .append(getHeight())
            .append("} {lastModified=")
            .append(getLastModified())
            .append("}]")
            .toString();
    }
}
