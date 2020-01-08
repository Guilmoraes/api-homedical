package br.com.homedical.repository;

import br.com.homedical.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, String>, JpaSpecificationExecutor<Address> {

    List<Address> findByLatIsNullAndLngIsNullAndIsValidAddressIsNull();


}
