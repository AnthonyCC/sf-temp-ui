package com.freshdirect.cms.media.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.freshdirect.cms.DatabaseTestConfiguration;
import com.freshdirect.cms.category.IntegrationTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DatabaseTestConfiguration.class })
@WebAppConfiguration
@ActiveProfiles("test")
@Category(IntegrationTest.class)
public class DatabaseMediaServiceIntegrationTest {

    @Autowired
    private DatabaseMediaService databaseMediaService;

    @Test
    public void testGetMediaByContentKey() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "media1");
        Media mediaToSave = EntityFactory.createMedia(contentKey);
        databaseMediaService.saveMedia(mediaToSave);

        Optional<Media> media = databaseMediaService.getMediaByContentKey(contentKey);

        Assert.assertTrue(media.isPresent());
        Assert.assertEquals(contentKey, media.get().getContentKey());
    }

    @Test
    public void testGetMediaByNotExistingContentKey() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "not_existing");
        Optional<Media> media = databaseMediaService.getMediaByContentKey(contentKey);

        Assert.assertFalse(media.isPresent());
    }

    @Test
    public void testGetMediasByContentKeys() {
        ContentKey contentKey1 = ContentKeyFactory.get(ContentType.Image, "media1");
        ContentKey contentKey2 = ContentKeyFactory.get(ContentType.Image, "media2");

        Map<ContentKey, Media> medias = databaseMediaService.getMediasByContentKeys(Arrays.asList(contentKey1, contentKey2));

        Assert.assertEquals(2, medias.size());
        Assert.assertTrue(medias.containsKey(contentKey1));
        Assert.assertTrue(medias.containsKey(contentKey2));
    }

    @Test
    public void testGetMediasByContentKeysContainingNotExisting() {
        ContentKey contentKey1 = ContentKeyFactory.get(ContentType.Image, "media1");
        ContentKey contentKey2 = ContentKeyFactory.get(ContentType.Image, "media2");
        ContentKey notExisting = ContentKeyFactory.get(ContentType.Image, "not_existing");

        Map<ContentKey, Media> medias = databaseMediaService.getMediasByContentKeys(Arrays.asList(contentKey1, contentKey2, notExisting));

        Assert.assertEquals(2, medias.size());
        Assert.assertTrue(medias.containsKey(contentKey1));
        Assert.assertTrue(medias.containsKey(contentKey2));
        Assert.assertFalse(medias.containsKey(notExisting));
    }

    @Test
    public void tetsGetMediasByNotExistingContentKeys() {
        ContentKey notExisting = ContentKeyFactory.get(ContentType.Image, "not_existing");
        ContentKey notExisting2 = ContentKeyFactory.get(ContentType.Image, "not_existing2");

        Map<ContentKey, Media> medias = databaseMediaService.getMediasByContentKeys(Arrays.asList(notExisting, notExisting2));

        Assert.assertEquals(0, medias.size());
    }

    @Test
    public void testGetMediaByUri() {
        String uri = "/media/test/media.html";

        Optional<Media> media = databaseMediaService.getMediaByUri(uri);

        Assert.assertTrue(media.isPresent());
    }

    @Test
    public void testGetMediaByNotExistingUri() {
        String uri = "/media/test/not/existing/just/like/hulk.html";

        Optional<Media> media = databaseMediaService.getMediaByUri(uri);

        Assert.assertFalse(media.isPresent());
    }

    @Test
    public void testSaveMedia() {
        Media mediaToSave = EntityFactory.createMedia();
        databaseMediaService.saveMedia(mediaToSave);

        Optional<Media> retrieved = databaseMediaService.getMediaByContentKey(mediaToSave.getContentKey());

        Assert.assertTrue(retrieved.isPresent());
        Assert.assertEquals(mediaToSave, retrieved.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveMediaWithoutContentKey() {
        Media mediaToSave = EntityFactory.createMedia();
        mediaToSave.setContentKey(null);
        databaseMediaService.saveMedia(mediaToSave);

        databaseMediaService.getMediaByContentKey(mediaToSave.getContentKey());
    }

    @Test
    public void testSaveMediaUpdate() {
        Media mediaToSave = EntityFactory.createMedia();
        databaseMediaService.saveMedia(mediaToSave);

        Optional<Media> retrieved = databaseMediaService.getMediaByContentKey(mediaToSave.getContentKey());

        Media media = retrieved.get();
        media.setHeight(1000);

        databaseMediaService.saveMedia(media);
        Optional<Media> updated = databaseMediaService.getMediaByContentKey(mediaToSave.getContentKey());
        Assert.assertTrue(updated.isPresent());
        Assert.assertEquals(media, updated.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNotFoundMediaThrowsException() {
        Media media = EntityFactory.createMedia();
        databaseMediaService.deleteMedia(media);
    }

    @Test
    public void testDeleteAlreadyPlacedMedia() {
        Media media = EntityFactory.createMedia();
        databaseMediaService.saveMedia(media);

        Optional<Media> updated = databaseMediaService.getMediaByContentKey(media.getContentKey());
        Assert.assertTrue(updated.isPresent());

        databaseMediaService.deleteMedia(media);

        updated = databaseMediaService.getMediaByContentKey(media.getContentKey());
        Assert.assertFalse(updated.isPresent());
    }

    @Test
    public void testGetMediasByUriStarts() {
        String uri = "alternativeUri";
        Media media = EntityFactory.createMedia();
        media.setUri(uri);
        databaseMediaService.saveMedia(media);

        List<Media> mediaItems = databaseMediaService.getMediasByUriStartsWith(uri);
        Assert.assertFalse(mediaItems.isEmpty());
        Assert.assertEquals(media, mediaItems.get(0));
    }

    @Test
    public void testFindMediaNewerThan() {
        Date date = new Date();
        Media pastMedia = EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), "newerMediaId1"));
        pastMedia.setLastModified(date);
        databaseMediaService.saveMedia(pastMedia);

        List<Media> mediaItems = databaseMediaService.findMediaNewerThan(date);

        Assert.assertEquals(1, mediaItems.size());
    }

    @Test
    public void testLoadAll() {
        List<Media> mediaItems = databaseMediaService.loadAll();
        Assert.assertEquals(3, mediaItems.size());
    }

    @Test
    public void testGetChildMediaKeys() {
        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "media1132121");
        Media mediaToSave = EntityFactory.createMedia(contentKey);
        mediaToSave.setUri("/media/test");
        databaseMediaService.saveMedia(mediaToSave);

        Set<ContentKey> childMediaKeys = databaseMediaService.getChildMediaKeys(contentKey);
        Assert.assertEquals(3, childMediaKeys.size());
    }

}
