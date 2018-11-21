package com.freshdirect.cms.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.changecontrol.entity.ContentChangeDetailEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity;
import com.freshdirect.cms.changecontrol.entity.ContentChangeType;
import com.freshdirect.cms.core.domain.AttributeFlags;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;
import com.freshdirect.cms.persistence.entity.AttributeEntity;
import com.freshdirect.cms.persistence.entity.ContentNodeEntity;
import com.freshdirect.cms.persistence.entity.RelationshipEntity;

public final class EntityFactory {

    public static final Date MEDIA_LAST_MODIFIED = new Date();
    public static final String MEDIA_MIME_TYPE_STRING = "image/jpeg";
    public static final int MEDIA_HEIGHT = 140;
    public static final int MEDIA_WIDTH = 125;
    public static final String MEDIA_URI = "/media/image/test.jpg";
    public static final String MEDIA_TYPE_STRING = "Image";
    public static final String MEDIA_ID = "mediaId";
    public static final int ATTRIBUTE_ID = 1;
    public static final String CONTENT_KEY = "Product:test";
    public static final String CONTENT_TYPE = "Product";
    public static final String CONTENT_ID = "test";
    public static final String NAME = "FULL_NAME";
    public static final String VALUE = "Test Product";
    public static final Integer ORDINAL = 0;
    public static final ContentType CONTENT_TYPE_ENUM = ContentType.Product;
    public static final String RELATIONSHIP_DESTINATION = "Product:destination";
    public static final String RELATIONSHIP_NAME = "testRelationship";
    public static final RelationshipCardinality RELATIONSHIP_CARDINALITY_ONE = RelationshipCardinality.ONE;
    public static final AttributeFlags ATTRIBUTE_FLAG_NONE = AttributeFlags.NONE;
    public static final boolean NOT_NAVIGABLE = false;
    public static final Integer CHANGESET_SINGLE_ID = 1;

    private EntityFactory() {
    }

    public static ContentNodeEntity createContentNode() {
        return createContentNode(CONTENT_KEY, CONTENT_TYPE);
    }

    public static ContentNodeEntity createContentNode(String contentKey, String contentType) {
        ContentNodeEntity contentNodeEntity = new ContentNodeEntity();
        contentNodeEntity.setContentKey(contentKey);
        contentNodeEntity.setContentType(contentType);
        return contentNodeEntity;
    }

    public static AttributeEntity createAttribute() {
        return createAttribute(ATTRIBUTE_ID, CONTENT_KEY, CONTENT_TYPE, NAME, VALUE, ORDINAL);
    }

    public static AttributeEntity createAttribute(int id, String contentKey, String contentType, String name, String value, Integer ordinal) {
        AttributeEntity attributeEntity = new AttributeEntity();
        attributeEntity.setId(id);
        attributeEntity.setContentKey(contentKey);
        attributeEntity.setContentType(contentType);
        attributeEntity.setName(name);
        attributeEntity.setValue(value);
        attributeEntity.setOrdinal(ordinal);
        return attributeEntity;
    }

    public static ContentKey createContentKey() {
        return createContentKey(CONTENT_TYPE_ENUM, CONTENT_ID);
    }

    public static ContentKey createContentKey(ContentType contentType, String contentId) {
        ContentKey contentKey = ContentKeyFactory.get(contentType, contentId);
        return contentKey;
    }

    public static com.freshdirect.cms.core.domain.Attribute createScalarAttribute(String attributeName, AttributeFlags flags, Class<?> attributeType) {
        return new Scalar(attributeName, flags, true, attributeType);
    }

    public static com.freshdirect.cms.core.domain.Attribute createScalarAttribute() {
        return createScalarAttribute(NAME, ATTRIBUTE_FLAG_NONE, String.class);
    }

    public static RelationshipEntity createRelationShip() {
        RelationshipEntity relationshipEntity = new RelationshipEntity();
        relationshipEntity.setId(ATTRIBUTE_ID);
        relationshipEntity.setOrdinal(ORDINAL);
        relationshipEntity.setRelationshipSource(CONTENT_KEY);
        relationshipEntity.setRelationshipDestination(RELATIONSHIP_DESTINATION);
        relationshipEntity.setRelationshipDestinationType(CONTENT_TYPE);
        relationshipEntity.setRelationshipName(RELATIONSHIP_NAME);

        return relationshipEntity;
    }
    
