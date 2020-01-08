package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.Schedule;
import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.facade.dto.DutySimpleDTO;
import br.com.homedical.facade.dto.ScheduleDTO;
import br.com.homedical.facade.dto.patient.PatientSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import br.com.homedical.repository.ScheduleRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.homedical.web.rest.util.AuthenticationUtil.getAccessToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the ScheduleResource REST controller.
 *
 * @see ScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Sql(scripts = {"classpath:sql/homedical_address_patient_test.sql", "classpath:sql/homedical_phone_patient_test.sql", "classpath:sql/homedical_patient_patient_test.sql", "classpath:sql/homedical_user_professional_test.sql", "classpath:sql/homedical_phone_professional_test.sql", "classpath:sql/homedical_address_professional_test.sql", "classpath:sql/homedical_professional_professional_test.sql", "classpath:sql/homedical_user_authority_professional_test.sql", "classpath:sql/homedical_duty_test.sql", "classpath:sql/homedical_schedule_test.sql"})
public class ScheduleResourceIntTest {

    private static final ZonedDateTime DEFAULT_START = ZonedDateTime.of(LocalDateTime.of(2018, 04, 10, 13, 00, 00), ZoneId.of("America/Sao_Paulo"));

    private static final ZonedDateTime DEFAULT_FINISH = ZonedDateTime.of(LocalDateTime.of(2018, 04, 10, 18, 00, 00), ZoneId.of("America/Sao_Paulo"));

    private static final ZonedDateTime DEFAULT_FILTER_START = ZonedDateTime.of(LocalDateTime.of(2018, 04, 10, 0, 0, 0), ZoneId.of("America/Sao_Paulo"));

    private static final ZonedDateTime DEFAULT_FILTER_FINISH = ZonedDateTime.of(LocalDateTime.of(2018, 04, 11, 0, 0, 0), ZoneId.of("America/Sao_Paulo"));

    private static final ZonedDateTime DEFAULT_ALTERNATIVE_FILTER_START = ZonedDateTime.of(LocalDateTime.of(2018, 04, 9, 0, 0, 0), ZoneId.of("America/Sao_Paulo"));

