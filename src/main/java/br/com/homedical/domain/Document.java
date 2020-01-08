package br.com.homedical.domain;

import br.com.homedical.domain.enumeration.DocumentStatus;
import br.com.homedical.domain.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Document extends AbstractAuditingEntity implements Serializable, DocumentS3 {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "url")
    private String url;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "s3_name")
    private String s3Name;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "processed")
    private Boolean processed;

    @ManyToOne(optional = false)
    private DocumentType type;

    @ManyToOne(optional = false)
    private Professional professional;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentStatus status;


}

