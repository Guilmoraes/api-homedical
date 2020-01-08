package br.com.homedical.facade.dto;


import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.facade.dto.patient.PatientSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class ScheduleDTO implements Serializable {

    private String id;

    @NotNull
    private ZonedDateTime start;

    @NotNull
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

    @Valid
    private Set<SignatureDTO> signatures;

    @Valid
    private Set<FileDTO> scheduleImages;

}