    public static RelationshipEntity createRelationship(int id, Integer ordinal,  String relationshipSource, String contentType, String relationshipDestination, String relationshipName) {
        RelationshipEntity relationshipEntity = new RelationshipEntity();
        relationshipEntity.setId(id);
        relationshipEntity.setOrdinal(ordinal);
        relationshipEntity.setRelationshipSource(relationshipSource);
        relationshipEntity.setRelationshipDestination(relationshipDestination);
        relationshipEntity.setRelationshipDestinationType(contentType);
        relationshipEntity.setRelationshipName(relationshipName);

        return relationshipEntity;
    }

    public static com.freshdirect.cms.core.domain.Relationship createRelationshipAttribute() {
        com.freshdirect.cms.core.domain.Relationship relationship = new com.freshdirect.cms.core.domain.Relationship(RELATIONSHIP_NAME, ATTRIBUTE_FLAG_NONE,
                true, RELATIONSHIP_CARDINALITY_ONE, NOT_NAVIGABLE, CONTENT_TYPE_ENUM);
        return relationship;
    }

    /**
     * Create a very basic changeset having only one category name rename change
     *
     * @return
     */
    public static com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity createBasicChangeSet() {
        com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity changeSet = new ContentChangeSetEntity();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 06);
        cal.set(Calendar.DAY_OF_MONTH, 13);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 33);

        changeSet.setId(CHANGESET_SINGLE_ID);
        changeSet.setTimestamp(cal.getTime());
        changeSet.setNote("Simple Change Set");
        changeSet.setUserId("vubul");

        List<ContentChangeEntity> changes = new ArrayList<ContentChangeEntity>();
        ContentChangeEntity changeEntity = new ContentChangeEntity();
        changeEntity.setId(12345678);
        changeEntity.setContentType(ContentType.Category);
        changeEntity.setContentId("apl_apl");

        List<ContentChangeDetailEntity> details = new ArrayList<ContentChangeDetailEntity>();
        ContentChangeDetailEntity detail = new ContentChangeDetailEntity();
        detail.setAttributeName("FULL_NAME");
        detail.setOldValue("Apfel");
        detail.setNewValue("Apples");
        detail.setChangeType(ContentChangeType.MOD);
        details.add(detail);

        changeEntity.setDetails(details);

        changes.add(changeEntity);
        changeSet.setChanges(changes);

        return changeSet;
    }

    public static Set<com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity> createChangeHistory() {
        Set<com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity> history = new HashSet<com.freshdirect.cms.changecontrol.entity.ContentChangeSetEntity>();

        history.add(createBasicChangeSet());

        return history;
    }

    public static MediaEntity createMediaEntity() {
        return createMediaEntity(MEDIA_ID, MEDIA_TYPE_STRING, MEDIA_URI, MEDIA_WIDTH, MEDIA_HEIGHT, MEDIA_MIME_TYPE_STRING, MEDIA_LAST_MODIFIED);
    }

    public static MediaEntity createMediaEntity(String id, String type, String uri, Integer width, Integer height, String mimeType, Date lastModified) {
        MediaEntity mediaEntity = new MediaEntity();
        mediaEntity.setId(id);
        mediaEntity.setType(type);
        mediaEntity.setMimeType(mimeType);
        mediaEntity.setLastModified(lastModified);
        mediaEntity.setHeight(height);
        mediaEntity.setWidth(width);
        mediaEntity.setUri(uri);
        return mediaEntity;
    }

    public static Media createMedia() {
        return createMedia(ContentKeyFactory.get(ContentType.valueOf(MEDIA_TYPE_STRING), MEDIA_ID));
    }

    public static Media createMedia(ContentKey key) {
        return createMedia(key, MEDIA_URI, MEDIA_WIDTH, MEDIA_HEIGHT, MEDIA_MIME_TYPE_STRING, MEDIA_LAST_MODIFIED);
    }

    public static Media createMedia(ContentKey key, String uri, Integer width, Integer height, String mimeType, Date lastModified) {
        Media media = new Media(key);
        media.setHeight(height);
        media.setWidth(width);
        media.setLastModified(lastModified);
        media.setUri(uri);
        media.setMimeType(mimeType);
        return media;
    }
}
