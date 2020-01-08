package br.com.homedical.repository;

import br.com.homedical.domain.Installation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public interface InstallationRepository extends JpaRepository<Installation, String> {

    List<Installation> findByDeviceTokenIn(Set<String> tokens);

    List<Installation> findByDeviceToken(String inactiveToken);

    @Modifying(clearAutomatically = true)
    @Query("update Installation set deviceToken = :newToken where deviceToken = :oldToken")
    void updateTokenForCanonicalID(@Param("oldToken") String oldToken, @Param("newToken") String newToken);

    List<Installation> findByUserId(Long id);

    List<Installation> findByUserLogin(String login);

    Page<Installation> findByAliasContaining(String query, Pageable pageable);

}
