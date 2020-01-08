package br.com.homedical.service;

import br.com.homedical.domain.Address;
import br.com.homedical.repository.AddressRepository;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.mapper.AddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository repository;

    private final AddressMapper mapper;

    public AddressService(AddressRepository repository,
                          AddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Transactional(readOnly = true)
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        Address address = mapper.toEntity(addressDTO);
        address = repository.save(address);
        return mapper.toDto(address);
    }

    @Transactional(readOnly = true)
    public AddressDTO update(AddressDTO addressDTO) {
        log.debug("Request to update Address : {}", addressDTO);
        Address address = mapper.toEntity(addressDTO);
        address = repository.save(address);
        return mapper.toDto(address);
    }

    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public AddressDTO findOne(String id) {
        log.debug("Request to get Address : {}", id);
        Address address = repository.findOne(id);
        return mapper.toDto(address);
    }


    public void delete(String id) {
        log.debug("Request to delete Address : {}", id);
        repository.delete(id);
    }
}
