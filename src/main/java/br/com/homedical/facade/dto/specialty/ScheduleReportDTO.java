package br.com.homedical.facade.dto.specialty;


import br.com.homedical.domain.enumeration.SchedulesStatus;
import br.com.homedical.facade.dto.DutyDTO;
import br.com.homedical.facade.dto.DutySimpleDTO;
import br.com.homedical.facade.dto.patient.PatientSimpleDTO;
import br.com.homedical.facade.dto.professional.ProfessionalSimpleDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ScheduleReportDTO implements Serializable {

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
    private DutyDTO duty;

}
