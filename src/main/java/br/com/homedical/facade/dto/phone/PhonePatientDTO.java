package br.com.homedical.facade.dto.phone;

import br.com.homedical.domain.enumeration.PhoneType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PhonePatientDTO {

    private String id;

    private Integer areaCode;

    @NotBlank
    private String number;

    private PhoneType type;
}
