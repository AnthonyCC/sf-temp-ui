package com.freshdirect.cmsadmin;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.freshdirect.cmsadmin.category.RestTest;
import com.freshdirect.cmsadmin.config.DataSourceConfig;

/**
 * REST integration test cases for security context.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DataSourceConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@Category(RestTest.class)
@TestExecutionListeners(listeners = SpringTestExecutionListener.class, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class SecurityRestTest {

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void loginWithCorrectCredentials() throws Exception {
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", "root").param("password", "12345678")).andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.user.name", is("root"))).andExpect(jsonPath("$.defaultPath", is("/api/home")));
    }

    @Test
    public void loginWithIncorrectCredentials() throws Exception {
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", "admin").param("password", "admin1")).andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.errors", not(empty()))).andExpect(jsonPath("$.errors.general", is("Bad Credentials. Try again!")));
    }

    @Test
    public void loginByUsingIncorrectRequestMethod() throws Exception {
        mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("username", "admin").param("password", "admin"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(get("/logout").contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(jsonPath("$", not(empty()))).andExpect(jsonPath("$.menuItems", hasSize(1)))
                .andExpect(jsonPath("$.menuItems[0].name", is("Home"))).andExpect(jsonPath("$.menuItems[0].path", is("/api/home"))).andExpect(jsonPath("$.user", nullValue()));
    }

    @Test
    public void readHomeViewPageWithoutAnyCredentials() throws Exception {
        mockMvc.perform(get("/api/home")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$.menuItems", hasSize(1))).andExpect(jsonPath("$.menuItems[0].name", is("Home"))).andExpect(jsonPath("$.menuItems[0].path", is("/api/home")))
                .andExpect(jsonPath("$.user", nullValue()));
    }

    @Test
    public void readNotWebViewPageWithoutCredentials() throws Exception {
        mockMvc.perform(get("/api")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void readNotPermissionViewPageWithoutCredentials() throws Exception {
        mockMvc.perform(get("/api/permission")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void readNotPersonaViewPageWithoutCredentials() throws Exception {
        mockMvc.perform(get("/api/persona")).andExpect(status().is3xxRedirection());
    }

    @Test
    public void readNotPersonaUserAssociationViewPageWithoutCredentials() throws Exception {
        mockMvc.perform(get("/api/associatepersona")).andExpect(status().is3xxRedirection());
    }

}
