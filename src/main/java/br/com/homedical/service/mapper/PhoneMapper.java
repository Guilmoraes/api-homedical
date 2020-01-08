package br.com.homedical.service.mapper;

import br.com.homedical.domain.Phone;
import br.com.homedical.facade.dto.phone.PhonePatientDTO;
import br.com.homedical.service.dto.PhoneDTO;
import br.com.homedical.service.dto.PhoneSimpleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Phone and its DTO PhoneDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PhoneMapper extends EntityMapper<PhoneDTO, Phone> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "areaCode", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "type", ignore = true)
    Phone toSimpleEntity(PhoneSimpleDTO dto);

    PhoneSimpleDTO toSimpleDto(Phone entity);

    List<Phone> toSimpleEntityList(List<PhoneSimpleDTO> dtoList);

    List<PhoneDTO> toSimpleDtoList(List<Phone> entityList);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Phone fromPhonePatientDtoToEntity(PhonePatientDTO phonePatientDTO);

}
