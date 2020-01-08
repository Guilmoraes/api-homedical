package br.com.homedical.facade.dto.patient;

import br.com.homedical.facade.dto.healthOperator.HealthOperatorDTO;
import br.com.homedical.facade.dto.adddress.AddressPatientDTO;
import br.com.homedical.facade.dto.phone.PhonePatientDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientCreateDTO {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthdate;

    @NotBlank
    private String clinicalCondition;

    @Valid
    @NotNull
    private HealthOperatorDTO healthOperator;

    @Valid
    @NotNull
    private PhonePatientDTO phone;

    @Valid
    @NotNull
    private AddressPatientDTO address;
}
