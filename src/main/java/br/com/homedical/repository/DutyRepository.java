package br.com.homedical.repository;

import br.com.homedical.domain.Duty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface DutyRepository extends JpaRepository<Duty, String>, JpaSpecificationExecutor<Duty> {

    Optional<Duty> findById(String id);

}
