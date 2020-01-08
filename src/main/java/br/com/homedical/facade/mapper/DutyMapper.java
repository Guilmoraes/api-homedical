package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Duty;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.DutySimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Duty and its DTO DutyDTO.
 */
@Mapper(componentModel = "spring")
public interface DutyMapper extends EntityMapper<DutyDTO, Duty> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "start", ignore = true)
    @Mapping(target = "finish", ignore = true)
    @Mapping(target = "overtime", ignore = true)
    @Mapping(target = "price", ignore = true)
    Duty toSimpleEntity(DutySimpleDTO dto);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Override
    Duty toEntity(DutyDTO dto);

    DutySimpleDTO toSimpleDto(Duty entity);

    List<Duty> toSimpleEntityList(List<DutySimpleDTO> dtoList);

}
