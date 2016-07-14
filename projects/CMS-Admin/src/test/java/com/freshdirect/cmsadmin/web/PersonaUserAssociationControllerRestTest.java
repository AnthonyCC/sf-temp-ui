package com.freshdirect.cmsadmin.web;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.freshdirect.cmsadmin.domain.UserPersona;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.utils.JsonUtils;

/**
 * REST integration test cases for persona user association controller.
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
        @Sql(scripts = {
                "classpath:db/TEST-truncate-relations.sql" }, executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
public class PersonaUserAssociationControllerRestTest {

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
            @Sql(scripts = { "classpath:db/TEST-insert_userpersona.sql",
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void readPersonaAssociationPage() throws Exception {
        mockMvc.perform(get(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root")))
                .andExpect(jsonPath("$.menuItems", not(empty()))).andExpect(jsonPath("$.groupedUserPersonas", not(empty()))).andExpect(jsonPath("$.unassociatedUsers", hasSize(1)));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:db/TEST-insert_userpersona.sql",
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void listUsersAssociatedWithPersona() throws Exception {
        mockMvc.perform(get(PersonaUserAssociationController.USER_PERSONA_ASSOCATIE_ACTION_PATH, 1L).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root")))
                .andExpect(jsonPath("$.menuItems", not(empty()))).andExpect(jsonPath("$.groupedUserPersonas", hasSize(1))).andExpect(jsonPath("$.unassociatedUsers", hasSize(1)));
    }

    @Test

    @SqlGroup({
            @Sql(scripts = { "classpath:db/TEST-insert_userpersona.sql",
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void listUsersAssociatedWithPersonaIdIsDummy() throws Exception {
        mockMvc.perform(get(PersonaUserAssociationController.USER_PERSONA_ASSOCATIE_ACTION_PATH, "dummyId").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void listUsersAssociatedPersonaWherePersonaIsNull() throws Exception {
        mockMvc.perform(get(PersonaUserAssociationController.USER_PERSONA_ASSOCATIE_ACTION_PATH, 0l).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @SqlGroup({
            @Sql(scripts = { "classpath:db/TEST-insert_userpersona.sql",
                    "classpath:db/TEST-insert_permissionmanager_user.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void removeUserPersonaAssociation() throws Exception {
        mockMvc.perform(delete(PersonaUserAssociationController.USER_PERSONA_ASSOCATIE_ACTION_PATH, "admin").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root")))
                .andExpect(jsonPath("$.menuItems", not(empty()))).andExpect(jsonPath("$.groupedUserPersonas", hasSize(1))).andExpect(jsonPath("$.unassociatedUsers", hasSize(2)));
    }

    @Test
    public void removeUserPersonaAssociationWhichUserIsNotInLdap() throws Exception {
        mockMvc.perform(delete(PersonaUserAssociationController.USER_PERSONA_ASSOCATIE_ACTION_PATH, "userNotInLdap").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void addUserPersonaAssociation() throws Exception {
        UserPersona userPersona = EntityFactory.createUserPersona("admin", null, EntityFactory.createPersona(1L, "Admin"));
        mockMvc.perform(post(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.convertObjectToJsonBytes(userPersona))).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root"))).andExpect(jsonPath("$.menuItems", not(empty())))
                .andExpect(jsonPath("$.groupedUserPersonas[0].users", hasSize(2))).andExpect(jsonPath("$.unassociatedUsers", hasSize(1)));
    }

    @Test
    public void addUserPersonaAssociationWhichUserIsNotInLdap() throws Exception {
        UserPersona userPersona = EntityFactory.createUserPersona("userNotInLdap", null, EntityFactory.createPersona(1L, "Admin"));
        mockMvc.perform(post(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.convertObjectToJsonBytes(userPersona))).andExpect(status().is4xxClientError()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_userpersona.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate-relations.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void listPersonaGivenUser() throws Exception {
        mockMvc.perform(get(PersonaUserAssociationController.USER_ASSOCIATED_PERSONA_PATH, "admin").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty())));
    }

    @Test
    public void validateNullUserPersonaUserIdReturnsClientErrorOnAddUserToPersona() throws Exception {
        UserPersona userPersona = EntityFactory.createUserPersona("", null, EntityFactory.createPersona(1L, "Admin"));
        mockMvc.perform(post(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.convertObjectToJsonBytes(userPersona))).andExpect(status().is4xxClientError());
    }

    @Test
    public void validateNullPersonaIdReturnsClientErrorOnAddUserToPersona() throws Exception {
        UserPersona userPersona = EntityFactory.createUserPersona("uid=bob,ou=people", null, EntityFactory.createPersona(null, "Admin"));
        mockMvc.perform(post(PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.convertObjectToJsonBytes(userPersona))).andExpect(status().is4xxClientError());
    }
}
