package br.com.homedical.facade;

import br.com.homedical.domain.Duty;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.mapper.DutyMapper;
import br.com.homedical.service.DutyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DutyFacade {

    private final Logger log = LoggerFactory.getLogger(DutyFacade.class);

    private final DutyService dutyService;

    private final DutyMapper dutyMapper;

    public DutyFacade(DutyService dutyService, DutyMapper dutyMapper) {
        this.dutyService = dutyService;
        this.dutyMapper = dutyMapper;
    }

    @Transactional
    public DutyDTO save(DutyDTO dutyDTO) {
        log.debug("Request to save Duty : {}", dutyDTO);
        Duty duty = dutyMapper.toEntity(dutyDTO);
        duty = dutyService.save(duty);
        return dutyMapper.toDto(duty);
    }

    @Transactional
    public DutyDTO update(DutyDTO dutyDTO) {
        log.debug("Request to update Duty : {}", dutyDTO);
        Duty duty = dutyMapper.toEntity(dutyDTO);
        duty = dutyService.save(duty);
        return dutyMapper.toDto(duty);
    }

    @Transactional(readOnly = true)
    public Page<DutyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Duties");
        return dutyService.findAll(pageable)
            .map(dutyMapper::toDto);
    }


    @Transactional(readOnly = true)
    public DutyDTO findOne(String id) {
        log.debug("Request to get Duty : {}", id);
        Duty duty = dutyService.findOne(id);
        return dutyMapper.toDto(duty);
    }

    @Transactional(readOnly = true)
    public List<DutyDTO> listDuties() {
        log.debug("Request to list of Duties");
        return dutyService.listDuties().stream().map(dutyMapper::toDto).collect(Collectors.toList());
    }

    public void delete(String id) {
        log.debug("Request to delete Duty : {}", id);
        dutyService.delete(id);
    }
}
