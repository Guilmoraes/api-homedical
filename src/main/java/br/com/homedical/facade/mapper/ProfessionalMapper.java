package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Professional;
import br.com.homedical.facade.dto.professional.ProfessionalDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterByAdminDTO;
import br.com.homedical.facade.dto.professional.ProfessionalRegisterDTO;
import br.com.homedical.facade.dto.professional.ProfessionalReportDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalUpdateDTO;
import br.com.homedical.service.mapper.AddressMapper;
import br.com.homedical.service.mapper.PhoneMapper;
import br.com.homedical.service.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Professional and its DTO ProfessionalDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SpecialtyMapper.class, PhoneMapper.class, AddressMapper.class, DutyMapper.class})
public interface ProfessionalMapper extends EntityMapper<ProfessionalDTO, Professional> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "duties", ignore = true)
    Professional toSimpleEntity(ProfessionalSimpleDTO dto);

    @Override
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "patients", ignore = true)
    Professional toEntity(ProfessionalDTO dto);

    ProfessionalReportDTO toReportDto(Professional entity);

    ProfessionalSimpleDTO toSimpleDto(Professional entity);

    List<Professional> toSimpleEntityList(List<ProfessionalSimpleDTO> dtoList);

    List<ProfessionalDTO> toSimpleDtoList(List<Professional> entityList);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "duties", ignore = true)
    Professional fromProfessionalRegisterDtoToEntity(ProfessionalRegisterDTO professionalRegisterDTO);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "patients", ignore = true)
    @Mapping(target = "duties", ignore = true)
    Professional fromProfessionalRegisterByAdminDtoToEntity(ProfessionalRegisterByAdminDTO professionalRegisterByAdminDTO);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.login", source = "email")
    @Mapping(target = "patients", ignore = true)
    Professional fromProfessionalUpdateDtoToEntity(ProfessionalUpdateDTO professionalUpdateDTO);

}
