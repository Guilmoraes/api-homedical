package br.com.homedical.repository;

import br.com.homedical.domain.Installation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface InstallationQueryRepository extends JpaRepository<Installation, String> {


}
