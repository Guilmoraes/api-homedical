package br.com.homedical.repository;

import br.com.homedical.domain.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, String>, JpaSpecificationExecutor<City> {


    Page<City> findAllByNameContains(String name, Pageable pageable);

    Optional<City> findByNameAndState_Acronym(String name, String acronym);
}
