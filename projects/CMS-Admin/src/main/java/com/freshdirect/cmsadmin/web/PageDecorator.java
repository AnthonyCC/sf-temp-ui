package com.freshdirect.cmsadmin.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.freshdirect.cmsadmin.business.MenuService;
import com.freshdirect.cmsadmin.config.security.SecurityContextWrapper;
import com.freshdirect.cmsadmin.config.security.dto.UserData;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.MenuItem;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.domain.User;
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.web.dto.BasicPage;
import com.freshdirect.cmsadmin.web.dto.DraftManagementPage;
import com.freshdirect.cmsadmin.web.dto.GroupedPersonaAssociationPage;
import com.freshdirect.cmsadmin.web.dto.GroupedUserPersona;
import com.freshdirect.cmsadmin.web.dto.PersonaAssociationPage;
import com.freshdirect.cmsadmin.web.dto.PersonaPage;
import com.freshdirect.cmsadmin.web.dto.PersonaSelectablePage;
import com.freshdirect.cmsadmin.web.dto.PersonaWithSelectablePermission;
import com.freshdirect.cmsadmin.web.dto.converter.PersonaToSelectablePermissionPersonaConverter;
import com.freshdirect.cmsadmin.web.dto.converter.UserPersonaToGroupedUserPersonaConverter;
import com.freshdirect.cmsadmin.web.dto.converter.UserToUserDataConverter;

@Component
public class PageDecorator {

    @Autowired
    private MenuService menuService;

    @Autowired
    private SecurityContextWrapper securityContextWrapper;

    @Autowired
    private UserToUserDataConverter userDataConverter;

    @Autowired
    private UserPersonaToGroupedUserPersonaConverter groupedUserPersonaConverter;

    @Autowired
    private PersonaToSelectablePermissionPersonaConverter personaToSelectablePermissionPersonaConverter;

    public PersonaAssociationPage decoratePersonaAssociationPage(PersonaAssociationPage page, List<UserPersona> userPersonas, List<User> unassociatedUsers) {
        page = decorateBasicPage(page);
        if (securityContextWrapper.isUserAuthenticated()) {
            page.setUserPersonas(userPersonas);
            page.setUnassociatedUsers(userDataConverter.convert(unassociatedUsers));
        }
        return page;
    }

    public PersonaPage decoratePersonaPage(PersonaPage page, List<Persona> personas) {
        page = decorateBasicPage(page);
        if (securityContextWrapper.isUserAuthenticated()) {
            page.setPersonas(personas);
        }
        return page;
    }

    public PersonaSelectablePage decoratePersonaSelectablePage(PersonaSelectablePage page, List<Persona> personas) {
        page = decorateBasicPage(page);
        if (securityContextWrapper.isUserAuthenticated()) {
            List<PersonaWithSelectablePermission> selectablePersonas = personaToSelectablePermissionPersonaConverter.convert(personas);
            page.setPersonas(personas);
            page.setSelectablePersonas(selectablePersonas);
        }
        return page;
    }

    public GroupedPersonaAssociationPage decorateGroupedPersonaAssociationPage(GroupedPersonaAssociationPage page, List<UserPersona> userPersonas, List<User> unassociatedUsers) {
        page = decorateBasicPage(page);
        if (securityContextWrapper.isUserAuthenticated()) {
            List<GroupedUserPersona> groupedUserPersonas = groupedUserPersonaConverter.convert(userPersonas);
            page.setUnassociatedUsers(userDataConverter.convert(unassociatedUsers));
            page.setUserPersonas(userPersonas);
            page.setGroupedUserPersonas(groupedUserPersonas);
        }
        return page;
    }

    public DraftManagementPage decorateDraftManagementPage(DraftManagementPage page, List<Draft> drafts) {
        page = decorateBasicPage(page);
        if (securityContextWrapper.isUserAuthenticated()) {
            page.setDrafts(drafts);
        }
        return page;
    }


    public <T extends BasicPage> T decorateBasicPage(T page) {
        Assert.notNull(page, "page can't be null");
        if (securityContextWrapper.isUserAuthenticated()) {
            String userName = securityContextWrapper.getAuthenticatedUserName();
            page.setMenuItems(menuService.loadMenu(userName));
            UserData userData = new UserData();
            userData.setId(userName);
            userData.setName(userName);
            page.setUser(userData);
        } else {
            List<MenuItem> menuItems = new ArrayList<MenuItem>(1);
            menuItems.add(MenuItem.DEFAULT_PAGE);
            page.setMenuItems(menuItems);
        }
        return page;
    }
}
