package br.com.homedical.facade;

import br.com.homedical.domain.Specialty;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyUpdateDTO;
import br.com.homedical.facade.mapper.SpecialtyMapper;
import br.com.homedical.service.SpecialtyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpecialtyFacade {

    private final Logger log = LoggerFactory.getLogger(SpecialtyFacade.class);

    private final SpecialtyService specialtyService;

    private final SpecialtyMapper specialtyMapper;

    public SpecialtyFacade(SpecialtyService specialtyService, SpecialtyMapper specialtyMapper) {
        this.specialtyService = specialtyService;
        this.specialtyMapper = specialtyMapper;
    }

    @Transactional
    public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
        log.debug("Request to save Specialty : {}", specialtyDTO);
        return specialtyMapper.toDto(specialtyService.save(specialtyMapper.toEntity(specialtyDTO)));
    }

    @Transactional
    public SpecialtyDTO update(SpecialtyUpdateDTO specialtyUpdateDTO) {
        log.debug("Request to update Specialty : {}", specialtyUpdateDTO);
        return specialtyMapper.toDto(specialtyService.update(specialtyMapper.fromSpecialtyUpdateDtoToEntity(specialtyUpdateDTO)));
    }

    @Transactional(readOnly = true)
    public Page<SpecialtyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Specialties");
        return specialtyService.findAll(pageable)
            .map(specialtyMapper::toDto);
    }


    @Transactional(readOnly = true)
    public SpecialtyDTO findOne(String id) {
        log.debug("Request to get Specialty : {}", id);
        Specialty specialty = specialtyService.findOne(id);
        return specialtyMapper.toDto(specialty);
    }

    @Transactional(readOnly = true)
    public List<SpecialtyDTO> getEnabledSpecialties() {
        log.debug("Request to get enabled Specialties");
        return specialtyService.getEnabledSpecialties().stream().map(specialtyMapper::toDto).collect(Collectors.toList());
    }
}
