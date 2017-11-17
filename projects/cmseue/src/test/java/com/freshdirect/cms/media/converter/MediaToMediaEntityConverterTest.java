package com.freshdirect.cms.media.converter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;
import com.freshdirect.cms.util.EntityFactory;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class MediaToMediaEntityConverterTest {

    @InjectMocks
    private MediaToMediaEntityConverter converter;

    @Test
    public void testConvertMediaToMediaEntity() {
        Media media = EntityFactory.createMedia();

        MediaEntity result = converter.convert(media, new MediaEntity());

        Assert.assertEquals(media.getContentId(), result.getId());
        Assert.assertEquals(media.getContentType(), result.getType());
        Assert.assertEquals(media.getUri(), result.getUri());
        Assert.assertEquals(media.getWidth(), result.getWidth());
        Assert.assertEquals(media.getHeight(), result.getHeight());
        Assert.assertEquals(media.getMimeType(), result.getMimeType());
        //Assert.assertEquals(media.getLastModified(), result.getLastModified()); // TODO find out why lastmodified date is missing
    }
}
