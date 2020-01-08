package br.com.homedical.service.mapper;

import br.com.homedical.domain.City;
import br.com.homedical.service.dto.CityDTO;
import br.com.homedical.service.dto.CitySimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity City and its DTO CityDTO.
 */
@Mapper(componentModel = "spring", uses = {StateMapper.class,})
public interface CityMapper extends EntityMapper<CityDTO, City> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "state", ignore = true)
    City toSimpleEntity(CitySimpleDTO dto);

    CitySimpleDTO toSimpleDto(City entity);

    List<City> toSimpleEntityList(List<CitySimpleDTO> dtoList);

    List<CityDTO> toSimpleDtoList(List<City> entityList);

}
