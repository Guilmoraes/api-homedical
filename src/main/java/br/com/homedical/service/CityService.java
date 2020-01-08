package br.com.homedical.service;

import br.com.homedical.domain.City;
import br.com.homedical.repository.CityRepository;
import br.com.homedical.service.dto.CityDTO;
import br.com.homedical.service.mapper.CityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CityService {

    private final Logger log = LoggerFactory.getLogger(CityService.class);

    private final CityRepository repository;

    private final CityMapper mapper;

    public CityService(CityRepository repository, CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        City city = mapper.toEntity(cityDTO);
        city = repository.save(city);
        return mapper.toDto(city);
    }

    @Transactional(readOnly = true)
    public CityDTO update(CityDTO cityDTO) {
        log.debug("Request to update City : {}", cityDTO);
        City city = mapper.toEntity(cityDTO);
        city = repository.save(city);
        return mapper.toDto(city);
    }

    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public CityDTO findOne(String id) {
        log.debug("Request to get City : {}", id);
        City city = repository.findOne(id);
        return mapper.toDto(city);
    }


    public void delete(String id) {
        log.debug("Request to delete City : {}", id);
        repository.delete(id);
    }

    public Page<CityDTO> findAllByNameContains(String query, Pageable pageable) {
        log.debug("Request to get Cities by Name: {}", query);

        return repository.findAllByNameContains(query, pageable)
            .map(mapper::toDto);
    }
}