    private static final SchedulesStatus DEFAULT_STATUS = SchedulesStatus.APPROVED;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private MockMvc restScheduleMockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.restScheduleMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
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
    public void createSchedule() throws Exception {
        ScheduleDTO schedule = createScheduleDto();
        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void createScheduleWithoutAuth() throws Exception {
        ScheduleDTO schedule = createScheduleDto();
        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void createScheduleWithoutRoleWrong() throws Exception {
        ScheduleDTO schedule = createScheduleDto();
        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "user", "user"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void createScheduleWithExistingId() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setId("9b129f27-d304-4217-9f73-5f4b680e2f0a");

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void createScheduleWithoutId() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setId(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void createScheduleWithFieldStartEmpty() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setStart(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createScheduleWithFieldFinishEmpty() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setFinish(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createScheduleWithoutPatient() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setPatient(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createScheduleWithoutProfessional() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setProfessional(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createScheduleWithoutDuties() throws Exception {
        ScheduleDTO schedule = createScheduleDto();

        schedule.setDuty(null);

        restScheduleMockMvc.perform(post("/api/schedules")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAllSchedules() throws Exception {

        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
        ).andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getAllWitchoutAuth() throws Exception {

        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc")
            .with(csrf())
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getAllSchedulesWrongRole() throws Exception {

        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "user", "user"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getSchedule() throws Exception {

        Schedule schedule = scheduleRepository.findById("1b443056-7857-4e12-b195-38ae46030e66").get();

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId())
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getScheduleWithoutAuth() throws Exception {

        Schedule schedule = scheduleRepository.findById("1b443056-7857-4e12-b195-38ae46030e66").get();

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId())
            .with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getScheduleWithRoleWrong() throws Exception {

        Schedule schedule = scheduleRepository.findById("1b443056-7857-4e12-b195-38ae46030e66").get();

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId())
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "user", "user")))
            .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    public void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", "123445")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin")))
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getProfessionalWithSchedule() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/professional", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportProfessionalsForDate() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/professional", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin")))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportProfessionalsForDateIdProfessionalWrong() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/professional", "1234")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin")))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportProfessionalsForDateRoleWrong() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/professional", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "user", "user")))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportProfessionalsForDateWithoutAuth() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/professional", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf()))
            .andExpect(status().isUnauthorized());
    }


    @Test
    @Transactional
    public void reportFilterSchedulesForDate() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .param("start", DEFAULT_START.toString())
            .param("finish", DEFAULT_FINISH.toString()))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateReturnZeroElements() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .param("start", DEFAULT_START.toString())
            .param("finish", DEFAULT_FINISH.toString()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.totalElements").value(0))
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateReturnCorrectTotalElements() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .param("start", DEFAULT_FILTER_START.toString())
            .param("finish", DEFAULT_FILTER_FINISH.toString()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath(".totalElements").value(1))
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateReturnCorrectValues() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .param("start", DEFAULT_ALTERNATIVE_FILTER_START.toString())
            .param("finish", DEFAULT_FILTER_FINISH.toString()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath(".totalElements").value(2))
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateIdProfessionalWrong() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "1234")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .param("start", DEFAULT_START.toString())
            .param("finish", DEFAULT_FINISH.toString()))
            .andExpect(status().isBadRequest())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateRoleWrong() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "user", "user"))
            .param("start", DEFAULT_START.toString())
            .param("finish", DEFAULT_FINISH.toString()))
            .andExpect(status().isForbidden())
            .andReturn();
    }

    @Test
    @Transactional
    public void reportFilterSchedulesForDateWithoutAuth() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/report", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .param("start", DEFAULT_START.toString())
            .param("finish", DEFAULT_FINISH.toString()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void getSchedulePending() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/pending")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
        ).andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void getSchedulePendingForProfessional() throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules/{id}/pending/", "179a277e-b7a8-4e86-90f5-d5cb4b94465d")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
        ).andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Transactional
    public void approvedSchedule() throws Exception {
        ScheduleDTO schedule = createScheduleDto();
        schedule.setId("1b443056-7857-4e12-b195-38ae46030e66");
        schedule.setStatus(SchedulesStatus.REFUSED);
        restScheduleMockMvc.perform(put("/api/schedules/approved")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void approvedScheduleStatusWrong() throws Exception {
        ScheduleDTO schedule = createScheduleDto();
        schedule.setId("1b443056-7857-4e12-b195-38ae46030e66");
        schedule.setStatus(SchedulesStatus.PENDING);
        restScheduleMockMvc.perform(put("/api/schedules/approved")
            .with(csrf())
            .header("Authorization", "Bearer " + getAccessToken(restScheduleMockMvc, "admin", "admin"))
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());
    }

    private ScheduleDTO createScheduleDto() {

        ScheduleDTO schedule = new ScheduleDTO();
        PatientSimpleDTO patient = new PatientSimpleDTO();
        ProfessionalSimpleDTO professional = new ProfessionalSimpleDTO();

        DutySimpleDTO duty = new DutySimpleDTO();

        duty.setId("9b129f27-d304-4217-9f73-5f4b680e2f0a");
        duty.setName("Plantao Casa do João");

        patient.setId("4479e19c-614d-497e-977f-889963f1e14d");
        patient.setName("Rafael Pacientee");

        professional.setId("179a277e-b7a8-4e86-90f5-d5cb4b94465d");
        professional.setName("Rafael Filipake");

        schedule.setStatus(DEFAULT_STATUS);
        schedule.setStart(DEFAULT_START);
        schedule.setFinish(DEFAULT_FINISH);
        schedule.setPatient(patient);
        schedule.setProfessional(professional);
        schedule.setDuty(duty);

        return schedule;
    }
}
