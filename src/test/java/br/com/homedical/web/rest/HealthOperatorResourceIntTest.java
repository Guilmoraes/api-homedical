package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.HealthOperator;
import br.com.homedical.facade.HealthOperatorFacade;
import br.com.homedical.facade.dto.healthOperator.HealthOperatorDTO;
import br.com.homedical.facade.mapper.HealthOperatorMapper;
import br.com.homedical.repository.HealthOperatorRepository;
import br.com.homedical.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the HealthOperatorResource REST controller.
 *
 * @see HealthOperatorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
public class HealthOperatorResourceIntTest {

    private static final Integer DEFAULT_ANS_REGISTER = 1;
    private static final Integer UPDATED_ANS_REGISTER = 2;

    private static final String DEFAULT_SOCIAL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_REASON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OBS = false;
    private static final Boolean UPDATED_OBS = true;

    @Autowired
    private HealthOperatorRepository healthOperatorRepository;

    @Autowired
    private HealthOperatorMapper healthOperatorMapper;

    @Autowired
    private HealthOperatorFacade healthOperatorFacade;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHealthOperatorMockMvc;

    private HealthOperator healthOperator;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HealthOperator createEntity(EntityManager em) {
        HealthOperator healthOperator = new HealthOperator();
        return healthOperator;
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HealthOperatorResource healthOperatorResource = new HealthOperatorResource(healthOperatorFacade);
        this.restHealthOperatorMockMvc = MockMvcBuilders.standaloneSetup(healthOperatorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        healthOperator = createEntity(em);
    }

    @Test
    @Transactional
    public void createHealthOperator() throws Exception {
        int databaseSizeBeforeCreate = healthOperatorRepository.findAll().size();

        // Create the HealthOperator
        HealthOperatorDTO healthOperatorDTO = healthOperatorMapper.toDto(healthOperator);
        restHealthOperatorMockMvc.perform(post("/api/health-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthOperatorDTO)))
            .andExpect(status().isCreated());

        // Validate the HealthOperator in the database
        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeCreate + 1);
        HealthOperator testHealthOperator = healthOperatorList.get(healthOperatorList.size() - 1);
        assertThat(testHealthOperator.getAnsRegister()).isEqualTo(DEFAULT_ANS_REGISTER);
        assertThat(testHealthOperator.getSocialReason()).isEqualTo(DEFAULT_SOCIAL_REASON);
        assertThat(testHealthOperator.getObs()).isEqualTo(DEFAULT_OBS);
    }

    @Test
    @Transactional
    public void createHealthOperatorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = healthOperatorRepository.findAll().size();

        // Create the HealthOperator with an existing ID
        healthOperator.setId("1L");
        HealthOperatorDTO healthOperatorDTO = healthOperatorMapper.toDto(healthOperator);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthOperatorMockMvc.perform(post("/api/health-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthOperatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSocialReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = healthOperatorRepository.findAll().size();
        // set the field null
        healthOperator.setSocialReason(null);

        // Create the HealthOperator, which fails.
        HealthOperatorDTO healthOperatorDTO = healthOperatorMapper.toDto(healthOperator);

        restHealthOperatorMockMvc.perform(post("/api/health-operators")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthOperatorDTO)))
            .andExpect(status().isBadRequest());

        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHealthOperators() throws Exception {
        // Initialize the database
        healthOperatorRepository.saveAndFlush(healthOperator);

        // Get all the healthOperatorList
        restHealthOperatorMockMvc.perform(get("/api/health-operators?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.content[*].id").value(hasItem(healthOperator.getId())))
            .andExpect(jsonPath("$.content[*].ansRegister").value(hasItem(DEFAULT_ANS_REGISTER)))
            .andExpect(jsonPath("$.content[*].socialReason").value(hasItem(DEFAULT_SOCIAL_REASON.toString())))
            .andExpect(jsonPath("$.content[*].obs").value(hasItem(DEFAULT_OBS.booleanValue())));
    }

    @Test
    @Transactional
    public void getHealthOperator() throws Exception {
        // Initialize the database
        healthOperatorRepository.saveAndFlush(healthOperator);

        // Get the healthOperator
        restHealthOperatorMockMvc.perform(get("/api/health-operators/{id}", healthOperator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(healthOperator.getId()))
            .andExpect(jsonPath("$.ansRegister").value(DEFAULT_ANS_REGISTER))
            .andExpect(jsonPath("$.socialReason").value(DEFAULT_SOCIAL_REASON.toString()))
            .andExpect(jsonPath("$.obs").value(DEFAULT_OBS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHealthOperator() throws Exception {
        // Get the healthOperator
        restHealthOperatorMockMvc.perform(get("/api/health-operators/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHealthOperator() throws Exception {
        // Initialize the database
        healthOperatorRepository.saveAndFlush(healthOperator);
        int databaseSizeBeforeUpdate = healthOperatorRepository.findAll().size();

        // Update the healthOperator
        HealthOperatorDTO healthOperatorDTO = healthOperatorMapper.toDto(healthOperator);

        restHealthOperatorMockMvc.perform(put("/api/health-operators/{id}", healthOperator.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthOperatorDTO)))
            .andExpect(status().isOk());

        // Validate the HealthOperator in the database
        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeUpdate);
        HealthOperator testHealthOperator = healthOperatorList.get(healthOperatorList.size() - 1);
        assertThat(testHealthOperator.getAnsRegister()).isEqualTo(UPDATED_ANS_REGISTER);
        assertThat(testHealthOperator.getSocialReason()).isEqualTo(UPDATED_SOCIAL_REASON);
        assertThat(testHealthOperator.getObs()).isEqualTo(UPDATED_OBS);
    }

    @Test
    @Transactional
    public void updateNonExistingHealthOperator() throws Exception {
        int databaseSizeBeforeUpdate = healthOperatorRepository.findAll().size();

        // Create the HealthOperator
        HealthOperatorDTO healthOperatorDTO = healthOperatorMapper.toDto(healthOperator);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHealthOperatorMockMvc.perform(put("/api/health-operators/{id}", "NOPE")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthOperatorDTO)))
            .andExpect(status().isCreated());

        // Validate the HealthOperator in the database
        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHealthOperator() throws Exception {
        // Initialize the database
        healthOperatorRepository.saveAndFlush(healthOperator);
        int databaseSizeBeforeDelete = healthOperatorRepository.findAll().size();

        // Get the healthOperator
        restHealthOperatorMockMvc.perform(delete("/api/health-operators/{id}", healthOperator.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HealthOperator> healthOperatorList = healthOperatorRepository.findAll();
        assertThat(healthOperatorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthOperator.class);
        HealthOperator healthOperator1 = new HealthOperator();
        healthOperator1.setId("1L");
        HealthOperator healthOperator2 = new HealthOperator();
        healthOperator2.setId(healthOperator1.getId());
        assertThat(healthOperator1).isEqualTo(healthOperator2);
        healthOperator2.setId("2L");
        assertThat(healthOperator1).isNotEqualTo(healthOperator2);
        healthOperator1.setId(null);
        assertThat(healthOperator1).isNotEqualTo(healthOperator2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthOperatorDTO.class);
        HealthOperatorDTO healthOperatorDTO1 = new HealthOperatorDTO();
        healthOperatorDTO1.setId("1L");
        HealthOperatorDTO healthOperatorDTO2 = new HealthOperatorDTO();
        assertThat(healthOperatorDTO1).isNotEqualTo(healthOperatorDTO2);
        healthOperatorDTO2.setId(healthOperatorDTO1.getId());
        assertThat(healthOperatorDTO1).isEqualTo(healthOperatorDTO2);
        healthOperatorDTO2.setId("2L");
        assertThat(healthOperatorDTO1).isNotEqualTo(healthOperatorDTO2);
        healthOperatorDTO1.setId(null);
        assertThat(healthOperatorDTO1).isNotEqualTo(healthOperatorDTO2);
    }
}
