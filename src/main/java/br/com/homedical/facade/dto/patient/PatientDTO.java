package br.com.homedical.facade.dto.patient;


import br.com.homedical.domain.HealthOperator;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.dto.PhoneDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    private HealthOperator healthOperator;

    @NotNull
    private String clinicalCondition;

    private PhoneDTO phone;

    private AddressDTO address;

}
