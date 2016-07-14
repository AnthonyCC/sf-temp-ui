package com.freshdirect.cmsadmin.web;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.freshdirect.cmsadmin.SpringTestExecutionListener;
import com.freshdirect.cmsadmin.category.RestTest;
import com.freshdirect.cmsadmin.config.DataSourceConfig;

/**
 * REST integration test cases for default view controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
@Category(RestTest.class)
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class DefaultViewControllerRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * Initialize variable(s) before every test case.
     */
    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void readDefaultViewPageWithoutLogin() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(null, null));
        mockMvc.perform(get(DefaultViewController.DEFAULT_VIEW_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("")))
                .andExpect(jsonPath("$.menuItems", hasSize(1))).andExpect(jsonPath("$.menuItems[0].name", is("Home")))
                .andExpect(jsonPath("$.menuItems[0].path", is(DefaultViewController.DEFAULT_VIEW_PATH)));
    }

    @Test
    public void readDefaultViewPageWithLoginUserIsNotAssociated() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("username", null));
        mockMvc.perform(get(DefaultViewController.DEFAULT_VIEW_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("username")))
                .andExpect(jsonPath("$.menuItems", hasSize(1))).andExpect(jsonPath("$.menuItems[0].name", is("Home")))
                .andExpect(jsonPath("$.menuItems[0].path", is(DefaultViewController.DEFAULT_VIEW_PATH)));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:db/TEST-insert_permissionmanager_user.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) )})
    public void readDefaultViewPageWithLoginUserIsAssociatedAndHasPermissionEditorPermission() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("root", null));
        mockMvc.perform(get(DefaultViewController.DEFAULT_VIEW_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root")))
                .andExpect(jsonPath("$.menuItems", hasSize(3))).andExpect(jsonPath("$.menuItems[0].name", is("Home")))
                .andExpect(jsonPath("$.menuItems[0].path", is(DefaultViewController.DEFAULT_VIEW_PATH)));
    }

    @Test
    public void readDefaultViewPageWithLoginUserIsAssociatedAndHasNotPermissionEditorPermission() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("aszabo", null));
        mockMvc.perform(get(DefaultViewController.DEFAULT_VIEW_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("aszabo")))
                .andExpect(jsonPath("$.menuItems", hasSize(1))).andExpect(jsonPath("$.menuItems[0].name", is("Home")))
                .andExpect(jsonPath("$.menuItems[0].path", is(DefaultViewController.DEFAULT_VIEW_PATH)));
    }

}
