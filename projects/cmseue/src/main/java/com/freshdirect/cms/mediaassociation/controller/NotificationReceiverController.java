package com.freshdirect.cms.mediaassociation.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.mediaassociation.domain.NotificationCommand;
import com.freshdirect.cms.mediaassociation.service.MediaEventHandlerService;
import com.freshdirect.cms.validation.ValidationResult;
import com.freshdirect.cms.validation.exception.ValidationFailedException;
import com.google.common.base.Optional;

@RestController
@RequestMapping("/notification")
public class NotificationReceiverController {

    private static final String IMAGE_MIMETYPE_PREFIX = "image";

    private static final int DEFAULT_IMAGE_WIDTH = 150;
    private static final int DEFAULT_IMAGE_HEIGHT = 150;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationReceiverController.class);

    @Autowired
    private MediaEventHandlerService mediaEventHandlerService;

    @RequestMapping(method = RequestMethod.HEAD, params = { "cmd", "user", "src" })
    public void receiveNotification(@RequestParam("cmd") String commandValue, @RequestParam("user") String user, @RequestParam("src") String source,
            @RequestParam(value = "mime", required = false) String mimeType, @RequestParam(value = "dim", required = false) String dimensions,
            @RequestParam(value = "dst", required = false) String destination) {

        NotificationCommand command = NotificationCommand.getEnumFromString(commandValue);
        switch (command) {
            case CREATE:
                mediaEventHandlerService.createMedia(createMediaFromRequest(source, mimeType, dimensions), user);
                break;

            case UPDATE:
                mediaEventHandlerService.updateMedia(createMediaFromRequest(source, mimeType, dimensions), user);
                break;

            case DELETE:
                mediaEventHandlerService.deleteMedia(source, user);
                break;

            case MOVE:
                Assert.notNull(destination, "Can't perform '" + command + "' without a destination!");
                mediaEventHandlerService.moveMedia(source, destination, user);
                break;

            default:
                break;
        }
        LOGGER.info("Command '" + command + "' on '" + source + "' has been completed");
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid command")
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleBadCommand(HttpServletRequest req, Exception exception) {
        LOGGER.error("Invalid command", exception);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ValidationFailedException.class)
    public void handleValidationException(ValidationFailedException exception) {
        LOGGER.error("Validation failed");
        for (ValidationResult result : exception.getValidationResults()) {
            LOGGER.error(result.toString());
        }
    }

    private Media createMediaFromRequest(String source, String mimeType, String dimensions) {

        checkRequestMimeType(source, mimeType);

        Optional<Media> optionalMedia = mediaEventHandlerService.lookupMedia(source);

        Media media = null;

        if (optionalMedia.isPresent()) {
            // this is an existing media
            media = optionalMedia.get();
        } else {
            ContentType mediaType = determineMediaType(source, mimeType);
            ContentKey mediaKey = ContentKeyFactory.get(mediaType, UUID.randomUUID().toString());

            // new media entry
            media = new Media(mediaKey);
            media.setUri(source);
            if (mimeType != null && !mimeType.isEmpty()) {
                media.setMimeType(mimeType);
            }
        }

        // set / update dimensions
        if (ContentType.Image == media.getContentKey().type) {
            if (dimensions != null && !dimensions.isEmpty()) {
                String[] dimensionValues = StringUtils.split(dimensions, 'x');
                Integer width = Integer.valueOf(dimensionValues[0]);
                Integer height = Integer.valueOf(dimensionValues[1]);

                media.setWidth(width);
                media.setHeight(height);
            } else {
                media.setWidth(DEFAULT_IMAGE_WIDTH);
                media.setHeight(DEFAULT_IMAGE_HEIGHT);
            }
        }
        return media;
    }

    private void checkRequestMimeType(String source, String mimeType) {
        if (mimeType != null && !mimeType.startsWith("image/") && !mimeType.startsWith("text/")) {
            LOGGER.error("Suspicious mime-type detected (not image or text): " + mimeType + ", for " + source);
        }
        if (mimeType != null && mimeType.contains("permission")) {
            LOGGER.error("Suspicious mime-type detected (possible permission error): " + mimeType + ", for " + source);
        }
    }

    private ContentType determineMediaType(String source, String mimeType) {
        ContentType mediaType;
        if (mimeType != null && !mimeType.isEmpty()) {
            if (mimeType.startsWith(IMAGE_MIMETYPE_PREFIX) || source.endsWith(".jpg") || source.endsWith(".jpeg") || source.endsWith(".gif") || source.endsWith(".png")) {
                mediaType = ContentType.Image;
            } else {
                mediaType = ContentType.Html;
            }
        } else {
            mediaType = ContentType.MediaFolder;
        }
        return mediaType;
    }
}
