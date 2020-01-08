package br.com.homedical.service.impl;

import br.com.homedical.domain.Document;
import br.com.homedical.domain.Duty;
import br.com.homedical.domain.Patient;
import br.com.homedical.domain.Professional;
import br.com.homedical.domain.Specialty;
import br.com.homedical.domain.User;
import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.event.ProfessionalRegistrationByAdminEmailEvent;
import br.com.homedical.event.ProfessionalRegistrationEmailEvent;
import br.com.homedical.repository.DocumentRepository;
import br.com.homedical.repository.DutyRepository;
import br.com.homedical.repository.PatientRepository;
import br.com.homedical.repository.ProfessionalRepository;
import br.com.homedical.repository.SpecialtyRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.repository.impl.ScheduleQueryRepository;
import br.com.homedical.security.SecurityUtils;
import br.com.homedical.service.ProfessionalService;
import br.com.homedical.service.UserService;
import br.com.homedical.service.exceptions.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PROFESSIONAL_ID_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_GET_PROFESSIONAL_SELF_INFORMATION_PROFESSIONAL_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_ID_DUTY_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_PROFESSIONAL_EMAIL_ALREADY_USED;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_REGISTER_PROFESSIONAL_PASSWORD_MUST_BE_EQUAL;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_REGISTER_PROFESSIONAL_SPECIALTY_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_UPDATE_PROFESSIONAL_ID_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_UPDATE_PROFESSIONAL_PATIENTS_PATIENT_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_UPDATE_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND;


@Service
public class ProfessionalServiceImpl implements ProfessionalService {

