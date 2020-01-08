package br.com.homedical.repository;

import br.com.homedical.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, String>, JpaSpecificationExecutor<DocumentType> {

    Optional<DocumentType> findById(String id);

    List<DocumentType> findByRequiredIsTrue();
}
