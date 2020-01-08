package br.com.homedical.service;

import br.com.homedical.config.Constants;
import br.com.homedical.domain.Authority;
import br.com.homedical.domain.Professional;
import br.com.homedical.domain.User;
import br.com.homedical.repository.AuthorityRepository;
import br.com.homedical.repository.UserRepository;
import br.com.homedical.security.AuthoritiesConstants;
import br.com.homedical.security.SecurityUtils;
import br.com.homedical.service.dto.UserDTO;
import br.com.homedical.service.exceptions.BusinessException;
import br.com.homedical.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.homedical.config.Constants.PTBR_LANGKEY;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_CHECK_IF_PROFESSIONAL_HAD_ACCEPTED_TERMS_USER_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_PROFESSIONAL_ACCEPT_TERM_USER_NOT_FOUND;
import static br.com.homedical.service.exceptions.ErrorConstants.ERROR_RESET_PASSWORD_EMAIL_NOT_FOUND;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@SuppressWarnings("squid:S1192")
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
            });
    }

    public User requestPasswordReset(String mail) {
        User user = userRepository.findOneByEmailAndActivated(mail, true).orElseThrow(() -> new BusinessException(ERROR_RESET_PASSWORD_EMAIL_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.setResetDate(Instant.now());
        user.setResetKey(RandomUtil.generateResetKey());

        return user;
    }

    public User createUser(String login, String password, String firstName, String lastName, String email,
                           String imageUrl, String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setImageUrl(imageUrl);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey("pt-br"); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName  last name of user
     * @param email     email id of user
     * @param langKey   language key
     * @param imageUrl  image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    public User createProfessionalUser(Professional professional) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.PROFESSIONAL);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(professional.getUser().getPassword());
        newUser.setLogin(professional.getUser().getEmail());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(professional.getName());
        newUser.setLastName(professional.getName());
        newUser.setEmail(professional.getUser().getEmail());
        newUser.setImageUrl(null);
        newUser.setAcceptedTerm(false);
        newUser.setLangKey(PTBR_LANGKEY);
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User adminCreateProfessionalUser(Professional professional) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.PROFESSIONAL);
        Set<Authority> authorities = new HashSet<>();
        newUser.setLogin(professional.getUser().getEmail());
        newUser.setFirstName(professional.getName());
        newUser.setLastName(professional.getName());
        newUser.setEmail(professional.getUser().getEmail());
        newUser.setImageUrl(null);
        newUser.setLangKey(PTBR_LANGKEY);
        newUser.setActivated(false);
        newUser.setAcceptedTerm(false);
        newUser.setResetKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public Optional<User> completeDefinePassword(String newPassword, String key) {
        log.debug("Define user password for define key {}", key);

        return userRepository.findOneByResetKey(key)
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                user.setActivated(true);
                user.setActivationKey(null);
                return user;
            });
    }

    public Boolean checkIfProfessionalHadAcceptedTerms() {
        log.debug("Check if professional had accepted terms");

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElseThrow(() -> new BusinessException(ERROR_CHECK_IF_PROFESSIONAL_HAD_ACCEPTED_TERMS_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        return user.getAcceptedTerm();
    }

    public void acceptTerm() {
        log.debug("Accept Term");

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).orElseThrow(() -> new BusinessException(ERROR_PROFESSIONAL_ACCEPT_TERM_USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        user.setAcceptedTerm(true);

        userRepository.save(user);
    }
}
