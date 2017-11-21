package com.freshdirect.cms.media.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.util.EntityFactory;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class AttributeValueToMediaConverterTest {

    @InjectMocks
    private AttributeValueToMediaConverter converter;

    @Test
    public void convertMinimalImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
    }

    @Test
    public void convertValidDateImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.lastmodified, "2017-01-01");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNotNull(media.getLastModified());
    }

    @Test
    public void convertInvalidDateImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.lastmodified, "");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNull(media.getLastModified());
    }

    @Test
    public void convertOnlyWidthImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.width, EntityFactory.MEDIA_WIDTH);

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNull(media.getWidth());
        Assert.assertNull(media.getHeight());
    }

    @Test
    public void convertOnlyHeigthImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.height, EntityFactory.MEDIA_HEIGHT);

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNull(media.getWidth());
        Assert.assertNull(media.getHeight());
    }

    @Test
    public void convertWidthAndHeigthImageMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Image.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.width, EntityFactory.MEDIA_WIDTH);
        mediaAttributes.put(ContentTypes.Image.height, EntityFactory.MEDIA_HEIGHT);

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertEquals(EntityFactory.MEDIA_WIDTH, media.getWidth().intValue());
        Assert.assertEquals(EntityFactory.MEDIA_HEIGHT, media.getHeight().intValue());
    }

    @Test
    public void convertValidDateHtmlMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Html, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Html.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.lastmodified, "2017-01-01");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNotNull(media.getLastModified());
    }

    @Test
    public void convertInvalidDateMinimalHtmlMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Html, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.Html.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.Image.lastmodified, "");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNull(media.getLastModified());
    }

    @Test
    public void convertValidDateMediaFolderMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.MediaFolder, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.MediaFolder.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.MediaFolder.lastmodified, "2017-01-01");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNotNull(media.getLastModified());
    }

    @Test
    public void convertInvalidDateMediaFolderMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.MediaFolder, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.MediaFolder.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.MediaFolder.lastmodified, "");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNull(media.getLastModified());
    }

    @Test
    public void convertFilesMediaFolderMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.MediaFolder, EntityFactory.MEDIA_ID);
        List<ContentKey> files = new ArrayList<ContentKey>();
        List<ContentKey> subFolders = new ArrayList<ContentKey>();

        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.MediaFolder.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.MediaFolder.files, files);
        mediaAttributes.put(ContentTypes.MediaFolder.subFolders, subFolders);

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertEquals(files, media.getFiles());
        Assert.assertEquals(files, media.getSubFolders());
    }

    @Test
    public void convertTemplateMediaAttributesToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Template, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();
        mediaAttributes.put(ContentTypes.MediaFolder.path, EntityFactory.MEDIA_URI);
        mediaAttributes.put(ContentTypes.MediaFolder.lastmodified, "2017-01-01");

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertEquals(EntityFactory.MEDIA_URI, media.getUri());
        Assert.assertNotNull(media.getLastModified());
    }

    @Test
    public void convertNotValidMediakeyToMedia() {
        ContentKey key = ContentKeyFactory.get(ContentType.Anchor, EntityFactory.MEDIA_ID);
        Map<Attribute, Object> mediaAttributes = new HashMap<Attribute, Object>();

        Media media = converter.convert(key, mediaAttributes);

        Assert.assertEquals(key, media.getContentKey());
        Assert.assertNull(media.getUri());
    }

}
