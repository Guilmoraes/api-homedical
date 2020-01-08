package br.com.homedical.facade.dto.document;

import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.facade.dto.documentType.DocumentTypeSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DocumentUploadDTO {

    @Lob
    private byte[] file;

    @NotNull
    private DocumentStatus status;

    @NotBlank
    private String fileName;

    @NotBlank
    private String fileContentType;

    @NotNull
    @Valid
    private DocumentTypeSimpleDTO type;

    @NotNull
    @Valid
    private ProfessionalSimpleDTO professional;
}
