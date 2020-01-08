package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.facade.dto.adddress.AddressPatientDTO;
import br.com.homedical.facade.dto.patient.PatientCreateDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.facade.dto.phone.PhonePatientDTO;
import br.com.homedical.facade.mapper.HealthOperatorMapper;
import br.com.homedical.facade.mapper.PatientMapper;
import br.com.homedical.repository.HealthOperatorRepository;
import br.com.homedical.repository.PatientRepository;
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

import java.time.LocalDate;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_phone_patient_test.sql", "classpath:sql/homedical_address_patient_test.sql", "classpath:sql/homedical_patient_patient_test.sql"})
public class PatientResourceIntTest {

    private final Logger log = LoggerFactory.getLogger(PatientResourceIntTest.class);

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc restPatientMockMvc;

    private String NAME = "Rafael";

    private LocalDate LOCAL_DATE = LocalDate.now();

    @Autowired
    private HealthOperatorRepository healthOperatorRepository;

    @Autowired
    private HealthOperatorMapper healthOperatorMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientMapper patientMapper;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restPatientMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void getPatientsWithWrongRole() throws Exception {

        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getPatientsWithoutRole() throws Exception {

        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getPatients() throws Exception {

        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOnePatient() throws Exception {

        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e14d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOnePatientWithWrongId() throws Exception {

        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e1")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOnePatientWithoutAuth() throws Exception {
        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e1")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOnePatientWithWrongRole() throws Exception {
        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e1")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOnePatient() throws Exception {
        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e14d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }


    @Test
    @Transactional
    public void deleteOnePatientWithoutRole() throws Exception {
        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e14d")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void deleteOnePatientWithWrongId() throws Exception {
        MvcResult mvcResult = restPatientMockMvc.perform(get("/api/patients/{id}", "4479e19c-614d-497e-977f-889963f1e")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithoutAuth() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithWrongRole() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithoutName() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();
        patientCreateDTO.setName("");
        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithoutBirthdate() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();
        patientCreateDTO.setBirthdate(null);

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithoutPhone() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();
        patientCreateDTO.setPhone(null);

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithoutAddress() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();
        patientCreateDTO.setAddress(null);

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createPatientWithWrongHealthOperator() throws Exception {
        PatientCreateDTO patientCreateDTO = createPatientCreateDto();
        patientCreateDTO.getHealthOperator().setId("1231231");

        MvcResult mvcResult = restPatientMockMvc.perform(post("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientCreateDTO)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void updatePatientWithoutAuth() throws Exception {
        PatientDTO patientDTO = patientMapper.toDto(patientRepository.findOne("4479e19c-614d-497e-977f-889963f1e14d"));
        patientDTO.setName("10101");

        MvcResult mvcResult = restPatientMockMvc.perform(put("/api/patients")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void updatePatientWithWrongRole() throws Exception {
        PatientDTO patientDTO = patientMapper.toDto(patientRepository.findOne("4479e19c-614d-497e-977f-889963f1e14d"));
        patientDTO.setName("10101");

        MvcResult mvcResult = restPatientMockMvc.perform(put("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        PatientDTO patientDTO = patientMapper.toDto(patientRepository.findOne("4479e19c-614d-497e-977f-889963f1e14d"));
        patientDTO.setName("10101");

        MvcResult mvcResult = restPatientMockMvc.perform(put("/api/patients")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restPatientMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    private PatientCreateDTO createPatientCreateDto() {
        PatientCreateDTO patientCreateDTO = new PatientCreateDTO();
        patientCreateDTO.setName(NAME);
        patientCreateDTO.setBirthdate(LOCAL_DATE);
        healthOperatorRepository.findById("9bce054d-c07b-4224-b1ff-5224b9232700").ifPresent(healthOperator -> patientCreateDTO.setHealthOperator(healthOperatorMapper.toDto(healthOperator)));

        AddressPatientDTO addressPatientDTO = new AddressPatientDTO();
        addressPatientDTO.setStateName("PR");
        addressPatientDTO.setCityName("Curitiba");
        addressPatientDTO.setStreet("Rua Martim Afonso, 1310");
        addressPatientDTO.setLat(-60.00);
        addressPatientDTO.setLng(-60.00);
        patientCreateDTO.setAddress(addressPatientDTO);

        PhonePatientDTO phonePatientDTO = new PhonePatientDTO();
        phonePatientDTO.setNumber("42998153717");
        patientCreateDTO.setPhone(phonePatientDTO);

        patientCreateDTO.setClinicalCondition("123");
        return patientCreateDTO;
    }

}
