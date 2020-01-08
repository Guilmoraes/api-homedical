package br.com.homedical.service;

import br.com.homedical.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    Patient save(Patient patient);

    Patient update(Patient patient);

    Page<Patient> findAll(String query, Pageable pageable);

    Patient findOne(String id);

    void delete(String id);
}
