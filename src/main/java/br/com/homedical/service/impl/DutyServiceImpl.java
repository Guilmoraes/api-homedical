package br.com.homedical.service.impl;

import br.com.homedical.domain.Duty;
import br.com.homedical.repository.DutyRepository;
import br.com.homedical.service.DutyService;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DutyServiceImpl implements DutyService {

    private final Logger log = LoggerFactory.getLogger(DutyServiceImpl.class);

    private final DutyRepository dutyRepository;

    public DutyServiceImpl(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }


    @Override
    public Duty save(Duty duty) {
        log.debug("Request to save Duty : {}", duty);
        return dutyRepository.save(duty);
    }

    @Override
    public Duty update(Duty duty) {
        log.debug("Request to update Duty : {}", duty);
        Duty existing = dutyRepository.findById(duty.getId()).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_DUTY_NOT_FOUND));
        return dutyRepository.save(existing);
    }

    @Override
    public Page<Duty> findAll(Pageable pageable) {
        log.debug("Request to get all Duties");
        return dutyRepository.findAll(pageable);
    }

    @Override
    public Duty findOne(String id) {
        log.debug("Request to get Duty : {}", id);
        return dutyRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_DUTY_NOT_FOUND));
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Duty : {}", id);
        Duty existing = dutyRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorConstants.ERROR_ID_DUTY_NOT_FOUND));
        dutyRepository.delete(existing.getId());
    }

    @Override
    public List<Duty> listDuties() {
        log.debug("Request list of dutites");
        return dutyRepository.findAll();
    }

}
