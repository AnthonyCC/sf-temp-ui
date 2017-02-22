package com.freshdirect.cms.publish.service;

import com.freshdirect.cms.publish.PublishMessage;

public interface PublishMessageLogger {

    void log(String publishId, PublishMessage message);
}
