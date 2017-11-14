package com.freshdirect.cms.ui.editor.permission.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.permission.domain.Permission;
import com.freshdirect.cms.ui.editor.permission.domain.Persona;
import com.freshdirect.cms.ui.editor.permission.domain.PersonaConversionData;
import com.freshdirect.cms.ui.editor.permission.domain.PersonaConverter;
import com.freshdirect.cms.ui.editor.permission.domain.PersonaWrapper;

@Service
public class PersonaService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaService.class);
    private static final String LIST_SEPARATOR = ", ";

    public static final String CMS_PERSONA_CACHE_NAME = "cmsPersona";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private PropertyResolverService propertyResolverService;

    public PersonaWrapper loadPermissionsForUser(String userName) {
        boolean updateUser = false;
        Persona persona = getPersonaFromCache(userName);
        if (persona == null) {
            PersonaConversionData personaConversionData = null;
            String uri = propertyResolverService.getCmsAdminServiceUri() + MessageFormat.format("/api/user/{0}/persona", userName);
            LOG.debug(MessageFormat.format("Try to load user[{0}] permissions from uri[{1}]", userName, uri));
            RestTemplate getPersonaDataTemplate = new RestTemplate();
            try {
                personaConversionData = getPersonaDataTemplate.getForObject(uri, PersonaConversionData.class);
                if (personaConversionData.getErrors() == null) {
                    persona = PersonaConverter.defaultConverter().convert(personaConversionData);
                    putPersonaIntoCache(userName, persona);
                    updateUser = true;
                    logGatheredPermissions(userName, persona.getPermissions());
                } else {
                    handleErrors(userName, personaConversionData.getErrors(), uri);
                }
            } catch (HttpStatusCodeException e) {
                LOG.error("Exception while loading persona: " + userName + " [" + e.getMessage() + "] ");
                return new PersonaWrapper(createNoPermissionPersona(), updateUser);
            }
        }
        return new PersonaWrapper(persona, updateUser);
    }

    public void invalidatePersona(final String userName) {
        Cache personaCache = cacheManager.getCache(CMS_PERSONA_CACHE_NAME);
        personaCache.evict(userName);
    }

    private Persona getPersonaFromCache(String userName) {
        Cache personaCache = cacheManager.getCache(CMS_PERSONA_CACHE_NAME);
        ValueWrapper cachedPersona = personaCache.get(userName);
        Persona persona = null;
        if (cachedPersona != null) {
            persona = (Persona) cachedPersona.get();
        }
        return persona;
    }

    private void putPersonaIntoCache(String userName, Persona persona) {
        Cache personaCache = cacheManager.getCache(CMS_PERSONA_CACHE_NAME);
        personaCache.put(userName, persona);
    }

    private void logGatheredPermissions(String userName, List<Permission> permissions) {
        StringBuilder permissionBuilder = new StringBuilder();
        for (Permission permission : permissions) {
            permissionBuilder.append(permission.getName()).append(LIST_SEPARATOR);
        }
        permissionBuilder.setLength(permissionBuilder.length() - LIST_SEPARATOR.length());
        LOG.debug(MessageFormat.format("Successfully loaded permissions for user: {0} - [{1}]", userName, permissionBuilder.toString()));
    }

    private void handleErrors(String userName, Map<String, Object> errors, String uri) {
        StringBuilder errorBuilder = new StringBuilder();
        for (String errorKeys : errors.keySet()) {
            errorBuilder.append(errorKeys).append(" - ").append(errors.get(errorKeys)).append(LIST_SEPARATOR);
        }
        errorBuilder.setLength(errorBuilder.length() - LIST_SEPARATOR.length());
        LOG.error(MessageFormat.format("Error occured when loading user[{0}] permissions from uri[{1}]: [{2}]", userName, uri, errorBuilder.toString()));
    }

    private Persona createNoPermissionPersona() {
        Persona persona = new Persona();
        persona.setId(0l);
        persona.setName("No Permission");
        persona.setPermissions(new ArrayList<Permission>());
        return persona;
    }
}
