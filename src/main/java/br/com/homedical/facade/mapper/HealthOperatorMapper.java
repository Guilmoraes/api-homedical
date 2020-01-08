package br.com.homedical.facade.mapper;

import br.com.homedical.domain.HealthOperator;
import br.com.homedical.facade.dto.healthOperator.HealthOperatorDTO;
import br.com.homedical.facade.dto.healthOperator.HealthOperatorSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity HealthOperator and its DTO HealthOperatorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HealthOperatorMapper extends EntityMapper<HealthOperatorDTO, HealthOperator> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "ansRegister", ignore = true)
    @Mapping(target = "socialReason", ignore = true)
    @Mapping(target = "obs", ignore = true)
    @Mapping(target = "modality", ignore = true)
    HealthOperator toSimpleEntity(HealthOperatorSimpleDTO dto);

    HealthOperatorSimpleDTO toSimpleDto(HealthOperator entity);

    List<HealthOperator> toSimpleEntityList(List<HealthOperatorSimpleDTO> dtoList);

    List<HealthOperatorDTO> toSimpleDtoList(List<HealthOperator> entityList);

}
