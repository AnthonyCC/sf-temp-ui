package com.freshdirect.cmsadmin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.cmsadmin.web.dto.DefaultPage;

/**
 * Default view providing controller.
 *
 * @author pkovacs
 *
 */
@RestController
public class DefaultViewController {

    public static final String DEFAULT_VIEW_PATH = "/api/home";

    @Autowired
    private PageDecorator pageDecorator;

    /**
     * Populate default page based on user authenticated state.
     *
     * @return default page
     */
    @RequestMapping(value = DEFAULT_VIEW_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultPage loadDefaultView() {
        return populateDefaultPage();
    }

    private DefaultPage populateDefaultPage() {
        return pageDecorator.decorateBasicPage(new DefaultPage());
    }
}
