package br.com.homedical.service.impl;

import br.com.homedical.domain.HealthOperator;
import br.com.homedical.repository.HealthOperatorRepository;
import br.com.homedical.service.HealthOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class HealthOperatorServiceImpl implements HealthOperatorService {

    private final Logger log = LoggerFactory.getLogger(HealthOperatorServiceImpl.class);

    private final HealthOperatorRepository healthOperatorRepository;

    public HealthOperatorServiceImpl(HealthOperatorRepository healthOperatorRepository) {
        this.healthOperatorRepository = healthOperatorRepository;
    }


    @Override
    public HealthOperator save(HealthOperator healthOperator) {
        log.debug("Request to save HealthOperator : {}", healthOperator);
        return healthOperatorRepository.save(healthOperator);
    }

    @Override
    public HealthOperator update(HealthOperator healthOperator) {
        log.debug("Request to update HealthOperator : {}", healthOperator);
        return healthOperatorRepository.save(healthOperator);
    }

    @Override
    public Page<HealthOperator> findAll(String query, Pageable pageable) {
        log.debug("Request to get all HealthOperators");
        return healthOperatorRepository.findAllBySocialReasonContaining(query, pageable);
    }


    @Override
    public HealthOperator findOne(String id) {
        log.debug("Request to get HealthOperator : {}", id);
        return healthOperatorRepository.findOne(id);
    }


    @Override
    public void delete(String id) {
        log.debug("Request to delete HealthOperator : {}", id);
        healthOperatorRepository.delete(id);
    }
}
