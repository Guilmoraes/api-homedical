package br.com.homedical.facade.dto.documentType;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class DocumentTypeCreateDTO implements Serializable {
    
    private String id;

    @NotBlank
    private String name;

    @NotNull
    private Boolean required;

}
