package com.freshdirect.cmsadmin.utils;

import java.util.ArrayList;

import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;

/**
 * Create entity with factory pattern.
 */
public final class EntityFactory {

    public static final Long ID = 0L;
    public static final Long OTHER_ID = 1L;
    public static final String PERSONA_NAME = "persona";
    public static final String PERMISSION_NAME = "permission";
    public static final String OTHER_PERMISSION_NAME = "other-permission";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String DRAFT_NAME = "draftName";
    public static final String DRAFT_CHANGE_CONTENTKEY = "cos_draftChange";
    public static final String DRAFT_CHANGE_ATTRIBUTE_NAME = "FULL_NAME";
    public static final String DRAFT_CHANGE_ATTRIBUTE_VALUE = "Test Full Name";

    private EntityFactory() {
    }

    /**
     * Create permission entity.
     *
     * @return new permission with default id and name.
     */
    public static Permission createPermission() {
        return createPermission(ID, PERMISSION_NAME);
    }

    /**
     * Create permission entity.
     *
     * @param id
     *            Long id
     * @param name
     *            String name
     *
     * @return new permission
     */
    public static Permission createPermission(Long id, String name) {
        Permission permission = new Permission();
        permission.setId(id);
        permission.setName(name);
        return permission;
    }

    /**
     * Create persona entity.
     *
     * @param permissions
     *            Permission... permissions
     * @return new persona with default id and name
     */
    public static Persona createPersona(Permission... permissions) {
        return createPersona(ID, PERSONA_NAME, permissions);
    }

    /**
     * Create persona entity.
     *
     * @param id
     *            Long id
     * @param name
     *            String name
     * @param permissions
     *            Permission... permissions
     * @return new persona
     */
    public static Persona createPersona(Long id, String name, Permission... permissions) {
        Persona persona = new Persona();
        persona.setId(id);
        persona.setName(name);
        persona.setPermissions(new ArrayList<Permission>());
        for (Permission permission : permissions) {
            persona.getPermissions().add(permission);
        }
        return persona;
    }

    /**
     * Create userpersona entity.
     *
     * @return new userPersona
     */
    public static UserPersona createUserPersona() {
        return createUserPersona(USER_ID, USER_NAME, createPersona());
    }

    /**
     * Create userpersona entity.
     *
     * @param userId
     *            String id
     * @param name
     *            String name
     * @param persona
     *            Persona persona
     * @return new userPersona
     */
    public static UserPersona createUserPersona(String userId, String name, Persona persona) {
        UserPersona userPersona = new UserPersona();
        userPersona.setUserId(userId);
        userPersona.setName(name);
        userPersona.setPersona(persona);
        return userPersona;
    }

    /**
     * Create user entity.
     *
     * @param id
     *            String id
     * @param fullName
     *            String fullName
     * @return new user
     */
    public static User createUser(String id, String fullName) {
        User user = new User();
        user.setAccountName(id);
        user.setFullName(fullName);
        return user;
    }

    public static Draft createDraft() {
        Draft draft = new Draft();
        draft.setId(ID);
        draft.setName(DRAFT_NAME);
        return draft;
    }

    public static Draft createDraft(long id, String name) {
        Draft draft = new Draft();
        draft.setId(id);
        draft.setName(name);
        return draft;
    }

    public static Draft createDraft(String name) {
        Draft draft = new Draft();
        draft.setId(null);
        draft.setName(name);
        return draft;
    }

    public static DraftChange createDraftChange() {
        DraftChange draftChange = new DraftChange();
        draftChange.setContentKey(DRAFT_CHANGE_CONTENTKEY);
        draftChange.setAttributeName(DRAFT_CHANGE_ATTRIBUTE_NAME);
        draftChange.setId(ID);
        draftChange.setValue(DRAFT_CHANGE_ATTRIBUTE_VALUE);
        draftChange.setDraft(createDraft(888L, "TestDraft"));
        return draftChange;
    }

    public static DraftChange createDraftChange(Draft draft) {
        DraftChange draftChange = new DraftChange();
        draftChange.setContentKey(DRAFT_CHANGE_CONTENTKEY);
        draftChange.setAttributeName(DRAFT_CHANGE_ATTRIBUTE_NAME);
        draftChange.setId(ID);
        draftChange.setValue(DRAFT_CHANGE_ATTRIBUTE_VALUE);
        draftChange.setDraft(draft);
        return draftChange;
    }

}
