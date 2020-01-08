package br.com.homedical.service;

import br.com.homedical.domain.Duty;
import br.com.homedical.domain.Patient;
import br.com.homedical.domain.Professional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ProfessionalService {

    Professional save(Professional professional);

    Professional update(Professional professional);

    Page<Professional> findAll(Pageable pageable);

    Page<Professional> findAllByName(Pageable pageable, String name);

    Professional findOne(String id);

    Professional registerProfessional(Professional professionalRegisterDTO, String password, String confirmPassword);

    Professional getMe();

    Page<Professional> getProfessionalDocuments(Pageable pageable);

    Set<Patient> getProfessionalPatients(String id);

    List<Patient> updateProfessionalPatients(String id, List<Patient> patients);

    List<Duty> updateDutyProfessional(String idProfessional, List<Duty> duties);

    List<Professional> searchProfessional(String name);

    Set<Duty> listProfessionalWithDuties(String id);
}
