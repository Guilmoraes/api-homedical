package br.com.homedical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface JPABaseRepository<B extends Serializable, T> extends JpaRepository<T, B>, QueryDslPredicateExecutor<T>, JpaSpecificationExecutor<T> {

}
