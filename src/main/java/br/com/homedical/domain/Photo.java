package br.com.homedical.domain;

import br.com.homedical.util.ObjectId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "photo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@ToString(exclude = "file")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class Photo extends AbstractAuditingEntity implements Serializable, ImageS3 {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "uuid")
    @JsonIgnore
    private String uuid;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "medium")
    private String medium;

    @Column(name = "original")
    private String original;

    @Column(name = "thumbnail_name")
    @JsonIgnore
    private String thumbnailName;

    @Column(name = "medium_name")
    @JsonIgnore
    private String mediumName;

    @Column(name = "original_name")
    @JsonIgnore
    private String originalName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Lob
    @Column(name = "file")
    private byte[] file;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "file_content_type")
    private String fileContentType;

    private String fileName;

    @Column(name = "processed")
    @JsonIgnore
    @Builder.Default
    private Boolean processed = Boolean.FALSE;

    @PostLoad
    public void validate() {
        if (processed == null) {
            processed = Boolean.TRUE;
        }
    }

    @PrePersist
    @PreUpdate
    private void fileNameValid() {
        if (StringUtils.isNotBlank(fileName)) {
            fileName = fileName.replaceAll("[ ]", "_");
        }
    }

    public void generateUuid() {
        this.uuid = ObjectId.get().toString();
        this.thumbnailName = ObjectId.get().toString();
        this.mediumName = ObjectId.get().toString();
        this.originalName = ObjectId.get().toString();
    }
}
