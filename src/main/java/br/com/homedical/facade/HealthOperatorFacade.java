package br.com.homedical.facade;

import br.com.homedical.domain.HealthOperator;
import br.com.homedical.facade.dto.healthOperator.HealthOperatorDTO;
import br.com.homedical.facade.mapper.HealthOperatorMapper;
import br.com.homedical.service.HealthOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class HealthOperatorFacade {

    private final Logger log = LoggerFactory.getLogger(HealthOperatorFacade.class);

    private final HealthOperatorService healthOperatorService;

    private final HealthOperatorMapper healthOperatorMapper;

    public HealthOperatorFacade(HealthOperatorService healthOperatorService, HealthOperatorMapper healthOperatorMapper) {
        this.healthOperatorService = healthOperatorService;
        this.healthOperatorMapper = healthOperatorMapper;
    }

    @Transactional
    public HealthOperatorDTO save(HealthOperatorDTO healthOperatorDTO) {
        log.debug("Request to save HealthOperator : {}", healthOperatorDTO);
        HealthOperator healthOperator = healthOperatorMapper.toEntity(healthOperatorDTO);
        healthOperator = healthOperatorService.save(healthOperator);
        return healthOperatorMapper.toDto(healthOperator);
    }

    @Transactional
    public HealthOperatorDTO update(HealthOperatorDTO healthOperatorDTO) {
        log.debug("Request to update HealthOperator : {}", healthOperatorDTO);
        HealthOperator healthOperator = healthOperatorMapper.toEntity(healthOperatorDTO);
        healthOperator = healthOperatorService.save(healthOperator);
        return healthOperatorMapper.toDto(healthOperator);
    }

    @Transactional(readOnly = true)
    public Page<HealthOperatorDTO> findAll(String query, Pageable pageable) {
        log.debug("Request to get all HealthOperators");
        return healthOperatorService.findAll(query, pageable)
            .map(healthOperatorMapper::toDto);
    }


    @Transactional(readOnly = true)
    public HealthOperatorDTO findOne(String id) {
        log.debug("Request to get HealthOperator : {}", id);
        HealthOperator healthOperator = healthOperatorService.findOne(id);
        return healthOperatorMapper.toDto(healthOperator);
    }


    public void delete(String id) {
        log.debug("Request to delete HealthOperator : {}", id);
        healthOperatorService.delete(id);
    }
}
