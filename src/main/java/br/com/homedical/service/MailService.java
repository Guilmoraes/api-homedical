package br.com.homedical.service;

import br.com.homedical.domain.User;

public interface MailService {

    void sendActivationEmail(User user);

    void sendPasswordResetMail(User user);

    void sendCreationEmail(User newUser);

    void sendEmail(String s, String testSubject, String testContent, boolean b, boolean b1);

    void sendEmailFromTemplate(User user, String testEmail, String s);

    void sendProfessionalRegistrationEmail(User user);

    void sendProfessionalRegistrationByAdminEmail(User user);

    void sendChangeStatusDocumentEmail(User user);
}
