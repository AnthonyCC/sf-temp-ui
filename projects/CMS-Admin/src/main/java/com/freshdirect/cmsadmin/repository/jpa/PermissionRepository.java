package com.freshdirect.cmsadmin.repository.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.freshdirect.cmsadmin.domain.Permission;

/**
 * Repository for permission.
 */
public interface PermissionRepository extends CrudRepository<Permission, Long> {

    @Override
    List<Permission> findAll();
}
