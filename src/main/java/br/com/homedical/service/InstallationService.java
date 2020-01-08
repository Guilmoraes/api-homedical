package br.com.homedical.service;

import br.com.homedical.domain.Installation;
import br.com.homedical.domain.User;
import br.com.homedical.repository.InstallationQueryRepository;
import br.com.homedical.repository.InstallationRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class InstallationService {

    private final Logger log = LoggerFactory.getLogger(InstallationService.class);

    @Autowired
    private InstallationRepository repository;

    @Autowired
    private InstallationQueryRepository queryRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Installation save(Installation installation) {
        log.debug("Request to save Installation : {}", installation);
        String currentUser = SecurityUtils.getCurrentUserLogin();

        if (currentUser == null) {
            throw new IllegalArgumentException("Not logged to register the token");
        }

        User user = userRepository.findOneByLogin(currentUser).orElseThrow(() -> new IllegalArgumentException("Installation for user must not be null"));
        installation.setUser(user);

        List<Installation> byDeviceTokenAndDestination = repository.findByDeviceToken(installation.getDeviceToken());
        if (!byDeviceTokenAndDestination.isEmpty()) {
            byDeviceTokenAndDestination.forEach(it -> it.setUser(user));
            repository.save(byDeviceTokenAndDestination);
            notificationService.confirm(byDeviceTokenAndDestination.get(0));
            return byDeviceTokenAndDestination.get(0);
        } else {
            repository.save(installation);
            notificationService.confirm(installation);
            return installation;
        }
    }


    @Transactional(readOnly = true)
    public Page<Installation> findAll(Pageable pageable) {
        log.debug("Request to get all Installations");
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Installation findOne(String id) {
        log.debug("Request to get Installation : {}", id);
        return queryRepository.findOne(id);
    }

    public void delete(String id) {
        log.debug("Request to inactivate Installation : {}", id);
        repository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<Installation> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Installations for queryCollectorAndOptionalEmployee {}", query);
        return repository.findByAliasContaining(query, pageable);
    }

    public void removeInstallationsForDeviceTokens(Set<String> inactiveTokens) {
        log.info("Removing from database the following Tokens: {}", inactiveTokens);
        List<Installation> expired = repository.findByDeviceTokenIn(inactiveTokens);
        if (expired != null && !expired.isEmpty()) {
            repository.deleteInBatch(expired);
        }
    }

    public void updateInstallationsForCanonicalIDs(Map<String, String> changedTokens) {

        log.info("Update tokens for new canonical tokens: {}", changedTokens);
        for (Map.Entry<String, String> entry : changedTokens.entrySet()) {
            repository.updateTokenForCanonicalID(entry.getKey(), entry.getValue());
        }
    }

    public void removeInstallationsForDeviceToken(String invalidToken) {
        List<Installation> expired = repository.findByDeviceToken(invalidToken);
        if (expired != null && !expired.isEmpty()) {
            log.info("Removing from database the following Token: {}", invalidToken);
            repository.delete(expired);
        }
    }

    public List<Installation> findByUserId(Long id) {
        return repository.findByUserId(id);
    }

}
