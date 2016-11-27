package com.freshdirect.cmsadmin.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cmsadmin.domain.MenuItem;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.PermissionIdentifier;
import com.freshdirect.cmsadmin.domain.UserPersona;

/**
 * Service for menu.
 */
@Service
public class MenuService {

    @Autowired
    private PersonaAssociationService personaAssociationService;

    @Autowired
    private PermissionService permissionService;

    /**
     * Return with all menu items.
     *
     * @return menuitems
     */
    public List<MenuItem> loadMenu(String userId) {
        List<MenuItem> list = new ArrayList<MenuItem>(Arrays.asList(MenuItem.values()));
        UserPersona userPersonaAssociation = personaAssociationService.loadUserPersonaAssociation(userId);
        if (userPersonaAssociation != null) {
            Permission permissionEditorPermission = permissionService.loadPermission(PermissionIdentifier.PERMISSION_EDITOR.id);
            if (!userPersonaAssociation.getPersona().getPermissions().contains(permissionEditorPermission)) {
                list.remove(MenuItem.PERSONA_PAGE);
                list.remove(MenuItem.PERSONA_USER_ASSOCIATION_PAGE);
            }

            Permission draftManagerPermission = permissionService.loadPermission(PermissionIdentifier.DRAFT_MANAGER.id);
            if (!userPersonaAssociation.getPersona().getPermissions().contains(draftManagerPermission)) {
                list.remove(MenuItem.DRAFT_MANAGEMENT_PAGE);
            }
        } else {
            list.remove(MenuItem.PERSONA_PAGE);
            list.remove(MenuItem.PERSONA_USER_ASSOCIATION_PAGE);
            list.remove(MenuItem.DRAFT_MANAGEMENT_PAGE);
        }
        return list;
    }
}
