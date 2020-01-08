package br.com.homedical.facade.dto.document;


import br.com.homedical.domain.Professional;
import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.domain.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
public class DocumentDTO implements Serializable {

    private String id;

    private String url;

    private String fileName;

    private String s3Name;

    @Lob
    private byte[] file;

    private String fileContentType;

    private Boolean processed;

    private DocumentType type;

    private DocumentStatus status;

    private Professional professional;

    private Instant lastModifiedDate;

}
