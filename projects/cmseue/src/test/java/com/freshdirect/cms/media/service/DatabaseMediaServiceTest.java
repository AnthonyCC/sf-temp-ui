package com.freshdirect.cms.media.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.converter.MediaEntityToMediaConverter;
import com.freshdirect.cms.media.converter.MediaToMediaEntityConverter;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.entity.MediaEntity;
import com.freshdirect.cms.media.repository.MediaEntityRepository;
import com.freshdirect.cms.util.EntityFactory;
import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DatabaseMediaServiceTest {

    private static final String NOT_EXISTING_MEDIA_URI = "/not/existing/media/uri/jabba_the_hutt.jpg";
    private static final String NOT_EXISTING_MEDIA_ID = "Surely_not_existing_media_id_logan_batman_hulk_yeah";

    @InjectMocks
    private DatabaseMediaService underTest;

    @Mock
    private MediaEntityRepository mediaEntityRepository;

    @Mock
    private MediaEntityToMediaConverter mediaEntityToMediaConverter;

    @Mock
    private MediaToMediaEntityConverter mediaToMediaEntityConverter;

    @Test
    public void getMediaByValidContentKey() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        Mockito.when(mediaEntityRepository.findByTypeAndId(mediaContentKey.type.toString(), mediaContentKey.id.toString())).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(media);

        Optional<Media> result = underTest.getMediaByContentKey(mediaContentKey);

        Assert.assertEquals(media, result.get());
    }

    @Test
    public void getMediaByNotExistingContentKey() {
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), NOT_EXISTING_MEDIA_ID);
        Optional<Media> result = underTest.getMediaByContentKey(mediaContentKey);
        Assert.assertFalse(result.isPresent());
        Mockito.verify(mediaEntityRepository).findByTypeAndId(EntityFactory.MEDIA_TYPE_STRING, mediaContentKey.getId());
        Mockito.verify(mediaEntityToMediaConverter).convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMediaByNullContentKey() {
        underTest.getMediaByContentKey(null);
    }

    @Test
    public void getMediasByValidContentKeys() {
        Media media = EntityFactory.createMedia();
        List<MediaEntity> mediaEntities = Arrays.asList(EntityFactory.createMediaEntity());
        Mockito.when(mediaEntityRepository.findByTypeInAndIdIn(Arrays.asList(media.getContentKey().type.toString()), Arrays.asList(media.getContentKey().id.toString())))
                .thenReturn(mediaEntities);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaEntities)).thenReturn(Arrays.asList(media));
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);

        Map<ContentKey, Media> result = underTest.getMediasByContentKeys(Arrays.asList(mediaContentKey));

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.keySet().contains(mediaContentKey));
        Assert.assertEquals(mediaContentKey, result.keySet().iterator().next());
        Assert.assertEquals(media, result.get(mediaContentKey));
    }

    @Test
    public void getMediasByNotExistingContentKeys() {
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), NOT_EXISTING_MEDIA_ID);
        List<ContentKey> contentKeys = Arrays.asList(mediaContentKey);
        List<String> contentTypes = Arrays.asList(mediaContentKey.type.toString());
        List<String> contentIds = Arrays.asList(mediaContentKey.id);

        Map<ContentKey, Media> result = underTest.getMediasByContentKeys(contentKeys);

        Assert.assertTrue(result.isEmpty());
        Assert.assertFalse(result.keySet().contains(mediaContentKey));
        Mockito.verify(mediaEntityRepository).findByTypeInAndIdIn(contentTypes, contentIds);
        Mockito.verify(mediaEntityToMediaConverter, Mockito.never()).convert(Mockito.any(MediaEntity.class));
    }

    @Test
    public void getMediasByContentKeysContainingNotExistent() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        ContentKey notExistingContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), NOT_EXISTING_MEDIA_ID);

        List<ContentKey> contentKeys = Arrays.asList(mediaContentKey, notExistingContentKey);
        List<String> contentTypes = Arrays.asList(mediaContentKey.type.toString(), notExistingContentKey.type.toString());
        List<String> contentIds = Arrays.asList(mediaContentKey.id, notExistingContentKey.id);
        List<MediaEntity> mediaEntities = Arrays.asList(mediaEntity);
        List<Media> mediaItems = Arrays.asList(media);

        Mockito.when(mediaEntityRepository.findByTypeInAndIdIn(contentTypes, contentIds)).thenReturn(mediaEntities);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaEntities)).thenReturn(mediaItems);

        Map<ContentKey, Media> result = underTest.getMediasByContentKeys(contentKeys);

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.keySet().contains(mediaContentKey));
        Assert.assertEquals(mediaContentKey, result.keySet().iterator().next());
        Assert.assertEquals(media, result.get(mediaContentKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMediasByNullContentKeys() {
        underTest.getMediasByContentKeys(null);
    }

    @Test
    public void getMediaByUri() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        Mockito.when(mediaEntityRepository.findByUri(EntityFactory.MEDIA_URI)).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(media);

        Optional<Media> result = underTest.getMediaByUri(EntityFactory.MEDIA_URI);

        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(media, result.get());
    }

    @Test
    public void getMediaByNotExistingUriReturnEmptyOptional() {
        Optional<Media> media = underTest.getMediaByUri(NOT_EXISTING_MEDIA_URI);

        Assert.assertFalse(media.isPresent());
        Mockito.verify(mediaEntityRepository).findByUri(NOT_EXISTING_MEDIA_URI);
        Mockito.verify(mediaEntityToMediaConverter).convert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMediaByNullUriThrowsIllegalArgumentException() {
        underTest.getMediaByUri(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNullMediaThrowsIllegalArgumentException() {
        underTest.saveMedia(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveMediaWithNullContentKeyThrowsIllegalArgumentException() {
        Media media = EntityFactory.createMedia();
        media.setContentKey(null);
        underTest.saveMedia(media);
    }

    @Test
    public void saveMediaWithinStoredMediaEntity() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        Mockito.when(mediaEntityRepository.findByTypeAndId(EntityFactory.MEDIA_TYPE_STRING, EntityFactory.MEDIA_ID)).thenReturn(mediaEntity);
        Mockito.when(mediaToMediaEntityConverter.convert(media, mediaEntity)).thenReturn(mediaEntity);

        underTest.saveMedia(media);

        Mockito.verify(mediaEntityRepository).save(mediaEntity);
    }

    @Test
    public void saveMediaWithoutStoredMediaEntity() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        Mockito.when(mediaEntityRepository.findByTypeAndId(EntityFactory.MEDIA_TYPE_STRING, EntityFactory.MEDIA_ID)).thenReturn(null);
        Mockito.when(mediaToMediaEntityConverter.convert(Mockito.any(Media.class), Mockito.any(MediaEntity.class))).thenReturn(mediaEntity);

        underTest.saveMedia(media);

        Mockito.verify(mediaEntityRepository).save(mediaEntity);
    }

    @Test
    public void loadEmptyMediaEntities() {
        List<MediaEntity> mediaEntities = Arrays.asList();
        Mockito.when(mediaEntityRepository.findAll()).thenReturn(mediaEntities);

        List<Media> result = underTest.loadAll();

        Assert.assertTrue("Media entities are not empty", result.isEmpty());
    }

    @Test
    public void loadMediaEntities() {
        List<Media> mediaItems = Arrays.asList(EntityFactory.createMedia(), EntityFactory.createMedia());
        List<MediaEntity> mediaEntities = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        Mockito.when(mediaEntityRepository.findAll()).thenReturn(mediaEntities);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaEntities)).thenReturn(mediaItems);

        List<Media> result = underTest.loadAll();

        Assert.assertEquals("Media entities are empty", mediaItems, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getChildMediaKeysWithNullContentKeyThrowsIllegalArgumentException() {
        underTest.getChildMediaKeys(null);
    }

    @Test
    public void getChildMediaKeysWhenMediaIsNotFound() {
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        ContentKey mediaContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        Mockito.when(mediaEntityRepository.findByTypeAndId(mediaContentKey.type.toString(), mediaContentKey.id.toString())).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(null);

        Set<ContentKey> result = underTest.getChildMediaKeys(mediaContentKey);

        Assert.assertTrue("Content keys are not empty", result.isEmpty());
    }

    @Test
    public void getChildMediaKeysWhenMediaIsFoundWithTheSameContentKeys() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        List<Media> mediaUriItems = Arrays.asList(EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID)),
                EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID)));
        List<MediaEntity> mediaUriEntites = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        ContentKey parentContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        Mockito.when(mediaEntityRepository.findByTypeAndId(parentContentKey.type.toString(), parentContentKey.id.toString())).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(media);
        Mockito.when(mediaEntityRepository.findByBasePath(EntityFactory.MEDIA_URI + "/%")).thenReturn(mediaUriEntites);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaUriEntites)).thenReturn(mediaUriItems);

        Set<ContentKey> result = underTest.getChildMediaKeys(parentContentKey);

        Assert.assertEquals("Content keys do not have 1 entry", 1, result.size());
        Assert.assertTrue("Content keys do not contain media key", result.contains(media.getContentKey()));
    }

    @Test
    public void getChildMediaKeysWhenMediaIsFoundWithTheSameContentKeysUriEndsWithSlash() {
        Media media = EntityFactory.createMedia();
        media.setUri(EntityFactory.MEDIA_URI + "/");
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        List<Media> mediaUriItems = Arrays.asList(EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID)),
                EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID)));
        List<MediaEntity> mediaUriEntites = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        ContentKey parentContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        Mockito.when(mediaEntityRepository.findByTypeAndId(parentContentKey.type.toString(), parentContentKey.id.toString())).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(media);
        Mockito.when(mediaEntityRepository.findByBasePath(EntityFactory.MEDIA_URI + "/%")).thenReturn(mediaUriEntites);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaUriEntites)).thenReturn(mediaUriItems);

        Set<ContentKey> result = underTest.getChildMediaKeys(parentContentKey);

        Assert.assertEquals("Content keys do not have 1 entry", 1, result.size());
        Assert.assertTrue("Content keys do not contain media key", result.contains(media.getContentKey()));
    }

    @Test
    public void getChildMediaKeysWhenMediaIsFoundWithTheDifferentContentKeys() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        List<Media> mediaUriItems = Arrays.asList(EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.CONTENT_TYPE), EntityFactory.MEDIA_ID)),
                EntityFactory.createMedia(ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID)));
        List<MediaEntity> mediaUriEntites = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        ContentKey parentContentKey = ContentKeyFactory.get(ContentType.valueOf(EntityFactory.MEDIA_TYPE_STRING), EntityFactory.MEDIA_ID);
        Mockito.when(mediaEntityRepository.findByTypeAndId(parentContentKey.type.toString(), parentContentKey.id.toString())).thenReturn(mediaEntity);
        Mockito.when(mediaEntityToMediaConverter.convert(mediaEntity)).thenReturn(media);
        Mockito.when(mediaEntityRepository.findByBasePath(EntityFactory.MEDIA_URI + "/%")).thenReturn(mediaUriEntites);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaUriEntites)).thenReturn(mediaUriItems);

        Set<ContentKey> result = underTest.getChildMediaKeys(parentContentKey);

        Assert.assertEquals("Content keys do not have 2 entry", 2, result.size());
        Assert.assertTrue("Content keys do not contain media key", result.contains(parentContentKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMediaWithNullMediaParameterThrowsIllegalArgumentException() {
        underTest.deleteMedia(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMediaWithNullContentKeyThrowsIllegalArgumentException() {
        Media media = EntityFactory.createMedia();
        media.setContentKey(null);
        underTest.deleteMedia(media);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMediaWithNullMediaThrowsIllegalArgumentException() {
        Media media = EntityFactory.createMedia();
        Mockito.when(mediaEntityRepository.findByTypeAndId(EntityFactory.CONTENT_TYPE, EntityFactory.CONTENT_ID)).thenReturn(null);
        underTest.deleteMedia(media);
    }

    @Test
    public void deleteMedia() {
        Media media = EntityFactory.createMedia();
        MediaEntity mediaEntity = EntityFactory.createMediaEntity();
        Mockito.when(mediaEntityRepository.findByTypeAndId(EntityFactory.MEDIA_TYPE_STRING, EntityFactory.MEDIA_ID)).thenReturn(mediaEntity);
        Mockito.when(mediaToMediaEntityConverter.convert(media, mediaEntity)).thenReturn(mediaEntity);
        underTest.deleteMedia(media);
        Mockito.verify(mediaEntityRepository).delete(mediaEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMediasByUriStartsWithWithNullMediaThrowsIllegalArgumentException() {
        underTest.getMediasByUriStartsWith(null);
    }

    @Test
    public void getMediasByUriStartsWithMediaUri() {
        List<Media> mediaItems = Arrays.asList(EntityFactory.createMedia(), EntityFactory.createMedia());
        List<MediaEntity> mediaEntities = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        Mockito.when(mediaEntityRepository.findByUriStartsWith(EntityFactory.MEDIA_URI)).thenReturn(mediaEntities);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaEntities)).thenReturn(mediaItems);
        List<Media> result = underTest.getMediasByUriStartsWith(EntityFactory.MEDIA_URI);
        Assert.assertEquals(mediaItems, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findMediaNewerThanWithNullMediaThrowsIllegalArgumentException() {
        underTest.findMediaNewerThan(null);
    }

    @Test
    public void findMediaNewerThanDate() {
        List<Media> mediaItems = Arrays.asList(EntityFactory.createMedia(), EntityFactory.createMedia());
        List<MediaEntity> mediaEntities = Arrays.asList(EntityFactory.createMediaEntity(), EntityFactory.createMediaEntity());
        Mockito.when(mediaEntityRepository.findByLastModifiedGreaterThan(EntityFactory.MEDIA_LAST_MODIFIED)).thenReturn(mediaEntities);
        Mockito.when(mediaEntityToMediaConverter.convertCollection(mediaEntities)).thenReturn(mediaItems);
        List<Media> result = underTest.findMediaNewerThan(EntityFactory.MEDIA_LAST_MODIFIED);
        Assert.assertEquals(mediaItems, result);
    }
}
