package br.com.homedical.service;

import br.com.homedical.domain.Duty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DutyService {

    Duty save(Duty duty);

    Duty update(Duty duty);

    Page<Duty> findAll(Pageable pageable);

    Duty findOne(String id);

    void delete(String id);

    List<Duty> listDuties();
}
