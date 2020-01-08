package br.com.homedical.repository.impl;

import br.com.homedical.domain.Installation;
import br.com.homedical.domain.QInstallation;
import br.com.homedical.domain.QUser;
import br.com.homedical.repository.BaseRepository;
import br.com.homedical.repository.InstallationQueryRepository;
import br.com.homedical.security.SecurityUtils;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class InstallationQueryRepositoryImpl extends BaseRepository<String, Installation> implements InstallationQueryRepository {

    private final EntityManager em;

    public InstallationQueryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Installation findOne(String id) {
        if (SecurityUtils.isProfessional()) {
            QInstallation installation = QInstallation.installation;

            QUser user = QUser.user;

            JPAQuery<Installation> query = new JPAQuery<>(em);
            query.leftJoin(installation.user, user)
                .where(user.login.eq(SecurityUtils.getCurrentUserLogin())
                    .and(installation.id.eq(id)));

            return query.fetchOne();
        } else {
            return super.findOne(id);
        }
    }
}
