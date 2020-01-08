package br.com.homedical.service;

import br.com.homedical.domain.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SpecialtyService {

    Specialty save(Specialty specialty);

    Specialty update(Specialty specialty);

    Page<Specialty> findAll(Pageable pageable);

    Specialty findOne(String id);

    List<Specialty> getEnabledSpecialties();
}
