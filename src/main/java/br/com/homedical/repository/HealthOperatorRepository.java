package br.com.homedical.repository;

import br.com.homedical.domain.HealthOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface HealthOperatorRepository extends JpaRepository<HealthOperator, String>, JpaSpecificationExecutor<HealthOperator> {

    Page<HealthOperator> findAllBySocialReasonContaining(String socialReason, Pageable pageable);

    Optional<HealthOperator> findById(String id);
}
