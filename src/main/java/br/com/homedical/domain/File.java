package br.com.homedical.domain;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class File extends AbstractAuditingEntity implements Serializable, DocumentS3 {

    @Id
    @GeneratedValue(generator = "uuid-custom")
    @GenericGenerator(name = "uuid-custom", strategy = "br.com.homedical.config.UuidCustomGenerator")
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

}

