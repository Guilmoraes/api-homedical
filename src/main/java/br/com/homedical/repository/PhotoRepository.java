package br.com.homedical.repository;

import br.com.homedical.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
public interface PhotoRepository extends JpaRepository<Photo, String> {

    List<Photo> findByFileIsNotNull();

    List<Photo> findByProcessedFalse();

}
