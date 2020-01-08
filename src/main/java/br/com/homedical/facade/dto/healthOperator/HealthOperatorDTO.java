package br.com.homedical.facade.dto.healthOperator;


import br.com.homedical.domain.HealthOperatorModality;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class HealthOperatorDTO implements Serializable {

    @NotBlank
    private String id;

    @NotNull
    private Integer ansRegister;

    @NotNull
    private String socialReason;

    @NotNull
    private Boolean obs;

    @NotNull
    private HealthOperatorModality modality;
}
