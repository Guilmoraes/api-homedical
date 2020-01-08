package br.com.homedical.facade.dto.specialty;


import br.com.homedical.facade.dto.FileDTO;
import br.com.homedical.facade.dto.SignatureDTO;
import br.com.homedical.facade.dto.patient.PatientReportDTO;
import br.com.homedical.facade.dto.professional.ProfessionalReportDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class ScheduleCompleteReportDTO implements Serializable {

    private String id;

    @NotNull
    private ZonedDateTime start;

    @NotNull
    private ZonedDateTime finish;

    @Valid
    @NotNull
    private ProfessionalReportDTO professional;

    @Valid
    @NotNull
    private PatientReportDTO patient;

    @Valid
    private Set<SignatureDTO> signatures;

    @Valid
    private Set<FileDTO> scheduleImages;

}
