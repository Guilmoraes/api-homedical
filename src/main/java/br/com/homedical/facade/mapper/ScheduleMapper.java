package br.com.homedical.facade.mapper;

import br.com.homedical.domain.Schedule;
import br.com.homedical.facade.dto.MobileScheduleDTO;
import br.com.homedical.facade.dto.ScheduleDTO;
import br.com.homedical.facade.dto.ScheduleSimpleDTO;
import br.com.homedical.facade.dto.specialty.ScheduleCompleteReportDTO;
import br.com.homedical.facade.dto.specialty.ScheduleReportDTO;
import br.com.homedical.service.mapper.FileMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Schedule and its DTO ScheduleDTO.
 */
@Mapper(componentModel = "spring", uses = {ProfessionalMapper.class, PatientMapper.class, DutyMapper.class, FileMapper.class, SignatureMapper.class})
public interface ScheduleMapper extends EntityMapper<ScheduleDTO, Schedule> {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "start", ignore = true)
    @Mapping(target = "finish", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "professional", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "duty", ignore = true)
    @Mapping(target = "scheduleImages", ignore = true)
    @Mapping(target = "signatures", ignore = true)
    Schedule toSimpleEntity(ScheduleSimpleDTO dto);

    MobileScheduleDTO toMobileDTO(Schedule schedule);

    ScheduleSimpleDTO toSimpleDto(Schedule entity);

    List<Schedule> toSimpleEntityList(List<ScheduleSimpleDTO> dtoList);

    List<ScheduleDTO> toSimpleDtoList(List<Schedule> entityList);

    ScheduleReportDTO toReportList(Schedule dtoList);

    ScheduleCompleteReportDTO toCompleteReport(Schedule entity);
}
