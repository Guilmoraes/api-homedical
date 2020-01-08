package br.com.homedical.web.rest;

import br.com.homedical.HomedicalApp;
import br.com.homedical.domain.State;
import br.com.homedical.repository.StateRepository;
import br.com.homedical.service.StateService;
import br.com.homedical.service.dto.StateDTO;
import br.com.homedical.service.mapper.StateMapper;
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
 * Test class for the StateResource REST controller.
 *
 * @see StateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
public class StateResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private StateService stateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStateMockMvc;

    private State state;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StateResource stateResource = new StateResource(stateService );
        this.restStateMockMvc = MockMvcBuilders.standaloneSetup(stateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static State createEntity(EntityManager em) {
        State state = new State()
            .toBuilder()
            .name(DEFAULT_NAME)
            .acronym(DEFAULT_ACRONYM)
            .active(DEFAULT_ACTIVE)
            .build();
        return state;
    }

    @Before
    public void initTest() {
        state = createEntity(em);
    }

    @Test
    @Transactional
    public void createState() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isCreated());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate + 1);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testState.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testState.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createStateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();

        // Create the State with an existing ID
        state.setId("1L");
        StateDTO stateDTO = stateMapper.toDto(state);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc.perform(post("/api/states")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStates() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList
        restStateMockMvc.perform(get("/api/states?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.content[*].id").value(hasItem(state.getId())))
            .andExpect(jsonPath("$.content[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.content[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.content[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get the state
        restStateMockMvc.perform(get("/api/states/{id}", state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(state.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingState() throws Exception {
        // Get the state
        restStateMockMvc.perform(get("/api/states/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state
        state
            .toBuilder()
            .name(UPDATED_NAME)
            .acronym(UPDATED_ACRONYM)
            .active(UPDATED_ACTIVE)
            .build();
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc.perform(put("/api/states/{id}", state.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testState.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testState.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStateMockMvc.perform(put("/api/states/{id}", "NOPE")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isCreated());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);
        int databaseSizeBeforeDelete = stateRepository.findAll().size();

        // Get the state
        restStateMockMvc.perform(delete("/api/states/{id}", state.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = new State();
        state1.setId("1L");
        State state2 = new State();
        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);
        state2.setId("2L");
        assertThat(state1).isNotEqualTo(state2);
        state1.setId(null);
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateDTO.class);
        StateDTO stateDTO1 = new StateDTO();
        stateDTO1.setId("1L");
        StateDTO stateDTO2 = new StateDTO();
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
        stateDTO2.setId(stateDTO1.getId());
        assertThat(stateDTO1).isEqualTo(stateDTO2);
        stateDTO2.setId("2L");
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
        stateDTO1.setId(null);
        assertThat(stateDTO1).isNotEqualTo(stateDTO2);
    }
}
