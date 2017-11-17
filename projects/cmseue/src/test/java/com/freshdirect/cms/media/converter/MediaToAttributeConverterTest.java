package com.freshdirect.cms.media.converter;

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
public class MediaToAttributeConverterTest {

    @InjectMocks
    private MediaToAttributeConverter converter;

    @Test
    public void convertImageTypedMediaToAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);

        Assert.assertEquals(1, mediaAttributes.size());
        Assert.assertEquals(4, mediaAttributes.get(key).size());
        Assert.assertEquals(media.getUri(), mediaAttributes.get(key).get(ContentTypes.Image.path));
        Assert.assertEquals(media.getHeight(), mediaAttributes.get(key).get(ContentTypes.Image.height));
        Assert.assertEquals(media.getWidth(), mediaAttributes.get(key).get(ContentTypes.Image.width));
        Assert.assertEquals(media.getLastModified(), mediaAttributes.get(key).get(ContentTypes.Image.lastmodified));
    }

    @Test
    public void convertHtmlTypedMediaToAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.Html, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);

        Assert.assertEquals(1, mediaAttributes.size());
        Assert.assertEquals(2, mediaAttributes.get(key).size());
        Assert.assertEquals(media.getUri(), mediaAttributes.get(key).get(ContentTypes.Html.path));
        Assert.assertEquals(media.getLastModified(),mediaAttributes.get(key).get(ContentTypes.Html.lastmodified));
    }

    @Test
    public void convertMediaFolderTypedMediaToAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.MediaFolder, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);

        Assert.assertEquals(1, mediaAttributes.size());
        Assert.assertEquals(2,mediaAttributes.get(key).size());
        Assert.assertEquals(media.getUri(), mediaAttributes.get(key).get(ContentTypes.MediaFolder.path));
        Assert.assertEquals(media.getLastModified(), mediaAttributes.get(key).get(ContentTypes.MediaFolder.lastmodified));
    }

    @Test
    public void convertTemplateTypedMediaToAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.Template, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);

        Assert.assertEquals(1, mediaAttributes.size());
        Assert.assertEquals(2, mediaAttributes.get(key).size());
        Assert.assertEquals(media.getUri(), mediaAttributes.get(key).get(ContentTypes.Template.path));
        Assert.assertEquals(media.getLastModified(), mediaAttributes.get(key).get(ContentTypes.Template.lastmodified));
    }

    @Test
    public void convertNotSupportedTypedMediaToAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.FDFolder, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);

        Assert.assertEquals(1, mediaAttributes.size());
        Assert.assertTrue(mediaAttributes.get(key).isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void convertReturnsNotModifiableAttributes() {
        ContentKey key = ContentKeyFactory.get(ContentType.Image, EntityFactory.MEDIA_ID);
        Media media = EntityFactory.createMedia(key);

        Map<ContentKey, Map<Attribute, Object>> mediaAttributes = converter.convert(media);
        mediaAttributes.put(ContentKeyFactory.get(ContentType.Html, EntityFactory.MEDIA_ID), null);

        Assert.fail();
    }

}
