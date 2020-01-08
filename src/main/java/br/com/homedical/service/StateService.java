package br.com.homedical.service;

import br.com.homedical.domain.State;
import br.com.homedical.repository.StateRepository;
import br.com.homedical.service.dto.StateDTO;
import br.com.homedical.service.mapper.StateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class StateService {

    private final Logger log = LoggerFactory.getLogger(StateService.class);

    private final StateRepository repository;

    private final StateMapper mapper;

    public StateService(StateRepository repository, StateMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public StateDTO save(StateDTO dto) {
        log.debug("Request to save State : {}", dto);
        State state = mapper.toEntity(dto);
        state = repository.save(state);
        return mapper.toDto(state);
    }

    @Transactional(readOnly = true)
    public StateDTO update(StateDTO dto) {
        log.debug("Request to update State : {}", dto);
        State state = mapper.toEntity(dto);
        state = repository.save(state);
        return mapper.toDto(state);
    }

    @Transactional(readOnly = true)
    public Page<StateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all States");
        return repository.findAll(pageable)
            .map(mapper::toDto);
    }


    @Transactional(readOnly = true)
    public StateDTO findOne(String id) {
        log.debug("Request to get State : {}", id);
        State state = repository.findOne(id);
        return mapper.toDto(state);
    }


    public void delete(String id) {
        log.debug("Request to delete State : {}", id);
        repository.delete(id);
    }
}
