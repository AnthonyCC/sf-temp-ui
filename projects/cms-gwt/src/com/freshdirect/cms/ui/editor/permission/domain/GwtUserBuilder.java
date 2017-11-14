package com.freshdirect.cms.ui.editor.permission.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.ui.editor.permission.converter.PermissionToContentTypeConverter;
import com.freshdirect.cms.ui.model.GwtUser;

public class GwtUserBuilder {

    private GwtUser user;
    private Persona persona;

    public GwtUserBuilder(String userName) {
        user = new GwtUser(userName);
    }

    public GwtUserBuilder setPersona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public GwtUser build() {
        if (persona != null) {
            List<Permission> permissions = persona.getPermissions();
            user.setPersonaName(persona.getName());
            user.setHasAccessToPermissionEditorApp(permissions.contains(Permission.PERMISSION_EDITOR));
            user.setCanChangeFDStore(permissions.contains(Permission.FD_STORE_EDITOR));
            user.setCanChangeFDXStore(permissions.contains(Permission.FDX_STORE_EDITOR));
            user.setHasAccessToAdminTab(permissions.contains(Permission.VIEW_ADMINISTRATION));
            user.setHasAccessToBulkLoaderTab(permissions.contains(Permission.BULK_LOAD));
            user.setHasAccessToChangesTab(permissions.contains(Permission.VIEW_CHANGES));
            user.setHasAccessToPublishTab(permissions.contains(Permission.STORE_PUBLISH));
            user.setHasAccessToFeedPublishTab(permissions.contains(Permission.FEED_PUBLISH));
            user.setHasAccessToDraftBranches(permissions.contains(Permission.DRAFT_EDITOR));
            user.setCanChangeOtherNodes(permissions.contains(Permission.OTHER_STORE_EDITOR));
            Set<ContentType> permittedContentTypes = new HashSet<ContentType>();
            for (Permission permission : permissions) {
                ContentType permittedContentType = PermissionToContentTypeConverter.defaultService().convert(permission);
                if (permittedContentType != null) {
                    permittedContentTypes.add(permittedContentType);
                }
            }
            Set<String> permittedContentTypesForClient = new HashSet<String>();
            for (ContentType contentType : permittedContentTypes) {
                permittedContentTypesForClient.add(contentType.toString());
            }
            user.setPermittedContentTypes(permittedContentTypesForClient);
        }
        return user;
    }
}
