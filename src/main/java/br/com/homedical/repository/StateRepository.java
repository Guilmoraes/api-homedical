package br.com.homedical.repository;

import br.com.homedical.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface StateRepository extends JpaRepository<State, String>, JpaSpecificationExecutor<State> {

}
