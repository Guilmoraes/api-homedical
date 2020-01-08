package br.com.homedical.facade;

import br.com.homedical.domain.Notification;
import br.com.homedical.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class NotificationFacade {

    private final Logger log = LoggerFactory.getLogger(NotificationFacade.class);

    private final NotificationService notificationService;

    public NotificationFacade(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Transactional
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationService.save(notification);
    }

    @Transactional
    public Notification update(Notification notification) {
        log.debug("Request to update Notification : {}", notification);
        return notificationService.save(notification);
    }

    @Transactional(readOnly = true)
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationService.findAll(pageable);
    }


    @Transactional(readOnly = true)
    public Notification findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        return notificationService.findOne(id);
    }


    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        notificationService.delete(id);
    }
}
