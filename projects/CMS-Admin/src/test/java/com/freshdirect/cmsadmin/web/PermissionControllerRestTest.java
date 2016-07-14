package com.freshdirect.cmsadmin.web;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
 * REST integration test cases for permission controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataSourceConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@Category(RestTest.class)
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class PermissionControllerRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * Initialize variable(s) before every test case.
     */
    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("root", "12345678"));
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:db/TEST-insert_permissions_for_persona.sql",
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void readPermissionForValidPersona() throws Exception {
        mockMvc.perform(get(PermissionController.PERMISSION_PERSONA_ACTION_PATH, 4).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void readPermissionForInvalidPersona() throws Exception {
        mockMvc.perform(get(PermissionController.PERMISSION_PERSONA_ACTION_PATH, 1337).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
    }

}
