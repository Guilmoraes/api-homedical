package br.com.homedical.repository;

import br.com.homedical.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, String>, JpaSpecificationExecutor<Notification> {

}
