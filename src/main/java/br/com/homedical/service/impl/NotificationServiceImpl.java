package br.com.homedical.service.impl;

import br.com.homedical.config.Messages;
import br.com.homedical.domain.Installation;
import br.com.homedical.domain.Notification;
import br.com.homedical.domain.User;
import br.com.homedical.domain.push.PushMessage;
import br.com.homedical.domain.push.PushType;
import br.com.homedical.repository.NotificationRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.service.MailService;
import br.com.homedical.service.NotificationService;
import br.com.homedical.service.push.PushService;
import br.com.homedical.service.push.sender.PushNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;


@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final MailService mailService;

    private final UserRepository userRepository;

    private final PushService pushService;

    private final Messages messages;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   MailService mailService,
                                   UserRepository userRepository,
                                   PushService pushService,
                                   Messages messages) {
        this.notificationRepository = notificationRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.pushService = pushService;
        this.messages = messages;
    }


    @Override
    public Notification save(Notification notification) {
        log.debug("Request to save Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification) {
        log.debug("Request to update Notification : {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable);
    }


    @Override
    public Notification findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findOne(id);
    }


    @Override
    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.delete(id);
    }

    @Override
    public void sendEmailAccountCreated(String email) {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        oneByEmail.ifPresent(mailService::sendActivationEmail);
    }

    @Override
    public void sendPasswordResetMail(String userId) {
        userRepository.findById(userId).ifPresent(mailService::sendPasswordResetMail);
    }

    @Override
    public void sendProfessionalRegistrationEmail(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(mailService::sendProfessionalRegistrationEmail);
    }

    @Override
    public void sendProfessionalRegistrationByAdminEmail(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(mailService::sendProfessionalRegistrationByAdminEmail);
    }

    @Override
    public void sendChangeStatusDocumentEmail(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(mailService::sendChangeStatusDocumentEmail);
    }

    @Override
    @Async
    public void confirm(Installation installation) {
        PushMessage message = new PushMessage();

        Locale locale = Locale.forLanguageTag(installation.getUser().getLangKey());

        String title = messages.get("installation.success.title", locale);
        String subject = messages.get("installation.success.message", locale);

        message.setType(PushType.SAMPLE_PUSH);
        Map<String, Object> payload = new HashMap<>();
        payload.put(PushType.Extras.PAYLOAD, installation.getUser());
        message.setPayload(payload);
        message.setTimeToLive(600);
        PushNotification.Builder builder = new PushNotification.Builder(null);
        builder.title(title);
        builder.body(subject);
        message.setNotification(builder.build());

        List<Installation> tokens = new ArrayList<>();
        tokens.add(installation);
        pushService.sendMessageForProviders(tokens, message);

    }
}
