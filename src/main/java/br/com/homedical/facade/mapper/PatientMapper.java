package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Patient;
import br.com.homedical.facade.dto.patient.PatientCreateDTO;
import br.com.homedical.facade.dto.patient.PatientDTO;
import br.com.homedical.facade.dto.patient.PatientIdDTO;
import br.com.homedical.facade.dto.patient.PatientReportDTO;
import br.com.homedical.facade.dto.patient.PatientSimpleDTO;
import br.com.homedical.service.mapper.AddressMapper;
import br.com.homedical.service.mapper.PhoneMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Patient and its DTO PatientDTO.
 */
@Mapper(componentModel = "spring", uses = {PhoneMapper.class, AddressMapper.class, HealthOperatorMapper.class})
public interface PatientMapper extends EntityMapper<PatientDTO, Patient> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "clinicalCondition", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "healthOperator", ignore = true)
    @Mapping(target = "address", ignore = true)
    Patient toSimpleEntity(PatientSimpleDTO dto);

    PatientSimpleDTO toSimpleDto(Patient entity);

    PatientReportDTO toReportDto(Patient entity);

    List<Patient> toSimpleEntityList(List<PatientSimpleDTO> dtoList);

    List<PatientDTO> toSimpleDtoList(List<Patient> entityList);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    Patient fromPatientCreateDtoToEntity(PatientCreateDTO patientCreateDTO);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "clinicalCondition", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "healthOperator", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "name", ignore = true)
    Patient fromPatientIdDtoToEntity(PatientIdDTO patientIdDTO);
}
