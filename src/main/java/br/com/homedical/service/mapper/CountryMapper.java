package br.com.homedical.service.mapper;

import br.com.homedical.domain.Country;
import br.com.homedical.service.dto.CountryDTO;
import br.com.homedical.service.dto.CountrySimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "acronym", ignore = true)
    Country toSimpleEntity(CountrySimpleDTO dto);

    CountrySimpleDTO toSimpleDto(Country entity);

    List<Country> toSimpleEntityList(List<CountrySimpleDTO> dtoList);

    List<CountryDTO> toSimpleDtoList(List<Country> entityList);

}
