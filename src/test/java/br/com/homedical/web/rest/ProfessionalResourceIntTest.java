package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.Specialty;
import br.com.homedical.domain.enumeration.PhoneType;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.patient.PatientIdDTO;
import br.com.homedical.facade.dto.professional.ProfessionalDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterByAdminDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyProfessionalDTO;
import br.com.homedical.facade.mapper.ProfessionalMapper;
import br.com.homedical.facade.mapper.SpecialtyMapper;
import br.com.homedical.repository.CityRepository;
import br.com.homedical.repository.ProfessionalRepository;
import br.com.homedical.repository.SpecialtyRepository;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.dto.PhoneDTO;
import br.com.homedical.service.mapper.CityMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ProfessionalResource REST controller.
 *
 * @see ProfessionalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_specialty_professional_test.sql", "classpath:sql/homedical_user_professional_test.sql", "classpath:sql/homedical_user_authority_professional_test.sql", "classpath:sql/homedical_phone_professional_test.sql", "classpath:sql/homedical_address_professional_test.sql", "classpath:sql/homedical_patient_professional_test.sql", "classpath:sql/homedical_professional_professional_test.sql", "classpath:sql/homedical_professional_patients_professional_test.sql", "classpath:sql/homedical_professional_specialties_professional_test.sql", "classpath:sql/homedical_document_test.sql", "classpath:sql/homedical_duty_test.sql"})