    private final Logger log = LoggerFactory.getLogger(ProfessionalServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    private final ProfessionalRepository professionalRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final PatientRepository patientRepository;

    private final ScheduleQueryRepository scheduleQueryRepository;

    private final DocumentRepository documentRepository;

    private final DutyRepository dutyRepository;

    public ProfessionalServiceImpl(SpecialtyRepository specialtyRepository, ProfessionalRepository professionalRepository, UserRepository userRepository, UserService userService, ApplicationEventPublisher publisher, PatientRepository patientRepository, ScheduleQueryRepository scheduleQueryRepository, DocumentRepository documentRepository, DutyRepository dutyRepository) {
        this.specialtyRepository = specialtyRepository;
        this.professionalRepository = professionalRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.publisher = publisher;
        this.patientRepository = patientRepository;
        this.scheduleQueryRepository = scheduleQueryRepository;
        this.documentRepository = documentRepository;
        this.dutyRepository = dutyRepository;
    }

    @Override
    public Professional update(Professional updateProfessional) {
        log.debug("Request to update Professional : {}", updateProfessional);

        Professional professional = professionalRepository.findById(updateProfessional.getId()).orElseThrow(() -> new BusinessException(ERROR_UPDATE_PROFESSIONAL_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (!professional.getUser().getEmail().equals(updateProfessional.getUser().getEmail())
            && !professional.getUser().getLogin().equals(updateProfessional.getUser().getEmail())
            && userRepository.findByLoginOrEmail(updateProfessional.getUser().getEmail(), updateProfessional.getUser().getEmail()).isPresent()) {
            throw new BusinessException(ERROR_PROFESSIONAL_EMAIL_ALREADY_USED);
        }

        professional.getUser().setLogin(updateProfessional.getUser().getEmail());
        professional.getUser().setEmail(updateProfessional.getUser().getEmail());
        updateProfessional.setUser(professional.getUser());


        return professionalRepository.save(updateProfessional);
    }

    @Override
    public Page<Professional> findAll(Pageable pageable) {
        log.debug("Request to get all Professionals");
        return professionalRepository.findAll(pageable);
    }

    @Override
    public Page<Professional> findAllByName(Pageable pageable, String name) {
        log.debug("Request to search and get Professionals");
        return professionalRepository.findAllByNameContains(pageable, name);
    }


    @Override
    public Professional findOne(String id) {
        log.debug("Request to get Professional : {}", id);
        return professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_ID_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Professional save(Professional professional) {
        log.debug("Request to save Professional : {}", professional);

        if (userRepository.findByLoginOrEmail(professional.getUser().getEmail(), professional.getUser().getEmail()).isPresent()) {
            throw new BusinessException(ERROR_PROFESSIONAL_EMAIL_ALREADY_USED);
        }

        Professional createProfessionalUser = adminCreateProfessionalUser(professional);

        publisher.publishEvent(new ProfessionalRegistrationByAdminEmailEvent(createProfessionalUser.getUser().getId()));

        return createProfessionalUser;
    }


    @Override
    public Professional registerProfessional(Professional professional, String password, String confirmPassword) {

        if (userRepository.findByLoginOrEmail(professional.getUser().getEmail(), professional.getUser().getEmail()).isPresent()) {
            throw new BusinessException(ERROR_PROFESSIONAL_EMAIL_ALREADY_USED);
        } else if (!password.equals(confirmPassword)) {
            throw new BusinessException(ERROR_REGISTER_PROFESSIONAL_PASSWORD_MUST_BE_EQUAL);
        }

        Professional createdProfessional = createProfessionalUser(professional);

        publisher.publishEvent(new ProfessionalRegistrationEmailEvent(createdProfessional.getUser().getId()));

        return createdProfessional;
    }

    @Override
    public Professional getMe() {
        return professionalRepository.findByUser_Login(SecurityUtils.getCurrentUserLogin()).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_SELF_INFORMATION_PROFESSIONAL_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public Set<Patient> getProfessionalPatients(String id) {
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND, HttpStatus.NOT_FOUND));

        return professional.getPatients();
    }

    @Override
    public Page<Professional> getProfessionalDocuments(Pageable pageable) {
        List<Professional> professionals = documentRepository.findByStatus(DocumentStatus.WAITING_APPROVEMENT)
            .stream()
            .map(Document::getProfessional)
            .distinct()
            .collect(Collectors.toList());

        Page<Professional> page = new PageImpl<Professional>(professionals, new PageRequest(pageable.getPageNumber(),
            pageable.getPageSize(), pageable.getSort()), professionals.size());

        return page;
    }

    @Override
    public List<Patient> updateProfessionalPatients(String id, List<Patient> patients) {
        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ERROR_UPDATE_PROFESSIONAL_PATIENTS_PROFESSIONAL_NOT_FOUND, HttpStatus.NOT_FOUND));

        checkPatients(patients);

        professional.setPatients(new HashSet<>(patients));

        Professional updatedProfessional = professionalRepository.save(professional);

        return new ArrayList<>(updatedProfessional.getPatients());
    }

    private Professional adminCreateProfessionalUser(Professional professional) {
        User user = userService.adminCreateProfessionalUser(professional);

        professional.setUser(user);

        checkSpecialties(professional.getSpecialties());

        return professionalRepository.save(professional);
    }

    private Professional createProfessionalUser(Professional professional) {
        User user = userService.createProfessionalUser(professional);

        professional.setUser(user);

        checkSpecialties(professional.getSpecialties());

        return professionalRepository.save(professional);
    }

    private void checkSpecialties(Set<Specialty> specialties) {
        specialties.forEach(specialty -> {
            if (!specialtyRepository.findById(specialty.getId()).isPresent()) {
                throw new BusinessException(ERROR_REGISTER_PROFESSIONAL_SPECIALTY_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        });
    }

    @Override
    public List<Duty> updateDutyProfessional(String idProfessional, List<Duty> duties) {
        Professional professional = professionalRepository.findById(idProfessional).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        checkDuties(duties);

        professional.setDuties(new HashSet<>(duties));

        Professional updateProfessional = professionalRepository.save(professional);

        return new ArrayList<>(updateProfessional.getDuties());
    }

    @Override
    public List<Professional> searchProfessional(String name) {
        return scheduleQueryRepository.searhProfessional(name);
    }

    @Override
    public Set<Duty> listProfessionalWithDuties(String id) {

        Professional professional = professionalRepository.findById(id).orElseThrow(() -> new BusinessException(ERROR_GET_PROFESSIONAL_ID_NOT_FOUND, HttpStatus.NOT_FOUND));

        return professional.getDuties();
    }

    private void checkDuties(List<Duty> duties) {
        if (duties
            .stream()
            .anyMatch(it -> !dutyRepository.findById(it.getId()).isPresent())) {
            throw new BusinessException(ERROR_ID_DUTY_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private void checkPatients(List<Patient> patients) {
        patients.forEach(patient -> {
            if (!patientRepository.findById(patient.getId()).isPresent()) {
                throw new BusinessException(ERROR_UPDATE_PROFESSIONAL_PATIENTS_PATIENT_NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        });
    }

}
