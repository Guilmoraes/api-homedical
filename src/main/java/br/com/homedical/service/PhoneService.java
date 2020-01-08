package br.com.homedical.service;

import br.com.homedical.domain.Phone;
import br.com.homedical.repository.PhoneRepository;
import br.com.homedical.service.dto.PhoneDTO;
import br.com.homedical.service.mapper.PhoneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PhoneService {

    private final Logger log = LoggerFactory.getLogger(PhoneService.class);

    private final PhoneRepository repository;

    private final PhoneMapper mapper;

    public PhoneService(PhoneRepository repository, PhoneMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public PhoneDTO save(PhoneDTO dto) {
        log.debug("Request to save Phone : {}", dto);
        Phone phone = mapper.toEntity(dto);
        phone = repository.save(phone);
        return mapper.toDto(phone);
    }

    @Transactional(readOnly = true)
    public PhoneDTO update(PhoneDTO dto) {
        log.debug("Request to update Phone : {}", dto);
        Phone phone = mapper.toEntity(dto);
        phone = repository.save(phone);
        return mapper.toDto(phone);
    }

    @Transactional(readOnly = true)
    public Page<PhoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Phones");
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public PhoneDTO findOne(String id) {
        log.debug("Request to get Phone : {}", id);
        Phone phone = repository.findOne(id);
        return mapper.toDto(phone);
    }


    public void delete(String id) {
        log.debug("Request to delete Phone : {}", id);
        repository.delete(id);
    }
}
