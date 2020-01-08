package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.enumeration.ObjectStatus;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyUpdateDTO;
import br.com.homedical.web.rest.util.CustomPageImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Objects;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the SpecialtyResource REST controller.
 *
 * @see SpecialtyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_specialty_specialty_test.sql"})
public class SpecialtyResourceIntTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc restSpecialtyMockMvc;

    private final Logger log = LoggerFactory.getLogger(SpecialtyResourceIntTest.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restSpecialtyMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void getSpecialtiesWithWrongRole() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSpecialtiesWithoutRole() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSpecialties() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        Page<SpecialtyDTO> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<CustomPageImpl<SpecialtyDTO>>() {
        });

        assertThat(page.getContent().size() > 0);
    }

    @Test
    @Transactional
    public void getEnabledSpecialtiesWithWrongRole() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/enabled")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getEnabledSpecialtiesWithoutAuth() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/enabled")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getEnabledSpecialtiesWithAuth() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/enabled")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        List<SpecialtyDTO> specialties = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<SpecialtyDTO>>() {
        });

        assertThat(specialties.size() == 1);
    }


    @Test
    @Transactional
    public void getOneSpecialtyWithWrongId() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "101010")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneSpecialty() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneSpecialtyWithoutRole() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "101010")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneSpecialtyWithWrongRole() throws Exception {
        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialtyWithEmptyFields() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialtyWithId() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        specialtyDTO.setId("1203");
        specialtyDTO.setName("Teste03");
        specialtyDTO.setStatus(ObjectStatus.ENABLED);

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialtyWithNameBlank() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        specialtyDTO.setName("");
        specialtyDTO.setStatus(ObjectStatus.ENABLED);

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialtyWithoutRole() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        specialtyDTO.setName("Teste05");
        specialtyDTO.setStatus(ObjectStatus.ENABLED);

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialtyWithWrongRole() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        specialtyDTO.setName("Teste05");
        specialtyDTO.setStatus(ObjectStatus.ENABLED);

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void createSpecialty() throws Exception {

        SpecialtyDTO specialtyDTO = new SpecialtyDTO();

        specialtyDTO.setName("Teste05");
        specialtyDTO.setStatus(ObjectStatus.ENABLED);

        MvcResult mvcResult = restSpecialtyMockMvc.perform(post("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyDTO)))
            .andExpect(status().isCreated())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateSpecialty() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO specialtyDTO = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResult.getResponse().getContentAsString());
        SpecialtyUpdateDTO specialtyUpdateDTO = new SpecialtyUpdateDTO();
        specialtyUpdateDTO.setName("Teste 8172387");
        specialtyUpdateDTO.setStatus(specialtyDTO.getStatus());
        specialtyUpdateDTO.setId(specialtyDTO.getId());

        MvcResult mvcResponse = restSpecialtyMockMvc.perform(put("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyUpdateDTO)))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO updated = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResponse.getResponse().getContentAsString());

        assertThat(!Objects.equals(updated.getName(), specialtyDTO.getName()));

    }

    @Test
    @Transactional
    public void updateSpecialtyWithoutId() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO specialtyDTO = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResult.getResponse().getContentAsString());
        SpecialtyUpdateDTO specialtyUpdateDTO = new SpecialtyUpdateDTO();
        specialtyUpdateDTO.setName("Teste 8172387");
        specialtyUpdateDTO.setStatus(specialtyDTO.getStatus());

        MvcResult mvcResponse = restSpecialtyMockMvc.perform(put("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyUpdateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateSpecialtyWithNameBlank() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO specialtyDTO = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResult.getResponse().getContentAsString());
        SpecialtyUpdateDTO specialtyUpdateDTO = new SpecialtyUpdateDTO();
        specialtyUpdateDTO.setName("");
        specialtyUpdateDTO.setStatus(specialtyDTO.getStatus());
        specialtyUpdateDTO.setId(specialtyDTO.getId());

        MvcResult mvcResponse = restSpecialtyMockMvc.perform(put("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyUpdateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();

    }

    @Test
    @Transactional
    public void updateSpecialtyWithoutRole() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO specialtyDTO = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResult.getResponse().getContentAsString());
        SpecialtyUpdateDTO specialtyUpdateDTO = new SpecialtyUpdateDTO();
        specialtyUpdateDTO.setName("HUE");
        specialtyUpdateDTO.setStatus(specialtyDTO.getStatus());
        specialtyUpdateDTO.setId(specialtyDTO.getId());

        MvcResult mvcResponse = restSpecialtyMockMvc.perform(put("/api/specialties")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyUpdateDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();

    }

    @Test
    @Transactional
    public void updateSpecialtyWithWrongRole() throws Exception {

        MvcResult mvcResult = restSpecialtyMockMvc.perform(get("/api/specialties/{id}", "b7ea10fb-2e27-4c5c-bff6-bada747e1bd6")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        SpecialtyDTO specialtyDTO = objectMapper.readerFor(SpecialtyDTO.class).readValue(mvcResult.getResponse().getContentAsString());
        SpecialtyUpdateDTO specialtyUpdateDTO = new SpecialtyUpdateDTO();
        specialtyUpdateDTO.setName("HUE");
        specialtyUpdateDTO.setStatus(specialtyDTO.getStatus());
        specialtyUpdateDTO.setId(specialtyDTO.getId());

        MvcResult mvcResponse = restSpecialtyMockMvc.perform(put("/api/specialties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restSpecialtyMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(specialtyUpdateDTO)))
            .andExpect(status().isForbidden())
            .andReturn();

    }


}
