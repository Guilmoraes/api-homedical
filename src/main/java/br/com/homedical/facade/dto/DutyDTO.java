package br.com.homedical.facade.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;


@Data
@NoArgsConstructor
public class DutyDTO implements Serializable {

    private String id;

    @NotBlank
    private String name;

    @NotNull
    private LocalTime start;

    @NotNull
    private LocalTime finish;

    private LocalTime overtime;

    private Double price;

}
