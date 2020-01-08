package br.com.homedical.repository.impl;

import br.com.homedical.domain.Professional;
import br.com.homedical.domain.Schedule;
import br.com.homedical.repository.JPABaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@SuppressWarnings("SpringDataMethodInconsistencyInspection")

@NoRepositoryBean
public interface ScheduleQueryRepository extends JPABaseRepository<String, Schedule> {

    Page<Schedule> filterScheduleForDate(String id, ZonedDateTime start, ZonedDateTime finish, Pageable pageable);

    List<Professional> searhProfessional(String name);

    List<Schedule> listSchedulesPendingForProfessional(String id);

    List<Schedule> listSchedulePending();
}
