package br.com.homedical.repository;

import br.com.homedical.domain.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface PhoneRepository extends JpaRepository<Phone, String>, JpaSpecificationExecutor<Phone> {

}
