package br.com.homedical.facade.dto.healthOperator;


import java.io.Serializable;
import java.util.Objects;

public class HealthOperatorSimpleDTO implements Serializable {

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

        HealthOperatorSimpleDTO healthOperatorSimpleDTO = (HealthOperatorSimpleDTO) o;
        if (healthOperatorSimpleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), healthOperatorSimpleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HealthOperatorSimpleDTO{" +
            "id=" + getId() +
            "}";
    }
}
