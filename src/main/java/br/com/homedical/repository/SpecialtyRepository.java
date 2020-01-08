package br.com.homedical.repository;

import br.com.homedical.domain.Specialty;
import br.com.homedical.domain.enumeration.ObjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, String>, JpaSpecificationExecutor<Specialty> {

    List<Specialty> findAllByStatus(ObjectStatus objectStatus);

    Optional<Specialty> findById(String id);
}
