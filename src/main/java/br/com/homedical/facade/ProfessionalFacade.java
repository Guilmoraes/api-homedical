package br.com.homedical.facade;

import br.com.homedical.domain.Professional;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.DutySimpleDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.facade.dto.patient.PatientIdDTO;
import br.com.homedical.facade.dto.professional.ProfessionalDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterByAdminDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterDTO;
import br.com.homedical.facade.dto.professional.ProfessionalUpdateDTO;
import br.com.homedical.facade.mapper.DutyMapper;
import br.com.homedical.facade.mapper.PatientMapper;
import br.com.homedical.facade.mapper.ProfessionalMapper;
import br.com.homedical.service.ProfessionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProfessionalFacade {

    private final Logger log = LoggerFactory.getLogger(ProfessionalFacade.class);

    private final ProfessionalService professionalService;

    private final ProfessionalMapper professionalMapper;

    private final PatientMapper patientMapper;

    private final DutyMapper dutyMapper;

    public ProfessionalFacade(ProfessionalService professionalService, ProfessionalMapper professionalMapper, PatientMapper patientMapper, DutyMapper dutyMapper) {
        this.professionalService = professionalService;
        this.professionalMapper = professionalMapper;
        this.patientMapper = patientMapper;
        this.dutyMapper = dutyMapper;
    }

    @Transactional
    public ProfessionalDTO save(ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO) {
        log.debug("Request to save Professional : {}", professionalRegisterByAdminDTO);
        return professionalMapper.toDto(professionalService.save(professionalMapper.fromProfessionalRegisterByAdminDtoToEntity(professionalRegisterByAdminDTO)));
    }

    @Transactional
    public List<ProfessionalDTO> searchProfessional(String name) {
        log.debug("Request list professional for name");
        return professionalService.searchProfessional(name).stream().map(professionalMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Professional update(ProfessionalUpdateDTO professionalDTO) {
        log.debug("Request to update Professional : {}", professionalDTO);
        return professionalService.update(professionalMapper.fromProfessionalUpdateDtoToEntity(professionalDTO));
    }

    @Transactional(readOnly = true)
    public Page<ProfessionalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Professionals");
        return professionalService.findAll(pageable)
            .map(professionalMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProfessionalDTO> findAllByName(Pageable pageable, String name) {
        log.debug("Request to search and get Professionals");
        return professionalService.findAllByName(pageable, name)
            .map(professionalMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ProfessionalDTO> getProfessionalDocuments(Pageable pageable) {
        return professionalService.getProfessionalDocuments(pageable).map(professionalMapper::toDto);
    }


    @Transactional(readOnly = true)
    public ProfessionalDTO findOne(String id) {
        log.debug("Request to get Professional : {}", id);
        Professional professional = professionalService.findOne(id);
        return professionalMapper.toDto(professional);
    }

    @Transactional
    public ProfessionalDTO registerProfessional(ProfessionalRegisterDTO professionalRegisterDTO) {
        return professionalMapper.toDto(professionalService.registerProfessional(professionalMapper.fromProfessionalRegisterDtoToEntity(professionalRegisterDTO), professionalRegisterDTO.getPassword(), professionalRegisterDTO.getConfirmPassword()));
    }

    @Transactional(readOnly = true)
    public ProfessionalDTO getMe() {
        return professionalMapper.toDto(professionalService.getMe());
    }

    @Transactional(readOnly = true)
    public List<PatientDTO> getProfessionalPatients(String id) {
        return professionalService.getProfessionalPatients(id).stream().map(patientMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DutyDTO> listProfessionalWitchDuty(String id) {
        return professionalService.listProfessionalWithDuties(id).stream().map(dutyMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<PatientDTO> updateProfessionalPatients(String id, List<PatientIdDTO> patients) {
        return professionalService.updateProfessionalPatients(id, patients.stream().map(patientMapper::fromPatientIdDtoToEntity).collect(Collectors.toList()))
            .stream().map(patientMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<DutyDTO> updateProfessionalDuites(String id, List<DutySimpleDTO> duties) {
        return professionalService.updateDutyProfessional(id, duties.stream().map(dutyMapper::toSimpleEntity).collect(Collectors.toList()))
            .stream()
            .map(dutyMapper::toDto)
            .collect(Collectors.toList());
    }
}
