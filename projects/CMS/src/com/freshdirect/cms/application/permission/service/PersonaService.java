package com.freshdirect.cms.application.permission.service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.permission.domain.Permission;
import com.freshdirect.cms.application.permission.domain.Persona;
import com.freshdirect.cms.application.permission.domain.PersonaConversionData;
import com.freshdirect.cms.application.permission.domain.PersonaConverter;
import com.freshdirect.cms.application.permission.domain.PersonaWrapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.http.HttpService;

import net.sf.ehcache.config.CacheConfiguration;

public class PersonaService {

    private static final String LIST_SEPARATOR = ", ";
    public static final String CMS_PERSONA_CACHE_NAME = "CMS_PERSONA";
    private static final PersonaService INSTANCE = new PersonaService();

    private PersonaService() {
        CacheConfiguration cacheConfiguration = populatePersonaCacheConfiguration();
        EhCacheUtil.createCache(cacheConfiguration);
    }

    public static PersonaService defaultService() {
        return INSTANCE;
    }

    private static final Logger LOG = LoggerFactory.getInstance(PersonaService.class);

    public PersonaWrapper loadPermissionsForUser(String userName) {
        boolean updateUser = false;
        Persona persona = EhCacheUtil.getObjectFromCache(CMS_PERSONA_CACHE_NAME, userName);
        if (persona == null) {
            PersonaConversionData personaConversionData = null;
            String uri = FDStoreProperties.getCMSAdminServiceURL() + MessageFormat.format("/api/user/{0}/persona", userName);
            try {
                LOG.info(MessageFormat.format("Try to load user[{0}] permissions from uri[{1}]", userName, uri));
                personaConversionData = HttpService.defaultService().<PersonaConversionData> getData(uri, PersonaConversionData.class);
                if (personaConversionData.getErrors() == null) {
                    persona = PersonaConverter.defaultConverter().convert(personaConversionData);
                    EhCacheUtil.putObjectToCache(CMS_PERSONA_CACHE_NAME, userName, persona);
                    updateUser = true;
                    logGatheredPermissions(userName, persona.getPermissions());
                } else {
                    handleErrors(userName, personaConversionData.getErrors(), uri);
                }
            } catch (IOException e) {
                LOG.error(MessageFormat.format("Failed to load user[{0}] permissions from uri[{1}]", userName, uri), e);
            }
        }
        return new PersonaWrapper(persona, updateUser);
    }

    public void invalidatePersona(final String userName) {
        EhCacheUtil.removeFromCache(PersonaService.CMS_PERSONA_CACHE_NAME, userName);
    }

    private void logGatheredPermissions(String userName, List<Permission> permissions) {
        StringBuilder permissionBuilder = new StringBuilder();
        for (Permission permission : permissions) {
            permissionBuilder.append(permission.getName()).append(LIST_SEPARATOR);
        }
        permissionBuilder.setLength(permissionBuilder.length() - LIST_SEPARATOR.length());
        LOG.info(MessageFormat.format("Successfully loaded permissions for user: {0} - [{1}]", userName, permissionBuilder.toString()));
    }

    private void handleErrors(String userName, Map<String, Object> errors, String uri) {
        StringBuilder errorBuilder = new StringBuilder();
        for (String errorKeys : errors.keySet()) {
            errorBuilder.append(errorKeys).append(" - ").append(errors.get(errorKeys)).append(LIST_SEPARATOR);
        }
        errorBuilder.setLength(errorBuilder.length() - LIST_SEPARATOR.length());
        LOG.error(MessageFormat.format("Error occured when loading user[{0}] permissions from uri[{1}]: [{2}]", userName, uri, errorBuilder.toString()));
    }

    private CacheConfiguration populatePersonaCacheConfiguration() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration(CMS_PERSONA_CACHE_NAME, 20);
        cacheConfiguration.setTimeToLiveSeconds(300l);
        return cacheConfiguration;
    }
}
