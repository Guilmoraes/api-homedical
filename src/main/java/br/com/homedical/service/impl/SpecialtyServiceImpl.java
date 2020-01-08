package br.com.homedical.service.impl;

import br.com.homedical.domain.Specialty;
import br.com.homedical.domain.enumeration.ObjectStatus;
import br.com.homedical.repository.SpecialtyRepository;
import br.com.homedical.service.SpecialtyService;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.service.exceptions.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository repository;

    public SpecialtyServiceImpl(SpecialtyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        log.debug("Request to save Specialty : {}", specialty);

        if (specialty.getId() != null) {
            throw new BusinessException(ErrorConstants.ENTITY_NOT_FOUND);
        }

        return repository.save(specialty);
    }

    @Override
    public Specialty update(Specialty specialty) {
        log.debug("Request to update Specialty : {}", specialty);

        if (!repository.findById(specialty.getId()).isPresent()) {
            throw new BusinessException(ErrorConstants.ERROR_UPDATE_SPECIALTY_ID_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return repository.save(specialty);
    }

    @Override
    public Page<Specialty> findAll(Pageable pageable) {
        log.debug("Request to get all Specialties");
        return repository.findAll(pageable);
    }

    @Override
    public Specialty findOne(String id) {
        log.debug("Request to get Specialty : {}", id);
        return repository.findOne(id);
    }

    @Override
    public List<Specialty> getEnabledSpecialties() {
        log.debug("Request to get enabled specialties");
        return repository.findAllByStatus(ObjectStatus.ENABLED);
    }
}
