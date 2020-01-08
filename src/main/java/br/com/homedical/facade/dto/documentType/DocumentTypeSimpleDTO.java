package br.com.homedical.facade.dto.documentType;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class DocumentTypeSimpleDTO implements Serializable {

    @NotBlank
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentTypeSimpleDTO documentTypeSimpleDTO = (DocumentTypeSimpleDTO) o;
        if(documentTypeSimpleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentTypeSimpleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentTypeSimpleDTO{" +
            "id=" + getId() +
            "}";
    }
}
