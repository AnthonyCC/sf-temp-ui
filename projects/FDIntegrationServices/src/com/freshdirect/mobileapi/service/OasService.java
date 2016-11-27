package com.freshdirect.mobileapi.service;

import java.util.Map;

import com.freshdirect.mobileapi.model.SessionUser;

public interface OasService {

    public Map<String, Object> getMessages() throws ServiceException;

    public Map<String, Object> getMessages(SessionUser user) throws ServiceException;
}
