package br.com.homedical.facade.dto;


import br.com.homedical.domain.enumeration.SignaturesType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SignatureDTO implements Serializable {

    private String id;

    @Valid
    private FileDTO image;

    @NotNull
    private SignaturesType type;

}
