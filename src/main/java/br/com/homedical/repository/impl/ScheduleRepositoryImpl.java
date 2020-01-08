package br.com.homedical.repository.impl;

import br.com.homedical.domain.Professional;
import br.com.homedical.domain.QProfessional;
import br.com.homedical.domain.QSchedule;
import br.com.homedical.domain.Schedule;
import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.repository.BaseRepository;
import br.com.homedical.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class ScheduleRepositoryImpl extends BaseRepository<String, Schedule> implements ScheduleQueryRepository {

    private final EntityManager em;

    public ScheduleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Page<Schedule> filterScheduleForDate(String id, ZonedDateTime start, ZonedDateTime finish, Pageable pageable) {
        JPAQuery<Schedule> query = new JPAQuery<>(em);

        QSchedule schedule = QSchedule.schedule;

        BooleanBuilder where = new BooleanBuilder();

        query.from(schedule);

        if (StringUtils.isNotBlank(id)) {
            where.and(QSchedule.schedule.professional.id.eq(id));
            where.and(QSchedule.schedule.status.eq(SchedulesStatus.APPROVED));
        }

        if (start != null && finish != null) {
            where.and(QSchedule.schedule.finish.between(start, finish.plusDays(1L)));
        }

        query.where(where);

        return findAll(query, pageable);
    }

    @Override
    public List<Professional> searhProfessional(String name) {
        JPAQuery<Professional> query = new JPAQuery<>(em);

        QProfessional professional = QProfessional.professional;

        BooleanBuilder where = new BooleanBuilder();

        query.from(professional);

        if (StringUtils.isNotBlank(name)) {
            where.and(QProfessional.professional.name.like("%" + name + "%"));
        }

        query.where(where);

        return query.fetch();
    }

    @Override
    public List<Schedule> listSchedulesPendingForProfessional(String id) {
        JPAQuery<Schedule> query = new JPAQuery<>(em);

        QSchedule schedule = QSchedule.schedule;

        BooleanBuilder where = new BooleanBuilder();

        query.from(schedule);

        if (StringUtils.isNotBlank(id)) {
            where.and(QSchedule.schedule.professional.id.eq(id));
            where.and(QSchedule.schedule.status.eq(SchedulesStatus.PENDING));
        }

        query.where(where);

        return query.fetch();
    }

    @Override
    public List<Schedule> listSchedulePending() {
        JPAQuery<Schedule> query = new JPAQuery<>(em);

        QSchedule schedule = QSchedule.schedule;

        BooleanBuilder where = new BooleanBuilder();

        query.from(schedule);

        where.and(QSchedule.schedule.status.eq(SchedulesStatus.PENDING));

        query.where(where);

        return query.fetch();
    }
}
