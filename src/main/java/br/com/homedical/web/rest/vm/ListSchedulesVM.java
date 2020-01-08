package br.com.homedical.web.rest.vm;

import br.com.homedical.facade.dto.ScheduleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ListSchedulesVM implements Serializable {

    @Valid
    @NotNull
    List<ScheduleDTO> schedules;

}
