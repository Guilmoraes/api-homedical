package br.com.homedical.facade.dto;


import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.facade.dto.patient.PatientSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class MobileScheduleDTO implements Serializable {

    private String id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime start;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private ZonedDateTime finish;

    @NotNull
    private SchedulesStatus status;

    @Valid
    @NotNull
    private ProfessionalSimpleDTO professional;

    @Valid
    @NotNull
    private PatientSimpleDTO patient;

    @Valid
    @NotNull
    private DutySimpleDTO duty;

    private Set<SignatureDTO> signatures = new HashSet<>();

    private Set<FileDTO> scheduleImages = new HashSet<>();

}
