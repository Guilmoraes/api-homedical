package br.com.homedical.facade;

import br.com.homedical.domain.Schedule;
import br.com.homedical.facade.dto.MobileScheduleDTO;
import br.com.homedical.facade.dto.ScheduleDTO;
import br.com.homedical.facade.dto.specialty.ScheduleCompleteReportDTO;
import br.com.homedical.facade.dto.specialty.ScheduleReportDTO;
import br.com.homedical.facade.mapper.ScheduleMapper;
import br.com.homedical.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ScheduleFacade {

    private final Logger log = LoggerFactory.getLogger(ScheduleFacade.class);

    private final ScheduleService scheduleService;

    private final ScheduleMapper scheduleMapper;

    public ScheduleFacade(ScheduleService scheduleService, ScheduleMapper scheduleMapper) {
        this.scheduleService = scheduleService;
        this.scheduleMapper = scheduleMapper;
    }

    @Transactional
    public List<ScheduleDTO> saveListSchedules(List<ScheduleDTO> scheduleDTO) {
        log.debug("Request to save Schedule : {}", scheduleDTO);

        List<Schedule> schedule = scheduleMapper.toEntity(scheduleDTO);
        schedule = scheduleService.saveListSchedules(schedule);
        return scheduleMapper.toDto(schedule);
    }

    @Transactional
    public ScheduleDTO saveSchedule(ScheduleDTO scheduleDTO) {
        log.debug("Request to save Schedule : {}", scheduleDTO);
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        schedule = scheduleService.saveSchedule(schedule);
        return scheduleMapper.toDto(schedule);
    }

    @Transactional
    public ScheduleDTO approvedSchedules(ScheduleDTO scheduleDTO) {
        log.debug("Request to aproved schedules");
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        return scheduleMapper.toDto(scheduleService.approvedSchedules(schedule));
    }

    @Transactional
    public List<ScheduleDTO> listSchedulePending() {
        return scheduleService.listSchedulePending().stream().map(scheduleMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<ScheduleDTO> listSchedulesPendingForProfessional(String id) {
        log.debug("Request list of schedules with status pending for professional");
        return scheduleService.listSchedulesPendingForProfessional(id)
            .stream()
            .map(scheduleMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Page<ScheduleReportDTO> filterScheduleForDate(String id, ZonedDateTime start, ZonedDateTime finish, Pageable pageable) {
        log.debug("Filter schedules for date");
        return scheduleService.filterScheduleForDate(id, start, finish, pageable)
            .map(scheduleMapper::toReportList);
    }

    @Transactional
    public ScheduleCompleteReportDTO getScheduleCompleteReport(String id) {
        log.debug("Filter schedules for date");
        return scheduleMapper.toCompleteReport(scheduleService.findOne(id));
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schedules");
        return scheduleService.findAll(pageable)
            .map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MobileScheduleDTO> listProfessionalSchedules(String id) {
        log.debug("Request to get professional with scredulles");
        return scheduleService.listProfessionalSchedules(id)
            .stream()
            .map(scheduleMapper::toMobileDTO).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ScheduleDTO findOne(String id) {
        log.debug("Request to get Schedule : {}", id);
        Schedule schedule = scheduleService.findOne(id);
        return scheduleMapper.toDto(schedule);
    }

}
