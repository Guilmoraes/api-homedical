package br.com.homedical.service.impl;

import br.com.homedical.domain.Patient;
import br.com.homedical.repository.HealthOperatorRepository;
import br.com.homedical.repository.PatientRepository;
import br.com.homedical.service.PatientService;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_CREATE_PATIENT_HEALTH_OPERATOR_NOT_FOUND;


@Service
public class PatientServiceImpl implements PatientService {

    private final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientRepository patientRepository;

    private final HealthOperatorRepository healthOperatorRepository;

    public PatientServiceImpl(PatientRepository patientRepository,
                              HealthOperatorRepository healthOperatorRepository) {
        this.patientRepository = patientRepository;
        this.healthOperatorRepository = healthOperatorRepository;
    }


    @Override
    public Patient save(Patient patient) {
        log.debug("Request to save Patient : {}", patient);

        checkHealthOperator(patient);

        return patientRepository.save(patient);
    }

    @Override
    public Patient update(Patient patient) {
        log.debug("Request to update Patient : {}", patient);

        checkHealthOperator(patient);

        return patientRepository.save(patient);
    }

    @Override
    public Page<Patient> findAll(String query, Pageable pageable) {
        log.debug("Request to get all Patients");
        return patientRepository.findAllByNameContaining(query, pageable);
    }


    @Override
    public Patient findOne(String id) {
        log.debug("Request to get Patient : {}", id);
        return patientRepository.findOne(id);
    }


    @Override
    public void delete(String id) {
        log.debug("Request to delete Patient : {}", id);

        try {
            patientRepository.delete(id);
        } catch (Exception e) {
            throw new BusinessException(ErrorConstants.ERROR_DELETE_PATIENT_NOT_ALLOWED);
        }
    }

    private void checkHealthOperator(Patient patient) {

        if (!healthOperatorRepository.findById(patient.getHealthOperator().getId()).isPresent()) {
            throw new BusinessException(ERROR_CREATE_PATIENT_HEALTH_OPERATOR_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }
}
