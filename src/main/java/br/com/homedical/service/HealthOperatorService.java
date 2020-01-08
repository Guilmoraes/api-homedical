package br.com.homedical.service;

import br.com.homedical.domain.HealthOperator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HealthOperatorService {

    HealthOperator save(HealthOperator healthOperator);

    HealthOperator update(HealthOperator healthOperator);

    Page<HealthOperator> findAll(String query, Pageable pageable);

    HealthOperator findOne(String id);

    void delete(String id);
}
