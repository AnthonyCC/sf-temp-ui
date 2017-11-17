package com.freshdirect.cms.core.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.google.common.base.Function;
import com.google.common.base.Optional;

@Service
public class UniqueContentKeyGeneratorService {

    public Optional<String> generateContentId(ContentType contentType) {
        Assert.notNull(contentType);

        if (contentType.generatedContentId) {
            return Optional.of(UUID.randomUUID().toString());
        }

        return Optional.absent();
    }

    public Optional<ContentKey> generateContentKey(final ContentType contentType) {
        Assert.notNull(contentType);

        return generateContentId(contentType).transform(new Function<String, ContentKey>() {
            @Override
            public ContentKey apply(String contentId) {
                return ContentKeyFactory.get(contentType, contentId);
            }
        });
    }
}
