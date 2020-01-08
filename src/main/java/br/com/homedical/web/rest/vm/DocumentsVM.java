package br.com.homedical.web.rest.vm;

import br.com.homedical.facade.dto.document.DocumentUploadDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class DocumentsVM {

    @NotNull
    @Valid
    private List<DocumentUploadDTO> documents;
}