public class ProfessionalResourceIntTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc restProfessionalMockMvc;

    private final Logger log = LoggerFactory.getLogger(ProfessionalResourceIntTest.class);
    private final String EMAIL = "r@s.com";
    private final String PASSWORD = "homedical123";
    private final String WRONG_PASSWORD = "homedical12";
    private final String NAME = "Kevin";
    private final String CPF = "21644788195";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private SpecialtyMapper specialtyMapper;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private ProfessionalRepository professionalRepository;
    @Autowired
    private ProfessionalMapper professionalMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restProfessionalMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @Transactional
    public void getProfessionalsWithWrongRole() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalsWithoutRole() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionals() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        Page<ProfessionalDTO> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<CustomPageImpl<SpecialtyDTO>>() {
        });

        assertThat(page.getContent().size() > 0);
    }

    @Test
    @Transactional
    public void getProfessionalsHaveDocumentsToApproveWithoutRole() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/documents")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalsHaveDocumentsToApproveWithWrongRole() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/documents")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();

    }

    @Test
    @Transactional
    public void getProfessionalsHaveDocumentsToApprove() throws Exception {

        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/documents")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();

        Page<ProfessionalDTO> page = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<CustomPageImpl<SpecialtyDTO>>() {
        });

        assertThat(page.getContent().size() > 0);
    }

    @Test
    @Transactional
    public void getOneProfessionalWithWrongRole() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}", "1010")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneProfessionalWithoutRole() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}", "1010")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneProfessionalWithWrongId() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}", "1010")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void getOneProfessional() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalInfoWithoutAuth() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/me")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalInfoWithWrongAuth() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/me")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalInfo() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/me")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "rafael@esparta.io", "homedical123")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSearchProfessionalWithoutAuth() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/searchlist")
            .with(csrf())
            .param("name", "rAfa"))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSearchProfessionalWithWrongAuth() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/searchlist")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "rafael@esparta.io", "homedical123"))
            .param("name", "rAfa"))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSearchProfessional() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/searchlist")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .param("name", "rAfa"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void SearchProfessionalAndReturnZeroElements() throws Exception {
        restProfessionalMockMvc.perform(get("/api/professionals/searchlist")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .param("name", "Teste"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.totalElements").value(0))
            .andReturn();
    }

    @Test
    @Transactional
    public void SearchProfessionalAndReturnOneElement() throws Exception {
        restProfessionalMockMvc.perform(get("/api/professionals/searchlist")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .param("name", "Rafael"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.totalElements").value(1))
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimself() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithNameBlank() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setName("");
        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithoutEmail() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setEmail(null);
        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithSamePassword() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setPassword(PASSWORD);
        professionalRegisterDTO.setConfirmPassword(WRONG_PASSWORD);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithoutPhone() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setPhone(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithoutAddress() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setAddress(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithoutSpecialty() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.setSpecialties(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void profissionalRegisterHimselfWithWrongSpecialty() throws Exception {

        ProfessionalRegisterDTO professionalRegisterDTO = createProfessionalRegisterDto();

        professionalRegisterDTO.getSpecialties().get(0).setId("123231331231");

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/register/professional")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterDTO)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessional() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithWrongRole() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutName() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setName("");

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutCpf() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setCpf("");

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutPhone() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setPhone(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutAddress() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setAddress(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutSpecialties() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setSpecialties(null);

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void createProfessionalWithoutEmail() throws Exception {

        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = createProfessionalRegisterByAdminDto();
        professionalRegisterByAdminDTO.setEmail("");

        MvcResult mvcResult = restProfessionalMockMvc.perform(post("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalRegisterByAdminDTO)))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessional() throws Exception {

        ProfessionalDTO professionalDTO = professionalMapper.toDto(professionalRepository.findById("179a277e-b7a8-4e86-90f5-d5cb4b94465d").get());

        professionalDTO.setName("Rafassss");
        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessionalWithWrongRole() throws Exception {

        ProfessionalDTO professionalDTO = professionalMapper.toDto(professionalRepository.findById("179a277e-b7a8-4e86-90f5-d5cb4b94465d").get());

        professionalDTO.setName("Rafassss");
        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(professionalDTO)))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalPatientsWithRoleAdmin() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalPatientsWithRoleProfessional() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "rafael@esparta.io", "homedical123")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalPatientsWithWrongId() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}/patients", "1010")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin")))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalPatientsWithWrongRole() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void getProfessionalPatientsWithoutAuth() throws Exception {
        MvcResult mvcResult = restProfessionalMockMvc.perform(get("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn();
    }


    @Test
    @Transactional
    public void updateProfessionalDuties() throws Exception {

        List<DutyDTO> dutiesDTO = new ArrayList<>();

        DutyDTO duties = new DutyDTO();

        duties.setId("9b129f27-d304-4217-9f73-5f4b680e2f0a");
        dutiesDTO.add(duties);
        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals/{id}/duties", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutiesDTO)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessionalPatients() throws Exception {

        List<PatientIdDTO> patientIdDTOS = new ArrayList<>();

        PatientIdDTO patientIdDTO = new PatientIdDTO();
        patientIdDTO.setId("d58b5656-cb3a-49ed-93f3-fee2d10d76b3");
        patientIdDTOS.add(patientIdDTO);

        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientIdDTOS)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessionalDutyWitchWrongId() throws Exception {

        List<DutyDTO> dutiesDTO = new ArrayList<>();

        DutyDTO duties = new DutyDTO();
        duties.setId("123455");
        dutiesDTO.add(duties);

        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals/{id}/duties", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dutiesDTO)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessionalPatientsWithWrongPatientId() throws Exception {

        List<PatientIdDTO> patientIdDTOS = new ArrayList<>();

        PatientIdDTO patientIdDTO = new PatientIdDTO();
        patientIdDTO.setId("fee2d10d76b3");
        patientIdDTOS.add(patientIdDTO);

        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals/{id}/patients", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientIdDTOS)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    @Transactional
    public void updateProfessionalWithWrongId() throws Exception {

        List<PatientIdDTO> patientIdDTOS = new ArrayList<>();

        PatientIdDTO patientIdDTO = new PatientIdDTO();
        patientIdDTO.setId("fee2d10d76b3");
        patientIdDTOS.add(patientIdDTO);

        MvcResult mvcResult = restProfessionalMockMvc.perform(put("/api/professionals/{id}/patients", "1010")
            .header("Authorization", "Bearer " + getAccessToken(restProfessionalMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patientIdDTOS)))
            .andExpect(status().isNotFound())
            .andReturn();
    }

    private ProfessionalRegisterByAdminDTO createProfessionalRegisterByAdminDto() {
        ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO = new ProfessionalRegisterByAdminDTO();
        professionalRegisterByAdminDTO.setName(NAME);
        professionalRegisterByAdminDTO.setEmail(EMAIL);
        professionalRegisterByAdminDTO.setCpf(CPF);

        Specialty specialty = specialtyRepository.findById("f2819742-4597-47ab-b2cb-9424592dea80").get();
        List<SpecialtyProfessionalDTO> specialtyDTOS = new ArrayList<>();
        specialtyDTOS.add(specialtyMapper.fromEntityToSpecialtyProfessionalDto(specialty));
        professionalRegisterByAdminDTO.setSpecialties(specialtyDTOS);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(cityMapper.toSimpleDto(cityRepository.findOne("A2878")));
        professionalRegisterByAdminDTO.setAddress(addressDTO);

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setAreaCode(55);
        phoneDTO.setNumber("998153717");
        phoneDTO.setType(PhoneType.CELLULAR);
        professionalRegisterByAdminDTO.setPhone(phoneDTO);

        return professionalRegisterByAdminDTO;
    }


    private ProfessionalRegisterDTO createProfessionalRegisterDto() {
        ProfessionalRegisterDTO professionalRegisterDTO = new ProfessionalRegisterDTO();
        professionalRegisterDTO.setName(NAME);
        professionalRegisterDTO.setEmail(EMAIL);
        professionalRegisterDTO.setPassword(PASSWORD);
        professionalRegisterDTO.setConfirmPassword(PASSWORD);
        professionalRegisterDTO.setCpf(CPF);

        Specialty specialty = specialtyRepository.findById("f2819742-4597-47ab-b2cb-9424592dea80").get();
        List<SpecialtyProfessionalDTO> specialtyDTOS = new ArrayList<>();
        specialtyDTOS.add(specialtyMapper.fromEntityToSpecialtyProfessionalDto(specialty));
        professionalRegisterDTO.setSpecialties(specialtyDTOS);

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity(cityMapper.toSimpleDto(cityRepository.findOne("A2878")));
        professionalRegisterDTO.setAddress(addressDTO);

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setAreaCode(55);
        phoneDTO.setNumber("998153717");
        phoneDTO.setType(PhoneType.CELLULAR);
        professionalRegisterDTO.setPhone(phoneDTO);

        return professionalRegisterDTO;
    }


}
