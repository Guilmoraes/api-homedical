package br.com.homedical.facade.dto.documentType;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class DocumentTypeDTO implements Serializable {


    private String id;

    private String name;

    private Boolean required;

}
