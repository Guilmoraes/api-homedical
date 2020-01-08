package br.com.homedical.service.mapper;

import br.com.homedical.domain.State;
import br.com.homedical.service.dto.StateDTO;
import br.com.homedical.service.dto.StateSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity State and its DTO StateDTO.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class,})
public interface StateMapper extends EntityMapper<StateDTO, State> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "acronym", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "country", ignore = true)
    State toSimpleEntity(StateSimpleDTO dto);

    StateSimpleDTO toSimpleDto(State entity);

    List<State> toSimpleEntityList(List<StateSimpleDTO> dtoList);

    List<StateDTO> toSimpleDtoList(List<State> entityList);

}
