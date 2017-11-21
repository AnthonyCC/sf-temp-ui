package com.freshdirect.cms.ui.editor.publish.flow.service;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;

public interface PublishMessageLogger {

    void log(Long publishId, StorePublishMessage message);
}
