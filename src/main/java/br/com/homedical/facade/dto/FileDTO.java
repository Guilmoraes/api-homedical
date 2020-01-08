package br.com.homedical.facade.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class FileDTO implements Serializable {

    private String id;

    @NotBlank
    private String fileName;

    private byte[] file;

    @NotBlank
    private String fileContentType;

    private String url;
}
