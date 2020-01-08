package br.com.homedical.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.Querydsl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class PageableQueryBuilder<T> {

    private final Querydsl querydsl;

    private final JPAQuery<T> query;

    private final BooleanBuilder where;

    private Pageable pageable;

    private StringPath[] sort;

    public PageableQueryBuilder(Querydsl querydsl, EntityManager entityManager) {
        this.querydsl = querydsl;
        this.query = new JPAQuery<>(entityManager);
        this.where = new BooleanBuilder();
    }

    public PageableQueryBuilder<T> withQuery(@Nonnull BiConsumer<JPAQuery<T>, BooleanBuilder> queryFunction) {
        queryFunction.accept(query, where);
        return this;
    }

    public PageableQueryBuilder<T> withPageable(@Nonnull Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public PageableQueryBuilder<T> withSort(@Nullable StringPath... sort) {
        this.sort = sort;
        return this;
    }

    public Page<T> execute() {

        Sort sort = pageable.getSort();

        if (sort == null && Objects.nonNull(this.sort) && ArrayUtils.isNotEmpty(this.sort)) {
            sort = new Sort(Sort.Direction.DESC, Arrays.stream(this.sort).map(it -> it.getMetadata().getName()).collect(Collectors.toList()));
        }

        PageRequest pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);

        JPAQuery<T> query = this.query.where(this.where);

        Long total = query.fetchCount();

        JPQLQuery jpqlQuery = querydsl.applyPagination(this.pageable, query);
        List<T> content = total > pageRequest.getOffset() ? jpqlQuery.fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageRequest, total);
    }
}
