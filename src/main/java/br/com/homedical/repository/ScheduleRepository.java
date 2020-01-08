package br.com.homedical.repository;

import br.com.homedical.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String>, JpaSpecificationExecutor<Schedule> {

    Optional<Schedule> findById(String id);

    List<Schedule> findByProfessionalId(String id);

}
