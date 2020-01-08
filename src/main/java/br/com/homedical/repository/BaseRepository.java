package br.com.homedical.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by rene on 10/16/17.
 */
@SuppressWarnings("unchecked")
@NoRepositoryBean
public abstract class BaseRepository<ID extends Serializable, T> implements Serializable, JPABaseRepository<ID, T> {

    @PersistenceContext
    private EntityManager entityManager;

    private static final long serialVersionUID = -7951474930842466983L;

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    protected SimpleJpaRepository<T, ID> jpaRepository;

    protected EntityManager manager;

    protected EntityPath<T> path;

    protected PathBuilder<T> builder;

    protected Querydsl querydsl;

    @PostConstruct
    public void init() {
        configure(this.entityManager);
    }

    public BaseRepository() {
    }

    protected void configure(EntityManager em) {
        final ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>) (type).getActualTypeArguments()[1];
        this.manager = em;
        this.path = DEFAULT_ENTITY_PATH_RESOLVER.createPath(entityClass);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.jpaRepository = new SimpleJpaRepository<>(entityClass, em);
    }

    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    public T findOne(Predicate predicate) {
        return (T) createQuery(predicate).fetchOne();
    }

    @Override
    public List<T> findAll(Predicate predicate) {
        return createQuery(predicate).fetch();
    }

    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        return (List<T>) querydsl.createQuery(path).where(predicate).orderBy(orders).fetch();
    }

    public List<T> findAllEager(Predicate predicate, OrderSpecifier<?>... orders) {
        return (List<T>) querydsl.createQuery(path).where(predicate).orderBy(orders).fetchAll().fetch();
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {

        JPQLQuery countQuery = createQuery(predicate);
        Long total = countQuery.fetchCount();

        JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));
        List<T> content = total > pageable.getOffset() ? query.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<T> findAll(JPQLQuery query, Pageable pageable) {

        JPQLQuery pagedQuery = querydsl.applyPagination(pageable, query);
        long total = query.fetchCount();

        List<T> content = total > pageable.getOffset() ? pagedQuery.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<T> findAll(JPAQuery<T> query, Pageable pageable) {

        JPQLQuery pagedQuery = querydsl.applyPagination(pageable, query);
        long total = query.fetchCount();

        List<T> content = total > pageable.getOffset() ? pagedQuery.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public long count(Predicate predicate) {
        return createQuery(predicate).fetchCount();
    }

    protected JPQLQuery createQuery(Predicate... predicate) {
        return querydsl.createQuery(path).where(predicate);
    }

    @Override
    public List<T> findAll() {
        return this.jpaRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return this.jpaRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return this.jpaRepository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return this.jpaRepository.findAll(ids);
    }

    @Override
    public long count() {
        return this.jpaRepository.count();
    }

    @Override
    public void delete(ID id) {
        this.jpaRepository.delete(id);
    }

    @Override
    public void delete(T entity) {
        this.jpaRepository.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        this.jpaRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        this.jpaRepository.deleteAll();
    }

    @Override
    public <S extends T> S save(S entity) {
        return this.jpaRepository.save(entity);
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return this.jpaRepository.save(entities);
    }

    @Override
    public T findOne(ID id) {
        return this.jpaRepository.findOne(id);
    }

    @Override
    public T getOne(ID id) {
        return this.jpaRepository.findOne(id);
    }

    @Override
    public boolean exists(ID id) {
        return this.jpaRepository.exists(id);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return this.jpaRepository.saveAndFlush(entity);
    }

    @Override
    public void flush() {
        this.jpaRepository.flush();
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        this.jpaRepository.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch() {
        this.jpaRepository.deleteAllInBatch();
    }

    @Override
    public T findOne(Specification<T> spec) {
        return this.jpaRepository.findOne(spec);
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return this.jpaRepository.findAll(spec);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return this.jpaRepository.findAll(spec, pageable);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return this.jpaRepository.findAll(spec, sort);
    }

    @Override
    public long count(Specification<T> spec) {
        return this.jpaRepository.count(spec);
    }


    @Override
    public Iterable<T> findAll(Predicate predicate, Sort sort) {
        return (Iterable<T>) querydsl.createQuery(path).where(predicate);
    }

    @Override
    public boolean exists(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate).fetchCount() > 0;
    }

    @Override
    public <S extends T> S findOne(Example<S> example) {
        return jpaRepository.findOne(example);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return jpaRepository.findAll(example, pageable);
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return jpaRepository.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return jpaRepository.exists(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return jpaRepository.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return jpaRepository.findAll(example, sort);
    }

    @Override
    public Iterable<T> findAll(OrderSpecifier<?>[] orders) {
        return querydsl.createQuery(path).orderBy(orders).createQuery().getResultList();
    }

    protected PageableQueryBuilder<T> createPaginationQuery() {
        return new PageableQueryBuilder<>(querydsl, entityManager);
    }

    protected Page<T> pagingQuery(@Nonnull BiConsumer<JPAQuery<T>, BooleanBuilder> queryFunction, Pageable pageable, @Nullable Path<?>... sortParameters) {

        Sort sort = pageable.getSort();

        if (sort == null && Objects.nonNull(sortParameters) && ArrayUtils.isNotEmpty(sortParameters)) {
            sort = new Sort(Sort.Direction.DESC, Arrays.stream(sortParameters).map(it -> it.getMetadata().getName()).collect(Collectors.toList()));
        }

        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);

        JPAQuery<T> query = new JPAQuery<>(entityManager);
        BooleanBuilder where = new BooleanBuilder();

        queryFunction.accept(query, where);

        query.where(where);

        Long total = query.fetchCount();

        JPQLQuery jpqlQuery = querydsl.applyPagination(pageRequest, query);
        List<T> content = total > pageRequest.getOffset() ? jpqlQuery.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageRequest, total);
    }

}
