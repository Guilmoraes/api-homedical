package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.Duty;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.mapper.DutyMapper;
import br.com.homedical.repository.DutyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.List;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the DutyResource REST controller.
 *
 * @see DutyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_duty_test.sql"})
public class DutyResourceIntTest {

    private static final String DEFAULT_NAME = "Plantao Casa do João";
    private static final String UPDATED_NAME = "Plantao Casa do Fabricio";

    private static final LocalTime DEFAULT_START = LocalTime.of(12, 00, 00);
    private static final LocalTime UPDATED_START = LocalTime.of(13, 00, 00);

    private static final LocalTime DEFAULT_FINISH = LocalTime.of(18, 00, 00);
    private static final LocalTime UPDATED_FINISH = LocalTime.of(19, 00, 00);

    private static final LocalTime DEFAULT_OVERTIME = LocalTime.of(00, 30);
    private static final LocalTime UPDATED_OVERTIME = LocalTime.of(01, 00);

    private static final Double DEFAULT_PRICE = 99.49;
    private static final Double UPDATED_PRICE = 123.56;

    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private DutyMapper dutyMapper;

    private MockMvc restDutyMockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restDutyMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */

    @Test
    @Transactional
    public void createDuty() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    public void createDutyWithoutAuth() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void createDutyWithWrongRole() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void createDutyWithExistingId() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();

        dutyDTO.setId("9b129f27-d304-4217-9f73-5f4b680e2f0a");

        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createDutyWitchFieldNameEmpty() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        dutyDTO.setName("");

        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createDutyWitchFieldStartEmpty() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        dutyDTO.setStart(null);

        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createDutyWitchFieldFinishEmpty() throws Exception {
        DutyDTO dutyDTO = createdDutyDto();
        dutyDTO.setFinish(null);

        restDutyMockMvc.perform(post("/api/duties")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAllDutiesWithRoleAdmin() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Get all the dutyList
        restDutyMockMvc.perform(get("/api/duties?sort=id,desc")
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getAllDutiesWithoutRole() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();
        // Get all the dutyList
        restDutyMockMvc.perform(get("/api/duties?sort=id,desc")
            .with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getAllDutiesWithRoleUser() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();
        // Get all the dutyList
        restDutyMockMvc.perform(get("/api/duties?sort=id,desc")
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "user", "user")))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getDuty() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Get the duty
        restDutyMockMvc.perform(get("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getDutyWithoutRole() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Get the duty
        restDutyMockMvc.perform(get("/api/duties/{id}", duty.getId())
            .with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getDutyWithRoleWrong() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Get the duty
        restDutyMockMvc.perform(get("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "user", "user")))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getNonExistingDuty() throws Exception {

        restDutyMockMvc.perform(get("/api/duties/{id}", "1234")
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void updateDuty() throws Exception {

        int databaseSizeBeforeUpdate = dutyRepository.findAll().size();

        Duty duty = dutyRepository.findOne("9b129f27-d304-4217-9f73-5f4b680e2f0a");

        duty.setName(UPDATED_NAME);
        duty.setStart(UPDATED_START);
        duty.setOvertime(UPDATED_OVERTIME);
        duty.setFinish(UPDATED_FINISH);
        duty.setPrice(UPDATED_PRICE);

        DutyDTO dutyDTO = dutyMapper.toDto(duty);

        restDutyMockMvc.perform(put("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isOk());

        // Validate the Duty in the database
        List<Duty> dutyList = dutyRepository.findAll();
        assertThat(dutyList).hasSize(databaseSizeBeforeUpdate);
        Duty testDuty = dutyList.get(dutyList.size() - 1);
        assertThat(testDuty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDuty.getStart()).isEqualTo(UPDATED_START);
        assertThat(testDuty.getFinish()).isEqualTo(UPDATED_FINISH);
        assertThat(testDuty.getOvertime()).isEqualTo(UPDATED_OVERTIME);
        assertThat(testDuty.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateDutyWitchoutRole() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Create the Duty
        DutyDTO dutyDTO = dutyMapper.toDto(duty);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDutyMockMvc.perform(put("/api/duties/{id}", duty.getId())
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void updateDutyWitchRoleWrong() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Create the Duty
        DutyDTO dutyDTO = dutyMapper.toDto(duty);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDutyMockMvc.perform(put("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutyDTO)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void deleteDuty() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        // Get the duty
        restDutyMockMvc.perform(delete("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void deleteDutyIdNoExist() throws Exception {

        restDutyMockMvc.perform(delete("/api/duties/{id}", "1234")
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "admin", "admin"))
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteDutyWrongRole() throws Exception {

        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        restDutyMockMvc.perform(delete("/api/duties/{id}", duty.getId())
            .with(csrf()).header("Authorization", "Bearer " + getAccessToken(restDutyMockMvc, "user", "user"))
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void deleteWithoutRole() throws Exception {
        Duty duty = dutyRepository.findById("9b129f27-d304-4217-9f73-5f4b680e2f0a").get();

        restDutyMockMvc.perform(delete("/api/duties/{id}", duty.getId())
            .with(csrf())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isUnauthorized());
    }

    private DutyDTO createdDutyDto() {

        DutyDTO dutyDTO = new DutyDTO();

        dutyDTO.setName(DEFAULT_NAME);
        dutyDTO.setStart(DEFAULT_START);
        dutyDTO.setFinish(DEFAULT_FINISH);
        dutyDTO.setOvertime(DEFAULT_OVERTIME);
        dutyDTO.setPrice(DEFAULT_PRICE);

        return dutyDTO;
    }

}
