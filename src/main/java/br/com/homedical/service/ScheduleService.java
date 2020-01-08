package br.com.homedical.service;

import br.com.homedical.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface ScheduleService {


    List<Schedule> saveListSchedules(List<Schedule> schedule);
    
    Schedule saveSchedule(Schedule schedule);

    Schedule approvedSchedules(Schedule schedule);

    List<Schedule> listSchedulesPendingForProfessional(String id);

    List<Schedule> listSchedulePending();

    List<Schedule> listProfessionalSchedules(String id);

    Page<Schedule> filterScheduleForDate(String id, ZonedDateTime start, ZonedDateTime finish, Pageable pageable);

    Page<Schedule> findAll(Pageable pageable);

    Schedule findOne(String id);

}
