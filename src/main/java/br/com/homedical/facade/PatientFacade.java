package br.com.homedical.facade;

import br.com.homedical.domain.City;
import br.com.homedical.domain.Patient;
import br.com.homedical.facade.dto.patient.PatientCreateDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.facade.mapper.PatientMapper;
import br.com.homedical.repository.CityRepository;
import br.com.homedical.service.PatientService;
import br.com.homedical.service.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_CREATE_PATIENT_CITY_NOT_FOUND;


@Service
public class PatientFacade {

    private final Logger log = LoggerFactory.getLogger(PatientFacade.class);

    private final CityRepository cityRepository;

    private final PatientService patientService;

    private final PatientMapper patientMapper;

    public PatientFacade(CityRepository cityRepository,
                         PatientService patientService,
                         PatientMapper patientMapper) {
        this.cityRepository = cityRepository;
        this.patientService = patientService;
        this.patientMapper = patientMapper;
    }

    @Transactional
    public PatientDTO save(PatientCreateDTO patientDTO) {
        log.debug("Request to save Patient : {}", patientDTO);

        Patient patient = patientMapper.fromPatientCreateDtoToEntity(patientDTO);

        mapPatientCity(patient, patientDTO);

        return patientMapper.toDto(patientService.save(patient));
    }

    @Transactional
    public PatientDTO update(PatientDTO patientDTO) {
        log.debug("Request to update Patient : {}", patientDTO);
        Patient patient = patientMapper.toEntity(patientDTO);

        mapPatientCity(patient, patientDTO);

        return patientMapper.toDto(patientService.update(patient));
    }

    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(String query, Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientService.findAll(query, pageable)
            .map(patientMapper::toDto);
    }


    @Transactional(readOnly = true)
    public PatientDTO findOne(String id) {
        log.debug("Request to get Patient : {}", id);
        Patient patient = patientService.findOne(id);
        return patientMapper.toDto(patient);
    }


    public void delete(String id) {
        log.debug("Request to delete Patient : {}", id);
        patientService.delete(id);
    }

    private void mapPatientCity(Patient patient, PatientDTO patientDTO) {
        if (patient.getAddress().getCity() == null) {
            patient.getAddress().setCity(findCity(patientDTO.getAddress().getCityName(), patientDTO.getAddress().getStateName()));
        }
    }

    private void mapPatientCity(Patient patient, PatientCreateDTO patientDTO) {
        patient.getAddress().setCity(findCity(patientDTO.getAddress().getCityName(), patientDTO.getAddress().getStateName()));
    }

    private City findCity(String cityName, String stateName) {
        return cityRepository.findByNameAndState_Acronym(cityName, stateName)
            .orElseThrow(() -> new BusinessException(ERROR_CREATE_PATIENT_CITY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
