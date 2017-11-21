package com.freshdirect.cms.media.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.media.domain.Media;
import com.google.common.base.Optional;

@Service
public interface MediaService {

    /**
     * Allows fetching all media entries with a single call
     * @return list of media items
     */
    List<Media> loadAll();

    /**
     * Get a media object by its ContentKey
     *
     * @param mediaContentKey
     *            the contentKey of the media
     * @return an optional which is absent if the media with the given contentKey is not existing, otherwise the media itself in the optional
     */
    Optional<Media> getMediaByContentKey(ContentKey mediaContentKey);

    /**
     * Get multiple media objects by a list of contentKeys
     *
     * @param mediaContentKeys
     *            a list of contentKeys
     * @return a Map of contentKey -> Media, containing only the existing media objects, so if there are no media object for a given contentKey, then it is not in the returned map
     */
    Map<ContentKey, Media> getMediasByContentKeys(List<ContentKey> mediaContentKeys);

    /**
     * Get a media object for a given URI.
     *
     * @param uri
     *            the URI for which the media object will be retrieved
     * @return an optional. If the media object exists, it will be returned, otherwise Optional.absent
     */
    Optional<Media> getMediaByUri(String uri);

    /**
     * Get all of the media entries which uri starts with the param 'prefix'
     *
     * @param uriPrefix
     *            the prefix for which the media entries will be returned
     * @return list of media entries if any or empty list
     */
    List<Media> getMediasByUriStartsWith(String uriPrefix);

    Set<ContentKey> getChildMediaKeys(ContentKey parentMediaContentKey);

    /**
     * Collects media created or updated after given date
     *
     * @param date timestamp
     *
     * @return collected media
     */
    List<Media> findMediaNewerThan(Date date);

    /**
     * Saves a media object. If there is an existing media entry with the contentKey, then this is an update otherwise an insert.
     *
     * @param media
     *            the media object to save
     */
    void saveMedia(Media media);

    /**
     * Delete a media object
     *
     * @param media
     *            Media object to delete
     */
    void deleteMedia(Media media);
}
