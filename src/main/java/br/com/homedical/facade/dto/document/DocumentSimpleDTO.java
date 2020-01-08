package br.com.homedical.facade.dto.document;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
public class DocumentSimpleDTO implements Serializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentSimpleDTO documentSimpleDTO = (DocumentSimpleDTO) o;
        if (documentSimpleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentSimpleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentSimpleDTO{" +
            "id=" + getId() +
            "}";
    }
}
