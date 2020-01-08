package br.com.homedical.repository;

import br.com.homedical.domain.Document;
import br.com.homedical.domain.Professional;
import br.com.homedical.domain.enumeration.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, String>, JpaSpecificationExecutor<Document> {

    List<Document> findByFileIsNotNull();

    List<Document> findByProcessedFalse();

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByProfessional(Professional professional);

    List<Document> findByProfessionalAndStatus(Professional professional, DocumentStatus status);

    Page<Document> findAllByProfessionalId(String id, Pageable pageable);

    Optional<Document> findById(String id);
}
