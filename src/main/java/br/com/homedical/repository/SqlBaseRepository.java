package br.com.homedical.repository;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public abstract class SqlBaseRepository {

    protected SQLQueryFactory sqlQueryFactory;

    public SqlBaseRepository(SQLQueryFactory sqlQueryFactory) {
        this.sqlQueryFactory = sqlQueryFactory;
    }

    @SuppressWarnings("unchecked")
    protected <T> SimplePath<T> createPathForField(Path<T> path) {
        return Expressions.path((Class<? extends T>) path.getMetadata().getPathType().getType(), CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, path.getMetadata().getName()));
    }
}
