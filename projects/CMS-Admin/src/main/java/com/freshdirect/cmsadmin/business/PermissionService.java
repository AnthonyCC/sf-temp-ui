package com.freshdirect.cmsadmin.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.repository.jpa.PermissionRepository;

/**
 * Service for permission.
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository repository;

    /**
     * Return with all permissions.
     *
     * @return allPermissions
     */
    public List<Permission> loadAllPermissions() {
        return repository.findAll();
    }

    /**
     * Return with permission associated with given id.
     *
     * @param id
     *            Long id
     *
     * @return permission
     */
    public Permission loadPermission(Long id) {
        Assert.notNull(id, "id can't be null");
        return repository.findOne(id);
    }
}
