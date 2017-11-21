package com.freshdirect.cms.media.converter;

import java.util.Arrays;
import java.util.List;

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
public class MediaEntityToMediaConverterTest {

    @InjectMocks
    private MediaEntityToMediaConverter converter;

    @Test
    public void testConvertNullMediaEntityToMedia() {
        Assert.assertNull(converter.convert(null));
    }

    @Test
    public void testConvertMediaEntityToMedia() {
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();

        Media media = converter.convert(mediaEntity);

        assertMediaAndMediaEntity(mediaEntity, media);
    }

    @Test
    public void testConvertMediaEntitiesToMediaItems() {
        List<MediaEntity> mediaEntities = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());

        List<Media> mediaItems = converter.convertCollection(mediaEntities);

        Assert.assertEquals(mediaEntities.size(), mediaItems.size());
        for (int i = 0; i < mediaEntities.size(); i++) {
            assertMediaAndMediaEntity(mediaEntities.get(i), mediaItems.get(i));
        }
    }

    private void assertMediaAndMediaEntity(MediaEntity mediaEntity, Media media) {
        Assert.assertEquals(mediaEntity.getId(), media.getContentId());
        Assert.assertEquals(mediaEntity.getType(), media.getContentType());
        Assert.assertEquals(mediaEntity.getUri(), media.getUri());
        Assert.assertEquals(mediaEntity.getWidth(), media.getWidth());
        Assert.assertEquals(mediaEntity.getHeight(), media.getHeight());
        Assert.assertEquals(mediaEntity.getMimeType(), media.getMimeType());
    }
}
