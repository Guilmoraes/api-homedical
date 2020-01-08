package br.com.homedical.service;

import br.com.homedical.domain.Country;
import br.com.homedical.repository.CountryRepository;
import br.com.homedical.service.dto.CountryDTO;
import br.com.homedical.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository repository;

    private final CountryMapper mapper;

    public CountryService(CountryRepository repository, CountryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public CountryDTO save(CountryDTO dto) {
        log.debug("Request to save Country : {}", dto);
        Country country = mapper.toEntity(dto);
        country = repository.save(country);
        return mapper.toDto(country);
    }

    @Transactional(readOnly = true)
    public CountryDTO update(CountryDTO dto) {
        log.debug("Request to update Country : {}", dto);
        Country country = mapper.toEntity(dto);
        country = repository.save(country);
        return mapper.toDto(country);
    }

    @Transactional(readOnly = true)
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public CountryDTO findOne(String id) {
        log.debug("Request to get Country : {}", id);
        Country country = repository.findOne(id);
        return mapper.toDto(country);
    }


    public void delete(String id) {
        log.debug("Request to delete Country : {}", id);
        repository.delete(id);
    }
}
