package br.com.homedical.repository;

import br.com.homedical.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface FileRepository extends JpaRepository<File, String>, JpaSpecificationExecutor<File> {

    List<File> findByFileIsNotNull();

    List<File> findByIdIn(List<String> ids);

    List<File> findByProcessedFalse();
}
