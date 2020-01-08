package br.com.homedical.facade.dto.professional;

import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyProfessionalDTO;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.dto.PhoneDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class ProfessionalUpdateDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Valid
    private List<SpecialtyProfessionalDTO> specialties;

    @Valid
    @NotNull
    private PhoneDTO phone;

    @NotNull
    @Valid
    private AddressDTO address;

    @NotBlank
    private String cpf;

    private List<DutyDTO> duties;

}
