package br.com.homedical.repository;

import br.com.homedical.domain.Professional;
import br.com.homedical.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@SuppressWarnings("unused")
@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, String>, JpaSpecificationExecutor<Professional> {

    Optional<Professional> findById(String id);

    Optional<Professional> findByUser_Login(String login);

    Optional<Professional> findByUser(User user);

    Page<Professional> findAllByNameContains(Pageable pageable, String name);

}
