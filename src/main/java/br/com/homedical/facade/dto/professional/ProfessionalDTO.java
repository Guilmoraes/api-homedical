package br.com.homedical.facade.dto.professional;


import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.specialty.SpecialtyDTO;
import br.com.homedical.service.dto.AddressDTO;
import br.com.homedical.service.dto.PhoneDTO;
import br.com.homedical.service.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ProfessionalDTO implements Serializable {

    private String id;

    @NotNull
    private String name;

    @NotNull
    @Valid
    private UserDTO user;

    @NotNull
    @Valid
    private List<SpecialtyDTO> specialties;

    @NotBlank
    private String cpf;

    @NotNull
    @Valid
    private AddressDTO address;

    @NotNull
    @Valid
    private PhoneDTO phone;

    private List<DutyDTO> duties;
}
