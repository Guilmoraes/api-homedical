package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Specialty;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyProfessionalDTO;
import br.com.homedical.facade.dto.specialty.SpecialtySimpleDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity Specialty and its DTO SpecialtyDTO.
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Specialty toSimpleEntity(SpecialtySimpleDTO dto);

    SpecialtySimpleDTO toSimpleDto(Specialty entity);

    List<Specialty> toSimpleEntityList(List<SpecialtySimpleDTO> dtoList);

    List<SpecialtyDTO> toSimpleDtoList(List<Specialty> entityList);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Specialty fromSpecialtyUpdateDtoToEntity(SpecialtyUpdateDTO specialtyUpdateDTO);

    SpecialtyUpdateDTO fromEntityToSpecialtyUpdateDto(Specialty specialty);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Specialty fromSpecialtyProfessionalDtoToEntity(SpecialtyProfessionalDTO specialtyProfessionalDTO);

    SpecialtyProfessionalDTO fromEntityToSpecialtyProfessionalDto(Specialty specialty);

    List<SpecialtyDTO> fromSpecialtySetToSpecialityListDto(Set<Specialty> specialties);
}
