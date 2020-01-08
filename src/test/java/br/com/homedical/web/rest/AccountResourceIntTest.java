package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {
    "classpath:sql/account/homedical_user_account_test.sql",
    "classpath:sql/account/homedical_user_authority_account_test.sql",
    "classpath:sql/account/homedical_phone_account_test.sql",
    "classpath:sql/account/homedical_address_account_test.sql",
    "classpath:sql/account/homedical_professional_account_test.sql"})

public class AccountResourceIntTest {


    private static final String USER_EMAIL = "rafael@esparta.io";
    private static final String USER_PASSWORD = "homedical123";
    private static final String USER_WRONG_EMAIL = "user";
    private static final String USER_WRONG_PASSWORD = "user";
    private final Logger log = LoggerFactory.getLogger(AccountResourceIntTest.class);
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc restAccountMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restAccountMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void checkIfUserHadAcceptedTermsWithWrongRole() throws Exception {

        MvcResult mvcResult = restAccountMvc.perform(get("/api/terms/check-if-user-had-accepted")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restAccountMvc, USER_WRONG_EMAIL, USER_WRONG_PASSWORD))
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isForbidden())
            .andReturn();

    }

    @Test
    @Transactional
    public void checkIfUserHadAcceptedTermsWithoutRole() throws Exception {

        MvcResult mvcResult = restAccountMvc.perform(get("/api/terms/check-if-user-had-accepted")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized())
            .andReturn();

    }

    @Test
    @Transactional
    public void checkIfUserHadAcceptedTerm() throws Exception {

        MvcResult mvcResult = restAccountMvc.perform(get("/api/terms/check-if-user-had-accepted")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restAccountMvc, USER_EMAIL, USER_PASSWORD))
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent())
            .andReturn();
    }

    @Test
    @Transactional
    public void acceptTermAsProfessionalWithWrongRole() throws Exception {
        MvcResult mvcResult = restAccountMvc.perform(post("/api/terms/accept")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restAccountMvc, USER_WRONG_EMAIL, USER_WRONG_PASSWORD))
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void acceptTermAsProfessionalWithoutAuth() throws Exception {
        MvcResult mvcResult = restAccountMvc.perform(post("/api/terms/accept")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void acceptTermAsProfessional() throws Exception {
        MvcResult mvcResult = restAccountMvc.perform(post("/api/terms/accept")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restAccountMvc, USER_EMAIL, USER_PASSWORD))
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andReturn();

        restAccountMvc.perform(get("/api/terms/check-if-user-had-accepted")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restAccountMvc, USER_EMAIL, USER_PASSWORD))
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isAccepted())
            .andReturn().getResponse().getContentAsString();

    }
}
