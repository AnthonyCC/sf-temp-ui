package com.freshdirect.cmsadmin.web;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlConfig.TransactionMode;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.freshdirect.cmsadmin.category.RestTest;
import com.freshdirect.cmsadmin.config.DataSourceConfig;
import com.freshdirect.cmsadmin.domain.Draft;
import com.freshdirect.cmsadmin.domain.DraftChange;
import com.freshdirect.cmsadmin.utils.EntityFactory;
import com.freshdirect.cmsadmin.utils.JsonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataSourceConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@Category(RestTest.class)
public class DraftChangeControllerRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * Initialize variable(s) before every test case.
     */
    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("root", null));
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draft.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draftchange.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate_drafts.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void getDraftChanges() throws Exception {
        mockMvc.perform(get(DraftChangeController.CMS_DRAFT_CHANGE_PATH).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$..value", contains("Demo Cat")))
                .andExpect(jsonPath("$..contentKey", contains("Category:TeztCat"))).andExpect(jsonPath("$..attributeName", contains("FULL_NAME")));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draft.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate_drafts.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void createDraftChange() throws Exception {
        List<DraftChange> draftChange = Arrays.asList(EntityFactory.createDraftChange());
        mockMvc.perform(post(DraftChangeController.CMS_DRAFT_CHANGE_PATH).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(draftChange)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$..contentKey", contains("cos_draftChange"))).andExpect(jsonPath("$..attributeName", contains("FULL_NAME")))
                .andExpect(jsonPath("$..value", contains("Test Full Name")));
    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draft.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draftchange.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate_drafts.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void getDraftChangesByDraft() throws Exception {
        Draft draft = EntityFactory.createDraft(888L, "TestDraft");
        mockMvc.perform(
                get(DraftChangeController.CMS_DRAFT_CHANGE_ACTION_PATH, draft.getId()).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(draft)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$..value", contains("Demo Cat"))).andExpect(jsonPath("$..contentKey", contains("Category:TeztCat")))
                .andExpect(jsonPath("$..attributeName", contains("FULL_NAME")));

    }

    @Test
    @SqlGroup({
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draft.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = {
                    "classpath:db/TEST-insert_draftchange.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ),
            @Sql(scripts = "classpath:db/TEST-truncate_drafts.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = TransactionMode.ISOLATED) ) })
    public void getDraftChangesByInvalidDraft() throws Exception {
        Draft draft = EntityFactory.createDraft(999L, "TestDraft");
        mockMvc.perform(
                get(DraftChangeController.CMS_DRAFT_CHANGE_ACTION_PATH, draft.getId()).contentType(MediaType.APPLICATION_JSON).content(JsonUtils.convertObjectToJsonBytes(draft)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", empty()));

    }

}
