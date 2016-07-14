package com.freshdirect.cmsadmin.web;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.freshdirect.cmsadmin.domain.Permission;
import com.freshdirect.cmsadmin.domain.Persona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.utils.JsonUtils;

/**
 * REST integration test cases for persona controller.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataSourceConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@Category(RestTest.class)
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
@SqlGroup({
        @Sql(scripts = {
                "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
        @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
public class PersonaControllerRestTest {

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
    public void getPersonaPageForValidPersona() throws Exception {
        mockMvc.perform(get(PersonaController.PERSONA_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root")))
                .andExpect(jsonPath("$.selectablePersonas", not(empty())));
    }

    @Test
    public void postValidPersona() throws Exception {
        Persona persona = EntityFactory.createPersona(0L, "Can modify FD storedata.");
        mockMvc.perform(post(PersonaController.PERSONA_PAGE_PATH).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(persona)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$.user.name", is("root"))).andExpect(jsonPath("$.menuItems", not(empty()))).andExpect(jsonPath("$.selectablePersonas", hasSize(5)));
    }

    @Test
    public void validateEmptyPersonaNameReturnsClientErrorOnCreatePersona() throws Exception {
        Permission permission = EntityFactory.createPermission(1L, "Can modify FD store.");
        Persona persona = EntityFactory.createPersona(5L, "", permission);
        mockMvc.perform(post(PersonaController.PERSONA_PAGE_PATH).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(persona)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void validateNullPersonaNameReturnsClientErrorOnCreatePersona() throws Exception {
        Permission permission = EntityFactory.createPermission(1L, "Can modify FD store.");
        Persona persona = EntityFactory.createPersona(5L, null, permission);
        mockMvc.perform(post(PersonaController.PERSONA_PAGE_PATH).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(persona)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void putPermissionToPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(1L, "Can modify FD store.");
        mockMvc.perform(put(PersonaController.PERSONA_ACTION_PATH, 4L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().isOk()).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root"))).andExpect(jsonPath("$.menuItems", not(empty())))
                .andExpect(jsonPath("$.selectablePersonas", not(empty())));
    }

    @Test
    public void validateNullPermissionIdReturnsClientErrorOnAddPermissionToPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(null, "Can modify FD store.");
        mockMvc.perform(put(PersonaController.PERSONA_ACTION_PATH, 4L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void validateNullPersonaReturnsClientErrorOnAddPermissionToPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(null, "Can modify FD store.");
        mockMvc.perform(put(PersonaController.PERSONA_ACTION_PATH, 0L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_permission_for_persona.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = {
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void deletePermissionFromPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(1L, "Can modify FD store.");
        mockMvc.perform(delete(PersonaController.PERSONA_ACTION_PATH, 4L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().isOk()).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root"))).andExpect(jsonPath("$.menuItems", not(empty())))
                .andExpect(jsonPath("$.selectablePersonas", not(empty())));
    }

    @Test
    public void validateNullPermissionIdReturnsClientErrorOnDeletePermissionFromPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(null, "Can modify FD store.");
        mockMvc.perform(delete(PersonaController.PERSONA_ACTION_PATH, 4L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void validateNullPersonaReturnsClientErrorOnDeletePermissionFromPersona() throws Exception {
        Permission permission = EntityFactory.createPermission(1L, "Can modify FD store.");
        mockMvc.perform(delete(PersonaController.PERSONA_ACTION_PATH, 0L).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(permission)))
                .andExpect(status().is4xxClientError());
    }

}
