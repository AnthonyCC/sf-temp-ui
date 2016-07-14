package com.freshdirect.cmsadmin.business;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.freshdirect.cmsadmin.ConsoleMockitoJUnitRunner;
import com.freshdirect.cmsadmin.category.UnitTest;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.repository.jpa.PermissionRepository;
import com.freshdirect.cmsadmin.utils.EntityFactory;

/**
 * Unit test cases for test PermissionService methods.
 */
@RunWith(ConsoleMockitoJUnitRunner.class)
@Category(UnitTest.class)
public class PermissionServiceTest {

    private static final Long ID = 0L;

    @InjectMocks
    private PermissionService underTest;

    @Mock
    private PermissionRepository repository;

    @Test
    public void testLoadAllPermissions() {
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(EntityFactory.createPermission()));
        List<Permission> allPermissions = underTest.loadAllPermissions();
        Assert.assertFalse(allPermissions.isEmpty());
    }

    @Test
    public void testLoadPermissionWithId() {
        Mockito.when(repository.findOne(ID)).thenReturn(EntityFactory.createPermission());
        Permission permission = underTest.loadPermission(ID);
        Assert.assertNotNull(permission);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadPermissionWithNullId() {
        underTest.loadPermission(null);
        Assert.fail("Service accepts null id!!!");
    }
}
