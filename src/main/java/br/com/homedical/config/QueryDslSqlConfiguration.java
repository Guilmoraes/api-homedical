package br.com.homedical.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by rene on 2/20/18.
 */
@Configuration
public class QueryDslSqlConfiguration {

    private final DataSource dataSource;

    public QueryDslSqlConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = MySQLTemplates.builder().build();
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        return configuration;
    }

    @Bean
    public SQLQueryFactory getSQLQueryFactory() {
        Provider<Connection> provider = new SpringConnectionProvider(dataSource);
        return new SQLQueryFactory(querydslConfiguration(), provider);
    }

}
